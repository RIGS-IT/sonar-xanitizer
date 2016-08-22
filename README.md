# sonar-xanitizer
Integrates Xanitizer results into SonarQube

[![Build Status](https://travis-ci.org/RIGS-IT/sonar-xanitizer.svg?branch=master)](https://travis-ci.org/RIGS-IT/sonar-xanitizer)
 [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.rigsit:sonar-xanitizer-plugin)](https://sonarqube.com/dashboard/index/com.rigsit:sonar-xanitizer-plugin)

- License: Apache License 2
- Author: [RIGS IT](https://www.rigs-it.net)

Compatible with Xanitizer 2.3

Check the GitHub Wiki for further information and the issues section for known problems.  

## Description
This plugin integrates the results of the Xanitizer security analysis into SonarQube. It parses an XML report created by Xanitizer and creates SonarQube issues for the non-informational security findings.

Xanitizer can be downloaded for free at <a href="http://www.xanitizer.net" >http://www.xanitizer.net</a>. 


## Download and Installation

1. Download the appropriate `sonar-xanitizer-plugin${version}.jar` file from the [releases page](https://github.com/RIGS-IT/sonar-xanitizer/releases)
2. Copy the plugin into `/path/to/sonarqube/extensions/plugins/`
3. Remove older versions of the plugin from `/path/to/sonarqube/extensions/plugins/`, if present
4. Restart SonarQube

## Integrating Xanitizer Results into SonarQube

1. Adapt the build system so that on the machine where SonarQube is running, Xanitizer is run on the projects to be analyzed in headless mode, and creates a findings list report as an XML output file. The plugin does not run Xanitizer, it just reads the results of previous Xanitizer runs.
2. Configure the location of the analysis results file of the project that should be displayed in SonarQube. This can be done in SonarQube's GUI:
	1. Log in as administrator.
	2. Select the project for which you want to specify the Xanitizer XML report file.
	3. Select the **“Administration”** menu entry in the project's area. **Note:** Do not use the global “Administration” menu item in the top-bar, but the project-specific one beneath the project name.
	4. Choose the **menu item “General Settings”**, and in the page that is shown then, at the left-hand side, click on the **category “Xanitizer”**.
	5. Specify the **XML report file** in the edit field labeled “Xanitizer XML Report File”. This is the findings list report output file that was created by the headless execution as described in the step above.
	6. Press the button labeled **“Save Xanitizer Settings”** so that the setting is saved in SonarQube.
	
![alt tag](https://cloud.githubusercontent.com/assets/20301605/17862408/61b13bbc-6895-11e6-8e58-54ed7711a381.png)

3. Activate Xanitizer's rules in the quality profile that you are using:
	1. Select the **“Rules”** menu item in the top-level bar.
	2. Enter **“xanitizer”** in the edit text at left-hand side. Then, only the Xanitizer rules are displayed in the right-hand area.
	3. Click on **“Bulk Change” > “Activate In...”** in the upper right corner. This allows to assign the displayed Xanitizer rules all at once to some quality profile.
	4. In the dialog that opens, select the quality profile that you use for your project.

![alt tag] (https://cloud.githubusercontent.com/assets/20301605/17862219/97f4b98e-6894-11e6-9a63-2a0eaa9e3f0c.png)
	
## Adding the widget

In order to display the Xanitizer issues, a special widget is provided. It is activated in the following way:
1. Select the project for which you want to activate the widget.
2. Click on the **“Dashboards”** menu item (in the third bar, below the project name), and select the dashboard that you are using (“Custom” by default). This will show your dashboard.
3. Click on **“Configure widgets”** in the upper right corner.
4. In the yellow area, type “xanitizer” into the search field. Now only the Xanitizer widget can be selected. Click on **“Add widget”**, and then on **“Back to dashboard”** in the upper right corner to display the Xanitizer widget.

![alt tag] (https://cloud.githubusercontent.com/assets/20301605/16995223/766968d0-4eab-11e6-88ec-8b1eb23d5d06.png)

## Mapping of Xanitizer Findings to SonarQube Issues

SonarQube issues are computed in the following way from Xanitizer findings:
- Only if a corresponding file could be found a SonarQube issue will be created for Xanitizer finding.
- Only for findings with problem classifications (e. g. “Warning”, “Must fix” etc.), issues are generated.
- Issues for FindBugs and OWASP Dependency Check findings are not created, because separate SonarQube plugins are available for these tools.
- For taint path findings, the location of the issue is the the taint sink. The taint source is registered as secondary location.
- For a single location, only one issue per problem type is created. So even if there are several taint paths with the same taint sink they result in one SonarQube issue (but with several taint sources as secondary locations).
- Issues for Xanitizer findings with classifications “must fix” and “urgent fix” get SonarQube severity “blocker”.
- For all other Xanitizer findings, the rating is used instead of the classification for determining the SonarQube severity:
	* Ratings larger than 7 are mapped to severity “critical”.
	* Ratings larger than 4, and up to 7, are mapped to severity “major”.
	* Ratings larger than 1, and up to 4, are mapped to severity “minor”.
	* Smaller ratings are mapped to severity “info”.

This means that for Xanitizer findings that are not classified as “must fix” or “urgent fix”, a rating larger than 7 must be assigned if the SonarQube issue corresponding to that finding should be considered to be “critical” in SonarQube.


