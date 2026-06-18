package com.hp.jetadvantage.link.device.services.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.hp.ext.types.imaging.ColorMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTypeConverterUnitTest {
    private DefaultTypeConverter<ColorMode, TestEnum> converter;

    @Before
    public void setUp() {
        converter = new DefaultTypeConverter<>(ColorMode.class, TestEnum.DEFAULT);
        converter.mapEtoW.put(ColorMode.CmAutoDetect, TestEnum.AUTO);
        converter.mapEtoW.put(ColorMode.CmColor, TestEnum.COLOR);
        converter.mapEtoW.put(ColorMode.CmGrayscale, TestEnum.GRAY);
        converter.mapEtoW.put(ColorMode.CmMonochrome, TestEnum.MONO);
    }

    @Test
    public void GivenDefaultTypeConverter_WhenConvertEtoWCalled_ReturnConvertedWorkpathEnum() {
        assertEquals(TestEnum.AUTO, converter.convertEtoW(ColorMode.CmAutoDetect));
        assertEquals(TestEnum.COLOR, converter.convertEtoW(ColorMode.CmColor));
        assertEquals(TestEnum.GRAY, converter.convertEtoW(ColorMode.CmGrayscale));
        assertEquals(TestEnum.MONO, converter.convertEtoW(ColorMode.CmMonochrome));
        assertNull(converter.convertEtoW(ColorMode.CmAutoColorAndGray));
    }

    @Test
    public void GivenDefaultTypeConverter_WhenConvertEtoWWithDefaultCalled_ReturnConvertedWorkpathEnum() {
        assertEquals(TestEnum.AUTO, converter.convertEtoW(ColorMode.CmAutoDetect, true));
        assertEquals(TestEnum.COLOR, converter.convertEtoW(ColorMode.CmColor, true));
        assertEquals(TestEnum.GRAY, converter.convertEtoW(ColorMode.CmGrayscale, true));
        assertEquals(TestEnum.MONO, converter.convertEtoW(ColorMode.CmMonochrome, true));
        assertEquals(TestEnum.DEFAULT, converter.convertEtoW(ColorMode.CmAutoColorAndGray, true));
        assertNull(converter.convertEtoW(ColorMode.CmAutoColorAndGray, false));
        assertEquals(TestEnum.DEFAULT, converter.convertEtoW(null, true));
        assertNull(converter.convertEtoW(null, false));
    }

    @Test
    public void GivenDefaultTypeConverter_WhenConvertWtoECalled_ReturnConvertedE2Value() {
        assertEquals(ColorMode.CmAutoDetect, converter.convertWtoE(TestEnum.AUTO));
        assertEquals(ColorMode.CmColor, converter.convertWtoE(TestEnum.COLOR));
        assertEquals(ColorMode.CmGrayscale, converter.convertWtoE(TestEnum.GRAY));
        assertEquals(ColorMode.CmMonochrome, converter.convertWtoE(TestEnum.MONO));
    }

    @Test
    public void GivenDefaultTypeConverter_WhenGetE2TypCalled_ReturnE2Type() {
        assertEquals(ColorMode.class, converter.getE2Type());
    }

    @Test
    public void GivenDefaultTypeConverter_WhenGetDefaultEnumValueCalled_ReturnWorkpathDefaultEnumValue() {
        assertEquals(TestEnum.DEFAULT, converter.getDefaultEnumValue());
    }

    private enum TestEnum {
        DEFAULT, AUTO, COLOR, GRAY, MONO
    }
}
