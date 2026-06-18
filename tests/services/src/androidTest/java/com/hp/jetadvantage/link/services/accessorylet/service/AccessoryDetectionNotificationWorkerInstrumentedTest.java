package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.ACCESSORY_RESOURCE_ID;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.APP_PACKAGE_NAME;
import static com.hp.jetadvantage.link.services.accessorylet.service.AccessoryDetectionNotificationWorker.ACCESSORY_EVENT_CODE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.UsbString;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.jetadvantage.link.api.accessory.hid.Accessorylet;
import com.hp.jetadvantage.link.api.accessory.hid.EventCode;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;
import com.hp.jetadvantage.link.common.helper.WorkManagerHelper;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessoryService;
import com.hp.jetadvantage.link.services.accessorylet.adapter.AccessoryDeviceAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AccessoryDetectionNotificationWorkerInstrumentedTest {
    final static int VALID_VID = 1008;
    final static int VALID_PID = 69;
    final static String VALID_SERIAL_NUMBER = "123456";
    final static String VALID_MANUFACTURER_NAME = "HP";
    final static String VALID_PRODUCT_NAME = "X6001";
    private static final String VALID_APP_PACKAGE_NAME = "com.hp.test.accessory.app";
    private static final String VALID_ACCESSORY_ID = "d3b07384-d9a0-4f3d-bb5f-2a5b2f7d5a3f";
    private static final String UNKNOWN_ACCESSORY_ID = "3f9a77b4-2c48-4b2a-b9fc-6d59e3b8a7d1";

    @Mock
    IDeviceAccessoryService mockAccessService;

    ItAppAccessoryContextChangeReceiver mReceiver;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this); // Mock 초기화
    }

    /**
     * Given AccessoryDetectionNotificationWorker
     * When Enqueue an OneTimeWorkRequest for the given worker to notify that an accessory is attached/detached
     * Then DoWork : notify the context change to the app
     *
     * @throws InterruptedException
     */
    @Test
    public void GivenAccessoryDetectionNotificationWorker_WhenEnqueueNotificationWorker_ThenDoWork() throws InterruptedException {
        boolean attached = true;
        CountDownLatch latch = new CountDownLatch(3);
        Accessory accessory = getSampleAccessory();
        Context context = ApplicationProvider.getApplicationContext();
        String packageName = context.getPackageName();

        when(mockAccessService.getAccessory(packageName, VALID_ACCESSORY_ID)).thenReturn(accessory);
        AccessoryDetectionNotificationWorkerTest.setDeviceAccessService(mockAccessService);
        registerDynamicReceivers(context, latch, attached);


        //enqueue work for an attached accessory
        try {
            WorkManager workManager = WorkManagerHelper.getWorkManager(context);
            OneTimeWorkRequest notificationRequest = createOneTimeRequest(packageName, attached, VALID_ACCESSORY_ID);

            String workName = "TEST" + "/" + packageName + "/" + VALID_ACCESSORY_ID;
            workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, notificationRequest);
        } catch (Exception e) {
            fail("Fail to enqueueUniqueWork, " + e.getMessage());
        }

        //verify the worker's notification
        boolean notificationResult = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Attach notification", notificationResult);

        //enqueue work for an detached accessory
        attached = false;
        try {
            WorkManager workManager = WorkManagerHelper.getWorkManager(ApplicationProvider.getApplicationContext());
            OneTimeWorkRequest notificationRequest = createOneTimeRequest(packageName, attached, VALID_ACCESSORY_ID);

            String workName = "TEST" + "/" + packageName + "/" + VALID_ACCESSORY_ID;
            workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, notificationRequest);
        } catch (Exception e) {
            fail("Fail to enqueueUniqueWork, " + e.getMessage());
        }

        //verify the worker's notification
        notificationResult = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Detach notification", notificationResult);

        unregisterDynamicReceivers(context);
    }

    /**
     * Given AccessoryDetectionNotificationWorker
     * When Enqueue an OneTimeWorkRequest for the given worker, but the accessory is not found from the E2 api
     * Then DoWork : Don't notify the context change to the app
     *
     * @throws InterruptedException
     */
    @Test
    public void GivenAccessoryDetectionNotificationWorker_WhenEnqueueWorkerWithNullAccessoryInfo_ThenSkipToNotify() throws InterruptedException {
        boolean attached = true;
        CountDownLatch notificationLatch = new CountDownLatch(3);
        Context context = ApplicationProvider.getApplicationContext();
        String packageName = context.getPackageName();

        // null accessory info from the device service
        when(mockAccessService.getAccessory(packageName, VALID_ACCESSORY_ID)).thenReturn(null);
        AccessoryDetectionNotificationWorkerTest.setDeviceAccessService(mockAccessService);
        registerDynamicReceivers(context, notificationLatch, attached);

        try {
            WorkManager workManager = WorkManagerHelper.getWorkManager(ApplicationProvider.getApplicationContext());
            OneTimeWorkRequest notificationRequest = createOneTimeRequest(packageName, attached, VALID_ACCESSORY_ID);

            String workName = "TEST" + "/" + packageName + "/" + VALID_ACCESSORY_ID;
            workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, notificationRequest);

            //wait to complete the worker

            while (true) {
                WorkInfo workInfo = WorkManager.getInstance(context).getWorkInfoById(notificationRequest.getId()).get();
                if (workInfo != null && workInfo.getState().isFinished()) {
                    // worker completed
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            fail("Fail to enqueueUniqueWork, " + e.getMessage());
        }

        //verify the worker's notification : no notification is expected
        assertEquals("shouldn't notify any event", 3, notificationLatch.getCount());
        unregisterDynamicReceivers(context);
    }

    /**
     * Given AccessoryDetectionNotificationWorker
     * When Enqueue an OneTimeWorkRequest for the given worker, but the detached accessory is not found in cache
     * Then DoWork : Don't notify the context change to the app
     *
     * @throws InterruptedException
     */
    @Test
    public void GivenAccessoryDetectionNotificationWorker_WhenEnqueueWorkerWithEmptyDetachAccessoryInfo_ThenSkipToNotify() throws InterruptedException {
        boolean attached = false;
        CountDownLatch notificationLatch = new CountDownLatch(3);
        Context context = ApplicationProvider.getApplicationContext();
        String packageName = context.getPackageName();

        // null accessory info from the device service
        when(mockAccessService.getAccessory(packageName, UNKNOWN_ACCESSORY_ID)).thenReturn(null);
        AccessoryCache.getInstance().remove(UNKNOWN_ACCESSORY_ID);
        AccessoryDetectionNotificationWorkerTest.setDeviceAccessService(mockAccessService);
        registerDynamicReceivers(context, notificationLatch, attached);

        try {
            WorkManager workManager = WorkManagerHelper.getWorkManager(ApplicationProvider.getApplicationContext());
            OneTimeWorkRequest notificationRequest = createOneTimeRequest(packageName, attached, UNKNOWN_ACCESSORY_ID);

            String workName = "TEST" + "/" + packageName + "/" + UNKNOWN_ACCESSORY_ID;
            workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, notificationRequest);

            //wait to complete the worker

            while (true) {
                WorkInfo workInfo = WorkManager.getInstance(context).getWorkInfoById(notificationRequest.getId()).get();
                if (workInfo != null && workInfo.getState().isFinished()) {
                    // worker completed
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            fail("Fail to enqueueUniqueWork, " + e.getMessage());
        }

        //verify the worker's notification : no notification is expected
        assertEquals("shouldn't notify any event", 3, notificationLatch.getCount());
        unregisterDynamicReceivers(context);
    }

    private Accessory getSampleAccessory() {
        Accessory accessory = new Accessory();
        accessory.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory.setVendorId(new Unsigned16(VALID_VID));
        accessory.setProductId(new Unsigned16(VALID_PID));
        accessory.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory.setProductName(new UsbString(VALID_PRODUCT_NAME));
        accessory.setManufacturerName(new UsbString(VALID_MANUFACTURER_NAME));
        return accessory;
    }

    private OneTimeWorkRequest createOneTimeRequest(String packageName, boolean attached, String accessoryId) {
        Data inputData = new Data.Builder()
                .putString(APP_PACKAGE_NAME, packageName)
                .putString(ACCESSORY_RESOURCE_ID, accessoryId)
                .putString(ACCESSORY_EVENT_CODE, attached? EventCode.CONTEXT_CREATED.name() :
                        EventCode.CONTEXT_REVOKED.name())
                .build();

        OneTimeWorkRequest notificationRequest =
                new OneTimeWorkRequest.Builder(AccessoryDetectionNotificationWorkerTest.class)
                        .setInputData(inputData)
                        //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setInitialDelay(0, TimeUnit.MILLISECONDS)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .build();
        return notificationRequest;
    }

    /**
     * Register context change dynamic receivers for testing
     */
    private void registerDynamicReceivers(Context context, CountDownLatch latch, boolean attached) {
        if (context != null) {
            IntentFilter filter = new IntentFilter(Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION);
            filter.addAction(Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP);
            filter.addAction(Accessorylet.ACCESSORY_STATUS_ACTION);
            mReceiver = new ItAppAccessoryContextChangeReceiver(latch, attached);
            context.registerReceiver(mReceiver, filter);
        }
    }

    private void unregisterDynamicReceivers(Context context) {
        if (context != null && mReceiver!= null) {
            context.unregisterReceiver(mReceiver);
            mReceiver= null;
        }
    }

    /**
     * Receiver for accessory context changes
     */
    public static class ItAppAccessoryContextChangeReceiver extends BroadcastReceiver {
        private CountDownLatch mLatch;
        private boolean mAttached;

        ItAppAccessoryContextChangeReceiver(CountDownLatch latch, boolean attached) {
            this.mLatch = latch;
            this.mAttached = attached;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION: {
                    verifyIntentForLib(intent, mAttached);
                    mLatch.countDown();
                    break;
                }
                case Accessorylet.ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: {
                    verifyIntentForApp(intent, mAttached);
                    mLatch.countDown();
                    break;
                }
                case Accessorylet.ACCESSORY_STATUS_ACTION:
                    verifyIntentForLegacy(intent, mAttached);
                    mLatch.countDown();
                    break;
            }
        }

        /**
         * Intent sent to SDK library uses com.hp.jetadvantage.link.api.accessory namespace
         *
         * @param intent
         * @param mAttached
         */
        private void verifyIntentForLib(Intent intent, boolean mAttached) {
            com.hp.jetadvantage.link.api.accessory.hid.EventCode expectedEventCode = mAttached ?
                    com.hp.jetadvantage.link.api.accessory.hid.EventCode.CONTEXT_CREATED :
                    com.hp.jetadvantage.link.api.accessory.hid.EventCode.CONTEXT_REVOKED;

            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION: ACCESSORY_CONTEXT_ID ",
                    VALID_ACCESSORY_ID, intent.getStringExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID));

            com.hp.jetadvantage.link.api.accessory.hid.EventCode receivedEventCode =
                    (com.hp.jetadvantage.link.api.accessory.hid.EventCode) intent.getSerializableExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_EVENT_CODE);
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION: KEY_ACCESSORY_CONTEXT_EVENT_CODE ",
                    expectedEventCode, receivedEventCode);

            com.hp.jetadvantage.link.api.accessory.AccessoryInfo accessoryInfo =
                    intent.getParcelableExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO);
            com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo hidAccessoryInfo = accessoryInfo.getDetails();

            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION: KEY_ACCESSORY_INFO, ProductId ", VALID_PID,
                    hidAccessoryInfo.getProductId());
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION: KEY_ACCESSORY_INFO, VendorId ", VALID_VID,
                    hidAccessoryInfo.getVendorId());
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION: KEY_ACCESSORY_INFO, SerialNumber ", VALID_SERIAL_NUMBER,
                    hidAccessoryInfo.getSerialNumber());
        }

        /**
         * Intent sent directly to the app uses the com.hp.workpath.api.accessory namespace.
         */
        private void verifyIntentForApp(Intent intent, boolean mAttached) {
            com.hp.workpath.api.accessory.hid.EventCode expectedEventCode = mAttached ?
                    com.hp.workpath.api.accessory.hid.EventCode.CONTEXT_CREATED :
                    com.hp.workpath.api.accessory.hid.EventCode.CONTEXT_REVOKED;

            assertNotNull("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: KEY_TIMESTAMP ",
                    intent.getStringExtra(Accessorylet.Keys.KEY_TIMESTAMP));
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: ACCESSORY_CONTEXT_ID ",
                    VALID_ACCESSORY_ID, intent.getStringExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_ID));

            com.hp.workpath.api.accessory.hid.EventCode receivedEventCode =
                    (com.hp.workpath.api.accessory.hid.EventCode) intent.getSerializableExtra(Accessorylet.Keys.KEY_ACCESSORY_CONTEXT_EVENT_CODE);
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: KEY_ACCESSORY_CONTEXT_EVENT_CODE ",
                    expectedEventCode, receivedEventCode);

            com.hp.workpath.api.accessory.hid.HIDAccessoryInfo hidAccessoryInfo =
                    intent.getParcelableExtra(Accessorylet.Keys.KEY_ACCESSORY_INFO);

            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: KEY_ACCESSORY_INFO, ProductId ", VALID_PID,
                    hidAccessoryInfo.getProductId());
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: KEY_ACCESSORY_INFO, VendorId ", VALID_VID,
                    hidAccessoryInfo.getVendorId());
            assertEquals("ACCESSORY_CONTEXT_CHANGE_ACTION_FOR_APP: KEY_ACCESSORY_INFO, SerialNumber ",
                    VALID_SERIAL_NUMBER,
                    hidAccessoryInfo.getSerialNumber());
        }

        private void verifyIntentForLegacy(Intent intent, boolean mAttached) {
            com.hp.jetadvantage.link.api.accessory.hid.EventCode expectedEventCode = mAttached ?
                    com.hp.jetadvantage.link.api.accessory.hid.EventCode.CONTEXT_CREATED :
                    com.hp.jetadvantage.link.api.accessory.hid.EventCode.CONTEXT_REVOKED;

            assertEquals("ACCESSORY_STATUS_ACTION: ACCESSORY_INFO_CONTEXT_ID ",
                    VALID_ACCESSORY_ID, intent.getStringExtra(Accessorylet.Keys.ACCESSORY_INFO_CONTEXT_ID));
            assertEquals("ACCESSORY_STATUS_ACTION: VENDER_ID ",
                    VALID_VID, intent.getIntExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDER_ID, 0));
            assertEquals("ACCESSORY_STATUS_ACTION: VENDOR_ID ",
                    VALID_VID, intent.getIntExtra(Accessorylet.Keys.ACCESSORY_INFO_VENDOR_ID, 0));
            assertEquals("ACCESSORY_STATUS_ACTION: PRODUCT_ID ",
                    VALID_PID, intent.getIntExtra(Accessorylet.Keys.ACCESSORY_INFO_PRODUCT_ID, 0));
            assertEquals("ACCESSORY_STATUS_ACTION: ACCESSORY_INFO_SERIAL_NUMBER ",
                    VALID_SERIAL_NUMBER, intent.getStringExtra(Accessorylet.Keys.ACCESSORY_INFO_SERIAL_NUMBER));
            assertEquals("ACCESSORY_STATUS_ACTION: PRODUCT_ID ",
                    expectedEventCode.ordinal(), intent.getIntExtra(Accessorylet.Keys.ACCESSORY_INFO_EVENT_CODE, 0));
        }
    }

    /**
     * AccessoryDetectionNotificationWorkerTest is used for testing AccessoryDetectionNotificationWorker.
     * This subclass overrides the getHidAccessoryInfoFromDevice method to mock IDeviceAccessoryService,
     * isolating the test from the actual device service implementation and controlling the
     * IDeviceAccessoryService behavior in the test.
     */
    public static class AccessoryDetectionNotificationWorkerTest extends AccessoryDetectionNotificationWorker {
        static IDeviceAccessoryService deviceAccessService;

        public AccessoryDetectionNotificationWorkerTest(@NonNull Context context,
                                                        @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        public static void setDeviceAccessService(IDeviceAccessoryService mockDeviceAccessService) {
            deviceAccessService = mockDeviceAccessService;
        }

        @Override
        protected HIDAccessoryInfo getHidAccessoryInfoFromDevice(String packageName, String resourceId) {
            return AccessoryDeviceAdapter.getHidAccessoryInfo(deviceAccessService, packageName,
                    resourceId);
        }
    }
}
