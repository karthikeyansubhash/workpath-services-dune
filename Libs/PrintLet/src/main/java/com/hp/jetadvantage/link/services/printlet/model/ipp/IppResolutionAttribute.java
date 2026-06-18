// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IppAttribute} containing {@link IppResolution} values
 */
@SuppressWarnings("WeakerAccess")
public final class IppResolutionAttribute extends IppAttribute<IppResolution> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppResolutionAttribute(int tag, String name, List<IppResolution> values) {
        super(tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppResolutionAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        IppResolutionAttribute other = (IppResolutionAttribute)attribute;
        List<IppResolution> values = new ArrayList<IppResolution>(mValues);
        values.addAll(other.mValues);
        return new IppResolutionAttribute(mTag, mName, values);
    }

    /**
     * Return a default value
     * @return An invalid resolution
     */
    @Override
    IppResolution getDefaultValue() {
        return new IppResolution(0,0, IppConstants.IppUnits.IPP_RES_PER_INCH);
    }

    /**
     * Builder for creating {@link IppResolutionAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<IppResolution> {

        /**
         * Builder constructor
         * @param name IPP attribute name
         */
        public Builder(String name) {
            super(IppConstants.IppTag.IPP_TAG_RESOLUTION, name);
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        public Builder addValue(IppResolution value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addValues(IppResolution... values) {
            if (values != null) {
                for(IppResolution value : values) {
                    if (value != null) mValues.add(value);
                }
            }
            return this;
        }

        /**
         * Build the {@link IppResolutionAttribute}
         * @return new {@link IppResolutionAttribute}
         */
        @Override
        public IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppResolutionAttribute(mTag, mName, mValues);
        }
    }
}
