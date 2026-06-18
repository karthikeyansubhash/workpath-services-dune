// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Base class for transforming data for sending via HTTP
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractHTTPOutput {

    /**
     * Constructor
     */
    protected AbstractHTTPOutput() {}

    /**
     * Send the data
     * @param outputStream Output stream to write the data into
     * @throws IOException if an I/O error occurs
     */
    public abstract void send(OutputStream outputStream) throws IOException;

    /**
     * Return the length of the content
     * @return Length of the data if known, or -1 if length is unknown
     */
    public abstract long length();

    /**
     * Return a string representation of the data
     * @return a string representation of the data being transmitted
     */
    public abstract String toString();

    /**
     * Create a sender for transmitting a file
     * @param fileData File to transmit
     * @return Sender which transmits specified file
     */
    public static AbstractHTTPOutput wrap(File fileData) {
        return ((fileData != null) ? new HTTPOutputFile(fileData) : null);
    }

    /**
     * Create a sender for transmitting byte data
     * @param rawData Byte data to transmit
     * @return Sender which transmit specified data
     */
    public static AbstractHTTPOutput wrap(byte[] rawData) {
        return ((rawData != null) ? new HTTPOutputRaw(rawData) : null);
    }

    /**
     * Create a sender for transmitting a string
     * @param stringData String data to transmit
     * @param encoding String character encoding to use
     * @return Sender which transmit specified string
     */
    public static AbstractHTTPOutput wrap(String stringData, String encoding) {
        if (TextUtils.isEmpty(encoding)) {
            encoding = HttpRequest.DEFAULT_TEXT_ENCODING;
        }
        return ((stringData != null) ? new HTTPOutputString(stringData, encoding) : null);
    }

    /**
     * Container for sending a file
     */
    private static class HTTPOutputFile extends AbstractHTTPOutput {

        /** File to send */
        private final File mFile;

        /**
         * Constructor
         * @param file File to send
         */
        public HTTPOutputFile(File file) {
            super();
            mFile = file;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void send(OutputStream outputStream) throws IOException {
            FileInputStream is = null;
            int bytesRead;
            try {
                is = new FileInputStream(mFile);
                byte[] buffer = new byte[4096];
                while((bytesRead = is.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                try {
                    if (is != null) is.close();
                } catch(IOException ignored) {}
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long length() {
            return mFile.length();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return mFile.toString();
        }
    }

    /**
     * Container for sending a raw byte array
     */
    private static class HTTPOutputRaw extends AbstractHTTPOutput {

        /** Data to send */
        protected final byte[] mData;

        /**
         * Constructor
         * @param data Data to send
         */
        public HTTPOutputRaw(byte[] data) {
            super();
            mData = data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void send(OutputStream outputStream) throws IOException {
            outputStream.write(mData);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long length() {
            return mData.length;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            int iMax = mData.length - 1;
            if (iMax == -1)
                return "[]";

            StringBuilder b = new StringBuilder();
            b.append('[');
            for (int i = 0; ; i++) {
                byte val = mData[i];
                b.append(Integer.toHexString((val >> 4) & 0xf));
                b.append(Integer.toHexString(val & 0xf));
                if (i == iMax)
                    return b.append(']').toString();
                b.append(", ");
            }
        }
    }

    /**
     * Container for sending a string
     */
    private static class HTTPOutputString extends HTTPOutputRaw {

        /** String byte encoding */
        private final Charset mEncoding;

        /**
         * Constructor
         * @param stringData String data
         * @param encoding String byte encoding to use
         */
        public HTTPOutputString(String stringData, String encoding) {
            this(stringData, Charset.forName(encoding));
        }

        /**
         * Constructor
         * @param stringData String data
         * @param encoding String byte encoding to use
         */
        public HTTPOutputString(String stringData, Charset encoding) {
            super(stringData.getBytes(encoding));
            mEncoding = encoding;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return new String(mData, mEncoding);
        }
    }
}
