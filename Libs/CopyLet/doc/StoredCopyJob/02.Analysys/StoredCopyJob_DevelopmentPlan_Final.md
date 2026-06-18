# StoredCopyJob Final Development Plan (Plan 1 + Plan 2 Integration)

> **DUNE-169955: Workpath Stored Copy Job**
> This document compares the 1st development plan (StoredCopyJob_DevelopmentPlan.md) and the 2nd cross-validation plan (StoredCopyJob_DevelopmentPlan_2.md), integrating differences and enhancements into the final development plan.

---

## 1. Plan 1 vs Plan 2 Comparison Analysis

### 1.1 Key Differences Summary

| # | Item | Plan 1 | Plan 2 | Final Decision |
|---|------|--------|--------|-----------|
| 1 | Architecture diagram inconsistency analysis | Not mentioned | Detailed analysis of sequence (CopyService→ScanService delegation) vs component (CopyLet internal only) differences | **Adopt Plan 2** — Explicitly state inconsistency, process internally in CopyLet per Option 2 principle |
| 2 | Delete ResultReceiver disconnection issue | Returns only `EndState()`, ResultReceiver.send() not implemented | Extract ResultReceiver from Intent and call send() directly | **Adopt Plan 2** — Without it, ContentProvider 60-second timeout |
| 3 | Processing Delete with asynchronous lifecycle | Implicitly passes through state machine | Delete is a synchronous single operation → Monitoring unnecessary | **Adopt Plan 2** — Return result immediately after E2 DELETE |
| 4 | CopyOptionProfileAdapter update | Not mentioned | Need to add `addJobExecutionMode(STORE)` | **Adopt Plan 2** — Without it, App's StoreCopyBuilder.build(caps) fails |
| 5 | Existing code TODO detailed analysis table | Not implemented ✅/❌ list (summary) | Detailed table with 7 items showing location/current state/required work | **Enhanced by Plan 2** — More precise |
| 6 | Standard Copy flow analysis | Summary (1 level) | 11-step detailed analysis (method call chain) | **Enhanced by Plan 2** — Higher reference value for implementation |
| 7 | CopyAttributes scan/print option classification | Only scan option list enumerated | Scan/print/both classification + Create/Release mapping table | **Enhanced by Plan 2** — Clear classification criteria |
| 8 | Type mapping strategy | "Reuse CopyTypeMapping" mentioned | E2 common type rationale + explicit note to verify ScanOptions setters | **Enhanced by Plan 2** |
| 9 | CopyJobCompletedState STORE default value | Not mentioned | mode==null→STORE logic may affect Create STORE | **Enhanced by Plan 2** — Defend with explicit mode setting |
| 10 | Risk factors and mitigation | Only dependency verification items | Table with 5 risk scenarios with impact/mitigation | **Enhanced by Plan 2** |
| 11 | E2 library pre-verification checklist | 3-item table | 9-item detailed table (verification method + alternative) | **Enhanced by Plan 2** |

### 1.2 Items in Plan 1 But Not in Plan 2

| # | Item | Plan 1 Content | Final Handling |
|---|------|------------|-----------|
| 1 | Existing infrastructure ✅/❌ classification | 13 completed + 11 not implemented items list | **Adopt** — Useful for understanding current state |
| 2 | Detailed processReleaseJobIntent implementation | Pseudocode including error handling, clientVersion, null checks | **Adopt** — Higher implementation completeness |
| 3 | StateMachine possibleNextStates check | Mentioned need to allow MonitoringStoredCopyJobState | **Verified** — No changes needed due to STATE_NAME inheritance (Section 4.2) |
| 4 | Detailed Create/Release ASCII flowcharts | Detailed flow per component (7.1, 7.2) | **Adopt** — Valuable as implementation guide |
| 5 | E2 response data type verification table | StoredJobs, StoredJobMember package verification | **Adopt** — Integrated with Plan 2's checklist |

### 1.3 Items Common to Both Plans

- **Architecture Direction:** Option 2 (Centralized Logic) — Internal CopyLet processing
- **3 NEW files:** StoredCopyJobAdapter, MonitoringStoredCopyJobState, StoredCopyJobPreferenceStorage
- **5 Common UPDATE files:** IDeviceCopyJobService, StandardDeviceCopyJobService, CreatingCopyJobState, CopyJobAdapter, OXPCopyletContentProvider
- **Development Phase structure:** 6 stages (DeviceService → Create → Enumerate → Release → Delete → Testing)
- **SharedPreferences persistence** pattern
- **Release → Reuses existing MonitoringCopyJobState**
- **CopyTypeMapping E2 type reuse**

---

## 2. Key Enhancements (Important Issues Found in Plan 2)

### 2.1 Architecture Diagram Inconsistency Resolution

**Problem:** StoredCopyJob_Create.puml shows `CopyService → ScanService : Send "Scan to JobStorage" JobIntent` delegation, but StoredCopyJob_Overview.md Section 4.1's component definition lists only CopyLet internal changes and ScanLet is not a change target.

**Rationale:**
1. Option 2 description: "The Copy Service manages the workflow by **adding a new job state machine** for 'Scan to JobStorage'"
2. Option 2 Cons: "**Code duplication**: Scan job state machine logic is **repeated in both Scan and Copy Services**"
3. ScanLet's `CreatingScanJobState.createJobBundle()` has `JobType.SCAN` hardcoded — cannot support COPY type
4. ScanLet's `MonitoringScanJobState` uses `ScanJobState`/`ScanJobData` — `CopyJobData` conversion not supported

**Conclusion:** CopyLet directly calls `IDeviceScanJobService` for E2 ScanJob creation and direct ScanNotification monitoring. No ScanLet modifications.

### 2.2 Delete ResultReceiver Flow Recovery

**Detailed Problem:**
```
OXPCopyletContentProvider.deleteJob()
  → ResultReceiver created (onReceiveResult calls countDownLatch.countDown())
  → CopyJobIntentService.startDelete(context, reqBundle, resultReceiver)
    → Intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver)
      → BaseJobIntentService.onStartCommand() → MSG_START
        → CreatingCopyJobState.processDeleteJobIntent()
          → deleteStoredCopyJob() → ReportErrorState(NOT_SUPPORTED)  ← ResultReceiver NOT called!
            → Reporter.fail() → EndState
```
The ContentProvider's countDownLatch times out after 60 seconds because ResultReceiver.send() is never called.

**Solution:**

> ⚠️ **Important:** `ResultReceiver` is stored **directly on the Intent**, not in `extraParams` Bundle.
> `CopyJobIntentService.createCopyIntent()` sets `intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver)`,
> and in `initializeJob()`, `extraParams = intent.getBundleExtra(EXTRA_PARAMS)` is a separate nested Bundle.
> Therefore `extraParams.getParcelable(EXTRA_RESULT_RECEIVER)` → **returns null!**

