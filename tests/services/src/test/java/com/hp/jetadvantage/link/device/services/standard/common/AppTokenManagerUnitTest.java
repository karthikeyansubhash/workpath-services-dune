package com.hp.jetadvantage.link.device.services.standard.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUnitTest;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class AppTokenManagerUnitTest extends StandardDeviceUnitTest {

    @Test
    public void GivenAppTokenManager_WhenConstructorCalled_ThenObjectCreated() {
        AppTokenManager appTokenManager = new AppTokenManager(mockCDMClient);
        assertNotNull(appTokenManager);
    }

    @Test
    public void GivenAppTokenManager_WhenGetSolutionTokenCalled_ThenSolutionTokenCreated() throws IOException {
        String testSolutionId = "11111111-1111-1111-9999-111111111111";
        String expectedToken = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7NzI3YjkzNDctMDliNC00ODlhLTk4ZTItZTZmNDY1OTExMDYwfSJ9";
        String expectedUrl = "/cdm/e2WorkpathInterop/v1/appToken/" + testSolutionId;

        //define mocked CDMClient's expected behavior
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "e2interop/GET_appToken.json");
        CDMResponse<String> expectedResponse = new CDMResponse<>(201, response);
        when(mockCDMClient.sendGetRequest(expectedUrl)).thenReturn(expectedResponse);

        AppTokenManager appTokenManager = new AppTokenManager(mockCDMClient);
        String token = appTokenManager.getSolutionToken(testSolutionId);
        assertFalse(token.isEmpty());
        assertEquals(expectedToken, token);
    }

    @Test
    public void GivenAppTokenManager_WhenGetSolutionTokenCalled_tokenIsCached_ThenCachedSolutionTokenReturned() throws IOException {
        String testSolutionId = "11111111-1111-1111-9999-111111111111";
        String expectedToken = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7NzI3YjkzNDctMDliNC00ODlhLTk4ZTItZTZmNDY1OTExMDYwfSJ9";
        String expectedUrl = "/cdm/e2WorkpathInterop/v1/appToken/" + testSolutionId;

        //define mocked CDMClient's expected behavior
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "e2interop/GET_appToken.json");
        CDMResponse<String> expectedResponse = new CDMResponse<>(201, response);
        when(mockCDMClient.sendGetRequest(expectedUrl)).thenReturn(expectedResponse);

        //get the token for the first time
        AppTokenManager appTokenManager = new AppTokenManager(mockCDMClient);
        String token = appTokenManager.getSolutionToken(testSolutionId);
        assertFalse(token.isEmpty());
        assertEquals(expectedToken, token);

        //get the token for the second time : the token should be returned from cache
        token = appTokenManager.getSolutionToken(testSolutionId);
        assertEquals(expectedToken, token);

        //get the token for the third time : the token should be returned from cache
        token = appTokenManager.getSolutionToken(testSolutionId);
        assertEquals(expectedToken, token);

        //verify the mocked CDMClient.sendGetRequest is called only once
        verify(mockCDMClient, times(1)).sendGetRequest(expectedUrl);
    }

    @Test
    public void GivenAppTokenManager_WhenGetSolutionTokenCalled_tokenExpired_ThenNewSolutionTokenReturned() throws Exception {
        String testSolutionId = "11111111-1111-1111-9999-111111111111";
        String expectedToken = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7NzI3YjkzNDctMDliNC00ODlhLTk4ZTItZTZmNDY1OTExMDYwfSJ9";
        String expectedToken2 = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7MzgwNWU4ODItZGRlYy00OWRmLTk0MzQtNDY0ZjJlZDdhYTgwfSJ9";
        String expectedUrl = "/cdm/e2WorkpathInterop/v1/appToken/" + testSolutionId;

        //define mocked CDMClient's expected behavior
        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "e2interop/GET_appToken.json");
        CDMResponse<String> expectedResponse = new CDMResponse<>(201, response);
        when(mockCDMClient.sendGetRequest(expectedUrl)).thenReturn(expectedResponse);

        //create TestAppTokenManager object to test expired token scenario
        TestAppTokenManager appTokenManager = new TestAppTokenManager(mockCDMClient);

        //get the token for the first time
        String token = appTokenManager.getSolutionToken(testSolutionId);
        assertFalse(token.isEmpty());
        assertEquals(expectedToken, token);


        //define mocked CDMClient's expected behavior to verify that it should call the CDMClient if the token is expired
        String response2 = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "e2interop/GET_appToken2.json");
        CDMResponse<String> expectedResponse2 = new CDMResponse<>(201, response2);
        when(mockCDMClient.sendGetRequest(expectedUrl)).thenReturn(expectedResponse2);

        //get the token for the second time : the expired cached token should not be returned
        token = appTokenManager.getSolutionToken(testSolutionId);
        assertEquals(expectedToken2, token);

        verify(mockCDMClient, times(2)).sendGetRequest(expectedUrl);
    }

    @Test
    public void GivenAppTokenManager_WhenGetSolutionTokenCalled_403ErrorOccurs_ThenEmptyTokenCreated() throws IOException {
        String testSolutionId = "11111111-1111-1111-9999-111111111111";
        String expectedUrl = "/cdm/e2WorkpathInterop/v1/appToken/" + testSolutionId;

        //define mocked CDMClient's expected behavior
        String response = "";
        CDMResponse<String> expectedResponse = new CDMResponse<>(403, response);
        when(mockCDMClient.sendGetRequest(expectedUrl)).thenReturn(expectedResponse);

        AppTokenManager appTokenManager = new AppTokenManager(mockCDMClient);
        String token = appTokenManager.getSolutionToken(testSolutionId);
        assertTrue(token.isEmpty());
    }

    //TestAppTokenManager : AppTokenManager class with overridden getAppTokenLifeTimeThresholdSecs method to test expired token scenario
    public class TestAppTokenManager extends AppTokenManager {
        public TestAppTokenManager(CDMClient cdmClient) {
            super(cdmClient);
        }

        @Override
        protected long getAppTokenLifeTimeThresholdSecs() {
            // return 0 means token life time is expired
            return 0L;
        }
    }
}
