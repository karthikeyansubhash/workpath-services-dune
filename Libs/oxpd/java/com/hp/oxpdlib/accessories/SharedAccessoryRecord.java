// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.accessories;

import android.text.TextUtils;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories.Constants;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xml.sax.Attributes;

public class SharedAccessoryRecord {
    public int vendorId;
    public int productId;
    public String serialNumber;

    private SharedAccessoryRecord(final Builder builder) {
        this.vendorId = builder.vendorId;
        this.productId = builder.productId;
        this.serialNumber = builder.serialNumber;
    }

    /**
     * Button record builder
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {
        private int vendorId;
        private int productId;
        private String serialNumber;

        /**
         * Constructor
         */
        public Builder() {}

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

        public SharedAccessoryRecord build() {
            return new SharedAccessoryRecord(this);
        }
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLStartTagHandler recordStart = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD, new SharedAccessoryRecord.Builder());
            }
        };

        RestXMLTagHandler.XMLEndTagHandler recordEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                SharedAccessoryRecord.Builder builder = (SharedAccessoryRecord.Builder) handler.getTagData(Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD);
                SharedAccessoryRecord record = (builder != null) ? builder.build() : null;
                handler.setTagData(localName, null);

                if (record != null) {
                    //noinspection unchecked
                    List<SharedAccessoryRecord> list = (List<SharedAccessoryRecord>) handler.getTagData(
                            Constants.XML_TAG__ACCESSORIES__GET_SHARED_ACCESSORY_RECORD_RESULT);
                    list.add(record);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler recordDataCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                SharedAccessoryRecord.Builder builder = (SharedAccessoryRecord.Builder) handler.getTagData(Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD);
                if (builder == null) return;
                if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, localName)) {
                    builder.setVendorId(Integer.parseInt(data));
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, localName)) {
                    builder.setProductId(Integer.parseInt(data));
                } else if (TextUtils.equals(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, localName)) {
                    builder.setSerialNumber(data);
                }
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD, recordStart, recordEnd);

        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__VENDOR_ID, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__PRODUCT_ID, null, recordDataCollector);
        tagHandler.setXMLHandler(Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER, null, recordDataCollector);
    }

    public static List<SharedAccessoryRecord> parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        List<SharedAccessoryRecord> accessoryList = new ArrayList<SharedAccessoryRecord>();
        tagHandler.setTagData(Constants.XML_TAG__ACCESSORIES__GET_SHARED_ACCESSORY_RECORD_RESULT, accessoryList);
        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdAccessories.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(accessoryList);
    }

    public static void writeToXml(SharedAccessoryRecord sharedAccessoryRecord, RestXMLWriter xmlWriter) {
        // start button record
        xmlWriter.writeStartTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD_PARAM, null);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__VENDOR_ID,null, "%d", sharedAccessoryRecord.vendorId);
        xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__PRODUCT_ID,null, "%d", sharedAccessoryRecord.productId);
        if (!TextUtils.isEmpty(sharedAccessoryRecord.serialNumber)) {
            xmlWriter.writeTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SERIAL_NUMBER,null, "%s", sharedAccessoryRecord.serialNumber);
        }
        xmlWriter.writeEndTag(OXPdAccessories.XML_SCHEMA__OXPD_ACCESSORIES, Constants.XML_TAG__ACCESSORIES__SHARED_ACCESSORY_RECORD_PARAM);
    }

    /**
     * String representation of SharedAccessoryRecord
     *
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("vendorId=").append(vendorId).append(", ").append("productId=").append(productId).append(", ").append("serialNumber=").append(serialNumber).append("]").toString();
    }
}
