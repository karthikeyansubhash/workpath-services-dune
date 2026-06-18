package com.hp.jetadvantage.link.services.copylet.adapter;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for StoredCopyJobAdapter.
 * NOTE: Tests that require mocking CopyAttributesReader (a concrete class) are excluded
 * due to Mockito 4.8.0 + JDK 21 ByteBuddy incompatibility. Those tests should be
 * run as instrumented tests or after upgrading Mockito to 5.x.
 * Only null-guard and interface-mock tests are included here.
 */
@RunWith(MockitoJUnitRunner.class)
public class StoredCopyJobAdapterTest {

    private static final String TEST_PACKAGE = "com.hp.test";

    @Mock private IDeviceScanJobService mockScanJobService;

    @Before
    public void setUp() {
    }

    @Test
    public void GivenNullScanJobService_WhenCreateScanJob_ThenNullReturned() throws Exception {
        // Pass non-null reader is not needed; null service triggers early return
        String jobId = StoredCopyJobAdapter.createScanJobForStorage(null, null, TEST_PACKAGE, null);
        assertNull(jobId);
    }

    @Test
    public void GivenNullCopyAttributes_WhenCreateScanJob_ThenNullReturned() throws Exception {
        String jobId = StoredCopyJobAdapter.createScanJobForStorage(mockScanJobService, null, TEST_PACKAGE, null);
        assertNull(jobId);
        verify(mockScanJobService, never()).createScanJob(anyString(), any(ScanJob_Create.class));
    }

    @Test
    public void GivenBothNull_WhenCreateScanJob_ThenNullReturned() throws Exception {
        String jobId = StoredCopyJobAdapter.createScanJobForStorage(null, null, TEST_PACKAGE, null);
        assertNull(jobId);
    }
}
