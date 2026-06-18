package com.hp.ext.clients.sdksupport;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.ControlPanel_CaptureScreen;
import com.hp.ext.service.solutionManager.CertificateAuthorities_Export;

public class CaptureScreenResourceFacade extends BaseResourceFacade implements CaptureScreenResource {
    public static final String name = "captureScreen";

    public CaptureScreenResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    public CompletableFuture<SimpleEntry<ControlPanel_CaptureScreen, byte[]>> executeAsync(String accessToken, String queryParams)
            throws URISyntaxException, IOException {
        URI requestUri = createRequestUri(super.getServiceUri(), super.getPath(), queryParams);
        return ResourceFacadeHelper.executeMultipartResourceOperationAsync(super.getHttpClient(), requestUri,
                accessToken, null).thenApply(CaptureScreenResourceFacade::multipartResponse);
    }

    public CompletableFuture<SimpleEntry<ControlPanel_CaptureScreen, byte[]>> executeAsync(String accessToken)
            throws URISyntaxException, IOException {
        return executeAsync(accessToken, null);
    }

     public static SimpleEntry<ControlPanel_CaptureScreen, byte[]> multipartResponse(List<MultipartResponseItem> list) {
        byte[] data = null;
        String contentPart = null;
        ControlPanel_CaptureScreen dataPart = null;
        CustomObjectMapper<ControlPanel_CaptureScreen> mapper = new CustomObjectMapper(ControlPanel_CaptureScreen.class);
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                dataPart = mapper.readValue(contentPart);
            }
            if (multipartResponseItem.getName().equals("screenshot")) {
                data = multipartResponseItem.getContent();
            }
        }
        SimpleEntry<ControlPanel_CaptureScreen, byte[]> map = new SimpleEntry<ControlPanel_CaptureScreen, byte[]>(dataPart, data);
        return map;
    }
}
