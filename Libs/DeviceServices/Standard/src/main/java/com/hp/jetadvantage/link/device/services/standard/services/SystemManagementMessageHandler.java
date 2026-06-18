package com.hp.jetadvantage.link.device.services.standard.services;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAuthSessionChangeCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSystemStateChangeCallback;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.system.IWebsocketCallback;
import com.hp.ws.websocket.SystemManagementMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SystemManagementMessageHandler {
    public static final String MESSAGE_TYPE = "systemManagement";
    private static final String TAG = Constants.TAG + "/WS/SysMgmt";
    private static final CopyOnWriteArrayList<IDeviceAuthSessionChangeCallback> authSessionChangeListeners =
            new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<SystemManagementMessage.SystemState, CopyOnWriteArrayList<IDeviceSystemStateChangeCallback>> systemStateChangeListenersMap = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public final IWebsocketCallback callback = new IWebsocketCallback.Stub() {
        @Override
        public void onMessageReceived(int what, String data) throws RemoteException {
            onReceived(what, data);
        }
    };

    public static void addAuthSessionChangeListener(IDeviceAuthSessionChangeCallback listener) {
        if (listener != null) {
            authSessionChangeListeners.add(listener);
            Log.d(TAG, "Auth session change listener added: " + listener);
        }
    }

    public static void removeAuthSessionChangeListener(IDeviceAuthSessionChangeCallback listener) {
        if (listener != null) {
            authSessionChangeListeners.remove(listener);
            Log.d(TAG, "Auth session change listener removed: " + listener);
        }
    }

    public static void clearAuthSessionChangeListeners() {
        int count = authSessionChangeListeners.size();
        authSessionChangeListeners.clear();
        Log.d(TAG, "Cleared " + count + " auth session change listeners");
    }

    public static void addSystemStateChangeListener(SystemManagementMessage.SystemState type, IDeviceSystemStateChangeCallback listener) {
        if (type != null && listener != null) {
            systemStateChangeListenersMap.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(listener);
            Log.d(TAG, "System State Change Listener added for type: " + type + ", listener: " + listener);
        }
    }

    public static void removeSystemStateChangeListener(IDeviceSystemStateChangeCallback listener) {
        if (listener != null) {
            systemStateChangeListenersMap.forEach((type, listeners) -> listeners.remove(listener));
            Log.d(TAG, "System State Change Listener removed: " + listener);
        }
    }

    public static void clearSystemStateChangeListeners() {
        int count = systemStateChangeListenersMap.size();
        systemStateChangeListenersMap.clear();
        Log.d(TAG, "Cleared " + count + " System State Change Listener");
    }

    public void onReceived(int what, String data) {
        Log.d(TAG, "onReceived what=" + what + ", data=" + data);
        if (StringUtility.isEmpty(data)) {
            Log.e(TAG, "Empty or null payload received");
            return;
        }

        try {
            SystemManagementMessage message = StandardJsonParser.INSTANCE.fromJson(data, SystemManagementMessage.class);
            if (message == null) {
                Log.e(TAG, "Failed to parse message, result is null");
                return;
            }
            if (message.hasAuthnSessionChange() && !authSessionChangeListeners.isEmpty()) {
                dispatchAuthSessionEvent(message.getSystemManagement().getDetails().getAuthnSessionChange().getEvent());
            }
            else if(message.hasSystemStateChange() && !systemStateChangeListenersMap.isEmpty()) {
                dispatchSystemStateChangeEvent(message.getSystemManagement().getDetails().getSystemStateChange().getEvent());
            }

        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Invalid JSON format: " + data, e);
        } catch (NullPointerException e) {
            Log.e(TAG, "Null reference while processing message: " + data, e);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error processing message: " + e.getMessage(), e);
        }
    }

    private void dispatchAuthSessionEvent(SystemManagementMessage.AuthnSessionEvent event) {
        if (event == null) {
            Log.e(TAG, "dispatchAuthSessionEvent: event is null");
            return;
        }
        executor.execute(() -> {
            int listenerCount = authSessionChangeListeners.size();
            Log.d(TAG, "dispatchAuthSessionEvent: " + event + " to " + listenerCount + " listeners");

            for (IDeviceAuthSessionChangeCallback listener : authSessionChangeListeners) {
                try {
                    switch (event) {
                        case AS_FRONT_PANEL_LOGIN:
                            listener.onSignIn();
                            break;
                        case AS_FRONT_PANEL_LOGOUT:
                            listener.onSignOut();
                            break;
                        default:
                            Log.w(TAG, "Unhandled auth session event: " + event);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in auth session callback: " + e.getMessage(), e);
                }
            }
            Log.d(TAG, "dispatchAuthSessionEvent: EXIT for event: " + event);
        });
    }

    private void dispatchSystemStateChangeEvent(SystemManagementMessage.SystemState type) {
        if (type == null) {
            return;
        }
        executor.execute(() -> {
            CopyOnWriteArrayList<IDeviceSystemStateChangeCallback> listeners = systemStateChangeListenersMap.get(type);
            int listenerCount = listeners != null ? listeners.size() : 0;
            Log.d(TAG, "dispatchSystemStateChangeEvent: " + type + " to " + listenerCount + " listeners");
            if (listeners != null) {
                for (IDeviceSystemStateChangeCallback listener : listeners) {
                    try {
                        listener.onChange();
                    } catch (Exception e) {
                        Log.e(TAG, "Error in state change callback: " + e.getMessage(), e);
                    }
                }
            }
            Log.d(TAG, "dispatchSystemStateChangeEvent: EXIT for type: " + type);
        });
    }

    /**
     * Shutdown executor service properly to avoid memory leaks
     */
    public void shutdown() {
        try {
            Log.d(TAG, "Shutting down executor service");
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                Log.w(TAG, "Executor did not terminate in the specified time, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Executor shutdown interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }
}
