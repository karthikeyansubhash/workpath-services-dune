// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.model;

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
import com.hp.jetadvantage.link.services.attestationlet.ClientInfoInternal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Provides the attestation attributes to be filled with details for requesting application token.
 *
 * @since API 3
 */
public final class AttestationAttributes implements Parcelable {

    private final int mVersion;
    private final String mAppId;
    private final List<ClientInfoInternal> mClients;
    private final String mDebugUserName;
    private final String mLDBKey;

    private AttestationAttributes(final AttestationAttributesBuilder builder) {
        this.mVersion = Sdk.VERSION.LEVEL;

        this.mAppId = builder.mAppId;

        if (builder instanceof DebugBuilder) {
            DebugBuilder debugBuilder = (DebugBuilder) builder;
            this.mClients = Collections.unmodifiableList(((DebugBuilder) builder).clients);
            mDebugUserName = debugBuilder.mDebugUserName;
            mLDBKey = debugBuilder.mLDBKey;
        } else {
            mClients = null;
            mDebugUserName = null;
            mLDBKey = null;
        }
    }

    @SuppressLint("RestrictedApi")
    private AttestationAttributes(Parcel in) {
        this.mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        this.mAppId = in.readString();
        this.mClients = Collections.unmodifiableList(in.createTypedArrayList(ClientInfoInternal.CREATOR));
        this.mDebugUserName = in.readString();
        this.mLDBKey = in.readString();
    }

    /**
     * @hide for internal use
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * @hide for internal use
     */
    public String getAppId() {
        return mAppId;
    }

    /**
     * @hide for internal use
     */
    public List<ClientInfoInternal> getClients() { return mClients; }

    /**
     * @hide for internal use
     */
    public String getDebugUserName() {
        return mDebugUserName;
    }

    /**
     * @hide for internal use
     */
    public String getLDBKey() {
        return mLDBKey;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Sdk.VERSION.LEVEL);
        dest.writeString(mAppId);
        dest.writeTypedList(mClients);
        dest.writeString(mDebugUserName);
        dest.writeString(mLDBKey);
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
    public static final Creator<AttestationAttributes> CREATOR = new Creator<AttestationAttributes>() {
        @Override
        public AttestationAttributes createFromParcel(final Parcel in) {
            return new AttestationAttributes(in);
        }

        @Override
        public AttestationAttributes[] newArray(final int size) {
            return new AttestationAttributes[size];
        }
    };

    /**
     * <p>Base builder of {@link AttestationAttributes attestationAttributes} which contain mandatory parameters
     * for building application token information.</p>
     *
     * @since API 3
     */
    @DeviceApi
    public static abstract class AttestationAttributesBuilder<T extends AttestationAttributesBuilder<T>> {
        String mAppId;

        /**
         * Sets application id.
         *
         * @param appId application uuid
         * @return this builder for method chaining
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setAppId(@NonNull final String appId) {
            mAppId = appId;
            return (T) this;
        }


        /**
         * Combines all of the attributes in this into a {@link AttestationAttributes AuthenticationAttributes} object.
         *
         * @return AuthenticationAttributes object containing all of the attributes.AttestationAttributes$DebugBuilder
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public AttestationAttributes build() throws CapabilitiesExceededException {
            try {
                if (UUID.fromString(mAppId) instanceof UUID) { }
                else {
                    throw new IllegalArgumentException("Application id is not UUID format");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Application id is invalid value");
            }
//            if (mClients.size() > 256) throw new IllegalArgumentException("Clients exceeds maximum size");
            return new AttestationAttributes(this);
        }
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + mAppId.hashCode();
        result = result * prime + ((mClients != null) ? mClients.hashCode() : 0);
        result = result * prime + ((mDebugUserName != null) ? mDebugUserName.hashCode() : 0);
        result = result * prime + ((mLDBKey != null) ? mLDBKey.hashCode() : 0);
        return result;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AttestationAttributes)) return false;
        AttestationAttributes other = (AttestationAttributes)obj;
        return (TextUtils.equals(this.mAppId, other.mAppId));
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "AttestationInfo{" +
                "AppId=" + mAppId +
                '}';
    }

    /**
     * The Builder for creating {@link AttestationAttributes attestationAttributes} of Debug mode to request to get application token.
     *
     * @since API 3
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class DebugBuilder extends AttestationAttributesBuilder<DebugBuilder> {
        List<ClientInfoInternal> clients = new ArrayList<>();
        String mDebugUserName;
        String mLDBKey;

        /**
         * Default constructor to create a new Debug Builder.<br>
         *
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        public DebugBuilder() {}


        /**
         * Adds client information.
         *
         * @param client client information including client id and secret
         * @return this builder for method chaining
         * @since API 3
         */
        @SuppressWarnings("unused")
        @Nullable
        public DebugBuilder addClient(@Nullable final ClientInfoInternal client) {
            return (DebugBuilder) this.addClients(Collections.singleton(client));
        }

        /**
         * Adds clients.
         *
         * @param clients the list of client information
         * @return this builder for method chaining
         * @since API 3
         */
        @SuppressWarnings("unused")
        @Nullable
        public DebugBuilder addClients(@Nullable final Collection<ClientInfoInternal> clients) {
            if (clients != null) {
                for(ClientInfoInternal clientInfo : clients) {
                    if ((clientInfo != null) && !this.clients.contains(clientInfo)) {
                        this.clients.add(clientInfo);
                    }
                }
            }
            return (DebugBuilder) this;
        }

        /**
         * Sets HP IO User name.
         *
         * @param hpioUserName hp io user name
         * @return this builder for method chaining
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public DebugBuilder setDebugUserName(@Nullable final String hpioUserName) {
            mDebugUserName = hpioUserName;
            return this;
        }

        /**
         * Sets HP IO LDB Key.
         *
         * @param ldbKey LDB Key
         * @return this builder for method chaining
         * @since API 3
         */
        @SuppressWarnings("unused")
        @NonNull
        public DebugBuilder setLDBKey(@Nullable final String ldbKey) {
            mLDBKey = ldbKey;
            return this;
        }
    }
}
