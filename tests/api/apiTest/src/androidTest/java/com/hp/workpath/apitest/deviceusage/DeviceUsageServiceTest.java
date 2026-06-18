package com.hp.workpath.apitest.deviceusage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.deviceusage.DeviceUsageInfo;
import com.hp.workpath.api.deviceusage.DeviceUsageService;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DeviceUsageServiceTest {
    private static final String TAG = "DeviceUsageServiceTest";
    private static Context mContext;
    private final DutController mDutController;

    // Constructor
    public DeviceUsageServiceTest() {
        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void SetUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        mDutController.stopApp(mContext);
    }

    /**
     * Test API : DeviceUsageService.isSupported
     * Target : Simulator, Emulator
     */
    @Test
    public void DeviceUsageService_isSupported_ReturnsTrue() {
        boolean supported = DeviceUsageService.isSupported(mContext);
        assertTrue(supported);
    }

    /**
     * Test API : DeviceUsageService.getDeviceUsageInfo
     * Target : Simulator, Emulator
     */
    @Test
    public void DeviceUsageService_getDeviceUsageInfo_ReturnsDeviceUsageInfo() {
        boolean supported = DeviceUsageService.isSupported(mContext);
        assertTrue(supported);

        Result result = new Result();
        DeviceUsageInfo deviceUsageInfo = DeviceUsageService.getDeviceUsageInfo(mContext, result);

        //validate the result default DeviceUsageInfo attributes
        assertNotNull("deviceUsageInfo", deviceUsageInfo);
        assertNotNull("deviceUsageInfo.getPrinter()", deviceUsageInfo.getPrinter());
        assertNotNull("deviceUsageInfo.getScanner()", deviceUsageInfo.getScanner());
        assertEquals("RESULT_OK", result.getCode(), Result.RESULT_OK);
    }
}
