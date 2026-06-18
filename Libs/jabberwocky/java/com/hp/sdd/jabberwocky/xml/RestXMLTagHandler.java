// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import java.util.HashMap;

import org.xml.sax.Attributes;

import android.text.TextUtils;

/**
 * XML tag handler
 */
@SuppressWarnings("unused")
public final class RestXMLTagHandler {

	/**
	 * Interface definition for callback that gets after the start of a registered
	 * XML tag element is detected
	 */
	public interface XMLStartTagHandler {
		/**
		 * Called to allow the registered client to do any tag specific processing
         * @param handler
         * 				Handler that invoked the callback
         * @param xmlTagStack
         * 				Current XML stack, can be used to lookup previous
         * @param uri
* 				        Namespace URI of the XML element
         * @param localName
* 				        XML element tag
         * @param attributes
         *              XML attributes associated with element
         */
		void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                     String localName, Attributes attributes);
	}

	/**
	 * Interface definition for callback that gets after the end of a registered
	 * XML tag element is detected
	 */
	public interface XMLEndTagHandler {
		/**
		 *  Called to allow the registered client to do any tag specific processing
         * @param handler
         * 				Handler that invoked the callback
         * @param xmlTagStack
         * 				Current XML stack, can be used to lookup previous
         * @param uri
* 				        Namespace URI of the XML element
         * @param localName
* 				        XML element tag
         * @param data
         *              Data associated with XML element
         *
         */
		void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                     String localName, String data);
	}

	/**
	 * Thread specific generic start tag handler
	 */
	private ThreadLocal<XMLStartTagHandler> xmlGenericStartTagHandler;
	/**
	 * Thread specific generic end tag handler
	 */
	private ThreadLocal<XMLEndTagHandler> xmlGenericEndTagHandler;

	/**
	 * Default list of start tag handlers
	 */
	private HashMap<String, XMLStartTagHandler> globalXMLStartTagHandlers;
	/**
	 * Default list of end tag handlers
	 */
	private HashMap<String, XMLEndTagHandler> globalXMLEndTagHandlers;
	/**
	 * Default generic start tag handler
	 */
	private XMLStartTagHandler globalXMLGenericStartTagHandler;
	/**
	 * Default generic end tag handler
	 */
	private XMLEndTagHandler globalXMLGenericEndTagHandler;
	/**
	 * Thread specific start tag handlers
	 */
	private ThreadLocal<HashMap<String, XMLStartTagHandler>> xmlStartTagHandlers;
	/**
	 * Thread specific start end handlers
	 */
	private ThreadLocal<HashMap<String, XMLEndTagHandler>> xmlEndTagHandlers;
	/**
	 * Thread specific tag data
	 */
	private ThreadLocal<HashMap<String, Object>> handlerTagData;
    /**
     * Thread specific indicator if parsing was started
     */
	private ThreadLocal<Boolean> parsingStarted;
    /**
     * Flag to check if parsing has been executed at least once
     */
    private boolean parsedAtLeastOnce = false;

	/**
	 * Constructor and initializer for the XML tag handler
	 */
	public RestXMLTagHandler() {
		
		// create global variables
		globalXMLStartTagHandlers       = new HashMap<String, XMLStartTagHandler>();
		globalXMLEndTagHandlers         = new HashMap<String, XMLEndTagHandler>();
		globalXMLGenericStartTagHandler = null;
		globalXMLGenericEndTagHandler   = null;
		
		// Create thread specific handlers
		xmlStartTagHandlers       = new ThreadLocal<HashMap<String, XMLStartTagHandler>>();
		xmlEndTagHandlers         = new ThreadLocal<HashMap<String, XMLEndTagHandler>>();
		handlerTagData            = new ThreadLocal<HashMap<String, Object>>();
		xmlGenericStartTagHandler = new ThreadLocal<XMLStartTagHandler>();
		xmlGenericEndTagHandler   = new ThreadLocal<XMLEndTagHandler>();
		parsingStarted            = new ThreadLocal<Boolean>();
	}

	/**
	 * Method to configure start/end handler for a particular XML tag
	 * 
	 * @param xmlTag
	 *            XML tag getting configured
	 * @param startTagHandler
	 *            Handler to be called when encountering the start of the
	 *            specified XML tag, may be null
	 * @param endTagHandler
	 *            Handler to be called when encountering the end of the
	 *            specified XML tag, may be null
	 */
	public void setXMLHandler(String xmlTag,
			RestXMLTagHandler.XMLStartTagHandler startTagHandler,
			RestXMLTagHandler.XMLEndTagHandler endTagHandler) {
		
		// sanity check on the xml tag
		if (!TextUtils.isEmpty(xmlTag))
		{
			if (parsedAtLeastOnce)
			{
				HashMap<String, XMLStartTagHandler> startTagHandlers = xmlStartTagHandlers.get();
				if (startTagHandlers == null)
				{
					startTagHandlers = new HashMap<String, XMLStartTagHandler>();
					xmlStartTagHandlers.set(startTagHandlers);
				}
				HashMap<String, XMLEndTagHandler> endTagHandlers = xmlEndTagHandlers.get();
				if (endTagHandlers == null)
				{
					endTagHandlers = new HashMap<String, XMLEndTagHandler>();
					xmlEndTagHandlers.set(endTagHandlers);
				}
				// Put the handlers into the hash maps
				startTagHandlers.put(xmlTag, startTagHandler);
				endTagHandlers.put(xmlTag, endTagHandler);
			}
			else
			{
				globalXMLStartTagHandlers.put(xmlTag, startTagHandler);
				globalXMLEndTagHandlers.put(xmlTag, endTagHandler);
			}
		}
	}

	/**
	 * Method that allows a client to remove XML tag handlers
	 * @param xmlTag
	 *            XML tag whose handlers are being removed
	 */
	public void removeXMLHandlers(String xmlTag) {
		// remove references to the tag from the hash tables
		if (parsedAtLeastOnce)
		{
			HashMap<String, XMLStartTagHandler> startTagHandlers = xmlStartTagHandlers.get();
			if (startTagHandlers != null)
				startTagHandlers.remove(xmlTag);
			HashMap<String, XMLEndTagHandler> endTagHandlers = xmlEndTagHandlers.get();
			if (endTagHandlers != null)
				endTagHandlers.remove(xmlTag);
		}
		else
		{
			globalXMLStartTagHandlers.remove(xmlTag);
			globalXMLEndTagHandlers.remove(xmlTag);
		}
	}

	/**
	 * Method to configure a generic handlers that should get called when a tag
	 * specific has not been set
	 * @param startTagHandler
	 *            Handler to be called when a tag specific start handler has not
	 *            been set, may be null
	 * @param endTagHandler
	 *            Handler to be called when a tag specific end handler has not
	 *            been set, may be null
	 */
	public void setGenericXMLHandler(
			RestXMLTagHandler.XMLStartTagHandler startTagHandler,
			RestXMLTagHandler.XMLEndTagHandler endTagHandler) {
		if (parsedAtLeastOnce)
		{
			// Store the generic handlers
			xmlGenericStartTagHandler.set(startTagHandler);
			xmlGenericEndTagHandler.set(endTagHandler);
		}
		else
		{
			globalXMLGenericStartTagHandler = startTagHandler;
			globalXMLGenericEndTagHandler = endTagHandler;
		}
	}

	/**
	 * Method use to retrieve a start tag handler for the specified tag
	 * @param xmlTag
	 *            The XML tag to lookup a handler for
	 * @return
     *              The configured start tag handler, may be null
	 */
	RestXMLTagHandler.XMLStartTagHandler getXMlStartHandler(String xmlTag) {
		// Check to see if we have a handler for the specified tag
		if (xmlStartTagHandlers.get().containsKey(xmlTag)) {
			// return the handler for the specified tag
			return xmlStartTagHandlers.get().get(xmlTag);
		} else {
			// return the generic handler or null if it hasn't been set
			return xmlGenericStartTagHandler.get();
		}
	}

	/**
	 * Method use to retrieve an end tag handler for the specified tag
	 * @param xmlTag
	 *            The XML tag to lookup a handler for
	 * @return
     *              The configured end tag handler, may be null
	 */
	RestXMLTagHandler.XMLEndTagHandler getXMLEndHandler(String xmlTag) {
		// Check to see if we have a handler for the specified tag
		if (xmlEndTagHandlers.get().containsKey(xmlTag)) {
			// return the handler for the specified tag
			return xmlEndTagHandlers.get().get(xmlTag);
		} else {
			// return the generic handler or null if it hasn't been set
			return xmlGenericEndTagHandler.get();
		}
	}

	/**
	 * Method to cleanup stored data from a previous XML parsing run
	 */
	public void cleanupData() {
		// clear out any previously stored data
		handlerTagData.remove();
		xmlStartTagHandlers.remove();
		xmlEndTagHandlers.remove();
		xmlGenericStartTagHandler.remove();
		xmlGenericEndTagHandler.remove();
		parsingStarted.remove();
	}

    /**
     * Method to retrieve stored data for a specified XML tag
     * @param tag
     *              Tag to retrieved stored data for
     * @param defaultValue
     *              Value to return if tag is not found
     * @return
     *              The value of the previously stored data
     */
	public Object getTagData(String tag, Object defaultValue) {
		return getTagData(tag, defaultValue, false);
	}

	/**
	 * Method to retrieve stored data for a specified XML tag
	 * @param tag
	 *              Tag to retrieved stored data for
	 * @param defaultValue
	 *              Value to return if tag is not found
	 * @param storeDefault
	 * 		  Store default value if used
	 * @return
	 *              The value of the previously stored data
	 */
	public Object getTagData(String tag, Object defaultValue, boolean storeDefault) {
		// check that we have a thread specific hash map
		HashMap<String, Object> tagData = handlerTagData.get();
		if (tagData == null) {
			if (storeDefault) {
                setTagData(tag, defaultValue);
			}
			return defaultValue;
		}
		// check what tag was provided
		if (tag != null) {
			Object value = tagData.containsKey(tag) ? tagData.get(tag) : defaultValue;
			if (tagData.containsKey(tag)) {
				return tagData.get(tag);
			} else {
				if (storeDefault) {
                    setTagData(tag, defaultValue);
				}
				return defaultValue;
			}
		}
		// return the data
		return defaultValue;
	}

	/**
	 * Method to retrieve stored data for a specified XML tag
	 * @param tag
     *              Tag to retrieved stored data for
	 * @return
     *              The value of the previously stored data
	 */
	public Object getTagData(String tag) {
		return getTagData(tag, null, false);
	}

	/**
	 * Method used to store data for a specified XML tag
	 * @param tag
	 *            Tag to store data for
	 * @param data
	 *            the data to store
	 */
	public void setTagData(String tag, Object data) {
		// check what tag was provided
		if (tag != null) {
			// 
			HashMap<String, Object> tagData = handlerTagData.get();
			if (tagData == null)
			{
				// create and store the thread specific tag hash map
				tagData = new HashMap<String, Object>();
				handlerTagData.set(tagData);
			}
			// store the provided data for the tag
			tagData.put(tag, data);
		}
	}

	/**
	 * Remove data associated with a specific XML tag
	 * @param tag
     *              Tag to clean data for
     */
	public void clearTagData(String tag) {
		// check what tag was provided
		if (tag != null) {
			//
			HashMap<String, Object> tagData = handlerTagData.get();
			if (tagData != null) {
				tagData.remove(tag);
			}
		}
	}

	/**
	 * Check if data is available for the specified tag
	 * @param tag
	 *              Tag to check data for
	 * @return true if data is available, false otherwise
	 */
	public boolean containsTagData(String tag) {
		HashMap<String, Object> tagData = handlerTagData.get();
		return ((tagData != null) && tagData.containsKey(tag));
	}

    /**
     * Start parsing
     */
	synchronized void parsingBegin()
	{
		parsedAtLeastOnce = true;
		
		if (parsingStarted.get() != null)
			return;
		
		// mark parsing as started
		parsingStarted.set(true);

		// setup thread specific tag data
		if (handlerTagData.get() == null)
			handlerTagData.set(new HashMap<String, Object>());
		
		// setup thread specific start tag handlers
		if (xmlStartTagHandlers.get() == null)
			xmlStartTagHandlers.set(new HashMap<String, XMLStartTagHandler>(globalXMLStartTagHandlers));
		else
			xmlStartTagHandlers.get().putAll(globalXMLStartTagHandlers);
		
		// setup thread specific end tag handlers
		if (xmlEndTagHandlers.get() == null)
			xmlEndTagHandlers.set(new HashMap<String, XMLEndTagHandler>(globalXMLEndTagHandlers));
		else
			xmlEndTagHandlers.get().putAll(globalXMLEndTagHandlers);
		
		// setup thread specific generic tag handlers
		if (xmlGenericStartTagHandler.get() == null)
			xmlGenericStartTagHandler.set(globalXMLGenericStartTagHandler);
		if (xmlGenericEndTagHandler.get() == null)
			xmlGenericEndTagHandler.set(globalXMLGenericEndTagHandler);
	}
}
