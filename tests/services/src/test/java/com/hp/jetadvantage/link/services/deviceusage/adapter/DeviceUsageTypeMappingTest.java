package com.hp.jetadvantage.link.services.deviceusage.adapter;

import static org.junit.Assert.assertEquals;

import com.hp.ext.types.media.MediaSizeId;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.services.deviceusagelet.adapter.DeviceUsageAdapter;
import com.hp.jetadvantage.link.services.deviceusagelet.adapter.DeviceUsageTypeMapping;
import com.hp.workpath.api.deviceusage.SubUnitInfo;

import org.junit.Test;

public class DeviceUsageTypeMappingTest {

    private void assertMediaSizeMapping(MediaSizeId mediaSizeId, SubUnitInfo.MediaSize expectedMediaSize) {
        String expected = expectedMediaSize.getValue();
        String actual = DeviceUsageAdapter.getMediaSizeValue(mediaSizeId);
        assertEquals(expected, actual);
    }

    /**
     * Test that verifies the mapping between MediaSizeId and SubUnitInfo.MediaSize.
     */
    @Test
    public void GivenDeviceUsageTypeMapping_WhenGetMediaSizeValueCalled_ThenReturnExpectedMapping() {
        // MsANSI / Letter 계열
        assertMediaSizeMapping(MediaSizeId.MsANSI_A_8point5x11in, SubUnitInfo.MediaSize.LETTER);
        assertMediaSizeMapping(MediaSizeId.MsANSI_A_Rotated_8point5x11in, SubUnitInfo.MediaSize.LETTER_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsANSI_B_11x17in, SubUnitInfo.MediaSize.LEDGER);
        assertMediaSizeMapping(MediaSizeId.MsANSI_C_17x22in, SubUnitInfo.MediaSize.ANSI_C_17X22in);
        assertMediaSizeMapping(MediaSizeId.MsANSI_D_22x34in, SubUnitInfo.MediaSize.ANSI_D_22X34in);
        assertMediaSizeMapping(MediaSizeId.MsANSI_E_34x44in, SubUnitInfo.MediaSize.ANSI_E_34X44in);
        assertMediaSizeMapping(MediaSizeId.MsANSI_F_28x40in, SubUnitInfo.MediaSize.ANSI_F_28X40in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_A_9x12in, SubUnitInfo.MediaSize.ARCHITECTURAL_A_9X12in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_B_12x18in, SubUnitInfo.MediaSize.INCH12X18);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_C_18x24in, SubUnitInfo.MediaSize.ARCHITECTURAL_C_18X24in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_D_24x36in, SubUnitInfo.MediaSize.ARCHITECTURAL_D_24X36in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_E_36x48in, SubUnitInfo.MediaSize.ARCHITECTURAL_E_36X48in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_E1_30x42in, SubUnitInfo.MediaSize.ARCHITECTURAL_E1_30X42in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_E2_26x38in, SubUnitInfo.MediaSize.ARCHITECTURAL_E2_26X38in);
        assertMediaSizeMapping(MediaSizeId.MsArchitectural_E3_27x39in, SubUnitInfo.MediaSize.ARCHITECTURAL_E3_27X39in);
        assertMediaSizeMapping(MediaSizeId.MsDIN_2A0_1189x1682mm, SubUnitInfo.MediaSize.DIN_2XA0_1189X1682mm);
        assertMediaSizeMapping(MediaSizeId.MsDIN_4A0_1682x2378mm, SubUnitInfo.MediaSize.DIN_4XA0_1682X2378mm);
        assertMediaSizeMapping(MediaSizeId.MsDL_99x210mm, SubUnitInfo.MediaSize.DL_99X210mm);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_A2_4point375x5point75in, SubUnitInfo.MediaSize.ENVELOPE_A2);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_Catalog1_6x9in, SubUnitInfo.MediaSize.ENVELOPE_CATALOG);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_Comm10_4point125x9point5in, SubUnitInfo.MediaSize.ENVELOPE_COMM10);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_Comm6point75_3point625x6point5in, SubUnitInfo.MediaSize.ENVELOPE_COMM6);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_DL_110x220mm, SubUnitInfo.MediaSize.ENVELOPE_DL);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_Monarch_3point875x7point5in, SubUnitInfo.MediaSize.ENVELOPE_MONARCH);
        assertMediaSizeMapping(MediaSizeId.MsEnvelope_Windsor_3point875x8point875in, SubUnitInfo.MediaSize.ENVELOPE_9);
        assertMediaSizeMapping(MediaSizeId.MsExecutive_7point25x10point5in, SubUnitInfo.MediaSize.EXECUTIVE);
        assertMediaSizeMapping(MediaSizeId.MsExecutive_Rotated_7point25x10point5in, SubUnitInfo.MediaSize.EXECUTIVE_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsFoolscap_8point5x13in, SubUnitInfo.MediaSize.INCH8POINT5X13);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_10x11in, SubUnitInfo.MediaSize.GENERAL_10X11in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_10x13in, SubUnitInfo.MediaSize.GENERAL_10X13in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_10x14in, SubUnitInfo.MediaSize.GENERAL_10X14in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_10x15in, SubUnitInfo.MediaSize.GENERAL_10X15in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_11x12in, SubUnitInfo.MediaSize.GENERAL_11X12in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_11x14in, SubUnitInfo.MediaSize.GENERAL_11X14in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_11x15in, SubUnitInfo.MediaSize.GENERAL_11X15in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_11x19in, SubUnitInfo.MediaSize.GENERAL_11X19in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_12x12in, SubUnitInfo.MediaSize.GENERAL_12X12in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_12x14in, SubUnitInfo.MediaSize.GENERAL_12X14in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_12x19in, SubUnitInfo.MediaSize.GENERAL_12X19in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_3point5x5in, SubUnitInfo.MediaSize.GENERAL_3POINT5X5in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_3x5in, SubUnitInfo.MediaSize.GENERAL_3X5in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_4x12in, SubUnitInfo.MediaSize.GENERAL_4X12in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_4x6in, SubUnitInfo.MediaSize.GENERAL_4X6in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_4x8in, SubUnitInfo.MediaSize.GENERAL_4X8in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_5x7in, SubUnitInfo.MediaSize.GENERAL_5X7in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_5x8in, SubUnitInfo.MediaSize.GENERAL_5X8in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_6x8in, SubUnitInfo.MediaSize.GENERAL_6X8in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_7x9in, SubUnitInfo.MediaSize.GENERAL_7X9in);
        assertMediaSizeMapping(MediaSizeId.MsGeneral_9x11in, SubUnitInfo.MediaSize.GENERAL_9X11in);
        assertMediaSizeMapping(MediaSizeId.MsGovt_Legal_8x13in, SubUnitInfo.MediaSize.GOVT_LEGAL);
        assertMediaSizeMapping(MediaSizeId.MsGovt_Letter_8x10in, SubUnitInfo.MediaSize.GOVT_LETTER);
        assertMediaSizeMapping(MediaSizeId.MsInvoice_5point5x8point5in, SubUnitInfo.MediaSize.STATEMENT);
        assertMediaSizeMapping(MediaSizeId.MsISO_A0_841x1189mm, SubUnitInfo.MediaSize.A0);
        assertMediaSizeMapping(MediaSizeId.MsISO_A1_594x841mm, SubUnitInfo.MediaSize.A1);
        assertMediaSizeMapping(MediaSizeId.MsISO_A2_420x594mm, SubUnitInfo.MediaSize.A2);
        assertMediaSizeMapping(MediaSizeId.MsISO_A3_297x420mm, SubUnitInfo.MediaSize.A3);
        assertMediaSizeMapping(MediaSizeId.MsISO_A4_210x297mm, SubUnitInfo.MediaSize.A4);
        assertMediaSizeMapping(MediaSizeId.MsISO_A4_Rotated_210x297mm, SubUnitInfo.MediaSize.A4_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsISO_A5_148x210mm, SubUnitInfo.MediaSize.A5);
        assertMediaSizeMapping(MediaSizeId.MsISO_A5_Rotated_148x210mm, SubUnitInfo.MediaSize.A5_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsISO_A6_105x148mm, SubUnitInfo.MediaSize.A6);
        assertMediaSizeMapping(MediaSizeId.MsISO_A7_74x105mm, SubUnitInfo.MediaSize.A7);
        assertMediaSizeMapping(MediaSizeId.MsISO_A8_52x74mm, SubUnitInfo.MediaSize.A8);
        assertMediaSizeMapping(MediaSizeId.MsISO_A9_37x52mm, SubUnitInfo.MediaSize.A9);
        assertMediaSizeMapping(MediaSizeId.MsISO_A10_26x37mm, SubUnitInfo.MediaSize.A10);
        assertMediaSizeMapping(MediaSizeId.MsISO_B0_1000x1414mm, SubUnitInfo.MediaSize.B0);
        assertMediaSizeMapping(MediaSizeId.MsISO_B1_707x1000mm, SubUnitInfo.MediaSize.B1);
        assertMediaSizeMapping(MediaSizeId.MsISO_B2_500x707mm, SubUnitInfo.MediaSize.B2);
        assertMediaSizeMapping(MediaSizeId.MsISO_B3_353x500mm, SubUnitInfo.MediaSize.B3);
        assertMediaSizeMapping(MediaSizeId.MsISO_B4_250x353mm, SubUnitInfo.MediaSize.B4);
        assertMediaSizeMapping(MediaSizeId.MsISO_B5_176x250mm, SubUnitInfo.MediaSize.B5);
        assertMediaSizeMapping(MediaSizeId.MsISO_B6_125x176mm, SubUnitInfo.MediaSize.B6);
        assertMediaSizeMapping(MediaSizeId.MsISO_B7_88x125mm, SubUnitInfo.MediaSize.B7);
        assertMediaSizeMapping(MediaSizeId.MsISO_B8_62x88mm, SubUnitInfo.MediaSize.B8);
        assertMediaSizeMapping(MediaSizeId.MsISO_B9_44x62mm, SubUnitInfo.MediaSize.B9);
        assertMediaSizeMapping(MediaSizeId.MsISO_B10_31x44mm, SubUnitInfo.MediaSize.B10);
        assertMediaSizeMapping(MediaSizeId.MsISO_C0_917x1297mm, SubUnitInfo.MediaSize.C0);
        assertMediaSizeMapping(MediaSizeId.MsISO_C1_648x917mm, SubUnitInfo.MediaSize.C1);
        assertMediaSizeMapping(MediaSizeId.MsISO_C2_458x648mm, SubUnitInfo.MediaSize.C2);
        assertMediaSizeMapping(MediaSizeId.MsISO_C3_324x458mm, SubUnitInfo.MediaSize.C3);
        assertMediaSizeMapping(MediaSizeId.MsISO_C4_229x324mm, SubUnitInfo.MediaSize.C4);
        assertMediaSizeMapping(MediaSizeId.MsISO_C5_162x229mm, SubUnitInfo.MediaSize.C5);
        assertMediaSizeMapping(MediaSizeId.MsISO_C6_114x162mm, SubUnitInfo.MediaSize.C6);
        assertMediaSizeMapping(MediaSizeId.MsISO_C7_81x114mm, SubUnitInfo.MediaSize.C7);
        assertMediaSizeMapping(MediaSizeId.MsISO_C8_57x81mm, SubUnitInfo.MediaSize.C8);
        assertMediaSizeMapping(MediaSizeId.MsISO_C9_40x57mm, SubUnitInfo.MediaSize.C9);
        assertMediaSizeMapping(MediaSizeId.MsISO_C10_28x40mm, SubUnitInfo.MediaSize.C10);
        assertMediaSizeMapping(MediaSizeId.MsJBusinessCard_55x91mm, SubUnitInfo.MediaSize.BUSINESS_CARD);
        assertMediaSizeMapping(MediaSizeId.MsJDoublePostcard_148x200mm, SubUnitInfo.MediaSize.JDOUBLE_POSTCARD);
        assertMediaSizeMapping(MediaSizeId.MsJDoublePostcard_Rotated_148x200mm, SubUnitInfo.MediaSize.JDOUBLE_POSTCARD_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B0_1030x1456mm, SubUnitInfo.MediaSize.JB0);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B1_728x1030mm, SubUnitInfo.MediaSize.JB1);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B2_515x728mm, SubUnitInfo.MediaSize.JB2);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B3_364x515mm, SubUnitInfo.MediaSize.JB3);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B4_257x364mm, SubUnitInfo.MediaSize.JB4);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B5_182x257mm, SubUnitInfo.MediaSize.JB5);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B5_Rotated_182x257mm, SubUnitInfo.MediaSize.JB5_ROTATE);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B6_128x182mm, SubUnitInfo.MediaSize.JB6);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B7_91x128mm, SubUnitInfo.MediaSize.JB7);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B8_64x91mm, SubUnitInfo.MediaSize.JB8);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B9_45x64mm, SubUnitInfo.MediaSize.JB9);
        assertMediaSizeMapping(MediaSizeId.MsJIS_B10_32x45mm, SubUnitInfo.MediaSize.JB10);
        assertMediaSizeMapping(MediaSizeId.MsJIS_Chou3_120x235mm, SubUnitInfo.MediaSize.JCHOU3);
        assertMediaSizeMapping(MediaSizeId.MsJIS_Chou4_90x205mm, SubUnitInfo.MediaSize.JCHOU4);
        assertMediaSizeMapping(MediaSizeId.MsJIS_Exec_216x330mm, SubUnitInfo.MediaSize.JEXEC);
        assertMediaSizeMapping(MediaSizeId.MsJIS_Kaku2_240x332mm, SubUnitInfo.MediaSize.JKAKU2);
        assertMediaSizeMapping(MediaSizeId.MsJPostcard_100x148mm, SubUnitInfo.MediaSize.JPOSTCARD);
        assertMediaSizeMapping(MediaSizeId.MsK16_184x260mm, SubUnitInfo.MediaSize.K16_184X260mm);
        assertMediaSizeMapping(MediaSizeId.MsK16_195x270mm, SubUnitInfo.MediaSize.K16);
        assertMediaSizeMapping(MediaSizeId.MsK16_197x273mm, SubUnitInfo.MediaSize.PK16);
        assertMediaSizeMapping(MediaSizeId.MsK8_260x368mm, SubUnitInfo.MediaSize.K8_260X368mm);
        assertMediaSizeMapping(MediaSizeId.MsK8_270x390mm, SubUnitInfo.MediaSize.K8);
        assertMediaSizeMapping(MediaSizeId.MsK8_273x394mm, SubUnitInfo.MediaSize.PK8);
        assertMediaSizeMapping(MediaSizeId.MsLegal_8point5x14in, SubUnitInfo.MediaSize.LEGAL);
        assertMediaSizeMapping(MediaSizeId.MsLongScan_8point5x34in, SubUnitInfo.MediaSize.LONG_SCAN);
        assertMediaSizeMapping(MediaSizeId.MsMutsugiri_203x254mm, SubUnitInfo.MediaSize.MUTSUGIRI);
        assertMediaSizeMapping(MediaSizeId.MsOficio_216x340mm, SubUnitInfo.MediaSize.OFICIO);
        assertMediaSizeMapping(MediaSizeId.MsRA0_860x1220mm, SubUnitInfo.MediaSize.RA0);
        assertMediaSizeMapping(MediaSizeId.MsRA1_610x860mm, SubUnitInfo.MediaSize.RA1);
        assertMediaSizeMapping(MediaSizeId.MsRA2_430x610mm, SubUnitInfo.MediaSize.RA2);
        assertMediaSizeMapping(MediaSizeId.MsRA3_305x430mm, SubUnitInfo.MediaSize.RA3);
        assertMediaSizeMapping(MediaSizeId.MsRA4_215x305mm, SubUnitInfo.MediaSize.RA4);
        assertMediaSizeMapping(MediaSizeId.MsSRA1_640x900mm, SubUnitInfo.MediaSize.SRA1);
        assertMediaSizeMapping(MediaSizeId.MsSRA2_450x640mm, SubUnitInfo.MediaSize.SRA2);
        assertMediaSizeMapping(MediaSizeId.MsSRA3_320x450mm, SubUnitInfo.MediaSize.SRA3);
        assertMediaSizeMapping(MediaSizeId.MsSRA4_225x320mm, SubUnitInfo.MediaSize.SRA4);
        assertMediaSizeMapping(MediaSizeId.MsSuper_B_13x19in, SubUnitInfo.MediaSize.SUPER_B);
        assertMediaSizeMapping(MediaSizeId.MsAny, SubUnitInfo.MediaSize.AUTO);
        assertMediaSizeMapping(MediaSizeId.MsMatchOriginal, SubUnitInfo.MediaSize.MATCH_ORIGINAL);
        assertMediaSizeMapping(MediaSizeId.MsMixedLetterLegal, SubUnitInfo.MediaSize.MIXED_LETTER_LEGAL);
        assertMediaSizeMapping(MediaSizeId.MsMixedLetterLedger, SubUnitInfo.MediaSize.MIXED_LETTER_LEDGER);
        assertMediaSizeMapping(MediaSizeId.MsMixedA4A3, SubUnitInfo.MediaSize.MIXED_A3_A4);
        assertMediaSizeMapping(MediaSizeId.MsCustom, SubUnitInfo.MediaSize.CUSTOM);
        assertMediaSizeMapping(MediaSizeId.MsOther, SubUnitInfo.MediaSize.OTHER);
        assertMediaSizeMapping(MediaSizeId.MsUnknown, SubUnitInfo.MediaSize.UNKNOWN);
        assertMediaSizeMapping(MediaSizeId.MsUnknownEnvelope, SubUnitInfo.MediaSize.UNKNOWN_ENVELOP);
        assertMediaSizeMapping(MediaSizeId.MsISO_INDEXCARD_100x150mm, SubUnitInfo.MediaSize.INDEXCARD);
        assertMediaSizeMapping(MediaSizeId.MsAnyCustom, SubUnitInfo.MediaSize.ANY_CUSTOM);
    }

    /**
     * Test that verifies that passing null to getMediaSizeValue returns OTHER.
     */
    @Test
    public void GivenDeviceUsageTypeMapping_WhenGetMediaSizeValueCalledWithNull_ShouldReturnOther() {
        String expected = SubUnitInfo.MediaSize.OTHER.getValue();
        String actual = DeviceUsageAdapter.getMediaSizeValue(null);
        assertEquals(expected, actual);
    }
}
