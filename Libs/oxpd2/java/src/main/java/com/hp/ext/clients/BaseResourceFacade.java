/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;

public class BaseResourceFacade implements ResourceFacade {

    private HttpClient httpClient;
    private URI serviceUri;
    private String path;

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public URI getServiceUri() {
        return this.serviceUri;
    }

    protected HttpClient getHttpClient() {
        return this.httpClient;
    }

    protected BaseResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        this.httpClient = httpClient;
        this.serviceUri = serviceUri;
        this.path = path;
    }

    protected URI createRequestUri(URI serviceUri, String path, String queryParams) throws URISyntaxException {
        URI requestUri;
        
        String canonicalServiceUri = serviceUri.toString();

        // Remove '/'
        if (canonicalServiceUri.endsWith("/")) {
            canonicalServiceUri = canonicalServiceUri.substring(0, canonicalServiceUri.length() - 1);
        }
        serviceUri = new URI(canonicalServiceUri);

        path = (path == null) ? "" : path;

        if (!path.startsWith("/") &&  !path.isEmpty()) {
            path = "/" + path;
        }

        queryParams = (queryParams== null || queryParams.isEmpty()) ? null :queryParams;
        requestUri = new URI(serviceUri.getScheme(), serviceUri.getAuthority(), serviceUri.getPath().concat(path) , queryParams, null);

        return requestUri;
    }
}
