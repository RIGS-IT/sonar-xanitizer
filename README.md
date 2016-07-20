# sonar-xanitizer
Integrates Xanitizer results into SonarQube

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
5. Adapt the build system so that on the machine where SonarQube is running, Xanitizer is run on the projects to be analyzed in headless mode, and creates a findings list report as an XML output file. The plugin does not run Xanitizer, it just reads the results of previous Xanitizer runs.
6. Configure the location of the analysis results file of the project that should be displayed in SonarQube. This can be done in SonarQube's GUI:
	a) Log in as administrator.
	b) Select the project for which you want to specify the Xanitizer XML report file.
	c) Select the “Administration” menu entry in the project's area. Note: Do not use the global “Administration” menu item in the top-bar, but the project-specific one beneath the project name.
	d) Choose the menu item “General Settings”, and in the page that is shown then, at the left-hand side, click on the category “Xanitizer”.
	e) Specify the XML report file in the edit field labeled “Xanitizer XML Report File”. This is the findings list report output file that was created by the headless execution as described in the step above.
	f) Press the button labeled “Save Xanitizer Settings” so that the setting is saved in SonarQube.
7. Activate Xanitizer's rules in the quality profile that you are using