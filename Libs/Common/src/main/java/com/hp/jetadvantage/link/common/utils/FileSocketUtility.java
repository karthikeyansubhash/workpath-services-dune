package com.hp.jetadvantage.link.common.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class FileSocketUtility {
    private static final String TAG = "FileSocketUtility";

    private static final int BUFFER_SIZE = 65536 << 3; //512KB
    private static final int ACCEPT_TIMEOUT = 30000;

    ServerSocketThread mServerThread;

    interface ConnectionSocketTask {
        void handleConnection(Socket socket);
    }

    class ServerSocketThread implements Runnable {
        private final String socketName;
        private final ConnectionSocketTask task;
        private ServerSocket mServerSocket;
        private int mPort = 0;


        ServerSocketThread(String socketName, ConnectionSocketTask task) {
            this.socketName = socketName;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                try {
                    mServerSocket = new ServerSocket(0);
                    mServerSocket.setSoTimeout(ACCEPT_TIMEOUT);
                    mPort = mServerSocket.getLocalPort();
                } catch (IOException e) {
                    SLog.d(TAG, "Failed to get a port!! ");
                }
                SLog.d(TAG, "(Socket)Listening for socket to " + mPort);
                Socket connection = mServerSocket.accept();
                try {
                    connection.setSendBufferSize(BUFFER_SIZE);
                    connection.setReceiveBufferSize(BUFFER_SIZE);

                    SLog.d(TAG, "(Socket)Connection accepted");
                    task.handleConnection(connection);
                } finally {
                    connection.close();
                }
                SLog.d(TAG, "(Socket)Finishing");
            } catch (Throwable e) {
                SLog.e(TAG, "(Socket)Failed to accept connection!!!!" + e.getMessage());
            } finally {
                if (mServerSocket != null) {
                    try {
                        mServerSocket.close();
                        SLog.d(TAG, "(Socket)Connection closed");
                    } catch (IOException e) {
                        SLog.w(TAG, "(Socket)Failed to close socket" + e.getMessage());
                    }
                    mServerSocket = null;
                }
            }
        }

        private String getIpAddress() {
            String ip = "";
            try {
                Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (enumNetworkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = enumNetworkInterfaces
                            .nextElement();
                    Enumeration<InetAddress> enumInetAddress = networkInterface
                            .getInetAddresses();
                    while (enumInetAddress.hasMoreElements()) {
                        InetAddress inetAddress = enumInetAddress.nextElement();

                        if (inetAddress.isSiteLocalAddress()) {
                            ip += "SiteLocalAddress: "
                                    + inetAddress.getHostAddress() + "\n";
                        }
                    }
                }
            } catch (SocketException e) {
                ip += "Something Wrong! " + e.toString() + "\n";
            }

            return ip;
        }

        void stop() {
            if (mServerSocket != null) {
                try {
                    mServerSocket.close();
                } catch (Throwable e) {
                    SLog.w(TAG, "(Socket)Server Socket is already closed: " + e.getMessage());
                }
            }
        }

        int getPort() {
            return mPort;
        }
    }

    public static int hashCode(String string) {
        return string != null ? string.hashCode() * 31 : 0;
    }

    public void createServerSocket(final String socketName) {
        ConnectionSocketTask task = new ConnectionSocketTask() {
            @Override
            public void handleConnection(Socket socket) {
                OutputStream outputStream = null;
                try {
                    SLog.d(TAG, "Copying data from input stream");
                    // maybe the outputStream should be a file path
                    // socketName is a name of a file to handle
                    //outputStream = socket.getOutputStream();
                    outputStream = new FileOutputStream(socketName);
                    copyStream(socket.getInputStream(), outputStream, BUFFER_SIZE, true, false);
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
        mServerThread = new ServerSocketThread(socketName, task);
        new Thread(mServerThread).start();
    }

    public String getIpAddress() {
        return mServerThread.getIpAddress();
    }

    public int getPort() {
        return mServerThread.getPort();
    }

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
