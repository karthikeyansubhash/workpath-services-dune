// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.text.TextUtils;

/**
 * UIConfiguration user authentication failure. Missing or incorrect credentials
 */
@SuppressWarnings("WeakerAccess")
public class AuthenticationException extends Error {

    /** OXPd Sender fault code */
    private static final String AUTHENTICATION_FAULT_CODE = "Sender";
    /** OXPd user authentication failed sub-code */
    private static final String AUTHENTICATION_FAULT_SUB_CODE = "UserAuthenticationFailed";
    /** OXPd legacy user authentication failure */
    private static final String FAILED_AUTHENTICATION_SUB_CODE = "FailedAuthentication";
    /** OXPd user sign-in method */
    private static final String SIGN_IN_METHOD = "SignInMethod";

    @SuppressWarnings("WeakerAccess")
    public final SignInMethod mSignInMethod;

    /**
     * Sign-In method reported by the device
     */
    @SuppressWarnings("WeakerAccess")
    public enum SignInMethod {
        /** Local sign-in */
        LOCAL_DEVICE("Local Device"),
        /** Windows sign-in */
        WINDOWS("Windows"),
        /** LDAP sign-in */
        LDAP("LDAP"),
        /** Other sign-in */
        OTHER("Other");

        /** sign-in method value */
        private final String mValue;

        /**
         * Constructor
         * @param value sign-in value
         */
        SignInMethod(String value) {
            mValue = value;
        }

        /**
         * Convert string value to {@link SignInMethod} enum
         * @param value sign-in value
         * @return corresponding {@link SignInMethod} enum or {@link SignInMethod#OTHER} otherwise
         */
        private static SignInMethod fromValue(String value) {
            for(SignInMethod method : values()) {
                if (TextUtils.equals(value, method.mValue)) {
                    return method;
                }
            }
            return OTHER;
        }
    }

    /**
     * Constructor
     * @param error UIConfiguration error to build from
     */
    private AuthenticationException(Error error) {
        super(ErrorName.AuthError, error.soapFault);

        if (TextUtils.equals(error.soapFault.mFaultCode.mSubCode.mValue, FAILED_AUTHENTICATION_SUB_CODE)) {
            mSignInMethod = SignInMethod.LOCAL_DEVICE;
        } else {
            // get the fault reason
            String reason = error.soapFault.mReasons.get(0);
            // look for the sign-in method
            int signInMethod = reason.indexOf(SIGN_IN_METHOD);
            if (signInMethod >= 0) {
                reason = reason.substring(signInMethod);
            }
            String parts[] = reason.split("=");
            // convert the sign-in value
            mSignInMethod = SignInMethod.fromValue(parts[parts.length - 1]);
        }
    }

    /**
     * Inspect error to determine if it is an Authorization failure
     * @param error UIConfiguration error to check
     * @return true if Authentication failure, false otherwise
     */
    private static boolean isAuthenticationFailure(Error error) {
        return ((error instanceof AuthenticationException)
                || ((error != null)
                    && (error.soapFault != null)
                    && (error.soapFault.mFaultCode.mSubCode != null)
                    && TextUtils.equals(error.soapFault.mFaultCode.mValue, AUTHENTICATION_FAULT_CODE)
                    && (TextUtils.equals(error.soapFault.mFaultCode.mSubCode.mValue, AUTHENTICATION_FAULT_SUB_CODE)
                        || TextUtils.equals(error.soapFault.mFaultCode.mSubCode.mValue, FAILED_AUTHENTICATION_SUB_CODE))));
    }

    /**
     * Convert UIConfiguration Error to Authentication error if possible
     * @param error UIConfiguration error to convert
     * @return new {@link AuthenticationException} instance if error can be converted, or return error otherwise
     */
    public static Error convert(Error error) {
        return (isAuthenticationFailure(error)) ? new AuthenticationException(error) : error;
    }
}
