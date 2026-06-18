/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hp.net.http.HttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

public class ResourceFacadeHelper {
    private static InjectedHttpClient injectedHttpClient;

    public static void setHttpClient(InjectedHttpClient client) {
        injectedHttpClient = client;
    }

    public static Supplier<String> createNonce = () -> {
        // create StringBuilder size of AlphaNumericString
        int length = 20;
        StringBuilder sb = new StringBuilder(length);
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz"
            + "#@$";
        for (int i = 0; i < length; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    };

    public static Request createRequest(String requestMethod, URI resourceUri, Map<String, String> headers,
                                        String requestBody) {
        Request.Builder requestBuilder = new Request.Builder()
            .url(resourceUri.toString());
        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        RequestBody generatedRequestBody = requestBody == null || requestBody.isEmpty()
            ? RequestBody.create("", null)
            : RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);

        switch (requestMethod) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
            case "PATCH":
            case "PUT":
                requestBuilder.method(requestMethod, generatedRequestBody);
                break;
            case "DELETE":
                requestBuilder.method(requestMethod, requestBody == null || requestBody.isEmpty() ? null : generatedRequestBody);
                break;
        }
        return requestBuilder.build();
    }

    public static Request createRequestForMultipartBody(String requestMethod, URI resourceUri, Map<String, String> headers,
                                                        MultipartBody multipartBody) {
        Request.Builder requestBuilder = new Request.Builder()
            .url(resourceUri.toString());
        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        switch (requestMethod) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
            case "PATCH":
            case "PUT":
            case "DELETE":
                requestBuilder.method(requestMethod, multipartBody);
                break;
        }
        return requestBuilder.build();
    }

    public static <TResponseContent> CompletableFuture<TResponseContent> asyncSend(HttpClient httpClient, Request request, Class<TResponseContent> type) {
        CustomObjectMapper<TResponseContent> objectMapper = new CustomObjectMapper<TResponseContent>(type);
        CompletableFuture<TResponseContent> response = CompletableFuture.supplyAsync(() -> {
            try {
                return objectMapper.readValue(injectedHttpClient.getResponseAsString(request));
            } catch (Exception e) {
                throw new OXPdHttpRequestException(e.getMessage());
            }
        });
        return response;
    }

    private static byte[] getOutputByte(BodyPart bodyPart) throws IOException, MessagingException {
        byte[] targetArray = new byte[bodyPart.getInputStream().available()];
        bodyPart.getInputStream().read(targetArray);
        if (bodyPart.getInputStream() != null) {
            bodyPart.getInputStream().close();
        }
        return targetArray;
    }

    public static CompletableFuture<List<MultipartResponseItem>> asyncSend(HttpClient httpClient, Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<MultipartResponseItem> parts = new ArrayList<>();
                ResponseBody responseBody = injectedHttpClient.getResponseBody(request);
                if (responseBody != null ) {
                    ByteArrayDataSource datasource = new ByteArrayDataSource(responseBody.byteStream(),
                        responseBody.contentType().toString());
                    MimeMultipart multipart = new MimeMultipart(datasource);
                    int count = multipart.getCount();

                    for (int i = 0; i < count; i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        MultipartResponseItem contentResponse = new MultipartResponseItem();
                        String content_disposition = bodyPart.getHeader("Content-Disposition")[0];
                        contentResponse.setMimeType(bodyPart.getContentType());
                        if (content_disposition != null) {
                            ContentDisposition disposition = new ContentDisposition(content_disposition);
                            contentResponse.setName(disposition.getParameter("name"));
                            contentResponse.setFileName(disposition.getParameter("filename"));
                        }
                        contentResponse.setContent(getOutputByte(bodyPart));
                        parts.add(contentResponse);
                    }
                    return parts;
                }
            } catch (Exception e) {
                Log.e("E2 ResourceFacadeHelper", "asyncSend: exception : " + e.getMessage(), e);
                throw new OXPdHttpRequestException(e.getMessage());
            }
            return null;
        });
    }

    public static <T> CompletableFuture<T> getResourceAsync(HttpClient httpClient, URI resourceUri, String accessToken,
                                                            Class<T> type) {
        Map<String, String> requestHeader = null;
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader = new HashMap<>();
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        Request request = createRequest("GET", resourceUri, requestHeader, null);
        return asyncSend(httpClient, request, type);
    }

    public static <TResponse, TResource> CompletableFuture<TResponse> replaceResourceAsync(HttpClient httpClient,
                                                                                           URI resourceUri, String accessToken, TResource resource, Class<TResponse> type)
        throws JsonProcessingException {
        Map<String, String> requestHeader = new HashMap<>();
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }

        Request request = null;
        if (resource != null) {
            if(resource instanceof MultipartBody) {
                requestHeader.put("Content-Type", ((MultipartBody)resource).contentType().toString());
                request = createRequestForMultipartBody("PUT", resourceUri, requestHeader, (MultipartBody)resource);
            } else {
                requestHeader.put("Content-Type", "application/json;charset=UTF-8");
                String requestBody = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(resource);
                request = createRequest("PUT", resourceUri, requestHeader, requestBody);
            }
        } else {
            request = createRequest("PUT", resourceUri, requestHeader, null);
        }
        return asyncSend(httpClient, request, type);
    }

    public static <TResource, TResponse> CompletableFuture<TResponse> modifyResourceAsync(HttpClient httpClient,
                                                                                          URI resourceUri,
                                                                                          String accessToken, TResource resource, Class<TResponse> type) throws JsonProcessingException {
        Map<String, String> requestHeader = new HashMap<>();
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        Request request = null;
        if (resource != null) {
            String requestBody = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(resource);
            request = createRequest("PATCH", resourceUri, requestHeader, requestBody);
        } else {
            request = createRequest("PATCH", resourceUri, requestHeader, null);
        }
        return asyncSend(httpClient, request, type);
    }

    public static <T> CompletableFuture<T> deleteResourceAsync(HttpClient httpClient, URI rescourceUri,
                                                               String accessToken, Class<T> type) {
        Map<String, String> requestHeader = null;
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader = new HashMap<>();
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        Request request = createRequest("DELETE", rescourceUri, requestHeader, null);
        return asyncSend(httpClient, request, type);

    }

    public static <TResource, T> CompletableFuture<T> createResourceAsync(HttpClient httpClient, URI resourceUri,
                                                                          String accessToken, TResource resource, Class<T> type) throws JsonProcessingException {
        Map<String, String> requestHeader = new HashMap<>();
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        requestHeader.put("Content-Type", "application/json;charset=UTF-8");
        Request request = null;
        if (resource != null) {
            String requestBody = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(resource);
            request = createRequest("POST", resourceUri, requestHeader, requestBody);
        } else {
            request = createRequest("POST", resourceUri, requestHeader, null);
        }
        return asyncSend(httpClient, request, type);
    }

    public static <TResponse, TResource> CompletableFuture<TResponse> executeResourceOperationAsync(
        HttpClient httpClient, URI resourceUri, String accessToken, TResource resource, Class<TResponse> type) throws JsonProcessingException {
        return createResourceAsync(httpClient, resourceUri, accessToken, resource, type);
    }

    // library changed to org.apache.http.HttpEntity -> okhttp3.MultipartBody
    public static <TResponse> CompletableFuture<TResponse> executeResourceOperationAsync(HttpClient httpClient,
        URI resourceUri, String accessToken, MultipartBody entity, Class<TResponse> type) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(resourceUri.toString());

        if (accessToken != null && !accessToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }

        if (entity != null) {
            // adapted from https://www.springcloud.io/post/2022-04/httpclient-multipart/
            Pipe pipe = Pipe.open();

            new Thread(() -> {
                try (OutputStream outputStream = Channels.newOutputStream(pipe.sink());
                     BufferedSink bufferedSink = Okio.buffer(Okio.sink(outputStream))) {
                    entity.writeTo(bufferedSink);
                } catch (IOException e) {
                    throw new OXPdHttpRequestException(e.getMessage());
                }
            }).start();

            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return entity.contentType();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try (okio.Source source = Okio.source(Channels.newInputStream(pipe.source()))) {
                        sink.writeAll(source);
                    }
                }
            };

            requestBuilder.post(requestBody);
        } else {
            requestBuilder.post(RequestBody.create(new byte[0]));
        }

        Request request = requestBuilder.build();
        return asyncSend(httpClient, request, type);
    }
    public static <TResponse> CompletableFuture<List<MultipartResponseItem>> executeResourceOperationAsync(HttpClient httpClient,
        URI resourceUri, String accessToken, MultipartBody entity) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(resourceUri.toString());

        if (accessToken != null && !accessToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }

        if (entity != null) {
            // adapted from https://www.springcloud.io/post/2022-04/httpclient-multipart/
            Pipe pipe = Pipe.open();

            new Thread(() -> {
                try (OutputStream outputStream = Channels.newOutputStream(pipe.sink());
                     BufferedSink bufferedSink = Okio.buffer(Okio.sink(outputStream))) {
                    entity.writeTo(bufferedSink);
                } catch (IOException e) {
                    throw new OXPdHttpRequestException(e.getMessage());
                }
            }).start();

            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return entity.contentType();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try (okio.Source source = Okio.source(Channels.newInputStream(pipe.source()))) {
                        sink.writeAll(source);
                    }
                }
            };

            requestBuilder.post(requestBody);
        } else {
            requestBuilder.post(RequestBody.create(new byte[0]));
        }

        Request request = requestBuilder.build();
        return asyncSend(httpClient, request);
    }

    public static <TResponse> CompletableFuture<List<MultipartResponseItem>> executeMultipartResourceOperationAsync(HttpClient httpClient, URI resourceUri, String accessToken, Class<TResponse> type) {
        Request httpRequest = null;
        Map<String, String> requestHeader = new HashMap<>();
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        httpRequest = createRequest("POST", resourceUri, requestHeader, null);
        return asyncSend(httpClient, httpRequest);
    }

    public static <TResponse> CompletableFuture<List<MultipartResponseItem>> getMultipartResponseAsync(HttpClient httpClient, URI requestUri, String accessToken) {

        Map<String, String> requestHeader = null;
        if (accessToken != null && !accessToken.isEmpty()) {
            requestHeader = new HashMap<>();
            requestHeader.put("Authorization", "Bearer " + accessToken);
        }
        Request request = createRequest("GET", requestUri, requestHeader, null);
        return asyncSend(httpClient, request);

    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
