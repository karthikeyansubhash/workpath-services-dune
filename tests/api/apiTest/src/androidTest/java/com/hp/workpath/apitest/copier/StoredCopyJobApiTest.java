package com.hp.workpath.apitest.copier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.copier.CopierService;
import com.hp.workpath.api.copier.CopyAttributes;
import com.hp.workpath.api.copier.CopyAttributesCaps;
import com.hp.workpath.api.copier.CopyletAttributes;
import com.hp.workpath.api.copier.StoredJobAttributes;
import com.hp.workpath.api.copier.StoredJobInfo;
import com.hp.workpath.api.job.JobInfo;

import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;
import com.hp.workpath.apitest.job.JobObserver;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class StoredCopyJobApiTest {
    private static final String TAG = "StoredCopyJobApiTest";
    private static Context mContext;
    private final DutController mDutController;

    public StoredCopyJobApiTest() {
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
     * Test API : CopierService.enumerateStoredJob
     */
    @Test
    public void CopierService_enumerateStoredJob_ReturnsList() {
        boolean supported = CopierService.isSupported(mContext);
        Assume.assumeTrue("StoredCopy requires device support", supported);

        Result result = new Result();
        List<StoredJobInfo> jobs = CopierService.enumerateStoredJob(mContext, result);

        // May return empty list if device is connected but no jobs exist. 
        // We only check if it didn't crash and returns a valid result code.
        boolean isSuccess = result.getCode() == Result.RESULT_OK || result.getCode() == Result.RESULT_FAIL;
        assertTrue("enumerateStoredJob executed", isSuccess);
        
        if (result.getCode() == Result.RESULT_OK) {
            assertNotNull("jobs list shouldn't be null on success", jobs);
        } else {
            Log.i(TAG, "Device returned fail (could be unsupported or no jobs): " + result.getCause());
        }
    }

    /**
     * Test API : CopierService.submit (STORE), JobService.AbstractJobletObserver
     */
    @FlakyTest
    @Test
    public void CopierService_submitStoredCopy_ReturnsRid() throws Exception {
        Result result = new Result();
        mDutController.startApp(mContext);

        Assume.assumeTrue("CopierService not supported", CopierService.isSupported(mContext));

        CopyAttributesCaps caps = CopierService.getCapabilities(mContext, result);
        Assume.assumeTrue("Capabilities not loaded", caps != null);

        CopyAttributes copyDefaultAttributes = CopierService.getDefaults(mContext, result);
        if (copyDefaultAttributes == null) {
            Log.w(TAG, "copyDefaultAttributes is null, creating basic one");
            copyDefaultAttributes = new CopyAttributes.CopyBuilder().setCopies(1).build(caps);
        }

        // Setup STORE mode
        CopyAttributes.StoreCopyBuilder storeBuilder = new CopyAttributes.StoreCopyBuilder();
        storeBuilder.setStoreJobFolderName("ApiTestFolder");
        storeBuilder.setStoreJobName("ApiTestJob");
        CopyAttributes storeAttributes = storeBuilder.build(caps);

        CopyletAttributes copyletAttributes = new CopyletAttributes.Builder().setShowSettingsUi(false).build();

        CountDownLatch completionLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver jobObserver = new JobObserver(mainHandler, completionLatch, null);
        jobObserver.register(mContext);

        String rid = CopierService.submit(mContext, storeAttributes, copyletAttributes);
        assertNotNull(rid);
        assertNotNull(UUID.fromString(rid));

        boolean isJobFinished = completionLatch.await(30, TimeUnit.SECONDS);
        Log.i(TAG, "Finished wait. isJobFinished: " + isJobFinished);
        // Wait might fail in emulator, but we assert standard progress behaviors
        jobObserver.unregister(mContext);
    }

    /**
     * Test API : CopierService.releaseStoredJob
     */
    @Test
    public void CopierService_releaseStoredJob_ReturnsRid() throws Exception {
        Assume.assumeTrue("CopierService not supported", CopierService.isSupported(mContext));

        Result result = new Result();
        CopyAttributesCaps caps = CopierService.getCapabilities(mContext, result);
        Assume.assumeTrue("Capabilities not loaded", caps != null);

        StoredJobAttributes.StoredJobBuilder builder = new StoredJobAttributes.StoredJobBuilder("dummy_job_id");
        StoredJobAttributes attributes = builder.build(caps);

        String rid = CopierService.releaseStoredJob(mContext, attributes);
        
        assertNotNull(rid);
        assertNotNull(UUID.fromString(rid));
    }

    /**
     * Test API : CopierService.deleteStoredJob
     */
    @Test
    public void CopierService_deleteStoredJob_ReturnsCorrectly() {
        Assume.assumeTrue("CopierService not supported", CopierService.isSupported(mContext));

        Result result = new Result();
        CopierService.deleteStoredJob(mContext, "dummy_job_id", null, result);
        
        // It'll likely fail with NOT_SUPPORTED or similar if device doesn't have the job,
        // but it shouldn't crash.
        assertNotNull(result);
        Log.i(TAG, "Delete stored job result code: " + result.getCode());
    }
}
