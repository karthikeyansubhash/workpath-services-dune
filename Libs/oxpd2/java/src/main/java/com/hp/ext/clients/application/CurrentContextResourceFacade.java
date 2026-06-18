/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.application.CurrentContext;

public class CurrentContextResourceFacade extends BaseResourceFacade implements CurrentContextResource {

    public static final String name = "currentContext";

    private BeepOperationResourceFacade beep = null;
    private ExecOperationResourceFacade exec = null;
    private ExitOperationResourceFacade exit = null;
    private ResetInactivityTimerOperationResourceFacade resetInactivityTimer = null;
    private RuntimeChromeResourceFacade runtimeChrome = null;
    private StartIntentResourceFacade startIntent = null;

    public CurrentContextResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        beep = new BeepOperationResourceFacade(httpClient, serviceUri,
            path + "/" + BeepOperationResourceFacade.name);
        exec = new ExecOperationResourceFacade(httpClient, serviceUri,
            path + "/" + ExecOperationResourceFacade.name);
        exit = new ExitOperationResourceFacade(httpClient, serviceUri,
            path + "/" + ExitOperationResourceFacade.name);
        resetInactivityTimer = new ResetInactivityTimerOperationResourceFacade(httpClient, serviceUri,
            path + "/" + ResetInactivityTimerOperationResourceFacade.name);
        runtimeChrome = new RuntimeChromeResourceFacade(httpClient, serviceUri,
            path + "/" + RuntimeChromeResourceFacade.name);
        startIntent = new StartIntentResourceFacade(httpClient, serviceUri,
            path + "/" + StartIntentResourceFacade.name);
    }

    @Override
    public CompletableFuture<CurrentContext> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, CurrentContext.class);
    }

    public CompletableFuture<CurrentContext> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public BeepOperationResourceFacade beep() {
        return beep;
    }

    @Override
    public ExecOperationResourceFacade exec() {
        return exec;
    }

    @Override
    public ExitOperationResourceFacade exit() {
       return exit;
    }

    @Override
    public ResetInactivityTimerOperationResourceFacade resetInactivityTimer() {
        return resetInactivityTimer;
    }

    @Override
    public RuntimeChromeResourceFacade runtimeChrome() {
        return runtimeChrome;
    }

    @Override
    public StartIntentResourceFacade startIntent() {
        return startIntent;
    }
}
