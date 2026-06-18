/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Data;
import com.hp.ext.service.solutionManager.Data_Replace;
import com.hp.net.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class DataResourceFacade extends BaseResourceFacade implements DataResource {

    public static final String name = "data";

    /**
     * @param httpClient
     * @param serviceUri
     * @param path
     */
    public DataResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SimpleEntry<Data, byte[]>> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        CompletableFuture<SimpleEntry<Data, byte[]>> response = null;
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);

        response = ResourceFacadeHelper.getMultipartResponseAsync(getHttpClient(), requestUri, accessToken)
                    .thenApply(DataResourceFacade::multipartResponse);
        return response;
    }



    public CompletableFuture<SimpleEntry<Data, byte[]>> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<Data> replaceAsync(String accessToken, InputStream dataStream, String mimeType,
            String queryParams) throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(getServiceUri(), getPath(), queryParams);
        CustomObjectMapper<Data_Replace> mapper = new CustomObjectMapper(Data_Replace.class);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.MIXED);
        RequestBody contentBody = RequestBody.create(
            mapper.writeValueAsString(new Data_Replace()),
            MediaType.parse("application/json")
        );

        builder.addPart(
            okhttp3.Headers.of("Content-Disposition", "attachment; name=\"content\""),
            contentBody
        );
        RequestBody dataBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse(mimeType);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeAll(okio.Okio.source(dataStream));
            }
        };

        builder.addPart(
            okhttp3.Headers.of("Content-Disposition", "attachment; name=\"data\""),
            dataBody
        );

        return ResourceFacadeHelper.replaceResourceAsync(super.getHttpClient(), requestUri,
                accessToken, builder.build(), Data.class);
    }

    public CompletableFuture<Data> replaceAsync(String accessToken, InputStream dataStream, String mimeType) throws URISyntaxException, IOException {
        return replaceAsync(accessToken, dataStream, mimeType, null);
    }

    public static SimpleEntry<Data, byte[]> multipartResponse(List<MultipartResponseItem> list) {
        byte[] data = null;
        String contentPart = null;
        Data dataPart = null;
        CustomObjectMapper<Data> mapper = new CustomObjectMapper(Data.class);
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                dataPart = mapper.readValue(contentPart);
            }
            if (multipartResponseItem.getName().equals("data")) {
                data = multipartResponseItem.getContent();
            }
        }
        SimpleEntry<Data, byte[]> map = new SimpleEntry<Data, byte[]>(dataPart, data);
        return map;
    }
}
