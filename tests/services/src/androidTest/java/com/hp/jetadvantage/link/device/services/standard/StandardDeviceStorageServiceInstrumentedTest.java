package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStorageService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.storage.RemovableDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceStorageServiceInstrumentedTest extends StandardDeviceInstrumentedTest {
    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceStorageService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceStorageService storageService = new StandardDeviceStorageService();
        assertNotNull(storageService);
    }

    @Test
    public void GivenStandardDeviceStorageService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceStorageService storageService = new StandardDeviceStorageService();
        assertNotNull(storageService);
    }

    /**
     * prerequisite : remove any mock usb storage on the target Dune simulator if it has (UDW : UsbHostMgr PUB_removeMockStorageDevice disk1)
     */
    @Test
    public void GivenStandardDeviceStorageService_WhenGetStoragesCalled_AfterDeviceManagementServiceInitiated_ThenNoRemovableDevicesShouldBeReturned() {

        //0. Setup dune simulator : remove any mock usb storage on the target Dune simulator if it has
        try {
            testConn.getUdwClient(deviceIp).sendUnderwareCommand("1.0.0", "mainApp", "UsbHostMgr PUB_removeMockStorageDevice disk1");
        } catch (Exception e) {
            fail("Setup failed:" + e);
        }

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create scan job service object
        StandardDeviceStorageService storageService = new StandardDeviceStorageService();
        assertNotNull(storageService);

        //3. get default scan options from the connected device simulator
        List<RemovableDevice> results = storageService.getStorages();

        //4. verify received scan default options
        assertNotNull(results);
        assertEquals(0, results.size());

    }

    /**
     * prerequisite : add a mock usb storage on the target Dune simulator, driverId:"disk1", volumnName:"f" (UDW : UsbHostMgr PUB_addMockStorageDevice disk1 f)
     */
    @Test
    public void GivenStandardDeviceStorageService_WhenGetStoragesCalled_AfterDeviceManagementServiceInitiated_ThenARemovableDeviceShouldBeReturned() {

        //0. Setup dune simulator : add a mock usb storage on the target Dune simulator, driverId:"disk1", volumnName:"f" (UDW : UsbHostMgr PUB_addMockStorageDevice disk1 f)
        try {
            testConn.getUdwClient(deviceIp).sendUnderwareCommand("1.0.0", "mainApp", "UsbHostMgr PUB_addMockStorageDevice disk1 f");
        } catch (Exception e) {
            fail("Setup failed:" + e);
        }

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create scan job service object
        IDeviceStorageService storageService = new StandardDeviceStorageService();
        assertNotNull(storageService);

        //3. get default scan options from the connected device simulator
        List<RemovableDevice> results = storageService.getStorages();

        //4. verify received scan default options
        assertNotNull(results);
        assertEquals("disk1", results.get(0).getDriveId());
        assertEquals("f", results.get(0).getVolumeName());
    }
}