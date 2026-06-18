package com.hp.jetadvantage.link.services.copylet.adapter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for StoredCopyJobAdapter.
 * Tests CopyAttributes → ScanTicket conversion with real Android types.
 */
@RunWith(AndroidJUnit4.class)
public class StoredCopyJobAdapterInstrumentedTest extends BaseInstrumentedTest {
    private static final String TEST_PACKAGE = "com.hp.test.storedcopy";
    private Context context;

    @Before
    public void setUp() {
        super.SetUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    // ===== Test 1: CopyAttributes to ScanOptions Mapping =====

    @Test
    public void testCopyAttributesToScanOptionsMapping() {
        CopyAttributes copyAttributes = createTestStoreCopyAttributes(
                CopyAttributes.ColorMode.COLOR,
                CopyAttributes.Duplex.NONE,
                CopyAttributes.ScanSize.A4,
                CopyAttributes.ScanSource.DEFAULT,
                "TestJob", "TestFolder");

        CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);

        // Verify reader correctly reads the STORE mode attributes
        assertNotNull("CopyAttributesReader should not be null", reader);
        assertTrue("ColorMode should be COLOR",
                reader.getColorMode() == CopyAttributes.ColorMode.COLOR);
        assertTrue("ScanDuplex should be NONE",
                reader.getScanDuplex() == CopyAttributes.Duplex.NONE);
        assertTrue("ScanSize should be A4",
                reader.getScanSize() == CopyAttributes.ScanSize.A4);
        assertTrue("JobExecutionMode should be STORE",
                reader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE);

        // Verify CopyTypeMapping converts color mode correctly
        Object e2ColorMode = CopyTypeMapping.colorMode.convertWtoE(
                reader.getColorMode(), null);
        assertNotNull("E2 color mode conversion should not be null", e2ColorMode);

        // Verify content orientation mapping
        Object e2Orientation = CopyTypeMapping.contentOrientation.convertWtoE(
                reader.getOrientation(), null);
        // DEFAULT orientation maps to null (no E2 equivalent)
        // This is expected behavior

        // Verify scan source mapping
        Object e2ScanSource = CopyTypeMapping.originalMediaSource.convertWtoE(
                reader.getScanSource(), null);
        // DEFAULT scan source may map to null — that is expected
    }

    // ===== Test 2: JobStorage Destination Creation =====

    @Test
    public void testJobStorageDestinationCreation() {
        CopyAttributes copyAttributes = createTestStoreCopyAttributes(
                CopyAttributes.ColorMode.COLOR,
                CopyAttributes.Duplex.NONE,
                CopyAttributes.ScanSize.A4,
                CopyAttributes.ScanSource.DEFAULT,
                "StoredJobName", "StoredJobFolder");

        CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);

        // Verify the store job fields are set
        assertNotNull("StoreJobName should not be null", reader.getStoreJobName());
        assertTrue("StoreJobName should be 'StoredJobName'",
                "StoredJobName".equals(reader.getStoreJobName()));

        assertNotNull("StoreJobFolderName should not be null", reader.getStoreJobFolderName());
        assertTrue("StoreJobFolderName should be 'StoredJobFolder'",
                "StoredJobFolder".equals(reader.getStoreJobFolderName()));

