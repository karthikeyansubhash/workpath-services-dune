// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Provides the change notification event code.
 *
 * @since API 9
 */
public enum ChangeNotificationEventCode implements Parcelable {
    /**
     * The proxy's configuration was modified.
     */
    PROXY_CONFIGURATION_CHANGED("ProxyConfigurationChanged"),

    /**
     * A permission was added.
     */
    PERMISSION_ADDED("PermissionAdded");


    private final String value;

    ChangeNotificationEventCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ChangeNotificationEventCode fromValue(String v) {
        for (ChangeNotificationEventCode c : ChangeNotificationEventCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * String representation of ChangeNotificationEventCode
     */
    @Override
    public String toString() {
        return new StringBuilder().append(value.toString()).toString();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<ChangeNotificationEventCode> CREATOR = new Parcelable.Creator<ChangeNotificationEventCode>() {
        @Override
        public ChangeNotificationEventCode createFromParcel(final Parcel in) {
            String value = in.readString();
            ChangeNotificationEventCode result = ChangeNotificationEventCode.fromValue(value);
            if (result == null) {
                throw new IllegalArgumentException("Unknown Binding value: " + value);
            }
            return result;
        }

        @Override
        public ChangeNotificationEventCode[] newArray(final int size) {
            return new ChangeNotificationEventCode[size];
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
        parcel.writeString(value);
    }

}
