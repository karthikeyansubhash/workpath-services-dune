package com.hp.workpath.apitest.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.device.DeviceAttribute;
import com.hp.workpath.api.device.DeviceService;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeviceServiceTest {

    private static Context mContext;
    private final DutController mDutController;

    public DeviceServiceTest() {
        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            fail("Workpath.getInstance().initialize failed");
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        // only call stopApp once
        mDutController.stopApp(mContext);
    }

    @Test
    public void DeviceService_isSupported_ReturnsTrue() {
        boolean supported = DeviceService.isSupported(mContext);
        assertTrue("isSupported", supported);
    }

    @Test
    public void DeviceService_getString_ReturnsTrue() {
        Result result = new Result();
        String value = DeviceService.getString(mContext, DeviceAttribute.DA_DEVICE_VENDOR, result);
        assertTrue("Result not OK: " + result, result.getCode() == Result.RESULT_OK);
        assertEquals("DA_DEVICE_VENDOR", Expected.DEVICE_VENDOR, value);
    }

    public static class Expected {
        public static final String DEVICE_VENDOR = "HP";
    }
}
