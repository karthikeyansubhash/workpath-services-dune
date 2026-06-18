// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to parse UI Attributes
 */
@SuppressWarnings("unused")
class UIAttributes {

    /**
     * Private constructor
     */
    private UIAttributes() {}

    /** XML tag containing values for {@link OXPdUIConfiguration#SOAP_OP__GET_UI_ATTRIBUTES} request */
    private static final String XML_TAG__UI_CONFIGURATION__GET_UI_ATTRIBUTES_RESULT = "GetUIAttributesResult";
    /** XML tag containing values for {@link OXPdUIConfiguration#SOAP_OP__GET_UI_PROFILE} request */
    private static final String XML_TAG__UI_CONFIGURATION__GET_UI_PROFILE_RESULT = "GetUIProfileResult";

    /**
     * The uiTimeout set on the device (integer).
     */
    public static final String UI_ATTRIBUTE__UI_TIMEOUT = "uiTimeout";
    /**
     * The nominal pixel height of a top level button icon for this device (integer).
     */
    public static final String UI_ATTRIBUTE__BUTTON_ICON_HEIGHT = "buttonIconHeight";
    /**
     * The nominal pixel width of a top level button icon for this device (integer).
     */
    public static final String UI_ATTRIBUTE__BUTTON_ICON_WIDTH = "buttonIconWidth";
    /**
     * Value 'true' if the device's display is color, otherwise 'false'.
     */
    public static final String UI_ATTRIBUTE__IS_COLOR = "isColor";
    /**
     * Comma separated list of top level button icon file formats supported by the device.
     * (Known values: 'Bmp', 'Gif', 'Jpg', 'Png')
     * */
    public static final String UI_ATTRIBUTE__ICON_FORMAT_SUPPORTED = "iconFormatSupported";
    /**
     * Identifies the user interface framework to be used with this device to achieve a look and
     * feel that matches the look and feel of native device applications.
     * (Known values: 'CloudUI', 'Winforms', 'Omni').
     */
    public static final String UI_ATTRIBUTE__USER_INTERFACE_ID = "userInterfaceId";
    /**
     * Identifies the device's control panel hardware technology.
     * (Known values: 'CCPI', 'CCPI27', 'Equinox', 'MagicFrame', 'Oriani', 'OrianiKb', 'Phantom',
     * 'PhantomKb', 'Pharos', 'PharosKb', 'PhotonR', 'PhotonRKb', 'Pulsar', 'PulsarKb', 'Quantum',
     * 'Quasar', 'STP'.
     */
    public static final String UI_ATTRIBUTE__CONTROL_PANEL_ID = "controlPanelId";
    /**
     * Identifies the current control panel theme.
     * (Known values: 'Windjammer', 'Opus')
     * */
    public static final String UI_ATTRIBUTE__THEME_ID = "themeId";
    /**
     * Indicates the type of look and feel exhibited by the device.
     */
    public static final String UI_ATTRIBUTE__LOOK_AND_FEEL = "lookAndFeel";

    /**
     * Helper function to parse results for either
     * {@link OXPdUIConfiguration#SOAP_OP__GET_UI_ATTRIBUTES} or
     * {@link OXPdUIConfiguration#SOAP_OP__GET_UI_PROFILE} requests
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              SOAP request/response container
     * @param tagHandler
     *              Response handler
     * @return
     *              Any mappings found during the data parsing
     * @throws Error
     *              If an HTTP error occurs or response contains an error
     */
    static Map<String,String> parseUIAttributes(OXPdDevice device, HttpRequestResponseContainer requestResponse, final RestXMLTagHandler tagHandler) throws Error  {
        // initialize the map
        HashMap<String,String> keyValuePairs = new HashMap<String,String>();
        tagHandler.setTagData(XML_TAG__UI_CONFIGURATION__GET_UI_ATTRIBUTES_RESULT, keyValuePairs);

        // setup the handlers
        final RestXMLTagHandler.XMLEndTagHandler keyValuePairHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                tagHandler.setTagData(localName, data);
            }
        };
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__KEY, null, keyValuePairHandler);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__VALUE_STRING, null, keyValuePairHandler);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__KEY_VALUE_PAIR,
                new RestXMLTagHandler.XMLStartTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                        tagHandler.clearTagData(OXPdDevice.Constants.XML_TAG__COMMON__KEY);
                        tagHandler.clearTagData(OXPdDevice.Constants.XML_TAG__COMMON__VALUE_STRING);
                    }
                },
                new RestXMLTagHandler.XMLEndTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                        String key = (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__KEY);
                        String value = (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__VALUE_STRING);
                        if ((key != null) && (value != null)) {
                            //noinspection unchecked
                            Map<String,String> keyValuePairs = (Map<String, String>) tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__GET_UI_ATTRIBUTES_RESULT);
                            keyValuePairs.put(key, value);
                        }
                    }
                });
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__GET_UI_PROFILE_RESULT,
                new RestXMLTagHandler.XMLStartTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                        tagHandler.setGenericXMLHandler(null,
                                new RestXMLTagHandler.XMLEndTagHandler() {
                                    @Override
                                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                                        //noinspection unchecked
                                        Map<String,String> keyValuePairs = (Map<String, String>) tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__GET_UI_ATTRIBUTES_RESULT);
                                        keyValuePairs.put(localName, data);
                                    }
                                });
                    }
                },
                new RestXMLTagHandler.XMLEndTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                        tagHandler.setGenericXMLHandler(null, null);
                    }
                }
        );
        // parse the result
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdUIConfiguration.faultExceptionCheck(tagHandler);
        // return the mappings
        return Collections.unmodifiableMap(keyValuePairs);
    }
}
