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

import java.util.ArrayList;
import java.util.List;

/**
 * @author rust
 *
 */
public class XMLReportContent {

	private String m_ToolVersionOrNull;
	private String m_ToolVersionShortOrNull;

	private long m_AnalysisEndDate;

	private List<XMLReportFinding> m_XMLReportFindings = new ArrayList<>();

	public XMLReportContent() {
	}

	public void setToolVersion(final String version) {
		m_ToolVersionOrNull = version;
	}

	public String getToolVersionOrNull() {
		return m_ToolVersionOrNull;
	}

	public void setToolVersionShort(final String versionShort) {
		m_ToolVersionShortOrNull = versionShort;
	}

	public String getToolVersionShortOrNull() {
		return m_ToolVersionShortOrNull;
	}

	public void add(final XMLReportFinding f) {
		m_XMLReportFindings.add(f);
	}

	public List<XMLReportFinding> getXMLReportFindings() {
		return m_XMLReportFindings;
	}

	public void setAnalysisEndDate(final long l) {
		m_AnalysisEndDate = l;
	}

	public long getAnalysisEndDate() {
		return m_AnalysisEndDate;
	}

}
