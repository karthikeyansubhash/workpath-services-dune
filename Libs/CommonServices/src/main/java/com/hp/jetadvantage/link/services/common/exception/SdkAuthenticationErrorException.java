// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkAuthenticationErrorException extends SdkException {

    private static final long serialVersionUID = 4995220243992239421L;

    public SdkAuthenticationErrorException(Throwable e){
        super(e);
    }

    public SdkAuthenticationErrorException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.AUTHENTICATION_ERROR;
    }
}
