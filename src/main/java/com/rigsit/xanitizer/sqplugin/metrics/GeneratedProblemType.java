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

XanitizerProblemType_1("GWT:FRAMEWORK_SIMULATION:UnusedRPCMethods", "GWT Unused RPC Methods", "This problem type is GWT framework specific and searches for defined Remote Service RPC methods that are not \n			invoked by GWT client code and hence should not be exposed to web access.\n			\n			Probably those unused methods are not tested so well or are left over from development phase.\n			By de-obfuscating the client JavaScript code an attacker can detect the names of all Remote Service methods\n			and then could use them as a target for attacks."),

XanitizerProblemType_2("FRAMEWORK_SIMULATION:ExposeRequestAttributesToFreemarker", "Freemarker Expose Request Attributes", "This problem type is only relevant for projects which use the Freemarker template engine for response output rendering.\n		\n			If the 'exposeRequestAttributes' bean property of the FreeMarker bean in the Spring config is set to true, \n			then the request attributes are added to the model. In that scenario request attributes can be directly accessed in\n			Freemarker templates, without involvement of application code. This makes the web application potentially vulnerable to XSS."),

XanitizerProblemType_3("USER:GeneralCodeIssue", "General Code Issue", "This problem type serves as placeholder to assign it to user-defined findings, \n			which are not related to any other problem type. This could be for example quality issues in the code."),

XanitizerProblemType_4("JNICall:All", "Unsafe Java Native Interface", "By using the Java Native Interface (JNI) the Java code could import all the memory issues  \n			which are known from other languages. Java code is not any more protected against buffer overflow vulnerabilities when invoking native code.\n			Exploitation of such vulnerabilities occurs in the same way - regardless if accessed directly in the foreign code or through JNI.\n			\n			Therefore it is important to understand what kind of native methods are invoked. \n			This problem type searches in all workspace methods belonging to the callgraph and finds all locations where JNI is potentially used. \n			There could be false positives if an invocation occurs via super/interface class \n			and there are several sub classes of which just a subset has a native method."),

XanitizerProblemType_5("LiteralStringsInVariablesSearch:password", "Hard-Coded Credentials in Variables: password", "Checks for password variables if they are assigned constant strings."),

XanitizerProblemType_6("LiteralStringsInVariablesSearch:username", "Hard-Coded Credentials in Variables: username", "Checks for username variables if they assigned constant strings."),

XanitizerProblemType_7("GWT:RegexSearch:JSNI", "GWT JSNI Usage", "Checks the usage of the JavaScript Native Interface (JSNI) feature of GWT in the Java code.\n		   \n		   Take care in your native JSNI methods to not do anything that would expose you to attacks.\n		   Native JavaScript code embedded via JSNI might do unsafe things and therefore should be checked carefully.\n		   \n		   A JSNI block begins with the exact token /*-{ and Xanitizer reports an information finding for every JSNI block in the code."),

XanitizerProblemType_8("RegexSearch:PasswordInConfigFile", "Password in config file", "Checks for hard-coded passwords in config files.\n		  \n		  Hard-coded passwords in config files or other resource files are a risk factor."),

XanitizerProblemType_9("SpecialMethodCall:AppliedReflection", "Applied Java Reflection", "Searches in project code for all Java language methods which conduct Java reflection.\n		   The goal is to find out where reflection is applied in the code."),

XanitizerProblemType_10("SpecialMethodCall:java.util.Random", "'java.util.Random' Used in Project", "Instances of class 'java.util.Random' are not cryptographically secure. \n		   Consider instead using class 'java.security.SecureRandom' to get a cryptographically secure \n		   pseudo-random number generator for security-sensitive applications.\n		   \n		   See also CWE-330."),

XanitizerProblemType_11("SpecialMethodCall:WeakEncryption", "Weak Cryptographic Algorithm", "Searches in project code for methods which select a cryptographic algorithm that is known to be weak.\n		   \n		   See also CWE-327."),

XanitizerProblemType_12("SpecialMethodCall:WeakHash", "Weak Hash Algorithm", "Searches in project code for methods which select a hash algorithm that is known to be weak, like MD5 or SHA-1.\n		   \n		   See also CWE-328."),

XanitizerProblemType_13("SpecialMethodCall:EncryptionUsedInProject", "Cryptographic Algorithms Used in Project", "Searches in project code for all methods which set, select or determine a cryptographic algorithm.\n		   The goal is to find out which cryptographic algorithms are applied in the code."),

XanitizerProblemType_14("SpecialMethodCall:EncryptionUsedInProjectWOProvider", "Cryptographic Algorithms Used in Project, Provider Not Specified", "Searches in project code for all methods which set, select or determine a cryptographic algorithm and do not specify a \n		   crypto-provider for it, although an alternative method with a parameter for the provider is available.\n		   Not specifying the crypto-provider makes it uncertain which crypto-provider the server will use - it might be an unsafe one."),

