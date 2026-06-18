// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.text.TextUtils;
import android.util.Base64;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

public class HidReportEvent {
    public int vendorId;
    public int productId;
    public String serialNumber;
    public String serverContextId;
    public long ordinal;
    public String timestamp;
    public List<HidReport> reports;

    @SuppressWarnings("unchecked")
    public HidReportEvent(RestXMLTagHandler tagHandler) throws Error {
        OXPdAccessories.faultExceptionCheck(tagHandler);

        vendorId = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__VENDOR_ID);
        productId = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID);
        serialNumber = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER);
        serverContextId = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID);
        ordinal = (Integer) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__ORDINAL);
        timestamp = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__TIMESTAMP);
        reports = (List<HidReport>) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__HID_REPORTS);
    }

    /**
     * Setup the XML handler required to capture the elements of a HidReportEvent object
     * @param tagHandler
     *              XML tag handler
     */
    @SuppressWarnings("unchecked")
    static void setupXMLTagHandler(final RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLStartTagHandler listCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__HID_REPORTS, localName)) {
                    handler.setTagData(localName, new ArrayList<HidReport>());
                    handler.setGenericXMLHandler(
                            null,
                            new RestXMLTagHandler.XMLEndTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                                    if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__HID_REPORTS, localName)) {
                                        handler.setGenericXMLHandler(null, null);
                                    } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__HID_REPORT, localName)) {
                                        HidReportType reportType = (HidReportType) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__REPORT_TYPE);
                                        String reportData = (String) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__DATA);
                                        HidReport hidReport = new HidReport(reportType, Base64.decode(reportData, Base64.DEFAULT));
                                        ((List<HidReport>) handler.getTagData(Constants.XML_TAG__ACCESSORIES__HID_REPORTS)).add(hidReport);
                                    } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__REPORT_TYPE, localName)) {
                                        handler.setTagData(localName, HidReportType.fromAttributeValue(data));
                                    } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__DATA, localName)) {
                                        handler.setTagData(localName, data);
                                    }
                                }
                            }
                    );
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch (NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__HID_REPORTS, listCreator, null);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__ORDINAL, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, null, integerCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, null, stringCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID, null, stringCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__TIMESTAMP, null, stringCollector);
    }

    public static HidReportEvent parseRequestResult(OXPdDevice mDevice, String xmlResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        mDevice.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new HidReportEvent(tagHandler);

    }

    /**
     * String representation of HidReportEvent
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("vendorId=").append(vendorId).append(", ").append("productId=").append(productId).append(", ").append("serialNumber=").append(serialNumber).append(", ").append("serverContextId=").append(serverContextId).append(", ").append("ordinal=").append(ordinal).append(", ").append("timestamp=").append(timestamp).append(", ").append("reports=").append(((reports == null)?"null":reports.toString())).append("]").toString();
    }
}
