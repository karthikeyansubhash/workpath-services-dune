// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

/**
 * Object representing an IPP resolution object
 */
@SuppressWarnings("WeakerAccess")
public class IppResolution {
    /** X resolution */
    @SuppressWarnings("WeakerAccess")
    public final int mXres;
    /** Y resolution */
    @SuppressWarnings("WeakerAccess")
    public final int mYres;
    /** IPP units */
    @SuppressWarnings("WeakerAccess")
    public final IppConstants.IppUnits mUnits;

    /** Constructor
     * @param xres x resolution
     * @param yres y resolution
     * @param units units
     */
    @SuppressWarnings("WeakerAccess")
    public IppResolution(int xres, int yres, IppConstants.IppUnits units) {
        mXres = xres;
        mYres = yres;
        mUnits = units;
    }

    /**
     * Returns the hash code value for this object
     * @return The hash code value for the object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mXres;
        result = prime * result + mYres;
        result = prime * result + mUnits.getValue();
        return result;
    }

    /**
     * Compares the specified object with this one for equality. Returns true if only if the specified object
     * is also an IppResolution with equal x & y resolutions and unit values.
     * @param obj Object to be compared  for equality
     * @return True if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IppResolution)) return false;
        IppResolution other = (IppResolution)obj;
        return ((this.mXres == other.mXres) && (this.mYres == other.mYres) && (this.mUnits == other.mUnits));
    }
}
