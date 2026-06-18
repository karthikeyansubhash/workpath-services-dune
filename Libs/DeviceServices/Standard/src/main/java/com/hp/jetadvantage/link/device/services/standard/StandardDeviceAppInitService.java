package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import androidx.lifecycle.Observer;

import com.hp.jetadvantage.link.device.services.interfaces.IAppInitService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;

public class StandardDeviceAppInitService extends StandardDeviceService implements IAppInitService {
    protected String TAG = Constants.TAG + "/AppInit";
    @Override
    public void emitAppInitEvent(String packageName) {
        //TODO
        Log.i(TAG, "init :" + packageName);
    }

    @Override
    public void registerObserver(Observer<String> observer) {
        //TODO
    }
}
