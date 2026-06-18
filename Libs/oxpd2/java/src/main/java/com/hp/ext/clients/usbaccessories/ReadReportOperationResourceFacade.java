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
import com.hp.ext.service.usbAccessories.Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory_ReadReport;

public class ReadReportOperationResourceFacade extends BaseResourceFacade implements ReadReportOperationResource {

    public static final String name = "readReport";

    public ReadReportOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<OpenHIDAccessory_ReadReport> executeAsync(String accessToken, Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params resource, String queryParams) throws URISyntaxException, JsonProcessingException {
        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(),
            createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, resource, OpenHIDAccessory_ReadReport.class);
    }

    public CompletableFuture<OpenHIDAccessory_ReadReport> executeAsync(String accessToken, Accessories_Accessory_Hid_OpenHIDAccessory_ReadReport_Params resource) throws URISyntaxException, JsonProcessingException {
        return executeAsync(accessToken, resource, null);
    }
}
