package com.hp.jetadvantage.link.services.statisticslet.adapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.ext.service.jobStatistics.Job;
import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.ScannedSheetSet;
import com.hp.ext.types.common.LocalPointInTime;
import com.hp.ext.types.usage.Counter;
import com.hp.ext.types.usage.FixedPointCounter;
import com.hp.workpath.internal.utils.adapter.FixedPointCounterConverter;
import com.hp.ext.types.usage.UnitCounter;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceStatisticsService;
import com.hp.workpath.api.statistics.StatisticsJobs;
import com.hp.workpath.api.statistics.Statisticslet;
import com.hp.workpath.api.statistics.StatisticsJobData;
import com.hp.workpath.api.statistics.jobinfo.FileInfo;
import com.hp.workpath.api.statistics.jobinfo.StatisticsJobInfo;
import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.workpath.api.statistics.jobinfo.Timestamp;
import com.hp.workpath.api.statistics.jobinfo.print.PrintAgentInfo;
import com.hp.workpath.api.statistics.jobinfo.print.PrintSettings;
import com.hp.workpath.api.statistics.jobinfo.print.PrintedSheetInfo;
import com.hp.workpath.api.statistics.jobinfo.scan.ScanInfo;
import com.hp.workpath.api.statistics.jobinfo.print.PrintInfo;
import com.hp.workpath.api.statistics.jobinfo.driverinfo.DriverInfo;
import com.hp.workpath.api.statistics.jobinfo.emailinfo.EmailInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxInInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxOutInfo;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxAttributes;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo;
import com.hp.workpath.api.statistics.jobinfo.folderinfo.FolderInfo;
import com.hp.workpath.api.statistics.jobinfo.ftpinfo.FtpInfo;
import com.hp.workpath.api.statistics.jobinfo.httpinfo.HttpInfo;
import com.hp.workpath.api.statistics.jobinfo.scan.ScannedSheetInfo;
import com.hp.workpath.api.statistics.jobinfo.userinfo.AuthenticatedUserInfo;
import com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StatisticsAdapter {
    private static final String TAG = Statisticslet.TAG + "/StatisticsAdapter";
    private static final Gson jsonParser = new GsonBuilder()
            .serializeNulls()
            .create();


    public static boolean isSupported(IDeviceStatisticsService statisticsService) {
        return statisticsService.isSupported();
    }

    public static String getAllJobsList(IDeviceStatisticsService statisticsService, String packName, int offset, int limit) {
        Jobs result = statisticsService.getAllJobsList(packName, offset, limit);
        return convertJobsToWorkpathInfo(result);
    }

    public static String getJobWithLastSequenceNumberProcessed(IDeviceStatisticsService statisticsService, String packName) {
        Jobs result = statisticsService.getJobWithLastSequenceNumberProcessed(packName);
        return convertLastJobSequence(result);
    }

    public static String getJobsList(IDeviceStatisticsService statisticsService, String packName) {
        Jobs result = statisticsService.getJobsList(packName);
        return convertJobsToWorkpathInfo(result);
    }

    public static String commitLastJobSequence(IDeviceStatisticsService statisticsService, String packageName, int lastSequenceNumberProcessed) {
        Jobs result = statisticsService.commitLastJobSequence(packageName, lastSequenceNumberProcessed);
        return convertLastJobSequence(result);

    }

    public static String convertLastJobSequence(Jobs jobs) {
        try {
            if (jobs == null) {
                Log.w(TAG, "Jobs is null");
                return "{}";
            }

            JSONObject result = new JSONObject();

            long lastSequenceNumberProcessed = 0;
            if (jobs.getLastSequenceNumberProcessed() != null &&
                    jobs.getLastSequenceNumberProcessed().getValue() != null) {
                lastSequenceNumberProcessed = jobs.getLastSequenceNumberProcessed().getValue();
            }
            result.put("lastSequenceNumberProcessed", lastSequenceNumberProcessed);

            Log.d(TAG, "LastJobSequence result: " + result);
            return result.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error converting lastJobSequence", e);
            return "{}";
        }
    }


    public static String convertJobsToWorkpathInfo(Jobs jobs) {
        if (jobs == null) {
            return null;
        }

        try {
            StatisticsJobs statisticsJobs = new StatisticsJobs();

            // memberIds (List<SequenceNumber> → List<String>)
            if (jobs.getMemberIds() != null) {
                List<String> memberIds = new ArrayList<>();
                for (Job job : jobs.getMembers()) {
                    String memberIdString = jobIdentifierToString(job.getJobId());
                    if (memberIdString != null) {
                        memberIds.add(memberIdString);
                    }
                }
                statisticsJobs.setMemberIds(memberIds);
            }

            // members (List<Job> → List<StatisticsJobData>)
            Log.d(TAG, "Converting " + jobs.getMembers().size() + " jobs");
            if (jobs.getMembers() != null) {
                List<StatisticsJobData> members = new ArrayList<>();
                for (Job job : jobs.getMembers()) {
                    StatisticsJobData jobData = convertJobToStatisticsJobData(job);
                    Log.d(TAG, "Converted job: " + (jobData != null ? "SUCCESS" : "FAILED"));
                    if (jobData != null) {
                        members.add(jobData);
                    }
                }
                Log.d(TAG, "Final members size: " + members.size());
                statisticsJobs.setMembers(members);
            }


            statisticsJobs.setOffset(safeLongToInt(jobs.getOffset()));
            statisticsJobs.setSelectedCount(safeLongToInt(jobs.getSelectedCount()));
            statisticsJobs.setTotalCount(safeLongToInt(jobs.getTotalCount()));
            statisticsJobs.setTotalJobEntries(safeLongToInt(jobs.getTotalJobEntries()));

            return jsonParser.toJson(statisticsJobs);

        } catch (Exception e) {
            Log.e(TAG, "Error converting Jobs to JSON", e);
            return null;
        }
    }

    private static StatisticsJobData convertJobToStatisticsJobData(Job job) {
        try {
            if (job == null) {
                return null;
            }

            StatisticsJobData jobData = new StatisticsJobData();
            String jobIdString = jobIdentifierToString(job.getJobId());

            // 1. resourceId
            if (jobIdString != null) {
                jobData.setResourceId(jobIdString);
            }

            // 2. jobId
            if (jobIdString != null) {
                jobData.setJobId(jobIdString);
            }

            // 3. jobInfo
            if (job.getJobInfo() != null) {
                jobData.setJobInfo(convertToJobInfo(job.getJobInfo()));
            } else {
                jobData.setJobInfo(null);
            }

            // 4. extendedUserInfo
            if(job.getUserInfo() != null) {
                jobData.setExtendedUserInfo(convertToExtendedUserInfo(job.getUserInfo()));
            } else {
                jobData.setExtendedUserInfo(null);
            }

            // 5. scanInfo
            if (job.getScanInfo() != null) {
                jobData.setScanInfo(convertToScanInfo(job.getScanInfo()));
            } else {
                jobData.setScanInfo(null);
            }

            // 6. printInfo
            if (job.getPrintInfo() != null) {
                jobData.setPrintInfo(convertToPrintInfo(job.getPrintInfo()));
            } else {
                jobData.setPrintInfo(null);
            }

            // 7. emailInfo
            if (job.getEmailInfo() != null) {
                jobData.setEmailInfo(convertToEmailInfo(job.getEmailInfo()));
            } else {
                jobData.setEmailInfo(null);
            }

            // 8. driverInfo
            if (job.getDriverInfo() != null) {
                jobData.setDriverInfo(convertToDriverInfo(job.getDriverInfo()));
            } else {
                jobData.setDriverInfo(null);
            }

            // 9. aFaxInInfo
            if (job.getAFaxInInfo() != null) {
                jobData.setAFaxInInfo(convertToFaxInInfo(job.getAFaxInInfo()));
            } else {
                jobData.setAFaxInInfo(null);
            }

            // 10. aFaxOutInfo
            if (job.getAFaxOutInfo() != null) {
                jobData.setAFaxOutInfo(convertToFaxOutInfo(job.getAFaxOutInfo()));
            } else {
                jobData.setAFaxOutInfo(null);
            }

            // 11. ipFaxInInfo
            if (job.getIpFaxInInfo() != null) {
                jobData.setIpFaxInInfo(convertToIpFaxInInfo(job.getIpFaxInInfo()));
            } else {
                jobData.setIpFaxInInfo(null);
            }

            // 12. ipFaxOutInfo
            if (job.getIpFaxOutInfo() != null) {
                jobData.setIpFaxOutInfo(convertToIpFaxOutInfo(job.getIpFaxOutInfo()));
            } else {
                jobData.setIpFaxOutInfo(null);
            }

            // 13. folderInfo
            if (job.getFolderInfo() != null) {
                java.util.List<com.hp.ext.service.jobStatistics.FolderInfo> e2FolderInfoList = job.getFolderInfo();
                com.hp.workpath.api.statistics.jobinfo.folderinfo.FolderInfo[] folderInfoArray =
                        new com.hp.workpath.api.statistics.jobinfo.folderinfo.FolderInfo[e2FolderInfoList.size()];

                for (int i = 0; i < e2FolderInfoList.size(); i++) {
                    if (e2FolderInfoList.get(i) != null) {
                        folderInfoArray[i] = convertToFolderInfoArray(e2FolderInfoList.get(i));
                    }
                }
                jobData.setFolderInfo(folderInfoArray);
            } else {
                jobData.setFolderInfo(null);
            }

            // 14. ftpInfo
            if (job.getFtpInfo() != null) {
                java.util.List<com.hp.ext.service.jobStatistics.FtpInfo> e2FtpInfoList = job.getFtpInfo();
                com.hp.workpath.api.statistics.jobinfo.ftpinfo.FtpInfo[] ftpInfoArray =
                        new com.hp.workpath.api.statistics.jobinfo.ftpinfo.FtpInfo[e2FtpInfoList.size()];

                for (int i = 0; i < e2FtpInfoList.size(); i++) {
                    if (e2FtpInfoList.get(i) != null) {
                        ftpInfoArray[i] = convertToFtpInfoArray(e2FtpInfoList.get(i));
                    }
                }
                jobData.setFtpInfo(ftpInfoArray);
            } else {
                jobData.setFtpInfo(null);
            }

            // 15. httpInfo
            if (job.getHttpInfo() != null) {
                java.util.List<com.hp.ext.service.jobStatistics.HttpInfo> e2HttpInfoList = job.getHttpInfo();
                com.hp.workpath.api.statistics.jobinfo.httpinfo.HttpInfo[] httpInfoArray =
                        new com.hp.workpath.api.statistics.jobinfo.httpinfo.HttpInfo[e2HttpInfoList.size()];

                for (int i = 0; i < e2HttpInfoList.size(); i++) {
                    if (e2HttpInfoList.get(i) != null) {
                        httpInfoArray[i] = convertToHttpInfoArray(e2HttpInfoList.get(i));
                    }
                }
                jobData.setHttpInfo(httpInfoArray);
            } else {
                jobData.setHttpInfo(null);
            }

            return jobData;

        } catch (Exception e) {
            Log.e(TAG, "Error converting Job to StatisticsJobData", e);
            return null;
        }
    }

    // ScanInfo
    private static ScanInfo convertToScanInfo(com.hp.ext.service.jobStatistics.ScanInfo e2ScanInfo) {
        try {
            ScanInfo scanInfo = new ScanInfo();
            scanInfo.setTotalSheets(safeCounterToInt(e2ScanInfo.getTotalSheets()));
            scanInfo.setAdfSheets(safeCounterToInt(e2ScanInfo.getAdfSheets()));
            scanInfo.setFlatbedSheets(safeCounterToInt(e2ScanInfo.getFlatbedSheets()));
            scanInfo.setSimplexSheets(safeCounterToInt(e2ScanInfo.getSimplexSheets()));
            scanInfo.setDuplexSheets(safeCounterToInt(e2ScanInfo.getDuplexSheets()));
            scanInfo.setA4EquivalentAdfDeciSheets(safeDeciCounterToInt(e2ScanInfo.getA4EquivalentAdfDeciSheets()));
            scanInfo.setA4EquivalentDuplexDeciSheets(safeDeciCounterToInt(e2ScanInfo.getA4EquivalentDuplexDeciSheets()));
            scanInfo.setA4EquivalentFlatbedlDeciSheets(safeDeciCounterToInt(e2ScanInfo.getA4EquivalentFlatbedlDeciSheets()));
            scanInfo.setA4EquivalentSimplexDeciSheets(safeDeciCounterToInt(e2ScanInfo.getA4EquivalentSimplexDeciSheets()));
            scanInfo.setA4EquivalentTotalDeciSheets(safeDeciCounterToInt(e2ScanInfo.getA4EquivalentTotalDeciSheets()));
            scanInfo.setScannedSheetInfo(convertToScannedSheetInfo(e2ScanInfo.getScannedSheetInfo()));
            return scanInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting scanInfo", e);
            return null;
        }
    }

    // PrintInfo
    private static PrintInfo convertToPrintInfo(com.hp.ext.service.jobStatistics.PrintInfo e2PrintInfo) {
        try {
            PrintInfo printInfo = new PrintInfo();
            printInfo.setTotalSheets(safeCounterToInt(e2PrintInfo.getTotalSheets()));
            printInfo.setTotalImpressions(safeCounterToInt(e2PrintInfo.getTotalImpressions()));
            printInfo.setColorImpressions(safeCounterToInt(e2PrintInfo.getColorImpressions()));
            printInfo.setMonochromeImpressions(safeCounterToInt(e2PrintInfo.getMonochromeImpressions()));
            printInfo.setSimplexSheets(safeCounterToInt(e2PrintInfo.getSimplexSheets()));
            printInfo.setDuplexSheets(safeCounterToInt(e2PrintInfo.getDuplexSheets()));
            printInfo.setBlankSides(safeCounterToInt(e2PrintInfo.getBlankSides()));
            printInfo.setA4EquivalentTotalDeciSheets(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentTotalDeciSheets()));
            printInfo.setA4EquivalentTotalDeciImpressions(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentTotalDeciImpressions()));
            printInfo.setA4EquivalentColorDeciImpressions(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentColorDeciImpressions()));
            printInfo.setA4EquivalentMonoChromeDeciImpressions(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentMonoChromeDeciImpressions()));
            printInfo.setA4EquivalentSimplexDeciSheets(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentSimplexDeciSheets()));
            printInfo.setA4EquivalentDuplexDeciSheets(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentDuplexDeciSheets()));
            printInfo.setA4EquivalentBlankDeciSides(safeDeciCounterToInt(e2PrintInfo.getA4EquivalentBlankDeciSides()));

            if (e2PrintInfo.getPrintSettings() != null) {
                PrintSettings workpathPrintSettings = convertE2PrintSettingsToWorkpathPrintSettings(e2PrintInfo.getPrintSettings());
                if (workpathPrintSettings != null) {
                    printInfo.setPrintSettings(workpathPrintSettings);
                }
            }

            if (e2PrintInfo.getAgents() != null && !e2PrintInfo.getAgents().isEmpty()) {
                java.util.List<com.hp.ext.service.jobStatistics.Agent> e2Agents = e2PrintInfo.getAgents();
                PrintAgentInfo[] agentInfoArray = new PrintAgentInfo[e2Agents.size()];

                for (int i = 0; i < e2Agents.size(); i++) {
                    if (e2Agents.get(i) != null) {
                        agentInfoArray[i] = convertE2AgentToWorkpathPrintAgentInfo(e2Agents.get(i));
                    }
                }
                printInfo.setAgents(agentInfoArray);
            } else {
                printInfo.setAgents(new PrintAgentInfo[0]);
            }

            if (e2PrintInfo.getSupplementalPrintInfo() != null &&
                    e2PrintInfo.getSupplementalPrintInfo().isPrintedSheetInfo()) {
                com.hp.ext.service.jobStatistics.PrintedSheetInfo e2PrintedSheetInfo =
                        e2PrintInfo.getSupplementalPrintInfo().getPrintedSheetInfo();
                if (e2PrintedSheetInfo != null) {
                    PrintedSheetInfo workpathPrintedSheetInfo = convertE2PrintedSheetInfoToWorkpathPrintedSheetInfo(e2PrintedSheetInfo);
                    if (workpathPrintedSheetInfo != null) {
                        printInfo.setPrintedSheetInfo(workpathPrintedSheetInfo);
                    }
                }
            }

            return printInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting printInfo", e);
            return null;
        }
    }

    private static PrintSettings convertE2PrintSettingsToWorkpathPrintSettings(com.hp.ext.service.jobStatistics.PrintSettings e2PrintSettings) {
        try {

            PrintSettings printSettings = new PrintSettings();

            if (e2PrintSettings.getEconoMode() != null) {
                printSettings.setEconoMode(e2PrintSettings.getEconoMode());
            } else {
                printSettings.setEconoMode(false);
            }

            return printSettings;
        } catch (Exception e) {
            Log.e(TAG, "Error converting PrintSettings", e);
            return null;
        }
    }

    private static PrintAgentInfo convertE2AgentToWorkpathPrintAgentInfo(com.hp.ext.service.jobStatistics.Agent e2Agent) {
        try {
            if (e2Agent == null) return null;

            PrintAgentInfo printAgentInfo = new PrintAgentInfo();

            if (e2Agent.getSlotId() != null) {
                printAgentInfo.setAgentId(e2Agent.getSlotId().toString());
            }

            if (e2Agent.getSupplyDescription() != null) {
                printAgentInfo.setDescription(e2Agent.getSupplyDescription());
            }

            if (e2Agent.getSerialNumber() != null) {
                printAgentInfo.setSerialNumber(e2Agent.getSerialNumber());
            }

            // Product number conversion
            if (e2Agent.getProductNumber() != null && e2Agent.getProductNumber().getValue() != null) {
                printAgentInfo.setProductNumber(e2Agent.getProductNumber().getValue());
            }

            // Agent used conversion
            if (e2Agent.getAgentUsed() != null) {
                PrintAgentInfo.AgentUsed agentUsed = printAgentInfo.new AgentUsed();
                UnitCounter e2AgentUsed = e2Agent.getAgentUsed();

                if (e2AgentUsed.getCount() != null) {
                    int amount = safeLongToInt(e2AgentUsed.getCount());
                    agentUsed.setAmount(amount);
                }

                if (e2AgentUsed.getUnit() != null) {
                    PrintAgentInfo.AgentUnit workpathUnit = StatisticsTypeMapping.counterUnitToAgentUnit.convertEtoW(e2AgentUsed.getUnit());
                    agentUsed.setUnit(workpathUnit);
                }

                printAgentInfo.setAgentUsed(agentUsed);
            }

            // Capacity conversion
            if (e2Agent.getCapacity() != null) {
                PrintAgentInfo.Capacity capacity = printAgentInfo.new Capacity();
                UnitCounter e2Capacity = e2Agent.getCapacity();

                if (e2Capacity.getCount() != null) {
                    int maxCapacity = safeLongToInt(e2Capacity.getCount());
                    capacity.setMaxCapacity(maxCapacity);
                }

                if (e2Capacity.getUnit() != null) {
                    PrintAgentInfo.AgentUnit workpathUnit = StatisticsTypeMapping.counterUnitToAgentUnit.convertEtoW(e2Capacity.getUnit());
                    capacity.setUnit(workpathUnit);
                }

                printAgentInfo.setCapacity(capacity);
            }

            // Manufacturer conversion
            if (e2Agent.getBrand() != null || e2Agent.getManufactureDate() != null) {
                PrintAgentInfo.AgentManufacturer manufacturer = printAgentInfo.new AgentManufacturer();

                if (e2Agent.getBrand() != null) {
                    manufacturer.setName(e2Agent.getBrand());
                }

                if (e2Agent.getManufactureDate() != null) {
                    manufacturer.setDate(e2Agent.getManufactureDate());
                }

                printAgentInfo.setManufacturer(manufacturer);
            }

            // Color conversion
            if (e2Agent.getSupplyColor() != null) {
                StatisticsAttributes.Color workpathColor = StatisticsTypeMapping.supplyColorToWorkpathColor.convertEtoW(e2Agent.getSupplyColor());
                printAgentInfo.setMarkerColor(workpathColor);
            }

            // Supply type to consumable type conversion
            if (e2Agent.getSupplyType() != null) {
                PrintAgentInfo.AgentConsumableType consumableType = StatisticsTypeMapping.supplyTypeToConsumableType.convertEtoW(e2Agent.getSupplyType());
                printAgentInfo.setConsumableType(consumableType);
            }

            // Supply agent type to consumable content type conversion
            if (e2Agent.getSupplyAgentType() != null) {
                PrintAgentInfo.AgentConsumableContentType contentType = StatisticsTypeMapping.supplyAgentTypeToContentType.convertEtoW(e2Agent.getSupplyAgentType());
                printAgentInfo.setConsumableContentType(contentType);
            }

            return printAgentInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting Agent to PrintAgentInfo", e);
            return null;
        }
    }

    private static PrintedSheetInfo convertE2PrintedSheetInfoToWorkpathPrintedSheetInfo(com.hp.ext.service.jobStatistics.PrintedSheetInfo e2PrintedSheetInfo) {
        try {
            if (e2PrintedSheetInfo == null) return null;

            PrintedSheetInfo printedSheetInfo = new PrintedSheetInfo();

            printedSheetInfo.setDescription(null);

            if (e2PrintedSheetInfo.getOtherPrintedSheets() != null) {
                printedSheetInfo.setOtherPrintedSheets(safeCounterToInt(e2PrintedSheetInfo.getOtherPrintedSheets()));
            } else {
                printedSheetInfo.setOtherPrintedSheets(0);
            }

            if (e2PrintedSheetInfo.getPrintedSheetSets() != null && !e2PrintedSheetInfo.getPrintedSheetSets().isEmpty()) {
                java.util.List<com.hp.ext.service.jobStatistics.PrintedSheetSet> e2PrintedSheetSets =
                        e2PrintedSheetInfo.getPrintedSheetSets();
                PrintedSheetInfo.PrintedSheetSets[] printedSheetSetsArray =
                        new PrintedSheetInfo.PrintedSheetSets[e2PrintedSheetSets.size()];

                for (int i = 0; i < e2PrintedSheetSets.size(); i++) {
                    com.hp.ext.service.jobStatistics.PrintedSheetSet e2PrintedSheetSet = e2PrintedSheetSets.get(i);
                    if (e2PrintedSheetSet != null) {
                        PrintedSheetInfo.PrintedSheetSets printedSheetSet =
                                convertE2PrintedSheetSetToWorkpathPrintedSheetSets(e2PrintedSheetSet, printedSheetInfo);
                        if (printedSheetSet != null) {
                            printedSheetSetsArray[i] = printedSheetSet;
                        }
                    }
                }
                printedSheetInfo.setPrintedSheetSets(printedSheetSetsArray);
            } else {
                printedSheetInfo.setPrintedSheetSets(new PrintedSheetInfo.PrintedSheetSets[0]);
            }

            return printedSheetInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting PrintedSheetInfo", e);
            return null;
        }
    }

    private static PrintedSheetInfo.PrintedSheetSets convertE2PrintedSheetSetToWorkpathPrintedSheetSets(
            com.hp.ext.service.jobStatistics.PrintedSheetSet e2PrintedSheetSet, PrintedSheetInfo parentPrintedSheetInfo) {
        try {
            if (e2PrintedSheetSet == null || parentPrintedSheetInfo == null) return null;

            PrintedSheetInfo.PrintedSheetSets printedSheetSets = parentPrintedSheetInfo.new PrintedSheetSets();

            if (e2PrintedSheetSet.getBackImpressionClassification() != null) {
                StatisticsAttributes.ImpressionClassification backClassification =
                        StatisticsTypeMapping.impressionClassification.convertEtoW(e2PrintedSheetSet.getBackImpressionClassification());
                printedSheetSets.setBackImpressionClassification(backClassification);
            }

            if (e2PrintedSheetSet.getCount() != null) {
                printedSheetSets.setCount(safeCounterToInt(e2PrintedSheetSet.getCount()));
            } else {
                printedSheetSets.setCount(0);
            }


            if (e2PrintedSheetSet.getFrontImpressionClassification() != null) {
                StatisticsAttributes.ImpressionClassification frontClassification =
                        StatisticsTypeMapping.impressionClassification.convertEtoW(e2PrintedSheetSet.getFrontImpressionClassification());
                printedSheetSets.setFrontImpressionClassification(frontClassification);
            }

            if (e2PrintedSheetSet.getLogicalMediaOutputId() != null) {
                StatisticsAttributes.OutputBin logicalOutput =
                        StatisticsTypeMapping.mediaOutputId.convertEtoW(e2PrintedSheetSet.getLogicalMediaOutputId());
                printedSheetSets.setLogicalMediaOutputId(logicalOutput);
            }

            if (e2PrintedSheetSet.getMediaInputId() != null) {
                StatisticsAttributes.PaperSource mediaInput =
                        StatisticsTypeMapping.mediaInputId.convertEtoW(e2PrintedSheetSet.getMediaInputId());
                printedSheetSets.setMediaInputId(mediaInput);
            }

            if (e2PrintedSheetSet.getMediaSizeId() != null) {
                StatisticsAttributes.ScanSize mediaSize =
                        StatisticsTypeMapping.mediaSizeId.convertEtoW(e2PrintedSheetSet.getMediaSizeId());
                printedSheetSets.setMediaSizeId(mediaSize);
            }

            if (e2PrintedSheetSet.getMediaTypeId() != null) {
                StatisticsAttributes.OutputMediaType mediaType =
                        StatisticsTypeMapping.mediaTypeId.convertEtoW(e2PrintedSheetSet.getMediaTypeId());
                printedSheetSets.setMediaTypeId(mediaType);
            }

            if (e2PrintedSheetSet.getPhysicalMediaOutputId() != null) {
                StatisticsAttributes.OutputBin physicalOutput =
                        StatisticsTypeMapping.mediaOutputId.convertEtoW(e2PrintedSheetSet.getPhysicalMediaOutputId());
                printedSheetSets.setPhysicalMediaOutputId(physicalOutput);
            }

            if (e2PrintedSheetSet.getPlex() != null) {
                StatisticsAttributes.Plex plex =
                        StatisticsTypeMapping.plexMode.convertEtoW(e2PrintedSheetSet.getPlex());
                printedSheetSets.setPlex(plex);
            }

            return printedSheetSets;
        } catch (Exception e) {
            Log.e(TAG, "Error converting PrintedSheetSet to PrintedSheetSets", e);
            return null;
        }
    }

    // ScannedSheetInfo
    private static ScannedSheetInfo convertToScannedSheetInfo(com.hp.ext.service.jobStatistics.ScannedSheetInfo e2ScanInfo) {
        try {
            ScannedSheetInfo scannedSheetInfo = new ScannedSheetInfo();

            scannedSheetInfo.setOtherPrintedSheets(safeCounterToInt(e2ScanInfo.getOtherScannedSheets()));

            if (e2ScanInfo.getScannedSheetSets() != null && e2ScanInfo.getScannedSheetSets().size() > 0) {
                ScannedSheetSet[] e2ScannedSheetSets = e2ScanInfo.getScannedSheetSets().toArray(new ScannedSheetSet[0]);

                ScannedSheetInfo.ScannedSheetSets[] scannedSheetSets = new ScannedSheetInfo.ScannedSheetSets[e2ScannedSheetSets.length];

                for (int i = 0; i < e2ScannedSheetSets.length; i++) {
                    if (e2ScannedSheetSets[i] == null) continue;

                    com.hp.workpath.api.statistics.jobinfo.scan.ScannedSheetInfo.ScannedSheetSets set =
                            scannedSheetInfo.new ScannedSheetSets();

                    if (e2ScannedSheetSets[i].getCount() != null) {
                        set.setCount(safeCounterToInt(e2ScannedSheetSets[i].getCount()));
                    }

                    if (e2ScannedSheetSets[i].getMediaInputId() != null) {
                        try {
                            StatisticsAttributes.PaperSource paperSource =
                                    (StatisticsAttributes.PaperSource) StatisticsTypeMapping.mediaInputId.getConverter().convertEtoW(e2ScannedSheetSets[i].getMediaInputId());
                            set.setMediaInputId(paperSource);
                        } catch (Exception e) {
                            Log.w(TAG, "Failed to convert mediaInputId: " + e2ScannedSheetSets[i].getMediaInputId(), e);
                        }
                    }

                    if (e2ScannedSheetSets[i].getMediaSizeId() != null) {
                        try {
                            StatisticsAttributes.ScanSize scanSize =
                                    (StatisticsAttributes.ScanSize) StatisticsTypeMapping.mediaSizeId.getConverter().convertEtoW(e2ScannedSheetSets[i].getMediaSizeId());
                            set.setMediaSizeId(scanSize);
                            Log.d(TAG, "Converted mediaSizeId: " + e2ScannedSheetSets[i].getMediaSizeId() + " → " + scanSize);
                        } catch (Exception e) {
                            Log.w(TAG, "Failed to convert mediaSizeId: " + e2ScannedSheetSets[i].getMediaSizeId(), e);
                        }
                    }

                    // plex 직접 변환
                    if (e2ScannedSheetSets[i].getPlex() != null) {
                        try {
                            StatisticsAttributes.Plex workpathPlex =
                                    (StatisticsAttributes.Plex) StatisticsTypeMapping.plexMode.getConverter().convertEtoW(e2ScannedSheetSets[i].getPlex());
                            set.setPlex(workpathPlex);
                            Log.d(TAG, "Converted plex: " + e2ScannedSheetSets[i].getPlex() + " → " + workpathPlex);
                        } catch (Exception e) {
                            Log.w(TAG, "Failed to convert plex: " + e2ScannedSheetSets[i].getPlex(), e);
                        }
                    }

                    scannedSheetSets[i] = set;
                }

                scannedSheetInfo.setScannedSheetSets(scannedSheetSets);
            }

            return scannedSheetInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting scannedSheetInfo", e);
            return null;
        }
    }

    // DriverInfo
    private static DriverInfo convertToDriverInfo(com.hp.ext.service.jobStatistics.DriverInfo e2DriverInfo) {
        try {
            DriverInfo driverInfo = new DriverInfo();

            driverInfo.setApplicationName(e2DriverInfo.getApplicationName());
            driverInfo.setClientHostName(e2DriverInfo.getClientHostName());
            driverInfo.setFileName(e2DriverInfo.getFileName());
            driverInfo.setJobAcct13(e2DriverInfo.getJobAcct13());
            driverInfo.setJobAcct14(e2DriverInfo.getJobAcct14());
            driverInfo.setJobAcct15(e2DriverInfo.getJobAcct15());
            driverInfo.setJobAcct16(e2DriverInfo.getJobAcct16());

            if (e2DriverInfo.getJobId() != null) {
                driverInfo.setJobId(e2DriverInfo.getJobId().toString());
            } else {
                driverInfo.setJobId(null);
            }

            driverInfo.setUserDomain(e2DriverInfo.getUserDomain());
            driverInfo.setUserName(e2DriverInfo.getUserName());


            return driverInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting driverInfo", e);
            return null;
        }
    }

    // EmailInfo
    private static EmailInfo convertToEmailInfo(com.hp.ext.service.jobStatistics.EmailInfo e2EmailInfo) {
        try {
            EmailInfo emailInfo = new EmailInfo();

            emailInfo.setEmailSubject(e2EmailInfo.getEmailSubject());
            emailInfo.setFromAddress(e2EmailInfo.getFromAddress().toString());
            emailInfo.setHostName(e2EmailInfo.getHostName().toString());
            emailInfo.setIpAddress(e2EmailInfo.getIpAddress().toString());
            emailInfo.setPort(e2EmailInfo.getPort().getValue());

            String[] toAddresses = convertEmailAddressListToStringArray(e2EmailInfo.getToAddresses());
            emailInfo.setToAddresses(toAddresses);

            String[] ccAddresses = convertEmailAddressListToStringArray(e2EmailInfo.getCcAddresses());
            emailInfo.setCcAddresses(ccAddresses);

            String[] bccAddresses = convertEmailAddressListToStringArray(e2EmailInfo.getBccAddresses());
            emailInfo.setBccAddresses(bccAddresses);

            String[] failedRecipients = convertEmailAddressListToStringArray(e2EmailInfo.getFailedRecipientsList());
            emailInfo.setFailedRecipientsList(failedRecipients);

            EmailInfo.DigitalFaxCall[] digitalFaxCalls = convertDigitalFaxCallsList(emailInfo, e2EmailInfo.getDigitalFaxCalls());
            emailInfo.setDigitalFaxCalls(digitalFaxCalls);

            if (e2EmailInfo.getDigitalSendInfo() != null) {
                StatisticsAttributes statisticsAttributes = new StatisticsAttributes();
                StatisticsAttributes.DigitalSendInfo digitalSendInfo = convertE2DigitalSendInfoToWorkpathDigitalSendInfo(
                        e2EmailInfo.getDigitalSendInfo(), statisticsAttributes);
                if (digitalSendInfo != null) {
                    emailInfo.setDigitalSendInfo(digitalSendInfo);
                }
            }
            
            return emailInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting emailInfo", e);
            return null;
        }
    }

    private static String[] convertEmailAddressListToStringArray(java.util.List<com.hp.ext.types.common.EmailAddress> emailAddressList) {
        if (emailAddressList == null || emailAddressList.isEmpty()) return null;

        String[] stringArray = new String[emailAddressList.size()];
        for (int i = 0; i < emailAddressList.size(); i++) {
            if (emailAddressList.get(i) != null) {
                stringArray[i] = emailAddressList.get(i).toString();
            }
        }
        return stringArray;
    }

    private static EmailInfo.DigitalFaxCall[] convertDigitalFaxCallsList(EmailInfo emailInfo,
                                                                         java.util.List<com.hp.ext.service.jobStatistics.DigitalFaxCall> e2DigitalFaxCalls) {

        if (e2DigitalFaxCalls == null || e2DigitalFaxCalls.isEmpty()) return null;

        EmailInfo.DigitalFaxCall[] digitalFaxCalls = new EmailInfo.DigitalFaxCall[e2DigitalFaxCalls.size()];

        for (int i = 0; i < e2DigitalFaxCalls.size(); i++) {
            com.hp.ext.service.jobStatistics.DigitalFaxCall e2DigitalFaxCall = e2DigitalFaxCalls.get(i);
            if (e2DigitalFaxCall == null) continue;

            EmailInfo.DigitalFaxCall digitalFaxCall = emailInfo.new DigitalFaxCall();

            if (e2DigitalFaxCall.getBillingCode() != null) {
                digitalFaxCall.setBillingCode(e2DigitalFaxCall.getBillingCode().toString());
            }

            if (e2DigitalFaxCall.getFaxNumber() != null) {
                digitalFaxCall.setFaxNumber(e2DigitalFaxCall.getFaxNumber().toString());
            }

            digitalFaxCalls[i] = digitalFaxCall;
        }

        return digitalFaxCalls;
    }

    // FileInfo
    private static FileInfo convertToFileInfo(com.hp.ext.service.jobStatistics.File e2FileInfo) {
        try {
            FileInfo fileInfo = new FileInfo();

            if (e2FileInfo.getFileName() != null) {
                fileInfo.setFileName(e2FileInfo.getFileName().toString()); // To String
            }

            if (e2FileInfo.getDataSize() != null) {
                fileInfo.setDataSize(e2FileInfo.getDataSize().intValue()); // Long → int
            }

            return fileInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting FileInfo", e);
            return null;
        }
    }

    private static StatisticsJobInfo convertToJobInfo(com.hp.ext.service.jobStatistics.JobInfo e2JobInfo) {
        try {
            StatisticsJobInfo jobInfo = new StatisticsJobInfo();

            jobInfo.setApplicationName(e2JobInfo.getApplicationName());
            jobInfo.setDeviceJobName(e2JobInfo.getDeviceJobName());
            jobInfo.setParentJobId(jobIdentifierToString(e2JobInfo.getParentJobId()));

            jobInfo.setProcessedBy(StatisticsTypeMapping.processedBy.convertEtoW(e2JobInfo.getProcessedBy()).toString());
            jobInfo.setJobCategory(StatisticsTypeMapping.jobCategory.convertEtoW(e2JobInfo.getJobCategory()));
            jobInfo.setJobDataSource(StatisticsTypeMapping.jobDataSource.convertEtoW(e2JobInfo.getJobDataSource()));
            jobInfo.setJobDestinations(convertJobDestinationsList(e2JobInfo.getJobDestinations()));
            jobInfo.setJobDoneStatus(StatisticsTypeMapping.jobDoneStatus.convertEtoW(e2JobInfo.getJobDoneStatus()));
            jobInfo.setJobPaused(StatisticsTypeMapping.jobPaused.convertEtoW(e2JobInfo.getJobPaused()));

            jobInfo.setJobDoneTimestamp(convertLocalPointInTimeToTimestamp(e2JobInfo.getJobDoneTimestamp()));
            jobInfo.setJobStartedTimestamp(convertLocalPointInTimeToTimestamp(e2JobInfo.getJobStartedTimestamp()));

            return jobInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting jobInfo", e);
            return null;
        }
    }

    private static StatisticsAttributes.JobDestination[] convertJobDestinationsList(
            java.util.List<com.hp.ext.types.job.JobDestination> e2JobDestinations) {

        if (e2JobDestinations == null || e2JobDestinations.isEmpty()) {
            return new StatisticsAttributes.JobDestination[0];
        }

        List<StatisticsAttributes.JobDestination> workpathDestinations = new ArrayList<>();

        for (com.hp.ext.types.job.JobDestination e2Destination : e2JobDestinations) {
            if (e2Destination != null) {
                StatisticsAttributes.JobDestination workpathDestination =
                        StatisticsTypeMapping.jobDestination.convertEtoW(e2Destination);
                if (workpathDestination != null) {
                    workpathDestinations.add(workpathDestination);
                }
            }
        }

        return workpathDestinations.toArray(new StatisticsAttributes.JobDestination[0]);
    }

    private static Timestamp convertLocalPointInTimeToTimestamp(LocalPointInTime e2LocalPointInTime) {
        try {
            if (e2LocalPointInTime == null) return null;
            Timestamp timestamp = new Timestamp();

            if (e2LocalPointInTime.getOffset() != null) {
                timestamp.setOffset(e2LocalPointInTime.getOffset().intValue());
            }

            if (e2LocalPointInTime.getTime() != null) {
                try {
                    timestamp.setTime(e2LocalPointInTime.getTime());
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Failed to parse time string: " + e2LocalPointInTime.getTime(), e);
                }
            }

            return timestamp;
        } catch (Exception e) {
            Log.e(TAG, "Error converting Timestamp", e);
            return null;
        }
    }

    // ExtendedUserInfo
    private static ExtendedUserInfo convertToExtendedUserInfo(com.hp.ext.types.security.AuthenticatedUserInfo e2UserInfo) {
        try {

            ExtendedUserInfo extendedUserInfo = new ExtendedUserInfo();

            AuthenticatedUserInfo workpathUserInfo = convertAuthenticatedUserInfo(e2UserInfo);
            extendedUserInfo.setAuthenticatedUserInfo(workpathUserInfo);
            extendedUserInfo.setAuthenticationAgentId(e2UserInfo.getAuthenticationAgentId().getValue().toString());
            extendedUserInfo.setAuthenticationAgentName(e2UserInfo.getAuthenticationAgentName().getValue().toString());

            return extendedUserInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting ExtendedUserInfo", e);
            return null;
        }
    }

    private static FaxInInfo convertToFaxInInfo(com.hp.ext.service.jobStatistics.AFaxInInfo e2AFaxInInfo) {
        try {
            FaxInInfo faxInInfo = new FaxInInfo();

            if (e2AFaxInInfo.getStationId() != null) {
                faxInInfo.setStationId(e2AFaxInInfo.getStationId().toString());
            } else {
                faxInInfo.setStationId(null);
            }

            if (e2AFaxInInfo.getTotalImagesReceived() != null) {
                faxInInfo.setTotalImagesReceived(safeCounterToInt(e2AFaxInInfo.getTotalImagesReceived()));
            } else {
                faxInInfo.setTotalImagesReceived(0);
            }

            if (e2AFaxInInfo.getCallerId() != null) {
                FaxInInfo.Id callerId = convertCallerId(faxInInfo, e2AFaxInInfo.getCallerId());
                faxInInfo.setCallerId(callerId);
            } else {
                faxInInfo.setCallerId(null);
            }

            return faxInInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting faxInInfo", e);
            return null;
        }
    }

    private static FaxInInfo.Id convertCallerId(FaxInInfo parentFaxInInfo, com.hp.ext.types.fax.CallerId e2CallerId) {
        try {
            if (e2CallerId == null) return null;

            FaxInInfo.Id callerId = parentFaxInInfo.new Id();

            if (e2CallerId.getName() != null) {
                callerId.setName(e2CallerId.getName().toString());
            }

            if (e2CallerId.getNumber() != null) {
                callerId.setNumber(e2CallerId.getNumber().toString());
            }

            return callerId;

        } catch (Exception e) {
            Log.w(TAG, "Error converting CallerId", e);
            return null;
        }
    }

    // FaxOutInfo
    private static FaxOutInfo convertToFaxOutInfo(com.hp.ext.service.jobStatistics.AFaxOutInfo e2AFaxOutInfo) {
        try {
            FaxOutInfo faxOutInfo = new FaxOutInfo();

            java.util.List<com.hp.ext.service.jobStatistics.FaxCall> e2FaxCalls = e2AFaxOutInfo.getFaxCalls();
            FaxAttributes.Call[] faxCalls = new FaxAttributes.Call[e2FaxCalls.size()];

            FaxAttributes faxAttributes = new FaxAttributes();
            for (int i = 0; i < e2FaxCalls.size(); i++) {
                com.hp.ext.service.jobStatistics.FaxCall e2FaxCall = e2FaxCalls.get(i);
                if (e2FaxCall == null) continue;

                FaxAttributes.Call faxCall = faxAttributes.new Call();

                // Convert billingCode (BillingCode -> String)
                if (e2FaxCall.getBillingCode() != null) {
                    faxCall.setBillingCode(e2FaxCall.getBillingCode().toString());
                }

                // Convert duration (MillisecondDuration -> int)
                if (e2FaxCall.getDuration() != null) {
                    // Convert from milliseconds to seconds
                    long durationInMs = e2FaxCall.getDuration();
                    int durationInSeconds = safeLongToInt(durationInMs / 1000);
                    faxCall.setDuration(durationInSeconds);
                }

                // Convert faxNumber (FaxNumber -> String)
                if (e2FaxCall.getFaxNumber() != null) {
                    faxCall.setFaxNumber(e2FaxCall.getFaxNumber().toString());
                }

                // Convert faxResult (E2 FaxResult -> Workpath Result)
                if (e2FaxCall.getFaxResult() != null) {
                    FaxAttributes.Result result = mapE2FaxResultToWorkpathResult(e2FaxCall.getFaxResult());
                    faxCall.setFaxResult(result);
                }

                faxCalls[i] = faxCall;
            }

            faxOutInfo.setFaxCalls(faxCalls);
            
            return faxOutInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting faxOutInfo", e);
            return null;
        }
    }

    private static FaxAttributes.Result mapE2FaxResultToWorkpathResult(com.hp.ext.service.jobStatistics.FaxResult e2Result) {
        if (e2Result == null) return null;

        String resultValue = e2Result.getValue();
        if (resultValue == null) return null;

        try {
            switch (resultValue.toLowerCase()) {
                case "frsuccessful":
                    return FaxAttributes.Result.SUCCESSFUL;
                case "frpartial":
                    return FaxAttributes.Result.PARTIAL;
                case "frbusy":
                    return FaxAttributes.Result.BUSY;
                case "frfailed":
                    return FaxAttributes.Result.FAILED;
                case "frcanceled":
                    return FaxAttributes.Result.CANCELED;
                default:
                    Log.w(TAG, "Unknown E2 fax result value: " + resultValue + ", defaulting to FAILED");
                    return FaxAttributes.Result.FAILED;
            }
        } catch (Exception e) {
            Log.w(TAG, "Error mapping fax result: " + resultValue, e);
            return FaxAttributes.Result.FAILED;
        }
    }

    // IpFaxInInfo
    private static FaxInInfo convertToIpFaxInInfo(com.hp.ext.service.jobStatistics.IpFaxInInfo e2IpFaxInInfo) {
        try {
            FaxInInfo ipFaxInInfo = new FaxInInfo();

            if (e2IpFaxInInfo.getCallerId() != null) {
                FaxInInfo.Id callerId = convertCallerId(ipFaxInInfo, e2IpFaxInInfo.getCallerId());
                ipFaxInInfo.setCallerId(callerId);
            } else {
                ipFaxInInfo.setCallerId(null);
            }
            if (e2IpFaxInInfo.getStationId() != null) {
                ipFaxInInfo.setStationId(e2IpFaxInInfo.getStationId().toString());
            } else {
                ipFaxInInfo.setStationId(null);
            }
            if (e2IpFaxInInfo.getTotalImagesReceived() != null) {
                ipFaxInInfo.setTotalImagesReceived(safeCounterToInt(e2IpFaxInInfo.getTotalImagesReceived()));
            } else {
                ipFaxInInfo.setTotalImagesReceived(0);
            }

            return ipFaxInInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting ipFaxInInfo", e);
            return null;
        }
    }

    // IpFaxOutInfo
    private static IpFaxOutInfo convertToIpFaxOutInfo(com.hp.ext.service.jobStatistics.IpFaxOutInfo e2IpFaxOutInfo) {
        try {

            IpFaxOutInfo ipFaxOutInfo = new IpFaxOutInfo();

            if (e2IpFaxOutInfo.getIpFaxCalls() != null && !e2IpFaxOutInfo.getIpFaxCalls().isEmpty()) {
                java.util.List<com.hp.ext.service.jobStatistics.IpFaxCall> e2IpFaxCalls = e2IpFaxOutInfo.getIpFaxCalls();
                com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo.FaxCall[] ipFaxCalls =
                        new com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo.FaxCall[e2IpFaxCalls.size()];

                for (int i = 0; i < e2IpFaxCalls.size(); i++) {
                    com.hp.ext.service.jobStatistics.IpFaxCall e2IpFaxCall = e2IpFaxCalls.get(i);
                    if (e2IpFaxCall == null) continue;

                    com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo.FaxCall workpathFaxCall = ipFaxOutInfo.new FaxCall();

                    if (e2IpFaxCall.getFaxCall() != null) {
                        FaxAttributes faxAttributes = new FaxAttributes();
                        FaxAttributes.Call faxCall = convertE2FaxCallToWorkpathCall(e2IpFaxCall.getFaxCall(), faxAttributes);
                        if (faxCall != null) {
                            workpathFaxCall.setFaxCall(faxCall);
                        }
                    }

                    if (e2IpFaxCall.getIpFaxConfiguration() != null) {
                        FaxAttributes faxAttributes = new FaxAttributes();
                        FaxAttributes.FaxConfiguration ipFaxConfiguration = convertE2IpFaxConfigurationToWorkpathFaxConfiguration(
                                e2IpFaxCall.getIpFaxConfiguration(), faxAttributes);
                        if (ipFaxConfiguration != null) {
                            workpathFaxCall.setIpFaxConfiguration(ipFaxConfiguration);
                        }
                    }

                    ipFaxCalls[i] = workpathFaxCall;
                }

                ipFaxOutInfo.setIpFaxCalls(ipFaxCalls);
            } else {
                ipFaxOutInfo.setIpFaxCalls(new com.hp.workpath.api.statistics.jobinfo.faxinfo.IpFaxOutInfo.FaxCall[0]);
            }
            return ipFaxOutInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 IpFaxOutInfo to Workpath IpFaxOutInfo", e);
            return null;
        }
    }

    private static FaxAttributes.Call convertE2FaxCallToWorkpathCall(com.hp.ext.service.jobStatistics.FaxCall e2FaxCall, FaxAttributes faxAttributes) {
        try {
            if (e2FaxCall == null || faxAttributes == null) return null;

            FaxAttributes.Call faxCall = faxAttributes.new Call();

            if (e2FaxCall.getBillingCode() != null) {
                String billingCodeStr = e2FaxCall.getBillingCode().toString();
                if (billingCodeStr != null) {
                    faxCall.setBillingCode(billingCodeStr);
                }
            }

            if (e2FaxCall.getDuration() != null) {
                try {
                    String durationStr = e2FaxCall.getDuration().toString();
                    if (durationStr != null && !durationStr.isEmpty()) {
                        long durationInMs = Long.parseLong(durationStr);
                        int durationInSeconds = safeLongToInt(durationInMs / 1000);
                        faxCall.setDuration(durationInSeconds);
                    } else {
                        faxCall.setDuration(0);
                    }
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Invalid duration format: " + e2FaxCall.getDuration().toString());
                    faxCall.setDuration(0);
                }
            } else {
                faxCall.setDuration(0);
            }

            if (e2FaxCall.getFaxNumber() != null) {
                String faxNumberStr = e2FaxCall.getFaxNumber().toString();
                if (faxNumberStr != null) {
                    faxCall.setFaxNumber(faxNumberStr);
                }
            }

            if (e2FaxCall.getFaxResult() != null) {
                FaxAttributes.Result result = mapE2FaxResultToWorkpathResult(e2FaxCall.getFaxResult());
                if (result != null) {
                    faxCall.setFaxResult(result);
                }
            }

            return faxCall;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 FaxCall to Workpath Call", e);
            return null;
        }
    }

    private static FaxAttributes.FaxConfiguration convertE2IpFaxConfigurationToWorkpathFaxConfiguration(
            com.hp.ext.service.jobStatistics.IpFaxConfiguration e2IpFaxConfig, FaxAttributes faxAttributes) {
        try {
            if (e2IpFaxConfig == null || faxAttributes == null) return null;

            FaxAttributes.FaxConfiguration faxConfiguration = faxAttributes.new FaxConfiguration();

            // SipServer 변환 - null 안전
            if (e2IpFaxConfig.getSipServer() != null) {
                FaxAttributes.IpServer ipServer = convertE2SipServerToWorkpathIpServer(e2IpFaxConfig.getSipServer(), faxAttributes);
                faxConfiguration.setSipServer(ipServer);
            }

            // SipTransport 변환 - StatisticsTypeMapping 사용
            if (e2IpFaxConfig.getSipTransport() != null) {
                FaxAttributes.IpTransport sipTransport = StatisticsTypeMapping.sipTransport.convertEtoW(e2IpFaxConfig.getSipTransport());
                faxConfiguration.setSipTransport(sipTransport);
            }

            // T38Transport 변환 - StatisticsTypeMapping 사용
            if (e2IpFaxConfig.getT38Transport() != null) {
                FaxAttributes.T38Transport t38Transport = StatisticsTypeMapping.t38Transport.convertEtoW(e2IpFaxConfig.getT38Transport());
                faxConfiguration.setFaxNumber(t38Transport);
            }

            return faxConfiguration;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 IpFaxConfiguration to Workpath FaxConfiguration", e);
            return null;
        }
    }

    private static FaxAttributes.IpServer convertE2SipServerToWorkpathIpServer(
            com.hp.ext.types.fax.SipServer e2SipServer, FaxAttributes faxAttributes) {
        try {
            if (e2SipServer == null || faxAttributes == null) return null;

            FaxAttributes.IpServer ipServer = faxAttributes.new IpServer();

            String serverStr = e2SipServer.toString();
            if (serverStr != null && !serverStr.isEmpty()) {
                ipServer.setServer(serverStr);
            }

            return ipServer;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 SipServer to Workpath IpServer", e);
            return null;
        }
    }

    // FolderInfo
    private static FolderInfo convertToFolderInfoArray(com.hp.ext.service.jobStatistics.FolderInfo e2FolderInfo) {
        try {
            FolderInfo folderInfo = new FolderInfo();

            if (e2FolderInfo.getDigitalFaxCalls() != null && !e2FolderInfo.getDigitalFaxCalls().isEmpty()) {
                java.util.List<com.hp.ext.service.jobStatistics.DigitalFaxCall> e2DigitalFaxCalls = e2FolderInfo.getDigitalFaxCalls();
                FaxAttributes.DigitalFaxCall[] digitalFaxCalls = new FaxAttributes.DigitalFaxCall[e2DigitalFaxCalls.size()];

                FaxAttributes faxAttributes = new FaxAttributes();
                for (int i = 0; i < e2DigitalFaxCalls.size(); i++) {
                    com.hp.ext.service.jobStatistics.DigitalFaxCall e2DigitalFaxCall = e2DigitalFaxCalls.get(i);
                    if (e2DigitalFaxCall == null) continue;

                    FaxAttributes.DigitalFaxCall digitalFaxCall = convertE2DigitalFaxCallToWorkpathDigitalFaxCall(e2DigitalFaxCall, faxAttributes);
                    if (digitalFaxCall != null) {
                        digitalFaxCalls[i] = digitalFaxCall;
                    }
                }

                folderInfo.setDigitalFaxCalls(digitalFaxCalls);
            } else {
                folderInfo.setDigitalFaxCalls(new FaxAttributes.DigitalFaxCall[0]);
            }

            if (e2FolderInfo.getDigitalSendInfo() != null) {
                StatisticsAttributes statisticsAttributes = new StatisticsAttributes();
                StatisticsAttributes.DigitalSendInfo digitalSendInfo = convertE2DigitalSendInfoToWorkpathDigitalSendInfo(
                        e2FolderInfo.getDigitalSendInfo(), statisticsAttributes);
                if (digitalSendInfo != null) {
                    folderInfo.setDigitalSendInfo(digitalSendInfo);
                }
            }

            if (e2FolderInfo.getUncPath() != null) {
                folderInfo.setUncPath(e2FolderInfo.getUncPath().toString());
            } else {
                folderInfo.setUncPath(null);
            }

            if (e2FolderInfo.getUserName() != null) {
                folderInfo.setUserName(e2FolderInfo.getUserName().toString());
            } else {
                folderInfo.setUserName(null);
            }

            return folderInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 FolderInfo to Workpath FolderInfo", e);
            return null;
        }
    }

    private static FaxAttributes.DigitalFaxCall convertE2DigitalFaxCallToWorkpathDigitalFaxCall(
            com.hp.ext.service.jobStatistics.DigitalFaxCall e2DigitalFaxCall, FaxAttributes faxAttributes) {
        try {
            if (e2DigitalFaxCall == null || faxAttributes == null) return null;

            FaxAttributes.DigitalFaxCall digitalFaxCall = faxAttributes.new DigitalFaxCall();

            if (e2DigitalFaxCall.getBillingCode() != null) {
                String billingCodeStr = e2DigitalFaxCall.getBillingCode().toString();
                if (billingCodeStr != null) {
                    digitalFaxCall.setBillingCode(billingCodeStr);
                }
            }

            if (e2DigitalFaxCall.getFaxNumber() != null) {
                String faxNumberStr = e2DigitalFaxCall.getFaxNumber().toString();
                if (faxNumberStr != null) {
                    digitalFaxCall.setFaxNumber(faxNumberStr);
                }
            }

            return digitalFaxCall;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 DigitalFaxCall to Workpath DigitalFaxCall", e);
            return null;
        }
    }

    private static StatisticsAttributes.DigitalSendInfo convertE2DigitalSendInfoToWorkpathDigitalSendInfo(
            com.hp.ext.service.jobStatistics.DigitalSendInfo e2DigitalSendInfo, StatisticsAttributes statisticsAttributes) {
        try {
            if (e2DigitalSendInfo == null || statisticsAttributes == null) return null;

            StatisticsAttributes.DigitalSendInfo digitalSendInfo = statisticsAttributes.new DigitalSendInfo();

            if (e2DigitalSendInfo.getDeliveredFiles() != null) {
                StatisticsAttributes.DigitalSendInfo.DeliveredFile deliveredFile = convertE2DeliveredFilesToWorkpathDeliveredFile(
                        e2DigitalSendInfo.getDeliveredFiles(), digitalSendInfo);
                if (deliveredFile != null) {
                    digitalSendInfo.setDeliveredFiles(deliveredFile);
                }
            }

            if (e2DigitalSendInfo.getResult() != null) {
                EmailInfo.Result result = mapE2DigitalSendResultToWorkpathResult(e2DigitalSendInfo.getResult());
                if (result != null) {
                    digitalSendInfo.setResult(result);
                }
            }

            if (e2DigitalSendInfo.getTotalDataSize() != null) {
                int totalDataSize = safeLongToInt(e2DigitalSendInfo.getTotalDataSize());
                digitalSendInfo.setTotalDataSize(totalDataSize);
            }

            return digitalSendInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 DigitalSendInfo to Workpath DigitalSendInfo", e);
            return null;
        }
    }

    private static StatisticsAttributes.DigitalSendInfo.DeliveredFile convertE2DeliveredFilesToWorkpathDeliveredFile(
            com.hp.ext.service.jobStatistics.DeliveredFiles e2DeliveredFiles,
            StatisticsAttributes.DigitalSendInfo digitalSendInfo) {
        try {
            if (e2DeliveredFiles == null || digitalSendInfo == null) return null;

            StatisticsAttributes.DigitalSendInfo.DeliveredFile deliveredFile = digitalSendInfo.new DeliveredFile();

            // files 변환 (List<File> -> FileInfo[])
            if (e2DeliveredFiles.getFiles() != null && !e2DeliveredFiles.getFiles().isEmpty()) {
                java.util.List<com.hp.ext.service.jobStatistics.File> e2Files = e2DeliveredFiles.getFiles();
                FileInfo[] fileInfos = new FileInfo[e2Files.size()];

                for (int i = 0; i < e2Files.size(); i++) {
                    com.hp.ext.service.jobStatistics.File e2File = e2Files.get(i);
                    if (e2File != null) {
                        FileInfo fileInfo = convertE2FileToWorkpathFileInfo(e2File);
                        if (fileInfo != null) {
                            fileInfos[i] = fileInfo;
                        }
                    }
                }
                deliveredFile.setFiles(fileInfos);
            } else {
                deliveredFile.setFiles(new FileInfo[0]);
            }

            if (e2DeliveredFiles.getMetadataFile() != null) {
                FileInfo metadataFileInfo = convertE2FileToWorkpathFileInfo(e2DeliveredFiles.getMetadataFile());
                if (metadataFileInfo != null) {
                    deliveredFile.setMetdataFile(metadataFileInfo); // Workpath API의 오타 유지
                }
            }

            return deliveredFile;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 DeliveredFiles to Workpath DeliveredFile", e);
            return null;
        }
    }

    private static FileInfo convertE2FileToWorkpathFileInfo(com.hp.ext.service.jobStatistics.File e2File) {
        try {
            if (e2File == null) return null;

            FileInfo fileInfo = new FileInfo();

            if (e2File.getDataSize() != null) {
                int dataSize = safeLongToInt(e2File.getDataSize());
                fileInfo.setDataSize(dataSize);
            } else {
                fileInfo.setDataSize(0);
            }


            if (e2File.getFileName() != null) {
                String fileName = e2File.getFileName().toString();
                if (fileName != null && !fileName.isEmpty()) {
                    fileInfo.setFileName(fileName);
                }
            }

            return fileInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 File to Workpath FileInfo", e);
            return null;
        }
    }

    private static EmailInfo.Result mapE2DigitalSendResultToWorkpathResult(
            com.hp.ext.service.jobStatistics.DigitalSendResult e2Result) {
        if (e2Result == null) return null;

        String resultValue = e2Result.getValue();
        if (resultValue == null) return null;

        try {
            switch (resultValue.toLowerCase()) {
                case "dsrsuccessful":
                    return EmailInfo.Result.SUCCESSFUL;
                case "dsrpartial":
                    return EmailInfo.Result.PARTIAL;
                case "dsrfailed":
                    return EmailInfo.Result.FAILED;
                case "dsrcanceled":
                    return EmailInfo.Result.CANCELED;
                default:
                    Log.w(TAG, "Unknown E2 digital send result value: " + resultValue + ", defaulting to PARTIAL");
                    return EmailInfo.Result.PARTIAL;
            }
        } catch (Exception e) {
            Log.w(TAG, "Error mapping digital send result: " + resultValue, e);
            return EmailInfo.Result.PARTIAL;
        }
    }

    // FtpInfo
    private static FtpInfo convertToFtpInfoArray(com.hp.ext.service.jobStatistics.FtpInfo e2FtpInfo) {
        try {

            FtpInfo ftpInfo = new FtpInfo();

            if (e2FtpInfo.getDigitalSendInfo() != null) {
                StatisticsAttributes statisticsAttributes = new StatisticsAttributes();
                StatisticsAttributes.DigitalSendInfo digitalSendInfo = convertE2DigitalSendInfoToWorkpathDigitalSendInfo(
                        e2FtpInfo.getDigitalSendInfo(), statisticsAttributes);
                if (digitalSendInfo != null) {
                    ftpInfo.setDigitalSendInfo(digitalSendInfo);
                }
            }

            if (e2FtpInfo.getDirectoryPath() != null) {
                ftpInfo.setDirectoryPath(e2FtpInfo.getDirectoryPath().toString());
            } else {
                ftpInfo.setDirectoryPath(null);
            }

            if (e2FtpInfo.getHostName() != null) {
                ftpInfo.setHostName(e2FtpInfo.getHostName().toString());
            } else {
                ftpInfo.setHostName(null);
            }

            if (e2FtpInfo.getIpAddress() != null) {
                ftpInfo.setIpAddress(e2FtpInfo.getIpAddress().toString());
            } else {
                ftpInfo.setIpAddress(null);
            }

            if (e2FtpInfo.getPort() != null) {
                int port = Integer.parseInt(e2FtpInfo.getPort().toString());
                ftpInfo.setPort(port);
            } else {
                ftpInfo.setPort(0);
            }

            if (e2FtpInfo.getUserName() != null) {
                ftpInfo.setUserName(e2FtpInfo.getUserName().toString());
            } else {
                ftpInfo.setUserName(null);
            }
            
            return ftpInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 FtpInfo to Workpath FtpInfo", e);
            return null;
        }
    }

    // HttpInfo
    private static HttpInfo convertToHttpInfoArray(com.hp.ext.service.jobStatistics.HttpInfo e2HttpInfo) {
        try {

            HttpInfo httpInfo = new HttpInfo();

            if (e2HttpInfo.getDigitalSendInfo() != null) {
                StatisticsAttributes statisticsAttributes = new StatisticsAttributes();
                StatisticsAttributes.DigitalSendInfo digitalSendInfo = convertE2DigitalSendInfoToWorkpathDigitalSendInfo(
                        e2HttpInfo.getDigitalSendInfo(), statisticsAttributes);
                if (digitalSendInfo != null) {
                    httpInfo.setDigitalSendInfo(digitalSendInfo);
                }
            }

            if (e2HttpInfo.getUri() != null && !e2HttpInfo.getUri().isEmpty()) {
                httpInfo.setUri(e2HttpInfo.getUri());
            } else {
                httpInfo.setUri(null);
            }

            if (e2HttpInfo.getUserName() != null) {
                httpInfo.setUserName(e2HttpInfo.getUserName().toString());
            } else {
                httpInfo.setUserName(null);
            }

            return httpInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting E2 HttpInfo to Workpath HttpInfo", e);
            return null;
        }
    }

    public static int safeLongToInt(Long value) {
        if (value == null) {
            return 0;
        }
        if (value > Integer.MAX_VALUE) {
            Log.w(TAG, "Long value exceeds Integer.MAX_VALUE, returning Integer.MAX_VALUE");
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            Log.e(TAG, "Long value below Integer.MIN_VALUE, returning Integer.MIN_VALUE");
            return Integer.MIN_VALUE;
        }
        return value.intValue();
    }

    public static int safeCounterToInt(Counter value) {
        if (value == null || value.getValue() == null) return 0;
        return safeLongToInt(value.getValue());
    }

    private static int safeDeciCounterToInt(FixedPointCounter counter) {
        return FixedPointCounterConverter.getFixedPointCounterValue(counter);
    }

    // AuthenticatedUserInfo
    private static AuthenticatedUserInfo convertAuthenticatedUserInfo(com.hp.ext.types.security.AuthenticatedUserInfo e2UserInfo) {
        try {

            AuthenticatedUserInfo workpathUserInfo = new AuthenticatedUserInfo();

            workpathUserInfo.setDisplayName(e2UserInfo.getDisplayName());
            workpathUserInfo.setExchangeMailboxUri(e2UserInfo.getExchangeMailboxUri());
            workpathUserInfo.setHomeFolderPath(e2UserInfo.getHomeFolderPath());
            workpathUserInfo.setLdapBindUser(e2UserInfo.getLdapBindUser());
            workpathUserInfo.setsAMAccountName(e2UserInfo.getSAMAccountName());
            workpathUserInfo.setSidString(e2UserInfo.getSidString());
            workpathUserInfo.setUserDomain(e2UserInfo.getUserDomain());
            workpathUserInfo.setUserName(e2UserInfo.getUserName());
            workpathUserInfo.setUserPrincipalName(e2UserInfo.getUserPrincipalName());

            if (e2UserInfo.getEmailAddress() != null) {
                workpathUserInfo.setEmailAddress(e2UserInfo.getEmailAddress().getValue());
            }
            if (e2UserInfo.getFullyQualifiedUserName() != null) {
                workpathUserInfo.setFullyQualifiedUserName(e2UserInfo.getFullyQualifiedUserName().getValue());
            }

            // AuthenticationKind → AuthenticationType
            if (e2UserInfo.getAuthenticationKind() != null) {
                ExtendedUserInfo.AuthenticationType authType = StatisticsTypeMapping.authenticationKind.convertEtoW(e2UserInfo.getAuthenticationKind());
                workpathUserInfo.setAuthenticationType(authType);
            }

            // customAttributes → keyValuePairs 변환 (필요시)
            // if (e2UserInfo.getCustomAttributes() != null) {
            //     ExtendedUserInfo.KeyValue[] keyValuePairs = convertCustomAttributes(e2UserInfo.getCustomAttributes());
            //     workpathUserInfo.setKeyValuePairs(keyValuePairs);
            // }

            return workpathUserInfo;
        } catch (Exception e) {
            Log.e(TAG, "Error converting AuthenticatedUserInfo", e);
            return null;
        }
    }

    // JobIdentifier → String
    private static String jobIdentifierToString(com.hp.ext.types.job.JobIdentifier jobIdentifier) {
        if (jobIdentifier == null || jobIdentifier.getValue() == null) {
            return null;
        }
        return jobIdentifier.getValue().toString();
    }
}
