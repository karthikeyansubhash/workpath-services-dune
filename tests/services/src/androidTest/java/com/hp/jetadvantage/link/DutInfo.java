package com.hp.jetadvantage.link;

public class DutInfo {
    //External IP address of the device under test
    public static String IP = "15.26.182.185";

    //HTTP request host header value : generally it's same as the external IP address of the device under test.
    //But if the target device (or simulator) is behind NAT, set the real device IP address inside NAT to bypass host header check of the device's web server.
    public static String HostHeader = "15.26.182.185";
    public static String TOKEN = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7YWM1MTk5MDQtMWFiOC00OGM3LWI4OWMtODMxOWNjOTQ3MDA1fSJ9..eS4QhrUvMGTr7t86.9wnixe2JWNxUsOYctdTRdmrP6JylLaY0BiGEJq7431zn-_8H3xLXDZywf7E0_FgJhipY0-iM4Gj3GGf4YHcekyxAup8lu78NU00gwifdE7N5cEq_sxNEdpjxwE2hhb9AtfZmOe0o2A9DDE4wv52DVxJTYFIhFwN9PHSTni8Gvm2C5x-31DpykynyyZlDwkppOAyvSCjvvBovZ2FJfrhWsEPYXqIfGMcb-pemb4GXaodYYZbJubobPRmDnpe7h9eB-bW5Xz7wQYreVoHhhBGzfwukTKTDQb9pWOrmjRF4ThjcGoBZqkUESrrxjqWraMiJoOyNCdFUDyLMyzPmAuSWYOrGnI1WOuJ9jUBgVUtoBLJEC1zV_-JfnmVUn8EpScdN1nAuNXz2Wi_Mnbrj_M9rlqPkyh0db46kA9NoCx3dim8Ik-FKXVdupFvJFi8ih-DYYMKBLp4xMUE7dU4JItWscwr5JJaxhQ.Ac9WngKM3a2oqOI11XTC2Q";

    // Pre-installed test app information for Workpath PI tests
    // Some instrumented tests require installing `tests\api\apiTest\bdl\Test-WorkpathAPIs-debug.bdl` on a device under test when executing tests manually
    public static final String PI_TEST_PACKAGE_NAME = "com.hp.workpath.apitest";
    public static final String PI_TEST_STATISTICS_PACKAGE_NAME = "com.hp.workpath.sample.statisticsample";
    public static final String PI_TEST_SOLUTION_ID = "b081727d-bb2e-46f6-94c0-5f17c4587b16"; //solutionId
    public static final String PI_TEST_APPLICATION_AGENT_ID = "a8c33621-ee8d-4680-8279-df74b49fa279"; //applicationAgentRegistrationRecord
    public static final String PI_TEST_SUPPLIES_AGENT_ID = "19cb99a7-f9fd-44db-ae16-0b07bd9b65fb"; //suppliesAgentRegistrationRecord
    public static final String PI_TEST_PRINT_AGENT_ID = "064740ed-c305-4ef7-8ff7-12200d1724b5"; //printJobAgentRegistrationRecord
    public static final String PI_TEST_SCAN_AGENT_ID = "4179b4ef-b73f-4e7b-a7a4-7dc46c14deba"; //scanJobAgentRegistrationRecord
    public static final String PI_TEST_COPY_AGENT_ID = "fab6247f-d687-453d-8fbe-90473d66f179"; //copyAgentRegistrationRecord
    public static final String PI_TEST_DEVICE_USAGE_AGENT_ID = "e6d29da1-c3e5-4d2b-b61d-e75971440569"; //deviceUsageAgentRegistrationRecord
    public static final String PI_TEST_USB_ACCESSORIES_AGENT_ID = "eb3e01b9-b421-40ef-9f2e-42f1742b16ab"; //usbAccessoriesRegistrationRecord
    public static final String PI_TEST_DEVICEUSAGE_AGENT_ID = "e6d29da1-c3e5-4d2b-b61d-e75971440569"; //deviceUsageRegistrationRecord
    public static final String PI_TEST_AUTHENTICATION_AGENT_ID = "a3e8701c-d508-466f-ae31-0a09bf1f1520"; //authenticationAgentRegistrationRecord
    public static final String PI_TEST_STATISTICS_AGENT_ID = "6fa438e8-fdbc-4dea-ab6c-4cdaa4aef555"; //jobStatisticsAgentRegistrationRecord

}
