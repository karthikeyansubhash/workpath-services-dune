/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Solution;

public class SolutionResourceFacade extends BaseResourceFacade implements SolutionResource {

    public static final String name = "solution";

    private CertificateAuthoritiesResourceFacade certificateAuthorities = null;
    private ContextResourceFacade context = null;
    private ReissueInstallCodeOperationResourceFacade reissueInstallCode = null;
    private ConfigurationResourceFacade configuration=null;
    private RuntimeRegistrationsResourceFacade runtimeRegistrations = null;

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path       for solution end point
     */
    public SolutionResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        certificateAuthorities = new CertificateAuthoritiesResourceFacade(httpClient, serviceUri,
                path + "/" + CertificateAuthoritiesResourceFacade.name);
        context = new ContextResourceFacade(httpClient, serviceUri, path + "/" + ContextResourceFacade.name);
        reissueInstallCode = new ReissueInstallCodeOperationResourceFacade(httpClient, serviceUri,
                path + "/" + ReissueInstallCodeOperationResourceFacade.name);
        configuration= new ConfigurationResourceFacade(httpClient, serviceUri,
                path + "/" + ConfigurationResourceFacade.name);
        runtimeRegistrations = new RuntimeRegistrationsResourceFacade(httpClient, serviceUri,
                path + "/" + RuntimeRegistrationsResourceFacade.name);

    }

    @Override
    public CompletableFuture<Solution> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Solution.class);
    }

    public CompletableFuture<Solution> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CertificateAuthoritiesResourceFacade certificateAuthorities() {
        return certificateAuthorities;
    }

    @Override
    public ContextResourceFacade context() {
        return context;
    }

    @Override
    public ReissueInstallCodeOperationResourceFacade reissueInstallCode() {
        return reissueInstallCode;
    }

    @Override
    public ConfigurationResourceFacade configuration() {
        return configuration;
    }

    @Override
    public RuntimeRegistrationsResourceFacade runtimeRegistrations() {
        return runtimeRegistrations;
    }
}
