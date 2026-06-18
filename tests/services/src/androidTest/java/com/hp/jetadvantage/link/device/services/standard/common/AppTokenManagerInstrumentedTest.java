package com.hp.jetadvantage.link.device.services.standard.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInstrumentedTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppTokenManagerInstrumentedTest extends StandardDeviceInstrumentedTest {

    String testSolutionId = "11111111-1111-1111-9999-111111111111";

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenAppTokenManagerHelper_WhenGetSolutionTokenWithInvalidParam_ThenEmptyStringShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        AppTokenManager appTokenMgr = new AppTokenManager(StandardDeviceManagementService.getInstance().getCDMClient());
        String solutionToken = appTokenMgr.getSolutionToken("");
        assertTrue(solutionToken.isEmpty());

        solutionToken = appTokenMgr.getSolutionToken(null);
        assertTrue(solutionToken.isEmpty());
    }

    // @Test
    // pre-requisites :
    // An App should be installed on the device with the testSolutionId
    public void GivenAppTokenManagerHelper_WhenGetSolutionTokenWithValidParam_ThenAppTokenShouldBeReturned() {

        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        AppTokenManager appTokenMgr = new AppTokenManager(StandardDeviceManagementService.getInstance().getCDMClient());
        String solutionToken = appTokenMgr.getSolutionToken(testSolutionId);
        assertFalse(solutionToken.isEmpty());

    }
}
