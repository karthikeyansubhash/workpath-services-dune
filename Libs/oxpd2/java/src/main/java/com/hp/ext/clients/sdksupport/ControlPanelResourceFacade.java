package com.hp.ext.clients.sdksupport;

import java.net.URI;
import java.net.URISyntaxException;
import com.hp.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.ControlPanel;

public class ControlPanelResourceFacade extends BaseResourceFacade implements ControlPanelResource {
    public static final String name = "controlPanel";

    private CaptureScreenResourceFacade captureScreen = null;
    private PressKeyResourceFacade pressKey = null;
    private PressScreenResourceFacade pressScreen = null;

    public ControlPanelResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        captureScreen = new CaptureScreenResourceFacade(httpClient, serviceUri,
                path + "/" + CaptureScreenResourceFacade.name);
        pressKey = new PressKeyResourceFacade(httpClient, serviceUri, path + "/" + PressKeyResourceFacade.name);
        pressScreen = new PressScreenResourceFacade(httpClient, serviceUri,
                path + "/" + PressScreenResourceFacade.name);
    }

    @Override
    public CompletableFuture<ControlPanel> getAsync(String accessToken, String queryParams) throws URISyntaxException {
        return ResourceFacadeHelper.getResourceAsync(getHttpClient(),
                createRequestUri(getServiceUri(), getPath(), queryParams), accessToken, ControlPanel.class);
    }

    public CompletableFuture<ControlPanel> getAsync(String accessToken) throws URISyntaxException {
        return getAsync(accessToken, null);
    }

    @Override
    public CaptureScreenResourceFacade captureScreen() {
        return captureScreen;
    }

    @Override
    public PressKeyResourceFacade pressKey() {
        return pressKey;
    }

    @Override
    public PressScreenResourceFacade pressScreen() {
        return pressScreen;
    }
}
