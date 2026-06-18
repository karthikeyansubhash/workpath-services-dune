package com.hp.jetadvantage.link.device.services.standard;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.storage.RemovableDevice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceStorageServiceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private CDMClient mockHttpClient;

    @Test
    public void GivenStandardDeviceStorageService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceStorageService storageService = new StandardDeviceStorageService();
        assertNotNull(storageService);
    }

    /**
     * Error case test : when device not connected
     */
    @Test
    public void GivenStandardDeviceStorageService_WhenGetStoragesCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        //Define mocked DeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        //Create target device service instance
        StandardDeviceStorageService storageService = new StandardDeviceStorageService(mockDeviceManagementService);
        assertNotNull(storageService);

        try {
            storageService.getStorages();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : get valid storage data from a connected device
     */
    @Test
    public void GivenStandardDeviceStorageService_WhenGetStoragesCalled_AndDeviceIsConnected_ThenValidStorageDataShouldBeReturn() throws IOException {
        //Define mocked objects
        CDMResponse<String> cdmResponse = CDMResponse.create(200, getSampleRemovableDevices());
        when(mockHttpClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        //Create target device service instance
        StandardDeviceStorageService storageService = new StandardDeviceStorageService(mockDeviceManagementService);
        assertNotNull(storageService);

        try {
            List<RemovableDevice> storageList = storageService.getStorages();
            assertNotNull(storageList);
            assertEquals(1, storageList.size());
            assertEquals("disk1", storageList.get(0).getDriveId());
            assertEquals("f", storageList.get(0).getVolumeName());

        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    @Test
    public void GivenStandardDeviceStorageService_WhenRegisterAppInstallUninstallCallback_ThenTheCallbackShouldBeRegistered() {
        IAppInstallUninstallCallback callback = (context, intent) -> {
            // Do nothing
        };
        // Register callback
        StandardDeviceStorageService storageService =
                new StandardDeviceStorageService(mockDeviceManagementService);
        storageService.registerAppInstallUninstallCallback(callback);

        verify(mockDeviceManagementService).registerAppInstallUninstallCallback(callback);

        storageService.unregisterAppInstallUninstallCallback(callback);

        verify(mockDeviceManagementService).unregisterAppInstallUninstallCallback(callback);
    }

    private String getSampleRemovableDevices() {
        return "{\n" +
                "  \"version\": \"1.2.0\",\n" +
                "  \"devices\": [\n" +
                "\t{\n" +
                "\t  \"driveId\": \"disk1\",\n" +
                "\t  \"volumeName\": \"f\",\n" +
                "\t  \"usbPortLocation\": \"frontUsb\",\n" +
                "\t  \"totalSpace\": 1516,\n" +
                "\t  \"usedSpace\": 0,\n" +
                "\t  \"availableSpace\": 1516,\n" +
                "\t  \"encryptionStatus\": \"false\",\n" +
                "\t  \"deviceType\": \"usb\",\n" +
                "\t  \"links\": [\n" +
                "\t\t{\n" +
                "\t\t  \"href\": \"/cdm/storageDevices/v1/removableDevices/disk1\",\n" +
                "\t\t  \"rel\": \"self\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t  \"href\": \"/cdm/storageDevices/v1/removableDevices/disk1/content/\",\n" +
                "\t\t  \"rel\": \"files\"\n" +
                "\t\t}\n" +
                "\t  ]\n" +
                "\t}\n" +
                "  ]\n" +
                "}";
    }
}
