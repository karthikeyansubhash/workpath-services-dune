package com.hp.jetadvantage.link.services.emaillet.util;

import android.text.TextUtils;

import com.hp.jetadvantage.link.api.helper.email.EmailAddressInfo;
import com.hp.jetadvantage.link.api.helper.email.EmailAttributes;
import com.hp.jetadvantage.link.api.helper.email.Emaillet;
import com.hp.jetadvantage.link.api.helper.email.ProxyAttributes;
import com.hp.jetadvantage.link.api.helper.email.SmtpAttributes;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.io.File;
import java.util.List;

public class EmailLetUtils {

    private static final String TAG = Emaillet.TAG;

    public static void send(EmailSettingsData emailSettingsData,
                             SmtpAttributes smtpAttributes, EmailAttributes emailAttributes) throws Exception {
        send(emailSettingsData, smtpAttributes, emailAttributes, null, null);
    }

    public static void send(EmailSettingsData emailSettingsData,
                             SmtpAttributes smtpAttributes, EmailAttributes emailAttributes,
                             ProxyAttributes proxyAttributes) throws Exception {
        send(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes, null);
    }

    public static void send(EmailSettingsData emailSettingsData,
                             SmtpAttributes smtpAttributes, EmailAttributes emailAttributes, List<String> attachedFileLists) throws Exception {
        send(emailSettingsData, smtpAttributes, emailAttributes, null, attachedFileLists);
    }

