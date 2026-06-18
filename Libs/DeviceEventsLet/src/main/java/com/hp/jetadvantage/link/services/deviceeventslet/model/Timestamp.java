package com.hp.jetadvantage.link.services.deviceeventslet.model;

/**
 * Represents a timestamp with an offset and time.
 */
public class Timestamp {
    private int offset;
    private String time;

    /**
     * Returns the offset of the timestamp.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the offset of the timestamp.
     * @param offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Returns the time of the timestamp.
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the timestamp.
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
}