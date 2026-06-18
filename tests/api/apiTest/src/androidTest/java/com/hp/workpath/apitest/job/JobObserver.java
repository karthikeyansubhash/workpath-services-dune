package com.hp.workpath.apitest.job;

import android.os.Handler;
import android.util.Log;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.job.JobInfo;
import com.hp.workpath.api.job.JobService;

import java.util.concurrent.CountDownLatch;

public class JobObserver extends JobService.AbstractJobletObserver {
    private static final String TAG = JobObserver.class.getCanonicalName();
    private final CountDownLatch jobEndLatch;
    private final CountDownLatch jobProgressLatch;
    private boolean isCompleteEventReceived = false;
    private boolean isProgressEventReceived = false;
    private boolean isFailEventReceived = false;
    private boolean isCancelEventReceived = false;
    private String requestId;
    private String jobIdentifier;
    private JobInfo jobInfo;

    public JobObserver(Handler handler, CountDownLatch jobEndLatch, CountDownLatch jobProgressLatch) {
        super(handler);
        this.jobEndLatch = jobEndLatch;
        this.jobProgressLatch = jobProgressLatch;
    }

    @Override
    public void onComplete(String requestId, JobInfo jobInfo) {
        isCompleteEventReceived = true;
        this.requestId = requestId;
        this.jobInfo = jobInfo;
        if (jobEndLatch != null) {
            jobEndLatch.countDown();
        }
    }

    @Override
    public void onProgress(String requestId, JobInfo jobInfo) {
        this.requestId = requestId;
        this.jobInfo = jobInfo;
        this.jobIdentifier = jobInfo.getJobId();
        isProgressEventReceived = true;

        Log.i(TAG, "JobObserver onProgress : jobIdentifier = " + jobIdentifier);
        if (jobProgressLatch != null) {
            jobProgressLatch.countDown();
        }
    }

    @Override
    public void onFail(String requestId, Result result) {
        isFailEventReceived = true;
        Log.i(TAG, "JobObserver onFail : requestId = " + requestId + ", result = " + result.toString());
        this.requestId = requestId;
        if (jobEndLatch != null) {
            jobEndLatch.countDown();
        }
    }

    @Override
    public void onCancel(String requestId) {
        if (this.requestId != null && this.requestId.equals(requestId)) {
            isCancelEventReceived = true;
            this.requestId = requestId;
            if (jobEndLatch != null) {
                jobEndLatch.countDown();
            }
        }
    }

    public boolean isCompleteEventReceived() {
        return isCompleteEventReceived;
    }

    public boolean isProgressEventReceived() {
        return isProgressEventReceived;
    }

    public boolean isFailEventReceived() {
        return isFailEventReceived;
    }

    public boolean isCancelEventReceived() {
        return isCancelEventReceived;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getJobIdentifier() {
        return jobIdentifier;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }
}