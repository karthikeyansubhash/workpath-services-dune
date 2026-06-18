// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

/**
 * Provides margin
 *
 * @since API 5
 */
public class Margins {
    float mTop;
    float mLeft;
    float mRight;
    float mBottom;

    public Margins(float left, float top, float right, float bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    /**
     * Returns top margin
     *
     * @return top margin
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getTopMargin() {
        return mTop;
    }

    /**
     * Returns left margin
     *
     * @return left margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getLeftMargin() {
        return mLeft;
    }

    /**
     * Returns right margin
     *
     * @return right margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getRightMargin() {
        return mRight;
    }

    /**
     * Returns bottom margin
     *
     * @return bottom margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getBottomMargin() {
        return mBottom;
    }

    /**
     * set top margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setTopMargin(float top) {
        this.mTop = top;
    }

    /**
     * set left margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setLeftMargin(float left) {
        this.mLeft = left;
    }

    /**
     * set right margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setRightMargin(float right) {
        this.mRight = right;
    }

    /**
     * set bottom margin
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setBottomMargin(float bottom) {
        this.mBottom = bottom;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "[left: " + mLeft + ", top: " + mTop + ", right: " + mRight + ", bottom: " + mBottom +"]";
    }
}
