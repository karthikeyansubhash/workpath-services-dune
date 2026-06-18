/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSettingsService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.ws.cdm.commonglossary.Property;
import com.hp.ws.cdm.network.AirPrint;
import com.hp.ws.cdm.network.FtpPrint;
import com.hp.ws.cdm.network.Ipp;
import com.hp.ws.cdm.network.LpdPrint;
import com.hp.ws.cdm.network.Port9100;
import com.hp.ws.cdm.network.PrintServices;
import com.hp.ws.cdm.network.WsPrint;

public class StandardDeviceSettingsService extends StandardDeviceService implements IDeviceSettingsService {
    private static final String TAG = Constants.TAG + "/Setting";
    public StandardDeviceSettingsService() {
        super();
    }

    public StandardDeviceSettingsService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public PrintServices getPrintServices() {
        CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.PRINT_SERVICES_DUNE);
        PrintServices printServices = perform(call, PrintServices.class);

        return printServices;
    }

    @Override
    public boolean disableNetworkPrintServices() {
        PrintServices printServices = getPrintServices();
        if (printServices != null) {
            AirPrint airPrint = printServices.getAirPrint();
            if (airPrint != null) airPrint.setEnabled(Property.FeatureEnabled.FALSE);

            FtpPrint ftpPrint = printServices.getFtpPrint();
            if (ftpPrint != null) ftpPrint.setEnabled(Property.FeatureEnabled.FALSE);

            WsPrint wsPrint = printServices.getWsPrint();
            if (wsPrint != null) wsPrint.setEnabled(Property.FeatureEnabled.FALSE);

            LpdPrint lpdPrint = printServices.getLpdPrint();
            if (lpdPrint != null) lpdPrint.setEnabled(Property.FeatureEnabled.FALSE);

            Ipp ipp = printServices.getIpp();
            if (ipp != null) {
                ipp.setIpp(Property.FeatureEnabled.FALSE);
                ipp.setIppSecure(Property.FeatureEnabled.FALSE);
            }

            Port9100 port9100 = printServices.getPort9100();
            if (port9100 != null) port9100.setEnabled(Property.FeatureEnabled.FALSE);

            try {
                CdmCall call = () -> getCDMClient().sendPatchRequest(CDMUrl.PRINT_SERVICES_DUNE, StandardJsonParser.INSTANCE.toJson(printServices));
                perform(call);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override //will not be supported due to mitigate security risk
    public boolean enableNetworkPrintServices() {
        PrintServices printServices = getPrintServices();
        if (printServices != null) {
            AirPrint airPrint = printServices.getAirPrint();
            if (airPrint != null) airPrint.setEnabled(Property.FeatureEnabled.TRUE);

            FtpPrint ftpPrint = printServices.getFtpPrint();
            if (ftpPrint != null) ftpPrint.setEnabled(Property.FeatureEnabled.TRUE);

            WsPrint wsPrint = printServices.getWsPrint();
            if (wsPrint != null) wsPrint.setEnabled(Property.FeatureEnabled.TRUE);

            LpdPrint lpdPrint = printServices.getLpdPrint();
            if (lpdPrint != null) lpdPrint.setEnabled(Property.FeatureEnabled.TRUE);

            Ipp ipp = printServices.getIpp();
            if (ipp != null) {
                ipp.setIpp(Property.FeatureEnabled.TRUE);
                ipp.setIppSecure(Property.FeatureEnabled.TRUE);
            }

            Port9100 port9100 = printServices.getPort9100();
            if (port9100 != null) port9100.setEnabled(Property.FeatureEnabled.TRUE);

            try {
                CdmCall call = () -> getCDMClient().sendPatchRequest(CDMUrl.PRINT_SERVICES_DUNE, StandardJsonParser.INSTANCE.toJson(printServices));
                perform(call);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static final class CDMUrl {
        public static final String PRINT_SERVICES_DUNE = "/cdm/network/v1/printServices";
    }
}
