package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceStatisticsJobServiceInstrumentedTest extends StandardDeviceInstrumentedTest {
    private static final String TAG = "StatisticsJobServiceTest";

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @After
    public void tearDown() {
        // Reset the singleton's PackageManagerHelper back to real instance
        // to prevent mock leakage to other test classes
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(new PackageManagerHelper());
    }

    @Test
    public void GivenStandardDeviceStatisticsJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceStatisticsService statisticsJobService = new StandardDeviceStatisticsService();
        assertNotNull(statisticsJobService);
    }

    @Test
    public void GivenStandardDeviceStatisticsJobService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {
        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create statistic job service object
        StandardDeviceStatisticsService statisticsJobService = new StandardDeviceStatisticsService();
        assertNotNull(statisticsJobService);
    }

    @Test
    public void GivenStandardDeviceStatisticsJobService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create statistic job service object
        StandardDeviceStatisticsService statisticsJobService = new StandardDeviceStatisticsService();
        assertNotNull(statisticsJobService);

        //3. call isSupported() : get E2 copyjob capabilities from the connected device simulator
        boolean supported = statisticsJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStatisticsService_WhenGetAllJobsListCalled_ThenReturnsJobsObject() {
        // Mock PackageManagerHelper to bypass androidTest permission restriction
        // (test APK lacks signature-level READ_PROVIDERS permission for PackageManager ContentProvider).
        // IMPORTANT: PI_TEST_STATISTICS_AGENT_ID must match the device's registered agent UUID.
        // If the test app is reinstalled, the agent UUID changes and DutInfo.java must be updated.
        PackageManagerHelper mockPmHelper = Mockito.mock(PackageManagerHelper.class);
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString()))
                .thenReturn(DutInfo.PI_TEST_SOLUTION_ID);
        Mockito.when(mockPmHelper.getAgentId(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(DutInfo.PI_TEST_STATISTICS_AGENT_ID);

        StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceStatisticsService statisticsJobService = new StandardDeviceStatisticsService();
        assertNotNull(statisticsJobService);

        Jobs jobs = statisticsJobService.getAllJobsList(DutInfo.PI_TEST_PACKAGE_NAME, 0, 50);
        assertNotNull("getAllJobsList returned null. Check if DutInfo.PI_TEST_STATISTICS_AGENT_ID matches the device", jobs);
    }

}