**Method 1 (Recommended):** Change `processDeleteJobIntent()` signature to pass Intent:
```java
// Inside initializeJob() — pass Intent to processDeleteJobIntent:
case CopyJobIntentService.PARAMS_TYPE_DELETE:
    return processDeleteJobIntent(stateMachine, extraParams, intent);

// processDeleteJobIntent — Extract ResultReceiver from Intent:
private BaseJobIntentServiceState processDeleteJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams, Intent intent) throws Exception {
    ResultReceiver resultReceiver = intent.getParcelableExtra(CopyJobIntentService.EXTRA_RESULT_RECEIVER);
    // ...
}
```

**Method 2:** Pre-extract ResultReceiver in `initializeJob()` and pass it:
```java
// Inside initializeJob():
case CopyJobIntentService.PARAMS_TYPE_DELETE:
    ResultReceiver resultReceiver = intent.getParcelableExtra(CopyJobIntentService.EXTRA_RESULT_RECEIVER);
    return processDeleteJobIntent(stateMachine, extraParams, resultReceiver);
```

### 2.3 CopyOptionProfileAdapter STORE Mode Addition

**Problem:** In `CopyOptionProfileAdapter.getCaps()` → `createCopyAttributeCapsBuilder()`:
```java
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
// Without STORE, the App's StoreCopyBuilder.build(caps) may throw
// CapabilitiesExceededException
```

**Solution:** Add `STORE` mode:
```java
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);
```

### 2.4 CopyJobCompletedState STORE Default Value Conflict Prevention

**Problem:** In `CopyJobCompletedState.processCompletedJob()`:
```java
if (copyJobData.getJobExecutionMode() == null) {
    copyJobData.setJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);
}
```
This code is defensive code for when mode is null during Release, but during Create STORE, mode should already be set to STORE.

**Analysis Result:** Already handled in existing `createCopyJobBundle()` (CreatingCopyJobState L151):
```java
mCopyJobData.setJobExecutionMode(copyAttributesReader.getJobExecutionMode());
```
If `copyAttributesReader.getJobExecutionMode()` returns `STORE`, it is correctly set on `CopyJobData`.

**Conclusion:** By reusing `createCopyJobBundle()` in `processStoredCopyJobIntent()`, mode=STORE is automatically set. No additional code needed. However, ensure that the `CopyAttributesReader` passed to `createCopyJobBundle()` contains mode=STORE.

---

## 3. Existing Code Current State (Plan 1 + Plan 2 Integration)

### 3.1 Already Implemented Infrastructure

**✅ API Classes (JetAdvantageLinkApi):**
- `CopyAttributes.JobExecutionMode` — NORMAL, STORE enumerations
- `CopyAttributes.StoreCopyBuilder` — setStoreJobName(), setStoreJobFolderName(), setStoreJobCredentials(), setRetentionMode*()
- `StoredJobAttributes` — StoredJobBuilder(storedJobId), setCopies(), setJobCredentials()  
- `StoredJobInfo` — storedJobId, folderName, jobName, userName, passwordType, timestamp, copies, colorMode, scanSize, duplex, totalPages
- `ReleaseRequestIntent` — ACTION_RELEASE_COPY, StoredJobAttributes delivery
- `DeleteRequestIntent` — ACTION_DELETE, JobCredentialsAttributes delivery
- `Copylet.Method` — GET_DEFAULTS, GET_CAPS, DELETE_JOB, ENUMERATE_JOBS, IS_SUPPORTED
- `Copylet.Keys` — KEY_STORED_JOB_ID, KEY_DELETE_REQ, KEY_CLIENT_VERSION

**✅ CopyLet Infrastructure:**
- `CopyJobIntentService` — PARAMS_TYPE_RELEASE, PARAMS_TYPE_DELETE constants, startRelease(), startDelete()
- `CreatingCopyJobState` — processReleaseJobIntent(), processDeleteJobIntent() skeletons
- `OXPCopyletBroadcastReceiver` — Copy submit Intent reception and routing
- `OXPCopyletReleaseBroadcastReceiver` — Release Intent reception and routing
- `OXPCopyletContentProvider` — DELETE_JOB (ResultReceiver pattern), ENUMERATE_JOBS (skeleton)
- `CopyJobCompletedState` — mode==null → STORE defensive code (for Release)
- `CopyJobAdapter.getCopyJobTicket()` — storeJob fields commented out (L66-86)

### 3.2 Detailed TODO/Skeleton Map

| # | Location | Current State | Required Work | Phase |
|---|------|-----------|-----------|-------|
| 1 | `CreatingCopyJobState.processCopyJobIntent()` | All submits processed as standard copy | Add STORE branch → processStoredCopyJobIntent() | 2 |
| 2 | `CreatingCopyJobState.processReleaseJobIntent()` | `setJobBundle` commented out + calls `createCopyJob()` → **NPE occurs** (getJobInfo() null since JobBundle not set) | PreferenceStorage lookup + release API call | 4 |
| 3 | `CreatingCopyJobState.processDeleteJobIntent()` | `deleteStoredCopyJob()` → `ReportErrorState(NOT_SUPPORTED)` + ResultReceiver not called → 60-second timeout | Signature change (add Intent) + E2 delete + extract ResultReceiver from Intent + send() + PreferenceStorage cleanup | 5 |
| 4 | `OXPCopyletContentProvider.ENUMERATE_JOBS` | `throw SdkNotSupportedException` [DUNE-169955] | Implement enumerateStoredJobs() | 3 |
| 5 | `CopyJobAdapter.getCopyJobTicket()` L66-86 | storeJob fields commented | Different path via ScanTicket for Stored Copy → no modification needed | - |
| 6 | `CopyOptionProfileAdapter.getOptionProfileHelper()` | Uses `profile.getBase()` + TODO comment | Phase 1: Add STORE mode caps / Phase 2: Distinguish storedCopy profile | 2 |
| 7 | `CopyJobCompletedState.processCompletedJob()` | mode==null → sets STORE | ✅ **Resolved** — `createCopyJobBundle()` L151 auto-sets via `setJobExecutionMode(reader.getJobExecutionMode())`. No additional work needed (see Section 2.4) | - |

---

## 4. Final File Change Summary

### 4.1 Complete File List (9 files: 3 NEW + 6 UPDATE)

