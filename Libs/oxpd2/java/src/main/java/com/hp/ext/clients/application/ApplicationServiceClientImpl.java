/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class ApplicationServiceClientImpl extends ServiceClient implements ApplicationServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private ApplicationAccessPointsResourceFacade applicationAccessPoints = null;
    private ApplicationAgentsResourceFacade applicationAgents = null;
    private ApplicationRuntimeResourceFacade applicationRuntime = null;
    private HomescreenResourceFacade homescreen = null;
    private I18nAssetsResourceFacade i18nAssets = null;
    private MessageCenterAgentsResourceFacade messageCenterAgents = null;

    /**
     * The standard production constructor.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public ApplicationServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), discoveredServices);
    }

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public ApplicationServiceClientImpl(HttpClient httpClient, URI serviceUri,
            ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
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
    public ApplicationServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        super(httpClient);
        setServiceUri(new URI(String.format(SERVICE_URI_FORMAT, "https", host, getServiceRoot())));
        initialize(null);
    }

    /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public ApplicationServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.application.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/application/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public ApplicationAccessPointsResourceFacade applicationAccessPoints() {
        return applicationAccessPoints;
    }

    @Override
    public ApplicationAgentsResourceFacade applicationAgents() {
        return applicationAgents;
    }

    @Override
    public ApplicationRuntimeResourceFacade applicationRuntime() {
        return applicationRuntime;
    }

    @Override
    public HomescreenResourceFacade homescreen() {
        return homescreen;
    }

    @Override
    public I18nAssetsResourceFacade i18nAssets() {
        return i18nAssets;
    }

    @Override
    public MessageCenterAgentsResourceFacade messageCenterAgents() {
        return messageCenterAgents;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
            applicationAccessPoints = new ApplicationAccessPointsResourceFacade(getHttpClient(), getServiceUri(),
                    ApplicationAccessPointsResourceFacade.name);
            applicationAgents = new ApplicationAgentsResourceFacade(getHttpClient(), getServiceUri(),
                    ApplicationAgentsResourceFacade.name);
            applicationRuntime = new ApplicationRuntimeResourceFacade(getHttpClient(), getServiceUri(),
                    ApplicationRuntimeResourceFacade.name);
            homescreen = new HomescreenResourceFacade(getHttpClient(), getServiceUri(),
                    HomescreenResourceFacade.name);
            i18nAssets = new I18nAssetsResourceFacade(getHttpClient(), getServiceUri(),
                    I18nAssetsResourceFacade.name);
            messageCenterAgents = new MessageCenterAgentsResourceFacade(getHttpClient(), getServiceUri(),
                    MessageCenterAgentsResourceFacade.name);
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
                    if (ApplicationAccessPointsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        applicationAccessPoints = new ApplicationAccessPointsResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                    if (ApplicationAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        applicationAgents = new ApplicationAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (ApplicationRuntimeResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        applicationRuntime = new ApplicationRuntimeResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (HomescreenResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        homescreen = new HomescreenResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (I18nAssetsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        i18nAssets = new I18nAssetsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (MessageCenterAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        messageCenterAgents = new MessageCenterAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
