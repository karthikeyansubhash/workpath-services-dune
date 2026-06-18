/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static com.hp.ext.types.common.BooleanOrUnknown.False;
import static com.hp.ext.types.common.BooleanOrUnknown.True;
import static com.hp.ext.types.common.BooleanOrUnknown.Unknown;

import com.hp.ext.service.device.Scanner;
import com.hp.ext.types.common.BooleanOrUnknown;
import com.hp.jetadvantage.link.api.scanner.StatusInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

public class ScanDeviceStatusAdapter {
    public static String getStatus(IDeviceScanJobService scanJobService) throws Exception {
        Scanner scanner = scanJobService.getScannerStatus();

        if (scanner != null) {
            StatusInfo status = new StatusInfo(scanner.getIsOnline(),
                    scanner.getIsBusy(),
                    convertToStatusCondition(scanner.getAdfOutputBinIsFull()),
                    convertToStatusCondition(scanner.getHasPaperInAdf()),
                    convertToStatusCondition(scanner.getHasPaperOnFlatbed()));

            return JsonParser.getInstance().toJson(status);
        }

        throw new SdkServiceErrorException("Failed to retrieve scanner status from the device");
    }

    public static StatusInfo.StatusCondition convertToStatusCondition(BooleanOrUnknown status) {
        if (status == Unknown) {
            return StatusInfo.StatusCondition.UNKNOWN;
        } else if (status == True) {
            return StatusInfo.StatusCondition.TRUE;
        } else if (status == False) {
            return StatusInfo.StatusCondition.FALSE;
        } else {
            return null;
        }
    }
}
