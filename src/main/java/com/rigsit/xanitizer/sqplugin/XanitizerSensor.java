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
 * Created on October 10, 2015
 */
package com.rigsit.xanitizer.sqplugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.java.api.JavaResourceLocator;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
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

	private final JavaResourceLocator javaResourceLocator;
	private final File reportFile;

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
	public XanitizerSensor(final JavaResourceLocator javaResourceLocator, final Settings settings,
			final ActiveRules activeRules, final SensorContext sensorContext) {
		this.javaResourceLocator = javaResourceLocator;

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
			this.reportFile = SensorUtil.geReportFile(sensorContext, settings);
		}
	}

	@Override
	public boolean shouldExecuteOnProject(final Project project) {
		return reportFile != null && !activeXanRuleNames.isEmpty();
	}

	@Override
	public void analyse(final Project project, final SensorContext sensorContext) {
		assert reportFile != null;
		assert !activeXanRuleNames.isEmpty();

		LOG.info("Reading Xanitizer findings from '" + reportFile + "' for project '"
				+ project.name() + "'");

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
		if (analysisEndDate == 0) {
			LOG.warn(
					"No Xanitizer analysis results found - Check if Xanitizer analysis has been executed!");
			return;
		}

		final String analysisDatePresentation = SensorUtil
				.convertToDateWithTimeString(new Date(analysisEndDate));
		LOG.info("Processing Xanitizer analysis results of " + analysisDatePresentation);
		LOG.debug("Create issues for " + content.getXMLReportFindings().size() + " Xanitizer findings.");
		createIssuesAndMeasures(project, sensorContext, content);
	}

	@SuppressWarnings("rawtypes")
	private void createIssuesAndMeasures(final Project project, final SensorContext sensorContext,
			final XMLReportContent content) {

		final Map<Metric, Map<Resource, Integer>> metricValues = new LinkedHashMap<>();
		initializeMetrics(metricValues, project);

		// Generate issues for findings.
		for (final XMLReportFinding f : content.getXMLReportFindings()) {
			generateIssueForFinding(f, metricValues, project, sensorContext);
		}
		
		for (final NewIssue issue : alreadyCreatedIssues.values()) {
			issue.save();
		}

		// Metrics: Counts of different findings.
		for (final Map.Entry<Metric, Map<Resource, Integer>> e : metricValues.entrySet()) {
			final Metric metric = e.getKey();
			for (final Map.Entry<Resource, Integer> e1 : e.getValue().entrySet()) {
				final Resource resource = e1.getKey();
				final Integer value = e1.getValue();
				if (resource == null) {
					continue;
				}

				// Only create metrics with 0 values on project level
				if (value != 0 || resource == project) {
					final Measure measure = new Measure(metric, value.doubleValue());
					LOG.debug("Creating measure for metric " + measure.getMetricKey()
							+ ": adding value = " + value + " to resource " + resource.getName());
					sensorContext.saveMeasure(resource, measure);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void generateIssueForFinding(final XMLReportFinding xanFinding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu, final Project project,
			final SensorContext sensorContext) {

		if (!activeXanRuleNames.contains(xanFinding.getProblemType().name())) {
			LOG.debug("Xanitizer: Skipping finding " + xanFinding.getFindingID()
			+ ": Rule for corresponding problem type is disabled.");
			return;
		}

		final InputFile inputFile = mkInputFileOrNull(xanFinding.getLocation(), sensorContext);
		if (inputFile == null) {
			/*
			 * Do not generate issues without code location
			 */
			LOG.debug("Xanitizer: Skipping finding " + xanFinding.getFindingID()
					+ ": Corresponding file could not be found in project.");

			return;
		}

		final boolean issueCreated = createNewIssue(inputFile, xanFinding, sensorContext);

		if (issueCreated) {
			incrementMetrics(xanFinding, metricValuesAccu, project, sensorContext, inputFile);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initializeMetrics(final Map<Metric, Map<Resource, Integer>> metricValuesAccu, final Project project) {
		
		for (Metric metric : new XanitizerMetrics().getMetrics()) {
			initializeMetric(metricValuesAccu, project, metric);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initializeMetric(final Map<Metric, Map<Resource, Integer>> metricValuesAccu, final Resource resource, final Metric metric) {
		Map<Resource, Integer> innerMap = new LinkedHashMap<>();
		innerMap.put(resource, 0);
		metricValuesAccu.put(metric, innerMap);
	}

	@SuppressWarnings("rawtypes")
	private void incrementMetrics(final XMLReportFinding xanFinding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu, final Project project,
			final SensorContext sensorContext, final InputFile inputFile) {
		final Severity severity = SensorUtil.mkSeverity(xanFinding);
		final Resource resourceToBeUsed = sensorContext.getResource(inputFile);

		final List<Metric> metrics = mkMetrics(xanFinding.getProblemType());
		for (final Metric metric : metrics) {
			incrementValueForResourceAndContainingResources(metric, resourceToBeUsed, project,
					metricValuesAccu);
		}

		final String matchCode = xanFinding.getMatchCode();
		if ("NOT".equals(matchCode)) {
			incrementValueForResourceAndContainingResources(
					XanitizerMetrics.getMetricForNewXanFindings(), resourceToBeUsed, project,
					metricValuesAccu);
		}

		final Metric metricForSeverity = XanitizerMetrics.getMetricForSeverity(severity);
		if (metricForSeverity != null) {
			incrementValueForResourceAndContainingResources(metricForSeverity, resourceToBeUsed,
					project, metricValuesAccu);
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

		final InputFile result = mkInputFileOrNullFromClass(node, sensorContext);
		if (result == null) {
			return mkInputFileOrNullFromPath(node, sensorContext);
		}

		return result;
	}

	private InputFile mkInputFileOrNullFromClass(final XMLReportNode node,
			final SensorContext sensorContext) {

		final String classFQNOrNull = node.getClassFQNOrNull();
		if (classFQNOrNull == null) {
			return null;
		}
		/*
		 * The API of the Java plugin has changed and returns InputFile directly
		 * since 4.0. To support previous versions, use reflection here and do
		 * an instance check.
		 */
		try {
			final Method findResource = javaResourceLocator.getClass()
					.getDeclaredMethod("findResourceByClassName", String.class);
			final Object resource = findResource.invoke(javaResourceLocator, classFQNOrNull);

			if (resource instanceof InputFile) {
				return (InputFile) resource;
			}

			if (resource instanceof Resource) {
				return mkInputFileOrNullFromResource((Resource) resource, sensorContext);
			}
		} catch (Exception e) {
			LOG.error("Could not call method 'findResourceByClassName' on Java resource locator!",
					e);
		}
		return null;
	}

	private InputFile mkInputFileOrNullFromPath(final XMLReportNode node,
			final SensorContext sensorContext) {

		final FileSystem fs = sensorContext.fileSystem();

		/*
		 * First check, if the absolute file exists on the machine and then try
		 * to detect it in the project context
		 */
		final String originalAbsoluteFile = node.getAbsolutePathOrNull();
		if (originalAbsoluteFile != null && new File(originalAbsoluteFile).isFile()) {
			final Iterable<InputFile> inputFilesIterable = sensorContext.fileSystem()
					.inputFiles(fs.predicates().hasAbsolutePath(originalAbsoluteFile));

			// Use first matching input file
			for (final InputFile inputFile : inputFilesIterable) {
				return inputFile;
			}
		}

		/*
		 * If the absolute path does not exist, create the relative path from
		 * the persistence string
		 */
		final String relativePath = node.getRelativePathOrNull();
		if (relativePath != null) {
			final File absoluteFile = new File(fs.baseDir(), relativePath);
			final String absoluteFilePath = absoluteFile.getAbsolutePath();

			final Iterable<InputFile> inputFilesIterable = sensorContext.fileSystem()
					.inputFiles(fs.predicates().hasAbsolutePath(absoluteFilePath));

			// Use first matching input file
			for (final InputFile inputFile : inputFilesIterable) {
				return inputFile;
			}

		}
		return null;
	}

	private InputFile mkInputFileOrNullFromResource(final Resource resource,
			final SensorContext sensorContext) {
		final String relativePath = resource.getPath();

		final FileSystem fs = sensorContext.fileSystem();

		final File absoluteFile = new File(fs.baseDir(), relativePath);
		final String absoluteFilePath = absoluteFile.getAbsolutePath();

		/*
		 * SonarQube 5.4 seems to sometimes enter an endless loop here, if a
		 * non-trivial predicate is given.
		 */
		final Iterable<InputFile> inputFilesIterable = sensorContext.fileSystem()
				.inputFiles(fs.predicates().hasAbsolutePath(absoluteFilePath));

		// Use first matching input file, or none.
		for (final InputFile inputFile : inputFilesIterable) {
			return inputFile;
		}

		return null;
	}

	private boolean createNewIssue(final InputFile inputFile, final XMLReportFinding xanFinding,
			final SensorContext sensorContext) {

		final GeneratedProblemType pt = xanFinding.getProblemType();
		final RuleKey ruleKey = RuleKey.of(XanitizerRulesDefinition.REPOSITORY_KEY, pt.name());
		final int lineNo = normalizeLineNo(xanFinding.getLocation().getLineNoOrMinus1());
		final Severity severity = SensorUtil.mkSeverity(xanFinding);

		final String issueKey = mkIssueKey(ruleKey, inputFile, lineNo);
		final NewIssue alreadyCreatedIssue = alreadyCreatedIssues.get(issueKey);
		if (alreadyCreatedIssue != null) {

			addSecondaryLocation(alreadyCreatedIssue, xanFinding, sensorContext);

			LOG.debug("Issue already exists: " + inputFile + ":" + lineNo + " - "
					+ pt.getPresentationName());
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

		newIssueLocation.message(pt.getMessage());
		newIssue.at(newIssueLocation);
		addSecondaryLocation(newIssue, xanFinding, sensorContext);

		alreadyCreatedIssues.put(issueKey, newIssue);

		LOG.debug("Issue saved: " + inputFile + ":" + lineNo + " - " + pt.getPresentationName());
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

	@SuppressWarnings("rawtypes")
	private List<Metric> mkMetrics(final GeneratedProblemType problemType) {
		final List<Metric> result = new ArrayList<>();
		result.add(XanitizerMetrics.getMetricForAllXanFindings());

		final Metric metricOrNull = XanitizerMetrics.mkMetricForProblemType(problemType);
		if (metricOrNull != null) {
			result.add(metricOrNull);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private static void incrementValueForResourceAndContainingResources(final Metric metric,
			final Resource resource, final Project project,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu) {
		Resource runner = resource;
		while (runner != null) {
			incrementValue(metric, runner, metricValuesAccu);
			if (ResourceUtils.isFile(runner)) {
				// Go to directory.
				runner = runner.getParent();
			} else if (ResourceUtils.isDirectory(runner)) {
				// Go to project.
				runner = project;
			} else {
				// This is a project. No container.
				runner = null;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static void incrementValue(final Metric metric, final Resource resource,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu) {
		Map<Resource, Integer> innerMap = metricValuesAccu.get(metric);
		if (innerMap == null) {
			innerMap = new LinkedHashMap<>();
			metricValuesAccu.put(metric, innerMap);
		}

		Integer value = innerMap.get(resource);
		if (value == null) {
			value = 0;
		}

		innerMap.put(resource, 1 + value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
