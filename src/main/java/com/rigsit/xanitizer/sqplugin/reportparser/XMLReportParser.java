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
 * Created on May 30, 2016
 */
package com.rigsit.xanitizer.sqplugin.reportparser;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.SAXException;

/**
 * @author rust
 *
 */
public class XMLReportParser {
	
	private static final Logger LOG = Loggers.get(XMLReportContent.class);

	/**
	 * Parses the given XML report file and stores the extracted information in
	 * the returned report content
	 * 
	 * @param reportFile
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public XMLReportContent parse(final File reportFile)
			throws SAXException, IOException, ParserConfigurationException {

		final XMLReportContent xmlReportContent = new XMLReportContent();

		/*
		 * Create a SAX handler that collects and registers the needed data from
		 * the findings file.
		 */
		final XMLReportHandler handler = new XMLReportHandler(xmlReportContent);

		final SAXParserFactory factory = SAXParserFactory.newInstance();
		
		/*
		 * Could cause a ParserConfigurationException for certain implementations of SAXParserFactory
		 */
		try {
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		} catch (Exception e) {
			LOG.info("Could not deactivate secure XML processing.");
		}
		final SAXParser saxParser = factory.newSAXParser();

		saxParser.parse(reportFile, handler);

		return xmlReportContent;
	}

}
