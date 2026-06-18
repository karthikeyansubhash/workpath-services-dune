// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.CommonUtility;
import com.hp.jetadvantage.link.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserOverrides class holds the user's override settings for various fields such as email addresses, subject, message, and fax details.
 *
 * @since API 9
 */
@SuppressWarnings("WeakerAccess")
public final class UserOverrides implements Parcelable {
    final List<EmailAddressInfo> mToList;
    final List<EmailAddressInfo> mCcList;
    final List<EmailAddressInfo> mBccList;
    final EmailAddressInfo mFrom;
    final String mSubject;
    final String mMessage;

    final String mFaxCompanyName;
    final String mFaxBillingCode;

    /**
     * Constructs a UserOverrides instance with the specified builder.
     *
     * @param builder The builder to construct the UserOverrides.
     * @since API 9
     */
    private UserOverrides(final UserOverrides.Builder builder) {
        mToList = builder.mToList;
        mCcList = builder.mCcList;
        mBccList = builder.mBccList;
        mFrom = builder.mFrom;
        mSubject = builder.mSubject;
        mMessage = builder.mMessage;

        mFaxCompanyName = builder.mFaxCompanyName;
        mFaxBillingCode = builder.mFaxBillingCode;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private UserOverrides(Parcel in) {
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
    public static final Parcelable.Creator<UserOverrides> CREATOR = new Parcelable.Creator<UserOverrides>() {
        @Override
        public UserOverrides createFromParcel(final Parcel in) {
            return new UserOverrides(in);
        }

        @Override
        public UserOverrides[] newArray(final int size) {
            return new UserOverrides[size];
        }
    };

    /**
     * Builder class for UserOverrides.
     *
     * @since API 9
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
         * Default constructor for a new Builder with default attributes.
         *
         * @since API 9
         */
        public Builder() {
        }

        /**
         * Adds a "To" address to the email message.
         *
         * @param emailAddress The email address of the recipient.
         * @param name         The name of the recipient.
         * @return The builder instance.
         * @throws NullPointerException if emailAddress is null.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder addToAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mToList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Sets the "To" addresses for the email message.
         *
         * @param emailAddresses The list of email addresses.
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder setToAddresses(@Nullable List<EmailAddressInfo> emailAddresses) {
            mToList = emailAddresses;
            return this;
        }

        /**
         * Adds a "Cc" address to the email message.
         *
         * @param emailAddress The email address of the recipient.
         * @param name         The name of the recipient.
         * @return The builder instance.
         * @throws NullPointerException if emailAddress is null.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder addCcAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mCcList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Sets the "Cc" addresses for the email message.
         *
         * @param emailAddresses The list of email addresses.
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder setCcAddresses(@Nullable List<EmailAddressInfo> emailAddresses) {
            mCcList = emailAddresses;
            return this;
        }

        /**
         * Adds a "Bcc" address to the email message.
         *
         * @param emailAddress The email address of the recipient.
         * @param name         The name of the recipient.
         * @return The builder instance.
         * @throws NullPointerException if emailAddress is null.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder addBccAddress(@NonNull final String emailAddress, @Nullable final String name) {
            mBccList.add(new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name));
            return this;
        }

        /**
         * Sets the "Bcc" addresses for the email message.
         *
         * @param emailAddresses The list of email addresses.
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder setBccAddresses(@Nullable List<EmailAddressInfo> emailAddresses) {
            mBccList = emailAddresses;
            return this;
        }

        /**
         * Sets the "From" address for the email message.
         *
         * @param emailAddress The email address of the sender.
         * @param name         The name of the sender.
         * @return The builder instance.
         * @throws NullPointerException if emailAddress or name is null.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public UserOverrides.Builder setFrom(@NonNull final String emailAddress, @Nullable final String name) {
            mFrom = new EmailAddressInfo(Preconditions.checkNotNull(emailAddress), name);
            return this;
        }

        /**
         * Sets the subject for the email message.
         *
         * @param subject The subject of the email message.
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverrides.Builder setSubject(@Nullable final String subject) {
            mSubject = subject;
            return this;
        }

        /**
         * Sets the message body for the email message.
         *
         * @param message The message body of the email.
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverrides.Builder setMessage(@Nullable final String message) {
            mMessage = message;
            return this;
        }

        /**
         * Sets the fax company name.
         *
         * @param faxCompanyName The fax company name (min length=1, max length=30, UTF-8, may be null).
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverrides.Builder setFaxCompanyName(@Nullable final String faxCompanyName) {
            mFaxCompanyName = faxCompanyName;
            return this;
        }

        /**
         * Sets the fax billing code.
         *
         * @param faxBillingCode The fax billing code (max length=16, UTF-8, may be null).
         * @return The builder instance.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverrides.Builder setFaxBillingCode(@Nullable final String faxBillingCode) {
            mFaxBillingCode = faxBillingCode;
            return this;
        }

        /**
         * Combines all of the attributes in this builder into a UserOverrides object.
         *
         * @return The UserOverrides object containing all of the attributes.
         * @throws CapabilitiesExceededException if attributes are out of supported range.
         * @since API 9
         */
        @SuppressWarnings("unused")
        @NonNull
        public UserOverrides build() throws CapabilitiesExceededException {
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

            return new UserOverrides(this);
        }
    }

    /**
     * @hide for internal use
     */
    @Override
    public String toString() {
        return "UserOverrides{" +
                "mToList=" + mToList +
                ", mCcList=" + mCcList +
                ", mBccList=" + mBccList +
                ", mFrom=" + mFrom +
                ", mSubject='" + mSubject + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mFaxCompanyName='" + mFaxCompanyName + '\'' +
                ", mFaxBillingCode='" + mFaxBillingCode + '\'' +
                '}';
    }
}