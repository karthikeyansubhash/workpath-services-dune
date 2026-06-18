// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.text.TextUtils;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Accessory {

    public int vendorId;
    public int productId;
    public String serialNumber;
    public AccessoryClass accessoryClass;
    public String description;
    public String manufacturer;
    public boolean isOwned;

    @SuppressWarnings("unchecked")
    public Accessory(RestXMLTagHandler tagHandler) {
        vendorId = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__VENDOR_ID);
        productId = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID);
        serialNumber = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER);
        accessoryClass = (AccessoryClass) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__ACCESSORY_CLASS_PARAM);
        description = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__DESCRIPTION);
        manufacturer = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__MANUFACTURER);
        isOwned = (Boolean) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__IS_OWNED);
    }

    /**
     * Setup the XML handler required to capture the elements of a Accessory object
     * @param tagHandler
     *              XML tag handler
     */
    @SuppressWarnings("unchecked")
    static void setupXMLTagHandler ( final RestXMLTagHandler tagHandler){
        RestXMLTagHandler.XMLEndTagHandler accessoryEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Accessory accessory = new Accessory(tagHandler);

                List<Accessory> list = (List<Accessory>) handler.getTagData(
                        Constants.XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_RESULT);
                list.add(accessory);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler objectCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__ACCESSORY_CLASS_PARAM, localName)) {
                    handler.setTagData(localName, AccessoryClass.fromAttributeValue(data));
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                    String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch (NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                    String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler booleanCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Boolean.valueOf(data));
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__ACCESSORY, null, accessoryEnd);

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, null, stringCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__ACCESSORY_CLASS_PARAM, null, objectCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__DESCRIPTION, null, stringCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__MANUFACTURER, null, stringCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__IS_OWNED, null, booleanCollector);
    }

    public static List<Accessory> parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        List<Accessory> accessoryList = new ArrayList<Accessory>();
        tagHandler.setTagData(Constants.XML_TAG__ACCESSORIES__ENUMERATE_ACCESSORIES_RESULT, accessoryList);
        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdAccessories.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(accessoryList);
    }

    /**
     * String representation of Accessory
     *
     */
    @Override
    public String toString () {
        return new StringBuilder().append("[").append("vendorId=").append(vendorId).append(", ")
                .append("productId=").append(productId).append(", ").append("serialNumber=").append(serialNumber)
                .append(", ").append("accessoryClass=").append(accessoryClass).append(", ").append("description=")
                .append(description).append(", ").append("manufacturer=").append(manufacturer).append(", ")
                .append("isOwned=").append(isOwned).append("]").toString();
    }
}
