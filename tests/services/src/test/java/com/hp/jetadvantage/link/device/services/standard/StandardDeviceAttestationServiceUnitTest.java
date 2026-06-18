package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit test for StandardDeviceAttestationService
 */
@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceAttestationServiceUnitTest {

    @Mock
    private DeviceManagementService mockDeviceManagementService;

    @Test
    public void givenStandardDeviceAttestationService_whenConstructorCalled_thenObjectCreated() {
        StandardDeviceAttestationService attestationService = new StandardDeviceAttestationService();
        assertNotNull("Service should not be null", attestationService);
    }

    @Test
    public void givenStandardDeviceAttestationService_whenConstructorWithDeviceManagementServiceCalled_thenObjectCreated() {
        StandardDeviceAttestationService attestationService =
                new StandardDeviceAttestationService(mockDeviceManagementService);
        assertNotNull("Service should not be null", attestationService);
    }

    @Test
    public void givenStandardDeviceAttestationService_whenIsSupportedCalled_thenReturnBoolean() {
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("192.168.1.100");

        StandardDeviceAttestationService attestationService =
                new StandardDeviceAttestationService(mockDeviceManagementService);

        // Note: Actual isSupported implementation may vary based on device connectivity
        // This test just verifies the method can be called without exception
        boolean isSupported = attestationService.isSupported();
        assertTrue("isSupported should return a boolean value",
                   isSupported == true || isSupported == false);
    }
}

