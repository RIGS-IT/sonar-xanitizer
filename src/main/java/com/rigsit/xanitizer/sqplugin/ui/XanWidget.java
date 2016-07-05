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
package com.rigsit.xanitizer.sqplugin.ui;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.Description;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;
import org.sonar.api.web.WidgetCategory;

/**
 * @author nwe
 * 
 */
@UserRole(UserRole.USER)
@Description("The Xanitizer widget")
@WidgetCategory("Xanitizer")
public class XanWidget extends AbstractRubyTemplate implements RubyRailsWidget {

	@Override
	public String getId() {
		return "xanitizer";
	}

	@Override
	public String getTitle() {
		return "Xanitizer";
	}

	@Override
	protected String getTemplatePath() {
		return "/widgets/xan_widget.html.erb";
	}
}
