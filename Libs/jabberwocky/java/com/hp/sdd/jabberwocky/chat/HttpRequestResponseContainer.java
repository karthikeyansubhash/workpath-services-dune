// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

/**
 * Container class to hold an HTTP request/response pair
 * This way we can pass them around together for logging purposes
 */
public final class HttpRequestResponseContainer {

	/** HTTP request */
	public final HttpRequest request;

	/** HTTP response */
	public final HttpResponse response;

	/** Exception, if any */
	public final Exception exception;

	/** Processed payload */
	public String payload;

	/**
	 * HTTP Request/Response pair constructor
	 */
	private HttpRequestResponseContainer(HttpRequest request,
                                         HttpResponse response,
                                         Exception exception) {
		this.request   = request;
        this.response  = response;
        this.exception = exception;
	}

    /**
     * Builder class
     */
    static class Builder {
        private final HttpRequest mRequest;

        /** Request-less constructor */
        Builder() {
            mRequest = null;
        }

        /** Constructor */
        Builder(HttpRequest request) {
            mRequest = request;
        }

        /** Exception from HTTP request */
        private Exception mException = null;
        /** Set the response exception */
        public Builder setException(Exception e) {
            mException = e;
            return this;
        }

        /** HTTP response */
        private HttpResponse mResponse = null;
        /** Set the response */
        public Builder setResponse(HttpResponse response) {
            mResponse = response;
            return this;
        }

        /** Build the request/response pair */
        public HttpRequestResponseContainer build() {
            return new HttpRequestResponseContainer(mRequest, mResponse, mException);
        }
    }
}
