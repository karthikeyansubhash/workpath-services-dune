// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ssp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.Joblet;

/**
 * Basic class for sending notifications of job states etc. via Broadcasts.
 */
final class AbstractBroadcast {
    private final String mAction;
    private String mExtraPermission = null;
    private String mTargetPackage = null;

    /**
     * Default constructor
     *
     * @param action to be send via Broadcasts
     */
    protected AbstractBroadcast(final String action) {
        mAction = action;
    }

    /**
     * Notifies about cancel
     *
     * @param context {@link Context}
     * @param rid of job
     */
    public void cancel(final Context context, final String rid) {
        final Bundle bundle = new Bundle();
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.CANCEL);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        notify(context, bundle);
    }

    /**
     * Notifies about error with provided data
     *
     * @param context {@link Context}
     * @param rid of job
     * @param code error code from {@link Result}
     */
    public void fail(final Context context, final String rid, final int code) {
        final Bundle bundle = new Bundle();
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.FAIL);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        bundle.putInt(Result.KEY_CODE, code);
        notify(context, bundle);
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
        final Bundle bundle = new Bundle();
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.FAIL);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        bundle.putInt(Result.KEY_CODE, code);
        bundle.putString(Result.KEY_CAUSE, cause);

        if (cause != null) {
            bundle.putString(Result.KEY_CAUSE, cause);
        }

        notify(context, bundle);
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
        final Bundle bundle = new Bundle();
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.FAIL);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        bundle.putInt(Result.KEY_CODE, code);

        if (errorCode != null) {
            bundle.putSerializable(Result.KEY_ERROR_CODE, errorCode);
        }

        if (cause != null) {
            bundle.putString(Result.KEY_CAUSE, cause);
        }

        notify(context, bundle);
    }

    /**
     * Notifies with provided bundle via {@link Context#sendBroadcast(Intent)}
     *
     * @param context {@link Context} to get observer
     * @param bundle {@link Bundle}
     */
    protected final void notify(final Context context, final Bundle bundle) {
        final Intent intent = new Intent(mAction);

        // Receiver shouldn't be interested in clients package name
        bundle.remove(Joblet.Keys.KEY_CLIENT_PACKAGE);
        intent.putExtras(bundle);

        if (!TextUtils.isEmpty(mTargetPackage)) {
            intent.setPackage(mTargetPackage);
        }

        //Log.d("AbstractBroadcast", "Sending broadcast: mTargetPackage" + mTargetPackage + " mAction: " + mAction + ", mExtraPermission:" + mExtraPermission);
        context.sendBroadcast(intent, mExtraPermission);
    }

    /**
     * Notifies with provided bundle via {@link Context#sendOrderedBroadcast(Intent, String)}}
     *
     * @param context {@link Context} to get observer
     * @param bundle {@link Bundle}
     */
    protected final void notifyOrdered(final Context context, final Bundle bundle) {
        final Intent intent = new Intent(mAction);
        intent.putExtras(bundle);

        if (!TextUtils.isEmpty(mTargetPackage)) {
            intent.setPackage(mTargetPackage);
        }
        context.sendOrderedBroadcast(intent, mExtraPermission);
    }

    /**
     * Reports complete with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void complete(final Context context, final String rid, final Bundle bundle) {
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.COMPLETE);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        notify(context, bundle);
    }

    /**
     * Reports progress with data from bundle with primitive data values
     *
     * @param context {@link Context}
     * @param rid {@link String}
     * @param bundle {@link Bundle} to get data from
     */
    public void progress(final Context context, final String rid, final Bundle bundle) {
        bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.PROGRESS);
        bundle.putString(ILetObserver.Keys.KEY_RID, rid);
        notify(context, bundle);
    }

    /**
     * Sets extra permission to be used instead
     *
     * @param extraPermission {@link String}
     */
    public void setExtraPermission(final String extraPermission) {
        mExtraPermission = extraPermission;
    }

    /**
     * Sets target package for broadcasts (and o, SDK API callbacks)
     *
     * @param packageName {@link String} to send broadcasts to
     */
    public void setTargetPackage(final String packageName) {
        mTargetPackage = packageName;
    }
}
