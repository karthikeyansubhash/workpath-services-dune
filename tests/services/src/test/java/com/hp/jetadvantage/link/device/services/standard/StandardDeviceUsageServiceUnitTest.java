package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.deviceUsage.LifetimeCounters;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceUsageServiceUnitTest extends StandardDeviceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private InjectedHttpClient mockHttpClient;

    @Test
    public void GivenStandardDeviceUsageService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService();
        assertNotNull(deviceUsageService);
    }


    /**
     * Happy case : get valid deviceUsageService from a connected device
     */
    @Test
    public void GivenStandardDeviceUsageService_WhenIsSupportedCalled_AndDeviceIsConnected_ThenTrueShouldBeReturn() throws IOException {
        String getUsageData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usage/GET_ext_getCapabilities.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getUsageData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService(mockDeviceManagementService);
        assertNotNull(deviceUsageService);

        try {
            assertTrue(deviceUsageService.isSupported());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    public LifetimeCounters getLifetimeCounter(String jsonPath) throws IOException {
        String getLifetimeCountersData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), jsonPath);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getLifetimeCountersData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService(mockDeviceManagementService);
        assertNotNull(deviceUsageService);

        LifetimeCounters lifetimeCounters = null;
        try {
            lifetimeCounters = deviceUsageService.getLifetimeCounters(testPackageName);
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
        return lifetimeCounters;
    }

    /**
     * Happy case : get valid getLifetimeCounters from a connected device
     */
    @Test
    public void GivenStandardDeviceUsageService_WhenGetLifetimeCountersCalled_AndDeviceIsConnected_ThenValidDeviceLifetimeCountersDataShouldBeReturn() throws IOException {
        try {
            LifetimeCounters lifetimeCounters = getLifetimeCounter("usage/GET_ext_getLifetimeCounters.json");
            assertNotNull(lifetimeCounters);
            assertEquals("11", lifetimeCounters.getJobUsage().getLastJobSequenceId().toString());
            assertEquals(2, lifetimeCounters.getPrintUsage().getSheetsByMediaSize().size());
            assertEquals("5", lifetimeCounters.getPrintUsage().getPrintSheets().getSimplex().getValue().toString());
            assertEquals("6", lifetimeCounters.getScanUsage().getTotalImages().getValue().toString());
            assertEquals("com.hp.ext.service.deviceUsage.version.1.type.lifetimeCounters", lifetimeCounters.getTypeGUN());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl deviceUsageServiceMetadata = new ServiceMetadataImpl();
        deviceUsageServiceMetadata.setDescription("DeviceUsage service Tests Discovery Tree");
        deviceUsageServiceMetadata.setServiceGun("com.hp.ext.service.deviceUsage.version.1");
        deviceUsageServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(deviceUsageServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/deviceUsage/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/deviceUsage/v1/deviceUsageAgents");
        link2.setRel("deviceUsageAgents");
        links.add(link2);

        return links;
    }
}