| # | File Path (relative — from project root) | Type | Phase | Change Description |
|---|-----------------|------|-------|-----------|
| 1 | `Libs/DeviceServices/Interfaces/src/main/java/com/hp/jetadvantage/link/device/services/interfaces/IDeviceCopyJobService.java` | UPDATE | 1 | Add 3 methods: enumerate, release, delete |
| 2 | `Libs/DeviceServices/Standard/src/main/java/com/hp/jetadvantage/link/device/services/standard/StandardDeviceCopyJobService.java` | UPDATE | 1 | E2 REST call implementation (3 endpoints) |
| 3 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/StoredCopyJobPreferenceStorage.java` | **NEW** | 2 | SharedPreferences CRUD (save, get, remove, replaceKey) |
| 4 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/StoredCopyJobAdapter.java` | **NEW** | 2 | CopyAttrs→ScanTicket(jobStorage) conversion + E2 ScanJob creation |
| 5 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/MonitoringStoredCopyJobState.java` | **NEW** | 2 | ScanNotification→CopyJobState/CopyJobData conversion |
| 6 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobState.java` | UPDATE | 2,4,5 | STORE branch + release re-implementation + delete ResultReceiver |
| 7 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyJobAdapter.java` | UPDATE | 3,4 | convertToStoredJobInfoList + buildCopyOptionsForRelease |
| 8 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/provider/OXPCopyletContentProvider.java` | UPDATE | 3 | ENUMERATE_JOBS implementation |
| 9 | `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyOptionProfileAdapter.java` | UPDATE | 2 | Add addJobExecutionMode(STORE) |

> **Difference:** Plan 1 has 8 files, Plan 2 has 9 files. `CopyOptionProfileAdapter` update was added in Plan 2.

### 4.2 CopyJobIntentServiceStateMachine — State Transition Verification (Resolved)

~~Since `MonitoringStoredCopyJobState` is added as a new state, `possibleNextStates` needs verification~~

**Code Verification Complete: No Changes Needed.**

`CreatingJobState`'s `possibleNextStates` includes `MonitoringJobState.class.getSimpleName()` = `"MonitoringJobState"`.
`MonitoringJobState`'s constructor sets `STATE_NAME` via `super(MonitoringJobState.class.getSimpleName())`.
Since `MonitoringStoredCopyJobState extends MonitoringJobState`, through the constructor chain, `STATE_NAME = "MonitoringJobState"`.
Therefore, `isValidTransition()` validation **passes automatically**. No `possibleNextStates` modification needed.

---

## 5. Feature Detailed Development Specifications

### 5.1 Create Stored Copy Job

#### 5.1.1 Sequence (Final)

```
[App] submit(CopyAttributes{mode=STORE, storeJobName, storeJobFolderName, ...})
  ↓
[OXPCopyletBroadcastReceiver] → CopyJobIntentService.startCopy(bundle)
  ↓
[CreatingCopyJobState.processCopyJobIntent()]
  ├─ Create CopyAttributesReader
  ├─ Check copyAttributesReader.getJobExecutionMode() == STORE
  └─ Call processStoredCopyJobIntent() ★ NEW
      │
      ├─ [1] createCopyJobBundle(clientVersion, copyAttributesReader)
      │       → JobInfo(JobType.COPY), CopyJobData(mode=STORE, state=ACTIVE)
      │       → At L151: setJobExecutionMode(reader.getJobExecutionMode()) — auto STORE setting
      │
      ├─ [2] StoredCopyJobPreferenceStorage.save(context, rid, copyAttributesJson)
      │       → context = stateMachine.getContext()
      │
      ├─ [3] IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
      │       ★ CreatingCopyJobState only has IDeviceCopyJobService → needs separate creation
      ├─ [4] StoredCopyJobAdapter.createScanJobForStorage(scanJobService, packageName, copyAttributesReader)
      │       ├─ scanJobService.getDefaultOptions(packageName)
      │       ├─ CopyAttributes → ScanOptions (using CopyTypeMapping E2 types):
      │       │   ├─ scanSize → mediaSize
      │       │   ├─ scanSource → mediaSource
      │       │   ├─ scanDuplex → plexMode + bindingFormat
      │       │   ├─ colorMode → colorMode
      │       │   ├─ contentType → contentType
      │       │   ├─ contentOrientation → contentOrientation
      │       │   ├─ copyPreview → imagePreviewMode
      │       │   ├─ captureMode → scanCaptureMode
      │       │   └─ progressDialogMode → scanProgressMode
      │       ├─ DestinationOptions.jobStorage = {} (key)
      │       ├─ ScanTicket.name = storeJobName, folderName = storeJobFolderName
      │       └─ scanJobService.createScanJob(packageName, scanJobCreate)
      │           → E2 POST /ext/scanJob/v1/scanJobAgents/{agentId}/scanJobs
      │           → return scanJobId
      │
      ├─ [5] scanJobId null/empty → ReportErrorState + PreferenceStorage.remove(rid)
      │
      ├─ [6] StoredCopyJobPreferenceStorage.replaceKey(context, rid, scanJobId)
      │
      ├─ [7] stateMachine.setJobId(scanJobId)
      │
      └─ [8] return new MonitoringStoredCopyJobState(scanJobId)
```

#### 5.1.2 CopyAttributes Option Classification (Enhanced by Plan 2)

| CopyAttributes Field | CopyTypeMapping | Create(ScanTicket) | Release(CopyOptions) | Note |
|---------------------|-----------------|------|---------|------|
| `scanSize` | originalMediaSize | ✅ | ❌ | Scan only |
| `scanSource` | originalMediaSource | ✅ | ❌ | Scan only |
| `scanDuplex` | originalPlexMode | ✅ | ❌ | Scan only |
| `colorMode` | colorMode | ✅ | ✅ | Both |
| `contentType` | contentType | ✅ | ✅ | Both |
| `contentOrientation` | contentOrientation | ✅ | ✅ | Both |
| `copyPreview` | imagePreviewMode | ✅ | ❌ | Scan only |
| `captureMode` | scanCaptureMode | ✅ | ❌ | Scan only |
| `progressDialogMode` | scanProgressMode | ✅ | ❌ | Scan only |
| `copies` | copies | ❌ | ✅ | Print only |
| `printSize` | outputMediaSize | ❌ | ✅ | Print only |
| `paperSource` | outputMediaSource | ❌ | ✅ | Print only |
| `printDuplex` | outputDuplexBinding | ❌ | ✅ | Print only |
| `paperType` | outputMediaType | ❌ | ✅ | Print only |
| `outputBin` | outputMediaDestination | ❌ | ✅ | Print only |
| `scaleMode` | scaleSelection | ❌ | ✅ | Print only |
| `collateMode` | collationType | ❌ | ✅ | Print only |
| `numberUpMode` | pagesPerSheet | ❌ | ✅ | Print only |
| `storeJobName` | - | ✅ (ticket name) | ❌ | Metadata |
| `storeJobFolderName` | - | ✅ (ticket folder) | ❌ | Metadata |

### 5.2 Enumerate Stored Copy Jobs

