// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Container for UI context information
 */
@SuppressWarnings("WeakerAccess")
public class UIContext implements Parcelable{

    /** XML Tag containing {@link #mUIContext ui context nonce} value */
    private static final String XML_TAG__UI_CONFIGURATION__RESERVE_UI_CONTEXT_RESULT = "ReserveUIContextResult";
    /** XML Tag containing UI inactivity timeout value */
    private static final String XML_TAG__UI_CONFIGURATION__UI_INACTIVITY_TIMEOUT_PERIOD = "uiInactivityTimeoutPeriod";
    /** XML Tag containing Printer public encryption key */
    private static final String XML_TAG__UI_CONFIGURATION__PUBLIC_KEY = "publicKey";
    /** XML Tag containing Shared encryption key type */
    private static final String XML_TAG__UI_CONFIGURATION__ALGORITHM = "algorithm";
    /** Dummy tag for storing Shared encryption key */
    private static final String SHARED_KEY_ENCRYPTION = "#sharedkey#";
    /** Default encryption mode */
    private static final String DEFAULT_SHARED_ENCRYPTION_ALGORITHM = "AES/CBC/NoPadding";

    /** Minimum UI inactivity timeout */
    static final int MIN_UI_INACTIVITY_TIMEOUT = 10;

    /** UI Context nonce value */
    private final String mUIContext;

    /** UI Inactivity timeout */
    final int mInactivityTimeout;
    /** Shared encryption key */
    final SecretKeySpec mSecretKey;
    /** Shared encryption algorithm */
    final String mAlgorithm;
    /** UI Context nonce was preconfigured for us? */
    final boolean mPreconfiguredContext;

    /** Invalid UI Context reference */
    public static UIContext INVALID_CONTEXT = new UIContext();

    /**
     * Get the UI Context key
     * @return
     *              UIContext
     * @hide
     */
    public String getUIContext() {
        return mUIContext;
    }

    /**
     * Get the Diffie-Hellman generated key
     * @return
     *              Diffie-Hellman key
     * @hide
     */
    public SecretKeySpec getSharedSecret() {
        return mSecretKey;
    }

    /**
     * Get the algorithm used by the shared key
     * @return
     *              Shared key algorithm
     * @hide
     */
    public String getSharedSecretAlgorithm() {
        return mAlgorithm;
    }

