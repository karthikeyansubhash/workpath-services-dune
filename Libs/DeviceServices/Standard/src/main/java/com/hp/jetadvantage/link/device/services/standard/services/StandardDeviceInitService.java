package com.hp.jetadvantage.link.device.services.standard.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;

/**
 * This service class is only for test/diagnostic purpose
 */
public class StandardDeviceInitService extends Service {
    private static final String TAG = Constants.TAG + "/S/InitService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind ENTER / EXIT");
        return null;
    }

    @Override
    public void onCreate() {
        SLog.i(TAG, "onCreate ENTER");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ServiceNotification.showNotification(this);
        }

        SLog.i(TAG, "onCreate EXIT");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand (startId=" + startId + ") ENTER / EXIT");
        return START_STICKY;
    }
}
