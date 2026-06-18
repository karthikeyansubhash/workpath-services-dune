package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.authentication.AuthenticationAccessPoint_InitiateLogin;
import com.hp.ext.service.authentication.Session_ForceLogout;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceAccessServiceUnitTest extends StandardDeviceUnitTest {

    private static final String TEST_PACKAGE_NAME = "com.test.package.name";
    private static final String TEST_AGENT_ID = "11111111-1111-1111-9999-111111111111";

    @Test
    public void StandardDeviceAccessServiceUnitTest_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceAccessService accessService = new StandardDeviceAccessService();
        assertNotNull(accessService);
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenIsSupportedCalled_ThenReturnTrue() throws IOException {
        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleCapabilities());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceAccessService accessService = new StandardDeviceAccessService(mockDeviceManagementService);

        boolean supported = accessService.isSupported();
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenInitiatedSignInCalled_ThenReturnTrue() throws IOException {
        String extResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "access/POST_ext_initiated_signin.json");
        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createAuthTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(extResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceAuthenticationService authService = new StandardDeviceAuthenticationService(mockDeviceManagementService);

        AuthenticationAccessPoint_InitiateLogin initiateSignIn = authService.initiateSignIn(TEST_PACKAGE_NAME);

        assertEquals("/ext/authentication/v1/authenticationAccessPoints/b03bcde3-56b6-4a27-8ff6-a1b451c5a487/initiateLogin",
                initiateSignIn.getLinks().get(0).getHref());
    }

    @Test
    public void GivenStandardDeviceAccessService_WhenSignOutCalled_ThenReturnTrue() throws IOException {
        String extResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "access/POST_ext_signout.json");
        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createAuthTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(extResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceAuthenticationService authService = new StandardDeviceAuthenticationService(mockDeviceManagementService);

        Session_ForceLogout signOut = authService.signOut(TEST_PACKAGE_NAME);
        assertEquals("/ext/authentication/v1/session/forceLogout",
                signOut.getLinks().get(0).getHref());
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    public ServicesDiscoveryImpl createAuthTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getAuthServiceMetadatas());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();

        ServiceMetadataImpl accessServiceMetadata = new ServiceMetadataImpl();
        accessServiceMetadata.setDescription("Access Service Client Tests Discovery Tree");
        accessServiceMetadata.setServiceGun("com.hp.ext.service.security.version.1");
        accessServiceMetadata.setLinks(getLinks());
        serviceMetadatas.add(accessServiceMetadata);

        return serviceMetadatas;
    }

    private List<ServiceMetadataImpl> getAuthServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();

        ServiceMetadataImpl accessServiceMetadata = new ServiceMetadataImpl();
        accessServiceMetadata.setDescription("Access Service Client Tests Discovery Tree");
        accessServiceMetadata.setServiceGun("com.hp.ext.service.authentication.version.1");
        accessServiceMetadata.setLinks(getLinks());
        serviceMetadatas.add(accessServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> getLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();

        link.setHref("/ext/security/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/security/v1/securityAgents");
        link2.setRel("securityAgents");
        links.add(link2);

        Link link3 = new Link();
        link3.setHref("/ext/authentication/v1/capabilities");
        link3.setRel("capabilities");
        links.add(link3);

        Link link4 = new Link();
        link4.setHref("/ext/authentication/v1/authenticationAgents");
        link4.setRel("authenticationAgents");
        links.add(link4);

        Link link5 = new Link();
        link5.setHref("/ext/authentication/v1/authenticationAccessPoints?includeMembers");
        link5.setRel("authenticationAccessPoints");
        links.add(link5);

        Link link6 = new Link();
        link6.setHref("/ext/authentication/v1/session/forceLogout");
        link6.setRel("session");
        links.add(link6);

        return links;
    }

    private String getSampleCapabilities() {
        return "{\n" +
                "    \"$opMeta\": {\n" +
                "        \"contentFilter\": [\n" +
                "            \"*\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"apiVersion\": \"1.0\",\n" +
                "    \"description\": \"This service provides access to a limited set of device security related features and data.\",\n" +
                "    \"implInfo\": [\n" +
                "        {\n" +
                "            \"typeGUN\": \"com.hp.ext.service.extensibility.version.1.impl.futuresmart.type.apiInformation\",\n" +
                "            \"value\": {\n" +
                "                \"apiMajorVersion\": 1,\n" +
                "                \"apiMinorVersion\": 0,\n" +
                "                \"releaseLevel\": \"Beta\",\n" +
                "                \"releaseSequence\": 4,\n" +
                "                \"toolVersions\": [\n" +
                "                    \"dsl 2.17.0.202407181406\",\n" +
                "                    \"umsgen 0.6.0\"\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"implVersion\": \"v=6.30.0.79-202503050328;r=6.30.0.79;d=202503050328;s=6.30;p=6D6664-0055\",\n" +
                "    \"links\": [\n" +
                "        {\n" +
                "            \"href\": \"/ext/security/v1/capabilities\",\n" +
                "            \"rel\": \"self\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"href\": \"/ext/security/v1/capabilities\",\n" +
                "            \"rel\": \"capabilities\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"href\": \"/ext/security/v1/securityAgents\",\n" +
                "            \"rel\": \"securityAgents\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"serviceGun\": \"com.hp.ext.service.security.version.1\"\n" +
                "}";
    }
}
