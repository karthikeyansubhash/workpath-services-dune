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
import com.hp.ext.service.solutionManager.CertificateAuthority;
import com.hp.ext.types.base.DeleteContent;

public class CertificateAuthorityResourceFacade extends BaseResourceFacade
implements CertificateAuthorityResource {

    public static final String name = "certificateAuthority";

    private ExportCertificateAuthorityOperationResourceFacade export = null;

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path for certificateAuthority end point
     */
    public CertificateAuthorityResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        export = new ExportCertificateAuthorityOperationResourceFacade(httpClient, serviceUri,
                path + "/" + ExportCertificateAuthorityOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<CertificateAuthority> getAsync(String accessToken, String queryParams)
                    throws URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), requestUri, accessToken,CertificateAuthority.class);
    }

    public CompletableFuture<CertificateAuthority> getAsync(String accessToken)
            throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams) throws URISyntaxException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(), requestUri, accessToken, DeleteContent.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }

    public ExportCertificateAuthorityOperationResourceFacade export() {
        return export;
    }

}
