// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import android.text.TextUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class for writing Rest XML requests
 */
@SuppressWarnings("unused")
public final class RestXMLWriter {

    /** XML header */
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    /** String string */
    private static final int DEFAULT_STRING_SIZE = 4096;
    
    /**
     * Helper class that allows clients to add XML tag attributes
     */
    @SuppressWarnings("unused")
    public static final class XMLAttributes
    {
        /**
         * Container class for an XML tag attribute
         */
        class Attribute implements Comparable<Attribute>
        {
            /** Prefix/Namespace for the attribute */
            final String prefix;
            /** Attribute name */
            final String name;
            /** Attribute value */
            final String value;
            /**
             * Default Attribute constructor
             * @param prefix
             *             Prefix/Namespace of the attribute, may be null 
             * @param name
             *             Attribute name
             * @param value
             *             Attribute value
             */
            private Attribute(String prefix, String name, String value)
            {
                // store the values
                // store something for the prefix
                this.prefix = ((prefix == null) ? "" : prefix);
                // store the name
                this.name   = name;
                // store something for the value
                this.value  = ((value == null) ? "" : value);
            }

            /**
             *
             * @param another
             *              Other attribute to compare to
             * @return
             *              true if attributes match
             *              false otherwise
             */
            @Override
            public int compareTo(Attribute another) {
                // compare by name
                int result = this.name.compareTo(another.name);
                // if names match, then compare by prefix
                if (result == 0)
                    result = this.prefix.compareTo(another.prefix);
                // return the result
                return result;
            }
        }

        /** list to hold attributes */
        private final ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
        
        /**
         * Default constructor for XML Attributes
         */
        public XMLAttributes()
        {

        }
        
        /**
         * Method that allows a client to add an attribute
         * @param prefix
         *             Prefix/Namespace to use for the attribute, may be null
         * @param name
         *             Attribute name
         * @param value
         *             Attribute value, may be null
         */
        public XMLAttributes add(String prefix, String name, String value)
        {
            // make sure the request at least has a name
            if (!TextUtils.isEmpty(name))
                attributeList.add(new Attribute(prefix, name, value));
            return this;
        }
        
        /**
         * Method to clear attributes
         */
        public XMLAttributes clear() {
            attributeList.clear();
            return this;
        }

        /**
         * Return the list of attributes
         * @return
         *              List of attributes
         */
        public ArrayList<Attribute> getAttributeList()
        {
            return attributeList;
        }
    }

    /**
     * XML payload builder
     */
    private final StringBuilder xmlData;
    /**
     * Current XML stack
     */
    private final RestXMLTagStack tagStack;
    /**
     * Format XML for readability?
     */
    private final boolean mPrettyFormat;

    /**
     * Namespace list to use for payload
     */
    private final ArrayList<String> xmlNamespaceList;
    /**
     * Namespace/prefix mappings
     */
    private final HashMap<String,String> xmlPrefixMap;

    /**
     * Rest XML Writer constructor
     * @param nsHandler
     *                 XML namespace handler used by the device instance
     * @param prettyFormat
     *                 Format XML for readability
     * @param namespaces
     *                 Variable list of XML namespaces to include in the XML header tag
     * @throws IllegalStateException
     *                  if a requested XML namespace entry has not been found
     * @throws InvalidParameterException
     *                  if no XML namespace handler is provided but namespace tags are
     */
    public RestXMLWriter(RestXMLNSHandler nsHandler, boolean prettyFormat, String... namespaces)
            throws IllegalStateException, InvalidParameterException {

        // sanity check
        if ((nsHandler == null) && (namespaces.length > 0))
            throw new InvalidParameterException("No namespace handler provided");

        mPrettyFormat = prettyFormat;

        tagStack = RestXMLTagStack.createStackForWriting();

        // create a list so we can store the known namespaces
        xmlNamespaceList = new ArrayList<String>();

        // create a hash map so we can store the known prefixes
        xmlPrefixMap = new HashMap<String,String>();

        // create the string builder
        xmlData = new StringBuilder(DEFAULT_STRING_SIZE);

        // add all the provided namespace entries
        for (String nsEntry : namespaces) {
            // grab the entry
            // add the namespace entry
            addNamespace(nsHandler, nsEntry);
        }
    }

