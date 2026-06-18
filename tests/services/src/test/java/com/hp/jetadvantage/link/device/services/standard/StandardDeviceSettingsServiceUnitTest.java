package com.hp.jetadvantage.link.device.services.standard;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.network.PrintServices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceSettingsServiceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private CDMClient mockHttpClient;

    @Test
    public void GivenStandardDeviceSettingsService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);
    }

    /**
     * Error case test : when device not connected
     */
    @Test
    public void GivenStandardDeviceSettingsService_WhenGetPrintServicesCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        //Define mocked DeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        //Create target device service instance
        StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService(mockDeviceManagementService);
        assertNotNull(deviceSettingsService);

        try {
            deviceSettingsService.getPrintServices();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : get valid print services configuration data from a connected device
     */
    @Test
    public void GivenStandardDeviceSettingsService_WhenGetPrintServicesCalled_AndDeviceIsConnected_ThenValidPrintServicesConfigurationShouldBeReturn() throws IOException {
        //Define mocked objects
        CDMResponse<String> cdmResponse = CDMResponse.create(200, getSamplePrintServices());
        when(mockHttpClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        //Create target device service instance
        StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService(mockDeviceManagementService);
        assertNotNull(deviceSettingsService);

        try {
            deviceSettingsService.enableNetworkPrintServices();
            PrintServices printServices =  deviceSettingsService.getPrintServices();
            assertNotNull(printServices);
            assertEquals("true", printServices.getAirPrint().getEnabled().toString());
            assertEquals("true", printServices.getIpp().getIpp().toString());
            assertEquals("true", printServices.getIpp().getIppSecure().toString());
            assertEquals("true", printServices.getLpdPrint().getEnabled().toString());
            assertEquals("true", printServices.getPort9100().getEnabled().toString());
            assertEquals("true", printServices.getWsPrint().getEnabled().toString());

        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    private String getSamplePrintServices() {
        return "{\n" +
                "  \"airPrint\": {\n" +
                "    \"enabled\": \"true\"\n" +
                "  },\n" +
                "  \"ipp\": {\n" +
                "    \"enableCertificateValidation\": \"true\",\n" +
                "    \"enableUserAuthentication\": \"true\",\n" +
                "    \"ipp\": \"true\",\n" +
                "    \"ippSecure\": \"true\"\n" +
                "  },\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/cdm/network/v1/printServices/constraints\",\n" +
                "      \"rel\": \"constraints\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"lpdPrint\": {\n" +
                "    \"enabled\": \"true\",\n" +
                "    \"maxCustomQueues\": 6,\n" +
                "    \"maxStringValues\": 8,\n" +
                "    \"systemQueues\": [\n" +
                "      {\n" +
                "        \"queueName\": \"auto\",\n" +
                "        \"queueType\": \"auto\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"queueName\": \"raw\",\n" +
                "        \"queueType\": \"raw\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"queueName\": \"binps\",\n" +
                "        \"queueType\": \"binps\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"queueName\": \"text\",\n" +
                "        \"queueType\": \"text\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"port9100\": {\n" +
                "    \"enabled\": \"true\"\n" +
                "  },\n" +
                "  \"version\": \"1.1.0\",\n" +
                "  \"wsPrint\": {\n" +
                "    \"enabled\": \"true\"\n" +
                "  }\n" +
                "}";
    }
}
