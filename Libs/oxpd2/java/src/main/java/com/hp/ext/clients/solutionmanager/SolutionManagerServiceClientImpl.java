/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class SolutionManagerServiceClientImpl extends ServiceClient implements SolutionManagerServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private InstallerResourceFacade installer = null;
    private SolutionsResourceFacade solutions = null;

    /**
     * The standard production constructor. It constructs the underlying resource
     * endpoints based on the Solution Manager
     * service node from the provided discovery-tree.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public SolutionManagerServiceClientImpl(HttpClient httpClient, String host,
            ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), discoveredServices);
    }

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree.
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public SolutionManagerServiceClientImpl(HttpClient httpClient, URI serviceUri,
            ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        this.initialize(discoveredServices);
    }

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree.
     * This constructor will initialize the client using the default/well-known
     * service URI
     * based on the provided serviceUri and is provided for test/debug purposes
     * only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public SolutionManagerServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree.
     * This constructor will initialize the client using the default/well-known
     * service URI
     * based on the provided host and is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public SolutionManagerServiceClientImpl(HttpClient httpClient, String host) throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.solutionManager.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/solutionManager/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return this.capabilities;
    }

    @Override
    public InstallerResourceFacade installer() {
        return this.installer;
    }

    @Override
    public SolutionsResourceFacade solutions() {
        return this.solutions;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (discoveredServices == null) {
            this.capabilities = new CapabilitiesResourceFacade(super.getHttpClient(), super.getServiceUri(),
                    CapabilitiesResourceFacade.name);
            this.installer = new InstallerResourceFacade(super.getHttpClient(), super.getServiceUri(),
                    InstallerResourceFacade.name);
            this.solutions = new SolutionsResourceFacade(super.getHttpClient(), super.getServiceUri(),
                    SolutionsResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> appServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices.getServices().stream()
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
                    if (InstallerResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        installer = new InstallerResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (SolutionsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        solutions = new SolutionsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (SolutionsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        solutions = new SolutionsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;

                    }
                }
            }
        }
    }

}
