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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.java.Java;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;

/**
 * A profile for the Xanitizer rules.
 * 
 * @author rust
 *
 */
public class XanitizerQualityProfile extends ProfileDefinition {

	@Override
	public RulesProfile createProfile(final ValidationMessages validation) {
		final RulesProfile rulesProfile = RulesProfile.create("Xanitizer", Java.KEY);

		// Add Xanitizer rules.
		for (final GeneratedProblemType problemType : GeneratedProblemType.values()) {
			final Rule rule = Rule.create(XanitizerRulesDefinition.REPOSITORY_KEY, problemType.name());
			rulesProfile.activateRule(rule, null /* optionalSeverity */);
		}

		return rulesProfile;
	}

}
