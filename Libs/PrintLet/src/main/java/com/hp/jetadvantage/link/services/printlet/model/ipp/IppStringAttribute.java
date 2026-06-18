// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * {@link IppAttribute} containing {@link String} values
 */
public final class IppStringAttribute extends IppAttribute<String> {

    /**
     * String locate
     */
    final Locale mStringLocale;

    private IppStringAttribute(int tag, String name, List<String> values, Locale stringLocale) {
        super(tag, name, values);
        mStringLocale = stringLocale;
    }

    /**
     * Additional equality checks. Checks if specified object is an {@link IppStringAttribute} and their
     * locales match.
     * @param obj Object to check
     * @return True if objects are equivalent
     */
    @Override
    boolean additionalEqualChecks(Object obj) {
        if (obj instanceof IppStringAttribute) {
            IppStringAttribute other = (IppStringAttribute)obj;
            return ((mStringLocale != null)
                    && mStringLocale.equals(other.mStringLocale));
        }
        return true;
    }

    /**
     * Compute the additional hash code value for this object
     * @return Additional hash code value
     */
    @Override
    int additionalHashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((mStringLocale != null) ? mStringLocale.hashCode() : 0);
        return result;
    }

    /**
     * Return the string locale
     * @return String locale
     */
    @SuppressWarnings("unused")
    public Locale getStringLocale() {
        return mStringLocale;
    }

    /**
     * Combine the attributes into a new
     * @param attribute Attribute being appended
     * @return New {@link IppStringAttribute} containing combined attribute values
     */
    @Override
    protected IppAttribute appendToVerified(IppAttribute attribute) {
        IppStringAttribute other = (IppStringAttribute)attribute;
        List<String> values = new ArrayList<String>(mValues);
        values.addAll(other.mValues);
        return new IppStringAttribute(mTag, mName, values, mStringLocale);
    }

    /**
     * Return a default value
     * @return An empty string
     */
    @Override
    String getDefaultValue() {
        return "";
    }

    /**
     * Builder for creating {@link IppStringAttribute}
     */
    public static final class Builder extends IppAttribute.Builder<String> {
        /** String locale */
        private Locale mLocale = IppConstants.DEFAULT_LOCALE;
        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        public Builder(int tag, String name) {
            super(tag, name);
        }

        /**
         * Builder constructor
         * @param tag IPP attribute type
         * @param name IPP attribute name
         */
        public Builder(IppConstants.IppTag tag, String name) {
            this(tag.getValue(), name);
        }

        /**
         * Set the attribute locale
         * @param locale Locale of attribute
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder setLocale(Locale locale) {
            mLocale = locale;
            return this;
        }

        /**
         * Add a value to the attribute
         * @param value Value to add
         * @return This builder
         */
        public Builder addValue(String value) {
            return addValues(value);
        }

        /**
         * Add the provided values to the attribute
         * @param values Values to add
         * @return This builder
         */
        public Builder addValues(String... values) {
            if ((values != null) && (values.length > 0)) {
                mValues.addAll(Arrays.asList(values));
            }
            return this;
        }

        /**
         * Build the {@link IppStringAttribute}
         * @return new {@link IppStringAttribute}
         */
        @Override
        public IppAttribute build() {
            if (mValues.isEmpty()) throw new IllegalArgumentException("empty set");
            return new IppStringAttribute(mTag, mName, mValues, ((mLocale != null) ? mLocale : IppConstants.DEFAULT_LOCALE));
        }
    }
}
