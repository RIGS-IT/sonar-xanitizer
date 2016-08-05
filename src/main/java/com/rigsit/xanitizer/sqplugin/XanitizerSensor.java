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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputModule;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputModule;
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

	/**
	 * The Xanitizer sensor
	 * 
	 * @param javaResourceLocator
	 * @param settings
	 * @param activeRules
	 */
	public XanitizerSensor(final JavaResourceLocator javaResourceLocator, final Settings settings,
			final ActiveRules activeRules) {
		this.javaResourceLocator = javaResourceLocator;
		this.reportFile = SensorUtil.geReportFile(settings);
		if (this.reportFile == null) {
			return;
		}

		for (final ActiveRule activeRule : activeRules.findAll()) {
			if (activeRule.ruleKey().repository()
					.equals(XanitizerRulesDefinition.getRepositoryKey())) {
				final String ruleAsString = activeRule.ruleKey().rule();
				activeXanRuleNames.add(ruleAsString);
			}
		}
		if (activeXanRuleNames.isEmpty()) {
			LOG.warn(
					"No Xanitizer rule is set active in the used quality profile. Skipping analysis.");
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

		final String toolVersionShortOrNull = content.getToolVersionShortOrNull();
		if (toolVersionShortOrNull == null) {
			LOG.error("No attribute 'xanitizerVersionShort' found in XML report file '" + reportFile
					+ "'. Skipping analysis.");
			return;
		}

		final String errMsgOrNull = SensorUtil.checkVersion(toolVersionShortOrNull, 2, 3, -1);
		if (errMsgOrNull != null) {
			LOG.error("Could not parse attribute 'xanitizerVersionShort' in XML report file '"
					+ reportFile + "': " + errMsgOrNull + ". Skipping analysis.");
			return;
		}

		final long analysisEndDate = content.getAnalysisEndDate();
		if (analysisEndDate == 0) {
			LOG.error(
					"No Xanitizer analysis results found - Check if Xanitizer analysis has been executed!");
			return;
		}

		createMeasures(project, sensorContext, analysisEndDate, content);
	}

	@SuppressWarnings("rawtypes")
	private void createMeasures(final Project project, final SensorContext sensorContext,
			final long analysisEndDate, final XMLReportContent content) {

		final String analysisDatePresentation = SensorUtil
				.convertToDateWithTimeString(new Date(analysisEndDate));

		final Map<Metric, Map<Resource, Integer>> metricValues = new LinkedHashMap<>();

		LOG.info("Processing Xanitizer analysis results of " + analysisDatePresentation
				+ "; findings: " + content.getXMLReportFindings().size());

		// Generate issues for findings.
		for (final XMLReportFinding f : content.getXMLReportFindings()) {
			generateIssuesForFinding(f, metricValues, project, sensorContext);
		}

		// Metrics: Counts of different findings.
		for (final Map.Entry<Metric, Map<Resource, Integer>> e : metricValues.entrySet()) {
			final Metric metric = e.getKey();
			for (final Map.Entry<Resource, Integer> e1 : e.getValue().entrySet()) {
				final Resource resource = e1.getKey();
				final Integer value = e1.getValue();

				if (value != 0) {
					final Measure measure = new Measure(metric, value.doubleValue());
					LOG.debug("Creating measure for metric " + measure.getMetricKey()
							+ ": adding value = " + value + " to resource " + resource.getName());
					sensorContext.saveMeasure(resource, measure);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void generateIssuesForFinding(final XMLReportFinding finding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu, final Project project,
			final SensorContext sensorContext) {

		final InputFile inputFileOrNull = mkInputFileOrNull(finding,
				sensorContext);

		generateIssueOnInputFileOrProject(inputFileOrNull, project,
				finding.getProblemType().getPresentationName() + mkKindString(finding)
						+ mkDescriptionSuffixForLocation(inputFileOrNull, finding),
				finding, metricValuesAccu, sensorContext);
	}

	private String mkKindString(final XMLReportFinding finding) {
		final StringBuffer sb = new StringBuffer(" (");
		switch (finding.getFindingKind()) {
		case NON_TAINTED:
			sb.append("non-tainted; ");
			break;
		case SANITIZER:
			sb.append("applied Sanitizer; ");
			break;
		case PATH:
			sb.append("taint sink; ");
			break;
		case USER:
			sb.append("user generated; ");
			break;
		default:
			// do nothing
		}

		final String findingId = Integer.toString(finding.getFindingID());
		sb.append("Xanitizer finding ID ");
		sb.append(findingId);
		sb.append(")");
		return sb.toString();
	}

	private String mkDescriptionSuffixForLocation(final InputComponent inputComponentOrNull,
			final XMLReportFinding finding) {
		if (inputComponentOrNull != null) {
			/*
			 * No need to generate an extra description for the location - the
			 * issue will be registered with the given resource.
			 */
			return "";
		}

		return " - " + mkResourcePresentation(finding) + ":" + finding.getLineNoOrMinus1();

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

	private String mkResourcePresentation(final XMLReportFinding finding) {
		if (finding.getClassFQNOrNull() != null) {
			return finding.getClassFQNOrNull();
		}
		
		final String relativePath = getRelativePathOrNull(finding);
		if (relativePath != null) {
			return relativePath;
		}
		
		return "<no resource found>";
	}
	
	private String getRelativePathOrNull(final XMLReportFinding finding) {
		if (finding.getPersistenceStringOrNull() != null) {
			// TODO at the moment a bug in the report file results in a reference to the project directory
			final String persistence = finding.getPersistenceStringOrNull().replace("XanitizerPath:${PROJECT_DIR}Resources/", "/");
			
			// No resource
			if (persistence.contains("XanitizerNonResource")) {
				return null;
			}
			
			return persistence.replace("XanitizerPath:",  "/");
		}
		return null;
	}

	private InputFile mkInputFileOrNull(final XMLReportFinding finding,
			final SensorContext sensorContext) {
		
		final String classFQNOrNull = finding.getClassFQNOrNull();
		if (classFQNOrNull == null) {
			final String relativePath = getRelativePathOrNull(finding);
			if (relativePath != null) {
				return mkInputFileOrNullFromPath(relativePath, sensorContext);
			}
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
			final Object resourceOrNull = findResource.invoke(javaResourceLocator, classFQNOrNull);

			if (resourceOrNull instanceof InputFile) {
				return (InputFile) resourceOrNull;
			}

			if (resourceOrNull instanceof Resource) {
				return mkInputFileOrNullFromResource((Resource) resourceOrNull, sensorContext);
			}
		} catch (Exception e) {
			LOG.error("Could not call method 'findResourceByClassName' on Java resource locator!");
		}

		return null;
	}
	
	private InputFile mkInputFileOrNullFromPath(final String relativePath,
			final SensorContext sensorContext) {

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

	@SuppressWarnings("rawtypes")
	private boolean generateIssueOnInputFileOrProject(final InputFile inputFileOrNull,
			final Project project, final String description, final XMLReportFinding xanFinding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final SensorContext sensorContext) {

		/*
		 * At first try to find the resource in another module. Only if the root
		 * project is analyzed, all remaining findings have to be added.
		 */
		if (inputFileOrNull == null && !project.isRoot()) {
			return false;
		}

		if (!activeXanRuleNames.contains(xanFinding.getProblemType().name())) {
			return false;
		}

		final Resource resourceToBeUsed = inputFileOrNull == null ? project
				: sensorContext.getResource(inputFileOrNull);

		final Severity severity = SensorUtil.mkSeverity(xanFinding);

		final int lineNo = normalizeLineNo(xanFinding.getLineNoOrMinus1());
		createNewIssue(inputFileOrNull, project, lineNo, description, xanFinding, sensorContext,
				resourceToBeUsed, severity);

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

		return true;
	}

	private void createNewIssue(final InputFile inputFileOrNull, final Project project,
			final int lineNo, final String description, final XMLReportFinding xanFinding,
			final SensorContext sensorContext, final Resource resourceToBeUsed,
			final Severity severity) {
		final NewIssue newIssue = sensorContext.newIssue();

		final RuleKey ruleKey = RuleKey.of(XanitizerRulesDefinition.getRepositoryKey(),
				xanFinding.getProblemType().name());
		newIssue.forRule(ruleKey);

		newIssue.overrideSeverity(severity);

		final NewIssueLocation newIssueLocation = newIssue.newLocation();

		if (inputFileOrNull != null) {
			newIssueLocation.on(inputFileOrNull);

			// If line number exceeds the current length of the file,
			// SonarQube will crash. So check length for robustness.
			if (lineNo > 0 && lineNo <= inputFileOrNull.lines()) {
				final TextRange textRange = inputFileOrNull.selectLine(lineNo);
				newIssueLocation.at(textRange);
			}
		} else {
			final InputModule inputModule = new DefaultInputModule(project.getKey());
			newIssueLocation.on(inputModule);
		}

		newIssueLocation.message(description);

		newIssue.at(newIssueLocation);

		newIssue.save();

		LOG.debug("Issue saved: " + resourceToBeUsed + ":" + lineNo + " - " + ruleKey + " - "
				+ description);
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
