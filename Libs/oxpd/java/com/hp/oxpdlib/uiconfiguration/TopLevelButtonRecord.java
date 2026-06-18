// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.text.TextUtils;
import android.util.Base64;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.common.Binding;
import com.hp.oxpdlib.common.NetworkCredentials;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import org.xml.sax.Attributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * OMNI button record
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TopLevelButtonRecord {

    /** XML tag to register button, {@link OXPdUIConfiguration#SOAP_OP__REGISTER_TOP_LEVEL_BUTTON} */
    private static final String XML_TAG__UI_CONFIGURATION__REGISTER__TOP_LEVEL_BUTTON_RECORD = "topLevelButtonRecord";
    /** XML tag used in listing registered buttons */
    private static final String XML_TAG__UI_CONFIGURATION__TOP_LEVEL_BUTTON_RECORD = "TopLevelButtonRecord";
    /** XML tag containing application UUID */
    private static final String XML_TAG__UI_CONFIGURATION__ID = "id";
    /** XML tag containing list of localized title values */
    private static final String XML_TAG__UI_CONFIGURATION__TITLE = "title";
    /** XML tag containing list of localized description values */
    private static final String XML_TAG__UI_CONFIGURATION__DESCRIPTION = "description";
    /** XML tag containing requested position */
    private static final String XML_TAG__UI_CONFIGURATION__REQUESTED_POSITION = "requestedPosition";
    /** XML tag containing BASE64 encoded icon data */
    private static final String XML_TAG__UI_CONFIGURATION__ICON = "icon";
    /** XML tag containing single button record, returned by {@link OXPdUIConfiguration#SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORD} */
    private static final String XML_TAG__UI_CONFIGURATION__GET_TOP_LEVEL_BUTTON_RECORD_RESULT = "GetTopLevelButtonRecordResult";
    /** XML tag containing list of button records, turned by {@link OXPdUIConfiguration#SOAP_OP__GET_TOP_LEVEL_BUTTON_RECORDS} */
    private static final String XML_TAG__UI_CONFIGURATION__GET_TOP_LEVEL_BUTTON_RECORDS_RESULT = "GetTopLevelButtonRecordsResult";

    /** Application UUID */
    public final UUID mButtonID;
    /** List of localized application title values */
    public final List<LocalizedString> mTitle;
    /** List of localized application title values */
    public final List<LocalizedString> mDescription;
    /** Requested position */
    public final int mRequestedPosition;
    /** Application target URL */
    public final URL mButtonTarget;
    /** Application target URL post query string */
    public final String mButtonTargetPostQuery;
    /** Application target URL binding */
    public final Binding mButtonTargetBinding;
    /** Login credentials, if needed */
    public final NetworkCredentials mNetworkCredentials;
    /** Icon data */
    public final byte[] mIconData;

    /**
     * Constructor
     * @param buttonID
     *              Application UUID
     * @param title
     *              List of localized application title values
     * @param description
     *              List of localized application description values
     * @param requestedPosition
     *              Requested position
     * @param buttonTarget
     *              Application target URL
     * @param networkCredentials
     *              Network credentials, if needed
     * @param iconData
     *              Icon data
     */
    private TopLevelButtonRecord(UUID buttonID,
                                 List<LocalizedString> title,
                                 List<LocalizedString> description,
                                 int requestedPosition,
                                 URL buttonTarget,
                                 Binding targetBinding,
                                 String targetPostQuery,
                                 NetworkCredentials networkCredentials,
                                 byte[] iconData) {
        Collections.sort(title);
        Collections.sort(description);
        mButtonID = buttonID;
        mTitle = Collections.unmodifiableList(title);
        mDescription = Collections.unmodifiableList(description);
        mRequestedPosition = requestedPosition;
        mButtonTarget = buttonTarget;
        mButtonTargetBinding = targetBinding;
        mButtonTargetPostQuery = targetPostQuery;
        mNetworkCredentials = networkCredentials;
        mIconData = iconData;
    }

    /**
     * Returns the hash code value for the button record
     * @return Button record hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + mButtonID.hashCode();
        result = result * prime + mTitle.hashCode();
        result = result * prime + mDescription.hashCode();
        result = result * prime + mRequestedPosition;
        result = result * prime + mButtonTarget.hashCode();
        result = result * prime + ((mNetworkCredentials != null) ? mNetworkCredentials.hashCode() : 0);
        result = result * prime + Arrays.hashCode(mIconData);
        return result;
    }

    /**
     * Compares the specified object with this one for equality. Returns true if only if the specified object
     * is also a {@link LocalizedString} with same username/password/domain values
     * @param obj Object to be compared  for equality
     * @return True if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TopLevelButtonRecord)) return false;
        TopLevelButtonRecord other = (TopLevelButtonRecord)obj;
        return (mButtonID.equals(other.mButtonID)
                && mTitle.equals(other.mTitle)
                && mDescription.equals(other.mDescription)
                && (mRequestedPosition == other.mRequestedPosition)
                && mButtonTarget.equals(other.mButtonTarget)
                && ((mNetworkCredentials == other.mNetworkCredentials)
                    || ((mNetworkCredentials != null)
                        && mNetworkCredentials.equals(other.mNetworkCredentials)))
                && (Arrays.equals(mIconData, other.mIconData)));
    }

    /**
     * Button record builder
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {
        /** Application UUID */
        private UUID mButtonID = null;
        /** Requested button position */
        private int mRequestedPosition = 0;
        /** Application URL */
        private URL mButtonTarget;
        /** Application target URL post query string */
        public String mButtonTargetPostQuery = null;
        /** Application target URL binding */
        public Binding mButtonTargetBinding = Binding.Plain;
        /** Localized application title values */
        private HashMap<Locale, LocalizedString> mTitle = new HashMap<Locale, LocalizedString>();
        /** Localized application description values */
        private HashMap<Locale, LocalizedString> mDescription = new HashMap<Locale, LocalizedString>();
        /** Network credentials, if needed */
        private NetworkCredentials mNetworkCredentials = null;
        /** Icon data */
        private byte[] mIconData;

        /**
         * Constructor
         */
        public Builder() {}

        /**
         * Set application UUID value
         * @param buttonID Application UUID
         * @return this builder for method chaining
         */
        public Builder setButtonID(UUID buttonID) {
            mButtonID = buttonID;
            return this;
        }

        /**
         * Set application UUID value
         * @param buttonID Application UUID
         * @return this builder for method chaining
         */
        public Builder setButtonID(String buttonID) {
            try {
                mButtonID = UUID.fromString(buttonID);
            } catch(IllegalArgumentException ignored) {}
            return this;
        }

        /**
         * Set application requested position
         * @param requestedPosition Requested position value
         * @return this builder
         */
        public Builder setRequestedPosition(int requestedPosition) {
            mRequestedPosition = requestedPosition;
            return this;
        }

        /**
         * Set application requested position
         * @param requestedPosition Requested position value
         * @return this builder
         */
        Builder setRequestedPosition(String requestedPosition) {
            try {
                mRequestedPosition = Integer.valueOf(requestedPosition);
            } catch(NumberFormatException ignored) {}
            return this;
        }

        /**
         * Set application URL target
         * @param buttonTarget Application URL
         * @return this builder
         */
        public Builder setButtonTarget(URL buttonTarget) {
            mButtonTarget = buttonTarget;
            return this;
        }

        /**
         * Set application URL target
         * @param buttonTarget Application URL
         * @return this builder
         */
        Builder setButtonTarget(String buttonTarget) {
            try {
                mButtonTarget = new URL(buttonTarget);
            } catch (MalformedURLException ignored) {}
            return this;
        }

        /**
         * Set application URL target binding
         * @param targetBinding Application URL binding
         * @return this builder
         */
        public Builder setButtonTargetBinding(Binding targetBinding) {
            mButtonTargetBinding = targetBinding;
            return this;
        }

        /**
         * Set application URL post query string
         * @param targetPostQuery Application URL post query string
         * @return this builder
         */
        public Builder setButtonTargetPostQuery(String targetPostQuery) {
            mButtonTargetPostQuery = targetPostQuery;
            return this;
        }

        /**
         * Add localized application title value
         * @param title Localized title
         * @return this builder
         */
        public Builder addTitle(LocalizedString title) {
            mTitle.put(title.mLocale, title);
            return this;
        }

        /**
         * Add localized application description value
         * @param description Localized description
         * @return this builder
         */
        public Builder addDescription(LocalizedString description) {
            mDescription.put(description.mLocale, description);
            return this;
        }

        /**
         * Add network credentials if needed
         * @param networkCredentials Network credentials
         * @return this builder
         */
        public Builder setNetworkCredentials(NetworkCredentials networkCredentials) {
            mNetworkCredentials = networkCredentials;
            return this;
        }

        /**
         * Set application icon data
         * @param iconData Icon data
         * @return this builder
         */
        public Builder setIconData(byte[] iconData) {
            mIconData = iconData;
            return this;
        }

        /**
         * Set application icon data
         * @param base64IconData Icon data
         * @return this builder
         */
        public Builder setIconData(String base64IconData) {
            return setIconData(Base64.decode(base64IconData, Base64.DEFAULT));
        }

        /**
         * Build the button record
         * @return Button record instance
         */
        public TopLevelButtonRecord build() {
            return new TopLevelButtonRecord(
                    mButtonID,
                    new ArrayList<LocalizedString>(mTitle.values()),
                    new ArrayList<LocalizedString>(mDescription.values()),
                    mRequestedPosition,
                    mButtonTarget,
                    mButtonTargetBinding,
                    mButtonTargetPostQuery,
                    mNetworkCredentials,
                    mIconData
            );
        }
    }

    /**
     * Parse Button record information from the request
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              SOAP request/response container
     * @param tagHandler
     *              Response handler
     * @return
     *              List of {@link TopLevelButtonRecord} contained in response
     * @throws Error
     *              If an HTTP error occurs or response contains an error
     */
    static List<TopLevelButtonRecord> parseGetTopLevelButtonRecords(OXPdDevice device, HttpRequestResponseContainer requestResponse, final RestXMLTagHandler tagHandler) throws Error {
        RestXMLTagHandler.XMLEndTagHandler recordDataCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                TopLevelButtonRecord.Builder builder = (TopLevelButtonRecord.Builder)handler.getTagData(XML_TAG__UI_CONFIGURATION__TOP_LEVEL_BUTTON_RECORD);
                if (builder == null) return;
                if (TextUtils.equals(XML_TAG__UI_CONFIGURATION__ID, localName)) {
                    builder.setButtonID(data);
                } else if (TextUtils.equals(XML_TAG__UI_CONFIGURATION__ICON, localName)) {
                    builder.setIconData(data);
                } else if (TextUtils.equals(XML_TAG__UI_CONFIGURATION__REQUESTED_POSITION, localName)) {
                    builder.setRequestedPosition(data);
                } else if (TextUtils.equals(OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__POST_QUERY, localName)) {
                    builder.setButtonTargetPostQuery(data);
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__COMMON__URI, localName)) {
                    builder.setButtonTarget(data);
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__COMMON__BINDING, localName)) {
                    builder.setButtonTargetBinding(Binding.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__COMMON__LOCALIZED_STRING, localName)) {
                    LocalizedString localizedString = new LocalizedString(
                            (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__CODE),
                            (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__VALUE));
                    if (xmlTagStack.isTagInStack(null, XML_TAG__UI_CONFIGURATION__TITLE)) {
                        builder.addTitle(localizedString);
                    } else if (xmlTagStack.isTagInStack(null, XML_TAG__UI_CONFIGURATION__DESCRIPTION)) {
                        builder.addDescription(localizedString);
                    }
                } else if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, localName)) {
                    builder.setNetworkCredentials(new NetworkCredentials(
                            (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__USERNAME),
                            (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD),
                            (String)handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN)
                    ));
                }
            }
        };
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__ID, null, recordDataCollector);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__ICON, null, recordDataCollector);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__REQUESTED_POSITION, null, recordDataCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__URI, null, recordDataCollector);
        tagHandler.setXMLHandler(OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__POST_QUERY, null, recordDataCollector);

        RestXMLTagHandler.XMLStartTagHandler buttonRecordStart = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(XML_TAG__UI_CONFIGURATION__TOP_LEVEL_BUTTON_RECORD, new TopLevelButtonRecord.Builder());
            }
        };
        RestXMLTagHandler.XMLEndTagHandler buttonRecordEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                TopLevelButtonRecord.Builder builder = (TopLevelButtonRecord.Builder)handler.getTagData(XML_TAG__UI_CONFIGURATION__TOP_LEVEL_BUTTON_RECORD);
                TopLevelButtonRecord record = (builder != null) ? builder.build() : null;
                handler.setTagData(localName, null);

                if (record != null) {
                    //noinspection unchecked
                    List<TopLevelButtonRecord> list = (List<TopLevelButtonRecord>) tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__GET_TOP_LEVEL_BUTTON_RECORDS_RESULT);
                    list.add(record);
                }
            }
        };

        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__TOP_LEVEL_BUTTON_RECORD, buttonRecordStart, buttonRecordEnd);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__GET_TOP_LEVEL_BUTTON_RECORD_RESULT, buttonRecordStart, buttonRecordEnd);

        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__LOCALIZED_STRING, new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(OXPdDevice.Constants.XML_TAG__COMMON__CODE, "");
                handler.setTagData(OXPdDevice.Constants.XML_TAG__COMMON__VALUE, "");
            }
        }, recordDataCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(OXPdDevice.Constants.XML_TAG__COMMON__USERNAME, "");
                handler.setTagData(OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD, "");
                handler.setTagData(OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN, null);
            }
        }, recordDataCollector);

        RestXMLTagHandler.XMLEndTagHandler quickCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__CODE, null, quickCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__VALUE, null, quickCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__USERNAME, null, quickCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD, null, quickCollector);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN, null, quickCollector);

        List<TopLevelButtonRecord> buttonList = new ArrayList<TopLevelButtonRecord>();
        tagHandler.setTagData(XML_TAG__UI_CONFIGURATION__GET_TOP_LEVEL_BUTTON_RECORDS_RESULT, buttonList);
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdUIConfiguration.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(buttonList);
    }

    /**
     * Serialize the specified button record to XML for registration
     * @param record Button record to serialize
     * @param writer XML writer to output data into
     */
    static void writeToXML(TopLevelButtonRecord record, RestXMLWriter writer) {
        // start button record
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__REGISTER__TOP_LEVEL_BUTTON_RECORD, null);
        // write ID
        writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__ID, null, "%s", record.mButtonID.toString());
        // write title
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__TITLE, null);
        for(LocalizedString localizedString : record.mTitle) {
            LocalizedString.writeToXML(localizedString, writer);
        }
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__TITLE);
        // write description
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__DESCRIPTION, null);
        for(LocalizedString localizedString : record.mDescription) {
            LocalizedString.writeToXML(localizedString, writer);
        }
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__DESCRIPTION);
        // write position
        writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__REQUESTED_POSITION, null, "%d", record.mRequestedPosition);
        // write browser target
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__BROWSER_TARGET, null);
        if (record.mButtonTargetPostQuery != null) {
            writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__POST_QUERY, null, "%s", record.mButtonTargetPostQuery);
        }
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__WEB_APPLICATION, null);
        writer.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", record.mButtonTarget.toString());
        writer.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", record.mButtonTargetBinding.mValue);
        // add network credentials if present
        NetworkCredentials.writeToXML(record.mNetworkCredentials, writer);
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__WEB_APPLICATION);
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__BROWSER_TARGET);
        // write icon
        if (record.mIconData != null && record.mIconData.length > 0) {
            writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__ICON, null, "%s", Base64.encodeToString(record.mIconData, Base64.NO_WRAP));
        }
        // end button record
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__REGISTER__TOP_LEVEL_BUTTON_RECORD);
    }
}
