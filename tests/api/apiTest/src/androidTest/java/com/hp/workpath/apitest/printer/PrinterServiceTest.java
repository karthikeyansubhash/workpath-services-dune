package com.hp.workpath.apitest.printer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;

import com.hp.workpath.api.CapabilitiesExceededException;
import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.job.JobInfo;
import com.hp.workpath.api.job.JobService;
import com.hp.workpath.api.job.PrintJobData;
import com.hp.workpath.api.job.PrintJobState;
import com.hp.workpath.api.printer.PrintAttributes;
import com.hp.workpath.api.printer.PrintAttributesCaps;
import com.hp.workpath.api.printer.PrintAttributesReader;
import com.hp.workpath.api.printer.PrinterService;
import com.hp.workpath.api.printer.PrintletAttributes;
import com.hp.workpath.apitest.TestInfra;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;
import com.hp.workpath.apitest.job.JobObserver;
import com.hp.workpath.apitest.util.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class PrinterServiceTest {
    private static final String TAG = "PrinterServiceTest";
    private static Context mContext;
    private final DutController mDutController;
    private final PrintAttributesCaps mExpectedPrintAttributesCaps;
    private final PrintAttributesReader mExpectedPrintAttributesReader;

    // Constructor
    public PrinterServiceTest() {
        this.mExpectedPrintAttributesCaps = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), PrintAttributesCaps.class, "printerService/PrintAttributesCaps_default.json");
        PrintAttributes mExpectedPrintAttributes = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), PrintAttributes.class, "printerService/PrintAttributes_default.json");
        this.mExpectedPrintAttributesReader = new PrintAttributesReader(mExpectedPrintAttributes);

        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void SetUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            fail("Workpath.getInstance().initialize : SsdkUnsupportedException");
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        mDutController.stopApp(mContext);
    }

    /**
     * Test API : PrinterService.isSupported
     * Target : Simulator, Emulator
     */
    @Test
    public void PrinterService_isSupported_ReturnsTrue() {
        boolean supported = PrinterService.isSupported(mContext);
        assertTrue("supported", supported);
    }

    /**
     * Test API : PrinterService.getCapabilities
     * Target : Simulator, Emulator
     */
    @Test
    public void PrinterService_getCapabilities_ReturnsPrintAttributesCaps() {
        boolean supported = PrinterService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        PrintAttributesCaps printAttributesCaps = PrinterService.getCapabilities(mContext, result);

        assertNotNull("printAttributesCaps", printAttributesCaps);
        assertEquals("result.getCode", Result.RESULT_OK, result.getCode());
        assertEquals("getMaxCopies", mExpectedPrintAttributesCaps.getMaxCopies(),
                printAttributesCaps.getMaxCopies());
        assertFalse("getColorModeList not empty", printAttributesCaps.getColorModeList().isEmpty());
    }

    /**
     * Test API : PrinterService.getDefaults
     * Target : Simulator, Emulator
     */
    @Test
    public void PrinterService_getDefaults_ReturnsPrintAttributes() {
        boolean supported = PrinterService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        PrintAttributes printDefaultAttributes = PrinterService.getDefaults(mContext, result);

        assertNotNull("printDefaultAttributes", printDefaultAttributes);
        assertEquals("printDefaultAttributes result code", result.getCode(), Result.RESULT_OK);

        PrintAttributesReader printAttributesReader = new PrintAttributesReader(printDefaultAttributes);
        assertNotNull("printAttributesReader", printAttributesReader);
        assertEquals("Copies", mExpectedPrintAttributesReader.getCopies(), printAttributesReader.getCopies());
        assertEquals("ColorMode", mExpectedPrintAttributesReader.getColorMode(), printAttributesReader.getColorMode());
    }

    /**
     * Test API : PrinterService.submit , JobService.AbstractJobletObserver.onProgress, JobService.AbstractJobletObserver.onComplete
     * Target : Simulator, Emulator
     */
    @FlakyTest
    @Test
    public void PrinterService_submit_ReturnsRid() throws InterruptedException, CapabilitiesExceededException {
        Result result = new Result();
        Log.i(TAG, "PrinterService_submit_ReturnsRid : START");

        Log.i(TAG, "PrinterService_submit_ReturnsRid : [STEP 1] launch the test app from the device home to acquire UIContext");
        mDutController.startApp(mContext);

        Log.i(TAG, "PrinterService_submit_ReturnsRid : [STEP 2] Call PrinterService.isSupported");
        boolean supported = PrinterService.isSupported(mContext);
        assertTrue("supported", supported);

        Log.i(TAG, "PrinterService_submit_ReturnsRid : [STEP 3] Call PrinterService.getCapabilities");
        PrintAttributesCaps printAttributesCaps = PrinterService.getCapabilities(mContext, result);
        assertNotNull("printAttributesCaps", printAttributesCaps);

        Log.i(TAG, "PrinterService_submit_ReturnsRid : [STEP 4] Build PrintAttributes with HTTP source");
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT + "/" + TestInfra.HTTP_SERVER_PATH + "?name=" + TestInfra.HTTP_SERVER_FILE_NAME);
        PrintAttributes printAttributes = new PrintAttributes.PrintFromHttpBuilder(httpUri)
                .setColorMode(PrintAttributes.ColorMode.DEFAULT)
                .setDuplex(PrintAttributes.Duplex.DEFAULT)
                .setCopies(1)
                .build(printAttributesCaps);
        PrintletAttributes printletAttributes = new PrintletAttributes.Builder().setShowSettingsUi(false).build();

        CountDownLatch completionLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver jobObserver = new JobObserver(mainHandler, completionLatch, null);
        jobObserver.register(mContext);

        Log.i(TAG, "PrinterService_submit_ReturnsRid : [STEP 5] Call PrinterService.submit");
        String rid = PrinterService.submit(mContext, printAttributes, printletAttributes);
        assertNotNull(rid);
        assertNotNull(UUID.fromString(rid));

        boolean isJobFinished = completionLatch.await(60, TimeUnit.SECONDS);
        assertTrue("isJobFinished", isJobFinished);
        assertEquals("rid", jobObserver.getRequestId(), rid);
        assertTrue("isProgressEventReceived", jobObserver.isProgressEventReceived());
        assertTrue("isCompleteEventReceived", jobObserver.isCompleteEventReceived());
        assertFalse("isFailEventReceived", jobObserver.isFailEventReceived());
        assertFalse("isCancelEventReceived", jobObserver.isCancelEventReceived());

        JobInfo jobInfo = jobObserver.getJobInfo();
        assertNotNull("jobInfo", jobInfo);
        assertEquals("JobType", JobInfo.JobType.PRINT, jobInfo.getJobType());
        assertNotEquals("getCompleteTime", 0, jobInfo.getCompleteTime());
        assertNotNull("getJobId", jobInfo.getJobId());
        assertNotNull("getJobId UUID", UUID.fromString(jobInfo.getJobId()));

        PrintJobData printJobData = jobInfo.getJobData();
        assertEquals("PrintJobState.State", PrintJobState.State.COMPLETED, printJobData.getJobState().getState());
        assertEquals("Source", PrintAttributes.Source.HTTP, printJobData.getSource());
        Log.i(TAG, "PrinterService_submit_ReturnsRid : END");
    }

    /**
     * Test API : JobService.cancelJob, JobService.AbstractJobletObserver.onCancel
     * Target : Simulator
     * Note : Adding @FlakyTest to not run on PI.
     */
    @FlakyTest
    @Test
    public void JobService_cancelJob_ReturnsOK() throws InterruptedException, CapabilitiesExceededException {
        Result result = new Result();
        Log.i(TAG, "JobService_cancelJob_ReturnsOK : START");

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 1] launch the test app from the device home to acquire UIContext");
        mDutController.startApp(mContext);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 2] Call PrinterService.isSupported");
        boolean supported = PrinterService.isSupported(mContext);
        assertTrue("supported", supported);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 3] Call PrinterService.getCapabilities");
        PrintAttributesCaps printAttributesCaps = PrinterService.getCapabilities(mContext, result);
        assertNotNull("printAttributesCaps", printAttributesCaps);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 4] Build PrintAttributes with HTTP source (10 copies)");
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT + "/" + TestInfra.HTTP_SERVER_PATH + "?name=" + TestInfra.HTTP_SERVER_FILE_NAME);
        PrintAttributes printAttributes = new PrintAttributes.PrintFromHttpBuilder(httpUri)
                .setColorMode(PrintAttributes.ColorMode.DEFAULT)
                .setDuplex(PrintAttributes.Duplex.DEFAULT)
                .setCopies(10)
                .build(printAttributesCaps);
        PrintletAttributes printletAttributes = new PrintletAttributes.Builder().setShowSettingsUi(false).build();

        CountDownLatch endLatch = new CountDownLatch(1);
        CountDownLatch progressLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver jobObserver = new JobObserver(mainHandler, endLatch, progressLatch);
        jobObserver.register(mContext);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 5] Call PrinterService.submit");
        String rid = PrinterService.submit(mContext, printAttributes, printletAttributes);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        boolean onProgress = progressLatch.await(30, TimeUnit.SECONDS);
        assertTrue("onProgress", onProgress);
        assertNotNull("getJobIdentifier", jobObserver.getJobIdentifier());

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 6] Call JobService.cancelJob");
        result = JobService.cancelJob(mContext, jobObserver.getJobIdentifier());
        assertEquals("result.getCode", result.getCode(), Result.RESULT_OK);

        boolean isJobFinished = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue("isJobFinished", isJobFinished);
        assertEquals("rid after cancel", jobObserver.getRequestId(), rid);
        assertTrue("isProgressEventReceived", jobObserver.isProgressEventReceived());
        assertTrue("isCancelEventReceived", jobObserver.isCancelEventReceived());
        assertFalse("isCompleteEventReceived", jobObserver.isCompleteEventReceived());
        assertFalse("isFailEventReceived", jobObserver.isFailEventReceived());
        Log.i(TAG, "JobService_cancelJob_ReturnsOK : END");
    }
}
