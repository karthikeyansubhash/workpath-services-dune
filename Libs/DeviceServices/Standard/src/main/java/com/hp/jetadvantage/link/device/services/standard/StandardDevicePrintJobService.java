package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.printjob.PrintJobServiceClientImpl;
import com.hp.ext.service.printJob.Capabilities;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscovery;
import com.hp.jetadvantage.link.device.services.clients.DeviceConnectorHelper;
import com.hp.jetadvantage.link.device.services.interfaces.IDevicePrintJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.jobmanagement.Job;
import com.hp.ws.cdm.jobmanagement.Jobs;
import com.hp.ws.cdm.jobmanagement.Pages;

import java.util.Optional;

public class StandardDevicePrintJobService extends StandardDeviceService implements IDevicePrintJobService {

    private static final String TAG = Constants.TAG + "/PrintJob";


    public StandardDevicePrintJobService() {
        super();
    }

    private static String sIppEndpoint = "";

    public StandardDevicePrintJobService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }


    @Override
    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            PrintJobServiceClientImpl client = new PrintJobServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try {
            Capabilities e2PrintCap = perform(call);
            String serviceGun = e2PrintCap.getServiceGun();
            if (serviceGun != null && serviceGun.equalsIgnoreCase(PrintJobConstants.E2SERVICE_PRINT_JOB_CANONICAL_GUN)) {
                return true;
            }
            Log.i(TAG, "isSupported : serviceGun not match :" + serviceGun);
            return false;
        } catch (RuntimeException e) {
            Log.i(TAG, "isSupported : RuntimeException=" + e.getMessage());
            return false;
        }
    }

    @Override
    public String getIppEndpoint() {
        if (sIppEndpoint.isEmpty()) {
            sIppEndpoint = PrintJobConstants.DEFAULT_IPPENDPOINT;
            ServicesDiscovery servicesDiscovery = getDiscoveryTree();
            Optional<ServiceMetadataImpl> appServiceMetadata = (null != servicesDiscovery.getServices()
                    ? servicesDiscovery.getServices().stream()
                    .filter(serviceMetadata -> serviceMetadata.getServiceGun()
                            .equalsIgnoreCase(PrintJobConstants.SERVICE_GUN))
                    .findAny()
                    : Optional.empty());
            if (appServiceMetadata.isPresent()) {
                for (Link link : appServiceMetadata.get().getLinks()) {
                    if (PrintJobConstants.IPP_REL.equalsIgnoreCase(link.getRel())) {
                        sIppEndpoint = link.getHref();
                    }
                }
            }
        }
        return DeviceConnectorHelper.generateHttpPrefixURL(getDeviceIPAddress(), 0, false) + sIppEndpoint;
    }

    @Override
    public String getUiContextToken(String packageName) {
        return super.getUiContextToken(packageName);
    }

    /*@Override
    public boolean isActiveJob(String jobUuid) {
        CdmCall cdmCall = () -> getCDMClient().sendGetRequest(PrintJobConstants.ACTIVE_JOB_URL);
        Jobs jobs = perform(cdmCall, Jobs.class);
        if (jobs != null) {
            Job.State state = jobs.getJobList().stream()
                    .filter(job -> jobUuid.equals(job.getJobId()))
                    .findFirst()
                    .map(job -> job.getState())
                    .get();

            return !Job.State.COMPLETED.equals(state);
        }
        return false;
    }
    @Override
    public int getPrintedPages(String jobUuid) {
        CdmCall cdmCall = () -> getCDMClient().sendGetRequest(String.format(PrintJobConstants.PAGES_URL, jobUuid));
        Pages pages = perform(cdmCall, Pages.class);
        if (pages != null) {
            return pages.getPages().stream()
                    .findFirst()
                    .map(page -> page.getCompletedCopiesCount())
                    .orElse(0);
        }
        return 0;
    }*/

    public static final class PrintJobConstants {
        public static final String E2SERVICE_PRINT_JOB_CANONICAL_GUN = "com.hp.ext.service.printJob.version.1";

        public static final String SERVICE_GUN = "com.hp.standard.feature.pwgIpp";

        public static final String IPP_REL = "homeUrl";

        public static final String DEFAULT_IPPENDPOINT = "/ipp/print";

        // The below URLs are not used in the current implementation
        // but may be used in future so keeping them here for reference.
        // public static final String ACTIVE_JOB_URL = "/cdm/jobManagement/v1/queue";
        // public static final String PAGES_URL = "/cdm/jobManagement/v1/jobs/%s/pages";
    }
}
