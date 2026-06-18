/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;

import com.hp.ext.clients.ServiceClient;

public class DiscoveryServiceClientImpl extends ServiceClient implements DiscoveryServiceClient {

    private ServicesDiscoveryResourceFacade servicesDiscovery = null;

    public DiscoveryServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        super(httpClient, serviceUri);
        initialize();
    }

    public DiscoveryServiceClientImpl(HttpClient httpClient, String host) throws URISyntaxException {
        super(httpClient);
        setServiceUri(new URI(String.format(SERVICE_URI_FORMAT, "https", host, getServiceRoot())));
        initialize();
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.cdm.servicesDiscovery";
    }

    @Override
    public String getServiceRoot() {
        return "/cdm/servicesDiscovery/";
    }

    @Override
    public ServicesDiscoveryResourceFacade servicesDiscovery() {
        return servicesDiscovery;
    }

    private void initialize() {
        servicesDiscovery = new ServicesDiscoveryResourceFacade(getHttpClient(), getServiceUri(),
                ServicesDiscoveryResourceFacade.name);
    }

}
