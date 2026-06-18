// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Principal object to return authenticated user details such as user name, domain, password and others.</p>
 *
 * @since API 1
 */
@DeviceApi
public final class Principal implements Parcelable {
    /**
     * <p>Enumeration of the accessible authorities</p>
     *
     * @since API 1
     * @deprecated This enum is deprecated and will be removed in a future release.
     */
    @Deprecated
    public enum SimpleAuthority {
        /**
         * <p>Can access to the local ui.</p>
         *
         * @since API 1
         */
        LOCAL_UI_ACCESS,
        /**
         * <p>Can submit job.</p>
         *
         * @since API 1
         */
        JOB_CONTROL,
        /**
         * <p>Can access print operations.</p>
         *
         * @since API 1
         */
        PRINT,
        /**
         * <p>Can access scan operations</p>
         *
         * @since API 1
         */
        SCAN,
        /**
         * <p>Can access copy operations</p>
         *
         * @since API 3
         */
        COPY,
        /**
         * <p>Has admin rights</p>
         *
         * @since API 1
         */
        ADMIN
    }

    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private final int mVersion;

    /**
     * Providers id
     *
     * @since API 1
     */
    private String provider;

    /**
     * Domain of this principal
     *
     * @since API 1
     */
    private String domain;

    /**
     * Name of the principal (full name, corporate id etc.)
     * It is unique id.
     *
     * @since API 1
     */
    private String principalId;

    /**
     * Full principal id.
     *
     * @since API 1
     */
    private String fullyQualifiedName;

    /**
     * Name of the User
     * It is display name.
     *
     * @since API 1
     */
    private String username;

    /**
     * User password
     *
     * @since API 1
     */
    private String password;

    /**
     * User email
     *
     * @since API 1
     */
    private String userEmail;

    /**
     * is logged or not
     *
     * @since API 1
     */
    private boolean authenticated = false;

    /**
     * is administrator or not
     *
     * @since API 1
     */
    private boolean admin = false;

    /**
     * authority list
     *
     * @since API 1
     */
    private List<SimpleAuthority> simpleAuthorities;

    /**
     * User properties
     *
     * @since API 1
     */
    private Map<String, String> userProperties;

    /**
     * Is hpcloud user logged in or not
     *
     * @since API 3
     */
     private boolean isHPCloudUser;

    /**
     * Is SmartCard user logged in or not
     *
     * @since API 3
     */
    private boolean isSmartCardUser;

    /**
     * Is service user logged in or not
     *
     * @since API 3
     */
    private boolean isServiceUser;

    /**
     * Is guest user logged in or not
     *
     * @since API 3
     */
    private boolean isGuestUser;

    /**
     * Is device user logged in or not
     *
     * @since API 3
     */
    private boolean isDeviceUser;

    /**
     * UUID of the authenticated provider
     *
     * @since API 3
     */
    private String providerUUID;

    /**
     * is whitelisted or not
     *
     * @since API 4
     */
    private boolean authNAgentTrusted;

