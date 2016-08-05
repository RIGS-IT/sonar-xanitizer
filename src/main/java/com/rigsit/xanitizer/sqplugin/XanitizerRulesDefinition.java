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

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;

/**
 * 
 * Defines the rules set of the Xanitizer plugin
 * 
 * @author nwe
 *
 */
public final class XanitizerRulesDefinition implements RulesDefinition {
	
	private static final String XANITIZER_TAG = "xanitizer";
	private static final String SECURITY_TAG = "security";

	@Override
	public void define(final Context context) {
		final String languageKey = getLanguageKey();
		final String repositoryKey = getRepositoryKey();
		final String repositoryName = "Xanitizer";

		final NewRepository repository = context.createRepository(repositoryKey, languageKey)
				.setName(repositoryName);

		for (final GeneratedProblemType problemType : GeneratedProblemType.values()) {
			final NewRule newRule = repository.createRule(problemType.name());
			newRule.setName(problemType.getPresentationName());
			newRule.setHtmlDescription(problemType.getDescription());
			newRule.setSeverity(Severity.MAJOR);
			newRule.setStatus(RuleStatus.READY);
			newRule.setTags(XANITIZER_TAG, SECURITY_TAG);
		}

		repository.done();
	}

	public static String getLanguageKey() {
		return Java.KEY;
	}

	public static String getRepositoryKey() {
		return "Xanitizer";
	}
}
