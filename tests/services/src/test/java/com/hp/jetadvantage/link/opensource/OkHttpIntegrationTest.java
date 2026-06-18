package com.hp.jetadvantage.link.opensource;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class OkHttpIntegrationTest {

    private MockWebServer mockWebServer;
    private OkHttpClient client;

    @Before
    public void setUp() throws IOException {
        // 1. MockWebServer instance create
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2. OkHttpClient instance create
        client = new OkHttpClient();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void whenGetRequestSent_thenCorrectResponseReceived() throws Exception {
        String jsonResponse = "{\"version\":\"1.0\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonResponse));

        Request request = new Request.Builder()
                .url(mockWebServer.url("/api/version"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals(jsonResponse, response.body().string());
        }

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/version", recordedRequest.getPath());
    }

    @Test
    public void testPostRequestWithJsonBody() throws Exception {
        // Arrange
        String jsonRequest = "{\"user\":\"test\"}";
        String jsonResponse = "{\"status\":\"success\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(jsonResponse));

        RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(mockWebServer.url("/api/users"))
                .post(body)
                .build();

        // Act
        try (Response response = client.newCall(request).execute()) {
            // Assert
            assertEquals(201, response.code());
            assertEquals(jsonResponse, response.body().string());
        }

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/users", recordedRequest.getPath());
        assertEquals("application/json; charset=utf-8", recordedRequest.getHeader("Content-Type"));
        assertEquals(jsonRequest, recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testMultipartRequest() throws Exception {
        // Arrange
        String jsonResponse = "{\"status\":\"file uploaded\"}";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(jsonResponse));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "My File")
                .addFormDataPart("file", "file.txt",
                        RequestBody.create("This is the file content.", MediaType.parse("text/plain")))
                .build();

        Request request = new Request.Builder()
                .url(mockWebServer.url("/api/upload"))
                .post(requestBody)
                .build();

        // Act
        try (Response response = client.newCall(request).execute()) {
            // Assert
            assertEquals(200, response.code());
            assertEquals(jsonResponse, response.body().string());
        }

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        String contentType = recordedRequest.getHeader("Content-Type");
        assert(contentType.startsWith("multipart/form-data"));

        // You can further inspect the multipart body if needed
        // For example, by reading the body and parsing it.
    }

    @Test
    public void testRequestWithCustomHeader() throws Exception {
        // Arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Request request = new Request.Builder()
                .url(mockWebServer.url("/api/secure"))
                .header("Authorization", "Bearer my-secret-token")
                .build();

        // Act
        try (Response response = client.newCall(request).execute()) {
            // Assert
            assertEquals(200, response.code());
        }

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("Bearer my-secret-token", recordedRequest.getHeader("Authorization"));
    }
}
