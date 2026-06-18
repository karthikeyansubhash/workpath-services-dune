package com.hp.jetadvantage.link.services.accessorylet.provider;

import static com.hp.jetadvantage.link.api.Result.KEY_CAUSE;
import static com.hp.jetadvantage.link.api.Result.KEY_CODE;
import static com.hp.jetadvantage.link.api.Result.KEY_ERROR_CODE;
import static com.hp.jetadvantage.link.api.Result.RESULT_FAIL;
import static com.hp.jetadvantage.link.api.Result.RESULT_OK;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.ACCESSORY_NOT_REGISTERED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_INFO;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_REPORT;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.InvalidParam.INVALID_ACCESSORY_REPORT_TYPE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_NOT_FOUND;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_NOT_OPENED;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_OPEN_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_READ_REPORT_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_START_READING_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_STOP_READING_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.ServiceError.ACCESSORY_WRITE_REPORT_FAILURE;
import static com.hp.jetadvantage.link.common.constants.ErrorMessages.Unauthorized.NO_ACTIVE_UI_SESSION;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.OPEN_HID_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.TEST_APP_PACKAGE_NAME;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_ACCESSORY_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_PID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SERIAL_NUMBER;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SHARED_ACCESSORY_ID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SHARED_PID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SHARED_SERIAL_NUMBER;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_SHARED_VID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.VALID_VID;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.getSample1AccessoriesFromDevice;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.getSampleOwnedAccessoryFromDevice;
import static com.hp.jetadvantage.link.services.accessorylet.FakeAccessory.getSampleSharedAccessoriesFromDevice;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_WriteReport;
import com.hp.ext.service.usbAccessories.UsbData;
import com.hp.ext.service.usbAccessories.UsbTransferStatus;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.api.accessory.hid.EventCode;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReport;
import com.hp.jetadvantage.link.api.accessory.hid.HIDReportType;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.services.accessorylet.service.AccessoryCache;
import com.hp.jetadvantage.link.services.accessorylet.service.AccessoryNotificationService;
import com.hp.jetadvantage.link.services.accessorylet.util.AccessoryRegistrationRecord;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class AccessoryLetContentProviderUnitTest {
    @Mock
    Bundle mockInputBundle;

    @Mock
    Bundle resultBundle;

    @Mock
    Context mockContext;

    @Mock
    IDeviceAccessoryService mockDeviceAccessoryService;

    AccessoryLetContentProvider accessoryLetContentProvider;

    @Before
    public void setUp() {
        AccessoryCache.getInstance().clear();
        accessoryLetContentProvider = new AccessoryLetContentProvider(mockDeviceAccessoryService);
    }

    /**
     * Test case for Accessorylet.Method.REGISTER
     * When call REGISTER method with null accessory info in input bundle
     * Then return invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallRegisterMethodWithNullAccessoryInfo_ThenReturnInvalidParamException() {
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            Bundle resultBundle = accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.REGISTER,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, this.resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    /**
     * Test case for Accessorylet.Method.REGISTER
     * When call REGISTER method with an accessory info that is not in the app's registration record
     * Then return invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallRegisterMethodWithNotRegisteredAccessoryInfo_ThenReturnInvalidParamException() {
        HIDAccessoryInfo missingAccessoryInfoInRegistrationRecord = new HIDAccessoryInfo(1, 2, null);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION)).thenReturn(missingAccessoryInfoInRegistrationRecord);

        try (MockedStatic<AccessoryRegistrationRecord> mockedUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockedUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(false);

            Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
                Bundle resultBundle = accessoryLetContentProvider.handleMethod(mockContext,
                        Accessorylet.Method.REGISTER,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, this.resultBundle);
            });
            assertEquals("ACCESSORY_NOT_REGISTERED", ACCESSORY_NOT_REGISTERED, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.REGISTER
     * When call REGISTER method with an accessory info that is not attached to the device, or not found from the device
     * Then return sdk service exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallRegisterMethodWithNotAttachedAccessoryInfo_ThenReturnSdkServiceExceptionIfNotFound() {
        HIDAccessoryInfo notAttachedAccessoryInfo = new HIDAccessoryInfo(1, 2, null);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION)).thenReturn(notAttachedAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME)).thenReturn(null);

        try (MockedStatic<AccessoryRegistrationRecord> mockedUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockedUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);
            Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
                Bundle resultBundle = accessoryLetContentProvider.handleMethod(mockContext,
                        Accessorylet.Method.REGISTER,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, this.resultBundle);
            });
            assertEquals("ACCESSORY_NOT_FOUND", ACCESSORY_NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.REGISTER
     * When call REGISTER method with an accessory info that is found from the device
     * Then return result OK in bundle and notify the app with the accessory context created event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallRegisterMethodWithValidAccessoryInfo_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSample1AccessoriesFromDevice());

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            Bundle resultBundle = accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.REGISTER,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, this.resultBundle);

            verify(this.resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID, EventCode.CONTEXT_CREATED));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        }
    }

    /**
     * Test case for Accessorylet.Method.REGISTER
     * When call REGISTER method with an accessory info that is found from the cache
     * Then return result OK in bundle and notify the app with the accessory context created event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallRegisterMethodWithCachedAccessoryInfo_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        String cachedAccessoryId = "d3b07384-d9a0-4f3d-bb5f-2a5b2f7d5a3f";
        AccessoryCache.getInstance().add(cachedAccessoryId, TEST_APP_PACKAGE_NAME, validAccessoryInfo);

        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_REGISTRATION)).thenReturn(validAccessoryInfo);

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.REGISTER,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, cachedAccessoryId, EventCode.CONTEXT_CREATED));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        } finally {
            AccessoryCache.getInstance().clear();
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with null accessory info in input bundle
     * Then return invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendMethodWithNullAccessoryInfo_ThenReturnInvalidParamException() {
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESEND_OWNED,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is not in the app's registration record
     * Then return invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithNotRegisteredAccessoryInfo_ThenReturnInvalidParamException() {
        HIDAccessoryInfo missingAccessoryInfoInRegistrationRecord = new HIDAccessoryInfo(1, 2, null);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(missingAccessoryInfoInRegistrationRecord);

        try (MockedStatic<AccessoryRegistrationRecord> mockedUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockedUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(false);

            Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
                accessoryLetContentProvider.handleMethod(mockContext,
                        Accessorylet.Method.RESEND_OWNED,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
            });
            assertEquals("ACCESSORY_NOT_REGISTERED", ACCESSORY_NOT_REGISTERED, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is not attached to the device, or not found from the
     * device
     * Then return sdk service exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithNotAttachedAccessoryInfo_ThenReturnSdkServiceExceptionIfNotFound() {
        HIDAccessoryInfo notAttachedAccessoryInfo = new HIDAccessoryInfo(1, 2, null);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(notAttachedAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME)).thenReturn(null);

        try (MockedStatic<AccessoryRegistrationRecord> mockedUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockedUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);
            Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
                accessoryLetContentProvider.handleMethod(mockContext,
                        Accessorylet.Method.RESEND_OWNED,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
            });
            assertEquals("ACCESSORY_NOT_FOUND", ACCESSORY_NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is found from the IDeviceAccessoryService
     * Then return result OK in bundle and notify the app with the accessory context resent event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithValidAccessoryInfo_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSample1AccessoriesFromDevice());

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESEND_OWNED,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID, EventCode.CONTEXT_RESENT));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is found from the IDeviceAccessoryService
     * Then return result OK in bundle and notify the app with the accessory context resent event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithValidAccessoryInfo2_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSample1AccessoriesFromDevice());
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSample1AccessoriesFromDevice());

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isOwnedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESEND_OWNED,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID, EventCode.CONTEXT_RESENT));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is found from the cache
     * Then return result OK in bundle and notify the app with the accessory context resent event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithCachedAccessoryInfo_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        String cachedAccessoryId = "d3b07384-d9a0-4f3d-bb5f-2a5b2f7d5a3f";
        AccessoryCache.getInstance().add(cachedAccessoryId, TEST_APP_PACKAGE_NAME, validAccessoryInfo);

        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);

        try (MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESEND_OWNED,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, cachedAccessoryId, EventCode.CONTEXT_RESENT));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        } finally {
            AccessoryCache.getInstance().clear();
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with an accessory info that is found from the cache
     * Then return result OK in bundle and notify the app with the accessory context resent event
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallResendOwnedMethodWithCachedAccessoryInfo2_ThenReturnResultOK() {
        HIDAccessoryInfo cachedAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        String cachedAccessoryId = "d3b07384-d9a0-4f3d-bb5f-2a5b2f7d5a3f";
        AccessoryCache.getInstance().add(cachedAccessoryId, TEST_APP_PACKAGE_NAME, cachedAccessoryInfo);

        HIDAccessoryInfo requestAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(requestAccessoryInfo);

        try (MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESEND_OWNED,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(mockContext,
                    TEST_APP_PACKAGE_NAME, cachedAccessoryId, EventCode.CONTEXT_RESENT));
        } catch (SdkException e) {
            fail("Should not throw any exception:" + e);
        } finally {
            AccessoryCache.getInstance().clear();
        }
    }

    /**
     * Test case for Accessorylet.Method.RESEND_OWNED
     * When call RESEND_OWNED method with null accessory info in the request bundle
     * Then return invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    /**
     * Test case for Accessorylet.Method.RESERVE
     * When call RESERVE method without UI context
     * Then throws unauthorized exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithoutUiContext_ThenThrowsUnauthorizedException() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.isUiContextAvailable(TEST_APP_PACKAGE_NAME)).thenReturn(false);

        Exception exception = assertThrows(SdkUnauthorizedException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("NO_ACTIVE_UI_SESSION", NO_ACTIVE_UI_SESSION, exception.getMessage());
    }

    /**
     * Test case for Accessorylet.Method.RESERVE
     * When call RESERVE method but the shared accessory is not registered by the app
     * Then throws invalid param exception
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithNotRegisteredAccessoryInfo_ThenThrowsInvalidParamException() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.isUiContextAvailable(TEST_APP_PACKAGE_NAME)).thenReturn(true);

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(false);

            Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
                accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
            });
            assertEquals("ACCESSORY_NOT_REGISTERED", ACCESSORY_NOT_REGISTERED, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.RESERVE
     * When call RESERVE method but the shared accessory is not found from the device
     * Then throws service error exception with ACCESSORY_NOT_FOUND message
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithNotFoundAccessory_ThenThrowsServiceErrorException() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.isUiContextAvailable(TEST_APP_PACKAGE_NAME)).thenReturn(true);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME)).thenReturn(null);

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
                accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
            });
            assertEquals("ACCESSORY_NOT_FOUND", ACCESSORY_NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * Test case for Accessorylet.Method.RESERVE
     * When call RESERVE method with valid shared accessory info
     * Then return result OK and shared accessory ID in a bundle and the reserved accessory should be added in
     * AccessoryCache. Also, the app should not be notified for the shared accessory.
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithValidSharedAccessory_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_SHARED_VID,
                VALID_SHARED_PID,
                VALID_SHARED_SERIAL_NUMBER);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSampleSharedAccessoriesFromDevice());
        when(mockDeviceAccessoryService.isUiContextAvailable(TEST_APP_PACKAGE_NAME)).thenReturn(true);

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            try {
                accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
                verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
                verify(resultBundle).putString(Result.KEY_RESULT, VALID_SHARED_ACCESSORY_ID);

                assertEquals("Check reserved accessory is added in AccessoryCache", TEST_APP_PACKAGE_NAME,
                        AccessoryCache.getInstance().getPackageName(VALID_SHARED_ACCESSORY_ID));

                assertFalse("isOwnedType",
                        AccessoryCache.getInstance().isOwnedType(VALID_SHARED_ACCESSORY_ID));

                //should not notify the app for shared accessory
                mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(any(), any(), any()
                        , any()), never());
            } catch (Exception e) {
                fail("Should not throw any exception:" + e);
            }
        }
    }

    /**
     *
     */
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReserveMethodWithValidSharedAccessory2_ThenReturnResultOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_SHARED_VID,
                VALID_SHARED_PID,
                VALID_SHARED_SERIAL_NUMBER, "test", "", RegistrationType.SHARED);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_ACCESSORY_INFO)).thenReturn(validAccessoryInfo);
        when(mockDeviceAccessoryService.getAccessories(TEST_APP_PACKAGE_NAME))
                .thenReturn(getSampleSharedAccessoriesFromDevice());
        when(mockDeviceAccessoryService.isUiContextAvailable(TEST_APP_PACKAGE_NAME)).thenReturn(true);

        try (MockedStatic<AccessoryRegistrationRecord> mockUtility = mockStatic(AccessoryRegistrationRecord.class);
             MockedStatic<AccessoryNotificationService> mockNoti = mockStatic(AccessoryNotificationService.class)) {
            mockUtility.when(() -> AccessoryRegistrationRecord.isSharedAccessoryRegisteredByApp(any(), any(), any())).thenReturn(true);

            try {
                accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RESERVE,
                        mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
                verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
                verify(resultBundle).putString(Result.KEY_RESULT,
                        VALID_SHARED_ACCESSORY_ID);

                assertEquals("Check reserved accessory is added in AccessoryCache", TEST_APP_PACKAGE_NAME,
                        AccessoryCache.getInstance().getPackageName(VALID_SHARED_ACCESSORY_ID));

                assertFalse("isOwnedType",
                        AccessoryCache.getInstance().isOwnedType(VALID_SHARED_ACCESSORY_ID));

                //should not notify the app for shared accessory
                mockNoti.verify(() -> AccessoryNotificationService.sendAccessoryNotificationToApp(any(), any(), any()
                        , any()), never());
            } catch (Exception e) {
                fail("Should not throw any exception:" + e);
            }
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReleaseMethod_ThenReturnOK() {
        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.RELEASE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithCachedOwnedAccessoryInfo_ThenThrowsSdkServiceExceptionIfFailed() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, null,
                null, RegistrationType.OWNED);

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.openOwnedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(null);

        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, hidAccessoryInfo);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_OPEN_FAILURE", ACCESSORY_OPEN_FAILURE, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithCachedOwnedAccessoryInfo_ThenReturnOK() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, null,
                null, RegistrationType.OWNED);

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.openOwnedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(UUID.fromString(OPEN_HID_ID));

        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, hidAccessoryInfo);

        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            assertEquals("OPEN_HID_ID", OPEN_HID_ID,
                    AccessoryCache.getInstance().getOpenHidId(VALID_ACCESSORY_ID).toString());
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithNoCachedOwnedAccessoryInfo_ThenThrowsSdkServiceExceptionIfNotFound() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.getAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(null);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_FOUND", ACCESSORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithNoCachedOwnedAccessoryInfo_ThenThrowsSdkServiceExceptionIfFailed() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.getAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(getSampleOwnedAccessoryFromDevice());
        when(mockDeviceAccessoryService.openOwnedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(null);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_OPEN_FAILURE", ACCESSORY_OPEN_FAILURE, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithNoCachedOwnedAccessoryInfo_ThenThrowsSdkServiceExceptionIfFailedWithException() {
        String errorMessage = "test";
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.getAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(getSampleOwnedAccessoryFromDevice());
        when(mockDeviceAccessoryService.openOwnedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenThrow(new RuntimeException(errorMessage));

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_OPEN_FAILURE", ACCESSORY_OPEN_FAILURE + errorMessage, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithNoCachedOwnedAccessoryInfo_ThenReturnOK() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.getAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(getSampleOwnedAccessoryFromDevice());
        when(mockDeviceAccessoryService.openOwnedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(UUID.fromString(OPEN_HID_ID));

        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            assertEquals("OPEN_HID_ID", OPEN_HID_ID,
                    AccessoryCache.getInstance().getOpenHidId(VALID_ACCESSORY_ID).toString());
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallOpenMethodWithCachedSharedAccessoryInfo_ThenThrowsSdkServiceExceptionIfFailed() {
        HIDAccessoryInfo hidAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, null,
                null, RegistrationType.SHARED);

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.openSharedHidAccessory(TEST_APP_PACKAGE_NAME, VALID_ACCESSORY_ID)).thenReturn(null);

        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, hidAccessoryInfo);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.OPEN,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_OPEN_FAILURE", ACCESSORY_OPEN_FAILURE, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallCloseMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.CLOSE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallCloseMethodWithNotOpened_ThenThrowsSdkServiceException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.CLOSE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_OPENED", ACCESSORY_NOT_OPENED, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallCloseMethod_ThenReturnOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.closeHidAccessory(eq(TEST_APP_PACKAGE_NAME), eq(VALID_ACCESSORY_ID),
                eq(openHidId))).thenReturn(true);

        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.CLOSE,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStartReadingMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.START_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStartReadingWithNotOpened_ThenThrowsSdkServiceException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.START_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_OPENED", ACCESSORY_NOT_OPENED, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStartReadingMethod_ThenReturnOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.startAsyncReading(eq(TEST_APP_PACKAGE_NAME), eq(openHidId),
                eq(VALID_ACCESSORY_ID), eq(true))).thenReturn(true);
        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.START_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStartReadingMethod_ThenReturnFailureIfFailed() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.startAsyncReading(eq(TEST_APP_PACKAGE_NAME), eq(openHidId),
                eq(VALID_ACCESSORY_ID), eq(true))).thenReturn(false);
        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.START_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_FAIL);
            verify(resultBundle).putSerializable(KEY_ERROR_CODE, Result.ErrorCode.SERVICE_ERROR);
            verify(resultBundle).putString(KEY_CAUSE, ACCESSORY_START_READING_FAILURE);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    ///
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStopReadingMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.STOP_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStopReadingWithNotOpened_ThenThrowsSdkServiceException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.STOP_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_OPENED", ACCESSORY_NOT_OPENED, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStopReadingMethod_ThenReturnOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.stopAsyncReading(eq(TEST_APP_PACKAGE_NAME), eq(openHidId),
                eq(VALID_ACCESSORY_ID), eq(true))).thenReturn(true);
        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.STOP_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallStopReadingMethod_ThenReturnFailureIfFailed() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockDeviceAccessoryService.stopAsyncReading(eq(TEST_APP_PACKAGE_NAME), eq(openHidId),
                eq(VALID_ACCESSORY_ID), eq(true))).thenReturn(false);
        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.STOP_READING,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_FAIL);
            verify(resultBundle).putSerializable(KEY_ERROR_CODE, Result.ErrorCode.SERVICE_ERROR);
            verify(resultBundle).putString(KEY_CAUSE, ACCESSORY_STOP_READING_FAILURE);
        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }


    /// readReport
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReadReportMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        when(mockInputBundle.getSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE)).thenReturn(HIDReportType.INPUT);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.READ_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReadReportWithNotOpened_ThenThrowsSdkServiceException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE)).thenReturn(HIDReportType.FEATURE);
        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.READ_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_OPENED", ACCESSORY_NOT_OPENED, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReadReportWithEmptyReporType_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.READ_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_REPORT_TYPE", INVALID_ACCESSORY_REPORT_TYPE, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReadReportMethod_ThenReturnOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);

        String base64Data = "dGVzdCBkYXRh";
        OpenHIDAccessory_ReadReport e2ReadReport = new OpenHIDAccessory_ReadReport();
        e2ReadReport.setData(new UsbData(base64Data));

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE)).thenReturn(HIDReportType.FEATURE);
        when(mockDeviceAccessoryService.readReport(eq(TEST_APP_PACKAGE_NAME), eq(openHidId), eq(VALID_ACCESSORY_ID),
                eq(true), any())).thenReturn(e2ReadReport);

        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.READ_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);
            ArgumentCaptor<Parcelable> captor = ArgumentCaptor.forClass(Parcelable.class);

            verify(resultBundle).putParcelable(eq(Result.KEY_RESULT), captor.capture());
            HIDReport report = (HIDReport) captor.getValue();
            String responseBase64data = Base64.getEncoder().encodeToString(report.getData());
            assertEquals("data", responseBase64data, base64Data);

        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallReadReportMethod_ThenReturnFailureIfFailed() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getSerializable(Accessorylet.Keys.KEY_HID_REPORT_TYPE)).thenReturn(HIDReportType.FEATURE);
        when(mockDeviceAccessoryService.readReport(eq(TEST_APP_PACKAGE_NAME), eq(openHidId), eq(VALID_ACCESSORY_ID),
                eq(true), any())).thenReturn(null);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.READ_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_READ_REPORT_FAILURE", ACCESSORY_READ_REPORT_FAILURE, exception.getMessage());
    }

    /// writeReport
    @Test
    public void GivenAccessoryLetContentProvider_WhenCallWriteReportMethodWithNullAccessoryInfo_ThenThrowsInvalidParamException() {
        HIDReport report = new HIDReport(HIDReportType.FEATURE, new byte[0]);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(null);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_HID_REPORT)).thenReturn(report);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.WRITE_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_INFO", INVALID_ACCESSORY_INFO, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallWriteReportWithNotOpened_ThenThrowsSdkServiceException() {
        HIDReport report = new HIDReport(HIDReportType.FEATURE, new byte[0]);
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_HID_REPORT)).thenReturn(report);
        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.WRITE_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_NOT_OPENED", ACCESSORY_NOT_OPENED, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallWriteReportWithEmptyReporType_ThenThrowsInvalidParamException() {
        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_HID_REPORT)).thenReturn(null);
        Exception exception = assertThrows(SdkInvalidParamException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.WRITE_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("INVALID_ACCESSORY_REPORT", INVALID_ACCESSORY_REPORT, exception.getMessage());
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallWriteReportMethod_ThenReturnOK() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);

        String base64Data = "dGVzdCBkYXRh";
        HIDReport requestReport = new HIDReport(HIDReportType.FEATURE, Base64.getDecoder().decode(base64Data));

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_HID_REPORT)).thenReturn(requestReport);

        OpenHIDAccessory_WriteReport e2WriteReport = new OpenHIDAccessory_WriteReport();
        e2WriteReport.setUsbTransferStatus(UsbTransferStatus.UtsOk);
        e2WriteReport.setBytesWritten(new Unsigned16(10));
        when(mockDeviceAccessoryService.writeReport(eq(TEST_APP_PACKAGE_NAME), eq(openHidId), eq(VALID_ACCESSORY_ID),
                eq(true), any())).thenReturn(e2WriteReport);

        try {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.WRITE_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);

            verify(resultBundle).putInt(KEY_CODE, RESULT_OK);

        } catch (Exception e) {
            fail("Should not throw any exception:" + e);
        }
    }

    @Test
    public void GivenAccessoryLetContentProvider_WhenCallWriteReportMethod_ThenReturnFailureIfFailed() {
        HIDAccessoryInfo validAccessoryInfo = new HIDAccessoryInfo(VALID_VID, VALID_PID, VALID_SERIAL_NUMBER, "test",
                "test", RegistrationType.OWNED);
        UUID openHidId = UUID.fromString(OPEN_HID_ID);
        AccessoryCache.getInstance().add(VALID_ACCESSORY_ID, TEST_APP_PACKAGE_NAME, validAccessoryInfo);
        AccessoryCache.getInstance().setOpenHidId(VALID_ACCESSORY_ID, openHidId);

        String base64Data = "dGVzdCBkYXRh";
        HIDReport requestReport = new HIDReport(HIDReportType.FEATURE, Base64.getDecoder().decode(base64Data));

        when(mockInputBundle.getString(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID)).thenReturn(VALID_ACCESSORY_ID);
        when(mockInputBundle.getParcelable(Accessorylet.Keys.KEY_HID_REPORT)).thenReturn(requestReport);
        when(mockDeviceAccessoryService.writeReport(eq(TEST_APP_PACKAGE_NAME), eq(openHidId), eq(VALID_ACCESSORY_ID),
                eq(true), any())).thenReturn(null);

        Exception exception = assertThrows(SdkServiceErrorException.class, () -> {
            accessoryLetContentProvider.handleMethod(mockContext, Accessorylet.Method.WRITE_REPORT,
                    mockInputBundle, TEST_APP_PACKAGE_NAME, resultBundle);
        });
        assertEquals("ACCESSORY_WRITE_REPORT_FAILURE", ACCESSORY_WRITE_REPORT_FAILURE, exception.getMessage());
    }
}