```
[App] CopierService.enumerateStoredJob(Context, Result)
  → ContentProvider.call(ENUMERATE_JOBS)
    → enumerateStoredJobs(appPackageName) ★ NEW
        ├─ copyDeviceService.enumerateStoredJobs(packageName)
        │   → E2 GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
        ├─ CopyJobAdapter.convertToStoredJobInfoList(storedJobs)
        │   └─ E2 member → StoredJobInfo mapping:
        │       jobId→storedJobId, jobName, folderName, jobTimestamp,
        │       jobUserName, totalPages
        └─ return JsonParser.toJson(list)
```

### 5.3 Release Stored Copy Job (Plan 1 Detailed Implementation Adopted)

```java
private BaseJobIntentServiceState processReleaseJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams) throws Exception {
    
    ReleaseRequestIntent.IntentParams reqParams = ReleaseRequestIntent.getIntentParams(extraParams);
    if (reqParams == null || reqParams.getStoredJobAttributes() == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "StoredJobAttributes not found");
    }
    
    StoredJobAttributes storedJobAttributes = reqParams.getStoredJobAttributes();
    String storedJobId = storedJobAttributes.getStoredJobId();
    int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel() 
                                                        : storedJobAttributes.getVersion();
    
    // 1. Restore original CopyAttributes from PreferenceStorage
    String savedCopyAttributesJson = StoredCopyJobPreferenceStorage.get(
            stateMachine.getContext(), storedJobId);
    if (savedCopyAttributesJson == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "Stored copy attributes not found for jobId: " + storedJobId);
    }
    
    // 2. Create Release JobBundle + ExtraJobBundle
    //    ★ createReleaseCopyJobBundle() is a NEW method to be added
    //    ★ Based on existing createCopyJobBundle() but constructs JobBundle with mode=STORE + saved CopyAttributes
    stateMachine.setJobBundle(createReleaseCopyJobBundle(clientVersion, storedJobAttributes, 
                                                         savedCopyAttributesJson));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(),
                                                        reqParams.getPackageName()));
    
    // 3. Construct CopyOptions (original attrs + release overrides)
    //    ★ Note: getCopyJobTicket() is private static, so access level change needed
    //    ★ Or include DefaultOptions query inside buildCopyOptionsForRelease()
    DefaultOptions defaultOptions = copyJobService.getDefaultOptions(reqParams.getPackageName());
    CopyOptions copyOptions = CopyJobAdapter.buildCopyOptionsForRelease(
            savedCopyAttributesJson, storedJobAttributes, defaultOptions);
    
    // 4. Call E2 Release API
    CopyJob copyJob = copyJobService.releaseStoredJob(
            reqParams.getPackageName(), storedJobId, copyOptions);
    if (copyJob == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                "Failed to release stored copy job");
    }
    
    String copyJobId = copyJob.getCopyJobId().getValue().toString();
    stateMachine.setJobId(copyJobId);
    
    // 5. Transition to existing MonitoringCopyJobState (E2 Copy notification monitoring)
    return new MonitoringCopyJobState(copyJobId);
}
```

**Key Point:** After Release, E2 creates a regular CopyJob, so CopyNotification is generated. The existing `MonitoringCopyJobState` is reused as-is.

> **Note: PreferenceStorage Cleanup Policy After Release**
> Whether to delete the PreferenceStorage entry after successful Release depends on the `retentionModeOnRelease` setting:
> - `DELETE_ON_RELEASE`: Call PreferenceStorage.remove(storedJobId) on successful Release
> - `RETAIN_ON_RELEASE`: Keep PreferenceStorage (allow re-release of same storedJob)
> In the current implementation, it is recommended to keep PreferenceStorage and only clean up on DELETE.
> If the E2 server auto-deletes the storedJob, synchronization during the next enumerate can clean up.

### 5.4 Delete Stored Copy Job (Enhanced by Plan 2 — ResultReceiver Handling)

> ⚠️ **Signature Change Required:** Since `ResultReceiver` is stored **directly on the Intent**, not in `extraParams`,
> `processDeleteJobIntent()` must additionally receive the `Intent`. (See Section 2.2)

```java
// ★ Inside initializeJob — pass Intent together in case branch:
case CopyJobIntentService.PARAMS_TYPE_DELETE:
    return processDeleteJobIntent(stateMachine, extraParams, intent);

// ★ Signature change: Intent parameter added
private BaseJobIntentServiceState processDeleteJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams, Intent intent) throws Exception {
    
    // ★ Key: Extract ResultReceiver directly from Intent (NOT from extraParams!)
    ResultReceiver resultReceiver = intent.getParcelableExtra(
            CopyJobIntentService.EXTRA_RESULT_RECEIVER);
    
    DeleteRequestIntent.IntentParams reqParams = DeleteRequestIntent.getIntentParams(extraParams);
    String storedJobId = extraParams.getString(Copylet.Keys.KEY_STORED_JOB_ID);
    if (storedJobId == null || storedJobId.isEmpty()) {
        sendResultToReceiver(resultReceiver, Activity.RESULT_CANCELED, "Stored JobID not found");
        return new EndState();
    }
    
    String packageName = reqParams != null ? reqParams.getPackageName() : null;
    
    try {
        // 1. E2 delete (may include credentials)
        // ★ TODO: For password-protected jobs, pass JobCredentialsAttributes
        //    reqParams.getJobCredentialsAttributes() → pass to deleteStoredJob
        copyJobService.deleteStoredJob(packageName, storedJobId);
        
        // 2. Clean up PreferenceStorage
        StoredCopyJobPreferenceStorage.remove(stateMachine.getContext(), storedJobId);
        
        // 3. ★ Send success result via ResultReceiver → Release ContentProvider CountDownLatch
        Bundle resultBundle = new Bundle();
        Result.pack(resultBundle, Result.RESULT_OK);
        sendResultToReceiver(resultReceiver, Activity.RESULT_OK, resultBundle);
    } catch (Exception e) {
        SLog.e(TAG, "deleteStoredCopyJob failed: " + e.getMessage(), e);
        Bundle errorBundle = new Bundle();
        Result.pack(errorBundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        sendResultToReceiver(resultReceiver, Activity.RESULT_CANCELED, errorBundle);
    }
    
    // ★ Delete does not need asynchronous lifecycle (Monitoring) → directly EndState
    return new EndState();
}

// ★ Helper method — ResultReceiver null defense
private void sendResultToReceiver(ResultReceiver receiver, int resultCode, String errorMsg) {
    if (receiver != null) {
        Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, errorMsg);
        receiver.send(resultCode, bundle);
    }
}

private void sendResultToReceiver(ResultReceiver receiver, int resultCode, Bundle bundle) {
    if (receiver != null) {
        receiver.send(resultCode, bundle);
    }
}
```

### 5.5 Profile & Default Options

**Phase 1 (Current Scope):**
- `getCapabilities()` → Uses only E2 copy profile `"base"` profile (existing)
- `getDefaults()` → Uses E2 copy `defaultOptions` (existing)
- Add `addJobExecutionMode(STORE)` in `CopyOptionProfileAdapter` ★Plan 2

