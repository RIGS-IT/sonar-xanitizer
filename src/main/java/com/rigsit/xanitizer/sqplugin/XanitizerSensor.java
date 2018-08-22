/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2018 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
 * Created on October 10, 2015
 */
package com.rigsit.xanitizer.sqplugin;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputModule;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputModule;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.measure.NewMeasure;
import org.sonar.api.measures.Metric;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.java.api.JavaResourceLocator;

import com.rigsit.xanitizer.sqplugin.metrics.XanitizerMetrics;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportContent;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportFinding;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportNode;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportParser;
import com.rigsit.xanitizer.sqplugin.util.SensorUtil;

/**
 * @author rust
 * 
 */
public class XanitizerSensor implements Sensor {
	private static final Logger LOG = Loggers.get(XanitizerSensor.class);

	private static final String SKIP_FINDING_MESSAGE = "Xanitizer: Skipping finding ";

	private final JavaResourceLocator javaResourceLocator;
	private final File reportFile;
	private final boolean importAllFindings;

	private final Set<String> activeXanRuleNames = new HashSet<>();

	private final Map<String, NewIssue> alreadyCreatedIssues = new HashMap<>();

	/**
	 * The Xanitizer sensor
	 * 
	 * @param javaResourceLocator
	 * @param settings
	 * @param activeRules
	 * @param sensorContext
	 */
	public XanitizerSensor(final JavaResourceLocator javaResourceLocator,
			final ActiveRules activeRules, final SensorContext sensorContext) {
		this.javaResourceLocator = javaResourceLocator;

		this.importAllFindings = SensorUtil.getImportAll(sensorContext);

		for (final ActiveRule activeRule : activeRules.findAll()) {
			if (activeRule.ruleKey().repository().equals(XanitizerRulesDefinition.REPOSITORY_KEY)) {
				final String ruleAsString = activeRule.ruleKey().rule();
				activeXanRuleNames.add(ruleAsString);
			}
		}
		if (activeXanRuleNames.isEmpty()) {
			/*
			 * If no rule is active, we do not need to read the report file
			 */
			this.reportFile = null;
		} else {
			this.reportFile = SensorUtil.geReportFile(sensorContext);
		}
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Xanitizer Sensor");
		descriptor.createIssuesForRuleRepository(XanitizerRulesDefinition.REPOSITORY_KEY);
	}

	@Override
	public void execute(SensorContext sensorContext) {
		if (!shouldExecute()) {
			return;
		}

		final DefaultInputModule project = (DefaultInputModule) sensorContext.module();
		LOG.info("Reading Xanitizer findings from '" + reportFile + "' for project '"
				+ project.getName() + "'");

		final XMLReportParser parser = new XMLReportParser();
		final XMLReportContent content;
		try {
			content = parser.parse(reportFile);
		} catch (final Exception ex) {
			LOG.error("Exception caught while parsing Xanitizer XML report file '" + reportFile
					+ "'.", ex);
			return;
		}

		final long analysisEndDate = content.getAnalysisEndDate();
		if (analysisEndDate == -1) {
			LOG.warn(
					"No Xanitizer analysis results found - Check if Xanitizer analysis has been executed!");
			return;
		}

		final String analysisDatePresentation = SensorUtil
				.convertToDateWithTimeString(new Date(analysisEndDate));
		LOG.info("Processing Xanitizer analysis results of " + analysisDatePresentation);
		LOG.info("Collected " + content.getXMLReportFindings().size() + " Xanitizer findings.");
		createIssuesAndMeasures(project, sensorContext, content);
		LOG.info("Created " + alreadyCreatedIssues.size() + " issues.");
	}

	public boolean shouldExecute() {
		return !activeXanRuleNames.isEmpty() && reportFile != null;
	}

