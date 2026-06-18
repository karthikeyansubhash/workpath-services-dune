// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base IPP attribute class
 * @param <AttributeValueKind> The type of value contained by this attribute
 */
public abstract class IppAttribute<AttributeValueKind> {

    /** Ipp attribute type, primarily based on values from {@link IppConstants.IppTag} */
    final int mTag;

    /** IPP attribute name */
    final String mName;

    /** List of values */
    final List<AttributeValueKind> mValues;

    /** Constructor
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    protected IppAttribute(int tag, String name, List<AttributeValueKind> values) {
        mTag = tag;
        mName = name;
        if (values == null) {
            mValues = Collections.emptyList();
        } else {
            mValues = Collections.unmodifiableList(values);
        }
    }

    /**
     * Constructor
     * @param tag  IPP attribute type
     * @param name IPP attribute name
     * @param values Values associated with attribute
     */
    protected IppAttribute(IppConstants.IppTag tag, String name, List<AttributeValueKind> values) {
        this(tag.getValue(), name, values);
    }

    /**
     * Method to merge two attributes
     * @param attr1 Attribute that is being appended to
     * @param attr2 Attribute being appended
     * @return Merge attribute result
     */
    static IppAttribute merge(IppAttribute attr1, IppAttribute attr2) {
        // nothing to do
        if (attr1 == null) return null;
        // nothing to do
        if (attr2 == null) return attr1;
        // check name
        if (TextUtils.isEmpty(attr1.mName)) return null;
        // check if merge possible; name, tag, and subclass must match
        if (attr1.getClass().equals(attr2.getClass())
                && TextUtils.isEmpty(attr2.mName)
                && (attr1.mTag == attr2.mTag)) {
            // merge by rules defined in subclass
            return attr1.appendToVerified(attr2);
        }
        // return original attribute
        return attr1;
    }

    /**
     * Merge attribute based on derived-class rules
     * @param attribute Attribute being appended
     * @return Merged attribute result
     */
    abstract IppAttribute appendToVerified(IppAttribute attribute);

    /**
     * Return a default value
     * @return Default value
     */
    abstract AttributeValueKind getDefaultValue();

    /**
     * Return the number of values this attribute holds
     * @return Number of values in attribute
     */
    @SuppressWarnings("WeakerAccess")
    public final int getCount() {
        return mValues.size();
    }

    /**
     * Return element value at specified position in the attribute
     * @param index The index of the element to return
     * @return The element at the specified position
     */
    public final AttributeValueKind get(int index) {
        // validate range
        if ((index < 0) || (index >= getCount())) return getDefaultValue();
        return mValues.get(index);
    }

    /**
     * Get all the values associated with this attribute
     * @return List of values in attribute
     */
    public final List<AttributeValueKind> getValues() { return mValues; }

    /**
     * Return the attribute tag
     * @return The attribute tag
     */
    public final int getTag() {
        return mTag;
    }

    /**
     * Return the attribute name
     * @return The attribute name
     */
    public final String getName() {
        return mName;
    }

    /**
     * Returns the hash code value for this attribute
     * @return Attribute hash code value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mTag;
        result = prime * result + mName.hashCode();
        result = prime * result + mValues.hashCode();
        result = prime * result + additionalHashCode();
        return result;
    }

    /**
     * Compares the specified object with this attribute for equality. Returns true if and only if
     * the specified object is of the same subclass, has the same name, type, and values in their lists.
     * @param obj The object to be compared for equality
     * @return true if the specified object is equal
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (!getClass().equals(obj.getClass())) return false;
        IppAttribute other = (IppAttribute)obj;
        return ((this.mTag == other.mTag) && TextUtils.equals(this.mName, other.mName) && this.mValues.equals(other.mValues) && additionalEqualChecks(other));
    }

    /**
     * Additional equality checks
     * @param obj Object to check
     * @return True additional checks passed or not
     */
    boolean additionalEqualChecks(Object obj) {
        return (obj instanceof IppAttribute);
    }

    /**
     * Additional hashCode computations
     * @return Additional hash code value
     */
    int additionalHashCode() {
        return 0;
    }

    /**
     * Base attribute builder class
     * @param <AttributeValueKind>
     */
    static abstract class Builder<AttributeValueKind> {

        /** IPP tag */
        final int mTag;
        /** IPP name */
        final String mName;
        /** List of attribute values */
        final List<AttributeValueKind> mValues = new ArrayList<AttributeValueKind>();

        /** Constructor */
        public Builder(int tag, String name) {
            mTag = tag;
            mName = name;
        }

        /** Constructor */
        public Builder(IppConstants.IppTag tag, String name) {
            this(tag.getValue(), name);
        }

        /** Build the ipp attribute */
        public abstract IppAttribute build();
    }
}
