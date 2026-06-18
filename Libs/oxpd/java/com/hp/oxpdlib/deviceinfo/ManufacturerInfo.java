// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.deviceinfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/**
 * Manufacturer information
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ManufacturerInfo implements Parcelable {

    /** XML tag containing {@link #deviceId} value */
    private static final String XML_TAG__DEVICE_ID = "deviceId";
    /** XML tag containing {@link #deviceSerialNumber} value */
    private static final String XML_TAG__DEVICE_SERIAL_NUMBER = "deviceSerialNumber";
    /** XML tag containing {@link #firmwareVersion} value */
    private static final String XML_TAG__FIRMWARE_VERSION = "firmwareVersion";
    /** XML tag containing {@link #formatterSerialNumber} value */
    private static final String XML_TAG__FORMATTER_SERIAL_NUMBER = "formatterSerialNumber";
    /** XML tag containing {@link #hostName} value */
    private static final String XML_TAG__HOST_NAME = "hostName";
    /** XML tag containing {@link #ipAddress} value */
    private static final String XML_TAG__IP_ADDRESS = "ipAddress";
    /** XML tag containing {@link #macAddress} value */
    private static final String XML_TAG__MAC_ADDRESS = "macAddress";
    /** XML tag containing {@link #modelName} value */
    private static final String XML_TAG__MODEL_NAME = "modelName";
    /** XML tag containing {@link #productNumber} value */
    private static final String XML_TAG__PRODUCT_NUMBER = "productNumber";

    /**
     * Globally unique device identifier (Example: 3e39cbdc-8760-4dd2-b598-a2898c35e702). No two devices will have the same ID.
     */
    public final String deviceId;
    /**
     * The serial number assigned to the device at the time of manufacture.
     */
    public final String deviceSerialNumber;
    /**
     * Current firmware version.
     */
    public final String firmwareVersion;
    /**
     * Serial number of the device's formatter (controller board).
     */
    public final String formatterSerialNumber;
    /**
     * Fully qualified host name.
     */
    public final String hostName;
    /**
     * IP Address. When a device has multiple IP addresses, only one address will be provided. An IPv4 address (Example: '1.2.3.4') will be represented in IPv6 format (Example: '::ffff:1.2.3.4').
     */
    public final String ipAddress;
    /**
     * Mac address (will be of the form AA:BB:CC:DD:EE:FF). When a device has multiple MAC addresses, only one address will be provided.
     */
    public final String macAddress;
    /**
     * Model name of the device (Example: 'HP LaserJet 500 color MFP M575'). Depending on the device and its options, it is possible to have two different devices with the same model name but different product numbers.
     */
    public final String modelName;
    /**
     * Product number of the product (more specific than model name), represented typically in the form 'CXXXXA'.
     */
    public final String productNumber;

    /**
     * Constructor used by the library to create a ManufacturerInfo object.
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              When errors are detected
     */
    private ManufacturerInfo(RestXMLTagHandler tagHandler) throws Error
    {
        // check for exceptions
        OXPdDeviceInfo.faultExceptionCheck(tagHandler);
        // extract values
        this.deviceId = (String)tagHandler.getTagData(XML_TAG__DEVICE_ID);
        this.deviceSerialNumber = (String)tagHandler.getTagData(XML_TAG__DEVICE_SERIAL_NUMBER);
        this.firmwareVersion = (String)tagHandler.getTagData(XML_TAG__FIRMWARE_VERSION);
        this.formatterSerialNumber = (String)tagHandler.getTagData(XML_TAG__FORMATTER_SERIAL_NUMBER);
        this.hostName = (String)tagHandler.getTagData(XML_TAG__HOST_NAME);
        this.ipAddress = (String)tagHandler.getTagData(XML_TAG__IP_ADDRESS);
        this.macAddress = (String)tagHandler.getTagData(XML_TAG__MAC_ADDRESS);
        this.modelName = (String)tagHandler.getTagData(XML_TAG__MODEL_NAME);
        this.productNumber = (String)tagHandler.getTagData(XML_TAG__PRODUCT_NUMBER);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeString(this.deviceSerialNumber);
        dest.writeString(this.firmwareVersion);
        dest.writeString(this.formatterSerialNumber);
        dest.writeString(this.hostName);
        dest.writeString(this.ipAddress);
        dest.writeString(this.macAddress);
        dest.writeString(this.modelName);
        dest.writeString(this.productNumber);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ManufacturerInfo(Parcel in) {
        this.deviceId = in.readString();
        this.deviceSerialNumber = in.readString();
        this.firmwareVersion = in.readString();
        this.formatterSerialNumber = in.readString();
        this.hostName = in.readString();
        this.ipAddress = in.readString();
        this.macAddress = in.readString();
        this.modelName = in.readString();
        this.productNumber = in.readString();
    }

    /**
     * CustomerInfo creator
     */
    public static final Parcelable.Creator<ManufacturerInfo> CREATOR =
            /**
             * Creator
             */
            new Parcelable.Creator<ManufacturerInfo>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ManufacturerInfo#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ManufacturerInfo createFromParcel(Parcel in) {
                    return new ManufacturerInfo(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ManufacturerInfo[] newArray(int size) {
                    return new ManufacturerInfo[size];
                }
            };


    /**
     * Builds a ManufacturerInfo instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response
     * @param tagHandler
     *              XML handler to use for parsing
     * @return
     *              CustomerInfo instance
     * @throws Error
     *              When errors are detected
     */
    static ManufacturerInfo parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName,data);
            }
        };
        // add handlers
        tagHandler.setXMLHandler(XML_TAG__DEVICE_ID, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__DEVICE_SERIAL_NUMBER, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__FIRMWARE_VERSION, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__FORMATTER_SERIAL_NUMBER, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__HOST_NAME, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__IP_ADDRESS, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__MAC_ADDRESS, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__MODEL_NAME, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__PRODUCT_NUMBER, null, infoCollector);
        // parse the data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new ManufacturerInfo(tagHandler);
    }
}
