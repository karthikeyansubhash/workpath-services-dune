// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hp.workpath.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>StatisticsService provides interfaces to retrieve Statistics information from a printer.</p>
 *
 * @since API 5
 */
@DeviceApi
public final class StatisticsService {
    private static final String TAG = Statisticslet.TAG;

    private StatisticsService() {
    }

    /**
     * <p>This method is needed to determine if the service is supported or not.
     * If it's not supported, StatisticsService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported.
     * <p>
     * <ul>
     * <li>If service is supported, method returns true.</li>
     * <li>If service is not supported, method returns false.</li>
     * </ul>
     * </p>
     * @exception NullPointerException Returns error if context is null.
     * @since API 5
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static boolean isSupported(@NonNull final Context context) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(Statisticslet.CONTENT_URI,
                        Statisticslet.Method.IS_SUPPORTED,
                        null,
                        extras);

        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Statisticslet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Statisticslet.IS_SUPPORTED_EXTRA);
    }

    /**
     * <p>Returns the last committed job sequence</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return int the last job sequence which is committed by agent
     * <p>
     * <ul>
     * <li>If the last committed job sequence is not retrieved, the return value is -1.</li>
     * <li>If the last committed job sequence is retrieved, the return value is greater than or equal to zero.</li>
     * </ul>
     * </p>     
     * @exception NullPointerException if context is null.
     * @since API 8
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized int getLastCommittedJobSequence(@NonNull final Context context,
                                                           @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }

        int lastCommittedJobSequence = -1;

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_LASTCOMMITTEDJOBSEQUENCE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        JSONObject jsonObject = new JSONObject(jobData);
                        if (jsonObject.has("lastSequenceNumberProcessed")) {
                            lastCommittedJobSequence = jsonObject.getInt("lastSequenceNumberProcessed");
                            SLog.d(TAG, "Retrieved committedJobSequence: " + lastCommittedJobSequence);
                        }
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo lastSequenceNumberProcessed is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return lastCommittedJobSequence;
    }

    /**
     * <p>Returns the last completed job sequence</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return int the last completed job sequence
     * <p>
     * <ul>
     * <li>Return value is greater than or equal to zero depending on the number of jobs.</li>
     * </ul>
     * </p>     
     * @exception NullPointerException if context is null.
     * @since API 8
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized int getLastCompletedJobSequence(@NonNull final Context context,
                                                               @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        if (result == null) {
            result = new Result();
        }

        int lastCompletedJobSequence = -1;
        int lastCommittedJobSequence = getLastCommittedJobSequence(context, result);
        int totalJobCount = getTotalCount(context, result);

        lastCompletedJobSequence = lastCommittedJobSequence + totalJobCount;

        if(lastCompletedJobSequence >= 0) {
            SLog.d(TAG, "Retrieved lastCompletedJobSequence: " + lastCompletedJobSequence);
        } else {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "last Completed Job Sequence is negative");
        }

        return lastCompletedJobSequence;
    }

    /**
     * <p>Returns the last completed job information</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return StatisticsJobData the last completed job information
     * @exception NullPointerException if context is null.
     * @since API 8
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized StatisticsJobData getLastCompletedJobInfo(@NonNull final Context context, @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 1); //TODO

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();
        int totalJobSequence = 0;
        try {
            totalJobSequence = getTotalCount(context, result);
            if (totalJobSequence <= 0) return new StatisticsJobData(); //FW defect(25.2)
        } catch (Throwable throwable) {
            SLog.i(TAG, "Failed to retrieve totalJobSequence(getLastJobInfo)");
        }

        extras.putInt(Statisticslet.Param.KEY_OFFSET, totalJobSequence - 1);

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved getLastCompletedJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");
                        jsonObject.getJSONObject(0).put("jobSequence", getLastCompletedJobSequence(context, result));

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        SLog.d(TAG, "Retrieved getLastCompletedJobInfo: " + statisticsJobInfoList.size());
                    } else {
                        SLog.d(TAG, "getLastCompletedJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getLastCompletedJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) ? statisticsJobInfoList.get(0) : new StatisticsJobData();
    }

    /**
     * <p>Returns all jobs of Statistics (maximum job count is 256 per each agent)</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return List StatisticsJobData information
     * @exception NullPointerException if context is null.
     * @since API 8
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized List<StatisticsJobData> getAllJobsList(@NonNull final Context context,
                                                                  @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 50);
        extras.putInt(Statisticslet.Param.KEY_OFFSET, 0);
        int maxTry = 5;

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();
        LinkedList<StatisticsJobData> statisticsJobInfoListForTmp = new LinkedList<StatisticsJobData>();

        boolean firstTry = false;
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved statisticsJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");

                        int lastSeq = getLastCommittedJobSequence(context, result);
                        for(int idx = 0; idx < jsonObject.length() ; idx++ )
                            jsonObject.getJSONObject(idx).put("jobSequence", lastSeq + idx + 1);

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        statisticsJobInfoListForTmp.addAll(statisticsJobInfoList);
                        SLog.d(TAG, "Retrieved statisticsJobInfo: " + statisticsJobInfoList.size() + "," + statisticsJobInfoListForTmp.size());

                        firstTry = true;

                        if (firstTry) {
                            boolean tmpTry = true;
                            for (int inx = 1; tmpTry && inx <= maxTry; inx++) {
                                tmpTry = false;

                                extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 50);
                                extras.putInt(Statisticslet.Param.KEY_OFFSET, 50 * inx);
                                SLog.d(TAG, "Retrieved statisticsJobInfo for next, pagination idx: " + (50 * inx));

                                try {
                                    Result resultForTmp = new Result();
                                    final Bundle bundleForRetry =
                                            context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);
                                    if (bundleForRetry != null) {
                                        Result.parse(bundleForRetry, resultForTmp);
                                        if (bundleForRetry.containsKey(Result.KEY_RESULT)) {
                                            final String jobDataForTmp = bundleForRetry.getString(Result.KEY_RESULT);
                                            if (!TextUtils.isEmpty(jobDataForTmp)) {
                                                SLog.d(TAG, "Retrieved statisticsJobInfo for next, jobData is not null. Try to parse ");

                                                JSONArray jsonObjectForTmp = new JSONObject(jobDataForTmp).getJSONArray("members");
                                                for(int idx = 0; idx < jsonObjectForTmp.length() ; idx++ )
                                                    jsonObjectForTmp.getJSONObject(idx).put("jobSequence", lastSeq + (50 * inx) + idx + 1);

                                                statisticsJobInfoList.clear();
                                                statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObjectForTmp.toString(), listType);

                                                if (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) {
                                                    SLog.d(TAG, "Retrieved statisticsJobInfo for tmp: " + statisticsJobInfoList.size());
                                                    statisticsJobInfoListForTmp.addAll(statisticsJobInfoList);
                                                    SLog.d(TAG, "Updated statisticsJobInfo for statisticsJobInfoListForTmpSub: " + statisticsJobInfoList.size() + ", " + statisticsJobInfoListForTmp.size());
                                                    tmpTry = true;
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable throwable) {
                                }
                            }
                        }
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        if (statisticsJobInfoListForTmp != null && statisticsJobInfoListForTmp.size() > 0) {
            SLog.d(TAG, "StatisticsJobInfo information size for return is " + statisticsJobInfoListForTmp.size());
        } else {
            SLog.d(TAG, "StatisticsJobInfo information size for return is zero");
        }
        return (statisticsJobInfoListForTmp != null && statisticsJobInfoListForTmp.size() > 0) ? new ArrayList<StatisticsJobData>(statisticsJobInfoListForTmp) : new ArrayList<StatisticsJobData>();
    }

    /**
     * <p>Returns Statistics job information by specific job sequence</p>
     *
     * @param context     The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param jobSequence job sequence
     * @param result      (optional) Indicates any errors which occurred while retrieving configuration.
     * @return StatisticsJobData statistics job information
     * @exception NullPointerException if context is null.
     * @since API 8
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    public static synchronized StatisticsJobData getJobInfoByJobSequence(@NonNull final Context context, @NonNull final int jobSequence,
                                                            @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        int lastSeq = getLastCommittedJobSequence(context, result);
        int offset = jobSequence - lastSeq - 1;

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 1);
        extras.putInt(Statisticslet.Param.KEY_OFFSET, offset);

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved statisticsJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");
                        jsonObject.getJSONObject(0).put("jobSequence", jobSequence);

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        SLog.d(TAG, "Retrieved statisticsJobInfo: " + statisticsJobInfoList.size());
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) ? statisticsJobInfoList.get(0) : new StatisticsJobData();
    }

    /**
     * <p>Returns all jobs of Statistics (maximum job count is 256 per each agent)</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return List StatisticsJobData information
     * @exception NullPointerException if context is null.
     * @since API 5
     * @deprecated API 8 "No longer in use, please use {@link StatisticsService#getAllJobsList(Context, Result)} instead."
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    @Deprecated
    public static synchronized List<StatisticsJobData> getJobInfo(@NonNull final Context context,
                                                                  @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 50);
        extras.putInt(Statisticslet.Param.KEY_OFFSET, 0);
        int maxTry = 5;

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();
        LinkedList<StatisticsJobData> statisticsJobInfoListForTmp = new LinkedList<StatisticsJobData>();

        boolean firstTry = false;
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved statisticsJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        statisticsJobInfoListForTmp.addAll(statisticsJobInfoList);
                        SLog.d(TAG, "Retrieved statisticsJobInfo: " + statisticsJobInfoList.size() + "," + statisticsJobInfoListForTmp.size());

                        firstTry = true;

                        if (firstTry) {
                            boolean tmpTry = true;
                            for (int inx = 1; tmpTry && inx <= maxTry; inx++) {
                                tmpTry = false;

                                extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 50);
                                extras.putInt(Statisticslet.Param.KEY_OFFSET, 50 * inx);
                                SLog.d(TAG, "Retrieved statisticsJobInfo for next, pagination idx: " + (50 * inx));

                                try {
                                    Result resultForTmp = new Result();
                                    final Bundle bundleForRetry =
                                            context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);
                                    if (bundleForRetry != null) {
                                        Result.parse(bundleForRetry, resultForTmp);
                                        if (bundleForRetry.containsKey(Result.KEY_RESULT)) {
                                            final String jobDataForTmp = bundleForRetry.getString(Result.KEY_RESULT);
                                            if (!TextUtils.isEmpty(jobDataForTmp)) {
                                                SLog.d(TAG, "Retrieved statisticsJobInfo for next, jobData is not null. Try to parse ");

                                                JSONArray jsonObjectForTmp = new JSONObject(jobDataForTmp).getJSONArray("members");
                                                statisticsJobInfoList.clear();
                                                statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObjectForTmp.toString(), listType);

                                                if (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) {
                                                    SLog.d(TAG, "Retrieved statisticsJobInfo for tmp: " + statisticsJobInfoList.size());
                                                    statisticsJobInfoListForTmp.addAll(statisticsJobInfoList);
                                                    SLog.d(TAG, "Updated statisticsJobInfo for statisticsJobInfoListForTmpSub: " + statisticsJobInfoList.size() + ", " + statisticsJobInfoListForTmp.size());
                                                    tmpTry = true;
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable throwable) {
                                }
                            }
                        }
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
        if (statisticsJobInfoListForTmp != null && statisticsJobInfoListForTmp.size() > 0) {
            SLog.d(TAG, "StatisticsJobInfo information size for return is " + statisticsJobInfoListForTmp.size());
        } else {
            SLog.d(TAG, "StatisticsJobInfo information size for return is zero");
        }
        return (statisticsJobInfoListForTmp != null && statisticsJobInfoListForTmp.size() > 0) ? new ArrayList<StatisticsJobData>(statisticsJobInfoListForTmp) : new ArrayList<StatisticsJobData>();
    }

    /**
     * <p>Returns Statistics job information by specific offset of job list</p>
     *
     * @param context     The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param offset      Offset of Statistics job list
     * @param result      (optional) Indicates any errors which occurred while retrieving configuration.
     * @return StatisticsJobData statistics job information
     * @exception NullPointerException if context is null.
     * @since API 5
     * @deprecated API 8 "No longer in use, please use {@link StatisticsService#getJobInfoByJobSequence(Context, int, Result)} instead."
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Deprecated
    public static synchronized StatisticsJobData getJobInfo(@NonNull final Context context, @NonNull final int offset,
                                                            @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 1);
        extras.putInt(Statisticslet.Param.KEY_OFFSET, offset);
        int maxTry = 5;

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();

        boolean firstTry = false;
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved statisticsJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        SLog.d(TAG, "Retrieved statisticsJobInfo: " + statisticsJobInfoList.size());
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) ? statisticsJobInfoList.get(0) : new StatisticsJobData();
    }

    /**
     * <p>Returns the last of job information</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return StatisticsJobData StatisticsJob data of an application
     * @exception NullPointerException if context is null.
     * @since API 5
     * @deprecated API 8 "No longer in use, please use {@link StatisticsService#getLastCompletedJobInfo(Context, Result)} instead."
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    @Deprecated
    public static synchronized StatisticsJobData getLastJobInfo(@NonNull final Context context, @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 1); //TODO

        if (result == null) {
            result = new Result();
        }

        List<StatisticsJobData> statisticsJobInfoList = new ArrayList<StatisticsJobData>();
        int totalJobSequence = 0;
        try {
            totalJobSequence = getTotalCount(context, result);
            if (totalJobSequence <= 0) return new StatisticsJobData(); //FW defect(25.2)
        } catch (Throwable throwable) {
            SLog.i(TAG, "Failed to retrieve totalJobSequence(getLastJobInfo)");
        }

        extras.putInt(Statisticslet.Param.KEY_OFFSET, totalJobSequence - 1);

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved getLastJobInfo, jobData is not null. Try to parse ");

                        JSONArray jsonObject = new JSONObject(jobData).getJSONArray("members");

                        Type listType = new TypeToken<List<StatisticsJobData>>() {
                        }.getType();
                        statisticsJobInfoList = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
                        SLog.d(TAG, "Retrieved getLastJobInfo: " + statisticsJobInfoList.size());
                    } else {
                        SLog.d(TAG, "getLastJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "getLastJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return (statisticsJobInfoList != null && statisticsJobInfoList.size() > 0) ? statisticsJobInfoList.get(0) : new StatisticsJobData();
    }

    /**
     * <p>Returns the last job sequence which is committed by agent</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return int the last job sequence which is committed by agent
     * @exception NullPointerException if context is null.
     * @since API 5
     * @deprecated API 8 "No longer in use, please use {@link StatisticsService#getLastCommittedJobSequence(Context, Result)} instead."
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    @Deprecated
    public static synchronized int getLastJobSequence(@NonNull final Context context,
                                                      @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }

        int lastJobSequence = -1;

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_LASTJOBSEQUENCE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        JSONObject jsonObject = new JSONObject(jobData);
                        if (jsonObject.has("lastSequenceNumberProcessed")) {
                            lastJobSequence = jsonObject.getInt("lastSequenceNumberProcessed");
                            SLog.d(TAG, "Retrieved lastJobSequence: " + lastJobSequence);
                        }
                    } else {
                        SLog.d(TAG, "StatisticsJobInfo lastSequenceNumberProcessed is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return lastJobSequence;
    }

    /**
     * <p>Returns total job count</p>
     *
     * @param context The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param result  (optional) Indicates any errors which occurred while retrieving configuration.
     * @return int total job count
     * @exception NullPointerException if context is null.
     * @since API 5
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized int getTotalCount(@NonNull final Context context,
                                                 @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putInt(Statisticslet.Param.KEY_JOBLIMIT, 1);
        extras.putInt(Statisticslet.Param.KEY_OFFSET, 0); //only 1 job for getting totalCount

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.GET_JOBINFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    final String jobData = bundle.getString(Result.KEY_RESULT);
                    if (!TextUtils.isEmpty(jobData)) {
                        SLog.d(TAG, "Retrieved statisticsJobInfo, jobData is not null. Try to parse ");

                        JSONObject jsonObject = new JSONObject(jobData);
                        if (jsonObject.has("totalCount")) {
                            SLog.d(TAG, "Retrieved totalCount: " + jsonObject.getInt("totalCount"));
                            return jsonObject.getInt("totalCount");
                        }
                    } else {
                        SLog.d(TAG, "(getTotalCount)StatisticsJobInfo information is null");
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo information is null");
                    }
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return 0;
    }

    /**
     * <p>Returns the result of requesting commit to update last job sequence</p>
     *
     * @param context            The Context in which the application is running. If it's null, configuration will not be retrieved.
     * @param lastSequenceNumber last sequence number for updating sequence of agent
     * @param result             (optional) Indicates any errors which occurred while retrieving configuration.
     * @return boolen the result of committing
     * @exception NullPointerException if context is null.
     * @since API 5
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    @SuppressLint("RestrictedApi")
    @Nullable
    public static synchronized boolean commit(@NonNull final Context context, int lastSequenceNumber,
                                              @Nullable Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");
        Preconditions.checkNotNull(lastSequenceNumber, "JobSequence must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Statisticslet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Statisticslet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }

        boolean isCommitted = false;
        try {
            extras.putInt(Statisticslet.Param.KEY_JOBSEQUENCE, lastSequenceNumber);
            final Bundle bundle =
                    context.getContentResolver().call(Statisticslet.CONTENT_URI, Statisticslet.Method.COMMIT_LASTJOBSEQUENCE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);

                if (bundle.containsKey(Result.KEY_RESULT)) {
                    isCommitted = bundle.getBoolean(Result.KEY_RESULT);
                    SLog.d(TAG, "StatisticsJobInfo is committed");
                } else {
                    SLog.d(TAG, "StatisticsJobInfo is not committed");
                    Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "StatisticsJobInfo is not committed");
                }
            }
        } catch (Throwable e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return isCommitted;
    }

    /**
     * <p>
     * AbstractStatisticsNotificationObserver provides a mechanism to monitor the state of the
     * {@link StatisticsService} operations. The AbstractStatisticsNotificationObserver receives
     * callbacks when there have been changes to the state of the event from a printer.
     * </p>
     *
     * @since API 5
     */
    @SuppressWarnings({"unused"})
    @DeviceApi
    public static abstract class AbstractStatisticsNotificationObserver extends BroadcastReceiver {
        private final Handler mHandler;

