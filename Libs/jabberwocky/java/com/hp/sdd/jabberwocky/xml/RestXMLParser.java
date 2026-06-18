// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Generic Rest XML SAX Parser
 */
public final class RestXMLParser extends DefaultHandler {

	/** Current xml element data */
	private StringBuilder xmlTagData;
    /** XML tag handler */
	private RestXMLTagHandler xmlTagHandler;
    /** XML namespace handler */
	private RestXMLNSHandler xmlNSHandler;
    /** Current XML tag stack */
	private RestXMLTagStack xmlStack;

	/**
	 * Constructor and initializer for the Rest XML Parser
	 */
	public RestXMLParser() {
		// initialize the super class
		super();

		// initialize our variables
		xmlTagHandler = null;
		xmlNSHandler = null;
		xmlTagData = new StringBuilder();
		xmlStack = RestXMLTagStack.createStackForParsing();
	}

	/**
	 * Method used to configure the handlers used during parsing of the XML
	 * @param handler
	 *            Handler for XML tags to be used during parsing
	 * @param nsHandler
	 *            XML Namespace handler, used to store namespaces for later use
	 */
	public void setHandlers(RestXMLTagHandler handler, RestXMLNSHandler nsHandler) {
		// store the handlers
		xmlTagHandler = handler;
		xmlNSHandler = nsHandler;
	}

	/**
	 * Callback used by XML reader to notify about start document
     * @throws SAXException
     *              When an error is encountered
	 */
	@Override
	public void startDocument() throws SAXException {
		// invalidate any data we may have previously stored
		invalidateData();
		// clear the xml stack
		xmlStack.clear();
		if (xmlTagHandler != null)
			xmlTagHandler.parsingBegin();
	}

	/**
	 * Callback used by XML reader to notify about document end
     * @throws SAXException
     *              When an error is encountered
	 */
	@Override
	public void endDocument() throws SAXException {
		// invalidate any data we may have previously stored
		invalidateData();
		// clear the xml stack
		xmlStack.clear();
	}

	/**
	 * Callback used by XML reader to notify about a prefix mapping
     * @param uri
     *              The Namespace prefix being declared.
     * @param prefix
     *              The Namespace URI mapped to the prefix.
     * @throws SAXException
     *              When an error is detected
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// if a namespace handler is registered, store information about the
		// namespace
		if (xmlNSHandler != null)
			xmlNSHandler.addXMLNS(prefix, uri);
	}

    /**
     * Callback used by the XML reader to notify about the start of an XML element
     * @param uri
     *              The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
     * @param localName
     *              The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName
     *              The qualified name (with prefix), or the empty string if qualified names are not available.
     * @param attributes
     *              The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
     * @throws SAXException
     *              When an error is detected
     */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// invalidate any data we may have previously stored
		invalidateData();
		
		// push an entry into the stack
		xmlStack.push(uri,  localName);

		// has a handler been registered?
		if (xmlTagHandler != null) {
			// see if there's a start tag handler registered
			RestXMLTagHandler.XMLStartTagHandler startHandler = xmlTagHandler
					.getXMlStartHandler(localName);
			// if found, call the handler
			if (startHandler != null) {
				startHandler.process(xmlTagHandler, xmlStack, uri, localName, attributes);
			}
		}
	}
	
	/**
	 * Invalidate the contents of the XML data we've been informed about
	 */
	private void invalidateData()
	{
		// invalidate the contents
		xmlTagData.setLength(0);
		// trim the builder
		xmlTagData.trimToSize();
	}

	/**
	 * Callback used by the XML reader to notify about characters inside an XML element
     * @param ch
     *              The characters
     * @param start
     *              The start of position in the character array
     * @param length
     *              The number of characters to use from the character array
     * @throws SAXException
     *              When an error is detected
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// store entry data
		xmlTagData.append(ch, start, length);
	}

	/**
	 * Callback used by the XML reader to notify about the end of an XML element
     * @param uri
     *              The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
     * @param localName
     *              The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName
     *              The qualified name (with prefix), or the empty string if qualified names are not available.
     * @throws SAXException
     *              When an error is encountered
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// has a handler been registered?
		if (xmlTagHandler != null) {
			// see if there's an end tag handler registered
			RestXMLTagHandler.XMLEndTagHandler endHandler = xmlTagHandler
					.getXMLEndHandler(localName);
			// if found, call the handler
			if (endHandler != null) {
				endHandler.process(xmlTagHandler, xmlStack, uri, localName, xmlTagData.toString().trim());
			}
		}

		// remove an entry from the stack
		xmlStack.pop();

		// invalidate any data we may have previously stored
		invalidateData();
	}
}
