// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Logging wrapper that can output to Android logcat or to a file
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Chronicler {

    private static final String TAG = "Chronicler";
    private static final int DEFAULT_LOG_LEVEL = 0;

    /**
     * Debugging off
     */
    public static int DEBUG_OFF = -1;
    /**
     * Force debug message
     */
    public static int DEBUG_FORCE = 0;

    /**
     * Logger locking
     * we are intentionally using a lock rather than making methods
     * synchronized to that clients have the ability to lock out access while they
     * make sequential logging commands to prevent messages from different threads
     * from being intertwined
     */
    private final ReentrantLock mLock;

    /**
     * Lock the logger
     */
    public void lock() {
        mLock.lock();
    }

    /**
     * Unlock the logger
     */
    public void unlock() {
        mLock.unlock();
    }

    /**
     * Constructor and initializer for the Chronicler
     */
    public Chronicler() {
        this(DEFAULT_LOG_LEVEL, null);
    }

    /**
     * Constructor and initializer for the Chronicler
     * @param level
     *              Initial log level
     */
    public Chronicler(int level) {
        this(level, null);
    }

    /**
     * Constructor and initializer for the Chronicler
     * @param logFileName
     *              Filename of file to output logging information to
     */
    public Chronicler(String logFileName) {
        this(DEFAULT_LOG_LEVEL, logFileName);
    }

    /**
     * Constructor and initializer for the Chronicler
     * @param other
     *              Chronicler to copy settings from
     */
    public Chronicler(Chronicler other) {
        mLock = other.mLock;
        debugLevel = other.debugLevel;
        fileDebugOutput = other.fileDebugOutput;
    }

    /**
     * Constructor and initializer for the Chronicler
     * @param level
     *              Initial log level
     * @param logFileName
     *              Filename of file to output logging information to
     */
    public Chronicler(int level, String logFileName) {
        mLock = new ReentrantLock(true);
        setLevelAndLogFile(level, logFileName);
    }

    // debug level set
    private int debugLevel = DEFAULT_LOG_LEVEL;
    // output stream for logging to file
    private FileOutputStream fileDebugOutput = null;

    /**
     * Return the current logging level
     * @return
     *              Current logging level
     */
    public int getLogLevel() {
        try {
            lock();
            return debugLevel;
        } finally {
            unlock();
        }
    }

    /**
     * Set the logging level and log file
     * @param level
     *              Logging level
     * @param logFileName
     *              Filename of file to output logging information to
     */
    public void setLevelAndLogFile(int level, String logFileName) {
        try {
            lock();
            setLogLevel(level);
            setLogFile(logFileName);
        } finally {
            unlock();
        }
    }

    /**
     * Set the logging level
     * @param level
     *              Logging level
     */
    public void setLogLevel(int level) {
        try {
            // lock the log
            lock();

            // check if the level is below the supported range
            if (level < Log.VERBOSE) {
                // below the supported range, disable debugging
                debugLevel = 0;
            }
            // check if the level is above the supported range
            else if (level > Log.ASSERT) {
                // above supported range, cap it to the highest level
                debugLevel = Log.ASSERT;
            } else {
                // level is OK
                debugLevel = level;
//			Log.d(TAG, "debugLevel: " + debugLevel   +  " Log.WARN: " + Log.WARN  + " Log.Error: " + Log.ERROR);
            }
        } finally {
            unlock();
        }
    }

    /**
     * Set the file to log to
     * @param logFileName
     *              Filename of file to output logging information to
     */
    public void setLogFile(String logFileName) {
        try {
            lock();
            if (!TextUtils.isEmpty(logFileName)) {
                shred();
                // need to create log file
                File extStore = Environment.getExternalStorageDirectory();
                File logFile = new File(extStore, logFileName);
                try {
                    fileDebugOutput = new FileOutputStream(logFile);
                    Log.i(TAG,
                            "Logging being saved to: "
                                    + logFile.getName());
                } catch (FileNotFoundException e) {
                    fileDebugOutput = null;
                    Log.e(TAG, "unable to create log file");
                }
            } else {
                shred();
            }
        } finally {
            unlock();
        }
    }

    /**
     * Close the log file
     */
    private void shred() {
        try {
            // lock the debug log
            lock();
            if (fileDebugOutput != null) {
                // need to close log file
                try {
                    fileDebugOutput.close();
                } catch (IOException ignored) {
                }
                fileDebugOutput = null;
            }
        } finally {
            // unlock the debug log
            unlock();
        }
    }

    /**
     * Checks to see whether or not a logging is enabled at the specified level.
     * @param level
     * 			    Logging level
     * @return
     *              true if logging level is enabled
     *              false otherwise
     */
    public boolean isLoggable(int level) {
        try {
            // assume loggable
            boolean loggable = true;

            // lock the debug log
            lock();

            // make sure the debug level is reasonable
            if (level < 0)
                level = DEBUG_OFF;
            if (level < Log.VERBOSE)
                level = 0;
            else if (level > Log.ASSERT)
                level = Log.ASSERT;

            // is this a forced or regular message
            if (level >= 0) {
                // regular message

                // check if debugging is enabled
                if (debugLevel == 0)
                    loggable = false;
                // check if the level is enabled
                if (level < debugLevel)
                    loggable = false;
            }
            return loggable;
        } finally {
            // unlock the debug log
            unlock();
        }
    }

    /**
     * Send a log message using the provided tag at the specified level
     * @param level
     * 			Logging level
     * @param tag
     * 			Logging tag to use
     * @param msg
     * 			The message to log
     */
    public void log(int level, String tag, String msg) {
        try {
            // lock the debug log
            lock();

            // make sure the debug level is reasonable
            if (level < 0)
                return;
            if (level < Log.VERBOSE)
                level = 0;
            else if (level > Log.ASSERT)
                level = Log.ASSERT;

            // is this a forced or regular message
            if (level > 0) {
                // regular message

                // check if debugging is enabled
                if (debugLevel == 0)
                    return;
                // check if the level is enabled
                if (level < debugLevel)
                    return;
            } else {
                // bump forced messages up to info priority
                level = Log.INFO;
            }

            // is file logging enabled
            if (fileDebugOutput != null) {
                // grab a time stamp
                Date timestamp = new Date(System.currentTimeMillis());

                // need to log to file as well
                try {
                    byte[] data;
                    char levelTag = '?';
                    switch (level) {
                        case Log.ASSERT:
                            levelTag = 'A';
                            break;
                        case Log.DEBUG:
                            levelTag = 'D';
                            break;
                        case Log.ERROR:
                            levelTag = 'E';
                            break;
                        case Log.INFO:
                            levelTag = 'I';
                            break;
                        case Log.VERBOSE:
                            levelTag = 'V';
                            break;
                        case Log.WARN:
                            levelTag = 'W';
                            break;
                    }

                    // write the header
                    String header = String.format(Locale.US, "\n%c/%s %s (%s):\n",
                            levelTag, tag, timestamp.toString(), Thread
                                    .currentThread().toString());
                    data = header.getBytes();
                    fileDebugOutput.write(data, 0, data.length);

                    // write the message
                    if (!TextUtils.isEmpty(msg)) {
                        data = msg.getBytes();
                        fileDebugOutput.write(data, 0, data.length);
                    }

                    // add a new line for subsequent messages
                    fileDebugOutput.write('\n');

                } catch (IOException ignored) {
                }
            }

            // split the logs at newlines to minimize having data dropped by the
            // logcat message size limit,
            // which is incidentally not documented anywhere but appears to be
            // 1024
            String[] parts = msg.split("\n");

            // print out the log message(s)
            for (String part : parts) {
                if (!TextUtils.isEmpty(msg))
                    Log.println(level, tag, part);
            }
        } finally {
            // unlock the debug log
            unlock();
        }
    }
}
