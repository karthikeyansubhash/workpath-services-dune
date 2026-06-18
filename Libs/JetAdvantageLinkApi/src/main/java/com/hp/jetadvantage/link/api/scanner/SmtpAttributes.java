// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;

import java.util.Locale;

/**
 * The sets of attributes for establishing a connection to SMTP server.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 1
 */
@SuppressWarnings("WeakerAccess")
public class SmtpAttributes implements Parcelable {
    /**
     * Transport mode used to connect to SMTP server
     *
     * @since API 1
     */
    @Keep
    public enum TransportMode {
        /**
         * Plain text connection
         *
         * @since API 1
         */
        PLAIN,

        /**
         * Encrypted connection using SSL/TLS protocol over separate port (usually 465)
         *
         * @since API 1
         */
        SSL_TLS,

        /**
         * Upgrade a plain text connection to an encrypted (SMTP server must enable STARTTLS option)
         *
         * @since API 1
         */
        START_TLS
    }

    @Keep
    final int mVersion;
    @Keep
    final TransportMode mTransportMode;
    @Keep
    final String mHost;
    @Keep
    final int mPort;
    @Keep
    final int mConnectTimeout;
    @Keep
    final int mReadTimeout;
    @Keep
    final NetworkCredentialsAttributes mNetworkCredentialsAttributes;

    private SmtpAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mTransportMode = builder.mTransportMode;
        mHost = builder.mHost;
        mPort = builder.mPort;
        mConnectTimeout = builder.mConnectTimeout;
        mReadTimeout = builder.mReadTimeout;
        mNetworkCredentialsAttributes = builder.mNetworkCredentialsAttributes;
    }

    @SuppressLint("RestrictedApi")
    private SmtpAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mTransportMode = (TransportMode) in.readSerializable();
        mHost = in.readString();
        mPort = in.readInt();
        mConnectTimeout = in.readInt();
        mReadTimeout = in.readInt();
        mNetworkCredentialsAttributes = in.readParcelable(NetworkCredentialsAttributes.class.getClassLoader());
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public int getVersion() {
        return mVersion;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public TransportMode getTransportMode() {
        return mTransportMode;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public String getHost() {
        return mHost;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public int getPort() {
        return mPort;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public int getReadTimeout() {
        return mReadTimeout;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public NetworkCredentialsAttributes getServerCredentials() {
        return mNetworkCredentialsAttributes;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeSerializable(mTransportMode);
        dest.writeString(mHost);
        dest.writeInt(mPort);
        dest.writeInt(mConnectTimeout);
        dest.writeInt(mReadTimeout);
        dest.writeParcelable(mNetworkCredentialsAttributes, flags);
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
    public static final Creator<SmtpAttributes> CREATOR = new Creator<SmtpAttributes>() {
        @Override
        public SmtpAttributes createFromParcel(Parcel in) {
            return new SmtpAttributes(in);
        }

        @Override
        public SmtpAttributes[] newArray(int size) {
            return new SmtpAttributes[size];
        }
    };

    /**
     * Builder for creating {@link SmtpAttributes} containing SMTP server connection parameters.
     *
     * @since API 1
     */
    @Keep
    public static class Builder {
        private static final int MINIMUM_PORT = 1;
        private static final int MAXIMUM_PORT = 65535;
        private static final int MINIMUM_TIMEOUT = 1;
        private static final int MAXIMUM_TIMEOUT = 300;

        TransportMode mTransportMode = TransportMode.PLAIN;
        String mHost;
        int mPort = 25;
        int mConnectTimeout = 60;
        int mReadTimeout = 60;
        NetworkCredentialsAttributes mNetworkCredentialsAttributes;

        /**
         * Constructor to create a new Builder with default attributes:
         * <ul>
         *     <li>Security Mode = PLAIN</li>
         *     <li>SMTP port = 25</li>
         *     <li>Connect timeout = 60 seconds</li>
         *     <li>Read timeout = 60 seconds</li>
         * </ul>
         *
         * @since API 1
         */
        public Builder(final String host) {
            this.mHost = host;
        }

        /**
         * Sets security mode for establishing a connection to SMTP server.<br>
         * If security mode is changed to {@link TransportMode#SSL_TLS}
         * port must be also switched to SSL port using {@link #setPort(int)}.
         *
         * @param transportMode transport mode for SMTP connection
         * @return this builder for method chaining.
         * @throws NullPointerException if transportMode is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setTransportMode(@NonNull final TransportMode transportMode) {
            mTransportMode = Preconditions.checkNotNull(transportMode);
            return this;
        }

        /**
         * Sets SMTP port for establishing a connection to SMTP server<br>
         * Default value is 25.<br>
         * Range: 1 ~ 65535.<br>
         * @param port SMTP port value
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public Builder setPort(@IntRange(from=MINIMUM_PORT, to=MAXIMUM_PORT) final int port) {
            mPort = port;
            return this;
        }

        /**
         * Sets the socket connect timeout - how much time to wait before socket will be opened<br>
         * Default value is 60 seconds.<br>
         * Range: 1 ~ 300 seconds.<br>
         * @param connectTimeout value in seconds
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public Builder setConnectTimeout(@IntRange(from=MINIMUM_TIMEOUT, to=MAXIMUM_TIMEOUT) final int connectTimeout) {
            mConnectTimeout = connectTimeout;
            return this;
        }

        /**
         * Sets the socket read timeout - how much time to wait
         *      before data becomes available to read from socket
         * Default value is 60 seconds.<br>
         * Range: 1 ~ 300 seconds.<br>
         * @param readTimeout value in seconds
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public Builder setReadTimeout(@IntRange(from=MINIMUM_TIMEOUT, to=MAXIMUM_TIMEOUT) final int readTimeout) {
            mReadTimeout = readTimeout;
            return this;
        }

        /**
         * Sets the user credentials for authenticating on SMTP server
         * @param networkCredentialsAttributes user credentials
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public Builder setServerCredentials(@Nullable final NetworkCredentialsAttributes networkCredentialsAttributes) {
            mNetworkCredentialsAttributes = networkCredentialsAttributes;
            return this;
        }

        /**
         * Combines all of the attributes into a {@link SmtpAttributes} object.
         *
         * @return a SmtpAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public SmtpAttributes build() throws CapabilitiesExceededException {
            if (TextUtils.isEmpty(mHost)) {
                throw new CapabilitiesExceededException("Host value is not valid");
            }

            if (mPort < MINIMUM_PORT || mPort > MAXIMUM_PORT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "SMTP port value must be in range [%d, %d] seconds",
                                MINIMUM_PORT, MAXIMUM_PORT));
            }

            if (mConnectTimeout < MINIMUM_TIMEOUT || mConnectTimeout > MAXIMUM_TIMEOUT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Connect Timeout value must be in range [%d, %d] seconds",
                                MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }

            if (mReadTimeout < MINIMUM_TIMEOUT || mReadTimeout > MAXIMUM_TIMEOUT) {
                throw new CapabilitiesExceededException(
                        String.format(Locale.US, "Read Timeout value must be in range [%d, %d] seconds",
                                MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }

            return new SmtpAttributes(this);
        }
    }
}
