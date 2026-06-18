// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.response;

import com.google.gson.annotations.SerializedName;

public class JobDetail {
    @SerializedName("TotalPages")
    private int totalPages;

    @SerializedName("NumberOfCopies")
    private int numberOfCopies;

    @SerializedName("PlexMode")
    private String plexMode;

    @SerializedName("State")
    private String state;

    @SerializedName("CompletionState")
    private String completionState;

    @SerializedName("StartTime")
    private String startTime;

    @SerializedName("CompletionTime")
    private String completionTime;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumberOfCopies() { return numberOfCopies; }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public String getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(String plexMode) {
        this.plexMode = plexMode;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCompletionState() { return completionState; }

    public void setCompletionState(String completionState) { this.completionState = completionState; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getCompletionTime() { return completionTime; }

    public void setCompletionTime(String completionTime) { this.completionTime = completionTime; }
}
