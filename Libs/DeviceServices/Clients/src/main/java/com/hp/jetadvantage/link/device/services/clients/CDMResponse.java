/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

public class CDMResponse<T> {
    public final Integer httpStatusCode;
    public final T httpBody;
    public final String contentType;

    /**
     * Constructor for a CDMResponse.
     *
     * @param status Http status code
     * @param body   Http body
     */
    public CDMResponse(Integer status, T body) {
        this.httpStatusCode = status;
        this.httpBody = body;
        this.contentType = null;
    }

    public CDMResponse(Integer status, T body, String contentType) {
        this.httpStatusCode = status;
        this.httpBody = body;
        this.contentType = contentType;
    }

    /**
     * Convenience method for creating an CDMResponse.
     *
     * @param a the Http status code
     * @param b the Http body
     * @return CDMResponse object
     */
    public static <T> CDMResponse<T> create(Integer a, T b) {
        return new CDMResponse<>(a, b);
    }

    public static <T> CDMResponse<T> create(Integer a, T b, String c) {
        return new CDMResponse<>(a, b, c);
    }

    @Override
    public String toString() {
        return "CDMResponse{" + String.valueOf(httpStatusCode) + "," + String.valueOf(httpBody) + "}";
    }
}
