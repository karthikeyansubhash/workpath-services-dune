package com.hp.jetadvantage.link.services.scanlet;

import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanTicketAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

public class ScanConstants {
    private static final String TAG = Scanlet.TAG + "/Const";
    /**
     * Define default destination for scan operations
     * Used as default value when destination is not specified in API calls
     */
    public static final ScanAttributes.Destination DEFAULT_SCAN_DESTINATION = ScanAttributes.Destination.HTTP;

    /**
     * Destination categories: For "Me", "Email", and "USB" Workpath destinations,
     *  the scan output is retrieved from the device's local folder,
     *  and delivered to the destination by Workpath platform.
     */
    public static final EnumSet<ScanAttributes.Destination> LOCAL_FOLDER_DESTINATIONS =
            EnumSet.of(ScanAttributes.Destination.ME, ScanAttributes.Destination.EMAIL, ScanAttributes.Destination.USB);
    /**
     * Destination categories: For "FTP", and "NETWORK_FOLDER" Workpath destinations,
     *  the scan output is delivered to the destination directly by the device firmware
     *  using a device's network folder destination job.
     */
    public static final EnumSet<ScanAttributes.Destination> NETWORK_FOLDER_DESTINATIONS =
            EnumSet.of(ScanAttributes.Destination.FTP, ScanAttributes.Destination.NETWORK_FOLDER);

    public static final String EXTENSIBILITY_WORKPATH_SOLUTIONS_DIRECTORY = "/data/workpath/Customer/solutions/";
    public static final String METADATA_PREFIX = "metadata-";
    public static final String METADATA_MEDIA_TYPE = "application/json";
    public static final String METADATA_FILE_NAME = METADATA_PREFIX + "$JOB(ID)$.json";
    public static final String METADATA_SCAN_SOURCE = "$SCAN(SOURCE)$";
    public static final String METADATA_SCAN_SIZE = "$SCAN(MEDIA_SIZE)$";
    public static final String DEFAULT_JOB_NAME = "[Untitled]";


    public static final String CACHED_METADATA_CONTENT;

    static {
        String metadataContent = null;
        try (InputStream is = ScanConstants.class.getClassLoader().getResourceAsStream("metadata_file_content.json")) {
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                metadataContent = sb.toString();
            } else {
                SLog.e(TAG, "metadata_file_content.json not found in resources");
            }
        } catch (Exception e) {
            SLog.e(TAG, "Failed to read metadata content: " + e.getMessage());
        }
        CACHED_METADATA_CONTENT = (metadataContent != null) ? metadataContent : "{}";
    }

    public static File getOriginalFiles(String solutionUuid, String jobId) {
        return new File(EXTENSIBILITY_WORKPATH_SOLUTIONS_DIRECTORY + solutionUuid + "/jobs/" + jobId);
    }
}
