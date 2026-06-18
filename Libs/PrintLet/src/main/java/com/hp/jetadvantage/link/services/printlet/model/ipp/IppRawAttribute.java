// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IppAttribute} consisting of byte array values
 */
@SuppressWarnings("WeakerAccess")
public class IppRawAttribute extends IppAttribute<byte[]> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppRawAttribute(int tag, String name, List<byte[]> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppIntegerAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        IppRawAttribute other = (IppRawAttribute)attribute;
        List<byte[]> values = new ArrayList<byte[]>(mValues);
        values.addAll(other.mValues);
        return new IppRawAttribute(mTag, mName, values);
    }

    /**
     * Return a default value
     * @return Empty byte array
     */
    @Override
    byte[] getDefaultValue() {
        return new byte[0];
    }

    /**
     * Builder for creating {@link IppRawAttribute}
     */
    public static class Builder extends IppAttribute.Builder<byte[]> {

        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        Builder(IppConstants.IppTag tag, String name) {
            this(tag.getValue(), name);
        }

        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        Builder(int tag, String name) {
            super(tag, name);
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        Builder addValue(byte[] value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        Builder addValues(byte[]... values) {
            if ((values != null) && (values.length > 0)) {
                for(byte[] value : values) {
                    if (value != null) mValues.add(value);
                }
            }
            return this;
        }

        /**
         * Build the {@link IppRawAttribute}
         * @return new {@link IppRawAttribute}
         */
        @Override
        public IppAttribute build() {
            return new IppRawAttribute(mTag, mName, mValues);
        }
    }
}
