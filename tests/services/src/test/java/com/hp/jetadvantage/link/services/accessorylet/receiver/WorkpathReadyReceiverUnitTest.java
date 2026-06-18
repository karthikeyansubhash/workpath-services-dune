package com.hp.jetadvantage.link.services.accessorylet.receiver;

import static com.hp.jetadvantage.link.common.constants.CommonConstants.BroadcastActions.WORKPATH_SERVICE_READY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class WorkpathReadyReceiverUnitTest {
    @Mock
    Context mockContext;

    @Mock
    Intent mockIntent;

    @Test
    public void GivenWorkpathReadyReceiver_WhenOnReceiveCalled_ThenCallStartForegroundService() {
        when(mockIntent.getAction()).thenReturn(WORKPATH_SERVICE_READY);
        WorkpathReadyReceiver readyReceiver = new WorkpathReadyReceiver();

        readyReceiver.onReceive(mockContext, mockIntent);

        verify(mockContext).startForegroundService(any());
        verify(mockContext).sendBroadcast(any());
    }

    @Test
    public void GivenWorkpathReadyReceiver_WhenOnReceiveCalledWithEmptyAction_ThenDoNothing() {
        when(mockIntent.getAction()).thenReturn(null);
        WorkpathReadyReceiver readyReceiver = new WorkpathReadyReceiver();

        readyReceiver.onReceive(mockContext, mockIntent);

        verify(mockContext, never()).startForegroundService(any());
        verify(mockContext, never()).sendBroadcast(any());
    }

    @Test
    public void GivenWorkpathReadyReceiver_WhenOnReceiveCalledWithNullIntent_ThenDoNothing() {
        WorkpathReadyReceiver readyReceiver = new WorkpathReadyReceiver();

        readyReceiver.onReceive(mockContext, null);

        verify(mockContext, never()).startForegroundService(any());
        verify(mockContext, never()).sendBroadcast(any());
    }
}
