// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IPP request
 */
public class IppRequest {

    /**
     * Unique request ID generator
     */
    private static final AtomicInteger sRequestID = new AtomicInteger(0);

    /** IPP version major */
    private final int mMajor;
    /** IPP version minor */
    private final int mMinor;
    /** Request ID */
    private final int mRequestID;
    /** IPP operation code */
    private final IppConstants.IppOperation mOperation;
    /** IPP attribute character set */
    private final Charset mCharset;
    /** IPP default language */
    private final Locale mLocale;
    /** IPP attributes by group */
    private final Map<Integer, IppCollection> mAttributes;

    /**
     * IPP request constructor
     * @param major IPP version major
     * @param minor IPP version minor
     * @param operation IPP operation code
     * @param charset IPP attribute character set
     * @param locale IPP default language
     * @param attributes IPP requests attributes by group
     */
    private IppRequest(int major, int minor, IppConstants.IppOperation operation, Charset charset, Locale locale, Map<Integer, IppCollection> attributes) {
        mMajor = major;
        mMinor = minor;
        mRequestID = sRequestID.incrementAndGet();
        mOperation = operation;
        mCharset = charset;
        mLocale = locale;
        mAttributes = Collections.unmodifiableMap(attributes);
    }

    /**
     * Encode the request to a byte array
     * @return The resulting byte array
     */
    public byte[] encode() {
        return new Encoder(this).getStream();
    }

    /**
     * Builder for creating {@link IppRequest}
     */
    public static final class Builder {

        /** IPP version major */
        private int mMajor = IppConstants.DEFAULT_IPP_VERSION_MAJOR;
        /** IPP version minor */
        private int mMinor = IppConstants.DEFAULT_IPP_VERSION_MINOR;
        /** IPP attributes associated with request */
        private LinkedHashMap<Integer, IppCollection.Builder> mAttributeMap = new LinkedHashMap<Integer,IppCollection.Builder>();
        /** IPP request attribute character set */
        private Charset mCharacterSet = IppConstants.DEFAULT_CHARSET;
        /** IPP request default attribute language */
        private Locale mDefaultLocale = IppConstants.DEFAULT_LOCALE;
        /** IPP operation code */
        private IppConstants.IppOperation mIppOp = null;

        /**
         * Constructor
         */
        public Builder() {
            // Add the operation group so it's first when iterating
            mAttributeMap.put((int) IppConstants.IppTag.IPP_TAG_OPERATION.getValue(), new IppCollection.Builder());
        }

        /**
         * Set the IPP version
         * @param major IPP version major
         * @param minor IPP version minor
         * @return This builder
         */
        @SuppressWarnings("unused")
        public Builder setVersion(int major, int minor) {
            mMajor = major;
            mMinor = minor;
            return this;
        }

        /**
         * Set the IPP operation code
         * @param operation IPP operation code
         * @return This builder
         */
        public Builder setIppOperation(IppConstants.IppOperation operation) {
            mIppOp = operation;
            return this;
        }

        /**
         * Set the IPP attribute character set
         * @param characterSet Attribute character set
         * @return This builder
         */
        @SuppressWarnings("unused")
        public Builder setCharacterSet(Charset characterSet) {
            mCharacterSet = characterSet;
            return this;
        }

        /**
         * Set the default IPP attribute language
         * @param locale IPP attribute language
         * @return This builder
         */
        @SuppressWarnings("unused")
        public Builder setLocale(Locale locale) {
            mDefaultLocale = locale;
            return this;
        }

        /**
         * Add an attribute to the specified attribute group
         * @param group Group to which to add attribute
         * @param attribute Attribute to add
         * @return This builder
         */
        public Builder addAttribute(IppConstants.IppTag group, IppAttribute attribute) {
            IppCollection.Builder builder = mAttributeMap.get((int)group.getValue());
            if (builder == null) {
                builder = new IppCollection.Builder();
                mAttributeMap.put((int)group.getValue(), builder);
            }
            builder.addAttribute(attribute);
            return this;
        }

        /**
         * Check the operation attributes. Add the {@link IppConstants#IPP_ATTRIBUTE__ATTRIBUTES_CHARSET} attribute and
         * {@link IppConstants#IPP_ATTRIBUTE__ATTRIBUTES_NATURAL_LANGUAGE} attributes if necessary. Also ensures that they
         * are the first attributes in the request
         */
        private void checkOperationAttributes() {
            List<IppAttribute> attrs = mAttributeMap.get((int)IppConstants.IppTag.IPP_TAG_OPERATION.getValue()).getAttributes();

            // look for attributes
            IppAttribute charsetAttribute, naturalLanguageAttr;
            charsetAttribute = naturalLanguageAttr = null;
            for(IppAttribute attribute : attrs) {
                if (TextUtils.equals(attribute.mName, IppConstants.IPP_ATTRIBUTE_NAME__CHARSET)) charsetAttribute = attribute;
                else if (TextUtils.equals(attribute.mName, IppConstants.IPP_ATTRIBUTE_NAME__NATURAL_LANGUAGE)) naturalLanguageAttr = attribute;
            }

            // remove if found
            if (charsetAttribute != null) {
                attrs.remove(charsetAttribute);
            }
            // create character set
            charsetAttribute = new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_CHARSET, IppConstants.IPP_ATTRIBUTE_NAME__CHARSET)
                    .addValue(mCharacterSet.name().toLowerCase())
                    .build();

