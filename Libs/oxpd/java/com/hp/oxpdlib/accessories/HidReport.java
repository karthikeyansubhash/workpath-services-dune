// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.util.Base64;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;
import java.util.Arrays;

public class HidReport {
    public HidReportType reportType;
    public byte[] data;

    /**
     * Fully-initialising value constructor
     * 
     * @param reportType
     *     the HidReportType
     * @param data
     *     the byte[]
     */
    public HidReport(final HidReportType reportType, final byte[] data) {
        this.reportType = reportType;
        this.data = data;
    }

    private HidReport(final RestXMLTagHandler tagHandler) throws Error  {
        OXPdAccessories.faultExceptionCheck(tagHandler);

        reportType = (HidReportType) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__REPORT_TYPE);
        data = (byte[]) tagHandler.getTagData(Constants.XML_TAG__ACCESSORIES__DATA);
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLEndTagHandler reportTypeCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, HidReportType.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler byteArrayCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Base64.decode(data, Base64.DEFAULT));
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__REPORT_TYPE, null, reportTypeCreator);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__DATA, null, byteArrayCollector);
    }

    public static HidReport parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer xmlResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        mDevice.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new HidReport(tagHandler);
    }

    public static void writeToXML(HidReport hidReport, RestXMLWriter xmlWriter) {
        xmlWriter.writeStartTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__HID_WRITE_REPORT, null);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__REPORT_TYPE, null, "%s", hidReport.reportType.value);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__DATA, null, "%s",
                Base64.encodeToString(hidReport.data, Base64.NO_WRAP));
        xmlWriter.writeEndTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__HID_WRITE_REPORT);
    }

    /**
     * String representation of HidReport
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("reportType=").append(((reportType == null)?"null":reportType.toString())).append(", ").append("data=").append(Arrays.toString(this.data)).append("]").toString();
    }
}
