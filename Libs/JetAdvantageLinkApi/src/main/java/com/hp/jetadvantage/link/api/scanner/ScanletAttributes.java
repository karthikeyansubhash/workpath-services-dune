// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Attributes to determine behavior after submitting scan job.
 * An instance of this class is created using {@link Builder}
 * <br/>
 * It can be used to configure displaying Settings UI page after submitting a job.
 * If {@link ScannerService#submit(Context, ScanAttributes, ScanletAttributes)} is called with
 * {@link ScanletAttributes} instance created by the following code, built-in Settings UI page will
 * be shown. This page lets user select each job parameter from the list of supported values.
 * <br/>
 * <script src=
 * "https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"
 * ></script>
 * <pre class="prettyprint" style="word-wrap: break-word; white-space: pre-wrap; white-space:-moz-pre-wrap; white-space:-pre-wrap; white-space:-o-pre-wrap; word-break:break-all;">
 * ScanletAttributes jobAttributes = new ScanletAttributes.Builder().setShowSettingsUi(true).build();
 * </pre>
 *
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess"})
public class ScanletAttributes implements Parcelable {
    @Keep
    final int mVersion;
    @Keep
    final boolean mShowSettingsUi;
    @Keep
    final boolean mShowCredentialsUi;
    @Keep
    final boolean mAllowMultipleScan;

    /**
     * @hide The client should not need to know about the scanlet parcelable methods.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the scanlet parcelable methods.
     */
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        // The Sdk version level is used to because changes to this would constitute API level changes. Additionally, this reduces management of
        // <xyz>letAttributes versions.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        out.writeInt(Sdk.VERSION.LEVEL);
        out.writeByte((byte) (mShowSettingsUi ? 1 : 0));
        out.writeByte((byte) (mShowCredentialsUi ? 1 : 0));
        out.writeByte((byte) (mAllowMultipleScan ? 1 : 0));
    }

    /**
     * @hide The client should not need to know about the scanlet parcelable methods
     */
    public static final Parcelable.Creator<ScanletAttributes> CREATOR = new Parcelable.Creator<ScanletAttributes>() {
        public ScanletAttributes createFromParcel(final Parcel in) {
            return new ScanletAttributes(in);
        }

        public ScanletAttributes[] newArray(final int size) {
            return new ScanletAttributes[size];
        }
    };

    @SuppressLint("RestrictedApi")
    private ScanletAttributes(final Parcel in) {
        // The version is used to support compatibility. It must be the first in the parcel. If a new attribute is added, then logic needs to be added
        // to the end of this constructor. The constructor will compare the version passed with the version supported and handle the compatibility. This
        // means that if the version passed is less than the version of the reader, then the reader will read all values up to the version passed.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mShowSettingsUi = in.readByte() != 0;
        mShowCredentialsUi = in.readByte() != 0;
        mAllowMultipleScan = in.readByte() != 0;
    }

    private ScanletAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mShowSettingsUi = builder.mShowSettingsUi;
        mShowCredentialsUi = builder.mShowCredentialsUi;
        mAllowMultipleScan = builder.mAllowMultipleScan;
    }

    /**
     * @return True, if a settings UI will be shown. Otherwise, false.
     * @hide not open API
     * Indicates whether a scanner settings UI should appear prior to sending to the printer.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public boolean getShowSettingsUI() {
        return mShowSettingsUi;
    }


    /**
     * @return True, if a credentials UI will be shown if authentication failed. Otherwise, job will fail immediately.
     * @hide not open API
     * Indicates whether a scanner credentials UI should appear in case of authentication error.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public boolean getShowCredentialsUI() {
        return mShowCredentialsUi;
    }

    /**
     * @return True, allow multiple scan, false if not.
     * @hide not open API
     * Indicates whether multiple scan should possible.
     * @since API 9
     */
    @SuppressWarnings({"unused"})
    @Keep
    public boolean getAllowMultipleScan() {
        return mAllowMultipleScan;
    }

    /**
     * @return of the Xlet attributes
     * @hide not open API
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * Builder for creating an instance of {@link ScanletAttributes}.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public static class Builder {
        boolean mShowSettingsUi = false;
        boolean mShowCredentialsUi = true;
        boolean mAllowMultipleScan = false;

        /**
         * Construct a new Builder with default attributes.<br>
         * <br>
         * Show Settings UI = false <br>
         * <br>
         *
         * @since API 1
         */
        public Builder() {
            // Default constructor
        }

        /**
         * Sets whether a scanner settings UI should appear prior to sending to the printer.
         *
         * @param visibility boolean true to show the scanner settings UI, false if not.
         * @return this builder for method chaining
         * @since API 1
         */
        public Builder setShowSettingsUi(final boolean visibility) {
            this.mShowSettingsUi = visibility;
            return this;
        }

        /**
         * Sets whether a credentials UI should appear in case of authentication error (default is true).
         *
         * @param visibility boolean true to show credentials UI, false if not.
         * @return this builder for method chaining
         * @since API 1
         */
        public Builder setShowCredentialsUi(final boolean visibility) {
            this.mShowCredentialsUi = visibility;
            return this;
        }

        /**
         * Sets whether to allow multiple scan (default is false).
         *
         * @param allowMultipleScan boolean true to allow multiple scan , false if not.
         * @return this builder for method chaining
         * @since API 9
         */
        public Builder setAllowMultipleScan(final boolean allowMultipleScan) {
            this.mAllowMultipleScan = allowMultipleScan;
            return this;
        }


        /**
         * Combine all of the attributes into a ScanletAttributes object.
         *
         * @return ScanletAttributes object containing all the attributes.
         * @since API 1
         */
        public ScanletAttributes build() {
            return new ScanletAttributes(this);
        }
    }
}
