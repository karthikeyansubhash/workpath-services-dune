package com.hp.jetadvantage.link.device.services.converter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hp.ext.types.protocol.Signed64;
import com.hp.ext.types.protocol.Unsigned32;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RangeTypeConverterUnitTest {

    private RangeTypeConverter<TestEnum> converter;

    @Before
    public void setUp() {
        converter = new RangeTypeConverter<>(3, TestEnum.class, TestEnum.DEFAULT, TestEnum.OPTION0, TestEnum.OPTION2);
        converter.setRange(new Signed64(0L), new Signed64(100L), new Unsigned32(10L));
    }

    @Test
    public void GivenRangeTypeConverter_WhenConvertEtoWIsCalled_ThenReturnConvertedWorkpathEnum() {
        Unsigned32 key = new Unsigned32(20L);
        TestEnum result = converter.convertEtoW(key);
        assertEquals(TestEnum.OPTION0, result);

        key = new Unsigned32(50L);
        result = converter.convertEtoW(key);
        assertEquals(TestEnum.OPTION1, result);

        key = new Unsigned32(100L);
        result = converter.convertEtoW(key);
        assertEquals(TestEnum.OPTION2, result);
    }

    @Test
    public void GivenRangeTypeConverter_WhenConvertEtoWIsCalledWithDefaultIfNotFound_ThenReturnDefaultEnumIfNotFoundInRange() {
        Unsigned32 key = new Unsigned32(200L);
        TestEnum result = converter.convertEtoW(key, true);
        assertEquals(TestEnum.DEFAULT, result);
    }

    @Test
    public void GivenRangeTypeConverter_WhenConvertWtoEIsCalled_ThenReturnConvertedE2Value() {
        Unsigned32 result = converter.convertWtoE(TestEnum.OPTION0);
        assertEquals("The lower boundary of the E2 range, 0, is expected",
                new Unsigned32(0L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION1);
        assertEquals("The median of the E2 range, 50, is expected",
                new Unsigned32(50L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION2);
        assertEquals("The upper boundary of the E2 range, 100, is expected",
                new Unsigned32(100L).getValue(), result.getValue());
    }

    @Test
    public void GivenRangeTypeConverter_WhenSetRangeIsCalledWithStep9AndConvertWtoEIsCalled_ThenReturnConvertedE2Value() {
        converter.setRange(new Signed64(1L), new Signed64(199L), new Unsigned32(9L));
        Unsigned32 result = converter.convertWtoE(TestEnum.OPTION0);
        assertEquals("The lower boundary of the E2 range, 1, is expected",
                new Unsigned32(1L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION1);
        assertEquals("The median of the E2 range with 9 range steps, 100, is expected",
                new Unsigned32(100L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION2);
        assertEquals("The highest value of the E2 range, 199, is expected",
                new Unsigned32(199L).getValue(), result.getValue());
    }

    @Test
    public void GivenRangeTypeConverter_WhenSetRangeIsCalledWithStep1AndConvertWtoEIsCalled_ThenReturnConvertedE2Value() {
        converter.setRange(new Signed64(1L), new Signed64(3L), new Unsigned32(1L));
        Unsigned32 result = converter.convertWtoE(TestEnum.OPTION0);
        assertEquals("The lower boundary of the E2 range, 1, is expected",
                new Unsigned32(1L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION1);
        assertEquals("The median of the E2, 2, is expected",
                new Unsigned32(2L).getValue(), result.getValue());

        result = converter.convertWtoE(TestEnum.OPTION2);
        assertEquals("The highest value of the E2 range, 3, is expected",
                new Unsigned32(3L).getValue(), result.getValue());
    }

    @Test
    public void GivenRangeTypeConverter_WhenGetE2TypeIsCalled_ThenReturnE2Type() {
        assertEquals(Unsigned32.class, converter.getE2Type());
    }


    @Test
    public void GivenRangeTypeConverter_WhenSetRangeIsCalledAndConvertEtoW_ThenReturnWorkpathEnum() {
        converter.setRange(new Signed64(0L), new Signed64(10L), new Unsigned32(5L));
        Unsigned32 key = new Unsigned32(10L);
        TestEnum result = converter.convertEtoW(key);
        assertEquals("Expected output is the highest Workpath Enum for the E2 upper boundary input.",
                TestEnum.OPTION2, result);


        key = new Unsigned32(0L);
        result = converter.convertEtoW(key);
        assertEquals("Expected output is the lowest Workpath Enum for the E2 low boundary input.",
                TestEnum.OPTION0, result);

        key = new Unsigned32(5L);
        result = converter.convertEtoW(key);
        assertEquals("Expected output is the median Workpath Enum for the E2 low boundary input.",
                TestEnum.OPTION1, result);
    }

    @Test
    public void GivenRangeTypeConverter_WhenSetRangeIsCalledAndConvertEtoWIsCalledWithRangeStep_ThenReturnWorkpathEnum() {
        converter.setRange(new Signed64(1L), new Signed64(200L), new Unsigned32(8L));
        Unsigned32 key = new Unsigned32(200L);
        TestEnum result = converter.convertEtoW(key);
        assertEquals("Expected output is the highest Workpath Enum for the E2 upper boundary input.",
                TestEnum.OPTION2, result);


        key = new Unsigned32(1L);
        result = converter.convertEtoW(key);
        assertEquals("Expected output is the lowest Workpath Enum for the E2 low boundary input.",
                TestEnum.OPTION0, result);

        key = new Unsigned32(97L);
        result = converter.convertEtoW(key);
        assertEquals("Expected output is the median Workpath Enum for the E2 median value input.",
                TestEnum.OPTION1, result);
    }

    @Test
    public void GivenRangeTypeConverter_WhenGetAllWorkpathTypesIsCalled_ThenReturnWorkpathEnumList() {
        List<TestEnum> result = converter.getAllWorkpathEnumsExceptDefault();
        assertFalse(result.contains(TestEnum.DEFAULT));
        assertTrue(result.contains(TestEnum.OPTION0));
        assertTrue(result.contains(TestEnum.OPTION1));
        assertTrue(result.contains(TestEnum.OPTION2));
    }

    @Test
    public void GivenRangeTypeConverter_WhenSetRangeIsCalledWithInvalidParam_ThenThrowException() {
        try {
            converter.setRange(new Signed64(0L), new Signed64(0L), new Unsigned32(5L));
            assertTrue("", false);
        } catch (IllegalArgumentException e) {
            assertTrue("The range is invalid. The upper boundary must be greater than the lower boundary.", true);
        }
    }
    private enum TestEnum {
        DEFAULT, OPTION0, OPTION1, OPTION2
    }
}
