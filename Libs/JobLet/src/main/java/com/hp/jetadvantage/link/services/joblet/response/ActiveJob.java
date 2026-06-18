// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.response;

import com.google.gson.annotations.SerializedName;

public class ActiveJob {
    @SerializedName("jobDetail")
    private JobDetail jobDetail;

    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;

    @SerializedName("userName")
    private String userName;

    @SerializedName("jobName")
    private String jobName;

    @SerializedName("jobType")
    private String jobType;

    @SerializedName("fullyQualifiedName")
    private String fullyQualifiedName;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }
}
