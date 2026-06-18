// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/** Enumeration of media sizes. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaSize {
    /** Default. */
    Default("default"),
    /** ISO/JIS A3 (297mm x 420mm). */
    A3("iso_a3_297x420mm"),
    /** ISO/JIS A4 (210mm x 297mm). */
    A4("iso_a4_210x297mm"),
    /** ISO/JIS A5 (148mm x 210mm). */
    A5("iso_a5_148x210mm"),
    /** ISO/JIS A6 (105mm x 148mm). */
    A6("iso_a6_105x148mm"),
    /** ISO B4 (250mm x 353mm). */
    B4("iso_b4_250x353mm"),
    /** ISO B5 (176mm x 250mm). */
    B5("iso_b5_176x250mm"),
    /** ISO B6 (125mm x 176mm). */
    B6("iso_b6_125x176mm"),
    /** Envelope #9 (3.875&quot; x 8.875&quot;). */
    Envelope9("na_number-9_3.875x8.875in"),
    /** Envelope #10 (4.125&quot; x 9.5&quot;). */
    EnvelopeComm10("na_number-10_4.125x9.5in"),
    /** Envelope Monarch (3.875&quot; x 7.5&quot;). */
    EnvelopeMonarch("na_monarch_3.875x7.5in"),
    /** Envelope C5 (162mm x 229mm). */
    EnvelopeC5("iso_c5_162x229mm"),
    /** Envelope C6 (114mm x 162mm). */
    EnvelopeC6("iso_c6_114x162mm"),
    /** Envelope DL (110mm x 220mm). */
    EnvelopeDL("iso_dl_110x220mm"),
    /** Envelope Chou #3 (120mm x 235mm). */
    EnvelopeJChou3("jpn_chou3_120x235mm"),
    /** Envelope Chou #4 (90mm x 205mm). */
    EnvelopeJChou4("jpn_chou4_90x205mm"),
    /** Executive (7.25&quot; x 10.5&quot;). */
    Exec("na_executive_7.25x10.5in"),
    /** 8.5&quot; x 13&quot;. */
    Foolscap("na_foolscap_8.5x13in"),
    /** 12&quot; x 18&quot;. */
    ArchitecturalB("na_arch-b_12x18in"),
    /** JIS B4 (257mm x 364mm). */
    JB4("jis_b4_257x364mm"),
    /** JIS B5 (182mm x 257mm). */
    JB5("jis_b5_182x257mm"),
    /** JIS B6 (128mm x 182mm). */
    JB6("jis_b6_128x182mm"),
    /** Dpostcard JIS (148mm x 200mm). */
    JDoublePostcard("jpn_oufuku_148x200mm"),
    /** Postcard JIS (100mm x 148mm). */
    Jpostcard("jpn_hagaki_100x148mm"),
    /** 8K (270mm x 390mm). */
    K8("om_8k_270x390mm"),
    /** 8K (260mm x 368mm). */
    K8_260x368mm("om_8k_260x368mm"),
    /** 16K (195mm x 270mm). */
    K16("om_16k_195x270mm"),
    /** 16K (184mm x 260mm). */
    K16_184x260mm("om_16k_184x260mm"),
    /** Ledger (11&quot; x 17&quot;). */
    Ledger("na_ledger_11x17in"),
    /** Legal (8.5&quot; x 14&quot;). */
    Legal("na_legal_8.5x14in"),
    /** Letter (8.5&quot; x 11&quot;). */
    Letter("na_letter_8.5x11in"),
    /** Oficio (216mm x 340mm). */
    Oficio_216x340mm("na_oficio_8.5x13.4in"),
    /** PRC 8K (273mm x 394mm). */
    PK8("roc_8k_10.75x15.5in"),
    /** PRC 16K (197mm x 273mm). */
    PK16("roc_16k_7.75x10.75in"),
    /** RA3 (305mm x 430mm). */
    RA3("iso_ra3_305x430mm"),
    /** RA4 (215mm x 305mm). */
    RA4("iso_ra4_215x305mm"),
    /** 10x15cm (100mm x 150mm). */
    Size10x15cm("om_small-photo_100x150mm"),
    /** 3x5 (3&quot; x 5&quot;). */
    Size3x5in("na_index-3x5_3x5in"),
    /** 4x6 (4&quot; x 6&quot;). */
    Size4x6in("na_index-4x6_4x6in"),
    /** 5x7 (5&quot; x 7&quot;). */
    Size5x7in("na_5x7_5x7in"),
    /** 5x8 (5&quot; x 8&quot;). */
    Size5x8in("na_index-5x8_5x8in"),
    /** SRA3 (320mm x 450mm). */
    Sra3("iso_sra3_320x450mm"),
    /** SRA4 (225mm x 320mm). */
    Sra4("iso_sra4_225x320mm"),
    /** Statement (5.5&quot; x 8.5&quot;). */
    Statement("na_invoice_5.5x8.5in");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

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
