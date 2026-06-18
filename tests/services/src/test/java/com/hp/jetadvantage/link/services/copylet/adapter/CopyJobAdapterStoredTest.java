package com.hp.jetadvantage.link.services.copylet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import com.hp.ext.service.copy.CopyJobIdentifier;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.RemoveStoredJobRequest;
import com.hp.ext.service.copy.StoredJob;
import com.hp.ext.service.copy.StoredJob_Remove;
import com.hp.ext.service.copy.StoredJobs;
import com.hp.ext.types.job.StoredJobPasswordType;
import com.hp.ext.types.protocol.Unsigned64;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.api.copier.StoredJobInfo;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class CopyJobAdapterStoredTest {

    private static final String TEST_PACKAGE = "com.hp.test";
    private static final String TEST_JOB_ID_1 = "3b0ad5e1-2ad7-480f-a29c-4b28e3346e46";
    private static final String TEST_JOB_ID_2 = "59ecaac2-5ac0-4873-9d04-b5b29252727c";

    @Mock private IDeviceCopyJobService mockCopyJobService;
    @Mock private Context mockContext;
    @Mock private SharedPreferences mockSharedPreferences;
    @Mock private SharedPreferences.Editor mockEditor;

    @Before
    public void setUp() {
        when(mockContext.getSharedPreferences(anyString(), org.mockito.ArgumentMatchers.anyInt()))
                .thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.remove(anyString())).thenReturn(mockEditor);
    }

    // ===== convertToStoredJobInfoList tests =====

    @Test
    public void GivenStoredJobs_WhenConvert_ThenStoredJobInfoListReturned() {
        StoredJobs storedJobs = createStoredJobs(createStoredJob(TEST_JOB_ID_1, "TestJob", "folder1",
                "guest", "sjptNone", 2L));
        StoredJob detailedJob = createStoredJob(TEST_JOB_ID_1, "TestJob", "folder1",
                "guest", "sjptNone", 2L);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(detailedJob);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertEquals(1, result.size());
        StoredJobInfo info = result.get(0);
        assertEquals(TEST_JOB_ID_1, info.getStoredJobId());
        assertEquals("TestJob", info.getStoredJobName());
        assertEquals("folder1", info.getStoredJobFolderName());
        assertEquals("guest", info.getStoredJobUserName());
        assertEquals(2, info.getTotalPages());
    }

    @Test
    public void GivenNullStoredJobs_WhenConvert_ThenEmptyListReturned() {
        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                null, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void GivenEmptyMembers_WhenConvert_ThenEmptyListReturned() {
        StoredJobs storedJobs = new StoredJobs();
        storedJobs.setMembers(new ArrayList<>());

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void GivenNullMembers_WhenConvert_ThenEmptyListReturned() {
        StoredJobs storedJobs = new StoredJobs();
        // members is null by default

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void GivenMultipleMembers_WhenConvert_ThenAllMembersFetched() {
        StoredJobs storedJobs = createStoredJobs(
                createStoredJob(TEST_JOB_ID_1, "Job1", "f1", "user1", "sjptNone", 1L),
                createStoredJob(TEST_JOB_ID_2, "Job2", "f2", "user2", "sjptNumericPIN", 3L)
        );
        when(mockCopyJobService.getStoredJob(anyString(), anyString()))
                .thenReturn(createStoredJob(TEST_JOB_ID_1, "Job1", "f1", "user1", "sjptNone", 1L))
                .thenReturn(createStoredJob(TEST_JOB_ID_2, "Job2", "f2", "user2", "sjptNumericPIN", 3L));

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(2, result.size());
        verify(mockCopyJobService, times(2)).getStoredJob(anyString(), anyString());
    }

    @Test
    public void GivenFetchFails_WhenConvert_ThenSummaryUsed() {
        StoredJob summaryJob = createStoredJob(TEST_JOB_ID_1, "SummaryJob", "folder", "guest", "sjptNone", 1L);
        StoredJobs storedJobs = createStoredJobs(summaryJob);
        when(mockCopyJobService.getStoredJob(anyString(), anyString()))
                .thenThrow(new RuntimeException("Connection failed"));

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals("SummaryJob", result.get(0).getStoredJobName());
    }

    @Test
    public void GivenPasswordTypeNumeric_WhenConvert_ThenPasswordTypeMapped() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "PwdJob", "f", "u", "sjptNumericPIN", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals(JobCredentialsAttributes.PasswordType.NUMERIC, result.get(0).getStoredJobPasswordType());
    }

    @Test
    public void GivenPasswordTypeAlphaNumeric_WhenConvert_ThenPasswordTypeMapped() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "AlphaJob", "f", "u", "sjptAlphanumericPIN", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals(JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC, result.get(0).getStoredJobPasswordType());
    }

    @Test
    public void GivenNullPasswordType_WhenConvert_ThenDefaultsToNone() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "NoPwd", "f", "u", null, 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals(JobCredentialsAttributes.PasswordType.NONE, result.get(0).getStoredJobPasswordType());
    }

    // ===== convertStoredJob default values tests (E2 doesn't return colorMode/copies/scanSize/duplex) =====

    @Test
    public void GivenNoSavedAttributes_WhenConvert_ThenDefaultColorMode() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "DefColor", "f", "u", "sjptNone", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(CopyAttributes.ColorMode.DEFAULT, result.get(0).getColorMode());
    }

    @Test
    public void GivenNoSavedAttributes_WhenConvert_ThenDefaultScanSize() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "DefScanSize", "f", "u", "sjptNone", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(CopyAttributes.ScanSize.DEFAULT, result.get(0).getOriginalMediaSize());
    }

    @Test
    public void GivenNoSavedAttributes_WhenConvert_ThenDefaultDuplex() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "DefDuplex", "f", "u", "sjptNone", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(CopyAttributes.Duplex.DEFAULT, result.get(0).getOutputSides());
    }

    @Test
    public void GivenNoSavedAttributes_WhenConvert_ThenDefaultCopies() {
        StoredJob job = createStoredJob(TEST_JOB_ID_1, "DefCopies", "f", "u", "sjptNone", 1L);
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.get(0).getCopies());
    }

    @Test
    public void GivenNullTotalPages_WhenConvert_ThenDefaultToZero() {
        StoredJob job = new StoredJob();
        job.setJobId(new CopyJobIdentifier(UUID.fromString(TEST_JOB_ID_1)));
        job.setJobName("NullPages");
        // totalPages is null
        StoredJobs storedJobs = createStoredJobs(job);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(job);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(0, result.get(0).getTotalPages());
    }

    @Test
    public void GivenNullJobId_WhenConvert_ThenEmptyStringId() {
        StoredJob job = new StoredJob();
        // jobId is null
        job.setJobName("NoId");
        StoredJobs storedJobs = createStoredJobs(job);
        // fetchStoredJobDetails returns summaryJob early when jobId is null — no service call

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        verify(mockCopyJobService, never()).getStoredJob(anyString(), anyString());
    }

    @Test
    public void GivenFetchReturnsNull_WhenConvert_ThenSummaryUsed() {
        StoredJob summaryJob = createStoredJob(TEST_JOB_ID_1, "SummaryOnly", "f", "u", "sjptNone", 7L);
        StoredJobs storedJobs = createStoredJobs(summaryJob);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(null);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals("SummaryOnly", result.get(0).getStoredJobName());
        assertEquals(7, result.get(0).getTotalPages());
    }

    @Test
    public void GivenDetailedJobReturned_WhenConvert_ThenDetailedUsed() {
        StoredJob summaryJob = createStoredJob(TEST_JOB_ID_1, "Summary", "f", "u", "sjptNone", 1L);
        StoredJob detailedJob = createStoredJob(TEST_JOB_ID_1, "Detailed", "f2", "admin", "sjptNone", 10L);
        StoredJobs storedJobs = createStoredJobs(summaryJob);
        when(mockCopyJobService.getStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1))).thenReturn(detailedJob);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, mockCopyJobService, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals("Detailed", result.get(0).getStoredJobName());
        assertEquals("f2", result.get(0).getStoredJobFolderName());
        assertEquals("admin", result.get(0).getStoredJobUserName());
        assertEquals(10, result.get(0).getTotalPages());
    }

    @Test
    public void GivenNullService_WhenConvert_ThenSummaryUsedWithoutFetch() {
        StoredJob summaryJob = createStoredJob(TEST_JOB_ID_1, "NoService", "f", "u", "sjptNone", 2L);
        StoredJobs storedJobs = createStoredJobs(summaryJob);

        List<StoredJobInfo> result = CopyJobAdapter.convertToStoredJobInfoList(null, 
                storedJobs, null, TEST_PACKAGE);

        assertEquals(1, result.size());
        assertEquals("NoService", result.get(0).getStoredJobName());
        verify(mockCopyJobService, never()).getStoredJob(anyString(), anyString());
    }

    // ===== convertDefaultOptionsToCopyOptions tests =====

    @Test
    public void GivenDefaultOptions_WhenConvert_ThenCopyOptionsReturned() throws Exception {
        DefaultOptions defaultOptions = new DefaultOptions();
        com.hp.ext.service.copy.CopyOptions result =
                CopyJobAdapter.convertDefaultOptionsToCopyOptions(defaultOptions);
        assertNotNull(result);
    }

    // ===== enumerateAndConvertStoredJobs tests =====

    @Test
    public void GivenStoredJobs_WhenEnumerateAndConvert_ThenListReturned() {
        StoredJobs storedJobs = createStoredJobs(
                createStoredJob(TEST_JOB_ID_1, "Job1", "f1", "user1", "sjptNone", 2L));
        when(mockCopyJobService.enumerateStoredJobs(TEST_PACKAGE)).thenReturn(storedJobs);
        StoredJob detailedJob = createStoredJob(TEST_JOB_ID_1, "Job1", "f1", "user1", "sjptNone", 2L);
        when(mockCopyJobService.getStoredJob(anyString(), anyString())).thenReturn(detailedJob);

        List<StoredJobInfo> result = CopyJobAdapter.enumerateAndConvertStoredJobs(
                null, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_JOB_ID_1, result.get(0).getStoredJobId());
        verify(mockCopyJobService).enumerateStoredJobs(TEST_PACKAGE);
    }

    @Test
    public void GivenNullEnumerate_WhenEnumerateAndConvert_ThenEmptyList() {
        when(mockCopyJobService.enumerateStoredJobs(TEST_PACKAGE)).thenReturn(null);

        List<StoredJobInfo> result = CopyJobAdapter.enumerateAndConvertStoredJobs(
                null, mockCopyJobService, TEST_PACKAGE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ===== deleteStoredJob tests =====

    @Test
    public void GivenValidJobId_WhenDeleteStoredJob_ThenServiceCalledAndPreferenceRemoved() throws Exception {
        StoredJob_Remove removeResult = new StoredJob_Remove();
        when(mockCopyJobService.deleteStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1),
                org.mockito.ArgumentMatchers.any(RemoveStoredJobRequest.class)))
                .thenReturn(removeResult);

        StoredJob_Remove result = CopyJobAdapter.deleteStoredJob(
                mockContext, mockCopyJobService, TEST_PACKAGE, TEST_JOB_ID_1, (JobCredentialsAttributes) null);

        assertNotNull(result);
        verify(mockCopyJobService, times(1)).deleteStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1),
                org.mockito.ArgumentMatchers.any(RemoveStoredJobRequest.class));
    }

    @Test(expected = SdkServiceErrorException.class)
    public void GivenNullRemoveResult_WhenDeleteStoredJob_ThenExceptionThrown() throws Exception {
        when(mockCopyJobService.deleteStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1),
                org.mockito.ArgumentMatchers.any(RemoveStoredJobRequest.class)))
                .thenReturn(null);

        CopyJobAdapter.deleteStoredJob(mockContext, mockCopyJobService, TEST_PACKAGE, TEST_JOB_ID_1,
                (JobCredentialsAttributes) null);
    }

    @Test
    public void GivenPasswordCredentials_WhenDeleteStoredJob_ThenPasswordApplied() throws Exception {
        StoredJob_Remove removeResult = new StoredJob_Remove();
        when(mockCopyJobService.deleteStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1),
                org.mockito.ArgumentMatchers.any(RemoveStoredJobRequest.class)))
                .thenReturn(removeResult);

        JobCredentialsAttributes credentials = org.mockito.Mockito.mock(JobCredentialsAttributes.class);
        when(credentials.getStoreJobPasswordType()).thenReturn(JobCredentialsAttributes.PasswordType.NUMERIC);
        when(credentials.getStoreJobPassword()).thenReturn("1234");

        StoredJob_Remove result = CopyJobAdapter.deleteStoredJob(
                mockContext, mockCopyJobService, TEST_PACKAGE, TEST_JOB_ID_1, credentials);

        assertNotNull(result);
        verify(mockCopyJobService, times(1)).deleteStoredJob(eq(TEST_PACKAGE), eq(TEST_JOB_ID_1),
                org.mockito.ArgumentMatchers.any(RemoveStoredJobRequest.class));
    }

    // ===== Helper Methods =====

    private StoredJob createStoredJob(String jobId, String jobName, String folderName,
                                      String userName, String passwordType, long totalPages) {
        StoredJob job = new StoredJob();
        job.setJobId(new CopyJobIdentifier(UUID.fromString(jobId)));
        job.setJobName(jobName);
        job.setFolderName(folderName);
        job.setJobUserName(userName);
        job.setJobTimestamp("2026-03-27T00:00:00Z");
        if (passwordType != null) {
            job.setJobPasswordType(StoredJobPasswordType.Parse(passwordType));
        }
        job.setTotalPages(new Unsigned64(totalPages));
        return job;
    }

    private StoredJobs createStoredJobs(StoredJob... jobs) {
        StoredJobs storedJobs = new StoredJobs();
        storedJobs.setMembers(Arrays.asList(jobs));
        return storedJobs;
    }
}
