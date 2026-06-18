// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.Date;
import java.util.List;

/**
 * {@link IppAttribute} containing a {@link Date} value
 */
@SuppressWarnings("WeakerAccess")
public final class IppDateAttribute extends IppAttribute<Date> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppDateAttribute(int tag, String name, List<Date> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppDateAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        return this;
    }

    /**
     * Return a default value
     * @return Current date
     */
    @Override
    Date getDefaultValue() {
        return new Date();
    }

    /**
     * Builder for creating {@link IppDateAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<Date> {

        /**
         * Builder constructor
         * @param name IPP attribute name
         */
        public Builder(String name) {
            super(IppConstants.IppTag.IPP_TAG_DATE, name);
        }

        /**
         * Set the date value of this attribute
         * @param value Value to set
         * @return This builder
         */
        public Builder setValue(Date value) {
            mValues.clear();
            mValues.add(value);
            return this;
        }

        /**
         * Build the {@link IppDateAttribute}
         * @return new {@link IppDateAttribute}
         */
        @Override
        public IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppDateAttribute(mTag, mName, mValues);
        }
    }
}
