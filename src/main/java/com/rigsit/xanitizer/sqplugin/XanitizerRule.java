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
 * Created on 07.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin;

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;

import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportFinding;

/**
 * @author nwe
 *
 */
public enum XanitizerRule {

	TAINT_PATH("Xanitizer Taint Path",
			"An identified taint path. The used location is the end of the path, i.e., the location where potentially manipulated data can cause harm.",
			RuleStatus.READY, Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG),

	SPECIAL_CODE_LOCATION("Xanitizer Special Code Location",
			"Some special location has been found in the code.", RuleStatus.READY, Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG),

	USER_FINDING("Xanitizer User-Specified Finding", "A user-specified finding", RuleStatus.READY,
			Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG),

	GENERIC_FINDING("Xanitizer Finding (other)", "Other kinds of Xanitizer findings",
			RuleStatus.READY, Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG),

	FINDBUGS_FINDING("Xanitizer Findbugs Finding",
			"Findbugs findings that are determined via Xanitizer", RuleStatus.READY, Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG, XanitizerRule.FINDBUGS_TAG),

	OWASP_DEPENDENCY_CHECK_FINDING("Xanitizer OWASP Dependency Check Findings",
			"OWASP dependency check findings that are determined via Xanitizer", RuleStatus.READY,
			Severity.MAJOR,

			XanitizerRule.XANITIZER_TAG, XanitizerRule.SECURITY_TAG, XanitizerRule.OWASP_TAG),

	;

	private static final String XANITIZER_TAG = "xanitizer";
	private static final String SECURITY_TAG = "security";
	private static final String FINDBUGS_TAG = "findbugs";
	private static final String OWASP_TAG = "owasp-dependency-check";

	private final String presentationName;
	private final String shortDescription;
	private final RuleStatus ruleStatus;
	private final String severity;
	private final String[] tags;

	/**
	 * @author rust
	 * 
	 */
	private XanitizerRule(final String presentationName, final String shortDescription,
			final RuleStatus status, final String severity, final String... tags) {
		this.presentationName = presentationName;
		this.shortDescription = shortDescription;
		this.ruleStatus = status;
		this.severity = severity;
		this.tags = tags;

		for (final String tag : tags) {
			final boolean containsUperCaseLetter = containsAtLeastOneUpperCaseLetter(tag);
			assert !containsUperCaseLetter : "tags may not contain capital letters";
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
		return presentationName;
	}

	public RuleStatus getRuleStatus() {
		return ruleStatus;
	}

	public String getSeverity() {
		return severity;
	}

	public String[] getTags() {
		return tags;
	}

	public String getShortHTMLDescription() {
		return shortDescription;
	}

	public static XanitizerRule mkForFindingOrNull(final XMLReportFinding finding) {
		switch (finding.getFindingKind()) {
		case USER:
			return USER_FINDING;
		case GENERIC:
			return mkGenericRule(finding);
		case SPECIAL:
			return SPECIAL_CODE_LOCATION;
		case PATH:
			return TAINT_PATH;
		default:
			return null;
		}
	}

	private static XanitizerRule mkGenericRule(final XMLReportFinding finding) {
		if ("PlugIn:Findbugs".equals(finding.getFindingProducer())) {
			return FINDBUGS_FINDING;
		}
		if ("PlugIn:OWASPDependencyCheck".equals(finding.getFindingProducer())) {
			return OWASP_DEPENDENCY_CHECK_FINDING;
		}
		return GENERIC_FINDING;
	}

}
