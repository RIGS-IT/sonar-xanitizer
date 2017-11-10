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
 * Created on 11.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sonar.api.batch.rule.Severity;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
import com.rigsit.xanitizer.sqplugin.reportparser.FindingKind;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportFinding;
import com.rigsit.xanitizer.sqplugin.util.SensorUtil;

/**
 * @author nwe
 *
 */
public class SeverityTest {

	@Test
	public void testSeverity() {

		// always blockers
		XMLReportFinding finding = mkFinding("Must Fix", 1);
		Severity severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.BLOCKER, severity);

		finding = mkFinding("Urgent Fix", 1);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.BLOCKER, severity);

		// severity from rating
		finding = mkFinding("Warning", 7.1);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.CRITICAL, severity);

		finding = mkFinding("Warning", 7.0);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.MAJOR, severity);

		finding = mkFinding("Warning", 4.1);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.MAJOR, severity);

		finding = mkFinding("Warning", 4.0);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals( Severity.MINOR, severity);

		finding = mkFinding("Warning", 1.1);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.MINOR, severity);

		finding = mkFinding("Warning", 0.9);
		severity = SensorUtil.mkSeverity(finding);
		assertEquals(Severity.INFO, severity);
	}

	private XMLReportFinding mkFinding(final String classification, final double rating) {
		return new XMLReportFinding(-1, GeneratedProblemType.values()[0], FindingKind.OTHER,
				classification, rating, null, null);
	}
}