**Phase 2 (Future):**
- `getAllCapabilities()` → Combine `"base"` + (`"storedCopy"` ∪ scan `"jobStorage"`)
- `getAllDefaults()` → standard defaults + (copy defaults ∪ scan `"jobStorage"` defaults)
- `CopyOptionProfileAdapter.getOptionProfileHelper()` → Distinguish `profile.getStoredCopy()`

---

## 6. NEW File Detailed Design

### 6.1 StoredCopyJobAdapter.java

**Location:** `CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/`

```java
public class StoredCopyJobAdapter {
    private static final String TAG = "Copylet/SCJAdap";
    
    public static String createScanJobForStorage(
            IDeviceScanJobService scanJobService,
            String packageName,
            CopyAttributesReader copyAttributes) throws Exception {
        
        // 1. Query scan defaultOptions (jobStorage profile)
        DefaultOptions scanDefaultOptions = scanJobService.getDefaultOptions(packageName);
        
        // 2. Construct ScanOptions (using CopyTypeMapping E2 types)
        //    ★ ScanOptions setter names need verification:
        //      - setMediaSize() / setInputMediaSize()
        //      - setMediaSource() / setInputSource()  
        //      - setColorMode()
        //      - setContentType()
        //      - setContentOrientation()
        //      - setPlexMode() + setBindingFormat() / setDuplex()
        //      - setImagePreviewMode()
        //      - setScanCaptureMode()
        //      - setScanProgressMode()
        //    ★ Reference: ScanTicketAdapter.getScanTicket() (ScanLet)
        
        // 3. Construct DestinationOptions
        //    → DestinationOptions.setJobStorage(new JobStorageDestination())
        //    ★ Reference: Verify destination setting pattern in ScanTicketAdapter
        
        // 4. Construct ScanTicket
        //    → name = copyAttributes.getStoreJobName()
        //    → folderName = copyAttributes.getStoreJobFolderName()
        //    → ★ Credentials handling (password-protected stored job):
        //       if (copyAttributes.getStoredJobCredentialsAttributes() != null)
        //         → set storeJobPasswordType, storeJobPassword
        //    → ★ Retention mode:
        //       → set retentionModeOnPowerCycle, retentionModeOnRelease
        //       (Reference: commented-out code in CopyJobAdapter.getCopyJobTicket() L66-86)
        
        // 5. scanJobService.createScanJob(packageName, scanJobCreate) → scanJobId
    }
}
```

### 6.2 MonitoringStoredCopyJobState.java

**Location:** `CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/`

```java
public class MonitoringStoredCopyJobState extends MonitoringJobState {
    private IDeviceScanJobService scanJobService;
    private IE2PayloadCallback<ScanNotification> scanNotificationCallback;
    
    protected MonitoringStoredCopyJobState(String scanJobId) {
        super(scanJobId);
        scanJobService = new StandardDeviceScanJobService();
    }
    
    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        scanNotificationCallback = (appPackageId, notification) -> {
            // Convert ScanNotification → CopyJobState
            // Only update imagesScanned (scan stage only, no sheetsPrinted)
            // Reuse processJobDoneStatus() (inherited)
        };
        scanJobService.registerNotificationCallback(scanNotificationCallback);
    }
    
    @Override
    protected void unregisterNotificationCallback() {
        scanJobService.unRegisterNotificationCallback();
    }
}
```

**Comparison with MonitoringCopyJobState:**
| Item | MonitoringCopyJobState | MonitoringStoredCopyJobState |
|------|----------------------|----------------------------|
| Device Service | IDeviceCopyJobService | IDeviceScanJobService |
| Notification Type | CopyNotification | ScanNotification |
| Status Type | CopyJobStatus | ScanJobStatus |
| Output JobType | COPY | COPY (same — App compat) |
| imagesScanned | ✅ | ✅ |
| sheetsPrinted | ✅ | ❌ (scan stage only) |
| printingActivity | ✅ | ❌ |

### 6.3 StoredCopyJobPreferenceStorage.java

**Location:** `CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/`

```java
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

**Thread Safety:** CopyJobIntentService processes sequentially on a single HandlerThread, so no concurrent access.

---

## 7. Detailed Component Flowcharts (Adopted from Plan 1)

### 7.1 Create Stored Copy Job

```
App
 │ submit(CopyAttributes{mode=STORE})
 ▼
OXPCopyletBroadcastReceiver
 │ CopyJobIntentService.startCopy(bundle)
 ▼
CopyJobIntentService (HandlerThread)
 │ CopyJobIntentServiceStateMachine
 ▼
CreatingCopyJobState
 │ initializeJob()
 │ ├─ PARAMS_TYPE == COPY
 │ └─ processCopyJobIntent()
 │     ├─ JobExecutionMode == STORE → processStoredCopyJobIntent()
 │     │   ├─ createCopyJobBundle(STORE)
 │     │   │   └─ L151: setJobExecutionMode(reader.getJobExecutionMode())  ★ Auto STORE setting
 │     │   ├─ PreferenceStorage.save(RID, attrs)
 │     │   ├─ StoredCopyJobAdapter.createScanJobForStorage()
 │     │   │   ├─ CopyTypeMapping → ScanOptions conversion
 │     │   │   ├─ DestinationOptions.jobStorage
 │     │   │   └─ IDeviceScanJobService.createScanJob()
 │     │   │       → E2: POST /ext/scanJob/v1/.../scanJobs
 │     │   ├─ PreferenceStorage.replaceKey(RID → scanJobId)
 │     │   └─ return MonitoringStoredCopyJobState(scanJobId)
 │     │
 │     └─ JobExecutionMode == NORMAL → (existing Standard Copy)
 ▼
MonitoringStoredCopyJobState (NEW)
 │ IDeviceScanJobService.registerNotificationCallback()
 │ ├─ ScanNotification → CopyJobState conversion
 │ └─ processJobDoneStatus()
 ▼
CopyJobCompletedState → EndState
```

### 7.2 Release Stored Copy Job

```
App
 │ releaseStoredJob(StoredJobAttributes{jobId, copies})
 ▼
OXPCopyletReleaseBroadcastReceiver
 │ CopyJobIntentService.startRelease(bundle)
 ▼
CreatingCopyJobState
 │ PARAMS_TYPE == RELEASE
 │ └─ processReleaseJobIntent()
 │     ├─ PreferenceStorage.get(storedJobId) → original CopyAttributes
 │     ├─ copyJobService.getDefaultOptions(packageName) → defaultOptions
 │     ├─ CopyJobAdapter.buildCopyOptionsForRelease(attrs, storedAttrs, defaultOptions)
 │     ├─ IDeviceCopyJobService.releaseStoredJob(pkg, jobId, copyOptions)
 │     │   → E2: POST /ext/copy/v1/.../storedJobs/{id}/release
 │     └─ return MonitoringCopyJobState(copyJobId) ← existing reused
 ▼
