// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ssp;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.Result;

/**
 * Wrapper above {@link AbstractBroadcast} and {@link AbstractNotify}
 * to provide sending of both notification and broadcast from same
 * place.
 */
public class AbstractReporter {

    private final AbstractNotify mNotify;
    private final AbstractBroadcast mBroadcast;

    /** @deprecated to be removed once remove content resolver usage */
    public static final String EXTRA_SEGMENT_COPY = "copy";
    /** @deprecated to be removed once remove content resolver usage */
    public static final String EXTRA_SEGMENT_SCAN = "scan";
    /** @deprecated to be removed once remove content resolver usage */
    public static final String EXTRA_SEGMENT_PRINT = "print";
    /** @deprecated to be removed once remove content resolver usage */
    public static final String EXTRA_SEGMENT_FAX = "fax";

    /**
     * Default constructor
     *
     * @param action to be used for broadcasts
     * @param baseUri to be used for ContentObservers notifications
     * @param tag to be used for logging
     */
    protected AbstractReporter(final String action, final Uri baseUri, final String tag) {
        mNotify = new AbstractNotify(tag, baseUri);
        mBroadcast = new AbstractBroadcast(action);
    }

    /**
     * Notifies about cancel
     *
     * @param context {@link Context}
     * @param rid of job
     */
    public void cancel(final Context context, final String rid) {
        mNotify.cancel(context, rid);
        mBroadcast.cancel(context, rid);
    }

    /**
     * Notifies about error with provided data
     *
     * @param context {@link Context}
     * @param rid of job
     * @param code error code from {@link Result}
     */
    public void fail(final Context context, final String rid, final int code) {
        fail(context, rid, code, null, null);
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
        fail(context, rid, code, null, cause);
    }

    /**
     * Notifies about error with provided data
     *
     * @param context {@link Context}
     * @param rid of job
     * @param code error code from {@link Result}
     * @param cause String cause
     */
    public void fail(final Context context, final String rid, final int code, final Result.ErrorCode errorCode, final String cause) {
        mNotify.fail(context, rid, code, cause);
        mBroadcast.fail(context, rid, code, errorCode, cause);
    }

    /**
     * Notifies with provided uri using {@link ContentObserver}
     *
     * @param context {@link Context} to get observer
     * @param bundle with notifications data
     * @param uri {@link Uri} can be null to send only broadcast notification
     */
    protected final void notify(final Context context, final Bundle bundle, final Uri uri) {
        if (uri != null) {
            mNotify.notify(context, uri);
        }
        mBroadcast.notify(context, bundle);
    }

    /**
     * Notifies with provided uri using {@link ContentObserver}
     * and attempts to send ordered broadcast
     *
     * @param context {@link Context} to get observer
     * @param bundle with notifications data
     * @param uri {@link Uri} can be null to send only broadcast notification
     */
    protected final void notifyOrdered(final Context context, final Bundle bundle, final Uri uri) {
        if (uri != null) {
            mNotify.notify(context, uri);
        }
        mBroadcast.notifyOrdered(context, bundle);
    }

    /**
     * Reports complete with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void complete(final Context context, final String rid, final Bundle bundle) {
        mNotify.complete(context, rid, bundle);
        mBroadcast.complete(context, rid, bundle);
    }

    /**
     * Reports progress with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void progress(final Context context, final String rid, final Bundle bundle) {
        mNotify.progress(context, rid, bundle);
        mBroadcast.progress(context, rid, bundle);
    }

    /**
     * Sets extra segment string to be used in notification
     *
     * @deprecated to be removed once removed content resolver usage
     *
     * @param extraSegment {@link String}
     */
    public void setExtraSegment(final String extraSegment) {
        mNotify.setExtraSegment(extraSegment);
    }

    /**
     * Sets extra permissions
     *
     * @param extraPermission {@link String} to be used with.
     */
    public void setExtraPermission(final String extraPermission) {
        mBroadcast.setExtraPermission(extraPermission);
    }

    /**
     * Sets target package for broadcasts (and o, SDK callbacks)
     *
     * @param packageName {@link String} to send broadcasts to
     */
    public void setTargetPackage(final String packageName) {
        mBroadcast.setTargetPackage(packageName);
    }
}