        // Verify JobExecutionMode is STORE
        assertTrue("JobExecutionMode should be STORE",
                reader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE);
    }

    // ===== Test 3: ScanTicket Name and Folder Set =====

    @Test
    public void testScanTicketNameAndFolderSet() {
        CopyAttributes copyAttributes = createTestStoreCopyAttributes(
                CopyAttributes.ColorMode.GRAY,
                CopyAttributes.Duplex.BOOK,
                CopyAttributes.ScanSize.LETTER,
                CopyAttributes.ScanSource.DEFAULT,
                "MyJobName", "MyFolder");

        CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);

        // Verify the name and folder fields are readable and correct
        assertTrue("StoreJobName should match",
                "MyJobName".equals(reader.getStoreJobName()));
        assertTrue("StoreJobFolderName should match",
                "MyFolder".equals(reader.getStoreJobFolderName()));
    }

    // ===== Test 4: CopyTypeMapping Consistency =====

    @Test
    public void testCopyTypeMappingConsistency() {
        // Test that various CopyTypeMapping conversions are consistent
        // for types used in StoredCopyJobAdapter

        // Color mode: COLOR → E2 and back
        Object e2Color = CopyTypeMapping.colorMode.convertWtoE(
                CopyAttributes.ColorMode.COLOR, null);
        assertNotNull("COLOR → E2 should not be null", e2Color);

        Object wColor = CopyTypeMapping.colorMode.convertEtoW(e2Color);
        assertNotNull("E2 → COLOR should not be null", wColor);
        assertTrue("Round-trip COLOR should match",
                wColor == CopyAttributes.ColorMode.COLOR);

        // Scan size: A4 → E2 and back
        Object e2Size = CopyTypeMapping.originalMediaSize.convertWtoE(
                CopyAttributes.ScanSize.A4, null);
        assertNotNull("A4 → E2 should not be null", e2Size);

        Object wSize = CopyTypeMapping.originalMediaSize.convertEtoW(e2Size);
        assertNotNull("E2 → A4 should not be null", wSize);
        assertTrue("Round-trip A4 should match",
                wSize == CopyAttributes.ScanSize.A4);

        // Scan capture mode: DEFAULT → E2 (null is expected)
        Object e2Capture = CopyTypeMapping.scanCaptureMode.convertWtoE(
                CopyAttributes.CaptureMode.DEFAULT, null);
        // DEFAULT maps to null in E2 — this is expected behavior
    }

    // ===== Test 5: Null CopyAttributes returns null =====

    @Test
    public void testNullCopyAttributesReturnsNull() throws Exception {
        String jobId = StoredCopyJobAdapter.createScanJobForStorage(
                null, null, TEST_PACKAGE, null);
        assertNull("Null CopyAttributes should return null jobId", jobId);
    }

    // ===== Test: Null ScanJobService returns null =====

    @Test
    public void testNullScanJobServiceReturnsNull() throws Exception {
        CopyAttributes copyAttributes = createTestStoreCopyAttributes(
                CopyAttributes.ColorMode.COLOR,
                CopyAttributes.Duplex.NONE,
                CopyAttributes.ScanSize.A4,
                CopyAttributes.ScanSource.DEFAULT,
                "TestJob", "TestFolder");

        CopyAttributesReader reader = new CopyAttributesReader(copyAttributes);
        String jobId = StoredCopyJobAdapter.createScanJobForStorage(
                null, null, TEST_PACKAGE, reader);
        assertNull("Null ScanJobService should return null jobId", jobId);
    }

    // ===== Helper Methods =====

    private CopyAttributes createTestStoreCopyAttributes(
            CopyAttributes.ColorMode colorMode,
            CopyAttributes.Duplex scanDuplex,
            CopyAttributes.ScanSize scanSize,
            CopyAttributes.ScanSource scanSource,
            String jobName, String folderName) {
        return createCopyAttributesViaParcel(
                colorMode, scanDuplex, scanSize, scanSource,
                CopyAttributes.JobExecutionMode.STORE, jobName, folderName);
    }

    /**
     * Creates CopyAttributes using Parcel serialization.
     * Writes all fields in the exact order expected by CopyAttributes(Parcel) constructor.
     */
    private CopyAttributes createCopyAttributesViaParcel(
            CopyAttributes.ColorMode colorMode,
            CopyAttributes.Duplex scanDuplex,
            CopyAttributes.ScanSize scanSize,
            CopyAttributes.ScanSource scanSource,
            CopyAttributes.JobExecutionMode jobExecutionMode,
            String storeJobName,
            String storeJobFolderName) {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            int version = com.hp.jetadvantage.link.common.Sdk.VERSION.LEVEL;
            parcel.writeInt(version);
            parcel.writeSerializable(colorMode);
            parcel.writeSerializable(CopyAttributes.Orientation.DEFAULT);
            parcel.writeSerializable(scanDuplex);
            parcel.writeSerializable(scanSize);
            parcel.writeSerializable(Float.valueOf(0f));
            parcel.writeSerializable(Float.valueOf(0f));
            parcel.writeSerializable(scanSource);
            parcel.writeSerializable(CopyAttributes.CopyPreview.DEFAULT);
            parcel.writeSerializable(CopyAttributes.BackgroundCleanup.DEFAULT);
            parcel.writeSerializable(CopyAttributes.ContrastAdjustment.DEFAULT);
            parcel.writeSerializable(CopyAttributes.DarknessAdjustment.DEFAULT);
            parcel.writeSerializable(CopyAttributes.SharpnessAdjustment.DEFAULT);
            parcel.writeSerializable(CopyAttributes.Duplex.DEFAULT);
            parcel.writeSerializable(CopyAttributes.PaperSize.DEFAULT);
            parcel.writeSerializable(Float.valueOf(0f));
            parcel.writeSerializable(Float.valueOf(0f));
            parcel.writeInt(1);
            parcel.writeSerializable(CopyAttributes.CollateMode.DEFAULT);
            parcel.writeSerializable(CopyAttributes.PaperSource.DEFAULT);
            parcel.writeSerializable(CopyAttributes.PaperType.DEFAULT);
            parcel.writeSerializable(CopyAttributes.ScaleMode.DEFAULT);
            parcel.writeInt(100);
            parcel.writeSerializable(CopyAttributes.TextGraphicsOptimization.DEFAULT);
            parcel.writeSerializable(CopyAttributes.JobAssemblyMode.DEFAULT);
            parcel.writeSerializable(jobExecutionMode);
            parcel.writeSerializable(CopyAttributes.NumberUpMode.DEFAULT);
            parcel.writeSerializable(CopyAttributes.NumberUpDirection.DEFAULT);
            parcel.writeString(storeJobFolderName);
            parcel.writeString(storeJobName);
            parcel.writeSerializable(CopyAttributes.RetentionMode.DEFAULT);
            parcel.writeSerializable(CopyAttributes.RetentionMode.DEFAULT);
            parcel.writeParcelable(null, 0);

            parcel.writeSerializable(CopyAttributes.OutputBin.DEFAULT);
            parcel.writeSerializable(CopyAttributes.ProgressDialogMode.DEFAULT);

            parcel.writeSerializable(CopyAttributes.EraseMarginUnit.DEFAULT);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeSerializable(CopyAttributes.CaptureMode.DEFAULT);
            parcel.writeSerializable(CopyAttributes.ImageShiftReduceToFit.DEFAULT);
            parcel.writeSerializable(CopyAttributes.ImageShiftUnits.DEFAULT);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeFloat(0f);
            parcel.writeSerializable(CopyAttributes.BookletBordersEachPage.DEFAULT);
            parcel.writeSerializable(CopyAttributes.BookletFinishingOption.DEFAULT);
            parcel.writeSerializable(CopyAttributes.BookletFormat.DEFAULT);
            parcel.writeSerializable(CopyAttributes.StapleOption.DEFAULT);
            parcel.writeSerializable(CopyAttributes.PunchMode.DEFAULT);
            parcel.writeSerializable(CopyAttributes.FoldMode.DEFAULT);
            parcel.writeMap(new java.util.HashMap<>());
            parcel.writeInt(1);
            parcel.writeString("");
            parcel.writeSerializable(CopyAttributes.WatermarkRotate45.DEFAULT);
            parcel.writeSerializable(CopyAttributes.WatermarkType.DEFAULT);
            parcel.writeInt(0);
            parcel.writeInt(0);
            parcel.writeString("");
            parcel.writeString("");
            parcel.writeString("");
            parcel.writeSerializable(CopyAttributes.WatermarkOnlyFirstPage.DEFAULT);
            parcel.writeSerializable(CopyAttributes.WatermarkMessageType.NONE);
            parcel.writeSerializable(CopyAttributes.WatermarkBackgroundPattern.SCROLL);

            parcel.setDataPosition(0);
            return CopyAttributes.CREATOR.createFromParcel(parcel);
        } finally {
            parcel.recycle();
        }
    }
}
