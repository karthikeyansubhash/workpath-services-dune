// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;

/**
 * The sets of credentials attributes related with authentication parameters to get a file for printing by device over network.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 1
 */
@SuppressWarnings("WeakerAccess")
public class NetworkCredentialsAttributes implements Parcelable {
    @Keep
    final String mUsername;
    @Keep
    final String mPassword;
    @Keep
    final String mCookie;

    private NetworkCredentialsAttributes(final Builder builder) {
        mUsername = builder.mUsername;
        mPassword = builder.mPassword;
        mCookie = builder.mCookie;
    }

    private NetworkCredentialsAttributes(Parcel in) {
        mUsername = in.readString();
        mPassword = in.readString();
        mCookie = in.readString();
    }

    /**
     * @hide for internal use
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * @hide for internal use
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * @hide for internal use
     */
    public String getCookie() {
        return mCookie;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUsername);
        dest.writeString(mPassword);
        dest.writeString(mCookie);
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
    public static final Creator<NetworkCredentialsAttributes> CREATOR = new Creator<NetworkCredentialsAttributes>() {
        @Override
        public NetworkCredentialsAttributes createFromParcel(Parcel in) {
            return new NetworkCredentialsAttributes(in);
        }

        @Override
        public NetworkCredentialsAttributes[] newArray(int size) {
            return new NetworkCredentialsAttributes[size];
        }
    };

    /**
     * Builder for creating {@link NetworkCredentialsAttributes} containing credentials.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public static class Builder {
        private static final int MAXIMUM_USERNAME = 128;
        private static final int MAXIMUM_PASSWORD = 128;
        private static final int MAXIMUM_COOKIE = 4092;

        String mUsername;
        String mPassword;
        String mCookie;

        /**
         * Default constructor to create a new Builder with default attributes.
         *
         * @since API 1
         */
        public Builder() {
        }

        /**
         * Sets username for URI authentication.<br>
         * If set the value must be non-empty and not exceed 128 characters length
         * and password value must be set as well.
         *
         * @param username Username for URI authentication.
         * @return this builder for method chaining.
         * @throws NullPointerException if username is null
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public Builder setUserName(@NonNull final String username) {
            mUsername = Preconditions.checkNotNull(username);
            return this;
        }

        /**
         * Sets password for URI authentication.<br>
         * If set the value must not exceed 128 characters length
         * and username value must be set as well.
         *
         * @param password Password for URI authentication.
         * @return this builder for method chaining.
         * @throws NullPointerException if password is null
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        public Builder setPassword(@NonNull final String password) {
            mPassword = Preconditions.checkNotNull(password);
            return this;
        }

        /**
         * Sets cookie for URI authentication, it will be added to a request.
         *
         * @param cookie Cookie for URI authentication.
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        public Builder setCookie(final String cookie) {
            mCookie = cookie;
            return this;
        }

        /**
         * Combines all of the attributes into a {@link NetworkCredentialsAttributes} object.
         *
         * @return NetworkCredentialsAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 1
         */
        @SuppressWarnings({"unused"})
        public NetworkCredentialsAttributes build() throws CapabilitiesExceededException {
            if (mUsername != null) {
                if (mUsername.isEmpty()) {
                    throw new CapabilitiesExceededException("Username value must be a non-empty string");
                } else if (mUsername.length() > MAXIMUM_USERNAME) {
                    throw new CapabilitiesExceededException(
                            "Username value length must not exceed " + MAXIMUM_USERNAME + " characters");
                } else if (mPassword == null || mPassword.isEmpty()) {
                    throw new CapabilitiesExceededException("If Username value is set, Password value must be also set");
                }
            }

            if (mPassword != null) {
                if (mPassword.length() > MAXIMUM_PASSWORD) {
                    throw new CapabilitiesExceededException(
                            "Password value length must not exceed " + MAXIMUM_PASSWORD + " characters");
                } else if (mUsername == null || mUsername.isEmpty()) {
                    throw new CapabilitiesExceededException("If Password value is set, Username value must be also set");
                }
            }

            if (mCookie != null && mCookie.length() > MAXIMUM_COOKIE) {
                throw new CapabilitiesExceededException("Cookie value length must not exceed " + MAXIMUM_COOKIE + " characters");
            }

            return new NetworkCredentialsAttributes(this);
        }
    }
}
