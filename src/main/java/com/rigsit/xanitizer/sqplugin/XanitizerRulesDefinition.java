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

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.java.Java;

public final class XanitizerRulesDefinition implements RulesDefinition {

	@Override
	public void define(final Context context) {
		final String languageKey = getLanguageKey();
		final String repositoryKey = getRepositoryKey();
		final String repositoryName = languageKey.toUpperCase() + " Xanitizer";

		final NewRepository repository = context.createRepository(repositoryKey, languageKey)
				.setName(repositoryName);

		for (final XanitizerRule xanitizerRule : XanitizerRule.values()) {
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
