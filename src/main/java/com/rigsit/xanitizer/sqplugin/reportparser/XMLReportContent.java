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

import java.util.ArrayList;
import java.util.List;

/**
 * @author rust
 *
 */
public class XMLReportContent {

	private String toolVersionOrNull;
	private String toolVersionShortOrNull;

	private long analysisEndDate;

	private List<XMLReportFinding> xmlReportFindings = new ArrayList<>();

	public void setToolVersion(final String version) {
		toolVersionOrNull = version;
	}

	public String getToolVersionOrNull() {
		return toolVersionOrNull;
	}

	public void setToolVersionShort(final String versionShort) {
		toolVersionShortOrNull = versionShort;
	}

	public String getToolVersionShortOrNull() {
		return toolVersionShortOrNull;
	}

	/**
	 * Adds a finding to the content
	 * @param f
	 */
	public void addFinding(final XMLReportFinding f) {
		xmlReportFindings.add(f);
	}

	public List<XMLReportFinding> getXMLReportFindings() {
		return xmlReportFindings;
	}

	public void setAnalysisEndDate(final long l) {
		analysisEndDate = l;
	}

	public long getAnalysisEndDate() {
		return analysisEndDate;
	}

}
