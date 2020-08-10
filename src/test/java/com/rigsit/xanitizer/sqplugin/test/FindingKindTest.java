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
 * Created on 24.08.2017
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.rigsit.xanitizer.sqplugin.reportparser.FindingKind;

/**
 * @author nwe
 *
 */
public class FindingKindTest {

	
	@Test
	public void testUnknownKind() {
		
		final FindingKind kind = FindingKind.mk("unknown");
		assertSame(FindingKind.OTHER, kind);
	}
	
	@Test
	public void testNull() {
		
		final FindingKind kind = FindingKind.mk(null);
		assertNull(kind);
	}
	
	@Test
	public void testRealKinds() {
		
		for (FindingKind value : FindingKind.values()) {
			final FindingKind kind = FindingKind.mk(value.toString());
			assertSame(kind, value);
		}
		
	}
}
