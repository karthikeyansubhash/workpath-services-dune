package com.hp.jetadvantage.link.services.configlet.service;

import static com.hp.jetadvantage.link.device.services.standard.services.AppChannelMessageTestHelper.makeTestChannelPayloadValue;
import static com.hp.jetadvantage.link.device.services.standard.services.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.ext.types.solutionManager.SolutionNotification;
import com.hp.jetadvantage.link.api.config.Configlet;
import com.hp.jetadvantage.link.common.PlatformTestHelper;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class ConfigNotificationServiceInstrumentedTest {
    @Mock
    StandardWebsocketCallbackService mockWSCallbackService;

    private Context context;
    private ConfigNotificationService service;

    @Before
    public void setup() throws TimeoutException {
        PlatformTestHelper.setTestMode(true);
        MockitoAnnotations.openMocks(this);
        context = ApplicationProvider.getApplicationContext();
        service = new ConfigNotificationService();
        //when(mockWSCallbackService.getApplicationContext()).thenReturn(context);
    }

    @After
    public void tearDown() {
        // no cleanup needed for ServiceTestRule
    }

    @Test
    public void processSolutionNotification_sendsConfigChangeBroadcast() throws Exception {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String typeGUN = new SolutionNotification().getTypeGUN();
        String packageId = "com.hp.jetadvantage.link.test";
        String sampleSolutionNotification = "{\"notificationType\":\"ntConfigurationModified\"}";

        CountDownLatch latch = new CountDownLatch(1);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (Configlet.CONFIG_CHANGE_ACTION.equals(intent.getAction())) {
                    latch.countDown();
                }
            }
        };
        context.registerReceiver(receiver, new IntentFilter(Configlet.CONFIG_CHANGE_ACTION));

        ConfigNotificationService.start(context);

        //inject test solution notification message
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWSCallbackService);
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId,
                StandardDeviceSolutionManager.E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN));
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleSolutionNotification));


        assertTrue("Expected CONFIG_CHANGE_ACTION broadcast", latch.await(2, TimeUnit.SECONDS));
        context.unregisterReceiver(receiver);
    }
}
