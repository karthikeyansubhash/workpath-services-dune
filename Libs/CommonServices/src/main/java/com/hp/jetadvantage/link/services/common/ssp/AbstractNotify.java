// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ssp;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;

import java.util.Set;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * Basic class for sending notifications of job states etc. via ContentObserver.
 */
final class AbstractNotify {

    final String mTag;
    final Uri mBaseUri;
    /** Extra segment for Uri. It's needed to keep joblet compatibility, because Joblet API 1 used uris different from others services */
    private String mExtraSegment;

    public static final class Segments {
        public static final String CANCEL = "cancel";
        public static final String COMPLETE = "complete";
        public static final String PROGRESS = "progress";
        public static final String FAIL = "fail";
    }

    /**
     * Default constructor
     *
     * @param tag to be used for logging
     * @param baseUri to create notification from
     */
    protected AbstractNotify(final String tag, final Uri baseUri) {
        mTag = tag;
        mBaseUri = baseUri;
    }

    /**
     * Notifies about cancel
     *
     * @param context {@link Context}
     * @param rid of job
     */
    public void cancel(final Context context, final String rid) {
        if (mBaseUri == null) {
            return;
        }

        final Uri.Builder builder = mBaseUri.buildUpon();
        builder.appendPath(rid);
        builder.appendPath(Segments.CANCEL);

        notify(context, builder.build());
    }

    /**
     * Notifies about error with provided data
     *
     * @param context {@link Context}
     * @param rid of job
     * @param code error code from {@link Result}
     */
    public void fail(final Context context, final String rid, final int code) {
        if (mBaseUri == null) {
            return;
        }

        final Uri.Builder builder = mBaseUri.buildUpon();
        builder.appendPath(rid);
        builder.appendPath(Segments.FAIL);
        builder.appendQueryParameter(Result.KEY_CODE, String.valueOf(code));

        notify(context, builder.build());
    }

    /**
     * Notifies about error with provided data
     *
     * @param context {@link Context}
     * @param rid of job
     * @param code error code from {@link Result}
     * @param cause String cause
     */
    public void fail(final Context context, final String rid, final int code, final String cause) {
        if (mBaseUri == null) {
            return;
        }

        final Uri.Builder builder = mBaseUri.buildUpon();
        builder.appendPath(rid);
        builder.appendPath(Segments.FAIL);
        builder.appendQueryParameter(Result.KEY_CODE, String.valueOf(code));

        if (cause != null) {
            builder.appendQueryParameter(Result.KEY_CAUSE, cause);
        }

        notify(context, builder.build());
    }

    /**
     * Notifies with provided uri using {@link ContentObserver}
     *
     * @param context {@link Context} to get observer
     * @param uri {@link Uri}
     */
    protected final void notify(final Context context, final Uri uri) {
        if (mBaseUri == null) {
            return;
        }

        SLog.d(mTag, uri.toString());
        context.getContentResolver().notifyChange(uri, null);
    }

    /**
     * Composes base complete uri for rid
     *
     * @param rid {@link String}
     */
    private Uri.Builder getCompleteBase(final String rid) {
        final Uri.Builder builder =  mBaseUri.buildUpon();

        builder.appendPath(rid);
        builder.appendPath(Segments.COMPLETE);

        if (mExtraSegment != null) {
            builder.appendEncodedPath(mExtraSegment);
        }

        return builder;
    }

    /**
     * Composes base progress uri for rid
     *
     * @param rid {@link String}
     */
    private Uri.Builder getProgressBase(final String rid) {
        final Uri.Builder builder =  mBaseUri.buildUpon();

        builder.appendPath(rid);
        builder.appendPath(Segments.PROGRESS);

        if (mExtraSegment != null) {
            builder.appendEncodedPath(mExtraSegment);
        }

        return builder;
    }

    /**
     * Reports complete with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void complete(final Context context, final String rid, final Bundle bundle) {
        if (mBaseUri == null) {
            return;
        }

        notifyFromBundle(context, getCompleteBase(rid), bundle);
    }

    /**
     * Reports progress with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void progress(final Context context, final String rid, final Bundle bundle) {
        if (mBaseUri == null) {
            return;
        }

        notifyFromBundle(context, getProgressBase(rid), bundle);
    }

    /**
     * Helper API to parse bundle and provide it's primitive data types into {@link Uri} and notify
     * with it
     *
     * @param context {@link Context}
     * @param builder {@link android.net.Uri.Builder} to write data to
     * @param bundle {@link Bundle} to get data from
     */
    private void notifyFromBundle(final Context context, final Uri.Builder builder, final Bundle bundle) {
        // Get all fields from bundle and put into uri if possible
        final Set<String> keySet = bundle.keySet();

        // we will include only simple types in uri from bundle
        for (String key : keySet) {
            final Object val = bundle.get(key);

            if ((val instanceof String) || (val instanceof Integer) || (val instanceof Long) ||
                    (val instanceof Boolean) || (val instanceof Float)) {
                builder.appendQueryParameter(key, String.valueOf(val));
            }
        }

        notify(context, builder.build());
    }

    /**
     * Sets extra segment string to be used in notification
     *
     * @param extraSegment {@link String}
     */
    public void setExtraSegment(final String extraSegment) {
        mExtraSegment = extraSegment;
    }
}
