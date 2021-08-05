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
 * Created on May 30, 2016
 */
package com.rigsit.xanitizer.sqplugin.reportparser;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * @author rust
 *
 */
public enum FindingKind {
	PATH, SPECIAL, USER, GENERIC, NON_TAINTED, SANITIZER, OTHER, PROTOCOL_CHECK

	;

	private static final Logger LOG = Loggers.get(FindingKind.class);

	public static FindingKind mk(final String s) {
		if (s == null) {
			return null;
		}
		try {
			return FindingKind.valueOf(s);
		} catch (final IllegalArgumentException | NullPointerException ex) {
			LOG.info("Unexpected finding kind: " + s, ex);
			return OTHER;
		}
	}
}
