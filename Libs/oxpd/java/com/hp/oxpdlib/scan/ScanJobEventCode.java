// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/**
 * Enumeration of scan job event code (job status changes that initiate scan job events).
 */
@SuppressWarnings({"WeakerAccess"})
public enum ScanJobEventCode {
    /** Scanning started */
    SCANNING_STARTED("ScanningStarted"),
    /** Scanning completed */
    SCANNING_COMPLETE("ScanningComplete"),
    /** Processing started */
    PROCESSING_STARTED("ProcessingStarted"),
    /**
     * Processing restarted.
     * Image processing may be restarted if interrupted by a loss of power. A restart of image
     * processing will reset total images processed to 0
     */
    PROCESSING_RESTARTED("ProcessingRestarted"),
    /** Processing complete */
    PROCESSING_COMPLETE("ProcessingComplete"),
    /** Transmitting started (sent jub before the first try). */
    TRANSMITTING_STARTED("TransmittingStarted"),
    /** Marks beginning of each retry (not sent on the first try). */
    TRANSMITTING_RETRYING("TransmittingRetrying"),
    /** Transmitting completed (will always be sent before {@link #JOB_SUCCEEDED} */
    TRANSMITTING_COMPLETED("TransmittingCompleted"),
    /** Canceling started */
    CANCELING_STARTED("CancelingStarted"),
    /** Canceling Completed (will always be sent before {@link #JOB_CANCELED} */
    CANCELING_COMPLETED("CancelingCompleted"),
    /** Job ended because it was canceled */
    JOB_CANCELED("JobCanceled"),
    /** Job ended because it failed */
    JOB_FAILED("JobFailed"),
    /** Job succeeded */
    JOB_SUCCEEDED("JobSucceeded");

    /** Soap value associated with enum */
    final String mValue;

    /**
     * ScanJobEventCode constructor
     * @param value
     *              Value associated with enum
     */
    ScanJobEventCode(String value) {
        mValue = value;
    }

    /**
     * Convert a SOAP value string into a {@link ScanJobEventCode}
     * @param value
     *              SOAP value string
     * @return
     *              Matching {@link ScanJobEventCode} or null if match not found
     */
    public static ScanJobEventCode fromValue(String value) {
        for(ScanJobEventCode enumValue : values()) {
            if (TextUtils.equals(value, enumValue.mValue)) {
                return enumValue;
            }
        }
        return null;
    }
}
