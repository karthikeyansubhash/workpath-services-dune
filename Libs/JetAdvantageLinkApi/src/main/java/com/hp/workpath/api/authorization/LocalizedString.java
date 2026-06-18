// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Provides the localized string
 *
 * @since API 9
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LocalizedString implements Parcelable {
    /**
     * String locale
     */
    public final Locale mLocale;
    /**
     * Localized String value
     */
    public final String mValue;

    /**
     * @hide Locale code delimiter
     */
    private static final String DELIMITER = "-";

    @SuppressLint("RestrictedApi")
    public LocalizedString(Locale locale, String value) {
        mLocale = locale;
        mValue = value;
    }

    public LocalizedString(String locale, String value) {
        String splits[] = locale.split(DELIMITER);
        mLocale = ((splits.length > 1) ? new Locale(splits[0], splits[1]) : new Locale(splits[0]));
        mValue = value;
    }

    /**
     * Gets the value of the locale property.
     *
     * @return The locale
     * {@link String }
     * @since API 9
     */
    public Locale getLocale() {
        return mLocale;
    }

    /**
     * Gets the value of the value property.
     *
     * @return The value
     * {@link String }
     * @since API 9
     */
    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "LocalizedString{" +
                "mLocale=" + mLocale +
                ", mValue='" + mValue + '\'' +
                '}';
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localeCodeValue());
        dest.writeString(mValue);
    }

    /**
     * @hide for internal use
     */
    private String localeCodeValue() {
        String language = mLocale.getLanguage();
        String country = mLocale.getCountry();

        if (!TextUtils.isEmpty(country)) {
            return language + DELIMITER + country;
        } else {
            return language;
        }
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    private LocalizedString(Parcel in) {
        this(in.readString(), in.readString());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<LocalizedString> CREATOR = new Creator<LocalizedString>() {

        /**
         * @hide The client should not need to know about the parcelable methods
         */
        @Override
        public LocalizedString createFromParcel(Parcel in) {
            return new LocalizedString(in);
        }

        /**
         * @hide The client should not need to know about the parcelable methods
         */
        @Override
        public LocalizedString[] newArray(int size) {
            return new LocalizedString[size];
        }
    };
}
