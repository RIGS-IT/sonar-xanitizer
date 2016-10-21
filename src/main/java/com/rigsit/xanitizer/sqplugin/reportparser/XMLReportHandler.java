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
 * Created on 06.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.reportparser;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;
import com.rigsit.xanitizer.sqplugin.util.SensorUtil;

/**
 * Event handler for parsing the XML report file
 * 
 * @author nwe
 *
 */
public class XMLReportHandler extends DefaultHandler {

	private static final Logger LOG = Loggers.get(XMLReportHandler.class);

	private final XMLReportContent xmlReportContent;
	private final StringBuilder collectedCharacters = new StringBuilder();

	private String problemTypeId;
	private int findingId = -1;
	private FindingKind findingKind;
	private String producer;
	private double rating;

	private String classificationOrNull;
	private String matchCode;

	private XMLReportNode nodeOrNull;
	private XMLReportNode startNodeOrNull;
	private XMLReportNode endNodeOrNull;

	/**
	 * 
	 * @param xmlReportContent
	 */
	public XMLReportHandler(final XMLReportContent xmlReportContent) {
		this.xmlReportContent = xmlReportContent;
	}

	@Override
	public void startElement(final String uri, final String localName, final String qName,
			final Attributes attributes) throws SAXException {

		collectedCharacters.setLength(0);

		switch (qName) {
		case "XanitizerFindingsList":
			startFindingsList(attributes);
			break;
		case "node":
			nodeOrNull = mkNodeFromAttributes(attributes);
			break;
		case "startNode":
			startNodeOrNull = mkNodeFromAttributes(attributes);
			break;
		case "endNode":
			endNodeOrNull = mkNodeFromAttributes(attributes);
			break;
		case "finding":
			findingId = Integer.parseInt(attributes.getValue("id"));
			findingKind = FindingKind.mk(attributes.getValue("kind"));
			break;
		default:
			// do nothing
		}
	}

	private void startFindingsList(final Attributes attributes) throws SAXException {
		final String toolVersionShortOrNull = attributes.getValue("xanitizerVersionShort");

		if (toolVersionShortOrNull == null) {
			throw new XMLReportException(
					"No attribute 'xanitizerVersionShort' found in XML report file.");
		}

		final String errMsgOrNull = SensorUtil.checkVersion(toolVersionShortOrNull, 2, 3, -1);
		if (errMsgOrNull != null) {
			throw new XMLReportException(
					"Error parsing attribute 'xanitizerVersionShort' in XML report file: "
							+ errMsgOrNull + ".");
		}

		xmlReportContent.setToolVersion(attributes.getValue("xanitizerVersion"));
		xmlReportContent.setToolVersionShort(toolVersionShortOrNull);

		final String timestamp = attributes.getValue("timeStampLong");
		if (timestamp != null) {
			xmlReportContent.setAnalysisEndDate(Long.parseLong(timestamp));
		}
	}

	private XMLReportNode mkNodeFromAttributes(final Attributes attributes) {
		final int lineNoOrMinus1 = parseInt(attributes.getValue("lineNo"), -1);
		final String classFQNOrNull = attributes.getValue("classFQN");
		final String relativePath = attributes.getValue("relativePath");
		final String absolutePath = attributes.getValue("absolutePath");

		return new XMLReportNode(classFQNOrNull, lineNoOrMinus1, relativePath, absolutePath);
	}

	private int parseInt(final String s, final int dflt) {
		try {
			return Integer.parseInt(s);
		} catch (final NumberFormatException ex) {
			LOG.info("Not a number: " + s, ex);
			return dflt;
		}
	}

	@Override
	public void endElement(final String uri, final String localName, final String qName)
			throws SAXException {
		switch (qName) {
		case "problemTypeId":
			problemTypeId = collectedCharacters.toString();
			break;
		case "classification":
			classificationOrNull = collectedCharacters.toString();
			break;
		case "producer":
			producer = collectedCharacters.toString();
			break;
		case "rating":
			rating = Double.parseDouble(collectedCharacters.toString());
			break;
		case "matchCode":
			matchCode = collectedCharacters.toString();
			break;
		case "finding":
			// Finishing a finding.
			endFinding();
			break;
		default:
			// do nothing
		}
	}

	private void endFinding() {

		collectedCharacters.setLength(0);

		createFinding();
		resetData();
	}

	private void createFinding() {
		// Defensiveness: This condition should always be true.
		if (!hasCollectedAllRelevantData()) {
			LOG.warn(
					"Xanitizer: Could not parse all necessary data. Skipping finding " + findingId);
			return;
		}

		if (shouldSkipFinding()) {
			return;
		}

		final GeneratedProblemType problemType = GeneratedProblemType.getForId(problemTypeId);
		if (problemType == null) {
			LOG.warn("Xanitizer: Unknown problem type '" + problemTypeId + "'. Skipping finding "
					+ findingId);
			return;
		}

		final XMLReportFinding f;
		if (findingKind == FindingKind.PATH) {
			f = new XMLReportFinding(findingId, problemType, findingKind, classificationOrNull,
					rating, matchCode, startNodeOrNull, endNodeOrNull);
		} else {
			f = new XMLReportFinding(findingId, problemType, findingKind, classificationOrNull,
					rating, matchCode, nodeOrNull);
		}

		xmlReportContent.addFinding(f);
	}

	/**
	 * Skip informational findings and FindBugs and OWASP Dependency Check
	 * Findings.
	 * 
	 * @return
	 */
	private boolean shouldSkipFinding() {
		if ("PlugIn:Findbugs".equals(producer)) {
			LOG.debug("Xanitizer: Skipping finding " + findingId + ": Ignoring Findbugs finding.");
			return true;
		}

		if ("PlugIn:OWASPDependencyCheck".equals(producer)) {
			LOG.debug("Xanitizer: Skipping finding " + findingId
					+ ": Ignoring OWASP Dependency check finding.");
			return true;
		}

		switch (classificationOrNull) {
		case "Information":
		case "Duplicate":
		case "Harmless":
		case "Ignore":
		case "Falsely Reported":
		case "Intended":
		case "Obsolete":
		case "Code Quality":
			LOG.debug("Xanitizer: Skipping finding " + findingId + ": Ignoring harmless finding.");
			return true;
		default:
			return false;
		}
	}

	private boolean hasCollectedAllRelevantData() {
		if (problemTypeId == null || findingId < 0 || findingKind == null || producer == null
				|| matchCode == null) {
			return false;
		}
		if (findingKind == FindingKind.PATH) {
			return startNodeOrNull != null && endNodeOrNull != null;
		}
		return nodeOrNull != null;
	}

	private void resetData() {
		problemTypeId = null;
		classificationOrNull = null;
		findingId = -1;
		findingKind = null;
		problemTypeId = null;
		rating = 0;

		matchCode = null;

		nodeOrNull = null;
		startNodeOrNull = null;
		endNodeOrNull = null;
	}

	@Override
	public void characters(final char[] ch, final int start, final int length) throws SAXException {
		collectedCharacters.append(ch, start, length);
	}
}