	private boolean skipFinding(final XMLReportFinding finding) {

		if (finding.isSpotBugsFinding()) {
			if (!importAllFindings) {
				LOG.debug(SKIP_FINDING_MESSAGE + finding.getFindingID()
						+ ": Ignoring SpotBugs finding.");
				return true;
			}
			if (!activeXanRuleNames.contains(XanitizerRulesDefinition.SPOTBUGS_RULE)) {
				LOG.debug(SKIP_FINDING_MESSAGE + finding.getFindingID()
						+ ": Rule for Xanitizer findings detected by SpotBugs is disabled.");
				return true;
			}
			return false;
		}

		if (finding.isDependencyCheckFinding()) {
			if (!importAllFindings) {
				LOG.debug(SKIP_FINDING_MESSAGE + finding.getFindingID()
						+ ": Ignoring OWASP Dependency Check finding.");
				return true;
			}
			if (!activeXanRuleNames
					.contains(XanitizerRulesDefinition.OWASP_DEPENDENCY_CHECK_RULE)) {
				LOG.debug(SKIP_FINDING_MESSAGE + finding.getFindingID()
						+ ": Rule for Xanitizer findings detected by OWASP Dependency Check is disabled.");
				return true;
			}
			return false;
		}

		if (finding.getProblemTypeOrNull() == null) {
			LOG.warn(SKIP_FINDING_MESSAGE + finding.getFindingID() + ": Unknown problem type '"
					+ finding.getProblemTypeString() + "'.");
			return true;
		}

		if (!activeXanRuleNames.contains(finding.getProblemTypeOrNull().name())) {
			LOG.debug(SKIP_FINDING_MESSAGE + finding.getFindingID()
					+ ": Rule for corresponding problem type is disabled.");
			return true;
		}

		return false;
	}

	private void createIssuesAndMeasures(final DefaultInputModule project,
			final SensorContext sensorContext, final XMLReportContent content) {

		final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValues = new LinkedHashMap<>();

		// initialize metrics on project level with value 0
		initializeMetrics(metricValues, project);

		// Generate issues for findings.
		for (final XMLReportFinding f : content.getXMLReportFindings()) {
			try {
				generateIssueForFinding(f, metricValues, project, sensorContext);
			} catch (Exception e) {
				// if something is wrong for a single finding, continue with the
				// next one
				LOG.debug("Error generating issue for finding " + f.getFindingID(), e);
			}
		}

		for (final NewIssue issue : alreadyCreatedIssues.values()) {
			issue.save();
		}

		// Metrics: Counts of different findings.
		for (final Map.Entry<Metric<Serializable>, Map<InputComponent, Integer>> e : metricValues
				.entrySet()) {
			final Metric<Serializable> metric = e.getKey();
			for (final Map.Entry<InputComponent, Integer> e1 : e.getValue().entrySet()) {
				final InputComponent resource = e1.getKey();
				final Integer value = e1.getValue();
				if (resource == null) {
					continue;
				}

				// Only create metrics with 0 values on project level
				if (value != 0 || resource == project) {
					final NewMeasure<Serializable> measure = sensorContext.newMeasure();
					measure.forMetric(metric);
					measure.withValue(value);
					measure.on(resource);
					LOG.debug("Creating measure for metric " + metric.getKey() + ": adding value = "
							+ value + " to resource " + resource.toString());
					measure.save();
				}
			}
		}
	}

	private void generateIssueForFinding(final XMLReportFinding xanFinding,
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu,
			final DefaultInputModule project, final SensorContext sensorContext) {

		if (skipFinding(xanFinding)) {
			return;
		}

		final InputFile inputFile = mkInputFileOrNull(xanFinding.getLocation(), sensorContext);

		if (inputFile == null) {
			if (importAllFindings) {

			} else {
				/*
				 * Do not generate issues without code location
				 */
				LOG.debug(SKIP_FINDING_MESSAGE + xanFinding.getFindingID()
						+ ": Corresponding file could not be found in project.");

				return;
			}
		}

		final boolean issueCreated = createNewIssue(inputFile, xanFinding, sensorContext);

		if (issueCreated) {
			incrementMetrics(xanFinding, metricValuesAccu, project, inputFile);
		}
	}

	@SuppressWarnings("unchecked")
	private void initializeMetrics(
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu,
			final DefaultInputModule project) {

		for (Metric<Serializable> metric : new XanitizerMetrics().getMetrics()) {
			initializeMetric(metricValuesAccu, project, metric);
		}
	}

