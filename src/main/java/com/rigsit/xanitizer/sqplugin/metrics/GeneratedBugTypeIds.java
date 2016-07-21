package com.rigsit.xanitizer.sqplugin.metrics;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Generated from the Xanitizer standard problem definitions.
 * Contains all Xanitizer problem types together with their description.
 *
 * @author nwe
 *
 */
public class GeneratedBugTypeIds {
    private static final Map<String, Integer> GENERATED_BUG_TYPE_IDS = new LinkedHashMap<>();
    private static final Map<String, String> PRESENTATION_NAMES = new LinkedHashMap<>();

    static {
          GENERATED_BUG_TYPE_IDS.put("XPathSearch:sessionTimeout", 1);
          PRESENTATION_NAMES.put("XPathSearch:sessionTimeout", "Application Server Session-Timeout Too Large");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:sessionTimeoutNotConfigured", 2);
          PRESENTATION_NAMES.put("XPathSearch:sessionTimeoutNotConfigured", "Application Server Session-Timeout Not Configured");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:useHttpOnly", 3);
          PRESENTATION_NAMES.put("XPathSearch:useHttpOnly", "Application Server UseHttpOnly Disabled");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:sessionConfigWOHttpOnlyAndSecureInCookieConfig", 4);
          PRESENTATION_NAMES.put("XPathSearch:sessionConfigWOHttpOnlyAndSecureInCookieConfig", "Application Server: cookie-config in web.xml: Not Properly Configured");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:sessionConfigWOTrackingModeCookieConfig", 5);
          PRESENTATION_NAMES.put("XPathSearch:sessionConfigWOTrackingModeCookieConfig", "Application Server: tracking-mode in web.xml: Not Set to 'COOKIE'");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:sessionConfigMissing", 6);
          PRESENTATION_NAMES.put("XPathSearch:sessionConfigMissing", "Application Server: session-config in web.xml Missing");

          GENERATED_BUG_TYPE_IDS.put("XPathSearch:exceptionTypeJavaLangThrowableMissing", 7);
          PRESENTATION_NAMES.put("XPathSearch:exceptionTypeJavaLangThrowableMissing", "Application Server: error-page exception-type Configuration Wrong or Missing");

          GENERATED_BUG_TYPE_IDS.put("GWT:RegexSearch:JSNI", 8);
          PRESENTATION_NAMES.put("GWT:RegexSearch:JSNI", "GWT JSNI Usage");

          GENERATED_BUG_TYPE_IDS.put("RegexSearch:PasswordInConfigFile", 9);
          PRESENTATION_NAMES.put("RegexSearch:PasswordInConfigFile", "Password in config file");

          GENERATED_BUG_TYPE_IDS.put("RegexSearch:CredentialsInJavaSourceFile", 10);
          PRESENTATION_NAMES.put("RegexSearch:CredentialsInJavaSourceFile", "n Java source file");

          GENERATED_BUG_TYPE_IDS.put("GWT:FRAMEWORK_SIMULATION:UnusedRPCMethods", 11);
          PRESENTATION_NAMES.put("GWT:FRAMEWORK_SIMULATION:UnusedRPCMethods", "GWT Unused RPC Methods");

          GENERATED_BUG_TYPE_IDS.put("FRAMEWORK_SIMULATION:ExposeRequestAttributesToFreemarker", 12);
          PRESENTATION_NAMES.put("FRAMEWORK_SIMULATION:ExposeRequestAttributesToFreemarker", "Freemarker Expose Request Attributes");

          GENERATED_BUG_TYPE_IDS.put("USER:GeneralCodeIssue", 13);
          PRESENTATION_NAMES.put("USER:GeneralCodeIssue", "General Code Issue");

          GENERATED_BUG_TYPE_IDS.put("ssl:SpecialMethodOverwriting:SSL/TLSValidation", 14);
          PRESENTATION_NAMES.put("ssl:SpecialMethodOverwriting:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Implementation");

          GENERATED_BUG_TYPE_IDS.put("JNICall:All", 15);
          PRESENTATION_NAMES.put("JNICall:All", "Unsafe Java Native Interface");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:AppliedReflection", 16);
          PRESENTATION_NAMES.put("SpecialMethodCall:AppliedReflection", "Applied Java Reflection");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:java.util.Random", 17);
          PRESENTATION_NAMES.put("SpecialMethodCall:java.util.Random", "'java.util.Random' Used in Project");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:EncryptionUsedInProject", 18);
          PRESENTATION_NAMES.put("SpecialMethodCall:EncryptionUsedInProject", "Cryptographic Algorithms Used in Project");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:EncryptionUsedInProjectWOProvider", 19);
          PRESENTATION_NAMES.put("SpecialMethodCall:EncryptionUsedInProjectWOProvider", "Cryptographic Algorithms Used in Project, Provider Not Specified");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:JavaServletFindAndInclude", 20);
          PRESENTATION_NAMES.put("SpecialMethodCall:JavaServletFindAndInclude", "Java Servlet Forward and Include");

          GENERATED_BUG_TYPE_IDS.put("GWT:SpecialMethodCall:JSONParserCallingJSEval", 21);
          PRESENTATION_NAMES.put("GWT:SpecialMethodCall:JSONParserCallingJSEval", "GWT Client Side Unsafe Calls");

          GENERATED_BUG_TYPE_IDS.put("ssl:SpecialMethodCall:SSL/TLSValidation", 22);
          PRESENTATION_NAMES.put("ssl:SpecialMethodCall:SSL/TLSValidation", "SSL/TLS Validation: Suspicious Usage");

          GENERATED_BUG_TYPE_IDS.put("ci:CommandInjection", 23);
          PRESENTATION_NAMES.put("ci:CommandInjection", "Command Injection");

          GENERATED_BUG_TYPE_IDS.put("ci:SQLInjection", 24);
          PRESENTATION_NAMES.put("ci:SQLInjection", "SQL Injection");

          GENERATED_BUG_TYPE_IDS.put("ci:XPathInjection", 25);
          PRESENTATION_NAMES.put("ci:XPathInjection", "XPath Injection");

          GENERATED_BUG_TYPE_IDS.put("ci:LDAPInjection", 26);
          PRESENTATION_NAMES.put("ci:LDAPInjection", "LDAP Injection");

          GENERATED_BUG_TYPE_IDS.put("ci:LogInjection", 27);
          PRESENTATION_NAMES.put("ci:LogInjection", "Log Injection");

          GENERATED_BUG_TYPE_IDS.put("hcc:HardCodedCredentials", 28);
          PRESENTATION_NAMES.put("hcc:HardCodedCredentials", "Hard-Coded Credentials");

          GENERATED_BUG_TYPE_IDS.put("pt:PathTraversal", 29);
          PRESENTATION_NAMES.put("pt:PathTraversal", "Manipulated File System Access");

          GENERATED_BUG_TYPE_IDS.put("xss:XSSFromDb", 30);
          PRESENTATION_NAMES.put("xss:XSSFromDb", "XSS Stored");

          GENERATED_BUG_TYPE_IDS.put("xss:XSSFromDbUnrendered", 31);
          PRESENTATION_NAMES.put("xss:XSSFromDbUnrendered", "XSS Stored of Unrendered Output");

          GENERATED_BUG_TYPE_IDS.put("xss:XSSFromRequest", 32);
          PRESENTATION_NAMES.put("xss:XSSFromRequest", "XSS Reflected");

          GENERATED_BUG_TYPE_IDS.put("xss:XSSFromRequestUnrendered", 33);
          PRESENTATION_NAMES.put("xss:XSSFromRequestUnrendered", "XSS Reflected of Unrendered Output");

          GENERATED_BUG_TYPE_IDS.put("xss:URLRedirectAbuse", 34);
          PRESENTATION_NAMES.put("xss:URLRedirectAbuse", "URL Redirection Abuse");

          GENERATED_BUG_TYPE_IDS.put("pl:PrivacyLeak", 35);
          PRESENTATION_NAMES.put("pl:PrivacyLeak", "Privacy Leak");

          GENERATED_BUG_TYPE_IDS.put("rhi:ResponseHeaderInjection", 36);
          PRESENTATION_NAMES.put("rhi:ResponseHeaderInjection", "Response Header Injection");

          GENERATED_BUG_TYPE_IDS.put("cook:UnsecuredCookie", 37);
          PRESENTATION_NAMES.put("cook:UnsecuredCookie", "Unsecured Cookie");

          GENERATED_BUG_TYPE_IDS.put("cook:HttpOnlyCookie", 38);
          PRESENTATION_NAMES.put("cook:HttpOnlyCookie", "No HttpOnly Cookie");

          GENERATED_BUG_TYPE_IDS.put("xxe:GeneralEntities", 39);
          PRESENTATION_NAMES.put("xxe:GeneralEntities", "XXE: General Entities");

          GENERATED_BUG_TYPE_IDS.put("xxe:DisallowDoc", 40);
          PRESENTATION_NAMES.put("xxe:DisallowDoc", "XXE: Disallow Doc - Xerces 2 only");

          GENERATED_BUG_TYPE_IDS.put("xxe:ParamEntities", 41);
          PRESENTATION_NAMES.put("xxe:ParamEntities", "XXE: Param Entities");

          GENERATED_BUG_TYPE_IDS.put("xxe:XMLSecureProcessing", 42);
          PRESENTATION_NAMES.put("xxe:XMLSecureProcessing", "XML Secure Processing");

          GENERATED_BUG_TYPE_IDS.put("rl:IOStreamResourceLeak", 43);
          PRESENTATION_NAMES.put("rl:IOStreamResourceLeak", "IO Stream Resource Leak");

          GENERATED_BUG_TYPE_IDS.put("rl:SocketResourceLeak", 44);
          PRESENTATION_NAMES.put("rl:SocketResourceLeak", "Socket Resource Leak");

          GENERATED_BUG_TYPE_IDS.put("idor:InsecureDirectObjectReferences", 45);
          PRESENTATION_NAMES.put("idor:InsecureDirectObjectReferences", "Insecure Direct Object References");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:WeakEncryption", 46);
          PRESENTATION_NAMES.put("SpecialMethodCall:WeakEncryption", "Weak Cryptographic Algorithm");

          GENERATED_BUG_TYPE_IDS.put("SpecialMethodCall:WeakHash", 47);
          PRESENTATION_NAMES.put("SpecialMethodCall:WeakHash", "Weak Hash Algorithm");

          GENERATED_BUG_TYPE_IDS.put("tbv:TrustBoundaryViolationSession", 48);
          PRESENTATION_NAMES.put("tbv:TrustBoundaryViolationSession", "Trust Boundary Violation: HTTP Session");

    }

    private GeneratedBugTypeIds() {
        // hide constructor
    }

    /**
     *
     * Returns the presentation name for the given problem type id if it is registered.
     * Otherwise returns the id again.
     * @return the presentation name or the problem type id, if it is not registered
     *
     */
    public static String mkPresentationNameForBugTypeId(final String bugTypeId) {
        String candidate = PRESENTATION_NAMES.get(bugTypeId);
        if (candidate == null) {
		       candidate = bugTypeId;
        }
        return candidate;
    }

    /**
     *
     * Returns the map of registered bug types.
     * @return the map of problem types
     *
     */
    public static Map<String, Integer> getPredefinedBugTypeIdMap() {
        return GENERATED_BUG_TYPE_IDS;
    }
}
