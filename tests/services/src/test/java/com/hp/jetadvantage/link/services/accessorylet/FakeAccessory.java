package com.hp.jetadvantage.link.services.accessorylet;

import com.hp.ext.service.usbAccessories.Accessories;
import com.hp.ext.service.usbAccessories.Accessory;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;
import com.hp.ext.service.usbAccessories.RegistrationKind;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.ext.types.usb.UsbString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakeAccessory {
    public final static String OPEN_HID_ID = "3e9f6d2a-8c47-4b1e-a5d0-f3b7c9e2d8a1";
    public final static String VALID_ACCESSORY_ID = "5bbcbd7c-dd6d-41c9-a8c3-809d5c7a237c";
    public final static int VALID_VID = 1008;
    public final static int VALID_PID = 69;
    public final static String VALID_SERIAL_NUMBER = "123456";
    public final static String VALID_MANUFACTURER_NAME = "HP";
    public final static String VALID_PRODUCT_NAME = "X6001";

    public final static String TEST_APP_PACKAGE_NAME = "com.hp.workpath.test";
    public final static int FEATURE_REPORT_LENGTH = 10;
    public final static int INPUT_REPORT_LENGTH = 100;
    public final static int OUTPUT_REPORT_LENGTH = 50;
    public final static boolean REPORT_READING_ACTIVE = true;

    public final static String VALID_SHARED_ACCESSORY_ID = "d5a3f8b7-2c4e-4a2b-9f64-7e1d8f5a9c3e";
    public final static String VALID_SHARED_SERIAL_NUMBER = "1";
    public final static int VALID_SHARED_VID = 2000;
    public final static int VALID_SHARED_PID = 77;

    public final static String EMPTY_SERIAL_ACCESSORY_ID = "4d19383f-31e1-4699-9577-999c0913bfb1";

    public static Accessory getSampleOwnedAccessoryFromDevice() {
        Accessory accessory1 = new Accessory();
        accessory1.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory1.setProductId(new Unsigned16(VALID_PID));
        accessory1.setVendorId(new Unsigned16(VALID_VID));
        accessory1.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory1.setRegistration(RegistrationKind.RkOwned);
        accessory1.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));
        return accessory1;
    }

    public static OpenHIDAccessory createSampleOpenHIDAccessory() {
        OpenHIDAccessory openHidAccessory = new OpenHIDAccessory();
        openHidAccessory.setOpenHIDAccessoryID(UUID.fromString(OPEN_HID_ID));
        openHidAccessory.setFeatureReportLength(new com.hp.ext.types.protocol.Byte(FEATURE_REPORT_LENGTH));
        openHidAccessory.setInputReportLength(new com.hp.ext.types.protocol.Byte(INPUT_REPORT_LENGTH));
        openHidAccessory.setOutputReportLength(new com.hp.ext.types.protocol.Byte(OUTPUT_REPORT_LENGTH));
        openHidAccessory.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));
        openHidAccessory.setReportReadingActive(REPORT_READING_ACTIVE);
        return openHidAccessory;
    }

    public static Accessories getSample1AccessoriesFromDevice() {
        Accessories accessories = new Accessories();
        Accessory accessory1 = new Accessory();
        accessory1.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory1.setProductId(new Unsigned16(VALID_PID));
        accessory1.setVendorId(new Unsigned16(VALID_VID));
        accessory1.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory1.setRegistration(RegistrationKind.RkOwned);
        accessory1.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));
        List<Accessory> members = new ArrayList<>();
        members.add(accessory1);
        accessories.setMembers(members);
        return accessories;
    }

    public static Accessories getSampleSharedAccessoriesFromDevice() {
        Accessories accessories = new Accessories();
        Accessory accessory1 = new Accessory();
        accessory1.setAccessoryID(UUID.fromString(VALID_ACCESSORY_ID));
        accessory1.setProductId(new Unsigned16(VALID_PID));
        accessory1.setVendorId(new Unsigned16(VALID_VID));
        accessory1.setSerialNumber(new UsbString(VALID_SERIAL_NUMBER));
        accessory1.setRegistration(RegistrationKind.RkShared);
        accessory1.setResourceId(UUID.fromString(VALID_ACCESSORY_ID));

        Accessory accessory2 = new Accessory();
        UUID accessory2Id = UUID.randomUUID();
        accessory2.setAccessoryID(accessory2Id);
        accessory2.setProductId(new Unsigned16(12));
        accessory2.setVendorId(new Unsigned16(34));
        accessory2.setSerialNumber(new UsbString("abcdef"));
        accessory2.setRegistration(RegistrationKind.RkOwned);
        accessory2.setResourceId(accessory2Id);

        Accessory accessory3 = new Accessory();
        accessory3.setAccessoryID(UUID.fromString(VALID_SHARED_ACCESSORY_ID));
        accessory3.setProductId(new Unsigned16(VALID_SHARED_PID));
        accessory3.setVendorId(new Unsigned16(VALID_SHARED_VID));
        accessory3.setRegistration(RegistrationKind.RkShared);
        accessory3.setResourceId(UUID.fromString(VALID_SHARED_ACCESSORY_ID));

        Accessory accessory4 = new Accessory();
        accessory4.setAccessoryID(UUID.fromString(EMPTY_SERIAL_ACCESSORY_ID));
        accessory4.setProductId(new Unsigned16(VALID_PID));
        accessory4.setVendorId(new Unsigned16(VALID_VID));
        accessory4.setRegistration(RegistrationKind.RkShared);
        accessory4.setResourceId(UUID.fromString(EMPTY_SERIAL_ACCESSORY_ID));

        List<com.hp.ext.service.usbAccessories.Accessory> members = new ArrayList<>();
        members.add(accessory4);
        members.add(accessory3);
        members.add(accessory2);
        members.add(accessory1);
        accessories.setMembers(members);
        return accessories;
    }
}
