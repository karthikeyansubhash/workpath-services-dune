# Job Statistics Notification — Final Changes Summary

---

## English

### Overview

Added E2 WebSocket-based Job Statistics Notification support to the StatisticsLet library for the Dune (E2) platform. The 3rd-party SDK API (`Statisticslet` class) was not modified; only internal implementation was changed.

---

### Notification Flow (Final)

```
E2 Statistics Agent  ──WebSocket──>  AppChannelCallbackRegistry
                                            │
                                    IServiceCallback (internal)
                                            │
                                    StandardDeviceStatisticsService
                                        ├── typeGUN validation
                                        ├── JSON deserialization (CustomObjectMapper)
                                        └── IE2PayloadCallback<StatisticsCallbackPayload>
                                                │
                                    StatisticsNotificationObserverService
                                        ├── processStatisticsNotification()
                                        └── sendBroadcastToApp() → app manager
```

---

### 1. IDeviceStatisticsService.java — Added Methods

```java
void registerNotificationCallback(IE2PayloadCallback<StatisticsCallbackPayload> callback);
void unRegisterNotificationCallback();
```

- Only one callback per instance is allowed (last registration wins)
- `unRegisterNotificationCallback()` must be called on cleanup to prevent memory leaks

---

### 2. StandardDeviceStatisticsService.java — Key Changes

| Item | Value |
|---|---|
| Callback type | `AtomicReference<IServiceCallback>` (thread-safe) |
| Registration API | `AppChannelCallbackRegistry.registerServiceCallback(GUN, path, callback)` |
| Unregistration API | `AppChannelCallbackRegistry.unregisterServiceCallback(GUN, path)` |
| Service path | `SERVICE_PATH_JOB_STATISTICS = "jobStatistics"` |
| Message processing | `AppChannelService.getRequestBody()` → typeGUN validation → JSON deserialization → callback |

> **Note**: E2 Statistics Agent delivers notifications as **"service" type** (not "payload" type like Copy/Scan), so `registerServiceCallback` is used instead of `registerPayloadCallback`.

---

### 3. StatisticsNotificationObserverService.java — Key Changes

#### 3.1 Added Fields

```java
private static final String STATISTICS_AGENT_TYPE_GUN =
        new JobStatisticsAgentRegistrationRecord().getTypeGUN();
private PackageManagerHelper packageManagerHelper = new PackageManagerHelper();
```

#### 3.2 Notification Subscription — Early Registration in `onCreate()`

Callback is registered **before** the READY broadcast to prevent notification loss during boot.

```java
@Override
public void onCreate() {
    ...
    // Register BEFORE READY broadcast
    subscribeStatisticsNotificationEvents();
    ...
    CommonConstants.sendBroadCastForBoot(..., READY_STATISTICSLET);
}
```

#### 3.3 `onStartCommand()` — Prevents Duplicate Subscription

```java
if (mDeviceStatisticsService == null) {
    mHandler.sendMessage(mHandler.obtainMessage(SubscribeHandler.MSG_START));
} else {
    Log.i(TAG, "Already subscribed to statistics notifications.");
}
```

#### 3.4 AgentId Lookup — 1-Step Direct Query

Changed from `StatisticsUtility.getAgentId()` (2-step, broken on Dune/E2) to `PackageManagerHelper.getAgentId()` (1-step, direct package query by packageName + FUNCTION_TYPE).

#### 3.5 `sendBroadcastToApp()` — App Broadcast

```java
Intent intent = new Intent(Statisticslet.NOTIFICATION_CHANGE_ACTION);
intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
intent.putExtra(EXTRA_UUID, agentId);
intent.putExtra(EXTRA_DATA, lastSequenceNumberProcessed);
intent.putExtra(EXTRA_DATA2, (int) lastSequenceNumberNotified);
intent.setPackage(pkgName);  // explicit broadcast
context.sendBroadcast(intent, SDK_ACCESS_STATISTICS_PERMISSION);
```

#### 3.6 Removed`sendBroadcastToPackageManager()` — System Broadcast

#### 3.7 `onDestroy()` — Resource Cleanup

