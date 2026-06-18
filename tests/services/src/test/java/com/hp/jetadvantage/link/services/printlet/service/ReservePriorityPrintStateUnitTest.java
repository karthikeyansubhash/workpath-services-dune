// Copyright 2026 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;

import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesReader;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.device.services.interfaces.IDevicePrintJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.printlet.ipp.IppClient;
import com.hp.jetadvantage.link.services.printlet.ipp.IppConnector;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.MediaSize;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;
import com.hp.jetadvantage.link.services.printlet.model.PrintTicket;
import com.hp.jetadvantage.link.services.printlet.model.PrinterAttributes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

/**
 * Unit tests for {@link ReservePriorityPrintState}.
 *
 * Covers:
 * - Media size fallback: when user omits paper size, printer's defaultMediaSize is used
 *   (core fix for E2 error when Duplex=BOOK/FLIP set without PaperSize)
 * - User-specified paper size takes priority over printer default
 * - Graceful handling when printer default fetch fails
 * - IPP request success → StartPrintState returned
 * - IPP request exception → ReportErrorState returned
 * - print() throws at runtime → ReportErrorState returned
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservePriorityPrintStateUnitTest {

    // ─── Mocks ───────────────────────────────────────────────────────────────
    @Mock private PrintJobIntentServiceStateMachine mockSm;
    @Mock private PrintAttributesReader mockAttrsReader;
    @Mock private IDevicePrintJobService mockPrintJobService;
    @Mock private Context mockContext;
    @Mock private PrintJobID mockPrintJobID;
    @Mock private Uri mockUri;
    @Mock private Bundle mockExtraBundle;
    @Mock private Bundle mockJobBundle;
    @Mock private IppClient mockIppClient;
    @Mock private PrintRequestIntent.IntentParams mockIntentParams;

    // ─── Static mocks (lifecycle managed manually) ───────────────────────────
    private MockedStatic<IppClient> mockedIppClientStatic;
    private MockedStatic<PrintRequestIntent> mockedPrintRequestIntentStatic;

    // ─── Target ──────────────────────────────────────────────────────────────
    private ReservePriorityPrintState state;

    // ─────────────────────────────────────────────────────────────────────────

    @Before
    public void setUp() {
        state = new ReservePriorityPrintState();

        // Static mock: IppClient.getInstance() → mockIppClient
        mockedIppClientStatic = mockStatic(IppClient.class);
        mockedIppClientStatic.when(IppClient::getInstance).thenReturn(mockIppClient);

        // Static mock: PrintRequestIntent.getIntentParams(Bundle) → mockIntentParams
        mockedPrintRequestIntentStatic = mockStatic(PrintRequestIntent.class);
        mockedPrintRequestIntentStatic.when(() -> PrintRequestIntent.getIntentParams(any(Bundle.class)))
                .thenReturn(mockIntentParams);

        // State machine stubs
        when(mockSm.getJobAttributesReader(PrintAttributesReader.class)).thenReturn(mockAttrsReader);
        when(mockSm.getExtraParams()).thenReturn(mockExtraBundle);
        when(mockSm.getPrintJobService()).thenReturn(mockPrintJobService);
        when(mockSm.getContext()).thenReturn(mockContext);
        when(mockSm.isBackgroundJob()).thenReturn(false);
        when(mockSm.isLastJob()).thenReturn(true);
        when(mockSm.getJobBundle()).thenReturn(mockJobBundle);

        // PrintJobService: valid UI context (non-null, non-empty → not background job check)
        when(mockPrintJobService.getUiContextToken(any())).thenReturn("test-ui-context");

        // IntentParams stubs
        when(mockIntentParams.getPackageName()).thenReturn("com.test.app");

        // PrintAttributesReader: HTTP source — simplest path (no file existence checks)
        when(mockAttrsReader.getSource()).thenReturn(PrintAttributes.Source.HTTP);
        when(mockUri.toString()).thenReturn("http://printer/test.pdf");
        when(mockAttrsReader.getUri()).thenReturn(mockUri);
        when(mockAttrsReader.getNetworkCredentials()).thenReturn(null);
        when(mockAttrsReader.getCopies()).thenReturn(1);
        when(mockAttrsReader.getJobName()).thenReturn("test-job");
        // All other attrsReader methods return null/0 by Mockito default
    }

    @After
    public void tearDown() {
        mockedIppClientStatic.close();
        mockedPrintRequestIntentStatic.close();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Media size fallback — core bug fix for E2 duplex error
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Given: User specified A4, printer default is Letter
     * When: action() builds the IPP ticket
     * Then: ticket.mediaSize == A4  (user value takes priority, no fallback)
     */
    @Test
    public void GivenUserPaperSizeA4DefaultLetter_WhenAction_ThenTicketMediaSizeIsA4() throws Exception {
        // Arrange
        when(mockAttrsReader.getPaperSize()).thenReturn(PrintAttributes.PaperSize.A4);
        setupPrinterDefaultAttrs(MediaSize.Letter);
        setupIppPrintSuccess();

        // Act
        ArgumentCaptor<PrintTicket> captor = ArgumentCaptor.forClass(PrintTicket.class);
        state.action(mockSm);

        // Assert
        verify(mockIppClient).priorityPrintUri(any(), anyString(), captor.capture(), anyInt());
        assertEquals(MediaSize.A4, captor.getValue().mediaSize);
    }

    /**
     * Given: User did NOT specify paper size (null), printer default is A4
     * When: action() builds the IPP ticket
     * Then: ticket.mediaSize == A4  (falls back to printer default)
     *
     * Core fix: Duplex=BOOK/FLIP set without PaperSize → getMediaSize() returns null
     *          → getPrintTicket() falls back to defaultAttrs.defaultMediaSize
     *          → E2 receives the required 'media' attribute
     */
    @Test
    public void GivenPaperSizeNullDefaultA4_WhenAction_ThenTicketFallsBackToDefaultA4() throws Exception {
        // Arrange
        when(mockAttrsReader.getPaperSize()).thenReturn(null);
        setupPrinterDefaultAttrs(MediaSize.A4);
        setupIppPrintSuccess();

        // Act
        ArgumentCaptor<PrintTicket> captor = ArgumentCaptor.forClass(PrintTicket.class);
        state.action(mockSm);

        // Assert
        verify(mockIppClient).priorityPrintUri(any(), anyString(), captor.capture(), anyInt());
        assertEquals(MediaSize.A4, captor.getValue().mediaSize);
    }

    /**
     * Given: User did NOT specify paper size, AND printer default fetch fails
     * When: action() builds the IPP ticket
     * Then: ticket.mediaSize == null  (no crash — exception is swallowed in getPrintTicket)
     */
    @Test
    public void GivenPaperSizeNullAndDefaultFetchFails_WhenAction_ThenTicketHasNoMediaSize() throws Exception {
        // Arrange
        when(mockAttrsReader.getPaperSize()).thenReturn(null);
        when(mockIppClient.getPrinterAttributes(any(), eq(true), anyInt()))
                .thenThrow(new RuntimeException("connection failed"));
        setupIppPrintSuccess();

        // Act
        ArgumentCaptor<PrintTicket> captor = ArgumentCaptor.forClass(PrintTicket.class);
        state.action(mockSm);

        // Assert
        verify(mockIppClient).priorityPrintUri(any(), anyString(), captor.capture(), anyInt());
        assertNull(captor.getValue().mediaSize);
    }

    /**
     * Given: User did NOT specify paper size, printer default has no media size (null)
     * When: action() builds the IPP ticket
     * Then: ticket.mediaSize == null  (defaultAttrs.defaultMediaSize == null is guarded)
     */
    @Test
    public void GivenPaperSizeNullDefaultMediaSizeNull_WhenAction_ThenTicketHasNoMediaSize() throws Exception {
        // Arrange
        when(mockAttrsReader.getPaperSize()).thenReturn(null);
        setupPrinterDefaultAttrs(null); // defaultAttrs exists but defaultMediaSize is null
        setupIppPrintSuccess();

        // Act
        ArgumentCaptor<PrintTicket> captor = ArgumentCaptor.forClass(PrintTicket.class);
        state.action(mockSm);

        // Assert
        verify(mockIppClient).priorityPrintUri(any(), anyString(), captor.capture(), anyInt());
        assertNull(captor.getValue().mediaSize);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // IPP request result handling
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Given: IPP request succeeds (REQUEST_RETURN_CODE__OK)
     * When: action() is called
     * Then: returns StartPrintState and setPrintJobID is called
     */
    @Test
    public void GivenIppResponseOk_WhenAction_ThenReturnsStartPrintState() throws Exception {
        // Arrange
        setupPrinterDefaultAttrs(null);
        setupIppPrintSuccess();

        // Act
        BaseJobIntentServiceState result = state.action(mockSm);

        // Assert
        assertTrue(result instanceof StartPrintState);
        verify(mockSm).setPrintJobID(mockPrintJobID);
    }

    /**
     * Given: IPP request returns REQUEST_RETURN_CODE__EXCEPTION
     * When: action() is called
     * Then: returns ReportErrorState and cancel batch is sent
     */
    @Test
    public void GivenIppResponseException_WhenAction_ThenReturnsReportErrorState() throws Exception {
        // Arrange
        setupPrinterDefaultAttrs(null);

        Error printError = mock(Error.class);
        when(printError.getMessage()).thenReturn("print server error");

        Message exResponse = new Message();
        exResponse.arg1 = IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION;
        exResponse.obj = printError;
        when(mockIppClient.priorityPrintUri(any(), anyString(), any(), anyInt()))
                .thenReturn(exResponse);

        // Act
        BaseJobIntentServiceState result = state.action(mockSm);

        // Assert
        assertTrue(result instanceof ReportErrorState);
        verify(mockSm).sendLocalPrintResult(
                eq(OXPCreatePrintSpoolerIntentService.ACTION_CANCEL_CURRENT_BATCH), eq(false));
    }

    /**
     * Given: priorityPrintUri throws a RuntimeException (network error etc.)
     * When: action() is called
     * Then: exception is caught → returns ReportErrorState (no crash)
     */
    @Test
    public void GivenPrintUriThrows_WhenAction_ThenReturnsReportErrorState() throws Exception {
        // Arrange
        setupPrinterDefaultAttrs(null);
        when(mockIppClient.priorityPrintUri(any(), anyString(), any(), anyInt()))
                .thenThrow(new RuntimeException("Network timeout"));

        // Act
        BaseJobIntentServiceState result = state.action(mockSm);

        // Assert
        assertTrue(result instanceof ReportErrorState);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Helper methods
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Stubs IppClient.getPrinterAttributes() to return a PrinterAttributes
     * whose defaultMediaSize field is set to the given value.
     *
     * Mockito.mock() uses Objenesis internally to allocate the instance without
     * calling the constructor (which requires IppResponse). The final field is
     * then set via reflection with setAccessible(true).
     */
    private void setupPrinterDefaultAttrs(MediaSize defaultMediaSize) throws Exception {
        Message msg = new Message();
        if (defaultMediaSize != null) {
            msg.obj = createPrinterAttrsWithMediaSize(defaultMediaSize);
        } else {
            msg.obj = createPrinterAttrsWithMediaSize(null);
        }
        when(mockIppClient.getPrinterAttributes(any(), eq(true), anyInt())).thenReturn(msg);
    }

    private void setupIppPrintSuccess() {
        Message okResponse = new Message();
        // arg1 defaults to 0 == REQUEST_RETURN_CODE__OK
        okResponse.obj = mockPrintJobID;
        when(mockIppClient.priorityPrintUri(any(), anyString(), any(), anyInt()))
                .thenReturn(okResponse);
    }

    private static PrinterAttributes createPrinterAttrsWithMediaSize(MediaSize mediaSize)
            throws Exception {
        // mock() uses Mockito's Objenesis integration: instance created without constructor
        PrinterAttributes attrs = mock(PrinterAttributes.class);
        Field field = PrinterAttributes.class.getDeclaredField("defaultMediaSize");
        field.setAccessible(true);
        field.set(attrs, mediaSize);
        return attrs;
    }
}
