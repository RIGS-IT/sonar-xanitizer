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
 * Created on 25.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.DefaultInputProject;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.code.internal.DefaultSignificantCode;
import org.sonar.api.batch.sensor.coverage.internal.DefaultCoverage;
import org.sonar.api.batch.sensor.cpd.internal.DefaultCpdTokens;
import org.sonar.api.batch.sensor.error.AnalysisError;
import org.sonar.api.batch.sensor.highlighting.internal.DefaultHighlighting;
import org.sonar.api.batch.sensor.internal.SensorStorage;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.issue.internal.DefaultExternalIssue;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssueLocation;
import org.sonar.api.batch.sensor.measure.Measure;
import org.sonar.api.batch.sensor.measure.NewMeasure;
import org.sonar.api.batch.sensor.measure.internal.DefaultMeasure;
import org.sonar.api.batch.sensor.rule.internal.DefaultAdHocRule;
import org.sonar.api.batch.sensor.symbol.internal.DefaultSymbolTable;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scanner.fs.InputProject;

import com.rigsit.xanitizer.sqplugin.XanitizerProperties;
import com.rigsit.xanitizer.sqplugin.XanitizerSensor;
import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
import com.rigsit.xanitizer.sqplugin.util.RepositoryConstants;

/**
 * @author nwe
 *
 */
public class SensorTest {

	private final File resourcesDirectory = new File("src/test/resources");
	private final File webgoatDir = new File(resourcesDirectory, "webgoat");
	private SensorContext context;
	private MapSettings settings;
	private int createdIssues;

	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		context = mock(SensorContext.class);
		settings = new MapSettings();
		when(context.config()).thenReturn(settings.asConfig());

		final InputProject project = new DefaultInputProject(
				ProjectDefinition.create().setKey("projectKey").setName("projectName")
						.setBaseDir(webgoatDir).setWorkDir(new File(webgoatDir, ".sonar")));
		when(context.project()).thenReturn(project);

		final SensorStorage storage = new SensorStorage() {

			@Override
			public void storeProperty(String key, String value) {
			}

			@Override
			public void store(AnalysisError analysisError) {
			}

			@Override
			public void store(DefaultSymbolTable symbolTable) {
			}

			@Override
			public void store(DefaultCpdTokens defaultCpdTokens) {
			}

			@Override
			public void store(DefaultCoverage defaultCoverage) {
			}

			@Override
			public void store(DefaultHighlighting highlighting) {

			}

			@Override
			public void store(Issue issue) {

			}

			@SuppressWarnings("rawtypes")
			@Override
			public void store(Measure measure) {

			}

			@Override
			public void store(DefaultSignificantCode significantCode) {
			}

			@Override
			public void store(DefaultExternalIssue issue) {

			}

			@Override
			public void store(DefaultAdHocRule adHocRule) {
			}
		};

		when(context.newMeasure()).thenAnswer(new Answer<NewMeasure<Serializable>>() {

			@Override
			public NewMeasure<Serializable> answer(InvocationOnMock invocation) throws Throwable {
				return new DefaultMeasure<>(storage);
			}
		});

		createdIssues = 0;
		when(context.newIssue()).thenAnswer(new Answer<NewIssue>() {
			@Override
			public NewIssue answer(final InvocationOnMock invocation) throws Throwable {
				createdIssues++;

				final NewIssue newIssue = mock(NewIssue.class);
				when(newIssue.newLocation()).then(new Answer<NewIssueLocation>() {

					@Override
					public NewIssueLocation answer(InvocationOnMock invocation) throws Throwable {
						return new DefaultIssueLocation();
					}

				});
				return newIssue;
			}
		});

		final FileSystem fileSystem = new DefaultFileSystem(webgoatDir);
		when(context.fileSystem()).thenReturn(fileSystem);