    /**
     * Rest XML Writer constructor
     * @param nsHandler
     *                 XML namespace handler used by the device instance
     * @param namespaces
     *                 Variable list of XML namespaces to include in the XML header tag
     * @throws IllegalStateException
     *                  if a requested XML namespace entry has not been found
     * @throws InvalidParameterException
     *                  if no XML namespace handler is provided but namespace tags are
     */
    public RestXMLWriter(RestXMLNSHandler nsHandler, String... namespaces)
            throws IllegalStateException, InvalidParameterException {
        this(nsHandler, true, namespaces);
    }
    
    /**
     * Helper function that adds a namespace entry to the xml header
     * @param nsHandler
     *                 XML namespace handler used by the device instance
     * @param ns
     *                 Namespace we are attempting to add
     * @throws InvalidParameterException
     *                 if the specified namespace prefix has already been added
     *                 with a different value
     */
    private void addNamespace(RestXMLNSHandler nsHandler, String ns) throws InvalidParameterException
    {
        // lookup the requested namespace
        String xmlNS = nsHandler.getXMLns(ns);
        if (TextUtils.isEmpty(xmlNS)) {
            // didn't find the requested namespace
            throw new IllegalStateException(
                    "Could not find entry for namespace entry: " + ns);
        }

        // split the request to grab the prefix
        String[] temp = ns.split(RestXMLNSHandler.XML_SEPARATOR);
        
        // grab the prefix
        String prefix = temp[0];
        
        // check for a previous entry
        String previousEntry = xmlPrefixMap.get(prefix);
        // found an entry using requested prefix
        if (previousEntry != null)
        {
            // check that the namespaces entries match
            if (!xmlNS.equals(previousEntry))
                // don't match, throw an exception
                throw new IllegalArgumentException("Multiple namespaces using same prefix");
            else
                // entry is already added, skip it
                return;
        }
        else
        {
            // new entry, add it to the hash map
            xmlPrefixMap.put(prefix, xmlNS);
        }
        
        // add the namespace to the list of known namespaces
        xmlNamespaceList.add(ns);
    }

    /**
     * Writes out the specified prefix/tag combination
     * The namespace has already been verified
     * @param ns
     *             XML namespace of the tag, used to get the prefix
     * @param tag
     *             XML tag to add
     * @param attributes
     *             XML attributes to add as part of the start tag
     */
    private void writeStartTagVerified(String ns, String tag, XMLAttributes attributes) {
        // get the current depth
        int depth = tagStack.getDepth();
        
        // is this the first tag being added
        if (depth == 0)
        {
            // add the XML header
            xmlData.append(XML_HEADER);
            // add a newline for readability
            if (mPrettyFormat) xmlData.append('\n');
        }

        if (mPrettyFormat) {
            // add some tabs for readability
            for (int i = 0; i < depth; i++)
                xmlData.append('\t');
        }
        
        // start the xml tag
        xmlData.append('<');

        // add the tag namespace
        if (!TextUtils.isEmpty(ns)) {
            xmlData.append(ns);
            xmlData.append(':');
        }

        // add the tag name
        xmlData.append(tag);
        
        // if this is the first tag then we also need to add namespaces
        if (depth == 0)
        {
            // get the prefix/namespace mapping
            Set<Entry<String,String>> entries = xmlPrefixMap.entrySet();
            // go through each entry
            for (Entry<String, String> entry : entries) {
                // grab the entry
                // add the prefix=namespace entry
                xmlData.append(String.format(Locale.US, " xmlns:%s=\"%s\"",
                        entry.getKey(), RestXMLEscape.escape(entry.getValue())));
            }
            
        }
        
        // are there any attributes we need to add
        if (attributes != null)
        {
            StringBuilder prefix = new StringBuilder();

            // go through the attribute list
            for (XMLAttributes.Attribute anAttributeList : attributes.attributeList) {
                prefix.setLength(0);

                // grab an attribute
                // is the attribute part of a namespace
                if (!TextUtils.isEmpty(anAttributeList.prefix)) {
                    // grab the prefix
                    String[] temp = anAttributeList.prefix.split(RestXMLNSHandler.XML_SEPARATOR);
                    prefix.append(temp[0]);
                    prefix.append(':');
                }
                // write out the rest of the attribute
                xmlData.append(String.format(Locale.US, " %s%s=\"%s\"",
                        prefix.toString(), anAttributeList.name, RestXMLEscape.escape(anAttributeList.value)));
            }
        }

        // close the xml tag
        xmlData.append('>');
    }

