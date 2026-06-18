package com.hp.workpath.apitest.accessory.hid;

import static com.hp.workpath.api.Result.RESULT_OK;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.OWNED_FEATURE_REPORT_LENGTH;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.OWNED_INPUT_REPORT_LENGTH;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.OWNED_OUTPUT_REPORT_LENGTH;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.OWNED_PRODUCT_ID;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.OWNED_VENDOR_ID;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.SHARED_PRODUCT_ID;
import static com.hp.workpath.apitest.TestApp.AccessoryRR.SHARED_VENDOR_ID;
import static com.hp.workpath.apitest.util.Utils.isValidUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.accessory.AccessoryInfo;
import com.hp.workpath.api.accessory.RegistrationType;
import com.hp.workpath.api.accessory.ReportEventInfo;
import com.hp.workpath.api.accessory.hid.AccessoryService;
import com.hp.workpath.api.accessory.hid.EventCode;
import com.hp.workpath.api.accessory.hid.HIDAccessoryInfo;
import com.hp.workpath.api.accessory.hid.HIDInfo;
import com.hp.workpath.api.accessory.hid.HIDReport;
import com.hp.workpath.api.accessory.hid.HIDReportEventInfo;
import com.hp.workpath.api.accessory.hid.HIDReportType;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AccessoryServiceTest {
    public static final String ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP = "com.hp.workpath.api.accessory" +
            ".ACCESSORY_CONTEXT_CHANGE_ACTION";
    public static final String ACCESSORY_STATUS_ACTION = "com.hp.workpath.action.ACCESSORY_STATUS_ACTION";
    public static final String ACCESSORY_CHANGE_ACTION = "com.hp.workpath.api.accessory.ACCESSORY_CHANGE_ACTION";
    public static final String ACCESSORY_REPORT_ACTION = "com.hp.workpath.action.ACCESSORY_REPORT_ACTION";

    private static final String TAG = "AccessoryServiceTest";
    private static final int TIMEOUT_SEC = 10;
    private static Context mContext;
    private final DutController mDutController;
    private String mSimHidDeviceId;
    private ContextChangeEventReceiver mContextChangeEventReceiver;
    private ReportReceiver mReportReceiver;

    public AccessoryServiceTest() {
        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    private static void validateOwnedHidAccessoryInfo(AccessoryInfo ai) {
        assertEquals("Accessory class", AccessoryInfo.AccessoryClass.HID, ai.getAccessoryClass());
        assertEquals("Registration type", RegistrationType.OWNED, ai.getRegistrationType());

        HIDAccessoryInfo hidAccessoryInfo = ai.getDetails();
        assertEquals("Product Id", OWNED_PRODUCT_ID, hidAccessoryInfo.getProductId());
        assertEquals("Vendor Id", OWNED_VENDOR_ID, hidAccessoryInfo.getVendorId());
        assertEquals("Serial Number", "NULL", hidAccessoryInfo.getSerialNumber());
        assertEquals("Description", "X773333", hidAccessoryInfo.getDescription());
        assertEquals("Manufacturer", "Api, Test.", hidAccessoryInfo.getManufacturer());
    }

    private static void validateHidReport(HIDReportEventInfo hidReportEventInfo, String expectedBase64Data) {
        List<HIDReport> hidReports = hidReportEventInfo.getReports();
        assertNotNull("HIDReport should not be null", hidReports);
        assertEquals("HIDReport count", 1, hidReports.size());
        assertEquals("HIDReport type", HIDReportType.INPUT, hidReports.get(0).getType());
        assertEquals("HIDReport data", expectedBase64Data,
                Base64.getEncoder().encodeToString(hidReports.get(0).getData()));
    }

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            fail("Workpath.getInstance().initialize failed");
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        // only call stopApp once
        mDutController.stopApp(mContext);
        mDutController.getAccessoryController().cleanup();
    }

    @Test
    public void AccessoryService_isSupported_$ReturnsTrue() {
        boolean supported = AccessoryService.isSupported(mContext);
        assertTrue("isSupported", supported);
    }

    @Test
    public void AccessoryService_isReady_$ReturnsTrue() {
        boolean ready = AccessoryService.isReady(mContext);
        assertTrue("isReady", ready);
    }

    @Test
    public void AbstractAccessoryStartObserver_onReady_$GivenOwnedAccessoryAttached_WhenObserverRegistered_ThenOnReadyCalled() {
        AccessoryStartObserverTest accessoryStartObserver = null;
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Handler handler = new Handler(Looper.getMainLooper());
            accessoryStartObserver = new AccessoryStartObserverTest(handler, latch);
            accessoryStartObserver.register(mContext);

            Log.i(TAG, "onReady: STEP1 ------------ Attach Owned USB HID Accessory");
            String simHidId = mDutController.getAccessoryController().attachOwnedUsbHidAccessory();
            if (simHidId == null) {
                fail("attachOwnedUsbHidAccessory returned null");
            }

            Log.i(TAG, "onReady: STEP2 ------------ Wait AbstractAccessoryStartObserver.onReady() callback");
            boolean onReadyCalled = latch.await(TIMEOUT_SEC, TimeUnit.SECONDS);
            assertTrue("onReadyCalled", onReadyCalled);
            validateOwnedHidAccessoryInfo(accessoryStartObserver.getAccessoryInfo());
            assertTrue("getAccessoryContextId", isValidUUID(accessoryStartObserver.getAccessoryContextId()));
        } catch (Exception e) {

        } finally {
            if (accessoryStartObserver != null) {
                accessoryStartObserver.unregister(mContext);
                mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
            }
        }
    }

    @Test
    public void AccessoryService_getOwnedAccessories_$GivenOwnedAccessoryAttached_WhenGetOwnedAccessories_ThenReturnsAccessoryInfoList() {
        try {
            String simHidId = mDutController.getAccessoryController().attachOwnedUsbHidAccessory();
            if (simHidId == null) {
                fail("attachOwnedUsbHidAccessory returned null");
            }
            Result result = new Result();
            List<AccessoryInfo> list = AccessoryService.getOwnedAccessories(mContext, result);
            assertEquals("result code", RESULT_OK, result.getCode());
            assertTrue("Owned accessories list", list != null && !list.isEmpty());
            validateOwnedHidAccessoryInfo(list.get(0));
        } catch (Exception e) {
            fail("getOwnedAccessories Exception: " + e.getMessage());
        } finally {
            mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
        }
    }

    @Test
    public void AccessoryService_resendOwnedAccessoryContext_$GivenOwnedAccessory_WhenResend_ThenContextResentAndBroadcastReceived() {
        try {
            Log.i(TAG, "Resend: STEP1 ------------ Attach Owned USB HID Accessory");
            String simHidId = mDutController.getAccessoryController().attachOwnedUsbHidAccessory();
            if (simHidId == null) {
                fail("attachOwnedUsbHidAccessory returned null");
            }

            Log.i(TAG, "Resend: STEP2 ------------ Get Owned USB HID Accessories");
            Result result = new Result();
            List<AccessoryInfo> list = AccessoryService.getOwnedAccessories(mContext, result);
            assertEquals("Owned accessories count", 1, list.size());

            CountDownLatch latch = new CountDownLatch(1);
            Handler handler = new Handler(Looper.getMainLooper());
            AccessoryObserverTest accessoryObserver = new AccessoryObserverTest(handler, latch);
            accessoryObserver.setObservedEvent(EventCode.CONTEXT_RESENT);
            accessoryObserver.register(mContext);

            CountDownLatch broadcastReceiverLatch = new CountDownLatch(2);
            registerDynamicReceiversForContextChange(mContext, broadcastReceiverLatch, EventCode.CONTEXT_RESENT);

            Log.i(TAG, "Resend: STEP3 ------------ Resend Owned Accessory Context : " + list.get(0));
            AccessoryService.resendOwnedAccessoryContext(mContext, list.get(0), result);
            if (result.getCode() != RESULT_OK) {
                Log.i(TAG, ">>> Previous test attach/detach events may not have fully completed. Retrying resend");
                Thread.sleep(1000);
                AccessoryService.resendOwnedAccessoryContext(mContext, list.get(0), result);
            }
            assertEquals("Result of resend", RESULT_OK, result.getCode());

            Log.i(TAG, "Resend: STEP4 ------------ Verify observed context change event");
            String accessoryId = null;
            if (latch.await(TIMEOUT_SEC, TimeUnit.SECONDS)) {
                EventCode eventCode = accessoryObserver.getOnContextChangeEventCode();
                assertEquals("Context event code", EventCode.CONTEXT_RESENT, eventCode);

                AccessoryInfo accessoryInfo = accessoryObserver.getOnContextChangeAccessoryInfo();
                validateOwnedHidAccessoryInfo(accessoryInfo);

                accessoryId = accessoryObserver.getOnContextChangeAccessoryContextId();
                assertTrue("AccessoryContextId should be valid", isValidUUID(accessoryId));
                Log.i(TAG, "Received accessoryId: " + accessoryId);
            } else {
                fail("Did not receive AbstractAccessoryObserver.onContextChange notification");
            }

            Log.i(TAG, "Resend: STEP5 ------------ Verify observed 2 broadcast events");
            if (broadcastReceiverLatch.await(TIMEOUT_SEC, TimeUnit.SECONDS)) {
                assertEquals("getAccessoryContextIdFromContextChangeAction",
                        mContextChangeEventReceiver.getAccessoryContextIdFromContextChangeAction(), accessoryId);
                assertEquals("getAccessoryContextIdFromContextChangeAction",
                        mContextChangeEventReceiver.getAccessoryContextIdFromContextChangeAction(), accessoryId);
            } else {
                fail("Did not receive 2 broadcast events for resending owned accessory context");
            }
            unregisterDynamicReceiversForContextChange(mContext);
            accessoryObserver.unregister(mContext);
        } catch (Exception e) {
            fail("Resend Exception: " + e.getMessage());
        } finally {
            mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
        }
    }

    @Test
    public void AccessoryService_open_getInfo_close_$GivenAccessoryContext_WhenOpenGetInfoClose_ThenReturnsResultOK() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Handler handler = new Handler(Looper.getMainLooper());
            AccessoryObserverTest accessoryObserver = new AccessoryObserverTest(handler, latch);
            accessoryObserver.setObservedEvent(EventCode.CONTEXT_CREATED);
            accessoryObserver.register(mContext);

            Log.i(TAG, "open: STEP1 ------------ Attach Owned USB HID Accessory");
            String simHidId = mDutController.getAccessoryController().attachOwnedUsbHidAccessory();
            if (simHidId == null) {
                fail("attachOwnedUsbHidAccessory returned null");
            }

            Log.i(TAG, "open: STEP2 ------------ Waiting for CONTEXT_CREATED event");
            boolean contextCreated = latch.await(TIMEOUT_SEC, TimeUnit.SECONDS);
            if (!contextCreated) {
                fail("Did not receive AbstractAccessoryObserver.onContextChange notification");
            }
            String accessoryId = accessoryObserver.getOnContextChangeAccessoryContextId();
            assertNotNull("AccessoryContextId should not be null", accessoryId);

            Log.i(TAG, "open: STEP3 ------------ Open AccessoryContextId: " + accessoryId);
            Result result = new Result();
            AccessoryService.open(mContext, accessoryId, result);
            assertEquals("Open result", RESULT_OK, result.getCode());

            Log.i(TAG, "open: STEP4 ------------ Get info for AccessoryContextId: " + accessoryId);
            HIDInfo hidInfo = AccessoryService.getInfo(mContext, accessoryId, result);
            assertNotNull("HIDInfo should not be null", hidInfo);
            assertEquals("FeatureReportLength", OWNED_FEATURE_REPORT_LENGTH, hidInfo.getFeatureReportLength());
            assertEquals("InputReportLength", OWNED_INPUT_REPORT_LENGTH, hidInfo.getInputReportLength());
            assertEquals("OutputReportLength", OWNED_OUTPUT_REPORT_LENGTH, hidInfo.getOutputReportLength());
            assertEquals("isReading flag", false, hidInfo.isReading());

            Log.i(TAG, "open: STEP5 ------------ Close AccessoryContextId: " + accessoryId);
            AccessoryService.close(mContext, accessoryId, result);
            assertEquals("Close result", RESULT_OK, result.getCode());
        } catch (Exception e) {
            fail("Open/GetInfo/Close Exception: " + e.getMessage());
        } finally {
            mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
        }
    }

    @Test
    public void AccessoryService_readReport_writeReport_$GivenOwnedAccessory_WhenReadAndWrite_ThenReturnsResultOK() {
        try {
            String accessoryId = resendOwnedAccessoryContext("readReport", 1);
            Result result = new Result();
            Log.i(TAG, "readReport: STEP5 ------------ Open AccessoryContextId: " + accessoryId);
            AccessoryService.open(mContext, accessoryId, result);
            assertEquals("Open result", RESULT_OK, result.getCode());

            read_write_report("readReport", 6, accessoryId);
        } catch (Exception e) {
            fail("readReport/writeReport Exception: " + e);
        } finally {
            mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
        }
    }

    @Test
    public void AccessoryService_startReading_stopReading_$GivenAccessoryOpen_WhenStartReadingAndStopReading_ThenReportsReceivedAndStopped() {
        final String base64CardTagData = "AAAhAAAAAAA=";
        AccessoryObserverTest accessoryObserver = null;
        try {
            String accessoryId = resendOwnedAccessoryContext("startReading", 1);
            Result result = new Result();
            Log.i(TAG, "startReading: STEP5 ------------ Open AccessoryContextId: " + accessoryId);
            AccessoryService.open(mContext, accessoryId, result);
            assertEquals("Open result", RESULT_OK, result.getCode());

            Log.i(TAG, "startReading: STEP6 ------------ Start reading for AccessoryContextId: " + accessoryId);
            AccessoryService.startReading(mContext, accessoryId, result);
            assertEquals("StartReading result", RESULT_OK, result.getCode());

            CountDownLatch contextLatch = new CountDownLatch(1);
            CountDownLatch dataLatch = new CountDownLatch(1);
            Handler handler = new Handler(Looper.getMainLooper());
            accessoryObserver = new AccessoryObserverTest(handler, contextLatch, dataLatch);
            accessoryObserver.register(mContext);

            CountDownLatch broadcastReceiverLatch = new CountDownLatch(2);
            registerDynamicReceiversForReport(mContext, broadcastReceiverLatch, base64CardTagData);

            Log.i(TAG,
                    "startReading: STEP7 ------------ Sending async HID report: " + mSimHidDeviceId + ", " + base64CardTagData);
            mDutController.getAccessoryController().sendAsyncHidReport(mSimHidDeviceId, base64CardTagData);

            // Wait for AbstractAccessoryObserver.onReceive() to be called to get the async HID report
            boolean dataReceived = dataLatch.await(TIMEOUT_SEC, TimeUnit.SECONDS);
            assertTrue("Async HID report data was not received", dataReceived);

            AccessoryInfo accessoryInfo = accessoryObserver.getOnReceiveAccessoryInfo();
            validateOwnedHidAccessoryInfo(accessoryInfo);
            ReportEventInfo reportEventInfo = accessoryObserver.getOnReceiveReportEventInfo();
            HIDReportEventInfo hidReportEventInfo = reportEventInfo.getDetails();
            validateHidReport(hidReportEventInfo, base64CardTagData);

            // Wait for broadcast events to get the async HID report
            boolean broadcastReceived = broadcastReceiverLatch.await(TIMEOUT_SEC, TimeUnit.SECONDS);
            assertTrue("Broadcast was not received for HID reports", broadcastReceived);

            Log.i(TAG, "startReading: STEP8 ------------ Stop reading for AccessoryContextId: " + accessoryId);
            AccessoryService.stopReading(mContext, accessoryId, result);
            assertEquals("StopReading result", RESULT_OK, result.getCode());

            Log.i(TAG, "startReading: STEP9 ------------ Close AccessoryContextId: " + accessoryId);
            AccessoryService.close(mContext, accessoryId, result);
            assertEquals("Close result", RESULT_OK, result.getCode());

        } catch (Exception e) {
            fail("Start/StopReading Exception: " + e);
        } finally {
            unregisterDynamicReceiversForReport(mContext);
            if (accessoryObserver != null) {
                accessoryObserver.unregister(mContext);
            }
            mDutController.getAccessoryController().detachOwnedUsbHidAccessory();
        }
    }

    @Test
    public void AccessoryService_reserveSharedAccessory_releaseSharedAccessory_$GivenSharedAccessory_WhenReserveAndRelease_ThenReturnsResultOK() {
        try {
            Log.i(TAG, "reserveSharedAccessory: STEP1 ------------ Attach Shared USB HID Accessory");
            String simSharedHidId = mDutController.getAccessoryController().attachSharedUsbHidAccessory();
            if (simSharedHidId == null) {
                fail("attachSharedUsbHidAccessory returned null");
            }

            Log.i(TAG, "reserveSharedAccessory: STEP2 ------------ Launching App for UI Context");
            mDutController.startApp(mContext);

            Log.i(TAG, "reserveSharedAccessory: STEP3 ------------ Get shared accessories");
            Result result = new Result();
            List<AccessoryInfo> accessoryInfoList = AccessoryService.getSharedAccessories(mContext, result);
            assertNotNull("Shared accessory list should not be null", accessoryInfoList);
            assertEquals("Shared accessory count", 1, accessoryInfoList.size());
            validateSharedHidAccessoryInfo(accessoryInfoList.get(0));

            Log.i(TAG, "reserveSharedAccessory: STEP4 ------------ Reserve Shared Accessory");
            String accessoryId = AccessoryService.reserveSharedAccessory(mContext, accessoryInfoList.get(0), result);
            assertNotNull("Reserved accessoryId should not be null", accessoryId);

            Log.i(TAG, "reserveSharedAccessory: STEP5 ------------ Open AccessoryContextId: " + accessoryId);
            AccessoryService.open(mContext, accessoryId, result);
            assertEquals("Open result", RESULT_OK, result.getCode());

            Log.i(TAG, "reserveSharedAccessory: STEP6 ------------ Get info for AccessoryContextId: " + accessoryId);
            HIDInfo hidInfo = AccessoryService.getInfo(mContext, accessoryId, result);
            assertNotNull("HIDInfo should not be null", hidInfo);
            assertEquals("FeatureReportLength", OWNED_FEATURE_REPORT_LENGTH, hidInfo.getFeatureReportLength());
            assertEquals("InputReportLength", OWNED_INPUT_REPORT_LENGTH, hidInfo.getInputReportLength());
            assertEquals("OutputReportLength", OWNED_OUTPUT_REPORT_LENGTH, hidInfo.getOutputReportLength());
            assertEquals("isReading flag", false, hidInfo.isReading());

            int nextStep = read_write_report("reserveSharedAccessory", 7, accessoryId);

            Log.i(TAG,
                    "reserveSharedAccessory: STEP" + (nextStep++) + " ------------ Close AccessoryContextId: " + accessoryId);
            AccessoryService.close(mContext, accessoryId, result);
            assertEquals("Close result", RESULT_OK, result.getCode());

            Log.i(TAG, "reserveSharedAccessory: STEP" + nextStep + " ------------ Release Shared Accessory");
            AccessoryService.releaseSharedAccessory(mContext, accessoryId, result);
            assertEquals("Release result", RESULT_OK, result.getCode());
        } catch (Exception e) {
            fail("reserveSharedAccessory Exception: " + e);
        } finally {
            mDutController.getAccessoryController().detachSharedUsbHidAccessory();
            mDutController.stopApp(mContext);
        }
    }

    // Helper method to resend owned accessory context and return the accessoryId
    private String resendOwnedAccessoryContext(String testTag, int step) throws InterruptedException {
        String accessoryId = null;
        Log.i(TAG, testTag + ": STEP" + step++ + " ------------ Attach Owned USB HID Accessory");
        mSimHidDeviceId = mDutController.getAccessoryController().attachOwnedUsbHidAccessory();
        if (mSimHidDeviceId == null) {
            fail("attachOwnedUsbHidAccessory returned null");
        }

        Log.i(TAG, testTag + ": STEP" + (step++) + " ------------ Get Owned USB HID Accessories");
        Result result = new Result();
        List<AccessoryInfo> list = AccessoryService.getOwnedAccessories(mContext, result);
        assertEquals("Owned accessories count", 1, list.size());

        CountDownLatch latch = new CountDownLatch(1);
        Handler handler = new Handler(Looper.getMainLooper());
        AccessoryObserverTest accessoryObserver = new AccessoryObserverTest(handler, latch);
        accessoryObserver.setObservedEvent(EventCode.CONTEXT_RESENT);
        accessoryObserver.register(mContext);

        Log.i(TAG, testTag + ": STEP" + (step++) + " ------------ Resend Owned Accessory Context");
        AccessoryService.resendOwnedAccessoryContext(mContext, list.get(0), result);
        if (result.getCode() != RESULT_OK) {
            Log.i(TAG, ">>> Previous test attach/detach events may not have fully completed. Retrying resend");
            Thread.sleep(1000);
            AccessoryService.resendOwnedAccessoryContext(mContext, list.get(0), result);
        }
        assertEquals("Resend result", RESULT_OK, result.getCode());

        boolean contextChanged = latch.await(TIMEOUT_SEC, TimeUnit.SECONDS);
        Log.i(TAG, testTag + ": STEP" + step + " ------------ Verify observed context change event");
        if (contextChanged) {
            EventCode eventCode = accessoryObserver.getOnContextChangeEventCode();
            assertEquals("Context event code", EventCode.CONTEXT_RESENT, eventCode);

            AccessoryInfo accessoryInfo = accessoryObserver.getOnContextChangeAccessoryInfo();
            validateOwnedHidAccessoryInfo(accessoryInfo);

            accessoryId = accessoryObserver.getOnContextChangeAccessoryContextId();
            assertTrue("AccessoryContextId should be valid", isValidUUID(accessoryId));
            Log.i(TAG, "Received accessoryId: " + accessoryId);
        } else {
            fail("Did not receive AbstractAccessoryObserver.onContextChange notification");
        }
        accessoryObserver.unregister(mContext);
        return accessoryId;
    }

    // Helper method for read and write report operations; returns next step number
    private int read_write_report(String testTag, int step, String accessoryId) {
        Result result = new Result();
        final String expectedInitialReadData = "TG9ydXMgaXNwc3Vt";
        final String writeData = "dGVzdCBvawAAAA==";

        Log.i(TAG, testTag + ": STEP" + (step++) + " ------------ Read report for AccessoryContextId: " + accessoryId);
        HIDReport hidReport = AccessoryService.readReport(mContext, accessoryId, HIDReportType.FEATURE, result);
        assertEquals("Read report result", RESULT_OK, result.getCode());
        assertEquals("HIDReport type", HIDReportType.FEATURE, hidReport.getType());
        Log.i(TAG, "Data: " + Arrays.toString(hidReport.getData()));
        assertEquals("Initial read data", expectedInitialReadData,
                Base64.getEncoder().encodeToString(hidReport.getData()));

        Log.i(TAG, testTag + ": STEP" + (step++) + " ------------ Write report for AccessoryContextId: " + accessoryId);
        HIDReport writeReport = new HIDReport(HIDReportType.FEATURE, Base64.getDecoder().decode(writeData));
        AccessoryService.writeReport(mContext, accessoryId, writeReport, result);
        assertEquals("Write report result", RESULT_OK, result.getCode());

        Log.i(TAG, testTag + ": STEP" + (step++) + " ------------ Read report again for accessoryId: " + accessoryId);
        hidReport = AccessoryService.readReport(mContext, accessoryId, HIDReportType.FEATURE, result);
        assertEquals("Read report 2 result", RESULT_OK, result.getCode());
        assertEquals("HIDReport type after write", HIDReportType.FEATURE, hidReport.getType());
        assertEquals("Data after write", writeData, Base64.getEncoder().encodeToString(hidReport.getData()));
        return step;
    }

    private void validateSharedHidAccessoryInfo(AccessoryInfo ai) {
        assertEquals("Accessory class", AccessoryInfo.AccessoryClass.HID, ai.getAccessoryClass());
        assertEquals("Registration type", RegistrationType.SHARED, ai.getRegistrationType());

        HIDAccessoryInfo hidAccessoryInfo = ai.getDetails();
        assertEquals("Product Id", SHARED_PRODUCT_ID, hidAccessoryInfo.getProductId());
        assertEquals("Vendor Id", SHARED_VENDOR_ID, hidAccessoryInfo.getVendorId());
        assertEquals("Serial Number", null, hidAccessoryInfo.getSerialNumber());
        assertEquals("Description", "X885555", hidAccessoryInfo.getDescription());
        assertEquals("Manufacturer", "Api, Test.", hidAccessoryInfo.getManufacturer());
    }

    private void registerDynamicReceiversForContextChange(Context context, CountDownLatch latch,
                                                          EventCode expectedEventCode) {
        Log.i(TAG, "registerDynamicReceiversForContextChange");
        if (context != null) {
            IntentFilter filter = new IntentFilter(ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP);
            filter.addAction(ACCESSORY_STATUS_ACTION);
            mContextChangeEventReceiver = new ContextChangeEventReceiver(latch, expectedEventCode);
            context.registerReceiver(mContextChangeEventReceiver, filter);
        }
    }

    private void registerDynamicReceiversForReport(Context context, CountDownLatch latch,
                                                   String expectedBase64Data) {
        Log.i(TAG, "registerDynamicReceiversForReport");
        if (context != null) {
            IntentFilter filter = new IntentFilter(ACCESSORY_CHANGE_ACTION);
            filter.addAction(ACCESSORY_REPORT_ACTION);
            mReportReceiver = new ReportReceiver(latch, expectedBase64Data);
            context.registerReceiver(mReportReceiver, filter);
        }
    }

    private void unregisterDynamicReceiversForReport(Context context) {
        Log.i(TAG, "unregisterDynamicReceiversForReport");
        if (context != null && mReportReceiver != null) {
            context.unregisterReceiver(mReportReceiver);
            mReportReceiver = null;
        }
    }

    private void unregisterDynamicReceiversForContextChange(Context context) {
        Log.i(TAG, "unregisterDynamicReceiversForContextChange");
        if (context != null && mContextChangeEventReceiver != null) {
            context.unregisterReceiver(mContextChangeEventReceiver);
            mContextChangeEventReceiver = null;
        }
    }

    public static class ReportReceiver extends BroadcastReceiver {
        CountDownLatch mLatch;
        String mExpectedBase64Data;

        public ReportReceiver(CountDownLatch latch, String expectedBase64Data) {
            mLatch = latch;
            mExpectedBase64Data = expectedBase64Data;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACCESSORY_CHANGE_ACTION: {
                    HIDAccessoryInfo hidAccessoryInfo = intent.getParcelableExtra("accessoryInfo");
                    validateOwnedHidAccessoryInfo(hidAccessoryInfo);

                    HIDReportEventInfo hidReportEventInfo = intent.getParcelableExtra("hidReportEventInfo");
                    validateHidReport(hidReportEventInfo, mExpectedBase64Data);
                    mLatch.countDown();
                }
                break;
                case ACCESSORY_REPORT_ACTION: {
                    String timeStamp = intent.getStringExtra("ACCESSORY_INFO_TIMESTAMP");
                    int count = intent.getIntExtra("ACCESSORY_REPORT_INFO_COUNT", -1);
                    ArrayList<byte[]> arrayLists = (ArrayList<byte[]>) intent.getSerializableExtra(
                            "ACCESSORY_REPORT_INFO_BYTE_ARRAY_LIST");
                    String dataString = intent.getStringExtra("ACCESSORY_REPORT_INFO_DATA");
                    int vendorId = intent.getIntExtra("ACCESSORY_INFO_VENDOR_ID", -1);
                    int productId = intent.getIntExtra("ACCESSORY_INFO_PRODUCT_ID", -1);
                    String serialNumber = intent.getStringExtra("ACCESSORY_INFO_SERIAL_NUMBER");

                    assertEquals("ACCESSORY_REPORT_ACTION: vendorId", OWNED_VENDOR_ID, vendorId);
                    assertEquals("ACCESSORY_REPORT_ACTION: productId", OWNED_PRODUCT_ID, productId);
                    assertEquals("ACCESSORY_REPORT_ACTION: serialNumber", "NULL", serialNumber);
                    assertEquals("ACCESSORY_REPORT_ACTION: ACCESSORY_REPORT_INFO_BYTE_ARRAY_LIST", mExpectedBase64Data,
                            Base64.getEncoder().encodeToString(arrayLists.get(0)));
                    assertEquals("ACCESSORY_REPORT_ACTION: ACCESSORY_REPORT_INFO_DATA", mExpectedBase64Data,
                            Base64.getEncoder().encodeToString(dataString.getBytes()));
                    assertEquals("ACCESSORY_REPORT_ACTION: count", arrayLists.size(), count);
                    mLatch.countDown();
                }
                break;
                default:
                    break;
            }
        }
    }

    public static class ContextChangeEventReceiver extends BroadcastReceiver {
        CountDownLatch mLatch;
        EventCode mExpectedEventCode;
        String accessoryContextIdFromContextChangeAction;
        String accessoryContextIdFromStatusAction;

        public ContextChangeEventReceiver(CountDownLatch latch, EventCode expectedEventCode) {
            mLatch = latch;
            mExpectedEventCode = expectedEventCode;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: {
                    HIDAccessoryInfo hidAccessoryInfo = intent.getParcelableExtra("accessoryInfo");
                    int vendorId = hidAccessoryInfo.getVendorId();
                    int productId = hidAccessoryInfo.getProductId();
                    String serialNumber = hidAccessoryInfo.getSerialNumber();
                    RegistrationType registrationType = hidAccessoryInfo.getRegistrationType();
                    String timeStamp = intent.getStringExtra("timestamp");
                    EventCode eventCode = (EventCode) intent.getSerializableExtra("accessoryContextEventCode");
                    String accessoryContextId = intent.getStringExtra("accessoryContextId");
                    if (mExpectedEventCode == eventCode) {
                        assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP : OWNED_VENDOR_ID",
                                OWNED_VENDOR_ID, vendorId);
                        assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP : OWNED_PRODUCT_ID",
                                OWNED_PRODUCT_ID, productId);
                        assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP : serialNumber",
                                "NULL", serialNumber);
                        assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP : OWNED",
                                RegistrationType.OWNED, registrationType);
                        assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP : eventCode",
                                mExpectedEventCode, eventCode);
                        assertNotNull("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: timeStamp", timeStamp);
                        assertTrue("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: accessoryContextId",
                                isValidUUID(accessoryContextId));
                        accessoryContextIdFromContextChangeAction = accessoryContextId;
                        mLatch.countDown();
                    }
                }
                break;
                case ACCESSORY_STATUS_ACTION: {
                    int eventCode = intent.getIntExtra("ACCESSORY_INFO_EVENT_CODE", -1);
                    String accessoryContextId = intent.getStringExtra("ACCESSORY_INFO_CONTEXT_ID");
                    int vendorId = intent.getIntExtra("ACCESSORY_INFO_VENDOR_ID", -1);
                    int productId = intent.getIntExtra("ACCESSORY_INFO_PRODUCT_ID", -1);
                    String serialNumber = intent.getStringExtra("ACCESSORY_INFO_SERIAL_NUMBER");
                    int regType = intent.getIntExtra("ACCESSORY_INFO_REGISTRATION_TYPE", -1);
                    String timeStamp = intent.getStringExtra("ACCESSORY_INFO_TIMESTAMP");
                    if (mExpectedEventCode.ordinal() == eventCode) {
                        assertEquals("ACCESSORY_STATUS_ACTION: OWNED_VENDOR_ID", OWNED_VENDOR_ID, vendorId);
                        assertEquals("ACCESSORY_STATUS_ACTION: OWNED_PRODUCT_ID", OWNED_PRODUCT_ID, productId);
                        assertEquals("ACCESSORY_STATUS_ACTION: serialNumber", "NULL", serialNumber);
                        assertEquals("ACCESSORY_STATUS_ACTION : OWNED", RegistrationType.OWNED.ordinal(), regType);
                        assertEquals("ACCESSORY_STATUS_ACTION : eventCode", mExpectedEventCode.ordinal(), eventCode);
                        assertNotNull("ACCESSORY_STATUS_ACTION: timeStamp", timeStamp);
                        assertTrue("ACCESSORY_STATUS_ACTION: accessoryContextId", isValidUUID(accessoryContextId));
                        accessoryContextIdFromStatusAction = accessoryContextId;
                        mLatch.countDown();
                    }
                }
                break;

                default:
                    break;
            }
        }

        public String getAccessoryContextIdFromContextChangeAction() {
            return accessoryContextIdFromContextChangeAction;
        }

        public String getAccessoryContextIdFromStatusAction() {
            return accessoryContextIdFromStatusAction;
        }
    }
}
