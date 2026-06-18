package com.hp.jetadvantage.link.services.devicelet.adapter;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.hp.ext.service.device.DeploymentInformation;
import com.hp.ext.service.device.FirmwareVersion;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.MakeAndModelInfo;
import com.hp.ext.service.device.ModelName;
import com.hp.ext.service.device.NetworkAdapterInfo;
import com.hp.ext.service.device.OwnerConfiguredInformation;
import com.hp.ext.service.device.ProductNumber;
import com.hp.ext.service.device.SerialNumber;
import com.hp.ext.types.network.IpAddressString;
import com.hp.ext.types.network.MacAddressString;
import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.ws.cdm.controlpanel.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class DeviceInfoAdapterUnitTest {

    // Test data constants
    private static final String ETH0_ADAPTER_NAME = "eth0";
    private static final String ETH0_HOSTNAME = "HOST-ETH0";
    private static final String ETH0_IP = "10.0.0.5";
    private static final String ETH0_MAC = "AA:BB:CC:DD:EE:FF";

    private static final String WLAN0_ADAPTER_NAME = "wlan0";
    private static final String WLAN0_HOSTNAME = "HOST-WLAN";
    private static final String WLAN0_IP_FIRST = "192.168.1.20";
    private static final String WLAN0_MAC_FIRST = "11:22:33:44:55:66";
    private static final String WLAN0_IP_SECOND = "172.16.0.10";
    private static final String WLAN0_MAC_SECOND = "66:55:44:33:22:11";

    private static final String ASSET_NUMBER = "ASSET-001";
    private static final String COMPANY_NAME = "HP Inc.";
    private static final String CONTACT_NAME = "Admin Contact";
    private static final String DEVICE_LOCATION = "Lab A";
    private static final String DEVICE_NAME = "MyDeviceName";

    private static final String MODEL_BASE = "Color LaserJet MFP E78625";
    private static final String MODEL_FAMILY = "HP HP LaserJet Flow MFP E826";
    private static final String MODEL_NAME = "HP Color LaserJet MFP E78625";
    private static final String DEVICE_UUID = "12345678-1234-1234-1234-1234567890ab";
    private static final String FIRMWARE_VERSION = "6.35.0.564+fcf7d2adbe8b-1-e7853fa-202509180735";
    private static final String SERIAL_NUMBER = "XXXXXXXXXX";
    private static final String PRODUCT_NUMBER = "CXXXXA";

    private static final String DEFAULT_LOCALE = "en-US";
    private static final String HP_VENDOR = "HP";
    private static final String HP_FUTURE_SMART_6 = "HP FutureSmart 6";

    @Mock
    private IDeviceInfoService mockDeviceInfoService;

    private DeploymentInformation deploymentInformation;
    private NetworkAdapterInfo eth0;
    private OwnerConfiguredInformation ownerInfo;
    private Identity identity;

    @Before
    public void setUp() {
        setupDefaultDeploymentInformation();
        setupDefaultIdentity();
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithDeploymentInformationAttribute_ThenReturnsExpectedValues() {
        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(deploymentInformation);

        // Network information assertions
        assertDeviceInfoEquals(ETH0_MAC, DeviceAttribute.DA_NETWORK_MACADDRESS,
                "MAC address should match eth0 adapter");
        assertDeviceInfoEquals(ETH0_IP, DeviceAttribute.DA_NETWORK_IPADDRESS,
                "IP address should match eth0 adapter");
        assertDeviceInfoEquals(ETH0_HOSTNAME, DeviceAttribute.DA_NETWORK_HOSTNAME,
                "Hostname should match eth0 adapter");

        // Owner information assertions
        assertDeviceInfoEquals(ASSET_NUMBER, DeviceAttribute.DA_ASSET_NUMBER,
                "Asset number should match");
        assertDeviceInfoEquals(CONTACT_NAME, DeviceAttribute.DA_COMPANY_CONTACT,
                "Company contact should match");
        assertDeviceInfoEquals(COMPANY_NAME, DeviceAttribute.DA_COMPANY_NAME,
                "Company name should match");
        assertDeviceInfoEquals(DEVICE_LOCATION, DeviceAttribute.DA_DEVICE_LOCATION,
                "Device location should match");
        assertDeviceInfoEquals(DEVICE_NAME, DeviceAttribute.DA_MACHINE_NAME,
                "Device name should match");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalled_AndDeploymentInformationIsNull_ThenReturnsNull() {
        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(null);

        String result = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_NETWORK_MACADDRESS);
        assertNull("DA_NETWORK_MACADDRESS", result);

        result = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_ASSET_NUMBER);
        assertNull("DA_ASSET_NUMBER", result);
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalled_AndNoEth0Adapter_ThenUsesFirstAvailableAdapter() {
        DeploymentInformation info = new DeploymentInformation();
        NetworkAdapterInfo onlyAdapter = createNetworkAdapter(WLAN0_ADAPTER_NAME, WLAN0_HOSTNAME,
                WLAN0_IP_SECOND, WLAN0_MAC_SECOND);
        info.setNetworkInfo(Collections.singletonList(onlyAdapter));
        info.setOwnerInfo(ownerInfo);

        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(info);

        assertDeviceInfoEquals(WLAN0_MAC_SECOND, DeviceAttribute.DA_NETWORK_MACADDRESS,
                "Should use first available adapter MAC");
        assertDeviceInfoEquals(WLAN0_IP_SECOND, DeviceAttribute.DA_NETWORK_IPADDRESS,
                "Should use first available adapter IP");
        assertDeviceInfoEquals(WLAN0_HOSTNAME, DeviceAttribute.DA_NETWORK_HOSTNAME,
                "Should use first available adapter hostname");
    }


    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalled_AndNoNetworkAdapter_ThenReturnsEmptyStrings() {
        DeploymentInformation info = new DeploymentInformation();
        info.setNetworkInfo(null); // no adapters
        info.setOwnerInfo(ownerInfo);
        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(info);

        // Network attributes should return empty strings
        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_MACADDRESS,
                "Should return empty string when no network adapters");
        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_IPADDRESS,
                "Should return empty string when no network adapters");
        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_HOSTNAME,
                "Should return empty string when no network adapters");

        // Owner info should still be present
        assertDeviceInfoEquals(ASSET_NUMBER, DeviceAttribute.DA_ASSET_NUMBER,
                "Owner info should still be available when network info is null");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithIdentityAttribute_ThenReturnsExpectedIdentityValues() {
        when(mockDeviceInfoService.getIdentity()).thenReturn(identity);

        assertDeviceInfoEquals(MODEL_NAME, DeviceAttribute.DA_SYSTEM_MODELNAME,
                "Model name should match");
        assertDeviceInfoEquals(SERIAL_NUMBER, DeviceAttribute.DA_SYSTEM_SERIALNUMBER,
                "Serial number should match");
        assertDeviceInfoEquals(FIRMWARE_VERSION, DeviceAttribute.DA_SYSTEM_FIRMWARE_VERSION,
                "Firmware version should match");
        assertDeviceInfoEquals(DEVICE_UUID, DeviceAttribute.DA_SYSTEM_DEVICE_ID,
                "Device UUID should match");
        assertDeviceInfoEquals("", DeviceAttribute.DA_SYSTEM_FORMATTER_SERIAL_NUMBER,
                "Formatter serial number should be empty when not set");
        assertDeviceInfoEquals(PRODUCT_NUMBER, DeviceAttribute.DA_SYSTEM_PRODUCT_NUMBER,
                "Product number should match");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithIdentityAttribute_AndIdentityIsNull_ThenReturnsNull() {
        when(mockDeviceInfoService.getIdentity()).thenReturn(null);

        assertNull("Should return null when identity is null",
                DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_MODELNAME));
        assertNull("Should return null when identity is null",
                DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_SERIALNUMBER));
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalled_AndDeviceLanguageIsNull_ThenReturnsDefaultLocale() {
        when(mockDeviceInfoService.getDeviceLanguage()).thenReturn(null);

        assertDeviceInfoEquals(DEFAULT_LOCALE, DeviceAttribute.DA_SYSTEM_LANGUAGE,
                "Should return default locale when device language is null");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithLanguageCapability_ThenReturnsFormattedLanguageList() {
        List<Configuration.Language> languages = Arrays.asList(
                Configuration.Language.AR,
                Configuration.Language.BG,
                Configuration.Language.CA,
                Configuration.Language.CS,
                Configuration.Language.DA,
                Configuration.Language.DE,
                Configuration.Language.EL,
                Configuration.Language.EN,
                Configuration.Language.EN_EU,
                Configuration.Language.ES,
                Configuration.Language.ET,
                Configuration.Language.EU,
                Configuration.Language.FI,
                Configuration.Language.FR,
                Configuration.Language.FR_CA,
                Configuration.Language.HE,
                Configuration.Language.HR,
                Configuration.Language.HU,
                Configuration.Language.ID,
                Configuration.Language.IT,
                Configuration.Language.JA,
                Configuration.Language.KO,
                Configuration.Language.LV,
                Configuration.Language.LT,
                Configuration.Language.MS,
                Configuration.Language.NB,
                Configuration.Language.NL,
                Configuration.Language.NN,
                Configuration.Language.NO,
                Configuration.Language.PL,
                Configuration.Language.PT,
                Configuration.Language.RO,
                Configuration.Language.RU,
                Configuration.Language.SK,
                Configuration.Language.SL,
                Configuration.Language.SV,
                Configuration.Language.TH,
                Configuration.Language.TR,
                Configuration.Language.UK,
                Configuration.Language.VI,
                Configuration.Language.ZH_CN,
                Configuration.Language.ZH_TW,
                Configuration.Language.ZH
        );
        when(mockDeviceInfoService.getAvailableDeviceLanguages()).thenReturn(languages);

        String actualLanguages = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService,
                DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY);

        String expected = "[\"ar-SA\",\"bg-BG\",\"ca-ES\",\"cs-CZ\",\"da-DK\",\"de-DE\",\"el-GR\",\"en-US\"," +
                "\"en-EU\",\"es-ES\",\"et-EE\",\"eu-ES\",\"fi-FI\",\"fr-FR\",\"fr-CA\",\"he-IL\",\"hr-HR\",\"hu-HU\"," +
                "\"id-ID\",\"it-IT\",\"ja-JP\",\"ko-KR\",\"lv-LV\",\"lt-LT\",\"ms-MY\",\"nb-NO\",\"nl-NL\",\"nn-NO\"," +
                "\"no-NO\",\"pl-PL\",\"pt-BR\",\"ro-RO\",\"ru-RU\",\"sk-SK\",\"sl-SI\",\"sv-SE\",\"th-TH\",\"tr-TR\"," +
                "\"uk-UA\",\"vi-VN\",\"zh-CN\",\"zh-TW\",\"zh\"]";

        assertEquals("Language capability list should match expected format", expected, actualLanguages);

        Type var7 = (new TypeToken<String[]>() {}).getType();

    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithConstantAttributes_ThenReturnsExpectedConstants() {
        assertDeviceInfoEquals(HP_FUTURE_SMART_6, DeviceAttribute.DA_SYSTEM_HP_FUTURE_SMART_LEVEL,
                "Should return HP FutureSmart 6");
        assertDeviceInfoEquals(HP_VENDOR, DeviceAttribute.DA_DEVICE_VENDOR,
                "Should return HP as vendor");
    }

    // Test individual language mappings to ensure correctness
    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithSpecificLanguages_ThenReturnsMappedLocales() {
        // Test a few key language mappings
        testLanguageMapping(Configuration.Language.EN, "en-US");
        testLanguageMapping(Configuration.Language.FR, "fr-FR");
        testLanguageMapping(Configuration.Language.DE, "de-DE");
        testLanguageMapping(Configuration.Language.ZH_CN, "zh-CN");
        testLanguageMapping(Configuration.Language.ZH_TW, "zh-TW");
        testLanguageMapping(Configuration.Language.PT, "pt-BR");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithNullOwnerInfo_ThenHandlesGracefully() {
        DeploymentInformation info = new DeploymentInformation();
        info.setNetworkInfo(Arrays.asList(eth0));
        info.setOwnerInfo(null);
        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(info);

        // Network info should still work
        assertDeviceInfoEquals(ETH0_MAC, DeviceAttribute.DA_NETWORK_MACADDRESS,
                "Network info should work even with null owner info");

        // Owner info should return empty strings
        assertDeviceInfoEquals("", DeviceAttribute.DA_ASSET_NUMBER,
                "Should return empty string when owner info is null");
        assertDeviceInfoEquals("", DeviceAttribute.DA_COMPANY_NAME,
                "Should return empty string when owner info is null");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithNullAvailableLanguages_ThenReturnsNull() {
        when(mockDeviceInfoService.getAvailableDeviceLanguages()).thenReturn(null);

        assertNull("Should return null when available languages is null",
                DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY));
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithEmptyAvailableLanguages_ThenReturnsNull() {
        when(mockDeviceInfoService.getAvailableDeviceLanguages()).thenReturn(Collections.emptyList());

        assertNull("Should return null when available languages is empty",
                DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY));
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithSingleLanguage_ThenReturnsFormattedSingleItemList() {
        when(mockDeviceInfoService.getAvailableDeviceLanguages())
                .thenReturn(Collections.singletonList(Configuration.Language.EN));

        String result = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService,
                DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY);

        assertEquals("Single language should be properly formatted", "[\"en-US\"]", result);
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithMakeAndModelInfoNull_ThenReturnsEmptyString() {
        Identity identityWithNullModel = new Identity();
        identityWithNullModel.setMakeAndModelInfo(null);
        identityWithNullModel.setSerialNumber(new SerialNumber(SERIAL_NUMBER));

        when(mockDeviceInfoService.getIdentity()).thenReturn(identityWithNullModel);

        assertDeviceInfoEquals("", DeviceAttribute.DA_SYSTEM_MODELNAME,
                "Should return empty string when MakeAndModelInfo is null");
        assertDeviceInfoEquals(SERIAL_NUMBER, DeviceAttribute.DA_SYSTEM_SERIALNUMBER,
                "Other identity fields should still work");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithNetworkAdapterMissingFields_ThenHandlesGracefully() {
        NetworkAdapterInfo incompleteAdapter = new NetworkAdapterInfo();
        incompleteAdapter.setAdapterName(ETH0_ADAPTER_NAME);
        // Deliberately missing hostname, IP, and MAC

        DeploymentInformation info = new DeploymentInformation();
        info.setNetworkInfo(Collections.singletonList(incompleteAdapter));
        info.setOwnerInfo(ownerInfo);

        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(info);

        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_MACADDRESS,
                "Should return empty string for missing MAC address");
        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_IPADDRESS,
                "Should return empty string for missing IP address");
        assertDeviceInfoEquals("", DeviceAttribute.DA_NETWORK_HOSTNAME,
                "Should return empty string for missing hostname");
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoCalledWithOwnerInfoMissingFields_ThenHandlesPartialData() {
        OwnerConfiguredInformation partialOwnerInfo = new OwnerConfiguredInformation();
        partialOwnerInfo.setAssetNumber(ASSET_NUMBER);
        partialOwnerInfo.setCompanyName(COMPANY_NAME);
        // Deliberately missing contactName, deviceLocation, deviceName

        DeploymentInformation info = new DeploymentInformation();
        info.setNetworkInfo(Arrays.asList(eth0));
        info.setOwnerInfo(partialOwnerInfo);

        when(mockDeviceInfoService.getDeploymentInformation()).thenReturn(info);

        // Available fields should work
        assertDeviceInfoEquals(ASSET_NUMBER, DeviceAttribute.DA_ASSET_NUMBER,
                "Available asset number should be returned");
        assertDeviceInfoEquals(COMPANY_NAME, DeviceAttribute.DA_COMPANY_NAME,
                "Available company name should be returned");

        // Missing fields should return empty strings
        assertDeviceInfoEquals("", DeviceAttribute.DA_COMPANY_CONTACT,
                "Missing contact should return empty string");
        assertDeviceInfoEquals("", DeviceAttribute.DA_DEVICE_LOCATION,
                "Missing location should return empty string");
        assertDeviceInfoEquals("", DeviceAttribute.DA_MACHINE_NAME,
                "Missing device name should return empty string");
    }

    /**************** Helper Methods  ****************/

    private void testLanguageMapping(Configuration.Language language, String expectedLocale) {
        when(mockDeviceInfoService.getDeviceLanguage()).thenReturn(language);
        String actual = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_LANGUAGE);
        assertEquals("Locale mapping incorrect for " + language, expectedLocale, actual);
    }


    private void setupDefaultDeploymentInformation() {
        deploymentInformation = new DeploymentInformation();

        // Preferred adapter (eth0)
        eth0 = createNetworkAdapter(ETH0_ADAPTER_NAME, ETH0_HOSTNAME, ETH0_IP, ETH0_MAC);

        // Non-preferred adapter (will be ignored when eth0 exists)
        NetworkAdapterInfo wifi = createNetworkAdapter(WLAN0_ADAPTER_NAME, WLAN0_HOSTNAME,
                WLAN0_IP_FIRST, WLAN0_MAC_FIRST);

        deploymentInformation.setNetworkInfo(Arrays.asList(wifi, eth0));

        ownerInfo = createOwnerInfo();
        deploymentInformation.setOwnerInfo(ownerInfo);
    }

    private void setupDefaultIdentity() {
        MakeAndModelInfo mm = new MakeAndModelInfo();
        mm.setBase(new ModelName(MODEL_BASE));
        mm.setFamily(new ModelName(MODEL_FAMILY));
        mm.setModel(new ModelName(MODEL_NAME));

        identity = new Identity();
        identity.setMakeAndModelInfo(mm);
        identity.setDeviceUuid(UUID.fromString(DEVICE_UUID));
        identity.setFirmwareVersion(new FirmwareVersion(FIRMWARE_VERSION));
        identity.setSerialNumber(new SerialNumber(SERIAL_NUMBER));
        identity.setProductNumber(new ProductNumber(PRODUCT_NUMBER));
    }

    private NetworkAdapterInfo createNetworkAdapter(String adapterName, String hostName,
                                                    String ipAddress, String macAddress) {
        NetworkAdapterInfo adapter = new NetworkAdapterInfo();
        adapter.setAdapterName(adapterName);
        adapter.setHostName(hostName);
        adapter.setIpV4(new IpAddressString(ipAddress));
        adapter.setMacAddress(new MacAddressString(macAddress));
        return adapter;
    }

    private OwnerConfiguredInformation createOwnerInfo() {
        OwnerConfiguredInformation owner = new OwnerConfiguredInformation();
        owner.setAssetNumber(ASSET_NUMBER);
        owner.setCompanyName(COMPANY_NAME);
        owner.setContactName(CONTACT_NAME);
        owner.setDeviceLocation(DEVICE_LOCATION);
        owner.setDeviceName(DEVICE_NAME);
        return owner;
    }

    private void assertDeviceInfoEquals(String expected, DeviceAttribute attribute, String errorMessage) {
        String actual = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, attribute);
        assertEquals(errorMessage, expected, actual);
    }
}