    /**
     * Write the start of an XML element
     * @param ns
     *             XML namespace of the tag
     * @param tag
     *             XML element to use
     * @param attributes
     *             XML attributes to add as part of the start tag
     * @throws IllegalStateException
     *              if an unknown prefix is used
     * @throws IllegalArgumentException
     *              if the namespace has not seen yet
     */
    public void writeStartTag(String ns, String tag, XMLAttributes attributes) throws IllegalStateException,
            IllegalArgumentException {
        if ((tagStack.getDepth() == 0) && (xmlData.length() != 0))
            throw new IllegalStateException("XML has already been closed");
        // get the xml namespace prefix
        String xmlNS = checkNamespace(ns, attributes);
        // write the start of the xml entry
        writeStartTagVerified(xmlNS, tag, attributes);
        // push the entry onto the stack
        tagStack.push(xmlNS,  tag);
        
        // add newline for readability
        if (mPrettyFormat) {
            xmlData.append('\n');
        }
    }

    /**
     * Writes the end of an XML element
     * Namespace has already been verified
     * @param ns
     *             XML namespace of the tag
     * @param tag
     *             XML element to use
     */
    private void writeEndTagVerified(String ns, String tag, boolean indent) {
        if (indent)
        {
            // get the current depth
            int depth = tagStack.getDepth();
            // add some tabs for readability
            if (mPrettyFormat) {
                for (int i = 0; i < depth; i++)
                    xmlData.append('\t');
            }
        }
        // start the xml tag
        xmlData.append("</");

        // add the tag namespace
        if (!TextUtils.isEmpty(ns)) {
            xmlData.append(ns);
            xmlData.append(':');
        }

        // add the tag name
        xmlData.append(tag);

        // close the xml tag
        xmlData.append('>');
        
        // add newline for readability
        if (mPrettyFormat) {
            xmlData.append('\n');
        }
    }

    /**
     * Write the end tag of an XML element
     * @param ns
     *             XML namespace to use
     * @param tag
     *             XML element tag to use
     * @throws IllegalStateException
     *              if an unknown prefix is used
     * @throws IllegalArgumentException
     *              if the namespace has not seen yet
     */
    public void writeEndTag(String ns, String tag) throws IllegalStateException,
            IllegalArgumentException {
        // get the xml namespace prefix
        String xmlNS = checkNamespace(ns, null);

        // pop the entry out of the stack
        tagStack.pop(xmlNS,  tag);

        // write the end of the xml tag
        writeEndTagVerified(xmlNS, tag, true);
    }

