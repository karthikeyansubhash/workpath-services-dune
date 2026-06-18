/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.adapter;

import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.EMPTY_SERIAL_ACCESSORY_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.FEATURE_REPORT_LENGTH;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.INPUT_REPORT_LENGTH;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.OPEN_HID_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.OUTPUT_REPORT_LENGTH;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.REPORT_READING_ACTIVE;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_ACCESSORY_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_MANUFACTURER_NAME;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_PID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_PRODUCT_NAME;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SERIAL_NUMBER;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_VID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.createSampleOpenHIDAccessory;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.getSample1AccessoriesFromDevice;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.getSampleSharedAccessoriesFromDevice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.RegistrationKind;
import com.hp.ext.service.usbAccessories.UsbData;
import com.hp.ext.service.usbAccessories.UsbTransferStatus;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.ext.types.usb.UsbString;
import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReport;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportType;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessoryService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class AccessoryDeviceAdapterTest {
    final static String VALID_PACKAGE_NAME = "com.hp.test.package";

    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private InjectedHttpClient mockHttpClient;
    @Mock
    private CDMClient mockCDMClient;
    @Mock
    private IDeviceAccessoryService mockDeviceAccessoryService;
    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        //setup tests
    }

    /// //////////////////////////// getHidAccessoryInfo() test ///////////////////////////////
    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetHidAccessoryInfoCalledForOwned_ThenHIDAccessoryInfoShouldBeReturned() {
        //create E2 Accessory
        Accessory e2UsbAccessory = new Accessory();
        e2UsbAccessory.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        e2UsbAccessory.setVendorId(new Unsigned16(VALID_VID));
        e2UsbAccessory.setProductId(new Unsigned16(VALID_PID));
        e2UsbAccessory.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        e2UsbAccessory.setManufacturerName(new UsbString(VALID_MANUFACTURER_NAME));
        e2UsbAccessory.setProductName(new UsbString(VALID_PRODUCT_NAME));
        e2UsbAccessory.setRegistration(RegistrationKind.RkOwned);

        when(mockDeviceAccessoryService.getAccessory(VALID_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(e2UsbAccessory);

        HIDAccessoryInfo hidAccessoryInfoResult = AccessoryDeviceAdapter
                .getHidAccessoryInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME, VALID_ACCESSORY_ID);
        assertEquals("ProductId", VALID_PID, hidAccessoryInfoResult.getProductId());
        assertEquals("VendorId", VALID_VID, hidAccessoryInfoResult.getVendorId());
        assertEquals("SerialNumber", VALID_SERIAL_NUMBER, hidAccessoryInfoResult.getSerialNumber());
        assertEquals("ManufacturerName", VALID_MANUFACTURER_NAME, hidAccessoryInfoResult.getManufacturer());
        assertEquals("Description", VALID_PRODUCT_NAME, hidAccessoryInfoResult.getDescription());
        assertEquals("RegistrationType", RegistrationType.OWNED, hidAccessoryInfoResult.getRegistrationType());
    }

    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetHidAccessoryInfoCalledForSharedWithNullSerial_ThenHIDAccessoryInfoShouldBeReturned() {
        //create E2 Accessory
        Accessory e2UsbAccessory = new Accessory();
        e2UsbAccessory.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        e2UsbAccessory.setVendorId(new Unsigned16(VALID_VID));
        e2UsbAccessory.setProductId(new Unsigned16(VALID_PID));
        e2UsbAccessory.setRegistration(RegistrationKind.RkShared);

        when(mockDeviceAccessoryService.getAccessory(VALID_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(e2UsbAccessory);

        HIDAccessoryInfo hidAccessoryInfoResult =
                AccessoryDeviceAdapter.getHidAccessoryInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                        VALID_ACCESSORY_ID);
        assertEquals("ProductId", VALID_PID, hidAccessoryInfoResult.getProductId());
        assertEquals("VendorId", VALID_VID, hidAccessoryInfoResult.getVendorId());
        assertEquals("SerialNumber", null, hidAccessoryInfoResult.getSerialNumber());
        assertEquals("ManufacturerName", null, hidAccessoryInfoResult.getManufacturer());
        assertEquals("Description", null, hidAccessoryInfoResult.getDescription());
        assertEquals("RegistrationType", RegistrationType.SHARED, hidAccessoryInfoResult.getRegistrationType());
    }

    /// //////////////////////////// getOwnedAccessoryId() test ///////////////////////////////
    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetOwnedAccessoryIdCalled_ThenAccessoryIdReturned() {

        Accessories accessories = getSample1AccessoriesFromDevice();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        String accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId", VALID_ACCESSORY_ID, accessoryId);
    }

    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetOwnedAccessoryIdCalledAndMultipleAccessoriesAttached_ThenAccessoryIdReturned() {

        Accessories accessories = getSample4AccessoriesFromDevice();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        String accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId", VALID_ACCESSORY_ID, accessoryId);
    }

    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetOwnedAccessoryIdCalledAndNoAccessoryAttached_ThenAccessoryIdReturned() {

        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(null);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        String accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId null accessories", null, accessoryId);

        Accessories accessories = new Accessories();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId with empty accessories", null, accessoryId);

        Accessory emptyAccessory = new Accessory();
        List<com.hp.ext.service.usbAccessories.Accessory> members = new ArrayList<>();
        members.add(emptyAccessory);
        accessories.setMembers(members);
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId with empty accessory member", null, accessoryId);
    }

    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetOwnedAccessoryIdCalledWithEmptySerialNumber_ThenAccessoryIdReturned() {
        //case 1 : find with null serialNumber, but the attached accessory has serial number
        Accessories accessories = getSample3AccessoriesFromDevice();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, null, "", "",
                RegistrationType.OWNED);
        String accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId", VALID_ACCESSORY_ID, accessoryId);

        //case 2 : find with null serialNumber, and the attached accessory has empty serial number
        accessories = getSample4AccessoriesFromDevice();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, null, "", "",
                RegistrationType.OWNED);
        accessoryId = AccessoryDeviceAdapter.getOwnedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId", EMPTY_SERIAL_ACCESSORY_ID, accessoryId);
    }

    /// //////////////////////////// getSharedAccessoryId() test ///////////////////////////////
    @Test
    public void GivenAccessoryDeviceAdapter_WhenGetSharedAccessoryIdCalled_ThenAccessoryIdReturned() {

        Accessories accessories = getSampleSharedAccessoriesFromDevice();
        when(mockDeviceAccessoryService.getAccessories(VALID_PACKAGE_NAME)).thenReturn(accessories);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        String accessoryId = AccessoryDeviceAdapter.getSharedAccessoryId(mockDeviceAccessoryService,
                VALID_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("accessoryId", VALID_ACCESSORY_ID, accessoryId);
    }

    /// //////////////////////////// isSupported() test ///////////////////////////////
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

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_cdm_capabilities.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, response);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertTrue(AccessoryDeviceAdapter.isSupported(accessoryService));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndCapabilityIsEmptyLocation_ThenFalseShouldBeReturned() throws IOException {

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_cdm_capabilities_empty_location.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, response);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(AccessoryDeviceAdapter.isSupported(accessoryService));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_AndCapabilityNoPort_ThenFalseShouldBeReturned() throws IOException {

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_cdm_capabilities_no_port.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, response);
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(AccessoryDeviceAdapter.isSupported(accessoryService));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenIsSupportedCalled_And401Unauthorized_ThenFalseShouldBeReturned() throws IOException {
        CDMResponse<String> cdmResponse = CDMResponse.create(401, "");
        when(mockCDMClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCDMClient);

        //create StandardDeviceAccessoryService and call isSupported
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        assertFalse(AccessoryDeviceAdapter.isSupported(accessoryService));
    }

    /// //////////////////////////// isReady() test ///////////////////////////////
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
        assertTrue(AccessoryDeviceAdapter.isReady(accessoryService));
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
        assertFalse(AccessoryDeviceAdapter.isReady(accessoryService));
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
        assertFalse(AccessoryDeviceAdapter.isReady(accessoryService));
    }

    /// //////////////////////////// getOwnedAccessories() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOwnedAccessoryCalled_AndHPCardReader_ThenAccessoryShouldBeReturned() throws IOException {

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
        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getOwnedAccessories(accessoryService,
                VALID_PACKAGE_NAME);

        AccessoryInfo accessoryInfo = accessories.get(0);
        assertEquals(accessoryInfo.getRegistrationType().toString(), "OWNED");
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getProductId(), 69);
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getVendorId(), 1008);
        assertNull(((HIDAccessoryInfo) accessoryInfo).getSerialNumber());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOwnedAccessoryCalled_AndSharedCardReader_ThenAccessoryShouldNotReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_shared.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getOwnedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getOwnedAccessories(accessoryService,
                VALID_PACKAGE_NAME);
        assertEquals(0, accessories.size());
    }

    /// //////////////////////////// getSharedAccessories() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetSharedAccessoriesCalled_AndHPCardReader_ThenAccessoryShouldBeReturned() throws IOException {

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
        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getSharedAccessories(accessoryService,
                VALID_PACKAGE_NAME);

        AccessoryInfo accessoryInfo = accessories.get(0);
        assertEquals(accessoryInfo.getRegistrationType().toString(), "SHARED");
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getProductId(), 69);
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getVendorId(), 1008);
        assertNull(((HIDAccessoryInfo) accessoryInfo).getSerialNumber());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetSharedAccessoriesCalled_AndOwnedCardReader_ThenAccessoryShouldNotReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "usbHost" +
                "/GET_ext_accessories_owned.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceAccessoryService and call getSharedAccessories
        StandardDeviceAccessoryService accessoryService =
                new StandardDeviceAccessoryService(mockDeviceManagementService);
        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.getSharedAccessories(accessoryService,
                VALID_PACKAGE_NAME);
        assertEquals(0, accessories.size());
    }

    /// //////////////////////////// enumerateAccessories() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenEnumerateAccessoryCalled_AndMultiCardReader_ThenAccessoryShouldBeReturned() throws IOException {

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
        ArrayList<AccessoryInfo> accessories = AccessoryDeviceAdapter.enumerateAccessories(accessoryService,
                VALID_PACKAGE_NAME);

        AccessoryInfo accessoryInfo = accessories.get(0);
        assertEquals(accessoryInfo.getRegistrationType().toString(), "OWNED");
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getProductId(), 69);
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getVendorId(), 1008);
        assertNull(((HIDAccessoryInfo) accessoryInfo).getSerialNumber());

        accessoryInfo = accessories.get(1);
        assertEquals(accessoryInfo.getRegistrationType().toString(), "SHARED");
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getProductId(), 272);
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getVendorId(), 7590);
        assertEquals(((HIDAccessoryInfo) accessoryInfo).getSerialNumber(), "1");
    }

    /// //////////////////////////// getOpenHidInfo() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOpenHidInfoCalled_ThenReturnHIDInfo() {
        OpenHIDAccessory openHidAccessory = createSampleOpenHIDAccessory();
        when(mockDeviceAccessoryService.getOpenHidAccessoryInfo(eq(VALID_PACKAGE_NAME), any(), eq(VALID_ACCESSORY_ID)))
                .thenReturn(openHidAccessory);

        HIDInfo hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID);
        assertEquals("HIDInfo.getFeatureReportLength", FEATURE_REPORT_LENGTH, hidInfo.getFeatureReportLength());
        assertEquals("HIDInfo.getInputReportLength", INPUT_REPORT_LENGTH, hidInfo.getInputReportLength());
        assertEquals("HIDInfo.getOutputReportLength", OUTPUT_REPORT_LENGTH, hidInfo.getOutputReportLength());
        assertEquals("HIDInfo.isReading", REPORT_READING_ACTIVE, hidInfo.isReading());
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOpenHidInfoCalledWithInvalidParams_ThenReturnNull() {
        HIDInfo hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(null, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID);
        assertNull("null deviceService", hidInfo);

        hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, null,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID);
        assertNull("null packageName", hidInfo);

        hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                null, VALID_ACCESSORY_ID);
        assertNull("null openHidId", hidInfo);

        hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), "");
        assertNull("empty accessoryId", hidInfo);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOpenHidInfoCalled_ThenReturnNullIfFailedToGetFromDevice() {
        when(mockDeviceAccessoryService.getOpenHidAccessoryInfo(anyString(), any(), anyString())).thenReturn(null);

        HIDInfo hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), "invalid");
        assertNull("hidInfo", hidInfo);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenGetOpenHidInfoCalled_ThenThrowNoExceptionIfEmptyFieldsReceivedFromDevice() {
        OpenHIDAccessory openHidAccessory = new OpenHIDAccessory();
        openHidAccessory.setOpenHIDAccessoryID(UUID.fromString(OPEN_HID_ID));

        when(mockDeviceAccessoryService.getOpenHidAccessoryInfo(eq(VALID_PACKAGE_NAME), any(), eq(VALID_ACCESSORY_ID)))
                .thenReturn(openHidAccessory);

        HIDInfo hidInfo = AccessoryDeviceAdapter.getOpenHidInfo(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID);
        assertEquals("HIDInfo.getFeatureReportLength", 0, hidInfo.getFeatureReportLength());
        assertEquals("HIDInfo.getInputReportLength", 0, hidInfo.getInputReportLength());
        assertEquals("HIDInfo.getOutputReportLength", 0, hidInfo.getOutputReportLength());
        assertEquals("HIDInfo.isReading", false, hidInfo.isReading());
    }

    /// //////////////////////////// readSyncReport() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenReadSyncReportCalled_ThenReturnReport() {
        String base64data = "YWNjZXNzb3J5IHRlc3Q=";
        OpenHIDAccessory_ReadReport e2Response = new OpenHIDAccessory_ReadReport();
        e2Response.setData(new UsbData(base64data));
        e2Response.setOperationId(UUID.randomUUID().toString());
        e2Response.setUsbTransferStatus(UsbTransferStatus.UtsOk);

        when(mockDeviceAccessoryService.readReport(eq(VALID_PACKAGE_NAME), any(), eq(VALID_ACCESSORY_ID), eq(true),
                any())).thenReturn(e2Response);

        HIDReport result = AccessoryDeviceAdapter.readSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, HIDReportType.FEATURE, true, (byte) 0);
        assertNotNull("result", result);
        assertEquals("getType", HIDReportType.FEATURE, result.getType());
        assertEquals("getData", base64data, Base64.getEncoder().encodeToString(result.getData()));
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenReadSyncReportCalledAndDeviceReturnNullReport_ThenReturnNull() {
        when(mockDeviceAccessoryService.readReport(anyString(), any(), anyString(), eq(true), any())).thenReturn(null);

        HIDReport result = AccessoryDeviceAdapter.readSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, HIDReportType.FEATURE, true, (byte) 0);
        assertNull("HIDReport", result);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenReadSyncReportCalledWithInvalidParams_ThenReturnNull() {
        HIDReport result = AccessoryDeviceAdapter.readSyncReport(null, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, HIDReportType.FEATURE, true, (byte) 0);
        assertNull("HIDReport 1", result);

        result = AccessoryDeviceAdapter.readSyncReport(mockDeviceAccessoryService, null,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, HIDReportType.FEATURE, true, (byte) 0);
        assertNull("HIDReport 2", result);

        result = AccessoryDeviceAdapter.readSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                null, VALID_ACCESSORY_ID, HIDReportType.FEATURE, true, (byte) 0);
        assertNull("HIDReport 3", result);

        result = AccessoryDeviceAdapter.readSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), null, HIDReportType.FEATURE, true, (byte) 0);
        assertNull("HIDReport 4", result);
    }

    /// //////////////////////////// writeSyncReport() test ///////////////////////////////
    @Test
    public void GivenStandardDeviceAccessoryService_WhenWriteSyncReportCalled_ThenReturnReport() {
        String base64data = "YWNjZXNzb3J5IHRlc3Q=";

        OpenHIDAccessory_WriteReport e2Response = new OpenHIDAccessory_WriteReport();
        e2Response.setBytesWritten(new Unsigned16(10));
        e2Response.setOperationId(UUID.randomUUID().toString());
        e2Response.setUsbTransferStatus(UsbTransferStatus.UtsOk);
        when(mockDeviceAccessoryService.writeReport(eq(VALID_PACKAGE_NAME), any(), eq(VALID_ACCESSORY_ID), eq(true),
                any())).thenReturn(e2Response);

        byte[] data = Base64.getDecoder().decode(base64data);
        HIDReport request = new HIDReport(HIDReportType.FEATURE, data);
        boolean result = AccessoryDeviceAdapter.writeSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, request, true, (byte) 0);
        assertTrue("result", result);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenWriteSyncReportCalledAndDeviceReturnNull_ThenReturnFalse() {
        String base64data = "YWNjZXNzb3J5IHRlc3Q=";

        when(mockDeviceAccessoryService.writeReport(eq(VALID_PACKAGE_NAME), any(), eq(VALID_ACCESSORY_ID), eq(true),
                any())).thenReturn(null);

        byte[] data = Base64.getDecoder().decode(base64data);
        HIDReport request = new HIDReport(HIDReportType.FEATURE, data);
        boolean result = AccessoryDeviceAdapter.writeSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, request, true, (byte) 0);
        assertFalse("result", result);
    }

    @Test
    public void GivenStandardDeviceAccessoryService_WhenWriteSyncReportCalledWithInvalidParam_ThenReturnFalse() {
        HIDReport request = new HIDReport(HIDReportType.FEATURE, null);
        boolean result = AccessoryDeviceAdapter.writeSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, request, true, (byte) 0);
        assertFalse("result 1", result);

        result = AccessoryDeviceAdapter.writeSyncReport(mockDeviceAccessoryService, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, null, true, (byte) 0);
        assertFalse("result 2", result);

        result = AccessoryDeviceAdapter.writeSyncReport(null, VALID_PACKAGE_NAME,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, request, true, (byte) 0);
        assertFalse("result 3", result);

        result = AccessoryDeviceAdapter.writeSyncReport(mockDeviceAccessoryService, null,
                UUID.fromString(OPEN_HID_ID), VALID_ACCESSORY_ID, request, true, (byte) 0);
        assertFalse("result 4", result);
    }

    private ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private Accessories getSample3AccessoriesFromDevice() {
        Accessories accessories = new Accessories();
        Accessory accessory1 = new Accessory();
        accessory1.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory1.setProductId(new Unsigned16(VALID_PID));
        accessory1.setVendorId(new Unsigned16(VALID_VID));
        accessory1.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory1.setRegistration(RegistrationKind.RkOwned);
        accessory1.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));

        Accessory accessory2 = new Accessory();
        UUID accessory2Id = UUID.randomUUID();
        accessory2.setAccessoryID(accessory2Id);
        accessory2.setProductId(new Unsigned16(12));
        accessory2.setVendorId(new Unsigned16(34));
        accessory2.setSerialNumber(new UsbString("abcdef"));
        accessory2.setRegistration(RegistrationKind.RkOwned);
        accessory2.setResourceId(accessory2Id);

        Accessory accessory3 = new Accessory();
        UUID accessory3Id = UUID.randomUUID();
        accessory3.setAccessoryID(accessory3Id);
        accessory3.setProductId(new Unsigned16(77));
        accessory3.setVendorId(new Unsigned16(88));
        accessory3.setRegistration(RegistrationKind.RkShared);
        accessory3.setResourceId(accessory3Id);

        List<com.hp.ext.service.usbAccessories.Accessory> members = new ArrayList<>();
        members.add(accessory3);
        members.add(accessory2);
        members.add(accessory1);
        accessories.setMembers(members);
        return accessories;
    }

    private Accessories getSample4AccessoriesFromDevice() {
        Accessories accessories = new Accessories();
        Accessory accessory1 = new Accessory();
        accessory1.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory1.setProductId(new Unsigned16(VALID_PID));
        accessory1.setVendorId(new Unsigned16(VALID_VID));
        accessory1.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory1.setRegistration(RegistrationKind.RkOwned);
        accessory1.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));

        Accessory accessory2 = new Accessory();
        UUID accessory2Id = UUID.randomUUID();
        accessory2.setAccessoryID(accessory2Id);
        accessory2.setProductId(new Unsigned16(12));
        accessory2.setVendorId(new Unsigned16(34));
        accessory2.setSerialNumber(new UsbString("abcdef"));
        accessory2.setRegistration(RegistrationKind.RkOwned);
        accessory2.setResourceId(accessory2Id);

        Accessory accessory3 = new Accessory();
        UUID accessory3Id = UUID.randomUUID();
        accessory3.setAccessoryID(accessory3Id);
        accessory3.setProductId(new Unsigned16(77));
        accessory3.setVendorId(new Unsigned16(88));
        accessory3.setRegistration(RegistrationKind.RkShared);
        accessory3.setResourceId(accessory3Id);

        Accessory accessory4 = new Accessory();
        accessory4.setAccessoryID(UUID.fromString(EMPTY_SERIAL_ACCESSORY_ID));
        accessory4.setProductId(new Unsigned16(VALID_PID));
        accessory4.setVendorId(new Unsigned16(VALID_VID));
        accessory4.setRegistration(RegistrationKind.RkOwned);
        accessory4.setResourceId(UUID.fromString(EMPTY_SERIAL_ACCESSORY_ID));

        List<com.hp.ext.service.usbAccessories.Accessory> members = new ArrayList<>();
        members.add(accessory4);
        members.add(accessory3);
        members.add(accessory2);
        members.add(accessory1);
        accessories.setMembers(members);
        return accessories;
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
}
