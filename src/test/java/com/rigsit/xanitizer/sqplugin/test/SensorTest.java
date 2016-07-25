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
 * Created on 25.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.java.api.JavaResourceLocator;

import com.rigsit.xanitizer.sqplugin.XanitizerRule;
import com.rigsit.xanitizer.sqplugin.XanitizerRulesDefinition;
import com.rigsit.xanitizer.sqplugin.XanitizerSensor;
import com.rigsit.xanitizer.sqplugin.XanitizerSonarQubePlugin;

/**
 * @author nwe
 *
 */
public class SensorTest {

	@Test
	public void testShouldExecuteOnProject() {
		final Settings settings = new Settings();

		XanitizerSensor sensor = new XanitizerSensor(mock(JavaResourceLocator.class), settings,
				mock(ActiveRules.class));
		assertEquals(false, sensor.shouldExecuteOnProject(mock(Project.class)));

		final String reportFileString = getClass().getResource("/webgoat-Findings-List.xml")
				.getFile();
		settings.setProperty(XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE, reportFileString);
		sensor = new XanitizerSensor(mock(JavaResourceLocator.class), settings,
				mock(ActiveRules.class));
		assertEquals(false, sensor.shouldExecuteOnProject(mock(Project.class)));

		final ActiveRulesBuilder builder = new ActiveRulesBuilder();
		final RuleKey ruleKey = RuleKey.of(XanitizerRulesDefinition.getRepositoryKey(),
				XanitizerRule.TAINT_PATH.name());
		final NewActiveRule activeRule = builder.create(ruleKey);
		activeRule.activate();
		sensor = new XanitizerSensor(mock(JavaResourceLocator.class), settings, builder.build());
		assertEquals(true, sensor.shouldExecuteOnProject(mock(Project.class)));

	}

	@Test
	public void testAnalyze() {
		final Settings settings = new Settings();
		final String reportFileString = getClass().getResource("/webgoat-Findings-List.xml")
				.getFile();
		settings.setProperty(XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE, reportFileString);

		final ActiveRulesBuilder builder = new ActiveRulesBuilder();
		for (XanitizerRule rule : XanitizerRule.values()) {
			final RuleKey ruleKey = RuleKey.of(XanitizerRulesDefinition.getRepositoryKey(),
					rule.name());
			final NewActiveRule activeRule = builder.create(ruleKey);
			activeRule.activate();
		}

		final int[] createdIssues = { 0 };
		final SensorContext context = mock(SensorContext.class);
		when(context.newIssue()).thenAnswer(new Answer<NewIssue>() {
			@Override
			public NewIssue answer(final InvocationOnMock invocation) throws Throwable {
				createdIssues[0]++;

				final NewIssue newIssue = mock(NewIssue.class);
				;
				when(newIssue.newLocation()).thenReturn(new DefaultIssueLocation());
				return newIssue;
			}
		});

		final XanitizerSensor sensor = new XanitizerSensor(mock(JavaResourceLocator.class),
				settings, builder.build());

		// If the project is a root project, all issues should be created at
		// project level (because we have no source file information)
		final Project rootProject = mock(Project.class);
		when(rootProject.isRoot()).thenReturn(true);
		sensor.analyse(rootProject, context);
		assertEquals(912, createdIssues[0]);

		// reset counted issues
		createdIssues[0] = 0;

		// If the project is not a root project, no issues should be created
		final Project nonRootProject = mock(Project.class);
		when(nonRootProject.isRoot()).thenReturn(false);

		sensor.analyse(nonRootProject, context);
		assertEquals(0, createdIssues[0]);
	}
}
