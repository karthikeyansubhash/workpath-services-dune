// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.providers;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.common.contract.SelectedPrinterContract;
import com.hp.jetadvantage.link.common.model.ApiType;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.util.BundleTypeAdapterFactory;
import com.hp.jetadvantage.link.services.common.util.UriJsonAdapter;

/**
 * Content provider which keeps the printer information along with the printer state
 * information.
 */
public class SelectedPrinterContentProvider extends ContentProvider {
    private static final String TAG = "LSMCP";

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.lsmcp";

    /**
     * Private LSM shared Preferences for printer info
     */
    private static final String LSM_PREFERENCES = "LSM_Content";

    private static final String PRINTER_DEFAULT_HOSTNAME = "fwprinter2";

    /**
     * Preference for Printer info
     */
    private static final String PRINTER_INFO_PREF = "spsPrinterInfo_pref";

    private static final String EXP_UNKNOWN_URI = "Unknown URI ";

    private static final String EXP_UNKNOWN_INVALID_URI = "Unknown or Invalid URI ";

    private static final int LSMCP_CODE = 1;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static PrinterInfo sPrinterInfo;

    static {
        URI_MATCHER.addURI(SelectedPrinterContract.AUTHORITY, SelectedPrinterContract.DIR_PATH_SEGMENT, LSMCP_CODE);
    }

    private Gson mGson;

    /**
     * To store printer info between restarts
     */
    private SharedPreferences mSharedPrefs;

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case LSMCP_CODE:
                SLog.d(TAG, "  in LSMCP_CODE");
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
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @Override
    public boolean onCreate() {
        mGson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Uri.class, new UriJsonAdapter())
                .registerTypeAdapterFactory(new BundleTypeAdapterFactory())
                .create();

        if (getContext() != null) {
            mSharedPrefs = getContext().getSharedPreferences(LSM_PREFERENCES, Context.MODE_PRIVATE);
        }

        SpsPermissionHelper.grantPermission(getContext());
        sPrinterInfo = retrievePrinterInfoFromPref();

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
    public Cursor query(@NonNull final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
                        final String sortOrder) {
        // No query supported for this LSM, to avoid crashes on tablet
        return null;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
     * Note: There is no need to close the DB in a Content Provider as per this post:
     * https://groups.google.com/forum/#!msg/android-developers/NwDRpHUXt0U/jIam4Q8-cqQJ
     * Also close database introduces exceptions with Cursors return using the query method:
     * Cannot perform this operation because the connection pool has been closed
     */
    @Override
    public int update(@NonNull final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        throw new IllegalArgumentException(EXP_UNKNOWN_URI + uri);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        final Bundle bundle = new Bundle();

        switch (method) {
            case SelectedPrinterContract.Method.ADD: {
                Preconditions.checkNotNull(extras, "Bundle is required for insertion");
                extras.setClassLoader(PrinterInfo.class.getClassLoader());
                final PrinterInfo pi = extras.getParcelable(SelectedPrinterContract.KEY_PRINTER_INFO);

                setPrinterInfo(pi);
                SLog.i(TAG, "ADD PrinterInfo: " + pi);
                break;
            }
            case SelectedPrinterContract.Method.CLEAR:
                setPrinterInfo(getDefaultPrinterInfo());

                break;
            case SelectedPrinterContract.Method.GET:
                final PrinterInfo pi = getPrinterInfo(false);
                SLog.d(TAG, "GET after PrinterInfo: " + pi);

                bundle.putParcelable(SelectedPrinterContract.KEY_PRINTER_INFO, pi != null ? pi : new PrinterInfo.Builder().build());
                break;
            default:
                // A call was made which is not understood
                throw new IllegalArgumentException("The method {" + method + " is not supported by the content provider");
        }

        return bundle;
    }

    /**
     * Retrieves SpsPrinterInfo
     *
     * @param force true if should restore value
     * @return {@link PrinterInfo}
     */
    private synchronized PrinterInfo getPrinterInfo(final boolean force) {
        SLog.d(TAG, "getPrinterInfo ENTER : " + sPrinterInfo + ", force :" + force);
        if (sPrinterInfo == null && force) {
            // Try to retrieve saved info
            sPrinterInfo = retrievePrinterInfoFromPref();
            setPrinterInfo(sPrinterInfo);
        }
        SLog.d(TAG, "getPrinterInfo : sPrinterInfo - " + sPrinterInfo);
        return sPrinterInfo != null ? sPrinterInfo : new PrinterInfo.Builder().build();
    }

    private synchronized PrinterInfo retrievePrinterInfoFromPref() {
        final String infoStr = mSharedPrefs.getString(PRINTER_INFO_PREF, null);
        PrinterInfo printerInfo;
        if (infoStr != null) {
            try {
                printerInfo = mGson.fromJson(infoStr, PrinterInfo.class);
                if (PrinterInfo.isEmpty(sPrinterInfo)) {
                    printerInfo = new PrinterInfo.Builder().build();
                }
            } catch (JsonSyntaxException e) {
                SLog.e(TAG, "Failed to retrieve printer info", e);
                printerInfo = new PrinterInfo.Builder().build();
            }
        } else {
            printerInfo = getDefaultPrinterInfo();
        }
        SLog.i(TAG, "retrievePrinterInfoFromPref : sPrinterInfo - " + sPrinterInfo);
        return printerInfo;
    }

    /**
     * Sets printer info
     *
     * @param info to be stored
     */
    private synchronized void setPrinterInfo(final PrinterInfo info) {
        if (info != null) {
            sPrinterInfo = info;
            SLog.i(TAG, "setPrinterInfo PrinterInfo : " + sPrinterInfo);
            try {
                mSharedPrefs.edit().putString(PRINTER_INFO_PREF, mGson.toJson(sPrinterInfo)).apply();
            } catch (Exception e) {
                SLog.e(TAG, "setPrinterInfo failed to save printerinfo " + e.getMessage(), e);
            }
        }
        SLog.d(TAG, "Printer info is set as " + sPrinterInfo);
    }

    private PrinterInfo getDefaultPrinterInfo() {
            PrinterInfo printerInfo = new PrinterInfo.Builder()
                    .api(ApiType.OXP)
                    .ip(PRINTER_DEFAULT_HOSTNAME)
                    .name("LOCAL OXPd2")
                    .baseUri(Uri.parse("https://"+PRINTER_DEFAULT_HOSTNAME+"/"))
                    .build();

            return printerInfo;
    }
}
