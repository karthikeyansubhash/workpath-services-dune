// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Attributes to determine behavior after submitting print job.
 * An instance of this class is created using {@link Builder}
 * <br/>
 * It can be used to configure displaying Settings UI page after submitting a job.
 * If {@link PrinterService#submit(Context, PrintAttributes, PrintletAttributes)} is called with
 * {@link PrintletAttributes} instance created by the following code, built-in Settings UI page will
 * be shown. This page lets user select each job parameter from the list of supported values.
 * <br/>
 * <script src=
 * "https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"
 * ></script>
 * <pre class="prettyprint" style="word-wrap: break-word; white-space: pre-wrap; white-space:-moz-pre-wrap; white-space:-pre-wrap; white-space:-o-pre-wrap; word-break:break-all;">
 * PrintletAttributes jobAttributes = new PrintletAttributes.Builder().setShowSettingsUi(true).build();
 * </pre>
 *
 * @since API 1
 */

@SuppressWarnings({"WeakerAccess"})
public class PrintletAttributes implements Parcelable {
    @Keep
    final int mVersion;

    @Keep
    final boolean mShowSettingsUi;

    @Keep
    final boolean mBackgroundJob;

    /**
     * @hide trivial
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide trivial
     */
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeInt(Sdk.VERSION.LEVEL);
        out.writeByte((byte) (mShowSettingsUi ? 1 : 0));
        if (mVersion >= Sdk.VERSION_LEVEL.EIGHT) {
            out.writeByte((byte) (mBackgroundJob ? 1 : 0));
        }
    }

    /**
     * @hide trivial
     */
    public static final Parcelable.Creator<PrintletAttributes> CREATOR = new Parcelable.Creator<PrintletAttributes>() {
        public PrintletAttributes createFromParcel(final Parcel in) {
            return new PrintletAttributes(in);
        }

        public PrintletAttributes[] newArray(final int size) {
            return new PrintletAttributes[size];
        }
    };

    @SuppressLint("RestrictedApi")
    private PrintletAttributes(final Parcel in) {
        // The version is used to support compatibility. It must be the first in the parcel. If a new attribute is added, then logic needs to be added
        // to the end of this constructor. The constructor will compare the version passed with the version supported and handle the compatibility. This
        // means that if the version passed is less than the version of the reader, then the reader will read all values up to the version passed.

        // Note: The order of these values is CRITICAL. When new values are needed, please add to the end.
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mShowSettingsUi = in.readByte() != 0;
        if (mVersion >= Sdk.VERSION_LEVEL.EIGHT) {
            mBackgroundJob = in.readByte() != 0;
        }else{
            mBackgroundJob = false;
        }
    }

    private PrintletAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;
        mShowSettingsUi = builder.mShowSettingsUi;
        mBackgroundJob = builder.mBackgroundJob;
    }

    /**
     * @return True, if a settings UI will be shown. Otherwise, false.
     * @hide not open API
     * Indicates whether a printer settings UI should appear prior to sending to the printer
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public boolean getShowSettingsUI() {
        return mShowSettingsUi;
    }

    /**
     * @return True, if a background job will be set. Otherwise, false.
     * @hide not open API
     * Indicates whether a printer work background mode
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public boolean getBackgroundJob() {
        return mBackgroundJob;
    }

    /**
     * @return of the xlet attributes
     * @hide not open API
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * Builder for creating an instance of {@link PrintletAttributes}.
     *
     * @since API 1
     */
    @Keep
    public static class Builder {

        boolean mShowSettingsUi = false;
        boolean mBackgroundJob = false;

        /**
         * Constructor to create a new Builder with default attributes.<br>
         * <br>
         * Show Settings UI = false <br>
         *
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public Builder() {
            // Default constructor
        }

        /**
         * Sets whether or not a printer settings UI should appear prior to sending to the device.
         *
         * @param visibility boolean  true to show the printer settings UI, false if not.
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public Builder setShowSettingsUi(final boolean visibility) {
            this.mShowSettingsUi = visibility;
            return this;
        }


        public Builder setBackgroundJob(final boolean backgroundJob){
            this.mBackgroundJob = backgroundJob;
            return this;
        }

        /**
         * Combines all of the attributes into a PrintletAttributes object.
         *
         * @return a PrintletAttributes object containing all of the attributes.
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public PrintletAttributes build() {
            return new PrintletAttributes(this);
        }
    }
}
