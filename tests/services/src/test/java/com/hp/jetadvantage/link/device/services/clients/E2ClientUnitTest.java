package com.hp.jetadvantage.link.device.services.clients;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import com.hp.ext.clients.ResourceFacadeHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(MockitoJUnitRunner.class)
public class E2ClientUnitTest {
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
    public void GivenE2Client_WhenConstructorCalled_ThenObjectCreated() {
        E2Client e2Client = new E2Client(okHttpClient, "testToken");
        assertNotNull(e2Client);
    }

    @Test
    public void GivenCDMClient_WhenGetResponseAsStringCalled_ThenItReturnResponse() throws IOException {
        String expectedResponse = "{\"message\": \"Hello, world!\"}";
        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(expectedResponse);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, "testToken");
        URI resourceUri = mockServer.url("/test").uri();
        Request request = ResourceFacadeHelper.createRequest("GET", resourceUri, null, null);
        String response = e2Client.getResponseAsString(request);
        assertEquals("response", expectedResponse, response);
    }

    @Test
    public void GivenCDMClient_WhenGetResponseAsStringCalledWithJsonBody_ThenItReturnResponse() throws IOException {
        String requestBody = "{\"key\": \"value\"}";
        String expectedResponse = "{\"message\": \"Hello, world!\"}";
        String expectedContentType = "application/json; charset=utf-8";
        String testToken = "testTokenString";
        String requestMethod = "PATCH";
        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(expectedResponse);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, testToken);
        URI resourceUri = mockServer.url("/test").uri();
        Map<String, String> headers = Map.of(
                "Authorization", testToken);

        Request request = ResourceFacadeHelper.createRequest(requestMethod, resourceUri, headers, requestBody);
        String response = e2Client.getResponseAsString(request);
        assertEquals("response", expectedResponse, response);

        // Verify the request sent to the mock server
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("contentType", expectedContentType, recordedRequest.getHeader("Content-Type"));
            assertEquals("token", testToken, recordedRequest.getHeader("Authorization"));
            assertEquals("method", requestMethod, recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            assertEquals("Uri", resourceUri.toString(), recordedRequestUrl);
            assertEquals("request body", requestBody, recordedRequest.getBody().readUtf8());
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenGetResponseAsStringCalledWithMultipartBody_ThenItReturnResponse() throws IOException {
        String data = "{\"key\": \"value\"}";
        MultipartBody multipartBody = createMultipartBody(data);
        String expectedResponse = "{\"links\": []}";
        String expectedContentTypeFormat = "multipart/mixed; boundary=.*";
        String testToken = "testTokenString";
        String requestMethod = "PUT";
        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(expectedResponse);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, testToken);
        URI resourceUri = mockServer.url("/test").uri();
        Map<String, String> headers = Map.of(
                "Authorization", testToken);

        //invoke the method under test
        Request request = ResourceFacadeHelper.createRequestForMultipartBody(requestMethod, resourceUri, headers,
                multipartBody);
        String response = e2Client.getResponseAsString(request);
        assertEquals("response", expectedResponse, response);

        // Verify the request sent to the mock server
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            String contentType = recordedRequest.getHeader("Content-Type");
            assertTrue("contentType", contentType.matches(expectedContentTypeFormat));
            assertEquals("token", testToken, recordedRequest.getHeader("Authorization"));
            assertEquals("method", requestMethod, recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            assertEquals("Uri", resourceUri.toString(), recordedRequestUrl);

            String expectedRequestBody = getExpectedMultipartBodyString(getBoundary(contentType), data,
                    data.length());
            String receivedRequestBody = recordedRequest.getBody().readUtf8();
            assertEquals("request body", expectedRequestBody, receivedRequestBody);
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenGetResponseAsStringCalled_ThenResponse400Error() throws IOException {
        String requestBody = "{\"key\": \"value\"}";
        String response = "{\"message\": \"BadRequest\"}";
        String contentType = "application/json";
        String testToken = "testTokenString";
        String expectedExceptionMessage = "HTTP request failed. 400: " + response;
        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(400)
                .setBody(response);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, testToken);
        URI resourceUri = mockServer.url("/test").uri();
        Map<String, String> headers = Map.of(
                "Content-Type", contentType,
                "Authorization", testToken);

        Request request = ResourceFacadeHelper.createRequest("GET", resourceUri, headers, requestBody);
        IOException exception = assertThrows(IOException.class, () -> {
            e2Client.getResponseAsString(request);
        });

        assertEquals("Exception message", expectedExceptionMessage, exception.getMessage());
    }

    @Test
    public void GivenCDMClient_WhenGetResponseBody_ThenItReturnResponse() throws IOException {
        String expectedResponse = getSampleMultipartBody();
        String testToken = "testTokenString";

        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setHeader("Content-Type", "multipart/mixed; boundary=\"MIME_boundary_1a2afdc3\"")
                .setResponseCode(200)
                .setBody(expectedResponse);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, testToken);
        URI resourceUri = mockServer.url("/test").uri();
        Map<String, String> headers = Map.of(
                "Authorization", testToken);

        //invoke the method under test
        Request request = ResourceFacadeHelper.createRequest("GET", resourceUri, headers, null);
        ResponseBody responseBody = e2Client.getResponseBody(request);
        assertEquals("response", expectedResponse, responseBody.string());

        // Verify the request sent to the mock server
        try {
            RecordedRequest recordedRequest = mockServer.takeRequest();
            assertEquals("token", testToken, recordedRequest.getHeader("Authorization"));
            assertEquals("method", "GET", recordedRequest.getMethod());

            String recordedRequestUrl = recordedRequest.getRequestUrl().toString();
            assertEquals("Uri", resourceUri.toString(), recordedRequestUrl);
        } catch (InterruptedException e) {
            fail("Unexpected exception:" + e);
        }
    }

    @Test
    public void GivenCDMClient_WhenGetResponseBody_ThenResponse403Error() throws IOException {
        String testToken = "testTokenString";

        //Configure test response from mockWebServer
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(403);
        mockServer.enqueue(mockResponse);

        E2Client e2Client = new E2Client(okHttpClient, testToken);
        URI resourceUri = mockServer.url("/test").uri();
        Map<String, String> headers = Map.of(
                "Authorization", testToken);

        //invoke the method under test
        Request request = ResourceFacadeHelper.createRequest("GET", resourceUri, headers, null);
        IOException exception = assertThrows(IOException.class, () -> {
            e2Client.getResponseBody(request);
        });

        assertNotNull(exception);
    }

    MultipartBody createMultipartBody(String jsonData) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.MIXED);

        // Add the first part
        RequestBody contentBody = RequestBody.create(
                "{}", // JSON content for the first part
                MediaType.parse("application/json")
        );
        builder.addPart(
                okhttp3.Headers.of("Content-Disposition", "attachment; name=\"content\""),
                contentBody
        );

        // Add the second part
        RequestBody dataBody = RequestBody.create(
                jsonData, // JSON content for the second part
                MediaType.parse("application/json")
        );
        builder.addPart(
                okhttp3.Headers.of("Content-Disposition", "attachment; name=\"data\""),
                dataBody
        );
        return builder.build();
    }

    String getBoundary(String contentType) {
        String boundary = "";

        Pattern pattern = Pattern.compile("boundary=(.*)");
        Matcher matcher = pattern.matcher(contentType);
        if (matcher.find()) {
            boundary = matcher.group(1);
        }
        return boundary;
    }

    String getExpectedMultipartBodyString(String boundary, String jsonData, int contentLength) {
        return "--" + boundary + "\r\n" +
                "Content-Disposition: attachment; name=\"content\"\r\n" +
                "Content-Type: application/json; charset=utf-8\r\n" +
                "Content-Length: 2\r\n" +
                "\r\n" +
                "{}\r\n" +
                "--" + boundary + "\r\n" +
                "Content-Disposition: attachment; name=\"data\"\r\n" +
                "Content-Type: application/json; charset=utf-8\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "\r\n" +
                jsonData + "\r\n" +
                "--" + boundary + "--\r\n";
    }

    String getSampleMultipartBody() {
        return "--MIME_boundary_1a2afdc3\n" +
                "Content-Type: application/json\n" +
                "Content-Disposition: attachment; name=content\n" +
                "\n" +
                "{\n" +
                "}\n" +
                "--MIME_boundary_1a2afdc3\n" +
                "Content-Type: application/json\n" +
                "Content-Disposition: attachment; name=data\n" +
                "\n" +
                "{\n" +
                "\t\"url\":\"https:\\/\\/developer.hp.com\",\n" +
                "\t\"colorMode\":\"MONO\",\n" +
                "\t\"paperSize\":\"LETTER\",\n" +
                "\t\"copies\":3,\n" +
                "\t\"desc\":\"option=empty\"\n" +
                "}\n" +
                "--MIME_boundary_1a2afdc3--";
    }
}

