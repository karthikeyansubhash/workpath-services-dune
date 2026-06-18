// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

/**
 * Range for retrieving capability of integer parameters
 *
 * @since API 3
 */
public class Range {
    int mLowerBound;
    int mUpperBound;

    Range(int lowerBound, int upperBound) {
        this.mLowerBound = lowerBound;
        this.mUpperBound = upperBound;
    }

    /**
     * Returns minimum supported value
     *
     * @return minimum
     *
     * @since API 3
     */
    public int getLowerBound() {
        return mLowerBound;
    }

    /**
     * Returns maximum supported value
     *
     * @return maximum
     *
     * @since API 3
     */
    public int getUpperBound() {
        return mUpperBound;
    }

    /**
     * Validates the provided value against this range
     *
     * @return true if value is within the range
     *
     * @since API 3
     */
    public boolean validate(int value) {
        return value >= mLowerBound && value <= mUpperBound && mLowerBound <= mUpperBound;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "[" + mLowerBound + ", " + mUpperBound + "]";
    }
}
