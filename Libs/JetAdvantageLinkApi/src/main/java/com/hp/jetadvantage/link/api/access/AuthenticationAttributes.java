// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the authentication attributes to be filled with user details for requesting sign in.
 *
 * @since API 2
 */
public final class AuthenticationAttributes implements Parcelable {
    /**
     * <p>A collection of authentication attributes type.
     * Each authentication attribute type should be built with different
     * {@link AuthenticationAttributesBuilder AuthenticationAttributesBuilder}
     * such as {@link WindowsBuilder WindowsBuilder} and {@link WindowsSmartCardBuilder WindowsSmartCardBuilder}.
     * </p>
     *
     * @since API 2
     */
    @DeviceApi
    public enum AuthenticationType {
        /**
         * <p>WINDOWS : NTLM, Windows Negotiated, and Kerberos.
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName, domain
         * May provide values for other AuthenticatedUserInfo fields.
         * </p>
         *
         * @since API 2
         */
        WINDOWS,

        /**
         * <p>WINDOWS_SMART_CARD : Smart Card Integrated with Windows Kerberos.
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName, domain
         * May provide values for other AuthenticatedUserInfo fields.
         * Will not provide a password.</p>
         *
         * @since API 2
         */
        WINDOWS_SMART_CARD,

        /**
         * <p>NOVELL : Novell Directory Services.
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName, ndsTreeName, ndsContext
         * The fullyQualifiedUserName will be of the form: '[TREE]\[Context]\[username]'
         * May provide values for other AuthenticatedUserInfo fields.</p>
         *
         * @since API 2
         */
        NOVELL,

        /**
         * <p>LDAP : Lightweight Directory Access Protocol (to Active Directory).
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName, domain, ldapBindUser
         * The LDAP server hostname (or IP address if hostname is not known) will be used for both the DOMAIN part of the fullyQualifiedUserName value and the domain value.
         * May provide values for other AuthenticatedUserInfo fields.</p>
         *
         * @since API 2
         */
        LDAP,

        /**
         * <p>PIN : Personal Identification Number (numeric or alphanumeric).
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName
         * May provide values for other AuthenticatedUserInfo fields.
         * </p>
         *
         * @since API 2
         */
        PIN,

        /**
         * <p>OTHER : An authentication type not represented by another value in this enumeration.
         * Must provide AuthenticatedUserInfo values for: userName, fullyQualifiedUserName
         * May provide values for other AuthenticatedUserInfo fields.
         * </p>
         *
         * @since API 2
         */
        OTHER
    }

    private final int mVersion;
    private final AuthenticationType mAuthenticationType;
    private final String mFullyQualifiedName;
    private final String mUserName;
    private final String mSidString;
    private final String mDisplayName;
    private final String mLdapBindUser;
    private final String mUserPrincipalName;
    private final String mSAMAccountName;
    private final String mHomeFolderPath;
    private final String mUserDomain;
    private final String mNdsContext;
    private final String mNdsTreeName;
    private final String mUserEmail;
    private final String mExchangeMailboxUri;
    private final String mPassword;
    private final Map<String, String> mUserProperties;
    private final UserPreferencesAttributes mUserPreferencesAttributes;
    private final UserOverridesAttributes mUserOverridesAttributes;

    private AuthenticationAttributes(final AuthenticationAttributesBuilder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mAuthenticationType = builder.mAuthenticationType;
        mFullyQualifiedName = builder.mFullyQualifiedName;
        mUserName = builder.mUserName;
        mSidString = builder.mSidString;
        mDisplayName = builder.mDisplayName;
        mUserPrincipalName = builder.mUserPrincipalName;
        mSAMAccountName = builder.mSAMAccountName;
        mHomeFolderPath = builder.mHomeFolderPath;
        mUserDomain = builder.mUserDomain;
        mUserEmail = builder.mUserEmail;
        mExchangeMailboxUri = builder.mExchangeMailboxUri;
        mPassword = builder.mPassword;
        mUserProperties = builder.mUserProperties;
        mUserPreferencesAttributes = builder.mUserPreferencesAttributes;
        mUserOverridesAttributes = builder.mUserOverridesAttributes;

        if (builder instanceof NovellBuilder) {
            NovellBuilder novellBuilder = (NovellBuilder) builder;
            mNdsContext = novellBuilder.mNdsContext;
            mNdsTreeName = novellBuilder.mNdsTreeName;
        } else {
            mNdsContext = null;
            mNdsTreeName = null;
        }

        if (builder instanceof LdapBuilder) {
            LdapBuilder ldapBuilder = (LdapBuilder) builder;
            mLdapBindUser = ldapBuilder.mLdapBindUser;
        } else {
            mLdapBindUser = null;
        }
    }

