
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * provides the print job specific details
 * 
 */
public class PrintInfo {

    /**
     * Percentage of color pixels in the job
     * 
     */
    @SerializedName("colorPixelDensity")
    @Expose
    private Integer colorPixelDensity;
    /**
     * number of copies printed in a copy job
     * 
     */
    @SerializedName("copiesCount")
    @Expose
    private Integer copiesCount;
    /**
     * number of impressions printed in job
     * 
     */
    @SerializedName("impressionCount")
    @Expose
    private Integer impressionCount;
    /**
     * Percentage of non-white pixels in the job
     * 
     */
    @SerializedName("nonWhitePixelDensity")
    @Expose
    private Integer nonWhitePixelDensity;
    /**
     * print settings (intent) used in the job
     * 
     */
    @SerializedName("printSettings")
    @Expose
    private PrintSettings printSettings;
    @SerializedName("printedSheetInfo")
    @Expose
    private SheetSetInfo printedSheetInfo;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("printingStartTime")
    @Expose
    private Date printingStartTime;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("printingEndTime")
    @Expose
    private Date printingEndTime;
    /**
     * Seconds remaining until job is completed. If the job has not started printing, the expected time is displayed.
     * 
     */
    @SerializedName("remainingPrintingTime")
    @Expose
    private Integer remainingPrintingTime;
    /**
     * Percentage of job completed.
     * 
     */
    @SerializedName("progress")
    @Expose
    private Integer progress;
    /**
     * Only applicable for products with curator module and non-completed jobs. Current curing temperature in celsius degrees
     * 
     */
    @SerializedName("currentCuringTemperature")
    @Expose
    private Integer currentCuringTemperature;
    @SerializedName("usageByMediaSourceAttributes")
    @Expose
    private List<UsageByMediaSourceAttribute> usageByMediaSourceAttributes = new ArrayList<UsageByMediaSourceAttribute>();
    /**
     * array describing the amount of marking agent by supply consumed for printing current job. inkCartridge and tonerCartridge are expected to be the only supplies added to the array.
     * 
     */
    @SerializedName("supplyUsage")
    @Expose
    private List<CurrentSupply> supplyUsage = new ArrayList<CurrentSupply>();
    /**
     * array describing the media used by media type over the lifetime of the printer
     * 
     */
    @SerializedName("areaUsageByMediaKeyId")
    @Expose
    private List<AreaUsageByMediaKeyId> areaUsageByMediaKeyId = new ArrayList<AreaUsageByMediaKeyId>();
    /**
     * array describing the lifetime media and ink used over the lifetime of the printer
     * 
     */
    @SerializedName("usageByPrintCategory")
    @Expose
    private List<UsageByPrintCategory> usageByPrintCategory = new ArrayList<UsageByPrintCategory>();
    /**
     * this provide the file name of print job
     * 
     */
    @SerializedName("fileName")
    @Expose
    private String fileName;

    /**
     * Percentage of color pixels in the job
     * 
     */
    public Integer getColorPixelDensity() {
        return colorPixelDensity;
    }

    /**
     * Percentage of color pixels in the job
     * 
     */
    public void setColorPixelDensity(Integer colorPixelDensity) {
        this.colorPixelDensity = colorPixelDensity;
    }

    /**
     * number of copies printed in a copy job
     * 
     */
    public Integer getCopiesCount() {
        return copiesCount;
    }

    /**
     * number of copies printed in a copy job
     * 
     */
    public void setCopiesCount(Integer copiesCount) {
        this.copiesCount = copiesCount;
    }

    /**
     * number of impressions printed in job
     * 
     */
    public Integer getImpressionCount() {
        return impressionCount;
    }

    /**
     * number of impressions printed in job
     * 
     */
    public void setImpressionCount(Integer impressionCount) {
        this.impressionCount = impressionCount;
    }

    /**
     * Percentage of non-white pixels in the job
     * 
     */
    public Integer getNonWhitePixelDensity() {
        return nonWhitePixelDensity;
    }

