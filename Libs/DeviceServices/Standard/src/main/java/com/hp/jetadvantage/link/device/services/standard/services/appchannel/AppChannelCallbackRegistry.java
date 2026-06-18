package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.IPayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.IServiceCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService.ISetupCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry for AppChannel callbacks
 * Each StandardDevice service can register its E2ServiceGun name and callback to receive notification messages from E2
 * service. For each E2ServiceGUN, multiple PayloadCallback can be registered
 */
public class AppChannelCallbackRegistry {
    // ConcurrentHashMap<E2ServiceGUN, List<IPayloadCallback>>
    private static final ConcurrentHashMap<String, List<IPayloadCallback>> payloadCallbackMap =
            new ConcurrentHashMap<>();

    // ConcurrentHashMap<E2ServiceGUN, List<ISetupCallback>>
    private static final ConcurrentHashMap<String, List<ISetupCallback>> setupCallbackMap = new ConcurrentHashMap<>();

    // ConcurrentHashMap<E2ServiceGUN, HashMap<path, IServiceCallback>>
    private static final ConcurrentHashMap<String, HashMap<String, IServiceCallback>> serviceCallbackMap =
            new ConcurrentHashMap<>();

    private static final CopyOnWriteArrayList<IPayloadCallbackRegistrationListener> payloadCallbackListeners =
            new CopyOnWriteArrayList<>();

    private static final CopyOnWriteArrayList<ISetupCallbackRegistrationListener> setupCallbackListeners =
            new CopyOnWriteArrayList<>();

    private static final CopyOnWriteArrayList<IServiceCallbackRegistrationListener> serviceCallbackListeners =
            new CopyOnWriteArrayList<>();

    public static void clear() {
        payloadCallbackMap.clear();
        setupCallbackMap.clear();
        serviceCallbackMap.clear();
        payloadCallbackListeners.clear();
        setupCallbackListeners.clear();
        serviceCallbackListeners.clear();
    }

    public static void registerPayloadCallback(String e2ServiceGun, IPayloadCallback callback) {
        synchronized (payloadCallbackMap) {
            try {
                payloadCallbackListeners.forEach(l -> l.beforeCallbackRegister(e2ServiceGun, callback));
            } catch (Exception ignored) {
            }
            registerCallback(e2ServiceGun, callback, payloadCallbackMap);
        }
    }

    public static void unregisterPayloadCallback(String e2ServiceGun, IPayloadCallback callback) {
        synchronized (payloadCallbackMap) {
            unregisterCallback(e2ServiceGun, callback, payloadCallbackMap);
        }
    }

    public static void registerSetupCallback(String e2ServiceGun, ISetupCallback callback) {
        synchronized (setupCallbackMap) {
            try {
                setupCallbackListeners.forEach(l -> l.beforeCallbackRegister(e2ServiceGun, callback));
            } catch (Exception ignored) {
            }
            registerCallback(e2ServiceGun, callback, setupCallbackMap);
        }
    }

    public static void unregisterSetupCallback(String e2ServiceGun, ISetupCallback callback) {
        synchronized (setupCallbackMap) {
            unregisterCallback(e2ServiceGun, callback, setupCallbackMap);
        }
    }

    /**
     * registerServiceCallback - register a callback for a service call
     * only one callback can be registered for a path of a E2ServiceGun
     *
     * @param e2ServiceGun E2 Service Gun (ex : "com.hp.ext.service.authentication.version.1")
     * @param path         path of the target service call (ex : "prePromptResult")
     * @param callback     callback to be invoked when the target service call is received
     */
    public static void registerServiceCallback(String e2ServiceGun, String path, IServiceCallback callback) {
        synchronized (serviceCallbackMap) {
            serviceCallbackListeners.forEach(l -> l.beforeCallbackRegister(e2ServiceGun, path, callback));
            serviceCallbackMap.computeIfAbsent(e2ServiceGun, k -> new HashMap<>()).put(path, callback);
        }
    }

    /**
     * unregisterServiceCallback - unregister a callback for a service call
     *
     * @param e2ServiceGun E2 Service Gun (ex : "com.hp.ext.service.authentication.version.1")
     * @param path         path of the service call (ex : "prePromptResult")
     */
    public static void unregisterServiceCallback(String e2ServiceGun, String path) {
        synchronized (serviceCallbackMap) {
            HashMap<String, IServiceCallback> pathCallbackMap = serviceCallbackMap.get(e2ServiceGun);
            if (pathCallbackMap != null) {
                pathCallbackMap.remove(path);
                if (pathCallbackMap.isEmpty()) {
                    serviceCallbackMap.remove(e2ServiceGun);
                }
            }
        }
    }

    protected static void addPayloadCallbackRegistrationListener(IPayloadCallbackRegistrationListener listener) {
        payloadCallbackListeners.add(listener);
    }

    protected static void removePayloadCallbackRegistrationListener(IPayloadCallbackRegistrationListener listener) {
        payloadCallbackListeners.remove(listener);
    }

    protected static void addSetupCallbackRegistrationListener(ISetupCallbackRegistrationListener listener) {
        setupCallbackListeners.add(listener);
    }

    protected static void removeSetupCallbackRegistrationListener(ISetupCallbackRegistrationListener listener) {
        setupCallbackListeners.remove(listener);
    }

    protected static void addServiceCallbackRegistrationListener(IServiceCallbackRegistrationListener listener) {
        serviceCallbackListeners.add(listener);
    }

    protected static void removeServiceCallbackRegistrationListener(IServiceCallbackRegistrationListener listener) {
        serviceCallbackListeners.remove(listener);
    }


    protected static List<IPayloadCallback> getPayloadCallback(String e2ServiceGun) {
        synchronized (payloadCallbackMap) {
            List<IPayloadCallback> list = payloadCallbackMap.get(e2ServiceGun);
            return list != null ? new ArrayList<>(list) : new ArrayList<>();
        }
    }

    protected static List<ISetupCallback> getSetupCallback(String e2ServiceGun) {
        synchronized (setupCallbackMap) {
            List<ISetupCallback> list = setupCallbackMap.get(e2ServiceGun);
            return list != null ? new ArrayList<>(list) : new ArrayList<>();
        }
    }

    protected static IServiceCallback getServiceCallback(String e2ServiceGun, String path) {
        synchronized (serviceCallbackMap) {
            return serviceCallbackMap.getOrDefault(e2ServiceGun, new HashMap<>()).get(path);
        }
    }

    private static <T> void registerCallback(String e2ServiceGun, T callback,
                                             ConcurrentHashMap<String, List<T>> callbackMap) {
        callbackMap.computeIfAbsent(e2ServiceGun, k -> new ArrayList<>()).add(callback);
    }

    private static <T> void unregisterCallback(String e2ServiceGun, T callback,
                                               ConcurrentHashMap<String, List<T>> callbackMap) {
        List<T> callbackList = callbackMap.get(e2ServiceGun);
        if (callbackList != null) {
            callbackList.remove(callback);
            if (callbackList.isEmpty()) {
                callbackMap.remove(e2ServiceGun);
            }
        }
    }

    public interface IPayloadCallbackRegistrationListener {
        void beforeCallbackRegister(String e2ServiceGun, IPayloadCallback callback);
    }

    public interface IServiceCallbackRegistrationListener {
        void beforeCallbackRegister(String e2ServiceGun, String path, IServiceCallback callback);
    }

    public interface ISetupCallbackRegistrationListener {
        void beforeCallbackRegister(String e2ServiceGun, ISetupCallback callback);
    }
}
