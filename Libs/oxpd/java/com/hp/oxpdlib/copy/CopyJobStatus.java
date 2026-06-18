// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.scan.ResultCode;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

public class CopyJobStatus {

    private static final String XML_TAG__COPY__CANCELING_STATE = "cancelingState";
    private static final String XML_TAG__COPY__IS_CANCELABLE = "isCancelable";
    private static final String XML_TAG__COPY__PROCESSING_STATE = "processingState";
    private static final String XML_TAG__COPY__RESULT_CODE = "resultCode";
    private static final String XML_TAG__COPY__RESULT_REASON = "resultReason";
    private static final String XML_TAG__COPY__SCANNING_STATE = "scanningState";
    private static final String XML_TAG__COPY__TOTAL_IMAGES_SCANNED = "totalImagesScanned";
    private static final String XML_TAG__COPY__TOTAL_SHEET_PRINTED = "totalSheetsPrinted";
    private static final String XML_TAG__COPY__PRINTING_STATE = "printingState";

    public JobResultCode resultCode;
    public String resultReason;
    public boolean isCancelable;
    public ActivityState scanningState;
    public ActivityState processingState;
    public ActivityState printingState;
    public ActivityState cancelingState;
    public int totalImagesScanned;
    public int totalSheetsPrinted;

    /**
     * Default no-arg constructor
     * 
     */
    public CopyJobStatus() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param scanningState
     *     the ActivityState
     * @param processingState
     *     the ActivityState
     * @param cancelingState
     *     the ActivityState
     * @param resultReason
     *     the String
     * @param resultCode
     *     the JobResultCode
     * @param isCancelable
     *     the boolean
     * @param printingState
     *     the ActivityState
     * @param totalSheetsPrinted
     *     the int
     * @param totalImagesScanned
     *     the int
     */
    public CopyJobStatus(final JobResultCode resultCode, final String resultReason, final boolean isCancelable, final ActivityState scanningState, final ActivityState processingState, final ActivityState printingState, final ActivityState cancelingState, final int totalImagesScanned, final int totalSheetsPrinted) {
        this.resultCode = resultCode;
        this.resultReason = resultReason;
        this.isCancelable = isCancelable;
        this.scanningState = scanningState;
        this.processingState = processingState;
        this.printingState = printingState;
        this.cancelingState = cancelingState;
        this.totalImagesScanned = totalImagesScanned;
        this.totalSheetsPrinted = totalSheetsPrinted;
    }

    public CopyJobStatus(RestXMLTagHandler tagHandler) throws Error {
        OXPdCopy.faultExceptionCheck(tagHandler);

        this.cancelingState = (ActivityState) tagHandler.getTagData(XML_TAG__COPY__CANCELING_STATE);
        this.isCancelable = (Boolean)tagHandler.getTagData(XML_TAG__COPY__IS_CANCELABLE, Boolean.FALSE);
        this.processingState = (ActivityState)tagHandler.getTagData(XML_TAG__COPY__PROCESSING_STATE);
        this.resultCode = (JobResultCode)tagHandler.getTagData(XML_TAG__COPY__RESULT_CODE);
        this.resultReason = (String)tagHandler.getTagData(XML_TAG__COPY__RESULT_REASON);
        this.scanningState = (ActivityState)tagHandler.getTagData(XML_TAG__COPY__SCANNING_STATE);
        this.totalImagesScanned = (Integer)tagHandler.getTagData(XML_TAG__COPY__TOTAL_IMAGES_SCANNED, 0);
        this.totalSheetsPrinted = (Integer)tagHandler.getTagData(XML_TAG__COPY__TOTAL_SHEET_PRINTED, 0);
        this.printingState = (ActivityState)tagHandler.getTagData(XML_TAG__COPY__PRINTING_STATE);
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
                } catch (NumberFormatException ignored) {
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
                handler.setTagData(localName, JobResultCode.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(XML_TAG__COPY__CANCELING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__IS_CANCELABLE, null, booleanCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__PROCESSING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__RESULT_CODE, null, resultCodeCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__RESULT_REASON, null, stringCollector);
        tagHandler.setXMLHandler(XML_TAG__COPY__SCANNING_STATE, null, activityStateCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__TOTAL_IMAGES_SCANNED, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__TOTAL_SHEET_PRINTED, null, integerCreator);
        tagHandler.setXMLHandler(XML_TAG__COPY__PRINTING_STATE, null, activityStateCreator);
    }

    public static CopyJobStatus parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new CopyJobStatus(tagHandler);

    }

    /**
     * String representation of CopyJobStatus
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("resultCode=").append(((resultCode == null)?"null":resultCode.toString())).append(", ").append("resultReason=").append(resultReason).append(", ").append("isCancelable=").append((isCancelable?"True":"False")).append(", ").append("scanningState=").append(((scanningState == null)?"null":scanningState.toString())).append(", ").append("processingState=").append(((processingState == null)?"null":processingState.toString())).append(", ").append("printingState=").append(((printingState == null)?"null":printingState.toString())).append(", ").append("cancelingState=").append(((cancelingState == null)?"null":cancelingState.toString())).append(", ").append("totalImagesScanned=").append(totalImagesScanned).append(", ").append("totalSheetsPrinted=").append(totalSheetsPrinted).append(", ").toString();
    }
}
