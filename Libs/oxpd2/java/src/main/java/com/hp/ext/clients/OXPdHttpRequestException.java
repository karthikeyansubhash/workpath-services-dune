/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import com.hp.ext.types.base.ErrorResponse;

public class OXPdHttpRequestException extends RuntimeException {

    private Integer statusCode;
    private String reasonPhrase;
    private ErrorResponse errorResponse;
    public OXPdHttpRequestException(){}

    public OXPdHttpRequestException(String message) {
        super(message);
    }

    public OXPdHttpRequestException(String message, Exception innerException) {
        super(message,innerException);
    }

    public OXPdHttpRequestException(Integer statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public OXPdHttpRequestException(Integer statusCode, String reasonPhrase) {
        super();
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public OXPdHttpRequestException(Integer statusCode, ErrorResponse error, String reasonPhrase) {
        super();
        this.statusCode = statusCode;
        this.errorResponse = error;
        this.reasonPhrase = reasonPhrase;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }
}
