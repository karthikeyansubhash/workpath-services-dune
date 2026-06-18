package com.hp.jetadvantage.link.services.accessorylet.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.services.accessorylet.model.AccessoryRegistrationInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AccessoryRegistrationRecordUnitTest {
    static String RR = "com.hp.ext.service.usbAccessories.version.1.type.usbAccessoriesAgentRegistrationRecord";
    @Mock
    private static Uri mockUri;
    @Mock
    private Context mockContext;
    @Mock
    private ContentResolver mockContentResolver;
    @Mock
    private Cursor mockCursor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getContentResolver()).thenReturn(mockContentResolver);
        TestAccessoryRegistrationRecord.setMockUri(mockUri);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenGetRegisteredAccessoriesCalled_ThenReturnsEmptyListIfNoData() {
        when(mockContentResolver.query(any(), any(), anyString(), any(), any(), any()))
                .thenReturn(mockCursor);

        List<AccessoryRegistrationInfo> result = TestAccessoryRegistrationRecord.getRegisteredAccessories(
                mockContext, "com.test.app");
        assertNotNull(result);
        assertEquals("result.size", 0, result.size());
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenGetRegisteredAccessoriesCalled_ThenReturnsEmptyListIfNullRecord() {
        when(mockContentResolver.query(any(), any(), anyString(), any(), any(), any()))
                .thenReturn(mockCursor);
        // Mocking cursor behavior
        when(mockCursor.moveToNext()).thenReturn(true).thenReturn(false);

        List<AccessoryRegistrationInfo> result = TestAccessoryRegistrationRecord.getRegisteredAccessories(
                mockContext, "com.test.app");
        assertNotNull(result);
        assertEquals("result.size", 0, result.size());
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenGetRegisteredAccessoriesCalled_ThenReturnsEmptyListIfNullRecord2() {
        int functionTypeIndex = 10;
        int param1Index = 11;

        when(mockContentResolver.query(any(), any(), anyString(), any(), any(), any()))
                .thenReturn(mockCursor);

        // Mocking cursor behavior
        when(mockCursor.moveToNext()).thenReturn(true, false);
        when(mockCursor.getColumnIndex(PackageContract.PackageProviderEntry.FUNCTION_TYPE)).thenReturn(functionTypeIndex);
        when(mockCursor.getColumnIndex(PackageContract.PackageProviderEntry.EXT_DATA1)).thenReturn(param1Index);
        when(mockCursor.getString(functionTypeIndex)).thenReturn(RR);
        when(mockCursor.getString(param1Index)).thenReturn(null);

        List<AccessoryRegistrationInfo> result = TestAccessoryRegistrationRecord.getRegisteredAccessories(
                mockContext, "com.test.app");
        assertNotNull(result);
        assertEquals("result.size", 0, result.size());
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenGetRegisteredAccessoriesCalled_ThenReturnsExpectedList() {
        String testAccRecord = "{\"productId\":\"272\",\"registrationType\":\"OWNED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        setupMockCursor(testAccRecord);

        List<AccessoryRegistrationInfo> result = TestAccessoryRegistrationRecord.getRegisteredAccessories(
                mockContext, "com.test.app");
        assertNotNull(result);
        assertEquals("result.size", 1, result.size());
        assertEquals("RegistrationType", AccessoryRegistrationInfo.RegistrationType.OWNED,
                result.get(0).getRegistrationType());
        assertEquals("getProductId", 272, result.get(0).getProductId());
        assertEquals("getVendorId", 7590, result.get(0).getVendorId());
        assertEquals("getSerialNumber", "1", result.get(0).getSerialNumber());
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenHasAppOwnedAccessoriesCalled_ThenReturnsTrue() {
        String testAccRecord1 = "{\"productId\":\"272\",\"registrationType\":\"OWNED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        String testAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"SHARED\",\"serialNumber\":\"NULL\"," +
                "\"vendorId\":\"4567\"}";
        String testAccRecord3 = "{\"productId\":\"111\",\"registrationType\":\"SHARED\"," +
                "\"vendorId\":\"222\"}";

        setupMockCursor(testAccRecord1, testAccRecord2, testAccRecord3);

        Boolean result = TestAccessoryRegistrationRecord.hasAppOwnedAccessories(
                mockContext, "com.test.app");
        assertTrue("hasAppOwnedAccessories", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenHasAppOwnedAccessoriesCalled_ThenReturnsFalseIfEmptyAccessoryRecord() {
        setupMockCursor("");

        Boolean result = TestAccessoryRegistrationRecord.hasAppOwnedAccessories(
                mockContext, "com.test.app");
        assertFalse("hasAppOwnedAccessories", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenHasAppOwnedAccessoriesCalled_ThenReturnsFalseIfOnlyShared() {
        String testAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"SHARED\",\"serialNumber\":\"NULL\"," +
                "\"vendorId\":\"4567\"}";

        setupMockCursor(testAccRecord2);

        Boolean result = TestAccessoryRegistrationRecord.hasAppOwnedAccessories(
                mockContext, "com.test.app");
        assertFalse("hasAppOwnedAccessories", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsSharedAccessoryRegisteredByAppCalled_ThenReturnTrue() {
        String testAccRecord1 = "{\"productId\":\"272\",\"registrationType\":\"OWNED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        String testAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"SHARED\",\"serialNumber\":\"abcd\"," +
                "\"vendorId\":\"4567\"}";
        String testAccRecord3 = "{\"productId\":\"111\",\"registrationType\":\"SHARED\"," +
                "\"vendorId\":\"222\"}";


        setupMockCursor(testAccRecord2);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertTrue("isSharedAccessoryRegisteredByApp for 1 record", result);

        setupMockCursor(testAccRecord1, testAccRecord2, testAccRecord3);
        hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd", "description", "manufacturer",
                RegistrationType.SHARED);
        result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertTrue("isSharedAccessoryRegisteredByApp for 3 record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsSharedAccessoryRegisteredByAppCalled_ThenReturnFalseIfNullRecord() {
        setupMockCursor((String[])null);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isSharedAccessoryRegisteredByApp for null record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsSharedAccessoryRegisteredByAppCalled_ThenReturnFalseIfEmptyRecord() {
        setupMockCursor("");
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isSharedAccessoryRegisteredByApp for empty record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsSharedAccessoryRegisteredByAppCalled_ThenReturnFalseIfOnlyOwnedRecords() {
        String testOwnedAccRecord1 = "{\"productId\":\"272\",\"registrationType\":\"OWNED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        String testOwnedAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"OWNED\"," +
                "\"serialNumber\":\"abcd\"," +
                "\"vendorId\":\"4567\"}";
        setupMockCursor(testOwnedAccRecord1);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isSharedAccessoryRegisteredByApp for 1 record", result);

        setupMockCursor(testOwnedAccRecord1, testOwnedAccRecord2);
        hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd", "description", "manufacturer",
                RegistrationType.SHARED);
        result = TestAccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isSharedAccessoryRegisteredByApp for 2 record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsOwnedAccessoryRegisteredByAppCalled_ThenReturnTrue() {
        String testAccRecord1 = "{\"productId\":\"272\",\"registrationType\":\"OWNED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        String testAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"OWNED\",\"serialNumber\":\"abcd\"," +
                "\"vendorId\":\"4567\"}";
        String testAccRecord3 = "{\"productId\":\"111\",\"registrationType\":\"SHARED\"," +
                "\"vendorId\":\"222\"}";


        setupMockCursor(testAccRecord2);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertTrue("isOwnedAccessoryRegisteredByApp for 1 record", result);

        setupMockCursor(testAccRecord1, testAccRecord2, testAccRecord3);
        hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd", "description", "manufacturer",
                RegistrationType.SHARED);
        result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertTrue("isOwnedAccessoryRegisteredByApp for 3 record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsOwnedAccessoryRegisteredByAppCalled_ThenReturnFalseIfNullRecord() {
        setupMockCursor((String[])null);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isOwnedAccessoryRegisteredByApp for null record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsOwnedAccessoryRegisteredByAppCalled_ThenReturnFalseIfEmptyRecord() {
        setupMockCursor("");
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isOwnedAccessoryRegisteredByApp for empty record", result);
    }

    @Test
    public void GivenAccessoryRegistrationRecord_WhenIsOwnedAccessoryRegisteredByAppCalled_ThenReturnFalseIfOnlySharedRecords() {
        String testOwnedAccRecord1 = "{\"productId\":\"272\",\"registrationType\":\"SHARED\",\"serialNumber\":\"1\"," +
                "\"vendorId\":\"7590\"}";
        String testOwnedAccRecord2 = "{\"productId\":\"123\",\"registrationType\":\"SHARED\"," +
                "\"serialNumber\":\"abcd\"," +
                "\"vendorId\":\"4567\"}";
        setupMockCursor(testOwnedAccRecord1);
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd");
        Boolean result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isOwnedAccessoryRegisteredByApp for 1 record", result);

        setupMockCursor(testOwnedAccRecord1, testOwnedAccRecord2);
        hidAccessoryInfo = new HIDAccessoryInfo(4567, 123, "abcd", "description", "manufacturer",
                RegistrationType.SHARED);
        result = TestAccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(
                mockContext, "com.test.app", hidAccessoryInfo);
        assertFalse("isOwnedAccessoryRegisteredByApp for 2 record", result);
    }

    private void setupMockCursor(String... testAccRecords) {
        int functionTypeIndex = 10;
        int param1Index = 11;

        when(mockContentResolver.query(any(), any(), anyString(), any(), any(), any()))
                .thenReturn(mockCursor);

        // Mocking cursor behavior
        if (testAccRecords == null) {
            when(mockCursor.moveToNext()).thenReturn(true, false);
            when(mockCursor.getString(param1Index)).thenReturn(null);
        } else {
            when(mockCursor.moveToNext()).thenReturn(true, testAccRecords.length > 1, testAccRecords.length > 2, false);
            when(mockCursor.getString(param1Index)).thenReturn(testAccRecords[0], testAccRecords.length > 1 ?
                    testAccRecords[1] : null, testAccRecords.length > 2 ? testAccRecords[2] : null);
        }
        when(mockCursor.getColumnIndex(PackageContract.PackageProviderEntry.FUNCTION_TYPE)).thenReturn(functionTypeIndex);
        when(mockCursor.getColumnIndex(PackageContract.PackageProviderEntry.EXT_DATA1)).thenReturn(param1Index);
        when(mockCursor.getString(functionTypeIndex)).thenReturn(RR);
    }


    public static class TestAccessoryRegistrationRecord extends AccessoryRegistrationRecord {
        public static void setMockUri(Uri mock) {
            setQueryUri(mock);
        }
    }
}

