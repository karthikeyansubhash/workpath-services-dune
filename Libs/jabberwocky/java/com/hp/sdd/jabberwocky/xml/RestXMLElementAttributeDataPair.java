// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Container for XML attributes and data associated with an XML element
 */
public final class RestXMLElementAttributeDataPair extends Pair<ArrayList<RestXMLElementAttribute>, Object> {

    /**
     * Container for XML attributes and data as.
     * @param attributes
     *              List of XML attributes of an XML element
     * @param object
     *              Data associated with XML element
     */
    public RestXMLElementAttributeDataPair(ArrayList<RestXMLElementAttribute> attributes, Object object) {
        super(attributes, object);
    }

    /**
     * Return  string representation of the object.
     * @return
     *          String representation of an object
     */
    public String toString() {
        return first.toString() + " = " + second.toString();
    }
}
