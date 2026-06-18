package com.hp.workpath.apitest.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.config.ConfigService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class ConfigServiceTest {
    private static Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            fail("Workpath.getInstance().initialize : SsdkUnsupportedException");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ConfigService_isSupported_$ReturnsTrue() {
        boolean result = ConfigService.isSupported(mContext);
        assertTrue("isSupported : " + result, result);
    }

    @Test
    public void ConfigService_getDefaultConfig_$ReturnsDefaultConfiguration() throws JSONException {
        Result result = new Result();
        JSONObject configJson = ConfigService.getDefaultConfig(mContext, result);

        assertEquals("getCode", Result.RESULT_OK, result.getCode());
        verifyJsonObject("default", getDefaultConfigJson(), configJson);
    }

    @Test
    public void ConfigService_setDefaultConfig_getDefaultConfig_$GivenNewConfig_WhenSetAndGet_ThenReturnsUpdatedConfig() throws JSONException {
        JSONObject newConfigJson = getDefaultConfigJson();
        newConfigJson.put("copies", 5);

        // Set new config
        Result result = ConfigService.setDefaultConfig(mContext, newConfigJson);
        assertEquals("getCode", Result.RESULT_OK, result.getCode());

        // Verify new config
        JSONObject configJson = ConfigService.getDefaultConfig(mContext, result);
        assertEquals("getCode", Result.RESULT_OK, result.getCode());
        verifyJsonObject("new", newConfigJson, configJson);

        revertToOriginalDefaultConfig();
    }

    //@Test
    public void AbstractConfigChangeObserver_onConfigChanged_$GivenConfigChanged_WhenObserverRegistered_ThenCallbackCalled() throws InterruptedException, JSONException {
        CountDownLatch latch = new CountDownLatch(1);
        ConfigChangeCallback testCallback = new ConfigChangeCallback() {
            @Override
            public void onConfigChanged(JSONObject configJson) {
                try {
                    verifyJsonObject("callback", getDefaultConfigJson(), configJson);
                    latch.countDown();
                } catch (JSONException e) {
                    fail("onConfigChanged : JSONException");
                }
            }
        };

        // initialize observer
        Handler mainHandler = new Handler(Looper.getMainLooper());
        ConfigChangeObserver observer = new ConfigChangeObserver(mainHandler, testCallback);
        observer.register(mContext);

        // change default config
        JSONObject newConfigJson = getDefaultConfigJson();
        newConfigJson.put("copies", 5);
        Result result = ConfigService.setDefaultConfig(mContext, newConfigJson);
        assertEquals("getCode", Result.RESULT_OK, result.getCode());

        boolean isCallbackCalled = latch.await(5, TimeUnit.SECONDS);
        assertTrue("change notified",isCallbackCalled);

        revertToOriginalDefaultConfig();
    }

    private JSONObject getDefaultConfigJson() throws JSONException {
        JSONObject configJson = new JSONObject();
        configJson.put("url", "https://developer.hp.com");
        configJson.put("colorMode", "MONO");
        configJson.put("paperSize", "LETTER");
        configJson.put("copies", 3);
        configJson.put("desc", "option=empty");
        return configJson;
    }

    private void verifyJsonObject(String prefix, JSONObject expected, JSONObject actual) throws JSONException {
        assertEquals(prefix + " url", expected.getString("url"), actual.getString("url"));
        assertEquals(prefix + " colorMode", expected.getString("colorMode"), actual.getString("colorMode"));
        assertEquals(prefix + " paperSize", expected.getString("paperSize"), actual.getString("paperSize"));
        assertEquals(prefix + " copies", expected.getInt("copies"), actual.getInt("copies"));
        assertEquals(prefix + " desc", expected.getString("desc"), actual.getString("desc"));
    }

    private void revertToOriginalDefaultConfig() throws JSONException {
        Result result = new Result();
        JSONObject orgConfigJson = getDefaultConfigJson();
        result = ConfigService.setDefaultConfig(mContext, orgConfigJson);
        assertEquals("getCode", Result.RESULT_OK, result.getCode());
    }

    public interface ConfigChangeCallback {
        void onConfigChanged(JSONObject configJson);
    }
}
