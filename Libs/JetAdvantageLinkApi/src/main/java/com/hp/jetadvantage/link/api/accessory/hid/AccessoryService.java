// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.ReportEventInfo;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.List;

/**
 * <p>AccessoryService provides interfaces for handling accessories on a device.</p>
 *
 * @since API 3
 */
@DeviceApi
public class AccessoryService {
    private static final String TAG = Accessorylet.TAG;

    private AccessoryService() {}

    /**
     * <p>Retrieves the list of accessories registered as owned by the application</p>
     *
     * @param context The Context object for your activity or application
     * @param result (optional) Indicates any errors occurred while getting owned accessories.
     * @return list of {@link AccessoryInfo}
     * @throws NullPointerException If context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static List<AccessoryInfo> getOwnedAccessories(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.GET_OWNED, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Retrieves list of accessories registered as shared</p>
     *
     * @param context The Context object for your activity or application
     * @param result (optional) Indicates any errors occurred while getting shared accessories.
     * @return list of {@link AccessoryInfo}
     * @throws NullPointerException If context is null.
     *
     * @since API 3
     * @hide
     */
    @SuppressWarnings({"unused"})
    public static List<AccessoryInfo> getSharedAccessories(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.GET_SHARED, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Retrieves the list of accessories registered as owned or shared and attached to the device</p>
     *
     * @param context The Context object for your activity or application
     * @param result (optional) Indicates any errors occurred while registering.
     * @return list of {@link AccessoryInfo}
     * @throws NullPointerException If context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static List<AccessoryInfo> enumerateAccessories(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.ENUMERATE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Forces resending of accessory context {@link AccessoryService.AbstractAccessoryObserver#onContextChange(AccessoryInfo, EventCode, String, String)}.</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryInfo AccessoryInfo to request resending context
     * @param result (optional) Indicates any errors occurred while getting owned accessory context.
     * @throws NullPointerException If context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void resendOwnedAccessoryContext(@NonNull final Context context, @NonNull AccessoryInfo accessoryInfo,  @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryInfo, "Accessory information must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO, accessoryInfo);

        if (result == null) {
            result = new Result();
        }

        try {
            HIDAccessoryInfo hidInfo = (HIDAccessoryInfo) accessoryInfo;

            Preconditions.checkNotNull(hidInfo.getProductId(), "Accessory product id must be provided");
            Preconditions.checkNotNull(hidInfo.getVendorId(), "Accessory vendor id must be provided");

            AccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(hidInfo.getVendorId(),
                    hidInfo.getProductId(),
                    hidInfo.getSerialNumber(),
                    null,
                    null,
                    RegistrationType.OWNED);
            extras.putParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION, hidAccessoryInfo);

            final Bundle bundleTmp =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.REGISTER, null, extras);
            if (null == bundleTmp) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result resultTmp = new Result();
                bundleTmp.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundleTmp, resultTmp);

                if (resultTmp.getCode() == Result.RESULT_OK) {
                    try {
                        final Bundle bundle =
                                context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.RESEND_OWNED, null, extras);

                        if (null == bundle) {
                            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
                        } else {
                            bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                            Result.parse(bundle, result);
                        }
                    } catch (Exception e) {
                        Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
                    }
                } else {
                    Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "No matching owned accessory record found in ResendOwnedAccessoryEvent");
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Requests to reserve shared accessory and create new context ID of the accessory.</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryInfo AccessoryInfo to register
     * @param result (optional) Indicates any errors occurred while reserving.
     * @throws NullPointerException If context or accessoryId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String reserveSharedAccessory(@NonNull final Context context,
                                @NonNull final AccessoryInfo accessoryInfo,
                                @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryInfo, "Accessory info must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO, accessoryInfo);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.RESERVE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getString(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Requests to release shared accessory with accessory context id</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to release
     * @throws NullPointerException If context or accessoryId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void releaseSharedAccessory(@NonNull final Context context,
            @NonNull final String accessoryContextId,
            @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory Context ID must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.RELEASE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Requests to open accessory with accessory context id</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to open
     * @throws NullPointerException If context or accessoryContextId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void open(@NonNull final Context context, @NonNull final String accessoryContextId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory Context ID must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.OPEN, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Returns detailed, HID-specific information about an individual HID accessory.</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to get detail
     * @param result (optional) Indicates any errors occurred while retrieving accessory.
     * @return Accessory {@link HIDInfo}
     * @throws NullPointerException If context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static HIDInfo getInfo(@NonNull final Context context, @NonNull final String accessoryContextId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory context id must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.GET_HID_INFO, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelable(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Requests to close accessory with accessory context id</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to close
     * @throws NullPointerException If context or accessoryContextId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void close(@NonNull final Context context, @NonNull final String accessoryContextId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory Context ID must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.CLOSE, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Signals the device to start processing reads from the accessory</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to start reading
     * @throws NullPointerException If context or accessoryContextId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void startReading(@NonNull final Context context, @NonNull final String accessoryContextId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory Context ID must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.START_READING, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Signals the device to stop processing reads from the accessory</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to stop reading
     * @throws NullPointerException If context or accessoryContextId is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void stopReading(@NonNull final Context context, @NonNull final String accessoryContextId, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory Context ID must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.STOP_READING, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Synchronously reads an input or feature report from the accessory</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to start reading
     * @param hidReportType report type to read {@link HIDReportType}
     * @param result (optional) Indicates any errors occurred while registering.
     * @return HID report {@link HIDReport}
     * @throws NullPointerException If context or accessoryContextId or hidReportType is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static HIDReport readReport(@NonNull final Context context, @NonNull final String accessoryContextId,
            @NonNull HIDReportType hidReportType, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory context id must be provided");
        Preconditions.checkNotNull(hidReportType, "Accessory report type must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);
        extras.putSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE, hidReportType);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.READ_REPORT, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelable(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>Synchronously writes a report to the accessory</p>
     *
     * @param context The Context object for your activity or application
     * @param accessoryContextId accessory context id to write
     * @param hidReport report to write {@link HIDReport }
     * @param result (optional) Indicates any errors occurred while registering.
     * @throws NullPointerException If context or accessoryContextId or hidReport data is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void writeReport(@NonNull final Context context, @NonNull final String accessoryContextId,
            @NonNull HIDReport hidReport, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(accessoryContextId, "Accessory context id must be provided");
        Preconditions.checkNotNull(hidReport, "Accessory report must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        extras.putString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID, accessoryContextId);
        extras.putParcelable(Accessorylet.Keys.KEY_HID_REPORT, hidReport);

        if (result == null) {
            result = new Result();
        }
        try {
            final Bundle bundle =
                    context.getContentResolver().call(Accessorylet.CONTENT_URI, Accessorylet.Method.WRITE_REPORT, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(AccessoryInfo.class.getClassLoader());
                Result.parse(bundle, result);
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, HID AccessoryService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Accessorylet.getContentUri(resolver),
                            Accessorylet.Method.IS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Accessorylet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Accessorylet.IS_SUPPORTED_EXTRA);
        } catch (Exception e) {
            SLog.e(TAG, "Failed to get supported status " + e.getMessage());
        }

        return false;
    }

    /**
     * <p>This method is needed to determine if accessories service is ready or not.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean If service is ready, method returns true. Otherwise method returns false.
     * @exception NullPointerException Returns error if calling a method on a null reference(context) or
     * trying to access a field of a null reference will trigger a NullPointerException.
     *
     * @since API 5
     */
    @SuppressWarnings({"unused"})
    public static boolean isReady(final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Accessorylet.Keys.PACKAGE_NAME, packageName);
        extras.putInt(Accessorylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Accessorylet.getContentUri(resolver),
                            Accessorylet.Method.IS_READY,
                            null,
                            extras);

            return returnBundle != null
                    && com.hp.workpath.api.Result.parse(returnBundle, new com.hp.workpath.api.Result()).getCode() == com.hp.workpath.api.Result.RESULT_OK
                    && returnBundle.containsKey(Accessorylet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Accessorylet.IS_SUPPORTED_EXTRA);
        } catch (Exception e) {
            SLog.e(TAG, "Failed to get READY status " + e.getMessage());
        }

        return false;
    }

    /**
     * <p>
     * AbstractAccessoryObserver provides a mechanism to monitor the state of the
     * {@link AccessoryService} operations. The AbstractAccessoryObserver receives
     * callbacks when there have been changes to the state of the registered accessory.
     * </p>
     *
     * @since API 3
     */
    public static abstract class AbstractAccessoryObserver extends BroadcastReceiver {
        private final Handler mHandler;

        /**
         * @param handler The handler to run callbacks on, or null if none.
         * @since API 3
         */
        public AbstractAccessoryObserver(final Handler handler) {
            super();
            mHandler = handler;
        }

        /**
         * <p>
         * Register the observer to monitor accessory state change events.
         * </p>
         *
         * @param context The Context in which the application is running. If it's null, event will not be triggered.
         * @throws NullPointerException if context is null.
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void register(final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");

            final IntentFilter filter = new IntentFilter();
            filter.addAction(Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION);
            filter.addAction(Accessorylet.ACCESSORY_CHANGE_ACTION);

            SLog.d(TAG, "Registering for accessory events");
            context.registerReceiver(this, filter);
        }

        /**
         * <p>
         * Unregister the observer and stop monitoring accessory state change events.
         * </p>
         *
         * @param context The Context in which the application is running.
         * @throws NullPointerException if context is null.
         * @since API 3
         */
        @SuppressWarnings({"unused"})
        @SuppressLint("RestrictedApi")
        public void unregister(final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");

            SLog.d(TAG, "Un-registering for accessory events");
            context.unregisterReceiver(this);
        }

        /**
         * @hide final
         */
        @Override
        public final void onReceive(final Context context, final Intent intent) {
            if (isOrderedBroadcast()) {
                abortBroadcast();
            }

            if (mHandler == null) {
                onRecv(context, intent);
            } else {
                mHandler.post(new AccessoryStateRunnable(context, intent));
            }
        }

        private void onRecv(final Context context, final Intent intent) {
            final String action = intent.getAction();
            final AccessoryInfo accessoryId = intent.getParcelableExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO);
            String timestamp = intent.getStringExtra(Accessorylet.Keys.KEY_TIMESTAMP);

            if (action != null && accessoryId != null) {
                SLog.d(TAG, "Received intent for " + action +", accessoryId is " + accessoryId);
                switch (action) {
                    case Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION:
                        EventCode eventCode = (EventCode) intent.getSerializableExtra(
                                Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_EVENT_CODE);

                        String accessoryContextId = intent.getStringExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID);
                        onContextChange(accessoryId, eventCode, timestamp, accessoryContextId);
                        break;
                    case Accessorylet.ACCESSORY_CHANGE_ACTION:
                        HIDReportEventInfo eventInfo = intent.getParcelableExtra(Accessorylet.Keys.KEY_HID_REPORT_EVENT_INFO);
                        onReceive(accessoryId, eventInfo);
                        break;
                }
            }
        }

        private final class AccessoryStateRunnable implements Runnable {
            private final Intent mIntent;
            private final Context mContext;

            AccessoryStateRunnable(final Context context, final Intent intent) {
                mIntent = intent;
                mContext = context;
            }

            @Override
            public void run() {
                onRecv(mContext, mIntent);
            }
        }

        /**
         * Called when context for registered accessory is changed.
         *
         * @param accessoryInfo accessory information when the event is invoked
         * @param eventCode The code of event for indicating the event of "owned" accessory
         * @param timestamp The value of the timestamp
         * @param accessoryContextId accessory context id

         * @since API 3
         */
        public abstract void onContextChange(final AccessoryInfo accessoryInfo, final EventCode eventCode, final String timestamp,
                final String accessoryContextId);

        /**
         * Called when the data for registered accessory is updated.
         *
         * @param accessoryInfo accessory information when the event is invoked
         * @param reportEventInfo reported data of accessory

         * @since API 3
         */
        public abstract void onReceive(final AccessoryInfo accessoryInfo, final ReportEventInfo reportEventInfo);
    }
}
