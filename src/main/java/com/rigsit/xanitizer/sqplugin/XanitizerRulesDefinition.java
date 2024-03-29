/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2021 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
import com.rigsit.xanitizer.sqplugin.util.RepositoryConstants;

/**
 * 
 * Defines the rules set of the Xanitizer plugin
 * 
 * @author nwe
 *
 */
public final class XanitizerRulesDefinition implements RulesDefinition {

	private static final String XANITIZER_TAG = "xanitizer";
	private static final String SERVER_CONFIG_TAG = "server-configuration";
	private static final String SECURITY_TAG = "security";
	private static final String SPOTBUGS_TAG = "spotbugs";
	private static final String DEPENDENCY_CHECK_TAG = "owasp-dependency-check";
	private static final String CWE_TAG = "cwe";
	private static final String OWASP_TAG_PREFIX = "owasp-a";

	@Override
	public void define(final Context context) {

		final NewRepository javaRepository = context
				.createRepository(RepositoryConstants.REPOSITORY_KEY_JAVA,
						RepositoryConstants.LANGUAGE_KEY_JAVA)
				.setName(RepositoryConstants.REPOSITORY_KEY_JAVA);
		final NewRepository javaScriptRepository = context
				.createRepository(RepositoryConstants.REPOSITORY_KEY_JAVA_SCRIPT,
						RepositoryConstants.LANGUAGE_KEY_JAVA_SCRIPT)
				.setName(RepositoryConstants.REPOSITORY_KEY_JAVA_SCRIPT);
		final NewRepository typeScriptRepository = context
				.createRepository(RepositoryConstants.REPOSITORY_KEY_TYPE_SCRIPT,
						RepositoryConstants.LANGUAGE_KEY_TYPE_SCRIPT)
				.setName(RepositoryConstants.REPOSITORY_KEY_TYPE_SCRIPT);

		for (final GeneratedProblemType problemType : GeneratedProblemType.values()) {

			if (RepositoryConstants.LANGUAGE_KEY_JAVA_SCRIPT.equals(problemType.getLanguage())) {
				/*
				 * In Xanitizer, there is no separation between JavaScript and
				 * TypeScript problem types at the moment. So we have to
				 * duplicate the rules.
				 */
				createRuleForProblemType(javaScriptRepository, problemType);
				createRuleForProblemType(typeScriptRepository, problemType);
			} else {
				createRuleForProblemType(javaRepository, problemType);
			}

		}

		createOWASPDependencyCheckRules(javaRepository);
		createOWASPDependencyCheckRules(javaScriptRepository);
		createOWASPDependencyCheckRules(typeScriptRepository);

		final NewRule spotbugsRule = javaRepository.createRule(RepositoryConstants.SPOTBUGS_RULE);
		spotbugsRule.setName("Xanitizer SpotBugs Findings");
		spotbugsRule.setHtmlDescription(
				"SpotBugs findings that are determined via Xanitizer. Check SpotBugs documentation for details.");
		spotbugsRule.setSeverity(Severity.MAJOR);
		spotbugsRule.setStatus(RuleStatus.READY);
		spotbugsRule.setTags(XANITIZER_TAG, SECURITY_TAG, SPOTBUGS_TAG);

		javaRepository.done();
		javaScriptRepository.done();
		typeScriptRepository.done();
	}

	private void createRuleForProblemType(final NewRepository repository,
			final GeneratedProblemType problemType) {
		final NewRule newRule = repository.createRule(problemType.name());
		newRule.setName(problemType.getPresentationName());
		newRule.setHtmlDescription(problemType.getDescription());
		newRule.setSeverity(Severity.MAJOR);
		newRule.setStatus(RuleStatus.READY);
		newRule.setTags(XANITIZER_TAG, SECURITY_TAG);
		newRule.setType(RuleType.VULNERABILITY);

		if (problemType.getPresentationName().startsWith("Application Server:")) {
			newRule.addTags(SERVER_CONFIG_TAG);
		}

		final int owaspCategory = problemType.getOWASPTopTen();
		if (owaspCategory > 0) {
			newRule.addTags(OWASP_TAG_PREFIX + owaspCategory);
			newRule.addOwaspTop10(OwaspTop10.values()[owaspCategory - 1]);
		}

		final int cwe = problemType.getCWE();
		if (cwe > 0) {
			newRule.addTags(CWE_TAG);
			newRule.addCwe(cwe);

			final String sansCategory = getSansCategory(cwe);
			if (!sansCategory.isEmpty()) {
				newRule.addTags(sansCategory);
			}
		}
	}

	private void createOWASPDependencyCheckRules(final NewRepository repository) {
		final NewRule dependencyCheckRule = repository
				.createRule(RepositoryConstants.OWASP_DEPENDENCY_CHECK_RULE);
		dependencyCheckRule.setName("Xanitizer OWASP Dependency Check Findings");
		dependencyCheckRule.setHtmlDescription(
				"OWASP dependency check findings that are determined via Xanitizer.");
		dependencyCheckRule.setSeverity(Severity.MAJOR);
		dependencyCheckRule.setStatus(RuleStatus.READY);
		dependencyCheckRule.setTags(XANITIZER_TAG, SECURITY_TAG, DEPENDENCY_CHECK_TAG, CWE_TAG);
		dependencyCheckRule.addTags(OWASP_TAG_PREFIX + 9);
		dependencyCheckRule.addOwaspTop10(OwaspTop10.A9);
		dependencyCheckRule.addCwe(937);
	}

	private String getSansCategory(final int cwe) {
		switch (cwe) {
		case 78:
		case 79:
		case 89:
		case 352:
		case 434:
		case 601:
			return "sans-top25-insecure";
		case 22:
		case 120:
		case 131:
		case 134:
		case 190:
		case 494:
		case 676:
		case 829:
			return "sans-top25-risky";
		case 250:
		case 306:
		case 307:
		case 311:
		case 327:
		case 732:
		case 759:
		case 798:
		case 807:
		case 862:
		case 863:
			return "sans-top25-porous";
		default:
			return "";
		}
	}
}
