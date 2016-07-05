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

/**
 * @author rust
 *
 */
public class XMLReportFinding {

	private final int m_FindingID;
	private final String m_ProblemTypeId;
	private final FindingKind m_FindingKind;
	private final String m_FindingProducer;
	private final int m_LineNoOrMinus1;

	private final String m_DescriptionOrNull;
	private final String m_ExtraDescriptionOrNull;

	private final String m_ClassFQNOrNull;
	private final String m_OriginalAbsFileOrNull;
	private final String m_ClassificationOrNull;
	private final double m_Rating;
	private final String m_MatchCode;
	private final String m_PersistenceStringOrNull;

	private final XMLReportNode m_NodeOrNull;
	private final XMLReportNode m_StartNodeOfPathOrNull;
	private final XMLReportNode m_EndNodeOfPathOrNull;

	public XMLReportFinding(final int findingID, final String problemTypeId,
			final FindingKind findingKind, final String findingProducer, final int lineNoOrMinus1,

			final String descriptionOrNull, final String extraDescriptionOrNull,

			final String classFQNOrNull, final String originalAbsFileOrNull,
			final String classificationOrNull, final double rating, final String matchCode,
			final String persistenceStringOrNull, final XMLReportNode nodeOrNull,
			final XMLReportNode startNodeOfPathOrNull, final XMLReportNode endNodeOfPathOrNull) {
		m_FindingID = findingID;
		m_ProblemTypeId = problemTypeId;
		m_FindingKind = findingKind;
		m_FindingProducer = findingProducer;
		m_LineNoOrMinus1 = lineNoOrMinus1;

		m_DescriptionOrNull = descriptionOrNull;
		m_ExtraDescriptionOrNull = extraDescriptionOrNull;

		m_ClassFQNOrNull = classFQNOrNull;
		m_OriginalAbsFileOrNull = originalAbsFileOrNull;
		m_ClassificationOrNull = classificationOrNull;
		m_Rating = rating;
		m_MatchCode = matchCode;
		m_PersistenceStringOrNull = persistenceStringOrNull;

		m_NodeOrNull = nodeOrNull;
		m_StartNodeOfPathOrNull = startNodeOfPathOrNull;
		m_EndNodeOfPathOrNull = endNodeOfPathOrNull;
	}

	public int getFindingID() {
		return m_FindingID;
	}

	public String getProblemTypeId() {
		return m_ProblemTypeId;
	}

	public FindingKind getFindingKind() {
		return m_FindingKind;
	}

	public String getFindingProducer() {
		return m_FindingProducer;
	}

	public int getLineNoOrMinus1() {
		return m_LineNoOrMinus1;
	}

	public String getDescriptionOrNull() {
		return m_DescriptionOrNull;
	}

	public String getExtraDescriptionOrNull() {
		return m_ExtraDescriptionOrNull;
	}

	public String getClassFQNOrNull() {
		return m_ClassFQNOrNull;
	}

	public String getOriginalAbsFileOrNull() {
		return m_OriginalAbsFileOrNull;
	}

	public String getFindingClassificationOrNull() {
		return m_ClassificationOrNull;
	}

	public double getRating() {
		return m_Rating;
	}

	public String getMatchCode() {
		return m_MatchCode;
	}

	public String getPersistenceStringOrNull() {
		return m_PersistenceStringOrNull;
	}

	public XMLReportNode getNodeOrNull() {
		return m_NodeOrNull;
	}

	public XMLReportNode getStartNodeOfPathOrNull() {
		return m_StartNodeOfPathOrNull;
	}

	public XMLReportNode getEndNodeOfPathOrNull() {
		return m_EndNodeOfPathOrNull;
	}

}
