/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2016 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This is meant for execution by hand.
 * 
 * Process all "std." files in Xanitizer's development tree, collect bug type
 * ids, and generate the necessary file for the SonarQube plugin.
 *
 * @author rust
 * 
 */
public class DoGenerateBugTypeIds {
	public static void main(final String[] args) throws IOException, XPathExpressionException {
		final File dir;
		if (args.length == 1) {
			dir = new File(args[0]).getCanonicalFile();
		} else {
			dir = new File("../com.rigsit.xanitizer.root.feature/rootfiles/all/problemDefinitions")
					.getCanonicalFile();
		}

		assert dir.isDirectory();

		final File tgtFile = new File(dir,
				"../../../../com.rigsit.xanitizer.sqplugin/src/main/java/com/rigsit/xanitizer/sqplugin/metrics/GeneratedBugTypeIds.java")
						.getCanonicalFile();

		/*
		 * We provide unique and constant numbers for the bug type ids, since in
		 * SonarQube, the metrics ids may not be very long.
		 * 
		 * These numbers may not change over time! That is why we read the old
		 * numbers from an old file.
		 */
		final Map<String, Integer> existingNumbers = new LinkedHashMap<>();
		final Map<String, String> existingPresentationNames = new LinkedHashMap<>();
		int largestSeenNumber = -1;

		final String anchorOne = "GENERATED_BUG_TYPE_IDS";
		final String anchorTwo = "PRESENTATION_NAMES";

		if (tgtFile.isFile()) {
			System.out.println("Overwriting existing target file " + tgtFile);

			final String oldContent = mkString(tgtFile, "UTF-8");
			int pos = 0;
			while (true) {
				final int foundAnchorOnePos = oldContent.indexOf(anchorOne, pos);
				final int foundAnchorTwoPos = oldContent.indexOf(anchorTwo, pos);
				if (foundAnchorOnePos == -1 && foundAnchorTwoPos == -1) {
					break;
				}

				if (foundAnchorTwoPos == -1
						|| foundAnchorOnePos != -1 && foundAnchorOnePos < foundAnchorTwoPos) {
					// Process anchor one.

					pos = foundAnchorOnePos + anchorOne.length();
					// Parse string and number starting from the position.
					final int dblQuotePos1 = oldContent.indexOf('"', pos);
					if (dblQuotePos1 == -1) {
						break;
					}
					pos = dblQuotePos1 + 1;

					final int dblQuotePos2 = oldContent.indexOf('"', pos);
					if (dblQuotePos2 == -1) {
						break;
					}
					pos = dblQuotePos2 + 1;

					final int commaPos = oldContent.indexOf(',', pos);
					if (commaPos == -1) {
						break;
					}
					pos = commaPos + 1;

					final int closingParenPos = oldContent.indexOf(')', pos);
					if (closingParenPos == -1) {
						break;
					}
					pos = closingParenPos + 1;

					final String key = oldContent.substring(dblQuotePos1 + 1, dblQuotePos2);
					final String numString = oldContent.substring(commaPos + 1, closingParenPos)
							.trim();

					final int num = Integer.parseInt(numString);

					existingNumbers.put(key, num);
					if (num > largestSeenNumber) {
						largestSeenNumber = num;
					}
				} else {
					// Process anchor two.

					pos = foundAnchorTwoPos + anchorTwo.length();
					// Parse string and another string starting from the
					// position.
					final int dblQuotePos1 = oldContent.indexOf('"', pos);
					if (dblQuotePos1 == -1) {
						break;
					}
					pos = dblQuotePos1 + 1;

					final int dblQuotePos2 = oldContent.indexOf('"', pos);
					if (dblQuotePos2 == -1) {
						break;
					}
					pos = dblQuotePos2 + 1;

					final int commaPos = oldContent.indexOf(',', pos);
					if (commaPos == -1) {
						break;
					}
					pos = commaPos + 1;

					final int dblQuotePos3 = oldContent.indexOf('"', pos);
					if (dblQuotePos3 == -1) {
						break;
					}
					pos = dblQuotePos3 + 1;

					final int dblQuotePos4 = oldContent.indexOf('"', pos);
					if (dblQuotePos4 == -1) {
						break;
					}
					pos = dblQuotePos4 + 1;

					final String key = oldContent.substring(dblQuotePos1 + 1, dblQuotePos2);
					final String presentationName = oldContent.substring(dblQuotePos3 + 2,
							dblQuotePos4);

					existingPresentationNames.put(key, presentationName);
				}
			}
		}

		final Map<String, String> bugTypeIdToPresentationNameOrNull = new LinkedHashMap<>();

		// Process all "std." files.
		for (final File candidate : dir.listFiles()) {
			if (candidate.isFile() && candidate.getName().startsWith("std.")) {
				collectBugTypeIdAndPresentationNames(candidate, bugTypeIdToPresentationNameOrNull);
			}
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("package com.rigsit.xanitizer.sqplugin.metrics;\n\n");
		sb.append("import java.util.LinkedHashMap;\n");
		sb.append("import java.util.Map;\n\n");
		sb.append("public class GeneratedBugTypeIds {\n");
		sb.append(
				"    public static final Map<String, Integer> GENERATED_BUG_TYPE_IDS = new LinkedHashMap<>();\n");
		sb.append(
				"    public static final Map<String, String> PRESENTATION_NAMES = new LinkedHashMap<>();\n");

		sb.append("    static {\n");

		// First, fill in the old mappings from bug type id to number.
		for (final Map.Entry<String, Integer> e : existingNumbers.entrySet()) {
			final String bugTypeId = e.getKey();

			sb.append("          " + anchorOne + ".put(\"" + bugTypeId + "\", " + e.getValue()
					+ ");\n");

			/*
			 * Look if there is a current presentation name for this bug type
			 * id.
			 */
			String presentationName = bugTypeIdToPresentationNameOrNull.get(bugTypeId);
			if (presentationName == null) {
				presentationName = existingPresentationNames.get(bugTypeId);
			}
			if (presentationName != null) {
				// Fill in presentation name.
				sb.append("          " + anchorTwo + ".put(\"" + bugTypeId + "\", \""
						+ presentationName + "\");\n");
			}
		}

		// Then, add new mappings.
		for (final Map.Entry<String, String> e : bugTypeIdToPresentationNameOrNull.entrySet()) {
			final String bugTypeId = e.getKey();
			final String presentationNameOrNull = e.getValue();

			if (!existingNumbers.containsKey(bugTypeId)) {
				// A new one.
				++largestSeenNumber;

				sb.append("          " + anchorOne + ".put(\"" + bugTypeId + "\", "
						+ largestSeenNumber + ");\n");

				if (presentationNameOrNull != null) {
					sb.append("          " + anchorTwo + ".put(\"" + bugTypeId + "\", \""
							+ presentationNameOrNull + "\");\n");
				}
			}
		}

		sb.append("    }\n");

		sb.append("}\n");

		try (

				final FileOutputStream fos = new FileOutputStream(tgtFile);

		) {
			fos.write(sb.toString().getBytes("UTF-8"));
		}
	}

	private static void collectBugTypeIdAndPresentationNames(final File candidate,
			final Map<String, String> bugTypeIdToPresentationNameAccu)
					throws IOException, XPathExpressionException {
		final Document doc = parse(candidate, false /* namespaceAware */,
				false /* collectLineNos */);

		final XPathFactory xPathfactory = XPathFactory.newInstance();

		final XPath xPath = xPathfactory.newXPath();

		final String[] specs = new String[] {
				/*
				 * List of XPath expressions that evaluate to the bug type
				 * element in predefined files, with (a) the attribute name of
				 * the bug type id, and (b) the attribute name of the
				 * presentation name.
				 */

				"//RegexSearch", "kind", "presentationName",

				"//XPathSearch", "kind", "presentationName",

				"//JNICalls", "kind", "presentationName",

				"//SpecialMethodCalls", "kind", "presentationName",

				"//SpecialMethodOverwritings", "kind", "presentationName",

				"//TaintFlowProblemType", "name", "presentationName",

				"//InternallyDefinedProblemType", "name", "presentationName",

		};

		for (int i = 0; i < specs.length; i += 3) {
			final String exprString = specs[i];
			final String bugTypeIdAttr = specs[i + 1];
			final String presentationNameAttr = specs[i + 2];

			final XPathExpression xPathExpression = xPath.compile(exprString);

			final NodeList nl = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
			for (int j = 0; true; ++j) {
				final Node node = nl.item(j);
				if (node == null) {
					break;
				}

				final String bugTypeId = node.getAttributes().getNamedItem(bugTypeIdAttr)
						.getNodeValue();
				if (!bugTypeId.isEmpty()) {
					String presentationNameOrNull = node.getAttributes()
							.getNamedItem(presentationNameAttr).getNodeValue();
					if (presentationNameOrNull.isEmpty()) {
						presentationNameOrNull = null;
					}
					bugTypeIdToPresentationNameAccu.put(bugTypeId, presentationNameOrNull);
				}
			}
		}
	}

	private static String mkString(final File f, final String encoding)
			throws UnsupportedEncodingException, IOException {
		final FileInputStream fis = new FileInputStream(f);
		try {
			return mkString(fis, encoding);
		} finally {
			fis.close();
		}
	}

	private static String mkString(final InputStream is, final String encoding)
			throws UnsupportedEncodingException, IOException {
		final StringBuilder sb = new StringBuilder();
		final InputStreamReader rdr = new InputStreamReader(new BufferedInputStream(is), encoding);
		int ch;
		while (-1 != (ch = rdr.read())) {
			sb.append((char) ch);
		}
		return sb.toString();
	}

	public static Document parse(final File inputFile, final boolean namespaceAware,
			final boolean collectLineNos) throws IOException {
		final InputStream is = new FileInputStream(inputFile);
		try {
			return parse(is, namespaceAware, collectLineNos, inputFile.toURI().toString());
		} finally {
			is.close();
		}
	}

	public static Document parse(final InputStream inputStream, final boolean namespaceAware,
			final boolean collectLineNos, final String documentURI) throws IOException {
		final Document doc;
		if (collectLineNos) {
			doc = parseCollectingLineNos(inputStream, namespaceAware);
		} else {
			doc = parseNotCollectingLineNos(inputStream, namespaceAware);
		}
		doc.setDocumentURI(documentURI);
		return doc;
	}

	private static Document parseNotCollectingLineNos(final InputStream inputStream,
			final boolean namespaceAware) throws IOException {
		try {

			final DocumentBuilder dBuilder = mkSafeDocumentBuilder(namespaceAware);
			dBuilder.setEntityResolver(new ContentIgnoringEntityResolver());
			return dBuilder.parse(inputStream);

		} catch (final SAXException ex) {
			throw new IOException(ex);
		}
	}

	public static DocumentBuilder mkSafeDocumentBuilder(final boolean namespaceAware)
			throws IOException {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringElementContentWhitespace(true);
		dbFactory.setNamespaceAware(namespaceAware);
		dbFactory.setValidating(false);
		try {
			// dbFactory.setFeature(GENERAL_ENTITIES, false);
			// dbFactory.setFeature(PARAM_ENTITIES, false);
			// dbFactory.setFeature(DISALLOW_DOC, true);
			// Not possible to make safe against XML eXternal Entities (XXE)
			// vulnerability
			// because completely disabling external entity resolution in XML
			// Processing leads to
			// ParseExceptions if e.g. external DTDs are provided in the XML
			// document.
			// Instead limit the number of external expansions by setting the
			// "secure processing" feature
			dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		} catch (final ParserConfigurationException ex) {
			// Just ignore and continue without having this feature set:
			// Should only occur in Junit Test with a different
			// implementation that is not supporting this feature
		}
		try {
			return dbFactory.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {
			throw new IOException(ex);
		}
	}

	private static Document parseCollectingLineNos(final InputStream inputStream,
			final boolean namespaceAware) throws IOException {
		try {

			final SAXSource saxSource;
			{
				final InputSource inputSource = new InputSource(inputStream);
				final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
				// Features are documented here:
				// xerces.apache.org/xerces2-j/features.html

				// Not possible to make safe against XML eXternal Entities (XXE)
				// vulnerability
				// because completely disabling external entity resolution in
				// XML Processing leads to
				// ParseExceptions if e.g. external DTDs are provided in the XML
				// document.
				// Instead limit the number of external expansions by setting
				// the "secure processing" feature
				try {
					xmlReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
				} catch (SAXNotRecognizedException e) {
					// Just ignore and continue without having this feature set:
					// Should only occur in Junit Test with a different
					// implementation that is not supporting this feature
				}

				// Do we want to process namespaces?
				xmlReader.setFeature("http://xml.org/sax/features/namespaces", namespaceAware);

				// No validation wanted.
				xmlReader.setFeature("http://xml.org/sax/features/validation", false);

				// Not even loading of DTDs is wanted.
				xmlReader.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				xmlReader.setEntityResolver(new ContentIgnoringEntityResolver());

				// Install a filter that adds line number information to the
				// attributes of each element.
				final LocationFilter locationFilter = new LocationFilter(xmlReader);
				saxSource = new SAXSource(locationFilter, inputSource);
			}

			final DOMResult domResult;
			{
				final TransformerFactory transformerFactory = TransformerFactory.newInstance();
				final Transformer transformer = transformerFactory.newTransformer();
				domResult = new DOMResult();
				transformer.transform(saxSource, domResult);
			}

			final Node result_ = domResult.getNode();
			assert result_ instanceof Document;
			final Document result = (Document) result_;
			return result;
		} catch (final SAXException | TransformerException ex) {
			throw new IOException(ex);
		}
	}

	public static class ContentIgnoringEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {
			return new InputSource(new ByteArrayInputStream(
					"<?xml version='1.0' encoding='UTF-8'?>".getBytes(Charset.forName("UTF-8"))));
		}
	}

	private static class LocationFilter extends XMLFilterImpl {

		private static final String HTTP_LOCAL_URI = "http://local";
		private static final String HTTP_LOCAL_URI_PFIX = "lcl";
		private static final String LINE_NO_ATTR_LOCALNAME = "lineno";
		private static final String LINE_NO_ATTR_QNAME = HTTP_LOCAL_URI_PFIX + ":"
				+ LINE_NO_ATTR_LOCALNAME;

		LocationFilter(XMLReader xmlReader) {
			super(xmlReader);
		}

		private Locator m_Locator = null;

		@Override
		public void setDocumentLocator(final Locator locator) {
			super.setDocumentLocator(locator);
			m_Locator = locator;
		}

		@Override
		public void startElement(final String uri, final String localName, final String qName,
				final Attributes attributes) throws SAXException {

			// Add extra attribute to elements to hold location
			final String lineNo = String.valueOf(m_Locator.getLineNumber());
			final Attributes2Impl attrs = new Attributes2Impl(attributes);
			attrs.addAttribute(HTTP_LOCAL_URI, LINE_NO_ATTR_LOCALNAME, LINE_NO_ATTR_QNAME, "CDATA",
					lineNo);
			super.startElement(uri, localName, qName, attrs);
		}
	}

}
