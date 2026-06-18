// Copyright 2026 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.receiver;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.api.job.intent.JobProgressRequestIntent;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link JobletReceiver}.
 *
 * Background (JALPINF-2601 / JALPINF-2602):
 * On DUNE, all services run in a single process. Services like
 * StatisticsNotificationObserverService call System.setProperty(SYSTEM_SVC_FLAG, ...)
 * in that shared process. The old code in JobletReceiver silently dropped JOBPROGRESS
 * broadcasts when this flag was set, causing the job-progress dialog to never appear.
 *
 * After the fix, SYSTEM_SVC_FLAG is intentionally ignored for the JOBPROGRESS action.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobletReceiverUnitTest {

    @Mock
    private Context mockContext;
    @Mock
    private Context mockAppContext;

    private JobletReceiver receiver;

    /** Params with a valid jobId used across multiple tests */
    private JobProgressRequestIntent.Params fakeParams;

    @Before
    public void setUp() {
        receiver = new JobletReceiver();
        fakeParams = new JobProgressRequestIntent.Params("test-job-001", null, null, null);
        when(mockContext.getApplicationContext()).thenReturn(mockAppContext);
    }

    @After
    public void tearDown() {
        // Ensure SYSTEM_SVC_FLAG is cleared after each test to avoid test pollution
        System.clearProperty(CommonConstants.SYSTEM_SVC_FLAG);
    }

    // ───────── DUNE 핵심 회귀 케이스 ─────────

    /**
     * SYSTEM_SVC_FLAG 가 세팅되어 있어도 JOBPROGRESS 브로드캐스트는 반드시 처리되어야 한다.
     *
     * 회귀 케이스: DUNE 단일 프로세스 환경에서 StatisticsNotificationObserverService 등이
     * SYSTEM_SVC_FLAG 를 설정하면, 기존 코드는 JobletReceiver.onReceive() 에서 early-return 하여
     * Job 진행 다이얼로그가 나타나지 않았다. (JALPINF-2601, 2602)
     */
    @Test
    public void onReceive_jobProgressAction_withSystemSvcFlagSet_startMonitoringIsCalled() {
        // DUNE 시나리오 재현: 다른 서비스가 같은 프로세스에 SYSTEM_SVC_FLAG 를 설정
        System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, "StatisticsNotificationObserverService");

        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(JobProgressRequestIntent.ACTION);

        try (MockedStatic<JobProgressRequestIntent> mockedIntentHelper = mockStatic(JobProgressRequestIntent.class);
             MockedStatic<SpsPermissionHelper> mockedPermHelper = mockStatic(SpsPermissionHelper.class);
             MockedStatic<JobletService> mockedJobletService = mockStatic(JobletService.class)) {

            mockedIntentHelper.when(() -> JobProgressRequestIntent.getIntentParams(any()))
                    .thenReturn(fakeParams);
            // packageName 이 null 이므로 permission 체크 skip — do-nothing stub 불필요
            mockedJobletService.when(
                    () -> JobletService.startMonitoring(any(), any(), any(), any())
            ).thenAnswer(invocation -> null);

            // 실행
            receiver.onReceive(mockContext, intent);

            // SYSTEM_SVC_FLAG 에 의한 early-return 이 없으므로 startMonitoring 이 반드시 호출되어야 함
            mockedJobletService.verify(
                    () -> JobletService.startMonitoring(
                            any(Context.class),
                            any(String.class),
                            isNull(),
                            isNull()
                    )
            );
        }
    }

    /**
     * SYSTEM_SVC_FLAG 가 비어 있을 때도 정상 동작해야 한다 (기준 케이스).
     */
    @Test
    public void onReceive_jobProgressAction_withoutSystemSvcFlag_startMonitoringIsCalled() {
        // SYSTEM_SVC_FLAG 미설정 상태 확인
        System.clearProperty(CommonConstants.SYSTEM_SVC_FLAG);

        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(JobProgressRequestIntent.ACTION);

        try (MockedStatic<JobProgressRequestIntent> mockedIntentHelper = mockStatic(JobProgressRequestIntent.class);
             MockedStatic<JobletService> mockedJobletService = mockStatic(JobletService.class)) {

            mockedIntentHelper.when(() -> JobProgressRequestIntent.getIntentParams(any()))
                    .thenReturn(fakeParams);
            mockedJobletService.when(
                    () -> JobletService.startMonitoring(any(), any(), any(), any())
            ).thenAnswer(invocation -> null);

            receiver.onReceive(mockContext, intent);

            mockedJobletService.verify(
                    () -> JobletService.startMonitoring(any(), any(), any(), any())
            );
        }
    }

    // ───────── 엣지 케이스 ─────────

    /**
     * intent 가 null 이면 크래시 없이 조용히 종료되어야 한다.
     */
    @Test
    public void onReceive_nullIntent_doesNotCrash() {
        // NPE 없이 통과해야 함
        receiver.onReceive(mockContext, null);
    }

    /**
     * action 이 null 이면 크래시 없이 조용히 종료되어야 한다.
     */
    @Test
    public void onReceive_nullAction_doesNotCrash() {
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(null);

        receiver.onReceive(mockContext, intent);
        // startMonitoring 이 호출되지 않아야 함 — MockedStatic without verify = no interaction
    }

    /**
     * 알 수 없는 action 은 JOBPROGRESS 처리 분기를 타지 않으므로
     * startMonitoring 이 호출되어서는 안 된다.
     */
    @Test
    public void onReceive_unknownAction_startMonitoringIsNotCalled() {
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn("com.hp.jetadvantage.some.OTHER_ACTION");

        try (MockedStatic<JobletService> mockedJobletService = mockStatic(JobletService.class)) {
            receiver.onReceive(mockContext, intent);

            mockedJobletService.verify(
                    () -> JobletService.startMonitoring(any(), any(), any(), any()),
                    never()
            );
        }
    }
}