    /**
     * Builds a UIContext instance from the provided HTTP request/response
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              When errors are detected
     */
    private UIContext(RestXMLTagHandler tagHandler) throws Error {
        try {
            OXPdUIConfiguration.faultExceptionCheck(tagHandler);
        } catch(Error exception) {
            exception = AuthenticationException.convert(exception);
            exception = AuthorizationException.convert(exception);
            throw (exception);
        }
        mUIContext = (String)tagHandler.getTagData(OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID);
        mInactivityTimeout = (Integer)tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__UI_INACTIVITY_TIMEOUT_PERIOD, 0);
        mSecretKey = (SecretKeySpec)tagHandler.getTagData(SHARED_KEY_ENCRYPTION, null);
        mAlgorithm = (String)tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__ALGORITHM, DEFAULT_SHARED_ENCRYPTION_ALGORITHM);
        mPreconfiguredContext = false;
        if (TextUtils.isEmpty(mUIContext)) throw new Error(ErrorName.Unknown, "invalid nonce value");
    }

    /**
     * Builds an invalid UIContext instance
     */
    private UIContext() {
        this("");
    }

    /**
     * Build a UIContext instance
     * @param context
     *              UI context value
     */
    public UIContext(String context) {
        mUIContext = context;
        mSecretKey = null;
        mInactivityTimeout = 0;
        mAlgorithm = DEFAULT_SHARED_ENCRYPTION_ALGORITHM;
        mPreconfiguredContext = true;
    }

    /**
     * Builds a CustomerInfo instance from the provided HTTP request/response
     * @param keyPair
     *              Diffie-Hellman key to finalize key agreement
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
    static UIContext parseRequestResult(KeyPair keyPair, OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        // define handlers
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName,data);
            }
        };
        RestXMLTagHandler.XMLEndTagHandler uiContextIDCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID, data);
            }
        };
        RestXMLTagHandler.XMLEndTagHandler timeoutCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Integer value;
                try {
                    value = Integer.valueOf(data);
                } catch(NumberFormatException ignored) {
                    value = 0;
                }
                handler.setTagData(localName, value);
            }
        };

        // add handlers
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__RESERVE_UI_CONTEXT_RESULT, null, uiContextIDCollector);
        tagHandler.setXMLHandler(OXPdUIConfiguration.XML_TAG__UI_CONFIGURATION__UI_CONTEXT_ID, null, uiContextIDCollector);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__ALGORITHM, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__PUBLIC_KEY, null, infoCollector);
        tagHandler.setXMLHandler(XML_TAG__UI_CONFIGURATION__UI_INACTIVITY_TIMEOUT_PERIOD, null, timeoutCollector);

        // parse data
        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        // get shared key value
        String publicKey = (String)tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__PUBLIC_KEY);
        // did the response contain a shared key?
        if ((publicKey != null) && (keyPair != null)) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(OXPdUIConfiguration.KEY_ALGORITHM);
                KeyAgreement keyAgreement = KeyAgreement.getInstance(OXPdUIConfiguration.KEY_ALGORITHM);
                keyAgreement.init(keyPair.getPrivate());
                keyAgreement.doPhase(keyFactory.generatePublic(
                        new DHPublicKeySpec(
                                new BigInteger(1, Base64.decode(publicKey, Base64.DEFAULT)),
                                OXPdUIConfiguration.mDHParams.getP(),
                                OXPdUIConfiguration.mDHParams.getG())),
                        true);
                tagHandler.setTagData(SHARED_KEY_ENCRYPTION,
                    new SecretKeySpec(MessageDigest.getInstance(MGF1ParameterSpec.SHA256.getDigestAlgorithm()).digest(keyAgreement.generateSecret()),
                            (String)tagHandler.getTagData(XML_TAG__UI_CONFIGURATION__ALGORITHM, DEFAULT_SHARED_ENCRYPTION_ALGORITHM)));
            } catch(Exception ignored) {

            }
        }

        // build object if possible
        return new UIContext(tagHandler);
    }

    /**
     * Load preconfigured UIContext
     * @param context
     *              Context using OXPdDevice
     * @return
     *              UIContext from preconfigured value if exists, false otherwise
     */
    static UIContext loadPreconfigured(Context context) {
        String value = null;
        if (OXPdDevice.isPanel()) {
            // on front panel, get UI Context nonce
            value = Settings.Secure.getString(context.getContentResolver(), "hp_ui_context");
        }
        return (!TextUtils.isEmpty(value) ? new UIContext(value) : null);
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
        dest.writeString(this.mUIContext);
        dest.writeInt(this.mInactivityTimeout);
        dest.writeString(mAlgorithm);
        dest.writeInt((mSecretKey != null) ? 1 : 0);
        if (mSecretKey != null) {
            dest.writeByteArray(mSecretKey.getEncoded());
            dest.writeString(mSecretKey.getAlgorithm());
        }
        dest.writeInt(mPreconfiguredContext ? 1 : 0);
    }

    /**
     * Returns a hash code value for the object
     * @return Integer hash value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mUIContext.hashCode();
        result = prime * result + mInactivityTimeout;
        result = prime * result + mAlgorithm.hashCode();
        result = prime * result + ((mSecretKey != null) ? mSecretKey.hashCode() : 0);
        result = prime * result + Boolean.valueOf(mPreconfiguredContext).hashCode();
        return result;
    }

    /**
     * Indicates whether some other object is "equal" to this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UIContext)) return false;
        UIContext other = (UIContext)obj;
        return TextUtils.equals(mUIContext, other.mUIContext)
                && (mInactivityTimeout == other.mInactivityTimeout)
                && TextUtils.equals(mAlgorithm, other.mAlgorithm)
                && ((mSecretKey == other.mSecretKey) || (mSecretKey != null && mSecretKey.equals(other.mSecretKey)))
                && (mPreconfiguredContext == other.mPreconfiguredContext);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private UIContext(Parcel in) {
        mUIContext = in.readString();
        mInactivityTimeout = in.readInt();
        mAlgorithm = in.readString();
        mSecretKey = (in.readInt() > 0) ? new SecretKeySpec(in.createByteArray(), in.readString()) : null;
        mPreconfiguredContext = (in.readInt() > 0);
    }

    /**
     * UIContext creator
     */
    public static final Parcelable.Creator<UIContext> CREATOR =
            new Parcelable.Creator<UIContext>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link UIContext#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public UIContext createFromParcel(Parcel in) {
                    return new UIContext(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public UIContext[] newArray(int size) {
                    return new UIContext[size];
                }
            };
}
