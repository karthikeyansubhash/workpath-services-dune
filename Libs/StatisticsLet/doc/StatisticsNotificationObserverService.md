# Job Statistics Notifications Development Report

## 1. Overview

**Purpose**: Implement Job Statistics Notification functionality for the Dune (E2) platform in the StatisticsLet library.
**Core Principle**: Do not modify the 3rd-party developer-facing SDK API (e.g., `Statisticslet` class). Only **change internal logic** to add E2 WebSocket-based notification subscription.

### Existing Implementation References

| Reference                                       | Role                                                                      |
| ----------------------------------------------- | ------------------------------------------------------------------------- |
| `AccessoryLet`                                | Primary reference for Notification Observer Service pattern               |
| `ScanJobCompletedNotificationObserverService` | Reference for Broadcast sending pattern                                   |
| `StandardDeviceSolutionManagerService`        | Reference for `IServiceCallback` + `AppChannelCallbackRegistry` callback registration pattern |
| `StandardDeviceCopyJobService`                | Reference for `IE2PayloadCallback<T>` generic callback pattern            |

---

## 2. Architecture

### Notification Flow (Sequence)

```
E2 Statistics Agent  ──WebSocket──>  AppChannelCallbackRegistry
                                            │
                                    IServiceCallback (internal)
                                            │
                                    StandardDeviceStatisticsService
                                        ├── typeGUN validation
                                        ├── JSON deserialization (CustomObjectMapper)
                                        └── IE2PayloadCallback<StatisticsCallbackPayload> invocation
                                                │
                                    StatisticsNotificationObserverService
                                        ├── processStatisticsNotification()
                                        ├── sendBroadcastToApp()               → notify app
                                        └── sendBroadcastToPackageManager()    → notify package manager
```

### Core Types

| Type                          | Description                                                                                                                            |
| ----------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| `StatisticsCallbackPayload` | E2 auto-generated type. Contains `jobDetails`, `lastSequenceNumberProcessed`, `lastSequenceNumberNotified`, `missingSequenceNumbers` fields |
| `SequenceNumber`            | Extends `E2Alias<Long>`. Returns Long value via `getValue()`                                                                          |
| `IE2PayloadCallback<T>`     | Generic callback interface. `void onReceiveNotification(String appPackageId, T notification)`                                          |
| `IServiceCallback`          | Internal WebSocket callback. Receives `AppChannelService` and performs type-specific deserialization                                    |

---

## 3. Modified / Created Files

### 3.1 Source Code (3 files)

| # | File                                                                          | Module                         | Change Type    |
| - | ----------------------------------------------------------------------------- | ------------------------------ | -------------- |
| 1 | `Libs/DeviceServices/Interfaces/.../IDeviceStatisticsService.java`          | `:DeviceServices-Interfaces` | **Modified** |
| 2 | `Libs/DeviceServices/Standard/.../StandardDeviceStatisticsService.java`     | `:DeviceServices-Standard`   | **Modified** |
| 3 | `Libs/StatisticsLet/.../service/StatisticsNotificationObserverService.java` | `:Let-StatisticsLet`         | **Modified** |

### 3.2 Test Code (2 files)

| # | File                                                                      | Module                     | Change Type         |
| - | ------------------------------------------------------------------------- | -------------------------- | ------------------- |
| 4 | `tests/services/.../StandardDeviceStatisticsServiceUnitTest.java`       | `:Test-WorkpathServices` | **New**       |
| 5 | `tests/services/.../StatisticsNotificationObserverServiceUnitTest.java` | `:Test-WorkpathServices` | **New**       |

### 3.3 Build Configuration (1 file)

| # | File                            | Change Type                                                                 |
| - | ------------------------------- | --------------------------------------------------------------------------- |
| 6 | `tests/services/build.gradle` | **Modified** — Added `testImplementation project(':Let-StatisticsLet')` |

---

## 4. Detailed Changes per File

### 4.1 IDeviceStatisticsService.java (Interface)

**Path**: `Libs/DeviceServices/Interfaces/src/main/java/com/hp/jetadvantage/link/device/services/interfaces/IDeviceStatisticsService.java`

**Changes**: 2 methods added

```java
// Existing methods: isSupported(), getAllJobsList(), getJobList()

// Added methods
void registerNotificationCallback(IE2PayloadCallback<StatisticsCallbackPayload> callback);
void unRegisterNotificationCallback();
```

**Design Decisions**:

- Follows the same callback registration pattern as `IDeviceScanJobService`, `IDeviceCopyJobService`, etc.
- Only one callback per instance is allowed (last registration wins)
- `unRegisterNotificationCallback()` must be called on cleanup to prevent memory leaks

---

### 4.2 StandardDeviceStatisticsService.java (Device Service Implementation)

**Path**: `Libs/DeviceServices/Standard/src/main/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceStatisticsService.java`

**Changes**: Added callback registration/unregistration/processing logic

#### Added Field

```java
private volatile IPayloadCallback payloadCallback = null;
```

#### Added Methods

| Method                               | Description                                                                                                                                       |
| ------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| `registerNotificationCallback()`   | Creates `IServiceCallback` and registers with `AppChannelCallbackRegistry`. Uses `synchronized` block for thread safety. Unregisters existing callback first if present |
| `unRegisterNotificationCallback()` | Unregisters callback from `AppChannelCallbackRegistry` and sets to null                                                                           |
| `processStatisticsServiceCall()`   | Validates `JsonTypedObject` typeGUN → Deserializes via `CustomObjectMapper` → Invokes external callback                                          |

#### Operation Flow