    public static void send(EmailSettingsData emailSettingsData,
                             SmtpAttributes smtpAttributes, EmailAttributes emailAttributes,
                             ProxyAttributes proxyAttributes, List<String> attachedFileNames) throws Exception {

        MultiPartEmail email = null;
        try {
            email = buildEmail(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes, attachedFileNames, false);
            email.send();
            SLog.d(TAG, "Email sending is finished");
        } catch (EmailException emailException) {
            SLog.d(TAG, "Email send error : " + emailException.getMessage());
            if (smtpAttributes == null && emailSettingsData.getUseSSL()) {
                SLog.d(TAG, "Retry to send email to TLS");
                email = buildEmail(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes, attachedFileNames, true);
                email.send();
                SLog.d(TAG, "Email sending is finished");
            } else {
                throw emailException;
            }

        } catch (Throwable t) {
            SLog.e(TAG, "Email send error : " + t.getMessage());
            throw new Exception(t.getMessage());
        } finally {
            if (attachedFileNames != null) {
                if (attachedFileNames.size() > 0) {
                    for (String fileName : attachedFileNames) {
                        try {
                            SLog.d(TAG, "Delete files: " + fileName);
                            new File(fileName).delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private static MultiPartEmail buildEmail(EmailSettingsData emailSettingsData,
                                             SmtpAttributes smtpAttributes, EmailAttributes emailAttributes,
                                             ProxyAttributes proxyAttributes, List<String> attachedFileNames,
                                             boolean forceTls) throws Exception {
        MultiPartEmail email = new MultiPartEmail();
        try {
            if (smtpAttributes != null) {
                if (smtpAttributes.getHost() == null) {
                    throw new IllegalArgumentException("SMTP host value must be provided");
                }

                email.setHostName(smtpAttributes.getHost());
                email.setSmtpPort(smtpAttributes.getPort());
                email.setSocketConnectionTimeout(smtpAttributes.getConnectTimeout() * 1000);
                email.setSocketTimeout(smtpAttributes.getReadTimeout() * 1000);
                switch (smtpAttributes.getTransportMode()) {
                    case SSL_TLS:
                        email.setSSLOnConnect(true);
                        email.setSslSmtpPort(Integer.toString(smtpAttributes.getPort()));
                        break;
                    case START_TLS:
                        email.setStartTLSEnabled(true);
                        email.setStartTLSRequired(true);
                }

                if (smtpAttributes.getServerCredentials() != null) {
                    if (!TextUtils.isEmpty(smtpAttributes.getServerCredentials().getUsername())) {
                        email.setAuthentication(
                                smtpAttributes.getServerCredentials().getUsername(),
                                !TextUtils.isEmpty(smtpAttributes.getServerCredentials().getPassword()) ?
                                        smtpAttributes.getServerCredentials().getPassword() : "");
                    }
                }
            } else {
                if (emailSettingsData == null || !emailSettingsData.isSendToEmailEnabled() || emailSettingsData.getSmtpServerHostName() == null || emailSettingsData.getSmtpServerPort() == null) {
                    throw new IllegalArgumentException("Email settings are not configured");
                }

                email.setHostName(emailSettingsData.getSmtpServerHostName());
                email.setSmtpPort(emailSettingsData.getSmtpServerPort());

                if (emailSettingsData.getAuthenticationRequired() && !TextUtils.isEmpty(emailSettingsData.getSmtpServerUserName())) {
                    email.setAuthentication(emailSettingsData.getSmtpServerUserName(),
                            emailSettingsData.getSmtpServerPassword());
                }

                if (forceTls) {
                    email.setStartTLSEnabled(true);
                    email.setStartTLSRequired(true);
                } else if (emailSettingsData.getUseSSL()) {
                    email.setSSLOnConnect(true);
                    email.setSslSmtpPort(Integer.toString(emailSettingsData.getSmtpServerPort()));
                }
            }

            if (emailAttributes.getFrom() != null) {
                email.setFrom(emailAttributes.getFrom().getAddress(), emailAttributes.getFrom().getName());
            } else if (emailSettingsData != null && emailSettingsData.getDefaultFrom() != null
                    && !TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getEmailAddress())) {
                email.setFrom(emailSettingsData.getDefaultFrom().getEmailAddress(),
                        (TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDisplayName()) ? "" : emailSettingsData.getDefaultFrom().getDisplayName()));
            } else if (emailSettingsData != null && emailSettingsData.getDefaultFrom() != null
                    && !TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDefaultEmailAddress())) {
                email.setFrom(emailSettingsData.getDefaultFrom().getDefaultEmailAddress(),
                        (TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDisplayName()) ? "" : emailSettingsData.getDefaultFrom().getDisplayName()));
            } else {
                throw new IllegalArgumentException("FROM value is missing in parameters and default value is not configured");
            }

            if (emailAttributes.getSubject() != null) {
                email.setSubject(emailAttributes.getSubject());
//            } else if (!TextUtils.isEmpty(emailSettingsData.getDefaultSubject())) {
//                email.setSubject(emailSettingsData.getDefaultSubject());
            }

            if (emailAttributes.getMessage() != null) {
                email.setMsg(emailAttributes.getMessage());
//            } else if (!TextUtils.isEmpty(emailSettingsData.getDefaultSubject())) {
//                email.setMsg(emailSettingsData.getDefaultMessage());
            }

            if (emailAttributes.getTo() != null) {
                for (EmailAddressInfo emailAddressInfo : emailAttributes.getTo()) {
                    email.addTo(emailAddressInfo.getAddress(), emailAddressInfo.getName());
                }
            }

            if (emailAttributes.getCc() != null) {
                for (EmailAddressInfo emailAddressInfo : emailAttributes.getCc()) {
                    email.addCc(emailAddressInfo.getAddress(), emailAddressInfo.getName());
                }
            }

            if (emailAttributes.getBcc() != null) {
                for (EmailAddressInfo emailAddressInfo : emailAttributes.getBcc()) {
                    email.addBcc(emailAddressInfo.getAddress(), emailAddressInfo.getName());
                }
            }

            if (attachedFileNames != null) {
                if (attachedFileNames.size() != emailAttributes.getAttachments().size()) {
                    SLog.d(TAG, "Invalid email parameters");
                    throw new IllegalArgumentException("Failed to attach files");
                }
            }

            int inx = 0;
            for (File file : emailAttributes.getAttachments()) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                if (attachedFileNames != null && attachedFileNames.size() > 0) {
                    attachment.setPath(attachedFileNames.get(inx));
                } else {
                    attachment.setPath(file.getAbsolutePath());
                }
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(file.getName());

                email.attach(attachment);
                inx++;

                SLog.d(TAG, "File is attached: " + attachment.getName());
            }

            // clear proxy setting initially (no proxy)
            email.getMailSession().getProperties().remove("mail.smtp.proxy.host");
            email.getMailSession().getProperties().remove("mail.smtp.proxy.port");

            if (proxyAttributes != null) {
                switch (proxyAttributes.getProxyConfigurationMode()) {
                    case SYSTEM:
                        // take system proxy settings if present
                        String proxyHost = System.getProperty("http.proxyHost");
                        if (!TextUtils.isEmpty(proxyHost)) {
                            email.getMailSession().getProperties().setProperty("mail.smtp.proxy.host", proxyHost);
                            email.getMailSession().getProperties().setProperty("mail.smtp.proxy.port", System.getProperty("http.proxyPort"));
                        }
                        break;
                    case CUSTOM:
                        // take proxy settings from provided attributes
                        email.getMailSession().getProperties().setProperty("mail.smtp.proxy.host", proxyAttributes.getHost());
                        email.getMailSession().getProperties().setProperty("mail.smtp.proxy.port", Integer.toString(proxyAttributes.getPort()));
                        break;
                }
            }
        } catch (EmailException e) {
            SLog.d(TAG, "Invalid email parameters");
            throw new IllegalArgumentException(e);
        }

        return email;
    }

}
