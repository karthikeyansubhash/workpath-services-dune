// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.service;

import androidx.annotation.Nullable;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OXPCreatePrintSpoolerIntentService extends IntentService {

    public static final String ACTION_PRINT_FINISH = "com.hp.jetadvantage.link.services.printlet.service.OXPCreatePrintSpoolerIntentService.ACTION_PRINT_FINISH";
    public static final String ACTION_CANCEL_CURRENT_BATCH = "com.hp.jetadvantage.link.services.printlet.service.OXPCreatePrintSpoolerIntentService.ACTION_CANCEL_CURRENT_BATCH";

    private static final String TAG = Printlet.TAG + "/OXPCreatePSIS";
    private static final String EXTRA_PARAMS = "params";
    public static final String EXTRA_IS_ERROR = "is_error";

    public static final String EXTRA_IS_BACKGROUNDJOB = "is_backgroundjob";
    public static final String EXTRA_IS_FIRSTJOB = "is_firstjob";
    public static final String EXTRA_IS_LASTJOB = "is_lastjob";

    private static final int STORE_JOB_LIMIT = 300;

    private AtomicBoolean mIsProcessing = new AtomicBoolean(false);
    private HashMap<String, SpoolElement> mRidHashMap;
    private LinkedList<SpoolElement> mSpoolerQueue;
    private static int sJobCount = 0;
    //private SpoolerHandler mSpoolerHandler;
    //private HandlerThread mHandlerThread;

    private final Handler mHandler = new Handler(Looper.myLooper());;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public OXPCreatePrintSpoolerIntentService() {
        super(OXPCreatePrintSpoolerIntentService.class.getSimpleName());
    }

    /*@Override
    protected void finalize() throws Throwable {
        super.finalize();
        SLog.d(TAG, "finalize()");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mJobFinishedReceiver);
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        mRidHashMap = new HashMap<String, SpoolElement>();
        mSpoolerQueue = new LinkedList<SpoolElement>();
        LocalBroadcastManager.getInstance(this).registerReceiver(mJobFinishedReceiver, new IntentFilter(ACTION_PRINT_FINISH));
        LocalBroadcastManager.getInstance(this).registerReceiver(mJobFinishedReceiver, new IntentFilter(ACTION_CANCEL_CURRENT_BATCH));

        SLog.d(TAG, "onCreate");
        /*mHandlerThread = new HandlerThread(TAG + ":" + getClass().getSimpleName(),
                android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        mSpoolerHandler = new SpoolerHandler(mHandlerThread.getLooper());*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ServiceNotification.showNotification(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SLog.d(TAG, "onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mJobFinishedReceiver);
        if (mRidHashMap != null && !mRidHashMap.isEmpty()) {
            mRidHashMap.clear();
            mRidHashMap = null;
        }
        if (mSpoolerQueue != null && !mSpoolerQueue.isEmpty()) {
            mSpoolerQueue.clear();
            mSpoolerQueue = null;
        }
        sJobCount = 0;
        /*if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }*/
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) { }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (!intent.hasExtra(EXTRA_PARAMS) || intent.getBundleExtra(EXTRA_PARAMS) == null) {
            SLog.e(TAG, "Expected parameters not found");
            return START_NOT_STICKY;
        }
        SLog.e(TAG, "onStartCommand 1");

        final Bundle extraParams = intent.getBundleExtra(EXTRA_PARAMS);
        PrintRequestIntent.IntentParams params = PrintRequestIntent.getIntentParams(extraParams);
        PrintletAttributes taskAttr = params.getTaskAttributes();

        boolean isBackgroundJob = taskAttr != null ? taskAttr.getBackgroundJob() : false;
        String rid = params.getReqId();
        // DUNE
        extraParams.putBoolean(EXTRA_IS_BACKGROUNDJOB, isBackgroundJob);

        // if the requested job is a foreground job,
        // immediately send it to create service so that the sender client(app) get a failed report.
        // add a requested PAs on hashmap and queue
        if (mRidHashMap.containsKey(rid)) {
            // has a job
            SLog.e(TAG, "has a job");
            SpoolElement se = mRidHashMap.get(rid);
            if (se.isFailed()) {
                // ignore
                // if spoolerQueue is empty then stopSelf()
                SLog.e(TAG, "ignore1");

                return START_NOT_STICKY;
            }
            // add to se's array
            ++sJobCount;
            extraParams.putBoolean(EXTRA_IS_FIRSTJOB, false);
            se.enqueue(extraParams);
        } else {
            // no such rid
            SLog.e(TAG, "no such rid: " + rid);
            // if new job is foreground
            // 1. create a new SpoolElement
            SpoolElement se = new SpoolElement(rid, isBackgroundJob);
            ++sJobCount;
            extraParams.putBoolean(EXTRA_IS_FIRSTJOB,true);
            se.enqueue(extraParams);

            // 2. add it to hashmap and queue
            mRidHashMap.put(rid, se);
            addElementToQueue(se);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPrint();
            }
        }, 200);

        return START_NOT_STICKY;
    }

    /*
    If an element is background job, it is added to the last in the queue.
    If an element is foreground job, it is added to the last of the foreground jobs already added to the queue.
     */
    private synchronized void addElementToQueue(SpoolElement se) {
        if(se.isBackgroundJob() || mSpoolerQueue.isEmpty()) {
            mSpoolerQueue.add(se);
            return;
        }
        LinkedList<SpoolElement> tempQueue = new LinkedList<SpoolElement>();
        SpoolElement tempElement = null;
        while (!mSpoolerQueue.isEmpty()) {
            tempElement = mSpoolerQueue.poll();
            if(tempElement.isBackgroundJob()){
                mSpoolerQueue.addFirst(tempElement);
                break;
            } else {
                tempQueue.add(tempElement);
                continue;
            }
        }
        tempQueue.add(se);
        tempQueue.addAll(mSpoolerQueue);
        mSpoolerQueue = tempQueue;
    }

    public static int getJobCount() {
        return sJobCount;
    }

    public static int getAvailableJobCount() {
        return STORE_JOB_LIMIT - sJobCount;
    }

    private BroadcastReceiver mJobFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_PRINT_FINISH.equals(intent.getAction())) {
                mIsProcessing.set(false);
                if (mSpoolerQueue.isEmpty()) {
                    SLog.d(TAG, "No more job is registered. Finish the service.");
                    stopSelf();
                } else {
                    // if a header of spoolerQueue has nothing to process
                    // then remove the header
                    if (mSpoolerQueue.peek().isEmptyQueue()) {
                        SLog.d(TAG, "if a header of spoolerQueue has nothing to process then remove the header");
                        mRidHashMap.remove(mSpoolerQueue.remove().getRid());
                        if (mSpoolerQueue.isEmpty() || mSpoolerQueue.peek().isFailed()) {
                            SLog.d(TAG, "Nothing to go on. SpoolerQueue is empty.");
                            stopSelf();
                            return;
                        }
                    }
                    SLog.d(TAG, "Another print job started");
                    startPrint();
                }
            } else if (ACTION_CANCEL_CURRENT_BATCH.equals(intent.getAction())) {
                // if cancel request is received, then clear all related jobs.
                boolean isError = intent.getBooleanExtra(EXTRA_IS_ERROR, false);
                SLog.d(TAG, "Clear all jobs: " + isError);
                sJobCount -= mSpoolerQueue.peek().clear();
                mRidHashMap.remove(mSpoolerQueue.remove().getRid());
                mIsProcessing.set(false);
                if (mSpoolerQueue.isEmpty()) {
                    stopSelf();
                    return;
                }
                if (!isError)
                    startPrint();
            }
        }
    };

    private synchronized boolean startPrint() {
        if (!mIsProcessing.get()) {
            boolean isFirstJob = false;
            boolean isLastJob = false;
            SpoolElement se = mSpoolerQueue.peek();

            if (se != null && se.isEmptyQueue()) {
                SLog.d(TAG, "No more PA for the job " + se.getRid());
                mRidHashMap.remove(mSpoolerQueue.remove().getRid());
                if (mSpoolerQueue.isEmpty()) {
                    SLog.d(TAG, "No more job is remained");
                    stopSelf();
                }
            }
            if (se != null && se.isFailed()) {
                // if failed one then move it to the end
                SLog.e(TAG, "if failed one then move it to the end");
                mSpoolerQueue.add(mSpoolerQueue.remove());
            }
            --sJobCount;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {}
            SLog.d(TAG, "start printing job " + se.getRid());

            // Last job in the Batch job list
            if(se.getQueue().size() == 1){
                isLastJob = true;
            }

            Bundle bundle = se.dequeue();
            bundle.putBoolean(EXTRA_IS_LASTJOB, isLastJob);

            //OXPCreatePrintJobIntentService.start(getApplicationContext(), bundle, se.isBackgroundJob(), isFirstJob, isLastJob);
            PrintJobIntentService.start(getApplicationContext(), bundle, PrintJobIntentService.class);
            mIsProcessing.set(true);
            return true;
        } else {
            SLog.d(TAG, "Another job is on processing");
            return false;
        }
    }

    public static void start(final Context context, final Bundle bundle) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            final Intent intent = new Intent(context, OXPCreatePrintSpoolerIntentService.class);
            intent.putExtra(EXTRA_PARAMS, bundle);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Throwable e) {
            SLog.d(TAG, "Not created print svc because of permission(start)." + e.getMessage());
        }
    }

    private class SpoolerHandler extends Handler {
        static final int MSG_ADD = 0;
        static final int MSG_START = 1;

        SpoolerHandler(final Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_ADD:
                    SLog.e(TAG, "MSG_ADD: ");
                    //SLog.e(TAG, "MSG_ADD: " + mSpoolerQueue.size());
                    //mSpoolerQueue.add(msg.getData());
                    break;
                case MSG_START:
                    SLog.e(TAG, "MSG_START");
                    //startPrint();
                    break;
            }
        }
    }

    private class SpoolElement {
        private String mRid = "";
        private Queue<Bundle> mBundleQueue;
        private boolean mIsFailed = false;
        private boolean mIsBackgroundJob = false;

        /**
         * Constructor
         */
        SpoolElement() {
            mBundleQueue = new LinkedList<Bundle>();
        }

        SpoolElement(String rid) {
            this();
            mRid = rid;
        }

        SpoolElement(String rid, boolean isBackgroundJob) {
            this(rid);
            mIsBackgroundJob = isBackgroundJob;
        }

        /*
         * Setter and getters
         */
        public void setRid(String rid) {
            mRid = rid;
        }

        public String getRid() {
            return mRid;
        }

        public void setIsFailed(boolean isFailed) {
            mIsFailed = isFailed;
        }

        public boolean isFailed() {
            return mIsFailed;
        }

        public void setIsBackgroundJob(boolean isBackgroundJob) {
            mIsBackgroundJob = isBackgroundJob;
        }

        public boolean isBackgroundJob() {
            return mIsBackgroundJob;
        }

        public Queue<Bundle> getQueue() {
            return mBundleQueue;
        }

        /*
         * Queue handling methods
         */
        public void enqueue(Bundle bundle) {
            mBundleQueue.add(bundle);
        }

        public Bundle dequeue() {
            if (mBundleQueue.isEmpty()) return null;

            if (mIsFailed) return null;

            return mBundleQueue.remove();
        }

        public int clear() {
            int remainedBundle = mBundleQueue.size();
            mBundleQueue.clear();
            return remainedBundle;
        }

        public boolean isEmptyQueue() {
            return mBundleQueue.isEmpty();
        }
    }
}