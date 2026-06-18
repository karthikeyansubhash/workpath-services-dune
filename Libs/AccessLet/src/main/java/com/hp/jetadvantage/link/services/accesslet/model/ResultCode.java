// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.model;

/** Result codes returned from AuthenticationData request */
public enum ResultCode {
    /** The authentication process was canceled */
    CANCELED("Canceled"),
    /** The authentication process is complete but failed. */
    FAILED("Failed"),
    /** The authentication process is complete and succeeded */
    SUCCEEDED("Succeeded"),
    /** The authentication process have to continue */
    CONTINUED("Continued"),
    /** The authentication process was canceled using home button*/
    HOME_CANCELED("HomeCanceled"),
    /** The authentication process was canceled using back button*/
    BACK_CANCELED("BackCanceled");

    /** Result code value */
    public final String mValue;

    /**
     * Constructor
     * @param value Enum value
     */
    ResultCode(String value) {
        mValue = value;
    }
}
