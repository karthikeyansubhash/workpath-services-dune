package com.hp.workpath.apitest.config;

import android.os.Handler;

import com.hp.workpath.api.config.ConfigService;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class ConfigChangeObserver extends ConfigService.AbstractConfigChangeObserver {
    ConfigServiceTest.ConfigChangeCallback callback;
    public ConfigChangeObserver(Handler handler, ConfigServiceTest.ConfigChangeCallback callback) {
        super(handler);
        this.callback = callback;
    }

    @Override
    public void onChange(JSONObject jsonObject) {
        callback.onConfigChanged(jsonObject);
    }
}
