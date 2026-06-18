// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.annotation.CommonApi;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * <p>Sets of result details of Workpath SDK API on various operations. Workpath SDK strongly recommends to check result after requesting operation.</p>
 * An application can use this class of these cases :
 * <ul>
 *     <li>To know operation is completed successfully or not</li>
 *     <li>To know the reason and error detail for failure</li>
 * </ul>
 *
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess"})
@CommonApi
public class Result {
    /**
     * @hide
     */
    public static final String KEY_RESULT = "result";

    /**
     * @hide
     */
    public static final String KEY_CODE = "resultCode";

    /**
     * @hide
     */
    public static final String KEY_ERROR_CODE = "resultErrorCode";

    /**
     * @hide
     */
    public static final String KEY_CAUSE = "resultCause";

    private int mCode;

    private String mCause;

    private ErrorCode mErrorCode;

    /**
     * @hide
     */
    public Result() {
        mCode = RESULT_FAIL;
        mErrorCode = ErrorCode.UNKNOWN;
        mCause = "";
    }

    /**
     * @hide
     */
    public Result(final Uri uri) {
        final String code = uri.getQueryParameter(Result.KEY_CODE);
        // Code
        if (!TextUtils.isEmpty(code)) {
            mCode = Integer.valueOf(code);
        } else {
            mCode = RESULT_FAIL;
        }

        // errorCode (optional)
        final String errorCode = uri.getQueryParameter(Result.KEY_ERROR_CODE);

        if (!TextUtils.isEmpty(errorCode)) {
            mErrorCode = getErrorCodeFromString(errorCode);
        }

        // Cause (optional)
        final String cause = uri.getQueryParameter(Result.KEY_CAUSE);

        if (!TextUtils.isEmpty(cause)) {
            mCause = cause;
        }
    }

    /**
     * @hide
     */
    @NonNull
    public static Result parse(final Bundle bundle, Result result) {
        if (result != null && bundle != null) {
            try {
                if (bundle.containsKey(KEY_CODE)) {
                    result.mCode = bundle.getInt(KEY_CODE);
                }
                if (bundle.containsKey(KEY_ERROR_CODE)) {
                    result.mErrorCode = ErrorCode.valueOf(bundle.get(KEY_ERROR_CODE).toString());
                }
                if (bundle.containsKey(KEY_CAUSE)) {
                    result.mCause = bundle.getString(KEY_CAUSE);
                }
                return result;
            } catch (Exception e) {
                SLog.e(Result.class.getSimpleName(), "Failed to parse bundle", e);
            }
        }
        return new Result(Result.RESULT_FAIL, ErrorCode.INVALID_PARAM, "Result value is not correct in bundle.");
    }

    /**
     * @hide
     */
    @Nullable
    public static Result parse(final Uri uri) {
        if (uri != null) {
            final String cause = uri.getQueryParameter(KEY_CAUSE);
            final String errorCodeStr = uri.getQueryParameter(KEY_ERROR_CODE);
            final String codeStr = uri.getQueryParameter(KEY_CODE);

            int code = RESULT_FAIL;
            ErrorCode errorCode = null;

            if (!TextUtils.isEmpty(codeStr)) {
                try {
                    code = Integer.valueOf(codeStr);
                } catch (NumberFormatException e) {
                    code = Result.RESULT_FAIL;
                }
            }

            if (!TextUtils.isEmpty(errorCodeStr)) {
                errorCode = getErrorCodeFromString(errorCodeStr);
            }

            return new Result(code, errorCode, cause);
        }

        return null;
    }

