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
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.optionProfile.OptionRule;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceCopyJobServiceUnitTest extends StandardDeviceUnitTest {
    private StandardDeviceCopyJobService copyJobService;

    @Test
    public void GivenStandardDeviceCopyJobService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        assertNotNull(copyJobService);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenGetDefaultOptionsCalled_AndDeviceNotConnected_ThenExceptionShouldBeThrown() {
        // Error case test : when device not connected
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);
        assertNotNull(copyJobService);

        try {
            copyJobService.getDefaultOptions(testPackageName);
            fail("expected exception does not occur");
        } catch (BoundDeviceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenIsSupported_ThenTrueShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleCapabilities());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceCopyJobService and call DefaultOptions
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);

        boolean supported = copyJobService.isSupported();
        assertTrue(supported);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenGetDefaultOptionsCalled_AndDeviceConnected_ThenDefaultOptionShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleCopyDefaultOptions());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceCopyJobService and call DefaultOptions
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);
        DefaultOptions defCopyOptions = copyJobService.getDefaultOptions(testPackageName);

        //verify returned copy default options
        assertNotNull(defCopyOptions);
//        assertNull(defCopyOptions.getEmail());
//        assertNotNull(defCopyOptions.getHttp());
//        assertEquals(defCopyOptions.getHttp().getAutoColorDetect(), AutoColorDetectMode.AcdmDetectOnly);
//        assertEquals(defCopyOptions.getHttp().getAutoDeskew(), false);
//        assertEquals(defCopyOptions.getHttp().getAutoExposure(), false);
//        assertEquals(defCopyOptions.getHttp().getBackgroundCleanup().toString(), "2");
//        assertEquals(defCopyOptions.getHttp().getBackgroundColorRemoval(), false);
//        assertEquals(defCopyOptions.getHttp().getBackgroundColorRemovalLevel().toString(), "0");
//        assertEquals(defCopyOptions.getHttp().getBackgroundNoiseRemoval(), false);
//        assertEquals(defCopyOptions.getHttp().getBinaryRendering(), BinaryRenderingType.BrHalftone);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenGetDefaultOptionsCalled_AndDeviceReturnNull_ThenNullOptionShouldBeReturned() throws IOException {
        // Error case test : when device return null data

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceCopyJobService and call DefaultOptions
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);
        DefaultOptions defCopyOptions = copyJobService.getDefaultOptions(testPackageName);

        //verify returned copy default options
        assertNull(defCopyOptions);
    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenGetProfileCalled_AndDeviceConnected_ThenCopyProfileShouldBeReturned() throws IOException {

        //define mockDeviceManagementService
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);  //unnecessary stub?
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());

        //define mockHttpClient to return sample json data for default options and inject it to the oxpd2lib
        String rawProfileData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), "copyJob/GET_ext_copy_profile.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(rawProfileData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //create StandardDeviceCopyJobService and call getProfile
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);
        Profile copyProfile = copyJobService.getProfile(testPackageName);

        //verify returned copy profile
        assertNotNull(copyProfile);
        assertEquals(copyProfile.getBase().getDefinitions().get(0).getOptionName(), "autoColorDetect");
        assertEquals(copyProfile.getBase().getDefinitions().get(0).getIsAvailable(), false);
        List<OptionRule> copyProgressModeOptionRule = copyProfile.getBase().getDefinitions().get(0).getRules();
        assertNotNull(copyProgressModeOptionRule);
        assertTrue(copyProgressModeOptionRule.get(0).getPossibleValues().getOptionValues().contains("acdmDetectOnly"));
        assertTrue(copyProgressModeOptionRule.get(0).getPossibleValues().getOptionValues().contains("acdmTreatNonColorAsMonochrome"));
        assertTrue(copyProgressModeOptionRule.get(0).getPossibleValues().getOptionValues().contains("acdmTreatNonColorAsGrayscale"));

    }

    @Test
    public void GivenStandardDeviceCopyJobService_WhenRegisterNotificationCallbackCalled_AndCopyNotificationOccurs_ThenCallbackCalled() {
        //define test sample data
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String typeGUN = "com.hp.ext.service.copy.version.1.type.copyNotification";
        String packageId = "app1";
        String sampleCopyJobNotification = "{\n" +
                "  \"jobNotification\": {\n" +
                "    \"copyJobId\": \"e3736199-5163-4c47-88d3-572e4e06dd6a\",\n" +
                "    \"copyJobStatus\": {\n" +
                "      \"cancelingActivity\": [\n" +
                "        null\n" +
                "      ],\n" +
                "      \"jobActivity\": [\n" +
                "        null\n" +
                "      ],\n" +
                "      \"jobDoneStatus\": \"jdsActive\",\n" +
                "      \"jobDoneStatusDetail\": \"string\",\n" +
                "      \"printingActivity\": [\n" +
                "        null\n" +
                "      ],\n" +
                "      \"scanningActivity\": [\n" +
                "        null\n" +
                "      ],\n" +
                "      \"totalImagesScanned\": 0,\n" +
                "      \"totalSheetsPrinted\": 0\n" +
                "    },\n" +
                "    \"solutionContext\": \"string\"\n" +
                "  }\n" +
                "}";

        AtomicInteger callbackCount = new AtomicInteger(0);

        //define test callback
        IE2PayloadCallback<CopyNotification> callback = (appPackageId, notification) -> {
            callbackCount.incrementAndGet();

            //verify received copy job notification
            assertEquals(packageId, appPackageId);
            assertTrue(notification.isJobNotification());
            assertEquals("com.hp.ext.service.copy.version.1.type.copyNotification", notification.getTypeGUN());
            assertEquals("e3736199-5163-4c47-88d3-572e4e06dd6a", notification.getJobNotification().getCopyJobId().toString());
        };

        //register callback
        StandardDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService(mockDeviceManagementService);
        copyJobService.registerNotificationCallback(callback);

        //1. test for valid channel payload message for copy notification
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId, StandardDeviceCopyJobService.E2SERVICE_COPY_JOB_CANONICAL_GUN));
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleCopyJobNotification));

        //verify callback is called
        assertEquals(1, callbackCount.get());
        callbackCount.set(0);

        //2. test for invalid payload message for copy notification
        String invalidCopyJobNotification = "{\"invalidNotification\":{\"copyJobId\":\"e3736199-5163-4c47-88d3-572e4e06dd6a\",\"copyJobStatus\":{}}}";
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, invalidCopyJobNotification));

        //verify callback is not called
        assertEquals(0, callbackCount.get());

        //3. test for invalid json payload message for copy notification
        String invalid2CopyJobNotification = "{\"jobNotification\":{\"copyJobId\":\"e3736199-5163-4c47-88d3-572e4e06dd6a\",\"copyJobStatus\":";
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, invalid2CopyJobNotification));

        //verify callback is not called
        assertEquals(0, callbackCount.get());

        //4. unregister callback
        copyJobService.unRegisterNotificationCallback();

        //verify that unregistered callback should not be called on receiving message
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleCopyJobNotification));
        assertEquals(0, callbackCount.get());
    }

