package com.hp.jetadvantage.link.services.printlet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;

import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.intents.PrintJobCancelIntent;
import com.hp.jetadvantage.link.services.printlet.ipp.IppClient;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.StateMachinePool;
import com.hp.jetadvantage.link.services.printlet.ipp.IppConnector;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.FileType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintJobIntentService extends BaseJobIntentService {

    public static final String DOWNLOAD_FILE_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public static final Map<FileType, List<String>> EXTENSIONS = new HashMap<>();

    static {
        EXTENSIONS.put(FileType.Jpeg, Arrays.asList("jpeg", "jpg", "jpe", "jfif"));
        EXTENSIONS.put(FileType.PDF, Collections.singletonList("pdf"));
        EXTENSIONS.put(FileType.Text, Collections.singletonList("txt"));
        EXTENSIONS.put(FileType.PS, Collections.singletonList("ps"));
        EXTENSIONS.put(FileType.Tiff, Arrays.asList("tif", "tiff"));
        EXTENSIONS.put(FileType.PCL5, Arrays.asList("pcl", "prn"));
        EXTENSIONS.put(FileType.PCL6, Arrays.asList("pcl", "prn"));
    }

    public PrintJobIntentService() {
        super(PrintJobIntentService.class.getSimpleName());
        TAG = TAG + "/Print";
    }

    @Override
    protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
        SLog.e(TAG, "createStateMachine");
        return new PrintJobIntentServiceStateMachine(service, looper);
    }

    @Override
    protected boolean cancelJob(Intent intent) {
        String requestJobId = PrintJobCancelIntent.getJobId(intent);
        if (requestJobId == null) {
            SLog.e(TAG, "cancelJob : Expected parameters not found. ignore");
            return false;
        }

        StateMachinePool pool = getStateMachinePool();
        if (pool != null && pool.findByJobId(requestJobId) == null) {
            SLog.w(TAG, "cancelJob : jobId " + requestJobId + " not found in active pool, forwarding to device anyway");
        }

        return cancelPrintJob(Integer.parseInt(requestJobId));
    }

    @Override
    protected String getJobCancelAction() {
        return PrintJobCancelIntent.ACTION;
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        final PrintRequestIntent.IntentParams reqParams = PrintRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();

        reportBusyToApp(rid, reqParams.getPackageName());
    }

    protected boolean cancelPrintJob(int jobId) {
        try {
            Message cancelResponse = IppClient.getInstance().cancelJob(this, 0, jobId);
            if (cancelResponse.arg1 == IppConnector.Constants.REQUEST_RETURN_CODE__OK) {
                SLog.i(TAG, "Print job(" + jobId + ") is successfully cancelled");
                return true;
            } else if (cancelResponse.obj instanceof Error) {
                SLog.e(TAG, "Job cancel failed, probably already completed or canceled");
                return false;
            }
        } catch (Error e) {
            SLog.e(TAG, "Job cancel error " + e.getMessage());
        }
        return false;
    }
}
