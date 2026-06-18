// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/**
 * Container for Accessory Context ID
 */
public class AccessoryContextID {

    /**
     * Tag containing Accessory Context ID result
     */
    private static final String XML_TAG__ACCESSORY__RESERVE_SHARED_ACCESSORY_RESULT = "ReserveSharedAccessoryContextResult";

    /**
     * Accessory Context ID
     */
    private final String mAccessoryContextID;

    /**
     * Retrieve string representation of Accessory Context ID
     *
     * @return Accessory Context ID
     */
    public String getAccessoryContextID() {
        return mAccessoryContextID;
    }

    /**
     * Accessory Context ID constructor
     *
     * @param tagHandler XML handler to extract data from
     * @throws Error If an error occurs.
     */
    AccessoryContextID(RestXMLTagHandler tagHandler) throws Error {
        OXPdAccessories.faultExceptionCheck(tagHandler);

        mAccessoryContextID = (String) tagHandler.getTagData(XML_TAG__ACCESSORY__RESERVE_SHARED_ACCESSORY_RESULT);
    }

    /**
     * Setup the XML handler required to capture the elements of a ReserveSharedAccessoryContextResult object
     *
     * @param tagHandler XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName,
                    String data) {
                handler.setTagData(localName, data);
            }
        };
        // add handlers
        tagHandler.setXMLHandler(XML_TAG__ACCESSORY__RESERVE_SHARED_ACCESSORY_RESULT, null, infoCollector);
    }

    /**
     * Builds a AccessoryContextID instance from the provided HTTP request/response
     *
     * @param device OXPd device instance
     * @param requestResponse HTTP request/response pair
     * @param tagHandler XML tag handler
     * @return AccessoryContextID instance
     * @throws Error When errors are detected
     */
    static AccessoryContextID parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse,
            RestXMLTagHandler tagHandler) throws Error {

        setupXMLTagHandler(tagHandler);

        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new AccessoryContextID(tagHandler);
    }
}
