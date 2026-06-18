// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import android.util.Pair;

import org.xml.sax.Attributes;

import java.util.ArrayList;

/**
 * XML Attribute container
 */
public class RestXMLElementAttribute extends Pair<String,String> {

    /**
     * XML Element attribute name/value constructor
     * @param name
     *              Attribute name
     * @param value
     *              Attribute value
     */
    @SuppressWarnings("WeakerAccess")
    RestXMLElementAttribute(String name, String value) {
        super(name, value);
    }

    /**
     * Convert SAX attributes to RestXMLElementAttribute
     * @param attributes
     *              List of SAX attributes
     * @return
     *              List of RestXMLElementAttribute values
     */
    public static ArrayList<RestXMLElementAttribute> parseAttributes(Attributes attributes) {
        ArrayList<RestXMLElementAttribute> attrs = null;
        int count = attributes.getLength();
        if (count > 0) {
            attrs = new ArrayList<RestXMLElementAttribute>(count);
            for(int i = 0; i < count; i++) {
                attrs.add(new RestXMLElementAttribute(attributes.getLocalName(i), attributes.getValue(i)));
            }
        }
        return attrs;
    }

    /**
     * Returns a string representation of the object
     * @return
     *          String representation of XML attribute
     */
    public String toString() {
        return first + ":" + second;
    }
}
