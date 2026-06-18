// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.attestation;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;


/**
 * <p>AppToken object to return application token such as app_token and expires_in.</p>
 *
 * @since API 3
 */
@DeviceApi
public final class AppToken implements Parcelable {

    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private final int mVersion;

    /**
     * The token of link application to communicate with token proxy service or others.
     *
     * @since API 3
     */
    private String appToken;

    /**
     * The remaining expiry time of the application token in seconds. (for example 3600 for an hour)
     *
     * @since API 3
     */
    private Long expiresIn;

    /**
     * For Parcelable implementation
     */
    @SuppressLint("RestrictedApi")
    private AppToken(final Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        appToken = in.readString();
        expiresIn = in.readLong();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<AppToken> CREATOR = new Creator<AppToken>() {

        @Override
        public AppToken createFromParcel(final Parcel in) {
            return new AppToken(in);
        }

        @Override
        public AppToken[] newArray(final int size) {
            return new AppToken[size];
        }
    };

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeInt(Sdk.VERSION.LEVEL);
        parcel.writeString(appToken);
        parcel.writeLong(expiresIn);
    }

    /**
     * <p>Returns application token</p>
     *
     * @return String application token.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public String getAppToken() {
        return appToken;
    }

    /**
     * <p>Returns expires time</p>
     *
     * @return Int expiry time of token in seconds
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Internal parcelable version
     *
     * @return internal version
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * @hide
     * @return string representation
     */
    public String toString() {
        return "app token expires in: " + expiresIn;
    }
}
