// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * Job credentials attributes for accessing a stored job from the printer.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 3
 */
public class JobCredentialsAttributes implements Parcelable {
    /**
     * Password type for stored copy job
     *
     * @since API 3
     */
    public enum PasswordType {
        /**
         * No password for stored copy job
         *
         * @since API 3
         */
        NONE,

        /**
         * Pin (numeric only)
         *
         * @since API 3
         */
        NUMERIC,

        /**
         * Password (alphanumeric)
         *
         * @since API 3
         */
        ALPHA_NUMERIC
    }

    @Keep
    final int mVersion;

    @Keep
    final PasswordType mStoreJobPasswordType;

    @Keep
    final String mStoreJobPassword;

    private JobCredentialsAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mStoreJobPasswordType = builder.mStoreJobPasswordType;
        mStoreJobPassword = builder.mStoreJobPassword;
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
    public PasswordType getStoreJobPasswordType() {
        return mStoreJobPasswordType;
    }

    /**
     * @hide for internal use
     */
    public String getStoreJobPassword() {
        return mStoreJobPassword;
    }

    @SuppressLint("RestrictedApi")
    private JobCredentialsAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mStoreJobPasswordType = (PasswordType) in.readSerializable();
        mStoreJobPassword = in.readString();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeSerializable(mStoreJobPasswordType);
        dest.writeString(mStoreJobPassword);
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
    public static final Creator<JobCredentialsAttributes> CREATOR = new Creator<JobCredentialsAttributes>() {
        @Override
        public JobCredentialsAttributes createFromParcel(Parcel in) {
            return new JobCredentialsAttributes(in);
        }

        @Override
        public JobCredentialsAttributes[] newArray(int size) {
            return new JobCredentialsAttributes[size];
        }
    };

    /**
     * Builder for creating {@link StoredJobAttributes} containing stored job attributes.
     *
     * @since API 3
     */
    @Keep
    public static class Builder {
        PasswordType mStoreJobPasswordType = PasswordType.NONE;
        String mStoreJobPassword;

        /**
         * Default constructor to create a new Builder with default attributes.
         *
         * @since API 3
         */
        public Builder() {
        }

        /**
         * Set stored job password type
         *
         * @param passwordType password type
         * @return this builder for method chaining.
         * @throws NullPointerException if jobCredentialsAttributes is null
         * @since API 3
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setPasswordType(@NonNull final PasswordType passwordType) {
            mStoreJobPasswordType = Preconditions.checkNotNull(passwordType);
            return this;
        }

        /**
         * Set stored job password
         *
         * @param password password
         * @return this builder for method chaining.
         * @throws NullPointerException if jobCredentialsAttributes is null
         * @since API 3
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setPassword(@NonNull final String password) {
            mStoreJobPassword = Preconditions.checkNotNull(password);
            return this;
        }

        /**
         * Combine all of the attributes in this into a {@link JobCredentialsAttributes} object.
         *
         * @return a JobCredentialsAttributes object containing all of the attributes.
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public JobCredentialsAttributes build() throws CapabilitiesExceededException {
            if (mStoreJobPasswordType != PasswordType.NONE && TextUtils.isEmpty(mStoreJobPassword)) {
                throw new CapabilitiesExceededException("For password type " + mStoreJobPasswordType + " a password must be provided");
            }

            if (mStoreJobPasswordType == PasswordType.NUMERIC && !TextUtils.isDigitsOnly(mStoreJobPassword)) {
                throw new CapabilitiesExceededException("For password type " + mStoreJobPasswordType + " only digits are allowed in a password");
            }

            return new JobCredentialsAttributes(this);
        }
    }
}
