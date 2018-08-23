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
 * Created on 11.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.rigsit.xanitizer.sqplugin.util.PluginUtil;

/**
 * @author nwe
 *
 */
public class VersionCheckTest {
	
	private static final String WRONG_PATTERN = "XML report file version does not match <number>.<number>[,<number>]";
	
	@Test
	public void testVersionCheckPatternFormat() {
		String result = PluginUtil.checkVersion("", 2, 3, 0);
		assertTrue(result != null && result.startsWith(WRONG_PATTERN));
		
		result = PluginUtil.checkVersion("1", 2, 3, 0);
		assertTrue(result != null && result.startsWith(WRONG_PATTERN));
		
		result = PluginUtil.checkVersion("ttt", 2, 3, 0);
		assertTrue(result != null && result.startsWith(WRONG_PATTERN));
		
		result = PluginUtil.checkVersion("1.0", 2, 3, 0);
		assertTrue(result != null && !result.startsWith(WRONG_PATTERN));
		
		result = PluginUtil.checkVersion("1.0.0", 2, 3, 0);
		assertTrue(result != null && !result.startsWith(WRONG_PATTERN));
		
		result = PluginUtil.checkVersion("1.0.0.0", 2, 3, 0);
		assertTrue(result != null && result.startsWith(WRONG_PATTERN));
	}
	
	@Test
	public void testVersionCheckCorrect() {
		// exact match
		String result = PluginUtil.checkVersion("2.3.3", 2, 3, 3);
		assertNull(result);
		
		// patch higher
		result = PluginUtil.checkVersion("2.3.1", 2, 3, 0);
		assertNull(result);
		
		// minor higher
		result = PluginUtil.checkVersion("2.5.0", 2, 3, 3);
		assertNull(result);
		
		// major higher
		result = PluginUtil.checkVersion("3.0.0", 2, 3, 3);
		assertNull(result);
	}
	
	@Test
	public void testVersionCheckTooLow() {
		// patch too low
		String result = PluginUtil.checkVersion("2.3.2", 2, 3, 3);
		assertTrue(result != null && result.contains("the patch number must be at least"));
		
		// minor too low
		result = PluginUtil.checkVersion("2.2.3", 2, 3, 3);
		assertTrue(result != null && result.contains("the minor version number must be at least"));
		
		// major too low
		result = PluginUtil.checkVersion("1.3.3", 2, 3, 3);
		assertTrue(result != null && result.startsWith("XML report file version: major version number must be at least "));
	}

}