    /**
     * @hide
     */
    @NonNull
    public static Bundle pack(@Nullable final Bundle bundle, @NonNull final Result result) {
        Bundle out = bundle;

        if (out == null) {
            out = new Bundle();
        }

        out.putInt(KEY_CODE, result.getCode());

        if (result.getCause() != null) {
            out.putString(KEY_CAUSE, result.getCause());
        }

        if (result.getErrorCode() != null) {
            out.putSerializable(KEY_ERROR_CODE, result.getErrorCode());
        }

        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));

        return out;
    }

    /**
     * Packs Result into the Result
     *
     * @param result to pack result into
     * @param code   result status (RESULT_OK or RESULT_FAIL)
     * @hide
     * @return
     */
    public static Result pack(@NonNull final Result result, int code) {

        result.mCode = code;
        result.mErrorCode = null;
        result.mCause = null;

        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return result;
    }

    /**
     * Packs Result into the Result
     *
     * @param result    to pack result into
     * @param code      result status (RESULT_OK or RESULT_FAIL)
     * @param errorCode error code.
     * @param cause     specific error cause.
     * @hide
     * @return
     */
    public static Result pack(@NonNull final Result result, int code, ErrorCode errorCode, String cause) {

        result.mCode = code;
        result.mErrorCode = errorCode;
        result.mCause = cause;

        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return result;
    }

    /**
     * Packs Result into the Result
     *
     * @param result       to pack result into
     * @param code         result status (RESULT_OK or RESULT_FAIL)
     * @param errorCodeStr error code.
     * @param cause        specific error cause.
     * @hide
     * @return
     */
    public static Result pack(@NonNull final Result result, int code, String errorCodeStr, String cause) {

        result.mCode = code;
        result.mErrorCode = getErrorCodeFromString(errorCodeStr);
        result.mCause = cause;

        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return result;
    }

    /**
     * @hide
     */
    @NonNull
    public static Bundle pack(@Nullable final Bundle bundle, final int resultCode) {
        Result result = new Result(resultCode, null, null);
        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return pack(bundle, result);
    }

    /**
     * @hide
     */
    @NonNull
    public static Bundle pack(@Nullable final Bundle bundle, final int resultCode, final ErrorCode errorCode) {
        Result result = new Result(resultCode, errorCode, null);
        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return pack(bundle, result);
    }

    /**
     * @hide
     */
    @NonNull
    public static Bundle pack(@Nullable final Bundle bundle, final int resultCode, ErrorCode errorCode, final String cause) {
        Result result = new Result(resultCode, errorCode, cause);
        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return pack(bundle, result);
    }

    /**
     * @hide
     */
    @NonNull
    public static Uri packUri(@NonNull final Uri uri, @NonNull final Result result) {
        Uri.Builder builder = uri.buildUpon();

        builder.appendQueryParameter(KEY_CODE, String.valueOf(result.getCode()));

        if (result.getCause() != null) {
            builder.appendQueryParameter(KEY_CAUSE, result.getCause());
        }

        if (result.getErrorCode() != null) {
            builder.appendQueryParameter(KEY_ERROR_CODE, result.getErrorCode().name());
        }
        SLog.w(Result.class.getSimpleName(), JsonParser.getInstance().toJson(result));
        return builder.build();
    }

    /**
     * @hide
     */
    @NonNull
    public static Uri packUri(@NonNull final Uri uri, final int resultCode, final ErrorCode errorCode) {
        return packUri(uri, new Result(resultCode, errorCode, null));
    }

    /**
     * @hide
     */
    @NonNull
    public static Uri packUri(@NonNull final Uri uri, final int resultCode, final ErrorCode errorCode, final String cause) {
        return packUri(uri, new Result(resultCode, errorCode, cause));
    }

    /**
     * <p>Provides error code when operation is failed.</p>
     *
     * @return ErrorCode code of error
     * @since API 1
     */
    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

    /**
     * @hide
     */
    public Result(final int code, final ErrorCode errorCode, final String cause) {
        mCode = code;
        mErrorCode = errorCode;
        mCause = cause;
    }

    /**
     * @hide
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Result: ");
        switch (getCode()) {
            case RESULT_OK:
                sb.append("Success");
                break;
            case RESULT_FAIL:
                sb.append("Fail");
                break;
        }

        if (getErrorCode() != null) {
            sb.append(" ErrorCode: ");
            switch (getErrorCode()) {
                case CONNECTION_ERROR:
                    sb.append("Connection error");
                    break;
                case INVALID_PARAM:
                    sb.append("Invalid params");
                    break;
                case SERVICE_ERROR:
                    sb.append("Service error");
                    break;
                case AUTHENTICATION_ERROR:
                    sb.append("Authentication error");
                    break;
                case UNAUTHORIZED:
                    sb.append("Unauthorized");
                    break;
                case UNKNOWN:
                    sb.append("Unknown");
                    break;
                case NOT_SUPPORTED:
                    sb.append("Not supported");
                    break;
                case SYSTEM_ERROR:
                    sb.append("System error");
                    break;
                case JOB_FAILURE:
                    sb.append("Job Failure");
                    break;
            }
        }

        if (!TextUtils.isEmpty(getCause())) {
            sb.append(" Cause: ");
            sb.append(getCause());
        }

        return sb.toString();
    }

    private static ErrorCode getErrorCodeFromString(String errorCode) {
        if (!TextUtils.isEmpty(errorCode)) {
            for (ErrorCode error : ErrorCode.values()) {
                if (errorCode.toLowerCase().equals(error.name().toLowerCase())) {
                    return error;
                }
            }
        }
        return null;
    }

    /**
     * <p>Provides the operation result when it's failed or succeeded.</p>
     *
     * @return code If it's succeeded, returns RESULT_OK. Otherwise, returns RESULT_FAIL.
     * @since API 1
     */
    public int getCode() {
        return mCode;
    }

    /**
     * <p>Provides reasons when operation is failed.</p>
     *
     * @return cause Returns the cause of this throwable or null if the cause is nonexistent or unknown.
     * @since API 1
     */
    public String getCause() {
        return mCause;
    }

    /**
     * @hide
     */
    public enum SpsCauseKind {
        /**
         * Job creation kind of cause
         *
         * @since API 1
         */
        CREATION,
        /**
         * Job processing kind of cause
         *
         * @since API 1
         */
        PROCESSING,
        /**
         * Job operation (like cancel etc.) kind of cause
         *
         * @since API 1
         */
        OPERATION
    }

    /**
     * <p>The result code of success</p>
     *
     * @since API 1
     */
    public static final int RESULT_OK = -1;

    /**
     * <p>The result code of fail</p>
     *
     * @since API 1
     */
    public static final int RESULT_FAIL = 0;

    /**
     * <p>Enumeration of errors when operation is failed.</p>
     *
     * @since API 1
     */
    @CommonApi
    public enum ErrorCode {
        /**
         * <p>Occurs error when parameters in request include data of invalid format or unsupported attribute.</p>
         *
         * @since API 1
         */
        INVALID_PARAM,

        /**
         * <p>Occurs error when SDK can not query because of connection error.</p>
         *
         * @since API 1
         */
        CONNECTION_ERROR,

        /**
         * <p>Occurs error when SDK can not complete the operation because of internal error.</p>
         *
         * @since API 1
         */
        SERVICE_ERROR,

        /**
         * <p>Occurs error when job is reported as fail by a printer.</p>
         *
         * @since API 1
         */
        JOB_FAILURE,

        /**
         * <p>Occurs error when operation is not permitted because of insufficient authentication.</p>
         *
         * @since API 1
         */
        AUTHENTICATION_ERROR,

        /**
         * <p>Occurs error because an application doesn't have the right permission to call SDK.</p>
         *
         * @since API 1
         */
        UNAUTHORIZED,

        /**
         * <p>Occurs error with unexpected reason.</p>
         *
         * @since API 1
         */
        UNKNOWN,

        /**
         * <p>Occurs error when operation is not supported by SDK.</p>
         *
         * @since API 1
         */
        NOT_SUPPORTED,

        /**
         * <p>Occurs error when device is in system issue while operating.</p>
         *
         * @since API 1
         */
        SYSTEM_ERROR,

        /**
         * <p>Occurs error when operation is not available by SDK.</p>
         *
         * @since API 9
         */
        UNAVAILABLE
    }
}
