/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Context_Replace;
import com.hp.ext.service.solutionManager.InstallSolutionRequest;
import com.hp.ext.service.solutionManager.Installer_InstallSolution;
import com.hp.net.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import javax.mail.internet.ParseException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Okio;

public class InstallSolutionOperationResourceFacade extends BaseResourceFacade implements InstallSolutionOperationResource {

    public static final String name = "installSolution";

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path       The path to the Installer_InstallSolution endpoint.
     */
    public InstallSolutionOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Installer_InstallSolution> executeAsync(String accessToken, InstallSolutionRequest installSolutionRequest, InputStream solutionBundle,
                                                                     String solutionBundleFilename, Context_Replace context, String queryParams) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException {

        URI uri = createRequestUri(getServiceUri(), getPath(), queryParams);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.MIXED);

        requestBodyBuilder.addFormDataPart("content", "content.json",
            RequestBody.create(mapper.writeValueAsString(installSolutionRequest),
                MediaType.parse("application/json")));

        if (solutionBundle != null) {
            RequestBody solutionBody = new RequestBody() {
                @Override
                public okhttp3.MediaType contentType() {
                    return okhttp3.MediaType.parse("application/vnd.hp.solution-bundle");
                }

                @Override
                public void writeTo(okio.BufferedSink sink) throws IOException {
                    try (okio.Source source = Okio.source(solutionBundle)) {
                        sink.writeAll(source);
                    }
                }
            };
            String filename = !solutionBundleFilename.isEmpty() ? solutionBundleFilename : null;
            requestBodyBuilder.addFormDataPart("solution", filename, solutionBody);
        }

        if (context != null) {
            String contextJson = mapper.writeValueAsString(context);
            RequestBody contextBody = RequestBody.create(
                contextJson,
                MediaType.parse("application/json")
            );
            requestBodyBuilder.addFormDataPart("context", null, contextBody);
        }

        return ResourceFacadeHelper.executeResourceOperationAsync(super.getHttpClient(), uri, accessToken, requestBodyBuilder.build(), Installer_InstallSolution.class);
    }

    public CompletableFuture<Installer_InstallSolution> executeAsync(String accessToken, InstallSolutionRequest installSolutionRequest, InputStream solutionBundle,
                                                                     String solutionBundleFilename, Context_Replace context) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException {
        return executeAsync(accessToken, installSolutionRequest, solutionBundle, solutionBundleFilename, context, null);
    }

}
