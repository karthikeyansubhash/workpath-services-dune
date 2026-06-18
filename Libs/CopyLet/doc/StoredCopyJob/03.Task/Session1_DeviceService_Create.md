# Session 1: Device Service Layer + Create Stored Copy Job

> **StoredCopyJob Development — Session 1/5**
> **Phase 1 (Device Service Layer) + Phase 2 (Create Stored Copy Job)**
> **Reference Document:** `StoredCopyJob_DevelopmentPlan_Final.md` Section 5.1, 6, 7.1, 8 Phase 1-2

---

## 1. Session Goal

In this session, we implement the foundational infrastructure for StoredCopyJob (Device Service interface + implementation) and the complete Create Stored Copy Job flow.

**Completion Criteria:**
- Add 3 stored job related methods to DeviceService (enumerate, release, delete)
- Complete Create Stored Copy Job flow working (App submit → ScanJob creation → Monitoring)
- `./gradlew :DeviceServices-Interfaces:assembleDebug` build succeeds
- `./gradlew :DeviceServices-Standard:assembleDebug` build succeeds
- `./gradlew :Let-CopyLet:assembleDebug` build succeeds

---

## 2. Work Order and Detailed Instructions

### Step 1: IDeviceCopyJobService.java — Add 3 Interface Methods

**File:** `Libs/DeviceServices/Interfaces/src/main/java/com/hp/jetadvantage/link/device/services/interfaces/IDeviceCopyJobService.java`
**Type:** UPDATE

**Current Status:** Existing methods — isSupported, getDefaultOptions, getProfile, createCopyJob, getCopyJob, cancelCopyJob, register/unregisterNotificationCallback

**3 Methods to Add:**

```java
/**
 * Enumerate stored copy jobs on the device.
 * E2 endpoint: GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
 *
 * @param packageName The package name of the calling application.
 * @return StoredJobs object containing the list of stored jobs, or null if not supported.
 */
StoredJobs enumerateStoredJobs(String packageName);

/**
 * Release a stored copy job for printing.
 * E2 endpoint: POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}/release
 *
 * @param packageName The package name of the calling application.
 * @param storedJobId The ID of the stored job to release.
 * @param copyOptions The copy options to apply during release (print options + overrides).
 * @return CopyJob created by the release operation, or null on failure.
 */
CopyJob releaseStoredJob(String packageName, String storedJobId, CopyOptions copyOptions);

/**
 * Delete a stored copy job from the device.
 * E2 endpoint: DELETE /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}
 *
 * @param packageName The package name of the calling application.
 * @param storedJobId The ID of the stored job to delete.
 */
void deleteStoredJob(String packageName, String storedJobId);
```

**Required imports to add:**
- `com.hp.ext.service.copy.StoredJobs` (★ Verify existence in E2 library)
- `com.hp.ext.service.copy.CopyOptions` (★ Verify existence in E2 library)

> ⚠️ **E2 Type Verification:** Verify that the `StoredJobs` and `CopyOptions` classes exist in the `com.hp.ext.service.copy` package first.
> If they don't exist, refer to the E2 API documentation and use alternative types or create POJOs directly.
> (See Final Document Section 9: E2 Library Pre-check Checklist)

---

### Step 2: StandardDeviceCopyJobService.java — Implement E2 REST Calls

**File:** `Libs/DeviceServices/Standard/src/main/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceCopyJobService.java`
**Type:** UPDATE

**Existing Pattern Reference:** `createCopyJob()` method — `CopyServiceClientImpl` + `copyAgents().getMember(id)` chain

**3 Methods to Implement (following existing patterns):**

