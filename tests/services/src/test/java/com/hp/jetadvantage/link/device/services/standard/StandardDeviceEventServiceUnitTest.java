package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.CdmPubMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.ws.cdm.alert.Alerts;
import com.hp.ws.cdm.pubsub.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceEventServiceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;

    @Mock
    private CDMClient mockHttpClient;

    private CountDownLatch latch;

    @Before
    public void SetUp() {
        latch = new CountDownLatch(1);
    }

    @Test
    public void GivenStandardDeviceEventService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService();
        assertNotNull(deviceEventService);
    }

    /**
     * Error case test : getDeviceEvents()
     * when device not connected while calling getDeviceEvents.
     */
    @Test
    public void GivenStandardDeviceEventService_WhenDeviceEventsCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            deviceEventService.getDeviceEvents();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : getDeviceEvents()
     * get valid device event data from a connected device
     */
    @Test
    public void GivenStandardDeviceEventService_WhenGetDeviceEventsCalled_AndDeviceIsConnected_ThenValidDeviceEventsDataShouldBeReturn() throws IOException {
        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/GET_cdm_alerts.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmAlertsResponse);
        when(mockHttpClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            Alerts alerts = deviceEventService.getDeviceEvents();
            assertNotNull(alerts);
            assertEquals(3, alerts.getAlerts().size());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    /**
     * Error case test : isSupported()
     * when device not connected while calling isSupported.
     */
    @Test
    public void GivenStandardDeviceEventService_WhenIsSupportedCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            deviceEventService.isSupported();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : isSupported()
     * get valid device event data from a connected device
     */
    @Test
    public void GivenStandardDeviceEventService_WhenIsSupportedCalled_AndDeviceIsConnected_ThenValidDeviceEventsDataShouldBeReturn() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/GET_cdm_capabilities.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockHttpClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockHttpClient);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            boolean isSupported = deviceEventService.isSupported();
            assertTrue(isSupported);
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    /**
     * Happy case : onChangeEvent()
     * callback should be added successfully and get events from CdmCallback.
     */
    @Test
    public void GivenStandardDeviceEventService_WhenAddCallbackCalled_ThenCallbackShouldBeAdded() throws IOException, InterruptedException {
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/PUB_msg_device_event.json");
        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();
        deviceEventService.addCallback(new ICdmCallback() {
            @Override
            public void onChangeEvent(List<Message> reports) {
                for (Message report : reports) {
                    if (report.getGun() != null &&
                            StandardDeviceEventService.Gun.GUN_ALERTS.equals(report.getGun())) {
                        JsonObject data = report.getData();

                        String title = data.get("stringId").getAsString();
                        String instanceId = data.get("instanceId").getAsString();
                        String category = data.get("category").getAsString();
                        String severity = data.get("severity").getAsString();
                        int eventCode = data.get("alertCode").getAsInt();

                        assertEquals("83045", title);
                        assertEquals("0", instanceId);
                        assertEquals("fuserWrapJam", category);
                        assertEquals("error", severity);
                        assertEquals(5308180, eventCode);
                    }
                }
                latch.countDown();
            }
        });

        cdmPubMessageHandler.onReceived(0, cdmAlertsResponse);
        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(StandardDeviceSubscriptionService.getCallback(StandardDeviceEventService.SUBSCRIPTION_ID));
    }
}