    /**
     * Verified the specified namespace is known
     * @param ns
     *              Namespace to lookup
     * @param attributes
     *              Attributes to check
     * @return
     *              Prefix of the namespace
     * @throws IllegalStateException
     *              if an unknown prefix is used
     * @throws IllegalArgumentException
     *              if the namespace has not seen yet
     */
    private String checkNamespace(String ns, XMLAttributes attributes) throws IllegalStateException,
            IllegalArgumentException {
        String xmlNS = null;

        // check the namespace if it's provided
        if (!TextUtils.isEmpty(ns)) {
            // check that this is a namespace we know about
            if (!xmlNamespaceList.contains(ns))
            {
                // get the prefix/namespace mapping
                Set<Entry<String,String>> entries = xmlPrefixMap.entrySet();
                // go through each entry
                for (Entry<String, String> entry : entries) {
                    // grab the entry
                    if (entry.getValue().equals(ns))
                        xmlNS = entry.getKey();
                    if (xmlNS != null) break;
                }
                if (xmlNS == null)
                    throw new IllegalStateException("Unknown namespace used");
            }
            else
            {

                // split the namespace request to grab the prefix
                String[] temp = ns.split(RestXMLNSHandler.XML_SEPARATOR);

                // grab the prefix
                xmlNS = temp[0];
            }

            // make sure the prefix is not empty
            if (TextUtils.isEmpty(xmlNS))
                throw new IllegalArgumentException(
                        "Could not parse tag namespace");
        }
        
        // were we asked to check attributes?
        if (attributes != null)
        {
            // go through each attribute
            for (XMLAttributes.Attribute attribute : attributes.attributeList) {
                // grab an attribute
                // does the attribute have a prefix?
                if (!TextUtils.isEmpty(attribute.prefix)) {
                    // check that this is a namespace we know about
                    if (!xmlNamespaceList.contains(attribute.prefix))
                        throw new IllegalStateException("Unknown namespace used");

                    // split the namespace request to grab the prefix
                    String[] temp = ns.split(RestXMLNSHandler.XML_SEPARATOR);

                    // make sure the prefix is not empty
                    if (TextUtils.isEmpty(temp[0]))
                        throw new IllegalArgumentException(
                                "Could not parse tag namespace");
                }
            }
        }

        // return the tag namespace
        return xmlNS;
    }

    /**
     * Writes out data enclosed by the specified XML element
     * @param ns
     *             XML namespace of the tax
     * @param tag
     *             XML element to use
     * @param attributes
     *             XML attributes to add as part of the tag
     * @param format
     *             String format of the data
     * @param args
     *             Arguments to sent as part of the string format
     * @throws IllegalStateException
     *              if an unknown prefix is used
     * @throws IllegalArgumentException
     *              if the namespace has not seen yet
     */
    public void writeTag(String ns, String tag, XMLAttributes attributes, String format, Object... args) throws IllegalStateException,
    IllegalArgumentException {
        // check and grab the namespace prefix
        String xmlNS = checkNamespace(ns, attributes);

        // write the start of the xml tag
        writeStartTagVerified(xmlNS, tag, attributes);

        // write the data if any
        if (!TextUtils.isEmpty(format))
            xmlData.append(RestXMLEscape.escape(String.format(Locale.US, format, args)));

        // write the end of the xml tag
        writeEndTagVerified(xmlNS, tag, false);
    }

    /**
     * Writes out data enclosed by the specified XML element
     * @param ns
     *             XML namespace of the tax
     * @param tag
     *             XML element to use
     * @param attributes
     *             XML attributes to add as part of the tag
     * @param rawData
     *             Raw data string to encode
     * @throws IllegalStateException
     *              if an unknown prefix is used
     * @throws IllegalArgumentException
     *              if the namespace has not seen yet
     */
    public void writeTagRawData(String ns, String tag, XMLAttributes attributes, String rawData) throws IllegalStateException, IllegalArgumentException {
        // check and grab the namespace prefix
        String xmlNS = checkNamespace(ns, attributes);

        // write the start of the xml tag
        writeStartTagVerified(xmlNS, tag, attributes);

        // write the data if any
        xmlData.append("<![CDATA[");
        if (rawData != null)
            xmlData.append(rawData);
        xmlData.append("]]>");

        // write the end of the xml tag
        writeEndTagVerified(xmlNS, tag, false);
    }

    /**
     * Method to get the constructed XML data
     * @return
     *              String representation of the XML data
     * @throws IllegalStateException
     *              if the XML hasn't been closed properly
     */
    public String getXMLPayload() throws IllegalStateException {
        // make sure the xml has been closed properly
        if (!tagStack.isEmpty())
            throw new IllegalStateException("XML tags not closed properly");

        // return the xml data
        return xmlData.toString();
    }
}
