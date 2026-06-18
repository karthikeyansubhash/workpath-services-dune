package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_PID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SERIAL_NUMBER;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_VID;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class AccessoryCacheUnitTest {

    private static final String VALID_ACCESSORY_ID = "d3b07384-d9a0-4f3d-bb5f-2a5b2f7d5a3f";
    private static final String INVALID_ACCESSORY_ID = "8f14e45f-ea1d-4c9f-9c28-93f5ef3d9a1a";
    private static final String VALID_APP_PACKAGE_NAME = "com.example.app";
    private final static String OPEN_HID_ID = "3e9f6d2a-8c47-4b1e-a5d0-f3b7c9e2d8a1";
    @Mock
    private HIDAccessoryInfo mockAccessoryInfo;
    private AccessoryCache accessoryCache;

    @Before
    public void setUp() {
        AccessoryCache.getInstance().clear();
        accessoryCache = AccessoryCache.getInstance();
    }

    @Test
    public void GivenAccessoryCache_WhenAddCalled_ThenAccessoryInfoAdded() {
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, mockAccessoryInfo);

        HIDAccessoryInfo result = accessoryCache.getHidAccessoryInfo(VALID_ACCESSORY_ID);
        assertNotNull(result);
        assertEquals(mockAccessoryInfo, result);
    }

    @Test
    public void GivenAccessoryCache_WhenRemovedCalled_ThenAccessoryInfoRemoved() {
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, mockAccessoryInfo);
        accessoryCache.remove(VALID_ACCESSORY_ID);

        HIDAccessoryInfo result = accessoryCache.getHidAccessoryInfo(VALID_ACCESSORY_ID);
        assertNull(result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetHidAccessoryInfoCalledWithNullResourceId_ThenNullReturned() {
        HIDAccessoryInfo result = accessoryCache.getHidAccessoryInfo(null);
        assertNull(result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetHidAccessoryInfoCalledWithNonExistentResourceId_ThenNullReturned() {
        HIDAccessoryInfo result = accessoryCache.getHidAccessoryInfo(INVALID_ACCESSORY_ID);
        assertNull(result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetPackageNameCalled_ThenReturnPackageName() {
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, mockAccessoryInfo);
        String result = accessoryCache.getPackageName(VALID_ACCESSORY_ID);
        assertEquals("package name", VALID_APP_PACKAGE_NAME, result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetPackageNameCalledWithNonCachedId_ThenReturnNull() {
        String result = accessoryCache.getPackageName(VALID_ACCESSORY_ID);
        assertNull("package name", result);
    }

    @Test
    public void GivenAccessoryCache_WhenIsCachedCalled_ThenReturnTrue() {
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, mockAccessoryInfo);
        boolean result = accessoryCache.isCached(VALID_ACCESSORY_ID);
        assertTrue("isCached", result);
    }

    @Test
    public void GivenAccessoryCache_WhenIsCachedCalledWithNotCachedId_ThenReturnFalse() {
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, mockAccessoryInfo);
        boolean result = accessoryCache.isCached(INVALID_ACCESSORY_ID);
        assertFalse("isCached", result);
    }

    /// //////////////////////////// getOwnedAccessoryId() ///////////////////////////////
    @Test
    public void GivenAccessoryCache_WhenGetOwnedAccessoryId_ThenReturnResourceId() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        String result = accessoryCache.getOwnedAccessoryId(VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        assertEquals("getOwnedAccessoryId", VALID_ACCESSORY_ID, result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOwnedAccessoryIdWithInvalidPackageName_ThenReturnNull() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        String result = accessoryCache.getOwnedAccessoryId(VALID_APP_PACKAGE_NAME + "invalid", hidAccessoryInfo);
        assertNull("getOwnedAccessoryId", result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOwnedAccessoryIdWithNullPackageName_ThenReturnNull() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        String result = accessoryCache.getOwnedAccessoryId(null, hidAccessoryInfo);
        assertNull("getOwnedAccessoryId", result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOwnedAccessoryIdWithNullAccInfo_ThenReturnNull() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        String result = accessoryCache.getOwnedAccessoryId(VALID_APP_PACKAGE_NAME, null);
        assertNull("getOwnedAccessoryId", result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOwnedAccessoryIdWithSharedHid_ThenReturnNull() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.SHARED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        String result = accessoryCache.getOwnedAccessoryId(VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        assertNull("getOwnedAccessoryId", result);
    }

    /// //////////////////////////// getOpenHidId(), setOpenHidId()  ///////////////////////////////
    @Test
    public void GivenAccessoryCache_WhenGetOpenHidIdWithEmptyCache_ThenReturnNull() {
        UUID result = accessoryCache.getOpenHidId(INVALID_ACCESSORY_ID);
        assertNull("getOpenHidId", result);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOpenHidIdWithOpenedAccessoryId_ThenReturnOpenHidId() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.SHARED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        boolean result = accessoryCache.setOpenHidId(VALID_ACCESSORY_ID, UUID.fromString(OPEN_HID_ID));
        assertTrue("setOpenHidId", result);

        UUID openHidId = accessoryCache.getOpenHidId(VALID_ACCESSORY_ID);
        assertEquals("getOpenHidId", OPEN_HID_ID, openHidId.toString());
    }

    @Test
    public void GivenAccessoryCache_WhenGetOpenHidIdWithNotOpenedAccessoryId_ThenReturnNull() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.SHARED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        UUID openHidId = accessoryCache.getOpenHidId(VALID_ACCESSORY_ID);
        assertNull("getOpenHidId", openHidId);
    }

    @Test
    public void GivenAccessoryCache_WhenGetOpenHidIdWithInvalidAccessoryId_ThenReturnOpenHidId() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.SHARED);

        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);
        boolean result = accessoryCache.setOpenHidId(VALID_ACCESSORY_ID, UUID.fromString(OPEN_HID_ID));
        assertTrue("setOpenHidId", result);

        UUID openHidId = accessoryCache.getOpenHidId(INVALID_ACCESSORY_ID);
        assertNull("getOpenHidId", openHidId);
    }

    @Test
    public void GivenAccessoryCache_WhenSetOpenHidIdWithNotAttachedAccessoryId_ThenReturnFalse() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        boolean result = accessoryCache.setOpenHidId(INVALID_ACCESSORY_ID, UUID.fromString(OPEN_HID_ID));
        assertFalse("setOpenHidId", result);
    }

    /// //////////////////////////// isOwnedType()  ///////////////////////////////
    @Test
    public void GivenAccessoryCache_WhenIsOwnedType_ThenReturnFalse() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.SHARED);
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        boolean result = accessoryCache.isOwnedType(VALID_ACCESSORY_ID);
        assertFalse("isOwnedType", result);
    }

    @Test
    public void GivenAccessoryCache_WhenIsOwnedType_ThenReturnTrue() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        boolean result = accessoryCache.isOwnedType(VALID_ACCESSORY_ID);
        assertTrue("isOwnedType", result);
    }

    @Test
    public void GivenAccessoryCache_WhenIsOwnedTypeWithInvalidAccessoryId_ThenReturnFalse() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "", "",
                RegistrationType.OWNED);
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        boolean result = accessoryCache.isOwnedType(INVALID_ACCESSORY_ID);
        assertFalse("isOwnedType", result);
    }

    @Test
    public void GivenAccessoryCache_WhenIsOwnedTypeAndCachedOwnedTypeIsUnknown_ThenReturnFalse() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        accessoryCache.add(VALID_ACCESSORY_ID, VALID_APP_PACKAGE_NAME, hidAccessoryInfo);

        boolean result = accessoryCache.isOwnedType(INVALID_ACCESSORY_ID);
        assertFalse("isOwnedType", result);
    }
}