		addFilesForDir(webgoatDir);
	}

	private void addFilesForDir(final File dir) {
		for (File child : dir.listFiles()) {
			if (child.isDirectory()) {
				addFilesForDir(child);
			} else {
				final String relativePath = child.getAbsolutePath()
						.replace(webgoatDir.getAbsolutePath() + File.separator, "");
				final DefaultInputFile inputFile = new TestInputFileBuilder("webgoat", relativePath)
						.initMetadata("a\nb\nc\nd\ne\nf\ng\nh\ni\n").build();

				((DefaultFileSystem) context.fileSystem()).add(inputFile);
			}
		}
	}

	@Test
	public void noActiveRules() throws Exception {

		final File reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml");
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE,
				reportFile.getAbsolutePath());
		final XanitizerSensor sensor = new XanitizerSensor(
				mock(ActiveRules.class), context);
		assertEquals(false, sensor.shouldExecute());
	}

	@Test
	public void missingReportFileAbsolute() throws Exception {
		final File reportFile = new File(resourcesDirectory, "webgoat/missing.xml");
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE,
				reportFile.getAbsolutePath());
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(false, sensor.shouldExecute());
	}

	@Test
	public void missingReportFileRelative() throws Exception {
		final String reportFile = "webgoat/missing.xml";
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(false, sensor.shouldExecute());
	}

	@Test
	public void existingReportFileAbsolute() throws Exception {
		final File reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml");
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE,
				reportFile.getAbsolutePath());
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(true, sensor.shouldExecute());
	}

	@Test
	public void existingReportFileRelative() throws Exception {
		final String reportFileName = "WEB-INF" + File.separator + "nested-Findings-List.xml";
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFileName);
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(true, sensor.shouldExecute());
	}

	@Test
	public void existingReportFileNested() throws Exception {
		final String reportFileName = "." + File.separator + "WEB-INF" + File.separator
				+ "nested-Findings-List.xml";
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFileName);
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(true, sensor.shouldExecute());
	}

	@Test
	public void existingReportFileOnlyName() throws Exception {

		final String reportFileName = "nested-Findings-List.xml";
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFileName);
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		assertEquals(true, sensor.shouldExecute());
	}

	@Test
	public void testAnalyzeWOPluginRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);

		sensor.execute(context);
		assertEquals(198, createdIssues);
	}

	@Test
	public void testAnalyzeJavaScriptWOPluginRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-javascript.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);

		sensor.execute(context);
		assertEquals(10, createdIssues);
	}
	
	@Test
	public void testAnalyzeJavaScriptOWASPRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-owasp-javascript.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		settings.setProperty(XanitizerProperties.XANITIZER_IMPORT_ALL_FINDINGS, true);
		
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, true), context);

		sensor.execute(context);
		assertEquals(1, createdIssues);
	}

	@Test
	public void testAnalyzeWithPluginRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(true, true), context);

		sensor.execute(context);
		assertEquals(198, createdIssues);
	}

	@Test
	public void testAnalyzeWithoutSpecifiedReportFile() {
		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(true, true), context);

		sensor.execute(context);
		assertEquals(528, createdIssues);
	}

	@Test
	public void testAnalyzeImportAllWOPluginRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		settings.setProperty(XanitizerProperties.XANITIZER_IMPORT_ALL_FINDINGS, true);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);

		sensor.execute(context);
		assertEquals(251, createdIssues);
	}

	@Test
	public void testAnalyzeImportAllWithPluginRules() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		settings.setProperty(XanitizerProperties.XANITIZER_IMPORT_ALL_FINDINGS, true);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(true, true), context);

		sensor.execute(context);
		assertEquals(418, createdIssues);
	}

	@Test
	public void testSingleActiveRule() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-all.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final ActiveRulesBuilder builder = new ActiveRulesBuilder();
		for (GeneratedProblemType problemType : GeneratedProblemType.values()) {
			if ("ci:SQLInjection".equals(problemType.getId())) {
				final RuleKey ruleKey = RuleKey.of(RepositoryConstants.REPOSITORY_KEY_JAVA,
						problemType.name());
				final NewActiveRule.Builder activeRuleBuilder = new NewActiveRule.Builder();
				activeRuleBuilder.setRuleKey(ruleKey);
				final NewActiveRule activeRule = activeRuleBuilder.build();
				builder.addRule(activeRule);
			}
		}

		final XanitizerSensor sensor = new XanitizerSensor(
				builder.build(), context);

		sensor.execute(context);
		assertEquals(23, createdIssues);
	}

	@Test
	public void testAnalyzeOldReportFile() {
		final String reportFile = new File(resourcesDirectory,
				"webgoat/webgoat-Findings-List-oldversion.xml").getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		sensor.execute(context);
		assertEquals(0, createdIssues);
	}

	@Test
	public void testReportFileWOAnalysis() {
		final String reportFile = new File(resourcesDirectory, "webgoat/empty-Findings-List.xml")
				.getAbsolutePath();
		settings.setProperty(XanitizerProperties.XANITIZER_XML_REPORT_FILE, reportFile);

		final XanitizerSensor sensor = new XanitizerSensor(
				getActiveRules(false, false), context);
		sensor.execute(context);
		assertEquals(0, createdIssues);
	}

	private ActiveRules getActiveRules(final boolean appendSpotBugs,
			final boolean appendDependencyCheck) {

		final ActiveRulesBuilder builder = new ActiveRulesBuilder();
		for (GeneratedProblemType problemType : GeneratedProblemType.values()) {
			if (RepositoryConstants.LANGUAGE_KEY_JAVA_SCRIPT
					.equals(problemType.getLanguage())) {
				addRule(builder, RepositoryConstants.REPOSITORY_KEY_JAVA_SCRIPT, problemType.name());
				addRule(builder, RepositoryConstants.REPOSITORY_KEY_TYPE_SCRIPT, problemType.name());
			} else {
				addRule(builder, RepositoryConstants.REPOSITORY_KEY_JAVA, problemType.name());
			}

			
		}

		if (appendSpotBugs) {
			final RuleKey ruleKey = RuleKey.of(RepositoryConstants.REPOSITORY_KEY_JAVA,
					RepositoryConstants.SPOTBUGS_RULE);
			final NewActiveRule.Builder activeRuleBuilder = new NewActiveRule.Builder();
			activeRuleBuilder.setRuleKey(ruleKey);
			final NewActiveRule activeRule = activeRuleBuilder.build();
			builder.addRule(activeRule);
		}

		if (appendDependencyCheck) {
			final NewActiveRule.Builder activeRuleBuilder = new NewActiveRule.Builder();

			final RuleKey ruleKeyJava = RuleKey.of(RepositoryConstants.REPOSITORY_KEY_JAVA,
					RepositoryConstants.OWASP_DEPENDENCY_CHECK_RULE);
			final RuleKey ruleKeyJavaScript = RuleKey.of(
					RepositoryConstants.REPOSITORY_KEY_JAVA_SCRIPT,
					RepositoryConstants.OWASP_DEPENDENCY_CHECK_RULE);

			activeRuleBuilder.setRuleKey(ruleKeyJava);
			builder.addRule(activeRuleBuilder.build());
			activeRuleBuilder.setRuleKey(ruleKeyJavaScript);
			builder.addRule(activeRuleBuilder.build());
		}

		return builder.build();
	}
	
	private void addRule(final ActiveRulesBuilder builder, final String repositoryKey, final String ruleName) {
		final RuleKey ruleKey = RuleKey.of(repositoryKey, ruleName);
		final NewActiveRule.Builder activeRuleBuilder = new NewActiveRule.Builder();
		activeRuleBuilder.setRuleKey(ruleKey);
		final NewActiveRule activeRule = activeRuleBuilder.build();
		builder.addRule(activeRule);
	}
}
