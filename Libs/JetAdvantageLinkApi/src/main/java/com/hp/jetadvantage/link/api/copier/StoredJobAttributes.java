// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * Stored job attributes for requesting a stored job from the printer.
 * An instance of this class is created using {@link StoredJobBuilder}.
 *
 * @since API 3
 */
public class StoredJobAttributes implements Parcelable {
    @Keep
    private final int mVersion;

    @Keep
    private final int mCopies;

    @Keep
    private final String mStoredJobId;

    @Keep
    private final JobCredentialsAttributes mJobCredentialsAttributes;

    private StoredJobAttributes(final StoredJobBuilder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mCopies = builder.mCopies;
        mStoredJobId = builder.mStoredJobId;
        mJobCredentialsAttributes = builder.mJobCredentialsAttributes;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public int getVersion() {
        return mVersion;
    }

    /**
     * @hide for internal use
     */
    public int getCopies() {
        return mCopies;
    }

    /**
     * @hide for internal use
     */
    public String getStoredJobId() {
        return mStoredJobId;
    }

    /**
     * @hide for internal use
     */
    public JobCredentialsAttributes getJobCredentialsAttributes() {
        return mJobCredentialsAttributes;
    }

    /**
     * Builder for creating {@link StoredJobAttributes} containing stored job attributes.
     *
     * @since API 3
     */
    @Keep
    public static class StoredJobBuilder {
        int mCopies = 1;
        String mStoredJobId;
        JobCredentialsAttributes mJobCredentialsAttributes;

        /**
         * Construct a new StoredJobBuilder for releasing a stored job.<br>
         * @param storedJobId ID of stored copy job
         *
         * @since API 3
         */
        @SuppressLint("RestrictedApi")
        public StoredJobBuilder(final String storedJobId) {
            this.mStoredJobId = Preconditions.checkNotNull(storedJobId);
        }

        /**
         * Sets number of copies.
         *
         * @param copies number of copies to print
         * @return this builder for method chaining.
         *
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public StoredJobBuilder setCopies(final int copies) {
            mCopies = copies;
            return this;
        }

        /**
         * Set stored job credentials attributes to access job
         *
         * @param jobCredentialsAttributes job credentials
         * @return this builder for method chaining.
         * @throws NullPointerException if jobCredentialsAttributes is null
         *
         * @since API 3
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public StoredJobBuilder setJobCredentials(@NonNull final JobCredentialsAttributes jobCredentialsAttributes) {
            mJobCredentialsAttributes = Preconditions.checkNotNull(jobCredentialsAttributes);
            return this;
        }

        /**
         * Combine all of the attributes in this into a {@link StoredJobAttributes} object.
         *
         * @return a StoredJobAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 3
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public StoredJobAttributes build(@NonNull final CopyAttributesCaps caps) throws CapabilitiesExceededException {
            if (caps == null) {
                throw new CapabilitiesExceededException("CopyAttributesCapabilities is required");
            }
            Preconditions.checkNotNull(caps.mCapsCreator);

            if (!caps.getCopiesRange().validate(mCopies)) {
                throw new CapabilitiesExceededException("Supplied copies value is not in range " + caps.getCopiesRange());
            }

            if (mJobCredentialsAttributes != null) {
                if (!caps.getPasswordTypeList().contains(mJobCredentialsAttributes.mStoreJobPasswordType)) {
                    throw new CapabilitiesExceededException("Supplied stored job password type not supported");
                }
            }

            return new StoredJobAttributes(this);
        }
    }

    private StoredJobAttributes(Parcel in) {
        mVersion = in.readInt();
        mCopies = in.readInt();
        mStoredJobId = in.readString();
        mJobCredentialsAttributes = in.readParcelable(JobCredentialsAttributes.class.getClassLoader());
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeInt(mCopies);
        dest.writeString(mStoredJobId);
        dest.writeParcelable(mJobCredentialsAttributes, 0);
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
    public static final Creator<StoredJobAttributes> CREATOR = new Creator<StoredJobAttributes>() {
        @Override
        public StoredJobAttributes createFromParcel(Parcel in) {
            return new StoredJobAttributes(in);
        }

        @Override
        public StoredJobAttributes[] newArray(int size) {
            return new StoredJobAttributes[size];
        }
    };
}
