// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.workpath.api.statistics.jobinfo.StatisticsJobInfo;
import com.hp.workpath.api.statistics.jobinfo.driverinfo.DriverInfo;
import com.hp.workpath.api.statistics.jobinfo.emailinfo.EmailInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxInInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxOutInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo;
import com.hp.workpath.api.statistics.jobinfo.folderinfo.FolderInfo;
import com.hp.workpath.api.statistics.jobinfo.ftpinfo.FtpInfo;
import com.hp.workpath.api.statistics.jobinfo.httpinfo.HttpInfo;
import com.hp.workpath.api.statistics.jobinfo.print.PrintInfo;
import com.hp.workpath.api.statistics.jobinfo.scan.ScanInfo;
import com.hp.workpath.api.statistics.jobinfo.userinfo.AuthenticatedUserInfo;
import com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides StatisticsJobData information
 *
 * @since API 5
 */
@DeviceApi
public final class StatisticsJobData {

    /**
     * The job Sequence for which the Job Info is being provided
     *
     * @since API 8
     */
    @Keep
    @SerializedName("jobSequence")
    private Integer jobSequence;

    /**
     * The job Id for which the Job Info is being provided
     *
     * @since API 5
     */
    @Keep
    @SerializedName("jobId")
    private String jobId;

    /**
     * The resource Id for which the Job Info is being provided
     *
     * @since API 5
     */
    @Keep
    @SerializedName("resourceId")
    private String resourceId;

    /**
     * The jobInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("jobInfo")
    private StatisticsJobInfo jobInfo;

    /**
     * The scan jobInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("scanInfo")
    private ScanInfo scanInfo;

    /**
     * The print jobInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("printInfo")
    private PrintInfo printInfo;

    /**
     * The extendedUserInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("extendedUserInfo")
    private ExtendedUserInfo extendedUserInfo;

    /**
     * The emailInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("emailInfo")
    private EmailInfo emailInfo;

    /**
     * The driverInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("driverInfo")
    private DriverInfo driverInfo;

    /**
     * FaxInInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("aFaxInInfo")
    private FaxInInfo aFaxInInfo;

    /**
     * FaxOutInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("aFaxOutInfo")
    private FaxOutInfo aFaxOutInfo;

    /**
     * FaxInInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("ipFaxInInfo")
    private FaxInInfo ipFaxInInfo;

    /**
     * IpFaxOutInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("ipFaxOutInfo")
    private IpFaxOutInfo ipFaxOutInfo;

    /**
     * FolderInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("folderInfo")
    private FolderInfo[] folderInfo;

    /**
     * FtpInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("ftpInfo")
    private FtpInfo[] ftpInfo;

    /**
     * HttpInfo
     *
     * @since API 5
     */
    @Keep
    @SerializedName("httpInfo")
    private HttpInfo[] httpInfo;

    /**
     * Returns jobSequence for statistics job data
     *
     * @return jobSequence
     * <p>
     * <ul>
     * <li>Return value will be greater than or equal to zero</li>
     * </ul>
     * </p>
     * @since API 8
     */
    public Integer getJobSequence() {
        return jobSequence;
    }

