package com.hp.jetadvantage.link.services.printlet.ipp;

import android.os.Message;
import android.util.Log;

import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.JobAttributes;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;
import com.hp.jetadvantage.link.services.printlet.service.PrintJobIntentServiceStateMachine;

public class IppJobMonitor {

    private PrintJobIntentServiceStateMachine mStateMachine;

    private IppJobMonitorThread mMonitorThread;

    public IppJobMonitor(BaseJobIntentServiceStateMachine stateMachine, IppJobMonitorCallback callback) {
        mStateMachine = (PrintJobIntentServiceStateMachine) stateMachine;
        mMonitorThread = new IppJobMonitorThread(mStateMachine, callback);
        mMonitorThread.start();
    }

    public void stop() {
        if (mMonitorThread != null && mMonitorThread.isAlive()) {
            mMonitorThread.interrupt();
            mMonitorThread = null;
        }
    }

    public interface IppJobMonitorCallback {
        // return true if the job is finished.
        void onJobStatusChanged(int jobId, JobAttributes jobAttributes);

        boolean isJobFinished(JobAttributes jobAttributes);
    }

    public class IppJobMonitorThread extends Thread {
        public final String TAG = IppJobMonitorThread.class.getSimpleName();
        private PrintJobIntentServiceStateMachine mStateMachine;
        private IppJobMonitorCallback mCallback;
        private final PrintJobID mJobId;
        private final int POLLING_INTERVAL = 2000;

        public IppJobMonitorThread(PrintJobIntentServiceStateMachine stateMachine, IppJobMonitorCallback callback) {
            mStateMachine = stateMachine;
            mJobId = stateMachine.getPrintJobID();
            mCallback = callback;
        }


        @Override
        public void run() {
            // The SDK of JEDI/JOLT also polls forever until the job is finished.
            for (; ; ) {
                try {
                    Thread.sleep(POLLING_INTERVAL);
                    Message response = IppClient.getInstance().getJobAttributes(mStateMachine.getContext(), 1, mJobId);
                    JobAttributes jobAttributes = (JobAttributes) response.obj;
                    if (mCallback != null) {
                        mCallback.onJobStatusChanged(mJobId.getJobID(), jobAttributes);
                        if (mCallback.isJobFinished(jobAttributes))
                            return;
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "IppJobMonitorThread interrupted");
                    return;
                } catch (Error e) {
                    Log.e(TAG, "IppJobMonitorThread Error: " + e.getMessage());
                    return;
                }
            }
        }
    }
}
