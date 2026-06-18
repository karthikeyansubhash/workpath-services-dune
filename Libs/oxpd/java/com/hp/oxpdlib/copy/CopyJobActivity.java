// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public class CopyJobActivity {
    public CopyJobActivityType activityType;
    public String timestamp;

    /**
     * Default no-arg constructor
     * 
     */
    public CopyJobActivity() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param activityType
     *     the CopyJobActivityType
     * @param timestamp
     *     the XMLGregorianCalendar
     */
    public CopyJobActivity(final CopyJobActivityType activityType, final String timestamp) {
        this.activityType = activityType;
        this.timestamp = timestamp;
    }

    /**
     * String representation of CopyJobActivity
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("activityType=").append(((activityType == null)?"null":activityType.toString())).append(", ").append("timestamp=").append(timestamp).toString();
    }
}