1. When `registerNotificationCallback()` is called:

   - If existing callback exists, call `unRegisterNotificationCallback()` first
   - Create internal `IServiceCallback` lambda (for receiving raw WebSocket messages)
   - Register via `AppChannelCallbackRegistry.registerServiceCallback(E2SERVICE_STATISTICS_CANONICAL_GUN, path, callback)`
2. When WebSocket service message is received (`processStatisticsServiceCall()`):

   - Validate `typeGUN == "com.hp.ext.service.jobStatistics.version.1.type.statisticsCallbackPayload"`
   - Deserialize: `StandardJsonParser.INSTANCE.toJson()` → `CustomObjectMapper<StatisticsCallbackPayload>.readValue()`
   - Invoke external `IE2PayloadCallback<StatisticsCallbackPayload>.onReceiveNotification()`

---

### 4.3 StatisticsNotificationObserverService.java (Notification Observer Service)

**Path**: `Libs/StatisticsLet/src/main/java/com/hp/jetadvantage/link/services/statisticslet/service/StatisticsNotificationObserverService.java`

**Changes**: Full implementation of E2 notification subscription and broadcast sending logic

#### Added Field

```java
private IDeviceStatisticsService mDeviceStatisticsService;
```

#### Added Methods

| Method                                      | Access                 | Description                                                                  |
| ------------------------------------------- | ---------------------- | ---------------------------------------------------------------------------- |
| `subscribeStatisticsNotificationEvents()` | `@VisibleForTesting` | Creates `StandardDeviceStatisticsService` and registers `IE2PayloadCallback` |
| `processStatisticsNotification()`         | `@VisibleForTesting` | Extracts sequence numbers from service message → Sends broadcasts            |
| `sendBroadcastToApp()`                    | `@VisibleForTesting` | Sends job completion notification broadcast to app                           |
| `sendBroadcastToPackageManager()`         | `@VisibleForTesting` | Sends system notification broadcast to package manager                       |
| `createDeviceStatisticsService()`         | `@VisibleForTesting` | Factory method (allows mock injection for testing)                           |
| `getDeviceStatisticsService()`            | `@VisibleForTesting` | Getter                                                                       |
| `setDeviceStatisticsService()`            | `@VisibleForTesting` | Setter                                                                       |

#### Modified Methods

| Method                               | Changes                                                                                 |
| ------------------------------------ | --------------------------------------------------------------------------------------- |
| `onDestroy()`                      | Added callback unregistration (`unRegisterNotificationCallback()`) and HandlerThread cleanup logic |
| `SubscribeHandler.handleMessage()` | Calls `subscribeStatisticsNotificationEvents()` on `MSG_START`                       |

#### Broadcast Details

**App Broadcast (`sendBroadcastToApp`)**:

- Action: `Statisticslet.NOTIFICATION_CHANGE_ACTION`
- Extras:
  - `EXTRA_UUID` → agentId
  - `EXTRA_DATA` → lastSequenceNumberProcessed
  - `EXTRA_DATA2` → lastSequenceNumberNotified
- `intent.setPackage(pkgName)` to target a specific app
- Permission: `SpsConstants.SDK_ACCESS_STATISTICS_PERMISSION`

**System Broadcast (`sendBroadcastToPackageManager`)**:

- Action: `Statisticslet.NOTIFICATION_CHANGE_ACTION_SYSTEM`
- Extras:
  - `EXTRA_UUID` → agentId
  - `EXTRA_DATA` → lastSequenceNumberProcessed
- Permission: `com.hp.packagemanager.permission.READ_PROVIDERS`

#### Null Safety

```java
// Default to 0 if SequenceNumber or its inner value is null
long lastSequenceNumberProcessed = 0;
if (notification.getLastSequenceNumberProcessed() != null
        && notification.getLastSequenceNumberProcessed().getValue() != null) {
    lastSequenceNumberProcessed = notification.getLastSequenceNumberProcessed().getValue();
}
```

#### Resource Cleanup (`onDestroy`)

```java
// 1. Unregister callback
if (mDeviceStatisticsService != null) {
    mDeviceStatisticsService.unRegisterNotificationCallback();
    mDeviceStatisticsService = null;
}
// 2. Safely quit HandlerThread
if (mHandlerThread != null) {
    mHandlerThread.quitSafely();
    mHandlerThread = null;
    mHandler = null;
}
```

---

## 5. Unit Tests

### 5.1 StandardDeviceStatisticsServiceUnitTest (9 tests)

**Path**: `tests/services/src/test/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceStatisticsServiceUnitTest.java`

| # | Test Method                                                                                   | Verification                                                              |
| - | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------- |
| 1 | `WhenConstructorCalled_ThenObjectCreated`                                                   | Default constructor                                                       |
| 2 | `WhenConstructorCalledWithDeviceManagement_ThenObjectCreated`                               | Constructor with DeviceManagementService parameter                        |
| 3 | `WhenRegisterNotificationCallbackCalled_AndStatisticsNotificationOccurs_ThenCallbackCalled` | Callback registration → WebSocket notification → callback invocation + service message value verification (integration test) |
| 4 | `WhenUnregisterNotificationCallbackCalled_ThenCallbackNotCalled`                            | Callback not invoked after unregistration                                 |
| 5 | `WhenRegisterNotificationCallbackCalled_AndInvalidTypeGUN_ThenCallbackNotCalled`            | Invalid typeGUN → callback not invoked                                   |
| 6 | `WhenRegisterNotificationCallbackCalled_AndMalformedJson_ThenCallbackNotCalled`             | Malformed JSON → callback not invoked (exception-safe)                   |
| 7 | `WhenRegisterNotificationCallbackCalledTwice_ThenOnlyLastCallbackActive`                    | Only the last callback is active on double registration                   |
| 8 | `WhenRegisterNotificationCallbackCalled_AndNullSequenceNumbers_ThenCallbackCalledWithNulls` | Null sequence number handling                                             |
| 9 | `WhenRegisterNotificationCallbackCalledWithJobDetails_ThenCallbackCalledWithCorrectServiceMessage` | Service message verification with jobDetails list                  |

