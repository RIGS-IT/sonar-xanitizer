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

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedBugTypeIds;
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

		for (final ActiveRule activeRule : activeRules.findAll()) {
			if (activeRule.ruleKey().repository()
					.equals(XanitizerRulesDefinition.getRepositoryKey())) {
				final String ruleAsString = activeRule.ruleKey().rule();
				activeXanRuleNames.add(ruleAsString);
			}
		}
		if (activeXanRuleNames.isEmpty()) {
			LOG.error(
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
			generateIssuesForFinding(f, metricValues, analysisDatePresentation, project,
					sensorContext);
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
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final String analysisDatePresentation, final Project project,
			final SensorContext sensorContext) {

		switch (finding.getFindingKind()) {
		case PATH:
			generateTaintPathIssue(finding, metricValuesAccu, analysisDatePresentation, project,
					sensorContext);
			break;
		case SPECIAL:
			generateSpecialCodeIssue(finding, metricValuesAccu, analysisDatePresentation, project,
					sensorContext);
			break;
		case USER:
			generateUserIssue(finding, metricValuesAccu, analysisDatePresentation, project,
					sensorContext);
			break;
		case GENERIC:
		case NON_TAINTED:
		case SANITIZER:
			generateGenericIssue(finding, metricValuesAccu, analysisDatePresentation, project,
					sensorContext);
			break;
		default:
			// Nothing to do.
			LOG.error("Unexpected Xanitizer finding kind:" + finding.getFindingKind());
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	private void generateGenericIssue(final XMLReportFinding finding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final String analysisDatePresentation, final Project project,
			final SensorContext sensorContext) {
		/*
		 * If this is an OWASP Dependency Check finding, create an issue on the
		 * root project.
		 */
		if (finding.getFindingProducer().toLowerCase().contains("owaspdependencycheck")) {

			// No line number.
			final int lineNumber = -1;
			generateIssueOnInputFileOrProject(null, project, lineNumber,
					XanitizerRule.mkRuleForFindingOrNull(finding),
					mkOWASPDepCheckFindingDescription(finding), finding, analysisDatePresentation,
					metricValuesAccu, sensorContext);
		} else {
			final InputFile inputFileOrNull = mkInputFileOrNull(finding.getClassFQNOrNull(),
					sensorContext);

			generateIssueOnInputFileOrProject(inputFileOrNull, project,
					normalizeLineNo(finding.getLineNoOrMinus1()),
					XanitizerRule.mkRuleForFindingOrNull(finding),
					mkGenericFindingDescription(finding), finding, analysisDatePresentation,
					metricValuesAccu, sensorContext);
		}
	}

	@SuppressWarnings("rawtypes")
	private void generateUserIssue(final XMLReportFinding finding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final String analysisDatePresentation, final Project project,
			final SensorContext sensorContext) {
		final InputFile inputFileOrNull = mkInputFileOrNull(finding.getClassFQNOrNull(),
				sensorContext);
		generateIssueOnInputFileOrProject(inputFileOrNull, project,
				normalizeLineNo(finding.getLineNoOrMinus1()),
				XanitizerRule.mkRuleForFindingOrNull(finding),
				"User-defined finding for problem type '"
						+ GeneratedBugTypeIds
								.mkPresentationNameForBugTypeId(finding.getProblemTypeId())
						+ "'" + finding.getDescription()
						+ mkDescriptionSuffixForLocation(inputFileOrNull,
								mkLocationForUserFindingString(finding)),
				finding, analysisDatePresentation, metricValuesAccu, sensorContext);
	}

	@SuppressWarnings("rawtypes")
	private void generateSpecialCodeIssue(final XMLReportFinding finding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final String analysisDatePresentation, final Project project,
			final SensorContext sensorContext) {
		final InputFile inputFileOrNull = mkInputFileOrNull(finding.getClassFQNOrNull(),
				sensorContext);
		final XMLReportNode node = finding.getNodeOrNull();

		if (node == null) {
			LOG.error("node missing for special finding " + finding.getFindingID() + "; skipping");
			return;
		}

		generateIssueOnInputFileOrProject(inputFileOrNull, project,
				normalizeLineNo(finding.getLineNoOrMinus1()),
				XanitizerRule.mkRuleForFindingOrNull(finding),
				"Special code location for problem type '"
						+ GeneratedBugTypeIds
								.mkPresentationNameForBugTypeId(finding.getProblemTypeId())
						+ "'"
						+ mkDescriptionSuffixForLocation(inputFileOrNull,
								mkLocationString(node, sensorContext)),
				finding, analysisDatePresentation, metricValuesAccu, sensorContext);
	}

	@SuppressWarnings("rawtypes")
	private void generateTaintPathIssue(final XMLReportFinding finding,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final String analysisDatePresentation, final Project project,
			final SensorContext sensorContext) {
		final XMLReportNode startNode = finding.getStartNodeOfPathOrNull();
		final XMLReportNode endNode = finding.getEndNodeOfPathOrNull();

		if (startNode == null || endNode == null) {
			LOG.error("start or end node missing for path finding " + finding.getFindingID()
					+ "; skipping");
			return;
		}

		final InputFile inputFileOrNull = mkInputFileOrNull(endNode, sensorContext);
		generateIssueOnInputFileOrProject(inputFileOrNull, project,
				normalizeLineNo(endNode.getLineNoOrMinus1()),
				XanitizerRule.mkRuleForFindingOrNull(finding),
				"Taint path for problem type '"
						+ GeneratedBugTypeIds
								.mkPresentationNameForBugTypeId(finding.getProblemTypeId())
						+ "', path starting at " + mkLocationString(startNode, sensorContext)
						+ " and ending at " + mkLocationString(endNode, sensorContext),
				finding, analysisDatePresentation, metricValuesAccu, sensorContext);
	}

	private String mkLocationForUserFindingString(final XMLReportFinding finding) {
		final String persistenceStringOrNull = finding.getPersistenceStringOrNull();
		if (persistenceStringOrNull != null) {
			return persistenceStringOrNull;
		}
		return "<no file found>";
	}

	private String mkDescriptionSuffixForLocation(final InputComponent inputComponentOrNull,
			final String locationStringOrNull) {
		if (inputComponentOrNull != null) {
			/*
			 * No need to generate an extra description for the location - the
			 * issue will be registered with the given resource.
			 */
			return "";
		}
		if (locationStringOrNull == null) {
			// No location description.
			return "";
		}
		return " - " + locationStringOrNull;
	}

	/*
	 * In SonarQube, line numbers must be strioctly positive.
	 */
	private static int normalizeLineNo(final int lineNo) {
		if (lineNo <= 0) {
			return 1;
		}
		return lineNo;
	}

	private static String mkOWASPDepCheckFindingDescription(final XMLReportFinding finding) {

		final StringBuilder sb = new StringBuilder("OWASP Dependency Check");
		appendDescription(sb, finding);
		return sb.toString();
	}

	private static String mkGenericFindingDescription(final XMLReportFinding finding) {

		final String prefix;
		if (finding.getFindingProducer().toLowerCase().contains("findbugs")) {
			prefix = "Findbugs";
		} else {
			prefix = "Generic";
		}

		final StringBuilder sb = new StringBuilder(prefix + " finding");
		appendDescription(sb, finding);
		return sb.toString();

	}

	private static void appendDescription(final StringBuilder sb, final XMLReportFinding finding) {
		final String origAbsFileOrNull = finding.getOriginalAbsFileOrNull();
		final String desc = finding.getDescription();
		final String extraDesc = finding.getExtraDescription();

		if (origAbsFileOrNull != null) {
			sb.append(" with " + origAbsFileOrNull + ":\n");
		}

		if (desc.isEmpty() && extraDesc.isEmpty()) {
			sb.append("- no description available -");
		} else {
			if (!desc.isEmpty()) {
				sb.append(desc + "\n");
			}
			if (!extraDesc.isEmpty()) {
				sb.append(extraDesc);
			}
		}
	}

	private String mkLocationString(final XMLReportNode node, final SensorContext sensorContext) {
		if (node == null) {
			return "<no resource found>";
		}
		final InputFile inputFileOrNull = mkInputFileOrNull(node, sensorContext);
		if (inputFileOrNull == null) {
			return mkResourcePresentation(node) + ":" + node.getLineNoOrMinus1();
		}
		return inputFileOrNull.absolutePath() + ":" + node.getLineNoOrMinus1();
	}

	private String mkResourcePresentation(final XMLReportNode node) {
		return node.getClassFQNOrNull() != null ? node.getClassFQNOrNull()
				: node.getXFilePersistenceOrNull() != null ? node.getXFilePersistenceOrNull()
						.replace("XanitizerPath:${PROJECT_DIR}", "/") : "<no resource found>";
	}

	private InputFile mkInputFileOrNull(final XMLReportNode nodeOrNull,
			final SensorContext sensorContext) {
		if (nodeOrNull == null) {
			return null;
		}
		final String classFQNOrNull = nodeOrNull.getClassFQNOrNull();
		if (classFQNOrNull == null) {
			return null;
		}
		final Resource resource = javaResourceLocator.findResourceByClassName(classFQNOrNull);
		if (resource == null) {
			return null;
		}

		return mkInputFileOrNullFromResource(resource, sensorContext);
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

	private InputFile mkInputFileOrNull(final String classFQNOrNull,
			final SensorContext sensorContext) {
		if (classFQNOrNull == null) {
			return null;
		}
		final Resource resourceOrNull = javaResourceLocator.findResourceByClassName(classFQNOrNull);
		if (resourceOrNull == null) {
			return null;
		}

		return mkInputFileOrNullFromResource(resourceOrNull, sensorContext);
	}

	@SuppressWarnings("rawtypes")
	private boolean generateIssueOnInputFileOrProject(final InputFile inputFileOrNull,
			final Project project, final int lineNo, final XanitizerRule rule,
			final String descriptionOrNull, final XMLReportFinding xanFinding,
			final String analysisDatePresentation,
			final Map<Metric, Map<Resource, Integer>> metricValuesAccu,
			final SensorContext sensorContext) {

		/*
		 * At first try to find the resource in another module. Only if the root
		 * project is analyzed, all remaining findings have to be added.
		 */
		if (inputFileOrNull == null && !project.isRoot()) {
			return false;
		}

		if (!activeXanRuleNames.contains(rule.toString())) {
			return false;
		}

		final Resource resourceToBeUsed = inputFileOrNull == null ? project
				: sensorContext.getResource(inputFileOrNull);

		final Severity severity = SensorUtil.mkSeverity(xanFinding);
		final RuleKey ruleKey = RuleKey.of(XanitizerRulesDefinition.getRepositoryKey(),
				rule.name());

		createNewIssue(inputFileOrNull, project, lineNo, rule, descriptionOrNull, xanFinding,
				analysisDatePresentation, sensorContext, resourceToBeUsed, severity, ruleKey);

		final String problemTypeId = xanFinding.getProblemTypeId();

		final List<Metric> metrics = mkMetrics(problemTypeId);
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
			final int lineNo, final XanitizerRule rule, final String descriptionOrNull,
			final XMLReportFinding xanFinding, final String analysisDatePresentation,
			final SensorContext sensorContext, final Resource resourceToBeUsed,
			final Severity severity, final RuleKey ruleKey) {
		final NewIssue newIssue = sensorContext.newIssue();

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

		final String msg = (descriptionOrNull != null ? descriptionOrNull
				: rule.getShortHTMLDescription())
				+ mkIdSuffixForFinding(xanFinding, analysisDatePresentation);

		newIssueLocation.message(msg);

		newIssue.at(newIssueLocation);

		newIssue.save();

		LOG.debug("Issue saved: " + resourceToBeUsed + ":" + lineNo + " - " + ruleKey + " - "
				+ descriptionOrNull);
	}

	private String mkIdSuffixForFinding(final XMLReportFinding xanFinding,
			final String analysisDatePresentation) {
		final String findingId = Integer.toString(xanFinding.getFindingID());
		return " (Xanitizer finding id " + findingId + ", analysis of " + analysisDatePresentation
				+ ")";
	}

	@SuppressWarnings("rawtypes")
	private List<Metric> mkMetrics(final String bugTypeId) {
		final List<Metric> result = new ArrayList<>();
		result.add(XanitizerMetrics.getMetricForAllXanFindings());

		final Metric metricOrNull = XanitizerMetrics.mkMetricForBugTypeIdOrNull(bugTypeId);
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
