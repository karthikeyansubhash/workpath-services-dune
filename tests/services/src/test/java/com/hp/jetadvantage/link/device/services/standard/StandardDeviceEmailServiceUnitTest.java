package com.hp.jetadvantage.link.device.services.standard;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceEmailServiceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private CDMClient mockCDMHttpClient;

    @Mock
    protected InjectedHttpClient mockE2HttpClient;

    @Test
    public void GivenStandardDeviceEmailService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        assertNotNull(deviceEmailService);
    }

    /**
     * Error case test : when device not connected
     */
    @Test
    public void GivenStandardDeviceEmailService_WhenGetPrintServicesCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        //Define mocked DeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        //Create target device service instance
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService(mockDeviceManagementService);
        assertNotNull(deviceEmailService);

        try {
            deviceEmailService.getEmailSettings();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : get valid email settings data
     */
    @Test
    public void GivenStandardDeviceEmailService_WhenGetEmailSettingsCalled_AndDeviceIsConnected_ThenValidEmailSettingsDataShouldBeReturn() throws IOException {
        //Define mocked objects
        CDMResponse<String> cdmResponse = CDMResponse.create(200, getSampleEmailSettingsData());
        when(mockCDMHttpClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMHttpClient);

        //Create target device service instance
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService(mockDeviceManagementService);
        assertNotNull(deviceEmailService);

        try {
            EmailSettingsData emailSettingsData =  deviceEmailService.getEmailSettings();
            assertNotNull(emailSettingsData);
            assertEquals("test1@workpathtest.com", emailSettingsData.getDefaultFrom().getEmailAddress());
            assertEquals("workpath", emailSettingsData.getDefaultFrom().getDisplayName());
            assertEquals(true, emailSettingsData.isSendToEmailEnabled());
            assertEquals("15.26.148.127", emailSettingsData.getSmtpServerHostName());
            assertEquals(25, emailSettingsData.getSmtpServerPort().intValue());
            assertEquals(false, emailSettingsData.getUseSSL());
            assertEquals(true, emailSettingsData.getAuthenticationRequired());
            assertEquals("test1@workpathtest.com", emailSettingsData.getSmtpServerUserName());
            assertEquals("workpath@123", emailSettingsData.getSmtpServerPassword());
        } catch (Exception e) {
            fail("getEmailSettings() failed:" + e);
        }
    }
    private String getSampleEmailSettingsData() {
        return "{\n" +
                "  \"version\": \"1.0.0-alpha.11\",\n" +
                "  \"smtpServers\": {\n" +
                "    \"servers\": [\n" +
                "      {\n" +
                "        \"displayName\": \"workpath\",\n" +
                "        \"serverAddress\": \"15.26.148.127\",\n" +
                "        \"serverCredential\": {\n" +
                "          \"credentialType\": \"alwaysUseCredential\",\n" +
                "          \"userName\": \"test1@workpathtest.com\",\n" +
                "          \"password\": \"workpath@123\",\n" +
                "          \"isPasswordSet\": true\n" +
                "        },\n" +
                "        \"serverPort\": 25,\n" +
                "        \"serverRequireAuthentication\": \"true\",\n" +
                "        \"smtpServerId\": \"b2e069a7-629b-47f6-8389-359f8c171e5e\",\n" +
                "        \"useSsl\": \"false\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"dest\": {\n" +
                "    \"email\": {\n" +
                "      \"from\": {\n" +
                "        \"displayName\": \"workpath\",\n" +
                "        \"emailAddress\": \"test1@workpathtest.com\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

}