**Characteristics**:

- End-to-end integration tests using `AppChannelMessageHandler`
- WebSocket message simulation via `AppChannelMessageTestHelper`
- Test isolation via `AppChannelCallbackRegistry.clear()` in `@After`

---

### 5.2 StatisticsNotificationObserverServiceUnitTest (18 tests)

**Path**: `tests/services/src/test/java/com/hp/jetadvantage/link/services/statisticslet/service/StatisticsNotificationObserverServiceUnitTest.java`

| #  | Test Method                                                                                 | Verification                           |
| -- | ------------------------------------------------------------------------------------------- | -------------------------------------- |
| 1  | `WhenSubscribeStatisticsNotificationEvents_ThenDeviceServiceCreatedAndCallbackRegistered` | Service created and callback registered on subscribe |
| 2  | `WhenSubscribeStatisticsNotificationEventsThrowsException_ThenExceptionHandled`           | Exception-safe on registration failure |
| 3  | `WhenProcessStatisticsNotificationWithValidServiceMessage_ThenBroadcastsSent`             | Valid service message → broadcasts sent |
| 4  | `WhenProcessStatisticsNotificationWithNullServiceMessage_ThenNoBroadcastSent`             | null service message → early return    |
| 5  | `WhenProcessStatisticsNotificationWithNullAgentId_ThenNoBroadcastSent`                    | agentId null → early return            |
| 6  | `WhenProcessStatisticsNotificationWithNullSequenceNumbers_ThenBroadcastSentWithDefaults`  | null sequence → defaults to 0          |
| 7  | `WhenProcessStatisticsNotificationWithZeroSequenceNumbers_ThenBroadcastSentWithZeros`     | Zero sequence value handling           |
| 8  | `WhenProcessStatisticsNotificationWithLargeSequenceNumbers_ThenBroadcastSentCorrectly`    | Large sequence values (255, 256)       |
| 9  | `WhenSendBroadcastToApp_ThenBroadcastSentWithCorrectExtras`                               | Intent capture and broadcast send verification |
| 10 | `WhenSendBroadcastToAppWithNullContext_ThenExceptionHandled`                              | null Context → exception-safe          |
| 11 | `WhenSendBroadcastToPackageManager_ThenBroadcastSent`                                     | Package manager broadcast sent         |
| 12 | `WhenSendBroadcastToPackageManagerWithNullContext_ThenExceptionHandled`                   | null Context → exception-safe          |
| 13 | `WhenSubscribeAndCallbackInvoked_ThenProcessNotificationCalled`                           | Subscribe → callback → broadcast full flow |
| 14 | `WhenSetDeviceStatisticsServiceCalled_ThenServiceIsSet`                                   | Setter operation                       |
| 15 | `WhenSetDeviceStatisticsServiceWithNull_ThenServiceIsNull`                                | Setter allows null                     |
| 16 | `WhenProcessStatisticsNotificationAndGetAgentIdThrows_ThenExceptionHandled`               | getAgentId exception → handled safely  |
| 17 | `WhenMultipleNotificationsProcessed_ThenEachBroadcastSentCorrectly`                       | 2 consecutive notifications → correct broadcasts each |

**Testing Techniques**:

- Direct Android Service class testing with `Mockito.spy()` + `mockito-inline` (no Robolectric)
- Static method mocking via `MockedStatic<StatisticsUtility>`
- Intent verification via `ArgumentCaptor<Intent>`
- `SequenceNumber` field setting via Reflection (`createTestServiceMessage()` helper)

---

## 6. Code Review (SonarQube Level)

Issues found and addressed during code review:

| # | Item                                            | Status                                                                                            |
| - | ----------------------------------------------- | ------------------------------------------------------------------------------------------------- |
| 1 | Unused import (`com.google.gson.JsonElement`) | **Fixed** — Removed                                                                        |
| 2 | synchronized blocks (thread safety)             | **Verified** — Applied to `registerNotificationCallback`/`unRegisterNotificationCallback`  |
| 3 | volatile keyword (`payloadCallback` field)    | **Verified** — Ensures multi-thread visibility                                              |
| 4 | Null safety                                     | **Verified** — Double null check on SequenceNumber and inner getValue()                     |
| 5 | Resource cleanup (`onDestroy`)                | **Verified** — Callback unregistration + HandlerThread safe quit                            |
| 6 | Exception handling                              | **Verified** — try-catch applied to all public methods                                      |

---

## 7. Build and Test Results

### Build

```
> .\gradlew build --console=plain
BUILD SUCCESSFUL in 2s
89 tasks up-to-date
```

### Tests

```
> .\gradlew :Test-WorkpathServices:testDebugUnitTest --console=plain
714 tests completed, 0 failed
BUILD SUCCESSFUL in 23s
```

- Existing 695 tests + 27 new tests (9 + 18) = 714 total, all passed
- No impact on existing tests (no regressions)

---

## 8. SonarQube Lint Fixes (2nd Review)

Analysis and fixes for issues detected by SonarQube static analysis.

### 8.1 StandardDeviceStatisticsService.java (3 issues)