```java
@Override
public StoredJobs enumerateStoredJobs(String packageName) {
    E2call<StoredJobs> call = () -> {
        CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
        // ★ Verify: whether client.copyAgents().getMember(id).storedJobs() method chain exists
        // If exists: client.copyAgents().getMember(id).storedJobs().getAsync(getSolutionToken(packageName)).get()
        // If not: direct REST call (GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers)
        return client.copyAgents().getMember(getAgentId(packageName)).storedJobs()
                .getAsync(getSolutionToken(packageName)).get();
    };
    return perform(call);
}

@Override
public CopyJob releaseStoredJob(String packageName, String storedJobId, CopyOptions copyOptions) {
    E2callBiParam<CopyJob, String, String> call = (String agentId, String jobId) -> {
        CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
        // ★ Verify: storedJobs().getMember(jobId).release() method chain
        return client.copyAgents().getMember(agentId).storedJobs().getMember(jobId)
                .release().executeAsync(getUiContextToken(packageName), copyOptions).get();
    };
    return perform(call, getAgentId(packageName), storedJobId);
}

@Override
public void deleteStoredJob(String packageName, String storedJobId) {
    E2callBiParam<Void, String, String> call = (String agentId, String jobId) -> {
        CopyServiceClient client = new CopyServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
        // ★ Verify: storedJobs().getMember(jobId).deleteAsync() pattern
        client.copyAgents().getMember(agentId).storedJobs().getMember(jobId)
                .deleteAsync(getSolutionToken(packageName)).get();
        return null;
    };
    perform(call, getAgentId(packageName), storedJobId);
}
```

> ⚠️ **E2 Client method chain verification is critical.**
> If the `CopyServiceClient` → `copyAgents()` → `getMember()` → `storedJobs()` chain does not exist,
> you must implement direct REST calls using the HTTP client from `StandardDeviceService`.
> Reference: Also refer to `StandardDeviceScanJobService`'s E2 call patterns.

**Build Verification:**
```bash
./gradlew :DeviceServices-Interfaces:assembleDebug
./gradlew :DeviceServices-Standard:assembleDebug
```

---

### Step 3: StoredCopyJobPreferenceStorage.java — Create New File

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/StoredCopyJobPreferenceStorage.java`
**Type:** NEW

**Full Implementation (Final Document Section 6.3):**

```java
package com.hp.jetadvantage.link.services.copylet.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences-based storage for Stored Copy Job attributes.
 * Stores the original CopyAttributes JSON keyed by scanJobId,
 * so that Release can restore the original scan/print options.
 *
 * Thread safety: CopyJobIntentService processes on a single HandlerThread,
 * so no concurrent access occurs.
 */
public class StoredCopyJobPreferenceStorage {
    private static final String PREF_FILE_NAME = "stored_copy_job_map";

    public static void save(Context context, String key, String copyAttributesJson) {
        getPrefs(context).edit().putString(key, copyAttributesJson).apply();
    }

    public static String get(Context context, String key) {
        return getPrefs(context).getString(key, null);
    }

    public static void remove(Context context, String key) {
        getPrefs(context).edit().remove(key).apply();
    }

