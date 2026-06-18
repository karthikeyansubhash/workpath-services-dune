package com.hp.oxpdlib.options;
import android.text.TextUtils;

/** Enumeration of copy file types. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum FoldMode {
    /** Fold mode location None */
    None("None"),
    /** Fold mode location CInwardTop */
    CInwardTop("CInwardTop"),
    /** Fold mode location CInwardBottom */
    CInwardBottom("CInwardBottom"),
    /** Fold mode location COutwardTop */
    COutwardTop("COutwardTop"),
    /** Fold mode location COutwardBottom */
    COutwardBottom("COutwardBottom"),
    /** Fold mode location VInwardTop */
    VInwardTop("VInwardTop"),
    /** Fold mode location VInwardBottom */
    VInwardBottom("VInwardBottom"),
    /** Fold mode location VOutwardTop */
    VOutwardTop("VOutwardTop"),
    /** Fold mode location VOutwardBottom */
    VOutwardBottom("VOutwardBottom"),
    /** Fold mode location Other */
    Other("Other");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * FoldMode constructor
     * @param value
     *              SOAP value associated with enum
     */
    FoldMode(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to FoldMode
     * @param value
     *              SOAP value string
     * @return
     *              Matching FoldMode enum or null if no match is found
     */
    public static FoldMode fromAttributeValue(String value) {
        for(FoldMode enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
