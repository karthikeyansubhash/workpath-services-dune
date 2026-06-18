package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for StoredCopyJobPreferenceStorage.
 * Tests real SharedPreferences CRUD operations on actual Android device/emulator.
 */
@RunWith(AndroidJUnit4.class)
public class StoredCopyJobPreferenceStorageInstrumentedTest extends BaseInstrumentedTest {
    private Context context;

    @Before
    public void setUp() {
        super.SetUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Clear SharedPreferences before each test
        context.getSharedPreferences("stored_copy_job_map", Context.MODE_PRIVATE)
                .edit().clear().commit();
    }

    @After
    public void tearDown() {
        // Clean up SharedPreferences after each test
        context.getSharedPreferences("stored_copy_job_map", Context.MODE_PRIVATE)
                .edit().clear().commit();
    }

    // ===== Test 1: Save and Get =====

    @Test
    public void testSaveAndGet() {
        CopyAttributes original = createTestCopyAttributes();
        String key = "testKey-save-get";

        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, key, original);

        CopyAttributes restored = StoredCopyJobPreferenceStorage.getCopyAttributes(context, key);
        assertNotNull("Restored CopyAttributes should not be null", restored);

        CopyAttributesReader originalReader = new CopyAttributesReader(original);
        CopyAttributesReader restoredReader = new CopyAttributesReader(restored);
        assertEquals("ColorMode should match",
                originalReader.getColorMode(), restoredReader.getColorMode());
        assertEquals("ScanDuplex should match",
                originalReader.getScanDuplex(), restoredReader.getScanDuplex());
        assertEquals("ScanSize should match",
                originalReader.getScanSize(), restoredReader.getScanSize());
        assertEquals("ScanSource should match",
                originalReader.getScanSource(), restoredReader.getScanSource());
        assertEquals("JobExecutionMode should match",
                originalReader.getJobExecutionMode(), restoredReader.getJobExecutionMode());
        assertEquals("StoreJobName should match",
                originalReader.getStoreJobName(), restoredReader.getStoreJobName());
        assertEquals("StoreJobFolderName should match",
                originalReader.getStoreJobFolderName(), restoredReader.getStoreJobFolderName());
    }

    // ===== Test 2: Get Non-Existent Key Returns Null =====

    @Test
    public void testGetNonExistentKey_ReturnsNull() {
        CopyAttributes result = StoredCopyJobPreferenceStorage.getCopyAttributes(
                context, "non-existent-key-12345");
        assertNull("Getting non-existent key should return null", result);
    }

    // ===== Test 3: Remove =====

    @Test
    public void testRemove() {
        CopyAttributes original = createTestCopyAttributes();
        String key = "testKey-remove";

        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, key, original);
        assertNotNull("Should exist after save",
                StoredCopyJobPreferenceStorage.getCopyAttributes(context, key));

        StoredCopyJobPreferenceStorage.remove(context, key);
        assertNull("Should be null after remove",
                StoredCopyJobPreferenceStorage.getCopyAttributes(context, key));
    }

    // ===== Test 4: Replace Key =====

    @Test
    public void testReplaceKey() {
        CopyAttributes original = createTestCopyAttributes();
        String oldKey = "oldKey-replace";
        String newKey = "newKey-replace";

        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, oldKey, original);
        StoredCopyJobPreferenceStorage.replaceKey(context, oldKey, newKey);

        // Wait for async apply to complete
        sleepMs(100);

        assertNull("Old key should no longer exist",
                StoredCopyJobPreferenceStorage.getCopyAttributes(context, oldKey));

        CopyAttributes restored = StoredCopyJobPreferenceStorage.getCopyAttributes(context, newKey);
        assertNotNull("New key should have the value", restored);

        CopyAttributesReader originalReader = new CopyAttributesReader(original);
        CopyAttributesReader restoredReader = new CopyAttributesReader(restored);
        assertEquals("ColorMode should match after replaceKey",
                originalReader.getColorMode(), restoredReader.getColorMode());
        assertEquals("JobExecutionMode should match after replaceKey",
                originalReader.getJobExecutionMode(), restoredReader.getJobExecutionMode());
    }

    // ===== Test 5: Replace Key - Old Key Not Exist =====

    @Test
    public void testReplaceKey_OldKeyNotExist() {
        String nonExistKey = "nonExistKey-12345";
        String newKey = "newKey-from-nonExist";

        // Should not crash
        StoredCopyJobPreferenceStorage.replaceKey(context, nonExistKey, newKey);

        // Wait for async apply
        sleepMs(100);

        assertNull("New key should be null when old key doesn't exist",
                StoredCopyJobPreferenceStorage.getCopyAttributes(context, newKey));
    }

    // ===== Test 6: Data Persistence =====

    @Test
    public void testDataPersistence() {
        CopyAttributes original = createTestCopyAttributes();
        String key = "testKey-persistence";

        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, key, original);

        // Re-read from same context (simulates persistence)
        CopyAttributes restored1 = StoredCopyJobPreferenceStorage.getCopyAttributes(context, key);
        assertNotNull("First read should return data", restored1);

        CopyAttributes restored2 = StoredCopyJobPreferenceStorage.getCopyAttributes(context, key);
        assertNotNull("Second read should also return data", restored2);

        CopyAttributesReader reader1 = new CopyAttributesReader(restored1);
        CopyAttributesReader reader2 = new CopyAttributesReader(restored2);
        assertEquals("ColorMode should be consistent across reads",
                reader1.getColorMode(), reader2.getColorMode());
        assertEquals("JobExecutionMode should be consistent across reads",
                reader1.getJobExecutionMode(), reader2.getJobExecutionMode());
    }

    // ===== Test 7: Multiple Entries =====

    @Test
    public void testMultipleEntries() {
        CopyAttributes attrs1 = createTestCopyAttributes();
        CopyAttributes attrs2 = createTestCopyAttributesWithDifferentSettings();

        String key1 = "multiKey-1";
        String key2 = "multiKey-2";

        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, key1, attrs1);
        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, key2, attrs2);

        CopyAttributes restored1 = StoredCopyJobPreferenceStorage.getCopyAttributes(context, key1);
        CopyAttributes restored2 = StoredCopyJobPreferenceStorage.getCopyAttributes(context, key2);

        assertNotNull("Key1 should have value", restored1);
        assertNotNull("Key2 should have value", restored2);

        CopyAttributesReader reader1 = new CopyAttributesReader(restored1);
        CopyAttributesReader reader2 = new CopyAttributesReader(restored2);

        assertEquals("Key1 ColorMode should match",
                CopyAttributes.ColorMode.COLOR, reader1.getColorMode());
        assertEquals("Key2 ColorMode should match",
                CopyAttributes.ColorMode.GRAY, reader2.getColorMode());
    }

    // ===== Helper Methods =====

    private CopyAttributes createTestCopyAttributes() {
        return createCopyAttributesViaParcel(
                CopyAttributes.ColorMode.COLOR,
                CopyAttributes.Duplex.NONE,
                CopyAttributes.ScanSize.A4,
                CopyAttributes.ScanSource.DEFAULT,
                CopyAttributes.JobExecutionMode.STORE,
                "TestStoredJob", "TestFolder");
    }

    private CopyAttributes createTestCopyAttributesWithDifferentSettings() {
        return createCopyAttributesViaParcel(
                CopyAttributes.ColorMode.GRAY,
                CopyAttributes.Duplex.BOOK,
                CopyAttributes.ScanSize.LETTER,
                CopyAttributes.ScanSource.DEFAULT,
                CopyAttributes.JobExecutionMode.STORE,
                "AnotherJob", "AnotherFolder");
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
            parcel.writeInt(version);                                              // mVersion
            parcel.writeSerializable(colorMode);                                   // mColorMode
            parcel.writeSerializable(CopyAttributes.Orientation.DEFAULT);          // mOrientation
            parcel.writeSerializable(scanDuplex);                                  // mScanDuplex
            parcel.writeSerializable(scanSize);                                    // mScanSize
            parcel.writeSerializable(Float.valueOf(0f));                           // mScanCustomLength
            parcel.writeSerializable(Float.valueOf(0f));                           // mScanCustomWidth
            parcel.writeSerializable(scanSource);                                  // mScanSource
            parcel.writeSerializable(CopyAttributes.CopyPreview.DEFAULT);          // mCopyPreview
            parcel.writeSerializable(CopyAttributes.BackgroundCleanup.DEFAULT);    // mBackgroundCleanup
            parcel.writeSerializable(CopyAttributes.ContrastAdjustment.DEFAULT);   // mContrastAdjustment
            parcel.writeSerializable(CopyAttributes.DarknessAdjustment.DEFAULT);   // mDarknessAdjustment
            parcel.writeSerializable(CopyAttributes.SharpnessAdjustment.DEFAULT);  // mSharpnessAdjustment
            parcel.writeSerializable(CopyAttributes.Duplex.DEFAULT);               // mPrintDuplex
            parcel.writeSerializable(CopyAttributes.PaperSize.DEFAULT);            // mPrintSize
            parcel.writeSerializable(Float.valueOf(0f));                           // mPrintCustomLength
            parcel.writeSerializable(Float.valueOf(0f));                           // mPrintCustomWidth
            parcel.writeInt(1);                                                    // mCopies
            parcel.writeSerializable(CopyAttributes.CollateMode.DEFAULT);          // mCollateMode
            parcel.writeSerializable(CopyAttributes.PaperSource.DEFAULT);          // mPaperSource
            parcel.writeSerializable(CopyAttributes.PaperType.DEFAULT);            // mPaperType
            parcel.writeSerializable(CopyAttributes.ScaleMode.DEFAULT);            // mScaleMode
            parcel.writeInt(100);                                                  // mScalePercent
            parcel.writeSerializable(CopyAttributes.TextGraphicsOptimization.DEFAULT); // mTextGraphicsOptimization
            parcel.writeSerializable(CopyAttributes.JobAssemblyMode.DEFAULT);      // mJobAssemblyMode
            parcel.writeSerializable(jobExecutionMode);                            // mJobExecutionMode
            parcel.writeSerializable(CopyAttributes.NumberUpMode.DEFAULT);         // mNumberUpMode
            parcel.writeSerializable(CopyAttributes.NumberUpDirection.DEFAULT);    // mNumberUpDirection
            parcel.writeString(storeJobFolderName);                                // mStoreJobFolderName
            parcel.writeString(storeJobName);                                      // mStoreJobName
            parcel.writeSerializable(CopyAttributes.RetentionMode.DEFAULT);        // mStoredJobRetentionModeOnPowerCycle
            parcel.writeSerializable(CopyAttributes.RetentionMode.DEFAULT);        // mStoredJobRetentionModeOnRelease
            parcel.writeParcelable(null, 0);                                       // mStoredJobCredentialsAttributes

            // Version >= 6 fields
            parcel.writeSerializable(CopyAttributes.OutputBin.DEFAULT);            // mOutputBin
            parcel.writeSerializable(CopyAttributes.ProgressDialogMode.DEFAULT);   // mProgressDialogMode

            // Version >= 7 fields
            parcel.writeSerializable(CopyAttributes.EraseMarginUnit.DEFAULT);      // mEraseMarginUnit
            parcel.writeFloat(0f); // mEraseBackBottom
            parcel.writeFloat(0f); // mEraseBackLeft
            parcel.writeFloat(0f); // mEraseBackRight
            parcel.writeFloat(0f); // mEraseBackTop
            parcel.writeFloat(0f); // mEraseFrontBottom
            parcel.writeFloat(0f); // mEraseFrontLeft
            parcel.writeFloat(0f); // mEraseFrontRight
            parcel.writeFloat(0f); // mEraseFrontTop
            parcel.writeSerializable(CopyAttributes.CaptureMode.DEFAULT);          // mCaptureMode
            parcel.writeSerializable(CopyAttributes.ImageShiftReduceToFit.DEFAULT);// mImageShiftReduceToFit
            parcel.writeSerializable(CopyAttributes.ImageShiftUnits.DEFAULT);      // mImageShiftUnits
            parcel.writeFloat(0f); // mImageShiftXFront
            parcel.writeFloat(0f); // mImageShiftYFront
            parcel.writeFloat(0f); // mImageShiftXBack
            parcel.writeFloat(0f); // mImageShiftYBack
            parcel.writeSerializable(CopyAttributes.BookletBordersEachPage.DEFAULT); // mBookletBordersEachPage
            parcel.writeSerializable(CopyAttributes.BookletFinishingOption.DEFAULT);  // mBookletFinishingOption
            parcel.writeSerializable(CopyAttributes.BookletFormat.DEFAULT);          // mBookletFormat
            parcel.writeSerializable(CopyAttributes.StapleOption.DEFAULT);           // mStapleOption
            parcel.writeSerializable(CopyAttributes.PunchMode.DEFAULT);              // mPunchMode
            parcel.writeSerializable(CopyAttributes.FoldMode.DEFAULT);               // mFoldMode
            parcel.writeMap(new java.util.HashMap<>());                              // mStampOptionMap
            parcel.writeInt(1);   // mWatermarkDarkness
            parcel.writeString(""); // mWatermarkText
            parcel.writeSerializable(CopyAttributes.WatermarkRotate45.DEFAULT);      // mWatermarkRotate45
            parcel.writeSerializable(CopyAttributes.WatermarkType.DEFAULT);          // mWatermarkType
            parcel.writeInt(0);   // mWatermarkTextSize
            parcel.writeInt(0);   // mWatermarkTransparency
            parcel.writeString(""); // mWatermarkBackgroundColor
            parcel.writeString(""); // mWatermarkFont
            parcel.writeString(""); // mWatermarkTextColor
            parcel.writeSerializable(CopyAttributes.WatermarkOnlyFirstPage.DEFAULT); // mWatermarkOnlyFirstPage
            parcel.writeSerializable(CopyAttributes.WatermarkMessageType.NONE);   // mWatermarkMessageType
            parcel.writeSerializable(CopyAttributes.WatermarkBackgroundPattern.SCROLL); // mWatermarkBackgroundPattern

            parcel.setDataPosition(0);
            return CopyAttributes.CREATOR.createFromParcel(parcel);
        } finally {
            parcel.recycle();
        }
    }
}
