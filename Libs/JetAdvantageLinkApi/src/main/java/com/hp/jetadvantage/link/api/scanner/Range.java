// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

/**
 * Provides range for retrieving capability of float parameters
 *
 * @since API 1
 */
public class Range {
    float mLowerBound;
    float mUpperBound;

    Range(float lowerBound, float upperBound) {
        this.mLowerBound = lowerBound;
        this.mUpperBound = upperBound;
    }

    Range(int lowerBound, int upperBound) {
        this.mLowerBound = lowerBound;
        this.mUpperBound = upperBound;
    }

    /**
     * Returns minimum supported value
     *
     * @return minimum boundary
     * @since API 1
     */
    @SuppressWarnings("unused")
    public float getLowerBound() {
        return mLowerBound;
    }

    /**
     * Returns maximum supported value
     *
     * @return maximum boundary
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public float getUpperBound() {
        return mUpperBound;
    }

    /**
     * Validates the provided value against this range
     *
     * @return boolean If data's valid, returns TRUE.
     *
     * @since API 1
     */
    @SuppressWarnings("unused")
    public boolean validate(float value) {
        return Float.compare(value, mLowerBound) >= 0 && Float.compare(value, mUpperBound) <= 0 && mLowerBound < mUpperBound;
    }

    /**
     * Validates the provided value against this range
     *
     * @return boolean If data's valid, returns TRUE.
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public boolean validate(int value) {
        return value >= (int) mLowerBound && value <= (int) mUpperBound && mLowerBound < mUpperBound;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "[" + mLowerBound + ", " + mUpperBound + "]";
    }
}
