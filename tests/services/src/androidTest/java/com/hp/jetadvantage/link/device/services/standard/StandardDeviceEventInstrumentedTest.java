package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.alert.Alerts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class StandardDeviceEventInstrumentedTest extends StandardDeviceInstrumentedTest {
    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceEventService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService();
        assertNotNull(deviceEventService);
    }

    @Test
    public void GivenStandardDeviceEventService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {
        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create event service object
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService();
        assertNotNull(deviceEventService);
    }

    @Test
    public void GivenStandardDeviceEventService_WhenGetDeviceEventsCalled_AfterDeviceManagementServiceInitiated_ThenDeviceEventsShouldBeReturned() {
        //0. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create event service object
        IDeviceEventService deviceEventService = new StandardDeviceEventService();
        assertNotNull(deviceEventService);

        //3. get device events from the connected device simulator
        Alerts alerts = deviceEventService.getDeviceEvents();
        assertNotNull(alerts);
    }

    @Test
    public void GivenStandardDeviceEventService_WhenIsSupportedCalled_AfterDeviceManagementServiceInitiated_ThenIsSupportedDataReturned() {
        //0. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //1. create event service object
        IDeviceEventService deviceEventService = new StandardDeviceEventService();
        assertNotNull(deviceEventService);

        //2. get device events from the connected device simulator
        boolean isSupported = deviceEventService.isSupported();
        assertEquals(true, isSupported);
    }
}
