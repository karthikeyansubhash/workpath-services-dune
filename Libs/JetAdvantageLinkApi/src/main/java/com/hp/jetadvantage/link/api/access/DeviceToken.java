// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.google.gson.annotations.SerializedName;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * <p>DeviceToken object to return certain token from a device.</p>
 *
 * @since API 3
 */
@DeviceApi
public final class DeviceToken implements Parcelable {

    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private final int mVersion;

    /**
     * The token from device to communicate with SIO services or others.
     *
     * @since API 3
     */
    @SerializedName("code")
    private String deviceToken;

    /**
     * For Parcelable implementation
     */
    @SuppressLint("RestrictedApi")
    private DeviceToken(final Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        deviceToken = in.readString();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<DeviceToken> CREATOR = new Creator<DeviceToken>() {

        @Override
        public DeviceToken createFromParcel(final Parcel in) {
            return new DeviceToken(in);
        }

        @Override
        public DeviceToken[] newArray(final int size) {
            return new DeviceToken[size];
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
        parcel.writeString(deviceToken);
    }

    /**
     * <p>Returns device token</p>
     *
     * @return String device token.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public String getDeviceToken() {
        return deviceToken;
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
        return "device token";
    }
}
