// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.model;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.ErrorCode;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Internal Support APK content provider to share jobs data between services.
 * Currently it operates as reduced collection.
 */
// TODO: should be backed up with database
public class JobDataContentProvider extends ContentProvider {
    private static final String TAG = "Joblet";

    // segments for uri building
    private static final String CONTENT_SCHEME = "content";
    private static final String AUTHORITY = "com.hp.jetadvantage.link.authority.joblet.internalcp.jobs";
    private static final String DIR_PATH_SEGMENT = "jobdata";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME).authority(AUTHORITY)
            .appendPath(DIR_PATH_SEGMENT).build();

    private static final int JOB_DATA_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, DIR_PATH_SEGMENT, JOB_DATA_CODE);
    }

    // Keys to use for communication with the provider
    private static final String KEY_COUNT = "count";
    private static final String KEY_JOB_DATA = "jobData";
    private static final String KEY_JOB_DATA_KEY = "jobDataKey";
    private static final String KEY_JOB_ID = "jobId";
    private static final String KEY_CONTAINS = "contains";
    private static final String KEY_JOBS = "jobs";
    private static final String KEY_CAUSES = "causes";

    // Main Data Structure
    private ConcurrentHashMap<String, JobData> mJobDataMap;    // Key = MFP_ID:JobID

    private BlockingQueue<String> mOldDataKeyQueue;
    private ConcurrentHashMap<String, JobData> mOldJobDataMap;

    // SharedPreferences files for job data
    private static final String JOB_DATA_FILE = "jobDataFile";
    private static final String PI_FILE = "pendingIntentFile";

    // Shared preferences objects for job data storing / restoring
    private SharedPreferences mJobDataSP;
    private SharedPreferences mPendingIntentSP; // Using a second SP to simplify the development and also to avoid creating an class with jobId and
    // PendingIntents and and adding more code to create, remove, marshall and unmarshall the object.
    // In addition, PendingIntents for a job could be null, so creating just one SP with key and null
    // value would result in removal of the item as in SharedPreferences API javadocs.

    private static boolean mIsPutData = true;

    public static void setIsPutData(boolean value){
        mIsPutData = value;
    }

    @Override
    public boolean onCreate() {
        if (getContext() == null) {
            return false;
        }

        mJobDataSP = getContext().getSharedPreferences(getContext().getPackageName() + JOB_DATA_FILE,
                Context.MODE_PRIVATE);
        mPendingIntentSP = getContext().getSharedPreferences(getContext().getPackageName() + PI_FILE,
                Context.MODE_PRIVATE);

        mJobDataMap = new ConcurrentHashMap<>();
        mOldDataKeyQueue = new LinkedBlockingQueue<String>();
        mOldJobDataMap = new ConcurrentHashMap<>();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(final String method, final String arg, final Bundle extras) {
        Bundle bundle = null;

        if (getContext() == null) {
            return null;
        }

        DataMethod providerMethod = null;
        try {
            providerMethod = DataMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            SLog.e(TAG, "Invalid method has been requested " + method);
            return null;
        }

        switch (providerMethod) {
            case GET:
                bundle = executeGet(extras);
                break;

            case PUT:
                bundle = executePut(extras);
                break;

            case REMOVE:
                bundle = executeRemove(extras);
                break;

            case REMOVE_ALL:
                bundle = executeRemoveAll(extras);
                break;

            case CONTAINS:
                bundle = executeContains(extras);
                break;

            case SIZE:
                bundle = executeSize();
                break;

            case GET_ALL:
                bundle = executeGetAll();
                break;

            case PERSISTENT_DATA:
                bundle = executeGetPersistentJobData();
                break;

            case GET_CAUSES_BY_ID:
                bundle = executeGetCausesByJobId(extras);
                break;

            case GET_IDS:
                bundle = executeGetJobIds();
                break;

            default:
                SLog.e(TAG, "Invalid method has been requested " + method);
                return null;
        }

        return bundle;
    }

    /**
     * Executes {@link DataMethod#GET_IDS}
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeGetJobIds() {
        final ArrayList<String> jobIds = new ArrayList<>();
        final Bundle bundle = new Bundle();

        for (JobData data : mJobDataMap.values()) {
            jobIds.add(data.mJobId);
        }

        bundle.putStringArrayList(KEY_JOBS, jobIds);

        return bundle;
    }

    /**
     * Executes {@link DataMethod#GET_CAUSES_BY_ID}
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeGetCausesByJobId(final Bundle extras) {
        final String jobId = extras.getString(KEY_JOB_ID);
        final String rid = JobService.getRid(getContext(), jobId);
        final Bundle bundle = new Bundle();

        if (TextUtils.isEmpty(rid)) {
            return null;
        }

        if (mJobDataMap.contains(rid) && mJobDataMap.get(rid).mProcessingCauses != null) {
            bundle.putParcelableArrayList(KEY_CAUSES, new ArrayList<>(mJobDataMap.get(rid).mProcessingCauses));
        } else {
            bundle.putParcelableArrayList(KEY_CAUSES, new ArrayList<ErrorCode>());
        }

        return bundle;
    }

    /**
     * Executes {@link DataMethod#GET_ALL}
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeGetAll() {
        final Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(KEY_JOBS, new ArrayList<>(mJobDataMap.values()));

        return bundle;
    }

    /**
     * Executes {@link DataMethod#SIZE}
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeSize() {
        final Bundle bundle = new Bundle();

        bundle.putInt(KEY_COUNT, mJobDataMap.size());

        return bundle;
    }

    /**
     * Executes {@link DataMethod#PERSISTENT_DATA}
     *
     * @return {@link Bundle} with output, can be empty
     */
    private Bundle executeGetPersistentJobData() {
        final Map<String, String> jobs = getPersistedJobData(getContext());
        final Bundle bundle = new Bundle();

        if (jobs == null) {
            return bundle;
        }

        final ArrayList<Bundle> jobBundles = new ArrayList<>(jobs.size());

        if (jobs.size() > 0) {
            for (Map.Entry<String, String> entry : jobs.entrySet()) {
                final Bundle jobsBundle = new Bundle();
                final List<Intent> intentList = getPersistedPendingIntents(getContext(), entry.getKey());

                jobsBundle.putString(Joblet.Keys.KEY_JOBID, entry.getValue());

                if (intentList != null && !intentList.isEmpty()) {
                    SLog.d(TAG, "Retrieved Intents from persistence as "+ intentList.toString());
                    jobsBundle.putParcelableArrayList(JobData.KEY_PENDING_INTENT_LIST, (ArrayList<Intent>) intentList);
                }

                jobBundles.add(jobsBundle);
            }
        }

        bundle.putParcelableArrayList(KEY_JOB_DATA, jobBundles);

        return null;
    }

    /**
     * Executes {@link DataMethod#CONTAINS}
     *
     * @param extras with in data
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeContains(final Bundle extras) {
        final String key = extras.getString(KEY_JOB_DATA_KEY);
        final Bundle bundle = new Bundle();

        if (TextUtils.isEmpty(key)) {
            bundle.putBoolean(KEY_CONTAINS, false);
            return bundle;
        }

        bundle.putBoolean(KEY_CONTAINS, mJobDataMap.containsKey(key));

        return bundle;
    }

    /**
     * Executes {@link DataMethod#REMOVE}
     *
     * @param extras with in data
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeRemove(final Bundle extras) {

        final String key = extras.getString(KEY_JOB_DATA_KEY);
        final Bundle bundle = new Bundle();

        if (TextUtils.isEmpty(key)) {
            bundle.putInt(KEY_COUNT, -1);
            return bundle;
        }
        JobData jobData = mJobDataMap.remove(key);
        if (jobData != null) {
            moveOldJobMapData(key, jobData);
        }
        removeFromSharedPrefs(key);
        bundle.putInt(KEY_COUNT, mJobDataMap.size());

        return bundle;
    }



    private Bundle executeRemoveAll(final Bundle extras) {
        final Bundle bundle = new Bundle();

        for(String key : mJobDataMap.keySet()){
            moveOldJobMapData(key,mJobDataMap.get(key));
        }
        mJobDataMap.clear();
        removeAllFromSharedPrefs();
        bundle.putInt(KEY_COUNT, mJobDataMap.size());

        return bundle;
    }

    private void moveOldJobMapData(String key, JobData jobData){
        try {
            if(mOldDataKeyQueue.size() > 30){
                String tmpKey = mOldDataKeyQueue.take();
                mOldJobDataMap.remove(tmpKey);
            }
            mOldDataKeyQueue.add(key);
            mOldJobDataMap.put(key, jobData);
        } catch (Exception e) {
            SLog.e(TAG, "Error moveOldJobMapData Key : " + key,e);
        }

    }

    /**
     * Executes {@link DataMethod#GET}
     *
     * @param extras with in data
     *
     * @return {@link Bundle} with output
     */
    private Bundle executeGet(final Bundle extras) {
        final String key = extras.getString(KEY_JOB_DATA_KEY);
        final Bundle bundle = new Bundle();

        if (TextUtils.isEmpty(key)) {
            bundle.putInt(KEY_COUNT, -1);
            return bundle;
        }

        if(mOldJobDataMap.containsKey(key)){
            bundle.putParcelable(KEY_JOB_DATA, mOldJobDataMap.get(key));
            bundle.putInt(KEY_COUNT, mJobDataMap.size());
            return bundle;
        }

        if (mJobDataMap.containsKey(key)) {
            bundle.putParcelable(KEY_JOB_DATA, mJobDataMap.get(key));
            bundle.putInt(KEY_COUNT, mJobDataMap.size());
        } else {
            bundle.putInt(KEY_COUNT, -1);
        }

        return bundle;
    }

    /**
     * Executes {@link DataMethod#PUT}
     *
     * @param extras with in data
     *
     * @return {@link Bundle} with output
     */
    private Bundle executePut(final Bundle extras) {
        final String key = extras.getString(KEY_JOB_DATA_KEY);
        final String jobId = extras.getString(KEY_JOB_ID);
        final JobData jobData = extras.getParcelable(KEY_JOB_DATA);
        final Bundle bundle = new Bundle();

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(jobId) || jobData == null) {
            bundle.putInt(KEY_COUNT, -1);
            return bundle;
        }

        saveToSharedPrefs(key, jobId, jobData.getPendingIntents());
        // Ensure job id is presented
        jobData.mJobId = jobId;
        mJobDataMap.put(key, jobData);
        bundle.putInt(KEY_COUNT, mJobDataMap.size());

        return bundle;
    }

    private void saveToSharedPrefs(final String key, final String jobId, final List<Intent> pendingIntents) {
        if (null == mJobDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot save");
            return;
        }
        final SharedPreferences.Editor jobDataEditor = mJobDataSP.edit();
        // Save the jobId
        jobDataEditor.putString(key, jobId);
        jobDataEditor.commit();
//[security]         SLog.v(TAG, "Saved key " + key + " and jobId " + jobId + " in sharedPreferences");

        // Save the pendingIntents
        if (null != pendingIntents) {
            final Set<String> stringSet = new HashSet<String>(2);

            for (final Intent pi : pendingIntents) {
                String str = pi.toUri(0);
                stringSet.add(str);
            }

            final SharedPreferences.Editor piEditor = mPendingIntentSP.edit();

            piEditor.putStringSet(key, stringSet);
            piEditor.commit();
            SLog.v(TAG, "Saved PendingIntents as stringSet in sharedPreferences");
        } else {
            SLog.d(TAG, "Not Saved PendingIntents is null");
        }
    }

    private void removeAllFromSharedPrefs() {
        removeFromSharedPrefs(null);
    }

    private void removeFromSharedPrefs(final String key) {
        if (null == mJobDataSP) {
            SLog.e(TAG, "SharedPrefs object is null.  Cannot remove");
            return;
        }
        final SharedPreferences.Editor jobDataEditor = mJobDataSP.edit();
        final SharedPreferences.Editor piEditor = mPendingIntentSP.edit();

        if(key == null) {
            jobDataEditor.clear();
            piEditor.clear();
        } else {
            jobDataEditor.remove(key);
            piEditor.remove(key);
        }
        jobDataEditor.commit();
        piEditor.commit();

        if(key!=null) SLog.v(TAG, "Removed key " + key + " from sharedPreferences");
    }

    @SuppressLint("RestrictedApi")
    private static Map<String, String> getPersistedJobData(final Context context) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        final SharedPreferences sharedPrefs = context.getSharedPreferences(context.getPackageName() + JOB_DATA_FILE, Context.MODE_PRIVATE);
        if (null == sharedPrefs) {
            SLog.w(TAG, "SharedPreferences for the specified context is null. Returning null");
            return null;
        }
        Map<String, String> persistedJobData = (Map<String, String>)sharedPrefs.getAll();
        // TODO: Must return only for the currently connected printer
        // NOTE: Didn't get to it.  Will add when working on BYOD changes