MonitoringCopyJobState (existing)
 │ CopyNotification → CopyJobState
 ▼
CopyJobCompletedState → EndState
```

### 7.3 Delete Stored Copy Job (Enhanced by Plan 2 + Code Verification)

```
App
 │ deleteStoredJob(jobId, credentials)
 ▼
WorkpathLib → ContentProvider.call(DELETE_JOB)
 ▼
OXPCopyletContentProvider.deleteJob()
 │ ├─ ResultReceiver(countDownLatch) created
 │ ├─ CopyJobIntentService.startDelete(reqBundle, resultReceiver)
 │ │   → Intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver)  ★ Stored directly on Intent
 │ └─ countDownLatch.await(60s)  ← Waits until ResultReceiver.send()
 ▼
BaseJobIntentService.onStartCommand()
 │ msg.obj = intent  (passes entire Intent as Message object)
 ▼
CreatingCopyJobState.initializeJob(Intent intent, stateMachine)
 │ final Bundle extraParams = intent.getBundleExtra(EXTRA_PARAMS);  ← nested Bundle (params only)
 │ PARAMS_TYPE == DELETE
 │ └─ processDeleteJobIntent(stateMachine, extraParams, intent)  ★ Signature change
 │     ├─ ★ Extract ResultReceiver directly from Intent:
 │     │     intent.getParcelableExtra(EXTRA_RESULT_RECEIVER)
 │     ├─ copyJobService.deleteStoredJob(pkg, storedJobId)
 │     │   → E2: DELETE /ext/copy/v1/.../storedJobs/{id}
 │     ├─ PreferenceStorage.remove(storedJobId)
 │     ├─ ★ resultReceiver.send(RESULT_OK, resultBundle)  → Release countDownLatch
 │     └─ return EndState()  (Monitoring unnecessary)
```

> **Key:** `ResultReceiver` is stored **directly on the Intent**, not in `extraParams` Bundle.
> `CopyJobIntentService.createCopyIntent()` sets `intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver)`,
> and in `initializeJob()`, `extraParams = intent.getBundleExtra(EXTRA_PARAMS)` is a separate nested Bundle.
> Therefore `processDeleteJobIntent()` must receive the `Intent` directly.

---

## 8. Final Development Order

### Phase 1: Device Service Layer
1. `IDeviceCopyJobService` — Add enumerateStoredJobs(), releaseStoredJob(), deleteStoredJob()
2. `StandardDeviceCopyJobService` — E2 REST call implementation
3. **★ Prerequisite verification:** Whether storedJobs method chain exists in E2 CopyServiceClient

### Phase 2: Create Stored Copy Job
1. `StoredCopyJobPreferenceStorage` — SharedPreferences CRUD
2. `CopyOptionProfileAdapter` — Add addJobExecutionMode(STORE) ★Plan 2
3. `StoredCopyJobAdapter` — CopyAttrs → ScanTicket(jobStorage) conversion
4. `MonitoringStoredCopyJobState` — ScanNotification → CopyJobState
5. `CreatingCopyJobState` — STORE branch + processStoredCopyJobIntent()
   - ★ Reusing `createCopyJobBundle()` auto-sets mode=STORE (L151)
   - ★ No `possibleNextStates` change needed (MonitoringStoredCopyJobState's STATE_NAME = "MonitoringJobState")

### Phase 3: Enumerate
1. `CopyJobAdapter.convertToStoredJobInfoList()`
2. `OXPCopyletContentProvider` — ENUMERATE_JOBS implementation

### Phase 4: Release
1. `CopyJobAdapter.buildCopyOptionsForRelease()` — DefaultOptions required (★ getCopyJobTicket() access level change or logic restructuring)
2. `CreatingCopyJobState.processReleaseJobIntent()` re-implementation + `createReleaseCopyJobBundle()` NEW method addition

### Phase 5: Delete
1. `CreatingCopyJobState.processDeleteJobIntent()` — **Signature change** (add Intent) + extract ResultReceiver from Intent + E2 delete + send() + PreferenceStorage

### Phase 6: Testing
1. Unit tests: Adapter conversion, PreferenceStorage CRUD, StoredJobInfo conversion
2. Integration tests: E2 integration
3. Scenario tests: Create → Enumerate → Release → Delete

---

## 9. E2 Library Pre-Verification Checklist (Integrated)

| # | Item to Verify | How to Verify | Alternative |
|---|----------|----------|------|
| 1 | `CopyServiceClient` has `storedJobs()` method chain | Source/Javadoc | Direct REST calls |
| 2 | `StoredJobs` response type exists | `com.hp.ext.service.copy` package | Create POJO directly |
| 3 | `StoredJobs.members` field structure | E2 API docs / real device | Real device test |
| 4 | Release API request type (`ReleaseRequest`?) | E2 client library | Direct HTTP POST |
| 5 | `ScanOptions` class setter names | `com.hp.ext.service.scanJob.ScanOptions` | Refer to ScanLet code |
| 6 | `DestinationOptions` jobStorage setting field | `com.hp.ext.service.scanJob.DestinationOptions` | Refer to ScanTicketAdapter |
| 7 | `ScanTicket` name/folderName setting location | ScanTicket class | - |
| 8 | `ScanNotification`/`ScanJobStatus` structure | `com.hp.ext.service.scanJob` | Refer to MonitoringScanJobState |
| 9 | `StoredJobInfo` constructor/Builder pattern | JetAdvantageLinkApi | - |
| 10 | ScanJob_Create's jobStorage destination JSON structure | E2 API docs | - |

---

## 10. Risk Factors and Mitigation (Integrated)

| # | Risk | Impact | Probability | Mitigation |
|---|------|------|------|------|
| 1 | E2 storedJobs client not supported | Phase 1 delay | Medium | Implement direct REST calls |
| 2 | ScanOptions/CopyOptions field name mismatch | Phase 2 mapping errors | Medium | Detailed comparison of ScanTicketAdapter + E2 Javadoc |
| 3 | PreferenceStorage data loss | Missing original options on Release | Low | Add enumerate result synchronization (future) |
| 4 | CopyJobCompletedState mode=null→STORE | Possible Create STORE malfunction | Low | ✅ Auto-set via createCopyJobBundle() L151 (Section 2.4) |
| 5 | Delete ResultReceiver not called | 60-second timeout | High → Resolved | processDeleteJobIntent **signature change** (add Intent parameter) + extract ResultReceiver from Intent + send() |
| 6 | ~~StateMachine state transition constraint~~ | ~~MonitoringStoredCopyJobState transition failure~~ | **Resolved** | Automatic pass via STATE_NAME inheritance (Section 4.2) |

---

## 11. Key Design Decision Summary (Integrated)

| # | Decision | Rationale |
|---|------|------|
| 1 | Create ScanJob directly within CopyLet | Option 2 + component definition + ScanLet JobType.SCAN fixed |
| 2 | Separate MonitoringStoredCopyJobState class | ScanNotification vs CopyNotification separation |
| 3 | SharedPreferences persistence | JobLet pattern + single-thread safety |
| 4 | Reuse CopyTypeMapping E2 types | E2 imaging/media types are common |
| 5 | Delete → Extract ResultReceiver from Intent + direct response + EndState | Single API call, monitoring unnecessary, must extract from Intent not extraParams |
| 6 | Release → Reuse existing MonitoringCopyJobState | After Release, E2 CopyJob/CopyNotification |
| 7 | Add STORE mode to CopyOptionProfileAdapter | Required for App's StoreCopyBuilder.build(caps) compatibility |
| 8 | Reusing createCopyJobBundle() auto-sets mode=STORE | getJobExecutionMode() called at L151 — no additional code needed |
| 9 | Profile/Options supports base only in Phase 1 | Per architecture document Phase separation |
| 10 | No possibleNextStates change needed | MonitoringStoredCopyJobState's STATE_NAME is set to "MonitoringJobState" by parent class |
| 11 | Keep PreferenceStorage after Release | Re-release possible per retentionModeOnRelease, cleanup only on DELETE |
| 12 | Add Intent to processDeleteJobIntent signature | ResultReceiver is stored directly on Intent (not in Bundle extraParams) |

---

## 12. Additional Items Confirmed Through Code Verification

### 12.1 deleteJob's ResultReceiver Result Format

`OXPCopyletContentProvider.deleteJob()`'s ResultReceiver processes results as follows:
```java
protected void onReceiveResult(final int resultCode, final Bundle resultData) {
    Result result = Result.parse(resultData, new Result());
    Result.pack(bundle, result);
    countDownLatch.countDown();
}
```

Therefore, the Bundle sent via ResultReceiver from `processDeleteJobIntent` must use the `Result.pack()` format:
```java
Bundle resultBundle = new Bundle();
Result.pack(resultBundle, Result.RESULT_OK);
resultReceiver.send(Activity.RESULT_OK, resultBundle);
```

Simply sending `Bundle.EMPTY` would prevent the ContentProvider from parsing the Result.

### 12.2 NPE Risk in Current processReleaseJobIntent

In the current code state, executing release will cause:
```java
//TODO : Implement stored job
//stateMachine.setJobBundle(createStoredCopyJobBundle(...));  // ← commented out!
stateMachine.setExtraJobBundle(createExtraJobBundle(...));
return createCopyJob(stateMachine, reqParams.getPackageName());
```

Since `setJobBundle` is not called, `stateMachine.getJobInfo()` → null → **NullPointerException** occurs inside `CopyJobAdapter.createCopyJob()`. The release feature is completely non-functional in its current state.

### 12.3 Intent Structure Details (CopyJobIntentService.createCopyIntent)

```
Intent (CopyJobIntentService.class)
 ├─ PARAMS_TYPE (String): "paramsCopy" / "paramsDelete" / "paramsRelease"
 ├─ EXTRA_PARAMS (Bundle): nested Bundle → CopyToRequest/ReleaseRequest/DeleteRequest extras
 │   ├─ CopyAttributes JSON / StoredJobAttributes / DeleteRequestIntent params
 │   ├─ KEY_STORED_JOB_ID (for delete)
 │   └─ KEY_DELETE_REQ (for delete)
 └─ EXTRA_RESULT_RECEIVER (ResultReceiver): exists only for delete  ★ Intent level!
