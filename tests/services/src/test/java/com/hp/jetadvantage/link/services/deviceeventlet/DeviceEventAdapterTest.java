package com.hp.jetadvantage.link.services.deviceeventlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Handler;
import android.os.Message;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSubscriptionService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.CdmPubMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.deviceeventslet.adapter.DeviceEventAdapter;
import com.hp.jetadvantage.link.services.deviceeventslet.handler.DeviceEventHandler;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class DeviceEventAdapterTest {

    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private CDMClient mockCDMClient;
    @Mock
    private Handler mockHandler;

    @Before
    public void setUp() {
        mockHandler = mock(Handler.class);
    }

    @Test
    public void GivenStandardDeviceEventService_WhenIsSupportedCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        // Error case test : when device not connected
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            DeviceEventAdapter.isSupported(deviceEventService);
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void GivenStandardDeviceEventService_WhenIsSupportedCalled_AndGetCapabilities_ThenValidDeviceEventsDataShouldBeReturn() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/GET_cdm_capabilities.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            boolean isSupported = DeviceEventAdapter.isSupported(deviceEventService);
            assertTrue(isSupported);
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    @Test
    public void GivenStandardDeviceEventService_WhenAddCallbackCalled_ThenCallbackShouldBeAdded() throws IOException {
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/PUB_msg_device_event.json");
        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();

        assertNotNull(deviceEventService);

        Message mockMessage = mock(Message.class);
        mockMessage.what = 1001;
        when(mockHandler.obtainMessage(anyInt())).thenReturn(mockMessage);

        DeviceEventAdapter.addCallback(deviceEventService, new DeviceEventHandler(mockHandler));
        // receive CDM alerts response.
        cdmPubMessageHandler.onReceived(0, cdmAlertsResponse);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockHandler).sendMessage(messageCaptor.capture());

        // Get the captured message
        Message capturedMessage = messageCaptor.getValue();
        DeviceEvent deviceEvent = (DeviceEvent) capturedMessage.obj;

        assertEquals("83045", deviceEvent.getTitle());
        assertEquals("0", deviceEvent.getInstanceId());
        assertEquals("fuserWrapJam", deviceEvent.getCategory());
        assertEquals("error", deviceEvent.getSeverity());
        assertEquals("5308180", deviceEvent.getEventCode());

        assertNotNull(StandardDeviceSubscriptionService.getCallback(StandardDeviceEventService.SUBSCRIPTION_ID));
    }

    @Test
    public void GivenStandardDeviceEventService_WhenGetDeviceEventsCalled_ThenValidDeviceEventsDataShouldBeReturn() throws IOException {
        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/GET_cdm_alerts.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmAlertsResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            List<DeviceEvent> deviceEventList = DeviceEventAdapter.getDeviceEvents(deviceEventService);
            assertNotNull(deviceEventList);
            assertEquals(3, deviceEventList.size());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    @Test
    public void GivenStandardDeviceEventService_WhenGetDeviceEventsCalled_WithInformationSeverity_ThenNoEventsDataShouldBeReturn() throws IOException {
        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/GET_cdm_alerts_severity_information.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmAlertsResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        assertNotNull(deviceEventService);

        try {
            List<DeviceEvent> deviceEventList = DeviceEventAdapter.getDeviceEvents(deviceEventService);
            assertNotNull(deviceEventList);
            assertEquals(0, deviceEventList.size());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    @Test
    public void GivenStandardDeviceEventService_WhenAddCallbackCalled_WithInformationSeverity_ThenNoEventsDataShouldBeReturn() throws IOException {
        StandardDeviceEventService deviceEventService = new StandardDeviceEventService(mockDeviceManagementService);
        String cdmAlertsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "deviceEvent/PUB_msg_severity_information.json");
        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();

        assertNotNull(deviceEventService);

        Message mockMessage = mock(Message.class);
        mockMessage.what = 1001;
        when(mockHandler.obtainMessage(anyInt())).thenReturn(mockMessage);

        DeviceEventAdapter.addCallback(deviceEventService, new DeviceEventHandler(mockHandler));
        // receive CDM alerts response.
        cdmPubMessageHandler.onReceived(0, cdmAlertsResponse);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockHandler).sendMessage(messageCaptor.capture());

        // Get the captured message
        Message capturedMessage = messageCaptor.getValue();
        DeviceEvent deviceEvent = (DeviceEvent) capturedMessage.obj;

        assertEquals("83045", deviceEvent.getTitle());
        assertEquals("0", deviceEvent.getInstanceId());
        assertEquals("fuserWrapJam", deviceEvent.getCategory());
        assertEquals("error", deviceEvent.getSeverity());
        assertEquals("5308180", deviceEvent.getEventCode());

        assertNotNull(StandardDeviceSubscriptionService.getCallback(StandardDeviceEventService.SUBSCRIPTION_ID));
    }
}
