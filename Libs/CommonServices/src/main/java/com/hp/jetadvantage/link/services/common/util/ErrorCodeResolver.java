/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.services.common.util;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;

import java.io.IOException;

public class ErrorCodeResolver {

    private static boolean isDeviceConnectionError(Throwable error) {
        boolean isConnectionError = false;
        if (error instanceof BoundDeviceException) {
            isConnectionError = true;
        } else if (error instanceof IOException) {
            isConnectionError = true;
        }
        return isConnectionError;
    }

    public static Result.ErrorCode resolve(Throwable error, Result.ErrorCode defaultErrorCode) {
        return (isDeviceConnectionError(error)) ? Result.ErrorCode.CONNECTION_ERROR : defaultErrorCode;
    }
}
