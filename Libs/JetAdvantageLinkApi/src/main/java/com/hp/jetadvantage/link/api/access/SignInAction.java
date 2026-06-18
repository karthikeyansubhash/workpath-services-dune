// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.access;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides the sets of sign-in actions and failure message to complete authentication process.
 *
 * @since API 2
 */
@DeviceApi
public class SignInAction implements Parcelable {

    private Action action;
    private String failureMessage;

    /**
     * Constructor to build sign-in action and failure message(optional).
     *
     * @param action Sign-in action
     * @param failureMessage (optional) custom message to replace "authentication failed" message
     * @since API 2
     */
    public SignInAction(Action action, String failureMessage){
        this.action = action;
        this.failureMessage = failureMessage;
    }

    private SignInAction(Parcel in) {
        action = (Action) in.readSerializable();
        failureMessage = in.readString();
    }

    /**
     * <p>Gets sign-in action.</p>
     *
     * @return Action action sign-in action
     * @since API 2
     */
    public Action getAction() {
        return action;
    }

    /**
     * <p>Gets optional custom "authentication failed" message.</p>
     *
     * @return String failureMessage (min length=1, max length=1024, may be null, UTF-8)
     * @since API 2
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(action);
        dest.writeString(failureMessage);
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
    public static final Parcelable.Creator<SignInAction> CREATOR = new Parcelable.Creator<SignInAction>() {
        @Override
        public SignInAction createFromParcel(final Parcel in) {
            return new SignInAction(in);
        }

        @Override
        public SignInAction[] newArray(final int size) {
            return new SignInAction[size];
        }
    };

    /**
     * <p>A collection of sign-in types for completing authentication process.</p>
     *
     * @since API 2
     */
    @DeviceApi
    public enum Action {
        /**
         * The authentication process is complete and succeeded.
         *
         * @since API 2
         */
        SUCCESS,
        /**
         * <p>The authentication process is incomplete and requires user prompting.
         * An application will set this type by {@link AbstractAuthenticationService AbstractAuthenticationService} onPrePrompt() method.</p>
         *
         * @since API 2
         */
        CONTINUE,
        /**
         * <p>The authentication process is complete but failed. If failureMessage is not null, failureMessage will show.</p>
         *
         * @since API 2
         */
        FAIL,
        /**
         * The authentication process is canceled.
         * @Deprecated
         * @since API 2
         */
        CANCEL,
        /**
         * The authentication process is canceled and moved to home screen.
         *
         * @since API 8
         */
        HOME,
        /**
         * The authentication process is canceled and moved to back screen.
         *
         * @since API 8
         */
        BACK
    }
}
