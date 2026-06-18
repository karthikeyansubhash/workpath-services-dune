// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/**
 * Container for Scan Job ID information
 */
public class ScanJobID implements Parcelable {

    /** Tag containing start scan job result, ie job id */
    private static final String XML_TAG__SCAN__START_SCAN_JOB_RESULT = "StartScanJobResult";
    /** Tag containing job id in a {@link ScanJobEvent} */
    static final String XML_TAG__SCAN__JOB_ID = "jobId";

    /** ScanJob ID UUID */
    private final String mScanJobID;

    /**
     * Retrieve string representation of Scan Job ID
     * @return
     *              scan job id
     */
    public String getScanJobID() {
        return mScanJobID;
    }

    /**
     * Scan Job ID constructor
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    ScanJobID(RestXMLTagHandler tagHandler) throws Error {
        OXPdScan.faultExceptionCheck(tagHandler);
        String jobID = (String) tagHandler.getTagData(XML_TAG__SCAN__START_SCAN_JOB_RESULT);
        if (TextUtils.isEmpty(jobID)) {
            jobID = (String) tagHandler.getTagData(XML_TAG__SCAN__JOB_ID);
        }
        mScanJobID = jobID;
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName,data);
            }
        };
        // add handlers
        tagHandler.setXMLHandler(XML_TAG__SCAN__START_SCAN_JOB_RESULT, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__SCAN__JOB_ID, null, infoCollector);
    }

    /**
     * Builds a ScanJobID instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScanJobID instance
     * @throws Error
     *              When errors are detected
     */
    static ScanJobID parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        setupXMLTagHandler(tagHandler);

        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new ScanJobID(tagHandler);
    }

    /**
     * Returns a hash code value for the object
     * @return Integer hash value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mScanJobID.hashCode();
        return result;
    }

    /**
     * Indicates whether some other object is "equal" to this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScanJobID)) return false;
        ScanJobID other = (ScanJobID) obj;
        return TextUtils.equals(mScanJobID, other.mScanJobID);
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
        dest.writeString(this.mScanJobID);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ScanJobID(Parcel in) {
        this.mScanJobID = in.readString();
    }

    /**
     * ScanJobID creator
     */
    public static final Parcelable.Creator<ScanJobID> CREATOR =
            new Parcelable.Creator<ScanJobID>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ScanJobID#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ScanJobID createFromParcel(Parcel in) {
                    return new ScanJobID(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ScanJobID[] newArray(int size) {
                    return new ScanJobID[size];
                }
            };
}
