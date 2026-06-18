// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * IPP response
 */
public class IppResponse {

    /** IPP version major */
    private final int mMajor;
    /** IPP version minor */
    private final int mMinor;
    /** IPP response ID */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int mRequestID;
    /** IPP response status */
    private final IppConstants.IppStatus mIppStatus;
    /** IPP response attributes */
    private final Map<Integer, IppCollection> mAttributes;
    /** Additional data received in request */
    private final byte[] mData;

    /**
     * Constructor
     * @param major IPP version major
     * @param minor IPP version minor
     * @param requestID IPP response ID
     * @param status IPP response status
     * @param attributeMap IPP response attributes
     * @param data Additional data received with response
     */
    private IppResponse(int major, int minor, int requestID, IppConstants.IppStatus status, Map<Integer, IppCollection> attributeMap, byte[] data) {
        mMajor = major;
        mMinor = minor;
        mRequestID = requestID;
        mIppStatus = status;
        mAttributes = Collections.unmodifiableMap(attributeMap);
        mData = data;
    }

    /**
     * Return the IPP version major
     * @return IPP version major
     */
    @SuppressWarnings("unused")
    public int getIppMajor() {
        return mMajor;
    }

    /**
     * Return the IPP version minor
     * @return IPP version minor
     */
    @SuppressWarnings("unused")
    public int getIppMinor() {
        return mMinor;
    }

    /**
     * Return the IPP response status
     * @return IPP response status
     */
    public IppConstants.IppStatus getResponseStatus() {
        return mIppStatus;
    }

    /**
     * Return the {@link IppCollection} for the specified group
     * @param groupTag Group of attributes to return
     * @return {@link IppCollection} of attributes for specified group or null if not found
     */
    @SuppressWarnings("WeakerAccess")
    public IppCollection getGroupAttributes(IppConstants.IppTag groupTag) {
        return mAttributes.get((int)groupTag.getValue());
    }

    /**
     * Return a list of attribute groups that were received
     * @return List of {@link  IppConstants.IppTag} groups in the response
     */
    @SuppressWarnings("unused")
    public List<IppConstants.IppTag> getGroups() {
        List<IppConstants.IppTag> groups = new ArrayList<IppConstants.IppTag>();
        for(Integer key : mAttributes.keySet()) {
            try {
                groups.add(IppConstants.IppTag.toIppTag(key));
            } catch(Exception ignored) {}
        }
        return groups;
    }

    /**
     * Find the requested attribute
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return {@link IppAttribute} instance or null if not found
     */
    @SuppressWarnings("unused")
    public IppAttribute findAttribute(IppConstants.IppTag tag, String name) {
        return findAttribute(tag.getValue(), name);
    }

    /**
     * Find the requested attribute
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return {@link IppAttribute} instance or null if not found
     */
    @SuppressWarnings("WeakerAccess")
    public IppAttribute findAttribute(int tag, String name) {
        for(IppCollection group : mAttributes.values()) {
            IppAttribute attr = group.findAttribute(tag, name);
            if (attr != null) return attr;
        }
        return null;
    }

    /**
     * Find all instances of the requested attribute
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return List of {@link IppAttribute}instances of requested attribute in response
     */
    @SuppressWarnings("unused")
    public List<IppAttribute> findAllAttributes(IppConstants.IppTag tag, String name) {
        return findAllAttributes(tag.getValue(), name);
    }

    /**
     * Find all instances of the requested attribute
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return List of {@link IppAttribute}instances of requested attribute in response
     */
    @SuppressWarnings("WeakerAccess")
    public List<IppAttribute> findAllAttributes(int tag, String name) {
        List<IppAttribute> attributes = new ArrayList<IppAttribute>();
        for(IppCollection group : mAttributes.values()) {
            attributes.addAll(group.findAllAttributes(tag, name));
        }
        return attributes;
    }

