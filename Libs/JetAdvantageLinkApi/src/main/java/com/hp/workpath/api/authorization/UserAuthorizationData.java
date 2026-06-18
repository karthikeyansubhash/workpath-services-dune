// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The UserAuthorizationData class represents the data required for user authorization.
 * This class includes authenticated user information, authentication agent details, user overrides, and a timestamp.
 *
 * @since API 9
 */
public class UserAuthorizationData implements Parcelable {
    protected AuthenticatedUserInfo authenticatedUserInfo;
    protected String authenticationAgentId;
    protected String authenticationAgentName;
    protected UserOverrides authenticatedUserOverrides;
    protected String opaqueAuthenticationToken;
    protected Timestamp timestamp;

    public UserAuthorizationData() {
        super();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    public UserAuthorizationData(final Parcel in) {
        authenticatedUserInfo = in.readParcelable(AuthenticatedUserInfo.class.getClassLoader());
        authenticationAgentId = in.readString();
        authenticationAgentName = in.readString();
        authenticatedUserOverrides = in.readParcelable(UserOverrides.class.getClassLoader());
        opaqueAuthenticationToken = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<UserAuthorizationData> CREATOR = new Parcelable.Creator<UserAuthorizationData>() {

        @Override
        public UserAuthorizationData createFromParcel(final Parcel in) {
            return new UserAuthorizationData(in);
        }

        @Override
        public UserAuthorizationData[] newArray(final int size) {
            return new UserAuthorizationData[size];
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
        parcel.writeParcelable(authenticatedUserInfo, i);
        parcel.writeString(authenticationAgentId);
        parcel.writeString(authenticationAgentName);
        parcel.writeParcelable(authenticatedUserOverrides, i);
        parcel.writeString(opaqueAuthenticationToken);
        parcel.writeParcelable(timestamp, i);
    }

    /**
     * Returns the authenticated user information.
     *
     * @return The authenticated user information.
     * @since API 9
     */
    public AuthenticatedUserInfo getAuthenticatedUserInfo() {
        return authenticatedUserInfo;
    }

    /**
     * Returns the authentication agent ID.
     *
     * @return The authentication agent ID.
     * @since API 9
     */
    public String getAuthenticationAgentId() {
        return authenticationAgentId;
    }

    /**
     * Returns the authentication agent name.
     *
     * @return The authentication agent name.
     * @since API 9
     */
    public String getAuthenticationAgentName() {
        return authenticationAgentName;
    }

    /**
     * Returns the authenticated user overrides.
     *
     * @return The authenticated user overrides.
     * @since API 9
     */
    public UserOverrides getAuthenticatedUserOverrides() {
        return authenticatedUserOverrides;
    }

    /**
     * Returns the opaque authentication token.
     *
     * @return The opaque authentication token.
     * @since API 9
     */
    public String getOpaqueAuthenticationToken() {
        return opaqueAuthenticationToken;
    }

    /**
     * Returns the timestamp of the user authorization data.
     *
     * @return The timestamp.
     * @since API 9
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @hide
     */
    public void setAuthenticatedUserInfo(AuthenticatedUserInfo authenticatedUserInfo) {
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    /**
     * @hide
     */
    public void setAuthenticationAgentId(String authenticationAgentId) {
        this.authenticationAgentId = authenticationAgentId;
    }

    /**
     * @hide
     */
    public void setAuthenticationAgentName(String authenticationAgentName) {
        this.authenticationAgentName = authenticationAgentName;
    }

    /**
     * @hide
     */
    public void setAuthenticatedUserOverrides(UserOverrides authenticatedUserOverrides) {
        this.authenticatedUserOverrides = authenticatedUserOverrides;
    }

    /**
     * @hide
     */
    public void setOpaqueAuthenticationToken(String opaqueAuthenticationToken) {
        this.opaqueAuthenticationToken = opaqueAuthenticationToken;
    }

    /**
     * @hide
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserAuthorizationData{" +
                "authenticatedUserInfo=" + authenticatedUserInfo +
                ", authenticationAgentId='" + authenticationAgentId + '\'' +
                ", authenticationAgentName='" + authenticationAgentName + '\'' +
                ", authenticatedUserOverrides=" + authenticatedUserOverrides +
                ", opaqueAuthenticationToken='" + opaqueAuthenticationToken + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
