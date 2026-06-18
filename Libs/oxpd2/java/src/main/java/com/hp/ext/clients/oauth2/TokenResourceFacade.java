/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.oauth2;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;

import okhttp3.Request;

public class TokenResourceFacade extends BaseResourceFacade implements TokenResource {

    public static final String name = "token";

    public TokenResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<Token> passwordGrantAsync(PasswordGrantRequest grantRequest)
            throws URISyntaxException, JsonProcessingException {
        URI resourceUri = createRequestUri(getServiceUri(), getPath(), null);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        Map<String, String> requestContent = new HashMap<>();
        if (grantRequest != null) {
            requestContent.put(PasswordGrantConstants.GRANTTYPEPARAMETER, grantRequest.getGrantType());
            requestContent.put(PasswordGrantConstants.USERNAMEPARAMETER, grantRequest.getUsername());
            requestContent.put(PasswordGrantConstants.PASSWORDPARAMETER, grantRequest.getPassword());
            requestContent.put(PasswordGrantConstants.SCOPEPARAMETER, grantRequest.getScope());
        }
        String requestBody = new ObjectMapper().writeValueAsString(requestContent);

        Request request = ResourceFacadeHelper.createRequest("POST", resourceUri, requestHeader,
            requestBody);

        return ResourceFacadeHelper.asyncSend(getHttpClient(), request, Token.class);
    }

    @Override
    public CompletableFuture<Token> authorizationCodeGrantAsync(AuthorizationCodeGrantRequest grantRequest)
            throws URISyntaxException, JsonProcessingException {
        URI resourceUri = createRequestUri(getServiceUri(), getPath(), null);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        Map<String, String> requestContent = new HashMap<>();
        if (grantRequest != null) {
            requestContent.put(AuthorizationCodeGrantConstants.GRANTTYPEPARAMETER, grantRequest.getGrantType());
            requestContent.put(AuthorizationCodeGrantConstants.CODEPARAMETER, grantRequest.getCode());
            requestContent.put(AuthorizationCodeGrantConstants.CLIENTIDPARAMETER, grantRequest.getClientId());
            requestContent.put(AuthorizationCodeGrantConstants.REFRESHTOKENPARAMETER, grantRequest.getRefreshTokenRequested().toString());
        }
        String requestBody = new ObjectMapper().writeValueAsString(requestContent);

        Request request = ResourceFacadeHelper.createRequest("POST", resourceUri, requestHeader,
            requestBody);
        return ResourceFacadeHelper.asyncSend(getHttpClient(), request, Token.class);
    }

    @Override
    public CompletableFuture<Token> refreshTokenGrantAsync(RefreshTokenGrantRequest grantRequest)
            throws JsonProcessingException, URISyntaxException {
        URI resourceUri = createRequestUri(getServiceUri(), getPath(), null);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        Map<String, String> requestContent = new HashMap<>();
        if (grantRequest != null) {
            requestContent.put(RefreshTokenGrantConstants.GRANTTYPEPARAMETER, grantRequest.getGrantType());
            requestContent.put(RefreshTokenGrantConstants.CLIENTIDPARAMETER, grantRequest.getClientId());
            requestContent.put(RefreshTokenGrantConstants.REFRESHTOKENPARAMETER, grantRequest.getRefreshToken());
        }
        String requestBody = new ObjectMapper().writeValueAsString(requestContent);

        Request request = ResourceFacadeHelper.createRequest("POST", resourceUri, requestHeader,
            requestBody);
        return ResourceFacadeHelper.asyncSend(getHttpClient(), request, Token.class);
    }

}
