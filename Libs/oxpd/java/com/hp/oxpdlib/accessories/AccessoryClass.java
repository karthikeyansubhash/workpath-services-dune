// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AccessoryClass {
    Unsupported("Unsupported"),
    Hid("Hid"),
    Ccid("Ccid"),
    MassStorage("MassStorage");

    public final String value;

    AccessoryClass(String v) {
        value = v;
    }

    public static AccessoryClass fromAttributeValue(String v) {
        for (AccessoryClass c: AccessoryClass.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of AccessoryClass
     *
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Builds a ScanJobStatus instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              XML response data
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScanJobStatus instance
     * @throws Error
     *              When errors are detected
     */
    static List<AccessoryClass> parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        RestXMLTagHandler.XMLEndTagHandler accessoryClassEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                AccessoryClass accessoryClass = AccessoryClass.fromAttributeValue(data);

                if (accessoryClass != null) {
                    //noinspection unchecked
                    List<AccessoryClass> list = (List<AccessoryClass>) handler.getTagData(
                            Constants.XML_TAG__ACCESSORIES__GET_SUPPORTED_ACCESSORY_CLASSES_RESULT);
                    list.add(accessoryClass);
                }
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__ACCESSORY_CLASS, null, accessoryClassEnd);

        List<AccessoryClass> accessoryClassList = new ArrayList<AccessoryClass>();
        tagHandler.setTagData(Constants.XML_TAG__ACCESSORIES__GET_SUPPORTED_ACCESSORY_CLASSES_RESULT, accessoryClassList);
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdAccessories.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(accessoryClassList);
    }
}
