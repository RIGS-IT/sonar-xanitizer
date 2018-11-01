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

import com.rigsit.xanitizer.sqplugin.metrics.GeneratedProblemType;

/**
 * 
 * The representation of a Xanitizer finding
 * 
 * @author rust
 *
 */
public class XMLReportFinding {

	private static final String RESOURCE_LEAK_LABEL = "Resource Leak";

	private final int findingID;
	private final String problemTypeId;
	private final String problemTypeName;
	private final GeneratedProblemType problemTypeOrNull;
	private final FindingKind findingKind;

	private final String classificationOrNull;
	private final double rating;
	private final String matchCode;
	private final String producer;

	private XMLReportNode nodeOrNull;
	private XMLReportNode startNodeOfPathOrNull;
	private XMLReportNode endNodeOfPathOrNull;

	/**
	 * 
	 * @param findingID
	 * @param problemType
	 * @param findingKind
	 * @param classificationOrNull
	 * @param rating
	 * @param matchCode
	 * @param producer
	 */
	public XMLReportFinding(final int findingID, final String problemTypeId, final String problemTypeName,
			final FindingKind findingKind, final String classificationOrNull, final double rating,
			final String matchCode, final String producer) {
		this.findingID = findingID;
		this.problemTypeId = problemTypeId;
		this.problemTypeName = problemTypeName;
		this.findingKind = findingKind;

		this.problemTypeOrNull = GeneratedProblemType.getForId(problemTypeId);

		this.classificationOrNull = classificationOrNull;
		this.rating = rating;
		this.matchCode = matchCode;
		this.producer = producer;
	}

	public void setSingleNode(final XMLReportNode node) {
		this.nodeOrNull = node;
	}

	public void setStartAndEnd(final XMLReportNode start, final XMLReportNode end) {
		this.startNodeOfPathOrNull = start;
		this.endNodeOfPathOrNull = end;
	}

	public String getProducer() {
		return producer;
	}

	public int getFindingID() {
		return findingID;
	}

	public String getProblemTypeId() {
		return problemTypeId;
	}
	
	public String getProblemTypeName() {
		return problemTypeName;
	}

	public GeneratedProblemType getProblemTypeOrNull() {
		return problemTypeOrNull;
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

	public boolean isResourceLeakFinding() {
		return problemTypeOrNull != null
				&& problemTypeOrNull.getPresentationName().contains(RESOURCE_LEAK_LABEL);
	}

	public XMLReportNode getLocation() {
		if (findingKind == FindingKind.PATH) {
			if (isResourceLeakFinding()) {
				return startNodeOfPathOrNull;
			}
			return endNodeOfPathOrNull;
		}
		return nodeOrNull;
	}

	public XMLReportNode getSecondaryLocationOrNull() {
		if (findingKind == FindingKind.PATH) {
			if (isResourceLeakFinding()) {
				return endNodeOfPathOrNull;
			}
			return startNodeOfPathOrNull;
		}
		return null;
	}

	public String getSecondaryLocationMessage() {
		if (findingKind == FindingKind.PATH) {
			if (isResourceLeakFinding()) {
				return "Last usage position w/o being closed";
			}
			return "Corresponding Taint Source";
		}
		return null;
	}

	public boolean isSpotBugsFinding() {
		return "PlugIn:Findbugs".equals(producer) || "PlugIn:SpotBugs".equals(producer);
	}

	public boolean isDependencyCheckFinding() {
		return "PlugIn:OWASPDependencyCheck".equals(producer);
	}

}
