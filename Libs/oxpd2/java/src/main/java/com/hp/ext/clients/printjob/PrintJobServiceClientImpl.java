/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.printjob;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class PrintJobServiceClientImpl extends ServiceClient implements PrintJobServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private PrintJobAgentsResourceFacade printJobAgents = null;

    public PrintJobServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
    }

    public PrintJobServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), discoveredServices);
    }

    public PrintJobServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/printJob/v1/")));
    }

    public PrintJobServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.printJob.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/printJob/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public PrintJobAgentsResourceFacade printJobAgents() {
        return printJobAgents;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> printServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices
                            .getServices().stream()
                            .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                                    .equalsIgnoreCase(getServiceVersion()))
                            .findAny()
                    : Optional.empty());
            if (printServiceMetadata.isPresent()) {
                for (Link link : printServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (PrintJobAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        printJobAgents = new PrintJobAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
