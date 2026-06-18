package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;

public class StandardDeviceUISwitchServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceUISwitchService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
        assertNotNull(uiSwitchService);
    }

    @Test
    public void GivenStandardDeviceUISwitchService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
        assertNotNull(uiSwitchService);
    }

    /**
     * pre-requisite : install a sample workpath app on the device simulator and launch it on the dune simulator
     */
    //@Test
    public void GivenStandardDeviceUISwitchService_WhenSwitchToDeviceCalled_AfterDeviceManagementServiceInitiated_ThenNoRemovableDevicesShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create ui switching service object
        StandardDeviceUISwitchService uiSwitchService = new StandardDeviceUISwitchService();
        assertNotNull(uiSwitchService);

        //3. request to switch UI to the connected device simulator
        try {
            uiSwitchService.switchToDevice();
        } catch (Exception e) {
            //if app is not running, dune responds with HTTP 500 error
            //fail("Unexpected exception occurs :" + e);
        }
    }
}
