// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of OCR languages. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum OcrLanguage {
    /** Not applicable. */
    NotApplicable("NotApplicable"),
    /** Optimize the OCR engine for Arabic. */
    Arabic("Arabic"),
    /** Optimize the OCR engine for Catalan. */
    Catalan("Catalan"),
    /** Optimize the OCR engine for Simplified Chinese. */
    ChineseSimplified("ChineseSimplified"),
    /** Optimize the OCR engine for Traditional Chinese. */
    ChineseTraditional("ChineseTraditional"),
    /** Optimize the OCR engine for Croatian. */
    Croatian("Croatian"),
    /** Optimize the OCR engine for Czech. */
    Czech("Czech"),
    /** Optimize the OCR engine for Danish. */
    Danish("Danish"),
    /** Optimize the OCR engine for Dutch. */
    Dutch("Dutch"),
    /** Optimize the OCR engine for English. */
    English("English"),
    /** Optimize the OCR engine for Finnish. */
    Finnish("Finnish"),
    /** Optimize the OCR engine for French. */
    French("French"),
    /** Optimize the OCR engine for German. */
    German("German"),
    /** Optimize the OCR engine for Greek. */
    Greek("Greek"),
    /** Optimize the OCR engine for Hebrew. */
    Hebrew("Hebrew"),
    /** Optimize the OCR engine for Hungarian. */
    Hungarian("Hungarian"),
    /** Optimize the OCR engine for Indonesian. */
    Indonesian("Indonesian"),
    /** Optimize the OCR engine for Italian. */
    Italian("Italian"),
    /** Optimize the OCR engine for Japanese. */
    Japanese("Japanese"),
    /** Optimize the OCR engine for Korean. */
    Korean("Korean"),
    /** Optimize the OCR engine for Norwegian. */
    Norwegian("Norwegian"),
    /** Optimize the OCR engine for Polish. */
    Polish("Polish"),
    /** Optimize the OCR engine for Portuguese. */
    Portuguese("Portuguese"),
    /** Optimize the OCR engine for Romanian. */
    Romanian("Romanian"),
    /** Optimize the OCR engine for Russian. */
    Russian("Russian"),
    /** Optimize the OCR engine for Slovak. */
    Slovak("Slovak"),
    /** Optimize the OCR engine for Slovenian. */
    Slovenian("Slovenian"),
    /** Optimize the OCR engine for Spanish. */
    Spanish("Spanish"),
    /** Optimize the OCR engine for Swedish. */
    Swedish("Swedish"),
    /** Optimize the OCR engine for Turkish. */
    Turkish("Turkish");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * OcrLanguage constructor
     * @param value
     *              SOAP value associated with enum
     */
    OcrLanguage(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to OcrLanguage
     * @param value
     *              SOAP value string
     * @return
     *              Matching OcrLanguage enum or null if no match is found
     */
    static OcrLanguage fromAttributeValue(String value) {
        for(OcrLanguage enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return NotApplicable;
    }
}