    /**
     * Find the requested attribute within the specified group
     * @param groupTag IPP group to search in
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return {@link IppAttribute} instance or null if not found
     */
    public IppAttribute findAttribute(IppConstants.IppTag groupTag, IppConstants.IppTag tag, String name) {
        return findAttribute(groupTag, tag.getValue(), name);
    }

    /**
     * Find the requested attribute within the specified group
     * @param groupTag IPP group to search in
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return {@link IppAttribute} instance or null if not found
     */
    @SuppressWarnings("WeakerAccess")
    public IppAttribute findAttribute(IppConstants.IppTag groupTag, int tag, String name) {
        IppCollection collection = getGroupAttributes(groupTag);
        return ((collection != null) ? collection.findAttribute(tag, name) : null);
    }

    /**
     * Find the requested attribute within the specified group, matching any of the given tags.
     * Used for attributes that may be encoded as keyword or name depending on the printer.
     * @param groupTag IPP group to search in
     * @param tags Array of IPP tag values to match
     * @param name IPP attribute name
     * @return {@link IppAttribute} instance or null if not found
     */
    public IppAttribute findAttributeByAnyTag(IppConstants.IppTag groupTag, int[] tags, String name) {
        IppCollection collection = getGroupAttributes(groupTag);
        return ((collection != null) ? collection.findAttributeByAnyTag(tags, name) : null);
    }

    /**
     * Find all instances of the requested attribute within the specified group
     * @param groupTag IPP group to search in
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return List of {@link IppAttribute}instances of requested attribute in response
     */
    @SuppressWarnings("unused")
    public List<IppAttribute> findAllAttributes(IppConstants.IppTag groupTag, IppConstants.IppTag tag, String name) {
        return findAllAttributes(groupTag, tag.getValue(), name);
    }

    /**
     * Find all instances of the requested attribute within the specified group
     * @param groupTag IPP group to search in
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return List of {@link IppAttribute}instances of requested attribute in response
     */
    @SuppressWarnings("WeakerAccess")
    public List<IppAttribute> findAllAttributes(IppConstants.IppTag groupTag, int tag, String name) {
        IppCollection collection = getGroupAttributes(groupTag);
        if (collection == null) {
            return Collections.emptyList();
        }
        return collection.findAllAttributes(tag, name);
    }

    /**
     * Return additional data received as part of the response
     * @return Additional data byte array
     */
    @SuppressWarnings("unused")
    public byte[] getAdditionalData() {
        return mData;
    }

    /**
     * IPP input decoder
     */
    private static class Decoder {

        /** Byte buffer to read data from */
        final ByteBuffer mByteBuffer;

        /** IPP version major */
        int mMajor;
        /** IPP version minor */
        int mMinor;
        /** IPP status */
        IppConstants.IppStatus mStatus;
        /** IPP response ID */
        int mResponseID;
        /** IPP attributes by group */
        Map<Integer, IppCollection.Builder> mAttributes = new LinkedHashMap<Integer,IppCollection.Builder>();

        /** Response locale */
        Locale mLocale = null;
        /** Response character set */
        Charset mCharset = null;
        /** Additional data in response */
        byte[] mExtraData;

        /**
         * Constructor
         * @param responseData raw IPP response data
         */
        private Decoder(byte[] responseData) {
            mByteBuffer = ByteBuffer.wrap(responseData).order(ByteOrder.BIG_ENDIAN).asReadOnlyBuffer();
            parse();
        }

        /**
         * Process the IPP response header
         */
        private void readResponseHeader() {
            try {
                mMajor = mByteBuffer.get();
                mMinor = mByteBuffer.get();
                try {
                    mStatus = IppConstants.IppStatus.toIppStatus(mByteBuffer.getShort());
                } catch (IllegalArgumentException ignored) {
                    mStatus = IppConstants.IppStatus.IPP_BAD_REQUEST;
                }
                mResponseID = mByteBuffer.getInt();
            } catch(Exception ignored) {
                mMajor = mMinor = mResponseID = 0;
                mStatus = IppConstants.IppStatus.IPP_BAD_REQUEST;
            }
        }

