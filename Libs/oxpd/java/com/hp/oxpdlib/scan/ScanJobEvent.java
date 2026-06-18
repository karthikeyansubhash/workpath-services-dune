// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Container for Scan Job Event data sent by printer
 */
@SuppressWarnings("WeakerAccess")
public class ScanJobEvent implements Parcelable {

    /** Debugging tag */
    private static final String TAG = "ScanJobEvent";

    /**
     * The timestamped change(s) in status that initiated this event. To improve event delivery
     * performance, device implementations may list multiple, temporally ordered (the oldest status
     * change listed first) changes in status for the same job in a single ScanJobEvent. The status
     * field always represents the status of the job at the time the ScanJobEvent was sent from the
     * device.
     */
    private static final String XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS = "eventCodesWithTimestamps";
    /**
     * A ScanJobEventCode with a timestamp.
     */
    private static final String XML_TAG__SCAN__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP = "ScanJobEventCodeWithTimestamp";
    /**
     * The status change.
     */
    private static final String XML_TAG__SCAN__EVENT_CODE = "eventCode";
    /**
     * The date and time on the device when this event was sent (device local time).
     */
    private static final String XML_TAG__SCAN__TIMESTAMP = "timestamp";
    /**
     * The ordinal of the event.
     */
    private static final String XML_TAG__SCAN__ORDINAL = "ordinal";

    /**
     * Dummy tag to store timestamp value for event codes
     */
    private static final String DUMMY_TAG__EVENTCODE_TIMESTAMP = "eventCode_timestamp";

    /**
     * Remote scan job event
     */
    private static final String XML_TAG__SCAN__REMOTE_SCAN_JOB_EVENT = "RemoteScanJobEvent";
    /**
     * Remote scan job event encrypted data
     */
    private static final String XML_TAG__SCAN__ENCRYPTED_DATA_CONTAINER = "encryptedDataContainer";

    /**
     * {@link ScanJobEventCode} with a timestamp
     */
    @SuppressWarnings("WeakerAccess")
    public static class TimestampedEventCode implements Parcelable {
        /** Scan job event code */
        public final ScanJobEventCode mEventCode;
        /** Event timestamp */
        public final String mTimestamp;

        /**
         * Constructor
         * @param eventCode
         *          Scan Job Event
         * @param timestamp
         *          Event timestamp
         */
        private TimestampedEventCode(ScanJobEventCode eventCode, String timestamp) {
            mEventCode = eventCode;
            mTimestamp = timestamp;
        }

        /**
         * Constructor
         * @param in Parcel from which the object should be reconstructed
         */
        private TimestampedEventCode(Parcel in) {
            mEventCode = ScanJobEventCode.fromValue(in.readString());
            mTimestamp = in.readString();
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
            dest.writeString(mEventCode.mValue);
            dest.writeString(mTimestamp);
        }

        /**
         * {@link TimestampedEventCode} creator
         */
        public static final Creator<TimestampedEventCode> CREATOR = new Creator<TimestampedEventCode>() {
            /**
             * Create a new instance of {@link TimestampedEventCode} class, instantiating it from the given Parcel whose data had previously been written by {@link TimestampedEventCode#writeToParcel(Parcel, int)}
             * @param in The Parcel to read the object's data from.
             * @return Returns a new instance of {@link TimestampedEventCode}
             */
            @Override
            public TimestampedEventCode createFromParcel(Parcel in) {
                return new TimestampedEventCode(in);
            }

            /**
             * Create a new array of {@link TimestampedEventCode}
             * @param size Create a new array of {@link TimestampedEventCode}
             * @return Returns an array of {@link TimestampedEventCode}, with every entry initialized to null.
             */
            @Override
            public TimestampedEventCode[] newArray(int size) {
                return new TimestampedEventCode[size];
            }
        };
    }

