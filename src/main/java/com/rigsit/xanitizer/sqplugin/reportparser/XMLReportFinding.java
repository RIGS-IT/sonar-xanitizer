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
package com.rigsit.xanitizer.sqplugin.reportparser;

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;

/**
 * 
 * The representation of a Xanitizer finding
 * 
 * @author rust
 *
 */
public class XMLReportFinding {
	
	private static String RESOURCE_LEAK_LABEL = "Resource Leak";

	private final int findingID;
	private final GeneratedProblemType problemType;
	private final FindingKind findingKind;

	private final String classificationOrNull;
	private final double rating;
	private final String matchCode;

	private final XMLReportNode nodeOrNull;
	private final XMLReportNode startNodeOfPathOrNull;
	private final XMLReportNode endNodeOfPathOrNull;

	/**
	 * 
	 * @param findingID
	 * @param problemType
	 * @param findingKind
	 * @param classificationOrNull
	 * @param rating
	 * @param matchCode
	 * @param node
	 */
	public XMLReportFinding(final int findingID, final GeneratedProblemType problemType,
			final FindingKind findingKind, final String classificationOrNull, final double rating,
			final String matchCode, final XMLReportNode node) {
		this.findingID = findingID;
		this.problemType = problemType;
		this.findingKind = findingKind;

		this.classificationOrNull = classificationOrNull;
		this.rating = rating;
		this.matchCode = matchCode;

		this.nodeOrNull = node;
		this.startNodeOfPathOrNull = null;
		this.endNodeOfPathOrNull = null;
	}

	/**
	 * 
	 * @param findingID
	 * @param problemType
	 * @param findingKind
	 * @param classificationOrNull
	 * @param rating
	 * @param matchCode
	 * @param startNodeOfPathOrNull
	 * @param endNodeOfPathOrNull
	 */
	public XMLReportFinding(final int findingID, final GeneratedProblemType problemType,
			final FindingKind findingKind, final String classificationOrNull, final double rating,
			final String matchCode, final XMLReportNode startNodeOfPath,
			final XMLReportNode endNodeOfPath) {
		this.findingID = findingID;
		this.problemType = problemType;
		this.findingKind = findingKind;

		this.classificationOrNull = classificationOrNull;
		this.rating = rating;
		this.matchCode = matchCode;

		this.nodeOrNull = null;
		this.startNodeOfPathOrNull = startNodeOfPath;
		this.endNodeOfPathOrNull = endNodeOfPath;
	}

	public int getFindingID() {
		return findingID;
	}

	public GeneratedProblemType getProblemType() {
		return problemType;
	}

	public FindingKind getFindingKind() {
		return findingKind;
	}

	public String getFindingClassificationOrNull() {
		return classificationOrNull;
	}

	public double getRating() {
		return rating;
	}

	public String getMatchCode() {
		return matchCode;
	}

	public XMLReportNode getLocation() {
		if (findingKind == FindingKind.PATH) {
			if (problemType.getPresentationName().contains(RESOURCE_LEAK_LABEL)) {
				return startNodeOfPathOrNull;
			}
			return endNodeOfPathOrNull;
		}
		return nodeOrNull;
	}

	public XMLReportNode getSecondaryLocationOrNull() {
		if (findingKind == FindingKind.PATH) {
			if (problemType.getPresentationName().contains(RESOURCE_LEAK_LABEL)) {
				return endNodeOfPathOrNull;
			}
			return startNodeOfPathOrNull;
		}
		return null;
	}
	
	public String getSecondaryLocationMessage() {
		if (findingKind == FindingKind.PATH) {
			if (problemType.getPresentationName().contains(RESOURCE_LEAK_LABEL)) {
				return "Last usage position w/o being closed";
			}
			return "Corresponding Taint Source";
		}
		return null;
	}

}
