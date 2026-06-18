/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.Data;
import com.hp.ext.service.solutionManager.Data_Replace;

public interface DataResource extends ReadableResource<SimpleEntry<Data, byte[]>>{
    /**
     * @param accessToken
     * @param dataStream
     * @param mimeType
     * @param queryParams
     * @return
     * @throws URISyntaxException
     * @throws JsonProcessingException
     * @throws IOException
     */
    CompletableFuture<Data> replaceAsync(String accessToken, InputStream dataStream, String mimeType,
            String queryParams) throws URISyntaxException, JsonProcessingException, IOException;

}
