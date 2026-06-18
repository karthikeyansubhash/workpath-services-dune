/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionDiagnostics.Log;

public class LogResourceFacade extends BaseResourceFacade implements LogResource {

    public static final String name = "log";

    public LogResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SimpleEntry<Log, byte[]>> getAsync(String accessToken, String queryParams)
            throws URISyntaxException {
        CompletableFuture<SimpleEntry<Log, byte[]>> response = null;
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);

        response = ResourceFacadeHelper.getMultipartResponseAsync(getHttpClient(), requestUri, accessToken)
                    .thenApply(LogResourceFacade::multipartResponse);

        return response;

    }

    public CompletableFuture<SimpleEntry<Log, byte[]>> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public static SimpleEntry<Log, byte[]> multipartResponse(List<MultipartResponseItem> list){

        byte[] data = null;
        String contentPart = null;
        Log dataPart = null;
        CustomObjectMapper<Log> mapper = new CustomObjectMapper(Log.class);
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                dataPart = mapper.readValue(contentPart);
            }
            if (multipartResponseItem.getName().equals("data")) {
                data = multipartResponseItem.getContent();
            }
        }
        SimpleEntry<Log, byte[]> map = new SimpleEntry<Log, byte[]>(dataPart, data);

        return map;
    }

}
