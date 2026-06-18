package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceAccessServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    private static final String ACCESS_PI_TEST_PACKAGE_NAME = DutInfo.PI_TEST_PACKAGE_NAME;
    private static final String ACCESS_PI_TEST_AGENT_ID = DutInfo.PI_TEST_AUTHENTICATION_AGENT_ID;

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceAccessService deviceAccessService = new StandardDeviceAccessService();
        assertNotNull(deviceAccessService);
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceAccessService deviceAccessService = new StandardDeviceAccessService();
        assertNotNull(deviceAccessService);
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create access service object
        StandardDeviceAccessService deviceAccessService = new StandardDeviceAccessService();
        assertNotNull(deviceAccessService);

        //3. call isSupported() : get E2 access capabilities from the connected device simulator
        boolean supported = deviceAccessService.isSupported();
        assertTrue(supported);
    }

//    TODO : Since the test BDL file is not ready yet, I will apply it to the next story (DUNE-251992).
//    @Test
//    public void GivenStandardDeviceAccessService_Check() {
//        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);
//
//        StandardDeviceAccessService deviceAccessService = new StandardDeviceAccessService();
//        assertNotNull(deviceAccessService);
//
//        try {
//            AuthenticationAccessPoint_InitiateLogin login = deviceAccessService.initiateSignIn(ACCESS_PI_TEST_PACKAGE_NAME, ACCESS_PI_TEST_AGENT_ID);
//            Log.i("jws", "GivenStandardDeviceAccessService_Check login: " + JsonParser.getInstance().toJson(login));
//
//        } catch (Exception e) {
//            fail("Please check the Supplies sample was installed in test device: " + e.getMessage());
//        }
//    }
}
