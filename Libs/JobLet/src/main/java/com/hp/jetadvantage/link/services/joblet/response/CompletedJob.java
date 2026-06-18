// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.response;

import com.google.gson.annotations.SerializedName;

public class CompletedJob {
    @SerializedName("jobDetail")
    private JobDetail jobDetail;

    @SerializedName("status")
    private String status;

    @SerializedName("completionState")
    private String completionState;

    @SerializedName("userName")
    private String userName;

    @SerializedName("jobName")
    private String jobName;

    @SerializedName("jobType")
    private String jobType;

    @SerializedName("fullyQualifiedName")
    private String fullyQualifiedName;

    @SerializedName("jobCategory")
    private String jobCategory;

    @SerializedName("completionTime")
    private String completionTime;

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletionState() {
        return completionState;
    }

    public void setCompletionState(String completionState) {
        this.completionState = completionState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) { this.fullyQualifiedName = fullyQualifiedName; }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }
}
