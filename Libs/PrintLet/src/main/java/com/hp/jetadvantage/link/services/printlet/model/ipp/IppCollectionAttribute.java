// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IppAttribute} containing {@link IppCollection} values
 */
@SuppressWarnings("WeakerAccess")
public final class IppCollectionAttribute extends IppAttribute<IppCollection> {
    /**
     * Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    private IppCollectionAttribute(int tag, String name, List<IppCollection> values) {
        super( tag, name, values);
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppCollectionAttribute} containing combined attribute values
     */
    @Override
    IppAttribute appendToVerified(IppAttribute attribute) {
        IppCollectionAttribute other = (IppCollectionAttribute)attribute;
        List<IppCollection> values = new ArrayList<IppCollection>(mValues);
        values.addAll(other.mValues);
        return new IppCollectionAttribute(mTag, mName, values);
    }

    /**
     * Return a default value
     * @return An empty collection
     */
    @Override
    IppCollection getDefaultValue() {
        return new IppCollection.Builder().build();
    }

    /**
     * Builder for creating {@link IppCollectionAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<IppCollection> {

        /**
         * Constructor
         * @param name IPP attribute name
         */
        public Builder(String name) {
            super(IppConstants.IppTag.IPP_TAG_BEGIN_COLLECTION, name);
        }

        /**
         * Add an {@link IppCollection} to this attribute
         * @param value Value to add
         * @return This builder
         */
        public Builder addValue(IppCollection value) {
            return addValues(value);
        }

        /**
         * Add a list of {@link IppCollection} to this attribute
         * @param values Values to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addValues(IppCollection... values) {
            if ((values != null) && (values.length > 0)) {
                for(IppCollection value : values) {
                    if (value != null) mValues.add(value);
                }
            }
            return this;
        }

        /**
         * Build the {@link IppCollectionAttribute}
         * @return new {@link IppCollectionAttribute}
         */
        @Override
        public IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppCollectionAttribute(mTag, mName, mValues);
        }
    }
}