            // remove if found
            if (naturalLanguageAttr != null) {
                attrs.remove(naturalLanguageAttr);
            }

            // create natural language
            naturalLanguageAttr = new IppStringAttribute.Builder(IppConstants.IppTag.IPP_TAG_LANGUAGE, IppConstants.IPP_ATTRIBUTE_NAME__NATURAL_LANGUAGE)
                    .addValue(localeToString(mDefaultLocale))
                    .build();

            // add language
            attrs.add(0, naturalLanguageAttr);
            // add character set
            attrs.add(0, charsetAttribute);

        }

        /**
         * Build the {@link IppRequest}
         * @return new {@link IppRequest}
         */
        public IppRequest build() {
            if (mIppOp == null) throw new IllegalArgumentException("missing operation");
            checkOperationAttributes();

            LinkedHashMap<Integer, IppCollection> attributeMap = new LinkedHashMap<Integer,IppCollection>();
            for(Map.Entry<Integer, IppCollection.Builder> entry : mAttributeMap.entrySet()) {
                attributeMap.put(entry.getKey(), entry.getValue().build());
            }

            return new IppRequest(mMajor, mMinor, mIppOp, mCharacterSet, mDefaultLocale, attributeMap);
        }
    }

    /**
     * {@link IppRequest} output encoder
     */
    private static class Encoder {

        /**
         * Output stream collector
         */
        private final ByteArrayOutputStream mBaos = new ByteArrayOutputStream();
        /** Attribute character set */
        private final Charset mCharSet;
        /** Request language */
        private final Locale mDefaultLocale;

        /**
         * Constructor
         * @param request Request to encode
         */
        Encoder(IppRequest request) {
            mCharSet = request.mCharset;
            mDefaultLocale = request.mLocale;
            writeByte(request.mMajor);
            writeByte(request.mMinor);
            writeShort(request.mOperation.getValue());
            writeInt(request.mRequestID);
            for(Map.Entry<Integer, IppCollection> entry : request.mAttributes.entrySet()) {
                addIppGroup(entry.getKey(), entry.getValue());
            }
            writeTag(IppConstants.IppTag.IPP_TAG_END.getValue());
        }

        /**
         * Process an ipp attribute grouping
         * @param groupTag IPP group tag
         * @param attributes IPP attributes collection
         */
        private void addIppGroup(int groupTag, IppCollection attributes) {
            writeTag(groupTag);
            for(IppAttribute attribute : attributes.getAttributes()) {
                addAttribute(attribute);
            }
        }

        /**
         * Collection depth tracker
         */
        private int mCollectionDepth = 0;

        /**
         * Encode an attribute
         * @param attribute Attribute to encode
         */
        private void addAttribute(IppAttribute attribute) {

            byte[] nameBytes = attribute.mName.getBytes(mCharSet);

            int count = 0;
            for(Object obj : attribute.mValues) {
                if (count == 0) {
                    if (mCollectionDepth == 0) {
                        writeTag(attribute.mTag);
                        writeShort(nameBytes.length);
                        writeBytes(nameBytes);
                    } else {
                        writeByte(IppConstants.IppTag.IPP_TAG_MEMBERNAME.getValue());
                        writeShort(0);
                        writeShort(nameBytes.length);
                        writeBytes(nameBytes);
                        writeTag(attribute.mTag);
                        writeShort(0);
                    }
                } else {
                    writeTag(attribute.mTag);
                    writeShort(0);
                }
                if (attribute instanceof IppBooleanAttribute) {
                    Boolean attrValue = (Boolean)obj;
                    writeShort(1);
                    writeByte(attrValue ? 0x1 : 0x0);
                } else if (attribute instanceof IppCollectionAttribute) {
                    IppCollection attrValue = (IppCollection)obj;
                    writeShort(0);
                    mCollectionDepth++;
                    for(IppAttribute collectionAttribute : attrValue.getAttributes()) {
                        addAttribute(collectionAttribute);
                    }
                    mCollectionDepth--;
                    writeByte(IppConstants.IppTag.IPP_TAG_END_COLLECTION.getValue());
                    writeShort(0);
                    writeShort(0);
                } else if (attribute instanceof IppDateAttribute) {
                    Date attrValue = (Date)obj;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(attrValue);
                    writeShort(11);
                    writeShort(cal.get(Calendar.YEAR));
                    writeByte(cal.get(Calendar.MONTH) + 1);
                    writeByte(cal.get(Calendar.DAY_OF_MONTH));
                    writeByte(cal.get(Calendar.HOUR_OF_DAY));
                    writeByte(cal.get(Calendar.MINUTE));
                    writeByte(cal.get(Calendar.SECOND));
                    writeByte(0);
                    int offset = cal.get(Calendar.ZONE_OFFSET);
                    writeByte((offset > 0) ? '+' : '-');
                    offset = Math.abs(offset) / (1000 * 60);
                    writeByte((offset / 60));
                    writeByte((offset % 60));
                } else if (attribute instanceof IppIntegerAttribute) {
                    Integer attrValue = (Integer)obj;
                    writeShort(4);
                    writeInt(attrValue);
                } else if (attribute instanceof IppRangeAttribute) {
                    IppRange attrValue = (IppRange) obj;
                    writeShort(8);
                    writeInt(attrValue.mLowerBound);
                    writeInt(attrValue.mUpperBound);
                } else if (attribute instanceof IppRawAttribute) {
                    byte[] attrValue = (byte[]) obj;
                    writeShort(attrValue.length);
                    if (attrValue.length > 0) {
                        writeBytes(attrValue);
                    }
                } else if (attribute instanceof IppResolutionAttribute) {
                    IppResolution attrValue = (IppResolution) obj;
                    writeShort(9);
                    writeInt(attrValue.mXres);
                    writeInt(attrValue.mYres);
                    writeByte(attrValue.mUnits.getValue());
                } else if (attribute instanceof IppStringAttribute) {
                    String attrValue = (String)obj;
                    byte[] attrValueBytes;
                    if (!TextUtils.isEmpty(attrValue)) {
                        attrValueBytes = attrValue.getBytes(mCharSet);
                    } else {
                        attrValueBytes = new byte[0];
                    }

                    if ((attribute.mTag == IppConstants.IppTag.IPP_TAG_TEXTLANG.getValue()) ||
                            (attribute.mTag == IppConstants.IppTag.IPP_TAG_NAMELANG.getValue())) {
                        Locale locale = ((IppStringAttribute)attribute).mStringLocale;
                        String localeString = localeToString((locale != null) ? locale : mDefaultLocale);
                        byte[] localeBytes = localeString.getBytes(mCharSet);
                        writeShort(4 + localeBytes.length + attrValueBytes.length);
                        writeShort(localeBytes.length);
                        writeBytes(localeBytes);
                    }

                    writeShort(attrValueBytes.length);
                    writeBytes(attrValueBytes);

                } else {
                    writeShort(0);
                }
                count++;
            }
        }

        /**
         * Encode a tag
         * @param tag tag to encode
         */
        private void writeTag(int tag) {
            if (tag > 0xff) {
                writeByte(IppConstants.IppTag.IPP_TAG_EXTENSION.getValue());
                writeInt(tag);
            } else {
                writeByte(tag);
            }
        }

        /**
         * Write out a byte
         * @param value Value to encode
         */
        private void writeByte(byte value) {
            mBaos.write(value);
        }

        /**
         * Write out a byte
         * @param value Value to encode
         */
        private void writeByte(int value) {
            mBaos.write(value);
        }

        /**
         * Write out a short
         * @param value Value to encode
         */
        private void writeShort(short value) {
            byte[] data = new byte[2];
            ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).putShort(value);
            mBaos.write(data, 0, data.length);
        }

        /**
         * Write out a short
         * @param value Value to encode
         */
        private void writeShort(int value) {
            byte[] data = new byte[2];
            ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).putShort((short)value);
            mBaos.write(data, 0, data.length);
        }

        /**
         * Write out an int
         * @param value Value to encode
         */
        private void writeInt(int value) {
            byte[] data = new byte[4];
            ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).putInt(value);
            mBaos.write(data, 0, data.length);
        }

        /**
         * Write out an byte array
         * @param value Value to encode
         */
        private void writeBytes(byte[] value) {
            mBaos.write(value, 0, value.length);
        }

        /**
         * Get the encoded stream
         * @return The encoded byte array stream
         */
        private byte[] getStream() {
            return mBaos.toByteArray();
        }
    }

    /**
     * Convert {@link Locale} to IPP string equivalent
     * @param locale Value to convert
     * @return IPP string equivalent of {@link Locale}
     */
    private static String localeToString(Locale locale) {
        StringBuilder localeStringBuilder = new StringBuilder();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (!TextUtils.isEmpty(language)) {
            localeStringBuilder.append(language.toLowerCase());
        }
        if (!TextUtils.isEmpty(country)) {
            if (localeStringBuilder.length() > 0) localeStringBuilder.append('-');
            localeStringBuilder.append(country.toLowerCase());
        }
        return localeStringBuilder.toString();
    }
}
