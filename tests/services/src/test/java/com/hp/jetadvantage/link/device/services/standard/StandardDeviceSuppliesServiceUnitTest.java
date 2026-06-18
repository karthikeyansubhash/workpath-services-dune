package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.supply.Supply;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
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
public class StandardDeviceSuppliesServiceUnitTest extends StandardDeviceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private InjectedHttpClient mockHttpClient;

    @Test
    public void GivenStandardDeviceSuppliesService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSuppliesService deviceSuppliesService = new StandardDeviceSuppliesService();
        assertNotNull(deviceSuppliesService);
    }

    /**
     * Error case test : when device not connected while calling getSuppliesInfo.
     */
    @Test
    public void GivenStandardDeviceSuppliesService_WhenDeviceSuppliesCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        StandardDeviceSuppliesService deviceSuppliesService = new StandardDeviceSuppliesService(mockDeviceManagementService);
        assertNotNull(deviceSuppliesService);

        try {
            deviceSuppliesService.getSuppliesInfo(testPackageName);
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    /**
     * Happy case : get valid deviceSuppliesService from a connected device
     */
    @Test
    public void GivenStandardDeviceSuppliesService_WhenIsSupportedCalled_AndDeviceIsConnected_ThenTrueShouldBeReturn() throws IOException {
        String getSuppliesData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "supplies/GET_ext_getCapabilities.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSuppliesData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceSuppliesService deviceSuppliesService = new StandardDeviceSuppliesService(mockDeviceManagementService);
        assertNotNull(deviceSuppliesService);

        try {
            assertTrue(deviceSuppliesService.isSupported());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    /**
     * Happy case : get valid getSuppliesInfo from a connected device
     */
    @Test
    public void GivenStandardDeviceSuppliesService_WhenGetSuppliesCalled_AndDeviceIsConnected_ThenValidDeviceSuppliesDataShouldBeReturn() throws IOException {
        String getSuppliesData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "supplies/GET_ext_getSuppliesInfo.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSuppliesData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceSuppliesService deviceSuppliesService = new StandardDeviceSuppliesService(mockDeviceManagementService);
        assertNotNull(deviceSuppliesService);

        try {
            SuppliesInfo suppliesInfo = deviceSuppliesService.getSuppliesInfo(testPackageName);
            assertNotNull(suppliesInfo);
            assertEquals(22, suppliesInfo.getSuppliesList().size());
            assertEquals("com.hp.ext.service.supplies.version.1.type.suppliesInfo", suppliesInfo.getTypeGUN());

            Supply supply = suppliesInfo.getSuppliesList().get(4);
            assertNotNull(supply);

            assertEquals("160000", supply.getApproximatePagesRemaining().toString());

            // TODO <-- Need to implement after Beta8.
            if (supply.getCapacity() != null) {
                assertEquals("", supply.getCapacity().getUnit().toString());
                assertEquals("", supply.getCapacity().getCount().toString());
            }
            assertEquals(null, supply.getSupplyDescription());
            // TODO Need to implement after Beta8. -->

            assertEquals("stImageDrum", supply.getSupplyType().getValue());
            assertEquals("sccK", supply.getSupplyColor().getValue());
            assertEquals("W9074MC", supply.getProductNumber().getValue());
            assertEquals("191125-0014", supply.getSerialNumber());

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
        ServiceMetadataImpl suppliesServiceMetadata = new ServiceMetadataImpl();
        suppliesServiceMetadata.setDescription("Supplies service Tests Discovery Tree");
        suppliesServiceMetadata.setServiceGun("com.hp.ext.service.supplies.version.1");
        suppliesServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(suppliesServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/supplies/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/supplies/v1/suppliesAgents");
        link2.setRel("suppliesAgents");
        links.add(link2);

        return links;
    }
}
