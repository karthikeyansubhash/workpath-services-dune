// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.utils;

import android.os.Environment;
import android.os.FileObserver;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SLog {
    private static final String TAGS_PREFIX = "[WS]";
    private static Logger logger;
    private static final String PROPERTIES_FILE_NAME = "logger.properties";
    private static final String DOWNLOAD_FOLDER;
    private static final String PROPERTIES_FOLDER_PATH;
    private static final String PROPERTIES_FILE_PATH;

    private static final String DEFAULT_LOG_FILE_NAME = "sdklog_%g.txt";
    private static final int DEFAULT_LOG_FILE_SIZE = 512 * 1024;
    private static final int DEFAULT_LOG_FILE_COUNT = 5;
    private static final Level DEFAULT_LOG_LEVEL = Level.ALL;

    private static final String PATTERN_DOWNLOAD = "{Download}";

    private static final ArrayList<PropertiesFileObserver> sFileObserver = new ArrayList<>();

    static {
        String envDownloadPath = "";
        try {
            envDownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        } catch (NullPointerException e)
        {
            //null is ok for unit testing
        }
        DOWNLOAD_FOLDER = envDownloadPath;
        PROPERTIES_FOLDER_PATH = DOWNLOAD_FOLDER + File.separator + "SDK";
        PROPERTIES_FILE_PATH = PROPERTIES_FOLDER_PATH + File.separator + PROPERTIES_FILE_NAME;

        setLogger();
        PropertiesFileObserver fileObserver = new PropertiesFileObserver(PROPERTIES_FOLDER_PATH);
        fileObserver.startWatching();
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable t){
        if (TextUtils.isEmpty(tag)) tag = "";
        if (TextUtils.isEmpty(msg)) msg = "";
        if (logger != null) {
            if(t == null) {
                logger.log(Level.FINE, String.format("V/%s: %s\n", tag, msg));
            } else {
                logger.log(Level.FINE, String.format("V/%s: %s\n", tag, msg), t);
            }
        }
        if(t == null) {
            Log.v(TAGS_PREFIX + tag, msg);
        } else {
            Log.v(TAGS_PREFIX + tag, msg, t);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable t){
        if (TextUtils.isEmpty(tag)) tag = "";
        if (TextUtils.isEmpty(msg)) msg = "";
        if (logger != null) {
            if(t == null) {
                logger.log(Level.CONFIG, String.format("D/%s: %s\n", tag, msg));
            } else {
                logger.log(Level.CONFIG, String.format("D/%s: %s\n", tag, msg), t);
            }

        }
        if(t == null) {
            Log.d(TAGS_PREFIX + tag, msg);
        } else {
            Log.d(TAGS_PREFIX + tag, msg, t);
        }
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable t){
        if (TextUtils.isEmpty(tag)) tag = "";
        if (TextUtils.isEmpty(msg)) msg = "";
        if (logger != null) {
            if(t == null) {
                logger.log(Level.INFO, String.format("I/%s: %s\n", tag, msg));
            } else {
                logger.log(Level.INFO, String.format("I/%s: %s\n", tag, msg), t);
            }

        }
        if(t == null) {
            Log.i(TAGS_PREFIX + tag, msg);
        } else {
            Log.i(TAGS_PREFIX + tag, msg, t);
        }
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable t){
        if (TextUtils.isEmpty(tag)) tag = "";
        if (TextUtils.isEmpty(msg)) msg = "";
        if (logger != null) {
            if(t == null) {
                logger.log(Level.WARNING, String.format("W/%s: %s\n", tag, msg));
            } else {
                logger.log(Level.WARNING, String.format("W/%s: %s\n", tag, msg), t);
            }

        }
        if(t == null) {
            Log.w(TAGS_PREFIX + tag, msg);
        } else {
            Log.w(TAGS_PREFIX + tag, msg, t);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable t){
        if (TextUtils.isEmpty(tag)) tag = "";
        if (TextUtils.isEmpty(msg)) msg = "";
        if (logger != null) {
            if(t == null) {
                logger.log(Level.SEVERE, String.format("E/%s: %s\n", tag, msg));
            } else {
                logger.log(Level.SEVERE, String.format("E/%s: %s\n", tag, msg), t);
            }
        }

        if(t == null) {
            Log.e(TAGS_PREFIX + tag, msg);
        } else {
            Log.e(TAGS_PREFIX + tag, msg, t);
        }

    }

    private static void setLogger() {
        File logProperties = new File(PROPERTIES_FILE_PATH);
        logger = null;
        logger = Logger.getLogger(SLog.class.getName());
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();

        getLogProperties(logProperties);

        if (logger != null) {
            logManager.addLogger(logger);
        }
    }

    private static void getLogProperties(File file){
        String filePath = PROPERTIES_FOLDER_PATH + File.separator + DEFAULT_LOG_FILE_NAME;
        int fileSizeLimit = DEFAULT_LOG_FILE_SIZE;
        int fileMaxCount = DEFAULT_LOG_FILE_COUNT;
        Level fileLevel = DEFAULT_LOG_LEVEL;
        String filter = "";

        FileInputStream is = null;
        BufferedReader br = null;
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("SLog.FileHandler.pattern")) {
                        try {
                            filePath = parsingValues(line);
                            if (filePath.contains(PATTERN_DOWNLOAD)) {
                                filePath = filePath.replace(PATTERN_DOWNLOAD, DOWNLOAD_FOLDER);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (line.contains("SLog.FileHandler.limit")) {
                        try {
                            String limitStr = parsingValues(line);
                            fileSizeLimit = Integer.getInteger(limitStr);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (line.contains("SLog.FileHandler.count")) {
                        try {
                            String CountStr = parsingValues(line);
                            fileMaxCount = Integer.getInteger(CountStr);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (line.contains("SLog.FileHandler.level")) {
                        try {
                            String levelStr = parsingValues(line);
                            fileLevel = getLevel(levelStr);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (line.contains("SLog.FileHandler.filter")) {
                        try {
                            filter = parsingValues(line);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }

                Log.i(TAGS_PREFIX, "SLog file is created: " + filePath);
                createFolder(filePath);

                FileHandler fileHandler = new FileHandler(filePath, fileSizeLimit, fileMaxCount, true);
                fileHandler.setFormatter(new SLogFormatter());
                logger = Logger.getLogger(SLog.class.getName());
                logger.addHandler(fileHandler);
                logger.setLevel(fileLevel);
                logger.setUseParentHandlers(false);
                logger.setFilter(new LoggerFilter(filter));
            }
        } catch(IOException ioe) {
        }finally {
            if (is != null) try { is.close(); } catch (IOException ioe) {}
            if (br != null) try { br.close(); } catch (IOException ioe) {}
        }
    }

    static class LoggerFilter implements Filter {

        String filter = "";

        LoggerFilter(String filter){
            this.filter = filter;
        }

        @Override
        public boolean isLoggable(LogRecord logRecord) {
            return logRecord.getMessage().contains(filter);
        }
    }

    private static String parsingValues(String line) {
        int start = line.indexOf("=");
        return line.substring(start + 1, line.length()).replace(" ", "");
    }

    private static Level getLevel(String levelStr) {
        if (Level.CONFIG.getName().equals(levelStr)) {
            return Level.CONFIG;
        } else if (Level.FINE.getName().equals(levelStr)) {
            return Level.FINE;
        } else if (Level.FINER.getName().equals(levelStr)) {
            return Level.FINER;
        } else if (Level.FINEST.getName().equals(levelStr)) {
            return Level.FINEST;
        } else if (Level.INFO.getName().equals(levelStr)) {
            return Level.INFO;
        } else if (Level.OFF.getName().equals(levelStr)) {
            return Level.OFF;
        } else if (Level.SEVERE.getName().equals(levelStr)) {
            return Level.SEVERE;
        } else if (Level.WARNING.getName().equals(levelStr)) {
            return Level.WARNING;
        } else {
            return Level.ALL;
        }
    }

    private static void createFolder(String logFilePath) {
        File filePath = new File(logFilePath);
        File folderPath = filePath.getParentFile();
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }
    }

    static class PropertiesFileObserver extends FileObserver {

        public PropertiesFileObserver(String path) {
            super(path);
            sFileObserver.add(this);
        }

        @Override
        public void onEvent(int i, String s) {
            if (PROPERTIES_FILE_NAME.equals(s)) {
                if (i == FileObserver.MODIFY ||
                        i == FileObserver.DELETE) {
                    setLogger();
                }
            }
        }
    }

    static class SLogFormatter extends Formatter {

        @Override
        public String format(LogRecord logRecord) {
            Date date = new Date();
            SimpleDateFormat formatter =
                    new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
            date.setTime(System.currentTimeMillis());

            StringBuilder ret = new StringBuilder(80);
            ret.append(formatter.format(date));
            ret.append(logRecord.getMessage());
            return ret.toString();
        }
    }
}
