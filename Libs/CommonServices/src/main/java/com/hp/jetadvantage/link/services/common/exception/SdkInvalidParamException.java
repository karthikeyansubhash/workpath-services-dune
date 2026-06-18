// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkInvalidParamException extends SdkException {

    private static final long serialVersionUID = 8273707215508705567L;

    public SdkInvalidParamException(Throwable e){
        super(e);
    }

    public SdkInvalidParamException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.INVALID_PARAM;
    }
}
