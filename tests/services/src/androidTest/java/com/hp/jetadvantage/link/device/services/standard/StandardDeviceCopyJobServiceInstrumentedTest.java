package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.copy.DefaultOptions;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceCopyJobServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        assertNotNull(copyJobService);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create copy job service object
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        assertNotNull(copyJobService);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create copy job service object
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        assertNotNull(copyJobService);

        //3. call isSupported() : get E2 copyjob capabilities from the connected device simulator
        boolean supported = copyJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenGetDefaultOptionsCalled_AfterDeviceManagementServiceInitiated_ThenDefaultOptionsShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create copy job service object
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        assertNotNull(copyJobService);

        //3. get default copy options from the connected device simulator
        DefaultOptions defCopyOptions = copyJobService.getDefaultOptions(testPackageName);

        //4. verify received copy default options
        assertNotNull(defCopyOptions);
    }
}
