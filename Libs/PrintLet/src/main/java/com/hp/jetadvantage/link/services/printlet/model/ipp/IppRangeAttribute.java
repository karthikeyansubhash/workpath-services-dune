// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link IppAttribute} containing {@link IppRange} values
 */
@SuppressWarnings("WeakerAccess")
public final class IppRangeAttribute extends IppAttribute<IppRange> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppRangeAttribute(int tag, String name, List<IppRange> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppRangeAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        IppRangeAttribute other = (IppRangeAttribute)attribute;
        List<IppRange> values = new ArrayList<IppRange>(mValues);
        values.addAll(other.mValues);
        return new IppRangeAttribute(mTag, mName, values);
    }

    /**
     * Return an empty range
     * @return Empty range
     */
    @Override
    IppRange getDefaultValue() {
        return new IppRange(0,0);
    }

    /**
     * Builder for creating {@link IppRangeAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<IppRange> {

        /**
         * Builder constructor
         * @param name IPP attribute name
         */
        public Builder(String name) {
            super(IppConstants.IppTag.IPP_TAG_RANGE, name);
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        public Builder addValue(IppRange value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addValues(IppRange... values) {
            if ((values != null) && (values.length > 0)) {
                mValues.addAll(Arrays.asList(values));
            }
            return this;
        }

        /**
         * Build the {@link IppRangeAttribute}
         * @return new {@link IppRangeAttribute}
         */
        @Override
        public IppAttribute build() {
            return new IppRangeAttribute(mTag, mName, mValues);
        }
    }
}
