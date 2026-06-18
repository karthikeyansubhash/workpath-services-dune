// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthenticatedUserInfo class represents information about an authenticated user info.
 * This class includes various user attributes such as username, display name, email address, and more.
 *
 * @since API 9
 */
public class AuthenticatedUserInfo implements Parcelable {
    protected AuthenticationType authenticationType;
    protected String fullyQualifiedUserName;
    protected String userName;
    protected String displayName;
    protected String sidString;
    protected String ldapBindUser;
    protected String userPrincipalName;
    protected String samAccountName;
    protected String homeFolderPath;
    protected String userDomain;
    protected String ndsContext;
    protected String ndsTreeName;
    protected String emailAddress;
    protected String exchangeMailboxUri;
    protected List<KeyValuePair> keyValuePairs;

    public AuthenticatedUserInfo() {
        super();
    }

    /**
     * @hide For Parcelable implementation
     */
    @SuppressLint("RestrictedApi")
    public AuthenticatedUserInfo(final Parcel in) {
        authenticationType = in.readParcelable(AuthenticationType.class.getClassLoader());
        fullyQualifiedUserName = in.readString();
        userName = in.readString();
        displayName = in.readString();
        sidString = in.readString();
        ldapBindUser = in.readString();
        userPrincipalName = in.readString();
        samAccountName = in.readString();
        homeFolderPath = in.readString();
        userDomain = in.readString();
        ndsContext = in.readString();
        ndsTreeName = in.readString();
        emailAddress = in.readString();
        exchangeMailboxUri = in.readString();
        keyValuePairs = new ArrayList<>();
        in.readTypedList(keyValuePairs, KeyValuePair.CREATOR);
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<AuthenticatedUserInfo> CREATOR = new Parcelable.Creator<AuthenticatedUserInfo>() {

        @Override
        public AuthenticatedUserInfo createFromParcel(final Parcel in) {
            return new AuthenticatedUserInfo(in);
        }

        @Override
        public AuthenticatedUserInfo[] newArray(final int size) {
            return new AuthenticatedUserInfo[size];
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
        parcel.writeInt(authenticationType == null ? -1 : authenticationType.ordinal());
        parcel.writeString(fullyQualifiedUserName);
        parcel.writeString(userName);
        parcel.writeString(displayName);
        parcel.writeString(sidString);
        parcel.writeString(ldapBindUser);
        parcel.writeString(userPrincipalName);
        parcel.writeString(samAccountName);
        parcel.writeString(homeFolderPath);
        parcel.writeString(userDomain);
        parcel.writeString(ndsContext);
        parcel.writeString(ndsTreeName);
        parcel.writeString(emailAddress);
        parcel.writeString(exchangeMailboxUri);
        parcel.writeTypedList(keyValuePairs);
    }

    /**
     * Gets the value of the authenticationType property.
     *
     * @return The authentication type.
     * {@link AuthenticationType }
     * @since API 9
     */
    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * @hide for internal use
     */
    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * Gets the value of the fullyQualifiedUserName property.
     *
     * @return The fully qualified username.
     * {@link String }
     * @since API 9
     */
    public String getFullyQualifiedUserName() {
        return fullyQualifiedUserName;
    }

    /**
     * @hide for internal use
     */
    public void setFullyQualifiedUserName(String fullyQualifiedUserName) {
        this.fullyQualifiedUserName = fullyQualifiedUserName;
    }

    /**
     * Gets the value of the userName property.
     *
     * @return The username.
     * {@link String }
     * @since API 9
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @hide for internal use
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return The display name.
     * {@link String }
     * @since API 9
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @hide for internal use
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the value of the sidString property.
     *
     * @return The SID string.
     * {@link String }
     * @since API 9
     */
    public String getSidString() {
        return sidString;
    }

    /**
     * @hide for internal use
     */
    public void setSidString(String sidString) {
        this.sidString = sidString;
    }

    /**
     * Gets the value of the ldapBindUser property.
     *
     * @return The LDAP bind user.
     * {@link String }
     * @since API 9
     */
    public String getLdapBindUser() {
        return ldapBindUser;
    }

    /**
     * @hide for internal use
     */
    public void setLdapBindUser(String ldapBindUser) {
        this.ldapBindUser = ldapBindUser;
    }

    /**
     * Gets the value of the userPrincipalName property.
     *
     * @return The user principal name.
     * {@link String }
     * @since API 9
     */
    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    /**
     * @hide for internal use
     */
    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    /**
     * Gets the value of the samAccountName property.
     *
     * @return The SAM account name.
     * {@link String }
     * @since API 9
     */
    public String getSamAccountName() {
        return samAccountName;
    }

    /**
     * @hide for internal use
     */
    public void setSamAccountName(String samAccountName) {
        this.samAccountName = samAccountName;
    }

    /**
     * Gets the value of the homeFolderPath property.
     *
     * @return The home folder path.
     * {@link String }
     * @since API 9
     */
    public String getHomeFolderPath() {
        return homeFolderPath;
    }

    /**
     * @hide for internal use
     */
    public void setHomeFolderPath(String homeFolderPath) {
        this.homeFolderPath = homeFolderPath;
    }

    /**
     * Gets the value of the userDomain property.
     *
     * @return The user domain.
     * {@link String }
     * @since API 9
     */
    public String getUserDomain() {
        return userDomain;
    }

    /**
     * @hide for internal use
     */
    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    /**
     * Gets the value of the ndsContext property.
     *
     * @return The NDS context.
     * {@link String }
     * @since API 9
     */
    public String getNdsContext() {
        return ndsContext;
    }

    /**
     * @hide for internal use
     */
    public void setNdsContext(String ndsContext) {
        this.ndsContext = ndsContext;
    }

    /**
     * Gets the value of the ndsTreeName property.
     *
     * @return The NDS tree name.
     * {@link String }
     * @since API 9
     */
    public String getNdsTreeName() {
        return ndsTreeName;
    }

    /**
     * @hide for internal use
     */
    public void setNdsTreeName(String ndsTreeName) {
        this.ndsTreeName = ndsTreeName;
    }

    /**
     * Gets the value of the emailAddress property.
     *
     * @return The email address.
     * {@link String }
     * @since API 9
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @hide for internal use
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the value of the exchangeMailboxUri property.
     *
     * @return The exchange mail box URI
     * {@link String }
     * @since API 9
     */
    public String getExchangeMailboxUri() {
        return exchangeMailboxUri;
    }

    /**
     * @hide for internal use
     */
    public void setExchangeMailboxUri(String exchangeMailboxUri) {
        this.exchangeMailboxUri = exchangeMailboxUri;
    }

    /**
     * Gets the value of the keyValuePairs property.
     *
     * @return The key value pairs
     * {@link KeyValuePair }
     * @since API 9
     */
    public List<KeyValuePair> getKeyValuePairs() {
        return keyValuePairs;
    }

    /**
     * @hide for internal use
     */
    public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }

    @Override
    public String toString() {
        return "AuthenticatedUserInfo{" +
                "authenticationType=" + authenticationType +
                ", fullyQualifiedUserName='" + fullyQualifiedUserName + '\'' +
                ", userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", sidString='" + sidString + '\'' +
                ", ldapBindUser='" + ldapBindUser + '\'' +
                ", userPrincipalName='" + userPrincipalName + '\'' +
                ", samAccountName='" + samAccountName + '\'' +
                ", homeFolderPath='" + homeFolderPath + '\'' +
                ", userDomain='" + userDomain + '\'' +
                ", ndsContext='" + ndsContext + '\'' +
                ", ndsTreeName='" + ndsTreeName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", exchangeMailboxUri='" + exchangeMailboxUri + '\'' +
                ", keyValuePairs=" + keyValuePairs +
                '}';
    }
}
