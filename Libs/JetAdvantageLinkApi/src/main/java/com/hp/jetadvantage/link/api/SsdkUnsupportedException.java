// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api;

import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * <p>Thrown to indicate that a printer doesn't support SDK or a device has incompatible version of SDK.</p>
 *
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@CommonApi
public class SsdkUnsupportedException extends Exception {
    /**
     * <p>Indicates that SDK is not installed.</p>
     */
    public static final int LIBRARY_NOT_INSTALLED = 2;

    /**
     * <p>Indicates that SDK needs to be updated to compatible version which is using by an application.</p>
     */
    public static final int LIBRARY_UPDATE_IS_REQUIRED = 3;

    private int mErrorType = 0;

    /**
     * Constructs a <code>SsdkUnsupportedException</code> class with the specified detail message and error type.
     *
     * @param message   the detailed message.
     * @param errorType the error type. ( VENDOR_NOT_SUPPORTED, DEVICE_NOT_SUPPORTED, LIBRARY_NOT_INSTALLED, LIBRARY_UPDATE_IS_REQUIRED, LIBRARY_UPDATE_IS_RECOMMENDED
     *                  )
     * @hide The client should not need to construct this exception
     */
    public SsdkUnsupportedException(String message, int errorType) {
        super(message);
        mErrorType = errorType;
    }

    /**
     * <p>Returns enumeration of error type when SDK is not initialized.</p>
     *
     * @return Enumeration The error type.(LIBRARY_NOT_INSTALLED, LIBRARY_UPDATE_IS_REQUIRED)
     */
    public int getType() {
        return mErrorType;
    }
}
