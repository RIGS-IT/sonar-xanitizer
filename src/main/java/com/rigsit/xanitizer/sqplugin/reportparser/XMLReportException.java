/** 
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2019 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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
 * Created on 17.08.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.reportparser;

import org.xml.sax.SAXException;

/**
 * @author nwe
 *
 */
public class XMLReportException extends SAXException {
	
	 // Added serialVersionUID to preserve binary compatibility
    static final long serialVersionUID = 2183271635256073760L;
    
    public XMLReportException(final String message) {
    	super(message);
    }
}
