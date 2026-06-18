package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.DestinationOptions;
import com.hp.ext.service.scanJob.HttpDestination;
import com.hp.ext.service.scanJob.HttpOptions;
import com.hp.ext.service.scanJob.NetworkFolderOptions;
import com.hp.ext.service.scanJob.ProtocolOptions;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Binding;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Value;
import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.service.scanJob.SmbProtocolOptions;
import com.hp.ext.service.scanJob.SmbProtocolOptions_UncPath_Binding;
import com.hp.ext.service.scanJob.SmbProtocolOptions_UncPath_Value;
import com.hp.ext.types.imaging.AutoColorDetectMode;
import com.hp.ext.types.imaging.BinaryRenderingType;
import com.hp.ext.types.network.NetworkFolderProtocol;
import com.hp.ext.types.network.UncPath;
import com.hp.ext.types.security.CredentialSourceType;
import com.hp.ext.types.security.UserCredential;
import com.hp.ext.types.security.UserCredential_Domain_Binding;
import com.hp.ext.types.security.UserCredential_Domain_Value;
import com.hp.ext.types.security.UserCredential_Password_Binding;
import com.hp.ext.types.security.UserCredential_Password_Value;
import com.hp.ext.types.security.UserCredential_UserName_Binding;
import com.hp.ext.types.security.UserCredential_UserName_Value;
import com.hp.ext.types.target.HttpPath;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Binding;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Value;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Binding;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Expression;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Value;
import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.TestInfra;
import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceScanJobServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    private static final String testPackageName = DutInfo.PI_TEST_PACKAGE_NAME;
    private static final String testScanJobAgentId = DutInfo.PI_TEST_SCAN_AGENT_ID;
    private static final String testScanSolutionId = DutInfo.PI_TEST_SOLUTION_ID;
    private static final String testApplicationId = DutInfo.PI_TEST_APPLICATION_AGENT_ID;

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);

        //3. call isSupported() : get E2 scanjob capabilities from the connected device simulator
        boolean supported = scanJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetScannerStatusCalled_ThenScannerShouldBeReturned() {
        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);

        //3. call isSupported() : get E2 scanjob capabilities from the connected device simulator
        Scanner scanner = scanJobService.getScannerStatus();
        assertNotNull(scanner);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetDefaultOptionsCalled_AfterDeviceManagementServiceInitiated_ThenDefaultOptionsShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);

        //3. get default scan options from the connected device simulator
        DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);

        //4. verify received scan default options
        assertNotNull(defScanOptions);
        assertNull(defScanOptions.getEmail());
        assertNotNull(defScanOptions.getHttp());
        assertEquals(defScanOptions.getHttp().getAutoColorDetect(), AutoColorDetectMode.AcdmDetectOnly);
        assertEquals(defScanOptions.getHttp().getAutoDeskew(), false);
        assertEquals(defScanOptions.getHttp().getAutoExposure(), false);
        assertEquals(defScanOptions.getHttp().getBackgroundCleanup().toString(), "2");
        assertEquals(defScanOptions.getHttp().getBackgroundColorRemoval(), false);
        assertEquals(defScanOptions.getHttp().getBackgroundColorRemovalLevel().toString(), "0");
        assertEquals(defScanOptions.getHttp().getBackgroundNoiseRemoval(), false);
        assertEquals(defScanOptions.getHttp().getBinaryRendering(), BinaryRenderingType.BrHalftone);
    }

    // Replaced with API test
    // @Test
    // pre-requisite:
    //  api test app (tests\api\apiTest\bdl\Test-WorkpathAPIs-debug.bdl) should be installed on the target or simulator
    public void GivenStandardDeviceScanJobService_WhenCreateScanJobCalled_WithDefaultOption_ThenScanJobShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //define mocked PackageManagerHelper behavior to return expected testScanJobAgentId, and testScanSolutionId
        PackageManagerHelper mockPmHelper = Mockito.mock(PackageManagerHelper.class);
        Mockito.when(mockPmHelper.getAgentId(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString())).thenReturn(testScanJobAgentId);
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(testScanSolutionId);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());

        StandardWebsocketCallbackService.start(ApplicationProvider.getApplicationContext(), true);

        //launch test scan sample app
        TestConnector testConn = new TestConnector();
        try {
            testConn.getUdwClient(deviceIp).closeCurrentApp();
            Thread.sleep(1000 * 2);
            testConn.getUdwClient(deviceIp).initiateAppLaunch(testApplicationId);
            Thread.sleep(1000 * 2);
        } catch (Exception e) {
            fail("Exception :" + e.getMessage());
        }

        //2. create scan job service object
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);

        //3. get default scan options from the connected device simulator
        DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);

        //4. create scan job ticket
        ScanJob_Create scanJobCreate = new ScanJob_Create();
        ScanTicket scanTicket = createScanTicketWithHttpDestination(
                defScanOptions.getHttp(),
                TestInfra.HTTP_SERVER_IP + ":" + TestInfra.HTTP_SERVER_PORT,
                TestInfra.HTTP_SERVER_PATH);
        scanJobCreate.setScanTicket(scanTicket);

        ScanOptions_FileName_Value fileNameValue = new ScanOptions_FileName_Value();
        fileNameValue.setExplicitValue("testScanJob");
        ScanOptions_FileName_Binding filename = new ScanOptions_FileName_Binding(fileNameValue);
        scanJobCreate.getScanTicket().getScanOptions().setFileName(filename);

        //5. create scan job with default options
        ScanJob scanJob = scanJobService.createScanJob(testPackageName, scanJobCreate);
        assertNotNull(scanJob);
        String jobId = scanJob.getScanJobId().toString();
        assertNotNull(jobId);
        assertFalse(jobId.isEmpty());

        try {
            testConn.getUdwClient(deviceIp).closeCurrentApp();
        } catch (Exception ignored) {
        }
    }

    // Replaced with API test
    // @Test
    // pre-requisite:
    //  api test app (tests\api\apiTest\bdl\Test-WorkpathAPIs-debug.bdl) should be installed on the target or simulator
    public void GivenStandardDeviceScanJobService_WhenCreateScanJobCalledWithNetworkFolderDestination_ThenScanJobShouldBeReturned() {
        try {
            //1. initialize device management service : try to connect to the device simulator
            StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

            //define mocked PackageManagerHelper behavior to return expected testScanJobAgentId, and testScanSolutionId
            PackageManagerHelper mockPmHelper = Mockito.mock(PackageManagerHelper.class);
            Mockito.when(mockPmHelper.getAgentId(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString())).thenReturn(testScanJobAgentId);
            Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(testScanSolutionId);
            StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
            StandardDeviceManagementService.getInstance().setApplicationContext(ApplicationProvider.getApplicationContext());

            StandardWebsocketCallbackService.start(ApplicationProvider.getApplicationContext(), true);

            //launch test scan sample app
            TestConnector testConn = new TestConnector();
            try {
                testConn.getUdwClient(deviceIp).closeCurrentApp();
                Thread.sleep(1000 * 2);
                testConn.getUdwClient(deviceIp).initiateAppLaunch(testApplicationId);
                Thread.sleep(1000 * 2);
            } catch (Exception e) {
                fail("Exception :" + e.getMessage());
            }

            //2. create scan job service object
            StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
            assertNotNull(scanJobService);

            //3. get default scan options from the connected device simulator
            DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);
            ScanOptions netFolderScanOptions = defScanOptions.getNetworkFolder();
            assertNotNull(netFolderScanOptions);

            //4. create scan job ticket
            ScanJob_Create scanJobCreate = new ScanJob_Create();

            String path = "\\\\" + TestInfra.NETWORK_FOLDER_SERVER_IP + "\\" + TestInfra.NETWORK_FOLDER_PATH;
            ScanTicket scanTicket = createScanTicketWithNetworkFolderDestination(
                    netFolderScanOptions,
                    TestInfra.NETWORK_FOLDER_DOMAIN,
                    TestInfra.NETWORK_FOLDER_USERNAME,
                    TestInfra.NETWORK_FOLDER_PASSWORD,
                    path);
            scanJobCreate.setScanTicket(scanTicket);

            ScanOptions_FileName_Value fileNameValue = new ScanOptions_FileName_Value();
            fileNameValue.setExplicitValue("testScanJob");
            ScanOptions_FileName_Binding filename = new ScanOptions_FileName_Binding(fileNameValue);
            scanJobCreate.getScanTicket().getScanOptions().setFileName(filename);

            //5. create scan job with default options
            ScanJob scanJob = scanJobService.createScanJob(testPackageName, scanJobCreate);
            assertNotNull(scanJob);
            String jobId = scanJob.getScanJobId().toString();
            assertNotNull(jobId);
            assertFalse(jobId.isEmpty());
        } finally {
            try {
                testConn.getUdwClient(deviceIp).closeCurrentApp();
            } catch (Exception ignored) {
            }
        }
    }

    private ScanTicket createScanTicketWithHttpDestination(ScanOptions options, String hostName, String httpPath) {
        //host
        HttpStyleHostCommon_Host_Expression expression = new HttpStyleHostCommon_Host_Expression();
        //expression.setExpressionPattern("$SOLUTION_CONTEXT(HOST)$");
        HttpStyleHostCommon_Host_Binding host = new HttpStyleHostCommon_Host_Binding();
        host.setExpression(expression);
        HttpStyleHostCommon_Host_Value explicitHost = new HttpStyleHostCommon_Host_Value();
        com.hp.ext.types.target.HostName explicitValueHost = new com.hp.ext.types.target.HostName(hostName);
        explicitHost.setExplicitValue(explicitValueHost);
        host.setExplicit(explicitHost);

        //path
        HttpPath explicitValue = new HttpPath(httpPath);
        HttpStyleClientCommon_Path_Value explicit = new HttpStyleClientCommon_Path_Value();
        explicit.setExplicitValue(explicitValue);
        HttpStyleClientCommon_Path_Binding path = new HttpStyleClientCommon_Path_Binding();
        path.setExplicit(explicit);

        //DestinationOptions
        HttpDestination destination = new HttpDestination();
        destination.setPath(path);
        destination.setHost(host);
        destination.setScheme("http");
        HttpOptions http = new HttpOptions();
        http.setDestination(destination);
        DestinationOptions destinationOptions = new DestinationOptions();
        destinationOptions.setHttp(http);

        //ScanTicket
        ScanTicket scanTicket = new ScanTicket();
        scanTicket.setDestinationOptions(destinationOptions);
        scanTicket.setScanOptions(options);
        return scanTicket;
    }

    private ScanTicket createScanTicketWithNetworkFolderDestination(ScanOptions options, String domain, String username, String password, String path) {
        ScanTicket scanTicket = new ScanTicket();
        scanTicket.setScanOptions(options);

        NetworkFolderOptions networkFolderOptions = new NetworkFolderOptions();

        // Credential
        UserCredential userCredential = new UserCredential();
        UserCredential_UserName_Binding usernameBinding = new UserCredential_UserName_Binding();
        UserCredential_UserName_Value userNameExplicit = new UserCredential_UserName_Value();
        userNameExplicit.setExplicitValue(username);
        usernameBinding.setExplicit(userNameExplicit);
        userCredential.setUserName(usernameBinding);

        UserCredential_Password_Binding passwordBinding = new UserCredential_Password_Binding();
        UserCredential_Password_Value passwordExplicit = new UserCredential_Password_Value();
        passwordExplicit.setExplicitValue(password);
        passwordBinding.setExplicit(passwordExplicit);
        userCredential.setPassword(passwordBinding);

        if (domain != null && !domain.isEmpty()) {
            UserCredential_Domain_Binding domainBinding = new UserCredential_Domain_Binding();
            UserCredential_Domain_Value domainExplicit = new UserCredential_Domain_Value();
            domainExplicit.setExplicitValue(domain);
            domainBinding.setExplicit(domainExplicit);
            userCredential.setDomain(domainBinding);
        } else {
            userCredential.setDomain(null);
        }
        networkFolderOptions.setCredential(userCredential);
        networkFolderOptions.setCredentialSource(null);

        // Credential Source
        networkFolderOptions.setCredentialSource(CredentialSourceType.CstProvided);

        // Protocol
        //networkFolderOptions.setProtocol(NetworkFolderProtocol.NfpSmb);
        ProtocolOptions protocolOptions = new ProtocolOptions();
        networkFolderOptions.setProtocolOptions(protocolOptions);

        SmbProtocolOptions smbProtocolOptions = new SmbProtocolOptions();
        SmbProtocolOptions_UncPath_Binding smbPathBinding = new SmbProtocolOptions_UncPath_Binding();
        SmbProtocolOptions_UncPath_Value smbPathValue = new SmbProtocolOptions_UncPath_Value();
        smbPathValue.setExplicitValue(new UncPath(path));
        smbPathBinding.setExplicit(smbPathValue);
        smbProtocolOptions.setUncPath(smbPathBinding);
        networkFolderOptions.getProtocolOptions().setSmbOptions(smbProtocolOptions);

        scanTicket.setDestinationOptions(new DestinationOptions(networkFolderOptions));
        return scanTicket;
    }
}
