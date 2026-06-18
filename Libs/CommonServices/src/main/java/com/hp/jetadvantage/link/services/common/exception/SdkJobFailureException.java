// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public class SdkJobFailureException extends SdkException {

    private static final long serialVersionUID = 4220903388219062782L;

    public SdkJobFailureException(Throwable e){
        super(e);
    }

    public SdkJobFailureException(String message){
        super(message);
    }

    @Override
    public Result.ErrorCode getErrorCode() {
        return Result.ErrorCode.JOB_FAILURE;
    }
}
