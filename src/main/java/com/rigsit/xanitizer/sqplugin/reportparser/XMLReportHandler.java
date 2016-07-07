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

/**
 * @author nwe
 *
 */
public class XMLReportHandler extends DefaultHandler {

	private static final Logger LOG = Loggers.get(XMLReportHandler.class);

	private final XMLReportContent xmlReportContent;
	private final StringBuilder collectedCharacters = new StringBuilder();

	private String problemTypeId;
	private String clazz;
	private String pckgPath;
	private int findingId = -1;
	private FindingKind findingKind;
	private String producer;
	private int lineNo = -1;
	private double rating;

	private String descriptionOrNull;
	private String extraDescriptionOrNull;

	private String originalAbsFileOrNull;
	private String classificationOrNull;
	private String matchCode;
	private String persistenceOrNull;

	private XMLReportNode nodeOrNull;
	private XMLReportNode startNodeOrNull;
	private XMLReportNode endNodeOrNull;

	public XMLReportHandler(final XMLReportContent xmlReportContent) {
		this.xmlReportContent = xmlReportContent;
	}

	@Override
	public void startElement(final String uri, final String localName, final String qName,
			final Attributes attributes) throws SAXException {

		collectedCharacters.setLength(0);

		switch (qName) {
		case "XanitizerFindingsList":
			xmlReportContent.setToolVersion(attributes.getValue("xanitizerVersion"));
			xmlReportContent.setToolVersionShort(attributes.getValue("xanitizerVersionShort"));
			xmlReportContent
					.setAnalysisEndDate(Long.parseLong(attributes.getValue("timeStampLong")));
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

	private XMLReportNode mkNodeFromAttributes(final Attributes attributes) {
		final int lineNoOrMinus1 = parseInt(attributes.getValue("lineNo"), -1);
		final String classFQNOrEmpty = attributes.getValue("classFQN");
		final String xFilePersistence = attributes.getValue("xFilePersistence");

		return new XMLReportNode(classFQNOrEmpty, lineNoOrMinus1, xFilePersistence);
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

		case "class":
			clazz = collectedCharacters.toString();
			break;

		case "package":
			pckgPath = collectedCharacters.toString();
			break;

		case "classification":
			classificationOrNull = collectedCharacters.toString();
			break;

		case "producer":
			producer = collectedCharacters.toString();
			break;

		case "line":
			// The line number might contain separator commas.
			lineNo = Integer.parseInt(collectedCharacters.toString().replace(",", ""));
			break;

		case "rating":
			rating = Double.parseDouble(collectedCharacters.toString());
			break;

		case "description":
			descriptionOrNull = collectedCharacters.toString();
			break;

		case "extraDescription":
			extraDescriptionOrNull = collectedCharacters.toString();
			break;

		case "originalAbsFile":
			extraDescriptionOrNull = collectedCharacters.toString();
			break;

		case "matchCode":
			matchCode = collectedCharacters.toString();
			break;

		case "xFilePersistence":
			persistenceOrNull = collectedCharacters.toString();
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

		// Defensiveness: This condition should always be true.
		if (hasCollectedAllRelevantData()) {

			// Skip informational findings.
			if (!"Information".equals(classificationOrNull)) {

				final XMLReportFinding f = new XMLReportFinding(findingId, problemTypeId,
						findingKind, producer, lineNo, descriptionOrNull, extraDescriptionOrNull,
						mkClassFQNOrNull(pckgPath, clazz), originalAbsFileOrNull,
						classificationOrNull, rating, matchCode, persistenceOrNull, nodeOrNull,
						startNodeOrNull, endNodeOrNull);

				xmlReportContent.add(f);
			}
		} else {
			LOG.error("Xanitizer: Skipping finding " + findingId + ": " + problemTypeId);
		}

		resetData();
	}

	private boolean hasCollectedAllRelevantData() {
		return problemTypeId != null && findingId >= 0 && findingKind != null && producer != null
				&& matchCode != null;
	}

	private void resetData() {
		problemTypeId = null;
		clazz = null;
		pckgPath = null;
		classificationOrNull = null;
		findingId = -1;
		findingKind = null;
		problemTypeId = null;
		lineNo = -1;
		rating = 0;

		descriptionOrNull = null;
		extraDescriptionOrNull = null;

		originalAbsFileOrNull = null;
		matchCode = null;
		persistenceOrNull = null;

		nodeOrNull = null;
		startNodeOrNull = null;
		endNodeOrNull = null;
	}

	private String mkClassFQNOrNull(final String pckgPathOrNull, final String clazzName) {
		if (clazzName == null) {
			return null;
		}

		if (pckgPathOrNull == null || pckgPathOrNull.isEmpty()) {
			return clazzName;
		}
		return pckgPathOrNull + "." + clazzName;
	}

	@Override
	public void characters(final char[] ch, final int start, final int length) throws SAXException {
		collectedCharacters.append(ch, start, length);
	}
}