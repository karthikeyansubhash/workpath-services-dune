// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.text.TextUtils;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.oxpdlib.common.WebResourceWithTimeout;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xml.sax.Attributes;

public class OwnedAccessoryRecord {
    public WebResourceWithTimeout callback;
    public int vendorId;
    public int productId;
    public String serialNumber;
    public String serverContextId;

    private OwnedAccessoryRecord(final Builder builder) {
        this.callback = builder.callback;
        this.vendorId = builder.vendorId;
        this.productId = builder.productId;
        this.serialNumber = builder.serialNumber;
        this.serverContextId = builder.serverContextId;
    }

    /**
     * Button record builder
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {
        private WebResourceWithTimeout callback;
        private int vendorId;
        private int productId;
        private String serialNumber;
        private String serverContextId;

        /**
         * Constructor
         */
        public Builder() {}

        public Builder setCallback(WebResourceWithTimeout callback) {
            this.callback = callback;
            return this;
        }

        public Builder setVendorId(int vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder setServerContextId(String serverContextId) {
            this.serverContextId = serverContextId;
            return this;
        }

        public OwnedAccessoryRecord build() {
            return new OwnedAccessoryRecord(this);
        }
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLStartTagHandler ownedAccessoryRecordStart = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD, new OwnedAccessoryRecord.Builder());
            }
        };

        RestXMLTagHandler.XMLEndTagHandler ownedAccessoryRecordEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                OwnedAccessoryRecord.Builder builder = (OwnedAccessoryRecord.Builder) handler.getTagData(Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD);
                OwnedAccessoryRecord record = (builder != null) ? builder.build() : null;
                handler.setTagData(localName, null);

                if (record != null) {
                    //noinspection unchecked
                    List<OwnedAccessoryRecord> list = (List<OwnedAccessoryRecord>) handler.getTagData(
                            Constants.XML_TAG__ACCESSORIES__GET_OWNED_ACCESSORY_RECORD_RESULT);
                    list.add(record);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler recordDataCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                OwnedAccessoryRecord.Builder builder = (OwnedAccessoryRecord.Builder) handler.getTagData(Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD);
                if (builder == null) return;
                if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, localName)) {
                    builder.setVendorId(Integer.parseInt(data));
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, localName)) {
                    builder.setProductId(Integer.parseInt(data));
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, localName)) {
                    builder.setSerialNumber(data);
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID, localName)) {
                    builder.setServerContextId(data);
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__CALLBACK, localName)) {
                    builder.setCallback(new WebResourceWithTimeout(handler));
                }
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD, ownedAccessoryRecordStart, ownedAccessoryRecordEnd);

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__CALLBACK, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID, null, recordDataCollector);

        WebResourceWithTimeout.setupXMLTagHandler(tagHandler);
    }

    public static List<OwnedAccessoryRecord> parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        List<OwnedAccessoryRecord> accessoryList = new ArrayList<OwnedAccessoryRecord>();
        tagHandler.setTagData(Constants.XML_TAG__ACCESSORIES__GET_OWNED_ACCESSORY_RECORD_RESULT, accessoryList);
        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdAccessories.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(accessoryList);
    }

    public static void writeToXml(OwnedAccessoryRecord ownedAccessoryRecord, RestXMLWriter xmlWriter) {
        // start button record
        xmlWriter.writeStartTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD_PARAM, null);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__VENDOR_ID,null, "%d", ownedAccessoryRecord.vendorId);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__PRODUCT_ID,null, "%d", ownedAccessoryRecord.productId);
        if (!TextUtils.isEmpty(ownedAccessoryRecord.serialNumber)) {
            xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER,null, "%s", ownedAccessoryRecord.serialNumber);
        }
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERVER_CONTEXT_ID,null, "%s", ownedAccessoryRecord.serverContextId);
        if (ownedAccessoryRecord.callback != null) {
            xmlWriter.writeStartTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, OXPdDevice.Constants.XML_TAG__COMMON__CALLBACK, null);
            WebResourceWithTimeout.writeToXML(ownedAccessoryRecord.callback, xmlWriter);
            xmlWriter.writeEndTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, OXPdDevice.Constants.XML_TAG__COMMON__CALLBACK);
        }
        xmlWriter.writeEndTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__OWNED_ACCESSORY_RECORD_PARAM);
    }

    /**
     * String representation of OwnedAccessoryRecord
     *
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("vendorId=").append(vendorId).append(", ").append("productId=").append(productId).append(", ").append("serialNumber=").append(serialNumber).append(", ").append("serverContextId=").append(serverContextId).append(", ").append("callback=").append(callback).append("]").toString();
    }
}
