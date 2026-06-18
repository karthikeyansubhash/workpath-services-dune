// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkConnectionErrorException extends SdkException {

    private static final long serialVersionUID = -8633327793387587234L;

    public SdkConnectionErrorException(Throwable e){
        super(e);
    }

    public SdkConnectionErrorException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.CONNECTION_ERROR;
    }
}