	private void initializeMetric(
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu,
			final InputComponent resource, final Metric<Serializable> metric) {
		Map<InputComponent, Integer> innerMap = new LinkedHashMap<>();
		innerMap.put(resource, 0);
		metricValuesAccu.put(metric, innerMap);
	}

	private void incrementMetrics(final XMLReportFinding xanFinding,
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu,
			final DefaultInputModule project, final InputFile inputFile) {
		final Severity severity = SensorUtil.mkSeverity(xanFinding);

		final List<Metric<Serializable>> metrics = mkMetrics(xanFinding);
		for (final Metric<Serializable> metric : metrics) {
			incrementValueForFileAndProject(metric, inputFile, project, metricValuesAccu);
		}

		final String matchCode = xanFinding.getMatchCode();
		if ("NOT".equals(matchCode)) {
			incrementValueForFileAndProject(XanitizerMetrics.getMetricForNewXanFindings(),
					inputFile, project, metricValuesAccu);
		}

		final Metric<Serializable> metricForSeverity = XanitizerMetrics
				.getMetricForSeverity(severity);
		if (metricForSeverity != null) {
			incrementValueForFileAndProject(metricForSeverity, inputFile, project,
					metricValuesAccu);
		}
	}

	/*
	 * In SonarQube, line numbers must be strictly positive.
	 */
	private static int normalizeLineNo(final int lineNo) {
		if (lineNo <= 0) {
			return 1;
		}
		return lineNo;
	}

	private InputFile mkInputFileOrNull(final XMLReportNode node,
			final SensorContext sensorContext) {

		if (node == null) {
			return null;
		}

		try {
			final InputFile result = mkInputFileOrNullFromClass(node);
			if (result == null) {
				return mkInputFileOrNullFromPath(node, sensorContext);
			}

			return result;
		} catch (Exception e) {
			LOG.debug("Error while detecting InputFile for node with " + node, e);
		}
		return null;
	}

	private InputFile mkInputFileOrNullFromClass(final XMLReportNode node) {

		final String classFQNOrNull = node.getClassFQNOrNull();
		if (classFQNOrNull == null) {
			return null;
		}

		return javaResourceLocator.findResourceByClassName(classFQNOrNull);
	}

	private InputFile mkInputFileOrNullFromPath(final XMLReportNode node,
			final SensorContext sensorContext) {

		final FileSystem fs = sensorContext.fileSystem();

		/*
		 * Create the relative path from the persistence string
		 */
		final String relativePath = node.getRelativePathOrNull();

		if (relativePath != null) {

			final Iterable<InputFile> inputFilesIterable = sensorContext.fileSystem()
					.inputFiles(fs.predicates().hasRelativePath(relativePath));

			// Use first matching input file
			for (final InputFile inputFile : inputFilesIterable) {
				return inputFile;
			}

		}
		return null;
	}

	private boolean createNewIssue(final InputFile inputFile, final XMLReportFinding xanFinding,
			final SensorContext sensorContext) {

		final RuleKey ruleKey = mkRuleKey(xanFinding);
		final int lineNo = normalizeLineNo(xanFinding.getLocation().getLineNoOrMinus1());
		final Severity severity = SensorUtil.mkSeverity(xanFinding);

		final String issueKey = mkIssueKey(ruleKey, inputFile, lineNo);
		final NewIssue alreadyCreatedIssue = alreadyCreatedIssues.get(issueKey);
		if (alreadyCreatedIssue != null) {

			addSecondaryLocation(alreadyCreatedIssue, xanFinding, sensorContext);

			LOG.debug("Issue already exists: " + inputFile + ":" + lineNo + " - "
					+ xanFinding.getProblemTypeString());
			return false;
		}

		final NewIssue newIssue = sensorContext.newIssue();
		newIssue.forRule(ruleKey);
		newIssue.overrideSeverity(severity);

		final NewIssueLocation newIssueLocation = newIssue.newLocation();
		newIssueLocation.on(inputFile);

		// If line number exceeds the current length of the file,
		// SonarQube will crash. So check length for robustness.
		if (lineNo <= inputFile.lines()) {
			final TextRange textRange = inputFile.selectLine(lineNo);
			newIssueLocation.at(textRange);
		}

		newIssueLocation.message(mkMessage(xanFinding));
		newIssue.at(newIssueLocation);
		addSecondaryLocation(newIssue, xanFinding, sensorContext);

		alreadyCreatedIssues.put(issueKey, newIssue);

		LOG.debug("Issue created: " + inputFile + ":" + lineNo + " - "
				+ xanFinding.getProblemTypeString());
		return true;
	}

