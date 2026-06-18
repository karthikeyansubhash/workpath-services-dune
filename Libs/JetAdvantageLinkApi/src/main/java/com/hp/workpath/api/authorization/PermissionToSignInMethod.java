// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The PermissionToSignInMethod class represents the mapping between a permission and a sign-in method.
 * This class includes the permission ID (UUID) and the sign-in method ID (UUID).
 *
 * @since API 9
 */
public class PermissionToSignInMethod implements Parcelable {
    protected String permissionId;
    protected String signInMethodId;

    /**
     * Default no-arg constructor
     */
    public PermissionToSignInMethod() {
        super();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    public PermissionToSignInMethod(final Parcel in) {
        this.permissionId = in.readString();
        this.signInMethodId = in.readString();
    }

    /**
     * Constructs a new PermissionToSignInMethod with the specified permission ID and sign-in method ID.
     *
     * @param permissionId   The ID of the permission.
     * @param signInMethodId The ID of the sign-in method.
     * @since API 9
     */
    public PermissionToSignInMethod(String permissionId, String signInMethodId) {
        this.permissionId = permissionId;
        this.signInMethodId = signInMethodId;
    }

    /**
     * Returns the ID of the permission.
     *
     * @return The permission ID.
     * @since API 9
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * Returns the ID of the sign-in method.
     *
     * @return The sign-in method ID.
     * @since API 9
     */
    public String getSignInMethodId() {
        return signInMethodId;
    }

    /**
     * String representation of PermissionToSignInMethod
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("permissionId=").append(permissionId).append(", ").append("signInMethodId=").append(signInMethodId).append("]").toString();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<PermissionToSignInMethod> CREATOR = new Parcelable.Creator<PermissionToSignInMethod>() {

        @Override
        public PermissionToSignInMethod createFromParcel(final Parcel in) {
            return new PermissionToSignInMethod(in);
        }

        @Override
        public PermissionToSignInMethod[] newArray(final int size) {
            return new PermissionToSignInMethod[size];
        }
    };

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(permissionId);
        parcel.writeString(signInMethodId);
    }
}