    /**
     * Returns Job ID for statistics job data
     *
     * @return jobId
     * <p>
     * <ul>
     * <li>Return can be null if the jobId is null</li>
     * <li>Return can be null if the jobId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Returns Resource ID for statistics job data
     *
     * @return resourceId
     * <p>
     * <ul>
     * <li>Return can be null if the resourceId is null</li>
     * <li>Return can be null if the resourceId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Retuns job info for statistics job data
     *
     * @return jobId
     * <p>
     * <ul>
     * <li>Return can be null if the jobInfo is null</li>
     * <li>Return can be null if the jobInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public StatisticsJobInfo getJobInfo() {
        return jobInfo;
    }

    /**
     * Returns scan info for statistics job data
     *
     * @return scanInfo
     * <p>
     * <ul>
     * <li>Return can be null if the scanInfo is null</li>
     * <li>Return can be null if the scanInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ScanInfo getScanInfo() {
        return scanInfo;
    }

    /**
     * Returns print info for statistics job data
     *
     * @return printInfo
     * <p>
     * <ul>
     * <li>Return can be null if the printInfo is null</li>
     * <li>Return can be null if the printInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public PrintInfo getPrintInfo() {
        return printInfo;
    }

    /**
     * Returns extended user info for statistics job data
     *
     * @return extendedUserInfo
     * <p>
     * <ul>
     * <li>Return can be null if the extendedUserInfo is null</li>
     * <li>Return can be null if the extendedUserInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ExtendedUserInfo getExtendedUserInfo() {
        return extendedUserInfo;
    }

    /**
     * Return email info for statistics job data
     *
     * @return emailInfo
     * <p>
     * <ul>
     * <li>Return can be null if the emailInfo is null</li>
     * <li>Return can be null if the emailInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public EmailInfo getEmailInfo() {
        return emailInfo;
    }

    /**
     * Return device info for statistics job data
     *
     * @return driverInfo
     * <p>
     * <ul>
     * <li>Return can be null if the driverInfo is null</li>
     * <li>Return can be null if the driverInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    /**
     * Returns faxin info for statistics job data
     *
     * @return FaxInInfo
     * <p>
     * <ul>
     * <li>Return can be null if the aFaxInInfo is null</li>
     * <li>Return can be null if the aFaxInInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FaxInInfo getFaxInInfo() {
        return aFaxInInfo;
    }

    /**
     * Returns faxout info for statistics job data
     *
     * @return FaxOutInfo
     * <p>
     * <ul>
     * <li>Return can be null if the aFaxOutInfo is null</li>
     * <li>Return can be null if the aFaxOutInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FaxOutInfo getFaxOutInfo() {
        return aFaxOutInfo;
    }

    /**
     * Retuns ipfaxin info for statistics job data
     *
     * @return ipFaxInInfo
     * <p>
     * <ul>
     * <li>Return can be null if the ipFaxInInfo is null</li>
     * <li>Return can be null if the ipFaxInInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FaxInInfo getIpFaxInInfo() {
        return ipFaxInInfo;
    }

    /**
     * Returns ipFaxout info for statistics job data
     *
     * @return ipFaxOutInfo
     * <p>
     * <ul>
     * <li>Return can be null if the ipFaxOutInfo is null</li>
     * <li>Return can be null if the ipFaxOutInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public IpFaxOutInfo getIpFaxOutInfo() {
        return ipFaxOutInfo;
    }

    /**
     * Returns folder info for statistics job data
     *
     * @return folderInfo
     * <p>
     * <ul>
     * <li>if {@link com.hp.workpath.api.statistics.jobinfo.folderinfo.FolderInfo} field is not added to FolderInfo array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FolderInfo[] getFolderInfo() {
        return folderInfo;
    }

    /**
     * Returns FTP info for statistics job data
     *
     * @return ftpInfo
     * <p>
     * <ul>
     * <li>if {@link com.hp.workpath.api.statistics.jobinfo.ftpinfo.FtpInfo} field is not added to FtpInfo array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FtpInfo[] getFtpInfo() {
        return ftpInfo;
    }

    /**
     * Returns HTTP info for statistics job data
     *
     * @return httpInfo
     * <p>
     * <ul>
     * <li>if {@link com.hp.workpath.api.statistics.jobinfo.httpinfo.HttpInfo} field is not added to HttpInfo array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public HttpInfo[] getHttpInfo() {
        return httpInfo;
    }

    public void setJobSequence(Integer jobSequence) {
        this.jobSequence = jobSequence;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public void setJobInfo(StatisticsJobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
    public void setScanInfo(ScanInfo scanInfo) {
        this.scanInfo = scanInfo;
    }
    public void setPrintInfo(PrintInfo printInfo) {
        this.printInfo = printInfo;
    }
    public void setExtendedUserInfo(ExtendedUserInfo extendedUserInfo) {
        this.extendedUserInfo = extendedUserInfo;
    }
    public void setEmailInfo(EmailInfo emailInfo) {
        this.emailInfo = emailInfo;
    }
    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
    public void setAFaxInInfo(FaxInInfo aFaxInInfo) {
        this.aFaxInInfo = aFaxInInfo;
    }
    public void setAFaxOutInfo(FaxOutInfo aFaxOutInfo) {
        this.aFaxOutInfo = aFaxOutInfo;
    }
    public void setIpFaxInInfo(FaxInInfo ipFaxInInfo) {
        this.ipFaxInInfo = ipFaxInInfo;
    }
    public void setIpFaxOutInfo(IpFaxOutInfo ipFaxOutInfo) {
        this.ipFaxOutInfo = ipFaxOutInfo;
    }
    public void setFolderInfo(FolderInfo[] folderInfo) {
        this.folderInfo = folderInfo;
    }
    public void setFtpInfo(FtpInfo[] ftpInfo) {
        this.ftpInfo = ftpInfo;
    }
    public void setHttpInfo(HttpInfo[] httpInfo) {
        this.httpInfo = httpInfo;
    }

}
