// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.userinfo;

import androidx.annotation.Keep;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Authenticated User information
 *
 * @since API 5
 */
@DeviceApi
public class AuthenticatedUserInfo {
    @Keep
    private ExtendedUserInfo.AuthenticationType authenticationType;
    private String displayName;
    private String emailAddress;
    private String exchangeMailboxUri;
    private String fullyQualifiedUserName;
    private String homeFolderPath;
    private String ldapBindUser;
    private String ndsContext;
    private String ndsTreeName;
    private String sAMAccountName;
    private String sidString;
    private String userDomain;
    private String userName;
    private String userPrincipalName;
    private ExtendedUserInfo.KeyValue[] keyValuePairs;

    /**
     * Returns AuthenticationType
     *
     * @return authenticationType
     * <p>
     * <ul>
     * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo.AuthenticationType} is null</li>
     * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo.AuthenticationType} is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ExtendedUserInfo.AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Returns Display name
     *
     * @return displayName
     * <p>
     * <ul>
     * <li>Return can be null if the displayName is null</li>
     * <li>Return can be null if the displayName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns email address
     *
     * @return emailAddress
     * <p>
     * <ul>
     * <li>Return can be null if the emailAddress is null</li>
     * <li>Return can be null if the emailAddress is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Returns exchange mail box Uri
     *
     * @return exchangeMailboxUri
     * <p>
     * <ul>
     * <li>Return can be null if the exchangeMailboxUri is null</li>
     * <li>Return can be null if the exchangeMailboxUri is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getExchangeMailboxUri() {
        return exchangeMailboxUri;
    }

    /**
     * Returns fully qualified Username
     *
     * @return fullyQualifiedUserName
     * <p>
     * <ul>
     * <li>Return can be null if the fullyQualifiedUserName is null</li>
     * <li>Return can be null if the fullyQualifiedUserName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getFullyQualifiedUserName() {
        return fullyQualifiedUserName;
    }

    /**
     * Returns home folder path
     *
     * @return homeFolderPath
     * <p>
     * <ul>
     * <li>Return can be null if the homeFolderPath is null</li>
     * <li>Return can be null if the homeFolderPath is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getHomeFolderPath() {
        return homeFolderPath;
    }

    /**
     * Returns ldap Bind User
     *
     * @return ldapBindUser
     * <p>
     * <ul>
     * <li>Return can be null if the ldapBindUser is null</li>
     * <li>Return can be null if the ldapBindUser is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getLdapBindUser() {
        return ldapBindUser;
    }

    /**
     * Returns nds Context
     *
     * @return ndsContext
     * <p>
     * <ul>
     * <li>Return can be null if the ndsContext is null</li>
     * <li>Return can be null if the ndsContext is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getNdsContext() {
        return ndsContext;
    }

    /**
     * Returns nds TreeName
     *
     * @return ndsTreeName
     * <p>
     * <ul>
     * <li>Return can be null if the ndsTreeName is null</li>
     * <li>Return can be null if the ndsTreeName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getNdsTreeName() {
        return ndsTreeName;
    }

    /**
     * Returns sAMAccountName
     *
     * @return sAMAccountName
     * <p>
     * <ul>
     * <li>Return can be null if the sAMAccountName is null</li>
     * <li>Return can be null if the sAMAccountName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getsAMAccountName() {
        return sAMAccountName;
    }

    /**
     * Returns sid String
     *
     * @return sidString
     * <p>
     * <ul>
     * <li>Return can be null if the sidString is null</li>
     * <li>Return can be null if the sidString is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getSidString() {
        return sidString;
    }

    /**
     * Returns user Domain
     *
     * @return userDomain
     * <p>
     * <ul>
     * <li>Return can be null if the userDomain is null</li>
     * <li>Return can be null if the userDomain is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUserDomain() {
        return userDomain;
    }

    /**
     * Returns user name
     *
     * @return userName
     * <p>
     * <ul>
     * <li>Return can be null if the userName is null</li>
     * <li>Return can be null if the userName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns user principal name
     *
     * @return userPrincipalName
     * <p>
     * <ul>
     * <li>Return can be null if the userPrincipalName is null</li>
     * <li>Return can be null if the userPrincipalName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    private final String SECURITY_CONTEXT_AUTH_CONTEXT_ID = "linkAuthenticationContextId";
    private final String SECURITY_CONTEXT_AUTH_PROVIDER_ID = "linkAuthenticationProviderId";

    /**
     * Returns keyValuePairs
     *
     * @return keyValuePairs
     * <p>
     * <ul>
     * <li>if {@link com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo.KeyValue} field is not added to KeyValue array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ExtendedUserInfo.KeyValue[] getKeyValuePairs() {
        ExtendedUserInfo.KeyValue[] keyValues = keyValuePairs;
        if (keyValuePairs != null) {
            for (int inx = 0; inx < keyValues.length; inx++) {
                ExtendedUserInfo.KeyValue keyValue = keyValues[inx];
                if (SECURITY_CONTEXT_AUTH_CONTEXT_ID.equalsIgnoreCase(keyValue.getKey()) ||
                        SECURITY_CONTEXT_AUTH_PROVIDER_ID.equalsIgnoreCase(keyValue.getKey())) {
                    keyValues[inx].setValueString("");
                }
            }
        }
        return keyValuePairs;
    }

    /**
     * Set the authentication type
     * @param authenticationType the authenticationType to set
     * @since API 5
     */
    public void setAuthenticationType(ExtendedUserInfo.AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * Set the display name
     * @param displayName the displayName to set
     * @since API 5
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set the email address
     * @param emailAddress the emailAddress to set
     * @since API 5
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Set the exchange mailbox URI
     * @param exchangeMailboxUri the exchangeMailboxUri to set
     * @since API 5
     */
    public void setExchangeMailboxUri(String exchangeMailboxUri) {
        this.exchangeMailboxUri = exchangeMailboxUri;
    }

    /**
     * Set the fully qualified user name
     * @param fullyQualifiedUserName the fullyQualifiedUserName to set
     * @since API 5
     */
    public void setFullyQualifiedUserName(String fullyQualifiedUserName) {
        this.fullyQualifiedUserName = fullyQualifiedUserName;
    }

    /**
     * Set the home folder path
     * @param homeFolderPath the homeFolderPath to set
     * @since API 5
     */
    public void setHomeFolderPath(String homeFolderPath) {
        this.homeFolderPath = homeFolderPath;
    }

    /**
     * Set the LDAP bind user
     * @param ldapBindUser the ldapBindUser to set
     * @since API 5
     */
    public void setLdapBindUser(String ldapBindUser) {
        this.ldapBindUser = ldapBindUser;
    }

    /**
     * Set the NDS context
     * @param ndsContext the ndsContext to set
     * @since API 5
     */
    public void setNdsContext(String ndsContext) {
        this.ndsContext = ndsContext;
    }

    /**
     * Set the NDS tree name
     * @param ndsTreeName the ndsTreeName to set
     * @since API 5
     */
    public void setNdsTreeName(String ndsTreeName) {
        this.ndsTreeName = ndsTreeName;
    }

    /**
     * Set the SAM account name
     * @param sAMAccountName the sAMAccountName to set
     * @since API 5
     */
    public void setsAMAccountName(String sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
    }

    /**
     * Set the SID string
     * @param sidString the sidString to set
     * @since API 5
     */
    public void setSidString(String sidString) {
        this.sidString = sidString;
    }

    /**
     * Set the user domain
     * @param userDomain the userDomain to set
     * @since API 5
     */
    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    /**
     * Set the user name
     * @param userName the userName to set
     * @since API 5
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set the user principal name
     * @param userPrincipalName the userPrincipalName to set
     * @since API 5
     */
    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    /**
     * Set the key value pairs
     * @param keyValuePairs the keyValuePairs to set
     * @since API 5
     */
    public void setKeyValuePairs(ExtendedUserInfo.KeyValue[] keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }
}