        /**
         * Return a locale to use
         * @return Decoded locale or {@link IppConstants#DEFAULT_LOCALE} if null
         */
        private Locale getLocale() {
            if (mLocale == null) {
                return IppConstants.DEFAULT_LOCALE;
            }
            return mLocale;
        }

        /**
         * Return response character set
         * @return Decoded character set or {@link IppConstants#DEFAULT_CHARSET} if null
         */
        private Charset getCharset() {
            if (mCharset == null) {
                return IppConstants.DEFAULT_CHARSET;
            }
            return mCharset;
        }

        /**
         * Parse the response
         */
        private void parse() {
            readResponseHeader();
            parseGroups();
            mExtraData = new byte[mByteBuffer.limit() - mByteBuffer.position()];
            mByteBuffer.get(mExtraData);
        }

        /**
         * Parse the attribute groups
         */
        private void parseGroups() {

            int tag;
            do {
                tag = readTag();
                mByteBuffer.mark();
                if (tag == IppConstants.IppTag.IPP_TAG_END.getValue()) break;
                IppCollection.Builder builder = mAttributes.get(tag);
                if (builder == null) {
                    builder = new IppCollection.Builder();
                    mAttributes.put(tag, builder);
                }
                builder.addAttributes(parseAttributes());
            } while(true);
        }

        /**
         * Return the locale of the IPP encoded language string
         * @param localeString IPP encoded language string
         * @return Language locale
         */
        private Locale getLocalFromString(String localeString) {
            Locale locale = null;
            if (!TextUtils.isEmpty(localeString)) {
                String[] splits = localeString.split("-");
                locale = new Locale(splits[0], ((splits.length > 1) ? splits[1] : ""));
            }
            return ((locale != null) ? locale : getLocale());
        }

