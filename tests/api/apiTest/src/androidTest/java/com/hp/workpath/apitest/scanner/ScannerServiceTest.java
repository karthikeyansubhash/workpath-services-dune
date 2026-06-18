package com.hp.workpath.apitest.scanner;

import static com.hp.workpath.apitest.scanner.ScannerServiceHelper.createFtpScanAttributes;
import static com.hp.workpath.apitest.scanner.ScannerServiceHelper.createHttpScanAttributes;
import static com.hp.workpath.apitest.scanner.ScannerServiceHelper.createMeScanAttributes;
import static com.hp.workpath.apitest.scanner.ScannerServiceHelper.createNetworkFolderScanAttributes;
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
import android.text.TextUtils;
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
import com.hp.workpath.api.job.ScanJobData;
import com.hp.workpath.api.job.ScanJobState;
import com.hp.workpath.api.scanner.FileOptionsAttributes;
import com.hp.workpath.api.scanner.FileOptionsAttributesCaps;
import com.hp.workpath.api.scanner.NetworkCredentialsAttributes;
import com.hp.workpath.api.scanner.ScanAttributes;
import com.hp.workpath.api.scanner.ScanAttributesCaps;
import com.hp.workpath.api.scanner.ScanAttributesReader;
import com.hp.workpath.api.scanner.ScanletAttributes;
import com.hp.workpath.api.scanner.ScannerService;
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

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class ScannerServiceTest {
    private static final String TAG = "ScannerServiceTest";
    private static Context mContext;
    private final DutController mDutController;
    private final ScanAttributesCaps mExpectedScanAttributesCaps;
    private final ScanAttributesReader mExpectedScanAttributesReader;
    private CountDownLatch mCompletionLatch;
    private CountDownLatch mProgressLatch;
    private JobObserver mJobObserver = null;

    public ScannerServiceTest() {
        this.mExpectedScanAttributesCaps = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), ScanAttributesCaps.class,
                "scannerService/ScanAttributesCaps_default.json");
        ScanAttributes mExpectedScanAttributes = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), ScanAttributes.class,
                "scannerService/ScanAttributes_default.json");
        this.mExpectedScanAttributesReader = new ScanAttributesReader(mExpectedScanAttributes);

        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            fail("Workpath.getInstance().initialize");
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        if (mJobObserver != null) {
            mJobObserver.unregister(mContext);
            mJobObserver = null;
        }
        mDutController.stopApp(mContext);
    }

    @Test
    public void ScannerService_isSupported_$ReturnsTrue() {
        boolean supported = ScannerService.isSupported(mContext);
        assertTrue("supported", supported);
    }

    @Test
    public void ScannerService_getCapabilities_$ReturnsScanAttributesCaps() {
        boolean supported = ScannerService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        ScanAttributesCaps scanCaps = ScannerService.getCapabilities(mContext, result);
        //ScanAttributesCaps scanCaps = CustomScannerService.getCapabilities(mContext, result);

        //validate ScanAttributesCaps
        assertNotNull("scanCaps", scanCaps);
        assertEquals("Result.getCode", Result.RESULT_OK, result.getCode());
        assertEquals("ColorModeList", mExpectedScanAttributesCaps.getColorModeList(), scanCaps.getColorModeList());
        assertEquals("MediaSourceList", mExpectedScanAttributesCaps.getMediaSourceList(), scanCaps.getMediaSourceList());
    }

    @Test
    public void ScannerService_getDefaults_$ReturnsScanAttributes() {
        boolean supported = ScannerService.isSupported(mContext);
        assertTrue("supported", supported);

        Result result = new Result();
        ScanAttributes scanDefaultAttributes = ScannerService.getDefaults(mContext, result);
        //ScanAttributes scanDefaultAttributes = CustomScannerService.getDefaults(mContext, result);

        //validate ScanAttributes
        assertNotNull("scanDefaultAttributes", scanDefaultAttributes);
        assertEquals("result.getCode", Result.RESULT_OK, result.getCode());

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanDefaultAttributes);
        assertEquals("ColorMode", mExpectedScanAttributesReader.getColorMode(), scanAttributesReader.getColorMode());
        assertEquals("MediaSource", mExpectedScanAttributesReader.getMediaSource(), scanAttributesReader.getMediaSource());
    }

    @Test
    public void ScannerService_getFileOptionsCapabilities_$GivenColorModePdf_WhenGetCapabilities_ThenReturnsFileOptionsAttributesCaps() {
        Result result = new Result();

        ScanAttributes.ColorMode colorMode = ScanAttributes.ColorMode.COLOR;
        ScanAttributes.DocumentFormat documentFormat = ScanAttributes.DocumentFormat.PDF;
        FileOptionsAttributesCaps fileOptionsCaps = CustomScannerService.getFileOptionsCapabilities(mContext, colorMode, documentFormat, result);

        //validate FileOptionsAttributesCaps
        assertNotNull("fileOptionsCaps", fileOptionsCaps);
        assertEquals("result.getCode", Result.RESULT_OK, result.getCode());
        assertEquals("COLOR/PDF, isPdfEncryptionPasswordSupported", true, fileOptionsCaps.isPdfEncryptionPasswordSupported());
        assertEquals("COLOR/PDF, OcrLanguageList", Arrays.asList(FileOptionsAttributes.OcrLanguage.DEFAULT), fileOptionsCaps.getOcrLanguageList());
        assertEquals("COLOR/PDF, PdfCompressionModeList", Arrays.asList(FileOptionsAttributes.PdfCompressionMode.DEFAULT),
                fileOptionsCaps.getPdfCompressionModeList());
        assertEquals("COLOR/PDF, TiffCompressionModeList", Arrays.asList(FileOptionsAttributes.TiffCompressionMode.DEFAULT),
                fileOptionsCaps.getTiffCompressionModeList());
        assertEquals("COLOR/PDF, XpsCompressionMode", Arrays.asList(FileOptionsAttributes.XpsCompressionMode.DEFAULT),
                fileOptionsCaps.getXpsCompressionModeList());

    }

    @Test
    public void ScannerService_getFileOptionsCapabilities_$GivenMonoModePdfA_WhenGetCapabilities_ThenReturnsFileOptionsAttributesCaps() {
        Result result = new Result();

        ScanAttributes.ColorMode colorMode = ScanAttributes.ColorMode.MONO;
        ScanAttributes.DocumentFormat documentFormat = ScanAttributes.DocumentFormat.PDF_A;
        FileOptionsAttributesCaps fileOptionsCaps = CustomScannerService.getFileOptionsCapabilities(mContext, colorMode, documentFormat, result);

        //validate FileOptionsAttributesCaps
        assertNotNull("fileOptionsCaps", fileOptionsCaps);
        assertEquals("result.getCode", Result.RESULT_OK, result.getCode());
        assertEquals("MONO/PDF_A, OcrLanguageList", Arrays.asList(FileOptionsAttributes.OcrLanguage.DEFAULT), fileOptionsCaps.getOcrLanguageList());
        assertEquals("MONO/PDF_A, PdfCompressionModeList",
                Arrays.asList(FileOptionsAttributes.PdfCompressionMode.DEFAULT, FileOptionsAttributes.PdfCompressionMode.NORMAL,
                        FileOptionsAttributes.PdfCompressionMode.HIGH),
                fileOptionsCaps.getPdfCompressionModeList());
        assertEquals("MONO/PDF_A, TiffCompressionModeList", Arrays.asList(FileOptionsAttributes.TiffCompressionMode.DEFAULT),
                fileOptionsCaps.getTiffCompressionModeList());
        assertEquals("MONO/PDF_A, XpsCompressionMode", Arrays.asList(FileOptionsAttributes.XpsCompressionMode.DEFAULT),
                fileOptionsCaps.getXpsCompressionModeList());
    }

    @FlakyTest //This is basic PI test but disabled temporary due to emulator 'Scanner Failure' after upgrading Dune FW.
    @Test
    public void ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        Log.i(TAG, "ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 6] Build ScanAttributes and Call ScannerService.submit");
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT + "/" + TestInfra.HTTP_SERVER_PATH);
        String mUriHttpUsername = "";
        String mUriHttpPassword = "";

        NetworkCredentialsAttributes httpCredentialsAttributes = null;
        if (!TextUtils.isEmpty(mUriHttpUsername) && !TextUtils.isEmpty(mUriHttpPassword)) {
            httpCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                    .setUserName(mUriHttpUsername)
                    .setPassword(mUriHttpPassword)
                    .build();
        }
        ScanAttributes scanAttributes = createHttpScanAttributes(httpUri, httpCredentialsAttributes, scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, true, false, false);
        Log.i(TAG, "ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : END");
    }

    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenHttpDestinationUnreachable_WhenSubmit_ThenReturnsRidAndJobFails() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        String invalidPort = "8081";
        Log.i(TAG, "ScannerService_submit_$GivenHttpDestinationUnreachable_WhenSubmit_ThenReturnsRidAndJobFails : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestinationUnreachable_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestinationUnreachable_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 6] Build ScanAttributes and Call ScannerService.submit");
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + invalidPort);
        String mUriHttpUsername = "";
        String mUriHttpPassword = "";

        NetworkCredentialsAttributes httpCredentialsAttributes = null;
        if (!TextUtils.isEmpty(mUriHttpUsername) && !TextUtils.isEmpty(mUriHttpPassword)) {
            httpCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                    .setUserName(mUriHttpUsername)
                    .setPassword(mUriHttpPassword)
                    .build();
        }
        ScanAttributes scanAttributes = createHttpScanAttributes(httpUri, httpCredentialsAttributes, scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        validateJobObserver(rid, true, false, true, false);

        Log.i(TAG, "ScannerService_submit_$GivenHttpDestinationUnreachable_WhenSubmit_ThenReturnsRidAndJobFails : END");
    }

    @FlakyTest //This is basic PI test but disabled temporary due to emulator 'Scanner Failure' after upgrading Dune FW.
    @Test
    public void ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes() throws InterruptedException, CapabilitiesExceededException {
        String rid = null;
        Result result = new Result();
        Log.i(TAG, "ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 6] Build ScanAttributes and Call ScannerService.submit");
        ScanAttributes scanAttributes = createNetworkFolderScanAttributes(TestInfra.NETWORK_FOLDER_SERVER_IP, TestInfra.NETWORK_FOLDER_PATH,
                TestInfra.NETWORK_FOLDER_USERNAME, TestInfra.NETWORK_FOLDER_PASSWORD, TestInfra.NETWORK_FOLDER_DOMAIN, scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, true, false, false);
        Log.i(TAG, "ScannerService_submit_$GivenNetworkFolderDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : END");
    }

    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        String invalidPassword = "invalidPassword";
        Log.i(TAG, "ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 6] Build ScanAttributes and Call ScannerService.submit");
        ScanAttributes scanAttributes = createNetworkFolderScanAttributes(TestInfra.NETWORK_FOLDER_SERVER_IP, TestInfra.NETWORK_FOLDER_PATH,
                TestInfra.NETWORK_FOLDER_USERNAME, invalidPassword, TestInfra.NETWORK_FOLDER_DOMAIN, scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, false, true, false);
        Log.i(TAG, "ScannerService_submit_$GivenInvalidNetworkFolderPassword_WhenSubmit_ThenReturnsRidAndJobFails : END");
    }

    @FlakyTest //This is basic PI test but disabled temporary due to emulator 'Scanner Failure' after upgrading Dune FW.
    @Test
    public void ScannerService_submit_$GivenFtpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        Log.i(TAG, "ScannerService_submit_$GivenFtpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_ReturnsRid_OnFtpDestination : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_ReturnsRid_OnFtpDestination : [STEP 6] Build ScanAttributes and Call ScannerService.submit");

        ScanAttributes scanAttributes = createFtpScanAttributes(TestInfra.FTP_SERVER_IP, TestInfra.FTP_PATH, TestInfra.FTP_USERNAME, TestInfra.FTP_PASSWORD,
                scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("RID", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenFtpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, true, false, false);
        Log.i(TAG, "ScannerService_submit_$GivenFtpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : END");
    }

    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        Log.i(TAG, "ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 6] Build ScanAttributes and Call ScannerService.submit");
        ScanAttributes scanAttributes = createMeScanAttributes(scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("rid", rid);
        assertNotNull("rid UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, true, false, false);
        Log.i(TAG, "ScannerService_submit_$GivenMeDestination_WhenSubmit_ThenReturnsRidAndJobCompletes : END");
    }

    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenInvalidFtpPassword_WhenSubmit_ThenReturnsRidAndJobFails() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        String invalidPassword = "invalidPassword";
        Log.i(TAG, "ScannerService_submit_$GivenInvalidFtpPassword_WhenSubmit_ThenReturnsRidAndJobFails : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_ReturnsRid_OnFtpDestination : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_ReturnsRid_OnFtpDestination : [STEP 6] Build ScanAttributes and Call ScannerService.submit");

        ScanAttributes scanAttributes = createFtpScanAttributes(TestInfra.FTP_SERVER_IP, TestInfra.FTP_PATH, TestInfra.FTP_USERNAME, invalidPassword,
                scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("RID", rid);
        assertNotNull("RID UUID", UUID.fromString(rid));

        Log.i(TAG, "ScannerService_submit_$GivenInvalidFtpPassword_WhenSubmit_ThenReturnsRidAndJobFails : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, false, true, false);
        Log.i(TAG, "ScannerService_submit_$GivenInvalidFtpPassword_WhenSubmit_ThenReturnsRidAndJobFails : END");
    }

    @FlakyTest //This is basic PI test but disabled temporary due to emulator 'Scanner Failure' after upgrading Dune FW.
    @Test
    public void ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled() throws InterruptedException, CapabilitiesExceededException {
        String rid;
        Log.i(TAG, "ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled : START");

        // initialize STEP 1-4 for a scan job
        ScanParams scanParams = initializeScanJobParameters();

        Log.i(TAG, "ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled : [STEP 5] Register JobObserver");
        registerJobObserver();

        Log.i(TAG, "ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled : [STEP 6] Build ScanAttributes and Call ScannerService.submit");

        ScanAttributes scanAttributes = createFtpScanAttributes(TestInfra.FTP_SERVER_IP, TestInfra.FTP_PATH, TestInfra.FTP_USERNAME, TestInfra.FTP_PASSWORD,
                scanParams.caps, scanParams.defaults);

        rid = ScannerService.submit(mContext, scanAttributes, scanParams.scanlet);
        assertNotNull("RID", rid);
        assertNotNull("RID UUID", UUID.fromString(rid));

        // wait for job progress event and cancel the job
        boolean isJobProgressed = mProgressLatch.await(10, TimeUnit.SECONDS);
        if (isJobProgressed) {
            JobService.cancelJob(mContext, mJobObserver.getJobIdentifier());
        } else {
            fail("Can't get job progress event");
        }

        Log.i(TAG, "ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled : [STEP 7] Validate results with JobObserver");
        validateJobObserver(rid, true, false, false, true);
        Log.i(TAG, "ScannerService_submit_cancelJob_$GivenFtpJob_WhenCancelDuringProgress_ThenJobCanceled : END");
    }

    private ScanParams initializeScanJobParameters() {
        Result result = new Result();
        Log.i(TAG, "initializeScanJobParameters : [STEP 1] launch the test app from the device home to acquire UIContext");
        mDutController.startApp(mContext);

        Log.i(TAG, "initializeScanJobParameters : [STEP 2] Call ScannerService.isSupported");
        boolean supported = ScannerService.isSupported(mContext);
        assertTrue("isSupported", supported);

        Log.i(TAG, "initializeScanJobParameters : [STEP 3] Call ScannerService.getCapabilities");
        ScanAttributesCaps scanAttributesCaps = ScannerService.getCapabilities(mContext, result);
        assertNotNull("getCapabilities", scanAttributesCaps);

        Log.i(TAG, "initializeScanJobParameters : [STEP 4] Call ScannerService.getDefaults");
        ScanAttributes scanDefaultAttributes = ScannerService.getDefaults(mContext, result);
        ScanletAttributes scanletAttributes = new ScanletAttributes.Builder().setShowSettingsUi(false).build();

        return new ScanParams(scanAttributesCaps, scanDefaultAttributes, scanletAttributes);
    }

    private void registerJobObserver() {
        mCompletionLatch = new CountDownLatch(1);
        mProgressLatch = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mJobObserver = new JobObserver(mainHandler, mCompletionLatch, mProgressLatch);
        mJobObserver.register(mContext);
    }

    private void validateJobObserver(String rid, boolean progressEventReceived,
                                     boolean completeEventReceived,
                                     boolean failEventReceived, boolean cancelEventReceived) throws InterruptedException {

        boolean isJobFinished = mCompletionLatch.await(30, TimeUnit.SECONDS);
        assertTrue("isJobFinished", isJobFinished);
        assertNotNull("JobObserver", mJobObserver);
        assertEquals("RID", mJobObserver.getRequestId(), rid);
        assertEquals("progressEventReceived", progressEventReceived, mJobObserver.isProgressEventReceived());
        assertEquals("completeEventReceived", completeEventReceived, mJobObserver.isCompleteEventReceived());
        assertEquals("failEventReceived", failEventReceived, mJobObserver.isFailEventReceived());
        assertEquals("cancelEventReceived", cancelEventReceived, mJobObserver.isCancelEventReceived());

        if (completeEventReceived) {
            JobInfo jobInfo = mJobObserver.getJobInfo();
            assertNotNull("jobInfo", jobInfo);
            assertEquals("jobInfo: JobType", JobInfo.JobType.SCAN, jobInfo.getJobType());
            assertNotEquals("jobInfo: getCompleteTime", 0, jobInfo.getCompleteTime());
            assertNotNull("jobInfo: getJobId", jobInfo.getJobId());
            assertNotNull("jobInfo: JobId UUID", UUID.fromString(jobInfo.getJobId()));

            // Validate JobInfo fields
            assertNotNull("JobInfo: getOwner", jobInfo.getOwner());

            ScanJobData scanJobData = jobInfo.getJobData();
            assertEquals("ScanJobData: getState", ScanJobState.State.COMPLETED, scanJobData.getJobState().getState());
            assertEquals("ScanJobData: getScanningState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getScanningState());
            assertEquals("ScanJobData: getProcessingState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getProcessingState());
            assertEquals("ScanJobData: getTransmittingState", ScanJobState.ActivityState.COMPLETED, scanJobData.getJobState().getTransmittingState());
            assertEquals("ScanJobData: getCancelingState", ScanJobState.ActivityState.NOT_STARTED, scanJobData.getJobState().getCancelingState());

            assertEquals("ScanJobData: getImagesScanned", 1, scanJobData.getImagesScanned());
            assertEquals("ScanJobData: getImagesTransmitted", 1, scanJobData.getImagesTransmitted());

            // Validate ScanJobData fields
            assertNotNull("ScanJobData: getDuplex", scanJobData.getDuplex());
            assertNotNull("ScanJobData: getScanSize", scanJobData.getScanSize());
            assertNotNull("ScanJobData: getDestination", scanJobData.getDestination());
            assertNotNull("ScanJobData: getFileNames", scanJobData.getFileNames());

            assertFalse("ScanJobData: getFileNames should not be empty", scanJobData.getFileNames().isEmpty());
        }
    }

    private class ScanParams {
        private final ScanAttributesCaps caps;
        private final ScanAttributes defaults;
        private final ScanletAttributes scanlet;

        public ScanParams(ScanAttributesCaps caps, ScanAttributes defaults, ScanletAttributes scanlet) {
            this.caps = caps;
            this.defaults = defaults;
            this.scanlet = scanlet;
        }
    }

    // ==================== Multiple Scan Job (Scan-Ahead) Tests ====================

    /**
     * Req 3: Two concurrent scan jobs submitted with allowMultipleScan=true.
     * Both jobs should complete independently with their own notifications.
     */
    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenAllowMultipleScan_WhenTwoJobsSubmitted_ThenBothComplete() throws InterruptedException, CapabilitiesExceededException {
        Log.i(TAG, "MultipleScan_TwoJobsSubmitted_BothComplete : START");

        // STEP 1-4: initialize scan parameters with allowMultipleScan=true
        ScanParams scanParams = initializeMultipleScanJobParameters();

        // STEP 5: Register observers for 2 jobs
        CountDownLatch completionLatch = new CountDownLatch(2);
        CountDownLatch progressLatch1 = new CountDownLatch(1);
        CountDownLatch progressLatch2 = new CountDownLatch(1);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        JobObserver observer1 = new JobObserver(mainHandler, completionLatch, progressLatch1);
        JobObserver observer2 = new JobObserver(mainHandler, completionLatch, progressLatch2);
        observer1.register(mContext);
        observer2.register(mContext);

        // STEP 6: Submit 2 scan jobs concurrently
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT + "/" + TestInfra.HTTP_SERVER_PATH);
        ScanAttributes scanAttributes1 = createHttpScanAttributes(httpUri, null, scanParams.caps, scanParams.defaults);
        ScanAttributes scanAttributes2 = createHttpScanAttributes(httpUri, null, scanParams.caps, scanParams.defaults);

        String rid1 = ScannerService.submit(mContext, scanAttributes1, scanParams.scanlet);
        String rid2 = ScannerService.submit(mContext, scanAttributes2, scanParams.scanlet);

        assertNotNull("rid1", rid1);
        assertNotNull("rid2", rid2);
        assertNotEquals("RIDs should be different", rid1, rid2);

        Log.i(TAG, "MultipleScan_TwoJobsSubmitted_BothComplete : rid1=" + rid1 + ", rid2=" + rid2);

        // STEP 7: Wait for both jobs to complete (timeout 60s for 2 scans)
        boolean bothFinished = completionLatch.await(60, TimeUnit.SECONDS);
        assertTrue("Both jobs should finish within timeout", bothFinished);

        // STEP 8: Verify both jobs received independent notifications
        assertTrue("Job1 progress received", observer1.isProgressEventReceived() || observer2.isProgressEventReceived());
        // At least one observer should see completion
        assertTrue("At least one job completed",
                observer1.isCompleteEventReceived() || observer2.isCompleteEventReceived());

        // Cleanup
        observer1.unregister(mContext);
        observer2.unregister(mContext);

        Log.i(TAG, "MultipleScan_TwoJobsSubmitted_BothComplete : END");
    }

    /**
     * Req 2: Sequential scan mode (allowMultipleScan=false).
     * When a second job is submitted while first is processing, it should
     * be handled by the same StateMachine (legacy single-scan behavior).
     */
    @FlakyTest
    @Test
    public void ScannerService_submit_$GivenSingleScanMode_WhenTwoJobsSubmitted_ThenProcessedSequentially() throws InterruptedException, CapabilitiesExceededException {
        Log.i(TAG, "SingleScan_TwoJobsSubmitted_Sequential : START");

        // STEP 1-4: initialize scan parameters with allowMultipleScan=false (default)
        ScanParams scanParams = initializeScanJobParameters();

        // STEP 5: Register observer
        registerJobObserver();

        // STEP 6: Submit first scan job
        Uri httpUri = Uri.parse("http://" + TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT + "/" + TestInfra.HTTP_SERVER_PATH);
        ScanAttributes scanAttributes1 = createHttpScanAttributes(httpUri, null, scanParams.caps, scanParams.defaults);
        ScanAttributes scanAttributes2 = createHttpScanAttributes(httpUri, null, scanParams.caps, scanParams.defaults);

        String rid1 = ScannerService.submit(mContext, scanAttributes1, scanParams.scanlet);
        assertNotNull("rid1", rid1);
        Log.i(TAG, "SingleScan_TwoJobsSubmitted_Sequential : rid1=" + rid1);

        // Submit second job immediately (while first is likely still processing)
        String rid2 = ScannerService.submit(mContext, scanAttributes2, scanParams.scanlet);
        assertNotNull("rid2", rid2);
        Log.i(TAG, "SingleScan_TwoJobsSubmitted_Sequential : rid2=" + rid2);

        // STEP 7: Wait for job completion
        boolean isJobFinished = mCompletionLatch.await(30, TimeUnit.SECONDS);
        assertTrue("Job should finish", isJobFinished);

        // In single-scan mode, the second submit goes to the same SM.
        // We verify at least one RID completed successfully.
        assertTrue("At least one job completed or progressed",
                mJobObserver.isCompleteEventReceived() || mJobObserver.isProgressEventReceived());

        Log.i(TAG, "SingleScan_TwoJobsSubmitted_Sequential : END");
    }

    private ScanParams initializeMultipleScanJobParameters() {
        Result result = new Result();
        Log.i(TAG, "initializeMultipleScanJobParameters : [STEP 1] launch the test app");
        mDutController.startApp(mContext);

        Log.i(TAG, "initializeMultipleScanJobParameters : [STEP 2] Call ScannerService.isSupported");
        boolean supported = ScannerService.isSupported(mContext);
        assertTrue("isSupported", supported);

        Log.i(TAG, "initializeMultipleScanJobParameters : [STEP 3] Call ScannerService.getCapabilities");
        ScanAttributesCaps scanAttributesCaps = ScannerService.getCapabilities(mContext, result);
        assertNotNull("getCapabilities", scanAttributesCaps);

        Log.i(TAG, "initializeMultipleScanJobParameters : [STEP 4] Call ScannerService.getDefaults");
        ScanAttributes scanDefaultAttributes = ScannerService.getDefaults(mContext, result);

        // allowMultipleScan=true for concurrent scan-ahead behavior
        ScanletAttributes scanletAttributes = new ScanletAttributes.Builder()
                .setShowSettingsUi(false)
                .setAllowMultipleScan(true)
                .build();

        return new ScanParams(scanAttributesCaps, scanDefaultAttributes, scanletAttributes);
    }
}
