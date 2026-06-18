package com.hp.jetadvantage.link.device.services.standard.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.Context;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUnitTest;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.ws.websocket.SystemManagementMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemManagementMessageHandlerUnitTest extends StandardDeviceUnitTest {
    @Mock
    Context mockContext;
    @Mock
    PackageManagerHelper mockPmHelper;
    private SystemManagementMessageHandler handler;
    private IDeviceAuthSessionChangeCallback mockListener1;
    private IDeviceAuthSessionChangeCallback mockListener2;
    private IDeviceSystemStateChangeCallback mockStateListener1;
    private IDeviceSystemStateChangeCallback mockStateListener2;

    @Before
    public void setUp() throws Exception {
        handler = new SystemManagementMessageHandler();
        mockListener1 = mock(IDeviceAuthSessionChangeCallback.class);
        mockListener2 = mock(IDeviceAuthSessionChangeCallback.class);

        mockStateListener1 = mock(IDeviceSystemStateChangeCallback.class);
        mockStateListener2 = mock(IDeviceSystemStateChangeCallback.class);
    }

    @After
    public void tearDown() throws Exception {
        handler.shutdown();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenAddAuthSessionChangeListenerCalledWithNull_ThenDoNothing() {
        SystemManagementMessageHandler.addAuthSessionChangeListener(null);
        // No exception should be thrown
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenRemoveAuthSessionChangeListenerCalledWithNull_ThenDoNothing() {
        SystemManagementMessageHandler.removeAuthSessionChangeListener(null);
        // No exception should be thrown
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithEmptyData_ThenDoNothing() {
        handler.onReceived(1, "");
        handler.onReceived(1, null);
        // Verify no exceptions and proper logging
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithInvalidJson_ThenDoNothing() {
        String invalidJson = "{invalid}";
        handler.onReceived(1, invalidJson);
        // Verify no exceptions and proper logging
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithEmptyJson_ThenDoNothing() {
        handler.onReceived(1, "{}"); // Simulate a message with no event
        // Verify no exceptions and proper logging
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithLogInEvent_ThenListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener2);

        SystemManagementMessage.AuthnSessionEvent mockEvent =
                SystemManagementMessage.AuthnSessionEvent.AS_FRONT_PANEL_LOGIN;
        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogin\"}},\"traceId\": 15}}");

        verify(mockListener1, timeout(1000)).onSignIn();
        verify(mockListener2, timeout(1000)).onSignIn();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithLogOutEvent_ThenListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener2);

        SystemManagementMessage.AuthnSessionEvent mockEvent =
                SystemManagementMessage.AuthnSessionEvent.AS_FRONT_PANEL_LOGIN;
        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogout\"}},\"traceId\": 15}}");

        verify(mockListener1, timeout(1000)).onSignOut();
        verify(mockListener2, timeout(1000)).onSignOut();
        verify(mockListener1, never()).onSignIn();
        verify(mockListener2, never()).onSignIn();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenClearAuthSessionChangeListenersCalled_ThenListenersShouldBeRemoved() {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);
        SystemManagementMessageHandler.clearAuthSessionChangeListeners();

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"asFrontPanelLogin\"}},\"traceId\": 15}}");

        verify(mockListener1, never()).onSignIn();
    }


    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithUninterestedMessage_ThenListenersShouldNotBeInvoked() {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);
        SystemManagementMessageHandler.clearAuthSessionChangeListeners();

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"powerLevelChange\": {\"from\": " +
                "\"plSystemActivity\",\"to\": \"plSleep\"}},\"traceId\": 16}}");

        verify(mockListener1, never()).onSignIn();
        verify(mockListener1, never()).onSignOut();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithEmptyEvent_ThenListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {\"event\": " +
                "\"\"}},\"traceId\": 15}}");

        verify(mockListener1, never()).onSignIn();
        verify(mockListener1, never()).onSignOut();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithEmptyEvent2_ThenListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addAuthSessionChangeListener(mockListener1);

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"authnSessionChange\": {}},\"traceId\": 15}}");

        verify(mockListener1, never()).onSignIn();
        verify(mockListener1, never()).onSignOut();
    }


    @Test
    public void GivenSystemManagementMessageHandler_WhenAddSystemStateChangeListenerCalledWithNull_ThenDoNothing() {
        SystemManagementMessageHandler.addSystemStateChangeListener(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, null);
        // No exception should be thrown
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenRemoveSystemStateChangeListenerCalledWithNull_ThenDoNothing() {
        SystemManagementMessageHandler.removeSystemStateChangeListener(null);
        // No exception should be thrown
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenNotifySystemStateChanged_ThenListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addSystemStateChangeListener(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, mockStateListener1);
        SystemManagementMessageHandler.addSystemStateChangeListener(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, mockStateListener2);

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"systemStateChange\": {\"event\": \"sceUsbStorageEvent\"}},\"traceId\": 1234}}");

        verify(mockStateListener1, timeout(1000)).onChange();
        verify(mockStateListener2, timeout(1000)).onChange();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenClearSystemStateChangeListenersCalled_ThenListenersShouldBeRemoved() {
        SystemManagementMessageHandler.addSystemStateChangeListener(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, mockStateListener1);
        SystemManagementMessageHandler.clearSystemStateChangeListeners();

        handler.onReceived(1, "{\"systemManagement\": {\"details\": {\"systemStateChange\": {\"event\": \"sceUsbStorageEvent\"}},\"traceId\": 21}}");

        verify(mockStateListener1, never()).onChange();
    }

    @Test
    public void GivenSystemManagementMessageHandler_WhenOnReceivedCalledWithUsbStorageEvent_ThenSystemStateChangeListenersShouldBeInvoked() throws Exception {
        SystemManagementMessageHandler.addSystemStateChangeListener(SystemManagementMessage.SystemState.SCE_USB_STORAGE_EVENT, mockStateListener1);

        handler.onReceived(1, "{\"systemManagement\":{\"details\":{\"systemStateChange\":{\"event\":\"sceUsbStorageEvent\"}},\"traceId\":1169}}");

        verify(mockStateListener1, timeout(1000)).onChange();
    }
}
