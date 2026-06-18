/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class SdkSupportServiceClientImpl extends ServiceClient implements SdkSupportServiceClient {

    private EchoResourceFacade echo = null;
    private FunctionsResourceFacade functions = null;
    private SimulatedHidDevicesResourceFacade simulatedHidDevices = null;
    private ControlPanelResourceFacade controlPanel = null;

    /**
     * The standard production constructor.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the service host
     * @param discoveredServices The discovery-tree retrieved from the host
     * @throws URISyntaxException
     */
    public SdkSupportServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
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
    public SdkSupportServiceClientImpl(HttpClient httpClient, URI serviceUri,
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
    public SdkSupportServiceClientImpl(HttpClient httpClient, String host)
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
    public SdkSupportServiceClientImpl(HttpClient httpClient, URI serviceUri) {
        this(httpClient, serviceUri, null);
    }

    @Override
    public EchoResourceFacade echo() {
        return echo;
    }

    @Override
    public FunctionsResourceFacade functions() {
        return functions;
    }

    @Override
    public SimulatedHidDevicesResourceFacade simulatedHidDevices() {
        return simulatedHidDevices;
    }

    @Override
    public ControlPanelResourceFacade controlPanel() {
        return controlPanel;
    }

    @Override
    public String getServiceVersion() {
        return "com.hp.ext.service.sdkSupport.version.1";
    }

    @Override
    public String getServiceRoot() {
        return "/ext/sdkSupport/v1/";
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            echo = new EchoResourceFacade(getHttpClient(), getServiceUri(), EchoResourceFacade.name);
            functions = new FunctionsResourceFacade(getHttpClient(), getServiceUri(),
                    FunctionsResourceFacade.name);
            simulatedHidDevices = new SimulatedHidDevicesResourceFacade(getHttpClient(), getServiceUri(),
                    SimulatedHidDevicesResourceFacade.name);
            controlPanel = new ControlPanelResourceFacade(getHttpClient(), getServiceUri(),
                    ControlPanelResourceFacade.name);
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
                    if (EchoResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        echo = new EchoResourceFacade(getHttpClient(), getServiceUri(), link.getHref());
                        continue;
                    }
                    if (FunctionsResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        functions = new FunctionsResourceFacade(getHttpClient(), getServiceUri(), link.getHref());
                        continue;
                    }
                    if (SimulatedHidDevicesResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        simulatedHidDevices = new SimulatedHidDevicesResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                        continue;
                    }
                    if (ControlPanelResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        controlPanel = new ControlPanelResourceFacade(getHttpClient(), getServiceUri(), link.getHref());
                        continue;
                    }
                }
            }
        }
    }
}
