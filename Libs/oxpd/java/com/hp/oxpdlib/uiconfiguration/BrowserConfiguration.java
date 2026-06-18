// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Browser configuration */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BrowserConfiguration implements Parcelable {
    /** XML tag to register button, {@link OXPdUIConfiguration#SOAP_OP__SET_BROWSER_CONFIGURATION} */
    private static final String XML_TAG__UI_CONFIGURATION__SET_BROWSER_CONFIGURATION = "browserConfiguration";
    /** XML tag containing {@link #connectionTimeout} value */
    private static final String XML_TAG__CONNECTION_TIMEOUT = "connectionTimeout";
    /** XML tag containing {@link #responseTimeout} value */
    private static final String XML_TAG__RESPONSE_TIMEOUT = "responseTimeout";
    /** XML tag containing {@link #trustedSites} value */
    private static final String XML_TAG__TRUSTED_SITES = "trustedSites";
    /** XML tag containing {@link #trustedSites} value */
    private static final String XML_TAG__TRUSTED_SITE = "trustedSite";

    /** Connection timeout */
    public final int connectionTimeout;
    /** Response timeout */
    public final int responseTimeout;
    /** Trusted sites */
    public final List<String> trustedSites;

    /**
     * Fully-initialising value constructor
     * 
     * @param connectionTimeout
     *     the int
     * @param responseTimeout
     *     the int
     * @param trustedSites
     *     the TrustedSites
     */
    private BrowserConfiguration(final int connectionTimeout, final int responseTimeout, final List<String> trustedSites) {
        this.connectionTimeout = connectionTimeout;
        this.responseTimeout = responseTimeout;
        this.trustedSites = Collections.unmodifiableList(trustedSites);
    }

    /**
     * Constructor
     * @param in Parcel to recreate object from
     */
    protected BrowserConfiguration(Parcel in) {
        connectionTimeout = in.readInt();
        responseTimeout = in.readInt();
        trustedSites = Collections.unmodifiableList(in.createStringArrayList());
    }

    /**
     * Constructor used by the library to construct BrowserConfiguration objects.
     * @param tagHandler
     *              XML handler to extract data from
     */
    @SuppressWarnings("unchecked")
    BrowserConfiguration(RestXMLTagHandler tagHandler) throws Error {
        // check for exceptions
        OXPdUIConfiguration.faultExceptionCheck(tagHandler);
        // extract values
        this.connectionTimeout = (Integer)tagHandler.getTagData(XML_TAG__CONNECTION_TIMEOUT);
        this.responseTimeout = (Integer)tagHandler.getTagData(XML_TAG__RESPONSE_TIMEOUT);
        List<String> list = (List<String>) tagHandler.getTagData(XML_TAG__TRUSTED_SITES);
        if (list == null) list = Collections.emptyList();
        this.trustedSites = Collections.unmodifiableList(list);
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(connectionTimeout);
        dest.writeInt(responseTimeout);
        dest.writeStringList(trustedSites);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel creator
     */
    public static final Creator<BrowserConfiguration> CREATOR = new Creator<BrowserConfiguration>() {
        @Override
        public BrowserConfiguration createFromParcel(Parcel in) {
            return new BrowserConfiguration(in);
        }

        @Override
        public BrowserConfiguration[] newArray(int size) {
            return new BrowserConfiguration[size];
        }
    };

    /**
     * String representation of BrowserConfiguration
     * @return String representation
     */
    @Override
    public String toString() {
        return "[" + "connectionTimeout=" + connectionTimeout + ", " + "responseTimeout=" + responseTimeout + ", " + "trustedSites=" + ((trustedSites == null) ? "null" : trustedSites.toString()) + "]";
    }

    /**
     * Browser Configuration builder
     */
    public static class Builder {
        /** Connection timeout */
        private int mConnectionTimeout;
        private int mResponseTimeout;
        private List<String> mTrustedSites = new ArrayList<String>();

        /**
         * Constructor
         */
        public Builder() {}

        /**
         * Set connection timeout
         * @param connectionTimeout timeout value in seconds
         * @return this builder
         */
        public BrowserConfiguration.Builder setConnectionTimeout(int connectionTimeout) {
            this.mConnectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Set response timeout
         * @param responseTimeout timeout value in seconds
         * @return this builder
         */
        public BrowserConfiguration.Builder setResponseTimeout(int responseTimeout) {
            this.mResponseTimeout = responseTimeout;
            return this;
        }

        /**
         * Add trusted site value
         * @param trustedSite trusted site
         * @return this builder
         */
        public BrowserConfiguration.Builder addTrustedSite(String trustedSite) {
            mTrustedSites.add(trustedSite);
            return this;
        }

        /**
         * Build the BrowserConfiguration object
         * @return BrowserConfiguration instance
         */
        public BrowserConfiguration build() {
            return new BrowserConfiguration(mConnectionTimeout, mResponseTimeout, mTrustedSites);
        }
    }

    /**
     * Builds a BrowserConfiguration instance from the provided HTTP request/response
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
    @SuppressWarnings("unchecked")
    static BrowserConfiguration parseXMLResponse(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws com.hp.oxpdlib.uiconfiguration.Error {
        RestXMLTagHandler.XMLStartTagHandler listCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(XML_TAG__TRUSTED_SITES, localName)) {
                    handler.setTagData(localName, new ArrayList<String>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(XML_TAG__TRUSTED_SITES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else if (TextUtils.equals(XML_TAG__TRUSTED_SITE, localName)) {
                                ((List<String>)handler.getTagData(XML_TAG__TRUSTED_SITES)).add(data);
                            }
                        }
                    });
                }
            }
        };
        RestXMLTagHandler.XMLEndTagHandler listCreatorEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setGenericXMLHandler(null, null);
            }
        };

        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Integer.valueOf(data));
            }
        };

        // add handlers
        tagHandler.setXMLHandler(XML_TAG__CONNECTION_TIMEOUT, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__RESPONSE_TIMEOUT, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__TRUSTED_SITES, listCreator, listCreatorEnd);

        // parse the data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        // build object if possible
        return new BrowserConfiguration(tagHandler);
    }

    /**
     * Write BrowserConfiguration instance to XML
     * @param browserConfiguration BrowserConfiguration instance
     * @param writer XML writer to to encode BrowserConfiguration instance into
     */
    static void writeToXML(BrowserConfiguration browserConfiguration, RestXMLWriter writer) {
        // start button record
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__SET_BROWSER_CONFIGURATION, null);
        // write connectionTimeout
        writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__CONNECTION_TIMEOUT, null, "%d", browserConfiguration.connectionTimeout);
        // write connectionTimeout
        writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__RESPONSE_TIMEOUT, null, "%d", browserConfiguration.responseTimeout);
        // write trustedSites
        writer.writeStartTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__TRUSTED_SITES, null);
        for(String trustedSite : browserConfiguration.trustedSites) {
            writer.writeTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__TRUSTED_SITE, null, "%s", trustedSite);
        }
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__TRUSTED_SITES);

        // end button record
        writer.writeEndTag(OXPdUIConfiguration.XML_SCHEMA__OXPD_UI_CONFIGURATION, XML_TAG__UI_CONFIGURATION__SET_BROWSER_CONFIGURATION);
    }
}