//[security]        SLog.d(TAG, "Returning persisted job data as " + persistedJobData.toString());
        return persistedJobData;
    }

    @SuppressLint("RestrictedApi")
    private static List<Intent> getPersistedPendingIntents(final Context context, final String key) {
        Preconditions.checkNotNull(context, "Context cannot be null");

        final SharedPreferences sharedPrefs = context.getSharedPreferences(context.getPackageName() + PI_FILE, Context.MODE_PRIVATE);
        if (null == sharedPrefs) {
            SLog.w(TAG, "SharedPreferences for the specified context is null. Returning null");
            return null;
        }
        final List<Intent> intentList = new ArrayList<>(2);
        final Set<String> stringSet = sharedPrefs.getStringSet(key, null);
        if (null == stringSet) {
            SLog.w(TAG, "SharedPreferences returned null Intents stringSet");
            return intentList;
        }
        for (final String str : stringSet) {
            SLog.d(TAG, "Iterating through stringSet from SP");
            try {
                Intent intent = Intent.parseUri(str, 0);
                SLog.d(TAG, "Obtained Intent from persistent data");
                intentList.add(intent);
            } catch (final URISyntaxException e) {
                SLog.e(TAG, "Retrieving intent from Uri failed with URISyntaxException with cause ", e);
            }
        }
        return intentList;
    }

    /**
     * Puts {@link JobData} into provider with provided key and jobId
     *
     * @param resolver {@link ContentResolver}
     * @param key {@link String} of JobData
     * @param jobId of JobData
     * @param jobData {@link JobData} to put into provider
     *
     * @return number of items in provider
     */
    public static int put(final ContentResolver resolver, final String key, final String jobId, final JobData jobData) {
        // call put
        final Bundle params = new Bundle();
        int ret = -1;

        params.putParcelable(KEY_JOB_DATA, jobData);
        params.putString(KEY_JOB_DATA_KEY, key);
        params.putString(KEY_JOB_ID, jobId);

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.PUT.name(), null, params);

        if (bundle != null && bundle.containsKey(KEY_COUNT)) {
            ret = bundle.getInt(KEY_COUNT);
        }

        return ret;
    }

    /**
     * Removes JobData with key from provider
     *
     * @param resolver {@link ContentResolver}
     * @param key to remove at
     *
     * @return number of remained items
     */
    public static int remove(final ContentResolver resolver, final String key) {
        final Bundle params = new Bundle();
        int ret = -1;

        //0. take current job log
        JobData ids = get(resolver, key);
        params.putString(KEY_JOB_DATA_KEY, key);

        //1. remove
        SLog.d(TAG, "Removed for " + getJobIds(resolver).toString());
        resolver.call(CONTENT_URI, DataMethod.REMOVE.name(), null, params);

        //2. put current job log when job is only SCAN.
        // Put is required because JobData is requested even after remove.
        // it using from Scanlet.Method.PUT_FILE_REQ of ScanletContentProvider.java
        if(ids != null && ids.mDeviceType == JobInfo.JobType.SCAN && mIsPutData) {
            ret = put(resolver, key, ids.mJobId, ids);
        }
        mIsPutData = true;

        SLog.d(TAG, "Logged for " + getJobIds(resolver).toString());
        return ret;
    }

    /**
     * Gets JobData with key from provider
     *
     * @param resolver {@link ContentResolver}
     * @param key to get at
     *
     * @return JobData with the key
     */
    public static JobData get(final ContentResolver resolver, final String key) {
        final Bundle params = new Bundle();
        JobData data = null;

        params.putString(KEY_JOB_DATA_KEY, key);

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.GET.name(), null, params);

        if (bundle != null && bundle.containsKey(KEY_JOB_DATA)) {
            data = bundle.getParcelable(KEY_JOB_DATA);
        }

        return data;
    }

    /**
     * Checks if job with key is contained
     *
     * @param resolver {@link ContentResolver}
     * @param key to be checked for existence in the provider
     *
     * @return true if job with key is contained
     */
    public static boolean contains(final ContentResolver resolver, final String key) {
        // call put
        final Bundle params = new Bundle();
        boolean ret = false;

        params.putString(KEY_JOB_DATA_KEY, key);

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.CONTAINS.name(), null, params);

        if (bundle != null && bundle.containsKey(KEY_CONTAINS)) {
            ret = bundle.getBoolean(KEY_CONTAINS);
        }
        return ret;
    }

    /**
     * Checks size
     *
     * @param resolver {@link ContentResolver}
     *
     * @return int number of jobs in the storage, -1 if call failed
     */
    public static int size(final ContentResolver resolver) {
        int ret = -1;
        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.SIZE.name(), null, null);

        if (bundle != null && bundle.containsKey(KEY_COUNT)) {
            ret = bundle.getInt(KEY_COUNT);
        }

        return ret;
    }

    /**
     * Retrieves all jobs from provider
     *
     * @param resolver {@link ContentResolver}
     *
     * @return List of JobData
     */
    public static List<JobData> getJobs(final ContentResolver resolver) {
        final List<JobData> jobs;
        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.GET_ALL.name(), null, null);

        if (bundle != null && bundle.containsKey(KEY_JOBS)) {
            jobs = bundle.getParcelableArrayList(KEY_JOBS);
        } else {
            jobs = new ArrayList<>();
        }

        return jobs;
    }


    /**
     * Gets all current jobs in the provider
     *
     * @param resolver {@link ContentResolver}
     *
     * @return List of stored job id
     */
    public static List<String> getJobIds(final ContentResolver resolver) {
        List<String> list = new ArrayList<>();

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.GET_IDS.name(), null, null);

        if (bundle != null && bundle.containsKey(KEY_JOBS)) {
            list = bundle.getStringArrayList(KEY_JOBS);
        }

        return list;
    }

    /**
     * Gets list of {@link ErrorCode} by job id
     *
     * @param resolver {@link ContentResolver}
     * @param jobId
     *
     * @return List of stored causes related to this job id
     */
    public static List<ErrorCode> getCausesById(final ContentResolver resolver, final String jobId) {
        final Bundle params = new Bundle();

        params.putString(KEY_JOB_ID, jobId);

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.GET_CAUSES_BY_ID.name(), null, params);

        if (bundle == null) {
            return null;
        } else {
            return bundle.getParcelableArrayList(KEY_CAUSES);
        }
    }

    /**
     * Gets all current persistent jobs
     *
     * @param resolver {@link ContentResolver}
     *
     * @return List of stored job id
     */
    public static List<Bundle> getPersistedJobData(final ContentResolver resolver) {
        // call put
        List<Bundle> list = new ArrayList<>();

        final Bundle bundle = resolver.call(CONTENT_URI, DataMethod.PERSISTENT_DATA.name(), null, null);

        if (bundle != null && bundle.containsKey(KEY_JOB_DATA)) {
            list = bundle.getParcelableArrayList(KEY_JOB_DATA);
        }

        return list;
    }
}
