// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CopyJobEvent {

    /**
     * The timestamped change(s) in status that initiated this event. To improve event delivery
     * performance, device implementations may list multiple, temporally ordered (the oldest status
     * change listed first) changes in status for the same job in a single CopyJobEvent. The status
     * field always represents the status of the job at the time the ScanJobEvent was sent from the
     * device.
     */
    private static final String XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS = "copyJobActivities";
    /**
     * A CopyJobEventCode with a timestamp.
     */
    private static final String XML_TAG__COPY__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP = "CopyJobActivity";
    /**
     * The status change.
     */
    private static final String XML_TAG__COPY__EVENT_CODE = "activityType";
    /**
     * The date and time on the device when this event was sent (device local time).
     */
    private static final String XML_TAG__COPY__TIMESTAMP = "timestamp";
    /**
     * The ordinal of the event.
     */
    private static final String XML_TAG__COPY__ORDINAL = "ordinal";

    /**
     * Dummy tag to store timestamp value for event codes
     */
    private static final String DUMMY_TAG__EVENTCODE_TIMESTAMP = "eventCode_timestamp";

    public List<CopyJobActivity> mEventList;
    public CopyJobID mCopyJobId;
    public long mOrdinal;
    public String mTimestamp;
    public String mServerContextID;
    public CopyJobStatus mJobStatus;

    public CopyJobEvent(RestXMLTagHandler tagHandler) throws Error {
        OXPdCopy.faultExceptionCheck(tagHandler);

        List<CopyJobActivity> eventList = (List<CopyJobActivity>) tagHandler.getTagData(XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS, Collections.emptyList());
        mEventList = Collections.unmodifiableList(eventList);
        mCopyJobId = new CopyJobID(tagHandler);
        mJobStatus = new CopyJobStatus(tagHandler);
        mOrdinal = (Integer)tagHandler.getTagData(XML_TAG__COPY__ORDINAL, 0);
        mTimestamp = (String)tagHandler.getTagData(XML_TAG__COPY__TIMESTAMP);
        mServerContextID = (String)tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__SERVER_CONTEXT_ID);
    }

    /**
     * Configure the handler to process XML payload
     * @param tagHandler
     *              XML handler to extract data from
     */
    private static void setupXMLTagHandler(final RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLStartTagHandler eventCodeStartHandler = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(localName, XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS)) {
                    handler.setTagData(XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS, new ArrayList<CopyJobActivity>());
                } else if (TextUtils.equals(localName, XML_TAG__COPY__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    handler.clearTagData(XML_TAG__COPY__EVENT_CODE);
                    handler.clearTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler eventCodeEndHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(localName, XML_TAG__COPY__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    //noinspection unchecked
                    List<CopyJobActivity> eventList = (List<CopyJobActivity>) handler.getTagData(XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS);
                    if (eventList != null) {
                        CopyJobActivityType eventCode = (CopyJobActivityType)handler.getTagData(XML_TAG__COPY__EVENT_CODE);
                        String timestamp = (String)handler.getTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP);
                        if (eventCode != null) {
                            eventList.add(new CopyJobActivity(eventCode, timestamp));
                        }
                    }
                } else if (TextUtils.equals(localName, XML_TAG__COPY__EVENT_CODE)) {
                    handler.setTagData(XML_TAG__COPY__EVENT_CODE, CopyJobActivityType.fromAttributeValue(data));
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler timestampHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (xmlTagStack.isTagInStack(null, XML_TAG__COPY__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    handler.setTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP, data);
                } else {
                    handler.setTagData(XML_TAG__COPY__TIMESTAMP, data);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler otherFieldsHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(localName, XML_TAG__COPY__ORDINAL)) {
                    Integer ordinal;
                    try {
                        ordinal = Integer.valueOf(data);
                    } catch(Exception ignored) {
                        ordinal = 0;
                    }
                    handler.setTagData(XML_TAG__COPY__ORDINAL, ordinal);
                } else if (TextUtils.equals(localName, OXPdCopy.Constants.XML_TAG__COPY__SERVER_CONTEXT_ID)) {
                    handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__SERVER_CONTEXT_ID, data);
                }
            }
        };

        tagHandler.setXMLHandler(XML_TAG__COPY__EVENT_CODES_WITH_TIMESTAMPS, eventCodeStartHandler, null);
        tagHandler.setXMLHandler(XML_TAG__COPY__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP, eventCodeStartHandler, eventCodeEndHandler);
        tagHandler.setXMLHandler(XML_TAG__COPY__EVENT_CODE, null, eventCodeEndHandler);
        tagHandler.setXMLHandler(XML_TAG__COPY__TIMESTAMP, null, timestampHandler);
        tagHandler.setXMLHandler(XML_TAG__COPY__ORDINAL, null, otherFieldsHandler);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__SERVER_CONTEXT_ID, null, otherFieldsHandler);
    }

    /**
     * Builds a CopyJobEvent instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param xmlResponse
     *              XML response data
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScanJobStatus instance
     * @throws Error
     *              When errors are detected
     */
    static CopyJobEvent parseRequestResult(OXPdDevice device, String xmlResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);
        CopyJobStatus.setupXMLTagHandler(tagHandler);
        CopyJobID.setupXMLTagHandler(tagHandler);

        // parse the data
        device.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new CopyJobEvent(tagHandler);
    }

}
