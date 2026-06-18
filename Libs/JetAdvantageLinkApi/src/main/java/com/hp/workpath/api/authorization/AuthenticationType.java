// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The AuthenticationType enum represents various types of authentication methods supported by the system.
 * Each authentication type includes a description of the method and the required user information.
 *
 * @since API 9
 */
public enum AuthenticationType implements Parcelable {
    /**
     * Windows type
     */
    WINDOWS("Windows"),

    /**
     * Windows Smart Card type
     */
    WINDOWS_SMART_CARD("WindowsSmartCard"),

    /**
     * Novell Directory Service type
     */
    NOVELL("Novell"),

    /**
     * LDAP type
     */
    LDAP("LDAP"),

    /**
     * Pin type
     */
    PIN("PIN"),

    /**
     * Other type
     */
    OTHER("Other");

    private final String value;

    AuthenticationType(String v) {
        this.value = v;
    }

    public static AuthenticationType fromValue(String v) {
        for (AuthenticationType c : AuthenticationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
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
        dest.writeString(value);
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<AuthenticationType> CREATOR = new Parcelable.Creator<AuthenticationType>() {
        @Override
        public AuthenticationType createFromParcel(Parcel source) {
            String value = source.readString();
            AuthenticationType type = AuthenticationType.fromValue(value);
            if (type == null) {
                throw new IllegalArgumentException("Unknown type value: " + value);
            }
            return type;
        }

        @Override
        public AuthenticationType[] newArray(int size) {
            return new AuthenticationType[size];
        }
    };

    /**
     * String representation of AuthenticationType
     */
    @Override
    public String toString() {
        return new StringBuilder().append(value.toString()).toString();
    }


}
