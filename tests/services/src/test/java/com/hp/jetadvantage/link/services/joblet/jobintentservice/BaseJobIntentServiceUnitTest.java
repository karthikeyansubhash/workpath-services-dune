package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static android.app.Service.START_NOT_STICKY;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseJobIntentServiceUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    BaseJobIntentService service = new BaseJobIntentService("TestService") {
        @Override
        protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
            return mockStateMachine;
        }

        @Override
        protected boolean cancelJob(Intent intent) {
            // Do nothing
            return false;
        }

        @Override
        protected String getJobCancelAction() {
            return "CANCEL";
        }

        @Override
        protected void reportBusyToApp(Intent intent) {

        }
    };
    @Mock
    private Intent mockIntent;

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOnCreate_ThenStateMachinePoolCreated() {
        service.onCreate();
        assertNotNull(service.getStateMachinePool());
    }

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOnStartCommand_ThenSendsStartMessageWhenIntentHasExtraParams() {
        service.onCreate();

        when(mockIntent.hasExtra(BaseJobIntentService.EXTRA_PARAMS)).thenReturn(true);
        when(mockIntent.getBundleExtra(BaseJobIntentService.EXTRA_PARAMS)).thenReturn(new Bundle());
        when(mockStateMachine.obtainMessage()).thenReturn(new Message());

        int ret = service.onStartCommand(mockIntent, 0, 0);

        verify(mockStateMachine).sendMessage(any(Message.class));
        assertEquals(START_NOT_STICKY, ret);
    }

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOnStartCommandWithCancelIntent_ThenCancelJobShouldBeCalled() {
        when(mockIntent.getAction()).thenReturn("CANCEL");

        BaseJobIntentService serviceSpy = Mockito.spy(service);
        serviceSpy.onCreate();
        int ret = serviceSpy.onStartCommand(mockIntent, 0, 0);

        verify(serviceSpy).cancelJob(mockIntent);
        assertEquals(START_NOT_STICKY, ret);
    }

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOnStartCommandWithEmptyIntent_ThenIgnore() {
        when(mockIntent.getAction()).thenReturn(null);
        when(mockIntent.hasExtra(BaseJobIntentService.EXTRA_PARAMS)).thenReturn(false);

        BaseJobIntentService serviceSpy = Mockito.spy(service);
        serviceSpy.onCreate();

        int ret = serviceSpy.onStartCommand(mockIntent, 0, 0);

        verify(serviceSpy, Mockito.never()).cancelJob(any(Intent.class));
        verify(mockStateMachine, Mockito.never()).sendMessage(any(Message.class));
        assertEquals(START_NOT_STICKY, ret);
    }

}