```java
if (mDeviceStatisticsService != null) {
    mDeviceStatisticsService.unRegisterNotificationCallback();
    mDeviceStatisticsService = null;
}
if (mHandlerThread != null) {
    mHandlerThread.quitSafely();
    mHandlerThread = null;
    mHandler = null;
}
```

---

### 4. Test Files

| File | Type | Count | Environment |
|---|---|---|---|
| `StandardDeviceStatisticsServiceUnitTest.java` | Unit | 18 | JVM (Mockito) |
| `StatisticsNotificationObserverServiceUnitTest.java` | Unit | 20 | JVM (Mockito) |
| `StatisticsNotificationServiceInstrumentedTest.java` | Instrumented | 7 | Device (HP-Printer) |

**Testing techniques**:
- `Mockito.spy()` + `mockito-inline` — Android Service direct testing without Robolectric
- `@Mock PackageManagerHelper` — instance mock (replaces former `MockedStatic<StatisticsUtility>`)
- `ArgumentCaptor<Intent>` — broadcast Intent verification
- `AppChannelMessageHandler` + `makeTestServiceMessageWithRequestBody()` — service-type WebSocket message simulation

---

### 5. Build Config

`tests/services/build.gradle` — Added `testImplementation project(':Let-StatisticsLet')`

---

### 6. Final Test Results

```
Unit Tests (JVM):
  StandardDeviceStatisticsServiceUnitTest:        18 tests, 0 failures
  StatisticsNotificationObserverServiceUnitTest:  20 tests, 0 failures
  StatisticsAdapterTest:                          15 tests, 0 failures

Instrumented Tests (Device):
  StatisticsNotificationServiceInstrumentedTest:   7 tests, 0 failures
  StandardDeviceStatisticsJobServiceInstrumentedTest: 4 tests, 0 failures

Total: 64 tests, all passed
```

---
---

## 한국어

### 개요

StatisticsLet 라이브러리에 Dune (E2) 플랫폼용 E2 WebSocket 기반 Job Statistics Notification 기능을 추가했습니다. 3rd-party 개발자용 SDK API(`Statisticslet` 클래스)는 수정하지 않고, 내부 구현만 변경했습니다.

---

### 알림 흐름 (최종)

```
E2 Statistics Agent  ──WebSocket──>  AppChannelCallbackRegistry
                                            │
                                    IServiceCallback (내부)
                                            │
                                    StandardDeviceStatisticsService
                                        ├── typeGUN 검증
                                        ├── JSON 역직렬화 (CustomObjectMapper)
                                        └── IE2PayloadCallback<StatisticsCallbackPayload>
                                                │
                                    StatisticsNotificationObserverService
                                        ├── processStatisticsNotification()
                                        └── sendBroadcastToApp()            → 앱
```

---

### 1. IDeviceStatisticsService.java — 메서드 추가

```java
void registerNotificationCallback(IE2PayloadCallback<StatisticsCallbackPayload> callback);
void unRegisterNotificationCallback();
```

- 인스턴스당 콜백 하나만 등록 가능 (마지막 등록이 유효)
- 사용 종료 시 반드시 `unRegisterNotificationCallback()` 호출 필요 (메모리 누수 방지)

---

### 2. StandardDeviceStatisticsService.java — 주요 변경

| 항목 | 값 |
|---|---|
| 콜백 타입 | `AtomicReference<IServiceCallback>` (스레드 안전) |
| 등록 API | `AppChannelCallbackRegistry.registerServiceCallback(GUN, path, callback)` |
| 해제 API | `AppChannelCallbackRegistry.unregisterServiceCallback(GUN, path)` |
| 서비스 경로 | `SERVICE_PATH_JOB_STATISTICS = "jobStatistics"` |
| 메시지 처리 | `AppChannelService.getRequestBody()` → typeGUN 검증 → JSON 역직렬화 → 콜백 호출 |

> **주의**: E2 Statistics Agent는 알림을 **"service" 타입**으로 전달합니다 (Copy/Scan의 "payload" 타입과 다름). 따라서 `registerPayloadCallback` 대신 `registerServiceCallback`을 사용해야 합니다.

---