    @SuppressLint("RestrictedApi")
    private AuthenticationAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mAuthenticationType = (AuthenticationType) in.readSerializable();
        mFullyQualifiedName = in.readString();
        mUserName = in.readString();
        mSidString = in.readString();
        mDisplayName = in.readString();
        mLdapBindUser = in.readString();
        mUserPrincipalName = in.readString();
        mSAMAccountName = in.readString();
        mHomeFolderPath = in.readString();
        mUserDomain = in.readString();
        mNdsContext = in.readString();
        mNdsTreeName = in.readString();
        mUserEmail = in.readString();
        mExchangeMailboxUri = in.readString();
        mPassword = in.readString();
        mUserProperties = in.readHashMap(String.class.getClassLoader());
        mUserPreferencesAttributes = in.readParcelable(UserPreferencesAttributes.class.getClassLoader());
        mUserOverridesAttributes = in.readParcelable(UserOverridesAttributes.class.getClassLoader());
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
    public AuthenticationType getAuthenticationType() {
        return mAuthenticationType;
    }

    /**
     * @hide for internal use
     */
    public String getFullyQualifiedName() {
        return mFullyQualifiedName;
    }

    /**
     * @hide for internal use
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * @hide for internal use
     */
    public String getSidString() {
        return mSidString;
    }

    /**
     * @hide for internal use
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * @hide for internal use
     */
    public String getLdapBindUser() {
        return mLdapBindUser;
    }

    /**
     * @hide for internal use
     */
    public String getUserPrincipalName() {
        return mUserPrincipalName;
    }

    /**
     * @hide for internal use
     */
    public String getSAMAccountName() {
        return mSAMAccountName;
    }

    /**
     * @hide for internal use
     */
    public String getHomeFolderPath() {
        return mHomeFolderPath;
    }

    /**
     * @hide for internal use
     */
    public String getUserDomain() {
        return mUserDomain;
    }

    /**
     * @hide for internal use
     */
    public String getNdsContext() {
        return mNdsContext;
    }

    /**
     * @hide for internal use
     */
    public String getNdsTreeName() {
        return mNdsTreeName;
    }

