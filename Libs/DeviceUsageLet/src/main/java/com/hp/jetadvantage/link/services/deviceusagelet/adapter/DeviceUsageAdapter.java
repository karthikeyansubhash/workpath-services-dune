package com.hp.jetadvantage.link.services.deviceusagelet.adapter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.hp.ext.service.deviceUsage.LifetimeCounters;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.ext.types.usage.Counter;
import com.hp.ext.types.usage.FixedPointCounter;
import com.hp.workpath.internal.utils.adapter.FixedPointCounterConverter;
import com.hp.ext.types.usage.FixedPointImpressionCounter;
import com.hp.ext.types.usage.ImpressionCounter;
import com.hp.ext.types.usage.MediaSizeImpressionSet;
import com.hp.ext.types.usage.MediaSizeSheetSet;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.workpath.api.deviceusage.DeviceUsageInfo;
import com.hp.workpath.api.deviceusage.DeviceUsagelet;
import com.hp.workpath.api.deviceusage.Plex;
import com.hp.workpath.api.deviceusage.SubUnitInfo;
import com.hp.workpath.api.deviceusage.printer.PrinterInfo;
import com.hp.workpath.api.deviceusage.scanner.ScannerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class DeviceUsageAdapter {
    private static String TAG = DeviceUsagelet.TAG + "/Adap";

    public static String convertToWorkpathDeviceUsageAdapter(LifetimeCounters lifetimeCounters) {
        try {
            if (lifetimeCounters == null) {
                Log.d(TAG, "convertToWorkpathDeviceUsageAdapter_1: lifetimeCounters is null");
                return null;
            }

            /* Print Usage */
            PrinterInfo printerInfo = new PrinterInfo();
            printerInfo.setSheets(getPrintSheets(lifetimeCounters));
            printerInfo.setEngineCycles(getEngineCycles(lifetimeCounters));
            printerInfo.setByPrintPlex(getPrintPlexArray(lifetimeCounters));
            printerInfo.setByJobCategory(getByJobCategoryArray(lifetimeCounters));
            printerInfo.setByJobCategoryAndMediaSize(getByJobCategoryAndMediaSizeArray(lifetimeCounters));
            printerInfo.setByColorMode(getByColorModeArray(lifetimeCounters));
            printerInfo.setA4EquivalentByJobCategory(getA4EquivalentByJobCategoryArray(lifetimeCounters));
            printerInfo.setPrintByMediaSize(getPrintByMediaSize(lifetimeCounters, MediaSizeImpressionSet::getPrintOtherImpressions));
            printerInfo.setCopyByMediaSize(getCopyByMediaSize(lifetimeCounters, MediaSizeImpressionSet::getCopyImpressions));
            printerInfo.setFaxByMediaSize(getFaxByMediaSize(lifetimeCounters, MediaSizeImpressionSet::getFaxInImpressions));
            printerInfo.setPlexByMediaSize(getPlexByMediaSizeArray(lifetimeCounters));

            /* Scan Usage */
            ScannerInfo scannerInfo = new ScannerInfo();
            scannerInfo.setSheets(getTotalSheets(lifetimeCounters));
            scannerInfo.setByScanPlex(getByScanPlexArray(lifetimeCounters));
            scannerInfo.setByJobCategory(getByJobCategoryScannerArray(lifetimeCounters));
            scannerInfo.setByMediaSize(getByMediaSizeArray(lifetimeCounters));

            DeviceUsageInfo deviceInfo = new DeviceUsageInfo();
            deviceInfo.setPrinter(printerInfo);
            deviceInfo.setScanner(scannerInfo);

            JSONObject deviceInfoJson = new JSONObject(JsonParser.getInstance().toJson(deviceInfo));
            JSONObject rootJson = new JSONObject();
            rootJson.put("subunits", deviceInfoJson);
            String result = rootJson.toString();


            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getPrintSheets(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getPrintUsage() != null &&
                lifetimeCounters.getPrintUsage().getPrintSheets() != null &&
                lifetimeCounters.getPrintUsage().getPrintSheets().getTotal() != null) {
            return safeLongToInt(lifetimeCounters.getPrintUsage().getPrintSheets().getTotal());
        } else {
            return 0;
        }
    }

    private static int getEngineCycles(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getJobUsage() != null &&
                lifetimeCounters.getJobUsage().getLastJobSequenceId() != null) {
            return safeLongToInt(lifetimeCounters.getJobUsage().getLastJobSequenceId());
        } else {
            return 0;
        }
    }

    private static Plex createPlexObject(String plexType, int sheets) {
        Plex plex = new Plex();
        plex.setPlex(plexType);
        plex.setSheets(sheets);
        return plex;
    }

    private static Plex[] getPrintPlexArray(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getPrintSheets() == null) {
            return new Plex[0];
        }

        List<Plex> result = new ArrayList<>();
        if (lifetimeCounters.getPrintUsage().getPrintSheets().getSimplex() != null) {
            result.add(createPlexObject(DeviceUsagelet.PlexType.SIMPLEX, safeLongToInt(lifetimeCounters.getPrintUsage().getPrintSheets().getSimplex())));
        }
        if (lifetimeCounters.getPrintUsage().getPrintSheets().getDuplex() != null) {
            result.add(createPlexObject(DeviceUsagelet.PlexType.DUPLEX, safeLongToInt(lifetimeCounters.getPrintUsage().getPrintSheets().getDuplex())));
        }

        return result.toArray(new Plex[0]);
    }

    private static PrinterInfo.ByJobCategory createByJobCategory(PrinterInfo info, String jobCategory, int a4EquivalentDeciImpressions) {
        PrinterInfo.ByJobCategory byJobCategory = info.new ByJobCategory();
        byJobCategory.setJobCategory(jobCategory);
        byJobCategory.setA4EquivalentDeciImpressions(a4EquivalentDeciImpressions);
        return byJobCategory;
    }

    public static int getCounterValue(FixedPointCounter counter) {

        return FixedPointCounterConverter.getFixedPointCounterValue(counter);
    }

    public static int safeLongToInt(Counter value) {
        if (value.getValue() > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value.getValue() < Integer.MIN_VALUE) {
            Log.e(TAG, "Long value below Integer.MIN_VALUE, returning Integer.MIN_VALUE");
            return Integer.MIN_VALUE;
        }
        return value.getValue().intValue();
    }

    private static PrinterInfo.ByJobCategory[] getByJobCategoryArray(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getPrintUsage() == null) {
            return new PrinterInfo.ByJobCategory[0];
        }
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.ByJobCategory> result = new ArrayList<>();

        if (lifetimeCounters.getPrintUsage().getA4EquivalentPrintOtherImpressions() != null)
            result.add(createByJobCategory(printerInfo, DeviceUsagelet.JobCategory.PRINT, getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentPrintImpressions().getTotal())));

        if (lifetimeCounters.getPrintUsage().getA4EquivalentCopyImpressions() != null)
            result.add(createByJobCategory(printerInfo, DeviceUsagelet.JobCategory.COPY, getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentCopyImpressions().getTotal())));

        if (lifetimeCounters.getPrintUsage().getA4EquivalentFaxInImpressions() != null)
            result.add(createByJobCategory(printerInfo, DeviceUsagelet.JobCategory.FAX, getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentFaxInImpressions().getTotal())));

        return result.toArray(new PrinterInfo.ByJobCategory[0]);
    }

    private static PrinterInfo.ByJobCategoryAndMediaSize createByJobCategoryAndMediaSize(PrinterInfo info, String jobCategory, String mediaSize, long impressions) {
        PrinterInfo.ByJobCategoryAndMediaSize byJobCategoryAndMediaSize = info.new ByJobCategoryAndMediaSize();
        byJobCategoryAndMediaSize.setJobCategory(jobCategory);
        byJobCategoryAndMediaSize.setMediaSize(mediaSize);
        byJobCategoryAndMediaSize.setImpressions((int) impressions);
        return byJobCategoryAndMediaSize;
    }

    private static PrinterInfo.ByJobCategoryAndMediaSize[] getByJobCategoryAndMediaSizeArray(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getImpressionsByMediaSize() == null) {
            return new PrinterInfo.ByJobCategoryAndMediaSize[0];
        }
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.ByJobCategoryAndMediaSize> result = new ArrayList<>();
        for (MediaSizeImpressionSet impressionSet : lifetimeCounters.getPrintUsage().getImpressionsByMediaSize()) {
            if (impressionSet.getPrintOtherImpressions() != null) {
                result.add(createByJobCategoryAndMediaSize(printerInfo, DeviceUsagelet.JobCategory.PRINT,
                        getMediaSizeValue(impressionSet.getMediaSize()),
                        impressionSet.getPrintOtherImpressions().getTotal().getValue()));
            }
            if (impressionSet.getCopyImpressions() != null) {
                result.add(createByJobCategoryAndMediaSize(printerInfo, DeviceUsagelet.JobCategory.COPY,
                        getMediaSizeValue(impressionSet.getMediaSize()),
                        impressionSet.getCopyImpressions().getTotal().getValue()));
            }
            if (impressionSet.getFaxInImpressions() != null) {
                result.add(createByJobCategoryAndMediaSize(printerInfo, DeviceUsagelet.JobCategory.FAX,
                        getMediaSizeValue(impressionSet.getMediaSize()),
                        impressionSet.getFaxInImpressions().getTotal().getValue()));
            }
        }
        return result.toArray(new PrinterInfo.ByJobCategoryAndMediaSize[0]);
    }

    private static PrinterInfo.ByColorMode createByColorMode(PrinterInfo info, String jobCategory, String colorMode, long impressions) {
        PrinterInfo.ByColorMode byColorMode = info.new ByColorMode();
        byColorMode.setJobCategory(jobCategory);
        byColorMode.setColorMode(colorMode);
        byColorMode.setImpressions((int) impressions);
        return byColorMode;
    }

    private static PrinterInfo.ByColorMode[] getByColorModeArray(LifetimeCounters lifetimeCounters) {
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.ByColorMode> result = new ArrayList<>();

        if (lifetimeCounters.getPrintUsage() == null) {
            return new PrinterInfo.ByColorMode[0];
        }
        if (lifetimeCounters.getPrintUsage().getPrintImpressions() != null) {
            if (lifetimeCounters.getPrintUsage().getPrintImpressions().getColor() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.PRINT, DeviceUsagelet.ColorMode.COLOR, lifetimeCounters.getPrintUsage().getPrintImpressions().getColor().getValue()));
            if (lifetimeCounters.getPrintUsage().getPrintImpressions().getMonochrome() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.PRINT, DeviceUsagelet.ColorMode.MONO, lifetimeCounters.getPrintUsage().getPrintImpressions().getMonochrome().getValue()));
        }
        if (lifetimeCounters.getPrintUsage().getCopyImpressions() != null) {
            if (lifetimeCounters.getPrintUsage().getCopyImpressions().getColor() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.COPY, DeviceUsagelet.ColorMode.COLOR, lifetimeCounters.getPrintUsage().getCopyImpressions().getColor().getValue()));
            if (lifetimeCounters.getPrintUsage().getCopyImpressions().getMonochrome() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.COPY, DeviceUsagelet.ColorMode.MONO, lifetimeCounters.getPrintUsage().getCopyImpressions().getMonochrome().getValue()));
        }
        if (lifetimeCounters.getPrintUsage().getFaxInImpressions() != null) {
            if (lifetimeCounters.getPrintUsage().getFaxInImpressions().getColor() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.FAX, DeviceUsagelet.ColorMode.COLOR, lifetimeCounters.getPrintUsage().getFaxInImpressions().getColor().getValue()));
            if (lifetimeCounters.getPrintUsage().getFaxInImpressions().getMonochrome() != null)
                result.add(createByColorMode(printerInfo, DeviceUsagelet.JobCategory.FAX, DeviceUsagelet.ColorMode.MONO, lifetimeCounters.getPrintUsage().getFaxInImpressions().getMonochrome().getValue()));
        }

        return result.toArray(new PrinterInfo.ByColorMode[0]);
    }

    private static PrinterInfo.A4EquivalentByJobCategory createA4EquivalentJobCategory(PrinterInfo info, String jobCategory, FixedPointImpressionCounter impressions) {
        PrinterInfo.A4EquivalentByJobCategory a4JobCategory = info.new A4EquivalentByJobCategory();
        a4JobCategory.setJobCategory(jobCategory);

        if (impressions.getColor() != null)
            {   int color = getCounterValue(impressions.getColor());
                a4JobCategory.setColorDeciImpressions(color);
            }
        if (impressions.getMonochrome() != null)
            {   int mono = getCounterValue(impressions.getMonochrome());
                a4JobCategory.setMonoDeciImpressions(mono);
            }
        if (impressions.getTotal() != null)
           {   int total = getCounterValue(impressions.getTotal());
               a4JobCategory.setTotalDeciImpressions(total);
           }

        return a4JobCategory;
    }


    private static PrinterInfo.A4EquivalentByJobCategory[] getA4EquivalentByJobCategoryArray(LifetimeCounters lifetimeCounters) {
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.A4EquivalentByJobCategory> result = new ArrayList<>();

        if (lifetimeCounters.getPrintUsage() == null) {
            return new PrinterInfo.A4EquivalentByJobCategory[0];
        }

        if (lifetimeCounters.getPrintUsage().getA4EquivalentPrintImpressions() != null)
            result.add(createA4EquivalentJobCategory(printerInfo, DeviceUsagelet.JobCategory.PRINT, lifetimeCounters.getPrintUsage().getA4EquivalentPrintImpressions()));

        if (lifetimeCounters.getPrintUsage().getA4EquivalentCopyImpressions() != null)
            result.add(createA4EquivalentJobCategory(printerInfo, DeviceUsagelet.JobCategory.COPY, lifetimeCounters.getPrintUsage().getA4EquivalentCopyImpressions()));

        if (lifetimeCounters.getPrintUsage().getA4EquivalentFaxInImpressions() != null)
            result.add(createA4EquivalentJobCategory(printerInfo, DeviceUsagelet.JobCategory.FAX, lifetimeCounters.getPrintUsage().getA4EquivalentFaxInImpressions()));

        return result.toArray(new PrinterInfo.A4EquivalentByJobCategory[0]);
    }

    private static PrinterInfo.PrintByMediaSize[] getPrintByMediaSize(LifetimeCounters lifetimeCounters, Function<MediaSizeImpressionSet, ImpressionCounter> getImpressions) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getImpressionsByMediaSize() == null) {
            return new PrinterInfo.PrintByMediaSize[0];
        }
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.PrintByMediaSize> result = new ArrayList<>();

        for (MediaSizeImpressionSet impressionSet : lifetimeCounters.getPrintUsage().getImpressionsByMediaSize()) {
            ImpressionCounter impressions = getImpressions.apply(impressionSet);
            if (impressions != null) {
                PrinterInfo.PrintByMediaSize printByMediaSize = printerInfo.new PrintByMediaSize();
                if (impressionSet.getMediaSize() != null)
                    printByMediaSize.setMediaSize(getMediaSizeValue(impressionSet.getMediaSize()));
                if (impressions.getColor() != null)
                    printByMediaSize.setColorImpressions(safeLongToInt(impressions.getColor()));
                if (impressions.getMonochrome() != null)
                    printByMediaSize.setMonoImpressions(safeLongToInt(impressions.getMonochrome()));
                if (impressions.getTotal() != null)
                    printByMediaSize.setTotalImpressions(safeLongToInt(impressions.getTotal()));
                result.add(printByMediaSize);
            }
        }
        return result.toArray(new PrinterInfo.PrintByMediaSize[0]);
    }

    private static PrinterInfo.CopyByMediaSize[] getCopyByMediaSize(LifetimeCounters lifetimeCounters, Function<MediaSizeImpressionSet, ImpressionCounter> getImpressions) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getImpressionsByMediaSize() == null) {
            return new PrinterInfo.CopyByMediaSize[0];
        }
        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.CopyByMediaSize> result = new ArrayList<>();

        for (MediaSizeImpressionSet impressionSet : lifetimeCounters.getPrintUsage().getImpressionsByMediaSize()) {
            ImpressionCounter impressions = getImpressions.apply(impressionSet);
            if (impressions != null) {
                PrinterInfo.CopyByMediaSize copyByMediaSize = printerInfo.new CopyByMediaSize();
                if (impressionSet.getMediaSize() != null)
                    copyByMediaSize.setMediaSize(getMediaSizeValue(impressionSet.getMediaSize()));
                if (impressions.getColor() != null)
                    copyByMediaSize.setColorImpressions(safeLongToInt(impressions.getColor()));
                if (impressions.getMonochrome() != null)
                    copyByMediaSize.setMonoImpressions(safeLongToInt(impressions.getMonochrome()));
                if (impressions.getTotal() != null)
                    copyByMediaSize.setTotalImpressions(safeLongToInt(impressions.getTotal()));
                result.add(copyByMediaSize);
            }
        }
        return result.toArray(new PrinterInfo.CopyByMediaSize[0]);
    }

    private static PrinterInfo.FaxByMediaSize[] getFaxByMediaSize(LifetimeCounters lifetimeCounters, Function<MediaSizeImpressionSet, ImpressionCounter> getImpressions) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getImpressionsByMediaSize() == null) {
            return new PrinterInfo.FaxByMediaSize[0];
        }

        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.FaxByMediaSize> result = new ArrayList<>();

        for (MediaSizeImpressionSet impressionSet : lifetimeCounters.getPrintUsage().getImpressionsByMediaSize()) {
            ImpressionCounter impressions = getImpressions.apply(impressionSet);
            if (impressions != null) {
                PrinterInfo.FaxByMediaSize faxByMediaSize = printerInfo.new FaxByMediaSize();
                if (impressionSet.getMediaSize() != null)
                    faxByMediaSize.setMediaSize(getMediaSizeValue(impressionSet.getMediaSize()));
                if (impressions.getColor() != null)
                    faxByMediaSize.setColorImpressions(safeLongToInt(impressions.getColor()));
                if (impressions.getMonochrome() != null)
                    faxByMediaSize.setMonoImpressions(safeLongToInt(impressions.getMonochrome()));
                if (impressions.getTotal() != null)
                    faxByMediaSize.setTotalImpressions(safeLongToInt(impressions.getTotal()));
                result.add(faxByMediaSize);
            }
        }
        return result.toArray(new PrinterInfo.FaxByMediaSize[0]);
    }

    private static PrinterInfo.PlexByMediaSize[] getPlexByMediaSizeArray(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getPrintUsage() == null || lifetimeCounters.getPrintUsage().getSheetsByMediaSize() == null) {
            return new PrinterInfo.PlexByMediaSize[0];
        }

        PrinterInfo printerInfo = new PrinterInfo();
        List<PrinterInfo.PlexByMediaSize> result = new ArrayList<>();

        for (MediaSizeSheetSet mediaSizeSheet : lifetimeCounters.getPrintUsage().getSheetsByMediaSize()) {
            PrinterInfo.PlexByMediaSize plexByMediaSize = printerInfo.new PlexByMediaSize();
            if (mediaSizeSheet.getMediaSize() != null)
                plexByMediaSize.setMediaSize(getMediaSizeValue(mediaSizeSheet.getMediaSize()));
            if (mediaSizeSheet.getSheets() != null)
                plexByMediaSize.setSimplexSheets(safeLongToInt(mediaSizeSheet.getSheets().getSimplex()));
            if (mediaSizeSheet.getSheets() != null)
                plexByMediaSize.setDuplexSheets(safeLongToInt(mediaSizeSheet.getSheets().getDuplex()));
            if (mediaSizeSheet.getSheets() != null)
                plexByMediaSize.setTotalSheets(safeLongToInt(mediaSizeSheet.getSheets().getTotal()));
            result.add(plexByMediaSize);
        }
        return result.toArray(new PrinterInfo.PlexByMediaSize[0]);
    }

    private static int getTotalSheets(LifetimeCounters lifetimeCounters) {
        if (lifetimeCounters.getScanUsage() == null || lifetimeCounters.getScanUsage().getTotalImages() == null) {
            return 0;
        } else {
            return safeLongToInt(lifetimeCounters.getScanUsage().getTotalImages());
        }
    }

    private static Plex createScanPlexObject(String scanPlex, int sheets) {
        Plex plex = new Plex();
        plex.setPlex(scanPlex);
        plex.setSheets(sheets);
        return plex;
    }

    private static Plex[] getByScanPlexArray(LifetimeCounters lifetimeCounters) throws JSONException {
        if (lifetimeCounters.getScanUsage() == null) {
            return new Plex[0];
        }

        List<Plex> plexList = new ArrayList<>();

        if (lifetimeCounters.getScanUsage().getAdfSimplexImages() != null)
            plexList.add(createScanPlexObject(DeviceUsagelet.ScanPlex.ADF_SIMPLEX, safeLongToInt(lifetimeCounters.getScanUsage().getAdfSimplexImages())));
        if (lifetimeCounters.getScanUsage().getAdfDuplexImages() != null)
            plexList.add(createScanPlexObject(DeviceUsagelet.ScanPlex.ADF_DUPLEX, safeLongToInt(lifetimeCounters.getScanUsage().getAdfDuplexImages())));
        if (lifetimeCounters.getScanUsage().getFlatbedImages() != null)
            plexList.add(createScanPlexObject(DeviceUsagelet.ScanPlex.FLATBED, safeLongToInt(lifetimeCounters.getScanUsage().getFlatbedImages())));

        return plexList.toArray(new Plex[0]);
    }

    private static ScannerInfo.ByJobCategory createJobCategoryScannerObject(ScannerInfo info, String jobCategory, int images) {
        ScannerInfo.ByJobCategory byJobCategory = info.new ByJobCategory();
        byJobCategory.setJobCategory(jobCategory);
        byJobCategory.setImages(images);
        return byJobCategory;
    }

    private static ScannerInfo.ByJobCategory[] getByJobCategoryScannerArray(LifetimeCounters lifetimeCounters) throws JSONException {
        if (lifetimeCounters.getScanUsage() == null) {
            return new ScannerInfo.ByJobCategory[0];
        }

        ScannerInfo scannerInfo = new ScannerInfo();
        List<ScannerInfo.ByJobCategory> result = new ArrayList<>();

        if (lifetimeCounters.getScanUsage().getCopyImages() != null)
            result.add(createJobCategoryScannerObject(scannerInfo, DeviceUsagelet.JobCategory.COPY, safeLongToInt(lifetimeCounters.getScanUsage().getCopyImages())));
        if (lifetimeCounters.getScanUsage().getSendImages() != null)
            result.add(createJobCategoryScannerObject(scannerInfo, DeviceUsagelet.JobCategory.SEND, safeLongToInt(lifetimeCounters.getScanUsage().getSendImages())));
        if (lifetimeCounters.getScanUsage().getFaxImages() != null)
            result.add(createJobCategoryScannerObject(scannerInfo, DeviceUsagelet.JobCategory.FAX, safeLongToInt(lifetimeCounters.getScanUsage().getFaxImages())));

        return result.toArray(new ScannerInfo.ByJobCategory[0]);
    }
    private static ScannerInfo.ByMediaSize createMediaSizeObject(ScannerInfo info, MediaSizeId size, int images) throws JSONException {
        ScannerInfo.ByMediaSize mediaSize = info.new ByMediaSize();

        String mapped;
        if (size == null) {
            mapped = SubUnitInfo.MediaSize.OTHER.getValue();
        } else {
            mapped = getMediaSizeValue(size);
        }

        mediaSize.setMediaSize(mapped);
        mediaSize.setImages(images);
        return mediaSize;
    }

    private static ScannerInfo.ByMediaSize[] getByMediaSizeArray(LifetimeCounters lifetimeCounters) throws JSONException {
        if (lifetimeCounters == null) {
            return new ScannerInfo.ByMediaSize[0];
        }
        if (lifetimeCounters.getScanUsage() == null || lifetimeCounters.getScanUsage().getImagesByMediaSize() == null) {
            return new ScannerInfo.ByMediaSize[0];
        }

        ScannerInfo scannerInfo = new ScannerInfo();
        List<ScannerInfo.ByMediaSize> result = new ArrayList<>();

        int sourceSize = lifetimeCounters.getScanUsage().getImagesByMediaSize().size();

        for (int i = 0; i < sourceSize; i++) {
            MediaSizeId rawMediaSize = lifetimeCounters.getScanUsage().getImagesByMediaSize().get(i).getMediaSize();

            int images = 0;
            if (lifetimeCounters.getScanUsage().getImagesByMediaSize().get(i).getImages() != null) {
                images = safeLongToInt(lifetimeCounters.getScanUsage().getImagesByMediaSize().get(i).getImages());
            }

            ScannerInfo.ByMediaSize entry = createMediaSizeObject(scannerInfo, rawMediaSize, images);
            result.add(entry);
        }

        return result.toArray(new ScannerInfo.ByMediaSize[0]);
    }

    public static String getMediaSizeValue(MediaSizeId mediaSizeName) {
        String defaultValue = SubUnitInfo.MediaSize.OTHER.getValue();

        if (mediaSizeName == null) {
            return defaultValue;
        }

        ITypeConverter<MediaSizeId, SubUnitInfo.MediaSize> converter = DeviceUsageTypeMapping.mediaSize.getConverter();
        if (converter == null) {
            return defaultValue;
        }

        SubUnitInfo.MediaSize mappedEnum = converter.convertEtoW(mediaSizeName);
        return (mappedEnum != null) ? mappedEnum.getValue() : defaultValue;
    }
}
