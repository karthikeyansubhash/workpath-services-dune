package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.scanlet.model.ParsedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanToMeProcessor extends LocalDestinationProcessor {

    public ScanToMeProcessor(BaseJobIntentServiceStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected String getOutputPath(JobInfo jobInfo) {
        return getStateMachine().getContext().getFilesDir() + "/.tmp/" + jobInfo.getJobId();
    }

    @Override
    protected List<String> postProcessFiles(List<File> scannedFileList) {
        List<String> grantedFileList = new ArrayList<>();
        for (File scannedFile : scannedFileList) {
            String uri = grantPermission(scannedFile);
            if (uri != null) {
                grantedFileList.add(uri);
            }
        }
        return grantedFileList;
    }

    private String grantPermission(File scannedFile) {
        if (scannedFile.exists()) {
            Uri fileUri = FileProvider.getUriForFile(getStateMachine().getContext(), getStateMachine().getContext().getPackageName() + ".provider", scannedFile);
            getStateMachine().getContext().grantUriPermission(getStateMachine().getTargetPackageName(), fileUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            return fileUri.toString();
        }
        return null;
    }

    @Override
    protected String getFileName(File originalFile, String timeStamp) {
        ParsedName parsedName = FilenameParser.parse(originalFile.getName());

        String newFilename;
        if (!TextUtils.isEmpty(parsedName.getFilename())) {
            newFilename = parsedName.getFilename() + "-";
        } else {
            newFilename = parsedName.getJob() + "-";
        }
        newFilename += String.format(Locale.US, "%03d", parsedName.getPage());
        newFilename += "." + parsedName.getExt();
        return newFilename;
    }
}
