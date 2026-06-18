// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkNotSupportedException extends SdkException {

    private static final long serialVersionUID = -4580305190768095284L;

    public SdkNotSupportedException(Throwable e){
        super(e);
    }

    public SdkNotSupportedException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.NOT_SUPPORTED;
    }
}
