package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelReturnMessage;
import com.hp.ws.websocket.AppChannelService;
import com.hp.ws.websocket.AppChannelServiceResponse;
import com.hp.ws.websocket.AppChannelSetup;

public class AppChannelServiceMessageProcessor extends AppChannelMessageProcessor {
    protected static final String TAG = Constants.TAG + "/AppChn/Service";
    protected AppChannelMessageQueue msgQueue;

    AppChannelServiceMessageProcessor(AppChannelContext ctx) {
        super(ctx);

        if (queueEnabled) {
            msgQueue = new AppChannelMessageQueue();
            AppChannelCallbackRegistry.addServiceCallbackRegistrationListener(
                    (e2ServiceGun, path, callback) -> deliverQueuedMessagesToCallback(e2ServiceGun, path, callback));
        }
    }

    @Override
    protected boolean canProcessMessage(ChannelMessage message) {
        return message.getMessage().hasService();
    }

    @Override
    protected void processMessage(String channelId, ChannelMessage message) {
        Log.d(TAG, "processMessage: ENTER [" + channelId + "]");
        try {
            String appPackageId = ctx.getAppChannelRegistry().getPackageId(channelId);
            AppChannelService appChannelService = message.getMessage().getService();
            if (appChannelService == null) {
                Log.e(TAG, "processMessage:[" + channelId + "] received empty service message");
                return;
            }

            String serviceCallId = appChannelService.getServiceCallId();
            String path = appChannelService.getPath();
            if (StringUtility.isEmpty(serviceCallId)) {
                Log.e(TAG, "processMessage:[" + channelId + "] empty serviceCallId");
                return;
            }

            AppChannelSetup channel = ctx.getAppChannelRegistry().getChannel(channelId);
            if (channel == null) {
                String errorMsg = "processMessage:[" + channelId + "] unknown channel";
                Log.e(TAG, errorMsg);
                sendServiceErrorResponse(channelId, serviceCallId, AppChannelHttpStatus.NOT_FOUND, errorMsg);
                return;
            }

            String e2ServiceGun = extractE2ServiceGun(channel, null);
            if (StringUtility.isEmpty(e2ServiceGun)) {
                String errorMsg = "can't find e2ServiceGun from existing channels";
                sendServiceErrorResponse(channelId, serviceCallId, AppChannelHttpStatus.NOT_FOUND, errorMsg);
                return;
            }

            StandardDeviceService.IServiceCallback callback =
                    AppChannelCallbackRegistry.getServiceCallback(e2ServiceGun, path);
            if (callback != null) {
                submitServiceCall(channelId, serviceCallId, appPackageId, appChannelService, callback);
            } else if (queueEnabled && msgQueue != null) {
                Log.d(TAG, String.format("processMessage :[%s] enqueue message for service call %s,%s,%s",
                        channelId, e2ServiceGun, path, serviceCallId));
                msgQueue.addMessage(getServiceQueueKey(e2ServiceGun, path), message);
            } else {
                Log.e(TAG, String.format("processMessage :[%s] can't find callback for service call %s,%s,%s",
                        channelId, e2ServiceGun, path, serviceCallId));
                String errorMsg = "can't find callback for service call :" + e2ServiceGun + ", path:" + path;
                sendServiceErrorResponse(channelId, serviceCallId, AppChannelHttpStatus.NOT_FOUND, errorMsg);
            }
        } catch (Exception e) {
            Log.e(TAG, "processMessage:[" + channelId + "] Exception: " + e.getMessage(), e);
        }
        Log.d(TAG, "processMessage: EXIT [" + channelId + "]");
    }

