// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.helper.email;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * <p>Provides the email information with address. </p>
 *
 * @since API 1
 */
@DeviceApi
public class EmailAddressInfo implements Parcelable {
    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private int mVersion;

    /**
     * Recipient email address
     */
    private String mAddress;

    /**
     * Recipient name
     */
    private String mName;

    private EmailAddressInfo() {
        mVersion = Sdk.VERSION.LEVEL;
    }

    EmailAddressInfo(@NonNull final String address, @Nullable final String name) {
        this();
        mAddress = address;
        mName = name;
    }

    private EmailAddressInfo(Parcel in) {
        mVersion = in.readInt();
        mAddress = in.readString();
        mName = in.readString();
    }

    /**
     * <p>Gets email recipient address</p>
     * @return String recipient address
     * @since API 1
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * <p>Gets recipient name</p>
     * @return String recipient name
     * @since API 1
     */
    public String getName() {
        return mName;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
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
        public EmailAddressInfo createFromParcel(Parcel in) {
            return new EmailAddressInfo(in);
        }

        @Override
        public EmailAddressInfo[] newArray(int size) {
            return new EmailAddressInfo[size];
        }
    };

}
