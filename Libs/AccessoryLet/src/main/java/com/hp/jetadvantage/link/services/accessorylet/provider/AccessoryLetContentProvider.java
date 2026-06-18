// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accessorylet.provider;

import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.ACCESSORY_NOT_REGISTERED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_INFO;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_REPORT;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_REPORT_TYPE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_CLOSE_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_GET_HID_INFO_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_GET_INFO_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_NOT_FOUND;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_NOT_OPENED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_NOT_READY;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_OPEN_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_READ_REPORT_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_REGISTRATION_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_RESEND_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_RESERVE_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_SERVICE_NOT_READY;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_START_READING_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_STOP_READING_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_WRITE_REPORT_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.Unauthorized.NO_ACTIVE_UI_SESSION;
import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryNotificationService.sendAccessoryNotificationToApp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet.Keys;
import com.hp.jetadvantage.link.api.accessory.hid.EventCode;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReport;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportType;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService;
import com.hp.jetadvantage.link.services.accessorylet.adapter.AccessoryDeviceAdapter;
import com.hp.jetadvantage.link.services.accessorylet.model.AccessoryRegistrationInfo;
import com.hp.jetadvantage.link.services.accessorylet.service.AccessoryCache;
import com.hp.jetadvantage.link.services.accessorylet.util.AccessoryRegistrationRecord;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Main AccessoryLet provider. It's responsible for serve base
 * {@link com.hp.jetadvantage.link.api.accessory.hid.AccessoryService} operations via
 * {@link #call(String, String, Bundle)} method.<br>
 */
public final class AccessoryLetContentProvider extends ContentProvider {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/CP";
    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Accessorylet";
    private static final int ACCESSORY_LET_CODE = 1;

    // Default report ID for HID reports :
    // In Jedi/Jolt, HID accessories that specify multiple report IDs are not recognized by the device,
    // and OXPd writes report ID "0" to the device according to the legacy OXPd spec. https://developers.hp.com/oxpd-netjava/doc/oxpd-accessories-service-specification
    // So, backward compatibility, the default report ID is set to 0.
    // Dune/E2 supports multiple report IDs, so the new Workpath API needs to define the report ID to set by
    // apps when reading or writing reports through accessory's control pipe in the future.
    private static final byte DEFAULT_REPORT_ID = 0;

    static {
        S_URI_MATCHER.addURI(Accessorylet.AUTHORITY, Accessorylet.DIR_PATH_SEGMENT, ACCESSORY_LET_CODE);
    }

    private IDeviceAccessoryService accessoryDeviceService;

    public AccessoryLetContentProvider() {
        this.accessoryDeviceService = new StandardDeviceAccessoryService();
    }

    // For testing purpose
    public AccessoryLetContentProvider(IDeviceAccessoryService accessoryService) {
        this.accessoryDeviceService = accessoryService;
    }

    /// ///////////////////// Override methods from ContentProvider ////////////////////////

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public synchronized Uri insert(final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public synchronized Cursor query(@NonNull final Uri uri, final String[] projection, final String selection,
                                     final String[] selectionArgs, final String sortOrder) {
        return null;
    }

    @Override
    public synchronized int update(@NonNull final Uri uri, final ContentValues values, final String selection,
                                   final String[] selectionArgs) {
        return 0;
    }

    @Override
    public synchronized int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        switch (S_URI_MATCHER.match(uri)) {
            case ACCESSORY_LET_CODE:
                Log.d(TAG, " in ACCESSORY_LET_CODE ");
                return CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Override
    public synchronized Bundle call(@NonNull final String method, final String arg, final Bundle extras) {
        Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        Log.v(TAG, "call " + method);
        try {
            PrinterInfo pi = validatePermissionAndGetPrinterInfo();
            // Allow network operations during the processing
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

            boolean serviceSupported = isSupported();
            if (Accessorylet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Accessorylet.IS_SUPPORTED_EXTRA, serviceSupported);
                return bundle;
            }
            if (!serviceSupported) {
                throw new SdkNotSupportedException("AccessoryService is not supported");
            }
            String pkgName = validatePackageName(extras, getCallingPackage());
            Log.v(TAG, "method: " + method + " pkgName: " + pkgName);
            validateClientVersion(extras);

            bundle = handleMethod(getContext(), method, extras, pkgName, bundle);
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }
        return bundle;
    }

    /// ///////////////////// Protected methods ////////////////////////

    protected Bundle handleMethod(Context context, String method, Bundle extras, String pkgName,
                                  Bundle bundle) throws SdkException {
        String accessoryContextId = extras.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID);
        switch (method) {
            case Accessorylet.Method.IS_READY:
                return isReady(bundle);
            case Accessorylet.Method.REGISTER:
                final AccessoryInfo regInfo = extras.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION);
                return registerAccessory(context, bundle, regInfo, pkgName);
            case Accessorylet.Method.ENUMERATE:
                return getFilteredAccessories(bundle, pkgName, AccessoryDeviceAdapter::enumerateAccessories);
            case Accessorylet.Method.GET_SHARED:
                return getFilteredAccessories(bundle, pkgName, AccessoryDeviceAdapter::getSharedAccessories);
            case Accessorylet.Method.GET_OWNED:
                return getFilteredAccessories(bundle, pkgName, AccessoryDeviceAdapter::getOwnedAccessories);
            case Accessorylet.Method.RESEND_OWNED:
                final HIDAccessoryInfo resendInfo = extras.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO);
                return resendOwnedAccessoryEvent(context, bundle, resendInfo, pkgName);
            case Accessorylet.Method.RESERVE:
                final HIDAccessoryInfo reserveInfo = extras.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO);
                return reserveSharedAccessory(context, bundle, reserveInfo, pkgName);
            case Accessorylet.Method.RELEASE:
                return releaseSharedAccessory(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.OPEN:
                return open(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.CLOSE:
                return close(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.START_READING:
                return startReading(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.STOP_READING:
                return stopReading(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.GET_HID_INFO:
                return getInfo(bundle, accessoryContextId, pkgName);
            case Accessorylet.Method.READ_REPORT:
                final HIDReportType reportType = (HIDReportType) extras.getSerializable(Keys.KEY_HID_REPORT_TYPE);
                return readReport(bundle, accessoryContextId, reportType, pkgName);
            case Accessorylet.Method.WRITE_REPORT:
                final HIDReport report = extras.getParcelable(Keys.KEY_HID_REPORT);
                return writeReport(bundle, accessoryContextId, report, pkgName);
            default:
                throw new SdkInvalidParamException("Method " + method + " is not supported");
        }
    }

    protected Bundle isReady(final Bundle bundle) throws SdkException {
        Bundle result = executeCallMethod(ACCESSORY_SERVICE_NOT_READY, () -> {
            if (AccessoryDeviceAdapter.isReady(accessoryDeviceService)) {
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putBoolean(Accessorylet.IS_SUPPORTED_EXTRA, true);
            } else {
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Service is not ready");
            }
            return bundle;
        });
        return result;
    }

    protected Bundle registerAccessory(final Context context, final Bundle bundle, final AccessoryInfo accessoryInfo,
                                       final String pkgName) throws SdkException {
        Log.d(TAG, String.format("registerAccessory: ENTER [%s, %s]", pkgName, accessoryInfo));
        Bundle result = executeCallMethod(ACCESSORY_REGISTRATION_FAILURE, () -> {
            HIDAccessoryInfo hidAccessoryInfo = (HIDAccessoryInfo) accessoryInfo;
            String accessoryId = getAccessoryIdOwnedByApp(context, pkgName, hidAccessoryInfo);
            if (StringUtility.isEmpty(accessoryId)) {
                Log.e(TAG, String.format("registerAccessory: %s [%s, %s]", ACCESSORY_NOT_FOUND, pkgName,
                        accessoryInfo));
                throw new SdkServiceErrorException(ACCESSORY_NOT_FOUND);
            }
            sendAccessoryNotificationToApp(context, pkgName, accessoryId, EventCode.CONTEXT_CREATED);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        });
        Log.d(TAG, String.format("registerAccessory: EXIT [%s, %s]", pkgName, accessoryInfo));
        return result;
    }

    // A helper method to get shared, owned, or enumerated accessories and filter only registered ones.
    protected Bundle getFilteredAccessories(final Bundle bundle, String pkgName, AccessoryProvider provider) throws SdkException {
        Log.d(TAG, String.format("getFilteredAccessories: ENTER [%s]", pkgName));
        Bundle result = executeCallMethod(ACCESSORY_GET_INFO_FAILURE, () -> {
            ArrayList<AccessoryInfo> accessories = provider.getAccessories(accessoryDeviceService, pkgName);
            List<AccessoryRegistrationInfo> registered =
                    AccessoryRegistrationRecord.getRegisteredAccessories(getContext(), pkgName);
            accessories.removeIf(accessory -> !isRegisteredAccessory(accessory, registered));
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putParcelableArrayList(Result.KEY_RESULT, accessories);
            return bundle;
        });
        Log.d(TAG, String.format("getFilteredAccessories: EXIT [%s]", pkgName));
        return result;
    }

    protected Bundle resendOwnedAccessoryEvent(final Context context, final Bundle bundle,
                                               final HIDAccessoryInfo accessoryInfo,
                                               final String pkgName) throws SdkException {
        Log.d(TAG, String.format("resendOwnedAccessoryEvent: ENTER [%s, %s]", pkgName, accessoryInfo));
        Bundle result = executeCallMethod(ACCESSORY_RESEND_FAILURE, () -> {
            String accessoryId = getAccessoryIdOwnedByApp(context, pkgName, accessoryInfo);
            if (StringUtility.isEmpty(accessoryId)) {
                Log.e(TAG, String.format("resendOwnedAccessoryEvent: %s [%s, %s]", ACCESSORY_NOT_FOUND, pkgName,
                        accessoryInfo));
                throw new SdkServiceErrorException(ACCESSORY_NOT_FOUND);
            }
            Log.d(TAG, String.format("resendOwnedAccessoryEvent: CONTEXT_RESENT [%s, %s]", pkgName,
                    accessoryInfo));
            sendAccessoryNotificationToApp(context, pkgName, accessoryId, EventCode.CONTEXT_RESENT);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        });

        Log.d(TAG, String.format("resendOwnedAccessoryEvent: EXIT [%s, %s]", pkgName, accessoryInfo));
        return result;
    }

    /**
     * In Dune/E2, there is no reserveSharedAccessory method. Instead, the shared accessory can be accessed with
     * UIContext token. So, just check here whether the shared accessory is registered for the app and the app is
     * active at the user interfaces. If so, return the accessoryId.
     *
     * @param bundle
     * @param accessoryInfo input accessory info that the app want to reserve
     * @param pkgName       package name of the app
     * @return bundle with the accessoryId if the shared accessory is registered for the app and the app has UI context
     * @throws SdkException SdkInvalidParamException if the accessory is not registered for the app,
     *                      SdkServiceErrorException if the accessory is not found in the device,
     *                      SdkUnauthorizedException if the app doesn't have UI context
     */
    protected Bundle reserveSharedAccessory(Context context, final Bundle bundle, final HIDAccessoryInfo accessoryInfo,
                                            final String pkgName) throws SdkException {
        Log.d(TAG, String.format("reserveSharedAccessory: ENTER [%s, %s]", pkgName, accessoryInfo));
        Bundle result = executeCallMethod(ACCESSORY_RESERVE_FAILURE, () -> {
            if (accessoryInfo == null) {
                Log.e(TAG, String.format("reserveSharedAccessory: %s [%s]", INVALID_ACCESSORY_INFO, pkgName));
                throw new SdkInvalidParamException(INVALID_ACCESSORY_INFO);
            }
            if (!accessoryDeviceService.isUiContextAvailable(pkgName)) {
                Log.e(TAG, "reserveSharedAccessory: UI context is not available for the package: " + pkgName);
                throw new SdkUnauthorizedException(NO_ACTIVE_UI_SESSION);
            }
            if (!AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(context, pkgName, accessoryInfo)) {
                Log.e(TAG, String.format("reserveSharedAccessory: %s [%s, %s]",
                        ACCESSORY_NOT_REGISTERED, pkgName, accessoryInfo));
                throw new SdkInvalidParamException(ACCESSORY_NOT_REGISTERED);
            }
            String accessoryId = AccessoryDeviceAdapter.getSharedAccessoryId(accessoryDeviceService, pkgName,
                    accessoryInfo);
            if (StringUtility.isEmpty(accessoryId)) {
                Log.e(TAG, String.format("reserveSharedAccessory: %s [%s, %s]",
                        ACCESSORY_NOT_FOUND, pkgName, accessoryInfo));
                throw new SdkServiceErrorException(ACCESSORY_NOT_FOUND);
            }
            AccessoryCache.getInstance().add(accessoryId, pkgName, accessoryInfo);
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putString(Result.KEY_RESULT, accessoryId);
            Log.d(TAG, String.format("reserveSharedAccessory:EXIT [%s,%s,%s]", accessoryId, pkgName, accessoryInfo));
            return bundle;
        });
        return result;
    }

    /**
     * In Dune/E2, there is no releaseSharedAccessory method. Just return the result with RESULT_OK.
     */
    protected Bundle releaseSharedAccessory(final Bundle bundle, final String accessoryContextId,
                                            final String pkgName) {
        Log.d(TAG, String.format("releaseSharedAccessory: ENTER [%s, %s]", pkgName, accessoryContextId));
        Result.pack(bundle, Result.RESULT_OK);
        Log.d(TAG, String.format("releaseSharedAccessory: EXIT [%s, %s]", pkgName, accessoryContextId));
        return bundle;
    }

    protected Bundle open(final Bundle bundle, final String accessoryId, final String pkgName) throws SdkException {
        Log.d(TAG, String.format("open: ENTER [%s, %s]", pkgName, accessoryId));
        Bundle result = executeCallMethod(ACCESSORY_OPEN_FAILURE, () -> {
            validateAccessoryExists(accessoryId, pkgName);
            UUID openHidId = AccessoryCache.getInstance().isOwnedType(accessoryId)
                    ? accessoryDeviceService.openOwnedHidAccessory(pkgName, accessoryId)
                    : accessoryDeviceService.openSharedHidAccessory(pkgName, accessoryId);
            if (openHidId == null) {
                Log.e(TAG, "open: failed to open : " + accessoryId);
                throw new SdkServiceErrorException(ACCESSORY_OPEN_FAILURE);
            }
            AccessoryCache.getInstance().setOpenHidId(accessoryId, openHidId);
            Log.d(TAG, "open: owned openHidId=" + openHidId);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        });
        Log.d(TAG, String.format("open: EXIT [%s, %s]", pkgName, accessoryId));
        return result;
    }

    protected Bundle close(final Bundle bundle, final String accessoryContextId, final String pkgName) throws SdkException {
        Log.d(TAG, String.format("close: ENTER [%s, %s]", pkgName, accessoryContextId));
        Bundle result = executeCallMethod(ACCESSORY_CLOSE_FAILURE, () -> {
            UUID openHidId = getOpenHidId(accessoryContextId, "close");
            accessoryDeviceService.closeHidAccessory(pkgName, accessoryContextId, openHidId);
            Result.pack(bundle, Result.RESULT_OK);
            return bundle;
        });
        Log.d(TAG, String.format("close: EXIT [%s, %s]", pkgName, accessoryContextId));
        return result;
    }

    protected Bundle startReading(final Bundle bundle, final String accessoryContextId, final String pkgName) throws SdkException {
        Log.d(TAG, String.format("startReading: ENTER [%s, %s]", accessoryContextId, pkgName));
        Bundle result = executeCallMethod(ACCESSORY_START_READING_FAILURE, () -> {
            UUID openHidId = getOpenHidId(accessoryContextId, "startReading");
            boolean isOwned = AccessoryCache.getInstance().isOwnedType(accessoryContextId);
            if (accessoryDeviceService.startAsyncReading(pkgName, openHidId, accessoryContextId, isOwned)) {
                Log.d(TAG, String.format("startReading: RESULT_OK [%s, %s]", accessoryContextId, pkgName));
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Log.e(TAG, "startReading: Failed to start reading accessory");
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                        ACCESSORY_START_READING_FAILURE);
            }
            return bundle;
        });
        Log.d(TAG, String.format("startReading: EXIT [%s, %s]", accessoryContextId, pkgName));
        return result;
    }

    protected Bundle stopReading(final Bundle bundle, final String accessoryContextId, final String pkgName) throws SdkException {
        Log.d(TAG, String.format("stopReading: ENTER [%s, %s]", accessoryContextId, pkgName));
        Bundle result = executeCallMethod(ACCESSORY_STOP_READING_FAILURE, () -> {
            UUID openHidId = getOpenHidId(accessoryContextId, "stopReading");
            boolean isOwned = AccessoryCache.getInstance().isOwnedType(accessoryContextId);
            if (accessoryDeviceService.stopAsyncReading(pkgName, openHidId, accessoryContextId, isOwned)) {
                Log.d(TAG, String.format("stopReading: RESULT_OK [%s, %s]", accessoryContextId, pkgName));
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Log.e(TAG, "stopReading: Failed to stop reading accessory");
                Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, ACCESSORY_STOP_READING_FAILURE);
            }
            return bundle;
        });
        Log.d(TAG, String.format("stopReading: EXIT [%s, %s]", accessoryContextId, pkgName));
        return result;
    }

    protected Bundle getInfo(final Bundle bundle, final String accessoryContextId, final String pkgName) throws SdkException {
        Bundle result = executeCallMethod(ACCESSORY_GET_HID_INFO_FAILURE, () -> {
            UUID openHidId = getOpenHidId(accessoryContextId, "getInfo");
            HIDInfo hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(accessoryDeviceService, pkgName, openHidId,
                    accessoryContextId);
            Result.pack(bundle, Result.RESULT_OK);
            bundle.putParcelable(Result.KEY_RESULT, hidInfo);
            return bundle;
        });
        return result;
    }

    protected Bundle readReport(final Bundle bundle, final String accessoryContextId, final HIDReportType reportType,
                                final String pkgName) throws SdkException {
        Bundle result = executeCallMethod(ACCESSORY_READ_REPORT_FAILURE, () -> {
            if (reportType == null) {
                throw new SdkInvalidParamException(INVALID_ACCESSORY_REPORT_TYPE);
            }
            UUID openHidId = getOpenHidId(accessoryContextId, "readReport");
            boolean isOwned = AccessoryCache.getInstance().isOwnedType(accessoryContextId);
            HIDReport report = AccessoryDeviceAdapter.readSyncReport(accessoryDeviceService, pkgName, openHidId,
                    accessoryContextId, reportType, isOwned, DEFAULT_REPORT_ID);
            if (report != null) {
                Result.pack(bundle, Result.RESULT_OK);
                bundle.putParcelable(Result.KEY_RESULT, report);
            } else {
                Log.e(TAG, "readReport: Failed to read report");
                throw new SdkServiceErrorException(ACCESSORY_READ_REPORT_FAILURE);
            }
            return bundle;
        });
        return result;
    }

    protected Bundle writeReport(final Bundle bundle, final String accessoryContextId, final HIDReport requestedReport,
                                 final String pkgName) throws SdkException {
        Bundle result = executeCallMethod(ACCESSORY_WRITE_REPORT_FAILURE, () -> {
            if (requestedReport == null) {
                Log.e(TAG, "writeReport: requestedReport is null");
                throw new SdkInvalidParamException(INVALID_ACCESSORY_REPORT);
            }
            UUID openHidId = getOpenHidId(accessoryContextId, "writeReport");
            boolean isOwned = AccessoryCache.getInstance().isOwnedType(accessoryContextId);
            boolean writeResult = AccessoryDeviceAdapter.writeSyncReport(accessoryDeviceService, pkgName, openHidId,
                    accessoryContextId, requestedReport, isOwned, DEFAULT_REPORT_ID);
            if (writeResult) {
                Result.pack(bundle, Result.RESULT_OK);
            } else {
                Log.e(TAG, "writeReport: Failed to write report");
                throw new SdkServiceErrorException(ACCESSORY_WRITE_REPORT_FAILURE);
            }
            return bundle;
        });
        return result;
    }

    //////////////////////// Private methods ////////////////////////

    private <T> T executeCallMethod(String errorMsgPrefix, CallMethod<T> op) throws SdkException {
        try {
            return op.execute();
        } catch (SdkException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "executeCallMethod: Exception - " + errorMsgPrefix + ": " + e.getMessage(), e);
            throw new SdkServiceErrorException(errorMsgPrefix + e.getMessage());
        }
    }

    private String getAccessoryIdOwnedByApp(Context context, String pkgName, HIDAccessoryInfo hidAccessoryInfo) throws SdkException {
        if (hidAccessoryInfo == null) {
            Log.e(TAG, String.format("getAccessoryIdOwnedByApp: %s [%s]", INVALID_ACCESSORY_INFO, pkgName));
            throw new SdkInvalidParamException(INVALID_ACCESSORY_INFO);
        }
        //find the accessory in the cache first
        String resourceId = AccessoryCache.getInstance().getOwnedAccessoryId(pkgName, hidAccessoryInfo);
        if (StringUtility.isEmpty(resourceId)) {
            //verify that the accessory is registered as owned by the app
            if (!AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(context, pkgName, hidAccessoryInfo)) {
                Log.e(TAG, String.format("getAccessoryIdOwnedByApp: %s [%s, %s]",
                        ACCESSORY_NOT_REGISTERED, pkgName, hidAccessoryInfo));
                throw new SdkInvalidParamException(ACCESSORY_NOT_REGISTERED);
            }
            //find it in the device
            resourceId = AccessoryDeviceAdapter.getOwnedAccessoryId(accessoryDeviceService, pkgName,
                    hidAccessoryInfo);
        } else if (AccessoryCache.getInstance().isInvalidated(resourceId)) {
            // if the accessory is in the cache but invalidated, it means that the accessory is in detach process
            // (detach event is received from device but not yet notified to the app)
            Log.e(TAG, String.format("getAccessoryIdOwnedByApp: [%s] %s ", resourceId, ACCESSORY_NOT_READY));
            throw new SdkServiceErrorException(ACCESSORY_NOT_READY);
        }
        return resourceId;
    }

    private UUID getOpenHidId(String accessoryContextId, String operation) throws SdkException {
        if (StringUtility.isEmpty(accessoryContextId)) {
            throw new SdkInvalidParamException(INVALID_ACCESSORY_INFO);
        }
        UUID openHidId = AccessoryCache.getInstance().getOpenHidId(accessoryContextId);
        if (openHidId == null) {
            Boolean isCached = AccessoryCache.getInstance().isCached(accessoryContextId);
            Log.e(TAG, operation + ": cannot find openHidId in cache, " + isCached);
            throw new SdkServiceErrorException(ACCESSORY_NOT_OPENED);
        }
        return openHidId;
    }

    private boolean isRegisteredAccessory(AccessoryInfo accessory, List<AccessoryRegistrationInfo> registered) {
        if (!(accessory instanceof HIDAccessoryInfo)) {
            return false;
        }
        HIDAccessoryInfo hidAccessory = (HIDAccessoryInfo) accessory;
        return registered.stream().anyMatch(reg -> reg.equals(hidAccessory));
    }

    private boolean isSupported() {
        return AccessoryDeviceAdapter.isSupported(accessoryDeviceService);
    }

    private void validateAccessoryExists(String accessoryId, String pkgName) throws SdkException {
        if (StringUtility.isEmpty(accessoryId)) {
            throw new SdkInvalidParamException(INVALID_ACCESSORY_INFO);
        }
        synchronized (AccessoryCache.getInstance()) {
            if (!AccessoryCache.getInstance().isCached(accessoryId)) {
                Log.d(TAG, String.format("validateAccessoryCache: Accessory not in cache [%s, %s]", pkgName,
                        accessoryId));
                HIDAccessoryInfo info = AccessoryDeviceAdapter.getHidAccessoryInfo(accessoryDeviceService, pkgName,
                        accessoryId);
                if (info == null) {
                    throw new SdkServiceErrorException(ACCESSORY_NOT_FOUND);
                }
                AccessoryCache.getInstance().add(accessoryId, pkgName, info);
                Log.d(TAG, String.format("validateAccessoryCache:added [%s, %s]", pkgName, accessoryId));
            }
        }
    }

    private void validateClientVersion(Bundle extras) throws SdkNotSupportedException {
        // if extras is null - it means it's old API 1
        int clientVersion = extras != null && extras.containsKey(Accessorylet.Keys.KEY_CLIENT_VERSION) ?
                extras.getInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

        if (Sdk.VERSION_LEVEL.EIGHT > clientVersion) {
            throw new SdkNotSupportedException("AccessoryService is not supported in version " + clientVersion);
        }
    }

    private String validatePackageName(Bundle extras, String callingPkg)
            throws SdkInvalidParamException, SdkUnauthorizedException {
        extras.setClassLoader(AccessoryInfo.class.getClassLoader());
        String pkgName = extras.getString(Accessorylet.Keys.PACKAGE_NAME);
        if (StringUtility.isEmpty(pkgName)) {
            throw new SdkInvalidParamException("Package name is empty");
        }
        if (StringUtility.isEmpty(callingPkg)
                || (!pkgName.equalsIgnoreCase(callingPkg) && !Sdk.SERVICES_PACKAGE.equalsIgnoreCase(callingPkg))) {
            Log.e(TAG, "package name does not match : callingPkg=" + callingPkg + ", pkgName=" + pkgName);
            throw new SdkUnauthorizedException("Service is not allowed for " + pkgName);
        }
        return pkgName;
    }

    private PrinterInfo validatePermissionAndGetPrinterInfo() throws SdkInvalidParamException,
            SdkConnectionErrorException {
        Context context = getContext();
        if (context == null) {
            throw new SdkInvalidParamException("Context is null");
        }
        SpsPermissionHelper.ensurePermission(context);

        final PrinterInfo pi = SelectedPrinterHelper.get(context.getContentResolver());
        if (PrinterInfo.isEmpty(pi)) {
            Log.e(TAG, "Device is not connected");
            throw new SdkConnectionErrorException("Device is not connected");
        }
        return pi;
    }

    // Functional interface for filtering accessory lists.
    @FunctionalInterface
    private interface AccessoryProvider {
        ArrayList<AccessoryInfo> getAccessories(IDeviceAccessoryService service, String pkgName);
    }

    @FunctionalInterface
    private interface CallMethod<T> {
        T execute() throws Exception;
    }
}