        /**
         * Parse the stream into an {@link IppCollection}
         * @return {@link IppCollection} of attributes
         */
        @SuppressWarnings("ConstantConditions")
        private IppCollection parseAttributes() {
            IppCollection.Builder builder = new IppCollection.Builder();

            String attrName;
            IppAttribute lastAttr = null;

            try {
                do {
                    mByteBuffer.mark();
                    int tag = readTag();
                    if (tag < 0xf) {
                        mByteBuffer.reset();
                        break;
                    }

                    short skipBytes, nameLength, valueLength;
                    byte[] nameBytes, valueBytes;
                    if (tag == IppConstants.IppTag.IPP_TAG_MEMBERNAME.getValue()) {
                        // skip name & get value
                        skipBytes = mByteBuffer.getShort();
                        mByteBuffer.position(mByteBuffer.position() + skipBytes);
                        nameLength = mByteBuffer.getShort();
                        nameBytes = new byte[nameLength];
                        mByteBuffer.get(nameBytes);
                        attrName = new String(nameBytes, getCharset());
                        tag = readTag();
                        skipBytes = mByteBuffer.getShort();
                        mByteBuffer.position(mByteBuffer.position() + skipBytes);
                    } else {
                        nameLength = mByteBuffer.getShort();
                        nameBytes = new byte[nameLength];
                        mByteBuffer.get(nameBytes);
                        attrName = new String(nameBytes, getCharset());
                    }

                    // value
                    valueLength = mByteBuffer.getShort();
                    valueBytes = new byte[valueLength];
                    mByteBuffer.get(valueBytes);
                    ByteBuffer valueByteBuffer = ByteBuffer.wrap(valueBytes).asReadOnlyBuffer();

                    mByteBuffer.mark();
                    if (tag == IppConstants.IppTag.IPP_TAG_END_COLLECTION.getValue()) {
                        break;
                    }

                    IppAttribute currentAttribute = null;
                    IppConstants.IppTag asTag = IppConstants.IppTag.toIppTag(tag);
                    if (asTag == null) {
                        currentAttribute = new IppRawAttribute.Builder(tag, attrName)
                                .addValue(valueBytes)
                                .build();
                    } else {
                        switch (asTag) {
                            case IPP_TAG_INTEGER:
                            case IPP_TAG_ENUM: {
                                currentAttribute = new IppIntegerAttribute.Builder(tag, attrName)
                                        .addValue(valueByteBuffer.getInt())
                                        .build();
                                break;
                            }
                            case IPP_TAG_BOOLEAN: {
                                currentAttribute = new IppBooleanAttribute.Builder(attrName)
                                        .addValue(valueByteBuffer.get() > 0)
                                        .build();
                                break;
                            }

                            case IPP_TAG_DATE: {
                                ByteBuffer dataReader = ByteBuffer.wrap(valueBytes).order(ByteOrder.BIG_ENDIAN).asReadOnlyBuffer();
                                DecimalFormat twoDigit = new DecimalFormat("00");
                                DecimalFormat fourDigit = new DecimalFormat("0000");
                                String dateString = String.format("%s-%s-%sT%s:%s:%s.%s%c%s:%s",
                                        fourDigit.format(dataReader.getShort()),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()),
                                        dataReader.get(),
                                        twoDigit.format(dataReader.get()),
                                        twoDigit.format(dataReader.get()));
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZ");
                                try {
                                    Date ippDate = dateFormat.parse(dateString);
                                    currentAttribute = new IppDateAttribute.Builder(attrName)
                                            .setValue(ippDate)
                                            .build();
                                } catch (ParseException ignored) {
                                }
                                break;
                            }
                            case IPP_TAG_RESOLUTION: {
                                currentAttribute = new IppResolutionAttribute.Builder(attrName)
                                        .addValue(new IppResolution(
                                                valueByteBuffer.getInt(),
                                                valueByteBuffer.getInt(),
                                                IppConstants.IppUnits.fromValue(valueByteBuffer.get())))
                                        .build();
                                break;
                            }
                            case IPP_TAG_RANGE: {
                                currentAttribute = new IppRangeAttribute.Builder(attrName)
                                        .addValue(new IppRange(valueByteBuffer.getInt(), valueByteBuffer.getInt()))
                                        .build();
                                break;
                            }
                            case IPP_TAG_BEGIN_COLLECTION: {
                                currentAttribute = new IppCollectionAttribute.Builder(attrName)
                                        .addValue(parseAttributes())
                                        .build();
                                break;
                            }

                            case IPP_TAG_TEXT:
                            case IPP_TAG_NAME:
                            case IPP_TAG_KEYWORD:
                            case IPP_TAG_URI:
                            case IPP_TAG_URISCHEME:
                            case IPP_TAG_CHARSET:
                            case IPP_TAG_LANGUAGE:
                            case IPP_TAG_MIMETYPE: {
                                currentAttribute = new IppStringAttribute.Builder(tag, attrName)
                                        .addValue(new String(valueBytes, getCharset()))
                                        .setLocale(getLocale())
                                        .build();
                                if ((asTag == IppConstants.IppTag.IPP_TAG_CHARSET)
                                    && (mCharset == null)) {
                                    try {
                                        String charsetString = ((IppStringAttribute)currentAttribute).get(0);
                                        if (!TextUtils.isEmpty(charsetString)) {
                                            mCharset = Charset.forName(charsetString.toUpperCase());
                                        }
                                    } catch(Exception ignored) {
                                        mCharset = IppConstants.DEFAULT_CHARSET;
                                    }
                                } else if ((asTag == IppConstants.IppTag.IPP_TAG_LANGUAGE)
                                        && (mLocale == null)) {
                                    mLocale = getLocalFromString(((IppStringAttribute)currentAttribute).get(0));
                                }
                                break;
                            }

                            case IPP_TAG_TEXTLANG:
                            case IPP_TAG_NAMELANG: {
                                ByteBuffer languageBuffer = ByteBuffer.wrap(valueBytes).order(ByteOrder.BIG_ENDIAN).asReadOnlyBuffer();
                                byte[] localeBytes = new byte[languageBuffer.getShort()];
                                languageBuffer.get(localeBytes);
                                byte[] localizedBytes = new byte[languageBuffer.getShort()];
                                languageBuffer.get(localizedBytes);

                                // process locale
                                Locale locale = getLocalFromString(new String(localeBytes, getCharset()));

                                // create attribute
                                currentAttribute = new IppStringAttribute.Builder(tag, attrName)
                                        .setLocale(locale)
                                        .addValue(new String(localizedBytes, getCharset()))
                                        .build();

                                break;
                            }

                            case IPP_TAG_STRING: {
                                currentAttribute = new IppRawAttribute.Builder(tag, attrName)
                                        .addValue(valueBytes)
                                        .build();
                                break;
                            }

                            case IPP_TAG_END_COLLECTION:
                            case IPP_TAG_RESERVED_STRING:
                            case IPP_TAG_MEMBERNAME:
                            case IPP_TAG_EXTENSION:
                            case IPP_TAG_ZERO:
                            case IPP_TAG_OPERATION:
                            case IPP_TAG_JOB:
                            case IPP_TAG_END:
                            case IPP_TAG_PRINTER:
                            case IPP_TAG_UNSUPPORTED_GROUP:
                            case IPP_TAG_SUBSCRIPTION:
                            case IPP_TAG_EVENT_NOTIFICATION:
                            case IPP_TAG_RESOURCE:
                            case IPP_TAG_DOCUMENT:
                            case IPP_TAG_UNSUPPORTED_VALUE:
                            case IPP_TAG_DEFAULT:
                            case IPP_TAG_UNKNOWN:
                            case IPP_TAG_NOVALUE:
                            case IPP_TAG_NOTSETTABLE:
                            case IPP_TAG_DELETEATTR:
                            case IPP_TAG_ADMINDEFINE:
                            default:
                                break;
                        }
                    }

                    if (lastAttr == null) {
                        lastAttr = currentAttribute;
                    } else if (TextUtils.isEmpty(attrName)) {
                        lastAttr = IppAttribute.merge(lastAttr, currentAttribute);
                    } else {
                        builder.addAttribute(lastAttr);
                        lastAttr = currentAttribute;
                    }

                } while (true);
            } catch(Exception ignored) {}

            if (lastAttr != null) {
                builder.addAttribute(lastAttr);
            }

            return builder.build();
        }

        /**
         * Read an IPP tag
         * @return IPP tag value
         */
        private int readTag() {
            try {
                int tag = mByteBuffer.get();
                if (tag == IppConstants.IppTag.IPP_TAG_EXTENSION.getValue()) {
                    tag = mByteBuffer.getInt();
                }
                return tag;
            } catch(Exception ignored) {
                return IppConstants.IppTag.IPP_TAG_END.getValue();
            }
        }
    }

    /**
     * Transform a raw IPP response into an {@link IppResponse} instance
     * @param responseData Response data to decode
     * @return {@link IppResponse} instance
     */
    public static IppResponse decode(byte[] responseData) {
        Decoder parser = new Decoder(responseData);
        LinkedHashMap<Integer, IppCollection> attributeMap = new LinkedHashMap<Integer,IppCollection>();
        for(Map.Entry<Integer, IppCollection.Builder> entry : parser.mAttributes.entrySet()) {
            attributeMap.put(entry.getKey(), entry.getValue().build());
        }
        return new IppResponse(parser.mMajor, parser.mMinor, parser.mResponseID, parser.mStatus, attributeMap, parser.mExtraData);
    }
}
