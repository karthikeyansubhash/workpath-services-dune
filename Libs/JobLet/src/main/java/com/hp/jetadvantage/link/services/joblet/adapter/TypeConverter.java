package com.hp.jetadvantage.link.services.joblet.adapter;

import static com.hp.ext.types.job.JobActivityState.JasCompleted;
import static com.hp.ext.types.job.JobActivityState.JasNotStarted;
import static com.hp.ext.types.job.JobActivityState.JasStarted;

import com.hp.ext.types.job.JobActivityState;
import com.hp.jetadvantage.link.api.job.CopyJobState;

import java.util.Hashtable;

public class TypeConverter {

    protected static Hashtable<JobActivityState, CopyJobState.ActivityState> activityStateMapEtoW;
    static {
        activityStateMapEtoW = new Hashtable<JobActivityState, CopyJobState.ActivityState> ();
        activityStateMapEtoW.put(JasNotStarted, CopyJobState.ActivityState.NOT_STARTED);
        activityStateMapEtoW.put(JasStarted, CopyJobState.ActivityState.STARTED);
        activityStateMapEtoW.put(JasCompleted, CopyJobState.ActivityState.COMPLETED);
    }

    public static CopyJobState.ActivityState convertJobActivityState(JobActivityState jobActivityState) {
        CopyJobState.ActivityState jobActivity =  activityStateMapEtoW.get(jobActivityState);
        if(jobActivity == null) {
            jobActivity = CopyJobState.ActivityState.NOT_STARTED;
        }
        return jobActivity;
    }
}
