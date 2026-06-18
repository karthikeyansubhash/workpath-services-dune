// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes principal used for authentication such as user, company, login id etc.<br>
 *
 * @since API 1
 * @hide
 */
public final class PrincipalInternal implements Parcelable {

    /**
     * Providers id
     * @since API 1
     */
    private String provider;
    /**
     * Domain of this principal
     * @since API 1
     */
    private String domain;
    /**
     * Name of the principal (full name, corporate id etc.)
     * @since API 1
     */
    private String principalId;

    /**
     * Full principal id.
     * @since API 1
     */
    private String fullyQualifiedName;

    /**
     * Name of the User
     * It is display name.
     * @since API 1
     */
    private String username;

    /**
     * User password
     * @since API 1
     */
    private String password;

    /**
     * User email
     * @since API 1
     */
    private String userEmail;

    /**
     * is logged or not
     * @since API 1
     */
    private boolean authenticated = false;

    /**
     * is administrator or not
     * @since API 1
     */
    private boolean admin = false;

    /**
     * AA is supported or not
     * @since API 1
     */
    private boolean supported = false;

    /**
     * authority list
     * @since API 1
     */
    private List<String> simpleAuthorities;

    /**
     * map for all user properties
     * @since API 1
     */
    private Map<String, String> userProperties = new HashMap<>();

    /**
     * is logged or not with user type
     * @since API 3
     */
    private boolean isSmartCardUser = false;
    private boolean isServiceUser = false;
    private boolean isGuestUser = false;
    private boolean isDeviceUser = false;
    private boolean isHPCloudUser = false;

    /**
     * UUID of the authenticated provider
     * @since API 3
     */
    private String providerUUID;

    /**
     * is whitelisted or not
     * @since API 4
     */
    private boolean authNAgentTrusted = false;

    public PrincipalInternal(){

    }
    /**
     * For Parcelable implementation
     */
    private PrincipalInternal(final Parcel in) {
        simpleAuthorities = new ArrayList<>();

        provider = in.readString();
        domain = in.readString();
        principalId = in.readString();
        fullyQualifiedName = in.readString();
        username = in.readString();
        password = in.readString();
        userEmail = in.readString();
        authenticated = (1 == in.readInt());
        admin = (1 == in.readInt());
        supported = (1 == in.readInt());
        in.readList(simpleAuthorities, this.getClass().getClassLoader());
        userProperties = in.readHashMap(String.class.getClassLoader());

        //api sdk 1.2.5
        isSmartCardUser = (1 == in.readInt());
        isServiceUser = (1 == in.readInt());
        isGuestUser = (1 == in.readInt());
        isDeviceUser = (1 == in.readInt());
        isHPCloudUser = (1 == in.readInt());

        //api sdk 1.2.5
        providerUUID = in.readString();

        authNAgentTrusted = (1 == in.readInt());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<PrincipalInternal> CREATOR = new Creator<PrincipalInternal>() {
        @Override
        public PrincipalInternal createFromParcel(final Parcel in) {
            return new PrincipalInternal(in);
        }

        @Override
        public PrincipalInternal[] newArray(final int size) {
            return new PrincipalInternal[size];
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
        parcel.writeString(provider);
        parcel.writeString(domain);
        parcel.writeString(principalId);
        parcel.writeString(fullyQualifiedName);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(userEmail);
        parcel.writeInt(authenticated? 1:0);
        parcel.writeInt(admin? 1:0);
        parcel.writeInt(supported? 1:0);
        parcel.writeList(simpleAuthorities);
        parcel.writeMap(userProperties);

        //api sdk 1.2.5
        parcel.writeInt(isSmartCardUser? 1:0);
        parcel.writeInt(isServiceUser? 1:0);
        parcel.writeInt(isGuestUser? 1:0);
        parcel.writeInt(isDeviceUser? 1:0);
        parcel.writeInt(isHPCloudUser? 1:0);

        //api sdk 1.2.5
        parcel.writeString(providerUUID);

        parcel.writeInt(authNAgentTrusted? 1:0);
    }
    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return (provider != null ? provider : "") + "\\" +
                (domain != null ? domain : "") + "\\" +
                (principalId != null ? principalId : "");
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public List<String> getSimpleAuthorities() {
        return simpleAuthorities;
    }

    public void setSimpleAuthorities(List<String> simpleAuthorities) {
        this.simpleAuthorities = simpleAuthorities;
    }

    public Map<String, String> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(Map<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    public boolean isSmartCardUser() {
        return isSmartCardUser;
    }

    public void setSmartCardUser(boolean smartCardUser) {
        isSmartCardUser = smartCardUser;
    }

    public boolean isServiceUser() {
        return isServiceUser;
    }

    public void setServiceUser(boolean serviceUser) {
        isServiceUser = serviceUser;
    }

    public boolean isGuestUser() {
        return isGuestUser;
    }

    public void setGuestUser(boolean guestUser) {
        isGuestUser = guestUser;
    }

    public boolean isDeviceUser() {
        return isDeviceUser;
    }

    public void setDeviceUser(boolean deviceUser) {
        isDeviceUser = deviceUser;
    }

    public boolean isHPCloudUser() {
        return isHPCloudUser;
    }

    public void setHPCloudUser(boolean HPCloudUser) {
        isHPCloudUser = HPCloudUser;
    }

    public String getProviderUUID() { return providerUUID; }

    public void setProviderUUID(String providerUUID) { this.providerUUID = providerUUID; }

    public boolean isAuthNAgentTrusted() { return authNAgentTrusted; }

    public void setAuthNAgentTrusted(boolean authNAgentTrusted) { this.authNAgentTrusted = authNAgentTrusted; }
}
