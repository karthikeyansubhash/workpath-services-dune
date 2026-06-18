/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.CertificateAuthority_Export;

public class ExportCertificateAuthorityOperationResourceFacade extends BaseResourceFacade
implements ExportCertificateAuthorityOperationResource {

    public static final String name = "export";

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path for export end point
     */
    public ExportCertificateAuthorityOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SimpleEntry<CertificateAuthority_Export, byte[]>> executeAsync(String accessToken, String queryParams) throws URISyntaxException, IOException{
        CompletableFuture<SimpleEntry<CertificateAuthority_Export, byte[]>> result = null;
        URI requestUri = createRequestUri(super.getServiceUri(), super.getPath(), queryParams);
        result = ResourceFacadeHelper.executeMultipartResourceOperationAsync(super.getHttpClient(), requestUri,
                accessToken, null).thenApply(ExportCertificateAuthorityOperationResourceFacade::multipartResponse);

        return result;
    }

    public CompletableFuture<SimpleEntry<CertificateAuthority_Export, byte[]>> executeAsync(String accessToken) throws URISyntaxException, IOException{
        return executeAsync(accessToken, null);
    }

    public static SimpleEntry<CertificateAuthority_Export, byte[]> multipartResponse(List<MultipartResponseItem> list) {
        byte[] data = null;
        String contentPart = null;
        CertificateAuthority_Export dataPart = null;
        CustomObjectMapper<CertificateAuthority_Export> mapper = new CustomObjectMapper(CertificateAuthority_Export.class);
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                dataPart = mapper.readValue(contentPart);
            }
            if (multipartResponseItem.getName().equals("certificate")) {
                data = multipartResponseItem.getContent();
            }
        }
        SimpleEntry<CertificateAuthority_Export, byte[]> map = new SimpleEntry<CertificateAuthority_Export, byte[]>(dataPart, data);
        return map;
    }

}
