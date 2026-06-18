package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.IPayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.IServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.ISetupCallback;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry.IPayloadCallbackRegistrationListener;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry.ISetupCallbackRegistrationListener;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry.IServiceCallbackRegistrationListener;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelCallbackRegistryUnitTest {
    private static final String E2_SERVICE_GUN = "com.hp.ext.service.test.version.1";
    private static final String PATH = "testPath";

    @After
    public void tearDown() {
        AppChannelCallbackRegistry.clear();
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRegisterAndRetrievePayloadCallback_ThenCallbackIsRetrieved() {
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        List<IPayloadCallback> callbacks = AppChannelCallbackRegistry.getPayloadCallback(E2_SERVICE_GUN);
        assertNotNull(callbacks);
        assertTrue(callbacks.contains(payloadCallback));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenUnregisterPayloadCallback_ThenCallbackIsRemoved() {
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);
        AppChannelCallbackRegistry.unregisterPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        List<IPayloadCallback> callbacks = AppChannelCallbackRegistry.getPayloadCallback(E2_SERVICE_GUN);
        assertTrue(callbacks.isEmpty());
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRegisterAndRetrieveSetupCallback_ThenCallbackIsRetrieved() {
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);

        List<ISetupCallback> callbacks = AppChannelCallbackRegistry.getSetupCallback(E2_SERVICE_GUN);
        assertNotNull(callbacks);
        assertTrue(callbacks.contains(setupCallback));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenUnregisterSetupCallback_ThenCallbackIsRemoved() {
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);
        AppChannelCallbackRegistry.unregisterSetupCallback(E2_SERVICE_GUN, setupCallback);

        List<ISetupCallback> callbacks = AppChannelCallbackRegistry.getSetupCallback(E2_SERVICE_GUN);
        assertTrue(callbacks.isEmpty());
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRegisterAndRetrieveServiceCallback_ThenCallbackIsRetrieved() {
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        IServiceCallback retrievedCallback = AppChannelCallbackRegistry.getServiceCallback(E2_SERVICE_GUN, PATH);
        assertNotNull(retrievedCallback);
        assertEquals(serviceCallback, retrievedCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenUnregisterServiceCallback_ThenCallbackIsRemoved() {
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);
        AppChannelCallbackRegistry.unregisterServiceCallback(E2_SERVICE_GUN, PATH);

        IServiceCallback retrievedCallback = AppChannelCallbackRegistry.getServiceCallback(E2_SERVICE_GUN, PATH);
        assertNull(retrievedCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenClearRegistry_ThenAllCallbacksAreRemoved() {
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);

        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        AppChannelCallbackRegistry.clear();

        assertTrue(AppChannelCallbackRegistry.getPayloadCallback(E2_SERVICE_GUN).isEmpty());
        assertTrue(AppChannelCallbackRegistry.getSetupCallback(E2_SERVICE_GUN).isEmpty());
        assertNull(AppChannelCallbackRegistry.getServiceCallback(E2_SERVICE_GUN, PATH));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenAddPayloadCallbackListener_ThenListenerIsNotified() {
        // Create a mock listener
        IPayloadCallbackRegistrationListener mockListener = Mockito.mock(IPayloadCallbackRegistrationListener.class);
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);

        // Add the listener to the registry
        AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(mockListener);

        // Register a payload callback which should trigger the listener
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        // Verify the listener was called with correct parameters
        Mockito.verify(mockListener).beforeCallbackRegister(E2_SERVICE_GUN, payloadCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRemovePayloadCallbackListener_ThenListenerIsNotNotified() {
        // Create a mock listener
        IPayloadCallbackRegistrationListener mockListener = Mockito.mock(IPayloadCallbackRegistrationListener.class);
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);

        // Add and then remove the listener
        AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(mockListener);
        AppChannelCallbackRegistry.removePayloadCallbackRegistrationListener(mockListener);

        // Register a payload callback
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        // Verify the listener was not called
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.any(IPayloadCallback.class));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenMultiplePayloadCallbackListeners_ThenAllAreNotified() {
        // Create multiple mock listeners
        IPayloadCallbackRegistrationListener mockListener1 = Mockito.mock(IPayloadCallbackRegistrationListener.class);
        IPayloadCallbackRegistrationListener mockListener2 = Mockito.mock(IPayloadCallbackRegistrationListener.class);
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);

        // Add both listeners
        AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(mockListener1);
        AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(mockListener2);

        // Register a payload callback
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        // Verify both listeners were called
        Mockito.verify(mockListener1).beforeCallbackRegister(E2_SERVICE_GUN, payloadCallback);
        Mockito.verify(mockListener2).beforeCallbackRegister(E2_SERVICE_GUN, payloadCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenClear_ThenPayloadListenersAreRemoved() {
        // Create a mock listener
        IPayloadCallbackRegistrationListener mockListener = Mockito.mock(IPayloadCallbackRegistrationListener.class);
        IPayloadCallback payloadCallback = Mockito.mock(IPayloadCallback.class);

        // Add the listener
        AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(mockListener);

        // Clear the registry
        AppChannelCallbackRegistry.clear();

        // Register a payload callback
        AppChannelCallbackRegistry.registerPayloadCallback(E2_SERVICE_GUN, payloadCallback);

        // Verify the listener was not called since it was cleared
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.any(IPayloadCallback.class));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenAddSetupCallbackListener_ThenListenerIsNotified() {
        // Create mock listener and setup callback
        ISetupCallbackRegistrationListener mockListener = Mockito.mock(ISetupCallbackRegistrationListener.class);
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);

        // Add the listener to the registry
        AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(mockListener);

        // Register a setup callback which should trigger the listener
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);

        // Verify the listener was called with correct parameters
        Mockito.verify(mockListener).beforeCallbackRegister(E2_SERVICE_GUN, setupCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRemoveSetupCallbackListener_ThenListenerIsNotNotified() {
        // Create mock listener and setup callback
        ISetupCallbackRegistrationListener mockListener = Mockito.mock(ISetupCallbackRegistrationListener.class);
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);

        // Add and then remove the listener
        AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(mockListener);
        AppChannelCallbackRegistry.removeSetupCallbackRegistrationListener(mockListener);

        // Register a setup callback
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);

        // Verify the listener was not called
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.any(ISetupCallback.class));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenMultipleSetupCallbackListeners_ThenAllAreNotified() {
        // Create multiple mock listeners
        ISetupCallbackRegistrationListener mockListener1 = Mockito.mock(ISetupCallbackRegistrationListener.class);
        ISetupCallbackRegistrationListener mockListener2 = Mockito.mock(ISetupCallbackRegistrationListener.class);
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);

        // Add both listeners
        AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(mockListener1);
        AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(mockListener2);

        // Register a setup callback
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);

        // Verify both listeners were called
        Mockito.verify(mockListener1, times(1)).beforeCallbackRegister(E2_SERVICE_GUN, setupCallback);
        Mockito.verify(mockListener2, times(1)).beforeCallbackRegister(E2_SERVICE_GUN, setupCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenClear_ThenSetupListenersAreRemoved() {
        // Create a mock listener
        ISetupCallbackRegistrationListener mockListener = Mockito.mock(ISetupCallbackRegistrationListener.class);
        ISetupCallback setupCallback = Mockito.mock(ISetupCallback.class);

        // Add the listener
        AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(mockListener);

        // Clear the registry
        AppChannelCallbackRegistry.clear();

        // Register a setup callback
        AppChannelCallbackRegistry.registerSetupCallback(E2_SERVICE_GUN, setupCallback);

        // Verify the listener was not called since it was cleared
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.any(ISetupCallback.class));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenAddServiceCallbackListener_ThenListenerIsNotified() {
        // Create a mock listener and service callback
        IServiceCallbackRegistrationListener mockListener = Mockito.mock(IServiceCallbackRegistrationListener.class);
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);

        // Add the listener to the registry
        AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(mockListener);

        // Register a service callback which should trigger the listener
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        // Verify the listener was called with correct parameters
        Mockito.verify(mockListener).beforeCallbackRegister(E2_SERVICE_GUN, PATH, serviceCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenRemoveServiceCallbackListener_ThenListenerIsNotNotified() {
        // Create a mock listener and service callback
        IServiceCallbackRegistrationListener mockListener = Mockito.mock(IServiceCallbackRegistrationListener.class);
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);

        // Add and then remove the listener
        AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(mockListener);
        AppChannelCallbackRegistry.removeServiceCallbackRegistrationListener(mockListener);

        // Register a service callback
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        // Verify the listener was not called
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(IServiceCallback.class));
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenMultipleServiceCallbackListeners_ThenAllAreNotified() {
        // Create multiple mock listeners
        IServiceCallbackRegistrationListener mockListener1 = Mockito.mock(IServiceCallbackRegistrationListener.class);
        IServiceCallbackRegistrationListener mockListener2 = Mockito.mock(IServiceCallbackRegistrationListener.class);
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);

        // Add both listeners
        AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(mockListener1);
        AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(mockListener2);

        // Register a service callback
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        // Verify both listeners were called
        Mockito.verify(mockListener1, times(1)).beforeCallbackRegister(E2_SERVICE_GUN, PATH, serviceCallback);
        Mockito.verify(mockListener2, times(1)).beforeCallbackRegister(E2_SERVICE_GUN, PATH, serviceCallback);
    }

    @Test
    public void GivenAppChannelCallbackRegistry_WhenClear_ThenServiceListenersAreRemoved() {
        // Create a mock listener
        IServiceCallbackRegistrationListener mockListener = Mockito.mock(IServiceCallbackRegistrationListener.class);
        IServiceCallback serviceCallback = Mockito.mock(IServiceCallback.class);

        // Add the listener
        AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(mockListener);

        // Clear the registry
        AppChannelCallbackRegistry.clear();

        // Register a service callback
        AppChannelCallbackRegistry.registerServiceCallback(E2_SERVICE_GUN, PATH, serviceCallback);

        // Verify the listener was not called since it was cleared
        Mockito.verify(mockListener, Mockito.never()).beforeCallbackRegister(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(IServiceCallback.class));
    }
}
