// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of media sizes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaSize {
    /** Custom size (must set customWidth and customHeight). */
    Custom("Custom"),
    /** If the job consists of a mixture of letter and legal media, the device will detect the size of each sheet and generate appropriately sized output. */
    MixedLetterLegal("MixedLetterLegal"),
    /** ISO/JIS A3 (297mm x 420mm). */
    A3("A3"),
    /** ISO/JIS A4 (210mm x 297mm). */
    A4("A4"),
    /** ISO/JIS A4 Rotated. */
    A4R("A4R"),
    /** ISO/JIS A5 (148mm x 210mm). */
    A5("A5"),
    /** ISO/JIS A5 Rotated. */
    A5R("A5R"),
    /** A6 (105mm x 148mm). */
    A6("A6"),
    /** ISO B4 (250mm x 353mm). */
    B4("B4"),
    /** ISO B5 (176mm x 250mm). */
    B5("B5"),
    /** ISO B5 Rotated. */
    B5R("B5R"),
    /** ISO B6 (125mm x 176mm). */
    B6("B6"),
    /** Business Card (2.16&quot; x 3.58&quot;). */
    BusinessCard("BusinessCard"),
    /** Executive (7.25&quot; x 10.5&quot;). */
    Exec("Exec"),
    /** 8.5&quot; x 13&quot;. */
    Inch8Point5x13("Inch8Point5x13"),
    /** 12&quot; x 18&quot;. */
    Inch12x18("Inch12x18"),
    /** JIS B4 (257mm x 364mm). */
    JB4("JB4"),
    /** JIS B5 (182mm x 257mm). */
    JB5("JB5"),
    /** JIS B5 Rotated. */
    JB5R("JB5R"),
    /** JIS B6 (128mm x 182mm). */
    JB6("JB6"),
    /** K8 (270mm x 390mm). */
    K8("K8"),
    /** K16 (195mm x 270mm). */
    K16("K16"),
    /** Ledger (11&quot; x 17&quot;). */
    Ledger("Ledger"),
    /** Legal (8.5&quot; x 14&quot;). */
    Legal("Legal"),
    /** Letter (8.5&quot; x 11&quot;). */
    Letter("Letter"),
    /** Letter Rotated. */
    LetterR("LetterR"),
    /** PRC 8K (273mm x 394mm). */
    PK8("PK8"),
    /** PRC 16K (197mm x 273mm). */
    PK16("PK16"),
    /** Statement (5.5&quot; x 8.5&quot;). */
    Statement("Statement"),
    /** Statement Rotated. */
    StatementR("StatementR"),
    /** Documents of any size */
    Any("Any"),
    /** Mix of A3/A4 Documents */
    MixedA3A4("MixedA3A4"),
    /** EnvelopeB5. */
    EnvelopeB5("EnvelopeB5"),
    /** Envelope9 */
    Envelope9("Envelope9"),
    /** EnvelopeComm10 */
    EnvelopeComm10("EnvelopeComm10"),
    /** EnvelopeMonarch */
    EnvelopeMonarch("EnvelopeMonarch"),
    /** EnvelopeC5 */
    EnvelopeC5("EnvelopeC5"),
    /** EnvelopeC6 */
    EnvelopeC6("EnvelopeC6"),
    /** EnvelopeDL */
    EnvelopeDL("EnvelopeDL"),
    /** EnvelopeJChou3 */
    EnvelopeJChou3("EnvelopeJChou3"),
    /** EnvelopeJChou4 */
    EnvelopeJChou4("EnvelopeJChou4"),
    /** EnvelopeUnknown */
    EnvelopeUnknown("EnvelopeUnknown"),
    /** JDoublePostcard */
    JDoublePostcard("JDoublePostcard"),
    /** JPostcard */
    JPostcard("JPostcard"),
    /** K8_260x368mm */
    K8_260x368mm("K8_260x368mm"),
    /** K16_184x260mm */
    K16_184x260mm("K16_184x260mm"),
    /** MixedLetterLedger */
    MixedLetterLedger("MixedLetterLedger"),
    /** MixedOriginal */
    MixedOriginal("MixedOriginal"),
    /** Oficio_216x340mm */
    Oficio_216x340mm("Oficio_216x340mm"),
    /** RA3 */
    RA3("RA3"),
    /** RA4 */
    RA4("RA4"),
    /** Size10x15cm */
    Size10x15cm("Size10x15cm"),
    /** Size12x18in */
    Size12x18in("Size12x18in"),
    /** Size3x5in */
    Size3x5in("Size3x5in"),
    /** Size4x6in */
    Size4x6in("Size4x6in"),
    /** Size5x7in */
    Size5x7in("Size5x7in"),
    /** Size5x8in */
    Size5x8in("Size5x8in"),
    /** Size8Point5x13in */
    Size8Point5x13in("Size8Point5x13in"),
    /** Size8point5x34in */
    Size8point5x34in("Size8point5x34in"),
    /** SizeL9x13cm */
    SizeL9x13cm("SizeL9x13cm"),
    /** Sra3 */
    Sra3("Sra3"),
    /** Sra4 */
    Sra4("Sra4"),
    /** Unknown */
    Unknown("Unknown"),
    // add items above this line
    ;

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * MediaSize constructor
     * @param value
     *              SOAP value associated with enum
     */
    MediaSize(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MediaSize
     * @param value
     *              SOAP value string
     * @return
     *              Matching MediaSize enum or null if no match is found
     */
    static MediaSize fromAttributeValue(String value) {
        for(MediaSize enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
