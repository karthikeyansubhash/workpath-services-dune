/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import android.os.Bundle;
import android.util.Log;

import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.scanner.Scanlet;

public class ScanJobStatusAdapter {
    private static final String TAG = Scanlet.TAG + "/JSAdap";

    static public void updateJobStatus(Bundle jobBundle, ScanJobStatus scanJobStatus) {
        if (jobBundle == null || scanJobStatus == null) {
            Log.e(TAG, "updateJobStatus() jobBundle or scanJobStatus is null (" + jobBundle + ", " + scanJobStatus + ")");
            return;
        }
        long totalImagesScanned = scanJobStatus.getTotalImagesScanned() != null ?
                scanJobStatus.getTotalImagesScanned().getValue() : 0;
        long totalImagesTransmitted = scanJobStatus.getTotalImagesTransmitted() != null ?
                scanJobStatus.getTotalImagesTransmitted().getValue() : 0;
        long totalImagesProcessed = scanJobStatus.getTotalImagesProcessed() != null ?
                scanJobStatus.getTotalImagesProcessed().getValue() : 0;

        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, (int) totalImagesScanned);
        jobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, (int) totalImagesTransmitted);

        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null) {
            Log.e(TAG, "updateJobStatus() jobInfo is null");
            return;
        }
        ScanJobData scanJobData = jobInfo.getJobData();
        if (scanJobData == null) {
            Log.e(TAG, "updateJobStatus() scanJobData is null");
            return;
        }
        scanJobData.setImagesScanned((int) totalImagesScanned);
        scanJobData.setImagesProcessed((int) totalImagesProcessed);
        scanJobData.setImagesTransmitted((int) totalImagesTransmitted);
        scanJobData.setJobState(ScanJobStatusAdapter.getScanJobState(scanJobStatus));
    }

    /**
     * Converts an E2 ScanJobStatus object to a Workpath ScanJobState object.
     *
     * @param scanJobStatus E2 ScanJobStatus object
     * @return Workpath ScanJobState object
     */
    static public ScanJobState getScanJobState(ScanJobStatus scanJobStatus) {
        ScanJobState jobState = new ScanJobState(ScanJobState.State.PENDING);
        jobState.setScanningState(
                ScanTypeMappingHelper.getJobActivityState(scanJobStatus.getScanningActivity())
        );
        jobState.setProcessingState(
                ScanTypeMappingHelper.getJobActivityState(
                        scanJobStatus.getProcessingActivity(),
                        scanJobStatus.getProcessingRestartCount() != null && scanJobStatus.getProcessingRestartCount().getValue() > 0
                )
        );
        jobState.setTransmittingState(
                ScanTypeMappingHelper.getJobActivityState(
                        scanJobStatus.getTransmittingActivity(),
                        scanJobStatus.getTransmissionRetryCount() != null && scanJobStatus.getTransmissionRetryCount().getValue() > 0
                )
        );
        jobState.setCancelingState(
                ScanTypeMappingHelper.getJobActivityState(scanJobStatus.getCancelingActivity())
        );

        //E2 does not update processing state, manually updates to make it completed when transmission is done
        if (jobState.getTransmittingState() == ScanJobState.ActivityState.COMPLETED) {
            jobState.setProcessingState(ScanJobState.ActivityState.COMPLETED);
        }
        return jobState;
    }
}
