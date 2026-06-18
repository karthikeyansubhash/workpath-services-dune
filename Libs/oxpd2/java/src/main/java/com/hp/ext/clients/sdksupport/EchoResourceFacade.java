package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.Echo;
import com.hp.ext.service.sdkSupport.Echo_Modify;
import com.hp.ext.service.sdkSupport.Echo_Replace;

public class EchoResourceFacade extends BaseResourceFacade implements EchoResource {
    
    public static final String name = "echo";

    private ExecuteEchoOperationResourceFacade execute = null;

    public EchoResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        execute = new ExecuteEchoOperationResourceFacade(httpClient, serviceUri,
            path + "/" + ExecuteEchoOperationResourceFacade.name);
    }

    @Override
    public ExecuteEchoOperationResourceFacade execute() {
        return execute;
    }

    @Override
    public CompletableFuture<Echo> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, Echo.class);
    }

    public CompletableFuture<Echo> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CompletableFuture<Echo> modifyAsync(String accessToken, Echo_Modify resource, String queryParams) throws JsonProcessingException, URISyntaxException  {
        return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Echo.class);
    }

    public CompletableFuture<Echo> modifyAsync(String accessToken, Echo_Modify resource) throws JsonProcessingException, URISyntaxException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public CompletableFuture<Echo> replaceAsync(String accessToken, Echo_Replace resource, String queryParams) throws JsonProcessingException, URISyntaxException {
        return ResourceFacadeHelper.replaceResourceAsync(getHttpClient(), createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, Echo.class);
    }

    public CompletableFuture<Echo> replaceAsync(String accessToken, Echo_Replace resource) throws JsonProcessingException, URISyntaxException {
        return replaceAsync(accessToken, resource, null);
    }
}
