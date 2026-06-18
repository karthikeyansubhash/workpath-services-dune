package com.hp.workpath.apitest;

public class TestApp {
    static public class AccessoryRR{
        static final public int OWNED_VENDOR_ID = 3333;
        static final public int OWNED_PRODUCT_ID = 77;
        static final public int SHARED_VENDOR_ID = 5555;
        static final public int SHARED_PRODUCT_ID = 88;

        // see tests\api\apiTest\src\androidTest\resources\accessoryService\SimulatedHidDevice_77_3333.json
        static final public int OWNED_FEATURE_REPORT_LENGTH = 10;
        static final public int OWNED_INPUT_REPORT_LENGTH = 42;
        static final public int OWNED_OUTPUT_REPORT_LENGTH = 12;
    }
}
