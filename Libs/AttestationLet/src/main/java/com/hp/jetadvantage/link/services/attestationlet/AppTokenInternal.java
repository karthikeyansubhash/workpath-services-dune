// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * <p>AppTokenInternal object to return application token such as app_token and expires_in.</p>
 *
 * @since API 3
 * @hide
 */
public final class AppTokenInternal implements Parcelable {

    /**
     * The token of link application to communicate with token proxy service or others.
     *
     * @since API 3
     */
    @SerializedName("app_token")
    private String appToken;

    /**
     * The remaining expiry time of the application token in seconds. (for example 3600 for an hour)
     *
     * @since API 3
     */
    @SerializedName("expires_in")
    private Long expiresIn;

    /**
     * For Parcelable implementation
     */
    private AppTokenInternal(final Parcel in) {
        appToken = in.readString();
        expiresIn = in.readLong();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<AppTokenInternal> CREATOR = new Creator<AppTokenInternal>() {

        @Override
        public AppTokenInternal createFromParcel(final Parcel in) {
            return new AppTokenInternal(in);
        }

        @Override
        public AppTokenInternal[] newArray(final int size) {
            return new AppTokenInternal[size];
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
     * @hide
     * @return string representation
     */
    public String toString() {
        return "app token: " + appToken +
                ", expires in: " + expiresIn;
    }
}
