// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.snmp;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.ProtocolException;

/**
 * Utility class that parses SNMP responses.
 */
public final class SnmpParser {

    // private static final String LOG_TAG = SnmpParser.class.getSimpleName();

    private static final SnmpTag SEQUENCE =     new SnmpTag((byte) 0x30, "SEQUENCE");
    private static final SnmpTag INTEGER =      new SnmpTag((byte) 0x02, "INTEGER");
    private static final SnmpTag OCTET_STRING = new SnmpTag((byte) 0x04, "OCTET_STRING");
    private static final SnmpTag OBJECT =       new SnmpTag((byte) 0x06, "OBJECT");
    private static final SnmpTag GET_RESPONSE = new SnmpTag((byte) 0xa2, "GET_RESPONSE");

//    public static final int ERR_NO_SUCH_NAME = 2;
    public static final int SNMP_PORT = 161;

    private final int mVersion;
    private final String mCommunity;
    private final int mRequestId;
    private final int mErrorStatus;
    private final int mErrorIndex;

    private final byte mBuffer[];
    private final int mBufLen;
    private int mBufOfs;
 //   private boolean mIsDebuggable = BuildConfig.DEBUG;
    /**
     * Instantiates a parser from the data contained in the given packet.
     *
     * @param packet a {@link DatagramPacket} containing an SNMP response.
     * @throws ProtocolException if the given packet does not contain a valid
     *             SNMP get-response sequence.
     */
    public SnmpParser(DatagramPacket packet) throws ProtocolException {
        mBuffer = packet.getData();
        mBufLen = packet.getLength();
        mBufOfs = 0;

        SnmpTag tag;
        @SuppressWarnings("unused")
        int len;
        tag = nextTag(SEQUENCE);
        nextLength(tag);
        mVersion = getInt();
        mCommunity = getString();
        tag = nextTag(GET_RESPONSE);
        nextLength(tag);
        mRequestId = getInt();
        mErrorStatus = getInt();
        mErrorIndex = getInt();
        tag = nextTag(SEQUENCE);
        nextLength(tag);
    }

    /**SnmpParser
     * @return the protocol version number
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * @return the community name
     */
    public String getCommunity() {
        return mCommunity;
    }

    /**
     * @return the request id
     */
    @SuppressWarnings("unused")
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * @return the error status
     */
    public int getErrorStatus() {
        return mErrorStatus;
    }

    /**
     * @return the error index
     */
    @SuppressWarnings("unused")
    public int getErrorIndex() {
        return mErrorIndex;
    }

    /**
     * @return the next object in the response, as an octet string (byte array).
     * @throws ProtocolException if the next object in the response is not a valid string.
     */
    public byte[] getOctetString() throws ProtocolException {
        SnmpTag tag = nextTag(OCTET_STRING);
        int len = nextLength(tag);
        byte[] b = new byte[len];
        System.arraycopy(mBuffer, mBufOfs, b, 0, len);
        mBufOfs += len;
        return b;
    }

    /**
     * @return the next object in the response, as a String
     * @throws ProtocolException if the next object in the response is not a valid string.
     */
    public String getString() throws ProtocolException {
        SnmpTag tag = nextTag(OCTET_STRING);
        int len = nextLength(tag);
        try {
            String s = new String(mBuffer, mBufOfs, len, "UTF-8");
            mBufOfs += len;
            return s;
        } catch (UnsupportedEncodingException e) {
            throw new ProtocolException(e.getMessage());
        }
    }

    /**
     * @return the next object in the response, as an OID
     * @throws ProtocolException if the next object in the response is not a valid OID.
     */
    public OID getOID() throws ProtocolException {
        SnmpTag tag = nextTag(SEQUENCE);
        nextLength(tag);
        tag = nextTag(OBJECT);
        int len = nextLength(tag);
        OID oid = new OID(mBuffer, mBufOfs, len);
        mBufOfs += len;
        return oid;
    }

    /**
     * @return the next object in the response, as an integer
     * @throws ProtocolException if the next object in the response is not a valid integer.
     */
    public int getInt() throws ProtocolException {
        SnmpTag tag = nextTag(INTEGER);
        int len = nextLength(tag);
        int i = 0;
        while (len-- > 0) {
            i = i << 8 | (mBuffer[mBufOfs++] & 0xff);
        }
        return i;
    }

    /**
     * Finishes the parsing, checking if all data was consumed.
     *
     * @throws ProtocolException if there is still unparsed data in the buffer.
     */
    public void finish() throws ProtocolException {
        // if(mIsDebuggable)LogViewer.LOGD(LOG_TAG, "mBufLen=" + mBufLen + " ofs=" + mBufOfs);
        if (mBufOfs != mBufLen) {
            throw new ProtocolException("Parsing error: mBufLen=" + mBufLen + " ofs=" + mBufOfs);
        }
    }

    private SnmpTag nextTag(SnmpTag expected) throws ProtocolException {
        if ((mBufOfs) >= mBufLen) {
            throw new ProtocolException("Insufficient data for SnmpTag");
        }
        byte id = mBuffer[mBufOfs++];
        if (id != expected.mId) {
            throw new ProtocolException("Expected tag " + expected.mName);
        }
        return expected;
    }

    private int nextLength(SnmpTag tag) throws ProtocolException {
        if ((mBufOfs) >= mBufLen) {
            throw new ProtocolException("Insufficient data for length");
        }
        int len, lenlen;
        lenlen = mBuffer[mBufOfs++] & 0xff;
        if ((lenlen & 0x80) == 0) {
            len = lenlen;
        } else {
            lenlen &= 0x7f;
            if (((mBufOfs) + lenlen) >= mBufLen) {
                throw new ProtocolException("Insufficient data for length lenlen=" + lenlen);
            }
            len = 0;
            while (lenlen-- > 0) {
                len = len << 8 | (mBuffer[mBufOfs++] & 0xff);
            }
        }
        if ((mBufOfs + len) > mBufLen) {
            throw new ProtocolException("Insufficient data for " + tag.mName + " len=" + len);
        }
        return len;
    }

    static final class SnmpTag {
        final byte mId;
        final String mName;

        SnmpTag(byte id, String name) {
            mId = id;
            mName = name;
        }

    }

    public static final class OID {
        private final byte[] mBytes;

        OID(byte[] buffer, int start, int len) {
            mBytes = new byte[len];
            System.arraycopy(buffer, start, mBytes, 0, len);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(mBytes.length * 3);
            /* mBytes[0] = 0x2b = 1 * 40 + 3 (all praise ISO!) */
            sb.append("1.3"); // iso.org
            int i;
            for (i = 1; i < mBytes.length; i++) {
                sb.append('.');
                sb.append(mBytes[i]);
            }
            return sb.toString();
        }

    }
}
