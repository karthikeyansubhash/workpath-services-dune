package com.hp.jetadvantage.link.device.services.standard.common;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInstrumentedTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceManagementServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceManagementService_WhenHasUnauthorizedTokenIsCalled_ThenValidationResultShouldBeReturned() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //test for valid token
        boolean result = StandardDeviceManagementService.getInstance().hasUnauthorizedToken();
        assertFalse(result);

        //test for invalid token
        accessToken = "invalid";
        StandardDeviceManagementService.getInstance().updateDeviceInfo(deviceIp, accessToken);
        result = StandardDeviceManagementService.getInstance().hasUnauthorizedToken();
        assertTrue(result);
    }

    /* pre-requisite:
        1. latest pacman apk should be installed
        2. test sample app (testPackageName) should be installed on the device and agent (testRRName) should be registered with the expected agentId
        */
    //@Test
    public void GivenStandardDeviceManagementService_WhenGetAgentIsCalled_ThenAgentIdShouldBeReturned2() {

        String expectedAgentId = "4fedb481-a950-42e4-9aaa-cd26fd3f2acc";
        String testPackageName = "com.hp.workpath.sample.accessorysample";
        String testRRName = "com.hp.ext.service.usbAccessories.version.1.type.usbAccessoriesRegistrationRecord";

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //test for valid token
        String agentId = StandardDeviceManagementService.getInstance().getAgentId(testPackageName, testRRName);
        assertEquals(expectedAgentId, agentId);
    }
}
