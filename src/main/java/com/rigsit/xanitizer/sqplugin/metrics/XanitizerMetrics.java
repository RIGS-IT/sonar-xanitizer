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
package com.rigsit.xanitizer.sqplugin.metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.rule.Severity;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * Implementation of the Metrics interface
 * 
 * @author rust
 * 
 */
@SuppressWarnings("rawtypes")
public final class XanitizerMetrics implements Metrics {

	private static final String DOMAIN = "Xanitizer Findings";

	private static final String PFIX = "XanFindingMetric_";

	private static final Metric<Serializable> ALL_XAN_FINDINGS_METRIC;
	private static final Metric<Serializable> NEW_FINDINGS_METRIC;
	private static final Metric<Serializable> BLOCKER_FINDINGS_METRIC;
	private static final Metric<Serializable> CRITICAL_FINDINGS_METRIC;
	private static final Metric<Serializable> MAJOR_FINDINGS_METRIC;
	private static final Metric<Serializable> MINOR_FINDINGS_METRIC;
	private static final Metric<Serializable> INFO_FINDINGS_METRIC;
	private static final Metric<Serializable> SPOTBUGS_FINDINGS_METRIC;
	private static final Metric<Serializable> DEPENDENCY_CHECK_FINDINGS_METRIC;
	
	/*
	 * There is a limit for metric names in SonarQube.
	 * 
	 * I (HRU) determined the limit 64 by trial and error, on 2015-10-12,
	 * with a test installation of SonarQube 5.1.2.
	 * 
	 * We use 60 to be on the safe side...
	 */
	private static final int METRIC_NAME_LIMIT = 60;

	static {
		ALL_XAN_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "All", "All Xanitizer Findings", Metric.ValueType.INT)

						.setDescription("All Xanitizer Findings")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();

		NEW_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "New", "New Xanitizer Findings", Metric.ValueType.INT)

						.setDescription("New Xanitizer Findings")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();

		BLOCKER_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "Blocker", "Xanitizer Blocker Findings",
						Metric.ValueType.INT)

								.setDescription("Xanitizer Blocker Findings")

								.setQualitative(true)

								.setBestValue(0.0)

								.setDirection(Metric.DIRECTION_WORST)

								.setDomain(DOMAIN)

								.create();

		CRITICAL_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "Critical", "Xanitizer Critical Findings",
						Metric.ValueType.INT)

								.setDescription("Xanitizer Critical Findings")

								.setQualitative(true)

								.setBestValue(0.0)

								.setDirection(Metric.DIRECTION_WORST)

								.setDomain(DOMAIN)

								.create();

		MAJOR_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "Major", "Xanitizer Major Findings", Metric.ValueType.INT)

						.setDescription("Xanitizer Major Findings")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();

		MINOR_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "Minor", "Xanitizer Minor Findings", Metric.ValueType.INT)

						.setDescription("Xanitizer Minor Findings")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();

		INFO_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "Info", "Xanitizer Info Findings", Metric.ValueType.INT)

						.setDescription("Xanitizer Info Findings")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();
		
		SPOTBUGS_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "SpotBugs", "Xanitizer SpotBugs Findings", Metric.ValueType.INT)

						.setDescription("Xanitizer findings that were detected by Spotbugs")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();
		
		DEPENDENCY_CHECK_FINDINGS_METRIC =

				new Metric.Builder(PFIX + "DependencyCheck", "Xanitizer Dependency Check Findings", Metric.ValueType.INT)

						.setDescription("Xanitizer findings that were detected by OWASP DEPENDENCY CHECK")

						.setQualitative(true)

						.setBestValue(0.0)

						.setDirection(Metric.DIRECTION_WORST)

						.setDomain(DOMAIN)

						.create();
	}

	private final List<Metric> metrics;

	/**
	 * Create all metrics
	 */
	public XanitizerMetrics() {
		metrics = new ArrayList<>();
		metrics.add(ALL_XAN_FINDINGS_METRIC);
		metrics.add(NEW_FINDINGS_METRIC);
		metrics.add(BLOCKER_FINDINGS_METRIC);
		metrics.add(CRITICAL_FINDINGS_METRIC);
		metrics.add(MAJOR_FINDINGS_METRIC);
		metrics.add(MINOR_FINDINGS_METRIC);
		metrics.add(INFO_FINDINGS_METRIC);
		metrics.add(SPOTBUGS_FINDINGS_METRIC);
		metrics.add(DEPENDENCY_CHECK_FINDINGS_METRIC);

		for (final GeneratedProblemType problemType : GeneratedProblemType.values()) {
			final Metric<Serializable> metricOrNull = mkMetricForProblemType(problemType);
			if (metricOrNull != null) {
				metrics.add(metricOrNull);
			}
		}
	}

	@Override
	public List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * Creates a metric object for a given Xanitizer problem type
	 * 
	 * @param problemType
	 * @return
	 */
	public static Metric<Serializable> mkMetricForProblemType(final GeneratedProblemType problemType) {
		
		if (problemType == null) {
			return null;
		}

		/*
		 * We use mainly numeric ids in order to avoid long ids - SonarQube just
		 * dies when an overly long id is used...
		 */
		final String metricId = PFIX + problemType.name();

		return new Metric.Builder(metricId, mkMetricName(problemType), Metric.ValueType.INT)

				.setDescription(
						"Xanitizer Findings for '" + problemType.getPresentationName() + "'")

				.setQualitative(false)

				.setBestValue(0.0)

				.setDirection(Metric.DIRECTION_WORST)

				.setDomain(DOMAIN)

				.create();
	}

	private static String mkMetricName(final GeneratedProblemType problemType) {
		String candidate = "Xanitizer Findings for " + problemType.getPresentationName();
		if (candidate.length() > METRIC_NAME_LIMIT) {
			candidate = candidate.substring(0, METRIC_NAME_LIMIT - 3) + "...";
		}
		return candidate;
	}

	/**
	 * Returns the metric object counting all Xanitizer findings
	 * 
	 * @return
	 */
	public static Metric<Serializable> getMetricForAllXanFindings() {
		return ALL_XAN_FINDINGS_METRIC;
	}

	/**
	 * Returns the metric object counting all new Xanitizer findings
	 * 
	 * @return
	 */
	public static Metric<Serializable> getMetricForNewXanFindings() {
		return NEW_FINDINGS_METRIC;
	}
	
	public static Metric<Serializable> getMetricForSpotBugsFindings() {
		return SPOTBUGS_FINDINGS_METRIC;
	}
	
	public static Metric<Serializable> getMetricForDependencyCheckFindings() {
		return DEPENDENCY_CHECK_FINDINGS_METRIC;
	}

	/**
	 * Returns the metric object counting the Xanitizer findings of the given
	 * severity
	 * 
	 * @param severity
	 * @return
	 */
	public static Metric<Serializable> getMetricForSeverity(final Severity severity) {

		switch (severity) {
		case BLOCKER:
			return BLOCKER_FINDINGS_METRIC;
		case CRITICAL:
			return CRITICAL_FINDINGS_METRIC;
		case MAJOR:
			return MAJOR_FINDINGS_METRIC;
		case MINOR:
			return MINOR_FINDINGS_METRIC;
		case INFO:
			return INFO_FINDINGS_METRIC;
		default:
			return null;
		}
	}
}
