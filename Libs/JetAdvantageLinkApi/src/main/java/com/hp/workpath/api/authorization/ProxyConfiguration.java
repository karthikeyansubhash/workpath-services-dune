// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The ProxyConfiguration class manages the proxy settings for the Authorization service.
 * This class includes configurations for guest user permissions, sign-in methods, proxy settings, and more.
 * <p>
 * The ProxyConfiguration includes the following:
 * <p>
 * Required:
 * - AddNewPermissionToGuestPermissionSet (Boolean): Determines whether new permissions are automatically added to the guest permission set.
 * - DefaultSignInMethod (UUID): The default sign-in method used for authentication.
 * - EnableSignInChoice (Boolean): Allows users to select an alternate sign-in method if available.
 * - EnableChangeNotification (Boolean): Enables notifications for changes in the proxy configuration.
 * <p>
 * Optional:
 * - GuestPermissionSet (List<{@link Permission}>): A list of permissions granted to unauthenticated (guest) users.
 * - GuestUserOverrides: Overrides for guest user settings.
 * - PermissionToSignInMethodMap: A map of permissions to specific sign-in methods.
 */
public class ProxyConfiguration implements Parcelable {
    final List<String> guestPermissionSet;
    final boolean addNewPermissionToGuestPermissionSet;
    final UserOverrides guestUserOverrides;
    final List<PermissionToSignInMethod> permissionToSignInMethodMap;
    final String defaultSignInMethod;
    final boolean enableSignInChoice;

    /**
     * Constructs a ProxyConfiguration with the specified builder.
     *
     * @param builder The builder to construct the ProxyConfiguration.
     * @since API 9
     */
    private ProxyConfiguration(final ProxyConfiguration.Builder builder) {
        guestPermissionSet = builder.guestPermissionSet;
        addNewPermissionToGuestPermissionSet = builder.addNewPermissionToGuestPermissionSet;
        guestUserOverrides = builder.guestUserOverrides;
        permissionToSignInMethodMap = builder.permissionToSignInMethodMap;
        defaultSignInMethod = builder.defaultSignInMethod;
        enableSignInChoice = builder.enableSignInChoice;
    }

    /**
     * Builder class for ProxyConfiguration.
     *
     * @since API 9
     */
    @DeviceApi
    public static class Builder {
        List<String> guestPermissionSet = new ArrayList<>();
        boolean addNewPermissionToGuestPermissionSet = false;
        boolean enableSignInChoice = false;
        String defaultSignInMethod;
        UserOverrides guestUserOverrides;
        List<PermissionToSignInMethod> permissionToSignInMethodMap = new ArrayList<>();

        /**
         * Default constructor for a new Builder with default attributes.
         *
         * @since API 9
         */
        public Builder() {
        }

        /**
         * Sets the guest permission set.
         *
         * @param guestPermissions The list of guest permissions.
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setGuestPermissionSet(@Nullable List<String> guestPermissions) {
            guestPermissionSet = guestPermissions;
            return this;
        }

        /**
         * Sets whether to add new permissions to the guest permission set.
         *
         * @param flag True to add new permissions, false otherwise.
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setAddNewPermissionToGuestPermissionSet(@NonNull boolean flag) {
            addNewPermissionToGuestPermissionSet = flag;
            return this;
        }

        /**
         * Sets whether to enable sign-in choice.
         *
         * @param flag True to enable sign-in choice, false otherwise.
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setEnableSignInChoice(@NonNull boolean flag) {
            enableSignInChoice = flag;
            return this;
        }

        /**
         * Sets the default sign-in method.
         *
         * @param defaultMethod The default sign-in method. (UUID)
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setDefaultSignInMethod(@NonNull String defaultMethod) {
            defaultSignInMethod = defaultMethod;
            return this;
        }

        /**
         * Sets the guest user overrides.
         *
         * @param guestUsers The guest user overrides.
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setGuestUserOverrides(@Nullable UserOverrides guestUsers) {
            guestUserOverrides = guestUsers;
            return this;
        }

        /**
         * Sets the permission to sign-in method map.
         *
         * @param permissionMethods The list of permission to sign-in methods.
         * @return The builder instance.
         * @since API 9
         */
        public ProxyConfiguration.Builder setPermissionToSignInMethodMap(@Nullable List<PermissionToSignInMethod> permissionMethods) {
            permissionToSignInMethodMap = permissionMethods;
            return this;
        }

