package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDevicePrintJobServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        assertNotNull(devicePrintJobService);
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {
        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create print job service object
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        assertNotNull(devicePrintJobService);
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create print job service object
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        assertNotNull(devicePrintJobService);

        //3. call isSupported() : get E2 printjob capabilities from the connected device simulator
        boolean supported = devicePrintJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenGetIppEndpointCalled_ThenIppEndpointShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create print job service object
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        assertNotNull(devicePrintJobService);

        //3. call getIppEndpoint() : get IppEndpoint from the connected device simulator
        String ippEndpoint = devicePrintJobService.getIppEndpoint();
        assertNotNull(ippEndpoint);
    }


}
