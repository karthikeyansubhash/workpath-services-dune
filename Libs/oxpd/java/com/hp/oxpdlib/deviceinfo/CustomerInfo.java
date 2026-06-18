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
 * Customer info object
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CustomerInfo implements Parcelable {

    /** XML tag containing {@link #machineName} value */
    private static final String XML_TAG__MACHINE_NAME = "machineName";
    /** XML tag containing {@link #deviceLocation} value */
    private static final String XML_TAG__DEVICE_LOCATION = "deviceLocation";
    /** XML tag containing {@link #assetNumber} value */
    private static final String XML_TAG__ASSET_NUMBER = "assetNumber";
    /** XML tag containing {@link #companyName } value */
    private static final String XML_TAG__COMPANY_NAME = "companyName";
    /** XML tag containing {@link #companyContact} value */
    private static final String XML_TAG__COMPANY_CONTACT = "companyContact";

    /**
     * Asset number assigned to the device created by the customer via the EWS page or Web Jetadmin.
     */
    public final String assetNumber;
    /**
     * Company contact name assigned to the device by the customer via the EWS page or Web Jetadmin.
     */
    public final String companyContact;
    /**
     * Company name assigned to the device by the customer via the EWS page or Web Jetadmin.
     */
    public final String companyName;
    /**
     * Physical location of the device assigned to the device by the customer via the EWS page or Web Jetadmin.
      */
    public final String deviceLocation;
    /**
     * Name assigned to the device by the customer via the EWS page or Web Jetadmin.
     */
    public final String machineName;
    
    /**
     * Constructor used by the library to create a CustomerInfo object.
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              When errors are detected
     */
    private CustomerInfo(RestXMLTagHandler tagHandler) throws Error
    {
        // check for exceptions
        OXPdDeviceInfo.faultExceptionCheck(tagHandler);
        // extract values
        this.assetNumber = (String)tagHandler.getTagData(XML_TAG__ASSET_NUMBER);
        this.companyContact = (String)tagHandler.getTagData(XML_TAG__COMPANY_CONTACT);
        this.companyName = (String)tagHandler.getTagData(XML_TAG__COMPANY_NAME);
        this.deviceLocation = (String)tagHandler.getTagData(XML_TAG__DEVICE_LOCATION);
        this.machineName = (String)tagHandler.getTagData(XML_TAG__MACHINE_NAME);
    }

    /**
     * Builds a CustomerInfo instance from the provided HTTP request/response
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
    static CustomerInfo parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName,data);
            }
        };
        // add handlers
        tagHandler.setXMLHandler(XML_TAG__MACHINE_NAME, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__DEVICE_LOCATION, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__ASSET_NUMBER, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__COMPANY_NAME, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__COMPANY_CONTACT, null, infoCollector);
        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new CustomerInfo(tagHandler);
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
        dest.writeString(this.assetNumber);
        dest.writeString(this.companyContact);
        dest.writeString(this.companyName);
        dest.writeString(this.deviceLocation);
        dest.writeString(this.machineName);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private CustomerInfo(Parcel in) {
        this.assetNumber = in.readString();
        this.companyContact = in.readString();
        this.companyName = in.readString();
        this.deviceLocation = in.readString();
        this.machineName = in.readString();
    }

    /**
     * CustomerInfo creator
     */
    public static final Parcelable.Creator<CustomerInfo> CREATOR =
            /**
             * Creator
             */
            new Parcelable.Creator<CustomerInfo>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link CustomerInfo#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public CustomerInfo createFromParcel(Parcel in) {
                    return new CustomerInfo(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public CustomerInfo[] newArray(int size) {
                    return new CustomerInfo[size];
                }
            };
}
