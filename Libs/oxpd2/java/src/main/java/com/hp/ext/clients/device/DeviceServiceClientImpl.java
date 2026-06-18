/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.device;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

/**
 * Types and definition of the Extensibility Device Service Client
 */
public class DeviceServiceClientImpl extends ServiceClient implements DeviceServiceClient {

    private CapabilitiesResourceFacade capabilities = null;
    private IdentityResourceFacade identity = null;
    private StatusResourceFacade status = null;
    private ScannerResourceFacade scanner = null;
    private EmailResourceFacade email = null;
    private DeploymentInformationResourceFacade deploymentInformation = null;

    /**
     * Alternate constructor for using a specified service URI along with a
     * discovery-tree
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     */
    public DeviceServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices) {
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
    public DeviceServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public DeviceServiceClientImpl(HttpClient httpClient, String host)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "/ext/device/v1/")));
        initialize(null);
    }

    /**
     * Alternate constructor allowing the complete service URI to be passed in
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     */
    public DeviceServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.device.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/device/v1/";
    }

    @Override
    public CapabilitiesResourceFacade capabilities() {
        return capabilities;
    }

    @Override
    public IdentityResourceFacade identity() {
        return identity;
    }

    @Override
    public StatusResourceFacade status() {
        return status;
    }

    @Override
    public ScannerResourceFacade scanner() {
        return scanner;
    }

    @Override
    public EmailResourceFacade email() {
        return email;
    }

    @Override
    public DeploymentInformationResourceFacade deploymentInformation() {
        return deploymentInformation;
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                    CapabilitiesResourceFacade.name);
            identity = new IdentityResourceFacade(getHttpClient(), getServiceUri(),
                    IdentityResourceFacade.name);
            status = new StatusResourceFacade(getHttpClient(), getServiceUri(),
                    StatusResourceFacade.name);
            scanner = new ScannerResourceFacade(getHttpClient(), getServiceUri(),
                    ScannerResourceFacade.name);
            email = new EmailResourceFacade(getHttpClient(), getServiceUri(),
                    EmailResourceFacade.name);
            deploymentInformation = new DeploymentInformationResourceFacade(getHttpClient(), getServiceUri(),
                    DeploymentInformationResourceFacade.name);
        } else {
            Optional<ServiceMetadataImpl> deviceServiceMetadata = (null != discoveredServices.getServices()
                    ? discoveredServices
                            .getServices().stream()
                            .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                                    .equalsIgnoreCase(getServiceVersion()))
                            .findAny()
                    : Optional.empty());
            if (deviceServiceMetadata.isPresent()) {
                for (Link link : deviceServiceMetadata.get().getLinks()) {
                    if (CapabilitiesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        capabilities = new CapabilitiesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (IdentityResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        identity = new IdentityResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                    if (StatusResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        status = new StatusResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                    if (ScannerResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        scanner = new ScannerResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                    if (EmailResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        email = new EmailResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                    if (DeploymentInformationResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        deploymentInformation = new DeploymentInformationResourceFacade(getHttpClient(),
                                getServiceUri(), link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
