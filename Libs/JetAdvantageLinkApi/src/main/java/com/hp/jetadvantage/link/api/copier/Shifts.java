package com.hp.jetadvantage.link.api.copier;

public class Shifts {
    float mXShift;
    float mYShift;

    public Shifts(float xShift, float yShift) {
        this.mXShift = xShift;
        this.mYShift = yShift;
    }

    /**
     * Returns X Shift
     *
     * @return X Shift
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getXShift() {
        return mXShift;
    }

    /**
     * Returns Y Shift
     *
     * @return Y Shift
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public float getYShift() {
        return mYShift;
    }

    /**
     * set X Shift
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setXShift(float xShift) {
        this.mXShift = xShift;
    }

    /**
     * set Y Shift
     *
     * @since API 5
     */
    @SuppressWarnings("unused")
    public void setYShift(float yShift) {
        this.mYShift = yShift;
    }
    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "[xShift: " + mXShift + ", yShift: " + mYShift + "]";
    }
}