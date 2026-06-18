package com.hp.workpath.apitest.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.device.DeviceAttribute;
import com.hp.workpath.api.device.DeviceService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DeviceServiceAllAttributesTest {
    final static String IPV4_REGEX = "^(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)"
            + "(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}$";
    final static String MAC_REGEX = "^(?i)[0-9a-f]{2}(:[0-9a-f]{2}){5}$";
    final static String FIRMWARE_VERSION_REGEX = "^6\\..*$";
    final static String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a" +
            "-fA-F]{12}$";

    private final DeviceAttribute attribute;
    private final Class<?> valueType;
    private final String expected;

    public DeviceServiceAllAttributesTest(DeviceAttribute attribute, Class<?> valueType, String expected) {
        this.attribute = attribute;
        this.valueType = valueType;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {DeviceAttribute.DA_NETWORK_HOSTNAME, String.class, null},
                {DeviceAttribute.DA_NETWORK_IPADDRESS, String.class, IPV4_REGEX},
                {DeviceAttribute.DA_NETWORK_MACADDRESS, String.class, MAC_REGEX},
                {DeviceAttribute.DA_SYSTEM_MODELNAME, String.class, null},
                {DeviceAttribute.DA_SYSTEM_SERIALNUMBER, String.class, null},
                {DeviceAttribute.DA_SYSTEM_LANGUAGE, String.class, "en-US"},
                {DeviceAttribute.DA_SYSTEM_LANGUAGE_CAPABILITY, String[].class, null},
                {DeviceAttribute.DA_SYSTEM_FIRMWARE_VERSION, String.class, FIRMWARE_VERSION_REGEX},
                {DeviceAttribute.DA_SYSTEM_DEVICE_ID, String.class, UUID_REGEX},
                {DeviceAttribute.DA_SYSTEM_FORMATTER_SERIAL_NUMBER, String.class, null},
                {DeviceAttribute.DA_SYSTEM_PRODUCT_NUMBER, String.class, null},
                {DeviceAttribute.DA_DEVICE_VENDOR, String.class, "HP"}
        });
    }

    @BeforeClass
    public static void initSdk() throws SsdkUnsupportedException {
        Context ctx = ApplicationProvider.getApplicationContext();
        Workpath.getInstance().initialize(ctx);
    }

    @Test
    public void DeviceService_getString_ReturnDeviceAttributeValue() {
        Context ctx = ApplicationProvider.getApplicationContext();
        Result result = new Result();

        if (valueType == String.class) {
            String value = DeviceService.getString(ctx, attribute, result);
            assertTrue("Result not OK for " + attribute.name() + ": " + result, result.getCode() == Result.RESULT_OK);
            assertNotNull(attribute.name(), value);
            if (expected != null) {
                if (expected.startsWith("^") && expected.endsWith("$")) {
                    // expected is regex then match regex
                    boolean matches = value.matches(expected);
                    assertTrue("Invalid " + attribute.name() + " format: " + value, matches);
                } else {
                    assertEquals(attribute.name(), expected, value);
                }
            }
        } else if (valueType == String[].class) {
            String[] values = DeviceService.getStringArray(ctx, attribute, result);
            assertTrue("Result not OK for " + attribute.name() + ": " + result, result.getCode() == Result.RESULT_OK);
            assertNotNull(attribute.name(), values);
        } else {
            throw new IllegalStateException("Unhandled type: " + valueType);
        }
    }
}
