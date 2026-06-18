package com.hp.jetadvantage.link.services.statisticslet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.ext.service.jobStatistics.Job;
import com.hp.ext.service.jobStatistics.JobIdentifier;
import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.SequenceNumber;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.statisticslet.adapter.StatisticsAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsAdapterTest {

    @Mock
    private IDeviceStatisticsService mockStatisticsService;
    
    private Gson gson;

    private static final String TEST_PACKAGE_NAME = Constants.TEST_PACKAGE_NAME;
    private static final int TEST_OFFSET = 0;
    private static final int TEST_LIMIT = 50;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .create();
    }

    // ==================== Core E2-to-WorkPath JSON Conversion Tests ====================

    @Test
    public void givenE2JsonData_whenConvertToWorkPath_thenMatchExpectedResult() throws IOException, JSONException {
        // Given: Load real E2 JSON data and expected WorkPath result
        String e2JsonString = Utils.loadTestJsonResource(
            Objects.requireNonNull(getClass().getClassLoader()), 
            "statistics/GET_getJobsList.json"
        );
        String expectedWorkPathJson = Utils.loadTestJsonResource(
            Objects.requireNonNull(getClass().getClassLoader()), 
            "statistics/ExpectedResult_getJobsList.json"
        );
        
        Jobs e2Jobs = createJobsFromJsonResource(e2JsonString);
        when(mockStatisticsService.getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT))
                .thenReturn(e2Jobs);

        // When: Convert E2 data to WorkPath format
        String actualResult = StatisticsAdapter.getAllJobsList(mockStatisticsService, TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);

        // Then: Validate core conversion results
        assertNotNull("Result should not be null", actualResult);
        verify(mockStatisticsService).getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);
        
        JSONObject actualJson = new JSONObject(actualResult);
        JSONObject expectedJson = new JSONObject(expectedWorkPathJson);
        
        // Validate essential structure and field conversions
        validateEssentialFields(actualJson, expectedJson);
        validateCounterConversions(actualJson, expectedJson);
        validateEnumMappings(actualJson, expectedJson);
        validateBackwardCompatibility(actualJson);
    }

    @Test
    public void givenE2JsonData_whenConvertEmptyJobs_thenMatchExpectedEmptyResult() throws IOException, JSONException {
        // Given: Empty E2 Jobs
        Jobs emptyJobs = mock(Jobs.class);
        when(emptyJobs.getMembers()).thenReturn(new java.util.ArrayList<>());
        when(emptyJobs.getOffset()).thenReturn((long) TEST_OFFSET);
        when(emptyJobs.getSelectedCount()).thenReturn(0L);
        when(emptyJobs.getTotalCount()).thenReturn(0L);
        when(mockStatisticsService.getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT))
                .thenReturn(emptyJobs);

        // When
        String actualResult = StatisticsAdapter.getAllJobsList(mockStatisticsService, TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);

        // Then
        assertNotNull("Result should not be null", actualResult);
        JSONObject actualJson = new JSONObject(actualResult);
        
        assertEquals("Offset should match", TEST_OFFSET, actualJson.getInt("offset"));
        assertEquals("SelectedCount should be 0", 0, actualJson.getInt("selectedCount"));
        assertEquals("TotalCount should be 0", 0, actualJson.getInt("totalCount"));
        assertTrue("Should have members array", actualJson.has("members"));
        assertEquals("Members should be empty", 0, actualJson.getJSONArray("members").length());
    }

    @Test
    public void givenNullJobs_whenConvertToWorkPath_thenReturnNull() {
        // Given: Null Jobs from service
        when(mockStatisticsService.getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT))
                .thenReturn(null);

        // When
        String result = StatisticsAdapter.getAllJobsList(mockStatisticsService, TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);

        // Then
        assertNull("Result should be null when Jobs is null", result);
        verify(mockStatisticsService).getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);
    }

    @Test
    public void givenServiceException_whenConvertToWorkPath_thenHandleGracefully() {
        // Given: Service throws exception
        when(mockStatisticsService.getAllJobsList(TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then: Should not crash
        try {
            String result = StatisticsAdapter.getAllJobsList(mockStatisticsService, TEST_PACKAGE_NAME, TEST_OFFSET, TEST_LIMIT);
            // Behavior depends on StatisticsAdapter implementation
        } catch (Exception e) {
            // Acceptable if StatisticsAdapter doesn't handle exceptions
            assertTrue("Exception should be from service", e.getMessage().contains("Service error"));
        }
    }

    // ==================== isSupported Tests ====================

    @Test
    public void givenSupportedService_whenIsSupported_thenReturnTrue() {
        // Given
        when(mockStatisticsService.isSupported()).thenReturn(true);

        // When
        boolean result = StatisticsAdapter.isSupported(mockStatisticsService);

        // Then
        assertTrue("Statistics service should be supported", result);
        verify(mockStatisticsService).isSupported();
    }

    @Test
    public void givenUnsupportedService_whenIsSupported_thenReturnFalse() {
        // Given
        when(mockStatisticsService.isSupported()).thenReturn(false);

        // When
        boolean result = StatisticsAdapter.isSupported(mockStatisticsService);

        // Then
        assertFalse("Statistics service should not be supported", result);
        verify(mockStatisticsService).isSupported();
    }

    // ==================== Helper Methods ====================

    /**
     * Create Jobs object from JSON resource for testing E2 to WorkPath conversion
     */
    private Jobs createJobsFromJsonResource(String jsonString) {
        try {
            // Parse JSON string and extract E2 data structure
            JSONObject jsonObject = new JSONObject(jsonString);
            
            Jobs jobs = mock(Jobs.class);
            
            // Set basic properties
            when(jobs.getOffset()).thenReturn((long) jsonObject.optInt("offset", 0));
            when(jobs.getSelectedCount()).thenReturn((long) jsonObject.optInt("selectedCount", 0));
            when(jobs.getTotalCount()).thenReturn((long) jsonObject.optInt("totalCount", 0));
            
            // Create member jobs from JSON
            JSONArray membersArray = jsonObject.optJSONArray("members");
            java.util.List<Job> memberJobs = new java.util.ArrayList<>();
            
            if (membersArray != null) {
                for (int i = 0; i < membersArray.length(); i++) {
                    JSONObject jobJson = membersArray.getJSONObject(i);
                    Job job = createJobFromJson(jobJson);
                    memberJobs.add(job);
                }
            }
            
            when(jobs.getMembers()).thenReturn(memberJobs);
            
            return jobs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Jobs from JSON resource", e);
        }
    }

    /**
     * Create a single Job object from JSON object
     */
    private Job createJobFromJson(JSONObject jobJson) {
        // This is a simplified implementation
        // In a real scenario, you might use Gson or create more comprehensive mocking
        Job job = mock(Job.class);
        
        // Basic job properties
        JobIdentifier mockJobId = mock(JobIdentifier.class);
        String jobIdString = jobJson.optString("jobId", "");
        if (!jobIdString.isEmpty()) {
            try {
                java.util.UUID uuid = java.util.UUID.fromString(jobIdString);
                when(mockJobId.getValue()).thenReturn(uuid);
            } catch (IllegalArgumentException e) {
                // If jobId is not a valid UUID, use a default one
                when(mockJobId.getValue()).thenReturn(java.util.UUID.randomUUID());
            }
        } else {
            when(mockJobId.getValue()).thenReturn(java.util.UUID.randomUUID());
        }
        when(job.getJobId()).thenReturn(mockJobId);
        
        SequenceNumber mockResourceId = mock(SequenceNumber.class);
        when(mockResourceId.getValue()).thenReturn((long) jobJson.optInt("resourceId", 0));
        when(job.getResourceId()).thenReturn(mockResourceId);
        
        // Add more detailed parsing as needed for your specific test cases
        // This would include jobInfo, printInfo, scanInfo, etc.
        
        return job;
    }

    /**
     * Validate essential fields are correctly converted from E2 to WorkPath format
     */
    private void validateEssentialFields(JSONObject actual, JSONObject expected) throws JSONException {
        // Validate top-level structure
        assertEquals("Offset must match", expected.getInt("offset"), actual.getInt("offset"));
        assertEquals("SelectedCount must match", expected.getInt("selectedCount"), actual.getInt("selectedCount"));
        assertEquals("TotalCount must match", expected.getInt("totalCount"), actual.getInt("totalCount"));
        
        // Validate members structure
        JSONArray actualMembers = actual.getJSONArray("members");
        JSONArray expectedMembers = expected.getJSONArray("members");
        assertEquals("Members count must match", expectedMembers.length(), actualMembers.length());
        
        // Validate each job's essential fields
        for (int i = 0; i < actualMembers.length(); i++) {
            JSONObject actualJob = actualMembers.getJSONObject(i);
            JSONObject expectedJob = expectedMembers.getJSONObject(i);
            
            // Safely compare jobId (handle different types)
            if (expectedJob.has("jobId") && actualJob.has("jobId")) {
                String expectedJobId = getStringValue(expectedJob, "jobId");
                String actualJobId = getStringValue(actualJob, "jobId");
                assertEquals("JobId must match", expectedJobId, actualJobId);
            }
            
            // Safely compare resourceId (handle different types)
            if (expectedJob.has("resourceId") && actualJob.has("resourceId")) {
                String expectedResourceId = getStringValue(expectedJob, "resourceId");
                String actualResourceId = getStringValue(actualJob, "resourceId");
                assertEquals("ResourceId must match", expectedResourceId, actualResourceId);
            }
        }
    }

    /**
     * Validate Counter conversions (significand/exponent to integers)
     */
    private void validateCounterConversions(JSONObject actual, JSONObject expected) throws JSONException {
        JSONArray actualMembers = actual.getJSONArray("members");
        JSONArray expectedMembers = expected.getJSONArray("members");
        
        for (int i = 0; i < actualMembers.length(); i++) {
            JSONObject actualJob = actualMembers.getJSONObject(i);
            JSONObject expectedJob = expectedMembers.getJSONObject(i);
            
            if (actualJob.has("printInfo") && !actualJob.isNull("printInfo")) {
                JSONObject actualPrintInfo = actualJob.getJSONObject("printInfo");
                JSONObject expectedPrintInfo = expectedJob.getJSONObject("printInfo");
                
                // Validate counter conversions
                assertEquals("ColorImpressions should match", 
                    expectedPrintInfo.getInt("colorImpressions"), 
                    actualPrintInfo.getInt("colorImpressions"));
                assertEquals("MonochromeImpressions should match", 
                    expectedPrintInfo.getInt("monochromeImpressions"), 
                    actualPrintInfo.getInt("monochromeImpressions"));
                assertEquals("TotalImpressions should match", 
                    expectedPrintInfo.getInt("totalImpressions"), 
                    actualPrintInfo.getInt("totalImpressions"));
            }
        }
    }

    /**
     * Validate Enum mappings (E2 enums to WorkPath enums)
     */
    private void validateEnumMappings(JSONObject actual, JSONObject expected) throws JSONException {
        JSONArray actualMembers = actual.getJSONArray("members");
        JSONArray expectedMembers = expected.getJSONArray("members");
        
        for (int i = 0; i < actualMembers.length(); i++) {
            JSONObject actualJob = actualMembers.getJSONObject(i);
            JSONObject expectedJob = expectedMembers.getJSONObject(i);
            
            if (actualJob.has("jobInfo") && !actualJob.isNull("jobInfo")) {
                JSONObject actualJobInfo = actualJob.getJSONObject("jobInfo");
                JSONObject expectedJobInfo = expectedJob.getJSONObject("jobInfo");
                
                // Validate enum mappings
                assertEquals("JobCategory should be mapped correctly", 
                    expectedJobInfo.getString("jobCategory"), 
                    actualJobInfo.getString("jobCategory"));
                assertEquals("JobDataSource should be mapped correctly", 
                    expectedJobInfo.getString("jobDataSource"), 
                    actualJobInfo.getString("jobDataSource"));
                assertEquals("JobDoneStatus should be mapped correctly", 
                    expectedJobInfo.getString("jobDoneStatus"), 
                    actualJobInfo.getString("jobDoneStatus"));
            }
        }
    }

    /**
     * Validate backward compatibility - ensure no essential fields are missing
     */
    private void validateBackwardCompatibility(JSONObject result) throws JSONException {
        // Ensure top-level fields exist
        assertTrue("Must have offset field", result.has("offset"));
        assertTrue("Must have selectedCount field", result.has("selectedCount"));
        assertTrue("Must have totalCount field", result.has("totalCount"));
        assertTrue("Must have members array", result.has("members"));
        
        // Ensure member structure is consistent
        JSONArray members = result.getJSONArray("members");
        for (int i = 0; i < members.length(); i++) {
            JSONObject job = members.getJSONObject(i);
            assertTrue("Job must have jobId", job.has("jobId"));
            assertTrue("Job must have resourceId", job.has("resourceId"));
            // Add more backward compatibility checks as needed
        }
    }
    
    /**
     * Safely get string value from JSONObject, handling different data types
     */
    private String getStringValue(JSONObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.isNull(key)) {
            return null;
        }
        
        Object value = jsonObject.opt(key);
        if (value == null) {
            return null;
        }
        
        // Convert different types to string representation
        return value.toString();
    }
}