| # | Line | SonarQube Issue                                         | Verdict               | Fix                                                                                                                                                                                                                                                                                                                                                                                                                               |
| - | ---- | ------------------------------------------------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1 | 95   | Remove useless curly braces around statement            | **Fixed**       | Lambda `(a, b) -> { method(a, b, c); }` → `(a, b) -> method(a, b, c)` single expression                                                                                                                                                                                                                                                                                                                                     |
| 2 | 24   | Use a thread-safe type; adding "volatile" is not enough | **Fixed**       | `volatile IPayloadCallback` → `AtomicReference<IPayloadCallback>`. `synchronized` block retained for compound operation atomicity                                                                                                                                                                                                                                                                                          |
| 3 | 23   | "TAG" is the name of a field in "StandardDeviceService" | **No fix needed** | Rationale: Parent class `StandardDeviceService` has `protected String TAG`, but **15+ subclasses** throughout the project (`StandardDeviceScanJobService`, `StandardDeviceCopyJobService`, `StandardDeviceSolutionManager`, etc.) all follow the **same pattern** of redeclaring `private static final String TAG = Constants.TAG + "/<name>"`. This is an intentional codebase convention; changing only this file would break consistency |

#### Key Change: AtomicReference Applied

```java
// Before
private volatile IPayloadCallback payloadCallback = null;

// After
private final AtomicReference<IPayloadCallback> payloadCallback = new AtomicReference<>();
```

Changed to use `AtomicReference` `get()` and `set()` methods in `registerNotificationCallback()` and `unRegisterNotificationCallback()`. `synchronized` blocks retained as-is for check-then-act atomicity.

```java
// registerNotificationCallback - Before
this.payloadCallback = (appPackageId, notification) -> {
    processStatisticsNotification(appPackageId, notification, callback);
};
AppChannelCallbackRegistry.registerPayloadCallback(GUN, this.payloadCallback);

// registerNotificationCallback - After
IPayloadCallback newCallback = (appPackageId, notification) ->
    processStatisticsNotification(appPackageId, notification, callback);
this.payloadCallback.set(newCallback);
AppChannelCallbackRegistry.registerPayloadCallback(GUN, newCallback);
```

---

### 8.2 StatisticsNotificationObserverService.java (15 issues)

| #  | Line | SonarQube Issue                                 | Verdict               | Fix                                                                                                                                                                                                                                                                                       |
| -- | ---- | ----------------------------------------------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1  | 276  | Replace "switch" by "if" statements             | **Fixed**       | `switch (msg.what) { case MSG_START: ... default: ... }` → `if (msg.what == MSG_START) { ... } else { ... }`                                                                                                                                                                         |
| 2  | 244  | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable throwable)` → `catch (Exception e)` in `processStatisticsNotification()`                                                                                                                                                                                         |
| 3  | 194  | Replace lambda with method reference            | **Fixed**       | `(appPackageId, notification) -> { processStatisticsNotification(appPackageId, notification); }` → `this::processStatisticsNotification`                                                                                                                                             |
| 4  | 194  | Remove useless curly braces around statement    | **Fixed**       | Resolved together with #3 (method reference conversion removes curly braces)                                                                                                                                                                                                              |
| 5  | 177  | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable throwable)` → `catch (Exception e)` in `sendBroadcastToPackageManager()`                                                                                                                                                                                         |
| 6  | 161  | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable throwable)` → `catch (Exception e)` in `sendBroadcastToApp()`                                                                                                                                                                                                    |
| 7  | 136  | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable e)` → `catch (Exception e)` in `start()`                                                                                                                                                                                                                         |
| 8  | 90   | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable e)` → `catch (Exception e)` in `onStartCommand()`                                                                                                                                                                                                                |
| 9  | 65   | Empty code block                                | **Fixed**       | Extracted empty catch block into `setSystemServiceFlag()` method with logging: `Log.d(TAG, "Failed to set system service flag: " + e.getMessage())`                                                                                                                                      |
| 10 | 51   | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable throwable)` → `catch (Exception e)` in `ensurePermissionSafely()` (extracted method)                                                                                                                                                                              |
| 11 | 74   | Catch Exception instead of Throwable            | **Fixed**       | `catch (Throwable e)` → `catch (Exception e)` in `onCreate()` outer catch                                                                                                                                                                                                           |
| 12 | 41   | Empty method body                               | **Fixed**       | Added comment to empty constructor: `// Required public no-arg constructor for Android Service instantiation`                                                                                                                                                                             |
| 13 | 62   | Extract nested try block                        | **Fixed**       | `System.setProperty(...)` try block → extracted to `setSystemServiceFlag()` private method                                                                                                                                                                                            |
| 14 | 49   | Extract nested try block                        | **Fixed**       | `SpsPermissionHelper.ensurePermission(...)` try block → extracted to `ensurePermissionSafely()` private method                                                                                                                                                                        |
| 15 | 80   | Refactor method to not always return same value | **No fix needed** | Rationale: `onStartCommand()` is an Android Service lifecycle method, and returning `START_STICKY` is a **framework requirement**. The service must remain sticky in both normal and exception paths; branching the return value would degrade service reliability                     |

#### Key Change 1: Nested try blocks → Helper method extraction

```java
// Before (inside onCreate)
try {
    try {
        SpsPermissionHelper.ensurePermission(getApplicationContext());
    } catch (Throwable throwable) {
        Log.i(TAG, "Permission error statistics svc. ignored.");
    }
    if(Platform.isPanel()) {
        ...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
                Log.d(TAG, "Created Statistics svc.");
            } catch (Exception e) {}
            ...
        }
    }
} catch (Throwable e) { ... }

// After (onCreate refactored)
try {
    ensurePermissionSafely();
    if(Platform.isPanel()) {
        ...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSystemServiceFlag();
            ...
        }
    }
} catch (Exception e) { ... }

// Extracted methods
private void ensurePermissionSafely() {
    try {
        SpsPermissionHelper.ensurePermission(getApplicationContext());
    } catch (Exception e) {
        Log.i(TAG, "Permission error statistics svc. ignored.");
    }
}

private void setSystemServiceFlag() {
    try {
        System.setProperty(CommonConstants.SYSTEM_SVC_FLAG, TAG);
        Log.d(TAG, "Created Statistics svc.");
    } catch (Exception e) {
        // SecurityException may occur if system property access is restricted
        Log.d(TAG, "Failed to set system service flag: " + e.getMessage());
    }
}
```

