// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.utils;

import android.util.Log;

import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  Input stream wrapper we use for parsing an HTTP response. The HttpEntity
 *  only allows a client to get the input stream once and therefore we can
 *  only read the data once.
 *
 *  This class allows us to intercept the data during read and make a copy that
 *  we can output to the log later
 */
public final class DebugInputStream extends InputStream {

    /**
     * Log tag
     */
	private static final String TAG = "DebugInputStream";
    /**
     * Input stream to debug
     */
	private InputStream is;
    /**
     * Debugging buffer
     */
	private ByteArrayOutputStream debugBuffer;
    /**
     * HTTP request/response to debug
     */
	private HttpRequestResponseContainer httpRequest;
    /**
     * Logger
     */
	private final Chronicler mChronicler;
    /** Payload */
	@SuppressWarnings("FieldCanBeLocal")
    private String mPayload = null;

	/**
	 * Constructor for debugging contents of an input stream
	 * @param requestResponsePair
	 * 			    HTTP request/response pair to debug
	 * @param chronicler
     *              Intercept data for debugging?
	 */
	public DebugInputStream(
			HttpRequestResponseContainer requestResponsePair,
			Chronicler chronicler) {

		// assume failure and no debugging
		is = null;
		debugBuffer = null;
		mChronicler = chronicler;

		// do we have a response to work with
		if (requestResponsePair != null) {
			httpRequest = requestResponsePair;
			// does the response have an entity
			if (requestResponsePair.response != null)
				try {
					// grab the input stream of the entity
					is = requestResponsePair.response.getInputStream();
					debugBuffer = new ByteArrayOutputStream();
				} catch (Exception ignored) {
				}
		}
	}

	/**
	 * Reads a single byte from this stream and returns it as an integer in the range from 0 to 255.
	 * @return
     *              the byte read or -1 if the end of stream has been reached
	 * @throws IOException
     *              if the stream is closed or another IOException occurs.
	 */
	@Override
	public int read() throws IOException {
		int input;

		// throw an exception if we don't have an input stream
		if (is == null)
			throw new IOException();

		// read a byte from the input stream
		input = is.read();

		// store the data into our buffer
		if ((debugBuffer != null) && (input >= 0)) {
			debugBuffer.write(input);
		}

		// return the data
		return input;
	}

	/**
	 * Reads at most length bytes from this stream and stores them in the byte array buffer starting at offset
	 * @param buffer
	 * 				the byte array in which to store the bytes read
	 * @param offset
	 * 				the initial position in buffer to store the bytes read from this stream
	 * @param length
	 * 				the maximum number of bytes to store in buffer
	 * @return
     *              the number of bytes actually read or -1 if the end of the stream has been reached
	 * @throws IOException
     *              if the stream is closed or another IOException occurs
	 * @throws IndexOutOfBoundsException
     *              if offset < 0 or length < 0, or if offset + length is greater than the length of buffer
	 */
	@Override
	public int read(byte buffer[], int offset, int length) throws IOException, IndexOutOfBoundsException {
		int readCount;
		// throw an exception if we don't have an input stream
		if (is == null)
			throw new IOException();

		// read data from the input stream
		readCount = is.read(buffer, offset, length);

		// store the read data into our buffer
		if ((debugBuffer != null) && (readCount >= 0)) {
			debugBuffer.write(buffer, offset, readCount);
		}

		// return the amount of bytes read
		return readCount;
	}

	/**
	 * Returns an estimated number of bytes that can be read or skipped without blocking for more input
	 * @return
     *              the estimated number of bytes available
     * @throws IOException
     *              If an error is detected
	 */
	@Override
	public int available() throws IOException {
		// throw an exception if we don't have an input stream
		if (is == null)
			throw new IOException();
		// return the amount of available data bytes
		return is.available();
	}

	/**
	 * Closes this stream, outputs intercepted if debugging was enabled
     * @throws IOException
     *              If an error is detected
	 */
	@Override
	public void close() throws IOException {
		// throw an exception if we don't have an input stream
		if (is == null)
			throw new IOException();
		// did we have a buffer where we were storing the data
		if (debugBuffer != null) {
			// close the buffer
			debugBuffer.close();
			// output the buffer contents to the log
			mPayload = debugBuffer.toString();
			httpRequest.payload = mPayload;
			if (mChronicler != null) {
				mChronicler.log(Log.DEBUG, TAG, String
						.format("Data from %s: \n%s\n", httpRequest.request.getURI()
								.toString(), debugBuffer.toString()));
			}
			debugBuffer = null;
		}
		// close the input stream
		is.close();
	}

	/**
	 * Sets a mark position in this InputStream. The parameter readlimit indicates
	 * how many bytes can be read before the mark is invalidated. Sending reset() will
	 * reposition the stream to back to the marked position provided readLimit has not
	 * been surpassed.
	 * @param readlimit
	 * 				the number of bytes that can be read from this stream before the
	 * 				mark is invalidated
	 */
	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public void mark(int readlimit) {
		// if we have an input stream to work with, mark the location
		if (is != null)
			is.mark(readlimit);
	}

	/**
	 * Resets this stream to the last marked location. Throws an IOException if the
	 * bytes read since the mark has been set is greater than the limit provided
	 * to mark, or if no mark has been set.
	 * @throws IOException
     *              if this stream is closed or another IOException occurs.
	 */
	@Override
	public void reset() throws IOException {
		// throw an exception if we don't have an input stream
		if (is == null)
			throw new IOException();
		// reset to the last marked position
		is.reset();
	}

	/**
	 * Indicates whether this stream supports the mark() and reset() method
     * @return
     *              true if mark is supported
     *              false otherwise
	 */
	@Override
	public boolean markSupported() {
// if we have an input stream to work with, check if marking is supported
		return ((is != null) && is.markSupported());
	}
}
