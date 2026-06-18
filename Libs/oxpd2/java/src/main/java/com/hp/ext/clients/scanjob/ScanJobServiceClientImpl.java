/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class ScanJobServiceClientImpl extends ServiceClient implements ScanJobServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private ScanJobAgentsResourceFacade scanJobAgents = null;
    private DefaultOptionsResourceFacade defaultOptions = null;
    private ProfileResourceFacade profile = null;

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public ScanJobServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
    }

    /**
     * The standard production constructor.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public ScanJobServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), discoveredServices);
    }

    /**
     * Alternate constructor that will use create a default/well-known service URI
     * This constructor will initialize the client using the default/well-known
     * service URI
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param host       The network address of the Application service host
     * @throws URISyntaxException
     */
    public ScanJobServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/scanJob/v1/")));
    }

     /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public ScanJobServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.scanJob.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/scanJob/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public ScanJobAgentsResourceFacade scanJobAgents() {
        return scanJobAgents;
    }

    @Override
    public DefaultOptionsResourceFacade defaultOptions() {
        return defaultOptions;
    }

    @Override
    public ProfileResourceFacade profile() {
        return profile;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> scanServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices
                            .getServices().stream()
                            .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                                    .equalsIgnoreCase(getServiceVersion()))
                            .findAny()
                    : Optional.empty());
            if (scanServiceMetadata.isPresent()) {
                for (Link link : scanServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (ScanJobAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        scanJobAgents = new ScanJobAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (DefaultOptionsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        defaultOptions = new DefaultOptionsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (ProfileResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        profile = new ProfileResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
