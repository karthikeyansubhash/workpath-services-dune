/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

/**
 * Types and definition of the Extensibility Authentication Service Client
 */
public class AuthenticationServiceClientImpl extends ServiceClient implements AuthenticationServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private AuthenticationAgentsResourceFacade authenticationAgents = null;
    private AuthenticationAccessPointsResourceFacade authenticationAccessPoints = null;
    private SessionResourceFacade session = null;

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public AuthenticationServiceClientImpl(HttpClient httpClient, URI serviceUri,
            ServicesDiscoveryImpl discoveredServices) {
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
    public AuthenticationServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public AuthenticationServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/authentication/v1/")));
        initialize(null);
    }

    /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public AuthenticationServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.authentication.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/authentication/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public AuthenticationAccessPointsResourceFacade authenticationAccessPoints() {
        return authenticationAccessPoints;
    }

    @Override
    public AuthenticationAgentsResourceFacade authenticationAgents() {
        return authenticationAgents;
    }

    @Override
    public SessionResourceFacade session() {
        return session;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> appServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices
                            .getServices().stream()
                            .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                                    .equalsIgnoreCase(getServiceVersion()))
                            .findAny()
                    : Optional.empty());
            if (appServiceMetadata.isPresent()) {
                for (Link link : appServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (AuthenticationAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        authenticationAgents = new AuthenticationAgentsResourceFacade(getHttpClient(),
                                getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (AuthenticationAccessPointsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        authenticationAccessPoints = new AuthenticationAccessPointsResourceFacade(getHttpClient(),
                                getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (SessionResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        session = new SessionResourceFacade(getHttpClient(),
                                getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }

}
