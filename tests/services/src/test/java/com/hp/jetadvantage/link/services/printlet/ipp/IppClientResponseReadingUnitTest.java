// Copyright 2026 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.ipp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for {@link IppClient#readResponseStream(InputStream, ByteArrayOutputStream)}.
 *
 * Verifies that when InputStream.read() throws IOException, the exception is propagated
 * to the caller rather than being swallowed, allowing the caller to handle connection errors.
 */
@RunWith(MockitoJUnitRunner.class)
public class IppClientResponseReadingUnitTest {

    /**
     * Given: InputStream.read() throws IOException on the very first call (connection closed)
     * When: readResponseStream is invoked
     * Then: IOException is propagated to the caller and output is empty
     */
    @Test(timeout = 5000)
    public void GivenInputStreamAlwaysThrows_WhenReadResponseStream_ThenThrowsIOException() {
        InputStream alwaysThrowsStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Connection reset");
            }
            @Override
            public int read(byte[] b) throws IOException {
                throw new IOException("Connection reset");
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IppClient.readResponseStream(alwaysThrowsStream, baos);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Output should be empty when read throws immediately", 0, baos.size());
        }
    }

    /**
     * Given: InputStream.read() succeeds once (returns 100 bytes) then throws on second call
     * When: readResponseStream is invoked
     * Then: IOException is propagated and data from the first successful read is preserved in baos
     */
    @Test(timeout = 5000)
    public void GivenInputStreamThrowsAfterOneRead_WhenReadResponseStream_ThenThrowsIOException() {
        InputStream throwAfterOneRead = new InputStream() {
            private int callCount = 0;
            @Override
            public int read() throws IOException {
                throw new IOException("Not used");
            }
            @Override
            public int read(byte[] b) throws IOException {
                callCount++;
                if (callCount == 1) {
                    for (int i = 0; i < 100; i++) {
                        b[i] = (byte) (i & 0xFF);
                    }
                    return 100;
                }
                throw new IOException("Stream closed");
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IppClient.readResponseStream(throwAfterOneRead, baos);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Output should contain data from first successful read before exception",
                    100, baos.size());
        }
    }

    /**
     * Given: InputStream.read() throws repeatedly (simulating persistent connection error)
     * When: readResponseStream is invoked
     * Then: IOException is propagated on the FIRST exception, read() is called exactly once
     */
    @Test(timeout = 5000)
    public void GivenInputStreamThrowsRepeatedly_WhenReadResponseStream_ThenExitsOnFirstException() {
        final int[] readCallCount = {0};
        InputStream repeatedlyThrowsStream = new InputStream() {
            @Override
            public int read() throws IOException {
                readCallCount[0]++;
                throw new IOException("Connection reset by peer");
            }
            @Override
            public int read(byte[] b) throws IOException {
                readCallCount[0]++;
                throw new IOException("Connection reset by peer");
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IppClient.readResponseStream(repeatedlyThrowsStream, baos);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("read() should be called exactly once", 1, readCallCount[0]);
            assertEquals("Output should be empty", 0, baos.size());
        }
    }

    /**
     * Given: InputStream has 2 successful reads then throws (mid-transfer cancel scenario)
     * When: readResponseStream is invoked
     * Then: IOException is propagated and data from 2 successful reads is preserved in baos
     */
    @Test(timeout = 5000)
    public void GivenInputStreamThrowsAfterMultipleReads_WhenReadResponseStream_ThenPartialDataPreserved() {
        final int[] readCallCount = {0};
        InputStream throwAfterTwoReads = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Not used");
            }
            @Override
            public int read(byte[] b) throws IOException {
                readCallCount[0]++;
                if (readCallCount[0] <= 2) {
                    for (int i = 0; i < 50; i++) {
                        b[i] = (byte) readCallCount[0];
                    }
                    return 50;
                }
                throw new IOException("Connection aborted during cancel");
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IppClient.readResponseStream(throwAfterTwoReads, baos);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("read() should be called exactly 3 times", 3, readCallCount[0]);
            assertEquals("Output should contain 100 bytes from 2 successful reads", 100, baos.size());
        }
    }

    /**
     * Given: Normal InputStream that returns data then -1 (EOF)
     * When: readResponseStream is invoked
     * Then: All data is read correctly (baseline / happy path)
     */
    @Test(timeout = 5000)
    public void GivenNormalInputStream_WhenReadResponseStream_ThenAllDataRead() throws IOException {
        byte[] testData = "Hello, IPP response data!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IppClient.readResponseStream(inputStream, baos);

        assertArrayEquals("All input data should be read correctly", testData, baos.toByteArray());
    }

    /**
     * Given: Empty InputStream (returns -1 immediately)
     * When: readResponseStream is invoked
     * Then: Loop terminates immediately with empty output
     */
    @Test(timeout = 5000)
    public void GivenEmptyInputStream_WhenReadResponseStream_ThenEmptyOutput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IppClient.readResponseStream(inputStream, baos);

        assertEquals("Output should be empty for empty input", 0, baos.size());
    }
}
