// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * <p>Holder for the user's preferences</p>
 *
 * @since API 2
 */
public class UserPreferencesAttributes implements Parcelable {
    private final int mVersion;
    private final String mAutoLaunchAppAccessPointId;
    private final String mLanguageCode;

    private UserPreferencesAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mAutoLaunchAppAccessPointId = builder.mAutoLaunchAppAccessPointId;
        mLanguageCode = builder.mLanguageCode;
    }

    /**
     * @hide for internal use
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * @hide for internal use
     */
    public String getAutoLaunchAppAccessPointId() {
        return mAutoLaunchAppAccessPointId;
    }

    /**
     * @hide for internal use
     */
    public String getLanguageCode() {
        return mLanguageCode;
    }

    @SuppressLint("RestrictedApi")
    private UserPreferencesAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mAutoLaunchAppAccessPointId = in.readString();
        mLanguageCode = in.readString();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Sdk.VERSION.LEVEL);
        dest.writeString(mAutoLaunchAppAccessPointId);
        dest.writeString(mLanguageCode);
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
    public static final Parcelable.Creator<UserPreferencesAttributes> CREATOR = new Parcelable.Creator<UserPreferencesAttributes>() {
        @Override
        public UserPreferencesAttributes createFromParcel(final Parcel in) {
            return new UserPreferencesAttributes(in);
        }

        @Override
        public UserPreferencesAttributes[] newArray(final int size) {
            return new UserPreferencesAttributes[size];
        }
    };

    /**
     * Builder for building user's preferences.
     *
     * @since API 2
     */
    @DeviceApi
    public static final class Builder {
        String mAutoLaunchAppAccessPointId;
        String mLanguageCode;

        /**
         * Default constructor a new Builder with default attributes.
         *
         * @since API 2
         */
        public Builder() {
        }

        /**
         * <p>Sets the UUID of an application access point which may be used by the device to automatically
         * launch the application access point immediately after signing in.
         * Ignored if the sign in process is not initiated by pressing the "Sign in" button on the device's home
         * screen. Also ignored if the application access point is not present, is hidden, or is disabled on the device.
         * For comparison purposes, devices will handle UUIDs as case-sensitive strings, meaning aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa != AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAA.</p>
         *
         * @param autoLaunchAppAccessPointId The application ID
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setAutoLaunchAppAccessPointId(@Nullable final String autoLaunchAppAccessPointId) {
            mAutoLaunchAppAccessPointId = autoLaunchAppAccessPointId;
            return this;
        }

        /**
         * Sets the user's preferred language. If it's null, language on a device will not change.
         *
         * @param languageCode The valid language code
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setLanguageCode(@Nullable final String languageCode) {
            mLanguageCode = languageCode;
            return this;
        }

        /**
         * Combines all of the attributes in this into a {@link UserPreferencesAttributes UserPreferencesAttributes} object.
         *
         * @return UserPreferencesAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserPreferencesAttributes build() throws CapabilitiesExceededException {
            return new UserPreferencesAttributes(this);
        }
    }
}
