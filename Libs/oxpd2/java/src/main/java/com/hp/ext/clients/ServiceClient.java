/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URI;
import com.hp.net.http.HttpClient;

public abstract class ServiceClient {

    private HttpClient httpClient;
    private URI serviceUri;
    public final static String SERVICE_URI_FORMAT = "%s://%s%s";

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    protected void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public URI getServiceUri() {
        return this.serviceUri;
    }

    protected void setServiceUri(URI serviceUri) {
        this.serviceUri = serviceUri;
    }

    protected ServiceClient(HttpClient httpClient){
        this.httpClient = httpClient;
    }

    protected ServiceClient(HttpClient httpClient, URI serviceUri) {
        this.httpClient = httpClient;
        this.serviceUri = serviceUri;
    }

    public abstract String getServiceVersion();
    public abstract String getServiceRoot();
}
