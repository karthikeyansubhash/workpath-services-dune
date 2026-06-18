/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import android.util.Log;

import com.hp.ext.types.common.E2Type;
import com.hp.ext.types.job.JobActivityEvent;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;

public class ScanTypeMappingHelper {
    protected final static String TAG = ScanTypeMappingHelper.class.getSimpleName();

    public static ITypeConverter getTypeConverter(String key) {
        return ScanTypeMapping.valueOf(key).getConverter();
    }

    @SuppressWarnings("unchecked")
    public static <E, W> W convertEtoW(ScanTypeMapping option, E2Type value) {
        ITypeConverter<E, W> converter = option.getConverter();
        if (converter != null) {
            return (W) option.getConverter().convertEtoW(value);
        }
        return null;
    }

    public static ScanJobState.ActivityState getJobActivityState(java.util.List<JobActivityEvent> e2JobActivityEventList) {
        if (e2JobActivityEventList == null || e2JobActivityEventList.isEmpty()) {
            return ScanJobState.ActivityState.NOT_STARTED;
        }
//        for(JobActivityEvent e2JobActivityEvent : e2JobActivityEventList) {
//            if(e2JobActivityEvent == null) {
//                continue;
//            }
//            Log.d(TAG, "e2JobActivityEvent: " + e2JobActivityEvent.getActivity().getValue());
//            Log.d(TAG, "e2JobActivityEvent: " + e2JobActivityEvent.getTimestamp());
//            Log.d(TAG, "e2JobActivityEvent: " + ScanTypeMapping.jobActivityState.convertEtoW(e2JobActivityEvent.getActivity()));
//        }
        com.hp.ext.types.job.JobActivityEvent lastEvent = e2JobActivityEventList.get(e2JobActivityEventList.size() - 1);
        return ScanTypeMapping.jobActivityState.convertEtoW(lastEvent.getActivity());
    }

    public static ScanJobState.ActivityState getJobActivityState(java.util.List<com.hp.ext.types.job.JobActivityEvent> e2JobActivityEventList, boolean restarted) {
        ScanJobState.ActivityState lastActivityState = getJobActivityState(e2JobActivityEventList);
        if(lastActivityState == ScanJobState.ActivityState.STARTED && restarted) {
            return ScanJobState.ActivityState.RESTARTED;
        }
        return lastActivityState;
    }
}
