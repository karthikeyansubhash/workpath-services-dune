package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.helper.email.EmailAttributes;
import com.hp.jetadvantage.link.api.helper.email.NetworkCredentialsAttributes;
import com.hp.jetadvantage.link.api.helper.email.SmtpAttributes;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.scanner.EmailAddressInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEmailService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;
import com.hp.jetadvantage.link.services.emaillet.util.EmailLetUtils;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.scanlet.model.ParsedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanToEmailProcessor extends LocalDestinationProcessor {
    public ScanToEmailProcessor(BaseJobIntentServiceStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected List<String> postProcessFiles(List<File> scannedFileList) throws Exception {
        List<String> filePathList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        for (File file : scannedFileList) {
            fileNameList.add(file.getName());
            filePathList.add(file.getAbsolutePath());
        }

        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        EmailSettingsData emailSettingsData = deviceEmailService.getEmailSettings();

        EmailLetUtils.send(emailSettingsData, getSmtpAttributes(getScanAttributesReader().getSmtpAttributes()),
                getEmailAttributes(getScanAttributesReader().getEmailAttributes(), filePathList));

        return fileNameList;
    }

    @Override
    protected String getOutputPath(JobInfo jobInfo) {
        return getStateMachine().getContext().getFilesDir() + "/.tmp/" + jobInfo.getJobId();
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

    private SmtpAttributes getSmtpAttributes(com.hp.jetadvantage.link.api.scanner.SmtpAttributes smtpAttributes)
            throws CapabilitiesExceededException {
        if (smtpAttributes == null) {
            return null;
        }

        return new SmtpAttributes.Builder(smtpAttributes.getHost())
                .setPort(smtpAttributes.getPort())
                .setConnectTimeout(smtpAttributes.getConnectTimeout())
                .setReadTimeout(smtpAttributes.getReadTimeout())
                .setServerCredentials(
                        smtpAttributes.getServerCredentials() != null ?
                                new NetworkCredentialsAttributes.Builder()
                                        .setUserName(smtpAttributes.getServerCredentials().getUsername())
                                        .setPassword(smtpAttributes.getServerCredentials().getPassword())
                                        .setDomain(smtpAttributes.getServerCredentials().getDomain())
                                        .build()
                                : null)
                .setTransportMode(SmtpAttributes.TransportMode.valueOf(smtpAttributes.getTransportMode().name()))
                .build();
    }

    private EmailAttributes getEmailAttributes(@NonNull com.hp.jetadvantage.link.api.scanner.EmailAttributes emailAttributes,
                                               List<String> fileList) throws CapabilitiesExceededException {
        EmailAttributes.Builder builder = new EmailAttributes.Builder();

        if (emailAttributes.getFrom() != null) {
            builder.setFrom(emailAttributes.getFrom().getAddress(), emailAttributes.getFrom().getName());
        }

        if (emailAttributes.getSubject() != null) {
            builder.setSubject(emailAttributes.getSubject());
        }

        if (emailAttributes.getMessage() != null) {
            builder.setMessage(emailAttributes.getMessage());
        }

        if (emailAttributes.getTo() != null) {
            for (EmailAddressInfo emailAddressInfo : emailAttributes.getTo()) {
                builder.addToAddress(emailAddressInfo.getAddress(), emailAddressInfo.getName());
            }
        }

        if (emailAttributes.getCc() != null) {
            for (EmailAddressInfo emailAddressInfo : emailAttributes.getCc()) {
                builder.addCcAddress(emailAddressInfo.getAddress(), emailAddressInfo.getName());
            }
        }

        if (emailAttributes.getBcc() != null) {
            for (EmailAddressInfo emailAddressInfo : emailAttributes.getBcc()) {
                builder.addBccAddress(emailAddressInfo.getAddress(), emailAddressInfo.getName());
            }
        }

        for (String filePath : fileList) {
            builder.addAttachments(new File(filePath));
        }

        return builder.build();
    }

}
