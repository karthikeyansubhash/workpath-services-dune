// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

public class CopyJobEventData {
    public List<CopyJobActivity> copyJobActivities;
    public String jobId;
    public long ordinal;
    public XMLGregorianCalendar timestamp;
    public String serverContextId;
    public CopyJobStatus status;

    /**
     * Default no-arg constructor
     * 
     */
    public CopyJobEventData() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param jobId
     *     the String
     * @param copyJobActivities
     *     the List<CopyJobActivity>
     * @param serverContextId
     *     the String
     * @param ordinal
     *     the long
     * @param timestamp
     *     the XMLGregorianCalendar
     * @param status
     *     the CopyJobStatus
     */
    public CopyJobEventData(final List<CopyJobActivity> copyJobActivities, final String jobId, final long ordinal, final XMLGregorianCalendar timestamp, final String serverContextId, final CopyJobStatus status) {
        this.copyJobActivities = copyJobActivities;
        this.jobId = jobId;
        this.ordinal = ordinal;
        this.timestamp = timestamp;
        this.serverContextId = serverContextId;
        this.status = status;
    }

    /**
     * String representation of CopyJobEventData
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append(((copyJobActivities == null)?"null":(("{"+ copyJobActivities.toString().substring(1, copyJobActivities.toString().lastIndexOf("]")))+"}"))).append(", ").append("jobId=").append(jobId).append(", ").append("ordinal=").append(ordinal).append(", ").append("timestamp=").append(((timestamp == null)?"null":toSimpleDate(timestamp))).append(", ").append("serverContextId=").append(serverContextId).append(", ").append("status=").append(((status == null)?"null":status.toString())).toString();
    }

    /**
     * String representation of a XMLGregorianCalendar in SimpleDate format
     * 
     * @param dateTime
     *     the XMLGregorianCalendar
     * @return
     *     String representation of a XMLGregorianCalendar in a simple date format
     */
    public String toSimpleDate(XMLGregorianCalendar dateTime) {
        Calendar calendar = dateTime.toGregorianCalendar();
        if (calendar.getTime() == null) return "null";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "";
        try{
        dateString = sdf.format(calendar.getTime());
        } catch (Exception ignore) {}
        return dateString;
    }

}