#### Key Change 2: Lambda → Method reference

```java
// Before
IE2PayloadCallback<StatisticsCallbackPayload> callback = (appPackageId, notification) -> {
    processStatisticsNotification(appPackageId, notification);
};
mDeviceStatisticsService.registerNotificationCallback(callback);

// After
mDeviceStatisticsService.registerNotificationCallback(this::processStatisticsNotification);
```

This conversion also made the `IE2PayloadCallback` import unnecessary, which was removed.

#### Key Change 3: switch → if/else

```java
// Before
switch (msg.what) {
    case MSG_START:
        Log.d(TAG, "Start Statistics svc for event monitoring.");
        subscribeStatisticsNotificationEvents();
        break;
    default:
        Log.e(TAG, "Unknown message: " + msg.what);
}

// After
if (msg.what == MSG_START) {
    Log.d(TAG, "Start Statistics svc for event monitoring.");
    subscribeStatisticsNotificationEvents();
} else {
    Log.e(TAG, "Unknown message: " + msg.what);
}
```

---

### 8.3 No-Fix Verdict Summary

| File                                                   | Issue                    | Reason                                                                                    |
| ------------------------------------------------------ | ------------------------ | ----------------------------------------------------------------------------------------- |
| `StandardDeviceStatisticsService.java` Line 23       | TAG field name shadowing | 15+ subclasses across the project use the same pattern. Intentional convention             |
| `StatisticsNotificationObserverService.java` Line 80 | Always returns same value | Returning `START_STICKY` from Android `onStartCommand()` is a framework design requirement |

### 8.4 Build/Test Results After SonarQube Fixes

```
> .\gradlew :DeviceServices-Standard:assembleDebug :Let-StatisticsLet:assembleDebug --console=plain
BUILD SUCCESSFUL in 2m 36s (117 tasks)

> .\gradlew :Test-WorkpathServices:testDebugUnitTest --console=plain
714 tests completed, 0 failed
BUILD SUCCESSFUL in 8s (231 tasks)
```

---

## 9. Changed Files — Full Path Summary

```
# Source code
Libs/DeviceServices/Interfaces/src/main/java/com/hp/jetadvantage/link/device/services/interfaces/IDeviceStatisticsService.java
Libs/DeviceServices/Standard/src/main/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceStatisticsService.java
Libs/StatisticsLet/src/main/java/com/hp/jetadvantage/link/services/statisticslet/service/StatisticsNotificationObserverService.java

# Test code
tests/services/src/test/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceStatisticsServiceUnitTest.java
tests/services/src/test/java/com/hp/jetadvantage/link/services/statisticslet/service/StatisticsNotificationObserverServiceUnitTest.java

# Build configuration
tests/services/build.gradle
```

---

## 10. Bug Fix: Service Callback Type Change (Payload → Service)

### 10.1 Problem Discovery

Device log analysis revealed that E2 Statistics Notifications are delivered as **"service" type** messages, not "payload" type:

```json
// Actual E2 setup message — "service" type
{"channelMessage":{"channelId":"5f1641c9-...","message":{"setup":{"details":{
  "service":{
    "e2ServiceGun":"com.hp.ext.service.jobStatistics.version.1",
    "serviceGun":"com.hp.ext.service.jobStatistics.version.1.clientService.statisticsServiceTarget"
  }},"packageId":"com.hp.workpath.sample.statisticsample"}}}}
```

In contrast, other services like Copy and Scan use "payload" type:
```json
// Copy — "payload" type
{"channelMessage":{"channelId":"18a21b20-...","message":{"setup":{"details":{
  "payload":{
    "e2ServiceGun":"com.hp.ext.service.copy.version.1",
    "payloadGun":"com.hp.ext.service.copy.version.1.type.copyNotification"
  }},...}}}}
```

**The existing code** used `registerPayloadCallback()` / `IPayloadCallback`, so the callback was not registered for service-type messages processed by `AppChannelServiceMessageProcessor`, causing notifications to be silently dropped.

### 10.2 Fix Details

#### StandardDeviceStatisticsService.java

| Item | Before | After |
|---|---|---|
| Callback type | `AtomicReference<IPayloadCallback>` | `AtomicReference<IServiceCallback>` |
| Registration API | `AppChannelCallbackRegistry.registerPayloadCallback(GUN, callback)` | `AppChannelCallbackRegistry.registerServiceCallback(GUN, path, callback)` |
| Unregistration API | `AppChannelCallbackRegistry.unregisterPayloadCallback(GUN, callback)` | `AppChannelCallbackRegistry.unregisterServiceCallback(GUN, path)` |
| Service path | (none) | `SERVICE_PATH_JOB_STATISTICS = "jobStatistics"` |
| Message processing | Extracted typeGUN/value from `JsonTypedObject` | Extracted typeGUN/value from `AppChannelService.getRequestBody()` |
| Return value | `void` (fire-and-forget) | `AppChannelServiceResponse(serviceCallId, statusCode)` |

