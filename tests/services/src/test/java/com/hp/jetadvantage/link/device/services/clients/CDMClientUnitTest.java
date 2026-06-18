package com.hp.jetadvantage.link.device.services.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(MockitoJUnitRunner.class)
public class CDMClientUnitTest {

    private MockWebServer mockServer;
    private OkHttpClient okHttpClient;

    @Before
    public void setUp() throws IOException {
        // setup mock webServer and start
        mockServer = new MockWebServer();
        mockServer.start();

        okHttpClient = DeviceConnectorHelper.createOkHttpClient();
    }

    @After
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void GivenCDMClient_WhenConstructorCalled_ThenObjectCreated() {
        CDMClient cdmClient = new CDMClient(okHttpClient, "1.2.3.4", "testToken");
        assertNotNull(cdmClient);
    }

    @Test
    public void GivenCDMClient_WhenSendGetRequestCalled_ThenItReturnSuccessResponse() throws IOException {
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "localhost", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer and validate response
        CDMResponse<String> response = cdmClient.sendGetRequest("/cdm/storageDevices/v1/removableDevices");
        assertEquals("200", response.httpStatusCode.toString());
        assertEquals("{\"message\": \"Hello, world!\"}", response.httpBody);
    }

    @Test
    public void GivenCDMClient_WhenSendGetRequestCalled_ThenItReturnIOException() {
        // 1) Configure test response from mockWebServer : server will respond with 400 Bad request
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(400)
                .setBody("{\"message\": \"Bad request\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "localhost", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer and validate response
        try {
            CDMResponse<String> response = cdmClient.sendGetRequest("/cdm/storageDevices/v1/removableDevices");
            fail("Expected IOException is not occurred");
        } catch (IOException e) {
            assertEquals(e.getMessage(), "HTTP request failed. " + "400: " + "{\"message\": \"Bad request\"}");
        }
    }

    @Test
    public void GivenCDMClient_WhenSendGetRequestCalled_ThenItReturnIOExceptionForServerInternalError() {
        // 1) Configure test response from mockWebServer : server will respond with 500 Server internal error
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(500)
                .setBody("{\"message\": \"Server internal error\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "localhost", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer and validate response
        try {
            CDMResponse<String> response = cdmClient.sendGetRequest("/cdm/storageDevices/v1/removableDevices");
            fail("Expected IOException is not occurred");
        } catch (IOException e) {
            assertEquals(e.getMessage(), "HTTP request failed. " + "500: " + "{\"message\": \"Server internal " +
                    "error\"}");
        }
    }

    @Test
    public void GivenCDMClient_WhenSendPostRequestCalled_ThenItReturnSuccessResponse() throws IOException {
        // 1) Configure test response from mockWebServer : server will respond with 201 created
        String expectedResponseBody = "{\"message\": \"Bad request\"}";
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(201)
                .setBody(expectedResponseBody);
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "localhost", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) create request
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("clientId", "workpath");
            requestBody.put("clientInstanceId", "workpath");
            requestBody.put("callbackUri", "http://localhost/testcallbackUri");

        } catch (Exception e) {
            fail("unexpected exception:" + e);
        }

        // 4) send HTTP POST Request to mockWebServer and validate response
        CDMResponse<String> response = cdmClient.sendPostRequest("/cdm/pubsub/v2/subscriptions",
                requestBody.toString());
        assertEquals("201", response.httpStatusCode.toString());
        assertEquals(expectedResponseBody, response.httpBody);
    }

