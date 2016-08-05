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
 * Created on 05.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.SAXException;

import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportContent;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportFinding;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportNode;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportParser;
import com.rigsit.xanitizer.sqplugin.util.SensorUtil;

/**
 * @author nwe
 *
 */
public class ParserTest {

	private static final Logger LOG = Loggers.get(ParserTest.class);

	@Test
	public void testValidFile() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-all.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(733, findings.size());

		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testSanitizers() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-sanitizers.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Warning", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.INFO, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSinks() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-sinks.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Must Fix", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.BLOCKER, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSources() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-sources.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Urgent Fix", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.BLOCKER, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testFindbugs() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-findbugs.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			// FindBugs findings are not created
			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(0, findings.size());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testOWASP() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-owasp.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			// OWASP Findings are not created
			assertEquals(0, findings.size());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSpecialCode() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-specialcode.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Could Improve", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.CRITICAL, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTaintPaths() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-taintpaths.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Needs Further Study", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.MAJOR, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUser() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-user.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.3.0, build no. 84 of 01.07.16", content.getToolVersionOrNull());
			assertEquals("2.3.0", content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			assertEquals(1, findings.size());
			final XMLReportFinding finding = findings.get(0);
			assertEquals("Warning", finding.getFindingClassificationOrNull());
			final Severity severity = SensorUtil.mkSeverity(finding);
			assertEquals(Severity.CRITICAL, severity);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnsupportedVersion() {
		final XMLReportParser parser = new XMLReportParser();
		final File reportFile = new File(
				getClass().getResource("/webgoat-Findings-List-oldversion.xml").getFile());
		try {
			final XMLReportContent content = parser.parse(reportFile);

			assertEquals("version 2.2.0, build no. 81 of 04.05.16", content.getToolVersionOrNull());
			assertNull(content.getToolVersionShortOrNull());

			final List<XMLReportFinding> findings = content.getXMLReportFindings();
			// all findings are skipped because finding kind is not exported in
			// this report version
			assertEquals(0, findings.size());

		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error parsing report file", e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testReportNodeEmpty() {
		final XMLReportNode node = new XMLReportNode("", -1, "");
		assertNull(node.getClassFQNOrNull());
		assertEquals(-1, node.getLineNoOrMinus1());
		assertNull(node.getXFilePersistenceOrNull());
	}
}