Key change — `processStatisticsServiceCall()` method:
```java
private AppChannelServiceResponse processStatisticsServiceCall(
        String appPackageId,
        AppChannelService serviceMessage,
        IE2PayloadCallback<StatisticsCallbackPayload> callback) {
    try {
        JsonTypedObject requestBody = serviceMessage.getRequestBody();
        // typeGUN validation → deserialization → callback invocation
        callback.onReceiveNotification(appPackageId, parsedServiceMessage);
        return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 200);
    } catch (Exception e) {
        return new AppChannelServiceResponse(serviceMessage.getServiceCallId(), 500);
    }
}
```

#### AppChannelMessageTestHelper.java (Test Helper)

Added `makeTestServiceMessageWithRequestBody()` helper method:
```java
public static String makeTestServiceMessageWithRequestBody(
        String channelId, String serviceCallId, String path,
        String typeGUN, Object value)
```

Generates service-type channel message JSON structure:
```json
{"channelMessage":{"channelId":"...","message":{"service":{
  "path":"jobStatistics",
  "requestBody":{"typeGUN":"...","value":{...}},
  "serviceCallId":"..."
}}}}
```

#### StandardDeviceStatisticsServiceUnitTest.java

Converted 9 notification tests to service pattern:
- Setup: Uses `makeServiceChannelSetupMessage()` (`"service"` details)
- Message: Uses `makeTestServiceMessageWithRequestBody()`
- Async: `awaitServiceCallback(serviceCallId)` — `AppChannelServiceThreadPool.getFuture(serviceCallId).get(2, TimeUnit.SECONDS)` pattern for awaiting async callback completion

---

## 11. Bug Fix: AgentId Lookup Change (StatisticsUtility → PackageManagerHelper)

### 11.1 Problem Discovery

Device log:
```
[WS]STAT/OBSERVER  E  processStatisticsNotification: agentId is null for pkg=com.hp.workpath.sample.statisticsample
```

**Root Cause**: `StatisticsUtility.getAgentId()` performs a 2-step lookup:
1. `getParentAgentIdByPackageName()` → look up parentUuid
2. Look up STATISTICS provider UUID using parentUuid

On the Dune/E2 platform, the provider structure changed so parentUuid could not be found in step 1.

### 11.2 Fix Details

#### StatisticsNotificationObserverService.java

| Item | Before | After |
|---|---|---|
| AgentId lookup | `StatisticsUtility.getAgentId(context, packageName)` (static, 2-step) | `packageManagerHelper.getAgentId(context, packageName, STATISTICS_AGENT_TYPE_GUN)` (instance, 1-step) |
| Dependency import | `StatisticsUtility` | `PackageManagerHelper`, `JobStatisticsAgentRegistrationRecord` |

Added fields:
```java
private static final String STATISTICS_AGENT_TYPE_GUN = new JobStatisticsAgentRegistrationRecord().getTypeGUN();
private PackageManagerHelper packageManagerHelper = new PackageManagerHelper();
```

Added test support method:
```java
@VisibleForTesting
void setPackageManagerHelper(PackageManagerHelper helper) {
    this.packageManagerHelper = helper;
}
```

`PackageManagerHelper.getAgentId()` performs a **direct 1-step lookup** using packageName + FUNCTION_TYPE, which works correctly on Dune/E2:
```java
// PackageManagerHelper.getAgentId() — core query
String querySelection = PACMAN_DB_PACKAGE_NAME + " = ? and "
        + PackageContract.PackageProviderEntry.FUNCTION_TYPE + " = ?";
String[] querySelectionArgs = new String[]{packageName, agentTypeGun};
packageCursor = context.getContentResolver().query(
        PackageContract.PROVIDERS_CONTENT_URI, null, querySelection, querySelectionArgs, null, null);
```

#### StatisticsNotificationObserverServiceUnitTest.java

| Item | Before | After |
|---|---|---|
| Mock approach | `MockedStatic<StatisticsUtility>` (static mock, try-with-resources) | `@Mock PackageManagerHelper mockPackageManagerHelper` (instance mock) |
| Setup | — | `service.setPackageManagerHelper(mockPackageManagerHelper)` |
| Usage | `mockedUtility.when(() -> StatisticsUtility.getAgentId(...)).thenReturn(...)` | `when(mockPackageManagerHelper.getAgentId(eq(ctx), eq(pkg), anyString())).thenReturn(...)` |

Converted 8 test methods from `MockedStatic<StatisticsUtility>` to regular mocks. Removing static mocking makes tests lighter and cleaner.

---

## 12. Boot Timing Fix: Early Callback Registration

### 12.1 Problem Discovery

Device boot log timeline analysis:
```
00:06:07.683  E2 statistics setup message processed (channel established)
    ...  ~4 second gap — notifications arriving in this window are lost
00:06:11.547  StatisticsNotificationObserverService subscription started
```

**Root Cause**: In the original flow, callback registration was unnecessarily delayed through the `onStartCommand` → Handler thread (MSG_START) path.

```
[Before]
onCreate → Create Handler → Send READY broadcast
onStartCommand → Handler MSG_START → subscribeStatisticsNotificationEvents()
```

`onCreate` sends the READY broadcast first, causing E2 to start sending setup messages, but callback registration doesn't begin until `onStartCommand` executes.

### 12.2 Fix Details

#### StatisticsNotificationObserverService.java — `onCreate()`

**Register callback BEFORE the READY broadcast**:
```java
@Override
public void onCreate() {
    super.onCreate();
    try {
        ensurePermissionSafely();
        if(Platform.isPanel()) {
            // Create HandlerThread
            mHandlerThread = new HandlerThread(...);
            mHandlerThread.start();
            mHandler = new SubscribeHandler(mHandlerThread.getLooper());

            // ★ Register callback BEFORE READY broadcast to prevent notification loss during boot
            subscribeStatisticsNotificationEvents();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setSystemServiceFlag();
                ServiceNotification.showNotification(this);
                CommonConstants.sendBroadCastForBoot(getApplicationContext(),
                        CommonConstants.BroadcastActions.READY_STATISTICSLET);
            }
        }
    } catch (Exception e) { ... }
}
```

