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
* Created on October 2, 2015
*/
package com.rigsit.xanitizer.sqplugin.metrics;

/**
 *
 * Generated from the Xanitizer standard problem definitions.
 * Contains all Xanitizer problem types together with their description.
 *
 * @author nwe
 *
 */
public enum GeneratedProblemType {

XANITIZER_PROBLEM_TYPE_1("USER:GeneralCodeIssue", "General Code Issue", "This problem type serves as placeholder to assign it to user-defined findings, \n			which are not related to any other problem type. This could be for example quality issues in the code.", "Fix issue.", -1, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_3("LiteralStringsInVariablesSearch:password", "Hard-Coded Credentials: Password in Java Variables", "Checks for password variables to which constant strings are assigned.\n\nSee also CWE-259", "Remove credentials from the code.", 259, 2, 7, "java"),

XANITIZER_PROBLEM_TYPE_4("LiteralStringsInVariablesSearch:username", "Hard-Coded Credentials: Username in Java Variables", "Checks for user name variables to which constant strings are assigned.\n\nSee also CWE-798", "Remove credentials from the code.", 798, 2, 7, "java"),

XANITIZER_PROBLEM_TYPE_56("protocolCheckProblemType:OnlyAllowedCryptoAlgorithms", "Cryptography: Check that only allowed crypto algorithms are used", "Uses of non-allowed crypto algorithms.\n\nSee also CWE-327", "Use one of the allowed crypto algorithms instead.", 327, 3, 19, "java"),

XANITIZER_PROBLEM_TYPE_66("protocolCheckProblemType:OnlyAllowedHashAlgorithms", "Cryptography: Check that only allowed hash algorithms are used", "Uses of non-allowed hash algorithms.\n\nSee also CWE-328", "Use one of the allowed hash algorithms instead.", 328, 3, 19, "java"),

XANITIZER_PROBLEM_TYPE_57("protocolCheckProblemType:AES_Decryption", "Cryptography: AES Decryption", "AES problems concerning decryption.  \n			\n			AES is a recommended algorithm for symmetric encryption and decryption.\n			Problems in AES decryption include wrong construction of an algorithm parameters,\n			object, wrong construction of a decryption cipher object, and wrong use of a \n			decryption cipher in a cipher input stream.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the AES decryption protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_58("protocolCheckProblemType:AES_Encryption", "Cryptography: AES Encryption", "AES problems concerning encryption.\n			\n			AES is a recommended algorithm for symmetric encryption and decryption.\n			Problems in AES encryption include wrong construction of a new encryption key\n			object, wrong construction of an encryption cipher object, and wrong use of an \n			encryption cipher in a cipher output stream.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the AES encryption protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_59("protocolCheckProblemType:AES_BothEncryptionAndDecryption", "Cryptography: Basic Protocol Violations for AES Encryption and Decryption", "AES problems concerning both encryption and decryption.\n			\n			AES is a recommended algorithm for symmetric encryption and decryption.\n			Problems in AES encryption and decryption include wrong construction of an AES key \n			spec or of an AES key.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the AES protocols that are used both in encryption and decryption.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_60("protocolCheckProblemType:RSA_Decryption", "Cryptography: RSA Decryption", "RSA problems concerning decryption.\n			\n			RSA is a recommended algorithm for asymmetric encryption and decryption.\n			Problems in RSA decryption include wrong construction of an RSA decryption \n			cipher.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the RSA decryption protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_61("protocolCheckProblemType:RSA_Encryption", "Cryptography: RSA Encryption", "RSA problems concerning encryption.\n			\n			RSA is a recommended algorithm for asymmetric encryption and decryption.\n			Problems in RSA encryption include wrong construction of an RSA encryption \n			cipher.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the RSA encryption protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_62("protocolCheckProblemType:RSA_BothEncryptionAndDecryption", "Cryptography: Basic Protocol Violations for RSA Encryption and Decryption", "RSA problems concerning both encryption and decryption.\n			\n			RSA is a recommended algorithm for asymmetric encryption and decryption.\n			Problems in RSA encryption and decryption include wrong construction and use\n			of a an RSA key generation parameter spec of wrong key pair generation.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the RSA protocols that are used both in encryption and decryption.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_80("protocolCheckProblemType:SecureRandomNumberGeneratorConstruction", "Cryptography: Construction of Secure Random Number Generator", "Problems concerning secure random number generator construction.\n\n			Secure random number generator construction might be compromised, e.g., \n			by using a non-securely constructed seed.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the secure random number construction protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_63("protocolCheckProblemType:SecureRandomNumberConstruction", "Cryptography: Construction of Secure Random Numbers", "Problems concerning secure random numbers.\n\n			Secure random number construction might be compromised, e.g., \n			by using a non-securely constructed seed for the generator.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the secure random number construction protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_64("protocolCheckProblemType:InitializationVectorConstruction", "Cryptography: Construction of Initialization Vectors", "Problems concerning wrong construction of an initialization vector.\n			\n			Some cryptographic algorithms need an initialization vector for starting up the engine.\n			This protocol checks the construction of such a vector.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the initialization vector construction protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_65("protocolCheckProblemType:PBE", "Cryptography: Password-Based Encryption", "Problems concerning wrong use of password-based encryption.\n\n			Password-based encryption constructs cryptographic keys from passwords.\n			A hash function, an iteration count and a salt are needed here.  This problem type\n			checks for correct generation of key specs and keys, algorithm parameter specs and\n			algorithm parameters, and ciphers.\n\nSee also CWE-310", "Correct the sequence of method invocations so that they match the password-based encryption protocols.", 310, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_67("protocolCheckProblemType:CryptographicHashing", "Cryptography: Cryptographic Hashing", "Problems concerning wrong use of cryptographic hashing.\n\n			Cryptographic hashes (aka message digests) are hashes that fulfill some special\n			criteria.  This problem type checks the proper use of the message digest API\n			when initializing and using a digest object.\n\nSee also CWE-328", "Correct the sequence of method invocations so that they match the cryptographic hashing protocols.", 328, 3, 19, "java"),

XANITIZER_PROBLEM_TYPE_68("protocolCheckProblemType:SignatureSigning", "Cryptography: Digital Signatures Signing", "Problems concerning wrong creation of digital signatures.\n			\n			Digitial signatures ensure that some document is signed by some given party.\n			This problem type checks if the signing is done properly.\n\nSee also CWE-347", "Correct the sequence of method invocations so that they match the digital signature protocols.", 347, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_69("protocolCheckProblemType:SignatureVerifying", "Cryptography: Digital Signatures Verifying", "Problems concerning wrong verification of digital signatures.\n			\n			Digitial signatures ensure that some document is signed by some given party.\n			This problem type checks if the verification is done properly.\n\nSee also CWE-347", "Correct the sequence of method invocations so that they match the digital signature protocols.", 347, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_81("protocolCheckProblemType:SecureXMLProcessing", "XML: Secure XML Processing", "Problems concerning insecure XML processing.\n		\n			Enable secure XML processing by setting\n			DocumentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)\n\nSee also CWE-776", "Set the feature on the DocumentBuilderFactory after initialization.", 776, 4, -1, "java"),

XANITIZER_PROBLEM_TYPE_82("protocolCheckProblemType:XMLExternalDTDs", "XML: Do not access external DTDs", "Problems concerning DTD access while XML processing.\n		\n			Suppress DTD access by setting\n			DocumentBuilderFactory.setFeature(\"http://apache.org/xml/features/nonvalidating/load-external-dtd\", true)\n\nSee also CWE-827", "Set the feature on the DocumentBuilderFactory after initialization, or do not call setValidating(true).", 827, -1, 16, "java"),

XANITIZER_PROBLEM_TYPE_83("protocolCheckProblemType:CatchBlockLogging", "Logging: Check that Logging Occurs in Catch Blocks", "Check that a catch block logs some output.\n		    \n		    Currently, calls of log outputs for frameworks \n		    'java.util.logging', 'org.apache.log4j', and 'org.lsf4j' are detected.\n\nSee also CWE-778", "Add a log output method call in the catch block.", 778, 10, -1, "java"),

XANITIZER_PROBLEM_TYPE_84("RegexSearch:SystemReferenceToFileInXMLFile", "XML: Use of SYSTEM Reference to File URI", "Checks in XML files if a SYSTEM reference is used with a 'file:' URI.\n		   \n           This allows access to any file in the file system.\n\nSee also CWE-22", "Do not use 'file:' URIs in SYSTEM references in XML files.", 22, 5, 13, "java"),

XANITIZER_PROBLEM_TYPE_5("GWT:RegexSearch:JSNI", "GWT: JSNI Usage", "Checks the usage of the JavaScript Native Interface (JSNI) feature of GWT in the Java code.\n		   \n		   Take care in your native JSNI methods to not do anything that would expose you to attacks.\n		   Native JavaScript code embedded via JSNI might do unsafe things and therefore should be checked carefully.\n		   \n		   A JSNI block begins with the exact token /*-{ and Xanitizer reports an information finding for every JSNI block in the code.\n\nSee also CWE-111", "Check JSNI block for possible vulnerabilities.", 111, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_6("RegexSearch:PasswordInConfigFile", "Application Server: Password in Configuration File", "Checks for hard-coded passwords in configuration files.\n		  \n		  Hard-coded passwords in configuration files or other resource files are a risk factor.\n\nSee also CWE-260", "Remove password from configuration file.", 260, 2, -1, "java"),

XANITIZER_PROBLEM_TYPE_37("rl:IOStreamResourceLeak", "Resource Leak: IO Stream Resource Leak", "This problem type searches for IO streams in the code that are opened but never closed.\n		   This has a negative impact on performance and availability. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   In this analysis certain resources as e.g. HttpServletResponse.getOutputStream() are not supposed to be closed by the servlet. \n		   Instead the servlet container must do that in this example.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the stream (preferably in a finally block).", 404, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_38("rl:SocketResourceLeak", "Resource Leak: Socket Resource Leak", "This problem type reports paths where a socket is opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the socket (preferably in a finally block).", 404, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_39("rl:DBAccessResourceLeak", "Resource Leak: Database Access Resource Leak", "This problem type reports paths where database connections, sessions or statements are opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.		   \n		   \n		   Xanitizer reports all code paths where the data flow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the data flow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the connection (preferably in a finally block).", 404, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_40("rl:DBSQLResultSetResourceLeak", "Resource Leak: SQL ResultSet Resource Leak", "This problem type reports paths where a SQL result set is opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.		   \n		   \n		   Xanitizer reports all code paths where the data flow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the data flow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   The resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the result set (preferably in a finally block).", 404, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_85("SpecialMethodCall:JNICall", "JNI: Unsafe Java Native Interface", "By using the Java Native Interface (JNI) the Java code could import all the memory issues  \n			which are known from other languages. Java code is not any more protected against buffer overflow vulnerabilities when invoking native code.\n			Exploitation of such vulnerabilities occurs in the same way - regardless if accessed directly in the foreign code or through JNI.\n			\n			Therefore it is important to understand what kind of native methods are invoked. \n			This problem type searches in all workspace methods belonging to the call graph and finds all locations where JNI is potentially used. \n			There could be false positives if an invocation occurs via super/interface class \n			and there are several sub classes of which just a subset has a native method.\n\nSee also CWE-111", "Implement error handling around the JNI call or do not use JNI at all.", 111, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_7("SpecialMethodCall:AppliedReflection", "Usage: Java Reflection", "Searches in project code for all Java language methods which conduct Java reflection.\n		   The goal is to find out where reflection is applied in the code.\n\nSee also CWE-545", "Do not use reflection.", 545, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_8("SpecialMethodCall:java.util.Random", "Usage: java.util.Random", "Instances of class 'java.util.Random' are not cryptographically secure. \n		   Consider instead using class 'java.security.SecureRandom' to get a cryptographically secure \n		   pseudo-random number generator for security-sensitive applications.\n\nSee also CWE-330", "Use java.security.SecureRandom instead of java.util.Random.", 330, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_70("SpecialMethodCall:AppliedDNSResolution", "Usage: Reverse DNS Resolution", "Searches in project code for all methods which resolve an IP address to a hostname. \n			The resolved hostname can be spoofed by an attacker and should therefore not be used in a security decision for authentication.\n\nSee also CWE-350", "DNS resolution not be used in a security decision for authentication.\n			You should use another security decision approach for authentication like certificates, account and password.", 350, 2, 10, "java"),

XANITIZER_PROBLEM_TYPE_71("SpecialMethodCall:AppliedPrivilegedAction", "Usage: Privileged Action", "Searches in project code for all methods which are executed with privileged rights.\n			Methods which are executed with privileged rights may not access tainted values and should not be public, \n			as described in the rules SEC00-J and SEC01-J of the SEI CERT Oracle Coding Standard for Java.\n			\n			Please see\n			https://wiki.sei.cmu.edu/confluence/display/java/SEC00-J.+Do+not+allow+privileged+blocks+to+leak+sensitive+information+across+a+trust+boundary\n			and\n			https://wiki.sei.cmu.edu/confluence/display/java/SEC01-J.+Do+not+allow+tainted+variables+in+privileged+blocks\n			for more information.\n\nSee also CWE-250", "If tainted data is used in methods with privileged rights or these methods are public,\n			you have to change the implementation to be private and sanitize the tainted data before using it.", 250, 5, 11, "java"),

XANITIZER_PROBLEM_TYPE_72("SpecialMethodCall:AppliedClassLoading", "Usage: Loading External Classes", "Searches in project code for all methods which are executed to load external classes.\n			Loading unsigned external classes allows an attacker to execute malicious code \n			by compromising the external classes with its own implementation on the external host, \n			by performing DNS spoofing, or by modifing the code during transmission (man-in-the-middle attack).\n\nSee also CWE-494", "Use a security manager and load only signed external classes.", 494, -1, 14, "java"),

XANITIZER_PROBLEM_TYPE_11("SpecialMethodCall:EncryptionUsedInProject", "Cryptography: Cryptographic Algorithms Used in Project", "Searches in project code for all methods which set, select or determine a cryptographic algorithm.\n		   The goal is to find out which cryptographic algorithms are applied in the code.\n\nSee also CWE-327", "n/a", 327, 3, 19, "java"),

XANITIZER_PROBLEM_TYPE_12("SpecialMethodCall:EncryptionUsedInProjectWOProvider", "Cryptography: Cryptographic Algorithms w/o Specified Crypto-Provider", "Searches in project code for all methods which set, select or determine a cryptographic algorithm and do not specify a \n		   crypto-provider for it, although an alternative method with a parameter for the provider is available.\n		   Not specifying the crypto-provider makes it uncertain which crypto-provider the server will use - it might be an unsafe one.\n\nSee also CWE-325", "Specify crypto-provider.", 325, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_13("SpecialMethodCall:JavaServletFindAndInclude", "Usage: Java Servlet Forward and Include", "This special method call kind determines where in the code Java servlet's RequestDispatcher forward and include methods\n		   (and affiliated methods) are applied.\n		   A Java servlet can forward a request from the current servlet to another resource \n		   and can include the content of another resource in the response of the current servlet.\n		   Usually the target resource is not known statically. In order to simulate the control flow correctly\n		   Xanitizer allows to specify the target resources for a current servlet. See more at \"intercepted methods\".\n\nSee also CWE-601", "No fix - this is just an informational issue.", 601, -1, 22, "java"),

XANITIZER_PROBLEM_TYPE_14("GWT:SpecialMethodCall:JSONParserCallingJSEval", "GWT: Client Side Unsafe Calls", "Searches in project code for the invocation of methods that cause the browser to evaluate their input parameter for example when treated as HTML.\n 			If such input parameter contains untrusted data then the application is exposed to XSS.\n 			Unsafe methods are e.g. setHTML(String) or some constructors of GWT widgets, like HTML(String).\n 			It is recommended to use the analogical methods / constructors which are accepting SafeHTML as input parameter.\n 			\n 			Also methods, like JSONValue parse(String), which invoke the JavaScript eval() function for the input parameter are dangerous and should be avoided.\n\nSee also CWE-79", "Use SafeHTML.", 79, 7, 4, "java"),

XANITIZER_PROBLEM_TYPE_15("ssl:SpecialMethodCall:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Usage", "Searches in the workspace for the invocation of methods which deal with SSL/TLS Validation \n      	and which could do the validation job not strict enough - because they are deprecated or dummy or self-made implementations. \n      	Are they invoked from test code only? Or also from production code?\n      	\n      	Check the documentation or the implementation of reported methods invoked in production code.\n      	\n      	Please see also the CWE-296, CWE-297, CWE-298, CWE-299, CWE370, \n      	which describe special problems regarding certificate validation.\n\nSee also CWE-295", "Check usage of validation method.", 295, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_86("SpecialMethodCall:Deserialization:UnsafeDeserializationConfiguration", "Deserialization: Unsafe Deserialization Configuration", "With polymorphic handling an attacker can potentially produce types of values that the service did not mean to allow. \n      	This can lead to remote code execution.\n\nSee also CWE-502", "Do not call ObjectMapper.enableDefaultTyping() or any of its variants.", 502, 8, -1, "java"),

XANITIZER_PROBLEM_TYPE_87("SpecialMethodCall:PrintStackTrace", "Usage: PrintStackTrace", "Depending on the system configuration, using java.lang.Throwable.printStackTrace can expose an exception to a remote user. \n        In some cases the error message tells the attacker precisely what sort of an attack the system will be vulnerable to.\n\nSee also CWE-497", "Use an appropriately configured logging mechanism and log the exception directly into a log file that is not viewable by an end user.", 497, 10, -1, "java"),

XANITIZER_PROBLEM_TYPE_16("ssl:SpecialMethodOverwriting:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Implementation", "Searches for project-specific implementations for SSL/TLS validation. \n      	Quite often such home-grown implementations are imperfect. \n      	Verify whether project-specific implementations perform all necessary validation steps:\n      \n 		* Is the certificate signed by a \"trusted\" CA (see CWE-296)?	\n 		\n		* Is the host name matching the certificate‘s CN (see CWE-297)?\n\n		* Is the certificate expired (see CWE-298)?\n		\n		* Has the certificate been revoked (see CWE-299, CWE-370)?\n		\n		If not, check where these project-specific implementations are used ? \n		Just in test code or also in production code ?\n\nSee also CWE-295", "Review project-specific validation method.", 295, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_17("ci:CommandInjection", "Injection: Command Injection", "This security problem represents the dynamic construction of commands using data which originates from the web client.\n\nSee also CWE-77", "Check that only allowed characters are used, because the value can be controlled from the outside.", 77, 1, -1, "java"),

XANITIZER_PROBLEM_TYPE_51("ci:OSCommandInjection", "Injection: OS Command Injection", "This security problem represents the dynamic construction of operating system commands using data which originates from the web client.\n\nSee also CWE-78", "Check that only allowed characters are used, because the value can be controlled from the outside.", 78, 1, 2, "java"),

XANITIZER_PROBLEM_TYPE_18("ci:SQLInjection", "Injection: SQL Injection", "This security problem represents SQL-based database access through queries which are partly formed from external input.\n\nSee also CWE-89", "Use prepared statement or check that only allowed characters are used, because the value can be controlled from the outside.", 89, 1, 1, "java"),

XANITIZER_PROBLEM_TYPE_19("ci:XPathInjection", "Injection: XPath Injection", "This security problem represents the dynamic construction of XPath queries using data which originates from the web client.\n\nSee also CWE-643", "Check that only allowed characters are used, because the value can be controlled from the outside.", 643, 1, -1, "java"),

XANITIZER_PROBLEM_TYPE_20("ci:LDAPInjection", "Injection: LDAP Injection", "This security problem represents the LDAP protocol access using data which originates from the web client.\n\nSee also CWE-90", "Check that only allowed characters are used, because the value can be controlled from the outside.", 90, 1, -1, "java"),

XANITIZER_PROBLEM_TYPE_21("ci:LogInjection", "Injection: Log Injection", "This security problem represents the write access to log media, like log files, using data which originates from the web client.\n\nSee also CWE-117", "Check that only allowed characters are used, because the value can be controlled from the outside.", 117, 1, -1, "java"),

XANITIZER_PROBLEM_TYPE_73("hcc:Hard-CodedCredentials:CredentialsAsMethodParameter", "Hard-Coded Credentials: Credentials as Method Parameter", "This security problem represents an access to some protected system component using hard-coded access data like user names.\n			\n			Do not additionally assign a taint source kind to this problem type. \n			It would be superfluous in this scenario and has no effect.\n\nSee also CWE-798", "Remove credentials from the code.", 798, 2, 7, "java"),

XANITIZER_PROBLEM_TYPE_74("hcc:Hard-CodedCredentials:CryptographicKeyAsMethodParameter", "Hard-Coded Credentials: Cryptographic Key as Method Parameter", "This security problem represents an access to some protected system component using hard-coded cryptographic keys.\n			\n			Do not additionally assign a taint source kind to this problem type. \n			It would be superfluous in this scenario and has no effect.\n\nSee also CWE-321", "Remove cryptographic key from the code.", 321, 3, 7, "java"),

XANITIZER_PROBLEM_TYPE_75("hcc:Hard-CodedCredentials:PasswordAsMethodParameter", "Hard-Coded Credentials: Password as Method Parameter", "This security problem represents an access to some protected system component using hard-coded passwords.\n			\n			Do not additionally assign a taint source kind to this problem type. \n			It would be superfluous in this scenario and has no effect.\n\nSee also CWE-259", "Remove password from the code.", 259, 2, 7, "java"),

XANITIZER_PROBLEM_TYPE_23("pt:PathTraversal", "Path Traversal: Manipulated File System Access", "This security problem represents the access to the file system using non-sanitized data which originates from an untrusted source.\n\nSee also CWE-22", "Check that only allowed characters are used, because the value can be controlled from the outside.", 22, 5, 13, "java"),

XANITIZER_PROBLEM_TYPE_24("xss:XSSFromDb", "XSS: Stored XSS", "This security problem represents the classic persistent cross-site scripting vulnerabilities.\n		   Tainted data is read from databases or other persistent storages and is delivered to a web client as part of a rendered view (e.g. html).\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value read from the database could be manipulated.", 79, 7, 4, "java"),

XANITIZER_PROBLEM_TYPE_25("xss:XSSFromDbUnrendered", "XSS: Stored XSS from Unrendered Output", "This security problem represents persistent cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data is read from databases or other persistent storages and is delivered to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value read from the database could be manipulated.", 79, 7, 4, "java"),

XANITIZER_PROBLEM_TYPE_26("xss:XSSFromRequest", "XSS: Reflected XSS", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities.\n		   Tainted data has been received from a web client and is delivered back to the web client as part of a rendered view (e.g. html).\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value can be controlled from the outside.", 79, 7, 4, "java"),

XANITIZER_PROBLEM_TYPE_27("xss:XSSFromRequestUnrendered", "XSS: Reflected XSS from Unrendered Output", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data has been received from a web client and is delivered back to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value can be controlled from the outside.", 79, 7, 4, "java"),

XANITIZER_PROBLEM_TYPE_76("xss:URLRedirectAbuse", "XSS: URL Redirection Abuse", "This security problem is an abuse of redirection functionality. It occurs when user-provided and non-validated data are used to construct\n		   a URL for a redirection directive which is sent back from the application to the browser.\n\nSee also CWE-601", "Validate the value, because it can be controlled from the outside.", 601, -1, 22, "java"),

XANITIZER_PROBLEM_TYPE_29("pl:PrivacyLeak", "Privacy Leak: Output of Confidential Data", "This security problem refers to private or confidential data which are not properly protected against  \n		   unauthorized access. This might occur if e.g. password is written in clear text to the console or to any file (like a log file)\n		   or to the web response or to a SOAP output.\n\nSee also CWE-359", "Do not output private information.", 359, 3, -1, "java"),

XANITIZER_PROBLEM_TYPE_30("rhi:ResponseHeaderInjection", "Injection: Response Header Injection", "This security problem refers to manipulation of the HTTP response header through non neutralized data.\n		   This includes especially HTTP Response Splitting.\n\nSee also CWE-113", "Validate the value used for the HTTP header, because it can be controlled from the outside.", 113, 1, -1, "java"),

XANITIZER_PROBLEM_TYPE_31("cook:UnsecuredCookie", "Cookies: Unsecured Cookie", "This security problem occurs when the secure attribute is not set for a cookie, \n		   which could cause the user agent (browser) to send the cookie in plain text over an HTTP session.\n		   \n		   A non-encrypted cookie on the transportation layer can be easily read from a third party. \n		   It is strongly recommended to always set the secure attribute when a cookie should be sent via HTTPS only.\n\nSee also CWE-614", "Set the secure attribute by calling setSecure(true).", 614, 3, 8, "java"),

XANITIZER_PROBLEM_TYPE_32("cook:HttpOnlyCookie", "Cookies: No HttpOnly Cookie", "This security problem occurs when the HttpOnly flag is not set for a cookie.\n		   HttpOnly cookies are not supposed to be easily exposed to client-side scripting code, \n		   and may therefore help mitigate certain kinds of cross-site scripting attacks.\n		    \n		   Be aware that the HttpOnly flag could also be set by alternative means that are not detected by Xanitizer, \n		   like e.g. setting the entire cookie entry via setHeader or in the configuration of the application server.\n		   Such alternative usage could cause Xanitizer to wrongly report a missing HttpOnly flag.\n\nSee also CWE-1004", "Set the cookie to be HttpOnly.", 1004, 5, 17, "java"),

XANITIZER_PROBLEM_TYPE_33("xxe:GeneralEntities", "XXE: General Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external general entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.		\n		   \n		   External general entities can be turned off by invoking setFeature(\"http://xml.org/sax/features/external-general-entities\", false) on the used factory.   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n\nSee also CWE-611", "Do not include external general entities.", 611, 4, -1, "java"),

XANITIZER_PROBLEM_TYPE_34("xxe:DisallowDoc", "XXE: Disallow Doc - Xerces 2 only", "This regards Xerces 2 (JAXP 1.2 or higher) only. Deactivate this problem type if your project uses an earlier XML parser implementation.\n		   Xanitizer reports a problem when the configuration of a XML parser allows the access to external subsets in XML documents.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.\n		   \n		   Doctype declarations can be turned off by invoking setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true) on the used factory.\n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n\nSee also CWE-611", "Disallow doctype declaration.", 611, 4, -1, "java"),

XANITIZER_PROBLEM_TYPE_35("xxe:ParamEntities", "XXE: Param Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external parameter entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.\n		   \n		   External parameter entities can be turned off by invoking setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false) on the used factory.	   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n		   \n		   See also CWE-611.\n\nSee also CWE-611", "Do not include external parameter entities or the external DTD subset.", 611, 4, -1, "java"),

XANITIZER_PROBLEM_TYPE_36("xxe:XMLSecureProcessing", "XXE: XML Secure Processing", "This problem type reports all XML processing in the code without having explicitly set the XMLConstants.FEATURE_SECURE_PROCESSING flag\n		   which instructs JAXP components such as parsers, transformers and so on to behave in a secure fashion. \n		   \n		   Secure processing does not completely prevent XML eXternal Entities (XXE) processing, \n		   but it limits the maximum number of entity expansions and hence reduces the surface for DOS attacks. 	   \n		   Depending on the used JAXP implementation the 'secure processing' feature might be switched on by default, \n		   but to be sure about the status it is better to set it explicitly.\n		   \n		   You can activate this problem type as an alternative to the XXE problem types if your project requires external access in its XML documents \n		   and therefore cannot switch on the XXE protection mechanisms.\n\nSee also CWE-611", "Set the XMLConstants.FEATURE_SECURE_PROCESSING flag.", 611, 4, -1, "java"),

XANITIZER_PROBLEM_TYPE_41("idor:InsecureDirectObjectReferences", "Path Traversal: Insecure Direct Object References", "A direct object reference occurs when a reference to an internal implementation object, such as a file, directory, or database key, is exposed to a client. \n          Without an access control check or other protection, attackers can manipulate these references to access unauthorized data.\n          Therefore only indirect object references should be exposed which are internally mapped to the corresponding direct object references.\n          \n          CAUTION: There are no predefined taint sink methods in the assigned taint sink kind because Xanitizer does not know which database table columns are regarded as database keys (direct object references)\n		  and which columns are just attributes.\n          Therefore the appropriate sinks have to be configured project-specific in the assigned taint sink kind.\n\nSee also CWE-813", "Check access permissions.", 813, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_42("tbv:TrustBoundaryViolationSession", "Trust Boundary Violation: HTTP Session", "This problem type reports paths where tainted data is stored in the HTTP Session object of web applications without prior validation.\n		  This is an instance of the vulnerability 'Trust Boundary Violation' as described in CWE-501.\n\nSee also CWE-501", "Validate value before store it into the session object.", 501, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_50("ci:ReflectionInjection", "Injection: Reflection Injection", "This security problem represents the loading of code via reflection from user-defined data. \n		  This allows an attacker to load unexpected classes or methods, which can result in bypassing authentication \n		  and access control checks or in unexpected behavior of the application.\n		  \n		  If an attacker can upload files to a location in the class path, malicious behavior can be added to the application.\n		  \n		  This security problem is also known as Unsafe Reflection.\n\nSee also CWE-470", "Do not use user-controlled data to load code via reflection or apply strict input validation \n			to ensure that only the expected classes or methods are used (whitelisting).", 470, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_53("id:InsecureDeserializationFromDb", "Deserialization: Insecure Stored Deserialization", "The stream of serialized data could have been tampered with \n			which could result in access control, denial-of-service and remote code execution attacks. \n			This security problem represents stored (persistent) insecure deserialization. \n			Tainted data is read from databases or other persistent storages.\n\nSee also CWE-502", "Use a subclass of OjectInputStream or a Java agent with a whitelist of deserializable classes as sanitizer. \n			See also https://www.owasp.org/index.php/Deserialization_Cheat_Sheet for further details.", 502, 8, -1, "java"),

XANITIZER_PROBLEM_TYPE_54("id:InsecureDeserializationFromRequest", "Deserialization: Insecure Reflected Deserialization", "The stream of serialized data could have been tampered with \n			which could result in access control, denial-of-service and remote code execution attacks. \n			This security problem represents reflected (non-persistent) insecure deserialization. \n			Tainted data has been received from a web client.\n\nSee also CWE-502", "Use a subclass of OjectInputStream or a Java agent with a whitelist of deserializable classes as sanitizer. \n			See also https://www.owasp.org/index.php/Deserialization_Cheat_Sheet for further details.", 502, 8, -1, "java"),

XANITIZER_PROBLEM_TYPE_77("ufdt:UploadofFilewithDangerousType", "Path Traversal: Upload of File with Dangerous Type", "This security problem represents an upload of file content where the original user defined file name is used. \n		   Using this file name allows the user to upload a file with a dangerous type \n		   which might be automatically processed on the client or server side later.\n\nSee also CWE-434", "Rename the file or the file extension to prevent dangerous types (e.g. to .txt) \n			or add a sanitizer to verify the file type and file identifier.", 434, -1, 9, "java"),

XANITIZER_PROBLEM_TYPE_78("ci:CodeInclusion", "Injection: Code Inclusion", "This security problem represents the loading of external code during runtime. \n			If an attacker can add its own code e.g. by controlling the remote or local location, \n			by performing DNS spoofing, or by manipulating the code in transit (man-in-the-middle attack), \n			an attacker is able to load malicious code into the running application, \n			which can result in bypassing authentication and access control checks \n			or in unexpected behavior of the application.\n\nSee also CWE-829", "Do not allow loading of user-controlled code during runtime. \n			Otherwise apply strict input validation to the code itself, \n			or ensure that only the expected locations are possible (whitelisting), \n			which should contain only signed jars and be executed with a security manager, \n			or execute the code in a secure sandbox.", 829, -1, 16, "java"),

XANITIZER_PROBLEM_TYPE_88("ci:CodeInjection", "Injection: Code Injection", "This security problem represents the execution of user-controlled code \n			or the manipulation of code by external entities during runtime. \n			If an attacker can add its own code, \n			an attacker is able to load and execute malicious code into the running application, \n			which can result in bypassing authentication and access control checks \n			or in unexpected behavior of the application.\n\nSee also CWE-94", "Do not allow execution of user-controlled code \n			or manipulating code by external entities during runtime. \n			Otherwise apply strict input validation to the code itself, \n			or execute the code in a secure sandbox.", 94, -1, -1, "java"),

XANITIZER_PROBLEM_TYPE_43("XPathSearch:sessionTimeout", "Application Server: 'session-timeout' Too Large", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   This configuration can be done in web.xml. Xanitizer reports a warning if there is a timeout value set larger than 15 (minutes).\n\nSee also CWE-613", "Reduce session timeout.", 613, 2, -1, "java"),

XANITIZER_PROBLEM_TYPE_44("XPathSearch:sessionTimeoutNotConfigured", "Application Server: 'session-timeout' Not Configured", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   Xanitizer reports a warning if there is no configuration for session idle timeout in web.xml.\n\nSee also CWE-613", "Configure session timeout.", 613, 2, -1, "java"),

XANITIZER_PROBLEM_TYPE_45("XPathSearch:useHttpOnly", "Application Server: 'useHttpOnly' Disabled", "In context.xml it can be configured that the 'httpOnly' flag should be set on session cookies \n		   to prevent client side script from accessing the session ID.\n		   Xanitizer reports a warning if the 'useHttpOnly' flag has been set to false in context.xml.\n\nSee also CWE-1004", "Set 'useHttpOnly' flag to true.", 1004, 5, 17, "java"),

XANITIZER_PROBLEM_TYPE_46("XPathSearch:sessionConfigWOHttpOnlyAndSecureInCookieConfig", "Application Server: 'cookie-config' Not Properly Configured", "In web.xml it can be configured, in a 'cookie-config' element nested in a 'session-config' element, to prevent client side scripts from accessing the\n		   session ID and to force the browser to only use secure connections for transmission of the cookie.\n		   \n		   Xanitizer reports a finding if the web.xml 'cookie-config' element is not configured in a way that both, the contained\n		   'http-only' flag and 'secure' flag, are set to 'true'.\n\nSee also CWE-614", "Set the 'http-only' and the 'secure' flag to true.", 614, 3, 8, "java"),

XANITIZER_PROBLEM_TYPE_47("XPathSearch:sessionConfigWOTrackingModeCookieConfig", "Application Server: 'tracking-mode' Not Set to 'COOKIE'", "In web.xml it can be configured, in a 'tracking-mode' element nested in a 'session-config' element, that \n		   the session IDs are only sent via cookies, not via request parameter.  \n		   \n		   Xanitizer reports a finding if there is no such configuration in a 'session-config' element.\n\nSee also CWE-16", "Set 'tracking-mode' to 'COOKIE'.", 16, 6, -1, "java"),

XANITIZER_PROBLEM_TYPE_48("XPathSearch:sessionConfigMissing", "Application Server: 'session-config' Is Missing", "In web.xml, many security-relevant session properties should be configured in a 'session-config' element; \n		   Xanitizer reports if this 'session-config' element is not contained at all. A secure configuration looks like this:\n		   \n		   <web-app ...\n		   \n				<session-config>\n				\n				   <session-timeout>30</session-timeout>\n				   \n				   <cookie-config>\n				   \n				      <http-only>true</http-only>\n				\n				      <secure>true</secure>\n				      \n				   </cookie-config>\n				   \n				   <tracking-mode>COOKIE</tracking-mode>\n				   \n				</session-config>\n\nSee also CWE-613", "Add 'session-config' element.", 613, 2, -1, "java"),

XANITIZER_PROBLEM_TYPE_49("XPathSearch:exceptionTypeJavaLangThrowableMissing", "Application Server: 'error-page' or 'exception-type' Configuration Wrong or Missing", "In web.xml, a catch-all 'error-page' element should be defined that deals with all exceptions, i.e. is defined for exception-type java.lang.Throwable.\n		   This ensures that no internal error information is sent to the HTTP response by accident (where it could help a hacker to direct his attack).\n		   \n		   Since Servlet version 3.0, an error-page specification with neither exception-type nor error-code element also specifies a catch-all.\n		   \n		   A secure configuration looks like this:\n		   \n			  <error-page>\n			  \n			    <exception-type>java.lang.Throwable</exception-type>\n			    \n			    <location>/error.jsp</location>\n			    \n			  </error-page>\n\nSee also CWE-16", "Configure 'error-page' element.", 16, 6, -1, "java"),

XANITIZER_PROBLEM_TYPE_55("XPathSearch:directoryBrowsingEnabled", "Application Server: Directory Browsing Enabled", "In web.xml it can be configured in a 'listing' element nested in a 'servlet' element,\n			that the directories can be browsed when entering the directory name. \n			This can allow an attacker to bypass the authorization by directly invoking a visible file.\n\nSee also CWE-425", "Set the 'listing' flag to false.", 425, 2, 6, "java"),

XANITIZER_PROBLEM_TYPE_89("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ClientSideXSS", "XSS: Client-Side XSS", "Data flow of tainted data in client from some taint source to some XSS taint sink.", "Break the flow of the tainted data at some location.", 79, 7, 4, "js"),

XANITIZER_PROBLEM_TYPE_90("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ClientSideXSSSink", "XSS: Client-Side XSS Sink", "If tainted data reaches this spot, cross-site scripting might be possible.", "Inspect the code carefully, checking that no harmful command can be injected here.", 79, 7, 4, "js"),

XANITIZER_PROBLEM_TYPE_116("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ClientSideURLRedirection", "Phishing: Client-Side URL Redirection", "Data flow of tainted data in client from some taint source to some spot where it might lead to navigation to an untrusted site.", "Break the flow of the tainted data at some location.", 601, -1, 22, "js"),

XANITIZER_PROBLEM_TYPE_91("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/CommandInjection", "Injection: Command Injection", "Data flow of user-controlled data to some spot where that data is executed as a command.", "Break the flow of the tainted data at some location.", 78, 1, 2, "js"),

XANITIZER_PROBLEM_TYPE_92("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/CommandInjectionSinks", "Injection: Command Injection Sinks", "If tainted data reaches this spot, a user-controlled command might be executed.", "Inspect the code carefully, checking that no harmful command can be executed here.", 78, 1, 2, "js"),

XANITIZER_PROBLEM_TYPE_93("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/PathTraversal", "Path Traversal: Improper Limitation of a Pathname to a Restricted Directory", "Data flow of user-controlled data to some spot where that data is used for accessing a file in the file system.", "Break the flow of the tainted data at some location.", 22, 5, 13, "js"),

XANITIZER_PROBLEM_TYPE_94("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/PathTraversalSinks", "Path Traversal Sinks: Possible Improper Limitation of a Pathname to a Restricted Directory", "If tainted data reaches this spot, some random file might be accessed in the file system.", "Inspect the code carefully, checking that no user-controlled data is used here for accessing random files.", 22, 5, 13, "js"),

XANITIZER_PROBLEM_TYPE_95("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/PseudoRandomBytes", "Cryptography: Pseudo-Random Bytes", "Some function returning random numbers is called that does not return cryptographically strong random numbers.", "For cryptographically strong numbers, call, e.g., 'crypto.randomBytes()'.", 330, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_96("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/WeakHash", "Cryptography: Weak Hash", "Some hashing functiion might be called that is not considered to be cryptographically strong.", "Use a stringer hash algorithm.", 70, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_97("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/BufferCallWithNoAssert", "Validation: Flag 'noAssert' Set on Buffer Access", "When accessing a binary buffer, the 'noAssert' parameter has been set. This circumvents index checks and can lead to crashes.", "No-assert should not be set - best use the default setting of 'false'.", 129, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_98("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/NewBufferWithNumberArg", "Privacy Leak: Buffer Constructor Called With Number Argument", "When creating a buffer, the constructor was perhaps called with a numerical argument. This can lead to secret information from the memory being exposed.", "Avoid calling 'new Buffer()' with a numeric argument.", 316, 3, 8, "js"),

XANITIZER_PROBLEM_TYPE_99("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/DisableMoustacheEscape", "XSS: Disabled '...escapeMarkup'", "Possibly property 'escapeMarkup' is set to 'false'. This can lead to XSS attacks.", "Avoid disabling markup escaping.", 80, 7, 4, "js"),

XANITIZER_PROBLEM_TYPE_100("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/RegExpInjection", "Injection: RegEx Injection", "Data flow of user-controlled data to the constructor of a regular expression. This can lead to a Denial-of-Service attack via overly time-consuming checks.", "Break the flow of the tainted data at some location.", 400, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_101("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/RegExpUnsafe", "General: RegEx Unsafe", "Potentially unsafe regular expression that might take a very long time to run.", "Simplify the regular expression, so that it uses less and/or non-nested iteration operators.", 400, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_102("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/RequireNonLiteral", "General: Non-Literal 'require()' argument", "'require()' is called with a non-literal argument. This might include any other module, and is difficult to analyze automatically.", "Specify modules being 'require'd with a literal argument.", -1, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_103("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/NoMethodOverrideAfterCsrf", "XSRF: Called '.methodOverride()' after '.csrf()' (Express Framework)", "'express.methodOverride()' is called after, and thus has lower priority than 'express.csrf()', which might be used to circumvent the CSRF checks. This can lead to Cross-Site Request Forgery.", "Do not call 'express.methodOverride()' after 'express.csrf()'.", 352, -1, 12, "js"),

XANITIZER_PROBLEM_TYPE_104("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/TimingAttack", "Privacy Leak: Possible Timing Attack", "Some secret data seems to be checked by some operators against expected values which might allow timing attacks.", "When comparing sensitive data, do not use short-cutting equality checks.", 308, 2, -1, "js"),

XANITIZER_PROBLEM_TYPE_105("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ObjectInjection", "Injection: Object Injection", "The square-bracket notation is used for assigning a user-controlled value to a user-controlled field", "Do not assign user-controlled values to user-controlled fields. This can lead to a variant of command injection.", 94, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_106("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/BypassAngularSecurityMethods", "XSS: Bypassed Angular Security Methods", "Angular normally escapes outputs for the correct spot; if this escaping is disabled, it should be checked if the data being output can not lead to XSS.", "Avoid bypassing Angular's escaping.", 80, 7, 4, "js"),

XANITIZER_PROBLEM_TYPE_107("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/SQLInjection", "Injection: SQL Injection", "Data flow of user-controlled data to some spot where that data can cause harm in a database.", "Break the flow of the tainted data at some location.", 89, 1, 1, "js"),

XANITIZER_PROBLEM_TYPE_108("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/SQLInjectionSinks", "Injection: SQL Injection Sinks", "If tainted data reaches this spot, an aggressor might access the database.", "Inspect the code carefully, checking that no harmful content can be injected here.", 89, 1, 1, "js"),

XANITIZER_PROBLEM_TYPE_109("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/StoredXSS", "XSS: Server-Side Stored XSS", "Data flow from database to the client w/o escaping.", "Break the flow of the tainted data at some location.", 80, 7, 4, "js"),

XANITIZER_PROBLEM_TYPE_110("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/PossibleCrossSiteCheckCircumvention", "XSRF: Possible Circumvention of Cross-Site Access Checks in Browser", "Browser checks that suppress cross-origin data transfer might be suppressed by this server answer.", "Inspect code carefully, checking that the cross-origin checks should really be avoided for the data being sent.", 352, -1, 12, "js"),

XANITIZER_PROBLEM_TYPE_111("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ServerSideRequestForgery", "SSRF: Server-Side Request Forgery", "Tainted data reaches a spot where might be used for accessing arbirary URLs", "Break the flow of the tainted data at some location.", 918, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_112("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/ServerSideRequestForgerySinks", "SSRF: Server-Side Request Forgery Sinks", "If tainted data reaches this spot, an aggressor might access an arbitrary URL via the server.", "Inspect the code carefully, checking that no harmful content can reach this spot.", 918, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_113("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/HardCodedCredentials", "Hard-Coded Credentials", "Passwords or login information is specified in the code.", "Use configuration files for pre-defined logins or passwords.", 259, 2, 7, "js"),

XANITIZER_PROBLEM_TYPE_114("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/InformationLeakage", "Information Leakage", "Possibly confidential information ends up in log files or at other places where it may inspected.", "Do not allow confidential information to end up in places where it may be inspected.", 214, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_115("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/SuspiciousComment", "Suspicious Comment", "Comments that contain 'TODO', 'TOFIX', etc.", "Fix the issue and change the comment.", 546, -1, -1, "js"),

XANITIZER_PROBLEM_TYPE_117("com.rigsit.xanitizer.plugins.languages.js.JavaScriptLanguagePlugin/jsTaint/EvalInjection", "Eval Injection", "Tainted data reaches a location where it is executed as code.", "Do not execute tainted data.", 95, -1, -1, "js"),

    ;

    private final String id;
    private final String presentationName;
    private final String description;
    private final String message;
    private final int cwe;
    private final int owasp;
    private final int sans;
    private final String language;
    private GeneratedProblemType(final String id, final String presentationName, final String description, final String message,
final int cwe, final int owasp, final int sans, final String language) {
         this.id = id;
         this.presentationName = presentationName;
         this.description = description;
         this.message = message;
         this.cwe = cwe;
         this.owasp = owasp;
         this.sans = sans;
         this.language = language;
    }

    /**
     *
     * Returns the problem type for the given id if it exists.
     * Otherwise returns null.
     *
     * @param problemTypeId
     * @return the problem type or null, if there is no problem type with the given id
     *
     */
    public static GeneratedProblemType getForId(final String problemTypeId) {
        for (GeneratedProblemType problemTyp : values()) {
            if (problemTyp.getId().equals(problemTypeId)) {
		           return problemTyp;
            }
        }
        return null;
    }

     public String getId() {
     	return id;
     }

     public String getPresentationName() {
     	return presentationName;
     }

     public String getMessage() {
     	return message;
     }

     public int getCWE() {
     	return cwe;
     }

     public int getOWASPTopTen() {
     	return owasp;
     }

     public int getSans25() {
     	return sans;
     }

     public String getLanguage() {
     	return language;
     }

     public String getDescription() {
     	return description;
     }
}
