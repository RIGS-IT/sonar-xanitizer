/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2020 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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

import org.sonar.api.Plugin;

import com.rigsit.xanitizer.sqplugin.metrics.XanitizerMetrics;

/**
 * @author rust
 * 
 */

public class XanitizerSonarQubePlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(XanitizerRulesDefinition.class,

				XanitizerSensor.class,

				XanitizerMetrics.class);

		XanitizerProperties.define(context);
	}
}