    /**
     * Percentage of non-white pixels in the job
     * 
     */
    public void setNonWhitePixelDensity(Integer nonWhitePixelDensity) {
        this.nonWhitePixelDensity = nonWhitePixelDensity;
    }

    /**
     * print settings (intent) used in the job
     * 
     */
    public PrintSettings getPrintSettings() {
        return printSettings;
    }

    /**
     * print settings (intent) used in the job
     * 
     */
    public void setPrintSettings(PrintSettings printSettings) {
        this.printSettings = printSettings;
    }

    public SheetSetInfo getPrintedSheetInfo() {
        return printedSheetInfo;
    }

    public void setPrintedSheetInfo(SheetSetInfo printedSheetInfo) {
        this.printedSheetInfo = printedSheetInfo;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getPrintingStartTime() {
        return printingStartTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setPrintingStartTime(Date printingStartTime) {
        this.printingStartTime = printingStartTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getPrintingEndTime() {
        return printingEndTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setPrintingEndTime(Date printingEndTime) {
        this.printingEndTime = printingEndTime;
    }

    /**
     * Seconds remaining until job is completed. If the job has not started printing, the expected time is displayed.
     * 
     */
    public Integer getRemainingPrintingTime() {
        return remainingPrintingTime;
    }

    /**
     * Seconds remaining until job is completed. If the job has not started printing, the expected time is displayed.
     * 
     */
    public void setRemainingPrintingTime(Integer remainingPrintingTime) {
        this.remainingPrintingTime = remainingPrintingTime;
    }

    /**
     * Percentage of job completed.
     * 
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Percentage of job completed.
     * 
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * Only applicable for products with curator module and non-completed jobs. Current curing temperature in celsius degrees
     * 
     */
    public Integer getCurrentCuringTemperature() {
        return currentCuringTemperature;
    }

    /**
     * Only applicable for products with curator module and non-completed jobs. Current curing temperature in celsius degrees
     * 
     */
    public void setCurrentCuringTemperature(Integer currentCuringTemperature) {
        this.currentCuringTemperature = currentCuringTemperature;
    }

    public List<UsageByMediaSourceAttribute> getUsageByMediaSourceAttributes() {
        return usageByMediaSourceAttributes;
    }

    public void setUsageByMediaSourceAttributes(List<UsageByMediaSourceAttribute> usageByMediaSourceAttributes) {
        this.usageByMediaSourceAttributes = usageByMediaSourceAttributes;
    }

    /**
     * array describing the amount of marking agent by supply consumed for printing current job. inkCartridge and tonerCartridge are expected to be the only supplies added to the array.
     * 
     */
    public List<CurrentSupply> getSupplyUsage() {
        return supplyUsage;
    }

    /**
     * array describing the amount of marking agent by supply consumed for printing current job. inkCartridge and tonerCartridge are expected to be the only supplies added to the array.
     * 
     */
    public void setSupplyUsage(List<CurrentSupply> supplyUsage) {
        this.supplyUsage = supplyUsage;
    }

    /**
     * array describing the media used by media type over the lifetime of the printer
     * 
     */
    public List<AreaUsageByMediaKeyId> getAreaUsageByMediaKeyId() {
        return areaUsageByMediaKeyId;
    }

    /**
     * array describing the media used by media type over the lifetime of the printer
     * 
     */
    public void setAreaUsageByMediaKeyId(List<AreaUsageByMediaKeyId> areaUsageByMediaKeyId) {
        this.areaUsageByMediaKeyId = areaUsageByMediaKeyId;
    }

    /**
     * array describing the lifetime media and ink used over the lifetime of the printer
     * 
     */
    public List<UsageByPrintCategory> getUsageByPrintCategory() {
        return usageByPrintCategory;
    }

    /**
     * array describing the lifetime media and ink used over the lifetime of the printer
     * 
     */
    public void setUsageByPrintCategory(List<UsageByPrintCategory> usageByPrintCategory) {
        this.usageByPrintCategory = usageByPrintCategory;
    }

    /**
     * this provide the file name of print job
     * 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * this provide the file name of print job
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