    /**
     * @hide for internal use
     */
    public String getExchangeMailboxUri() {
        return mExchangeMailboxUri;
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
    public Map<String, String> getUserProperties() {
        return mUserProperties;
    }

    /**
     * @hide for internal use
     */
    public UserPreferencesAttributes getUserPreferencesAttributes() {
        return mUserPreferencesAttributes;
    }

    /**
     * @hide for internal use
     */
    public UserOverridesAttributes getUserOverridesAttributes() {
        return mUserOverridesAttributes;
    }

    /**
     * @hide for internal use
     */
    public String getUserEmail() {
        return mUserEmail;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Sdk.VERSION.LEVEL);
        dest.writeSerializable(mAuthenticationType);
        dest.writeString(mFullyQualifiedName);
        dest.writeString(mUserName);
        dest.writeString(mSidString);
        dest.writeString(mDisplayName);
        dest.writeString(mLdapBindUser);
        dest.writeString(mUserPrincipalName);
        dest.writeString(mSAMAccountName);
        dest.writeString(mHomeFolderPath);
        dest.writeString(mUserDomain);
        dest.writeString(mNdsContext);
        dest.writeString(mNdsTreeName);
        dest.writeString(mUserEmail);
        dest.writeString(mExchangeMailboxUri);
        dest.writeString(mPassword);
        dest.writeMap(mUserProperties);
        dest.writeParcelable(mUserPreferencesAttributes, 0);
        dest.writeParcelable(mUserOverridesAttributes, 0);
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
    public static final Parcelable.Creator<AuthenticationAttributes> CREATOR = new Parcelable.Creator<AuthenticationAttributes>() {
        @Override
        public AuthenticationAttributes createFromParcel(final Parcel in) {
            return new AuthenticationAttributes(in);
        }

        @Override
        public AuthenticationAttributes[] newArray(final int size) {
            return new AuthenticationAttributes[size];
        }
    };

    /**
     * <p>Base builder of {@link AuthenticationAttributes AuthenticationAttributes} which contain mandatory parameters
     * for building authentication information.
     * Implementations of this class provide different authentication information of a particular type.</p>
     *
     * @since API 2
     */
    @DeviceApi
    public static abstract class AuthenticationAttributesBuilder<T extends AuthenticationAttributesBuilder<T>> {
        AuthenticationType mAuthenticationType;

        String mFullyQualifiedName;
        String mUserName;
        String mSidString;
        String mDisplayName;
        String mUserPrincipalName;
        String mSAMAccountName;
        String mHomeFolderPath;
        String mUserDomain;
        String mUserEmail;
        String mExchangeMailboxUri;
        String mPassword;
        Map<String, String> mUserProperties = new HashMap<>();
        UserPreferencesAttributes mUserPreferencesAttributes;
        UserOverridesAttributes mUserOverridesAttributes;


        /**
         * Sets fully qualified name.
         *
         * @param fullyQualifiedName fully qualified name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setFullyQualifiedName(@Nullable final String fullyQualifiedName) {
            mFullyQualifiedName = fullyQualifiedName;
            return (T) this;
        }

        /**
         * Sets user name.
         *
         * @param userName user name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserName(@Nullable final String userName) {
            mUserName = userName;
            return (T) this;
        }

        /**
         * Sets the text format (SDDL) of the user's Microsoft Security Descriptor.
         *
         * @param sidString security descriptor
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setSidString(@Nullable final String sidString) {
            mSidString = sidString;
            return (T) this;
        }

        /**
         * Sets user name for display purposes.
         *
         * @param displayName user display name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setDisplayName(@Nullable final String displayName) {
            mDisplayName = displayName;
            return (T) this;
        }

        /**
         * Sets user principal name.
         *
         * @param userPrincipalName user principal name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserPrincipalName(@Nullable final String userPrincipalName) {
            mUserPrincipalName = userPrincipalName;
            return (T) this;
        }

        /**
         * Sets sAMAccountName.
         *
         * @param sAMAccountName account name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setSAMAccountName(@Nullable final String sAMAccountName) {
            mSAMAccountName = sAMAccountName;
            return (T) this;
        }

        /**
         * Sets user home folder path.
         *
         * @param homeFolderPath folder path
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setHomeFolderPath(@Nullable final String homeFolderPath) {
            mHomeFolderPath = homeFolderPath;
            return (T) this;
        }

        /**
         * Sets user domain.
         *
         * @param userDomain domain
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserDomain(@Nullable final String userDomain) {
            mUserDomain = userDomain;
            return (T) this;
        }

        /**
         * Sets user email.
         *
         * @param userEmail user email
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserEmail(@Nullable final String userEmail) {
            mUserEmail = userEmail;
            return (T) this;
        }

        /**
         * Sets Exchange Mailbox URI.
         *
         * @param exchangeMailboxUri exchange mailbox uri
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setExchangeMailboxUri(@Nullable final String exchangeMailboxUri) {
            mExchangeMailboxUri = exchangeMailboxUri;
            return (T) this;
        }

        /**
         * <p>Sets user's password (or PIN) as gathered by the authentication process (min length=1, max length=128, may be null). May be used by workflow applications to impersonate
         * the user (only if configured/allowed by the administrator).</p>
         *
         * @param password password
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setPassword(@Nullable final String password) {
            mPassword = password;
            return (T) this;
        }

        /**
         * Sets the custom user property.
         *
         * @param name property name. The key (or name) of the item (min length=1, max length=512).
         * @param value property value. The value of the item as a string (min length=1, max length=512, may be null).
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T addUserProperty(@NonNull final String name, @Nullable final String value) {
            mUserProperties.put(name,value);
            return (T) this;
        }

        /**
         * Sets user preferences applied after sign in successfully.
         *
         * @param userPreferencesAttributes user preferences attributes
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserPreferencesAttributes(@Nullable final UserPreferencesAttributes userPreferencesAttributes) {
            mUserPreferencesAttributes = userPreferencesAttributes;
            return (T) this;
        }

        /**
         * Sets user overrides of default device settings applied after sign in successfully.
         *
         * @param userOverridesAttributes user overrides attributes
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public T setUserOverridesAttributes(@Nullable final UserOverridesAttributes userOverridesAttributes) {
            mUserOverridesAttributes = userOverridesAttributes;
            return (T) this;
        }

        /**
         * Combines all of the attributes in this into a {@link AuthenticationAttributes AuthenticationAttributes} object.
         *
         * @return AuthenticationAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public AuthenticationAttributes build() throws CapabilitiesExceededException {
            return new AuthenticationAttributes(this);
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of Windows type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class WindowsBuilder extends AuthenticationAttributesBuilder<WindowsBuilder> {
        /**
         * Default constructor to create a new Windows Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public WindowsBuilder() {
            this.mAuthenticationType = AuthenticationType.WINDOWS;
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of Windows Smart Card type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class WindowsSmartCardBuilder extends AuthenticationAttributesBuilder<WindowsSmartCardBuilder> {
        /**
         * Default constructor to create a new Smart Card Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public WindowsSmartCardBuilder() {
            this.mAuthenticationType = AuthenticationType.WINDOWS_SMART_CARD;
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of Novell Directory Service type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class NovellBuilder extends AuthenticationAttributesBuilder<NovellBuilder> {
        String mNdsContext;
        String mNdsTreeName;

        /**
         * Default constructor to create a new Novell Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public NovellBuilder() {
            this.mAuthenticationType = AuthenticationType.NOVELL;
        }

        /**
         * Sets NDS context.
         *
         * @param ndsContext domain
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public NovellBuilder setNdsContext(@Nullable final String ndsContext) {
            mNdsContext = ndsContext;
            return this;
        }

        /**
         * Sets NDS tree.
         *
         * @param ndsTreeName domain
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public NovellBuilder setNdsTreeName(@Nullable final String ndsTreeName) {
            mNdsTreeName = ndsTreeName;
            return this;
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of LDAP type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class LdapBuilder extends AuthenticationAttributesBuilder<LdapBuilder> {
        String mLdapBindUser;

        /**
         * Default constructor to create a new LDAP Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public LdapBuilder() {
            this.mAuthenticationType = AuthenticationType.LDAP;
        }

        /**
         * Sets LDAP Bind user.
         *
         * @param ldapBindUser domain
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public LdapBuilder setLdapBindUser(@Nullable final String ldapBindUser) {
            mLdapBindUser = ldapBindUser;
            return this;
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of Pin type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class PinBuilder extends AuthenticationAttributesBuilder<PinBuilder> {
        /**
         * Default constructor to create a new Pin Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public PinBuilder() {
            this.mAuthenticationType = AuthenticationType.PIN;
        }
    }

    /**
     * The Builder for creating {@link AuthenticationAttributes AuthenticationAttributes} of Other type to request sign in.
     *
     * @since API 2
     */
    @DeviceApi
    @SuppressWarnings("unused")
    public static class OtherBuilder extends AuthenticationAttributesBuilder<OtherBuilder> {
        /**
         * Default constructor to create a new Other Builder.<br>
         *
         * @since API 2
         */
        @SuppressWarnings({"unused"})
        public OtherBuilder() {
            this.mAuthenticationType = AuthenticationType.OTHER;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[").append("AuthenticationType=").append(((mAuthenticationType != null)?mAuthenticationType.name().toString():"null")).append("]").toString();
    }
}
