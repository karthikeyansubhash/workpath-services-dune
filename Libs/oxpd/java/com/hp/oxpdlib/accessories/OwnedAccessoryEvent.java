// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

public class OwnedAccessoryEvent {
    private static final String XML_TAG__ACCESSORIES__EVENT_CODE = "eventCode";
    private static final String XML_TAG__ACCESSORIES__VENDOR_ID = "vendorId";
    private static final String XML_TAG__ACCESSORIES__PRODUCT_ID = "productId";
    private static final String XML_TAG__ACCESSORIES__SERIAL_NUMBER = "serialNumber";
    private static final String XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID = "serverContextId";
    private static final String XML_TAG__ACCESSORIES__TIMESTAMP = "timestamp";
    private static final String XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID = "accessoryContextId";

    public OwnedAccessoryEventCode eventCode;
    public int vendorId;
    public int productId;
    public String serialNumber;
    public String serverContextId;
    public String timestamp;
    public String accessoryContextId;

    public OwnedAccessoryEvent(RestXMLTagHandler tagHandler) throws Error {
        OXPdAccessories.faultExceptionCheck(tagHandler);

        //noinspection unchecked
        eventCode = (OwnedAccessoryEventCode) tagHandler.getTagData(XML_TAG__ACCESSORIES__EVENT_CODE);
        vendorId = (Integer) tagHandler.getTagData(XML_TAG__ACCESSORIES__VENDOR_ID);
        productId = (Integer) tagHandler.getTagData(XML_TAG__ACCESSORIES__PRODUCT_ID);
        serialNumber = (String) tagHandler.getTagData(XML_TAG__ACCESSORIES__SERIAL_NUMBER);
        serverContextId = (String) tagHandler.getTagData(XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID);
        timestamp = (String) tagHandler.getTagData(XML_TAG__ACCESSORIES__TIMESTAMP);
        accessoryContextId = (String) tagHandler.getTagData(XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID);
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch (NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler eventCodeCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, OwnedAccessoryEventCode.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__EVENT_CODE, null, eventCodeCreator);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__VENDOR_ID, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__PRODUCT_ID, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__SERIAL_NUMBER, null, stringCollector);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID, null, stringCollector);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__TIMESTAMP, null, stringCollector);
        tagHandler.setXMLHandler(XML_TAG__ACCESSORIES__ACCESSORY_CONTEXT_ID, null, stringCollector);
    }

    public static OwnedAccessoryEvent parseRequestResult(OXPdDevice mDevice, String xmlResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        mDevice.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new OwnedAccessoryEvent(tagHandler);

    }

    /**
     * String representation of OwnedAccessoryEventData
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("eventCode=").append(((eventCode == null)?"null":eventCode.toString())).append(", ").append("vendorId=").append(vendorId).append(", ").append("productId=").append(productId).append(", ").append("serialNumber=").append(serialNumber).append(", ").append("serverContextId=").append(serverContextId).append(", ").append("timestamp=").append(timestamp).append(", ").append("accessoryContextId=").append(accessoryContextId).append("]").toString();
    }
}
