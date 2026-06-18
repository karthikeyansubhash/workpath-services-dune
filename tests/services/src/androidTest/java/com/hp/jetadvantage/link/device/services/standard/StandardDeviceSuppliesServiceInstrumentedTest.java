package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.device.Identity;
import com.hp.ext.service.supplies.SuppliesAgents;
import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.ext.types.supply.Supply;
import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceSuppliesServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    private static final String SUPPLIES_PI_TEST_PACKAGE_NAME = DutInfo.PI_TEST_PACKAGE_NAME;
    private static final String SUPPLIES_PI_TEST_AGENT_ID = DutInfo.PI_TEST_SUPPLIES_AGENT_ID;

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceSuppliesService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSuppliesService suppliesService = new StandardDeviceSuppliesService();
        assertNotNull(suppliesService);
    }

    @Test
    public void GivenStandardDeviceSuppliesService_Check_WhetherSupplyAgentExists() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceSuppliesService suppliesService = new StandardDeviceSuppliesService();
        assertNotNull(suppliesService);

        SuppliesAgents suppliesAgents = suppliesService.getSuppliesAgent(SUPPLIES_PI_TEST_PACKAGE_NAME);
        assertNotNull(suppliesAgents);

        try {
            assertFalse(suppliesAgents.getMemberIds().isEmpty());
            assertEquals(SUPPLIES_PI_TEST_AGENT_ID, suppliesAgents.getMemberIds().get(0).getValue().toString());
        } catch (Exception e) {
            fail("Please check the Supplies sample was installed in test device: " + e.getMessage());
        }
    }

    /**
     * TEST for SuppliesAdapter.isSupported : Happy case
     */
    @Test
    public void GivenSuppliesAdapter_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceSuppliesService suppliesService = new StandardDeviceSuppliesService();
        assertNotNull(suppliesService);

        assertTrue(suppliesService.isSupported());
    }

    /**
     * TEST for SuppliesAdapter.getIdentity : Happy case
     */
    @Test
    public void GivenSuppliesAdapter_WhenGetIdentityCalled_ThenMakeAndModelShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceSuppliesService suppliesService = new StandardDeviceSuppliesService();
        assertNotNull(suppliesService);

        Identity identity = suppliesService.getIdentity();
        assertNotNull(identity);

        //assertEquals("HP Color LaserJet MFP E785", identity.getMakeAndModelInfo().getFamily().getValue());
        assertTrue(identity.getMakeAndModelInfo().getFamily().getValue().contains("LaserJet"));
    }

    /**
     * TEST for SuppliesAdapter.getSuppliesInfo : Happy case
     */
    @Test
    public void GivenSuppliesAdapter_WhenGetSuppliesInfoCalled_ThenSuppliesInfoObjectShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        PackageManagerHelper mockPmHelper = Mockito.mock(PackageManagerHelper.class);
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(DutInfo.PI_TEST_SOLUTION_ID);
        Mockito.when(mockPmHelper.getAgentId(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString())).thenReturn(SUPPLIES_PI_TEST_AGENT_ID);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());

        StandardDeviceSuppliesService suppliesService = new StandardDeviceSuppliesService();
        assertNotNull(suppliesService);

        SuppliesInfo suppliesInfo = suppliesService.getSuppliesInfo(SUPPLIES_PI_TEST_PACKAGE_NAME);
        assertNotNull(suppliesInfo);

        assertFalse(suppliesInfo.getSuppliesList().isEmpty());
        assertEquals("com.hp.ext.service.supplies.version.1.type.suppliesInfo", suppliesInfo.getTypeGUN());

        Supply supply = suppliesInfo.getSuppliesList().get(suppliesInfo.getSuppliesList().size() - 1);
        assertNotNull(supply);
    }
}
