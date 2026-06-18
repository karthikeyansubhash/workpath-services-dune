package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.device.DeviceStatusCategory;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.device.Status;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceInfoServiceInstrumentedTest extends StandardDeviceInstrumentedTest {
    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create deviceInfoService object
        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService();
        assertNotNull(deviceInfoService);
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetIdentityCalled_ThenIdentityShouldBeObtained() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create deviceInfoService object
        IDeviceInfoService deviceInfoService = new StandardDeviceInfoService();
        assertNotNull(deviceInfoService);

        //get device identity
        Identity deviceIdentity = deviceInfoService.getIdentity();

        //verify the retrieved device identity
        assertNotNull(deviceIdentity);
        assertNotNull(deviceIdentity.getDeviceUuid());
        assertNotNull(deviceIdentity.getFirmwareVersion());

        //{"$opMeta":{"contentFilter":["*"]},"deviceUuid":"b8ea31b1-64ec-483c-be5e-3532c5a0caa8","firmwareVersion":"6.24.0.646+723c31e8-202404091140","links":[{"href":"/ext/device/v1/identity","rel":"self"}],"makeAndModelInfo":{"base":"HP Color LaserJet Flow E877","family":"HP Color LaserJet Flow MFP E877","model":"HP Color LaserJet Flow E87740"},"serialNumber":"unknown"}
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetScannerCalled_ThenScannerShouldBeObtained() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create deviceInfoService object
        IDeviceInfoService deviceInfoService = new StandardDeviceInfoService();
        assertNotNull(deviceInfoService);

        //get scanner
        Scanner scanner = deviceInfoService.getScanner();

        //verify the retrieved scanner
        assertNotNull(scanner);

        //online depends on the status of the scanner
        //assertTrue(scanner.getIsOnline());

        //{"$opMeta":{"contentFilter":["*"]},"adfOutputBinIsFull":"unknown","hasPaperInAdf":"true","hasPaperOnFlatbed":"true","isBusy":false,"isOnline":true,"links":[{"href":"/ext/device/v1/scanner","rel":"self"}]}
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetStatusCalled_ThenStatusShouldBeObtained() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create deviceInfoService object
        IDeviceInfoService deviceInfoService = new StandardDeviceInfoService();
        assertNotNull(deviceInfoService);

        //get device status
        Status status = deviceInfoService.getStatus();

        //verify the retrieved status
        assertNotNull(status);
        DeviceStatusCategory statusCategory = status.getStatus();
        assertNotNull(statusCategory.getValue());

        //{"$opMeta":{"contentFilter":["*"]},"links":[{"href":"/ext/device/v1/status","rel":"self"}],"status":"dsInPowerSave"}
    }
}
