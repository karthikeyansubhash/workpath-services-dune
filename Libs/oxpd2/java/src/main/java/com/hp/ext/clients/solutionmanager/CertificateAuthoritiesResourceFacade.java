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
import com.hp.ext.clients.CollectionResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.CertificateAuthorities;

public class CertificateAuthoritiesResourceFacade extends BaseResourceFacade
implements CollectionResourceFacade<CertificateAuthorityResource>, CertificateAuthoritiesResource {

    public static final String name = "certificateAuthorities";

    private ExportCertificateAuthoritiesOperationResourceFacade export = null;
    private ImportCertificateAuthoritiesOperationResourceFacade importCertificateAuthorities = null;

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path       for certificateAuthorities end point
     */
    public CertificateAuthoritiesResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        export = new ExportCertificateAuthoritiesOperationResourceFacade(httpClient, serviceUri,
                path + "/" + ExportCertificateAuthoritiesOperationResourceFacade.name);
        importCertificateAuthorities = new ImportCertificateAuthoritiesOperationResourceFacade(httpClient, serviceUri,
                path + "/" + ImportCertificateAuthoritiesOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<CertificateAuthorities> getAsync(String accessToken, String queryParams)
                    throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, CertificateAuthorities.class);
    }

    public CompletableFuture<CertificateAuthorities> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public ExportCertificateAuthoritiesOperationResourceFacade exportCertificateAuthorities() {
        return export;
    }

    @Override
    public ImportCertificateAuthoritiesOperationResourceFacade importCertificateAuthorities() {
        return importCertificateAuthorities;
    }

    @Override
    public CertificateAuthorityResourceFacade getMember(String id) {
        CertificateAuthorityResourceFacade certificateAuthorityResourceFacade = new CertificateAuthorityResourceFacade(
                getHttpClient(), getServiceUri(), getPath() + "/" + id);
        return certificateAuthorityResourceFacade;
    }


}
