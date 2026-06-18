package com.hp.jetadvantage.link.device.services.standard;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

import org.mockito.Mock;

public class StandardDeviceUnitTest {
    protected String testPackageName = Constants.TEST_PACKAGE_NAME;

    @Mock
    protected StandardWebsocketCallbackService mockWsCallbackService;
    @Mock
    protected StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    protected InjectedHttpClient mockHttpClient;
    @Mock
    protected CDMClient mockCDMClient;
}
