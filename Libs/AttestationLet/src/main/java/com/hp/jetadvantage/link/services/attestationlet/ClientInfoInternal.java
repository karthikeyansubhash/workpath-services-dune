// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Client information
 * @hide
 */
public final class ClientInfoInternal implements Parcelable {
    private String client_id;
    private String client_secret;
    private String alt_client_id;

    /**
     * @hide parcelable implementation
     */
    private ClientInfoInternal(Parcel in) {
        client_id = in.readString();
        client_secret = in.readString();

        try {
            alt_client_id = in.readString();
        } catch (Exception e) {}
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client_id);
        dest.writeString(client_secret);
        dest.writeString(alt_client_id);
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
    public static final Creator<ClientInfoInternal> CREATOR = new Creator<ClientInfoInternal>() {
        @Override
        public ClientInfoInternal createFromParcel(Parcel in) {
            return new ClientInfoInternal(in);
        }

        @Override
        public ClientInfoInternal[] newArray(int size) { return new ClientInfoInternal[size]; }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "Client{" +
                "client id=" + client_id + '}';
    }
}