    protected void deliverQueuedMessagesToCallback(String e2ServiceGun, String path,
                                                   StandardDeviceService.IServiceCallback callback) {
        if (msgQueue == null || callback == null || StringUtility.isEmpty(e2ServiceGun) || StringUtility.isEmpty(path)) {
            Log.w(TAG, "deliverQueuedMessagesToCallback: Invalid parameters");
            return;
        }

        try {
            String key = getServiceQueueKey(e2ServiceGun, path);
            while (!msgQueue.isEmpty(key)) {
                ChannelMessage channelMessage = msgQueue.pollMessage(key);
                if (channelMessage == null) break;

                AppChannelService service = channelMessage.getMessage().getService();
                if (service != null && path.equals(service.getPath())) {
                    Log.d(TAG, "deliverQueuedMessagesToCallback: e2ServiceGun: " + e2ServiceGun);
                    String packageId = ctx.getAppChannelRegistry().getPackageId(channelMessage.getChannelId());
                    if (!StringUtility.isEmpty(packageId)) {
                        submitServiceCall(
                                channelMessage.getChannelId(),
                                service.getServiceCallId(),
                                packageId,
                                service,
                                callback
                        );
                    } else {
                        Log.e(TAG, "deliverQueuedMessagesToCallback: packageId is null for channelId: "
                                + channelMessage.getChannelId());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deliverQueuedMessagesToCallback: Exception: " + e.getMessage(), e);
        }
    }

    private String getServiceQueueKey(String e2ServiceGun, String path) {
        return e2ServiceGun + "/" + path;
    }

    private boolean submitServiceCall(String channelId, String serviceCallId, String appPackageId,
                                      AppChannelService appChannelService,
                                      StandardDeviceService.IServiceCallback callback) {
        if (StringUtility.isEmpty(channelId) || StringUtility.isEmpty(serviceCallId) ||
                callback == null || appChannelService == null) {
            Log.e(TAG, "submitServiceCall: Invalid parameters");
            return false;
        }

        Log.d(TAG, String.format("submitServiceCall: [%s,%s] ENTER", channelId, serviceCallId));
        boolean result = AppChannelServiceThreadPool.submit(channelId, serviceCallId, () -> {
            AppChannelServiceResponse response = null;
            try {
                Log.d(TAG, String.format("ServiceCall: [%s,%s] ENTER onServiceCall", channelId, serviceCallId));
                response = callback.onServiceCall(appPackageId, appChannelService);
                Log.d(TAG, String.format("ServiceCall: [%s,%s] EXIT onServiceCall", channelId, serviceCallId));
            } catch (Exception e) {
                Log.e(TAG, String.format("ServiceCall: [%s,%s] Exception: %s", channelId, serviceCallId,
                        e.getMessage()), e);
            }
            if (response == null) {
                String errMsg = "The service callback returned null or exception";
                sendServiceErrorResponse(channelId, serviceCallId, AppChannelHttpStatus.INTERNAL_SERVER_ERROR, errMsg);
            } else {
                sendServiceResponse(channelId, serviceCallId, response);
            }
        });

        if (!result) {
            String errorMsg = "conflict serviceCallId:" + serviceCallId;
            Log.e(TAG, "submitServiceCall: [" + channelId + "]" + errorMsg);
            sendServiceErrorResponse(channelId, serviceCallId, AppChannelHttpStatus.CONFLICT, errorMsg);
        }
        Log.d(TAG, String.format("submitServiceCall: [%s,%s] EXIT", channelId, serviceCallId));
        return result;
    }

    private void sendServiceErrorResponse(String channelId, String serviceCallId,
                                          AppChannelHttpStatus httpStatus, String errorMsg) {
        if (StringUtility.isEmpty(channelId) || StringUtility.isEmpty(serviceCallId) || httpStatus == null) {
            Log.e(TAG, "sendServiceErrorResponse: Invalid parameters");
            return;
        }

        try {
            Log.i(TAG, String.format("sendServiceErrorResponse:[%s, %s] httpStatus = %s(%d), %s",
                    channelId, serviceCallId, httpStatus.reason(), httpStatus.code(), errorMsg));
            AppChannelServiceResponse serviceResponse = new AppChannelServiceResponse(serviceCallId, httpStatus.code());
            AppChannelReturnMessage returnMsg = new AppChannelReturnMessage(channelId);
            serviceResponse.setServiceCallId(serviceCallId);
            returnMsg.getChannelMessage().getMessage().setServiceResponse(serviceResponse);
            ctx.send(returnMsg);
        } catch (Exception e) {
            Log.e(TAG, String.format("[%s,%s] sendServiceErrorResponse: Exception : %s",
                    channelId, serviceCallId, e.getMessage()), e);
        }
    }

    private void sendServiceResponse(String channelId, String serviceCallId,
                                     AppChannelServiceResponse response) {
        if (StringUtility.isEmpty(channelId) || StringUtility.isEmpty(serviceCallId) || response == null) {
            Log.e(TAG, "sendServiceResponse: Invalid parameters");
            return;
        }

        try {
            AppChannelReturnMessage returnMsg = new AppChannelReturnMessage(channelId);
            response.setServiceCallId(serviceCallId);
            returnMsg.getChannelMessage().getMessage().setServiceResponse(response);

            Log.d(TAG, String.format("sendServiceResponse:[%s, %s] %s",
                    channelId, serviceCallId, StandardJsonParser.INSTANCE.toJson(returnMsg)));
            ctx.send(returnMsg);
        } catch (Exception e) {
            Log.e(TAG, String.format("[%s,%s] sendServiceResponse: Exception : %s",
                    channelId, serviceCallId, e.getMessage()), e);
        }
    }
}
