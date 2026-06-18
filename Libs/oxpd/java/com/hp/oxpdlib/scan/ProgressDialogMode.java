// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan progress dialog modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ProgressDialogMode {
    /** The device will not display built-in scan progress dialogs. The embedded browser will not be obscured while the device is scanning, allowing the web application to update and display browser content to the walk-up user. The web application is responsible for presenting scan progress information to the walk-up user, providing a soft button for canceling the job, etc. Scanner error dialogs (ADF paper jam, mispick, etc.) will obscure the embedded browser. When JobAssemblyMode is On, the built-in job segment dialog will be displayed at segment boundaries, obscuring the embedded browser. */
    Off("Off"),
    /** The device will display built-in scan progress dialogs. The embedded browser will be obscured by a built-in scan progress dialog while the device is scanning. The built-in dialog will present the user with a soft 'cancel' button, allowing the user to cancel the job. The browser content may be updated by the web application, but updates will not be visible to the walk-up user until scanning has completed. Scanner error dialogs (ADF paper jam, mispick, etc.) will obscure the embedded browser. When JobAssemblyMode is On, the built-in job segment dialog will be displayed at segment boundaries, obscuring the embedded browser. */
    On("On");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ProgressDialogMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    ProgressDialogMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ProgressDialogMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ProgressDialogMode enum or null if no match is found
     */
    static ProgressDialogMode fromAttributeValue(String value) {
        for(ProgressDialogMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
