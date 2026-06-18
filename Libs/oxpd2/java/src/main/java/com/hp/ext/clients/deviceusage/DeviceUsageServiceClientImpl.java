/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class DeviceUsageServiceClientImpl extends ServiceClient implements DeviceUsageServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private DeviceUsageAgentsResourceFacade deviceUsageAgents = null;

    /**
     * The standard production constructor.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public DeviceUsageServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public DeviceUsageServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
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
     * @param host       The network address of the Device Usage service host
     * @throws URISyntaxException
     */
    public DeviceUsageServiceClientImpl(HttpClient httpClient, String host)
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
    public DeviceUsageServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public DeviceUsageAgentsResourceFacade deviceUsageAgents() {
        return deviceUsageAgents;
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.deviceUsage.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/deviceUsage/v1/";
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
                    : null);
            if (Optional.ofNullable(appServiceMetadata).isPresent()) {
                for (Link link : appServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(), link.getHref());
                        continue;
                    }
                    if (DeviceUsageAgentsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        deviceUsageAgents = new DeviceUsageAgentsResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