    /**
     * For Parcelable implementation
     */
    @SuppressLint("RestrictedApi")
    private Principal(final Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        provider = in.readString();
        domain = in.readString();
        principalId = in.readString();
        fullyQualifiedName = in.readString();
        username = in.readString();
        password = in.readString();
        userEmail = in.readString();
        authenticated = (1 == in.readInt());
        admin = (1 == in.readInt());
        simpleAuthorities = new ArrayList<>();
        in.readList(simpleAuthorities, this.getClass().getClassLoader());
        userProperties = in.readHashMap(String.class.getClassLoader());

        if (mVersion >= Sdk.VERSION_LEVEL.THREE) {
            isHPCloudUser = (1 == in.readInt());
            isSmartCardUser = (1 == in.readInt());
            isServiceUser = (1 == in.readInt());
            isGuestUser = (1 == in.readInt());
            isDeviceUser = (1 == in.readInt());
            providerUUID = in.readString();
        } else {
            isHPCloudUser = false;
            isSmartCardUser = false;
            isServiceUser = (1 == in.readInt());
            isGuestUser = (1 == in.readInt());
            isDeviceUser = (1 == in.readInt());
            providerUUID = null;
        }

        if (mVersion >= Sdk.VERSION_LEVEL.FOUR) {
            authNAgentTrusted = (1 == in.readInt());
        } else {
            authNAgentTrusted = false;
        }
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<Principal> CREATOR = new Parcelable.Creator<Principal>() {

        @Override
        public Principal createFromParcel(final Parcel in) {
            return new Principal(in);
        }

        @Override
        public Principal[] newArray(final int size) {
            return new Principal[size];
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
        parcel.writeInt(Sdk.VERSION.LEVEL);
        parcel.writeString(provider);
        parcel.writeString(domain);
        parcel.writeString(principalId);
        parcel.writeString(fullyQualifiedName);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(userEmail);
        parcel.writeInt(authenticated ? 1 : 0);
        parcel.writeInt(admin ? 1 : 0);
        parcel.writeList(simpleAuthorities);
        parcel.writeMap(userProperties);

        //API LEVEL FOUR
        parcel.writeInt(isHPCloudUser ? 1 : 0);

        //API LEVEL FIVE
        parcel.writeInt(isSmartCardUser ? 1 : 0);
        parcel.writeInt(isServiceUser ? 1 : 0);
        parcel.writeInt(isGuestUser ? 1 : 0);
        parcel.writeInt(isDeviceUser ? 1 : 0);

        parcel.writeString(providerUUID);

        parcel.writeInt(authNAgentTrusted ? 1 : 0);
    }

    /**
     * <p>Returns authenticated user provider</p>
     *
     * @return String authenticated user provider. If device is not authenticated, no value returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getProvider() {
        return provider;
    }

    /**
     * <p>Returns authenticated user domain</p>
     *
     * @return String authenticated user domain. If device is not authenticated, no value returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getDomain() {
        return domain;
    }

    /**
     * <p>Returns authenticated user full name : provider/domain/id</p>
     *
     * @return String authenticated user full name. If device is not authenticated, default value returns (LOCAL//guest).
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    /**
     * <p>Returns authenticated user id</p>
     *
     * @return String authenticated user id. If device is not authenticated, guest userid returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getPrincipalId() {
        return principalId;
    }


    /**
     * <p>Returns authenticated user name (display name)</p>
     *
     * @return String authenticated user name. If device is not authenticated, guest username returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getUsername() {
        return username;
    }

    /**
     * <p>Returns authenticated user password if authentication provider requires password when sign in</p>
     *
     * @return String authenticated user password. If device is not authenticated, no value returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getPassword() {
        return password;
    }

    /**
     * <p>Returns authenticated user email</p>
     *
     * @return String authenticated user email. If device is not authenticated, no value returns.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * <p>Returns status whether device is authenticated or not</p>
     *
     * @return boolean result of authenticated. If device is not authenticated, returns false.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * <p>Returns result whether authenticated user has system admin privilege or not</p>
     *
     * @return boolean result of user permission to access as system admin. If authenticated user doesn't have admin privilege, returns false.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public boolean isAdmin() {
        return admin;
    }

    /**
     * <p>Returns lists of authorities that current authenticated user has</p>
     *
     * @return List<SimpleAuthority> the list of authorities.
     * @since API 1
     * @deprecated This method is deprecated and will be removed in a future release.
     * On FutureSmart 6+, this method returns a fixed list containing all {@link SimpleAuthority} enum values solely to maintain legacy application continuity.
     */
    @SuppressWarnings({"unused"})
    @Deprecated
    public List<SimpleAuthority> getSimpleAuthorities() {
        return simpleAuthorities;
    }

    /**
     * <p>Return user property</p>
     *
     * @param propertyName the property name for retrieving value
     * @return String the value of the property. If there is no property with such name, returns null.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public String getUserProperty(final String propertyName) {
        return userProperties.get(propertyName);
    }

    /**
     * Internal parcelable version
     *
     * @return internal version
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * <p>Returns current status whether authenticated user is a hpclouduser or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a hpclouduser, returns false.
     * @since API 3
     */
    public boolean isHPCloudUser() {
        return isHPCloudUser;
    }

    /**
     * <p>Returns current status whether authenticated user is a smartcard user or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a smartcard user, returns false.
     * @since API 3
     */
    public boolean isSmartCardUser() {
        return isSmartCardUser;
    }

    /**
     * <p>Returns current status whether authenticated user is a service user or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a service user, returns false.
     * @since API 3
     */
    public boolean isServiceUser() {
        return isServiceUser;
    }

    /**
     * <p>Returns current status whether authenticated user is a guest user or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a guest user, returns false.
     * @since API 3
     */
    public boolean isGuestUser() {
        return isGuestUser;
    }

    /**
     * <p>Returns current status whether authenticated user is a device user or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a device user, returns false.
     * @since API 3
     */
    public boolean isDeviceUser() {
        return isDeviceUser;
    }

    /**
     * <p>Returns authenticated agent provider uuid</p>
     *
     * @return String authenticated agent provider uuid. If device is not authenticated, no value returns.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public String getProviderUUID() {
        return providerUUID;
    }

    /**
     * <p>Returns current status whether authenticated user is a whitelisted or not</p>
     *
     * @return boolean result of user type. If authenticated user is not a whitelisted, returns false.
     * @since API 4
     */
    public boolean isAuthNAgentTrusted() { return authNAgentTrusted; }

    /**
     * @hide
     * @return string representation
     */
    public String toString() {
        return "provider: " + provider +
                ", domain: " + domain +
                ", username: " + username +
                ", isAuthenticated: " + authenticated +
                ", isAdmin: " + admin +
                ", isHPCloudUser: " + isHPCloudUser +
                ", isSmartCardUser: " + isSmartCardUser +
                ", isServiceUser: " + isServiceUser +
                ", isGuestUser: " + isGuestUser +
                ", isDeviceUser: " + isDeviceUser +
                ", providerUUID: " + providerUUID +
                ", authNAgentTrusted: " + authNAgentTrusted;
    }
}
