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

XANITIZER_PROBLEM_TYPE_1("USER:GeneralCodeIssue", "General Code Issue", "This problem type serves as placeholder to assign it to user-defined findings, \n			which are not related to any other problem type. This could be for example quality issues in the code.", "Fix issue.", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_2("JNICall:All", "JNI: Unsafe Java Native Interface", "By using the Java Native Interface (JNI) the Java code could import all the memory issues  \n			which are known from other languages. Java code is not any more protected against buffer overflow vulnerabilities when invoking native code.\n			Exploitation of such vulnerabilities occurs in the same way - regardless if accessed directly in the foreign code or through JNI.\n			\n			Therefore it is important to understand what kind of native methods are invoked. \n			This problem type searches in all workspace methods belonging to the call graph and finds all locations where JNI is potentially used. \n			There could be false positives if an invocation occurs via super/interface class \n			and there are several sub classes of which just a subset has a native method.\n\nSee also CWE-111", "Implement error handling around the JNI call or do not use JNI at all.", 111, -1, -1),

XANITIZER_PROBLEM_TYPE_3("LiteralStringsInVariablesSearch:password", "Hard-Coded Credentials: Password in Variables", "Checks for password variables to which constant strings are assigned.\n\nSee also CWE-798", "Remove credentials from the code.", 798, 2, 7),

XANITIZER_PROBLEM_TYPE_4("LiteralStringsInVariablesSearch:username", "Hard-Coded Credentials: Username in Variables", "Checks for user name variables to which constant strings are assigned.\n\nSee also CWE-798", "Remove credentials from the code.", 798, 2, 7),

XANITIZER_PROBLEM_TYPE_56("protocolCheckProblemType:OnlyAllowedCryptoAlgorithms", "Cryptography: Check that only allowed crypto algorithms are used", "Uses of non-allowed crypto algorithms.", "Use one of the allowed algorithms instead.", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_57("protocolCheckProblemType:AES_Decryption", "Cryptography: AES Decryption Wrong", "AES problems concerning decryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_58("protocolCheckProblemType:AES_Encryption", "Cryptography: AES Encryption Wrong", "AES problems concerning encryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_59("protocolCheckProblemType:AES_BothEncryptionAndDecryption", "Cryptography: Basic Protocol Violations for AES Encryption and Decryption", "AES problems concerning both encryption and decryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_60("protocolCheckProblemType:RSA_Decryption", "Cryptography: RSA Decryption Wrong", "RSA problems concerning decryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_61("protocolCheckProblemType:RSA_Encryption", "Cryptography: RSA Encryption Wrong", "RSA problems concerning encryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_62("protocolCheckProblemType:RSA_BothEncryptionAndDecryption", "Cryptography: Basic Protocol Violations for RSA Encryption and Decryption", "RSA problems concerning both encryption and decryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_63("protocolCheckProblemType:SecureRandomNumberConstruction", "Cryptography: Construction of Secure Random Number Generator Wrong", "Secure random number generator construction.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_64("protocolCheckProblemType:InitializationVectorConstruction", "Cryptography: Construction of Initialization Vectors Wrong", "Problems concerning wrong construction of an initialization vector.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_65("protocolCheckProblemType:PBE", "Cryptography: Password-Based Encryption", "Problems concerning wrong use password-based encryption.", "... to be done ...", -1, -1, -1),

XANITIZER_PROBLEM_TYPE_5("GWT:RegexSearch:JSNI", "GWT: JSNI Usage", "Checks the usage of the JavaScript Native Interface (JSNI) feature of GWT in the Java code.\n		   \n		   Take care in your native JSNI methods to not do anything that would expose you to attacks.\n		   Native JavaScript code embedded via JSNI might do unsafe things and therefore should be checked carefully.\n		   \n		   A JSNI block begins with the exact token /*-{ and Xanitizer reports an information finding for every JSNI block in the code.\n\nSee also CWE-111", "Check JSNI block for possible vulnerabilities.", 111, -1, -1),

XANITIZER_PROBLEM_TYPE_6("RegexSearch:PasswordInConfigFile", "Application Server: Password in Configuration File", "Checks for hard-coded passwords in configuration files.\n		  \n		  Hard-coded passwords in configuration files or other resource files are a risk factor.\n\nSee also CWE-260", "Remove password from configuration file.", 260, 2, -1),

XANITIZER_PROBLEM_TYPE_37("rl:IOStreamResourceLeak", "Resource Leak: IO Stream Resource Leak", "This problem type searches for IO streams in the code that are opened but never closed.\n		   This has a negative impact on performance and availability. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   In this analysis certain resources as e.g. HttpServletResponse.getOutputStream() are not supposed to be closed by the servlet. \n		   Instead the servlet container must do that in this example.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the stream (preferably in a finally block).", 404, -1, -1),

XANITIZER_PROBLEM_TYPE_38("rl:SocketResourceLeak", "Resource Leak: Socket Resource Leak", "This problem type reports paths where a socket is opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the socket (preferably in a finally block).", 404, -1, -1),

XANITIZER_PROBLEM_TYPE_39("rl:DBAccessResourceLeak", "Resource Leak: Database Access Resource Leak", "This problem type reports paths where database connections, sessions or statements are opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.		   \n		   \n		   Xanitizer reports all code paths where the data flow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the data flow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the connection (preferably in a finally block).", 404, -1, -1),

XANITIZER_PROBLEM_TYPE_40("rl:DBSQLResultSetResourceLeak", "Resource Leak: SQL ResultSet Resource Leak", "This problem type reports paths where a SQL result set is opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.		   \n		   \n		   Xanitizer reports all code paths where the data flow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the data flow reaches a point (marked as a taint sink) in the code after that no further access to the resource instance occurs. \n		   \n		   The resource leak search does not work if the resource object is stored in non-local variables.\n\nSee also CWE-404", "Close the result set (preferably in a finally block).", 404, -1, -1),

XANITIZER_PROBLEM_TYPE_7("SpecialMethodCall:AppliedReflection", "Usage of Java Reflection", "Searches in project code for all Java language methods which conduct Java reflection.\n		   The goal is to find out where reflection is applied in the code.\n\nSee also CWE-545", "Do not use reflection.", 545, -1, -1),

XANITIZER_PROBLEM_TYPE_8("SpecialMethodCall:java.util.Random", "Usage of 'java.util.Random'", "Instances of class 'java.util.Random' are not cryptographically secure. \n		   Consider instead using class 'java.security.SecureRandom' to get a cryptographically secure \n		   pseudo-random number generator for security-sensitive applications.\n\nSee also CWE-330", "Use java.security.SecureRandom instead of java.util.Random.", 330, -1, -1),

XANITIZER_PROBLEM_TYPE_9("SpecialMethodCall:WeakEncryption", "Cryptography: Weak Cryptographic Algorithm", "Searches in project code for methods which select a cryptographic algorithm that is known to be weak.\n\nSee also CWE-327", "Use stronger cryptographic algorithm (e.g. to SHA-2).", 327, 3, 19),

XANITIZER_PROBLEM_TYPE_10("SpecialMethodCall:WeakHash", "Cryptography: Weak Hash Algorithm", "Searches in project code for methods which select a hash algorithm that is known to be weak, like MD5 or SHA-1.\n\nSee also CWE-328", "Use stronger hash algorithm (e.g. SHA-2).", 328, 3, 19),

XANITIZER_PROBLEM_TYPE_11("SpecialMethodCall:EncryptionUsedInProject", "Cryptography: Cryptographic Algorithms Used in Project", "Searches in project code for all methods which set, select or determine a cryptographic algorithm.\n		   The goal is to find out which cryptographic algorithms are applied in the code.\n\nSee also CWE-327", "n/a", 327, 3, 19),

XANITIZER_PROBLEM_TYPE_12("SpecialMethodCall:EncryptionUsedInProjectWOProvider", "Cryptography: Cryptographic Algorithms w/o Specified Crypto-Provider", "Searches in project code for all methods which set, select or determine a cryptographic algorithm and do not specify a \n		   crypto-provider for it, although an alternative method with a parameter for the provider is available.\n		   Not specifying the crypto-provider makes it uncertain which crypto-provider the server will use - it might be an unsafe one.\n\nSee also CWE-327", "Specify crypto-provider.", 327, 3, 19),

XANITIZER_PROBLEM_TYPE_13("SpecialMethodCall:JavaServletFindAndInclude", "Usage of Java Servlet Forward and Include", "This special method call kind determines where in the code Java servlet's RequestDispatcher forward and include methods\n		   (and affiliated methods) are applied.\n		   A Java servlet can forward a request from the current servlet to another resource \n		   and can include the content of another resource in the response of the current servlet.\n		   Usually the target resource is not known statically. In order to simulate the control flow correctly\n		   Xanitizer allows to specify the target resources for a current servlet. See more at \"intercepted methods\".\n\nSee also CWE-601", "No fix - this is just an informational issue.", 601, -1, 22),

XANITIZER_PROBLEM_TYPE_14("GWT:SpecialMethodCall:JSONParserCallingJSEval", "GWT: Client Side Unsafe Calls", "Searches in project code for the invocation of methods that cause the browser to evaluate their input parameter for example when treated as HTML.\n 			If such input parameter contains untrusted data then the application is exposed to XSS.\n 			Unsafe methods are e.g. setHTML(String) or some constructors of GWT widgets, like HTML(String).\n 			It is recommended to use the analogical methods / constructors which are accepting SafeHTML as input parameter.\n 			\n 			Also methods, like JSONValue parse(String), which invoke the JavaScript eval() function for the input parameter are dangerous and should be avoided.\n\nSee also CWE-79", "Use SafeHTML.", 79, 7, 4),

XANITIZER_PROBLEM_TYPE_15("ssl:SpecialMethodCall:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Usage", "Searches in the workspace for the invocation of methods which deal with SSL/TLS Validation \n      	and which could do the validation job not strict enough - because they are deprecated or dummy or self-made implementations. \n      	Are they invoked from test code only? Or also from production code?\n      	\n      	Check the documentation or the implementation of reported methods invoked in production code.\n\nSee also CWE-295", "Check usage of validation method.", 295, 3, -1),

XANITIZER_PROBLEM_TYPE_16("ssl:SpecialMethodOverwriting:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Implementation", "Searches for project-specific implementations for SSL/TLS validation. \n      	Quite often such home-grown implementations are imperfect. \n      	Verify whether project-specific implementations perform all necessary validation steps:\n      \n 		* Is the certificate signed by a \"trusted\" CA?\n 		\n		* Is the certificate expired?\n		\n		* Has the certificate been revoked?\n		\n		* Is the host name matching the certificate‘s CN?\n\n		If not, check where these project-specific implementations are used ? \n		Just in test code or also in production code ?\n\nSee also CWE-295", "Review project-specific validation method.", 295, 3, -1),

XANITIZER_PROBLEM_TYPE_17("ci:CommandInjection", "Injection: Command Injection", "This security problem represents the dynamic construction of commands using data which originates from the web client.\n\nSee also CWE-77", "Check that only allowed characters are used, because the value can be controlled from the outside.", 77, 1, -1),

XANITIZER_PROBLEM_TYPE_51("ci:OSCommandInjection", "Injection: OS Command Injection", "This security problem represents the dynamic construction of operating system commands using data which originates from the web client.\n\nSee also CWE-78", "Check that only allowed characters are used, because the value can be controlled from the outside.", 78, 1, 2),

XANITIZER_PROBLEM_TYPE_18("ci:SQLInjection", "Injection: SQL Injection", "This security problem represents SQL-based database access through queries which are partly formed from external input.\n\nSee also CWE-89", "Use prepared statement or check that only allowed characters are used, because the value can be controlled from the outside.", 89, 1, 1),

XANITIZER_PROBLEM_TYPE_19("ci:XPathInjection", "Injection: XPath Injection", "This security problem represents the dynamic construction of XPath queries using data which originates from the web client.\n\nSee also CWE-91", "Check that only allowed characters are used, because the value can be controlled from the outside.", 91, 1, -1),

XANITIZER_PROBLEM_TYPE_20("ci:LDAPInjection", "Injection: LDAP Injection", "This security problem represents the LDAP protocol access using data which originates from the web client.\n\nSee also CWE-90", "Check that only allowed characters are used, because the value can be controlled from the outside.", 90, 1, -1),

XANITIZER_PROBLEM_TYPE_21("ci:LogInjection", "Injection: Log Injection", "This security problem represents the write access to log media, like log files, using data which originates from the web client.\n\nSee also CWE-117", "Check that only allowed characters are used, because the value can be controlled from the outside.", 117, 1, -1),

XANITIZER_PROBLEM_TYPE_22("hcc:HardCodedCredentials", "Hard-Coded Credentials: Credentials as Method Parameters", "This security problem represents an access to some protected system component using hard-coded access data.\n		   \n		   Do not additionally assign a taint source kind to this problem type. \n		   It would be superfluous in this scenario and has no effect.\n\nSee also CWE-798", "Remove credentials from the code.", 798, 2, 7),

XANITIZER_PROBLEM_TYPE_23("pt:PathTraversal", "Path Traversal: Manipulated File System Access", "This security problem represents the access to the file system using non-sanitized data which originates from an untrusted source.\n\nSee also CWE-22", "Check that only allowed characters are used, because the value can be controlled from the outside.", 22, 5, 13),

XANITIZER_PROBLEM_TYPE_24("xss:XSSFromDb", "XSS: Stored XSS", "This security problem represents the classic persistent cross-site scripting vulnerabilities.\n		   Tainted data is read from databases or other persistent storages and is delivered to a web client as part of a rendered view (e.g. html).\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value read from the database could be manipulated.", 79, 7, 4),

XANITIZER_PROBLEM_TYPE_25("xss:XSSFromDbUnrendered", "XSS: Stored XSS from Unrendered Output", "This security problem represents persistent cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data is read from databases or other persistent storages and is delivered to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value read from the database could be manipulated.", 79, 7, 4),

XANITIZER_PROBLEM_TYPE_26("xss:XSSFromRequest", "XSS: Reflected XSS", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities.\n		   Tainted data has been received from a web client and is delivered back to the web client as part of a rendered view (e.g. html).\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value can be controlled from the outside.", 79, 7, 4),

XANITIZER_PROBLEM_TYPE_27("xss:XSSFromRequestUnrendered", "XSS: Reflected XSS from Unrendered Output", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data has been received from a web client and is delivered back to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n\nSee also CWE-79", "Escape the string or check that only allowed characters are used, because the value can be controlled from the outside.", 79, 7, 4),

XANITIZER_PROBLEM_TYPE_52("or:URLRedirectAbuse", "URL Redirection Abuse", "This security problem is an abuse of redirection functionality. It occurs when user-provided and non-validated data are used to construct\n		   a URL for a redirection directive which is sent back from the application to the browser.\n\nSee also CWE-601", "Validate the value, because it can be controlled from the outside.", 601, -1, 22),

XANITIZER_PROBLEM_TYPE_29("pl:PrivacyLeak", "Privacy Leak: Output of Confidential Data", "This security problem refers to private or confidential data which are not properly protected against  \n		   unauthorized access. This might occur if e.g. password is written in clear text to the console or to any file (like a log file)\n		   or to the web response or to a SOAP output.\n\nSee also CWE-359", "Do not output private information.", 359, 3, -1),

XANITIZER_PROBLEM_TYPE_30("rhi:ResponseHeaderInjection", "Injection: Response Header Injection", "This security problem refers to manipulation of the HTTP response header through non neutralized data.\n		   This includes especially HTTP Response Splitting.\n\nSee also CWE-113", "Validate the value used for the HTTP header, because it can be controlled from the outside.", 113, 1, -1),

XANITIZER_PROBLEM_TYPE_31("cook:UnsecuredCookie", "Cookies: Unsecured Cookie", "This security problem occurs when the secure attribute is not set for a cookie, \n		   which could cause the user agent (browser) to send the cookie in plain text over an HTTP session.\n		   \n		   A non-encrypted cookie on the transportation layer can be easily read from a third party. \n		   It is strongly recommended to always set the secure attribute when a cookie should be sent via HTTPS only.\n\nSee also CWE-614", "Set the secure attribute by calling setSecure(true).", 614, 3, 8),

XANITIZER_PROBLEM_TYPE_32("cook:HttpOnlyCookie", "Cookies: No HttpOnly Cookie", "This security problem occurs when the HttpOnly flag is not set for a cookie.\n		   HttpOnly cookies are not supposed to be easily exposed to client-side scripting code, \n		   and may therefore help mitigate certain kinds of cross-site scripting attacks.\n		    \n		   Be aware that the HttpOnly flag could also be set by alternative means that are not detected by Xanitizer, \n		   like e.g. setting the entire cookie entry via setHeader or in the configuration of the application server.\n		   Such alternative usage could cause Xanitizer to wrongly report a missing HttpOnly flag.\n\nSee also CWE-1004", "Set the cookie to be HttpOnly.", 1004, 5, 17),

XANITIZER_PROBLEM_TYPE_33("xxe:GeneralEntities", "XXE: General Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external general entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.		\n		   \n		   External general entities can be turned off by invoking setFeature(\"http://xml.org/sax/features/external-general-entities\", false) on the used factory.   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n\nSee also CWE-611", "Do not include external general entities.", 611, 4, -1),

XANITIZER_PROBLEM_TYPE_34("xxe:DisallowDoc", "XXE: Disallow Doc - Xerces 2 only", "This regards Xerces 2 (JAXP 1.2 or higher) only. Deactivate this problem type if your project uses an earlier XML parser implementation.\n		   Xanitizer reports a problem when the configuration of a XML parser allows the access to external subsets in XML documents.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.\n		   \n		   Doctype declarations can be turned off by invoking setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true) on the used factory.\n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n\nSee also CWE-611", "Disallow doctype declaration.", 611, 4, -1),

XANITIZER_PROBLEM_TYPE_35("xxe:ParamEntities", "XXE: Param Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external parameter entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.\n		   \n		   External parameter entities can be turned off by invoking setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false) on the used factory.	   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n		   \n		   See also CWE-611.\n\nSee also CWE-611", "Do not include external parameter entities or the external DTD subset.", 611, 4, -1),

XANITIZER_PROBLEM_TYPE_36("xxe:XMLSecureProcessing", "XXE: XML Secure Processing", "This problem type reports all XML processing in the code without having explicitly set the XMLConstants.FEATURE_SECURE_PROCESSING flag\n		   which instructs JAXP components such as parsers, transformers and so on to behave in a secure fashion. \n		   \n		   Secure processing does not completely prevent XML eXternal Entities (XXE) processing, \n		   but it limits the maximum number of entity expansions and hence reduces the surface for DOS attacks. 	   \n		   Depending on the used JAXP implementation the 'secure processing' feature might be switched on by default, \n		   but to be sure about the status it is better to set it explicitly.\n		   \n		   You can activate this problem type as an alternative to the XXE problem types if your project requires external access in its XML documents \n		   and therefore cannot switch on the XXE protection mechanisms.\n\nSee also CWE-611", "Set the XMLConstants.FEATURE_SECURE_PROCESSING flag.", 611, 4, -1),

XANITIZER_PROBLEM_TYPE_41("idor:InsecureDirectObjectReferences", "Path Traversal: Insecure Direct Object References", "A direct object reference occurs when a reference to an internal implementation object, such as a file, directory, or database key, is exposed to a client. \n          Without an access control check or other protection, attackers can manipulate these references to access unauthorized data.\n          Therefore only indirect object references should be exposed which are internally mapped to the corresponding direct object references.\n          \n          CAUTION: There are no predefined taint sink methods in the assigned taint sink kind because Xanitizer does not know which database table columns are regarded as database keys (direct object references)\n		  and which columns are just attributes.\n          Therefore the appropriate sinks have to be configured project-specific in the assigned taint sink kind.\n\nSee also CWE-813", "Check access permissions.", 813, -1, -1),

XANITIZER_PROBLEM_TYPE_42("tbv:TrustBoundaryViolationSession", "Trust Boundary Violation: HTTP Session", "This problem type reports paths where tainted data is stored in the HTTP Session object of web applications without prior validation.\n		  This is an instance of the vulnerability 'Trust Boundary Violation' as described in CWE-501.\n\nSee also CWE-501", "Validate value before store it into the session object.", 501, -1, -1),

XANITIZER_PROBLEM_TYPE_50("ci:ReflectionInjection", "Injection: Reflection Injection", "This security problem represents the loading of code via reflection from user-defined data. \n		  This allows an attacker to load unexpected classes or methods, which can result in bypassing authentication \n		  and access control checks or in unexpected behavior of the application.\n		  \n		  If an attacker can upload files to a location in the class path, malicious behavior can be added to the application.\n		  \n		  This security problem is also known as Unsafe Reflection.\n\nSee also CWE-470", "Do not use user-controlled data to load code via reflection or apply strict input validation \n			to ensure that only the expected classes or methods are selected (whitelisting).", 470, -1, -1),

XANITIZER_PROBLEM_TYPE_53("id:InsecureDeserializationFromDb", "Insecure Deserialization (stored)", "The stream of serialized data could be tempered which could result in access control, denial-of-service and remote code execution attacks. \n			This security problem represents stored (persistent) insecure deserialization. \n			Tainted data is read from databases or other persistent storages.\n\nSee also CWE-502", "Use a subclass of OjectInputStream or a Java agent with a whitelist of desiralizable classes as sanitizer. \n			See also https://www.owasp.org/index.php/Deserialization_Cheat_Sheet for further details.", 502, 8, -1),

XANITIZER_PROBLEM_TYPE_54("id:InsecureDeserializationFromRequest", "Insecure Deserialization (reflected)", "The stream of serialized data could be tempered which could result in access control, denial-of-service and remote code execution attacks. \n			This security problem represents reflected (non-persistent) insecure deserialization. \n			Tainted data has been received from a web client.\n\nSee also CWE-502", "Use a subclass of OjectInputStream or a Java agent with a whitelist of desiralizable classes as sanitizer. \n			See also https://www.owasp.org/index.php/Deserialization_Cheat_Sheet for further details.", 502, 8, -1),

XANITIZER_PROBLEM_TYPE_43("XPathSearch:sessionTimeout", "Application Server: 'session-timeout' Too Large", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   This configuration can be done in web.xml. Xanitizer reports a warning if there is a timeout value set larger than 15 (minutes).\n\nSee also CWE-613", "Reduce session timeout.", 613, 2, -1),

XANITIZER_PROBLEM_TYPE_44("XPathSearch:sessionTimeoutNotConfigured", "Application Server: 'session-timeout' Not Configured", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   Xanitizer reports a warning if there is no configuration for session idle timeout in web.xml.\n\nSee also CWE-613", "Configure session timeout.", 613, 2, -1),

XANITIZER_PROBLEM_TYPE_45("XPathSearch:useHttpOnly", "Application Server: 'useHttpOnly' Disabled", "In context.xml it can be configured that the 'httpOnly' flag should be set on session cookies \n		   to prevent client side script from accessing the session ID.\n		   Xanitizer reports a warning if the 'useHttpOnly' flag has been set to false in context.xml.\n\nSee also CWE-1004", "Set 'useHttpOnly' flag to true.", 1004, 5, 17),

XANITIZER_PROBLEM_TYPE_46("XPathSearch:sessionConfigWOHttpOnlyAndSecureInCookieConfig", "Application Server: 'cookie-config' Not Properly Configured", "In web.xml it can be configured, in a 'cookie-config' element nested in a 'session-config' element, to prevent client side scripts from accessing the\n		   session ID and to force the browser to only use secure connections for transmission of the cookie.\n		   \n		   Xanitizer reports a finding if the web.xml 'cookie-config' element is not configured in a way that both, the contained\n		   'http-only' flag and 'secure' flag, are set to 'true'.\n\nSee also CWE-614", "Set the 'http-only' and the 'secure' flag to true.", 614, 3, 8),

XANITIZER_PROBLEM_TYPE_47("XPathSearch:sessionConfigWOTrackingModeCookieConfig", "Application Server: 'tracking-mode' Not Set to 'COOKIE'", "In web.xml it can be configured, in a 'tracking-mode' element nested in a 'session-config' element, that \n		   the session IDs are only sent via cookies, not via request parameter.  \n		   \n		   Xanitizer reports a finding if there is no such configuration in a 'session-config' element.\n\nSee also CWE-16", "Set 'tracking-mode' to 'COOKIE'.", 16, 6, -1),

XANITIZER_PROBLEM_TYPE_48("XPathSearch:sessionConfigMissing", "Application Server: 'session-config' Is Missing", "In web.xml, many security-relevant session properties should be configured in a 'session-config' element; \n		   Xanitizer reports if this 'session-config' element is not contained at all. A secure configuration looks like this:\n		   \n		   <web-app ...\n		   \n				<session-config>\n				\n				   <session-timeout>30</session-timeout>\n				   \n				   <cookie-config>\n				   \n				      <http-only>true</http-only>\n				\n				      <secure>true</secure>\n				      \n				   </cookie-config>\n				   \n				   <tracking-mode>COOKIE</tracking-mode>\n				   \n				</session-config>\n\nSee also CWE-613", "Add 'session-config' element.", 613, 2, -1),

XANITIZER_PROBLEM_TYPE_49("XPathSearch:exceptionTypeJavaLangThrowableMissing", "Application Server: 'error-page' or 'exception-type' Configuration Wrong or Missing", "In web.xml, a catch-all 'error-page' element should be defined that deals with all exceptions, i.e. is defined for exception-type java.lang.Throwable.\n		   This ensures that no internal error information is sent to the HTTP response by accident (where it could help a hacker to direct his attack).\n		   \n		   Since Servlet version 3.0, an error-page specification with neither exception-type nor error-code element also specifies a catch-all.\n		   \n		   A secure configuration looks like this:\n		   \n			  <error-page>\n			  \n			    <exception-type>java.lang.Throwable</exception-type>\n			    \n			    <location>/error.jsp</location>\n			    \n			  </error-page>\n\nSee also CWE-16", "Configure 'error-page' element.", 16, 6, -1),

XANITIZER_PROBLEM_TYPE_55("XPathSearch:directoryBrowsingEnabled", "Application Server: Directory Browsing Enabled", "In web.xml it can be configured in a 'listing' element nested in a 'servlet' element,\n			that the directories can be browsed when entering the directory name. \n			This can allow an attacker to bypass the authorization by directly invoking a visible file.\n\nSee also CWE-425", "Set the 'listing' flag to false.", 425, 2, 6),

    ;

    private final String id;
    private final String presentationName;
    private final String description;
    private final String message;
    private final int cwe;
    private final int owasp;
    private final int sans;
    private GeneratedProblemType(final String id, final String presentationName, final String description, final String message,
final int cwe, final int owasp, final int sans) {
         this.id = id;
         this.presentationName = presentationName;
         this.description = description;
         this.message = message;
         this.cwe = cwe;
         this.owasp = owasp;
         this.sans = sans;
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

     public String getDescription() {
     	return description;
     }
}
