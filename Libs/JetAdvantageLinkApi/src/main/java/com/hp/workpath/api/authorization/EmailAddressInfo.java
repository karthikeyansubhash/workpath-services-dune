// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Container for an email address and optional display name
 *
 * @since API 9
 */
@DeviceApi
public class EmailAddressInfo implements Parcelable {
    /**
     * Recipient email address
     */
    private String mAddress;

    /**
     * Recipient name
     */
    private String mName;

    public EmailAddressInfo(@NonNull final String address, @Nullable final String name) {
        mAddress = address;
        mName = name;
    }

    private EmailAddressInfo(Parcel in) {
        mAddress = in.readString();
        mName = in.readString();
    }

    /**
     * Gets email address
     *
     * @return String email address
     * <ul>
     * <li>Return can be null if the value for mAddress is null</li>
     * <li>Return can be empty if the value for mAddress is empty</li>
     * </ul>
     * @since API 9
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * Gets email display name
     *
     * @return String display name
     * <ul>
     * <li>Return can be null if the value for mName is null</li>
     * <li>Return can be empty if the value for mName is empty</li>
     * </ul>
     * @since API 9
     */
    public String getName() {
        return mName;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAddress);
        dest.writeString(mName);
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide parcelable implementation
     */
    public static final Creator<EmailAddressInfo> CREATOR = new Creator<EmailAddressInfo>() {
        @Override
        public EmailAddressInfo createFromParcel(final Parcel in) {
            return new EmailAddressInfo(in);
        }

        @Override
        public EmailAddressInfo[] newArray(final int size) {
            return new EmailAddressInfo[size];
        }
    };

    /**
     * @hide parcelable implementation
     */
    @Override
    public String toString() {
        return "EmailAddressInfo{" +
                "mAddress='" + mAddress + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }

}
