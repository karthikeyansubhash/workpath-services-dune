package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDevicePrintJobServiceUnitTest extends StandardDeviceUnitTest {
    @Test
    public void GivenStandardDevicePrintJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        assertNotNull(devicePrintJobService);
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenIsSupportedCalled_ThenReturnTrue() throws IOException {
        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleCapabilities());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDevicePrintJobService printJobService = new StandardDevicePrintJobService(mockDeviceManagementService);

        boolean supported = printJobService.isSupported();
    }

    @Test
    public void GivenStandardDevicePrintJobService_WhenGetIppEndpointCalled_ThenReturnExpectedEndpoint() {
        // Arrange
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("192.168.1.1");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        // Act
        StandardDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService(mockDeviceManagementService);
        String result = devicePrintJobService.getIppEndpoint();

        // Assert
        assertEquals("http://192.168.1.1/ipp/print", result);
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();

        ServiceMetadataImpl printJobServiceMetadata = new ServiceMetadataImpl();
        printJobServiceMetadata.setDescription("Print Job Service Client Tests Discovery Tree");
        printJobServiceMetadata.setServiceGun("com.hp.ext.service.printJob.version.1");
        printJobServiceMetadata.setLinks(getPrintJobLinks());
        serviceMetadatas.add(printJobServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> getPrintJobLinks() {
        List<Link> links = new ArrayList<>();

        Link link = new Link();
        link.setHref("/ext/printJob/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/printJob/v1/printJobAgents");
        link2.setRel("printJobAgents");
        links.add(link2);

        return links;
    }

    private String getSampleCapabilities() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"apiVersion\": \"1.0\",\n" +
                "  \"description\": \"Using this service, client can initiate printing operations.\",\n" +
                "  \"implInfo\": [\n" +
                "    {\n" +
                "      \"typeGUN\": \"com.hp.ext.service.extensibility.version.1.impl.futuresmart.type.apiInformation\",\n" +
                "      \"value\": {\n" +
                "        \"apiMajorVersion\": 1,\n" +
                "        \"apiMinorVersion\": 0,\n" +
                "        \"releaseLevel\": \"Beta\",\n" +
                "        \"releaseSequence\": 3,\n" +
                "        \"toolVersions\": [\n" +
                "          \"dsl 2.12.5.202305061829\",\n" +
                "          \"umsgen 0.5.0\"\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"implVersion\": \"v=6.25.0.150+752ece1d-202404190608;r=6.25.0.150+752ece1d;d=202404190608;s=6.25;p=6D6664-0035\",\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/printJob/v1/capabilities\",\n" +
                "      \"rel\": \"self\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/printJob/v1/capabilities\",\n" +
                "      \"rel\": \"capabilities\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/printJob/v1/defaultOptions\",\n" +
                "      \"rel\": \"defaultOptions\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/printJob/v1/profile\",\n" +
                "      \"rel\": \"profile\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/printJob/v1/printJobAgents\",\n" +
                "      \"rel\": \"printJobAgents\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"serviceGun\": \"com.hp.ext.service.printJob.version.1\"\n" +
                "}";
    }
}