XanitizerProblemType_15("SpecialMethodCall:JavaServletFindAndInclude", "Java Servlet Forward and Include", "This special method call kind determines where in the code Java servlet's RequestDispatcher forward and include methods\n		   (and affiliated methods) are applied.\n		   A Java servlet can forward a request from the current servlet to another resource \n		   and can include the content of another resource in the response of the current servlet.\n		   Usually the target resource is not known statically. In order to simulate the control flow correctly\n		   Xanitizer allows to specify the target resources for a current servlet. See more at \"intercepted methods\"."),

XanitizerProblemType_16("GWT:SpecialMethodCall:JSONParserCallingJSEval", "GWT Client Side Unsafe Calls", "Searches in project code for the invocation of methods that cause the browser to evaluate their input parameter for example when treated as HTML.\n 			If such input parameter contains untrusted data then the application is exposed to XSS.\n 			Unsafe methods are e.g. setHTML(String) or some constructors of GWT widgets, like HTML(String).\n 			It is recommended to use the analogical methods / constructors which are accepting SafeHTML as input parameter.\n 			\n 			Also methods, like JSONValue parse(String), which invoke the JavaScript eval() function for the input parameter are dangerous and should be avoided."),

XanitizerProblemType_17("ssl:SpecialMethodCall:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Usage", "Searches in the workspace for the invocation of methods which deal with SSL/TLS Validation \n      	and which could do the validation job not strict enough - because they are deprecated or dummy or self-made implementations. \n      	Are they invoked from test code only ? Or also from production code ?\n      	Check the documentation or the implementation of reported methods invoked in production code."),

XanitizerProblemType_18("ssl:SpecialMethodOverwriting:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Implementation", "Searches for project-specific implementations for SSL/TLS validation. \n      	Quite often such home-grown implementations are imperfect. \n      	Verify whether project-specific implementations perform all necessary validation steps:\n      \n 		* Is the certificate signed by a \"trusted\" CA?\n 		\n		* Is the certificate expired?\n		\n		* Has the certificate been revoked?\n		\n		* Is the hostname matching the certificate‘s CN?\n\n		If not, check where these project-specific implementations are used ? \n		Just in test code or also in production code ?"),

XanitizerProblemType_19("ci:CommandInjection", "Command Injection", "This security problem represents the dynamic construction of system commands using data which originates from the web client.\n		   \n		   See also CWE-78."),

XanitizerProblemType_20("ci:SQLInjection", "SQL Injection", "This security problem represents SQL-based database access through queries which are partly formed from external input.\n		    \n		   Use e.g \"Prepared Statements\" to protect against SQLI.\n		   \n		   \n		   See also CWE-89."),

XanitizerProblemType_21("ci:XPathInjection", "XPath Injection", "This security problem represents the dynamic construction of XPath queries using data which originates from the web client.\n		   \n		   See also CWE-643."),

XanitizerProblemType_22("ci:LDAPInjection", "LDAP Injection", "This security problem represents the LDAP protocol access using data which originates from the web client.\n		   \n		   See also CWE-90."),

XanitizerProblemType_23("ci:LogInjection", "Log Injection", "This security problem represents the write access to log media, like log files, using data which originates from the web client."),

XanitizerProblemType_24("hcc:HardCodedCredentials", "Hard-Coded Credentials", "This security problem represents an access to some protected system component using hard-coded access data.\n		   \n		   Do not additionally assign a taint source kind to this problem type. \n		   It would be superfluous in this scenario and has no effect."),

XanitizerProblemType_25("pt:PathTraversal", "Manipulated File System Access", "This security problem represents the access to the file system using non-sanitized data which originates from an untrusted source.\n		   \n		    See also CWE-22."),

XanitizerProblemType_26("xss:XSSFromDb", "XSS Stored", "This security problem represents the classic persistent cross-site scripting vulnerabilities.\n		   Tainted data is read from databases or other persistent storages and is delivered to a web client as part of a rendered view (e.g. html).\n		   \n		   See also CWE-79."),

XanitizerProblemType_27("xss:XSSFromDbUnrendered", "XSS Stored of Unrendered Output", "This security problem represents persistent cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data is read from databases or other persistent storages and is delivered to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n		   \n		   See also CWE-79."),

