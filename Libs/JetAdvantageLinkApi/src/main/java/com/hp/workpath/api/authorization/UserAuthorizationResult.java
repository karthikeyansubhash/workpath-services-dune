// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserAuthorizationResult class represents the result of the user authorization process.
 * This class includes the set of permissions granted to the user and any user overrides.
 *
 * @since API 9
 */
public class UserAuthorizationResult implements Parcelable {
    final List<String> userPermissionSet;
    final UserOverrides authorizedUserOverrides;

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private UserAuthorizationResult(final Parcel in) {
        this.userPermissionSet = new ArrayList<>();
        in.readStringList(userPermissionSet);
        this.authorizedUserOverrides = in.readParcelable(UserOverrides.class.getClassLoader());
    }

    /**
     * Constructs a UserAuthorizationResult with the specified builder.
     *
     * @param builder The builder to construct the UserAuthorizationResult.
     * @since API 9
     */
    private UserAuthorizationResult(final UserAuthorizationResult.Builder builder) {
        userPermissionSet = builder.userPermissionSet;
        authorizedUserOverrides = builder.authorizedUserOverrides;
    }

    /**
     * Builder class for UserAuthorizationResult.
     *
     * @since API 9
     */
    @DeviceApi
    public static class Builder {
        List<String> userPermissionSet = new ArrayList<>();
        UserOverrides authorizedUserOverrides;

        /**
         * Default constructor a new Builder with default attributes.
         *
         * @since API 9
         */
        public Builder() {
        }

        /**
         * Sets the user permission set.
         *
         * @param guestPermissions The list of user permissions.
         * @return The builder instance.
         * @since API 9
         */
        public UserAuthorizationResult.Builder setUserPermissionSet(@Nullable List<String> guestPermissions) {
            userPermissionSet = guestPermissions;
            return this;
        }

        /**
         * Sets the authorized user overrides.
         *
         * @param guestUsers The authorized user overrides.
         * @return The builder instance.
         * @since API 9
         */
        public UserAuthorizationResult.Builder setAuthorizedUserOverrides(@Nullable UserOverrides guestUsers) {
            authorizedUserOverrides = guestUsers;
            return this;
        }

        /**
         * Builds and returns the UserAuthorizationResult instance.
         *
         * @return The UserAuthorizationResult instance.
         * @since API 9
         */
        @NonNull
        public UserAuthorizationResult build() {
            return new UserAuthorizationResult(this);
        }
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<UserAuthorizationResult> CREATOR = new Parcelable.Creator<UserAuthorizationResult>() {

        @Override
        public UserAuthorizationResult createFromParcel(final Parcel in) {
            return new UserAuthorizationResult(in);
        }

        @Override
        public UserAuthorizationResult[] newArray(final int size) {
            return new UserAuthorizationResult[size];
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
        parcel.writeStringList(userPermissionSet);
        parcel.writeParcelable(authorizedUserOverrides, i);
    }

    /**
     * Returns the set of permissions granted to the user.
     *
     * @return The user permission set.
     * @since API 9
     */
    public List<String> getUserPermissionSet() {
        return this.userPermissionSet;
    }


    /**
     * Returns the authorized user overrides.
     *
     * @return The authorized user overrides.
     * @since API 9
     */
    public UserOverrides getAuthorizedUserOverrides() {
        return authorizedUserOverrides;
    }

    @Override
    public String toString() {
        return "UserAuthorizationResult{" +
                "userPermissionSet=" + (userPermissionSet != null ? userPermissionSet : null) +
                ", authorizedUserOverrides=" + authorizedUserOverrides +
                '}';
    }
}