	private void addSecondaryLocation(final NewIssue issue, final XMLReportFinding xanFinding,
			final SensorContext sensorContext) {
		final InputFile secondaryFile = mkInputFileOrNull(xanFinding.getSecondaryLocationOrNull(),
				sensorContext);
		if (secondaryFile != null) {
			final NewIssueLocation secondaryLocation = issue.newLocation();
			secondaryLocation.on(secondaryFile);
			secondaryLocation.message(xanFinding.getSecondaryLocationMessage());
			final int secondaryLine = normalizeLineNo(
					xanFinding.getSecondaryLocationOrNull().getLineNoOrMinus1());
			if (secondaryLine <= secondaryFile.lines()) {
				final TextRange textRange = secondaryFile.selectLine(secondaryLine);
				secondaryLocation.at(textRange);
			}
			issue.addLocation(secondaryLocation);

			LOG.debug("Added secondary location for finding " + xanFinding.getFindingID());
		}
	}

	private String mkIssueKey(final RuleKey ruleKey, final InputFile file, final int lineNo) {
		return ruleKey.toString() + ":" + file.toString() + ":" + lineNo;
	}

	private List<Metric<Serializable>> mkMetrics(final XMLReportFinding finding) {
		final List<Metric<Serializable>> result = new ArrayList<>();
		result.add(XanitizerMetrics.getMetricForAllXanFindings());

		if (finding.isDependencyCheckFinding()) {
			result.add(XanitizerMetrics.getMetricForDependencyCheckFindings());
		} else if (finding.isSpotBugsFinding()) {
			result.add(XanitizerMetrics.getMetricForSpotBugsFindings());
		} else {
			final Metric<Serializable> metricOrNull = XanitizerMetrics
					.mkMetricForProblemType(finding.getProblemTypeOrNull());
			if (metricOrNull != null) {
				result.add(metricOrNull);
			}
		}

		return result;
	}

	private RuleKey mkRuleKey(final XMLReportFinding finding) {
		final String rule;
		if (finding.isDependencyCheckFinding()) {
			rule = XanitizerRulesDefinition.OWASP_DEPENDENCY_CHECK_RULE;
		} else if (finding.isSpotBugsFinding()) {
			rule = XanitizerRulesDefinition.SPOTBUGS_RULE;
		} else {
			rule = finding.getProblemTypeOrNull().name();
		}

		return RuleKey.of(XanitizerRulesDefinition.REPOSITORY_KEY, rule);
	}

	private String mkMessage(final XMLReportFinding finding) {
		if (finding.isDependencyCheckFinding()) {
			return "Remove outdated library.";
		}

		if (finding.isSpotBugsFinding()) {
			return "Check SpotBugs documentation how to resolve '" + finding.getProblemTypeString()
					+ "' problems.";
		}

		return finding.getProblemTypeOrNull().getMessage();
	}

	private static void incrementValueForFileAndProject(final Metric<Serializable> metric,
			final InputFile resource, final InputModule project,
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu) {
		incrementValue(metric, resource, metricValuesAccu);
		incrementValue(metric, project, metricValuesAccu);
	}

	private static void incrementValue(final Metric<Serializable> metric,
			final InputComponent resource,
			final Map<Metric<Serializable>, Map<InputComponent, Integer>> metricValuesAccu) {
		final Map<InputComponent, Integer> innerMap = metricValuesAccu.computeIfAbsent(metric,
				k -> new LinkedHashMap<>());

		final Integer value = innerMap.computeIfAbsent(resource, k -> 0);
		innerMap.put(resource, 1 + value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
