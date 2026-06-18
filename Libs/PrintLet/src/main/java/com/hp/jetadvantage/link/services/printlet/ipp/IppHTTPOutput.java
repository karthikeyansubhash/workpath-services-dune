package com.hp.jetadvantage.link.services.printlet.ipp;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRequest;
import com.hp.sdd.jabberwocky.chat.AbstractHTTPOutput;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class IppHTTPOutput extends AbstractHTTPOutput {
    /**
     * Content Resolver for Uris
     */
    private final ContentResolver mContentResolver;
    /**
     * Uri for additional data
     */
    private final Uri mAdditionalData;
    /**
     * Uri for additional data
     */
    private final InputStream mPrintStream;
    /**
     * IPP Data
     */
    private final byte[] mIppData;

    private static final int BUFFER_SIZE = 65536 << 3; //512KB

    /**
     * Constructor
     *
     * @param request        IPP request data
     * @param context        Context for content resolver
     * @param additionalData Additional data to send
     */
    public IppHTTPOutput(IppRequest request, Context context, Uri additionalData) {
        mIppData = request.encode();
        mContentResolver = context.getContentResolver();
        mAdditionalData = additionalData;
        mPrintStream = null;
    }

    /**
     * Constructor
     *
     * @param request     IPP request data
     * @param context     Context for content resolver
     * @param printStream Print stream
     */
    public IppHTTPOutput(IppRequest request, Context context, InputStream printStream) {
        mIppData = request.encode();
        mContentResolver = context.getContentResolver();
        mAdditionalData = null;
        mPrintStream = printStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(OutputStream outputStream) throws IOException {

        outputStream.write(mIppData);
        if (mAdditionalData != null || mPrintStream != null) {
            InputStream is = mPrintStream != null ? mPrintStream : mContentResolver.openInputStream(mAdditionalData);

            boolean edxMode = "true".equals(Settings.Secure.getString(mContentResolver, "edx_state"));
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            if (edxMode) {
                String filenameInTimeFormat = new SimpleDateFormat("yyyymmdd-HHmmss", Locale.getDefault()).format(new Date());
                File dir = new File("/sdcard/Download/print_test");
                if (!dir.exists()) dir.mkdir();

                File file = new File("/sdcard/Download/print_test/" + filenameInTimeFormat + ".tmp");
                if (file.exists()) file.delete();
                file.createNewFile();

                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
            }

            try {
                Long begin = System.currentTimeMillis();
                if (is != null) {
                    int bytesRead;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = is.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        if (edxMode) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                    outputStream.flush();
                    if (edxMode) {
                        bos.flush();
                    }
                }
                Long end = System.currentTimeMillis();
                Log.e("lhs", "Elapsed time(ms) = " + (end - begin) + "ms");
            } catch (IOException ignored) {
            } finally {
                try {
                    if (is != null) is.close();
                    if (edxMode) {
                        if (fos != null) {
                            fos.close();
                            fos = null;
                        }
                        if (bos != null) {
                            bos.close();
                            bos = null;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long length() {
        return ((mAdditionalData != null || mPrintStream != null) ? -1 : mIppData.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Arrays.toString(mIppData));
        if (mAdditionalData != null) {
            builder.append(" with additional data from ");
            builder.append(mAdditionalData.toString());
        }
        return builder.toString();
    }
}
