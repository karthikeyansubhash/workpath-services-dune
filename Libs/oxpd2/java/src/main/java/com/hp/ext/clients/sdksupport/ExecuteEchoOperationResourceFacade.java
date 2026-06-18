package com.hp.ext.clients.sdksupport;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.sdkSupport.Echo_Execute;
import com.hp.ext.service.sdkSupport.Echo_Execute_Params;
import com.hp.net.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ExecuteEchoOperationResourceFacade extends BaseResourceFacade
    implements ExecuteEchoOperationResource {

    public static String name = "execute";

    public ExecuteEchoOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<SimpleEntry<Echo_Execute, List<MultipartResponseItem>>> executeAsync(String accessToken, Echo_Execute_Params echoExecuteParams, List<MultipartBody.Part> parts,
                                                                                                  String queryParams) throws URISyntaxException, IOException {
        URI uri = createRequestUri(getServiceUri(), getPath(), queryParams);
        CustomObjectMapper<Echo_Execute_Params> mapper = new CustomObjectMapper<Echo_Execute_Params>(Echo_Execute_Params.class);

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.MIXED);
        requestBodyBuilder.addFormDataPart("content", "content.json",
            RequestBody.create(
                mapper.writeValueAsString(echoExecuteParams),
                MediaType.parse("application/json")));

        if (parts != null) {
            for (MultipartBody.Part part : parts) {
                requestBodyBuilder.addPart(part);
            }
        }

        CompletableFuture<SimpleEntry<Echo_Execute, List<MultipartResponseItem>>> result = null;
        result = ResourceFacadeHelper.executeResourceOperationAsync(super.getHttpClient(), uri,
            accessToken, requestBodyBuilder.build()).thenApply(ExecuteEchoOperationResourceFacade::multipartResponse);
        return result;
    }

    public CompletableFuture<SimpleEntry<Echo_Execute, List<MultipartResponseItem>>> executeAsync(String accessToken, Echo_Execute_Params echoExecuteParams, List<MultipartBody.Part> parts) throws URISyntaxException, IOException {
        return executeAsync(accessToken, echoExecuteParams, parts, null);
    }

    public static SimpleEntry<Echo_Execute, List<MultipartResponseItem>> multipartResponse(List<MultipartResponseItem> list) {
        String contentPart = null;
        Echo_Execute dataPart = null;
        CustomObjectMapper<Echo_Execute> mapper = new CustomObjectMapper<Echo_Execute>(Echo_Execute.class);
        List<MultipartResponseItem> echoedParts = new ArrayList<MultipartResponseItem>();
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                dataPart = mapper.readValue(contentPart);
            } else {
                echoedParts.add(multipartResponseItem);
            }
        }
        SimpleEntry<Echo_Execute, List<MultipartResponseItem>> map = new SimpleEntry<Echo_Execute, List<MultipartResponseItem>>(dataPart, echoedParts);
        return map;
    }
}
