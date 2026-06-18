package com.hp.jetadvantage.link.device.services.interfaces;

public interface IDevicePrintJobService {
    boolean isSupported();

    String getIppEndpoint();

    String getUiContextToken(String packageName);

    //boolean isActiveJob(String jobUuid);

    //int getPrintedPages(String jobUuid);
}
