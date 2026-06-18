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
import com.hp.jetadvantage.link.common.utils.CommonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Holder for the user's overrides</p>
 *
 * @since API 2
 */
@SuppressWarnings("WeakerAccess")
public final class UserOverridesAttributes implements Parcelable {
    final int mVersion;
    final List<EmailAddressInfo> mToList;
    final List<EmailAddressInfo> mCcList;
    final List<EmailAddressInfo> mBccList;
    final EmailAddressInfo mFrom;
    final String mSubject;
    final String mMessage;

    final String mFaxCompanyName;
    final String mFaxBillingCode;

    private UserOverridesAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mToList = builder.mToList;
        mCcList = builder.mCcList;
        mBccList = builder.mBccList;
        mFrom = builder.mFrom;
        mSubject = builder.mSubject;
        mMessage = builder.mMessage;

        mFaxCompanyName = builder.mFaxCompanyName;
        mFaxBillingCode = builder.mFaxBillingCode;
    }

    @SuppressLint("RestrictedApi")
    private UserOverridesAttributes(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mToList = new ArrayList<>();
        in.readTypedList(mToList, EmailAddressInfo.CREATOR);
        mCcList = new ArrayList<>();
        in.readTypedList(mCcList, EmailAddressInfo.CREATOR);
        mBccList = new ArrayList<>();
        in.readTypedList(mBccList, EmailAddressInfo.CREATOR);
        mFrom = in.readParcelable(EmailAddressInfo.class.getClassLoader());
        mSubject = in.readString();
        mMessage = in.readString();

        mFaxCompanyName = in.readString();
        mFaxBillingCode = in.readString();
    }

    /**
     * @hide for internal use
     */
    public List<EmailAddressInfo> getTo() {
        return mToList;
    }

    /**
     * @hide for internal use
     */
    public List<EmailAddressInfo> getCc() {
        return mCcList;
    }

    /**
     * @hide for internal use
     */
    public List<EmailAddressInfo> getBcc() {
        return mBccList;
    }

    /**
     * @hide for internal use
     */
    public EmailAddressInfo getFrom() {
        return mFrom;
    }

    /**
     * @hide for internal use
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * @hide for internal use
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * @hide for internal use
     */
    public String getFaxCompanyName() {
        return mFaxCompanyName;
    }

    /**
     * @hide for internal use
     */
    public String getFaxBillingCode() {
        return mFaxBillingCode;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeTypedList(mToList);
        dest.writeTypedList(mCcList);
        dest.writeTypedList(mBccList);
        dest.writeParcelable(mFrom, 0);
        dest.writeString(mSubject);
        dest.writeString(mMessage);
        dest.writeString(mFaxCompanyName);
        dest.writeString(mFaxBillingCode);
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
    public static final Parcelable.Creator<UserOverridesAttributes> CREATOR = new Parcelable.Creator<UserOverridesAttributes>() {
        @Override
        public UserOverridesAttributes createFromParcel(final Parcel in) {
            return new UserOverridesAttributes(in);
        }

        @Override
        public UserOverridesAttributes[] newArray(final int size) {
            return new UserOverridesAttributes[size];
        }
    };

    /**
     * Builder for building user's override parameters.
     *
     * @since API 2
     */
    @DeviceApi
    public static class Builder {
        List<EmailAddressInfo> mToList = new ArrayList<>();
        List<EmailAddressInfo> mCcList = new ArrayList<>();
        List<EmailAddressInfo> mBccList = new ArrayList<>();
        EmailAddressInfo mFrom;
        String mSubject;
        String mMessage;

        String mFaxCompanyName;
        String mFaxBillingCode;

        /**
         * Default constructor a new Builder with default attributes.
         *
         * @since API 2
         */
        public Builder() {
        }

        /**
         * Adds "To" value to an email message. Includes optional "Name" parameter for recipient's display name.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddress email address of the recipient
         * @param name name of the recipient
         * @return Builder builder with addToAddress.
         * @throws NullPointerException if emailAddress is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addToAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mToList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Adds multiple "To" values to an email message.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddresses array of email addresses
         * @return Builder builder with addToAddresses.
         * @throws NullPointerException if emailAddresses is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addToAddresses(@NonNull String... emailAddresses) {
            for (String emailAddress : Preconditions.checkNotNull(emailAddresses)) {
                mToList.add(new EmailAddressInfo(emailAddress, null));
            }
            return this;
        }

        /**
         * Adds "Cc" value to an email message. Includes optional "Name" parameter for recipient's display name.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddress email address of the recipient
         * @param name name of the recipient
         * @return Builder builder with addCcAddress.
         * @throws NullPointerException if emailAddress is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addCcAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mCcList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Adds multiple "Cc" values to an email message.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddresses array of email addresses
         * @return Builder builder with addToAddresses.
         * @throws NullPointerException if emailAddresses is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addCcAddresses(@NonNull String... emailAddresses) {
            for (String emailAddress : Preconditions.checkNotNull(emailAddresses)) {
                mCcList.add(new EmailAddressInfo(emailAddress, null));
            }
            return this;
        }

        /**
         * Adds "Bcc" value to an email message. Includes optional "Name" parameter for recipient's display name.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddress email address of the recipient
         * @param name name of the recipient
         * @return Builder builder with addBccAddress.
         * @throws NullPointerException if emailAddress is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addBccAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mBccList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Adds multiple "Bcc" values to an email message.
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddresses array of email addresses
         * @return Builder builder with addToAddresses.
         * @throws NullPointerException if emailAddresses is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder addBccAddresses(@NonNull String... emailAddresses) {
            for (String emailAddress : Preconditions.checkNotNull(emailAddresses)) {
                mBccList.add(new EmailAddressInfo(emailAddress, null));
            }
            return this;
        }

        /**
         * Sets "From" for an email message
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddress email address of the sender
         * @param name sender name
         * @return this builder for method chaining
         * @throws NullPointerException if emailAddress or name is null
         * @since API 2
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setFrom(@NonNull final String emailAddress, @Nullable final String name) {
            mFrom = new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name);
            return this;
        }

        /**
         * Sets Subject for an email message.
         *
         * @param subject subject for email message
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setSubject(@Nullable final String subject) {
            mSubject = subject;
            return this;
        }

        /**
         * Sets text content for an email message.
         *
         * @param message email message body text
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setMessage(@Nullable final String message) {
            mMessage = message;
            return this;
        }

        /**
         * Sets fax company name (min length=1, max length=30, UTF-8, may be null).
         * <p> Will be used by the device to automatically populate the fax company name field, if the fax app allows the field to be edited. The setting will return to the guest
         * user default when the user session ends.
         * A null value indicates that the producer does not wish override the value, deferring to other sources.
         * This value cannot be cleared.</p>
         *
         * @param faxCompanyName fax company name
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setFaxCompanyName(@Nullable final String faxCompanyName) {
            mFaxCompanyName = faxCompanyName;
            return this;
        }

        /**
         * Sets fax billing code. (max length=16, UTF-8, may be null).
         * <p>The supplied value must conform to the device's minimum fax billing code length setting. Otherwise, if it is used the fax job will fail.
         * May be used by the device to automatically populate the fax billing code field, if the fax app allows the field to be edited. The setting will return to the guest
         * user default when the user session ends.
         * A null value indicates that the producer does not wish override the value, deferring to other sources.
         * A empty string effectively clears the value.</p>
         *
         * @param faxBillingCode fax billing code
         * @return this builder for method chaining
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setFaxBillingCode(@Nullable final String faxBillingCode) {
            mFaxBillingCode = faxBillingCode;
            return this;
        }

        /**
         * Combines all of the attributes in this into a {@link UserOverridesAttributes UserOverridesAttributes} object.
         *
         * @return User's overrides attributes containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 2
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverridesAttributes build() throws CapabilitiesExceededException {
            if (mFrom != null && !CommonUtility.isValidEmail(mFrom.getAddress())) {
                throw new CapabilitiesExceededException("Email FROM value is not valid");
            }

            if (mToList != null) {
                for (EmailAddressInfo emailAddressInfo : mToList) {
                    if (emailAddressInfo == null || !CommonUtility.isValidEmail(emailAddressInfo.getAddress())) {
                        throw new CapabilitiesExceededException("Email TO values are not valid");
                    }
                }
            }

            if (mCcList != null) {
                for (EmailAddressInfo emailAddressInfo : mCcList) {
                    if (emailAddressInfo == null || !CommonUtility.isValidEmail(emailAddressInfo.getAddress())) {
                        throw new CapabilitiesExceededException("Email CC values are not valid");
                    }
                }
            }

            if (mBccList != null) {
                for (EmailAddressInfo emailAddressInfo : mBccList) {
                    if (emailAddressInfo == null || !CommonUtility.isValidEmail(emailAddressInfo.getAddress())) {
                        throw new CapabilitiesExceededException("Email BCC values are not valid");
                    }
                }
            }

            return new UserOverridesAttributes(this);
        }
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("from=").append(((mFrom != null && mFrom.getAddress() != null)?mFrom.getName().toString():"null")).append("]").toString();
    }
}
