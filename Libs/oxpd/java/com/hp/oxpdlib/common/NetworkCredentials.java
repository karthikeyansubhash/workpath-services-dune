// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.OXPdDevice.Constants;
import com.hp.oxpdlib.uiconfiguration.UIContext;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler.XMLStartTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;
import org.xml.sax.Attributes;

/**
 * Network Credentials that should be provided
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NetworkCredentials implements Parcelable {
    /** User name */
    public final String mUserName;
    /** Password */
    public final String mPassword;
    /** Username domain */
    public final String mDomain;

    /**
     * Constructor
     * @param userName User name
     * @param password Password
     */
    public NetworkCredentials(String userName, String password) {
        this(userName, password, null);
    }

    /**
     * Constructor
     * @param userName User name
     * @param password Password
     * @param domain User name domain
     */
    public NetworkCredentials(String userName, String password, String domain) {
        mUserName = userName;
        mPassword = password;
        mDomain = domain;
    }

    public NetworkCredentials(RestXMLTagHandler tagHandler) throws Error {
        mUserName = (String) tagHandler.getTagData(Constants.XML_TAG__COMMON__USERNAME);
        mPassword = (String) tagHandler.getTagData(Constants.XML_TAG__COMMON__PASSWORD);
        mDomain = (String) tagHandler.getTagData(Constants.XML_TAG__COMMON__DOMAIN);
    }

    public static void setupXMLTagHandler(RestXMLTagHandler tagHandler) {
        XMLStartTagHandler networkCredentialsStartHandler = new XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName,
                    Attributes attributes) {
                handler.setTagData(Constants.XML_TAG__COMMON__USERNAME, "");
                handler.setTagData(Constants.XML_TAG__COMMON__PASSWORD, "");
                handler.setTagData(Constants.XML_TAG__COMMON__DOMAIN, null);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler networkCredentialsEndHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, localName)) {
                    handler.setTagData(localName, new NetworkCredentials(
                            (String) handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__USERNAME),
                            (String) handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD),
                            (String) handler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN)
                    ));
                }
            }
        };

        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, networkCredentialsStartHandler, networkCredentialsEndHandler);

        RestXMLTagHandler.XMLEndTagHandler stringFieldsHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__USERNAME, null, stringFieldsHandler);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD, null, stringFieldsHandler);
        tagHandler.setXMLHandler(OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN, null, stringFieldsHandler);
    }

    /**
     * Returns the hash code value for the network credentials
     * @return Network credentials hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + ((mUserName != null) ? mUserName.hashCode() : 0);
        result = result * prime + ((mPassword != null) ? mPassword.hashCode() : 0);
        result = result * prime + ((mDomain != null) ? mDomain.hashCode() : 0);
        return result;
    }

    /**
     * Compares the specified object with this one for equality. Returns true if only if the specified object
     * is also {@link NetworkCredentials} with same username/password/domain values
     * @param obj Object to be compared  for equality
     * @return True if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkCredentials)) return false;
        NetworkCredentials other = (NetworkCredentials)obj;
        return (TextUtils.equals(mUserName, other.mUserName)
                && TextUtils.equals(mPassword, other.mPassword)
                && TextUtils.equals(mDomain, other.mDomain));
    }

    /**
     * Serialize the network credentials to XML
     * @param networkCredentials Network credentials to serialize
     * @param xmlWriter XML writer to output data to
     */
    public static void writeToXML(NetworkCredentials networkCredentials, RestXMLWriter xmlWriter) {
        if (networkCredentials != null) {
            xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS, null);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__USERNAME, null, "%s", networkCredentials.mUserName);
            xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__PASSWORD, null, "%s", networkCredentials.mPassword);
            if (!TextUtils.isEmpty(networkCredentials.mDomain)) {
                xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__DOMAIN, null, "%s", networkCredentials.mDomain);
            }
            xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS);

        }
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
        dest.writeString(mUserName);
        dest.writeString(mPassword);
        dest.writeString(mDomain);
    }

    /**
     * Constructor
     * @param in
     *           Parcel from which the object should be reconstructed
     */
    private NetworkCredentials(Parcel in) {
        this(in.readString(), in.readString(), in.readString());
    }

    /**
     * NetworkCredentials creator
     */
    public static final Parcelable.Creator<NetworkCredentials> CREATOR =
            new Parcelable.Creator<NetworkCredentials>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link UIContext#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public NetworkCredentials createFromParcel(Parcel in) {
                    return new NetworkCredentials(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public NetworkCredentials[] newArray(int size) {
                    return new NetworkCredentials[size];
                }
            };
}
