package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.hp.ext.service.deviceUsage.LifetimeCounters;
import com.hp.ext.service.deviceUsage.DeviceUsageAgents;
import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceUsageServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    private static final String DEVICEUSAGE_PI_TEST_PACKAGE_NAME = DutInfo.PI_TEST_PACKAGE_NAME;
    private static final String DEVICEUSAGE_PI_TEST_AGENT_ID = DutInfo.PI_TEST_DEVICEUSAGE_AGENT_ID;

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceUsageService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService();
        assertNotNull(deviceUsageService);
    }

    @Test
    public void GivenStandardDeviceUsageService_Check_WhetherDeviceUsageAgentExists() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService();
        assertNotNull(deviceUsageService);

        DeviceUsageAgents deviceUsageAgents = deviceUsageService.getDeviceUsageAgent(DEVICEUSAGE_PI_TEST_PACKAGE_NAME);
        assertNotNull(deviceUsageAgents);

        try {
            assertFalse(deviceUsageAgents.getMemberIds().isEmpty());
            assertEquals(DEVICEUSAGE_PI_TEST_AGENT_ID, deviceUsageAgents.getMemberIds().get(0).getValue().toString());
        } catch (Exception e) {
            fail("Please check the DeviceUsage sample was installed in test device: " + e.getMessage());
        }
    }

    /**
     * TEST for deviceUsageService.isSupported : Happy case
     */
    @Test
    public void GivenStandardDeviceUsageService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService();
        assertNotNull(deviceUsageService);

        assertTrue(deviceUsageService.isSupported());
    }

    /**
     * TEST for deviceUsageService.getLifetimeCounters : Happy case
     */
    @Test
    public void GivenStandardDeviceUsageService_WhenGetLifetimeCountersCalled_ThenLifetimeCountersInfoObjectShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        PackageManagerHelper mockPmHelper = Mockito.mock(PackageManagerHelper.class);
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(DutInfo.PI_TEST_SOLUTION_ID);
        Mockito.when(mockPmHelper.getAgentId(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString())).thenReturn(DEVICEUSAGE_PI_TEST_AGENT_ID);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService();
        assertNotNull(deviceUsageService);

        LifetimeCounters lifetimeCounters = deviceUsageService.getLifetimeCounters(DEVICEUSAGE_PI_TEST_PACKAGE_NAME);
        assertNotNull(lifetimeCounters);

        assertEquals("com.hp.ext.service.deviceUsage.version.1.type.lifetimeCounters", lifetimeCounters.getTypeGUN());
    }

}
