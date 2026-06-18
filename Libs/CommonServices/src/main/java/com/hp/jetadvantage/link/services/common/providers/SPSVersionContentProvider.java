// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.mfpsversion.data.MFPSVersionCP;


/**
 * Content provider which keeps the printer information along with the up state
 * information.
 * 
 * @author APS
 * 
 */
public class SPSVersionContentProvider extends ContentProvider {
    private static final String LOG_TAG = "MFPSVCP";

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.mfpsvcp";

    private static final String EXP_UNKNOWN_URI = "Unknown URI ";

    private static final String EXP_UNKNOWN_INVALID_URI = "Unknown or Invalid URI ";

    private static final int CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String ACTION_START_AUTH_OBSERVER = "com.hp.jetadvantage.link.services.accesslet.START_AUTH_OBSERVER";
//    private static final String ACTION_START_ACCESSORY_OBSERVER = "com.hp.jetadvantage.link.services.accessorylet.START_ACCESSORY_OBSERVER";
    private static final String ACTION_START_DEVICE_EVENT_OBSERVER = "com.hp.jetadvantage.link.services.deviceeventslet.START_DEVICE_EVENT_OBSERVER";

    static {
        URI_MATCHER.addURI(MFPSVersionCP.AUTHORITY, MFPSVersionCP.DIR_PATH_SEGMENT, CODE);
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @Override
    public String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CODE:
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException(EXP_UNKNOWN_INVALID_URI + uri);
        }

    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     * Note: There is no need to close the DB in a Content Provider as per this post:
     * https://groups.google.com/forum/#!msg/android-developers/NwDRpHUXt0U/jIam4Q8-cqQJ
     * Also close database introduces exceptions with Cursors return using the query method:
     * Cannot perform this operation because the connection pool has been closed
     */
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     * Note: There is no need to close the DB in a Content Provider as per this post:
     * https://groups.google.com/forum/#!msg/android-developers/NwDRpHUXt0U/jIam4Q8-cqQJ
     * Also close database introduces exceptions with Cursors return using the query method:
     * Cannot perform this operation because the connection pool has been closed
     */
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
                        final String sortOrder) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
     * Note: There is no need to close the DB in a Content Provider as per this post:
     * https://groups.google.com/forum/#!msg/android-developers/NwDRpHUXt0U/jIam4Q8-cqQJ
     * Also close database introduces exceptions with Cursors return using the query method:
     * Cannot perform this operation because the connection pool has been closed
     */
    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @Override
    public synchronized Bundle call(final String method, final String arg, final Bundle extras) {
        final Bundle bundle = new Bundle();

        if (getContext() == null) {
            return null;
        }

        if (Platform.isPanel()) {
            try {
                // start auth observer
                Intent startServer = new Intent(ACTION_START_AUTH_OBSERVER);
                startServer.setPackage(getContext().getPackageName()); // sent only to Package Manager app
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(startServer);
            } catch (Throwable throwable) {}

//            try {
//                // start accessory observer
//                Intent startServerForAccessory = new Intent(ACTION_START_ACCESSORY_OBSERVER);
//                startServerForAccessory.setPackage(getContext().getPackageName());
//                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(startServerForAccessory);
//            } catch (Throwable throwable) {}

            try {
                // start device events observer
                Intent startDeviceEventsObserver = new Intent(ACTION_START_DEVICE_EVENT_OBSERVER);
                startDeviceEventsObserver.setPackage(getContext().getPackageName());
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(startDeviceEventsObserver);
            } catch (Throwable throwable) {}
        }

        if (MFPSVersionCP.Method.GET_VERSION.equals(method)) {
            bundle.putString(MFPSVersionCP.Contract.KEY_VERSION, Sdk.VERSION.VERSION_NAME);
        } else if (MFPSVersionCP.Method.GET_API_LEVEL.equals(method)) {
            // first check permission and show our activity if needed

            if (!SpsPermissionHelper.grantPermission(getContext())) {
                throw new SecurityException("Permission is not granted");
            }

            bundle.putInt(MFPSVersionCP.Contract.KEY_API_LEVEL, Sdk.VERSION.LEVEL);
            bundle.putString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL, Sdk.VERSION.VERSION_NAME);

            if(TextUtils.isEmpty(arg)) {
                throw new SecurityException("HP Workpath SDK library requires update");
            }

//TODO : [DUNE-XXX] not sure this is required on Dune

//            if (Platform.isPanel()) { //Try to enable device events
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("EnableDeviceEvents", true);
//
//                    Log.i(LOG_TAG, "Try to enable device events: " + jsonObject.toString());
//                    String response = OXPdConnect.getInstance().callOmniInternal(OmniRequest.PUT, OmniRequest.DEVICE_EVENTS_INFO, jsonObject.toString());
//                    Log.i(LOG_TAG, "Try to enable device events result: " + ((response != null)?response:"empty"));
//                } catch (Throwable throwable) {
//                    Log.e(LOG_TAG, "Failed to enable device events: " + throwable.getMessage());
//                }
//            }

            if(extras == null) {
                if(Integer.valueOf(arg) >= 3) {
                    bundle.putInt(MFPSVersionCP.Contract.KEY_API_LEVEL, Sdk.VERSION_LEVEL.ONE);
                    bundle.putString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL, Sdk.VERSION.NO_VERSION);
                }
            } else {
                if(extras.containsKey(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL)) {
                    String versionName = extras.getString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL);
                    if(!Sdk.VERSION.VERSION_NAME.equalsIgnoreCase(versionName)) {
                        bundle.putString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL, Sdk.VERSION.NO_VERSION);
                    }
                }
            }
        } else if (MFPSVersionCP.Method.GET_API_LEVEL_INTERNAL.equals(method)) {
            bundle.putInt(MFPSVersionCP.Contract.KEY_API_LEVEL, Sdk.VERSION.LEVEL);
            bundle.putString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL, Sdk.VERSION.VERSION_NAME);
        } else {
            // A call was made which is not understood
            throw new IllegalArgumentException("The method {" + method + " is not supported by the content provider");
        }

        return bundle;
    }
}