    public static void replaceKey(Context context, String oldKey, String newKey) {
        SharedPreferences prefs = getPrefs(context);
        String value = prefs.getString(oldKey, null);
        if (value != null) {
            prefs.edit().remove(oldKey).putString(newKey, value).apply();
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}
```

---

### Step 4: CopyOptionProfileAdapter.java — Add STORE Mode

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyOptionProfileAdapter.java`
**Type:** UPDATE (1 line added)

**Location:** Inside `createCopyAttributeCapsBuilder()` method, directly below the existing `addJobExecutionMode(NORMAL)`:

```java
// Current:
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);

// After Change:
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);
```

---

### Step 5: StoredCopyJobAdapter.java — Create New File

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/StoredCopyJobAdapter.java`
**Type:** NEW

**Core Functionality:** CopyAttributes → ScanTicket(jobStorage destination) conversion + E2 ScanJob creation

**Implementation Reference:**
- Final Document Section 6.1 (pseudocode)
- Final Document Section 5.1.2 (CopyAttributes option classification table — use only fields marked ✅ for Create)
- `ScanTicketAdapter.getScanTicket()` (ScanLet) — ScanOptions setter patterns
- `CopyJobAdapter.getCopyJobTicket()` — CopyTypeMapping E2 type reference
- `CopyJobAdapter.getCopyJobTicket()` L66-86 — storeJobPasswordType, storeJobPassword, retentionMode fields (note: currently commented out)

**CopyAttributes → ScanOptions Mapping (only Create=✅ from Section 5.1.2):**

| CopyAttributes | ScanOptions setter | CopyTypeMapping |
|----------------|-------------------|-----------------|
| scanSize | setMediaSize / setInputMediaSize | originalMediaSize |
| scanSource | setMediaSource / setInputSource | originalMediaSource |
| scanDuplex | setPlexMode + setBindingFormat | originalPlexMode |
| colorMode | setColorMode | colorMode |
| contentType | setContentType | contentType |
| contentOrientation | setContentOrientation | contentOrientation |
| copyPreview | setImagePreviewMode | imagePreviewMode |
| captureMode | setScanCaptureMode | scanCaptureMode |
| progressDialogMode | setScanProgressMode | scanProgressMode |

**Additional ScanTicket Settings:**
- name = `copyAttributes.getStoreJobName()`
- folderName = `copyAttributes.getStoreJobFolderName()`
- DestinationOptions.jobStorage = `new JobStorageDestination()`
- credentials: storeJobPasswordType, storeJobPassword (if password-protected)
- retention: retentionModeOnPowerCycle, retentionModeOnRelease

> ⚠️ **ScanOptions setter name verification required:** Verify the actual setter names in the `com.hp.ext.service.scanJob.ScanOptions` class.
> Refer to `ScanTicketAdapter.getScanTicket()` (ScanLet) for the exact setter names.

---

### Step 6: MonitoringStoredCopyJobState.java — Create New File

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/MonitoringStoredCopyJobState.java`
**Type:** NEW

**Implementation Reference:**
- Final Document Section 6.2 (Design + MonitoringCopyJobState comparison table)
- `MonitoringCopyJobState.java` — Existing Copy monitoring pattern (convert this file to ScanNotification version)
- `MonitoringScanJobState.java` (ScanLet) — ScanNotification handling pattern

**Key Differences (vs MonitoringCopyJobState):**

| Item | MonitoringCopyJobState | MonitoringStoredCopyJobState |
|------|----------------------|----------------------------|
| Device Service | `IDeviceCopyJobService` | `IDeviceScanJobService` |
| Notification | `CopyNotification` | `ScanNotification` |
| Status | `CopyJobStatus` | `ScanJobStatus` |
| Output JobType | COPY | COPY (same — App compatibility) |
| imagesScanned | ✅ | ✅ |
| sheetsPrinted | ✅ | ❌ (scan phase only) |

**Key Implementation Details:**
1. `extends MonitoringJobState` (STATE_NAME automatically inherited → no possibleNextStates change needed)
2. `registerNotificationCallback()` — ScanNotification → CopyJobState conversion
3. `unregisterNotificationCallback()` — IDeviceScanJobService.unRegisterNotificationCallback()
4. Extract `ScanJobStatus` from ScanNotification → update CopyJobState/CopyJobData
5. Reuse `processJobDoneStatus()` (inherited from parent MonitoringJobState)

---

### Step 7: CreatingCopyJobState.java — Add STORE Branch

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobState.java`
**Type:** UPDATE

**Change 1: Add STORE branch to processCopyJobIntent()**

Currently `processCopyJobIntent()` processes all submissions as standard copy.
Check `getJobExecutionMode()` from `CopyAttributesReader` and branch to `processStoredCopyJobIntent()` when STORE:

```java
// Add inside processCopyJobIntent():
CopyAttributesReader copyAttributesReader = new CopyAttributesReader(extraParams);
if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
    return processStoredCopyJobIntent(stateMachine, extraParams, copyAttributesReader);
}
// Continue with existing standard copy logic...
```

**Change 2: Add new processStoredCopyJobIntent() method**

Following the Final Document Section 5.1.1 sequence:

```java
private BaseJobIntentServiceState processStoredCopyJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams,
        CopyAttributesReader copyAttributesReader) throws Exception {

    BaseCopyRequestIntent.IntentParams reqParams = CopyToRequestIntent.getIntentParams(extraParams);
    int clientVersion = reqParams.getApiLevel();
    String packageName = reqParams.getPackageName();

    // [1] createCopyJobBundle — mode=STORE automatically set (L151: setJobExecutionMode(reader.getJobExecutionMode()))
    stateMachine.setJobBundle(createCopyJobBundle(clientVersion, copyAttributesReader));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(), packageName));

    // [2] Save to PreferenceStorage (RID → copyAttributesJson)
    String rid = reqParams.getReqId();
    String copyAttributesJson = /* CopyAttributes JSON serialization */;
    StoredCopyJobPreferenceStorage.save(stateMachine.getContext(), rid, copyAttributesJson);

    // [3] Create IDeviceScanJobService (CreatingCopyJobState only has IDeviceCopyJobService)
    IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();

    // [4] Create ScanJob (convert CopyAttrs → ScanTicket then E2 call)
    String scanJobId = StoredCopyJobAdapter.createScanJobForStorage(
            scanJobService, packageName, copyAttributesReader);

    // [5] Failure handling
    if (scanJobId == null || scanJobId.isEmpty()) {
        StoredCopyJobPreferenceStorage.remove(stateMachine.getContext(), rid);
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                "Failed to create scan job for storage");
    }

    // [6] Replace PreferenceStorage key (RID → scanJobId)
    StoredCopyJobPreferenceStorage.replaceKey(stateMachine.getContext(), rid, scanJobId);

    // [7] Set jobId on StateMachine
    stateMachine.setJobId(scanJobId);

    // [8] Transition to MonitoringStoredCopyJobState
    return new MonitoringStoredCopyJobState(scanJobId);
}
```

**Required imports to add:**
```java
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.copylet.adapter.StoredCopyJobAdapter;
```

---

### Step 8: Build Verification

```bash
# 1. DeviceServices build
./gradlew :DeviceServices-Interfaces:assembleDebug
./gradlew :DeviceServices-Standard:assembleDebug

