/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.Optional;

import com.hp.ext.clients.ServiceClient;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;

public class OAUTH2ServiceClientImpl extends ServiceClient implements OAUTH2ServiceClient {
    private TokenResourceFacade tokenResourceFacade = null;

    /**
     * {@summary The standard production constructor}
     * This is the normal constructor to use in production code. It constructs the
     * underlying
     * resource endpoints based on the Token service node from the provided
     * discovery-tree.
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param host               The network address of the OAUTH2 service host
     * @param discoveredServices The discovery-tree retieved from the host
     * @throws URISyntaxException
     */
    public OAUTH2ServiceClientImpl(HttpClient httpClient, String host, ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        this(httpClient, new URI(String.format(SERVICE_URI_FORMAT, "https", host, "")), discoveredServices);
    }

    /**
     * {@summary Alternate constructor for using a specified service URI along with
     * a discovery-tree}
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient         The instance of the HttpClient to use
     * @param serviceUri         The fully-formed service URI of the OAUTH2 service
     *                           host
     * @param discoveredServices A discovery-tree instance to use
     * @throws URISyntaxException
     */
    public OAUTH2ServiceClientImpl(HttpClient httpClient, URI serviceUri, ServicesDiscoveryImpl discoveredServices)
            throws URISyntaxException {
        super(httpClient, serviceUri);
        initialize(discoveredServices);
    }

    /**
     * {@summary Alternate constructor that will use create a default/well-known
     * service URI}
     *
     * This constructor will initialize the client using the default/well-known
     * service root along with the provided host and is provided for test/debug
     * purposes
     * only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param host       The network address of the OAUTH2 service host
     * @throws URISyntaxException
     */
    public OAUTH2ServiceClientImpl(HttpClient httpClient, String host) throws URISyntaxException {
        super(httpClient);
        setServiceUri(new URI(String.format(SERVICE_URI_FORMAT, "https", host, getServiceRoot())));
        initialize(null);
    }

    /**
     * {@summary Alternate constructor allowing the complete service URI to be
     * passed in}
     * This constructor is provided for test/debug purposes only!
     *
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the OAUTH2 service host
     * @throws URISyntaxException
     */
    public OAUTH2ServiceClientImpl(HttpClient httpClient, URI serviceUri) throws URISyntaxException {
        this(httpClient, serviceUri, null);
    }

    @Override
    public TokenResourceFacade token() {
        return tokenResourceFacade;
    }

    /**
     * {@summary The version of the service supported by this client}
     */
    @Override
    public String getServiceVersion() {
        return "com.hp.cdm.service.oauth2.version.1";
    }

    /**
     * {@summary The default/well-known service root path}
     */
    @Override
    public String getServiceRoot() {
        return "/cdm/oauth2/v1/";
    }

    private void initialize(ServicesDiscoveryImpl discoveredServices) {
        if (null == discoveredServices) {
            tokenResourceFacade = new TokenResourceFacade(getHttpClient(), getServiceUri(),
                    TokenResourceFacade.name);
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
                    if (TokenResourceFacade.name.equalsIgnoreCase(link.getRel())) {
                        tokenResourceFacade = new TokenResourceFacade(getHttpClient(), getServiceUri(),
                                link.getHref());
                    }
                }
            }
        }
    }

}