### 3. StatisticsNotificationObserverService.java — 주요 변경

#### 3.1 추가된 필드

```java
private static final String STATISTICS_AGENT_TYPE_GUN =
        new JobStatisticsAgentRegistrationRecord().getTypeGUN();
private PackageManagerHelper packageManagerHelper = new PackageManagerHelper();
```

#### 3.2 알림 구독 — `onCreate()`에서 조기 등록

READY broadcast 전에 콜백을 등록하여 부팅 시 알림 유실을 방지합니다.

```java
@Override
public void onCreate() {
    ...
    // READY broadcast 전에 콜백 등록
    subscribeStatisticsNotificationEvents();
    ...
    CommonConstants.sendBroadCastForBoot(..., READY_STATISTICSLET);
}
```

#### 3.3 `onStartCommand()` — 중복 구독 방지

```java
if (mDeviceStatisticsService == null) {
    mHandler.sendMessage(mHandler.obtainMessage(SubscribeHandler.MSG_START));
} else {
    Log.i(TAG, "Already subscribed to statistics notifications.");
}
```

#### 3.4 AgentId 조회 — 1단계 직접 쿼리

`StatisticsUtility.getAgentId()` (2단계 조회, Dune/E2에서 동작 안 함)를 `PackageManagerHelper.getAgentId()` (packageName + FUNCTION_TYPE으로 직접 1단계 조회)로 변경했습니다.

#### 3.5 `sendBroadcastToApp()` — 앱 브로드캐스트

```java
Intent intent = new Intent(Statisticslet.NOTIFICATION_CHANGE_ACTION);
intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
intent.putExtra(EXTRA_UUID, agentId);
intent.putExtra(EXTRA_DATA, lastSequenceNumberProcessed);
intent.putExtra(EXTRA_DATA2, (int) lastSequenceNumberNotified);
intent.setPackage(pkgName);  // explicit broadcast
context.sendBroadcast(intent, SDK_ACCESS_STATISTICS_PERMISSION);
```

#### 3.6 Removed `sendBroadcastToPackageManager()` — 시스템 브로드캐스트

#### 3.7 `onDestroy()` — 리소스 정리

```java
if (mDeviceStatisticsService != null) {
    mDeviceStatisticsService.unRegisterNotificationCallback();
    mDeviceStatisticsService = null;
}
if (mHandlerThread != null) {
    mHandlerThread.quitSafely();
    mHandlerThread = null;
    mHandler = null;
}
```

---

### 4. 테스트 파일

| 파일 | 종류 | 테스트 수 | 실행 환경 |
|---|---|---|---|
| `StandardDeviceStatisticsServiceUnitTest.java` | 단위 | 18 | JVM (Mockito) |
| `StatisticsNotificationObserverServiceUnitTest.java` | 단위 | 20 | JVM (Mockito) |
| `StatisticsNotificationServiceInstrumentedTest.java` | 연결 | 7 | 디바이스 (HP-Printer) |

**테스트 기법**:
- `Mockito.spy()` + `mockito-inline` — Robolectric 없이 Android Service 직접 테스트
- `@Mock PackageManagerHelper` — 인스턴스 mock (기존 `MockedStatic<StatisticsUtility>` 대체)
- `ArgumentCaptor<Intent>` — broadcast Intent 검증
- `AppChannelMessageHandler` + `makeTestServiceMessageWithRequestBody()` — service 타입 WebSocket 메시지 시뮬레이션

---

### 5. 빌드 설정

`tests/services/build.gradle` — `testImplementation project(':Let-StatisticsLet')` 추가

---

### 6. 최종 테스트 결과

```
단위 테스트 (JVM):
  StandardDeviceStatisticsServiceUnitTest:        18개, 0 실패
  StatisticsNotificationObserverServiceUnitTest:  20개, 0 실패
  StatisticsAdapterTest:                          15개, 0 실패

연결 테스트 (디바이스):
  StatisticsNotificationServiceInstrumentedTest:   7개, 0 실패
  StandardDeviceStatisticsJobServiceInstrumentedTest: 4개, 0 실패

합계: 64개, 전체 통과
```
