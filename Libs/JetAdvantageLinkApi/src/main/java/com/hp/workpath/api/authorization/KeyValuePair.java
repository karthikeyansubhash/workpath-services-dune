// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Provides the key value pair
 *
 * @since API 9
 */
public class KeyValuePair implements Parcelable {
    protected String key;
    protected String value;

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private KeyValuePair(final Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public KeyValuePair(final String key, final String valueString) {
        this.key = key;
        this.value = valueString;
    }

    /**
     * Gets the value of the key property.
     *
     * @return The key
     * {@link String }
     * @since API 9
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value of the value property.
     *
     * @return The value
     * {@link String }
     * @since API 9
     */
    public String getValue() {
        return value;
    }

    /**
     * @hide
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @hide
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<KeyValuePair> CREATOR = new Parcelable.Creator<KeyValuePair>() {

        @Override
        public KeyValuePair createFromParcel(final Parcel in) {
            return new KeyValuePair(in);
        }

        @Override
        public KeyValuePair[] newArray(final int size) {
            return new KeyValuePair[size];
        }
    };

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
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(key);
        parcel.writeString(value);
    }

    @Override
    public String toString() {
        return "KeyValuePair{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
