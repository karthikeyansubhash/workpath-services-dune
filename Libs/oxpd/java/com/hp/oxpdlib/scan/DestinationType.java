// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan destination type */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum DestinationType {
    /**
     * Send to HTTP.<br /><br />Uri example: 'http://MyWebServer[:port]/MyWebApp/MyScanReceiver.aspx'.
     */
    Http("Http"),
    /**
     * Send to HTTP using SSL.<br /><br />
     * Uri example: 'https://MyWebServer[:port]/MyWebApp/MyScanReceiver.aspx'.
     */
    Https("Https"),
    /**
     * Send to network folder using CIFS.<br /><br />
     * Uri example: 'file:////MyFileServer/MyShare[/MySubFolder]'.<br /><br />
     * If MySubFolder (or any subfolder specified in the Uri) does not exist at the destination,
     * some device implementations may create it (assuming the device has write permission), while
     * others will fail the job. To ensure fleet-wide compatibility, solutions must not rely on the
     * device to create subfolders. Instead, the solution must ensure that the subfolders exist
     * before starting the scan job.
     */
    NetworkFolder("NetworkFolder"),
    /**
     * <b>Reserved (do not use)</b>.
     */
    LocalFolder("LocalFolder"),
    /**
     * Send to FTP.<br /><br />
     * Uri example: 'ftp://MyFtpServer[/MyFolder[/MySubFolder]]'.<br /><br />
     * If MyFolder (or any subfolder psecified in the Uri) does not exist at the destination, some
     * device implementations may create it (assuming the device has write permission), while
     * others will fail the job. To ensure fleet-wide compatibility, solutions must not rely on the
     * device to create folders or subfolders. Instead, the solution must ensure that the folder and
     * subfolders exist before starting the scan job.
     */
    Ftp("Ftp");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ActivityState constructor
     * @param value
     *              SOAP value associated with enum
     */
    DestinationType(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to DestinationType
     * @param value
     *              SOAP value string
     * @return
     *              Matching DestinationType enum or null if no match is found
     */
    static DestinationType fromAttributeValue(String value) {
        for(DestinationType enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
