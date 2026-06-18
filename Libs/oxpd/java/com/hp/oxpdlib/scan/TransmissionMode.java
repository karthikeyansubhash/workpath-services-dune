// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of transmission modes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum TransmissionMode {
    /** Transmit all images after the job has completed processing. When used with an HTTP or HTTPS scheme, all images (and the metadata file, if specified in the job ticket) will be transmitted in a single HTTP post transaction using a 'multipart form', similar to uploading files to a web site. The HTTP post will include a 'TransmittingState' form part with a value of 'Completed'. */
    Job("Job"),
    /** Transmit images as they become available (may only be used with HTTP and HTTPS schemes). Available images will be transmitted in separate HTTP post transactions using a 'multipart form', similar to uploading files to a web site. To improve image delivery performance, a single HTTP post may contain more than one image. All but the final HTTP post will indicate that the job is incomplete by including a 'TransmittingState' form part with a value of 'Started'. The final HTTP post will indicate that the job is complete by including a 'TransmittingState' form part with a value of 'Completed'. The final HTTP post will not include any images (the 'ScanFileCount' form part will have a value of '0'). The final HTTP post will include the metadata file (if specified in the job ticket). Note that for JobAssemblyMode=On, the final HTTP post will be transmitted only after the user has expressly finished the job at the device's control panel. */
    Image("Image");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ActivityState constructor
     * @param value
     *              SOAP value associated with enum
     */
    TransmissionMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to TransmissionMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching TransmissionMode enum or null if no match is found
     */
    static TransmissionMode fromAttributeValue(String value) {
        for(TransmissionMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
