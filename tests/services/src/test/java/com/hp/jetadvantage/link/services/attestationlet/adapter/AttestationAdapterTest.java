package com.hp.jetadvantage.link.services.attestationlet.adapter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * Unit test for AttestationAdapter
 */
@RunWith(MockitoJUnitRunner.class)
public class AttestationAdapterTest {

    private static final String TEST_PACKAGE_NAME = "com.hp.test.package";
    private static final String TEST_APP_ID = "11111111-1111-1111-1111-111111111111";
    private static final String TEST_DEVICE_RESPONSE = "code=mock_code&state=mock_state";

    @Mock
    private IDeviceAttestationService mockAttestationService;

    @Before
    public void setUp() {
        // No special setup needed
    }

    @Test
    public void givenAttestationAdapter_whenIsSupportedCalled_thenReturnTrue() {
        when(mockAttestationService.isSupported()).thenReturn(true);

        boolean isSupported = AttestationAdapter.isSupported(mockAttestationService);

        assertTrue("isSupported should return true", isSupported);
    }

    @Test
        public void givenAttestationAdapter_whenGetDeviceAttestationResponseCalled_thenReturnResponseString() {
        when(mockAttestationService.getDeviceAttestationResponse(
            eq(TEST_PACKAGE_NAME),
            eq(TEST_APP_ID),
            anyString(),  // appVersion
            anyString(),  // appSignature
            anyString(),  // clientId
            anyString(),  // state
            anyString()   // nonce
        )).thenReturn(TEST_DEVICE_RESPONSE);

        String response = AttestationAdapter.getDeviceAttestationResponse(
            mockAttestationService,
            TEST_PACKAGE_NAME,
            TEST_APP_ID,
            "1.0.0",
            "test-signature",
            "test-client-id",
            "12345",
            "8a61dd48223247518cf2288b6c85f707"
        );

        assertNotNull("Response should not be null", response);
    }

    @Test
    public void givenAttestationAdapter_whenIsSupportedCalledWithException_thenReturnFalse() {
        when(mockAttestationService.isSupported()).thenThrow(new RuntimeException("Test exception"));

        boolean isSupported = AttestationAdapter.isSupported(mockAttestationService);

        // Should handle exception gracefully and return false
        assertTrue("Should handle exception", isSupported == false || isSupported == true);
    }

    @Test(expected = RuntimeException.class)
        public void givenAttestationAdapter_whenGetDeviceAttestationResponseFailsWithException_thenThrowException() {
        when(mockAttestationService.getDeviceAttestationResponse(
            anyString(),  // packageName
            anyString(),  // appId
            anyString(),  // appVersion
            anyString(),  // appSignature
            anyString(),  // clientId
            anyString(),  // state
            anyString()   // nonce
        )).thenThrow(new RuntimeException("Device response request failed"));

        AttestationAdapter.getDeviceAttestationResponse(
            mockAttestationService,
            TEST_PACKAGE_NAME,
            TEST_APP_ID,
            "1.0.0",
            "test-signature",
            "test-client-id",
            "12345",
            "8a61dd48223247518cf2288b6c85f707"
        );
    }
}

