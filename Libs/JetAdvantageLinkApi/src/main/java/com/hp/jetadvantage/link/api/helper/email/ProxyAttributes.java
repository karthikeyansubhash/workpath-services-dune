// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.helper.email;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

import java.util.Locale;

/**
 * The sets of attributes to update proxy settings.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 1
 */
@SuppressWarnings("WeakerAccess")
public class ProxyAttributes implements Parcelable {
    /**
     * Enumeration for setting configuration mode of proxy
     *
     * @since API 1
     */
    @DeviceApi
    public enum ProxyConfigurationMode {
        /**
         * No proxy
         *
         * @since API 1
         */
        NONE,

        /**
         * Use system proxy settings defined on a device
         *
         * @since API 1
         */
        SYSTEM,

        /**
         * Use provided proxy settings
         *
         * @since API 1
         */
        CUSTOM
    }

    @DeviceApi
    final int mVersion;
    @DeviceApi
    final ProxyConfigurationMode mProxyConfigurationMode;
    @DeviceApi
    final String mHost;
    @DeviceApi
    final int mPort;

    private ProxyAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mProxyConfigurationMode = builder.mProxyConfigurationMode;
        mHost = builder.mHost;
        mPort = builder.mPort;
    }

    @SuppressLint("RestrictedApi")
    private ProxyAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mProxyConfigurationMode = (ProxyConfigurationMode) in.readSerializable();
        mHost = in.readString();
        mPort = in.readInt();
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public ProxyConfigurationMode getProxyConfigurationMode() {
        return mProxyConfigurationMode;
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
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeSerializable(mProxyConfigurationMode);
        dest.writeString(mHost);
        dest.writeInt(mPort);
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
    public static final Creator<ProxyAttributes> CREATOR = new Creator<ProxyAttributes>() {
        @Override
        public ProxyAttributes createFromParcel(Parcel in) {
            return new ProxyAttributes(in);
        }

        @Override
        public ProxyAttributes[] newArray(int size) {
            return new ProxyAttributes[size];
        }
    };

    /**
     * Builder for creating {@link ProxyAttributes} containing proxy parameters.
     *
     * @since API 1
     */
    @DeviceApi
    public static class Builder {
        private static final int MINIMUM_PORT = 1;
        private static final int MAXIMUM_PORT = 65535;

        ProxyConfigurationMode mProxyConfigurationMode = ProxyConfigurationMode.NONE;
        String mHost;
        int mPort = 80;

        /**
         * Sets the configuration mode for proxy
         * @param proxyConfigurationMode proxy configuration mode
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setProxyConfigurationMode(@Nullable final ProxyConfigurationMode proxyConfigurationMode) {
            mProxyConfigurationMode = Preconditions.checkNotNull(proxyConfigurationMode);
            return this;
        }

        /**
         * Sets Proxy host<br>
         * @param host proxy host value
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setHost(@NonNull final String host) {
            mHost = Preconditions.checkNotNull(host);
            return this;
        }

        /**
         * Sets Proxy port<br>
         * Default value is 80.<br>
         * Range: 1 ~ 65535.<br>
         * @param port proxy port value
         * @return this builder for method chaining
         * @since API 1
         */
        @SuppressWarnings({"unused", "unchecked"})
        @NonNull
        public Builder setPort(@IntRange(from=MINIMUM_PORT, to=MAXIMUM_PORT) final int port) {
            mPort = port;
            return this;
        }

        /**
         * <p>Builds ProxyAttributes. All of the attributes combine into a {@link ProxyAttributes} object.</p>
         *
         * @return a ProxyAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public ProxyAttributes build() throws CapabilitiesExceededException {
            if (mProxyConfigurationMode == ProxyConfigurationMode.CUSTOM) {
                if (TextUtils.isEmpty(mHost)) {
                    throw new CapabilitiesExceededException("Host value is not valid");
                }

                if (mPort < MINIMUM_PORT || mPort > MAXIMUM_PORT) {
                    throw new CapabilitiesExceededException(
                            String.format(Locale.US, "Port value must be in range [%d, %d] seconds",
                                    MINIMUM_PORT, MAXIMUM_PORT));
                }
            }

            return new ProxyAttributes(this);
        }
    }
}
