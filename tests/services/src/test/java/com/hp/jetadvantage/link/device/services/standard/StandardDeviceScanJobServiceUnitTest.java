package com.hp.jetadvantage.link.device.services.standard;

import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelPayloadValue;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.DestinationOptions;
import com.hp.ext.service.scanJob.HttpDestination;
import com.hp.ext.service.scanJob.HttpOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Cancel;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Binding;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Value;
import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.imaging.AutoColorDetectMode;
import com.hp.ext.types.imaging.BinaryRenderingType;
import com.hp.ext.types.optionProfile.OptionRule;
import com.hp.ext.types.target.HttpPath;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Binding;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Value;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Binding;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Expression;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Value;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceScanJobServiceUnitTest extends StandardDeviceUnitTest{
    @Captor
    ArgumentCaptor<Request> requestCaptor;

    private StandardDeviceScanJobService scanJobService;

    @Test
    public void GivenStandardDeviceScanJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        assertNotNull(scanJobService);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetDefaultOptionsCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        // Error case test : when device not connected
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        assertNotNull(scanJobService);

        try {
            scanJobService.getDefaultOptions(testPackageName);
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenIsSupported_ThenTrueShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleCapabilities());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);

        boolean supported = scanJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetScannerStatus_ThenScannerShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleScanner());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);

        Scanner scanner = scanJobService.getScannerStatus();
        assertNotNull(scanner);
        assertEquals(false, scanner.getIsBusy().booleanValue());
        assertEquals(true, scanner.getIsOnline().booleanValue());
        assertEquals("true", scanner.getHasPaperInAdf().getValue());
        assertEquals("true", scanner.getHasPaperOnFlatbed().getValue());
        assertEquals("unknown", scanner.getAdfOutputBinIsFull().getValue());
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetDefaultOptionsCalled_AndDeviceConnected_ThenDefaultOptionShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleScanDefaultOptions());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);

        //verify returned scan default options
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

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetDefaultOptionsCalled_AndDeviceReturnNull_ThenNullOptionShouldBeReturned() throws IOException {
        // Error case test : when device return null data

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call DefaultOptions
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);

        //verify returned scan default options
        assertNull(defScanOptions);
    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenGetProfileCalled_AndDeviceConnected_ThenScanProfileShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);  //unnecessary stub?
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleProfile());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call getProfile
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        Profile scanProfile = scanJobService.getProfile(testPackageName);

        //verify returned scan profile
        assertNotNull(scanProfile);
        assertEquals(scanProfile.getBase().getDefinitions().get(0).getOptionName(), "scanProgressMode");
        assertEquals(scanProfile.getBase().getDefinitions().get(0).getIsAvailable(), true);
        List<OptionRule> scanProgressModeOptionRule = scanProfile.getBase().getDefinitions().get(0).getRules();
        assertNotNull(scanProgressModeOptionRule);
        assertTrue(scanProgressModeOptionRule.get(0).getPossibleValues().getOptionValues().contains("spmNone"));
        assertTrue(scanProgressModeOptionRule.get(0).getPossibleValues().getOptionValues().contains("spmStandard"));

    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenRegisterNotificationCallbackCalled_AndScanNotificationOccurs_ThenCallbackCalled() {
        //define test sample data
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String typeGUN = "com.hp.ext.service.scanJob.version.1.type.scanNotification";
        String packageId = "app1";
        String sampleScanJobNotification = "{\"jobNotification\":{\"scanJobId\":\"94b87ae5-0620-49a8-a31e-86a919edc5a9\",\"scanJobStatus\":{\"cancelingActivity\":null,\"jobActivity\":null,\"jobDoneStatus\":null,\"jobDoneStatusDetail\":null,\"processingActivity\":null,\"processingRestartCount\":null,\"scanningActivity\":null,\"totalImagesProcessed\":null,\"totalImagesScanned\":null,\"totalImagesTransmitted\":null,\"transmissionConsecutiveRetryCount\":null,\"transmissionRetryCount\":null,\"transmittingActivity\":null},\"solutionContext\":null}}";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //define test callback
        IE2PayloadCallback<ScanNotification> callback = new IE2PayloadCallback<ScanNotification>() {
            @Override
            public void onReceiveNotification(String appPackageId, ScanNotification notification) {
                callbackCount.incrementAndGet();

                //verify received scan job notification
                assertEquals(packageId, appPackageId);
                assertTrue(notification.isJobNotification());
                assertEquals("com.hp.ext.service.scanJob.version.1.type.scanNotification", notification.getTypeGUN());
                assertEquals("94b87ae5-0620-49a8-a31e-86a919edc5a9", notification.getJobNotification().getScanJobId().toString());
            }
        };

        //register callback
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        scanJobService.registerNotificationCallback(callback);

        //1. test for valid channel payload message for scan notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, StandardDeviceScanJobService.E2SERVICE_SCAN_JOB_CANONICAL_GUN));
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleScanJobNotification));

        //verify callback is called
        assertEquals(1, callbackCount.get());
        callbackCount.set(0);

        //2. test for invalid payload message for scan notification
        String invalidScanJobNotification = "{\"invalidNotification\":{\"scanJobId\":\"94b87ae5-0620-49a8-a31e-86a919edc5a9\",\"scanJobStatus\":{}}}";
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, invalidScanJobNotification));

        //verify callback is not called
        assertEquals(0, callbackCount.get());

        //3. test for invalid json payload message for scan notification
        String invalid2ScanJobNotification = "{\"jobNotification\":{\"scanJobId\":\"94b87ae5-0620-49a8-a31e-86a919edc5a9\",\"scanJobStatus\":";
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, invalid2ScanJobNotification));

        //verify callback is not called
        assertEquals(0, callbackCount.get());

        //4. unregister callback
        scanJobService.unRegisterNotificationCallback();

        //verify that unregistered callback should not be called on receiving message
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleScanJobNotification));
        assertEquals(0, callbackCount.get());
    }

