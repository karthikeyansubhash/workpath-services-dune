// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.ErrorCode;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.intents.BaseJobCancelIntent;
import com.hp.jetadvantage.link.common.intents.CopyJobCancelIntent;
import com.hp.jetadvantage.link.common.intents.PrintJobCancelIntent;
import com.hp.jetadvantage.link.common.intents.ScanJobCancelIntent;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.common.ssp.SpsCauseHelper;
import com.hp.jetadvantage.link.services.joblet.model.JobData;
import com.hp.jetadvantage.link.services.joblet.model.JobDataContentProvider;
import com.hp.jetadvantage.link.services.joblet.model.JobState;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ContentProvider} for storing jobs data, registered clients data and execute elementary operations
 * on jobs like confirm, cancel etc.
 */
public class JobletContentProvider extends ContentProvider {
    private static final String TAG = "Joblet";
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.joblet";
    private static final String CONTENT_CLIENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.joblet.client";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String EXP_UNKNOWN_INVALID_URI = "Unknown or Invalid URI";
    private static final int JOBLET_CODE = 2;
    private static final int JOBLET_CLIENT_CODE = 3;
    private static final String JOBLET_DATABASE = "joblet_db";
    private static final int DB_VERSION = 1;

    private SQLiteDbHelper mDbHelper;

    private static final Map<String, String> PROJECTION_MAP;

    /**
     * Uri for clients data, only for internal use since there's accessor methods presented here
     */
    private static final Uri CLIENTS_CONTENT_URI = Joblet.CONTENT_URI.buildUpon().appendPath(Joblet.CLIENTS_PATH_SEGMENT).build();

    static {
        URI_MATCHER.addURI(Joblet.AUTHORITY, Joblet.DIR_PATH_SEGMENT, JOBLET_CODE);
        URI_MATCHER.addURI(Joblet.AUTHORITY, Joblet.DIR_PATH_SEGMENT + "/" + Joblet.CLIENTS_PATH_SEGMENT, JOBLET_CLIENT_CODE);
    }

    static {
        // Creates a new projection map with the mappings for the error codes
        // contract
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(Columns._ID, Columns._ID);
        PROJECTION_MAP.put(Columns.PACKAGE_NAME, Columns.PACKAGE_NAME);
        PROJECTION_MAP.put(Columns.RID, Columns.RID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new SQLiteDbHelper(getContext(), JOBLET_DATABASE, null, DB_VERSION);

        return true;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case JOBLET_CODE:
                return CONTENT_TYPE;
            case JOBLET_CLIENT_CODE:
                return CONTENT_CLIENT_TYPE;
            default:
                throw new IllegalArgumentException(EXP_UNKNOWN_INVALID_URI + uri);
        }
    }

    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        Bundle bundle = new Bundle();

        if (getContext() == null) {
            return null;
        }

