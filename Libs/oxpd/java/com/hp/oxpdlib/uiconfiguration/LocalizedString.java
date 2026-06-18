// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

import java.util.Locale;

/**
 * OXPd localized string
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LocalizedString implements Parcelable, Comparable<LocalizedString> {
    /** String locale */
    public final Locale mLocale;
    /** Localized String value */
    public final String mValue;

    /** Locale code delimiter */
    private static final String DELIMITER = "-";

    /**
     * Constructor
     * @param locale String locale
     * @param value Localized string value
     */
    public LocalizedString(Locale locale, String value) {
        mLocale = locale;
        mValue = value;
    }

    /**
     * Constructor
     * @param locale String locale
     * @param value Localized string value
     */
    public LocalizedString(String locale, String value) {
        String splits[] = locale.split(DELIMITER);
        mLocale = ((splits.length > 1) ? new Locale(splits[0], splits[1]) : new Locale(splits[0]));
        mValue = value;
    }

    /**
     * Returns the hash code value for the localized string
     * @return Localized string hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + mLocale.hashCode();
        result = result * prime + mValue.hashCode();
        return result;
    }

    /**
     * Compares the specified object with this one for equality. Returns true if only if the specified object
     * is also a {@link LocalizedString} with same locale/value
     * @param obj Object to be compared  for equality
     * @return True if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocalizedString)) return false;
        LocalizedString other = (LocalizedString)obj;
        return (mLocale.equals(other.mLocale) && TextUtils.equals(mValue, other.mValue));
    }

    /**
     * Compares this object with the specified object for order.
     * @param other The object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(LocalizedString other) {
        return localeCodeValue().compareTo(other.localeCodeValue());
    }

    /**
     * Convert locale to code value
     * @return OXPd string representation of Locale
     */
    private String localeCodeValue() {
        String language = mLocale.getLanguage();
        String country = mLocale.getCountry();

        if (!TextUtils.isEmpty(country)) {
            return language + DELIMITER + country;
        } else {
            return language;
        }
    }

    /**
     * Serialize the localized string value to XML
     * @param localizedString Localized string to serialize
     * @param xmlWriter XML writer to output data to
     */
    static void writeToXML(LocalizedString localizedString, RestXMLWriter xmlWriter) {
        xmlWriter.writeStartTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__LOCALIZED_STRING, null);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__CODE, null, "%s", localizedString.localeCodeValue());
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__VALUE, null, "%s", localizedString.mValue);
        xmlWriter.writeEndTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__LOCALIZED_STRING);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localeCodeValue());
        dest.writeString(mValue);
    }

    /**
     * Constructor
     * @param in
     *           Parcel from which the object should be reconstructed
     */
    private LocalizedString(Parcel in) {
        this(in.readString(), in.readString());
    }

    /**
     * LocalizedString creator
     */
    public static final Parcelable.Creator<LocalizedString> CREATOR =
            new Parcelable.Creator<LocalizedString>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link UIContext#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public LocalizedString createFromParcel(Parcel in) {
                    return new LocalizedString(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public LocalizedString[] newArray(int size) {
                    return new LocalizedString[size];
                }
            };
}