        /**
         * <p>Constructor to create new instance of AbstractStatisticsNotificationObserver</p>
         *
         * @param handler The handler to run callbacks on, or null if none.
         * @since API 5
         */
        public AbstractStatisticsNotificationObserver(final Handler handler) {
            super();
            mHandler = handler;
        }

        /**
         * <p>
         * Register the observer to monitor state when statistics data is changed.
         * </p>
         *
         * @param context The Context in which the application is running. If it's null, event will not be triggered.
         * @exception NullPointerException if context is null.
         * @since API 5
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void register(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            SLog.d(TAG, "Registering Statistics changed event");

            final IntentFilter filter = new IntentFilter(Statisticslet.NOTIFICATION_CHANGE_ACTION);
            context.registerReceiver(this, filter);
        }

        /**
         * <p>
         * Unregister the observer and stop monitoring state when statistics data is changed.
         * </p>
         *
         * @param context The Context in which the application is running.
         * @exception NullPointerException if context is null.
         * @since API 5
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void unregister(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            SLog.d(TAG, "Un-registering Statistics changed event");
            context.unregisterReceiver(this);
        }

        /**
         * @hide final
         */
        @Override
        public final void onReceive(final Context context, final Intent intent) {
            SLog.d(TAG, "Received intent for " + intent.getAction());
            final String action = intent.getAction();
            if (!Statisticslet.NOTIFICATION_CHANGE_ACTION.equals(action)) {
                SLog.e(TAG, "Received invalid intent");
                return;
            }

            if (mHandler == null) {
                onRecv(context, intent);
            } else {
                mHandler.post(new StatisticsService.AbstractStatisticsNotificationObserver.StatisticsletRunnable(context, intent));
            }
        }