        /**
         * Builds and returns the ProxyConfiguration instance.
         *
         * @return The ProxyConfiguration instance.
         * @throws CapabilitiesExceededException if the capabilities are exceeded.
         * @since API 9
         */
        @NonNull
        public ProxyConfiguration build() throws CapabilitiesExceededException {
            if (!isValidUUID(defaultSignInMethod)) {
                throw new CapabilitiesExceededException("DefaultSignInMethod value is not valid");
            }
            return new ProxyConfiguration(this);
        }

        private boolean isValidUUID(String uuid) {
            if (TextUtils.isEmpty(uuid)) {
                return false;
            }
            try {
                UUID.fromString(uuid);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private ProxyConfiguration(Parcel in) {
        this.guestPermissionSet = new ArrayList<>();
        in.readStringList(guestPermissionSet);
        addNewPermissionToGuestPermissionSet = in.readByte() != 0;
        guestUserOverrides = in.readParcelable(UserOverrides.class.getClassLoader());
        this.permissionToSignInMethodMap = new ArrayList<>();
        in.readTypedList(permissionToSignInMethodMap, PermissionToSignInMethod.CREATOR);
        defaultSignInMethod = in.readString();
        enableSignInChoice = in.readByte() != 0;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(guestPermissionSet);
        dest.writeByte((byte) (addNewPermissionToGuestPermissionSet ? 1 : 0));
        dest.writeParcelable(guestUserOverrides, flags);
        dest.writeTypedList(permissionToSignInMethodMap);
        dest.writeString(defaultSignInMethod);
        dest.writeByte((byte) (enableSignInChoice ? 1 : 0));
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
    public static final Creator<ProxyConfiguration> CREATOR = new Creator<ProxyConfiguration>() {
        @Override
        public ProxyConfiguration createFromParcel(Parcel in) {
            return new ProxyConfiguration(in);
        }

        @Override
        public ProxyConfiguration[] newArray(int size) {
            return new ProxyConfiguration[size];
        }
    };

    /**
     * Returns the guest permission set.
     *
     * @return The guest permission set.
     * @since API 9
     */
    public List<String> getGuestPermissionSet() {
        return guestPermissionSet;
    }

    /**
     * Returns whether new permissions are automatically added to the guest permission set.
     *
     * @return True if new permissions are automatically added, false otherwise.
     * @since API 9
     */
    public boolean isAddNewPermissionToGuestPermissionSet() {
        return addNewPermissionToGuestPermissionSet;
    }

    /**
     * Returns the guest user overrides.
     *
     * @return The guest user overrides.
     * @since API 9
     */
    public UserOverrides getGuestUserOverrides() {
        return guestUserOverrides;
    }

    /**
     * Returns the permission to sign-in method map.
     *
     * @return The permission to sign-in method map.
     * @since API 9
     */
    public List<PermissionToSignInMethod> getPermissionToSignInMethodMap() {
        return permissionToSignInMethodMap;
    }

    /**
     * Returns the default sign-in method.
     *
     * @return The default sign-in method.
     * @since API 9
     */
    public String getDefaultSignInMethod() {
        return defaultSignInMethod;
    }

    /**
     * Returns whether sign-in choice is enabled.
     *
     * @return True if sign-in choice is enabled, false otherwise.
     * @since API 9
     */
    public boolean isEnableSignInChoice() {
        return enableSignInChoice;
    }

    @Override
    public String toString() {
        return "ProxyConfiguration{" +
                "guestPermissionSet=" + guestPermissionSet +
                ", addNewPermissionToGuestPermissionSet=" + addNewPermissionToGuestPermissionSet +
                ", guestUserOverrides=" + guestUserOverrides +
                ", permissionToSignInMethodMap=" + permissionToSignInMethodMap +
                ", defaultSignInMethod='" + defaultSignInMethod + '\'' +
                ", enableSignInChoice=" + enableSignInChoice +
                '}';
    }
}
