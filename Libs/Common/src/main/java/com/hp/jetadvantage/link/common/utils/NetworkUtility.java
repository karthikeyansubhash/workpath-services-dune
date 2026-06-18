// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.utils;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NetworkUtility {
    private static final String TAG = "NetworkUtility";

    private static final int BUFFER_SIZE = 65536 << 3; //512KB
    private static final int ACCEPT_TIMEOUT = 30000;

    interface ConnectionTask {
        void handleConnection(LocalSocket socket);
    }

    static class ServerThread implements Runnable {
        private final String socketName;
        private final ConnectionTask task;
        private LocalServerSocket mServerSocket;

        ServerThread(String socketName, ConnectionTask task) {
            this.socketName = socketName;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                mServerSocket = new LocalServerSocket(socketName);

                SLog.d(TAG, "Listening for socket to " + socketName);
                LocalSocket connection = mServerSocket.accept();
                try {
                    connection.setSendBufferSize(BUFFER_SIZE);
                    connection.setReceiveBufferSize(BUFFER_SIZE);

                    SLog.d(TAG, "Connection accepted");
                    task.handleConnection(connection);
                } finally {
                    connection.close();
                }
                SLog.d(TAG, "Finishing");
            } catch (Exception e) {
                SLog.e(TAG, "Failed to accept connection", e);
            } finally {
                if (mServerSocket != null) {
                    try {
                        mServerSocket.close();
                        SLog.d(TAG, "Connection closed");
                    } catch (IOException e) {
                        SLog.w(TAG, "Failed to close socket", e);
                    }
                    mServerSocket = null;
                }
            }
        }

        void stop() {
            if (mServerSocket != null) {
                LocalSocket receiver = null;
                try {
                    mServerSocket.close();
                    // double check / fake accept
                    receiver = new LocalSocket();
                    receiver.connect(new LocalSocketAddress(socketName));
                } catch (IOException e) {
                    SLog.w(TAG, "Failed to close connection: " + e.getMessage());
                } finally {
                    if (receiver != null) {
                        try {
                            receiver.close();
                        } catch (IOException e) {
                            SLog.w(TAG, "Failed to close socket", e);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void createServerSocket(final String rid, final InputStream inputStream) {
        ConnectionTask task = new ConnectionTask() {
            @Override
            public void handleConnection(LocalSocket socket) {
                OutputStream outputStream = null;
                try {
                    SLog.d(TAG, "Copying data from input stream");
                    outputStream = socket.getOutputStream();
                    copyStream(inputStream, outputStream, BUFFER_SIZE, true, false);
                } catch (Exception e) {
                    SLog.e(TAG, "Failed to copy stream data: " + e.getMessage());
                } finally {
                    try {
                        // before closing output stream need to mark end of stream first to ensure that other side reads EOF
                        socket.shutdownOutput();
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        socket.close();
                    } catch (Exception e) {
                        SLog.w(TAG, "Failed to close output stream: " + e.getMessage());
                    }
                }
            }
        };

        final ServerThread serverThread = new ServerThread(rid, task);
        new Thread(serverThread).start();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SLog.d(TAG, "No connection after " + ACCEPT_TIMEOUT + " ms, stopping");
                serverThread.stop();
            }
        }, ACCEPT_TIMEOUT);
    }

    @SuppressWarnings("unused")
    public static int copyStream(final InputStream input, final OutputStream output, int bufferSize) throws Exception {
        return copyStream(input, output, bufferSize, true, true);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static int copyStream(final InputStream input, final OutputStream output, final int bufferSize,
            final boolean closeInput, final boolean closeOutput) throws Exception {
        byte[] buffer = new byte[bufferSize];
        int read;

        int total = 0;
        try {
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                total += read;
            }
            output.flush();
        } finally {
            try {
                if (closeInput && input != null) {
                    input.close();
                }
            } catch (Exception e) {
                SLog.w(TAG, "Failed to close input stream", e);
            }
            try {
                if (closeOutput && output != null) {
                    output.close();
                }
            } catch (Exception e) {
                SLog.w(TAG, "Failed to close output stream", e);
            }
        }

        return total;
    }
}
