// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

public class HidInfo {
    public int featureReportLength;
    public int inputReportLength;
    public int outputReportLength;
    public boolean isReading;

    @SuppressWarnings("unchecked")
    public HidInfo(RestXMLTagHandler tagHandler) throws Error {
        OXPdAccessories.faultExceptionCheck(tagHandler);

        featureReportLength = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__FEATURE_REPORT_LENGTH);
        inputReportLength = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__INPUT_REPORT_LENGTH);
        outputReportLength = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__OUTPUT_REPORT_LENGTH);
        isReading = (Boolean) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__IS_READING);
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    @SuppressWarnings("unchecked")
    static void setupXMLTagHandler(final RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch (NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler booleanCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Boolean.valueOf(data));
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__FEATURE_REPORT_LENGTH, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__INPUT_REPORT_LENGTH, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__OUTPUT_REPORT_LENGTH, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__IS_READING, null, booleanCollector);
    }

    public static HidInfo parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer xmlResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        mDevice.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new HidInfo(tagHandler);
    }

    /**
     * String representation of HidInfo
     *
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("featureReportLength=").append(featureReportLength).append(", ").append("inputReportLength=").append(inputReportLength).append(", ").append("outputReportLength=").append(outputReportLength).append(", ").append("isReading=").append(isReading).append("]").toString();
    }
}
