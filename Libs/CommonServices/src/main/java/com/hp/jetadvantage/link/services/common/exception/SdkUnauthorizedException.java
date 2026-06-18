// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkUnauthorizedException extends SdkException {

    private static final long serialVersionUID = -8946843772553914516L;

    public SdkUnauthorizedException(Throwable e){
        super(e);
    }

    public SdkUnauthorizedException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.UNAUTHORIZED;
    }
}
