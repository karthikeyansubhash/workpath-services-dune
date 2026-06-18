// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * The sets of attributes to determine to use a built-in dialog during job.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 1
 */
public class JobletAttributes implements Parcelable {
    @Keep
    final int mVersion;
    @Keep
    final boolean mShowUi;
    @Keep
    Bundle mExtras;

    /**
     * @hide The client should not need to know about the joblet parcelable methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the joblet parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        // The Sdk version level is used to because changes to this would constitute API level changes. Additionally, this reduces management of
        // <xyz>letAttributes versions.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        out.writeInt(Sdk.VERSION.LEVEL);
        out.writeByte((byte) (mShowUi ? 1 : 0));
        out.writeBundle(mExtras);
    }

    /**
     * @hide The client should not need to know about the joblet parcelable methods
     */
    public static final Parcelable.Creator<JobletAttributes> CREATOR = new Parcelable.Creator<JobletAttributes>() {
        public JobletAttributes createFromParcel(final Parcel in) {
            return new JobletAttributes(in);
        }

        public JobletAttributes[] newArray(final int size) {
            return new JobletAttributes[size];
        }
    };

    @SuppressLint("RestrictedApi")
    private JobletAttributes(final Parcel in) {
        // The version is used to support compatibility. It must be the first in the parcel. If a new attribute is added, then logic needs to be added
        // to the end of this constructor. The constructor will compare the version passed with the version supported and handle the compatibility. This
        // means that if the version passed is less than the version of the reader, then the reader will read all values up to the version passed.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);
        mShowUi = in.readByte() != 0;

        mExtras = in.readBundle(JobletAttributes.class.getClassLoader());
    }

    private JobletAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mShowUi = builder.mShowUi;
        mExtras = builder.mExtras;
    }

    /**
     * Indicates whether the JobProgress UI should appear as the job is being executed.
     *
     * @return boolean true if the Job Progress UI will be shown, false if not.
     * @hide only for internal user
     */
    @SuppressWarnings("unused")
    public boolean getShowUi() {
        return mShowUi;
    }

    /**
     * Returns the extra data bundle
     *
     * @return Bundle
     * @hide only for internal user
     */
    @SuppressWarnings("unused")
    public Bundle getExtras() {
        return mExtras;
    }

    /**
     * Returns the version of SDK the attributes has been created
     *
     * @return int version from {@link Sdk.VERSION#LEVEL}
     * @hide only for internal user
     */
    @SuppressWarnings("unused")
    public int getVersion() {
        return mVersion;
    }

    /**
     * Builder for constructing the JobletAttributes to configure the Joblet.
     *
     * @since API 1
     */
    @Keep
    public static class Builder {

        boolean mShowUi = false;
        Bundle mExtras = null;

        /**
         * <p>Default constructor to create a new Builder with default attributes.</br>
         * Show UI = false </br></p>
         *
         * @since API 1
         */
        public Builder() {
            // Default constructor
        }

        /**
         * <p>Sets option to use a job progress built-in UI that's provided by SDK Service when the job is being executed.</p>
         *
         * @param visibility TRUE is to show a built-in UI while progressing a job
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings("unused")
        public Builder setShowUi(final boolean visibility) {
            this.mShowUi = visibility;
            return this;
        }

        /**
         * @param extra Bundle to send extra data.
         * @return this builder for method chaining.
         * @hide only for internal user
         * <p>Sets any extra data to send to Joblet.</p>
         * @since API 1
         */
        @SuppressWarnings("unused")
        public Builder setExtras(final Bundle extra) {
            this.mExtras = extra;
            return this;
        }

        /**
         * <p>Builds JobletAttributes. All of the attributes combine into a {@link JobletAttributes} object.</p>
         *
         * @return a JobletAttributes object containing all of the attributes.
         * @since API 1
         */
        @SuppressWarnings("unused")
        public JobletAttributes build() {
            return new JobletAttributes(this);
        }
    }
}
