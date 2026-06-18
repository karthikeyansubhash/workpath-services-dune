// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import java.util.Locale;

/**
 * OMNI current language setting
 */
class CurrentLanguage {

    /** XML tag containing current language information */
    private static final String XML_TAG__UI_CONFIGURATION__GET_CURRENT_LANGUAGE_RESULT = "GetCurrentLanguageResult";

    /**
     * Constructor
     */
    private CurrentLanguage() {
    }

    /**
     * Parse current language information from the request
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              SOAP request/response container
     * @param tagHandler
     *              Response handler
     * @return
     *              List of {@link TopLevelButtonRecord} contained in response
     * @throws Error
     *              If an HTTP error occurs or response contains an error
     */
    static Locale parseCurrentLanguageResponse(OXPdDevice device, HttpRequestResponseContainer requestResponse, final RestXMLTagHandler tagHandler) throws Error {

        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__GET_CURRENT_LANGUAGE_RESULT, null, new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(localName, XML_TAG__UI_CONFIGURATION__GET_CURRENT_LANGUAGE_RESULT))
                    handler.setTagData(localName, data);
            }
        });

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdUIConfiguration.faultExceptionCheck(tagHandler);
        String locale = (String)tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__GET_CURRENT_LANGUAGE_RESULT);
        if (TextUtils.isEmpty(locale)) return null;
        String parts[] = locale.split("-");
        if (parts.length != 2) return null;
        return new Locale(parts[0], parts[1]);
    }
}
