package com.hp.ext.clients.sdksupport;

import com.hp.net.http.HttpClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.ControlPanel_PressScreen;
import com.hp.ext.service.sdkSupport.ScreenPress;

public class PressScreenResourceFacade extends BaseResourceFacade implements PressScreenResource {
    public static final String name = "pressScreen";

    public PressScreenResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<ControlPanel_PressScreen> executeAsync(String accessToken, ScreenPress resource,
            String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource,
                ControlPanel_PressScreen.class);
    }

    public CompletableFuture<ControlPanel_PressScreen> executeAsync(String accessToken, ScreenPress resource)
            throws JsonProcessingException, URISyntaxException {
        return executeAsync(accessToken, resource, null);
    }
}
