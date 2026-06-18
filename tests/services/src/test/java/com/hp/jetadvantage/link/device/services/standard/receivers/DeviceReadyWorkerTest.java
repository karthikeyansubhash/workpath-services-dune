package com.hp.jetadvantage.link.device.services.standard.receivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.testing.TestWorkerBuilder;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Unit tests for DeviceReadyWorker.
 *
 * Covers:
 * 1. AIDL connected immediately          → Result.success()
 * 2. AIDL never connects (timeout)       → Result.retry()  [CHANGED behavior]
 * 3. initialize() throws Exception       → Result.retry()
 * 4. ip or token is null                 → Result.success() (FAIL:xxx path)
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceReadyWorkerTest {

    private static final String VALID_IP = "192.168.1.1";
    private static final String VALID_TOKEN = "test-token-abc";

    @Mock
    private Context mockContext;

    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;

    private Executor executor;

    @Before
    public void setUp() {
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Creates a DeviceReadyWorker via TestWorkerBuilder with given input data.
     * waitCount and sleepIntervalMs are set to minimal values for fast unit testing.
     */
    private DeviceReadyWorker buildWorker(String ip, String token) {
        Data inputData = new Data.Builder()
                .putString(DeviceReadyReceiver.DEVICE_IP, ip)
                .putString(DeviceReadyReceiver.DEVICE_TOKEN, token)
                .build();
        DeviceReadyWorker worker = TestWorkerBuilder.from(mockContext, DeviceReadyWorker.class, executor)
                .setInputData(inputData)
                .build();
        // Override for fast unit tests: 1 × 1ms instead of 1000 × 100ms
        worker.mWaitCount = 1;
        worker.mSleepIntervalMs = 1;
        return worker;
    }

    // ===== Success path =====

    /**
     * Given: valid ip/token, initialize() succeeds, AIDL connects immediately
     * When:  doWork() is called
     * Then:  Result.success() is returned
     */
    @Test
    public void GivenServiceConnectedImmediately_WhenDoWork_ThenReturnSuccess() throws Exception {
        DeviceReadyWorker worker = buildWorker(VALID_IP, VALID_TOKEN);

        try (MockedStatic<StandardDeviceManagementService> mockedSingleton =
                     mockStatic(StandardDeviceManagementService.class);
             MockedStatic<StandardWebsocketCallbackService> mockedWs =
                     mockStatic(StandardWebsocketCallbackService.class)) {

            mockedSingleton.when(StandardDeviceManagementService::getInstance)
                    .thenReturn(mockDeviceManagementService);
            // initialize() does nothing (no exception)

            // AIDL reports connected immediately
            mockedWs.when(StandardWebsocketCallbackService::getServiceConnected)
                    .thenReturn(true);

            ListenableWorker.Result result = worker.doWork();

            assertEquals(ListenableWorker.Result.success(), result);
        }
    }

    // ===== Retry path: AIDL timeout =====

    /**
     * Given: valid ip/token, initialize() succeeds, but AIDL never connects (timeout)
     * When:  doWork() is called
     * Then:  Result.retry() is returned
     */
    @Test
    public void GivenServiceNeverConnects_WhenDoWork_ThenReturnRetry() throws Exception {
        DeviceReadyWorker worker = buildWorker(VALID_IP, VALID_TOKEN);

        try (MockedStatic<StandardDeviceManagementService> mockedSingleton =
                     mockStatic(StandardDeviceManagementService.class);
             MockedStatic<StandardWebsocketCallbackService> mockedWs =
                     mockStatic(StandardWebsocketCallbackService.class)) {

            mockedSingleton.when(StandardDeviceManagementService::getInstance)
                    .thenReturn(mockDeviceManagementService);

            // AIDL never connects
            mockedWs.when(StandardWebsocketCallbackService::getServiceConnected)
                    .thenReturn(false);

            ListenableWorker.Result result = worker.doWork();

            assertEquals(ListenableWorker.Result.retry(), result);
        }
    }

    // ===== Retry path: initialize() exception =====

    /**
     * Given: valid ip/token, but initialize() throws a RuntimeException
     * When:  doWork() is called
     * Then:  Result.retry() is returned
     */
    @Test
    public void GivenInitializeThrowsException_WhenDoWork_ThenReturnRetry() throws Exception {
        DeviceReadyWorker worker = buildWorker(VALID_IP, VALID_TOKEN);

        try (MockedStatic<StandardDeviceManagementService> mockedSingleton =
                     mockStatic(StandardDeviceManagementService.class)) {

            mockedSingleton.when(StandardDeviceManagementService::getInstance)
                    .thenReturn(mockDeviceManagementService);
            doThrow(new RuntimeException("network error"))
                    .when(mockDeviceManagementService)
                    .initialize(any(Context.class), anyString(), anyString());

            ListenableWorker.Result result = worker.doWork();

            assertEquals(ListenableWorker.Result.retry(), result);
        }
    }

    // ===== Null ip/token path =====

    /**
     * Given: ip is null (invalid input)
     * When:  doWork() is called
     * Then:  Result.success() is returned (FAIL:xxx is logged but no retry)
     */
    @Test
    public void GivenNullIp_WhenDoWork_ThenReturnSuccess() throws Exception {
        DeviceReadyWorker worker = buildWorker(null, VALID_TOKEN);

        try (MockedStatic<StandardDeviceManagementService> mockedSingleton =
                     mockStatic(StandardDeviceManagementService.class)) {

            mockedSingleton.when(StandardDeviceManagementService::getInstance)
                    .thenReturn(mockDeviceManagementService);

            ListenableWorker.Result result = worker.doWork();

            // Current behavior: logs FAIL:xxx but returns success.
            // Consider changing to Result.failure() for invalid input.
            assertEquals(ListenableWorker.Result.success(), result);
        }
    }

    /**
     * Given: token is null (invalid input)
     * When:  doWork() is called
     * Then:  Result.success() is returned (FAIL:xxx is logged but no retry)
     */
    @Test
    public void GivenNullToken_WhenDoWork_ThenReturnSuccess() throws Exception {
        DeviceReadyWorker worker = buildWorker(VALID_IP, null);

        try (MockedStatic<StandardDeviceManagementService> mockedSingleton =
                     mockStatic(StandardDeviceManagementService.class)) {

            mockedSingleton.when(StandardDeviceManagementService::getInstance)
                    .thenReturn(mockDeviceManagementService);

            ListenableWorker.Result result = worker.doWork();

            assertEquals(ListenableWorker.Result.success(), result);
        }
    }
}