    @Test
    public void GivenCDMClient_WhenSendPostRequestCalled_ThenItReturnIOException() {
        // 1) Configure test response from mockWebServer : server will respond with 400 Bad request
        String expectedResponseBody = "{\"message\": \"Bad request\"}";
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(400)
                .setBody(expectedResponseBody);
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "localhost", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) create request
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("clientId", "workpath");
            requestBody.put("clientInstanceId", "workpath");
            requestBody.put("callbackUri", "http://localhost/testcallbackUri");

        } catch (Exception e) {
            fail("Unexpected exception:" + e);
        }

        // 4) send HTTP POST Request to mockWebServer and validate response
        try {
            CDMResponse<String> response = cdmClient.sendPostRequest("/cdm/pubsub/v2/subscriptions",
                    requestBody.toString());
            fail("Expected IOException is not occurred");
        } catch (IOException e) {
            assertEquals(e.getMessage(), "HTTP request failed. " + "400: " + "{\"message\": \"Bad request\"}");
        }
    }

    @Test
    public void GivenCDMClient_WhenSendGetRequestCalled_ThenValidateForTheRequestHttpHeader() throws IOException {
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer
        String requestUrl = "/cdm/storageDevices/v1/removableDevices";
        CDMResponse<String> response = cdmClient.sendGetRequest(requestUrl);

        // 4) validate requested http header
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("application/json", recordedRequest.getHeader("Content-Type"));
            assertEquals("Bearer testToken", recordedRequest.getHeader("Authorization"));
            assertEquals("GET", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            String expectedRequestUrl;
            if (recordedRequestUrl.startsWith("http://127.0.0.1")) {
                expectedRequestUrl = "http://127.0.0.1:" + mockServer.getPort() + requestUrl;
            } else {
                //depending on the MockWebServer, 127.0.0.1 is converted to localhost.localdomain automatically
                expectedRequestUrl = "http://localhost.localdomain:" + mockServer.getPort() + requestUrl;
            }
            assertEquals(expectedRequestUrl, recordedRequestUrl);
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenSendGetRequestCalledWithErrorCodeRequired_ThenItReturnsErrorCode() throws IOException {
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(400);
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer
        String requestUrl = "/cdm/storageDevices/v1/removableDevices";
        CDMResponse<String> response = cdmClient.sendGetRequest(requestUrl, true);

        assertEquals("ErrorCode", 400, response.httpStatusCode.intValue());
    }

    @Test
    public void GivenCDMClient_WhenUpdateDeviceInfoCalled_ThenUpdatedInfoShouldBeApplied() throws IOException {
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        String requestUrl = "/cdm/storageDevices/v1/removableDevices";


        // 3) update token information and send GetRequest
        cdmClient.updateDeviceInfo("127.0.0.1", "updatedTestToken");
        CDMResponse<String> response2 = cdmClient.sendGetRequest(requestUrl);

        // 4) validate requested http header
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("application/json", recordedRequest.getHeader("Content-Type"));
            assertEquals("Bearer updatedTestToken", recordedRequest.getHeader("Authorization"));
            assertEquals("GET", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            String expectedRequestUrl;
            if (recordedRequestUrl.startsWith("http://127.0.0.1")) {
                expectedRequestUrl = "http://127.0.0.1:" + mockServer.getPort() + requestUrl;
            } else {
                //depending on the MockWebServer, 127.0.0.1 is converted to localhost.localdomain automatically
                expectedRequestUrl = "http://localhost.localdomain:" + mockServer.getPort() + requestUrl;
            }
            assertEquals(expectedRequestUrl, recordedRequestUrl);
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenSendPatchRequestCalled_ThenSendRequestBody() throws IOException {
        String requestBody = "{\"key\": \"value\"}";
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer
        String requestUrl = "/cdm/storageDevices/v1/removableDevices";
        CDMResponse<String> response = cdmClient.sendPatchRequest(requestUrl, requestBody);

        // 4) validate requested http header
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("application/json; charset=utf-8", recordedRequest.getHeader("Content-Type"));
            assertEquals("Bearer testToken", recordedRequest.getHeader("Authorization"));
            assertEquals("PATCH", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            String expectedRequestUrl;
            if (recordedRequestUrl.startsWith("http://127.0.0.1")) {
                expectedRequestUrl = "http://127.0.0.1:" + mockServer.getPort() + requestUrl;
            } else {
                //depending on the MockWebServer, 127.0.0.1 is converted to localhost.localdomain automatically
                expectedRequestUrl = "http://localhost.localdomain:" + mockServer.getPort() + requestUrl;
            }
            assertEquals(expectedRequestUrl, recordedRequestUrl);
            assertEquals("Request Body", requestBody, recordedRequest.getBody().readUtf8());
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenSendPostRequestCalled_ThenSendRequestBody() throws IOException {
        String requestBody = "{\"key\": \"value\"}";
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer
        String requestUrl = "/cdm/storageDevices/v1/removableDevices";
        CDMResponse<String> response = cdmClient.sendPostRequest(requestUrl, requestBody);

        // 4) validate requested http header
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("application/json; charset=utf-8", recordedRequest.getHeader("Content-Type"));
            assertEquals("Bearer testToken", recordedRequest.getHeader("Authorization"));
            assertEquals("POST", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            String expectedRequestUrl;
            if (recordedRequestUrl.startsWith("http://127.0.0.1")) {
                expectedRequestUrl = "http://127.0.0.1:" + mockServer.getPort() + requestUrl;
            } else {
                //depending on the MockWebServer, 127.0.0.1 is converted to localhost.localdomain automatically
                expectedRequestUrl = "http://localhost.localdomain:" + mockServer.getPort() + requestUrl;
            }
            assertEquals(expectedRequestUrl, recordedRequestUrl);
            assertEquals("Request Body", requestBody, recordedRequest.getBody().readUtf8());
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenSendPutRequestCalled_ThenSendRequestBody() throws IOException {
        String requestBody = "{\"key\": \"value\"}";
        // 1) Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Hello, world!\"}");
        mockServer.enqueue(mockResponse);

        // 2) create test CDMClient
        CDMClient cdmClient = new CDMClient(okHttpClient, "127.0.0.1", "testToken", false, mockServer.getPort());
        assertNotNull(cdmClient);

        // 3) send HTTP GET Request to mockWebServer
        String requestUrl = "/cdm/storageDevices/v1/removableDevices";
        CDMResponse<String> response = cdmClient.sendPutRequest(requestUrl, requestBody);

        // 4) validate requested http header
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("application/json; charset=utf-8", recordedRequest.getHeader("Content-Type"));
            assertEquals("Bearer testToken", recordedRequest.getHeader("Authorization"));
            assertEquals("PUT", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            String expectedRequestUrl;
            if (recordedRequestUrl.startsWith("http://127.0.0.1")) {
                expectedRequestUrl = "http://127.0.0.1:" + mockServer.getPort() + requestUrl;
            } else {
                //depending on the MockWebServer, 127.0.0.1 is converted to localhost.localdomain automatically
                expectedRequestUrl = "http://localhost.localdomain:" + mockServer.getPort() + requestUrl;
            }
            assertEquals(expectedRequestUrl, recordedRequestUrl);
            assertEquals("Request Body", requestBody, recordedRequest.getBody().readUtf8());
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }
}
