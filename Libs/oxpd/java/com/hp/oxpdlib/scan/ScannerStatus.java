// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/**
 * Scanner status object
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScannerStatus implements Parcelable {

    /** XML tag containing value for {@link #isBusy} */
    private static final String XML_TAG__IS_BUSY = "isBusy";
    /** XML tag containing value for {@link #isOnline} */
    private static final String XML_TAG__IS_ONLINE = "isOnline";
    /** XML tag containing value for {@link #isPaperInAdf} */
    private static final String XML_TAG__IS_PAPER_IN_ADF = "isPaperInAdf";
    /** XML tag containing value for {@link #isPaperInFlatbed} */
    private static final String XML_TAG__IS_PAPER_IN_FLATBED = "isPaperInFlatbed";
    /** XML tag containing value for {@link #isAdfOutputBinFull} */
    private static final String XML_TAG__IS_ADF_OUTPUT_BIN_FULL = "isAdfOutputBinFull";

    /**
     * Indicates whether the ADF's output bin is full. Devices with no sensor to detect paper in the ADF output bin will supply a value of {@see ThreeStateBoolean}.Unknown. Devices that do not have an ADF output bin will report {@see ThreeStateBoolean}.False.
     */
    public final ThreeStateBoolean isAdfOutputBinFull;
    /**
     * Indicates whether the scanner is busy.
     */
    public final boolean isBusy;
    /**
     * Indicates whether the scanner is online.
     */
    public final boolean isOnline;
    /**
     * Indicates whether there is paper in the ADF. Devices with no sensor to detect paper on the flatbed will supply a value of {@see ThreeStateBoolean}.Unknown. Devices that do not have a flatbed will report {@see ThreeStateBoolean}.False.
     */
    public final ThreeStateBoolean isPaperInAdf;
    /**
     * Indicates whether there is paper on the flatbed. Devices with no sensor to detect paper in the ADF will supply a value of {@see ThreeStateBoolean}.Unknown. Devices that do not have an ADF will report {@see ThreeStateBoolean}.False.
     */
    public final ThreeStateBoolean isPaperInFlatbed;

    /**
     * Constructor used by the library to construct ScannerStatus objects. 
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    private ScannerStatus(RestXMLTagHandler tagHandler) throws Error
    {
        OXPdScan.faultExceptionCheck(tagHandler);

        this.isAdfOutputBinFull = ThreeStateBoolean.fromAttributeValue((String) tagHandler.getTagData(XML_TAG__IS_ADF_OUTPUT_BIN_FULL));
        this.isPaperInAdf = ThreeStateBoolean.fromAttributeValue((String) tagHandler.getTagData(XML_TAG__IS_PAPER_IN_ADF));
        this.isPaperInFlatbed = ThreeStateBoolean.fromAttributeValue((String) tagHandler.getTagData(XML_TAG__IS_PAPER_IN_FLATBED));
        this.isBusy =  Boolean.valueOf((String)tagHandler.getTagData(XML_TAG__IS_BUSY));
        this.isOnline = Boolean.valueOf((String)tagHandler.getTagData(XML_TAG__IS_ONLINE));
    }

    /**
     * Builds a ScannerStatus instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScannerStatus instance
     * @throws Error
     *              When errors are detected
     */
    static ScannerStatus parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName,data);
            }
        };
        // add handlers
        tagHandler.setXMLHandler(XML_TAG__IS_ADF_OUTPUT_BIN_FULL, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__IS_BUSY, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__IS_ONLINE, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__IS_PAPER_IN_ADF, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__IS_PAPER_IN_FLATBED, null, infoCollector);
        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new ScannerStatus(tagHandler);
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
        dest.writeInt(this.isAdfOutputBinFull.ordinal());
        dest.writeInt(this.isBusy ? 1 : 0);
        dest.writeInt(this.isOnline ? 1 : 0);
        dest.writeInt(this.isPaperInAdf.ordinal());
        dest.writeInt(this.isPaperInFlatbed.ordinal());
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ScannerStatus(Parcel in) {
        this.isAdfOutputBinFull = ThreeStateBoolean.values()[in.readInt()];
        this.isBusy = in.readInt() > 0;
        this.isOnline = in.readInt() > 0;
        this.isPaperInAdf = ThreeStateBoolean.values()[in.readInt()];
        this.isPaperInFlatbed = ThreeStateBoolean.values()[in.readInt()];
    }

    /**
     * Indicates whether some other object is "equal" to this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScannerStatus)) return false;
        ScannerStatus other = (ScannerStatus)obj;
        return ((this.isAdfOutputBinFull == other.isAdfOutputBinFull)
                && (this.isPaperInFlatbed == other.isPaperInFlatbed)
                && (this.isPaperInAdf == other.isPaperInAdf)
                && (this.isOnline == other.isOnline)
                && (this.isBusy == other.isBusy));
    }

    /**
     * Returns a hash code value for the object
     * @return Integer hash value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + this.isAdfOutputBinFull.hashCode();
        result = result * prime + this.isPaperInAdf.hashCode();
        result = result * prime + this.isPaperInFlatbed.hashCode();
        result = result * prime + Boolean.valueOf(this.isBusy).hashCode();
        result = result * prime + Boolean.valueOf(this.isOnline).hashCode();
        return result;
    }

    /**
     * ScannerStatus creator
     */
    public static final Parcelable.Creator<ScannerStatus> CREATOR =
            new Parcelable.Creator<ScannerStatus>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ScannerStatus#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ScannerStatus createFromParcel(Parcel in) {
                    return new ScannerStatus(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ScannerStatus[] newArray(int size) {
                    return new ScannerStatus[size];
                }
            };
}
