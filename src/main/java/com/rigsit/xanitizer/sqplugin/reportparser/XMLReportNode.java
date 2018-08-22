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
 * Created on May 31, 2016
 */
package com.rigsit.xanitizer.sqplugin.reportparser;

/**
 * @author rust
 *
 */
public class XMLReportNode {

	private final String classFQNOrNull;
	private final int lineNoOrMinus1;
	private final String relativePathOrNull;

	/**
	 * The representation of a single node of a taint path
	 * 
	 * @param classFQNOrNull
	 * @param lineNoOrMinus1
	 * @param relativePathOrNull
	 */
	public XMLReportNode(final String classFQNOrNull, final int lineNoOrMinus1, final String relativePathOrNull) {
		this.classFQNOrNull = classFQNOrNull != null && classFQNOrNull.isEmpty() ? null : classFQNOrNull;
		this.lineNoOrMinus1 = lineNoOrMinus1;
		this.relativePathOrNull = relativePathOrNull != null && relativePathOrNull.isEmpty() ? null
				: relativePathOrNull;
	}

	public String getClassFQNOrNull() {
		return classFQNOrNull;
	}

	public int getLineNoOrMinus1() {
		return lineNoOrMinus1;
	}

	public String getRelativePathOrNull() {
		return relativePathOrNull;
	}

	@Override
	public String toString() {
		return "Class: " + classFQNOrNull + " Relative Path: " + relativePathOrNull + " Line: " + lineNoOrMinus1;
	}
}
