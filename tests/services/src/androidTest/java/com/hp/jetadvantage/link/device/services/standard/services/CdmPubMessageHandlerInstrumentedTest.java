package com.hp.jetadvantage.link.device.services.standard.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSubscriptionService;
import com.hp.ws.cdm.pubsub.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CdmPubMessageHandlerInstrumentedTest {
    boolean onChangeEventCalled = false;

    @Before
    public void SetUp() {
        onChangeEventCalled = false;
    }

    /**
     * Test case 1: the registered callback should be successfully invoked with valid json reports.
     */
    @Test
    public void GivenCdmPubMessageHandler_WhenOnReceivedCalled_ThenItHandlesMessages() {

        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();
        ICdmCallback callback = new ICdmCallback() {
            @Override
            public void onChangeEvent(List<Message> reports) {
                assertEquals(1, reports.size());
                assertEquals("com.hp.cdm.service.clock.version.1.resource.configuration", reports.get(0).getGun());
                assertEquals("1000001", reports.get(0).geteTag());
                onChangeEventCalled = true;
            }
        };

        TestStandardDeviceSubscriptionService.addCallback("1822922915", callback);

        String data = getSampleCdmPubMessage();
        cdmPubMessageHandler.onReceived(0, data);

        assertTrue(onChangeEventCalled);
    }

    /**
     * Test case 2 : the registered callback should not be invoked when CDM reports is empty
     */
    @Test
    public void GivenCdmPubMessageHandler_WhenOnReceivedCalledWithEmptyReports_ThenCallbackShouldNotBeInvoked() {

        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();
        ICdmCallback callback = new ICdmCallback() {
            @Override
            public void onChangeEvent(List<Message> reports) {
                onChangeEventCalled = true;
            }
        };

        TestStandardDeviceSubscriptionService.addCallback("1822922915", callback);

        String data = getSampleCdmPubMessageEmptyReports();
        cdmPubMessageHandler.onReceived(0, data);

        assertFalse(onChangeEventCalled);
    }

    /**
     * Test case 3 : the registered callback should not be invoked when subscriptionId does not match
     */
    @Test
    public void GivenCdmPubMessageHandler_WhenOnReceivedCalledWithInvalidSubscriptionId_ThenCallbackShouldNotBeInvoked() {

        CdmPubMessageHandler cdmPubMessageHandler = new CdmPubMessageHandler();
        ICdmCallback callback = new ICdmCallback() {
            @Override
            public void onChangeEvent(List<Message> reports) {
                onChangeEventCalled = true;
            }
        };

        TestStandardDeviceSubscriptionService.addCallback("00000000", callback);

        String data = getSampleCdmPubMessageEmptyReports();
        cdmPubMessageHandler.onReceived(0, data);

        assertFalse(onChangeEventCalled);
    }

    private String getSampleCdmPubMessage() {
        return "{\n" +
                "    \"cdmPubMessage\": {\n" +
                "        \"message\": {\n" +
                "            \"continuingReports\": \"true\",\n" +
                "            \"reports\": [\n" +
                "                {\n" +
                "                    \"data\": {\n" +
                "                        \"customTimeZone\": {\n" +
                "                            \"dstEndTime\": {\n" +
                "                                \"dayCount\": 1,\n" +
                "                                \"dayOfWeek\": \"sunday\",\n" +
                "                                \"hour\": 0,\n" +
                "                                \"month\": 1\n" +
                "                            },\n" +
                "                            \"dstOffsetInMinutes\": 0,\n" +
                "                            \"dstStartTime\": {\n" +
                "                                \"dayCount\": 1,\n" +
                "                                \"dayOfWeek\": \"sunday\",\n" +
                "                                \"hour\": 0,\n" +
                "                                \"month\": 1\n" +
                "                            },\n" +
                "                            \"timeZoneOffsetHours\": 0,\n" +
                "                            \"timeZoneOffsetMinutes\": 0\n" +
                "                        },\n" +
                "                        \"dateFormat\": \"ddmmmyyyy\",\n" +
                "                        \"dstEnabled\": \"true\",\n" +
                "                        \"localTime\": \"2024-02-25T15:01:09Z\",\n" +
                "                        \"ntpLocalPortNumber\": 1230,\n" +
                "                        \"ntpServer\": \"\",\n" +
                "                        \"ntpSyncFrequency\": 24,\n" +
                "                        \"systemTime\": \"2024-02-25T15:01:09Z\",\n" +
                "                        \"systemTimeAuthority\": \"adminSet\",\n" +
                "                        \"systemTimeSync\": \"none\",\n" +
                "                        \"timeFormat\": \"hr24\",\n" +
                "                        \"timeZone\": \"Etc/GMT\",\n" +
                "                        \"timeZoneDescription\": \"(GMT) Coordinated Universal Time\",\n" +
                "                        \"version\": \"1.2.0\"\n" +
                "                    },\n" +
                "                    \"eTag\": \"1000001\",\n" +
                "                    \"gSeqNum\": 1,\n" +
                "                    \"gun\": \"com.hp.cdm.service.clock.version.1.resource.configuration\",\n" +
                "                    \"path\": \"/cdm/clock/v1/configuration\",\n" +
                "                    \"updateType\": \"delta\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"subscriptionId\": \"1822922915\",\n" +
                "            \"version\": \"2.3.0\"\n" +
                "        },\n" +
                "        \"subscriptionId\": \"1822922915\"\n" +
                "    }\n" +
                "}";
    }

    private String getSampleCdmPubMessageEmptyReports() {
        return "{\n" +
                "    \"cdmPubMessage\": {\n" +
                "        \"message\": {\n" +
                "            \"continuingReports\": \"false\",\n" +
                "            \"reports\": [\n" +
                "            ],\n" +
                "            \"subscriptionId\": \"1822922915\",\n" +
                "            \"version\": \"2.3.0\"\n" +
                "        },\n" +
                "        \"subscriptionId\": \"1822922915\"\n" +
                "    }\n" +
                "}";
    }

    public class TestStandardDeviceSubscriptionService extends StandardDeviceSubscriptionService {
        public static void addCallback(@NonNull String subscriptionId, @NonNull ICdmCallback callback) {
            StandardDeviceSubscriptionService.addCallback(subscriptionId, callback);
        }
    }
}