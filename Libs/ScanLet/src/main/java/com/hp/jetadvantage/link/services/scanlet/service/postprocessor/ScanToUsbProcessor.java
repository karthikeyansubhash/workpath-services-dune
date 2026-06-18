package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.scanlet.model.ParsedName;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanToUsbProcessor extends LocalDestinationProcessor {
    private static final String TAG = Scanlet.TAG + "/ScanToUsb";
    public ScanToUsbProcessor(BaseJobIntentServiceStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected String getOutputPath(JobInfo jobInfo) {
        String usbLocationPath = getScanAttributesReader().getUsbLocation();
        //1. get USB Storage
        IStorage usbStorage = StorageFactory.INSTANCE.getStorageForPath(MassStorageInfo.StorageType.USB, usbLocationPath);
        if (usbStorage == null) {
            throw new IllegalStateException("USB Storage not found");
        }

        return usbLocationPath;
    }

    @Override
    protected List<String> postProcessFiles(List<File> scannedFileList) {
        //basic process: return absolute paths
        List<String> filePaths = new ArrayList<>();
        for (File file : scannedFileList) {
            filePaths.add(file.getAbsolutePath());
        }
        syncToStorage();
        return filePaths;
    }

    @Override
    protected String getFileName(File originalFile, String timeStamp) {
        //already file path check in getOutputPath()
        File usbLocation = new File(getScanAttributesReader().getUsbLocation());
        ParsedName parsedName = FilenameParser.parse(originalFile.getName());
        String newFilename;

        if (!TextUtils.isEmpty(parsedName.getFilename())) { //file name exist
            if (isFilesWithNameExist(usbLocation, parsedName.getFilename())) { //same name file exist in USB
                newFilename = parsedName.getFilename() + "-" + timeStamp + "-";
            } else {
                newFilename = parsedName.getFilename() + "-";
            }

        } else { //file name not exist, use timeStamp
            newFilename = parsedName.getJob() + "-";
        }
        //set the page number
        newFilename += String.format(Locale.US, "%03d", parsedName.getPage());
        //set the extension
        newFilename += "." + parsedName.getExt();

        return newFilename;
    }

    private boolean isFilesWithNameExist(final File folder, final String baseName) {
        if (!TextUtils.isEmpty(baseName)) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(baseName);
                }
            };
            if (!folder.isDirectory()) {
                return false;
            }
            File[] files = folder.listFiles(filter);
            return files != null && files.length > 0;
        }
        return false;
    }

    /**
     * Flush all pending writes to physical storage via Linux sync command.
     */
    private void syncToStorage() {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("sync");
            p.waitFor();
            Log.d(TAG, "syncToStorage: sync done");
        } catch (IOException | InterruptedException e) {
            Log.w(TAG, "syncToStorage: failed: " + e.getMessage(), e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }
}