```

The `extraParams` extracted via `intent.getBundleExtra(EXTRA_PARAMS)` in `initializeJob()` is a nested Bundle, so it does NOT contain `EXTRA_RESULT_RECEIVER`.

### 12.4 CopyOptionProfileAdapter Dummy Code Verification

There is currently dummy code in `createCopyAttributeCapsBuilder()`:
```java
//TODO : add dummy just for test - revisit later
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
builder.addStapleOption(CopyAttributes.StapleOption.NONE);
builder.addPunchMode(CopyAttributes.PunchMode.NONE);
// ...
```

When adding STORE mode, add it near this TODO:
```java
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);  // ★ Add
```

---

## 13. Build Verification and Test Requirements

### 13.1 Build Verification

After each Phase implementation, a build must be performed and build errors must be fixed.

**Build Commands:**
```bash
# CopyLet module build
./gradlew :Let-CopyLet:assembleDebug

# DeviceServices module build (Phase 1)
./gradlew :DeviceServices-Interfaces:assembleDebug
./gradlew :DeviceServices-Standard:assembleDebug

# Test module build
./gradlew :Test-WorkpathServices:assembleDebug
```

**Build Verification Procedure:**
1. Complete code implementation per Phase
2. Execute `./gradlew :Let-CopyLet:assembleDebug`
3. Fix build errors immediately when they occur
4. Repeat until build succeeds without errors
5. Proceed to next Phase

### 13.2 Unit Tests

After the build is complete, write unit test code. **Code coverage must be 80% or higher.**

**Test Location:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/`
- `adapter/` — Adapter-related tests
- `service/` — Service/State-related tests

**Test Framework:**
- JUnit 4 (`@RunWith(MockitoJUnitRunner.class)`)
- Mockito + mockito-inline (static method mocking)
- `unitTests.returnDefaultValues = true` (Android framework default values)

**Existing Test Pattern References:**
- `CreatingCopyJobStateTest.java` — `testMode=true` constructor to bypass `isSupported()`, Mock IDeviceCopyJobService
- `MonitoringCopyJobStateUnitTest.java` — Notification callback tests
- `CopyOptionProfileAdapterUnitTest.java` — Caps/Options tests

**Required Test Files (NEW):**

| # | Test File | Target | Key Test Cases |
|---|-----------|------|-----------------|
| 1 | `adapter/StoredCopyJobAdapterTest.java` | StoredCopyJobAdapter | createScanJobForStorage() CopyAttrs→ScanOptions conversion, jobStorage destination setting, null/error handling |
| 2 | `service/StoredCopyJobPreferenceStorageTest.java` | StoredCopyJobPreferenceStorage | save/get/remove/replaceKey CRUD, query for non-existent key, verify old key absence after replaceKey |
| 3 | `service/MonitoringStoredCopyJobStateTest.java` | MonitoringStoredCopyJobState | ScanNotification→CopyJobState conversion, callback register/unregister, processJobDoneStatus |
| 4 | `service/CreatingCopyJobStateStoredTest.java` | CreatingCopyJobState (Stored branch) | processStoredCopyJobIntent success/failure, processReleaseJobIntent success/failure, processDeleteJobIntent + ResultReceiver send |
| 5 | `adapter/CopyJobAdapterStoredTest.java` | CopyJobAdapter (Stored additions) | convertToStoredJobInfoList conversion, buildCopyOptionsForRelease option mapping |