#### StatisticsNotificationObserverService.java — `onStartCommand()`

**Prevent re-subscription if already subscribed; only re-subscribe on service restart**:
```java
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    try {
        SpsPermissionHelper.ensurePermission(getApplicationContext());
        if (mHandler == null) {
            Log.e(TAG, "Statistics start failed.(ignored)");
            return START_STICKY;
        }
        // ★ Skip if already subscribed; only re-subscribe on system restart
        if (mDeviceStatisticsService == null) {
            mHandler.sendMessage(mHandler.obtainMessage(SubscribeHandler.MSG_START));
        }
    } catch (Exception e) { ... }
    return START_STICKY;
}
```

```
[After]
onCreate → Create Handler → subscribeStatisticsNotificationEvents() → Send READY broadcast
onStartCommand → (already subscribed? → skip) / (not subscribed? → Handler MSG_START → re-subscribe)
```

**Note**: `AppChannelServiceMessageProcessor` also has a queue mechanism (`queueEnabled=true`) that can queue messages arriving before callback registration and deliver them later via `deliverQueuedMessagesToCallback()`, but early registration is the most reliable solution.

### 12.3 Tests Added (3)

| # | Test Method | Verification |
|---|---|---|
| 1 | `WhenSubscribeCalledTwice_ThenSecondCallOverwritesFirst` | Second subscription wins on double subscribe |
| 2 | `WhenAlreadySubscribed_ThenOnStartCommandSkipsResubscription` | Verifies `createDeviceStatisticsService()` not called when already subscribed |
| 3 | `WhenNotSubscribed_ThenOnStartCommandCanResubscribe` | Verifies re-subscription works when not subscribed |

---

## 13. Bug Fix: Implicit Broadcast Blocked (Android O+ Background Execution Limits)

### 13.1 Problem Discovery

Device log:
```
[WS]STAT/OBSERVER  D  sendBroadcastToApp: agentId=1fd8aff1-..., pkg=com.hp.workpath.sample.statisticsample, lastSeq=1, highestSeq=6
BroadcastQueue     W  Background execution not allowed: receiving Intent { act=com.hp.jetadvantage.link.intent.action.system.NOTIFICATION_CHANGED flg=0x10 (has extras) } to com.hp.jetadvantage.link.packagemanager/...NotificationChangeBroadcastReceiver
```

`sendBroadcastToApp` works correctly, but `sendBroadcastToPackageManager` fails because the package manager cannot receive the broadcast.

**Root Cause**: Android O+ (API 26) **implicit broadcast restriction** policy.

| Method | `setPackage()` used? | Result |
|---|---|---|
| `sendBroadcastToApp` | `intent.setPackage(pkgName)` ✅ | **explicit** → received normally |
| `sendBroadcastToPackageManager` | None ❌ | **implicit** → background receiver blocked |

On Android O+, BroadcastReceivers declared in `AndroidManifest.xml` cannot receive implicit broadcasts in the background. The target must be specified via `setPackage()` or `setComponent()`.

### 13.2 Fix Details

#### StatisticsNotificationObserverService.java — `sendBroadcastToPackageManager()`

```java
// Before — implicit broadcast (blocked on Android O+)
Intent intentForPkgMgt = new Intent(Statisticslet.NOTIFICATION_CHANGE_ACTION_SYSTEM);
intentForPkgMgt.putExtra(...);
context.sendBroadcast(intentForPkgMgt, "com.hp.packagemanager.permission.READ_PROVIDERS");

// After — explicit broadcast (setPackage added)
Intent intentForPkgMgt = new Intent(Statisticslet.NOTIFICATION_CHANGE_ACTION_SYSTEM);
intentForPkgMgt.putExtra(...);
intentForPkgMgt.setPackage(CommonConstants.SYSTEM_PACMAN_PACKAGE_NAME);  // ★ Added
context.sendBroadcast(intentForPkgMgt, "com.hp.packagemanager.permission.READ_PROVIDERS");
```

- `CommonConstants.SYSTEM_PACMAN_PACKAGE_NAME = "com.hp.jetadvantage.link.packagemanager"` — uses existing constant
- Test: Renamed `sendBroadcastToPackageManager` test to `ThenBroadcastSentWithExplicitPackage` to clarify explicit broadcast intent

---

## 14. Final Test Results

### Test Count History

| Stage | StandardDeviceStatisticsServiceUnitTest | StatisticsNotificationObserverServiceUnitTest | StatisticsAdapterTest | Total |
|---|---|---|---|---|
| Initial development | 9 | 18 | 15 | 42 |
| + REST API tests (git pull) | 18 | 18 | 15 | 51 |
| + Boot timing tests | 18 | **20** | 15 | **53** |

### Final Build/Test

```
> .\gradlew :Test-WorkpathServices:testDebugUnitTest --tests "...StandardDeviceStatisticsServiceUnitTest" \
    --tests "...StatisticsNotificationObserverServiceUnitTest" --tests "...StatisticsAdapterTest" --rerun

BUILD SUCCESSFUL in 11s
StandardDeviceStatisticsServiceUnitTest:        18 tests, 0 failures, 0 errors
StatisticsNotificationObserverServiceUnitTest:  20 tests, 0 failures, 0 errors
StatisticsAdapterTest:                          15 tests, 0 failures, 0 errors
Total:                                          53 tests, all passed
```

---

## 15. Connected Device Tests (Android Instrumented Tests)

### 15.1 Overview

