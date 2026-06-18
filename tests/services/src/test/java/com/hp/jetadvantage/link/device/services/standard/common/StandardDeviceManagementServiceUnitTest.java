package com.hp.jetadvantage.link.device.services.standard.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUnitTest;
import com.hp.jetadvantage.link.device.services.standard.receivers.AppInstallEventReceiver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceManagementServiceUnitTest extends StandardDeviceUnitTest {

    @Mock
    Context mockContext;

    @Mock
    Context mockApplicationContext;

    @Mock
    PackageManagerHelper mockPmHelper;

    @Test
    public void GivenStandardDeviceManagementService_WhenInitialized_ThenRegistersBroadcastReceivers() {
        when(mockContext.getApplicationContext()).thenReturn(mockApplicationContext);

        DeviceManagementService service = StandardDeviceManagementService.getInstance();
        assertNotNull(service);

        service.initialize(mockContext, "localhost", "testToken");
        verify(mockContext, times(1)).registerReceiver(any(AppInstallEventReceiver.class), any(),
                eq(PackageContract.Permission.PACKAGE_LIFECYCLE_EVENTS), any());
    }

    @Test
    public void GivenStandardDeviceManagementService_WhenRegisterAppInstallUninstallCallback_ThenRegistered() {
        IAppInstallUninstallCallback callback = (context, intent) -> {
        };

        when(mockContext.getApplicationContext()).thenReturn(mockApplicationContext);

        DeviceManagementService service = StandardDeviceManagementService.getInstance();
        assertNotNull(service);

        service.initialize(mockContext, "localhost", "testToken");

        service.registerAppInstallUninstallCallback(callback);
        List<IAppInstallUninstallCallback> callbacklist = service.getAppInstallCallbacks("test.package");
        assertNotNull(callbacklist);
        assertTrue(callbacklist.contains(callback));

        service.unregisterAppInstallUninstallCallback(callback);
        callbacklist = service.getAppInstallCallbacks("test.package");
        assertEquals(0, callbacklist.size());
    }


    @Test
    public void GivenStandardDeviceManagementService_WhenRegisterAppInstallUninstallReceiver_ThenRegistered() {
        IAppInstallUninstallCallback callback = (context, intent) -> {
        };

        when(mockContext.getApplicationContext()).thenReturn(mockApplicationContext);
        when(mockPmHelper.getAgentId(any(), anyString(), anyString()))
                .thenReturn("b50f55a7-4e6f-43a1-b59a-095cf76f6efd");

        DeviceManagementService service = StandardDeviceManagementService.getInstance();
        assertNotNull(service);

        service.initialize(mockContext, "localhost", "testToken");
        service.setPackageManagerHelper(mockPmHelper);

        service.registerAppInstallUninstallReceiver("testTypeGun", callback);
        List<IAppInstallUninstallCallback> callbacklist = service.getAppInstallCallbacks("test.package");
        assertNotNull(callbacklist);
        assertTrue(callbacklist.contains(callback));

        service.unRegisterAppInstallUninstallReceiver("testTypeGun");
        callbacklist = service.getAppInstallCallbacks("test.package");
        assertEquals(0, callbacklist.size());
    }


    @Test
    public void GivenStandardDeviceManagementService_WhenRegisterAppInstallUninstallReceiverMultipleTimes_ThenRegistered() {
        IAppInstallUninstallCallback callback = (context, intent) -> {
        };

        IAppInstallUninstallCallback callback2 = (context, intent) -> {
        };

        IAppInstallUninstallCallback callback3 = (context, intent) -> {
        };

        when(mockContext.getApplicationContext()).thenReturn(mockApplicationContext);
        when(mockPmHelper.getAgentId(any(), anyString(), anyString()))
                .thenReturn("b50f55a7-4e6f-43a1-b59a-095cf76f6efd");

        DeviceManagementService service = StandardDeviceManagementService.getInstance();
        assertNotNull(service);

        service.initialize(mockContext, "localhost", "testToken");
        service.setPackageManagerHelper(mockPmHelper);

        service.registerAppInstallUninstallReceiver("testTypeGun", callback);
        service.registerAppInstallUninstallReceiver("testTypeGun2", callback2);
        service.registerAppInstallUninstallCallback(callback3);
        List<IAppInstallUninstallCallback> callbacklist = service.getAppInstallCallbacks("test.package");
        assertNotNull(callbacklist);
        assertTrue(callbacklist.contains(callback));
        assertTrue(callbacklist.contains(callback2));
        assertTrue(callbacklist.contains(callback3));

        service.unRegisterAppInstallUninstallReceiver("testTypeGun");
        service.unRegisterAppInstallUninstallReceiver("testTypeGun2");
        service.unregisterAppInstallUninstallCallback(callback3);
        callbacklist = service.getAppInstallCallbacks("test.package");
        assertEquals(0, callbacklist.size());
    }
}
