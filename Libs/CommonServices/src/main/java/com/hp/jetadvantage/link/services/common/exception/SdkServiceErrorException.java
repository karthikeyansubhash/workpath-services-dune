// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkServiceErrorException extends SdkException {

    private static final long serialVersionUID = -8249825540483943402L;

    public SdkServiceErrorException(Throwable e){
        super(e);
    }

    public SdkServiceErrorException(String message) {
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.SERVICE_ERROR;
    }
}
