/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class SolutionDiagnosticsServiceClientImpl extends ServiceClient implements SolutionDiagnosticsServiceClient {


    private CapabilitiesResourceFacade capabilities = null;
    private SolutionDiagnosticsAgentsResourceFacade solutionDiagnosticsAgents = null;
    
    /**
     * The standard production constructor.
     * 
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public SolutionDiagnosticsServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
    }

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the Application service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public SolutionDiagnosticsServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public SolutionDiagnosticsServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/solutionDiagnostics/v1/")));
    }

    /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public SolutionDiagnosticsServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return this.capabilities;
    }

    @Override
    public SolutionDiagnosticsAgentsResourceFacade solutionDiagnosticsAgents() {
        return this.solutionDiagnosticsAgents;
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.solutionDiagnostics.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/solutionDiagnostics/v1/";
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);

            solutionDiagnosticsAgents = new SolutionDiagnosticsAgentsResourceFacade(getHttpClient(), getServiceUri(), 
                    SolutionDiagnosticsAgentsResourceFacade.name);
            
        } else {
            Optional<ServiceMetadataImpl> solutionDiagnosticsServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices.getServices().stream()
                    .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                            .equalsIgnoreCase(getServiceVersion()))
                    .findAny()
                    : Optional.empty());
            if (solutionDiagnosticsServiceMetadata.isPresent()) {
                for (Link link : solutionDiagnosticsServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (SolutionDiagnosticsAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        solutionDiagnosticsAgents = new SolutionDiagnosticsAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    
                }
            }
        }
    }
    
}
