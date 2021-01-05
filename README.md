# sonar-xanitizer
Integrates Xanitizer results into SonarQube

[![Build Status](https://travis-ci.org/RIGS-IT/sonar-xanitizer.svg?branch=master)](https://travis-ci.org/RIGS-IT/sonar-xanitizer)
 [![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=com.rigsit%3Asonar-xanitizer-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.rigsit%3Asonar-xanitizer-plugin)

- License: Apache License 2
- Author: [RIGS IT](https://www.rigs-it.com)

Requires at least Xanitizer 4.1

Xanitizer can be downloaded at <a href="http://www.xanitizer.com" >http://www.xanitizer.com</a>.

## Description
This plugin integrates the results of the Xanitizer security analysis into SonarQube. It parses an XML report created by Xanitizer and creates SonarQube issues for the non-informational security findings.


## Download and Installation
The easiest way to install the plugin is via the SonarQube Update Center / Marketplace.

If you want to install it manually, you can do it the following way:
1. Download the appropriate `sonar-xanitizer-plugin${version}.jar` file from the [releases page](https://github.com/RIGS-IT/sonar-xanitizer/releases)
2. Copy the plugin into `/path/to/sonarqube/extensions/plugins/`
3. Remove older versions of the plugin from `/path/to/sonarqube/extensions/plugins/`, if present
4. Restart SonarQube

## Integrating Xanitizer Results into SonarQube

1. Adapt the build system so that on the machine where SonarQube is running, Xanitizer is run on the projects to be analyzed in headless mode, and creates a findings list report as an XML output file. The plugin does not run Xanitizer, it just reads the results of previous Xanitizer runs.
2. Configure the location of the analysis results file of the project that should be displayed in SonarQube. This can be done in SonarQube's GUI:
	1. **Log in as administrator**.
	2. Select the project for which you want to specify the Xanitizer XML report file.
	3. Select the **“Administration”** menu entry in the project's area. **Note:** Do not use the global “Administration” menu item in the top-bar, but the project-specific one beneath the project name.
	4. Choose the **menu item “General Settings”**, and in the page that is shown then, at the left-hand side, click on the **category “External Analyzers”**.
	5. Specify the **XML report file** in the edit field labeled “Xanitizer Report File”. This is the findings list report output file that was created by the headless execution as described in the step above.
	6. Press the button labeled **“Save”** so that the setting is saved in SonarQube.
	7. You can also activate the option **Xanitizer Import All** to also import Xanitizer findings that were generated by the external tools SpotBugs and OWASP Dependency Check. To avoid redundant issues only turn this on when you do not use the SonarQube plugins of these tools. 
	
![alt tag](https://user-images.githubusercontent.com/20301605/47866655-acc83100-ddff-11e8-990f-934a1be9991f.png)

3. Activate Xanitizer's rules in the quality profile(s) that you are using. At the moment, Xanitizer provides Rules for Java, JavaScript and TypeScript. For each language that is relevant to you, you have to perform the following steps:
	1. Select the **“Rules”** menu item in the top-level bar.
	2. Activate the checkbox for **Tags**.
	3. Enter **“xanitizer”** in the search field. Then, only the Xanitizer rules are displayed in the right-hand area.
	4. Click on **“Bulk Change” > “Activate In...”** in the upper right corner. This allows to assign the displayed Xanitizer rules all at once to some quality profile (e.g. to the Java quality profile).
	5. In the dialog that opens, select the quality profile that you use for the selected programming language in your project.

![alt tag](https://cloud.githubusercontent.com/assets/20301605/17862219/97f4b98e-6894-11e6-9a63-2a0eaa9e3f0c.png)
	
## Adding the widget (Widget support has been removed since SonarQube 6.2 - this description is only valid for older versions)

In order to display the Xanitizer issues, a special widget is provided. It is activated in the following way:
1. Select the project for which you want to activate the widget.
2. Click on the **“Dashboards”** menu item (in the third bar, below the project name), and select the dashboard that you are using (“Custom” by default). This will show your dashboard.
3. Click on **“Configure widgets”** in the upper right corner.
4. In the yellow area, type “xanitizer” into the search field. Now only the Xanitizer widget can be selected. Click on **“Add widget”**, and then on **“Back to dashboard”** in the upper right corner to display the Xanitizer widget.

![alt tag](https://cloud.githubusercontent.com/assets/20301605/16995223/766968d0-4eab-11e6-88ec-8b1eb23d5d06.png)

## Mapping of Xanitizer Findings to SonarQube Issues

SonarQube issues are computed in the following way from Xanitizer findings:
- Only for findings with problem classifications (e. g. “Warning”, “Must fix” etc.), issues are generated.
- If the corresponding file of a Xanitizer finding could not be found a SonarQube issue will only be created, when the option **Xanitizer Import All** is turned on.
- Issues for SpotBugs and OWASP Dependency Check findings are only created, when the option **Xanitizer Import All** is turned on, because separate SonarQube plugins are available for these tools.
- For taint path findings, the location of the issue is the the taint sink. The taint source is registered as secondary location.
- For a single location, only one issue per problem type is created. So even if there are several taint paths with the same taint sink they result in one SonarQube issue (but with several taint sources as secondary locations).
- Issues for Xanitizer findings with classifications “must fix” and “urgent fix” get SonarQube severity “blocker”.
- For all other Xanitizer findings, the rating is used instead of the classification for determining the SonarQube severity:
	* Ratings larger than 7 are mapped to severity “critical”.
	* Ratings larger than 4, and up to 7, are mapped to severity “major”.
	* Ratings larger than 1, and up to 4, are mapped to severity “minor”.
	* Smaller ratings are mapped to severity “info”.

This means that for Xanitizer findings that are not classified as “must fix” or “urgent fix”, a rating larger than 7 must be assigned if the SonarQube issue corresponding to that finding should be considered to be “critical” in SonarQube.

## Issues for Resource Files

Some of the Special Code problem types analyze generic resource files with no corresponding programming language (e.g. to check the server configuration in the web.xml). To raise issues on these files, they have to be scanned by SonarQube. This can be reached by turning on the import of unknown files by setting the property "sonar.import_unknown_files" to "true" or <a href="http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-Unrecognizedfiles">enable it via GUI</a>. 


