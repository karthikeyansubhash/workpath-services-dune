// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.exception;

import com.hp.jetadvantage.link.api.Result;

public abstract class SdkException extends Exception {

    private static final long serialVersionUID = 4025842914391609817L;

    public SdkException(Throwable e){
        super(e.getMessage(), e);
    }

    public SdkException(String message) {
        super(message);
    }

    public abstract Result.ErrorCode getErrorCode();

    public Result getResult(){
        return new Result(Result.RESULT_FAIL, getErrorCode(), getMessage());
    }
}
