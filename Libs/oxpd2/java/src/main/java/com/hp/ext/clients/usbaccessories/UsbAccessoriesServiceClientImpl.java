/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class UsbAccessoriesServiceClientImpl extends ServiceClient implements UsbAccessoriesServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private UsbAccessoriesAgentsResourceFacade usbAccessoriesAgents = null;
    private AccessoriesResourceFacade accessories = null;

    /**
     * The standard production constructor.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public UsbAccessoriesServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
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
    public UsbAccessoriesServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public UsbAccessoriesServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/usbAccessories/v1/")));
    }

    /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public UsbAccessoriesServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.usbAccessories.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/usbAccessories/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public UsbAccessoriesAgentsResourceFacade usbAccessoriesAgents() {
        return this.usbAccessoriesAgents;
    }


    @Override
    public AccessoriesResourceFacade accessories() {
        return this.accessories;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
            usbAccessoriesAgents = new UsbAccessoriesAgentsResourceFacade(getHttpClient(), getServiceUri(),
                    UsbAccessoriesAgentsResourceFacade.name);
            accessories = new AccessoriesResourceFacade(getHttpClient(), getServiceUri(),
                    AccessoriesResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> usbServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices
                            .getServices().stream()
                            .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                                    .equalsIgnoreCase(getServiceVersion()))
                            .findAny()
                    : Optional.empty());
            if (usbServiceMetadata.isPresent()) {
                for (Link link : usbServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (UsbAccessoriesAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        usbAccessoriesAgents = new UsbAccessoriesAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (AccessoriesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        accessories = new AccessoriesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }

}