//    @Test
//    public void testSampleScanJobNotification() throws JsonProcessingException {
//        ScanJobNotificationContent content = new ScanJobNotificationContent();
//        content.setScanJobId(new ScanJobIdentifier(java.util.UUID.randomUUID()));
//        content.setScanJobStatus(new ScanJobStatus());
//        ScanNotification scanNotification = new ScanNotification(content);
//        String scanNotificationJsonStr = new ObjectMapper().writeValueAsString(scanNotification);
//        assertEquals("", scanNotificationJsonStr);
//    }

    @Test
    public void GivenStandardDeviceScanJobService_WhenCreateScanJobCalled_AndDeviceConnected_ThenScanJobShouldBeCreated() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);  //unnecessary stub?
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockDeviceManagementService.getUiContextToken(Mockito.anyString())).thenReturn("testUiContextToken");
        when(mockDeviceManagementService.getAgentId(Mockito.anyString(), Mockito.anyString())).thenReturn("26c78c4c-b81b-4812-9e56-c8f8830589dd");
        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleScanDefaultOptions());

        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceScanJobService and call getProfile
        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);

        //get default scan options from the connected device simulator
        DefaultOptions defScanOptions = scanJobService.getDefaultOptions(testPackageName);

        String createScanJobResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/POST_ext_createScanJob.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(createScanJobResponse);

        //create scan job ticket
        ScanJob_Create scanJobCreate = new ScanJob_Create();
        HttpStyleHostCommon_Host_Expression expression = new HttpStyleHostCommon_Host_Expression();
        //expression.setExpressionPattern("$SOLUTION_CONTEXT(HOST)$");
        HttpStyleHostCommon_Host_Binding host = new HttpStyleHostCommon_Host_Binding();
        host.setExpression(expression);
        HttpPath explicitValue = new HttpPath("HTTPpages/file");
        HttpStyleClientCommon_Path_Value explicit = new HttpStyleClientCommon_Path_Value();
        explicit.setExplicitValue(explicitValue);
        HttpStyleClientCommon_Path_Binding path = new HttpStyleClientCommon_Path_Binding();
        path.setExplicit(explicit);

        HttpStyleHostCommon_Host_Value explicitHost = new HttpStyleHostCommon_Host_Value();
        com.hp.ext.types.target.HostName explicitValueHost = new com.hp.ext.types.target.HostName("15.26.148.134:8080");
        explicitHost.setExplicitValue(explicitValueHost);
        host.setExplicit(explicitHost);

        HttpDestination destination = new HttpDestination();
        destination.setPath(path);
        destination.setHost(host);
        destination.setScheme("http");
        HttpOptions http = new HttpOptions();
        http.setDestination(destination);
        DestinationOptions destinationOptions = new DestinationOptions();
        destinationOptions.setHttp(http);
        ScanTicket scanTicket = new ScanTicket();
        scanTicket.setDestinationOptions(destinationOptions);
        scanTicket.setScanOptions(defScanOptions.getHttp());
        scanJobCreate.setScanTicket(scanTicket);

        ScanOptions_FileName_Value fileNameValue = new ScanOptions_FileName_Value();
        fileNameValue.setExplicitValue("testScanJob");
        ScanOptions_FileName_Binding filename = new ScanOptions_FileName_Binding(fileNameValue);
        scanJobCreate.getScanTicket().getScanOptions().setFileName(filename);

        //create scan job with default options
        ScanJob scanJob = scanJobService.createScanJob(testPackageName, scanJobCreate);
        assertNotNull(scanJob);
        String jobId = scanJob.getScanJobId().toString();
        assertEquals("afc620c8-c37c-4850-81c4-e3fc3330c883", jobId);

    }

    /**
     * Cancel ScanJob test case to verify cancel request message
     */
    @Test
    public void GivenStandardDeviceScanJobService_WhenCancelScanJobCalled_AndDeviceConnected_ThenPostCancelRequestWithoutBody() throws IOException {
        String agentId = "2f6c980d-c8db-468f-9965-8f26f651fc03";
        String jobId = "e3736199-5163-4c47-88d3-572e4e06dd6a";

        //define mock behavior for the test scenario
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockDeviceManagementService.getSolutionToken(Mockito.anyString())).thenReturn("testSolutionToken");
        when(mockDeviceManagementService.getAgentId(Mockito.anyString(), Mockito.anyString())).thenReturn(agentId);
        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        String cancelScanJobResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "scanJob/POST_ext_cancel.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(cancelScanJobResponse);

        StandardDeviceScanJobService scanJobService = new StandardDeviceScanJobService(mockDeviceManagementService);
        ScanJob_Cancel scanJobCancel = scanJobService.cancelScanJob(testPackageName, jobId);
        assertNotNull(scanJobCancel);

        //verify cancel request message
        Mockito.verify(mockHttpClient).getResponseAsString(requestCaptor.capture());
        Request capturedRequest = requestCaptor.getValue();
        assertEquals("POST", capturedRequest.method());
        assertEquals("https://localhost/ext/scanJob/v1/scanJobAgents/" + agentId + "/scanJobs/" + jobId + "/cancel", capturedRequest.url().toString());
        assertEquals("Bearer testSolutionToken", capturedRequest.header("Authorization"));

        //verify that Content-Type should not be set in the request header, and request body should be empty in the request for the cancel POST request
        //otherwise it will cause 400 Bad Request
        assertNull(capturedRequest.header("Content-Type"));
        assertEquals(0, capturedRequest.body().contentLength());
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/scanJob/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/scanJob/v1/defaultOptions");
        link2.setRel("defaultOptions");
        links.add(link2);

        Link link3 = new Link();
        link3.setHref("/ext/scanJob/v1/profile");
        link3.setRel("profile");
        links.add(link3);

        Link link4 = new Link();
        link4.setHref("/ext/scanJob/v1/scanJobAgents");
        link4.setRel("scanJobAgents");
        links.add(link4);

        return links;
    }
    private List<Link> getDeviceLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/device/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/device/v1/scanner");
        link2.setRel("scanner");
        links.add(link2);

        Link link3 = new Link();
        link3.setHref("/ext/device/v1/email");
        link3.setRel("email");
        links.add(link3);

        Link link4 = new Link();
        link4.setHref("/ext/device/v1/printEngine");
        link4.setRel("printEngine");
        links.add(link4);

        Link link5 = new Link();
        link5.setHref("/ext/device/v1/alerts");
        link5.setRel("alerts");
        links.add(link5);

        Link link6 = new Link();
        link6.setHref("/ext/device/v1/identity");
        link6.setRel("identity");
        links.add(link6);

        Link link7 = new Link();
        link7.setHref("/ext/device/v1/status");
        link7.setRel("status");
        links.add(link7);

        return links;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl scanServiceMetadata = new ServiceMetadataImpl();
        scanServiceMetadata.setDescription("Scan Job Service Client Tests Discovery Tree");
        scanServiceMetadata.setServiceGun("com.hp.ext.service.scanJob.version.1");
        scanServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(scanServiceMetadata);

        ServiceMetadataImpl deviceServiceMetadata = new ServiceMetadataImpl();
        deviceServiceMetadata.setDescription("APIs to acquire basic information about the entire device.");
        deviceServiceMetadata.setServiceGun("com.hp.ext.service.device.version.1");
        deviceServiceMetadata.setLinks(getDeviceLinks());
        serviceMetadatas.add(deviceServiceMetadata);

        return serviceMetadatas;
    }

    private String getSampleScanDefaultOptions() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"http\": {\n" +
                "    \"autoColorDetect\": \"acdmDetectOnly\",\n" +
                "    \"autoDeskew\": false,\n" +
                "    \"autoExposure\": false,\n" +
                "    \"backgroundCleanup\": 2,\n" +
                "    \"backgroundColorRemoval\": false,\n" +
                "    \"backgroundColorRemovalLevel\": 0,\n" +
                "    \"backgroundNoiseRemoval\": false,\n" +
                "    \"binaryRendering\": \"brHalftone\",\n" +
                "    \"blackBackground\": false,\n" +
                "    \"blackEnhancementLevel\": 60,\n" +
                "    \"blankPageSuppression\": false,\n" +
                "    \"ccdChannel\": \"cctGrayEmulated\",\n" +
                "    \"colorAndGrayCompression\": \"ctJpeg\",\n" +
                "    \"colorMode\": \"cmColor\",\n" +
                "    \"colorRange\": 0,\n" +
                "    \"colorSensitivity\": 0,\n" +
                "    \"compressionFactor\": 0,\n" +
                "    \"contentOrientation\": \"coPortrait\",\n" +
                "    \"contentType\": \"dctMixed\",\n" +
                "    \"contrast\": 1,\n" +
                "    \"descreen\": false,\n" +
                "    \"edgeToEdgeScan\": false,\n" +
                "    \"encryptionPassword\": \"\",\n" +
                "    \"exposureLevel\": 5,\n" +
                "    \"fileName\": {\n" +
                "      \"explicit\": {\n" +
                "        \"explictValue\": \"scan\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"fileTransmissionMode\": \"tmJob\",\n" +
                "    \"gamma\": 100,\n" +
                "    \"highlightLevel\": 1,\n" +
                "    \"imagePreviewMode\": \"ipmNone\",\n" +
                "    \"invertColors\": false,\n" +
                "    \"longPlotScan\": false,\n" +
                "    \"mediaBindingFormat\": \"bfFlipLeft\",\n" +
                "    \"mediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "    \"mediaSource\": \"miAdf\",\n" +
                "    \"mediaType\": \"mtPlain\",\n" +
                "    \"misfeedDetection\": false,\n" +
                "    \"monoCompression\": \"ctAuto\",\n" +
                "    \"outputCanvasAnchor\": \"ocaTopLeft\",\n" +
                "    \"outputCanvasMediaSize\": \"msAny\",\n" +
                "    \"outputCanvasMediaSource\": \"moAuto\",\n" +
                "    \"outputCanvasOrientation\": \"coPortrait\",\n" +
                "    \"outputCanvasXExtent\": 0,\n" +
                "    \"outputCanvasYExtent\": 0,\n" +
                "    \"outputFileCompression\": false,\n" +
                "    \"outputFileEncryption\": false,\n" +
                "    \"outputFileFormat\": \"ffJpeg\",\n" +
                "    \"outputQualityVsSize\": \"qvsMedium\",\n" +
                "    \"pagesPerSheet\": \"ppsOneUp\",\n" +
                "    \"plexMode\": \"pmSimplex\",\n" +
                "    \"resolution\": \"dpi300\",\n" +
                "    \"scanCaptureMode\": \"scmStandard\",\n" +
                "    \"scanProgressMode\": \"spmNone\",\n" +
                "    \"scanningSpeed\": \"ssAuto\",\n" +
                "    \"shadowLevel\": 0,\n" +
                "    \"sharpness\": 1,\n" +
                "    \"threshold\": 0,\n" +
                "    \"xOffset\": 0,\n" +
                "    \"yOffset\": 0\n" +
                "  },\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/defaultOptions\",\n" +
                "      \"rel\": \"self\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private String getSampleProfile() {
        return "{\n" +
                "  \"base\": {\n" +
                "    \"definitions\": [\n" +
                "      {\n" +
                "        \"optionName\": \"scanProgressMode\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"spmNone\",\n" +
                "                \"spmStandard\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"scanCaptureMode\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"scmStandard\",\n" +
                "                \"scmJobBuild\",\n" +
                "                \"scmBook\",\n" +
                "                \"scmIdCard\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"imagePreviewMode\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ipmNone\",\n" +
                "                \"ipmStandard\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"autoColorDetect\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"acdmDetectOnly\",\n" +
                "                \"acdmTreatNonColorAsMonochrome\",\n" +
                "                \"acdmTreatNonColorAsGrayscale\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"colorMode\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"cmMonochrome\",\n" +
                "                \"cmColor\",\n" +
                "                \"cmGrayscale\"\n" +
                "              ]\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"validValues\": {\n" +
                "              \"message\": \"JPEG file format does not support Monochrome color mode.\",\n" +
                "              \"optionValues\": [\n" +
                "                \"cmColor\",\n" +
                "                \"cmGrayscale\"\n" +
                "              ],\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"optionName\": \"outputFileFormat\",\n" +
                "                  \"optionValue\": \"ffJpeg\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"mediaSource\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"miAuto\",\n" +
                "                \"miFlatbed\",\n" +
                "                \"miAdf\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"mediaSize\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"msAny\",\n" +
                "                \"msANSI_A_8point5x11in\",\n" +
                "                \"msLegal_8point5x14in\",\n" +
                "                \"msExecutive_7point25x10point5in\",\n" +
                "                \"msInvoice_5point5x8point5in\",\n" +
                "                \"msANSI_B_11x17in\",\n" +
                "                \"msGeneral_5x7in\",\n" +
                "                \"msGeneral_5x8in\",\n" +
                "                \"msFoolscap_8point5x13in\",\n" +
                "                \"msOficio_216x340mm\",\n" +
                "                \"msISO_A3_297x420mm\",\n" +
                "                \"msISO_A4_210x297mm\",\n" +
                "                \"msISO_A5_148x210mm\",\n" +
                "                \"msISO_A6_105x148mm\",\n" +
                "                \"msJIS_B5_182x257mm\",\n" +
                "                \"msJIS_B6_128x182mm\",\n" +
                "                \"msK16_195x270mm\",\n" +
                "                \"msK16_184x260mm\",\n" +
                "                \"msK16_197x273mm\",\n" +
                "                \"msJDoublePostcard_148x200mm\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"xOffset\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 1000,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"yOffset\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 1000,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"plexMode\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"pmSimplex\",\n" +
                "                \"pmDuplex\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"resolution\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"dpi75\",\n" +
                "                \"dpi150\",\n" +
                "                \"dpi200\",\n" +
                "                \"dpi300\",\n" +
                "                \"dpi400\",\n" +
                "                \"dpi600\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"contentType\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"dctMixed\",\n" +
                "                \"dctPhoto\",\n" +
                "                \"dctText\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"contentOrientation\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"coPortrait\",\n" +
                "                \"coLandscape\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"mediaBindingFormat\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"bfFlipLeft\",\n" +
                "                \"bfFlipUp\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"blackBackground\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"mediaType\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"mtPlain\",\n" +
                "                \"mtPhoto\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"autoExposure\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"exposureLevel\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 1,\n" +
                "              \"upperBoundary\": 9,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"gamma\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 2000,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"highlightLevel\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 1,\n" +
                "              \"upperBoundary\": 10,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"colorSensitivity\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 100,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"colorRange\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 100,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"ccdChannel\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"cctNtsc\",\n" +
                "                \"cctGray\",\n" +
                "                \"cctGrayEmulated\",\n" +
                "                \"cctRed\",\n" +
                "                \"cctGreen\",\n" +
                "                \"cctBlue\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"binaryRendering\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"brHalftone\",\n" +
                "                \"brThreshold\",\n" +
                "                \"brErrorDiffusion\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"descreen\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"misfeedDetection\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"shadowLevel\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 100,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"compressionFactor\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 10,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"threshold\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 100,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"scanningSpeed\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ssAuto\",\n" +
                "                \"ssSlow\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"autoDeskew\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"edgeToEdgeScan\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"longPlotScan\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"invertColors\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"sharpness\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 1,\n" +
                "              \"upperBoundary\": 9,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"backgroundCleanup\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 100,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"contrast\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 8,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"blankPageSuppression\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"bpdDisabled\",\n" +
                "                \"bdpDetectAndSuppress\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"pagesPerSheet\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ppsOneUp\",\n" +
                "                \"ppsTwoUp\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"backgroundNoiseRemoval\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"backgroundColorRemoval\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"backgroundColorRemovalLevel\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"blackEnhancementLevel\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"range\": {\n" +
                "              \"lowerBoundary\": 0,\n" +
                "              \"upperBoundary\": 255,\n" +
                "              \"step\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputFileFormat\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ffJpeg\",\n" +
                "                \"ffPdf\",\n" +
                "                \"ffTiff\",\n" +
                "                \"ffMtiff\",\n" +
                "                \"ffPdfa\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputQualityVsSize\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"qvsLow\",\n" +
                "                \"qvsMedium\",\n" +
                "                \"qvsHigh\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputFileCompression\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"description\": \"supported with PDF/A only (basically the normal/high compression setting)\",\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"disable\": {\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.orOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"operators\": [\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffJpeg\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdf\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffTiff\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffMiff\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"monoCompression\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"description\": \"supported for TIFF/MTIFF when color is MONO. Supported values are Auto and G4\",\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"disable\": {\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.orOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"operators\": [\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffJpeg\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdf\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdfa\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"colorMode\",\n" +
                "                        \"optionValue\": \"cmColor\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"colorMode\",\n" +
                "                        \"optionValue\": \"cmGrayscale\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ctAuto\",\n" +
                "                \"ctG4\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"colorAndGrayCompression\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"description\": \"supported for TIFF/MTIFF when color is color/gray. Supported values are tiff60 and posttiff60. OJPEG is tiff6, JPEG is postTiff6\",\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"disable\": {\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.orOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"operators\": [\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffJpeg\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdf\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdfa\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.equalsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"colorMode\",\n" +
                "                        \"optionValue\": \"cmMonochrome\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"ctOJpeg\",\n" +
                "                \"ctJpeg\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputFileEncryption\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"disable\": {\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.notEqualsOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"optionName\": \"outputFileFormat\",\n" +
                "                  \"optionValue\": \"ffPdf\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"encryptionPassword\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"disable\": {\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.orOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"operators\": [\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.notEqualsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffPdf\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.notEqualsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileEncryption\",\n" +
                "                        \"optionValue\": \"true\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"transmissionMode\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"tmJob\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasMediaSize\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasMediaSource\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasXExtent\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasYExtent\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasAnchor\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"optionName\": \"outputCanvasOrientation\",\n" +
                "        \"isAvailable\": false,\n" +
                "        \"rules\": []\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"http\": {\n" +
                "    \"definitions\": [\n" +
                "      {\n" +
                "        \"optionName\": \"transmissionMode\",\n" +
                "        \"isAvailable\": true,\n" +
                "        \"rules\": [\n" +
                "          {\n" +
                "            \"possibleValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"tmJob\",\n" +
                "                \"tmImage\"\n" +
                "              ]\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"validValues\": {\n" +
                "              \"optionValues\": [\n" +
                "                \"tmJob\"\n" +
                "              ],\n" +
                "              \"condition\": {\n" +
                "                \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.andOperator\",\n" +
                "                \"value\": {\n" +
                "                  \"operators\": [\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.notEqualsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffJpeg\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"typeGUN\": \"com.hp.ext.types.optionProfile.version.1.type.notEqualsOperator\",\n" +
                "                      \"value\": {\n" +
                "                        \"optionName\": \"outputFileFormat\",\n" +
                "                        \"optionValue\": \"ffTiff\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    String getSampleCapabilities() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"apiVersion\": \"1.0\",\n" +
                "  \"description\": \"Using this service, client can initiate scanning operations that create files that are then delivered to a specified destination.\",\n" +
                "  \"implInfo\": [\n" +
                "    {\n" +
                "      \"typeGUN\": \"com.hp.ext.service.extensibility.version.1.impl.futuresmart.type.apiInformation\",\n" +
                "      \"value\": {\n" +
                "        \"apiMajorVersion\": 1,\n" +
                "        \"apiMinorVersion\": 0,\n" +
                "        \"releaseLevel\": \"Beta\",\n" +
                "        \"releaseSequence\": 3,\n" +
                "        \"toolVersions\": [\n" +
                "          \"dsl 2.12.5.202305061829\",\n" +
                "          \"umsgen 0.5.0\"\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"implVersion\": \"v=6.25.0.150+752ece1d-202404190608;r=6.25.0.150+752ece1d;d=202404190608;s=6.25;p=6D6664-0035\",\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/capabilities\",\n" +
                "      \"rel\": \"self\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/capabilities\",\n" +
                "      \"rel\": \"capabilities\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/defaultOptions\",\n" +
                "      \"rel\": \"defaultOptions\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/profile\",\n" +
                "      \"rel\": \"profile\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/scanJob/v1/scanJobAgents\",\n" +
                "      \"rel\": \"scanJobAgents\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"serviceGun\": \"com.hp.ext.service.scanJob.version.1\"\n" +
                "}";
    }

    private String getSampleScanner() {
        return "{\"$opMeta\":{\"contentFilter\":[\"*\"]},\"adfOutputBinIsFull\":\"unknown\",\"hasPaperInAdf\":\"true\",\"hasPaperOnFlatbed\":\"true\",\"isBusy\":false,\"isOnline\":true,\"links\":[{\"href\":\"/ext/device/v1/scanner\",\"rel\":\"self\"}]}";
    }
}
