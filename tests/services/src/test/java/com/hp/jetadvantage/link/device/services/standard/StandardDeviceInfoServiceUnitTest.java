package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.device.Status;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceInfoServiceUnitTest {
    private StandardDeviceScanJobService scanJobService;
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private InjectedHttpClient mockHttpClient;
    @Mock
    private CDMClient mockCdmClient;

    @Test
    public void GivenStandardDeviceInfoService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService();
        assertNotNull(deviceInfoService);
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetIdentityCalled_AndDeviceNotConnected_ThenNullShouldBeReturned() {
        // Error case test : when device not connected
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);
        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        Identity identity = deviceInfoService.getIdentity();
        assertNull(identity);
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetIdentityCalled_ThenIdentityObtained() throws IOException {

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleIdentity());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        Identity identity = deviceInfoService.getIdentity();
        assertNotNull(identity);
        assertEquals("b8ea31b1-64ec-483c-be5e-3532c5a0caa8", identity.getDeviceUuid().toString());
        assertEquals("6.24.0.646+723c31e8-202404091140", identity.getFirmwareVersion().toString());
        assertEquals("12341234", identity.getSerialNumber().toString());
        assertEquals("HP Color LaserJet Flow E877", identity.getMakeAndModelInfo().getBase().toString());
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetScannerCalled_ThenScannerObtained() throws IOException {

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleScanner());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        Scanner scanner = deviceInfoService.getScanner();
        assertNotNull(scanner);
        assertTrue(scanner.getIsOnline());
        assertFalse(scanner.getIsBusy());
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetScannerCalled_ScannerIsNotAvailable_ThenNullObtained() throws IOException {

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenThrow(new IOException("IOException"));
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        Scanner scanner = deviceInfoService.getScanner();
        assertNull(scanner);
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetStatusCalled_ThenStatusObtained() throws IOException {

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getSampleStatus());
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        Status status = deviceInfoService.getStatus();
        assertNotNull(status);
        assertEquals("dsInPowerSave", status.getStatus().getValue());
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetDeviceLanguageCalled_ThenExpectedLanguageReturned() throws IOException {
        // Test data for different languages
        String[] testLanguages =
                {"en", "es", "fr", "pt", "id", "ko", "zh-CN", "zh-TW", "ja", "th", "it", "de", "da", "nl", "fi", "nb"
                        , "sv", "ru", "tr", "pl", "cs", "hu", "ca", "ro", "hr", "sl", "sk", "el", "bg", "ar", "he"};

        for (String deviceLanguage : testLanguages) {
            // Arrange
            when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
            when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);

            String configurationJson = buildConfigurationJson(deviceLanguage);
            CDMResponse<String> cdmResponse = CDMResponse.create(200, configurationJson);
            when(mockCdmClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

            StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

            // Act
            com.hp.ws.cdm.controlpanel.Configuration.Language language = service.getDeviceLanguage();

            // Assert
            assertNotNull("Language should not be null for " + deviceLanguage, language);
            assertEquals("Expected language " + deviceLanguage,
                    deviceLanguage.toLowerCase(), language.toString().toLowerCase());
        }
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetDeviceLanguageCalled_ThenDefaultLanguageReturnedForEmptyLanguageResponse() throws IOException {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);

        String configurationJson = buildConfigurationJson("");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, configurationJson);
        when(mockCdmClient.sendGetRequest(anyString())).thenReturn(cdmResponse);

        StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

        // Act
        com.hp.ws.cdm.controlpanel.Configuration.Language language = service.getDeviceLanguage();

        // Assert
        assertNotNull("Language should not be null", language);
        assertEquals("Expected default language", "en", language.toString().toLowerCase());
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetAvailableDeviceLanguagesCalled_WithConstraintsJson_ThenExpectedLanguageListReturned() throws Exception {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);

        String constraintsJson =
                Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                        "deviceInfo/GET_cdm_system_v1_configuration_contraints.json");
        CDMResponse<String> cdmResponse = CDMResponse.create(200, constraintsJson);
        when(mockCdmClient.sendGetRequest(StandardDeviceInfoService.CDMUrl.SYSTEM_CONFIGURATION_CONSTRAINTS))
                .thenReturn(cdmResponse);

        StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

        // Act
        List<com.hp.ws.cdm.controlpanel.Configuration.Language> languages = service.getAvailableDeviceLanguages();

        // Assert
        List<com.hp.ws.cdm.controlpanel.Configuration.Language> expected = Arrays.asList(
                com.hp.ws.cdm.controlpanel.Configuration.Language.EN,
                com.hp.ws.cdm.controlpanel.Configuration.Language.ES,
                com.hp.ws.cdm.controlpanel.Configuration.Language.FR,
                com.hp.ws.cdm.controlpanel.Configuration.Language.PT,
                com.hp.ws.cdm.controlpanel.Configuration.Language.ID,
                com.hp.ws.cdm.controlpanel.Configuration.Language.KO,
                com.hp.ws.cdm.controlpanel.Configuration.Language.ZH_CN,
                com.hp.ws.cdm.controlpanel.Configuration.Language.ZH_TW,
                com.hp.ws.cdm.controlpanel.Configuration.Language.JA,
                com.hp.ws.cdm.controlpanel.Configuration.Language.TH,
                com.hp.ws.cdm.controlpanel.Configuration.Language.IT,
                com.hp.ws.cdm.controlpanel.Configuration.Language.DE,
                com.hp.ws.cdm.controlpanel.Configuration.Language.DA,
                com.hp.ws.cdm.controlpanel.Configuration.Language.NL,
                com.hp.ws.cdm.controlpanel.Configuration.Language.FI,
                com.hp.ws.cdm.controlpanel.Configuration.Language.NB,
                com.hp.ws.cdm.controlpanel.Configuration.Language.SV,
                com.hp.ws.cdm.controlpanel.Configuration.Language.RU,
                com.hp.ws.cdm.controlpanel.Configuration.Language.TR,
                com.hp.ws.cdm.controlpanel.Configuration.Language.PL,
                com.hp.ws.cdm.controlpanel.Configuration.Language.CS,
                com.hp.ws.cdm.controlpanel.Configuration.Language.HU,
                com.hp.ws.cdm.controlpanel.Configuration.Language.CA,
                com.hp.ws.cdm.controlpanel.Configuration.Language.RO,
                com.hp.ws.cdm.controlpanel.Configuration.Language.HR,
                com.hp.ws.cdm.controlpanel.Configuration.Language.SL,
                com.hp.ws.cdm.controlpanel.Configuration.Language.SK,
                com.hp.ws.cdm.controlpanel.Configuration.Language.EL,
                com.hp.ws.cdm.controlpanel.Configuration.Language.BG,
                com.hp.ws.cdm.controlpanel.Configuration.Language.AR,
                com.hp.ws.cdm.controlpanel.Configuration.Language.HE
        );

        assertEquals("Language list size should match", expected.size(), languages.size());
        assertEquals("Language order/content should match expected", expected, languages);
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetAvailableDeviceLanguagesCalled_AndConstraintsCallFails_ThenDefaultLanguageReturned() throws Exception {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);
        when(mockCdmClient.sendGetRequest(StandardDeviceInfoService.CDMUrl.SYSTEM_CONFIGURATION_CONSTRAINTS))
                .thenThrow(new IOException("Network error"));

        StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

        // Act
        List<com.hp.ws.cdm.controlpanel.Configuration.Language> languages = service.getAvailableDeviceLanguages();

        // Assert
        assertEquals("Should fall back to exactly one language (default EN)", 1, languages.size());
        assertEquals(com.hp.ws.cdm.controlpanel.Configuration.Language.EN, languages.get(0));
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetAvailableDeviceLanguagesCalled_ThenDefaultLanguageReturnedForEmptyValidatorsResponse() throws Exception {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);

        String constraintsJson = "{\"validators\": []}";
        CDMResponse<String> cdmResponse = CDMResponse.create(200, constraintsJson);
        when(mockCdmClient.sendGetRequest(StandardDeviceInfoService.CDMUrl.SYSTEM_CONFIGURATION_CONSTRAINTS))
                .thenReturn(cdmResponse);

        StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

        // Act
        List<com.hp.ws.cdm.controlpanel.Configuration.Language> languages = service.getAvailableDeviceLanguages();

        assertEquals("Should fall back to exactly one language (default EN)", 1, languages.size());
        assertEquals(com.hp.ws.cdm.controlpanel.Configuration.Language.EN, languages.get(0));
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetAvailableDeviceLanguagesCalled_ThenDefaultLanguageReturnedForEmptyResponse() throws Exception {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getCDMClient()).thenReturn(mockCdmClient);

        String constraintsJson = "";
        CDMResponse<String> cdmResponse = CDMResponse.create(200, constraintsJson);
        when(mockCdmClient.sendGetRequest(StandardDeviceInfoService.CDMUrl.SYSTEM_CONFIGURATION_CONSTRAINTS))
                .thenReturn(cdmResponse);

        StandardDeviceInfoService service = new StandardDeviceInfoService(mockDeviceManagementService);

        // Act
        List<com.hp.ws.cdm.controlpanel.Configuration.Language> languages = service.getAvailableDeviceLanguages();

        assertEquals("Should fall back to exactly one language (default EN)", 1, languages.size());
        assertEquals(com.hp.ws.cdm.controlpanel.Configuration.Language.EN, languages.get(0));
    }

    @Test
    public void GivenStandardDeviceInfoService_WhenGetDeploymentInformationCalled_ThenDeploymentInformationObtained() throws IOException {
        // Arrange
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        String deploymentInfoJson = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "deviceInfo/GET_ext_device_v1_deploymentInformation.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(deploymentInfoJson);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceInfoService deviceInfoService = new StandardDeviceInfoService(mockDeviceManagementService);
        assertNotNull(deviceInfoService);

        // Act
        com.hp.ext.service.device.DeploymentInformation deploymentInformation = deviceInfoService.getDeploymentInformation();

        // Assert
        assertNotNull(deploymentInformation);
        assertNotNull("Network info list should not be null", deploymentInformation.getNetworkInfo());
        assertFalse("Network info list should not be empty", deploymentInformation.getNetworkInfo().isEmpty());
        com.hp.ext.service.device.NetworkAdapterInfo first = deploymentInformation.getNetworkInfo().get(0);
        assertEquals("getAdapterName", "eth0", first.getAdapterName().toString());
        assertEquals("getHostName", "HPI1014FC", first.getHostName().toString());
        assertEquals("getIpV4", "15.26.148.154", first.getIpV4().toString());
        assertEquals("getMacAddress", "14:CB:19:10:14:FC", first.getMacAddress().toString());

        com.hp.ext.service.device.OwnerConfiguredInformation ownerConfigInfo = deploymentInformation.getOwnerInfo();
        assertNotNull("Owner config info should not be null", ownerConfigInfo);
        assertEquals("assetNumberValue", "123", ownerConfigInfo.getAssetNumber().toString());
        assertEquals("companyName", "HP", ownerConfigInfo.getCompanyName().toString());
        assertEquals("contactName", "John", ownerConfigInfo.getContactName().toString());
        assertEquals("deviceLocation", "Office", ownerConfigInfo.getDeviceLocation().toString());
        assertEquals("deviceName", "HP Printer", ownerConfigInfo.getDeviceName().toString());
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetaData());
        return resource;
    }

    private List<Link> geLinks() {
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

        // Added deploymentInformation link required for getDeploymentInformation()
        Link link8 = new Link();
        link8.setHref("/ext/device/v1/deploymentInformation");
        link8.setRel("deploymentInformation");
        links.add(link8);

        return links;
    }

    private List<ServiceMetadataImpl> getServiceMetaData() {
        List<ServiceMetadataImpl> serviceMetaData = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl deviceServiceMetadata = new ServiceMetadataImpl();
        deviceServiceMetadata.setDescription("APIs to acquire basic information about the entire device.");
        deviceServiceMetadata.setServiceGun("com.hp.ext.service.device.version.1");
        deviceServiceMetadata.setLinks(geLinks());
        serviceMetaData.add(deviceServiceMetadata);
        return serviceMetaData;
    }

    private String getSampleIdentity() {
        return "{\"$opMeta\":{\"contentFilter\":[\"*\"]},\"deviceUuid\":\"b8ea31b1-64ec-483c-be5e-3532c5a0caa8\"," +
                "\"firmwareVersion\":\"6.24.0.646+723c31e8-202404091140\"," +
                "\"links\":[{\"href\":\"/ext/device/v1/identity\",\"rel\":\"self\"}]," +
                "\"makeAndModelInfo\":{\"base\":\"HP Color LaserJet Flow E877\",\"family\":\"HP Color LaserJet Flow " +
                "MFP E877\",\"model\":\"HP Color LaserJet Flow E87740\"},\"serialNumber\":\"12341234\"}";
    }

    private String getSampleScanner() {
        return "{\"$opMeta\":{\"contentFilter\":[\"*\"]},\"adfOutputBinIsFull\":\"unknown\"," +
                "\"hasPaperInAdf\":\"true\",\"hasPaperOnFlatbed\":\"true\",\"isBusy\":false,\"isOnline\":true," +
                "\"links\":[{\"href\":\"/ext/device/v1/scanner\",\"rel\":\"self\"}]}";
    }

    private String getSampleStatus() {
        return "{\"$opMeta\":{\"contentFilter\":[\"*\"]},\"links\":[{\"href\":\"/ext/device/v1/status\"," +
                "\"rel\":\"self\"}],\"status\":\"dsInPowerSave\"}";
    }

    private String buildConfigurationJson(String lang) {
        return "{" +
                "\"assetNumber\":\"\"," +
                "\"companyContact\":\"\"," +
                "\"companyName\":\"\"," +
                "\"deviceDescription\":\"\"," +
                "\"deviceLanguage\":\"" + lang + "\"," +
                "\"deviceLocation\":\"\"," +
                "\"displayUnitOfMeasure\":\"metric\"," +
                "\"supportContact\":\"\"," +
                "\"links\":[{\"href\":\"/cdm/system/v1/configuration/constraints\",\"rel\":\"constraints\"}]," +
                "\"version\":\"1.6.0\"" +
                "}";
    }
}
