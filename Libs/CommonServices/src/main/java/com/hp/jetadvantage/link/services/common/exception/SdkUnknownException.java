// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkUnknownException extends SdkException {

    private static final long serialVersionUID = 3029941599901276287L;

    public SdkUnknownException(Throwable e){
        super(e);
    }

    public SdkUnknownException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.UNKNOWN;
    }
}