        private void onRecv(final Context context, final Intent intent) {
            final Bundle bundle = intent.getExtras();

            if (bundle.containsKey(Statisticslet.Keys.EXTRA_UUID)
                    && bundle.containsKey(Statisticslet.Keys.EXTRA_DATA)
                    && bundle.containsKey(Statisticslet.Keys.EXTRA_DATA2)) {
                final String uuid = bundle.getString(Statisticslet.Keys.EXTRA_UUID);
                final int lastSequenceNumberProcessed = (int) bundle.getLong(Statisticslet.Keys.EXTRA_DATA);
                final int highestSequenceNumberCompleted = bundle.getInt(Statisticslet.Keys.EXTRA_DATA2);
                if (context.getPackageName() != null) { // && Sdk.SERVICES_PACKAGE.equalsIgnoreCase(context.getPackageName())) {
                    SLog.d(TAG, "Statistics state: " + uuid + ", " + lastSequenceNumberProcessed);
                    Result result = new Result();
                    int lastSeq = 0;
                    try {
                        lastSeq = getLastJobSequence(context, result);
                        if (result.getCode() != Result.RESULT_OK) {
                            lastSeq = 0;
                        }
                    } catch (Throwable throwable) {
                    }

                    if (lastSeq <= lastSequenceNumberProcessed) {
                        onChange(lastSequenceNumberProcessed);
                        onComplete(highestSequenceNumberCompleted);
                    }
                    else
                        SLog.d(TAG, "(ignored) Statistics state: " + uuid + ", " + lastSeq + ", " + lastSequenceNumberProcessed);
                } else {
                    SLog.d(TAG, "invalid request from " + uuid);
                }
            } else {
                SLog.d(TAG, "invalid call");
            }
        }

        private final class StatisticsletRunnable implements Runnable {
            private final Intent mIntent;
            private final Context mContext;

            public StatisticsletRunnable(final Context context, final Intent intent) {
                mIntent = intent;
                mContext = context;
            }

            @Override
            public void run() {
                StatisticsService.AbstractStatisticsNotificationObserver.this.onRecv(mContext, mIntent);
            }
        }

        /**
         * <p>Called to notify the client that a printer has event.</p>
         *
         * @param lastSequenceNumberProcessed processed job sequence number
         * @since API 5
         * @deprecated API 8 "No longer in use, please use {@link StatisticsService.AbstractStatisticsNotificationObserver#onComplete(int)} instead."
         */
        @Deprecated
        public void onChange(int lastSequenceNumberProcessed) {
            SLog.d(TAG, "onChange - committed job sequence number: " + lastSequenceNumberProcessed);
        }

        /**
         * <p>Called to notify the client that a printer has event.</p>
         *
         * @param completedJobSequence completed job sequence number
         * @since API 8
         */
        public abstract void onComplete(int completedJobSequence);
    }

}
