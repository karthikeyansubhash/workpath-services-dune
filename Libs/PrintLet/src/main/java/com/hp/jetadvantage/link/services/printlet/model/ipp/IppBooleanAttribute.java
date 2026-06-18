// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IppAttribute} containing {@link Boolean} values
 */
public final class IppBooleanAttribute extends IppAttribute<Boolean> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppBooleanAttribute(int tag, String name, List<Boolean> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppBooleanAttribute} containing combined attribute values
     */
    @Override
    final IppAttribute appendToVerified(IppAttribute attribute) {
        IppBooleanAttribute other = (IppBooleanAttribute)attribute;
        List<Boolean> values = new ArrayList<Boolean>(mValues);
        values.addAll(other.mValues);
        return new IppBooleanAttribute(mTag, mName, values);
    }

    /**
     * Return a default value
     * @return {@link Boolean#FALSE}
     */
    @Override
    Boolean getDefaultValue() {
        return Boolean.FALSE;
    }

    /**
     * Builder for creating {@link IppBooleanAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<Boolean> {

        /**
         * Builder constructor
         * @param name IPP attribute name
         */
        public Builder(String name) {
            super(IppConstants.IppTag.IPP_TAG_BOOLEAN, name);
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        public final Builder addValue(boolean value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public final Builder addValues(boolean... values) {
            if ((values != null) && (values.length > 0)) {
                for(boolean value : values) mValues.add(value);
            }
            return this;
        }

        /**
         * Build the {@link IppBooleanAttribute}
         * @return new {@link IppBooleanAttribute}
         */
        @Override
        public final IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppBooleanAttribute(mTag, mName, mValues);
        }
    }
}
