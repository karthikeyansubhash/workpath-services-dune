package com.hp.jetadvantage.link.services.accesslet.service;

import android.content.Context;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import com.hp.ext.types.authentication.AuthenticationCanceled;
import com.hp.ext.types.authentication.AuthenticationContinued;
import com.hp.ext.types.authentication.AuthenticationFailed;
import com.hp.ext.types.authentication.AuthenticationSuccess;
import com.hp.ext.types.authentication.PostAuthInitiateApplicationLaunchAction;
import com.hp.ext.types.authentication.PostPromptResult;
import com.hp.ext.types.authentication.PostPromptResultValue;
import com.hp.ext.types.authentication.PrePromptResult;
import com.hp.ext.types.authentication.PrePromptResultValue;
import com.hp.ext.types.authentication.PromptResultAction;
import com.hp.ext.types.common.E2Type;
import com.hp.ext.types.security.KeyValuePair;
import com.hp.ext.types.security.KeyValuePairValue;
import com.hp.jetadvantage.link.api.access.AuthenticationAttributes;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.api.access.SignInAction;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IE2ServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceAccessService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.services.accesslet.adapter.AccessAdapter;
import com.hp.jetadvantage.link.services.accesslet.sync.AuthSyncManager;
import com.hp.jetadvantage.link.services.accesslet.util.AuthenticationUtility;
import com.hp.ws.websocket.AppChannelServiceResponse;
import com.hp.ws.websocket.JsonTypedObject;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CallbackHandler {
    private static final String TAG = "CallbackHandler";
    private String mLastAuthPackageName;
    private final Context mContext;
    private final AuthenticationObserverService mService;
    private IDeviceAccessService accessDeviceService;

    public CallbackHandler(Context context, AuthenticationObserverService service) {
        mContext = context;
        mService = service;
        accessDeviceService = new StandardDeviceAccessService();
    }

    public final IDeviceAuthSessionChangeCallback authSessionChangeCallback = new IDeviceAuthSessionChangeCallback() {
        @Override
        public void onSignIn() {
            SLog.i(TAG, "onSignIn");
            if (mLastAuthPackageName != null) {
                Principal principal = AccessAdapter.getPrincipalProperties(accessDeviceService, mLastAuthPackageName);
                mService.sendMessageToClient(mContext, mLastAuthPackageName, principal);
                mLastAuthPackageName = null;
            }
        }

        @Override
        public void onSignOut() {
            SLog.i(TAG, "onSignOut");
        }
    };

    public final IE2ServiceCallback prePromptCallback = (packageName, serviceMessage)
            -> handlePrePromptMessage(packageName, serviceMessage.getServiceCallId());

    public final IE2ServiceCallback postPromptCallback = (packageName, serviceMessage)
            -> handlePostPromptMessage(packageName, serviceMessage.getServiceCallId());

    public final IE2ServiceCallback signOutNotificationCallback = (packageName, serviceMessage)
            -> handleSignOutNotificationCallback(packageName, serviceMessage.getServiceCallId());

    private AppChannelServiceResponse handlePrePromptMessage(String packageName, String serviceCallId) {
        String typeName = new PrePromptResult().getTypeName();
        SLog.i(TAG, "handlePrePromptMessage");
        try {
            mService.sendMessagePrePromptToClient(packageName);

            CompletableFuture<Bundle> signInFuture = AuthSyncManager.createSignInFuture(packageName);

            SLog.i(TAG, "Hold for a moment and wait for the AccessletContentProvider.SignIn() to be called.");
            Bundle signInDataFromProvider = signInFuture.get(60, TimeUnit.SECONDS);

            if (signInDataFromProvider != null && signInDataFromProvider.containsKey("authentication")) {
                SLog.i(TAG, "Received sign-in data for package: " + packageName);
                AuthenticationAttributes authAttr = signInDataFromProvider.getParcelable("authentication");
                SignInAction signInAction = signInDataFromProvider.getParcelable("action");
                E2Type authResult = AuthenticationUtility.getAuthenticationResultFromAuthenticationAttributes(authAttr, signInAction);
                if (authResult instanceof AuthenticationSuccess) {
                    mLastAuthPackageName = packageName;
                }
                return getPrePromptResponse(serviceCallId, authResult);
            } else {
                return getErrorResponse("Received null sign-in data for package: " + packageName, serviceCallId, typeName);
            }
        } catch (Exception e) {
            return getErrorResponse("Failed to process from prePromptResult: " + e.getMessage(), serviceCallId, typeName);
        }
    }

    private AppChannelServiceResponse handlePostPromptMessage(String packageName, String serviceCallId) {
        String typeName = new PostPromptResult().getTypeName();
        SLog.i(TAG, "handlePostPromptMessage");
        try {
            E2Type authResult = AuthSyncManager.getAuthResult(packageName);
            if (authResult instanceof AuthenticationSuccess) {
                mLastAuthPackageName = packageName;
            }
            return getPostPromptResponse(serviceCallId, authResult);
        } catch (Exception e) {
            return getErrorResponse("Failed to process postPromptResult: " + e.getMessage(), serviceCallId, typeName);
        } finally {
            AuthSyncManager.removeAuthResult(packageName);
        }
    }

    private AppChannelServiceResponse handleSignOutNotificationCallback(String packageName, String serviceCallId) {
        SLog.i(TAG, "handleSignOutNotificationCallback: 200 OK");
        Principal principal = AccessAdapter.getPrincipalProperties(accessDeviceService, packageName);
        mService.sendMessageToClient(mContext, packageName, principal);
        return new AppChannelServiceResponse(serviceCallId, 200);
    }

    private AppChannelServiceResponse getErrorResponse(String message, String serviceCallId, String type) {
        SLog.e(TAG, "getErrorResponse: " + message);
        AuthenticationFailed failed = new AuthenticationFailed();
        failed.setMessage(message);
        if (new PrePromptResult().getTypeName().equals(type)) {
            return getPrePromptResponse(serviceCallId, failed);
        } else if (new PostPromptResult().getTypeName().equals(type)) {
            return getPostPromptResponse(serviceCallId, failed);
        }
        return null;
    }

    private AppChannelServiceResponse buildPromptResponse(String serviceCallId, E2Type authResult, PromptResultBuilder resultBuilder) {
        AppChannelServiceResponse response = new AppChannelServiceResponse(serviceCallId, 200);
        try {
            authResult = (authResult == null) ? new AuthenticationCanceled() : authResult;
            E2Type finalResultObject = resultBuilder.build(authResult);
            response.setResponseBody(getPromptResultToJson(finalResultObject));
        } catch (Exception e) {
            SLog.e(TAG, "buildPromptResponse exception: " + e.getMessage(), e);
        }
        return response;
    }

    private AppChannelServiceResponse getPrePromptResponse(String serviceCallId, E2Type authResult) {
        return buildPromptResponse(serviceCallId, authResult, (currentAuthResult) -> {
            PrePromptResult prePromptResult = new PrePromptResult();
            PrePromptResultValue prePromptResultValue = new PrePromptResultValue();
            SLog.i(TAG, "getPrePromptResponse: " + currentAuthResult.getClass().getSimpleName());

            if (currentAuthResult instanceof AuthenticationSuccess) {
                AuthenticationSuccess authenticationSuccess = (AuthenticationSuccess) currentAuthResult;
                prePromptResult.setAction(getPromptResultAction(authenticationSuccess));
                prePromptResultValue.setSucceeded(authenticationSuccess);
            } else if (currentAuthResult instanceof AuthenticationCanceled) {
                prePromptResultValue.setCanceled((AuthenticationCanceled) currentAuthResult);
            } else if (currentAuthResult instanceof AuthenticationFailed) {
                prePromptResultValue.setFailed((AuthenticationFailed) currentAuthResult);
            } else if (currentAuthResult instanceof AuthenticationContinued) {
                prePromptResultValue.setContinue((AuthenticationContinued) currentAuthResult);
            } else {
                SLog.e(TAG, "getPrePromptResponse: Unknown type found");
            }
            prePromptResult.setResult(prePromptResultValue);
            return prePromptResult;
        });
    }

    private AppChannelServiceResponse getPostPromptResponse(String serviceCallId, E2Type authResult) {
        return buildPromptResponse(serviceCallId, authResult, (currentAuthResult) -> {
            PostPromptResult postPromptResult = new PostPromptResult();
            PostPromptResultValue postPromptResultValue = new PostPromptResultValue();
            SLog.i(TAG, "getPostPromptResponse: " + currentAuthResult.getClass().getSimpleName());

            if (currentAuthResult instanceof AuthenticationSuccess) {
                AuthenticationSuccess authenticationSuccess = (AuthenticationSuccess) currentAuthResult;
                postPromptResult.setAction(getPromptResultAction(authenticationSuccess));
                postPromptResultValue.setSucceeded(authenticationSuccess);
            } else if (currentAuthResult instanceof AuthenticationCanceled) {
                postPromptResultValue.setCanceled((AuthenticationCanceled) currentAuthResult);
            } else if (currentAuthResult instanceof AuthenticationFailed) {
                postPromptResultValue.setFailed((AuthenticationFailed) currentAuthResult);
            } else {
                SLog.i(TAG, "getPostPromptResponse: Unknown type found");
            }
            postPromptResult.setResult(postPromptResultValue);
            return postPromptResult;
        });
    }

    private PromptResultAction getPromptResultAction(AuthenticationSuccess authenticationSuccess) {
        if (authenticationSuccess.getDetails().getCustomAttributes() != null) {
            List<KeyValuePair> keyValuePairs = authenticationSuccess.getDetails().getCustomAttributes();
            Iterator<KeyValuePair> iterator = keyValuePairs.iterator();
            while (iterator.hasNext()) {
                KeyValuePair keyValuePair = iterator.next();
                if (keyValuePair != null &&
                        keyValuePair.getKey() != null &&
                        "autoLaunchAppAccessPointId".equals(keyValuePair.getKey().getValue())) {
                    KeyValuePairValue keyValuePairValue = keyValuePair.getValueString();
                    String autoLaunchAppId = keyValuePairValue.getValue();
                    SLog.i(TAG, "Found autoLaunchAppAccessPointId value: " + autoLaunchAppId);
                    iterator.remove();
                    UUID applicationAccessPointId = AuthenticationUtility.getApplicationUuid(mContext, autoLaunchAppId);
                    if (applicationAccessPointId != null) {
                        SLog.i(TAG, "Found applicationAccessPointId value: " + applicationAccessPointId);
                        PromptResultAction action = new PromptResultAction();
                        PostAuthInitiateApplicationLaunchAction launchAction = new PostAuthInitiateApplicationLaunchAction();
                        launchAction.setApplicationAccessPointId(applicationAccessPointId);
                        action.setInitiateAppLaunch(launchAction);
                        return action;
                    }
                    break;
                }
            }
        }
        return null;
    }

    private JsonTypedObject getPromptResultToJson(E2Type data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String postPromptResultString = objectMapper.writeValueAsString(data);
        JsonTypedObject responseBody = new JsonTypedObject(data.getTypeGUN());
        responseBody.setValue(StandardJsonParser.INSTANCE.fromJson(postPromptResultString, JsonObject.class));
        return responseBody;
    }

    @FunctionalInterface
    private interface PromptResultBuilder {
        E2Type build(E2Type authResult) throws Exception;
    }
}