        if (extras == null) {
            return Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "No Job ID received");
        }

        SpsPermissionHelper.ensurePermission(getContext());

        final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
        if (PrinterInfo.isEmpty(pi)) {
            SLog.w(TAG, "PrinterInfo is null");
            return Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "PrinterInfo is null");
        }

        final String appPackageName = this.getCallingPackage();

        ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            final ThreadPolicy policy = new ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            final String jobId = extras.getString(Joblet.Params.JOB_ID_TAG);
            switch (method) {
                case Joblet.Method.CANCEL_JOB:
                    switch (pi.getApiType()) {
                        case BASIC:
                        case OXP:
                            bundle = cancelBasicJob(jobId, appPackageName);
                            break;

                        default:
                            SLog.w(TAG, "API doesn't support job cancel");
                            bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Job cancel is not supported");
                            break;
                    }
                    break;
                case Joblet.Method.GET_JOB_INFO:
                    switch (pi.getApiType()) {
                        case OXP:
                            bundle = getOXPdJobInfo(jobId);
                            break;
                    }
                    break;
                case Joblet.Method.GET_PROCESSING_INFO:
                    SLog.w(TAG, "API doesn't support job info retrieval");
                    bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Job data is not supported");
                    break;
                case Joblet.Method.GET_JOB_RID:
                    final JobData jobData = JobDataContentProvider.get(getContext().getContentResolver(),
                            JobService.getRid(getContext(), jobId));
                    if (jobData != null && jobData.mRid != null) {
                        SLog.d(TAG, "Found rid " + jobData.mRid + " for job " + jobId);
                        bundle = Result.pack(bundle, Result.RESULT_OK);
                        bundle.putString(Joblet.Keys.KEY_RID, jobData.mRid);
                    } else {
                        SLog.w(TAG, "API doesn't support job info retrieval");
                        bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, "Job data is missing or doesn't contain RID");
                    }
                    break;
            }
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }
        return bundle;
    }

    private Bundle getOXPdJobInfo(final String jobId) {
        final JobData jobData = JobDataContentProvider.get(getContext().getContentResolver(),
                JobService.getRid(getContext(), jobId));

        final Bundle bundle = new Bundle();
        bundle.putInt(Result.KEY_CODE, Result.RESULT_OK);

        try {
            final JobInfo info = JobData.getJobInfo(jobData);
            bundle.putParcelable(Joblet.Keys.KEY_JOB_INFO, info);
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        }

        return bundle;
    }

    /**
     * Executes 'basic' API job cancel
     *
     * @param jobId to be cancelled
     * @return result bundle
     */
    private Bundle cancelBasicJob(final String jobId, final String appPackageName) {
        final JobData jobData = JobDataContentProvider.get(getContext().getContentResolver(), JobService.getRid(getContext(), jobId));
        Bundle bundle = new Bundle();

        if (jobData == null) {
            Log.d(TAG, "Job " + jobId + " doesn't exist");
            //bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "No such job");
            bundle.putInt(Result.KEY_CODE, Result.RESULT_OK);
            bundle.putString(Result.KEY_CAUSE, SpsCauseHelper
                    .generateJsonWithCauses(Collections.singletonList(ErrorCode.OPERATION_NOT_SUPPORTED)));

            sendLocalJobData(JobletService.Event.TL_EV_JOB_CANCELED, new Bundle());
        } else {
            final JobState state = jobData.getState();

            switch (state) {
                case TL_ST_CANCELED:
                case TL_ST_COMPLETE:
                case TL_ST_FAILED:
                    // Job is already finished, not able to cancel!
//                    bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE,
//                            SpsCauseHelper
//                                    .generateJsonWithCauses(null, Collections.singletonList(ErrorCode.INVALID_JOB_STATE)));
                    Log.d(TAG, "Job cancel failed : " + SpsCauseHelper
                            .generateJsonWithCauses(Collections.singletonList(ErrorCode.INVALID_JOB_STATE)));
                    bundle.putInt(Result.KEY_CODE, Result.RESULT_OK);
                    bundle.putString(Result.KEY_CAUSE, SpsCauseHelper
                            .generateJsonWithCauses(Collections.singletonList(ErrorCode.INVALID_JOB_STATE)));

                    sendLocalJobData(JobletService.Event.TL_EV_JOB_CANCELED, new Bundle());
                    break;

                // All other states are basically fine to try to cancel the job
                default:
                    Intent intent = createJobCancelIntent(jobData.mDeviceType, jobId, appPackageName);
                    if (intent != null) {
                        // Send it only to service app
                        intent.setPackage(Sdk.SERVICES_PACKAGE);
                        getContext().startForegroundService(intent);

                        bundle = new Bundle();
                        bundle.putInt(Result.KEY_CODE, Result.RESULT_OK);
                    } else {
                        //bundle = Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN);
                        bundle.putInt(Result.KEY_CODE, Result.RESULT_OK);
                        bundle.putString(Result.KEY_CAUSE, SpsCauseHelper
                                .generateJsonWithCauses(Collections.singletonList(ErrorCode.UNABLE_TO_GET_SUBMISSION_RESULT)));

                        sendLocalJobData(JobletService.Event.TL_EV_JOB_CANCELED, new Bundle());
                    }
                    break;
            }
        }

        return bundle;
    }

    private void sendLocalJobData(final JobletService.Event event, final Bundle jobBundle) {
        final Intent intent = new Intent(JobletService.ACTION_BASIC_EVENT);

        jobBundle.putString(JobletService.EXTRA_BASIC_EVENT, event.name());
        intent.putExtras(jobBundle);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    /**
     * Checks if client package has joblet observer registered
     *
     * @param resolver      {@link ContentResolver}
     * @param clientPackage {@link String} to be checked
     * @param rid           of the job to check client for
     * @return true if client is registered
     */
    public static boolean isClientRegistered(final ContentResolver resolver, final String clientPackage, final String rid) {
        final Cursor cursor = resolver.query(CLIENTS_CONTENT_URI,
                new String[]{Columns.PACKAGE_NAME},
                Columns.PACKAGE_NAME + "=?" + " AND " + Columns.RID + "=?",
                new String[]{clientPackage, rid},
                null);
        boolean presented = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }

        return presented;
    }

    /**
     * Removes client package from registered list
     *
     * @param resolver    {@link ContentResolver}
     * @param packageName {@link String} package name to remove
     * @param rid         {@link String} to be removed
     */
    public static void removeClient(final ContentResolver resolver, final String packageName, final String rid) {
        final boolean packagePresented = !TextUtils.isEmpty(packageName);
        final boolean ridPresented = !TextUtils.isEmpty(rid);

        if (packagePresented && ridPresented) {
            resolver.delete(CLIENTS_CONTENT_URI, Columns.RID + "=?" + " AND " + Columns.PACKAGE_NAME + "=?", new String[]{rid, packageName});
        } else {
            if (packagePresented) {
                resolver.delete(CLIENTS_CONTENT_URI, Columns.PACKAGE_NAME + "=?", new String[]{packageName});
            } else if (ridPresented) {
                resolver.delete(CLIENTS_CONTENT_URI, Columns.RID + "=?", new String[]{rid});
            }
        }
    }

    @Override
    public synchronized Cursor query(@NonNull final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        SpsPermissionHelper.ensurePermission(getContext());

        switch (URI_MATCHER.match(uri)) {
            case JOBLET_CLIENT_CODE:
                break;

            default:
                throw new IllegalArgumentException("Illegal uri requested " + uri);
        }

        Cursor cursor = null;

        try {
            final SQLiteDatabase db = mDbHelper.getReadableDatabase();

            final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setStrict(true);
            qb.setTables(CLIENTS_TABLE);
            qb.setProjectionMap(PROJECTION_MAP);

            cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (final SQLiteException ex) {
            SLog.e(TAG, "Failed to get DB " + ex);
        } catch (final NullPointerException ex2) {
            SLog.e(TAG, "Failed to get DB " + ex2);
            // Old DB get corrupted, remove it and create new one
            if (getContext() != null) {
                getContext().deleteDatabase(JOBLET_DATABASE);
                mDbHelper = new SQLiteDbHelper(getContext(), JOBLET_DATABASE, null, DB_VERSION);
            }
        }

        return cursor;
    }

    @Override
    public synchronized Uri insert(@NonNull final Uri uri, final ContentValues values) {
        SpsPermissionHelper.ensurePermission(getContext());

        long inserted = 0;

        try {
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();

            inserted = db.insert(CLIENTS_TABLE, null, values);
            SLog.w(TAG, "Inserted " + inserted);
        } catch (final SQLiteException ex) {
            SLog.e(TAG, "Failed to get DB " + ex);
        } catch (final NullPointerException ex2) {
            SLog.e(TAG, "Failed to get DB " + ex2);
            // Old DB get corrupted, remove it and create new one
            if (getContext() != null) {
                getContext().deleteDatabase(JOBLET_DATABASE);
                mDbHelper = new SQLiteDbHelper(getContext(), JOBLET_DATABASE, null, DB_VERSION);
            }
        }

        return CLIENTS_CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(inserted)).build();
    }

    @Override
    public synchronized int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        SpsPermissionHelper.ensurePermission(getContext());

        int deleted = 0;

        try {
            /*
              delete() is called only in removeClient(ContentResolver, String, String) above,
              so all parameters for selection are predefined and known.
              To avoid SQL injection warning selection is recreated here,
              so input parameter "selection" is not passed directly to database
             */
            String querySelection = "";
            if (selection != null) {
                if (selection.contains(Columns.RID) && selection.contains(Columns.PACKAGE_NAME)) {
                    querySelection = Columns.RID + "=? AND " + Columns.PACKAGE_NAME + "=?";
                } else if (selection.contains(Columns.RID)) {
                    querySelection = Columns.RID + "=?";
                } else if (selection.contains(Columns.PACKAGE_NAME)) {
                    querySelection = Columns.PACKAGE_NAME + "=?";
                }

                final SQLiteDatabase db = mDbHelper.getWritableDatabase();

                deleted = db.delete(CLIENTS_TABLE, querySelection, selectionArgs);
            }
            SLog.w(TAG, "Deleted " + deleted);
        } catch (final SQLiteException ex) {
            SLog.e(TAG, "Failed to get DB " + ex);
        } catch (final NullPointerException ex2) {
            SLog.e(TAG, "Failed to get DB " + ex2);
            // Old DB get corrupted, remove it and create new one
            if (getContext() != null) {
                getContext().deleteDatabase(JOBLET_DATABASE);
                mDbHelper = new SQLiteDbHelper(getContext(), JOBLET_DATABASE, null, DB_VERSION);
            }
        }

        return deleted;
    }

    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return -1;
    }

    private static final String CLIENTS_TABLE = "clients";
    private static final String[] TABLES = {CLIENTS_TABLE};

    private static class Columns {
        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        static final String _ID = BaseColumns._ID;
        /**
         * Clients package name
         */
        static final String PACKAGE_NAME = "packageName";
        /**
         * Rid the client assigned to
         */
        static final String RID = "rid";
    }

    /**
     * Own database helper
     */
    private static class SQLiteDbHelper extends SQLiteOpenHelper {
        // A string that defines the SQL statement for creating a table
        private static final String SQL_CREATE_CLIENTS = "CREATE TABLE "
                + CLIENTS_TABLE + " ("
                + Columns._ID + " INTEGER PRIMARY KEY, "
                + Columns.PACKAGE_NAME + " TEXT, "
                + Columns.RID + " TEXT, "
                + " UNIQUE (" + Columns.RID + ") ON CONFLICT REPLACE"
                + ")";

        SQLiteDbHelper(final Context context, final String name, final SQLiteDatabase.CursorFactory factory, final int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CLIENTS);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            SLog.w(TAG, "Upgrading database, this will drop tables and recreate.");
            for (String s : TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + s);
            }
            onCreate(db);
        }
    }

    private BaseJobCancelIntent createJobCancelIntent(JobInfo.JobType jobType, String jobId, String appPackageName) {
        BaseJobCancelIntent intent = null;
        switch (jobType) {
            case PRINT:
                intent = new PrintJobCancelIntent(jobId, appPackageName);
                break;
            case SCAN:
                intent = new ScanJobCancelIntent(jobId, appPackageName);
                break;
            case COPY:
                intent = new CopyJobCancelIntent(jobId, appPackageName);
                break;
            default:
                break;
        }
        SLog.d(TAG, "createJobCancelIntent : jobType" + jobType + " jobId : " + jobId + " appPackageName : " + appPackageName + " intent : " + intent);
        return intent;
    }
}