XanitizerProblemType_28("xss:XSSFromRequest", "XSS Reflected", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities.\n		   Tainted data has been received from a web client and is delivered back to the web client as part of a rendered view (e.g. html).\n		   \n		   See also CWE-79."),

XanitizerProblemType_29("xss:XSSFromRequestUnrendered", "XSS Reflected of Unrendered Output", "This security problem represents reflected (non-persistent) cross-site scripting vulnerabilities via unrendered data.\n		   Tainted data has been received from a web client and is delivered back to a client as non-HTML data (e.g. as JSON or SOAP).\n		   This is a non-typical XSS where it depends on the receiver whether the tainted data are harmful.\n		   \n		   See also CWE-79."),

XanitizerProblemType_30("xss:URLRedirectAbuse", "URL Redirection Abuse", "This security problem is an abuse of redirection functionality. It occurs when user-provided and non-validated data are used to construct\n		   a URL for a redirection directive which is sent back from the application to the browser."),

XanitizerProblemType_31("pl:PrivacyLeak", "Privacy Leak", "This security problem refers to private or confidential data which are not properly protected against  \n		   unauthorized access. This might occur if e.g. password is written in clear text to the console or to any file (like a log file)\n		   or to the web response or to a SOAP output."),

XanitizerProblemType_32("rhi:ResponseHeaderInjection", "Response Header Injection", "This security problem refers to manipulation of the HTTP response header through non neutralized data.\n		   This includes especially HTTP Response Splitting."),

XanitizerProblemType_33("cook:UnsecuredCookie", "Unsecured Cookie", "This security problem occurs when the secure attribute is not set for a cookie, \n		   which could cause the user agent (browser) to send the cookie in plain text over an HTTP session.\n		   \n		   A non-encrypted cookie on the transportation layer can be easily read from a third party. \n		   It is strongly recommended to always set the secure attribute when a cookie should be sent via HTTPS only.\n		   \n		   See also CWE-614."),

XanitizerProblemType_34("cook:HttpOnlyCookie", "No HttpOnly Cookie", "This security problem occurs when the httpOnly flag is not set for a cookie.\n		   HttpOnly cookies are not supposed to be easily exposed to client-side scripting code, \n		   and may therefore help mitigate certain kinds of cross-site scripting attacks.\n		    \n		   Be aware that the httpOnly flag could also be set by alternative means that are not detected by Xanitizer, \n		   like e.g. setting the entire cookie entry via setHeader or in the configuration of the application server.\n		   Such alternative usage could cause Xanitizer to wrongly report a missing HttpOnly flag."),

XanitizerProblemType_35("xxe:GeneralEntities", "XXE: General Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external general entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.		   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n		   \n		   See also CWE-611."),

XanitizerProblemType_36("xxe:DisallowDoc", "XXE: Disallow Doc - Xerces 2 only", "This regards Xerces 2 (JAXP 1.2 or higher) only. Deactivate this problem type if your project uses an earlier XML parser implementation.\n		   Xanitizer reports a problem when the configuration of a XML parser allows the access to external subsets in XML documents.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.		   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n		   \n		   See also CWE-611."),

XanitizerProblemType_37("xxe:ParamEntities", "XXE: Param Entities", "Xanitizer reports a problem when the configuration of a XML parser does not explicitly disable the resolution of external parameter entities.\n		   This is one (of three) mitigations for XML eXternal Entities (XXE) Processing.		   \n		   \n		   XML processing of an external entity containing tainted data may lead to disclosure of confidential information and other system impacts.\n		   \n		   See also CWE-611."),

XanitizerProblemType_38("xxe:XMLSecureProcessing", "XML Secure Processing", "This problem type reports all XML processing in the code without having explicitly set the XMLConstants.FEATURE_SECURE_PROCESSING flag\n		   which instructs JAXP components such as parsers, transformers and so on to behave in a secure fashion. \n		   \n		   Secure processing does not completely prevent XML eXternal Entities (XXE) processing, \n		   but it limits the maximum number of entity expansions and hence reduces the surface for DOS attacks. 	   \n		   Depending on the used JAXP implementation the 'secure processing' feature might be switched on by default, \n		   but to be sure about the status it is better to set it explicitly.\n		   \n		   You can activate this problem type as an alternative to the XXE problem types if your project requires external access in its XML documents \n		   and therefore cannot switch on the XXE protection mechanisms."),

XanitizerProblemType_39("rl:IOStreamResourceLeak", "IO Stream Resource Leak", "This problem type searches for IO streams in the code that are opened but never closed.\n		   This has a negative impact on performance and availability. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   In this analysis certain resources as e.g. HttpServletResponse.getOutputStream() are not supposed to be closed by the servlet. \n		   Instead the servlet container must do that in this example.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables."),

XanitizerProblemType_40("rl:SocketResourceLeak", "Socket Resource Leak", "This problem type reports paths where a socket is opened but never closed. \n		   Such unreleased resources can be classified as a security risk because they enable a denial-of-service attack.\n		   \n		   Xanitizer reports all code paths where the dataflow starts from a created resource instance and is leaving the validity scope of that resource without having it closed.\n		   This is the case when the dataflow reaches a point (marked as a sink) in the code after that no further access to the resource instance occurs. \n		   \n		   This resource leak search does not work if the resource object is stored in non-local variables."),

XanitizerProblemType_41("idor:InsecureDirectObjectReferences", "Insecure Direct Object References", "A direct object reference occurs when a reference to an internal implementation object, such as a file, directory, or database key, is exposed to a client. \n          Without an access control check or other protection, attackers can manipulate these references to access unauthorized data.\n          Therefore only indirect object references should be exposed which are internally mapped to the corresponding direct object references.\n          \n          CAUTION: There are no predefined sink methods in the assigned sink kind because Xanitizer does not know which db table columns are regarded as db keys (direct object references)\n		  and which columns are just attributes.\n          Therefore the appropriate sinks have to be configured project-specific in the assigned sink kind."),

XanitizerProblemType_42("tbv:TrustBoundaryViolationSession", "Trust Boundary Violation: HTTP Session", "This problem type reports paths where tainted data is stored in the HTTP Session object of web applications without prior validation.\n		  This is an instance of the vulnerability 'Trust Boundary Violation' as described in CWE-501."),

XanitizerProblemType_43("XPathSearch:sessionTimeout", "Application Server Session-Timeout Too Large", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   This configuration can be done in web.xml. Xanitizer reports a warning if there is a timeout value set larger than 15 (minutes)."),

XanitizerProblemType_44("XPathSearch:sessionTimeoutNotConfigured", "Application Server Session-Timeout Not Configured", "It is recommended, that for every web application a limit is globally configured for the session idle timeout.\n		   Xanitizer reports a warning if there is no configuration for session idle timeout in web.xml."),

XanitizerProblemType_45("XPathSearch:useHttpOnly", "Application Server UseHttpOnly Disabled", "In context.xml it can be configured that the httpOnly flag should be set on session cookies \n		   to prevent client side script from accessing the session ID.\n		   Xanitizer reports a warning if the useHttpOnly flag has been set to false in context.xml."),

XanitizerProblemType_46("XPathSearch:sessionConfigWOHttpOnlyAndSecureInCookieConfig", "Application Server: cookie-config in web.xml: Not Properly Configured", "In web.xml it can be configured, in a cookie-config element nested in a session-config element, to prevent client side scripts from accessing the\n		   session ID and to force the browser to only use secure connections for transmission of the cookie.\n		   \n		   Xanitizer reports a finding if the web.xml cookie-config element is not configured in a way that both, the contained\n		   http-only flag and secure flag, are set to 'true'."),

XanitizerProblemType_47("XPathSearch:sessionConfigWOTrackingModeCookieConfig", "Application Server: tracking-mode in web.xml: Not Set to 'COOKIE'", "In web.xml it can be configured, in a tracking-mode element nested in a session-config element, that \n		   the session IDs are only sent via cookies, not via request parameter.  \n		   \n		   Xanitizer reports a finding if there is\n		   no such configuration in a session-config element."),

XanitizerProblemType_48("XPathSearch:sessionConfigMissing", "Application Server: session-config in web.xml Missing", "In web.xml, many security-relevant session properties should be configured in a session-config element; \n		   Xanitizer reports if this session-config element is not contained at all. A secure configuration looks like this:\n		   \n		   <web-app ...\n		   \n				<session-config>\n				\n				   <session-timeout>30</session-timeout>\n				   \n				   <cookie-config>\n				   \n				      <http-only>true</http-only>\n				\n				      <secure>true</secure>\n				      \n				   </cookie-config>\n				   \n				   <tracking-mode>COOKIE</tracking-mode>\n				   \n				</session-config>"),

XanitizerProblemType_49("XPathSearch:exceptionTypeJavaLangThrowableMissing", "Application Server: error-page exception-type Configuration Wrong or Missing", "In web.xml, a catch-all error-page element should be defined that deals with all exceptions, i.e. is defined for exception-type java.lang.Throwable.\n		   This ensures that no internal error information is sent to the HTTP response by accident (where it could help a hacker to direct his attack.)\n		   A secure configuration looks like this:\n		   \n			  <error-page>\n			  \n			    <exception-type>java.lang.Exception</exception-type>\n			    \n			    <location>/error.jsp</location>\n			    \n			  </error-page>"),

    ;

    private final String m_Id;
    private final String m_Name;
    private final String m_Description;
    private GeneratedProblemType(final String id, final String name, final String description) {
         m_Id = id;
         m_Name = name;
         m_Description = description;
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
     	return m_Id;
     }

     public String getName() {
     	return m_Name;
     }

     public String getDescription() {
     	return m_Description;
     }
}