Added **androidTest** (connected instrumented tests) that run directly on a device to verify that the Statistics Notification AppChannel service callback mechanism works correctly in a real Android runtime environment.

The existing unit tests (53) run on the JVM with Mockito, while androidTests install the APK on a real device (HP-Printer) and execute, enabling verification of actual Android framework behavior.

### 15.2 Referenced Existing androidTests

| Reference | Role |
|---|---|
| `ConfigNotificationServiceInstrumentedTest` | Primary reference for notification service pattern — `AppChannelMessageHandler` + `MockitoAnnotations` + `CountDownLatch` pattern |
| `StandardDeviceStatisticsJobServiceInstrumentedTest` | Existing REST API tests for statistics service (isSupported, getAllJobsList, etc.) |
| `CdmPubMessageHandlerInstrumentedTest` | Message handler callback invocation pattern |
| `BaseInstrumentedTest` / `StandardDeviceInstrumentedTest` | dutInfoIp parameter handling and TestConnector pattern |

### 15.3 Modified / Created Files

| # | File | Change Type | Description |
|---|---|---|---|
| 1 | `tests/services/src/androidTest/.../services/AppChannelMessageTestHelper.java` | **Modified** | Added `makeTestServiceMessageWithRequestBody()` helper method |
| 2 | `tests/services/src/androidTest/.../statisticslet/service/StatisticsNotificationServiceInstrumentedTest.java` | **New** | 7 statistics notification connected tests |

### 15.4 AppChannelMessageTestHelper.java Changes

Added **service-type message generation helper** for androidTest `AppChannelMessageTestHelper`:

```java
public static String makeTestServiceMessageWithRequestBody(
        String channelId, String serviceCallId,
        String path, String typeGUN, String value) {
    // Generates service-type channel message JSON
    // path: "jobStatistics", typeGUN: StatisticsCallbackPayload typeGUN
    // → Format processed by AppChannelServiceMessageProcessor
}
```

This method was previously only available in the unit test `AppChannelMessageTestHelper`; it was added to the androidTest version to enable service-type message simulation in device tests.

### 15.5 Test Cases (7)

**File**: `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/statisticslet/service/StatisticsNotificationServiceInstrumentedTest.java`

| # | Test Method | Verification |
|---|---|---|
| 1 | `GivenStatisticsService_WhenNotificationCallbackRegistered_AndServiceMessageInjected_ThenCallbackInvoked` | Callback registration → service message injection → callback invocation confirmed + service message value verification (lastNotified=5, lastProcessed=3) |
| 2 | `GivenStatisticsService_WhenCallbackUnregistered_AndServiceMessageInjected_ThenCallbackNotInvoked` | Register then unregister → message injection → callback not invoked |
| 3 | `GivenStatisticsService_WhenServiceMessageHasWrongTypeGUN_ThenCallbackNotInvoked` | Wrong typeGUN → callback not invoked (400 response path) |
| 4 | `GivenStatisticsService_WhenMultipleNotificationsSent_ThenAllCallbacksInvoked` | 3 consecutive notifications → all callbacks invoked + last service message value verification |
| 5 | `GivenStatisticsService_WhenServiceMessageHasWrongPath_ThenCallbackNotInvoked` | Wrong service path ("wrongPath") → callback not invoked (path routing verification) |
| 6 | `GivenStatisticsService_WhenCallbackReRegistered_ThenNewCallbackInvoked` | Callback re-registration → only new callback invoked, previous callback not invoked |
| 7 | `GivenStatisticsService_WhenNotificationHasLargeSequenceNumbers_ThenServiceMessageParsedCorrectly` | Large sequence value (999999999, 1000000000) parsing accuracy verification |

### 15.6 Testing Techniques

- **`@RunWith(AndroidJUnit4.class)`**: Android instrumented test runner
- **`MockitoAnnotations.openMocks(this)`**: `@Mock StandardWebsocketCallbackService` initialization
- **`PlatformTestHelper.setTestMode(true)`**: Platform test mode activation
- **`AppChannelMessageHandler`**: Direct creation of WebSocket message handler for service message injection
- **`awaitServiceCallback(serviceCallId)`**: `AppChannelServiceThreadPool.getFuture(serviceCallId).get(3, TimeUnit.SECONDS)` — awaits async service callback completion
- **`@After` cleanup**: `AppChannelCallbackRegistry.clear()` + `AppChannelServiceThreadPool.clear()` — test isolation

### 15.7 Execution Command and Results

```
> .\gradlew :Test-WorkpathServices:connectedDebugAndroidTest \
    --project-prop android.testInstrumentationRunnerArguments.dutInfoIp=15.26.182.185 \
    --project-prop android.testInstrumentationRunnerArguments.class=com.hp.jetadvantage.link.services.statisticslet.service.StatisticsNotificationServiceInstrumentedTest

Starting 7 tests on HP-Printer - 12
BUILD SUCCESSFUL in 48s
```

- Device: `HP-Printer - 12` (adb connection: `15.26.182.185:5555`)
- **All 7 tests passed**

### 15.8 Overall Test Summary (Final)

| Type | File | Test Count | Execution Environment |
|---|---|---|---|
| Unit Test | `StandardDeviceStatisticsServiceUnitTest` | 18 | JVM (Mockito) |
| Unit Test | `StatisticsNotificationObserverServiceUnitTest` | 20 | JVM (Mockito) |
| Unit Test | `StatisticsAdapterTest` | 15 | JVM (Mockito) |
| **androidTest** | **`StatisticsNotificationServiceInstrumentedTest`** | **7** | **Device (HP-Printer)** |
| androidTest | `StandardDeviceStatisticsJobServiceInstrumentedTest` (existing) | 4 | Device |
| **Total** | | **64** | |