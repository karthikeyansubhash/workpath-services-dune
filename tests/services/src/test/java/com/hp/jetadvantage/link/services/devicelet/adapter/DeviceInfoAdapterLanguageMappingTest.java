package com.hp.jetadvantage.link.services.devicelet.adapter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.api.device.DeviceAttribute;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.ws.cdm.controlpanel.Configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized test class for testing language mappings in DeviceInfoAdapter.
 * This class focuses specifically on verifying that all supported language enums
 * are correctly mapped to their expected locale strings.
 */
@RunWith(Parameterized.class)
public class DeviceInfoAdapterLanguageMappingTest {

    private final Configuration.Language inputLanguage;
    private final String expectedLocale;
    private final IDeviceInfoService mockDeviceInfoService;

    public DeviceInfoAdapterLanguageMappingTest(Configuration.Language inputLanguage, String expectedLocale) {
        this.inputLanguage = inputLanguage;
        this.expectedLocale = expectedLocale;
        // Create mock directly since @Mock doesn't work with parameterized tests
        this.mockDeviceInfoService = mock(IDeviceInfoService.class);
    }

    @Parameterized.Parameters(name = "{0} should map to {1}")
    public static Collection<Object[]> languageMappingData() {
        return Arrays.asList(new Object[][]{
                {Configuration.Language.AR, "ar-SA"},
                {Configuration.Language.BG, "bg-BG"},
                {Configuration.Language.CA, "ca-ES"},
                {Configuration.Language.CS, "cs-CZ"},
                {Configuration.Language.DA, "da-DK"},
                {Configuration.Language.DE, "de-DE"},
                {Configuration.Language.EL, "el-GR"},
                {Configuration.Language.EN, "en-US"},
                {Configuration.Language.EN_EU, "en-EU"},
                {Configuration.Language.ES, "es-ES"},
                {Configuration.Language.ET, "et-EE"},
                {Configuration.Language.EU, "eu-ES"},
                {Configuration.Language.FI, "fi-FI"},
                {Configuration.Language.FR, "fr-FR"},
                {Configuration.Language.FR_CA, "fr-CA"},
                {Configuration.Language.HE, "he-IL"},
                {Configuration.Language.HR, "hr-HR"},
                {Configuration.Language.HU, "hu-HU"},
                {Configuration.Language.ID, "id-ID"},
                {Configuration.Language.IT, "it-IT"},
                {Configuration.Language.JA, "ja-JP"},
                {Configuration.Language.KO, "ko-KR"},
                {Configuration.Language.LV, "lv-LV"},
                {Configuration.Language.LT, "lt-LT"},
                {Configuration.Language.MS, "ms-MY"},
                {Configuration.Language.NB, "nb-NO"},
                {Configuration.Language.NL, "nl-NL"},
                {Configuration.Language.NN, "nn-NO"},
                {Configuration.Language.NO, "no-NO"},
                {Configuration.Language.PL, "pl-PL"},
                {Configuration.Language.PT, "pt-BR"},
                {Configuration.Language.RO, "ro-RO"},
                {Configuration.Language.RU, "ru-RU"},
                {Configuration.Language.SK, "sk-SK"},
                {Configuration.Language.SL, "sl-SI"},
                {Configuration.Language.SV, "sv-SE"},
                {Configuration.Language.TH, "th-TH"},
                {Configuration.Language.TR, "tr-TR"},
                {Configuration.Language.UK, "uk-UA"},
                {Configuration.Language.VI, "vi-VN"},
                {Configuration.Language.ZH_CN, "zh-CN"},
                {Configuration.Language.ZH_TW, "zh-TW"},
                {Configuration.Language.ZH, "zh"}
        });
    }

    @Test
    public void GivenDeviceInfoAdapter_WhenGetDeviceInfoWithLanguage_ThenReturnsMappedLocale() {
        when(mockDeviceInfoService.getDeviceLanguage()).thenReturn(inputLanguage);

        String actual = DeviceInfoAdapter.getDeviceInfo(mockDeviceInfoService, DeviceAttribute.DA_SYSTEM_LANGUAGE);

        assertEquals("Language " + inputLanguage + " should map to " + expectedLocale,
                expectedLocale, actual);
    }
}
