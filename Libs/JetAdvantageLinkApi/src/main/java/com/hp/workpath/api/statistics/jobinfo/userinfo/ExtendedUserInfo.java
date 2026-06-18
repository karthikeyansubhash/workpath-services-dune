// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.userinfo;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Extended User information
 *
 * @since API 5
 */
@DeviceApi
public class ExtendedUserInfo {
    @Keep
    private AuthenticatedUserInfo authenticatedUserInfo;
    private String authenticationAgentId;
    private String authenticationAgentName;
    private String authorizationAgentId;
    private String authorizationAgentName;

    /**
     * Enums of AuthenticationType
     *
     * @since API 5
     */
    @Keep
    public enum AuthenticationType {
        /**
         * Windows
         *
         * @since API 5
         */
        @SerializedName("Windows")
        WINDOWS,

        /**
         * WindowsSmartCard
         *
         * @since API 5
         */
        @SerializedName("WindowsSmartCard")
        WINDOWS_SMART_CARD,

        /**
         * Novell
         *
         * @since API 5
         */
        @SerializedName("Novell")
        NOVELL,

        /**
         * LDAP
         *
         * @since API 5
         */
        @SerializedName("LDAP")
        LDAP,

        /**
         * PIN
         *
         * @since API 5
         */
        @SerializedName("PIN")
        PIN,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * A collection of keyValue
     *
     * @since API 5
     */
    public class KeyValue {
        private String key;
        private String valueString;

        /**
         * Returns KEY
         *
         * @return key
         * <p>
         * <ul>
         * <li>Return can be null if the key is null</li>
         * <li>Return can be null if the key is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getKey() {
            return key;
        }

        /**
         * Returns VALUE
         *
         * @return valueString
         * <p>
         * <ul>
         * <li>Return can be null if the valueString is null</li>
         * <li>Return can be null if the valueString is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getValueString() {
            return valueString;
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
        public void setValueString(String valueString) {
            this.valueString = valueString;
        }
    }

    /**
     * Returns Authenticated User Information
     *
     * @return authenticatedUserInfo
     * <p>
     * <ul>
     * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.userinfo.AuthenticatedUserInfo} is null</li>
     * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.userinfo.AuthenticatedUserInfo} is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public AuthenticatedUserInfo getAuthenticatedUserInfo() {
        return authenticatedUserInfo;
    }

    /**
     * Returns authentication Agent Id
     *
     * @return authenticationAgentId
     * <p>
     * <ul>
     * <li>Return can be null if the authenticationAgentId is null</li>
     * <li>Return can be null if the authenticationAgentId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getAuthenticationAgentId() {
        return authenticationAgentId;
    }

    /**
     * Returns authentication Agent Name
     *
     * @return authenticationAgentName
     * <p>
     * <ul>
     * <li>Return can be null if the authenticationAgentName is null</li>
     * <li>Return can be null if the authenticationAgentName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getAuthenticationAgentName() {
        return authenticationAgentName;
    }

    /**
     * Returns authorization AgentId
     *
     * @return authorizationAgentId
     * <p>
     * <ul>
     * <li>Return can be null if the authorizationAgentId is null</li>
     * <li>Return can be null if the authorizationAgentId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getAuthorizationAgentId() {
        return authorizationAgentId;
    }

    /**
     * Returns authorization Agent Name
     *
     * @return authorizationAgentName
     * <p>
     * <ul>
     * <li>Return can be null if the authorizationAgentName is null</li>
     * <li>Return can be null if the authorizationAgentName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getAuthorizationAgentName() {
        return authorizationAgentName;
    }

    /**
     * Set the authenticated user info
     * @param authenticatedUserInfo the authenticatedUserInfo to set
     * @since API 5
     */
    public void setAuthenticatedUserInfo(AuthenticatedUserInfo authenticatedUserInfo) {
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    /**
     * Set the authentication agent ID
     * @param authenticationAgentId the authenticationAgentId to set
     * @since API 5
     */
    public void setAuthenticationAgentId(String authenticationAgentId) {
        this.authenticationAgentId = authenticationAgentId;
    }

    /**
     * Set the authentication agent name
     * @param authenticationAgentName the authenticationAgentName to set
     * @since API 5
     */
    public void setAuthenticationAgentName(String authenticationAgentName) {
        this.authenticationAgentName = authenticationAgentName;
    }

    /**
     * Set the authorization agent ID
     * @param authorizationAgentId the authorizationAgentId to set
     * @since API 5
     */
    public void setAuthorizationAgentId(String authorizationAgentId) {
        this.authorizationAgentId = authorizationAgentId;
    }

    /**
     * Set the authorization agent name
     * @param authorizationAgentName the authorizationAgentName to set
     * @since API 5
     */
    public void setAuthorizationAgentName(String authorizationAgentName) {
        this.authorizationAgentName = authorizationAgentName;
    }
}