    /**
     * List of timestamp change(s) in status that initiated this event
     */
    public final List<TimestampedEventCode> mEventList;
    /**
     * The ID of the scan job to which this event pertains.
     */
    public final ScanJobID mScanJobID;
    /**
     * The ordinal of the event.
     */
    public final int mOrdinal;
    /**
     * The date and time on the device when this event was sent (device local time).
     */
    public final String mTimestamp;
    /**
     * The server context ID that was supplied when the scan job was started
     */
    final String mServerContextID;
    /**
     * The scan job's status at the time this event was sent from the device.
     */
    public final ScanJobStatus mJobStatus;

    /**
     * Constructor used by the library to construct ScanJobEvent object from the device
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    private ScanJobEvent(RestXMLTagHandler tagHandler) throws Error {
        //noinspection unchecked
        List<TimestampedEventCode> eventList = (List<TimestampedEventCode>) tagHandler.getTagData(XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS, Collections.emptyList());
        mEventList = Collections.unmodifiableList(eventList);
        mScanJobID = new ScanJobID(tagHandler);
        mJobStatus = new ScanJobStatus(tagHandler);
        mOrdinal = (Integer)tagHandler.getTagData(XML_TAG__SCAN__ORDINAL, 0);
        mTimestamp = (String)tagHandler.getTagData(XML_TAG__SCAN__TIMESTAMP);
        mServerContextID = (String)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SERVER_CONTEXT_ID);
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
                if (TextUtils.equals(localName, XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS)) {
                    handler.setTagData(XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS, new ArrayList<TimestampedEventCode>());
                } else if (TextUtils.equals(localName, XML_TAG__SCAN__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    handler.clearTagData(XML_TAG__SCAN__EVENT_CODE);
                    handler.clearTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler eventCodeEndHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(localName, XML_TAG__SCAN__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    //noinspection unchecked
                    List<TimestampedEventCode> eventList = (List<TimestampedEventCode>) handler.getTagData(XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS);
                    if (eventList != null) {
                        ScanJobEventCode eventCode = (ScanJobEventCode)handler.getTagData(XML_TAG__SCAN__EVENT_CODE);
                        String timestamp = (String)handler.getTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP);
                        if (eventCode != null) {
                            eventList.add(new TimestampedEventCode(eventCode, timestamp));
                        }
                    }
                } else if (TextUtils.equals(localName, XML_TAG__SCAN__EVENT_CODE)) {
                    handler.setTagData(XML_TAG__SCAN__EVENT_CODE, ScanJobEventCode.fromValue(data));
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler timestampHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (xmlTagStack.isTagInStack(null, XML_TAG__SCAN__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP)) {
                    handler.setTagData(DUMMY_TAG__EVENTCODE_TIMESTAMP, data);
                } else {
                    handler.setTagData(XML_TAG__SCAN__TIMESTAMP, data);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler otherFieldsHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(localName, XML_TAG__SCAN__ORDINAL)) {
                    Integer ordinal;
                    try {
                        ordinal = Integer.valueOf(data);
                    } catch(Exception ignored) {
                        ordinal = 0;
                    }
                    handler.setTagData(XML_TAG__SCAN__ORDINAL, ordinal);
                } else if (TextUtils.equals(localName, OXPdScan.Constants.XML_TAG__SCAN__SERVER_CONTEXT_ID)) {
                    handler.setTagData(OXPdScan.Constants.XML_TAG__SCAN__SERVER_CONTEXT_ID, data);
                }
            }
        };

        tagHandler.setXMLHandler(XML_TAG__SCAN__EVENT_CODES_WITH_TIMESTAMPS, eventCodeStartHandler, null);
        tagHandler.setXMLHandler(XML_TAG__SCAN__SCAN_JOB_EVENT_CODE_WITH_TIMESTAMP, eventCodeStartHandler, eventCodeEndHandler);
        tagHandler.setXMLHandler(XML_TAG__SCAN__EVENT_CODE, null, eventCodeEndHandler);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TIMESTAMP, null, timestampHandler);
        tagHandler.setXMLHandler(XML_TAG__SCAN__ORDINAL, null, otherFieldsHandler);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__SERVER_CONTEXT_ID, null, otherFieldsHandler);

        tagHandler.setXMLHandler(XML_TAG__SCAN__ENCRYPTED_DATA_CONTAINER, null, new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (!xmlTagStack.isTagInStack(null, XML_TAG__SCAN__REMOTE_SCAN_JOB_EVENT)) return;
                handler.setTagData(XML_TAG__SCAN__ENCRYPTED_DATA_CONTAINER, data);
            }
        });

    }

    /**
     * Builds a ScanJobEvent instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param xmlResponse
     *              XML response data
     * @param tagHandler
     *              XML tag handler
     * @param decryptionKey
     *              Decryption key
     * @return
     *              ScanJobStatus instance
     * @throws Error
     *              When errors are detected
     */
    static ScanJobEvent parseRequestResult(OXPdDevice device, String xmlResponse, RestXMLTagHandler tagHandler, SecretKeySpec decryptionKey, String algorithm) throws Error {

        setupXMLTagHandler(tagHandler);
        ScanJobStatus.setupXMLTagHandler(tagHandler);
        ScanJobID.setupXMLTagHandler(tagHandler);

        // parse the data
        device.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        // encrypted payload?
        xmlResponse = (String)tagHandler.getTagData(XML_TAG__SCAN__ENCRYPTED_DATA_CONTAINER);
        if (!TextUtils.isEmpty(xmlResponse)) {
            device.log(Log.DEBUG, TAG, "DECRYPTION start");
            try {
                Cipher cipher = Cipher.getInstance(algorithm);
                byte[] rawData = Base64.decode(xmlResponse, Base64.DEFAULT);
                cipher.init(Cipher.DECRYPT_MODE, decryptionKey, new IvParameterSpec(rawData, 0, 16));
                xmlResponse = new String(cipher.doFinal(rawData, 16, rawData.length - 16));
                device.log(Log.DEBUG, TAG, "DECRYPTED: \n" + xmlResponse);
                // parse decrypted data
                tagHandler.cleanupData();
                tagHandler.removeXMLHandlers(XML_TAG__SCAN__ENCRYPTED_DATA_CONTAINER);
                device.parseXMLResponse(xmlResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
            } catch (Exception ignored) {
                device.log(Log.ERROR, TAG, "DECRYPTION failed");
                throw new Error(ErrorName.Unknown, "decryption error");
            }
        }

        return new ScanJobEvent(tagHandler);
    }

    /**
     * Constructor
     * @param in Parcel from which the object should be reconstructed
     */
    private ScanJobEvent(Parcel in) {
        List<TimestampedEventCode> eventList = in.createTypedArrayList(TimestampedEventCode.CREATOR);
        mEventList = Collections.unmodifiableList(eventList);
        mScanJobID = in.readParcelable(ScanJobID.class.getClassLoader());
        mOrdinal = in.readInt();
        mTimestamp = in.readString();
        mServerContextID = in.readString();
        mJobStatus = in.readParcelable(ScanJobStatus.class.getClassLoader());
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
        dest.writeTypedList(mEventList);
        dest.writeParcelable(mScanJobID, flags);
        dest.writeInt(mOrdinal);
        dest.writeString(mTimestamp);
        dest.writeString(mServerContextID);
        dest.writeParcelable(mJobStatus, flags);
    }

    /**
     * {@link ScanJobEvent} Creator
     */
    public static final Creator<ScanJobEvent> CREATOR = new Creator<ScanJobEvent>() {
        /**
         * Create a new instance of {@link ScanJobEvent} class, instantiating it from the given Parcel whose data had previously been written by {@link ScanJobEvent#writeToParcel(Parcel, int)}
         * @param in The Parcel to read the object's data from.
         * @return Returns a new instance of {@link ScanJobEvent}
         */
        @Override
        public ScanJobEvent createFromParcel(Parcel in) {
            return new ScanJobEvent(in);
        }

        /**
         * Create a new array of {@link ScanJobEvent}
         * @param size Create a new array of {@link ScanJobEvent}
         * @return Returns an array of {@link ScanJobEvent}, with every entry initialized to null.
         */
        @Override
        public ScanJobEvent[] newArray(int size) {
            return new ScanJobEvent[size];
        }
    };
}
