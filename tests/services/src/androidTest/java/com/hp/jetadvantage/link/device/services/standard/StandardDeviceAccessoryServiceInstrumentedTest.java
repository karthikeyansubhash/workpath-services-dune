package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.services.accessorylet.adapter.AccessoryDeviceAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceAccessoryServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);
    }

    /**
     * TEST for AccessoryDeviceAdapter.isSupported : Happy case
     */
    @Test
    public void GivenAccessoryDeviceAdapter_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);

        assertTrue(accessoryService.isSupported());
    }

    /**
     * TEST for ScanDeviceAdapter.isReady : Happy case
     */
    @Test
    public void GivenAccessoryDeviceAdapter_WhenIsReadyCalled_ThenTrueShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);

        assertTrue(accessoryService.isReady());
    }

    /**
     * TEST for ScanDeviceAdapter.getOwnedAccessories : Happy case
     * Need to improve accessory case
     */
    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetOwnedAccessoriesCalled_ThenAccessoriesShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);

        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getOwnedAccessories(accessoryService, testPackageName);
        assertEquals(0, accessories.size());
    }

    /**
     * TEST for ScanDeviceAdapter.getSharedAccessories : Happy case
     * Need to improve accessory case
     */
    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetSharedAccessoriesCalled_ThenAccessoriesShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);

        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getSharedAccessories(accessoryService, testPackageName);
        assertEquals(0, accessories.size());
    }

    /**
     * TEST for ScanDeviceAdapter.enumerateAccessories : Happy case
     * Need to improve accessory case
     */
    @Test
    public void GivenAccessoryDeviceAdapter_WhenEnumerateAccessoriesCalled_ThenAccessoriesShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);

        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.enumerateAccessories(accessoryService, testPackageName);
        assertEquals(0, accessories.size());
    }
}