**Existing Test Files (UPDATE):**

| # | Test File | Additional Cases |
|---|-----------|-----------|
| 1 | `adapter/CopyOptionProfileAdapterUnitTest.java` | Verify STORE mode caps inclusion |

**Test Execution Commands:**
```bash
# Full CopyLet unit tests
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.*"

# Individual test class
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.adapter.StoredCopyJobAdapterTest"
```

**Coverage Verification:**
```bash
# Generate JaCoCo coverage report (if configured)
./gradlew :Test-WorkpathServices:jacocoTestReport
```

> **80% Coverage Standard:** Line coverage of 80% or higher for newly added/modified code (StoredCopyJobAdapter, MonitoringStoredCopyJobState, StoredCopyJobPreferenceStorage, CreatingCopyJobState Stored branch, CopyJobAdapter Stored methods).

### 13.3 Android Instrumented Tests (androidTest)

After unit tests are complete, write and execute Android instrumented tests (androidTest).

**Test Location:** `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/copylet/`
- `adapter/` — Adapter instrumented tests
- `service/` — Service/StateMachine instrumented tests

**Test Framework:**
- AndroidJUnit4 (`@RunWith(AndroidJUnit4.class)`)
- Espresso + AndroidX Test Core
- `InstrumentationRegistry.getInstrumentation().getTargetContext()` — real Context
- Mockito-android (for Android instrumented environment)

**Existing Test Pattern References:**
- `CopyJobIntentServiceStateMachineInstrumentedTest.java` — StateMachine + real Context
- `CopyDeviceAdapterInstrumentedTest.java` — Device adapter + real API

**Required Test Files (NEW):**

| # | Test File | Key Test Cases |
|---|-----------|-----------------|
| 1 | `service/StoredCopyJobPreferenceStorageInstrumentedTest.java` | Real SharedPreferences CRUD (real device Context), data persistence verification, replaceKey atomicity |
| 2 | `service/CreatingCopyJobStateStoredInstrumentedTest.java` | StateMachine state transitions (Create→MonitoringStored, Release→MonitoringCopy, Delete→End), real Intent/Bundle |
| 3 | `adapter/StoredCopyJobAdapterInstrumentedTest.java` | E2 API integration test (when connected to real device), CopyAttrs→ScanTicket conversion + real type verification |

**Test Execution Commands:**
```bash
# Full CopyLet androidTest
./gradlew :Test-WorkpathServices:connectedDebugAndroidTest --tests "com.hp.jetadvantage.link.services.copylet.*"

# Individual androidTest class
./gradlew :Test-WorkpathServices:connectedDebugAndroidTest --tests "com.hp.jetadvantage.link.services.copylet.service.StoredCopyJobPreferenceStorageInstrumentedTest"
```

### 13.4 Test Progression Order

```
Phase 1~5 Implementation (per Phase)
  │
  ├─ [1] Build: ./gradlew :Let-CopyLet:assembleDebug
  │     └─ Build error → Fix → Rebuild (until success)
  │
  ├─ [2] Write + Execute Unit Tests
  │     ├─ Create new test files (tests/services/src/test/.../copylet/)
  │     ├─ ./gradlew :Test-WorkpathServices:testDebugUnitTest
  │     ├─ Test failure → Fix code → Re-execute
  │     └─ Verify code coverage 80% or higher ★ Required
  │
  └─ [3] Write + Execute androidTests
        ├─ Create new test files (tests/services/src/androidTest/.../copylet/)
        ├─ ./gradlew :Test-WorkpathServices:connectedDebugAndroidTest
        ├─ Test failure → Fix code → Re-execute
        └─ Verify all instrumented tests pass ★ Required
```

> **Important:** Complete unit tests first before proceeding to androidTests.
> androidTests require an emulator or real device connection.

---

## 14. Development Effort Estimation and Token Execution Feasibility

### 14.1 Code Change Volume Estimation

**Existing File Modifications (6 UPDATE files):**

| File | Current LOC | Estimated Added/Modified LOC | Content |
|------|---------|-----------------|------|
| CreatingCopyJobState.java | ~336 | +80 | STORE branch, release re-implementation, delete signature change |
| CopyJobAdapter.java | ~271 | +60 | convertToStoredJobInfoList, buildCopyOptionsForRelease |
| OXPCopyletContentProvider.java | ~206 | +30 | ENUMERATE_JOBS implementation |
| CopyOptionProfileAdapter.java | ~235 | +1 | addJobExecutionMode(STORE) |
| IDeviceCopyJobService.java | ~49 | +15 | 3 new method signatures |
| StandardDeviceCopyJobService.java | ~154 | +60 | 3 E2 REST call implementations |

**New Files (3 NEW):**

| File | Estimated LOC |
|------|---------|
| StoredCopyJobAdapter.java | ~120-150 |
| MonitoringStoredCopyJobState.java | ~100-120 |
| StoredCopyJobPreferenceStorage.java | ~40 |

**Test Code (NEW):**

| Type | File Count | Estimated Total LOC |
|------|---------|------------|
| Unit Tests | 5 | ~800-1200 |
| androidTest | 3 | ~300-500 |

### 14.2 Total Estimation

| Category | LOC |
|------|-----|
| Production Code (modifications) | ~246 |
| Production Code (new) | ~310 |
| Unit Tests (new) | ~1,000 |
| androidTest (new) | ~400 |
| **Total** | **~1,956** |

### 14.3 Token Execution Feasibility Assessment

**Conclusion: Phase-by-phase split execution recommended**

Full implementation + build verification + test writing in a single session is difficult due to context limitations.

**Reasons:**
1. Reference context: This document (~900 lines) + existing source (~1,654 lines) + existing tests (~1,254 lines) = ~3,800 lines to read
2. Generated code: ~1,956 lines
3. Build error fixing: Build output interpretation + iterative fixes
4. Each Phase requires understanding and modifying multiple files simultaneously

**Recommended Execution Plan (per session):**

| Session | Phase | Work Content | Estimated Token Weight |
|------|-------|----------|-------------|
| Session 1 | Phase 1 + 2 | DeviceService interfaces + Create (4 files new/modify) + build | Medium |
| Session 2 | Phase 3 + 4 | Enumerate + Release (3 files modify) + build | Medium |
| Session 3 | Phase 5 | Delete (1 file modify) + build + full integration verification | Small |
| Session 4 | Phase 6a | Write 5 unit test files + execute + verify 80% coverage | Large |
| Session 5 | Phase 6b | Write 3 androidTest files + execute | Medium |

> **Note:** Referencing this document at the start of each session allows quick context restoration.
