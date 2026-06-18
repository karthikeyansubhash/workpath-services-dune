package com.hp.jetadvantage.link.services.deviceusage.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.deviceUsage.LifetimeCounters;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.usage.MediaSizeSheetSet;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUnitTest;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceUsageService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.utils.Utils;
import com.hp.jetadvantage.link.services.deviceusagelet.adapter.DeviceUsageAdapter;
import com.hp.workpath.api.deviceusage.DeviceUsageInfo;
import com.hp.workpath.api.deviceusage.DeviceUsagelet;
import com.hp.workpath.api.deviceusage.Plex;
import com.hp.workpath.api.deviceusage.SubUnitInfo;
import com.hp.workpath.api.deviceusage.printer.PrinterInfo;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;

@RunWith(MockitoJUnitRunner.class)
public class DeviceUsageAdapterTest extends StandardDeviceUnitTest {
    @Mock
    private StandardDeviceManagementService mockDeviceManagementService;
    @Mock
    private InjectedHttpClient mockHttpClient;

    public LifetimeCounters getLifetimeCounter(String jsonPath) throws IOException {
        String getLifetimeCountersData = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), jsonPath);

        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(createBasicTestServicesDiscovery());
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(getLifetimeCountersData);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        StandardDeviceUsageService deviceUsageService = new StandardDeviceUsageService(mockDeviceManagementService);
        assertNotNull(deviceUsageService);

        LifetimeCounters lifetimeCounters = null;
        try {
            lifetimeCounters = deviceUsageService.getLifetimeCounters(testPackageName);
        } catch (Exception e) {
            fail("Unexpected exception occurs:" + e);
        }
        return lifetimeCounters;
    }

    /**
     * Test that verifies the correct mapping of print usage counters to job categories in DeviceUsageInfo.
     */
    @Test
    public void GivenDeviceUsageAdapter_WhenGetDefaultsCalled_PrintUsageA4EquivalentByJobCategory() throws IOException {
        LifetimeCounters lifetimeCounters = getLifetimeCounter("usage/GET_ext_getLifetimeCounters.json");
        assertNotNull(lifetimeCounters);

        String info = DeviceUsageAdapter.convertToWorkpathDeviceUsageAdapter(lifetimeCounters);
        try {
            JSONObject jsonObject = new JSONObject(info).getJSONObject("subunits");
            Type listType = new TypeToken<DeviceUsageInfo>() {
            }.getType();

            DeviceUsageInfo deviceUsageInfo = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
            assertNotNull(deviceUsageInfo);

            PrinterInfo.ByJobCategory[] categories = deviceUsageInfo.getPrinter().getByJobCategory();
            for (PrinterInfo.ByJobCategory category : categories) {
                if (category.getJobCategoryType() == SubUnitInfo.JobCategory.COPY) {
                    assertEquals(10, DeviceUsageAdapter.getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentCopyImpressions().getTotal()));
                } else if (category.getJobCategoryType() == SubUnitInfo.JobCategory.PRINT) {
                    assertEquals(50, DeviceUsageAdapter.getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentPrintImpressions().getTotal()));
                } else if (category.getJobCategoryType() == SubUnitInfo.JobCategory.FAX) {
                    assertEquals(0, DeviceUsageAdapter.getCounterValue(lifetimeCounters.getPrintUsage().getA4EquivalentFaxInImpressions().getTotal()));
                }
            }

        } catch (Exception e) {
            fail("Unexpected exception occurs while parsing to DeviceUsageInfo:" + e);
        }

        assertNotNull(info);
    }

    /**
     * Test that verifies the correct mapping of print usage counters to media sizes in DeviceUsageInfo.
     */
    @Test
    public void GivenDeviceUsageAdapter_WhenGetDefaultsCalled_PrintByMediaSize() throws IOException {
        LifetimeCounters lifetimeCounters = getLifetimeCounter("usage/GET_ext_getLifetimeCounters.json");
        assertNotNull(lifetimeCounters);

        String info = DeviceUsageAdapter.convertToWorkpathDeviceUsageAdapter(lifetimeCounters);
        try {
            JSONObject jsonObject = new JSONObject(info).getJSONObject("subunits");
            Type listType = new TypeToken<DeviceUsageInfo>() {
            }.getType();

            DeviceUsageInfo deviceUsageInfo = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
            assertNotNull(deviceUsageInfo);

            PrinterInfo.PlexByMediaSize[] mediaSizes = deviceUsageInfo.getPrinter().getPlexByMediaSize();
            List<MediaSizeSheetSet> mediaSizeSheetSets = lifetimeCounters.getPrintUsage().getSheetsByMediaSize();

            assertEquals(mediaSizes.length, mediaSizeSheetSets.size());

            for (PrinterInfo.PlexByMediaSize mediaSize : mediaSizes) {
                for (MediaSizeSheetSet sheetSet : mediaSizeSheetSets) {
                    if (mediaSize.getMediaSize().equals(DeviceUsageAdapter.getMediaSizeValue(sheetSet.getMediaSize()))) {
                        assertEquals(mediaSize.getDuplexSheets(), DeviceUsageAdapter.safeLongToInt(sheetSet.getSheets().getDuplex()));
                        assertEquals(mediaSize.getSimplexSheets(), DeviceUsageAdapter.safeLongToInt(sheetSet.getSheets().getSimplex()));
                        assertEquals(mediaSize.getTotalSheets(), DeviceUsageAdapter.safeLongToInt(sheetSet.getSheets().getTotal()));
                    }
                }
            }

        } catch (Exception e) {
            fail("Unexpected exception occurs while parsing to DeviceUsageInfo:" + e);
        }

        assertNotNull(info);
    }

    /**
     * Test that verifies the correct mapping of scan usage counters to plex sheets in DeviceUsageInfo.
     */
    @Test
    public void GivenDeviceUsageAdapter_WhenGetDefaultsCalled_ScanUsageScanPlex() throws IOException {
        LifetimeCounters lifetimeCounters = getLifetimeCounter("usage/GET_ext_getLifetimeCounters.json");
        assertNotNull(lifetimeCounters);

        String info = DeviceUsageAdapter.convertToWorkpathDeviceUsageAdapter(lifetimeCounters);
        try {
            JSONObject jsonObject = new JSONObject(info).getJSONObject("subunits");
            Type listType = new TypeToken<DeviceUsageInfo>() {
            }.getType();
            DeviceUsageInfo deviceUsageInfo = JsonParser.getInstance().fromJson(jsonObject.toString(), listType);
            assertNotNull(deviceUsageInfo);

            Plex[] plexs = deviceUsageInfo.getScanner().getByScanPlex();
            for (Plex plex : plexs) {
                if (plex.getPlex().equals(DeviceUsagelet.ScanPlex.ADF_SIMPLEX)) {
                    assertEquals(DeviceUsageAdapter.safeLongToInt(lifetimeCounters.getScanUsage().getAdfSimplexImages()),
                            plex.getSheets());
                } else if (plex.getPlex().equals(DeviceUsagelet.ScanPlex.ADF_DUPLEX)) {
                    assertEquals(DeviceUsageAdapter.safeLongToInt(lifetimeCounters.getScanUsage().getAdfDuplexImages()),
                            plex.getSheets());
                } else if (plex.getPlex().equals(DeviceUsagelet.ScanPlex.FLATBED)) {
                    assertEquals(DeviceUsageAdapter.safeLongToInt(lifetimeCounters.getScanUsage().getFlatbedImages()),
                            plex.getSheets());
                }
            }
        } catch (Exception e) {
            fail("Unexpected exception occurs while parsing to DeviceUsageInfo:" + e);
        }
        assertNotNull(info);
    }

    /**
     * Test that verifies the correct handling of max and min values for copy impressions in DeviceUsageAdapter.
     */
    @Test
    public void GivenDeviceUsageAdapter_WhenGetDefaultsCalled_CopyImpression_thenMaxMinValue() throws IOException {
        LifetimeCounters lifetimeCounters = getLifetimeCounter("usage/GET_ext_getLifetimeCounters.json");

        assertEquals(Integer.MAX_VALUE, DeviceUsageAdapter.safeLongToInt(lifetimeCounters.getPrintUsage().getCopyImpressions().getMonochrome()));
        assertEquals(Integer.MIN_VALUE, DeviceUsageAdapter.safeLongToInt(lifetimeCounters.getPrintUsage().getCopyImpressions().getBlank()));
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetadatas());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetadatas() {
        List<ServiceMetadataImpl> serviceMetadatas = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl deviceUsageServiceMetadata = new ServiceMetadataImpl();
        deviceUsageServiceMetadata.setDescription("DeviceUsage service Tests Discovery Tree");
        deviceUsageServiceMetadata.setServiceGun("com.hp.ext.service.deviceUsage.version.1");
        deviceUsageServiceMetadata.setLinks(geLinks());
        serviceMetadatas.add(deviceUsageServiceMetadata);

        return serviceMetadatas;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/ext/deviceUsage/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link link2 = new Link();
        link2.setHref("/ext/deviceUsage/v1/deviceUsageAgents");
        link2.setRel("deviceUsageAgents");
        links.add(link2);

        return links;
    }
}
