package com.hp.jetadvantage.link.device.services.standard;

import static com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService.E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN;
import static com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService.E2SERVICE_USB_ACCESSORIES_GUN;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeServiceChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelPayloadValue;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestOperationCallbackServiceMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestRegistrationCallbackServiceMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.RegistrationKind;
import com.hp.ext.service.usbAccessories.UsbAccessoriesAgentRegistrationRecord;
import com.hp.ext.service.usbAccessories.UsbCallback;
import com.hp.ext.service.usbAccessories.UsbCallbackEnvelope;
import com.hp.ext.service.usbAccessories.UsbRegistrationPayload;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2AsyncIoCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ChannelSetupCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.ws.websocket.AppChannelMessage;
import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceAccessoryServiceUnitTest extends StandardDeviceUnitTest {

    String channelId = "2777402e-86d1-4a4e-8aed-79c77ccbccdf";
    String resourceId = "12a16940-9fed-46ea-84f8-debd75006cc2";
    String typeGUN = new UsbRegistrationPayload().getTypeGUN();
    String registrationRecordTypeGun = new UsbAccessoriesAgentRegistrationRecord().getTypeGUN();
    String packageId = "com.hp.workpath.sample.accessoryservicesample";

    @After
    public void tearDown() {
        AppChannelCallbackRegistry.clear();
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService();
        assertNotNull(accessoryService);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsAgentRegisteredCalled_ThenTrueReturned() {
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        when(mockDeviceManagementService.getAgentId(packageId, registrationRecordTypeGun)).thenReturn(UUID.randomUUID().toString());
        assertTrue("isAgentRegistered", accessoryService.isAgentRegistered(packageId));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsAgentRegisteredCalled_ThenFalseReturned() {
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        when(mockDeviceManagementService.getAgentId(packageId, registrationRecordTypeGun)).thenReturn(null);
        assertFalse("isAgentRegistered", accessoryService.isAgentRegistered(packageId));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSolutionRegisteredCalled_ThenTrueReturned() {
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        when(mockDeviceManagementService.getSolutionId(packageId)).thenReturn(UUID.randomUUID().toString());
        assertTrue("isSolutionRegistered", accessoryService.isSolutionRegistered(packageId));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSolutionRegisteredCalled_ThenFalseReturned() {
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        when(mockDeviceManagementService.getSolutionId(packageId)).thenReturn(null);
        assertFalse("isSolutionRegistered", accessoryService.isSolutionRegistered(packageId));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        // Error case test : when device not connected
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertNotNull(accessoryService);

        try {
            accessoryService.isSupported();
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndGetCapabilities_ThenTrueShouldBeReturned() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader())
                , "usbHost/GET_cdm_capabilities.json");
        String e2CapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "usbHost/GET_ext_capabilities.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(e2CapabilityResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertTrue(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndCapabilityIsEmptyLocation_ThenFalseShouldBeReturned() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader())
                , "usbHost/GET_cdm_capabilities_empty_location.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndCapabilityNoPort_ThenFalseShouldBeReturned() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader())
                , "usbHost/GET_cdm_capabilities_no_port.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_And401Unauthorized_ThenFalseShouldBeReturned() throws IOException {

        CDMResponse<String> cdmResponse = CDMResponse.create(401, "");
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndNoLinksInExtCall_ThenFalseShouldBeReturned() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader())
                , "usbHost/GET_cdm_capabilities.json");
        String e2CapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "usbHost/GET_ext_capabilities_no_links.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(e2CapabilityResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndExtReturnNull_ThenFalseShouldBeReturned() throws IOException {
        String cdmCapabilityResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader())
                , "usbHost/GET_cdm_capabilities.json");

        CDMResponse<String> cdmResponse = CDMResponse.create(200, cdmCapabilityResponse);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isSupported());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsReadyCalled_AndConfiguration_ThenTrueShouldBeReturned() throws IOException {

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_cdm_configuration.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, response);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isReady
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertTrue(accessoryService.isReady());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsReadyCalled_AndConfigurationDisabled_ThenFalseShouldBeReturned() throws IOException {

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_cdm_configuration_disabled.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, response);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isReady
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isReady());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsReadyCalled_And400BadRequest_ThenFalseShouldBeReturned() throws IOException {

        CDMResponse<String> cdmResponse = CDMResponse.create(400, "");
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isReady
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(accessoryService.isReady());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoriesCalled_AndOwnedHPCardReader_ThenAccessoryShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_owned.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getOwnedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessories accessories = accessoryService.getAccessories(testPackageName);
        com.hp.ext.service.usbAccessories.Accessory accessory = accessories.getMembers().get(0);

        assertEquals("rkOwned", accessory.getRegistration().getValue());
        assertEquals(Integer.valueOf(69), accessory.getProductId().getValue());
        assertEquals(Integer.valueOf(1008), accessory.getVendorId().getValue());
        assertEquals("", accessory.getSerialNumber().getValue());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoriesCalled_AndOwnedHPCardReader_ThenAccessoryCountShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_owned.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getOwnedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessories accessories = accessoryService.getAccessories(testPackageName);
        assertEquals(Long.valueOf(1), accessories.getTotalCount());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoriesCalled_AndSharedHPCardReader_ThenAccessoryShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_shared.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getSharedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessories accessories = accessoryService.getAccessories(testPackageName);
        com.hp.ext.service.usbAccessories.Accessory accessory = accessories.getMembers().get(0);

        assertEquals("rkShared", accessory.getRegistration().getValue());
        assertEquals(Integer.valueOf(69), accessory.getProductId().getValue());
        assertEquals(Integer.valueOf(1008), accessory.getVendorId().getValue());
        assertEquals("", accessory.getSerialNumber().getValue());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoriesCalled_AndSharedHPCardReader_ThenAccessoryCountShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_shared.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getSharedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessories accessories = accessoryService.getAccessories(testPackageName);
        assertEquals(Long.valueOf(1), accessories.getTotalCount());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoriesCalled_AndMultiCardReader_ThenAccessoryShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_mixed.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call enumerateAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessories accessories = accessoryService.getAccessories(testPackageName);
        com.hp.ext.service.usbAccessories.Accessory accessory = accessories.getMembers().get(0);

        assertEquals("rkOwned", accessory.getRegistration().getValue());
        assertEquals(Integer.valueOf(69), accessory.getProductId().getValue());
        assertEquals(Integer.valueOf(1008), accessory.getVendorId().getValue());
        assertEquals("", accessory.getSerialNumber().getValue());

        accessory = accessories.getMembers().get(1);
        assertEquals("rkShared", accessory.getRegistration().getValue());
        assertEquals(Integer.valueOf(272), accessory.getProductId().getValue());
        assertEquals(Integer.valueOf(7590), accessory.getVendorId().getValue());
        assertEquals("1", accessory.getSerialNumber().getValue());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetAccessoryCalled_ThenAccessoryShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "usbAccessories" + "/GET_ext_usbAccessories_v1_accessories_accessoryId.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getOwnedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        Accessory accessory = accessoryService.getAccessory(testPackageName, resourceId);

        assertEquals("accessoryID", "4e596c92-b9b5-49fe-bbb7-96a986f210f5", accessory.getAccessoryID().toString());
        assertEquals("manufacturerName", "Acme, Inc.", accessory.getManufacturerName().getValue());
        assertEquals("productName", "X6001", accessory.getProductName().getValue());
        assertEquals("serialNumber", "NULL", accessory.getSerialNumber().getValue());
        assertEquals("productId", 69, accessory.getProductId().getValue().intValue());
        assertEquals("vendorId", 1008, accessory.getVendorId().getValue().intValue());
        assertEquals("registration", RegistrationKind.RkOwned, accessory.getRegistration());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterNotificationCallbackCalledAndUsbAttached_ThenCallbackCalled() {
        String usbAttachNotification = "{" +
                "    \"usbAttached\": {" +
                "       \"resourceId\": \"" + resourceId + "\"," +
                "       \"resourceLink\": \"/ext/usbAccessories/v1/accessories/" + resourceId + "\"}" +
                "}";

        registerAndTestCallback(channelId, resourceId, typeGUN, packageId, usbAttachNotification, true, 1);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterNotificationCallbackCalledAndUsbDeAttached_ThenCallbackCalled() {
        String usbDetachNotification = "{" +
                "    \"usbDetached\": {" +
                "       \"resourceId\": \"" + resourceId + "\"," +
                "       \"resourceLink\": \"/ext/usbAccessories/v1/accessories/" + resourceId + "\"}" +
                "}";

        registerAndTestCallback(channelId, resourceId, typeGUN, packageId, usbDetachNotification, false, 1);
    }

    //TODO : Disable this test case for now; revisit later if needed
    //  The payload setup for the async I/O callback has been removed starting from E2 beta10 rev2.
    //  Instead, the service channel setup callback is used to notify USB registration (attach/detach).
    //  In the case of the service channel, we cannot process the notification without a setup message.
    //@Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterNotificationCallbackCalledAndUsbDeAttachedWithoutSetup_ThenCallbackCalled() {
        String usbDetachNotification = "{" +
                "    \"usbDetached\": {" +
                "       \"resourceId\": \"" + resourceId + "\"," +
                "       \"resourceLink\": \"/ext/usbAccessories/v1/accessories/" + resourceId + "\"}" +
                "}";

        registerAndTestCallback(channelId, resourceId, typeGUN, "", usbDetachNotification, false, 1);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterNotificationCallbackCalledAndEmptyNotification_ThenCallbackShouldNotBeCalled() {
        String emptyNotification = "{}";

        registerAndTestCallback(channelId, resourceId, typeGUN, "", emptyNotification, false, 0);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterNotificationCallbackCalledAndInvalidNotification_ThenCallbackShouldNotBeCalled() {
        String emptyNotification = "{\"invalidUsbDetection\": {}}";

        registerAndTestCallback(channelId, resourceId, typeGUN, "", emptyNotification, false, 0);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterUsbRegistrationChannelSetupCallbackCalledAndSetup_ThenCallbackCalled() {
        String usbAttachNotification = "{" +
                "    \"usbAttached\": {" +
                "       \"resourceId\": \"" + resourceId + "\"," +
                "       \"resourceLink\": \"/ext/usbAccessories/v1/accessories/" + resourceId + "\"}" +
                "}";

        registerAndTestCallback(channelId, resourceId, typeGUN, packageId, usbAttachNotification, true, 1);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterUsbRegistrationChannelSetupCallbackAndSetupReceived_ThenCallbackCalled() {
        registerAndTestSetupCallback(channelId, packageId, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN, 1, true);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterUsbRegistrationChannelSetupCallbackAndMultipleSetupReceived_ThenCallbackCalled() {
        registerAndTestSetupCallback(channelId, packageId, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN, 10, true);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterUsbRegistrationChannelSetupCallbackAndSetupReceivedForUnexpectedTypeGun_ThenCallbackCalled() {
        registerAndTestSetupCallback(channelId, packageId, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN, 0, false);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterAppInstallUninstallCallback_ThenTheCallbackShouldBeRegistered() {
        IAppInstallUninstallCallback callback = (context, intent) -> {
            // Do nothing
        };
        // Register callback
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        accessoryService.registerAppInstallUninstallCallback(callback);

        verify(mockDeviceManagementService).registerAppInstallUninstallReceiver(registrationRecordTypeGun, callback);

        accessoryService.unRegisterAppInstallUninstallCallback();

        verify(mockDeviceManagementService).unRegisterAppInstallUninstallReceiver(registrationRecordTypeGun);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterOperationCallbackAndUsbCallbackMessageReceived_ThenCallbackCalled() {
        String data = "SGVsbG8gQWNjZXNzb3J5IFRlc3Q=";
        String usbCallback = "\"usbCallback\": {" +
                "                            \"hidRead\": {" +
                "                                \"data\": \"" + data + "\"," +
                "                                \"hidReadSequence\": 0" +
                "             }}";

        registerOperationCallbackAndTestCallback(channelId, packageId, resourceId, usbCallback, 1, true,
                data);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterOperationCallbackAndInvalidJsonMessageReceived_ThenCallbackNotCalled() {
        String data = "SGVsbG8gQWNjZXNzb3J5IFRlc3Q=";
        String usbCallback = "{\"usbCallback\": {" +
                "                            \"hidRead\": {" +
                "                                \"data\": \"" + data + "\"," +
                "                                \"hidReadSequence\": 0";

        registerOperationCallbackAndTestCallback(channelId, packageId, resourceId, usbCallback, 0, false, null);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterOperationCallbackAndEmptyUsbCallbackReceived_ThenCallbackNotCalled() {
        String usbCallback = "{}";

        registerOperationCallbackAndTestCallback(channelId, packageId, resourceId, usbCallback, 0, false, null);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenRegisterOperationCallbackAndNotHidReadMessageReceived_ThenCallbackCalled() {
        String data = "SGVsbG8gQWNjZXNzb3J5IFRlc3Q=";
        String usbCallback = "\"usbCallback\": {" +
                "                            \"usbRead\": {" +
                "                                \"data\": \"" + data + "\"" +
                "             }}";

        registerOperationCallbackAndTestCallback(channelId, packageId, resourceId, usbCallback, 1, false,
                null);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenAllCallbackRegistered_ThenAllCallbackShouldWork() {
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        AtomicInteger usbRegistrationPayloadCallbackCount = new AtomicInteger(0);
        AtomicInteger asyncIoCallbackCount = new AtomicInteger(0);
        AtomicInteger channelSetupCallbackCount = new AtomicInteger(0);
        CountDownLatch latchForUsbRegistrationEvent = new CountDownLatch(1);
        CountDownLatch latchForUsbOperation = new CountDownLatch(1);

        // 1.1 Register UsbRegistrationPayloadCallback
        IE2PayloadCallback<UsbRegistrationPayload> usbRegistrationPayloadCallback = (appPackageId, notification) -> {
            usbRegistrationPayloadCallbackCount.incrementAndGet();
            assertEquals("UsbRegistrationPayload packageId", packageId, appPackageId);
            if (notification.isUsbAttached()) {
                assertEquals("UsbRegistrationPayload resourceId", resourceId,
                        notification.getUsbAttached().getResourceId().toString());
            } else {
                assertEquals("UsbRegistrationPayload resourceId", resourceId,
                        notification.getUsbDetached().getResourceId().toString());
            }
            assertEquals("typeGUN", typeGUN, notification.getTypeGUN());
            latchForUsbRegistrationEvent.countDown();
        };
        accessoryService.registerNotificationCallback(usbRegistrationPayloadCallback);

        // 1.2 Register UsbRegistrationChannelSetupCallback
        IE2ChannelSetupCallback channelSetupCallback = (appPackageId) -> {
            channelSetupCallbackCount.incrementAndGet();
            // Verify received notification
            assertEquals("channelSetupCallback packageId", packageId, appPackageId);
        };
        accessoryService.registerUsbRegistrationChannelSetupCallback(channelSetupCallback);

        // 1.3 Register AsyncIOCallback
        IE2AsyncIoCallback<UsbCallback> asyncIoCallback = (appPackageId, contextId, notification) -> {
            asyncIoCallbackCount.incrementAndGet();
            assertEquals("asyncIoCallback appPackageId", packageId, appPackageId);
            assertEquals("asyncIoCallback resourceId", resourceId, contextId);
            latchForUsbOperation.countDown();
        };
        accessoryService.registerOperationCallback(asyncIoCallback);


        // 2.1 Test for valid channel setup message
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                E2SERVICE_USB_ACCESSORIES_GUN, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN));

        assertEquals("2.1 channelSetupCallbackCount", 1, channelSetupCallbackCount.get());
        assertEquals("2.1 asyncIoCallbackCount", 0, asyncIoCallbackCount.get());
        assertEquals("2.1 usbRegistrationPayloadCallbackCount", 0, usbRegistrationPayloadCallbackCount.get());
        channelSetupCallbackCount.set(0);


        // 2.2 Test for valid usbRegistrationPayload notification
        String usbAttachNotification =
                "    {\"usbAttached\": {" +
                        "       \"resourceId\": \"" + resourceId + "\"," +
                        "       \"resourceLink\": \"/ext/usbAccessories/v1/accessories/" + resourceId + "\"}}";
        handler.onReceived(0, makeTestRegistrationCallbackServiceMessage(channelId, "7b0a9e6b-340b-4065-bfa6-50fdb1c4cdde", usbAttachNotification));

        try {
            latchForUsbRegistrationEvent.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Exception occurred while waiting for callback: " + e.getMessage());
        }
        assertEquals("2.2 usbRegistrationPayloadCallbackCount", 1, usbRegistrationPayloadCallbackCount.get());
        assertEquals("2.2 channelSetupCallbackCount", 0, channelSetupCallbackCount.get());
        assertEquals("2.2 asyncIoCallbackCount", 0, asyncIoCallbackCount.get());
        usbRegistrationPayloadCallbackCount.set(0);

        // 2.3 Test for valid asyncIoCallback notification
        String usbCallback = "\"usbCallback\": {" +
                "                            \"hidRead\": {" +
                "                                \"data\": \"SGVsbG8gQWNjZXNzb3J5IFRlc3Q=\"," +
                "                                \"hidReadSequence\": 0" +
                "             }}";
        handler.onReceived(0, makeTestOperationCallbackServiceMessage(channelId, "8b0a9e6b-440b-4065-bfa6-60fdb1c4cdde", usbCallback));

        try {
            latchForUsbOperation.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Exception occurred while waiting for callback: " + e.getMessage());
        }
        assertEquals("2.3 asyncIoCallbackCount", 1, asyncIoCallbackCount.get());
        assertEquals("2.3 usbRegistrationPayloadCallbackCount", 0, usbRegistrationPayloadCallbackCount.get());
        assertEquals("2.3 channelSetupCallbackCount", 0, channelSetupCallbackCount.get());
        asyncIoCallbackCount.set(0);

        accessoryService.unregisterOperationCallback();
        accessoryService.unRegisterNotificationCallback();
        accessoryService.unRegisterAppChannelSetupCallback();
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStartAsyncReadingCalledForOwned_ThenSetReportReadingActive() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = true;
        boolean result = accessoryService.startAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertTrue("result", result);
        verify(mockDeviceManagementService).getSolutionToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStartAsyncReadingCalledWithNullParam_ThenReturnFalse() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        boolean result = accessoryService.startAsyncReading(testPackageName, null, resourceId, true);
        assertFalse("result", result);
        verify(mockDeviceManagementService, never()).getSolutionToken(testPackageName);

        result = accessoryService.startAsyncReading("", UUID.randomUUID(), resourceId, true);
        assertFalse("result", result);
        verify(mockDeviceManagementService, never()).getSolutionToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStartAsyncReadingCalledForShared_ThenSetReportReadingActive() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = false;
        boolean result = accessoryService.startAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertTrue("result", result);
        verify(mockDeviceManagementService).getUiContextToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStartAsyncReadingCalledForShared_ThenReturnFalseForFalseResponse() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}_readingActiveFalse.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = false;
        boolean result = accessoryService.startAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertFalse("result", result);
        verify(mockDeviceManagementService).getUiContextToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStopAsyncReadingCalledForOwned_ThenReturnTrue() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}_readingActiveFalse.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = true;
        boolean result = accessoryService.stopAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertTrue("result", result);
        verify(mockDeviceManagementService).getSolutionToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStopAsyncReadingCalledForOwned_ThenReturnFalseIfActiveResponse() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = true;
        boolean result = accessoryService.stopAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertFalse("result", result);
        verify(mockDeviceManagementService).getSolutionToken(testPackageName);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenStopAsyncReadingCalledForShared_ThenReturnTrue() {
        StandardDeviceAccessoryService accessoryService = new StandardDeviceAccessoryService(mockDeviceManagementService);
        setupMockServices("usbAccessories/PATCH_ext_usbAccessories_v1_accessories_{accessoryId}_hid_{openHidid}_readingActiveFalse.json");
        UUID openHidId = UUID.randomUUID();
        boolean isOwned = false;
        boolean result = accessoryService.stopAsyncReading(testPackageName, openHidId, resourceId, isOwned);
        assertTrue("result", result);
        verify(mockDeviceManagementService).getUiContextToken(testPackageName);
    }

    /*** AccessoryCallbackRegistry tests ***/

    @Test
    public void GivenAccessoryCallbackRegistry_WhenCreateRegistrationResponseCalled_ThenReturnResponse() throws IOException {
        AccessoryCallbackRegistry registry = new AccessoryCallbackRegistry(mockDeviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);
        String jsonString = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbAccessories/channelMessage_service_registration_usbRegistrationPayload.json");
        final AppChannelMessage channelMessage = StandardJsonParser.INSTANCE.fromJson(jsonString, AppChannelMessage.class);
        final AppChannelService serviceMessage = channelMessage.getChannelMessage().getMessage().getService();

        AppChannelServiceResponse responseMessage = registry.createRegistrationResponse(serviceMessage);

        assertNotNull("responseMessage", responseMessage);
        assertEquals("responseMessage status", 200, responseMessage.getHttpStatus());
        assertEquals("getServiceCallId", "6b0a9e6b-240b-4065-bfa6-40fdb1c4cdde", responseMessage.getServiceCallId());
    }

    @Test
    public void GivenAccessoryCallbackRegistry_WhenCreateOperationCallbackResponseCalled_ThenReturnResponse() throws IOException {
        AccessoryCallbackRegistry registry = new AccessoryCallbackRegistry(mockDeviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);
        String jsonString = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbAccessories/channelMessage_service_operationCallback_usbCallbackEnvelope.json");
        final AppChannelMessage channelMessage = StandardJsonParser.INSTANCE.fromJson(jsonString, AppChannelMessage.class);
        final AppChannelService serviceMessage = channelMessage.getChannelMessage().getMessage().getService();

        AppChannelServiceResponse responseMessage = registry.createOperationCallbackResponse(serviceMessage);

        assertNotNull("responseMessage", responseMessage);
        assertEquals("responseMessage status", 200, responseMessage.getHttpStatus());
        assertEquals("responseMessage ServiceCallId", "de081437-e2b7-40ea-8107-f99866022f5e", responseMessage.getServiceCallId());
        assertNotNull("responseMessage body", responseMessage.getResponseBody());
    }

    @Test
    public void GivenAccessoryCallbackRegistry_WhenHandleUsbRegistrationCallback_ThenReturnResonse() throws IOException {
        AccessoryCallbackRegistry registry = new AccessoryCallbackRegistry(mockDeviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);

        // Load test JSON resource
        String jsonString = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbAccessories/channelMessage_service_registration_usbRegistrationPayload.json");
        final AppChannelMessage channelMessage = StandardJsonParser.INSTANCE.fromJson(jsonString, AppChannelMessage.class);
        final AppChannelService serviceMessage = channelMessage.getChannelMessage().getMessage().getService();

        // Create test callback with verification
        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<String> receivedPackageId = new AtomicReference<>();
        AtomicReference<UsbRegistrationPayload> receivedPayload = new AtomicReference<>();

        IE2PayloadCallback<UsbRegistrationPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            receivedPackageId.set(appPackageId);
            receivedPayload.set(notification);
        };

        // Call handleUsbRegistrationCallback
        AppChannelServiceResponse response = registry.handleUsbRegistrationCallback(callback, packageId, serviceMessage);

        // Verify response
        assertNotNull("response should not be null", response);
        assertEquals("response status should be 200", 200, response.getHttpStatus());
        assertEquals("response serviceCallId should match", "6b0a9e6b-240b-4065-bfa6-40fdb1c4cdde", response.getServiceCallId());

        // Verify callback was invoked
        assertEquals("callback should be invoked once", 1, callbackCount.get());
        assertEquals("packageId should match", packageId, receivedPackageId.get());

        // Verify payload
        assertNotNull("received payload should not be null", receivedPayload.get());
        assertTrue("payload should indicate USB attached", receivedPayload.get().isUsbAttached());
        assertEquals("resourceId should match", "5add062b-2f22-48f7-84c4-6359f422dda1",
                receivedPayload.get().getUsbAttached().getResourceId().toString());
        assertEquals("typeGUN should match", typeGUN, receivedPayload.get().getTypeGUN());
    }

    @Test
    public void GivenAccessoryCallbackRegistry_WhenHandleOperationCallbackMessage_ThenReturnResponse() throws IOException {
        AccessoryCallbackRegistry registry = new AccessoryCallbackRegistry(mockDeviceManagementService, E2SERVICE_USB_ACCESSORIES_GUN);

        // Load test JSON resource
        String jsonString = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbAccessories/channelMessage_service_operationCallback_usbCallbackEnvelope.json");
        final AppChannelMessage channelMessage = StandardJsonParser.INSTANCE.fromJson(jsonString, AppChannelMessage.class);
        final AppChannelService serviceMessage = channelMessage.getChannelMessage().getMessage().getService();

        // Create test callback with verification
        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<String> receivedAppPackageName = new AtomicReference<>();
        AtomicReference<String> receivedAccessoryId = new AtomicReference<>();
        AtomicReference<UsbCallback> receivedUsbCallback = new AtomicReference<>();

        IE2AsyncIoCallback<UsbCallback> callback = (appPackageName, accessoryId, notification) -> {
            callbackCount.incrementAndGet();
            receivedAppPackageName.set(appPackageName);
            receivedAccessoryId.set(accessoryId);
            receivedUsbCallback.set(notification);
        };

        // Call handleOperationCallbackMessage
        AppChannelServiceResponse response = registry.handleOperationCallbackMessage(callback, serviceMessage);

        // Verify response
        assertNotNull("response should not be null", response);
        assertEquals("response status should be 200", 200, response.getHttpStatus());
        assertEquals("response serviceCallId should match", "de081437-e2b7-40ea-8107-f99866022f5e", response.getServiceCallId());
        assertNotNull("response body should not be null", response.getResponseBody());

        // Verify callback was invoked
        assertEquals("callback should be invoked once", 1, callbackCount.get());
        assertEquals("appPackageName should match", "com.hp.workpath.sample.accessorysample", receivedAppPackageName.get());
        assertEquals("accessoryId should match", "56a88191-b64b-4cff-9bf2-b7037a8d52df", receivedAccessoryId.get());

        // Verify UsbCallback payload
        assertNotNull("received UsbCallback should not be null", receivedUsbCallback.get());
        assertNotNull("hidRead should not be null", receivedUsbCallback.get().getHidRead());
        assertEquals("hidReadSequence should match", 1, receivedUsbCallback.get().getHidRead().getHidReadSequence().intValue());
        assertNotNull("hidRead data should not be null", receivedUsbCallback.get().getHidRead().getData());
    }

    private void setupMockServices(String e2JsonResponseFileName) {
        try {
            //define mockDeviceManagementService
            when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
            when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
            when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

            //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
            String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), e2JsonResponseFileName);
            when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
            ResourceFacadeHelper.setHttpClient(mockHttpClient);
        } catch (Exception e) {
            fail("fail to setupMockServices");
        }
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/usbAccessories/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link accessoriesAgentsLink = new Link();
        accessoriesAgentsLink.setHref("/ext/usbAccessories/v1/usbAccessoriesAgents");
        accessoriesAgentsLink.setRel("usbAccessoriesAgents");
        links.add(accessoriesAgentsLink);

        Link accessoriesLink = new Link();
        accessoriesLink.setHref("/ext/usbAccessories/v1/accessories");
        accessoriesLink.setRel("accessories");
        links.add(accessoriesLink);
        return links;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl appServiceMetadata = new ServiceMetadataImpl();
        appServiceMetadata.setDescription("Usb Accessories Service Client Tests Discovery Tree");
        appServiceMetadata.setServiceGun("com.hp.ext.service.usbAccessories.version.1");
        appServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(appServiceMetadata);
        return serviceMetadatas;
    }

    private void registerAndTestCallback(String channelId, String resourceId, String typeGUN, String packageId,
                                         String usbNotification, boolean isAttached, int expectedCallbackCnt) {
        AtomicInteger callbackCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(expectedCallbackCnt);

        // Define test callback
        IE2PayloadCallback<UsbRegistrationPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            latch.countDown();

            // Verify received notification
            assertEquals("packageId", packageId, appPackageId);
            if (isAttached) {
                assertTrue("isUsbAttached", notification.isUsbAttached());
                assertEquals("resourceId", resourceId, notification.getUsbAttached().getResourceId().toString());
            } else {
                assertTrue("isUsbDetached", notification.isUsbDetached());
                assertEquals("resourceId", resourceId, notification.getUsbDetached().getResourceId().toString());
            }
            assertEquals("typeGUN", typeGUN, notification.getTypeGUN());
        };

        // Register callback
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        accessoryService.registerNotificationCallback(callback);

        // Test for valid channel service message for notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!StringUtility.isEmpty(packageId)) {
            handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                    E2SERVICE_USB_ACCESSORIES_GUN, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN));
        }
        handler.onReceived(0, makeTestRegistrationCallbackServiceMessage(channelId,
                "5b0a9e6a-240c-4066-bfa6-40fdb1c4cdd1", usbNotification));

        // Wait for callback invocation with 1 second timeout
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Exception occurred while waiting for callback: " + e.getMessage());
        }

        // Verify callback is called
        assertEquals("callbackCount", expectedCallbackCnt, callbackCount.get());
        callbackCount.set(0);

        // Unregister callback
        accessoryService.unRegisterNotificationCallback();

        // Verify that unregistered callback should not be called on receiving message
        handler.onReceived(0, makeTestRegistrationCallbackServiceMessage(channelId,
                "5b0a9e6a-240c-4066-bfa6-40fdb1c4cdd2", usbNotification));
        assertEquals("callbackCount after unregistering", 0, callbackCount.get());
    }

    private void registerAndTestSetupCallback(String channelId, String packageId, String typeGUN,
                                              int expectedCallbackCnt, boolean isExpectedTypeGUN) {
        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<String> expectedPackageIdRef = new AtomicReference<>(packageId);

        // Define test callback
        IE2ChannelSetupCallback callback = (appPackageId) -> {
            callbackCount.incrementAndGet();
            // Verify received notification
            assertEquals("packageId", expectedPackageIdRef.get(), appPackageId);
        };

        // Register callback
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        accessoryService.registerUsbRegistrationChannelSetupCallback(callback);

        // Test for valid channel payload message for notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (isExpectedTypeGUN) {
            for (int i = 1; i <= expectedCallbackCnt; i++) {
                String newPackageId = packageId + i;
                expectedPackageIdRef.set(newPackageId);
                handler.onReceived(0, makeServiceChannelSetupMessage(UUID.randomUUID().toString(), newPackageId,
                        E2SERVICE_USB_ACCESSORIES_GUN, typeGUN));
            }
        } else {
            handler.onReceived(0, makeServiceChannelSetupMessage(UUID.randomUUID().toString(), packageId,
                    "com.hp.UnexpectedTestPayloadType", typeGUN));
        }

        // Verify callback is called
        assertEquals("callbackCount", expectedCallbackCnt, callbackCount.get());
        callbackCount.set(0);

        // Unregister callback
        accessoryService.unRegisterAppChannelSetupCallback();

        // Verify that unregistered callback should not be called on receiving message
        handler.onReceived(0, makeServiceChannelSetupMessage(UUID.randomUUID().toString(), packageId,
                E2SERVICE_USB_ACCESSORIES_GUN, typeGUN));
        assertEquals("callbackCount after unregistering", 0, callbackCount.get());
    }

    private void registerOperationCallbackAndTestCallback(String channelId, String packageId, String resourceId,
                                                          String usbCallback, int expectedCallbackCnt,
                                                          boolean isHidRead, String expectedData) {
        String usbCallbackEnvelopeTypeGun = new UsbCallbackEnvelope().getTypeGUN();
        AtomicInteger callbackCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(expectedCallbackCnt);

        IE2AsyncIoCallback<UsbCallback> callback = (appPackageId, contextId, notification) -> {
            callbackCount.incrementAndGet();
            latch.countDown();
            assertEquals("appPackageId", packageId, appPackageId);
            assertEquals("appPackageId", resourceId, contextId);
            assertEquals("isHidRead", isHidRead, notification.isHidRead());
            if (expectedData != null) {
                assertEquals("data", expectedData, notification.getHidRead().getData().getValue().toString());
            }
        };

        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);

        accessoryService.registerOperationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        if (!StringUtility.isEmpty(packageId)) {
            handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                    E2SERVICE_USB_ACCESSORIES_GUN, E2SERVICE_ACCESSORY_CLIENT_SERVICE_TARGET_GUN));
        }
        handler.onReceived(0, makeTestOperationCallbackServiceMessage(channelId,
                "6b0a9e6b-240b-4065-bfa6-40fdb1c4cdde", usbCallback));

        // Wait for callback invocation with 1 second timeout
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Exception occurred while waiting for callback: " + e.getMessage());
        }

        // Verify callback is called
        assertEquals("callbackCount", expectedCallbackCnt, callbackCount.get());
        callbackCount.set(0);

        // Unregister callback
        accessoryService.unregisterOperationCallback();

        // Verify that unregistered callback should not be called on receiving message
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, usbCallbackEnvelopeTypeGun, usbCallback));
        assertEquals("callbackCount after unregistering", 0, callbackCount.get());
    }
}
