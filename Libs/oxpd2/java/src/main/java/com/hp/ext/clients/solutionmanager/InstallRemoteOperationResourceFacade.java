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
import com.hp.ext.service.solutionManager.InstallRemoteRequest;
import com.hp.ext.service.solutionManager.Installer_InstallRemote;
import com.hp.ext.types.solutionManager.RemoteArchive;
import com.hp.net.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import javax.mail.internet.ParseException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class InstallRemoteOperationResourceFacade extends BaseResourceFacade implements InstallRemoteOperationResource {

    public static final String name = "installRemote";

    /**
     * @param httpClient The instance of the HttpClient to use
     * @param serviceUri The fully-formed service URI of the service host
     * @param path       The path to the Installer_InstallRemote endpoint
     */
    public InstallRemoteOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Installer_InstallRemote> executeAsync(String accessToken, InstallRemoteRequest installRemoteRequest,
                                                                   RemoteArchive remoteArchive, String queryParams) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException {

        URI uri = createRequestUri(getServiceUri(), getPath(), queryParams);
        ObjectMapper mapper = new ObjectMapper();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.MIXED);

        requestBodyBuilder.addFormDataPart("content", "content.json",
            RequestBody.create(
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(installRemoteRequest),
                MediaType.parse("application/json")));

        if (remoteArchive != null) {
            requestBodyBuilder.addFormDataPart("remote", "remote.json",
                RequestBody.create(
                    mapper.writeValueAsString(remoteArchive),
                    MediaType.parse("application/json")));
        }

        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), uri, accessToken, requestBodyBuilder.build(), Installer_InstallRemote.class);
    }

    public CompletableFuture<Installer_InstallRemote> executeAsync(String accessToken, InstallRemoteRequest installRemoteRequest,
                                                                   RemoteArchive remoteArchive) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException {
        return executeAsync(accessToken, installRemoteRequest, remoteArchive, null);
    }
}
