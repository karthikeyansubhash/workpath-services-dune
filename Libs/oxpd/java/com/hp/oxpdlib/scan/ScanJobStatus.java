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
 * Device's ScanJobStatus.
 * <b>Apps should never construct a ScanJobStatus object.</b>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScanJobStatus implements Parcelable {

    private static final String XML_TAG__SCAN__CANCELING_STATE = "cancelingState";
    private static final String XML_TAG__SCAN__IS_CANCELABLE = "isCancelable";
    private static final String XML_TAG__SCAN__PROCESSING_STATE = "processingState";
    private static final String XML_TAG__SCAN__PROCESS_RESTART_COUNT = "processRestartCount";
    private static final String XML_TAG__SCAN__RESULT_CODE = "resultCode";
    private static final String XML_TAG__SCAN__RESULT_REASON = "resultReason";
    private static final String XML_TAG__SCAN__SCANNING_STATE = "scanningState";
    private static final String XML_TAG__SCAN__TOTAL_IMAGES_PROCESSED = "totalImagesProcessed";
    private static final String XML_TAG__SCAN__TOTAL_IMAGES_SCANNED = "totalImagesScanned";
    private static final String XML_TAG__SCAN__TOTAL_IMAGES_TRANSMITTED = "totalImagesTransmitted";
    private static final String XML_TAG__SCAN__TRANSMISSION_CONSECUTIVE_RETRY_COUNT = "transmissionConsecutiveRetryCount";
    private static final String XML_TAG__SCAN__TRANSMISSION_RETRY_COUNT = "transmissionRetryCount";
    private static final String XML_TAG__SCAN__TRANSMITTING_STATE = "transmittingState";
    
    /**
     * The scan job's canceling state.
     */
    public final ActivityState cancelingState;
    /**
     * Indicates whether the job is currently cancelable.
     */
    public final boolean isCancelable;
    /**
     * The scan job's processing state.
     */
    public final ActivityState processingState;
    /**
     * The cumulative number of image processing restarts that have been attempted for this job (0 means no restarts).
     */
    public final int processRestartCount;
    /**
     * The scan result. See resultReason for a textual explanation of why the job failed.
     */
    public final ResultCode resultCode;
    /**
     * An implementation-specific description of the reason for the result.
     */
    public final String resultReason;
    /**
     * The scan job's scanning state.
     */
    public final ActivityState scanningState;
    /**
     * The number of images that have been processed.
     */
    public final int totalImagesProcessed;
    /**
     * The number of images that have been scanned.
     */
    public final int totalImagesScanned;
    /**
     * The number of images that have been transmitted.
     */
    public final int totalImagesTransmitted;
    /**
     * The number of consecutive transmission retries that have been attempted for the current transmission operation for this job (0 means no retries attempted for the current transmission operation).
     */
    public final int transmissionConsecutiveRetryCount;
    /**
     * The cumulative number of transmission retries that have been attempted for this job (0 means no retries attempted).
     */
    public final int transmissionRetryCount;
    /**
     * The scan job's transmitting state.
     */
    public final ActivityState transmittingState;

    /**
     * Constructor used by the library to construct ScanJobStatus objects from the device's ScanJobStatus.
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    ScanJobStatus(RestXMLTagHandler tagHandler) throws Error
    {
        OXPdScan.faultExceptionCheck(tagHandler);

        this.cancelingState = (ActivityState)tagHandler.getTagData(XML_TAG__SCAN__CANCELING_STATE);
        this.isCancelable = (Boolean)tagHandler.getTagData(XML_TAG__SCAN__IS_CANCELABLE, Boolean.FALSE);
        this.processingState = (ActivityState)tagHandler.getTagData(XML_TAG__SCAN__PROCESSING_STATE);
        this.processRestartCount = (Integer)tagHandler.getTagData(XML_TAG__SCAN__PROCESS_RESTART_COUNT, 0);
        this.resultCode = (ResultCode)tagHandler.getTagData(XML_TAG__SCAN__RESULT_CODE);
        this.resultReason = (String)tagHandler.getTagData(XML_TAG__SCAN__RESULT_REASON);
        this.scanningState = (ActivityState)tagHandler.getTagData(XML_TAG__SCAN__SCANNING_STATE);
        this.totalImagesProcessed = (Integer)tagHandler.getTagData(XML_TAG__SCAN__TOTAL_IMAGES_PROCESSED, 0);
        this.totalImagesScanned = (Integer)tagHandler.getTagData(XML_TAG__SCAN__TOTAL_IMAGES_SCANNED, 0);
        this.totalImagesTransmitted = (Integer)tagHandler.getTagData(XML_TAG__SCAN__TOTAL_IMAGES_TRANSMITTED, 0);
        this.transmissionConsecutiveRetryCount = (Integer)tagHandler.getTagData(XML_TAG__SCAN__TRANSMISSION_CONSECUTIVE_RETRY_COUNT, 0);
        this.transmissionRetryCount = (Integer)tagHandler.getTagData(XML_TAG__SCAN__TRANSMISSION_RETRY_COUNT, 0);
        this.transmittingState = (ActivityState)tagHandler.getTagData(XML_TAG__SCAN__TRANSMITTING_STATE);
    }

    /**
     * Setup the XML handler required to capture the elements of a ScanJobStatus object
     * @param tagHandler
     *              XML tag handler
     */
    static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLEndTagHandler booleanCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Boolean.valueOf(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch(NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler activityStateCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, ActivityState.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler resultCodeCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, ResultCode.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(XML_TAG__SCAN__CANCELING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__IS_CANCELABLE, null, booleanCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__PROCESSING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__PROCESS_RESTART_COUNT, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__RESULT_CODE, null, resultCodeCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__RESULT_REASON, null, stringCollector);
        tagHandler.setXMLHandler(XML_TAG__SCAN__SCANNING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TOTAL_IMAGES_PROCESSED, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TOTAL_IMAGES_SCANNED, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TOTAL_IMAGES_TRANSMITTED, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TRANSMISSION_CONSECUTIVE_RETRY_COUNT, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TRANSMISSION_RETRY_COUNT, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TRANSMITTING_STATE, null, activityStateCreator);
    }

    /**
     * Builds a ScanJobStatus instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScanJobStatus instance
     * @throws Error
     *              When errors are detected
     */
    static ScanJobStatus parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        setupXMLTagHandler(tagHandler);

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new ScanJobStatus(tagHandler);
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
        dest.writeInt(this.cancelingState.ordinal());
        dest.writeInt(this.isCancelable ? 1: 0);
        dest.writeInt(this.processingState.ordinal());
        dest.writeInt(this.processRestartCount);
        dest.writeInt(this.resultCode.ordinal());
        dest.writeString(this.resultReason);
        dest.writeInt(this.scanningState.ordinal());
        dest.writeInt(this.totalImagesProcessed);
        dest.writeInt(this.totalImagesScanned);
        dest.writeInt(this.totalImagesTransmitted);
        dest.writeInt(this.transmissionConsecutiveRetryCount);
        dest.writeInt(this.transmissionRetryCount);
        dest.writeInt(this.transmittingState.ordinal());
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ScanJobStatus(Parcel in) {
        this.cancelingState = ActivityState.values()[in.readInt()];
        this.isCancelable = in.readInt() > 0;
        this.processingState = ActivityState.values()[in.readInt()];
        this.processRestartCount = in.readInt();
        this.resultCode = ResultCode.values()[in.readInt()];
        this.resultReason = in.readString();
        this.scanningState = ActivityState.values()[in.readInt()];
        this.totalImagesProcessed = in.readInt();
        this.totalImagesScanned = in.readInt();
        this.totalImagesTransmitted = in.readInt();
        this.transmissionConsecutiveRetryCount = in.readInt();
        this.transmissionRetryCount = in.readInt();
        this.transmittingState = ActivityState.values()[in.readInt()];
    }

    /**
     * ScanJobStatus creator
     */
    public static final Parcelable.Creator<ScanJobStatus> CREATOR =
            new Parcelable.Creator<ScanJobStatus>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ScanJobStatus#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ScanJobStatus createFromParcel(Parcel in) {
                    return new ScanJobStatus(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ScanJobStatus[] newArray(int size) {
                    return new ScanJobStatus[size];
                }
            };
}
