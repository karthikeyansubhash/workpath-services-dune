package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.pubsub.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceSubscriptionServiceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private CDMClient mockHttpClient;

    private ICdmCallback testCdmCallback = new ICdmCallback() {
        @Override
        public void onChangeEvent(List<Message> reports) {

        }
    };

    @Test
    public void GivenStandardDeviceSubscriptionService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSubscriptionService subscriptionService = new StandardDeviceSubscriptionService();
        assertNotNull(subscriptionService);
    }

    /**
     * Error case test : when device not connected
     */
    @Test
    public void GivenStandardDeviceSubscriptionService_WhenSubscribeCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        //Define mocked DeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        //Create target device service instance
        StandardDeviceSubscriptionService subscriptionService = new StandardDeviceSubscriptionService(mockDeviceManagementService);
        assertNotNull(subscriptionService);

        try {
            String[] gun = {"com.hp.cdm.service.clock.version.1.resource.configuration"};
            subscriptionService.Subscribe(gun, testCdmCallback);
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : when CDM subscription is requested, then valid subscriptionId should be returned from a connected device
     */
    @Test
    public void GivenStandardDeviceSubscriptionService_WhenSubscribeCalled_AndDeviceIsConnected_ThenValidSubscriptionIdShouldBeReturn() throws IOException {
        //Define mocked objects
        CDMResponse<String> cdmResponse = CDMResponse.create(201, getSampleSubscriptionResponse());
        when(mockHttpClient.sendPostRequest(eq(StandardDeviceSubscriptionService.Url.CDM_SUBSCRIPTIONS), anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        //Create target device service instance
        StandardDeviceSubscriptionService subscriptionService = new StandardDeviceSubscriptionService(mockDeviceManagementService);
        assertNotNull(subscriptionService);

        try {
            String[] gun = {"com.hp.cdm.service.clock.version.1.resource.configuration"};
            String subscriptionId = subscriptionService.Subscribe(gun, testCdmCallback);

            assertEquals("1822922915", subscriptionId);
            assertEquals(testCdmCallback, subscriptionService.getCallback(subscriptionId));

        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    /**
     * Happy case : when CDM un-subscription is requested with valid subscriptionId,
     * then success should be returned from a connected device
     */
    @Test
    public void GivenStandardDeviceSubscriptionService_WhenUnsubscribeCalled_AndDeviceIsConnected_ThenTrueShouldBeReturn() throws IOException {
        //Define mocked objects
        CDMResponse<String> cdmResponse = CDMResponse.create(201, getSampleSubscriptionResponse());
        CDMResponse<String> cdmDeleteResponse = CDMResponse.create(204, "");
        when(mockHttpClient.sendPostRequest(eq(StandardDeviceSubscriptionService.Url.CDM_SUBSCRIPTIONS), anyString()))
                .thenReturn(cdmResponse);
        when(mockHttpClient.sendDeleteRequest(eq(StandardDeviceSubscriptionService.Url.CDM_SUBSCRIPTIONS + "/1822922915?clientId=workpathsvc")))
                .thenReturn(cdmDeleteResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        //Create target device service instance
        StandardDeviceSubscriptionService subscriptionService = new StandardDeviceSubscriptionService(mockDeviceManagementService);
        assertNotNull(subscriptionService);

        try {
            String[] gun = {"com.hp.cdm.service.clock.version.1.resource.configuration"};
            String subscriptionId = subscriptionService.Subscribe(gun, testCdmCallback);
            assertEquals("1822922915", subscriptionId);
            assertEquals(testCdmCallback, subscriptionService.getCallback(subscriptionId));

            boolean result = subscriptionService.Unsubscribe(subscriptionId);
            assertTrue(result);

            assertNull(subscriptionService.getCallback(subscriptionId));

        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    private String getSampleSubscriptionResponse() {
        return "{\n" +
                "    \"links\": [\n" +
                "        {\n" +
                "            \"href\": \"/cdm/pubsub/v2/subscriptions/1822922915\",\n" +
                "            \"rel\": \"self\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"href\": \"/cdm/pubsub/v2/subscriptions/1822922915/events\",\n" +
                "            \"rel\": \"events\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"href\": \"/cdm/pubsub/v2/subscriptions/1822922915/aggregate\",\n" +
                "            \"rel\": \"aggregate\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"resources\": [\n" +
                "        {\n" +
                "            \"gun\": \"com.hp.cdm.service.clock.version.1.resource.configuration\",\n" +
                "            \"gunValidationEnforced\": \"true\",\n" +
                "            \"syncOnBoot\": \"false\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"subscriptionId\": \"1822922915\",\n" +
                "    \"version\": \"2.3.0\"\n" +
                "}";
    }
}
