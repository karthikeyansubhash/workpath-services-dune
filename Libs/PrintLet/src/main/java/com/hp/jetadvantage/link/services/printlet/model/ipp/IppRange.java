// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

/**
 * Object representing an IPP range
 */
@SuppressWarnings("WeakerAccess")
public class IppRange {
    /** Range lower bound */
    @SuppressWarnings("WeakerAccess")
    public final int mLowerBound;
    /** range upper bound */
    @SuppressWarnings("WeakerAccess")
    public final int mUpperBound;

    /**
     * Constructor
     * @param lowerBound range lower bound
     * @param upperBound range upper bound
     */
    public IppRange(int lowerBound, int upperBound) {
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
    }

    /**
     * Returns the hash code value for this object
     * @return The hash code value for the object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mLowerBound;
        result = prime * result + mUpperBound;
        return result;
    }

    /**
     * Compares the specified object with this one for equality. Returns true if only if the specified object
     * is also an IppRange with equal ranges.
     * @param obj Object to be compared  for equality
     * @return True if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IppRange)) return false;
        IppRange other = (IppRange)obj;
        return ((this.mLowerBound == other.mLowerBound) && (this.mUpperBound == other.mUpperBound));
    }
}
