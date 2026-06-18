package com.hp.ext.clients.application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.application.ApplicationAccessPoint_InitiateLaunch;
import com.hp.ext.service.application.InitiateLaunchRequest;

public interface InitiateLaunchOperationResource extends ExecutableResource<ApplicationAccessPoint_InitiateLaunch> {
    CompletableFuture<ApplicationAccessPoint_InitiateLaunch> executeAsync(String accessToken,
            InitiateLaunchRequest initiateLaunchRequest, Object startIntent, String queryParams)
            throws URISyntaxException, IOException;
}
