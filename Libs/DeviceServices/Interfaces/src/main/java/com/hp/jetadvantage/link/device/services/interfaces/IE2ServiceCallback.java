package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;

public interface IE2ServiceCallback {
    AppChannelServiceResponse onReceive(String appPackageId, AppChannelService serviceMessage);
}
