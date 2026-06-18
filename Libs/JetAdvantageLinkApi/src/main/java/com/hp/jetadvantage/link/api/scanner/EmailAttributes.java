// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.CommonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * The sets of attributes for an email to be sent.
 * An instance of this class is created using {@link Builder}.
 *
 * @since API 1
 */
@SuppressWarnings("WeakerAccess")
public final class EmailAttributes implements Parcelable {
    @Keep
    final int mVersion;
    @Keep
    final List<EmailAddressInfo> mToList;
    @Keep
    final List<EmailAddressInfo> mCcList;
    @Keep
    final List<EmailAddressInfo> mBccList;
    @Keep
    final EmailAddressInfo mFrom;
    @Keep
    final String mSubject;
    @Keep
    final String mMessage;

    private EmailAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mToList = builder.mToList;
        mCcList = builder.mCcList;
        mBccList = builder.mBccList;
        mFrom = builder.mFrom;
        mSubject = builder.mSubject;
        mMessage = builder.mMessage;
    }

    @SuppressLint("RestrictedApi")
    private EmailAttributes(Parcel in) {
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
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getTo() {
        return mToList;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getCc() {
        return mCcList;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getBcc() {
        return mBccList;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public EmailAddressInfo getFrom() {
        return mFrom;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public String getSubject() {
        return mSubject;
    }

    /**
     * @hide for internal use
     */
    @SuppressWarnings("unused")
    public String getMessage() {
        return mMessage;
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
    public static final Creator<EmailAttributes> CREATOR = new Creator<EmailAttributes>() {
        @Override
        public EmailAttributes createFromParcel(Parcel in) {
            return new EmailAttributes(in);
        }

        @Override
        public EmailAttributes[] newArray(int size) {
            return new EmailAttributes[size];
        }
    };

    /**
     * Builder for creating {@link EmailAttributes} containing email parameters.
     *
     * @since API 1
     */
    @Keep
    public static class Builder {
        List<EmailAddressInfo> mToList = new ArrayList<>();
        List<EmailAddressInfo> mCcList = new ArrayList<>();
        List<EmailAddressInfo> mBccList = new ArrayList<>();
        EmailAddressInfo mFrom;
        String mSubject;
        String mMessage;

        /**
         * Default constructor to create a new Builder with default attributes.
         *
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddress is null
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddresses is null
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddress is null
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddresses is null
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddress is null
         * @since API 1
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
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddresses is null
         * @since API 1
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
         * Sets "From" value to send an email
         * <p>This is an e-mail address in the form described by RFC 5322, section 3.4 (mailbox address),
         * except that such addresses may not contain new lines (but may contain comments).
         * Note that this means that an address may take either of the following forms:<br>
         * <ul><li>[display-name] &lt;addr-spec&gt;.</li></ul>
         * Examples: John Doe &lt;j.doe@acme.com&gt;.</p>
         *
         * @param emailAddress email address of the sender
         * @return this builder for method chaining.
         * @throws NullPointerException if emailAddress or name is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setFrom(@NonNull final String emailAddress, @Nullable final String name) {
            mFrom = new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name);
            return this;
        }

        /**
         * Sets "Subject" value to send an email.
         *
         * @param subject subject for email message
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setSubject(@Nullable final String subject) {
            mSubject = subject;
            return this;
        }

        /**
         * Sets message to send an email.
         *
         * @param message email message body text
         * @return this builder for method chaining.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setMessage(@Nullable final String message) {
            mMessage = message;
            return this;
        }

        /**
         * Combines all of the attributes into a {@link EmailAttributes} object.
         *
         * @return EmailAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public EmailAttributes build() throws CapabilitiesExceededException {
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

            return new EmailAttributes(this);
        }
    }
}
