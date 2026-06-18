// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan ticket validation codes */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ValidationCode {
    /** The element is valid. */
    Valid("Valid"),
    /** The element was ignored. */
    Ignored("Ignored"),
    /** The value of the element is unsupported on this device. */
    UnsupportedValue("UnsupportedValue"),
    /** The value of this element is incompatible with the associated binding. For example, a destination WebResource must specify a Plain binding. */
    ConflictsWithBinding("ConflictsWithBinding"),
    /** The value of this element is incompatible with the associated color mode. Examples include:<ul><li>A scan ticket specifying a filetype of Tiff or Mtiff and a color mode other than Black cannot use G3 or G4 compression.</li><li>A scan ticket specifying a filetype of Tiff or Mtiff and a color mode of Black cannot use JpegTiff6 or JpegTTN2 compression.</li></ul> */
    ConflictsWithColorMode("ConflictsWithColorMode"),
    /** The value of this element is incompatible with the associated file type. Examples include:<ul><li>A scan ticket specifying a non-null and non-empty encryption password must specify a file type for which the device is capable of encrypting with a password.</li><li>A scan ticket specifying a color mode of Black must not specify a file type of Jpeg.</li></ul> */
    ConflictsWithFileType("ConflictsWithFileType"),
    /** The value of this element is incompatible with the associated transmission mode. Examples include:<ul><li>A scan ticket specifying a transmission mode of Image must also use a file type of JPeg.</li><li>A scan ticket specifying a transmission mode of Image must not specify a blank image removal mode of On.</li><li>A scan ticket specifying a transmission mode of Image must not specify a crop mode of On.</li><li>A scan ticket specifying a transmission mode of Image must not specify a preview mode of On.</li></ul> */
    ConflictsWithTransmissionMode("ConflictsWithTransmissionMode"),
    /** The value of this element is incompatible with the associated URI. Examples include:<ul><li>A scan ticket specifying a transmission mode of Image must also use a URI scheme of HTTP or HTTPS.</li><li>A network credentials domain may only be used with a URI specifying a FILE scheme.</li></ul> */
    ConflictsWithUri("ConflictsWithUri");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * ValidationCode constructor
     * @param value SOAP value associated with enum
     */
    ValidationCode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to ValidationCode
     * @param value
     *              SOAP value string
     * @return
     *              Matching ValidationCode enum or null if no match is found
     */
    static ValidationCode fromAttributeValue(String value) {
        for(ValidationCode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
