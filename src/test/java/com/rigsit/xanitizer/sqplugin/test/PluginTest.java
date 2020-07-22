/** 
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2019 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.utils.Version;

import com.rigsit.xanitizer.sqplugin.XanitizerProperties;
import com.rigsit.xanitizer.sqplugin.XanitizerRulesDefinition;
import com.rigsit.xanitizer.sqplugin.XanitizerSensor;
import com.rigsit.xanitizer.sqplugin.XanitizerSonarQubePlugin;
import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
import com.rigsit.xanitizer.sqplugin.metrics.XanitizerMetrics;

/**
 * @author nwe
 *
 */
public class PluginTest {

	@Test
	@SuppressWarnings("rawtypes")
	public void testExtensions() {

		final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(7, 9),
				SonarQubeSide.SCANNER, SonarEdition.COMMUNITY);
		final Plugin.Context context = new Plugin.Context(runtime);
		new XanitizerSonarQubePlugin().define(context);

		final List extensions = context.getExtensions();
		assertNotNull(extensions);

		assertTrue(extensions.contains(XanitizerSensor.class));
		assertTrue(extensions.contains(XanitizerMetrics.class));
		assertTrue(extensions.contains(XanitizerRulesDefinition.class));
	}

	@Test
	public void testRules() {
		final RulesDefinition rulesDefinition = new XanitizerRulesDefinition();
		final RulesDefinition.Context context = new RulesDefinition.Context();
		rulesDefinition.define(context);

		final Repository repository = context.repository(XanitizerRulesDefinition.REPOSITORY_KEY);
		assertNotNull(XanitizerRulesDefinition.REPOSITORY_KEY, repository);
		assertEquals(XanitizerRulesDefinition.LANGUAGE_KEY, repository.language());

		for (GeneratedProblemType problemType : GeneratedProblemType.values()) {
			assertNotNull(repository.rule(problemType.name()));
		}
	}

	@Test
	public void testSettings() {
		final MapSettings settings = new MapSettings();
		final SensorContextTester sensorContext = SensorContextTester.create(new File(""));
		sensorContext.setSettings(settings);
		assertNull(XanitizerProperties.geReportFile(sensorContext));

		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, "");
		assertNull(XanitizerProperties.geReportFile(sensorContext));

		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, "/doesNotExist.xml");
		assertNull(XanitizerProperties.geReportFile(sensorContext));

		final String reportFileString = getClass()
				.getResource("/webgoat/webgoat-Findings-List-all.xml").getFile();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFileString);
		final File reportFile = XanitizerProperties.geReportFile(sensorContext);
		assertNotNull(reportFile);
		assertTrue(reportFile.isFile());
		assertEquals(new File(reportFileString), reportFile);
	}

	@Test
	public void testMetrics() {
		assertNotNull(XanitizerMetrics.getMetricForAllXanFindings());
		assertNotNull(XanitizerMetrics.getMetricForNewXanFindings());

		for (Severity severity : Severity.values()) {
			assertNotNull("No metric for severity " + severity,
					XanitizerMetrics.getMetricForSeverity(severity));
		}

		final XanitizerMetrics metrics = new XanitizerMetrics();

		final int allMetrics = metrics.getMetrics().size();

		assertEquals(allMetrics, GeneratedProblemType.values().length + Severity.values().length
				+ 4 /* all and new findings, SpotBugs, Dependency Check */);

	}
}
