package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.hp.ext.service.device.Scanner;
import com.hp.ext.types.common.BooleanOrUnknown;
import com.hp.jetadvantage.link.api.scanner.StatusInfo;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScanDeviceStatusAdapterUnitTest {
    @Mock
    Bundle mockBundle;
    @Mock
    IDeviceScanJobService mockScanJobService;

    @Test
    public void GivenScanDeviceStatusAdapter_WhenConvertToStatusCondition_ThenReturnsStatusCondition() {
        StatusInfo.StatusCondition result = ScanDeviceStatusAdapter.convertToStatusCondition(BooleanOrUnknown.Parse("unknown"));
        assertEquals(StatusInfo.StatusCondition.UNKNOWN, result);

        result = ScanDeviceStatusAdapter.convertToStatusCondition(BooleanOrUnknown.Parse("true"));
        assertEquals(StatusInfo.StatusCondition.TRUE, result);

        result = ScanDeviceStatusAdapter.convertToStatusCondition(BooleanOrUnknown.Parse("false"));
        assertEquals(StatusInfo.StatusCondition.FALSE, result);
    }

    @Test
    public void GivenScanDeviceStatusAdapter_WhenGetStatus_ThenReturnsStatus() {
        Scanner scanner = new Scanner();
        scanner.setIsBusy(true);
        scanner.setIsOnline(true);
        scanner.setAdfOutputBinIsFull(BooleanOrUnknown.True);
        scanner.setHasPaperInAdf(BooleanOrUnknown.True);
        scanner.setHasPaperOnFlatbed(BooleanOrUnknown.True);

        when(mockScanJobService.getScannerStatus()).thenReturn(scanner);
        try {
            String result = ScanDeviceStatusAdapter.getStatus(mockScanJobService);
            assertEquals("{\"mVersion\":9,\"mIsOnline\":true,\"mIsBusy\":true,\"mIsAdfOutputBinFull\":\"TRUE\",\"mIsPaperInAdf\":\"TRUE\"," +
                    "\"mIsPaperInFlatbed\":\"TRUE\"}", result);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void GivenScanDeviceStatusAdapter_WhenGetStatus_ThenReturnsStatus2() {
        Scanner scanner = new Scanner();
        scanner.setIsBusy(false);
        scanner.setIsOnline(false);
        scanner.setAdfOutputBinIsFull(BooleanOrUnknown.False);
        scanner.setHasPaperInAdf(BooleanOrUnknown.False);
        scanner.setHasPaperOnFlatbed(BooleanOrUnknown.False);

        when(mockScanJobService.getScannerStatus()).thenReturn(scanner);
        try {
            String result = ScanDeviceStatusAdapter.getStatus(mockScanJobService);
            assertEquals("{\"mVersion\":9,\"mIsOnline\":false,\"mIsBusy\":false,\"mIsAdfOutputBinFull\":\"FALSE\",\"mIsPaperInAdf\":\"FALSE\"," +
                    "\"mIsPaperInFlatbed\":\"FALSE\"}", result);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void GivenScanDeviceStatusAdapter_WhenGetStatus_ThenReturnsStatus3() {
        Scanner scanner = new Scanner();
        scanner.setIsBusy(false);
        scanner.setIsOnline(false);
        scanner.setAdfOutputBinIsFull(BooleanOrUnknown.Unknown);
        scanner.setHasPaperInAdf(BooleanOrUnknown.Unknown);
        scanner.setHasPaperOnFlatbed(BooleanOrUnknown.Unknown);

        when(mockScanJobService.getScannerStatus()).thenReturn(scanner);
        try {
            String result = ScanDeviceStatusAdapter.getStatus(mockScanJobService);
            assertEquals("{\"mVersion\":9,\"mIsOnline\":false,\"mIsBusy\":false,\"mIsAdfOutputBinFull\":\"UNKNOWN\",\"mIsPaperInAdf\":\"UNKNOWN\"," +
                    "\"mIsPaperInFlatbed\":\"UNKNOWN\"}", result);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void GivenScanDeviceStatusAdapter_WhenGetStatus_ScannerNull_ThenSdkServiceErrorException() {
        when(mockScanJobService.getScannerStatus()).thenReturn(null);
        try {
            String result = ScanDeviceStatusAdapter.getStatus(mockScanJobService);
            fail("Expected an SdkServiceErrorException to be thrown");
        } catch (SdkServiceErrorException e) {
            assertEquals("Failed to retrieve scanner status from the device", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception :" + e.getMessage());
        }
    }
}
