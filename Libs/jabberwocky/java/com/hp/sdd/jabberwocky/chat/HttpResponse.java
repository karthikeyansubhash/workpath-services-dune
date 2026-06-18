// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * HTTP response container
 */
@SuppressWarnings("WeakerAccess")
public final class HttpResponse {
	/**
	 * HttpURLConnection associated with response
	 */
	private final HttpURLConnection mURLConnection;

	/**
	 * HttpResponse constructor and initializer
	 * @param request
	 * 				HttpRequest associated with response
     */
    HttpResponse(HttpRequest request) {
        mURLConnection = request.mURLConnection;
    }

	/**
	 * Get the HTTP response headers
	 * @return
	 * 				List of {@link HttpHeader} received from the response
     */
    public List<HttpHeader> getHeaders() {
		return HttpUtils.getHeadersFromMap(mURLConnection.getHeaderFields());
    }

	/**
	 * Lookup the specified HttpHeader by name
	 * @param headerKey
	 * 				Specific HttpHeader to lookup
	 * @return
	 * 				HttpHeader if key is found;
	 * 				null otherwise
     */
	public HttpHeader getHeader(String headerKey) {
		if (headerKey == null) return null;
		String headerValue = mURLConnection.getHeaderField(headerKey);
		return ((headerValue != null) ? HttpHeader.create(headerKey, headerValue) : null);
	}

	/**
	 * Get the input stream to read the response data from
	 * @return
	 * 				InputStream to read data from
	 * @throws IOException
	 * 				If an error occurs
     */
	public InputStream getInputStream() throws IOException {
		return mURLConnection.getInputStream();
	}

	/**
	 * Return the HTTP status code
	 * @return
	 * 				HTTP status code
     */
	public int getResponseCode() {
		int response = 0;
		try {
			response = mURLConnection.getResponseCode();
		} catch(IOException ignored) {}
		return response;
	}

	/**
	 * Does the response have data to read?
	 * @return
	 * 				true if there is data to read;
	 * 				false otherwise
     */
    public boolean hasInput() {
        return mURLConnection.getDoInput();
    }

	/**
	 * Return the HTTP status code or exception
	 * @return
	 * 				HTTP status code
	 * @throws IOException
	 * 				if an error occurs
     */
	int getResponseCodeWithException() throws IOException {
		return mURLConnection.getResponseCode();
	}

	/**
	 * Return the response content type
	 * @return
	 * 				Response mime-type
     */
	public String getContentType() {
		return mURLConnection.getContentType();
	}

	/**
	 * Return the response length
	 * @return
	 * 				Response data length in bytes
     */
	public int getContentLength() {
		return mURLConnection.getContentLength();
	}

	/**
	 * Disconnect the HttpUrlConnection
	 */
    public void disconnect() {
        mURLConnection.disconnect();
    }

}
