/** 
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2021 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
 * Created on 11.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.rule.Severity;

import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportFinding;
import com.rigsit.xanitizer.sqplugin.reportparser.XMLReportNode;

/**
 * @author nwe
 *
 */
public class PluginUtil {

	private static final Pattern TOOL_VERSION_PATTERN = Pattern
			.compile("([0-9]+)[.]([0-9]+)(?:[.]([0-9]+))?");

	private PluginUtil() {
		// hide constructor
	}

	/**
	 * Formats a date
	 * 
	 * @param date
	 * @return
	 */
	public static String convertToDateWithTimeString(Date date) {
		final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static boolean isFileName(final String fileString) {
		return fileString.contains(".") && !fileString.contains(File.separator);
	}

	public static File searchRecursivlyInDir(final File dir, final String filename) {
		if (!dir.exists()) {
			return null;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (file.getName().equals(filename)) {
					return file;
				}
			} else {
				final File result = searchRecursivlyInDir(file, filename);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Checks if the given version string represents a version that matches or
	 * is higher than the given major.minor.patch values
	 * 
	 * @param shortToolVersion
	 * @param majorNeeded
	 * @param minorNeeded
	 * @param patchNeeded
	 * @return An error message or null if the version string is okay
	 */
	public static String checkVersion(final String shortToolVersion, final int majorNeeded,
			final int minorNeeded, final int patchNeeded) {

		if ("nightlyBuild".equals(shortToolVersion)) {
			// special case for nightly build version
			return null;
		}

		final Matcher m = TOOL_VERSION_PATTERN.matcher(shortToolVersion);
		if (!m.matches()) {
			return "XML report file version does not match <number>.<number>[,<number>]: '"
					+ shortToolVersion + "'";
		}
		final int majorNo = Integer.parseInt(m.group(1));
		final int minorNo = Integer.parseInt(m.group(2));

		final int patchNo;
		final String patchNoString = m.group(3);
		if (patchNoString == null) {
			// Does not exist.
			patchNo = -1;
		} else {
			patchNo = Integer.parseInt(patchNoString);
		}

		if (majorNo < majorNeeded) {
			return "XML report file version: major version number must be at least " + majorNeeded
					+ ": '" + shortToolVersion + "'";
		}

		if (majorNo == majorNeeded) {
			if (minorNo < minorNeeded) {
				return "XML report file version: when the major version is " + majorNo
						+ ", the minor version number must be at least " + minorNeeded + ": '"
						+ shortToolVersion + "'";
			}

			if (minorNo == minorNeeded && patchNo < patchNeeded) {
				return "XML report file version: when the major and minor version is " + majorNo
						+ "." + minorNo + ", the patch number must be at least " + patchNeeded
						+ ": '" + shortToolVersion + "'";
			}
		}

		// Fine.
		return null;
	}

	/**
	 * Returns the severity of a SonarQube issue for the given Xanitizer finding
	 * 
	 * @param xanFinding
	 * @return
	 */
	public static Severity mkSeverity(final XMLReportFinding xanFinding) {
		final String findingClassification = xanFinding.getFindingClassificationOrNull();
		if (findingClassification == null) {
			// Should not occur.
			return Severity.MINOR;
		}
		switch (findingClassification.toUpperCase()) {
		case "MUST FIX":
		case "URGENT FIX":
			return Severity.BLOCKER;

		default:
			return mkSeverityFromRating(xanFinding);
		}
	}

	private static Severity mkSeverityFromRating(final XMLReportFinding xanFinding) {
		// For the rest, we use the rating.
		final double rating = xanFinding.getRating();
		if (rating > 7) {
			return Severity.CRITICAL;
		}
		if (rating > 4) {
			return Severity.MAJOR;
		}
		if (rating > 1) {
			return Severity.MINOR;
		}
		return Severity.INFO;
	}

	public static String getRepositoryKeyForFinding(final XMLReportFinding finding) {
		final XMLReportNode location = finding.getLocation();
		if (location != null && location.getRelativePathOrNull() != null) {
			final String extension = getFileNameExtensionOrEmpty(location.getRelativePathOrNull());
			switch (extension) {
			case RepositoryConstants.LANGUAGE_KEY_JAVA_SCRIPT:
				return RepositoryConstants.REPOSITORY_KEY_JAVA_SCRIPT;
			case RepositoryConstants.LANGUAGE_KEY_TYPE_SCRIPT:
				return RepositoryConstants.REPOSITORY_KEY_TYPE_SCRIPT;
			default:
				return RepositoryConstants.REPOSITORY_KEY_JAVA;
			}
		}
		return RepositoryConstants.REPOSITORY_KEY_JAVA;
	}

	public static String getFileNameExtensionOrEmpty(final String fileName) {
		final int idx = fileName.lastIndexOf('.');
		if (idx == -1)
			return "";
		return fileName.substring(idx + 1);
	}
}
