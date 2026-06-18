// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.text.TextUtils;

/**
 * UIConfiguration user authorization failure. Returned when a user it not allowed to use the app
 */
@SuppressWarnings("WeakerAccess")
public class AuthorizationException extends Error {

    /** OXPd Sender fault code */
    private static final String AUTHORIZATION_FAULT_CODE = "Sender";
    /** OXPd user authorization failed sub-code */
    private static final String AUTHORIZATION_FAULT_SUB_CODE = "UserAuthorizationFailed";

    /**
     * Constructor
     * @param error UIConfiguration error to build from
     */
    private AuthorizationException(Error error) {
        super(error.name, error.soapFault);
    }

    /**
     * Inspect error to determine if it is an Authorization failure
     * @param error UIConfiguration error to check
     * @return true if Authorization failure, false otherwise
     */
    private static boolean isAuthorizationFailure(Error error) {
        return ((error instanceof AuthorizationException)
                || ((error != null)
                && (error.soapFault != null)
                && (error.soapFault.mFaultCode.mSubCode != null)
                && TextUtils.equals(error.soapFault.mFaultCode.mValue, AUTHORIZATION_FAULT_CODE)
                && TextUtils.equals(error.soapFault.mFaultCode.mSubCode.mValue, AUTHORIZATION_FAULT_SUB_CODE)));
    }

    /**
     * Convert UIConfiguration Error to Authorization error if possible
     * @param error UIConfiguration error to convert
     * @return new {@link AuthorizationException} instance if error can be converted, or return error otherwise
     */
    public static Error convert(Error error) {
        return (isAuthorizationFailure(error)) ? new AuthorizationException(error) : error;
    }

}
