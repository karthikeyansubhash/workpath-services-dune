// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.helper.email;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>The sets of attributes for sending email.
 * An instance of this class is created using {@link Builder}.</p>
 *
 * @since API 1
 */
@SuppressWarnings("WeakerAccess")
@DeviceApi
public final class EmailAttributes implements Parcelable {
    final int mVersion;
    final List<EmailAddressInfo> mToList;
    final List<EmailAddressInfo> mCcList;
    final List<EmailAddressInfo> mBccList;
    final EmailAddressInfo mFrom;
    final String mSubject;
    final String mMessage;
    final List<File> mAttachmentList;

    private EmailAttributes(final Builder builder) {
        mVersion = Sdk.VERSION.LEVEL;

        mToList = builder.mToList;
        mCcList = builder.mCcList;
        mBccList = builder.mBccList;
        mFrom = builder.mFrom;
        mSubject = builder.mSubject;
        mMessage = builder.mMessage;
        mAttachmentList = builder.mAttachmentList;
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
        mAttachmentList = new ArrayList<>();
        in.readList(mAttachmentList, File.class.getClassLoader());
    }

    /**
     * Returns "TO" recipients
     * @return List list of recipients
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getTo() {
        return mToList;
    }

    /**
     * Returns "CC" recipients
     * @return List list of recipients
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getCc() {
        return mCcList;
    }

    /**
     * Returns "BCC" recipients
     * @return List list of recipients
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<EmailAddressInfo> getBcc() {
        return mBccList;
    }

    /**
     * Returns email sender "FROM"
     * @return EmailAddressInfo sender
     * @since API 1
     */
    @SuppressWarnings("unused")
    public EmailAddressInfo getFrom() {
        return mFrom;
    }

    /**
     * Returns email subject
     * @return EmailAddressInfo subject
     * @since API 1
     */
    @SuppressWarnings("unused")
    public String getSubject() {
        return mSubject;
    }

    /**
     * Returns email body message
     * @return EmailAddressInfo message
     * @since API 1
     */
    @SuppressWarnings("unused")
    public String getMessage() {
        return mMessage;
    }

    /**
     * Returns email attachments
     * @return List list of {@link File}
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<File> getAttachments() {
        return mAttachmentList;
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
        dest.writeList(mAttachmentList);
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
    @DeviceApi
    public static class Builder {
        List<EmailAddressInfo> mToList = new ArrayList<>();
        List<EmailAddressInfo> mCcList = new ArrayList<>();
        List<EmailAddressInfo> mBccList = new ArrayList<>();
        EmailAddressInfo mFrom;
        String mSubject;
        String mMessage;
        List<File> mAttachmentList = new ArrayList<>();

        /**
         * Default constructor a new Builder with default attributes.
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
         * @return Builder builder with addToAddress.
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
         * @return Builder builder with addToAddresses.
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
         * @return Builder builder with addCcAddress.
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
         * @return Builder builder with addCcAddresses.
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
         * @return Builder builder with addBccAddress.
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
         * @return Builder builder with addBccAddresses.
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
         * Sets Subject for an email message.
         *
         * @param subject subject for email message
         * @return this builder for method chaining
         * @since API 1
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
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setMessage(@Nullable final String message) {
            mMessage = message;
            return this;
        }

        /**
         * Adds files as email attachments.
         *
         * @param files array of email attachment files
         * @return Builder builder with attachments.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder addAttachments(@NonNull final File... files) {
            mAttachmentList.addAll(Arrays.asList(files));
            return this;
        }

        /**
         * Combines all of the attributes in this into a {@link EmailAttributes} object.
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

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("From=").append(((mFrom != null && mFrom.getAddress() != null)?mFrom.getAddress().toString():"null")).append("]").toString();
    }
}
