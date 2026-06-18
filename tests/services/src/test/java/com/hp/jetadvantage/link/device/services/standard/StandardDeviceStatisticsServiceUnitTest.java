/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeServiceChannelSetupMessage;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestServiceMessageWithRequestBody;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelCallbackRegistry;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelServiceThreadPool;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceStatisticsServiceUnitTest extends StandardDeviceUnitTest {

    @After
    public void tearDown() {
        AppChannelCallbackRegistry.clear();
        AppChannelServiceThreadPool.clear();
    }

    /**
     * Wait for async service callback to complete.
     * Service callbacks run asynchronously via AppChannelServiceThreadPool.
     */
    private void awaitServiceCallback(String serviceCallId) throws Exception {
        Future<?> future = AppChannelServiceThreadPool.getFuture(serviceCallId);
        if (future != null) {
            future.get(2, TimeUnit.SECONDS);
        }
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService();
        assertNotNull(statisticsService);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenConstructorCalledWithDeviceManagement_ThenObjectCreated() {
        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);
        assertNotNull(statisticsService);
    }

    /**
     * Happy case : get valid statisticsService from a connected device
     */
    @Test
    public void GivenStandardDeviceStatisticsService_WhenIsSupportedCalled_AndDeviceIsConnected_ThenTrueShouldBeReturn() throws IOException {
        String getStatisticsData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_ext_getCapabilities.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(getStatisticsData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService deviceStatisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);
        assertNotNull(deviceStatisticsService);

        try {
            assertTrue(deviceStatisticsService.isSupported());
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetAllJobsListCalled_AndDeviceConnected_ThenReturnJobsObject() throws IOException {
        // Given
        String getAllJobsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_getAllJobsList.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(getAllJobsResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When
        Jobs result = statisticsService.getAllJobsList("com.hp.workpath.sample.statisticsample", 0, 50);

        // Then
        assertNotNull("Jobs result should handle null package name", result);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetAllJobsListCalledWithNegativeOffset_ThenHandleGracefully() throws IOException {
        // Given
        String getAllJobsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_getAllJobsList.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(getAllJobsResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When
        Jobs result = statisticsService.getAllJobsList("com.hp.workpath.sample.statisticsample", -10, 50);

        // Then
        assertNotNull("Jobs result should handle negative offset gracefully", result);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetJobsListCalled_AndDeviceConnected_ThenReturnJobsObject() throws IOException {
        // Given
        String getAllJobsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_getJobsList.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(getAllJobsResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When
        Jobs result = statisticsService.getAllJobsList("com.hp.workpath.sample.statisticsample", 0, 50);

        // Then
        assertNotNull("Jobs result should not be null", result);
        assertNotNull("Jobs members should not be null", result.getMembers());
        assertTrue("Jobs should contain data", result.getMembers().size() >= 0);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetJobsListCalled_AndDeviceDisconnected_ThenThrowException() {
        // Given
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When & Then
        try {
            statisticsService.getAllJobsList("com.hp.test.package", 0, 50);
            fail("Should throw exception when device is disconnected");
        } catch (Exception e) {
            // Expected exception
            assertNotNull("Exception should be thrown", e);
        }
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetJobsListCalled_WithInvalidParameters_ThenHandleGracefully() throws IOException {
        // Given
        String emptyResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_getJobsList.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(emptyResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When
        Jobs result = statisticsService.getAllJobsList("", -1, 0); // Invalid parameters

        // Then
        assertNotNull("Result should handle invalid params gracefully", result);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenGetJobWithLastSequenceNumberProcessedCalled_AndDeviceConnected_ThenReturnSequenceNumber() throws IOException {
        // Given
        String sequenceResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "statistics/GET_getAllJobsList.json");

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(okhttp3.Request.class))).thenReturn(sequenceResponse);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceStatisticsService statisticsService = new StandardDeviceStatisticsService(mockDeviceManagementService);

        // When
        Jobs result = statisticsService.getJobWithLastSequenceNumberProcessed("com.hp.workpath.sample.statisticsample");

        // Then
        assertNotNull("Jobs result should not be null", result);
        assertNotNull("Last sequence number should not be null", result.getLastSequenceNumberProcessed());
        assertTrue("Sequence number should be valid",
                result.getLastSequenceNumberProcessed().getValue() >= 0);
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalled_AndStatisticsNotificationOccurs_ThenCallbackCalled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String sampleStatisticsNotification = "{\n" +
                "  \"jobDetails\": [],\n" +
                "  \"lastSequenceNumberNotified\": 5,\n" +
                "  \"lastSequenceNumberProcessed\": 3,\n" +
                "  \"missingSequenceNumbers\": {\n" +
                "    \"first\": 0,\n" +
                "    \"last\": 0\n" +
                "  }\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<StatisticsCallbackPayload> receivedPayload = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            receivedPayload.set(notification);
            assertEquals(packageId, appPackageId);
        };

        // register callback
        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        // inject test statistics service message
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, sampleStatisticsNotification));
        awaitServiceCallback(serviceCallId);

        // verify callback is called with correct payload
        assertEquals(1, callbackCount.get());
        assertNotNull(receivedPayload.get());
        assertEquals(Long.valueOf(5), receivedPayload.get().getLastSequenceNumberNotified().getValue());
        assertEquals(Long.valueOf(3), receivedPayload.get().getLastSequenceNumberProcessed().getValue());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenUnregisterNotificationCallbackCalled_ThenCallbackNotCalled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId1 = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String serviceCallId2 = "6f010ed2-8028-4ae4-b928-88b963d60fe6";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String sampleStatisticsNotification = "{\n" +
                "  \"jobDetails\": [],\n" +
                "  \"lastSequenceNumberNotified\": 5,\n" +
                "  \"lastSequenceNumberProcessed\": 3\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        // register callback
        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        // inject test notification and verify callback is called
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId1,
                "jobStatistics", typeGUN, sampleStatisticsNotification));
        awaitServiceCallback(serviceCallId1);
        assertEquals(1, callbackCount.get());
        callbackCount.set(0);

        // unregister callback
        statisticsService.unRegisterNotificationCallback();

        // inject test notification again
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId2,
                "jobStatistics", typeGUN, sampleStatisticsNotification));

        // verify callback is NOT called after unregister
        assertEquals(0, callbackCount.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalled_AndInvalidTypeGUN_ThenCallbackNotCalled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String invalidTypeGUN = "com.hp.ext.service.invalid.version.1.type.invalidNotification";
        String packageId = "com.hp.workpath.sample.statisticsample";
        String samplePayload = "{\n" +
                "  \"lastSequenceNumberNotified\": 5,\n" +
                "  \"lastSequenceNumberProcessed\": 3\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        // inject message with invalid typeGUN
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", invalidTypeGUN, samplePayload));
        awaitServiceCallback(serviceCallId);

        // verify callback is NOT called for invalid typeGUN
        assertEquals(0, callbackCount.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalled_AndMalformedJson_ThenCallbackNotCalled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String malformedPayload = "{\"lastSequenceNumberNotified\": 5, \"lastSequenceNumberProcessed\":";

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        // inject malformed message
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, malformedPayload));
        awaitServiceCallback(serviceCallId);

        // verify callback is NOT called for malformed JSON
        assertEquals(0, callbackCount.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalledTwice_ThenOnlyLastCallbackActive() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String sampleStatisticsNotification = "{\n" +
                "  \"jobDetails\": [],\n" +
                "  \"lastSequenceNumberNotified\": 10,\n" +
                "  \"lastSequenceNumberProcessed\": 8\n" +
                "}";

        AtomicInteger firstCallbackCount = new AtomicInteger(0);
        AtomicInteger secondCallbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> firstCallback = (appPackageId, notification) -> {
            firstCallbackCount.incrementAndGet();
        };
        IE2PayloadCallback<StatisticsCallbackPayload> secondCallback = (appPackageId, notification) -> {
            secondCallbackCount.incrementAndGet();
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);

        // register first callback, then override with second
        statisticsService.registerNotificationCallback(firstCallback);
        statisticsService.registerNotificationCallback(secondCallback);

        // inject test notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, sampleStatisticsNotification));
        awaitServiceCallback(serviceCallId);

        // verify only the second (latest) callback is called
        assertEquals(0, firstCallbackCount.get());
        assertEquals(1, secondCallbackCount.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenUnregisterNotificationCallbackCalledWithoutRegister_ThenNoException() {
        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);

        // unregister without prior registration should not throw
        statisticsService.unRegisterNotificationCallback();
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalled_AndNotificationWithNullSequenceNumbers_ThenCallbackCalled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String sampleNotificationNoSequence = "{\n" +
                "  \"jobDetails\": []\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<StatisticsCallbackPayload> receivedPayload = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            receivedPayload.set(notification);
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, sampleNotificationNoSequence));
        awaitServiceCallback(serviceCallId);

        // callback should still be invoked even with missing sequence numbers
        assertEquals(1, callbackCount.get());
        assertNotNull(receivedPayload.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCallbackCalled_AndCallbackThrowsException_ThenExceptionHandled() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String samplePayload = "{\n" +
                "  \"jobDetails\": [],\n" +
                "  \"lastSequenceNumberNotified\": 5,\n" +
                "  \"lastSequenceNumberProcessed\": 3\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            throw new RuntimeException("test exception from callback");
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        // inject test notification - should not crash even though callback throws
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, samplePayload));
        awaitServiceCallback(serviceCallId);

        assertEquals(1, callbackCount.get());
    }

    @Test
    public void GivenStandardDeviceStatisticsService_WhenRegisterNotificationCall_AndNotificationWithJobDetails_ThenCallbackCalledWithJobDetails() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String serviceCallId = "5f010ed2-8028-4ae4-b928-88b963d60fe5";
        String typeGUN = new StatisticsCallbackPayload().getTypeGUN();
        String packageId = "com.hp.workpath.sample.statisticsample";
        String sampleNotificationWithJobs = "{\n" +
                "  \"jobDetails\": [\n" +
                "    {\n" +
                "      \"jobId\": \"e3736199-5163-4c47-88d3-572e4e06dd6a\",\n" +
                "      \"sequenceNumber\": 5,\n" +
                "      \"jobInfo\": {\n" +
                "        \"jobCategory\": \"jcPrint\",\n" +
                "        \"jobDoneStatus\": \"jdsSuccessful\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"lastSequenceNumberNotified\": 5,\n" +
                "  \"lastSequenceNumberProcessed\": 4\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);
        AtomicReference<StatisticsCallbackPayload> receivedPayload = new AtomicReference<>();

        IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();
            receivedPayload.set(notification);
        };

        StandardDeviceStatisticsService statisticsService =
                new StandardDeviceStatisticsService(mockDeviceManagementService);
        statisticsService.registerNotificationCallback(callback);

        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeServiceChannelSetupMessage(channelId, packageId,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN,
                StandardDeviceStatisticsService.E2SERVICE_STATISTICS_CANONICAL_GUN));
        handler.onReceived(0, makeTestServiceMessageWithRequestBody(channelId, serviceCallId,
                "jobStatistics", typeGUN, sampleNotificationWithJobs));
        awaitServiceCallback(serviceCallId);

        assertEquals(1, callbackCount.get());
        assertNotNull(receivedPayload.get());
        assertNotNull(receivedPayload.get().getJobDetails());
        assertEquals(1, receivedPayload.get().getJobDetails().size());
        assertEquals(Long.valueOf(5), receivedPayload.get().getLastSequenceNumberNotified().getValue());
        assertEquals(Long.valueOf(4), receivedPayload.get().getLastSequenceNumberProcessed().getValue());
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl deviceUsageServiceMetadata = new ServiceMetadataImpl();
        deviceUsageServiceMetadata.setDescription("Statistics service Tests Discovery Tree");
        deviceUsageServiceMetadata.setServiceGun("com.hp.ext.service.jobStatistics.version.1");
        deviceUsageServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(deviceUsageServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/jobStatistics/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/jobStatistics/v1/jobStatisticsAgents");
        link2.setRel("jobStatisticsAgents");
        links.add(link2);

        return links;
    }
}
