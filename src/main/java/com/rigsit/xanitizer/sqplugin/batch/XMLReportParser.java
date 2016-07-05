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
 * Created on May 30, 2016
 */
package com.rigsit.xanitizer.sqplugin.batch;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author rust
 *
 */
public class XMLReportParser {
	private final static Logger LOG = Loggers.get(XMLReportParser.class);

	// Debugging code.
	public static void main(final String[] args) throws Exception {
		final XMLReportContent content = new XMLReportParser().parse(
				new File("/home/rust/ttt-webgoatTest-Findings-List.xml"),
				true /* onlyProblemFindings */);

		int xxx = 0;
		++xxx;
	}

	public XMLReportParser() {
	}

	public XMLReportContent parse(final File f, final boolean onlyProblemFindings)
			throws Exception {

		final XMLReportContent xmlReportContent = new XMLReportContent();

		/*
		 * Create a SAX handler that collects and registers the needed data from
		 * the findings file.
		 */
		final DefaultHandler handler = new DefaultHandler() {

			private final StringBuilder m_CollectedCharacters = new StringBuilder();

			private String m_ProblemTypeId;
			private String m_Class;
			private String m_PckgPath;
			private int m_FindingId = -1;
			private FindingKind m_FindingKind;
			private String m_Producer;
			private int m_LineNo = -1;
			private double m_Rating;

			private String m_DescriptionOrNull;
			private String m_ExtraDescriptionOrNull;

			private String m_OriginalAbsFileOrNull;
			private String m_ClassificationOrNull;
			private String m_MatchCode;
			private String m_PersistenceOrNull;

			private XMLReportNode m_NodeOrNull;
			private XMLReportNode m_StartNodeOrNull;
			private XMLReportNode m_EndNodeOrNull;

			@Override
			public void startElement(final String uri, final String localName, final String qName,
					final Attributes attributes) throws SAXException {

				m_CollectedCharacters.setLength(0);

				switch (qName) {
				case "XanitizerFindingsList":

					xmlReportContent.setToolVersion(attributes.getValue("xanitizerVersion"));

					final String versionShort = attributes.getValue("xanitizerVersionShort");
					xmlReportContent.setToolVersionShort(versionShort);

					final String timeStampLongAsString = attributes.getValue("timeStampLong");

					xmlReportContent.setAnalysisEndDate(Long.parseLong(timeStampLongAsString));

					break;

				case "node":
					m_NodeOrNull = mkNodeFromAttributes(attributes);
					break;
				case "startNode":
					m_StartNodeOrNull = mkNodeFromAttributes(attributes);
					break;
				case "endNode":
					m_EndNodeOrNull = mkNodeFromAttributes(attributes);
					break;

				case "finding":

					m_FindingId = Integer.parseInt(attributes.getValue("id"));
					m_FindingKind = FindingKind.mk(attributes.getValue("kind"));
					break;

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
				} catch (final Exception ex) {
					return dflt;
				}
			}

			@Override
			public void endElement(final String uri, final String localName, final String qName)
					throws SAXException {
				switch (qName) {

				case "problemTypeId":
					m_ProblemTypeId = m_CollectedCharacters.toString();
					break;

				case "class":
					m_Class = m_CollectedCharacters.toString();
					break;

				case "package":
					m_PckgPath = m_CollectedCharacters.toString();
					break;

				case "classification":
					m_ClassificationOrNull = m_CollectedCharacters.toString();
					break;

				case "producer":
					m_Producer = m_CollectedCharacters.toString();
					break;

				case "line":
					// The line number might contain separator commas.
					m_LineNo = Integer.parseInt(m_CollectedCharacters.toString().replace(",", ""));
					break;

				case "rating":
					m_Rating = Double.parseDouble(m_CollectedCharacters.toString());
					break;

				case "description":
					m_DescriptionOrNull = m_CollectedCharacters.toString();
					break;

				case "extraDescription":
					m_ExtraDescriptionOrNull = m_CollectedCharacters.toString();
					break;

				case "originalAbsFile":
					m_ExtraDescriptionOrNull = m_CollectedCharacters.toString();
					break;

				case "matchCode":
					m_MatchCode = m_CollectedCharacters.toString();
					break;

				case "xFilePersistence":
					m_PersistenceOrNull = m_CollectedCharacters.toString();
					break;

				case "finding":
					// Finishing a finding.
					m_CollectedCharacters.setLength(0);

					// Defensiveness: This condition should always be true.
					if (m_ProblemTypeId != null && m_FindingId >= 0 && m_FindingKind != null
							&& m_Producer != null && m_MatchCode != null) {

						// Skip informational findings.
						if (!onlyProblemFindings || !m_ClassificationOrNull.equals("Information")) {

							final XMLReportFinding f = new XMLReportFinding(m_FindingId,
									m_ProblemTypeId, m_FindingKind, m_Producer, m_LineNo,
									m_DescriptionOrNull, m_ExtraDescriptionOrNull,
									mkClassFQNOrNull(m_PckgPath, m_Class), m_OriginalAbsFileOrNull,
									m_ClassificationOrNull, m_Rating, m_MatchCode,
									m_PersistenceOrNull, m_NodeOrNull, m_StartNodeOrNull,
									m_EndNodeOrNull);

							xmlReportContent.add(f);
						}
					} else {
						LOG.error("Xanitizer: Skipping finding " + m_FindingId + ": "
								+ m_ProblemTypeId);
					}

					m_ProblemTypeId = null;
					m_Class = null;
					m_PckgPath = null;
					m_ClassificationOrNull = null;
					m_FindingId = -1;
					m_FindingKind = null;
					m_ProblemTypeId = null;
					m_LineNo = -1;
					m_Rating = 0;

					m_DescriptionOrNull = null;
					m_ExtraDescriptionOrNull = null;

					m_OriginalAbsFileOrNull = null;
					m_MatchCode = null;
					m_PersistenceOrNull = null;

					m_NodeOrNull = null;
					m_StartNodeOrNull = null;
					m_EndNodeOrNull = null;
					break;
				}
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
			public void characters(final char ch[], final int start, final int length)
					throws SAXException {
				m_CollectedCharacters.append(ch, start, length);
			}
		};

		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		final SAXParser saxParser = factory.newSAXParser();

		saxParser.parse(f, handler);

		return xmlReportContent;
	}

}