//    @Test
//    public void testSampleCopyJobNotification() throws JsonProcessingException {
//        CopyJobNotificationContent content = new CopyJobNotificationContent();
//        content.setCopyJobId(new CopyJobIdentifier(java.util.UUID.randomUUID()));
//        content.setCopyJobStatus(new CopyJobStatus());
//        CopyNotification copyNotification = new CopyNotification(content);
//        String copyNotificationJsonStr = new ObjectMapper().writeValueAsString(copyNotification);
//        assertEquals("", copyNotificationJsonStr);
//    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/copy/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/copy/v1/defaultOptions");
        link2.setRel("defaultOptions");
        links.add(link2);

        Link link3 = new Link();
        link3.setHref("/ext/copy/v1/profile");
        link3.setRel("profile");
        links.add(link3);

        return links;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl copyServiceMetadata = new ServiceMetadataImpl();
        copyServiceMetadata.setDescription("Copy Job Service Client Tests Discovery Tree");
        copyServiceMetadata.setServiceGun("com.hp.ext.service.copy.version.1");
        copyServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(copyServiceMetadata);
        return serviceMetadatas;
    }

    private String getSampleCopyDefaultOptions() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"autoColorDetect\": \"acdmDetectOnly\",\n" +
                "  \"autoDeskew\": true,\n" +
                "  \"autoExposure\": true,\n" +
                "  \"backgroundCleanup\": 0,\n" +
                "  \"backgroundColorRemoval\": true,\n" +
                "  \"backgroundColorRemovalLevel\": 0,\n" +
                "  \"backgroundNoiseRemoval\": true,\n" +
                "  \"binaryRendering\": \"brHalftone\",\n" +
                "  \"blackBackground\": true,\n" +
                "  \"blackEnhancementLevel\": 0,\n" +
                "  \"blankPageSuppression\": \"bpdDisable\",\n" +
                "  \"ccdChannel\": \"cctNtsc\",\n" +
                "  \"collationType\": \"sctCollated\",\n" +
                "  \"colorMode\": \"cmAutoDetect\",\n" +
                "  \"colorRange\": 0,\n" +
                "  \"colorSensitivity\": 0,\n" +
                "  \"compressionFactor\": 0,\n" +
                "  \"contentOrientation\": \"coAutoDetect\",\n" +
                "  \"contentType\": \"dctAutoDetect\",\n" +
                "  \"contrast\": 0,\n" +
                "  \"copies\": 0,\n" +
                "  \"descreen\": true,\n" +
                "  \"edgeToEdgeScan\": true,\n" +
                "  \"exposureLevel\": 0,\n" +
                "  \"gamma\": 0,\n" +
                "  \"highlightLevel\": 0,\n" +
                "  \"imagePreviewMode\": \"ipmNone\",\n" +
                "  \"invertColors\": true,\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/defaultOptions\",\n" +
                "      \"rel\": \"self\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"longPlotScan\": true,\n" +
                "  \"mediaBindingFormat\": \"bfFlipLeft\",\n" +
                "  \"misfeedDetection\": true,\n" +
                "  \"originalMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"originalMediaSource\": \"miAdf\",\n" +
                "  \"originalMediaType\": \"mtBond\",\n" +
                "  \"originalPlexMode\": \"pmAutoDetect\",\n" +
                "  \"outputCanvasAnchor\": \"ocaTopLeft\",\n" +
                "  \"outputCanvasMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"outputCanvasMediaSource\": \"miAdf\",\n" +
                "  \"outputCanvasOrientation\": \"coAutoDetect\",\n" +
                "  \"outputCanvasXExtent\": 0,\n" +
                "  \"outputCanvasYExtent\": 0,\n" +
                "  \"outputDuplexBinding\": \"dbfOneSided\",\n" +
                "  \"outputMediaSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"outputMediaSource\": \"miAdf\",\n" +
                "  \"outputMediaType\": \"mtBond\",\n" +
                "  \"outputPlexMode\": \"pmAutoDetect\",\n" +
                "  \"outputQuality\": \"qmNormal\",\n" +
                "  \"pagesPerSheet\": \"ppsOneUp\",\n" +
                "  \"resolution\": \"dpi75\",\n" +
                "  \"scaleSelection\": \"sstNone\",\n" +
                "  \"scaleToFitEnabled\": true,\n" +
                "  \"scaleToOutput\": \"miAdf\",\n" +
                "  \"scaleToSize\": \"msANSI_A_8point5x11in\",\n" +
                "  \"scanCaptureMode\": \"scmStandard\",\n" +
                "  \"scanProgressMode\": \"spmNone\",\n" +
                "  \"scanningSpeed\": \"ssAuto\",\n" +
                "  \"shadowLevel\": 0,\n" +
                "  \"sharpness\": 0,\n" +
                "  \"threshold\": 0,\n" +
                "  \"xOffset\": 0,\n" +
                "  \"xScalePercent\": 0,\n" +
                "  \"yOffset\": 0,\n" +
                "  \"yScalePercent\": 0\n" +
                "}";
    }

    private String getSampleProfile() {
        return "{\n" +
                "  \"definitions\": [\n" +
                "    {\n" +
                "      \"optionName\": \"autoColorDetect\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"acdmDetectOnly\",\n" +
                "              \"acdmTreatNonColorAsMonochrome\",\n" +
                "              \"acdmTreatNonColorAsGrayscale\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"autoDeskew\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"autoExposure\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"backgroundCleanup\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 100,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"backgroundColorRemoval\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"backgroundColorRemovalLevel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"backgroundNoiseRemoval\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"binaryRendering\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"brHalftone\",\n" +
                "              \"brThreshold\",\n" +
                "              \"brErrorDiffusion\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"blackBackground\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"blackEnhancementLevel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 255,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"blankPageSuppression\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"bpdDisable\",\n" +
                "              \"bdpDetectAndSuppress\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"ccdChannel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"cctNtsc\",\n" +
                "              \"cctGray\",\n" +
                "              \"cctGrayEmulated\",\n" +
                "              \"cctRed\",\n" +
                "              \"cctGreen\",\n" +
                "              \"cctBlue\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"collationType\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"sctCollated\",\n" +
                "              \"sctUncollated\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"colorMode\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"cmColor\",\n" +
                "              \"cmGrayscale\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"colorRange\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 100,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"colorSensitivity\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 100,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"compressionFactor\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 10,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"contentOrientation\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"coPortrait\",\n" +
                "              \"coLandscape\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"contentType\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"dctText\",\n" +
                "              \"dctPhoto\",\n" +
                "              \"dctMixed\",\n" +
                "              \"dctImage\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"contrast\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 8,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"copies\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 1,\n" +
                "            \"upperBoundary\": 999,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"descreen\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"edgeToEdgeScan\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"exposureLevel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 1,\n" +
                "            \"upperBoundary\": 9,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"gamma\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 2000,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"highlightLevel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 1,\n" +
                "            \"upperBoundary\": 10,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"imagePreviewMode\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"ipmNone\",\n" +
                "              \"ipmStandard\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"invertColors\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"longPlotScan\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"mediaBindingFormat\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"bfFlipLeft\",\n" +
                "              \"bfFlipUp\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"misfeedDetection\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"originalMediaSize\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"msAny\",\n" +
                "              \"msANSI_A_8point5x11in\",\n" +
                "              \"msLegal_8point5x14in\",\n" +
                "              \"msExecutive_7point25x10point5in\",\n" +
                "              \"msInvoice_5point5x8point5in\",\n" +
                "              \"msANSI_B_11x17in\",\n" +
                "              \"msGeneral_5x7in\",\n" +
                "              \"msGeneral_5x8in\",\n" +
                "              \"msFoolscap_8point5x13in\",\n" +
                "              \"msOficio_216x340mm\",\n" +
                "              \"msISO_A3_297x420mm\",\n" +
                "              \"msISO_A4_210x297mm\",\n" +
                "              \"msISO_A5_148x210mm\",\n" +
                "              \"msISO_A6_105x148mm\",\n" +
                "              \"msJIS_B5_182x257mm\",\n" +
                "              \"msJIS_B6_128x182mm\",\n" +
                "              \"msK16_195x270mm\",\n" +
                "              \"msK16_184x260mm\",\n" +
                "              \"msK16_197x273mm\",\n" +
                "              \"msJDoublePostcard_148x200mm\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"originalMediaSource\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"miAdf\",\n" +
                "              \"miFlatbed\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"originalMediaType\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"mtPlain\",\n" +
                "              \"mtPhoto\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"originalPlexMode\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"pmSimplex\",\n" +
                "              \"pmDuplex\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasAnchor\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasMediaSize\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasMediaSource\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasOrientation\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasXExtent\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputCanvasYExtent\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputDuplexBinding\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"dbfOneSided\",\n" +
                "              \"dbfDuplexLongEdge\",\n" +
                "              \"dbfDuplexShortEdge\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputMediaSize\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"msANSI_A_Rotated_8point5x11in\",\n" +
                "              \"msANSI_A_8point5x11in\",\n" +
                "              \"msLegal_8point5x14in\",\n" +
                "              \"msExecutive_7point25x10point5in\",\n" +
                "              \"msInvoice_5point5x8point5in\",\n" +
                "              \"msFoolscap_8point5x13in\",\n" +
                "              \"msISO_A3_297x420mm\",\n" +
                "              \"msISO_A4_Rotated_210x297mm\",\n" +
                "              \"msISO_A4_210x297mm\",\n" +
                "              \"msISO_A5_148x210mm\",\n" +
                "              \"msISO_A5_Rotated_148x210mm\",\n" +
                "              \"msISO_A6_105x148mm\",\n" +
                "              \"msRA3_305x430mm\",\n" +
                "              \"msSRA3_320x450mm\",\n" +
                "              \"msRA4_215x305mm\",\n" +
                "              \"msSRA4_225x320mm\",\n" +
                "              \"msJIS_B4_257x364mm\",\n" +
                "              \"msJIS_B5_182x257mm\",\n" +
                "              \"msJIS_B5_Rotated_182x257mm\",\n" +
                "              \"msJIS_B6_128x182mm\",\n" +
                "              \"msK8_270x390mm\",\n" +
                "              \"msK16_195x270mm\",\n" +
                "              \"msK8_260x368mm\",\n" +
                "              \"msK16_184x260mm\",\n" +
                "              \"msJDoublePostcard_148x200mm\",\n" +
                "              \"msEnvelope_Monarch_3point875x7point5in\",\n" +
                "              \"msISO_B5_176x250mm\",\n" +
                "              \"msISO_C5_162x229mm\",\n" +
                "              \"msISO_C6_114x162mm\",\n" +
                "              \"msCustom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputMediaSource\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"miAuto\",\n" +
                "              \"miTray1\",\n" +
                "              \"miTray2\",\n" +
                "              \"miTray3\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputMediaType\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"mtAny\",\n" +
                "              \"mtHPMatte_90g\",\n" +
                "              \"mtRecycled\",\n" +
                "              \"mtHPMatte_105g\",\n" +
                "              \"mtHPMatte_120g\",\n" +
                "              \"mtHPGloss_130g\",\n" +
                "              \"mtHPMatte_160g\",\n" +
                "              \"mtHPGloss_160g\",\n" +
                "              \"mtHPMatte_200g\",\n" +
                "              \"mtHPGloss_220g\",\n" +
                "              \"mtTransparency\",\n" +
                "              \"mtLabels\",\n" +
                "              \"mtEnvelope\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"outputQuality\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"qmBest\",\n" +
                "              \"qmDraft\",\n" +
                "              \"qmNormal\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"pagesPerSheet\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"ppsOneUp\",\n" +
                "              \"ppsTwoUp\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"resolution\",\n" +
                "      \"isAvailable\": true,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"dpi75\",\n" +
                "              \"dpi150\",\n" +
                "              \"dpi200\",\n" +
                "              \"dpi300\",\n" +
                "              \"dpi400\",\n" +
                "              \"dpi600\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scaleSelection\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"sstNone\",\n" +
                "              \"sstCustom\",\n" +
                "              \"sstFitToPage\",\n" +
                "              \"sstFullPage\",\n" +
                "              \"sstLegalToLetter\",\n" +
                "              \"sstA4ToLetter\",\n" +
                "              \"sstLetterToA4\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scaleToFitEnabled\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scaleToOutput\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"miAuto\",\n" +
                "              \"miTray1\",\n" +
                "              \"miTray2\",\n" +
                "              \"miTray3\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scaleToSize\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"msISO_A4_210x297mm\",\n" +
                "              \"msISO_A5_148x210mm\",\n" +
                "              \"msISO_A6_105x148mm\",\n" +
                "              \"msISO_B5_176x250mm\",\n" +
                "              \"msISO_B6_125x176mm\",\n" +
                "              \"msISO_C5_162x229mm\",\n" +
                "              \"msISO_C6_114x162mm\",\n" +
                "              \"msJIS_B5_182x257mm\",\n" +
                "              \"msJIS_B6_128x182mm\",\n" +
                "              \"msJIS_Chou3_120x235mm\",\n" +
                "              \"msJDoublePostcard_148x200mm\",\n" +
                "              \"msExecutive_7point25x10point5in\",\n" +
                "              \"msFoolscap_8point5x13in\",\n" +
                "              \"msGovt_Letter_8x10in\",\n" +
                "              \"msLegal_8point5x14in\",\n" +
                "              \"msANSI_A_8point5x11in\",\n" +
                "              \"msK16_184x260mm\",\n" +
                "              \"msK16_195x270mm\",\n" +
                "              \"msInvoice_5point5x8point5in\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scanCaptureMode\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"scmStandard\",\n" +
                "              \"scmJobBuild\",\n" +
                "              \"scmBook\",\n" +
                "              \"scmIdCard\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scanProgressMode\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"spmNone\",\n" +
                "              \"spmStandard\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"scanningSpeed\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"possibleValues\": {\n" +
                "            \"optionValues\": [\n" +
                "              \"ssAuto\",\n" +
                "              \"ssSlow\"\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"shadowLevel\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 100,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"sharpness\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 1,\n" +
                "            \"upperBoundary\": 9,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"threshold\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 100,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"xOffset\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 1000,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"xScalePercent\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 25,\n" +
                "            \"upperBoundary\": 400,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"yOffset\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 0,\n" +
                "            \"upperBoundary\": 1000,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"optionName\": \"yScalePercent\",\n" +
                "      \"isAvailable\": false,\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"lowerBoundary\": 25,\n" +
                "            \"upperBoundary\": 400,\n" +
                "            \"step\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    String getSampleCapabilities() {
        return "{\n" +
                "  \"$opMeta\": {\n" +
                "    \"contentFilter\": [\n" +
                "      \"*\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"serviceGun\": \"com.hp.ext.service.copy.version.1\",\n" +
                "  \"description\": \"The ping service, providing the ability to ping the device.\",\n" +
                "  \"apiVersion\": \"1.4\",\n" +
                "  \"implInfo\": [\n" +
                "    {\n" +
                "      \"typeGUN\": \"com.hp.cdm.service.echo.version.1.types.exampleType\",\n" +
                "      \"value\": {\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"implVersion\": \"v=6.25.0.150+752ece1d-202404190608;r=6.25.0.150+752ece1d;d=202404190608;s=6.25;p=6D6664-0035\",\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/capabilities\",\n" +
                "      \"rel\": \"self\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/capabilities\",\n" +
                "      \"rel\": \"capabilities\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/defaultOptions\",\n" +
                "      \"rel\": \"defaultOptions\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/profile\",\n" +
                "      \"rel\": \"profile\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"href\": \"/ext/copy/v1/copyAgents\",\n" +
                "      \"rel\": \"copyAgents\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
