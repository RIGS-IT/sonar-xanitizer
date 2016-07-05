/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2016 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
 * mailto: info@rigs-it.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Created on October 2, 2015
 */
package com.rigsit.xanitizer.sqplugin;

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.java.Java;

import com.rigsit.xanitizer.sqplugin.batch.XMLReportFinding;

public final class XanRulesDefinition implements RulesDefinition {

	private static final String XANITIZER_TAG = "xanitizer";
	private static final String SECURITY_TAG = "security";
	private static final String FINDBUGS_TAG = "findbugs";
	private static final String OWASP_TAG = "owasp-dependency-check";

	public enum XanRule {

		TaintPath("Xanitizer Taint Path",
				"An identified taint path. The used location is the end of the path, i.e., the location where potentially manipulated data can cause harm.",
				RuleStatus.READY, Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG),

		SpecialCodeLocation("Xanitizer Special Code Location",
				"Some special location has been found in the code.", RuleStatus.READY,
				Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG),

		UserFinding("Xanitizer User-Specified Finding", "A user-specified finding",
				RuleStatus.READY, Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG),

		GenericFinding("Xanitizer Finding (other)", "Other kinds of Xanitizer findings",
				RuleStatus.READY, Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG),

		FindbugsFinding("Xanitizer Findbugs Finding",
				"Findbugs findings that are determined via Xanitizer", RuleStatus.READY,
				Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG, FINDBUGS_TAG),

		OWASPDependencyCheckFinding("Xanitizer OWASP Dependency Check Findings",
				"OWASP dependency check findings that are determined via Xanitizer",
				RuleStatus.READY, Severity.MAJOR,

				XANITIZER_TAG, SECURITY_TAG, OWASP_TAG),

		;

		private final String m_PresentationName;
		private final String m_ShortDescription;
		private final RuleStatus m_RuleStatus;
		private final String m_Severity;
		private final String[] m_Tags;

		/**
		 * @author rust
		 * 
		 */
		private XanRule(final String presentationName, final String shortDescription,
				final RuleStatus status, final String severity, final String... tags) {
			m_PresentationName = presentationName;
			m_ShortDescription = shortDescription;
			m_RuleStatus = status;
			m_Severity = severity;
			m_Tags = tags;

			for (final String tag : tags) {
				assert !containsAtLeastOneUpperCaseLetter(
						tag) : "tags may not contain capital letters";
			}
		}

		private static boolean containsAtLeastOneUpperCaseLetter(final String tag) {
			final int sz = tag.length();

			for (int i = 0; i < sz; ++i) {
				final char ch = tag.charAt(i);
				if (Character.isUpperCase(ch)) {
					return true;
				}
			}

			return false;
		}

		public String getPresentationName() {
			return m_PresentationName;
		}

		public RuleStatus getRuleStatus() {
			return m_RuleStatus;
		}

		public String getSeverity() {
			return m_Severity;
		}

		public String[] getTags() {
			return m_Tags;
		}

		public String getShortHTMLDescription() {
			return m_ShortDescription;
		}

		public static XanRule mkForFindingOrNull(final XMLReportFinding finding) {
			switch (finding.getFindingKind()) {
			case USER:
				return UserFinding;
			case GENERIC:
				if (finding.getFindingProducer().equals("PlugIn:Findbugs")) {
					return FindbugsFinding;
				}
				if (finding.getFindingProducer().equals("PlugIn:OWASPDependencyCheck")) {
					return OWASPDependencyCheckFinding;
				}
				return GenericFinding;
			case SPECIAL:
				return SpecialCodeLocation;
			case PATH:
				return TaintPath;
			default:
				return null;
			}
		}
	}

	public XanRulesDefinition() {
	}

	@Override
	public void define(final Context context) {
		final String languageKey = getLanguageKey();
		final String repositoryKey = getRepositoryKey();
		final String repositoryName = languageKey.toUpperCase() + " Xanitizer";

		final NewRepository repository = context.createRepository(repositoryKey, languageKey)
				.setName(repositoryName);

		for (final XanRule xanitizerRule : XanRule.values()) {
			final NewRule newRule = repository.createRule(xanitizerRule.name());
			newRule.setName(xanitizerRule.getPresentationName());
			newRule.setHtmlDescription(xanitizerRule.getShortHTMLDescription());
			newRule.setSeverity(xanitizerRule.getSeverity());
			newRule.setStatus(xanitizerRule.getRuleStatus());
			newRule.setTags(xanitizerRule.getTags());
		}

		repository.done();
	}

	public static String getLanguageKey() {
		return Java.KEY;
	}

	public static String getRepositoryKey() {
		return getLanguageKey() + "-Xanitizer";
	}
}
