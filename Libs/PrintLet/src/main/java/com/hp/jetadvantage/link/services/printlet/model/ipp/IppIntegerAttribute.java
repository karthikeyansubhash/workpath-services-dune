// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IppAttribute} containing {@link Integer} values
 */
public final class IppIntegerAttribute extends IppAttribute<Integer> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppIntegerAttribute(int tag, String name, List<Integer> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppIntegerAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        IppIntegerAttribute other = (IppIntegerAttribute)attribute;
        List<Integer> values = new ArrayList<Integer>(mValues);
        values.addAll(other.mValues);
        return new IppIntegerAttribute(mTag, mName, values);
    }

    /**
     * Return a default value
     * @return 0
     */
    @Override
    Integer getDefaultValue() {
        return 0;
    }

    /**
     * Builder for creating {@link IppIntegerAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<Integer> {

        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        public Builder(IppConstants.IppTag tag, String name) {
            this(tag.getValue(), name);
        }

        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        public Builder(int tag, String name) {
            super(tag, name);
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        public Builder addValue(int value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addValues(int... values) {
            if ((values != null) && (values.length > 0)) {
                for(int value : values) mValues.add(value);
            }
            return this;
        }

        /**
         * Build the {@link IppIntegerAttribute}
         * @return new {@link IppIntegerAttribute}
         */
        @Override
        public IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppIntegerAttribute(mTag, mName, mValues);
        }
    }
}
