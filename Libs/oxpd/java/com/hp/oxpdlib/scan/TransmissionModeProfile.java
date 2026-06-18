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

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Device supported transmission modes
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TransmissionModeProfile implements Parcelable {

    /**
     * List of destination types supported by the device in 'Job' transmission mode. (May be empty.)
     */
    public final List<DestinationType> jobModeDestinationTypes;
    /**
     * List of destination types supported by the device in 'Image' transmission mode. (May be empty.)
     */
    public final List<DestinationType> imageModeDestinationTypes;

    /**
     * Constructor used by the library to construct TransmissionModeProfile objects.
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    private TransmissionModeProfile(RestXMLTagHandler tagHandler) throws Error
    {
        OXPdScan.faultExceptionCheck(tagHandler);

        List<DestinationType> destinationList;
        //noinspection unchecked
        HashMap<TransmissionMode, List<DestinationType>> transmissionModeMap =
                (HashMap<TransmissionMode, List<DestinationType>>)
                        tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODES);

        //noinspection ConstantConditions
        destinationList = transmissionModeMap.get(TransmissionMode.Job);
        if (destinationList == null) {
            destinationList = Collections.emptyList();
        }
        jobModeDestinationTypes = Collections.unmodifiableList(destinationList);

        destinationList = transmissionModeMap.get(TransmissionMode.Image);
        if (destinationList == null) {
            destinationList = Collections.emptyList();
        }
        imageModeDestinationTypes = Collections.unmodifiableList(destinationList);
    }

    /**
     * Builds a TransmissionModeProfile instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              TransmissionModeProfile instance
     * @throws Error
     *              When errors are detected
     */
    static TransmissionModeProfile parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        // define handlers
        RestXMLTagHandler.XMLStartTagHandler startTagHandler = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODES, localName)) {
                    handler.setTagData(localName, new HashMap<TransmissionMode, DestinationType>());
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES, localName)){
                    handler.setTagData(localName, new ArrayList<DestinationType>());
                }
            }
        };
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @SuppressWarnings({"unchecked", "ConstantConditions"})
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPE, localName)) {
                    DestinationType type = DestinationType.fromAttributeValue(data);
                    if (type != null) {
                        ((List<DestinationType>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES)).add(type);
                    }
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__TRANSMISSION_MODE, localName)) {
                    TransmissionMode mode = TransmissionMode.fromAttributeValue(data);
                    if (mode != null) {
                        handler.setTagData(localName, mode);
                    }
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODE, localName)) {
                    List<DestinationType> list = (List<DestinationType>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES);
                    TransmissionMode mode = (TransmissionMode) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TRANSMISSION_MODE);
                    if (mode != null) {
                        ((HashMap<TransmissionMode,List<DestinationType>>)
                                handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODES))
                                .put(mode, list);

                    }
                    handler.setTagData(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES, null);
                    handler.setTagData(OXPdScan.Constants.XML_TAG__SCAN__TRANSMISSION_MODE, null);
                }
            }
        };
        // add handlers
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODES, startTagHandler, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES, startTagHandler, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__TRANSMISSION_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DESTINATION_TYPES_BY_TRANSMISSION_MODE, null, infoCollector);

        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        // build object if possible
        return new TransmissionModeProfile(tagHandler);
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
        dest.writeInt(this.jobModeDestinationTypes.size());
        for(Enum enumValue : this.jobModeDestinationTypes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.imageModeDestinationTypes.size());
        for(Enum enumValue : this.imageModeDestinationTypes) {
            dest.writeInt(enumValue.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private TransmissionModeProfile(Parcel in) {
        List<DestinationType> jobModeDestinationTypes = new ArrayList<DestinationType>();
        for(int length = in.readInt(); length > 0; length--) {
            jobModeDestinationTypes.add(DestinationType.values()[in.readInt()]);
        }
        this.jobModeDestinationTypes = Collections.unmodifiableList(jobModeDestinationTypes);
        List<DestinationType> imageModeDestinationTypes = new ArrayList<DestinationType>();
        for(int length = in.readInt(); length > 0; length--) {
            imageModeDestinationTypes.add(DestinationType.values()[in.readInt()]);
        }
        this.imageModeDestinationTypes = Collections.unmodifiableList(imageModeDestinationTypes);

    }

    /**
     * TransmissionModeProfile creator
     */
    public static final Parcelable.Creator<TransmissionModeProfile> CREATOR =
            new Parcelable.Creator<TransmissionModeProfile>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link TransmissionModeProfile#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public TransmissionModeProfile createFromParcel(Parcel in) {
                    return new TransmissionModeProfile(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public TransmissionModeProfile[] newArray(int size) {
                    return new TransmissionModeProfile[size];
                }
            };
}
