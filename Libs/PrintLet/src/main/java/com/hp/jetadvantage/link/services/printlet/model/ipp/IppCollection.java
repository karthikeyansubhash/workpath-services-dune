// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of {@link IppAttribute}
 */
@SuppressWarnings("WeakerAccess")
public class IppCollection {

    /** IPP attributes in this collection */
    private final List<IppAttribute> mAttributes;

    /**
     * Constructor
     * @param attributes IPP attributes in collection
     */
    private IppCollection(List<IppAttribute> attributes) {
        mAttributes = Collections.unmodifiableList(attributes);
    }

    /**
     * Get the list of attributes
     * @return List of attributes in collection
     */
    public List<IppAttribute> getAttributes() {
        return mAttributes;
    }

    /**
     * Get the number of attributes in this collection
     * @return The number of attributes
     */
    @SuppressWarnings("WeakerAccess")
    public int getAttributeCount() {
        return mAttributes.size();
    }

    /**
     * Return the IPP attribute at the specified position
     * @param index the index of the IPP attribute to return
     * @return The IPP attribute at the specified position
     */
    public IppAttribute get(int index) {
        if ((index < 0) || (index >= getAttributeCount())) return null;
        return mAttributes.get(index);
    }

    /**
     * Returns the hash code value for this IPP collection
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        return mAttributes.hashCode();
    }

    /**
     * Compares the specified object with this IPP collection for equality. Returns true if and only if
     * the specified object is also an IppCollection whose attributes are also equal
     * @param obj The object to be compared for equality
     * @return True if the specified object is equal
     */
    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof IppCollection) && mAttributes.equals(((IppCollection)obj).mAttributes));
    }

    /**
     * Find the specified attribute in the collection
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return The first instance of this attribute or null if not found
     */
    @SuppressWarnings("unused")
    public IppAttribute findAttribute(IppConstants.IppTag tag, String name) {
        return findAttribute(tag.getValue(), name);
    }

    /**
     * Find the specified attribute in the collection
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return The first instance of this attribute or null if not found
     */
    public IppAttribute findAttribute(int tag, String name) {
        for(IppAttribute attr : mAttributes) {
            if (TextUtils.equals(name, attr.mName)
                    && (tag == attr.mTag)) return attr;
        }
        return null;
    }

    /**
     * Find the specified attribute in the collection, matching any of the given tags.
     * Used for attributes that may be encoded as either keyword or name depending on the printer.
     * @param tags Array of IPP tag values to match
     * @param name IPP attribute name
     * @return The first instance of this attribute or null if not found
     */
    public IppAttribute findAttributeByAnyTag(int[] tags, String name) {
        for (IppAttribute attr : mAttributes) {
            if (TextUtils.equals(name, attr.mName)) {
                for (int tag : tags) {
                    if (tag == attr.mTag) return attr;
                }
            }
        }
        return null;
    }

    /**
     * Find all instances of the specified attribute in the collection
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return All instances of this attribute
     */
    @SuppressWarnings("unused")
    public List<IppAttribute> findAllAttributes(IppConstants.IppTag tag, String name) {
        return findAllAttributes(tag.getValue(), name);
    }

    /**
     * Find all instances of the specified attribute in the collection
     * @param tag IPP attribute type
     * @param name IPP attribute name
     * @return All instances of this attribute
     */
    public List<IppAttribute> findAllAttributes(int tag, String name) {
        List<IppAttribute> attributes = new ArrayList<IppAttribute>();
        for(IppAttribute attr : mAttributes) {
            if (TextUtils.equals(name, attr.mName)
                    && (tag == attr.mTag)) attributes.add(attr);
        }
        return attributes;
    }

    /** Builder for creating {@link IppCollection} */
    public static class Builder {

        /** IPP attributes in collection */
        private List<IppAttribute> mAttributes = new ArrayList<IppAttribute>();

        /**
         * Constructor
         */
        public Builder() {
        }

        /**
         * Add an attribute to the collection
         * @param attribute Attribute to add to collection
         * @return This builder
         */
        public Builder addAttribute(IppAttribute attribute) {
            return addAttributes(attribute);
        }

        /**
         * Add attributes to the collection
         * @param attributes Attributes to add to collection
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addAttributes(IppAttribute... attributes) {
            if (attributes != null) {
                for (IppAttribute attr : attributes) {
                    if (attr != null) mAttributes.add(attr);
                }
            }
            return this;
        }

        /**
         * Add attribute in collection to this one
         * @param collection Collection of attributes to add
         * @return This builder
         */
        @SuppressWarnings("WeakerAccess")
        public Builder addAttributes(IppCollection collection) {
            mAttributes.addAll(collection.mAttributes);
            return this;
        }

        /**
         * Return the current attribute list
         * @return List of attributes
         */
        List<IppAttribute> getAttributes() {
            return mAttributes;
        }

        /**
         * Build the IPP collection
         * @return new IppCollection
         */
        public IppCollection build() {
            return new IppCollection(mAttributes);
        }
    }
}
