/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.sdksupport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.clients.MultipartResponseItem;
import com.hp.ext.service.sdkSupport.Echo_Execute;
import com.hp.ext.service.sdkSupport.Echo_Execute_Params;

import okhttp3.MultipartBody;

public interface ExecuteEchoOperationResource extends ExecutableResource<SimpleEntry<Echo_Execute, byte[]>> {
    // library changed to org.apache.http.entity.mime.FormBodyPart -> okhttp3.MultipartBody.Part
    CompletableFuture<SimpleEntry<Echo_Execute, List<MultipartResponseItem>>> executeAsync(String accessToken, Echo_Execute_Params echoExecuteParams, List<MultipartBody.Part> parts, String queryParams) throws URISyntaxException, IOException;
}
