package com.hp.workpath.apitest.copier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
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
import com.hp.workpath.api.copier.CopierService;
import com.hp.workpath.api.copier.CopyAttributes;
import com.hp.workpath.api.copier.CopyAttributesCaps;
import com.hp.workpath.api.copier.CopyAttributesReader;
import com.hp.workpath.api.copier.CopyletAttributes;
import com.hp.workpath.api.job.CopyJobData;
import com.hp.workpath.api.job.CopyJobState;
import com.hp.workpath.api.job.JobInfo;
import com.hp.workpath.api.job.JobService;
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
public class CopierServiceTest {
    private static final String TAG = "CopierServiceTest";
    private static Context mContext;
    private final DutController mDutController;
    private final CopyAttributesCaps mExpectedCopyAttributesCaps;
    private final CopyAttributesReader mExpectedCopyAttributesReader;

    // Constructor
    public CopierServiceTest() {
        this.mExpectedCopyAttributesCaps = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), CopyAttributesCaps.class, "copierService/CopyAttributesCaps_default.json");
        CopyAttributes mExpectedCopyAttributes = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), CopyAttributes.class, "copierService/CopyAttributes_default.json");
        this.mExpectedCopyAttributesReader = new CopyAttributesReader(mExpectedCopyAttributes);

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
     * Test API : CopierService.isSupported
     * Target : Simulator, Emulator
     */
    @Test
    public void CopierService_isSupported_ReturnsTrue() {
        boolean supported = CopierService.isSupported(mContext);
        assertTrue("supported", supported);
    }

    /**
     * Test API : CopierService.getCapabilities
     * Target : Simulator, Emulator
     */
    @Test
    public void CopierService_getCapabilities_ReturnsCopyAttributesCaps() {
        boolean supported = CopierService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        CopyAttributesCaps copyAttributesCapsCaps = CopierService.getCapabilities(mContext, result);

        assertNotNull("copyAttributesCapsCaps", copyAttributesCapsCaps);
        assertEquals("result.getCode", Result.RESULT_OK, result.getCode());
        assertEquals("getCopiesRange().getLowerBound", mExpectedCopyAttributesCaps.getCopiesRange().getLowerBound(),
                copyAttributesCapsCaps.getCopiesRange().getLowerBound());
        assertEquals("getCopiesRange().getUpperBound", mExpectedCopyAttributesCaps.getCopiesRange().getUpperBound(),
                copyAttributesCapsCaps.getCopiesRange().getUpperBound());
    }

    /**
     * Test API : CopierService.getDefaults
     * Target : Simulator, Emulator
     */
    @Test
    public void CopierService_getDefaults_ReturnsCopyAttributes() {
        boolean supported = CopierService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        CopyAttributes copyDefaultAttributes = CopierService.getDefaults(mContext, result);

        //validate the result default copy attributes
        assertNotNull("copyDefaultAttributes", copyDefaultAttributes);
        assertEquals("copyDefaultAttributes result code", result.getCode(), Result.RESULT_OK);

        CopyAttributesReader copyAttributesReader = new CopyAttributesReader(copyDefaultAttributes);
        assertNotNull("copyAttributesReader", copyAttributesReader);
        assertEquals("Copies", mExpectedCopyAttributesReader.getCopies(), copyAttributesReader.getCopies());
    }

    /**
     * Test API : CopierService.submit , JobService.AbstractJobletObserver.onProgress, JobService.AbstractJobletObserver.onComplete
     * Target : Simulator, Emulator
     */
    @FlakyTest //This is basic PI test but disabled temporary due to emulator 'Scanner Failure' after upgrading Dune FW.
    @Test
    public void CopierService_submit_ReturnsRid() throws InterruptedException {
        Result result = new Result();
        Log.i(TAG, "CopierService_submit_ReturnsRid : START");

        Log.i(TAG, "CopierService_submit_ReturnsRid : [STEP 1] launch the test app from the device home to acquire UIContext");
        mDutController.startApp(mContext);

        Log.i(TAG, "CopierService_submit_ReturnsRid : [STEP 2] Call CopierService.isSupported");
        boolean supported = CopierService.isSupported(mContext);
        assertTrue("supported", supported);

        Log.i(TAG, "CopierService_submit_ReturnsRid : [STEP 3] Call CopierService.getCapabilities");
        CopyAttributesCaps copyAttributesCapsCaps = CopierService.getCapabilities(mContext, result);
        assertNotNull("copyAttributesCapsCaps", copyAttributesCapsCaps);

        Log.i(TAG, "CopierService_submit_ReturnsRid : [STEP 4] Call CopierService.getDefaults");
        CopyAttributes copyDefaultAttributes = CopierService.getDefaults(mContext, result);
        CopyletAttributes copyletAttributes = new CopyletAttributes.Builder().setShowSettingsUi(false).build();

        CountDownLatch completionLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver jobObserver = new JobObserver(mainHandler, completionLatch, null);
        jobObserver.register(mContext);

        Log.i(TAG, "CopierService_submit_ReturnsRid : [STEP 5] Call CopierService.submit");
        String rid = CopierService.submit(mContext, copyDefaultAttributes, copyletAttributes);
        assertNotNull(rid);
        assertNotNull(UUID.fromString(rid));

        boolean isJobFinished = completionLatch.await(30, TimeUnit.SECONDS);
        assertTrue("isJobFinished", isJobFinished);
        assertEquals("rid", jobObserver.getRequestId(), rid);
        assertTrue("isProgressEventReceived", jobObserver.isProgressEventReceived());
        assertTrue("isCompleteEventReceived", jobObserver.isCompleteEventReceived());
        assertFalse("isFailEventReceived", jobObserver.isFailEventReceived());
        assertFalse("isCancelEventReceived", jobObserver.isCancelEventReceived());

        JobInfo jobInfo = jobObserver.getJobInfo();
        assertNotNull("jobInfo", jobInfo);
        assertEquals("JobType", JobInfo.JobType.COPY, jobInfo.getJobType());
        assertNotEquals("getCompleteTime", 0, jobInfo.getCompleteTime());
        assertNotNull("getJobId", jobInfo.getJobId());
        assertNotNull("getJobId UUID", UUID.fromString(jobInfo.getJobId()));

        CopyJobData copyJobData = jobInfo.getJobData();
        assertEquals("CopyJobState.State", CopyJobState.State.COMPLETED, copyJobData.getJobState().getState());
        //assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobData.getJobState().getScanningState());
        //assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobData.getJobState().getPrintingState());
        //assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobData.getJobState().getProcessingState());
        assertEquals("CopyJobState.ActivityState", CopyJobState.ActivityState.NOT_STARTED,
                copyJobData.getJobState().getCancelingState());

        assertEquals("CopyAttributes.JobExecutionMode", CopyAttributes.JobExecutionMode.NORMAL,
                copyJobData.getJobExecutionMode());
        //assertEquals(1, copyJobData.getImagesScanned());
        Log.i(TAG, "CopierService_submit_ReturnsRid : END");
    }

    /**
     * Test API : JobService.cancelJob, JobService.AbstractJobletObserver.onCancel
     * Target : Simulator
     * Note : Adding @FlakyTest  to not  run on PI. (Copy Job canceling is not working on Emulator)
     */
    @FlakyTest
    @Test
    public void JobService_cancelJob_ReturnsOK() throws InterruptedException, CapabilitiesExceededException {
        Result result = new Result();
        Log.i(TAG, "JobService_cancelJob_ReturnsOK : START");

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 1] launch the test app from the device home to acquire UIContext");
        mDutController.startApp(mContext);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 2] Call CopierService.isSupported");
        boolean supported = CopierService.isSupported(mContext);
        assertTrue("supported", supported);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 3] Call CopierService.getCapabilities");
        CopyAttributesCaps copyAttributesCapsCaps = CopierService.getCapabilities(mContext, result);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 4] Call CopierService.getDefaults");
        CopyAttributes copyDefaultAttributes = CopierService.getDefaults(mContext, result);
        CopyletAttributes copyletAttributes = new CopyletAttributes.Builder().setShowSettingsUi(false).build();

        CountDownLatch endLatch = new CountDownLatch(1);
        CountDownLatch progressLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver jobObserver = new JobObserver(mainHandler, endLatch, progressLatch);
        jobObserver.register(mContext);

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 5] Call CopierService.submit");
        CopyAttributes.CopyBuilder copyBuilder = new CopyAttributes.CopyBuilder().setCopies(10);
        CopyAttributes copyAttributes = copyBuilder.build(copyAttributesCapsCaps);

        String rid = CopierService.submit(mContext, copyDefaultAttributes, copyletAttributes);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        boolean onProgress = progressLatch.await(10, TimeUnit.SECONDS);
        assertTrue("UUID", onProgress);
        assertNotNull("getJobIdentifier", jobObserver.getJobIdentifier());

        Log.i(TAG, "JobService_cancelJob_ReturnsOK : [STEP 6] Call JobService.cancelJob");
        result = JobService.cancelJob(mContext, jobObserver.getJobIdentifier());
        assertEquals("result.getCode", result.getCode(), Result.RESULT_OK);

        boolean isJobFinished = endLatch.await(20, TimeUnit.SECONDS);
        assertTrue("isJobFinished", isJobFinished);
        assertEquals("rid after cancel", jobObserver.getRequestId(), rid);
        assertTrue("isProgressEventReceived", jobObserver.isProgressEventReceived());
        assertTrue("isCancelEventReceived", jobObserver.isCancelEventReceived());
        assertFalse("isCancelEventReceived", jobObserver.isCompleteEventReceived());
        assertFalse("isFailEventReceived", jobObserver.isFailEventReceived());

        JobInfo jobInfo = jobObserver.getJobInfo();
        CopyJobData copyJobData = jobInfo.getJobData();
        //assertEquals(CopyJobState.State.CANCELED, copyJobData.getJobState().getState());
        //assertEquals(CopyJobState.ActivityState.COMPLETED, copyJobData.getJobState().getCancelingState());
        assertEquals("CopyAttributes.JobExecutionMode", CopyAttributes.JobExecutionMode.NORMAL,
                copyJobData.getJobExecutionMode());
        Log.i(TAG, "JobService_cancelJob_ReturnsOK : END");
    }
}