# 2. CopyLet build
./gradlew :Let-CopyLet:assembleDebug
```

Fix build errors immediately and rebuild.

---

## 3. Considerations and Pitfalls

1. **E2 Type Verification:** Always verify first whether E2 types like `StoredJobs`, `CopyOptions`, `ScanOptions`, `DestinationOptions`, `JobStorageDestination` actually exist (Section 9 Checklist)
2. **ScanOptions setter names:** Refer to `ScanTicketAdapter.getScanTicket()` (ScanLet) for exact setter names
3. **No possibleNextStates change needed:** MonitoringStoredCopyJobState inherits STATE_NAME as "MonitoringJobState" and passes automatically (Section 4.2)
4. **mode=STORE is set automatically:** Handled at createCopyJobBundle() L151 — no separate code needed (Section 2.4)
5. **IDeviceScanJobService is not in CopyLet:** Create `new StandardDeviceScanJobService()` directly inside processStoredCopyJobIntent()

## 4. Created/Modified Files Checklist

| # | File | Type | Step |
|---|------|------|------|
| 1 | `IDeviceCopyJobService.java` | UPDATE | Step 1 |
| 2 | `StandardDeviceCopyJobService.java` | UPDATE | Step 2 |
| 3 | `StoredCopyJobPreferenceStorage.java` | NEW | Step 3 |
| 4 | `CopyOptionProfileAdapter.java` | UPDATE (+1 line) | Step 4 |
| 5 | `StoredCopyJobAdapter.java` | NEW | Step 5 |
| 6 | `MonitoringStoredCopyJobState.java` | NEW | Step 6 |
| 7 | `CreatingCopyJobState.java` | UPDATE | Step 7 |

## 5. Prompt to Request from User After Session Completion

```
Referring to StoredCopyJob_DevelopmentPlan_Final.md, please implement
Phase 3 (Enumerate) + Phase 4 (Release) according to Session2_Enumerate_Release.md.
The Device Service interface and Create flow from Session 1 are already complete.
Please build and fix any build errors.
```
