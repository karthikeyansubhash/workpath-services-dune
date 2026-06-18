/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_Modify;
import com.hp.ext.types.base.DeleteContent;

public class OpenHidAccessoryResourceFacade extends BaseResourceFacade implements OpenHidAccessoryResource {

    private ReadReportOperationResourceFacade readReport = null;
    private WriteReportOperationResourceFacade writeReport = null;

    public static final String name = "openHidAccessory";

    public OpenHidAccessoryResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        readReport = new ReadReportOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + ReadReportOperationResourceFacade.name);
        writeReport = new WriteReportOperationResourceFacade(getHttpClient(), getServiceUri(),
                getPath() + "/" + WriteReportOperationResourceFacade.name);
    }

    @Override
    public CompletableFuture<OpenHIDAccessory> getAsync(String accessToken, String queryParams) throws URISyntaxException {
            return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, OpenHIDAccessory.class);
    }

    public CompletableFuture<OpenHIDAccessory> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken, String queryParams) throws URISyntaxException {
            return ResourceFacadeHelper.deleteResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, DeleteContent.class);
    }

    public CompletableFuture<DeleteContent> deleteAsync(String accessToken) throws URISyntaxException {
        return deleteAsync(accessToken, null);
    }

    public CompletableFuture<OpenHIDAccessory> modifyAsync(String accessToken, OpenHIDAccessory_Modify resource, String queryParams) throws URISyntaxException, JsonProcessingException {
            return ResourceFacadeHelper.modifyResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, OpenHIDAccessory.class);
    }

    public CompletableFuture<OpenHIDAccessory> modifyAsync(String accessToken, OpenHIDAccessory_Modify resource) throws URISyntaxException, JsonProcessingException {
        return modifyAsync(accessToken, resource, null);
    }

    @Override
    public ReadReportOperationResourceFacade readReport() {
        return readReport;
    }

    @Override
    public WriteReportOperationResourceFacade writeReport() {
        return writeReport;
    }
}
