/** 
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2020 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
 * Created on 10.08.2020
 *
 */
package com.rigsit.xanitizer.sqplugin.util;

import org.sonar.plugins.java.Java;

/**
 * @author norma
 *
 */
public class RepositoryConstants {
	
	public static final String REPOSITORY_KEY_JAVA = "Xanitizer";
	public static final String REPOSITORY_KEY_JAVA_SCRIPT = "Xanitizer_JavaScript";
	public static final String REPOSITORY_KEY_TYPE_SCRIPT = "Xanitizer_TypeScript";
	public static final String LANGUAGE_KEY_JAVA = Java.KEY;
	public static final String LANGUAGE_KEY_JAVA_SCRIPT = "js";
	public static final String LANGUAGE_KEY_TYPE_SCRIPT = "ts";

	public static final String SPOTBUGS_RULE = "spotbugs-finding";
	public static final String OWASP_DEPENDENCY_CHECK_RULE = "dependency-check-finding";

}
