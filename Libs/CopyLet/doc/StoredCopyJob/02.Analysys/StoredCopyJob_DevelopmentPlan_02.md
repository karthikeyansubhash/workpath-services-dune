# StoredCopyJob Development Plan (V2 — Cross-Validation)

> **DUNE-169955: Workpath Stored Copy Job**
> This document is the 2nd development plan, written by cross-validating the architecture documents and existing code.
> It independently re-validates the analysis from the 1st plan and supplements omissions/errors.

---

## 1. Architecture Document vs Code Cross-Validation Results

### 1.1 Key Finding: Inconsistency Between Sequence Diagram and Component Diagram

**StoredCopyJob_Create.puml Sequence Diagram:**
```
CopyService → ScanService : Send "Scan to JobStorage" JobIntent
ScanService → ScanService : createJobBundle(JobType.COPY)
ScanService → E2ScanAgent : POST .../scanJobs
```
→ A structure where CopyService **delegates to ScanService (ScanLet)**

**StoredCopyJob_Overview.md Component Diagram (Section 4.1):**
- Only change components within CopyLet are defined (StoredCopyJobAdapter, CopyJobAdapter, StateMachine, etc.)
- **ScanLet is NOT included as a change target**

**Option 2 Description:**
> "The Copy Service manages the workflow by **adding a new job state machine** for 'Scan to JobStorage'"
> Cons: "**Code duplication**: Scan job state machine logic is **repeated in both Scan and Copy Services**"

**Analysis Result:**
- The essence of Option 2 (Centralized Logic) is to **implement the scan job logic internally within CopyLet**
- The "Scan Service" in the sequence diagram is likely a logical-level representation or a remnant of early design
- **The component definition and Option 2 description are the substantive basis for the implementation direction**

**Conclusion:** CopyLet directly uses `IDeviceScanJobService` to create E2 ScanJobs and directly monitor ScanNotifications. It does NOT delegate to ScanLet (ScanJobIntentService).

**Rationale:**
1. ScanLet's `CreatingScanJobState.createJobBundle()` always sets `JobType.SCAN` — supporting COPY type would require ScanLet modifications
2. ScanLet's `MonitoringScanJobState` uses `ScanJobState`/`ScanJobData` — cannot convert to `CopyJobState`/`CopyJobData`
3. Section 4.1 component definition has no ScanLet changes
4. Option 2 cons explicitly states "code duplication" → implies re-implementing scan logic within CopyLet

### 1.2 Detailed Analysis of Existing Code TODO/Skeletons

| Location | Code | Current State | Required Work |
|------|------|-----------|-----------|
| `CreatingCopyJobState.processCopyJobIntent()` | No `JobExecutionMode.STORE` check | All submits are processed as standard copy | Add STORE branch |
| `CreatingCopyJobState.processReleaseJobIntent()` | Calls `createCopyJob(stateMachine, packageName)` | Replaced with regular copy job creation (incorrect behavior) | Replace with release API call |
| `CreatingCopyJobState.processDeleteJobIntent()` | `deleteStoredCopyJob()` → `ReportErrorState(NOT_SUPPORTED)` | Not implemented | E2 delete + ResultReceiver response |
| `OXPCopyletContentProvider.ENUMERATE_JOBS` | `throw SdkNotSupportedException` | Not implemented | Implement enumerate API |
| `CopyJobAdapter.getCopyJobTicket()` L66-86 | storeJob fields commented out | Not implemented | Different path via ScanTicket for Stored Copy |
| `CopyOptionProfileAdapter.getOptionProfileHelper()` | Uses `profile.getBase()` + TODO comment | Only base profile used | Distinguish storedCopy profile for STORE mode |
| `CopyJobCompletedState.processCompletedJob()` | Sets STORE if mode==null | Defensive code for release | Verify correct operation during Create STORE as well |

### 1.3 Delete Flow's ResultReceiver Disconnection Issue

**Current delete flow:**
```
OXPCopyletContentProvider.deleteJob()
  → ResultReceiver created + CountDownLatch
  → CopyJobIntentService.startDelete(context, reqBundle, resultReceiver)
    → Intent stores EXTRA_RESULT_RECEIVER
      → BaseJobIntentService.onStartCommand() → MSG_START
        → CreatingCopyJobState.initializeJob()
          → processDeleteJobIntent() → deleteStoredCopyJob()
            → ReportErrorState(NOT_SUPPORTED)
              → Reporter.fail() → EndState
```

**Problem:** The `ResultReceiver` is stored in the Intent's extras, but there is no code in `processDeleteJobIntent()` to extract and `send()` to it. The `ContentProvider`'s `CountDownLatch` waits until the 60-second timeout and then fails.

**Solution:** Delete does not require an asynchronous Job lifecycle (Creating→Monitoring→Completed) — it is a **synchronous single operation**. After executing delete, the ResultReceiver must be extracted from the Intent and the result sent directly.

---

## 2. Existing Standard Copy Job Flow (Detailed Analysis)

### 2.1 Full Execution Flow

```
[1] App → CopierService.submit(CopyAttributes, CopyletAttributes)
    ↓ WorkpathLib creates RID, broadcasts CopyToRequestIntent

[2] OXPCopyletBroadcastReceiver.onReceive()
    ├─ CopyletAttributes.showSettingsUI == true → Launch SettingsUIActivity
    └─ else → CopyJobIntentService.startCopy(context, intent.getExtras())

[3] CopyJobIntentService (extends BaseJobIntentService)
    ├─ onCreate(): Create HandlerThread, createStateMachine() → CopyJobIntentServiceStateMachine
    └─ onStartCommand(): MSG_START message → StateMachine

[4] CopyJobIntentServiceStateMachine.handleMessage(MSG_START)
    └─ createCreatingJobState(intent) → new CreatingCopyJobState(intent)
       → transitionTo(creatingState)

[5] CreatingCopyJobState.onProcess()  (via onEnter → WakeLock acquire)
    ├─ initializeJob(intent, stateMachine)
    │   ├─ setTargetPackage(stateMachine, extraParams)
    │   ├─ isSupported(contentResolver) → ContentProvider.call(IS_SUPPORTED)
    │   ├─ switch(PARAMS_TYPE)
    │   │   case PARAMS_TYPE_COPY → processCopyJobIntent()
    │   │       ├─ CopyToRequestIntent.getIntentParams(extraParams)
    │   │       ├─ new CopyAttributesReader(copyAttributes)
    │   │       ├─ stateMachine.setJobBundle(createCopyJobBundle())
    │   │       │   ├─ JobInfo(JobType.COPY)
    │   │       │   ├─ CopyJobData(state=ACTIVE, duplex, scanSize, executionMode)
    │   │       │   └─ Bundle keys: CLIENTS_VERSION, JOB_TYPE, SCAN_IMAGE_COUNT, DST_TOTAL, JOB_EMULATED, JOB_SOURCE
    │   │       ├─ stateMachine.setExtraJobBundle(RID, JOB_TYPE, CLIENTS_VERSION, CLIENT_PACKAGE)
    │   │       ├─ stateMachine.setJobAttributesReader(copyAttributesReader)
    │   │       └─ createCopyJob(stateMachine, packageName)
    │   │           ├─ CopyJobAdapter.createCopyJob(copyJobService, packageName, jobInfo, attributesReader)
    │   │           │   ├─ copyJobService.getDefaultOptions(packageName) → E2 DefaultOptions
    │   │           │   ├─ getCopyJobTicket(attributesReader, defaultOptions) → CopyJobTicket
    │   │           │   │   └─ CopyTypeMapping.*.convertWtoE() to convert each option
    │   │           │   └─ copyJobService.createCopyJob(packageName, copyJobCreate)
    │   │           │       → E2 POST /ext/copy/v1/copyAgents/{agentId}/copyJobs
    │   │           ├─ stateMachine.setJobId(jobId)
    │   │           └─ return MonitoringCopyJobState(jobId)
    └─ setNextState(nextState) → transitionTo

[6] MonitoringCopyJobState.onEnter()
    ├─ JobletService.startMonitoring(context, jobId, null, taskAttributes) → Notify JobLet to start monitoring
    └─ stateMachine.sendLocalJobDataToJobLet(TL_EV_JOB_PROGRESS) → Initial progress event

[7] MonitoringCopyJobState.onProcess()
    └─ registerNotificationCallback(stateMachine)
        └─ copyJobService.registerNotificationCallback(callback)
            → Await E2 websocket CopyNotification

[8] On E2 CopyNotification received:
    ├─ Extract CopyJobStatus
    ├─ updateJobStatus(jobBundle, copyJobStatus)
    │   ├─ totalImagesScanned, totalSheetsPrinted → Update JobBundle
    │   ├─ CopyJobData.setImagesScanned(), setSheetsPrinted()
    │   └─ getCopyJobState() → Map ScanningActivity, PrintingActivity, CancelingActivity
    └─ processJobDoneStatus(stateMachine, jobDoneStatus)
        ├─ JdsSucceeded/JdsPartiallySucceeded → MSG_REPORT_COMPLETED
        ├─ JdsFailed → MSG_REPORT_FAIL
        ├─ JdsCanceled → MSG_REPORT_CANCELLED
        └─ JdsActive → TL_EV_JOB_PROGRESS (continue monitoring)

[9] StateMachine.handleMessage(MSG_REPORT_COMPLETED)
    ├─ updateCompletedJobState() → updateFinalJobState(COMPLETED)
    │   └─ CopyJobData.getJobState().setState(COMPLETED), setCompleteTime()
    ├─ sendLocalJobDataToJobLet(TL_EV_JOB_COMPLETED) → JobLet → Final notification to App
    └─ transitionTo(createJobCompletedState()) → CopyJobCompletedState

[10] CopyJobCompletedState.onProcess()
    ├─ processCompletedJob()
    │   └─ If mode==null, set mode=STORE (in preparation for Release Jobs)
    ├─ stateMachine.updateCompletedJobState()
    ├─ sendLocalJobDataToJobLet(TL_EV_JOB_COMPLETED)
    └─ transitionTo(EndState)

[11] EndState.onProcess()
    ├─ releaseWakeLock()
    ├─ removeCallbacksAndMessages(null)
    └─ stateMachine.stop()
```

### 2.2 Synchronous Query Flow (getCaps, getDefaults)

```
App → ContentProvider.call(GET_CAPS)
  → CopyOptionProfileAdapter.getCaps(packageName, deviceService, clientVersion)
    → deviceService.getProfile(packageName)
      → E2 GET /ext/copy/v1/profile → Profile{"base":{}, "storedCopy":{}}
    → profile.getBase().getDefinitions() → OptionProfile
    → CopyTicketHelper → OptionProfileHelper<CopyOptions>
    → For each CopyTypeMapping item: addPossibleOptions() → CopyAttributesCapsCreator.Builder
    → builder.addJobExecutionMode(JobExecutionMode.NORMAL)  // ★ Currently only NORMAL is added
    → return CopyAttributesCaps JSON

App → ContentProvider.call(GET_DEFAULTS)
  → CopyDeviceAdapter.getDefaults(packageName, deviceService, clientVersion)
    → deviceService.getProfile(packageName) → Profile
    → deviceService.getDefaultOptions(packageName) → DefaultOptions
    → profile.getBase() → OptionProfile-based validation
    → For each CopyTypeMapping item: setDefaultOption() → CopyBuilder
    → CopyBuilder.build(caps) → CopyAttributes JSON
```

---

## 3. StoredCopyJob Feature Detailed Development Items

### 3.1 Create Stored Copy Job

#### 3.1.1 Sequence (Option 2: CopyLet Internal Self-Processing)

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
      │       → JobType.COPY, mode=STORE, jobName=storeJobName
      │
      ├─ [2] Temporarily save original CopyAttributes JSON to PreferenceStorage (key: RID)
      │       → StoredCopyJobPreferenceStorage.save(context, rid, copyAttributesJson)
      │
      ├─ [3] Extract scan-related options from CopyAttributes + Construct ScanTicket
      │       → StoredCopyJobAdapter.createScanJobForStorage(scanJobService, packageName, copyAttributesReader)
      │       │
      │       ├─ IDeviceScanJobService.getDefaultOptions(packageName) → Scan DefaultOptions
      │       ├─ ScanJob_Create construction:
      │       │   ├─ DestinationOptions.jobStorage = {}  (key!)
      │       │   └─ ScanOptions:
      │       │       ├─ mediaSize = CopyTypeMapping.originalMediaSize.convertWtoE(scanSize)
      │       │       ├─ mediaSource = CopyTypeMapping.originalMediaSource.convertWtoE(scanSource)
      │       │       ├─ colorMode = CopyTypeMapping.colorMode.convertWtoE(colorMode)
      │       │       ├─ contentType = CopyTypeMapping.contentType.convertWtoE(textGraphicsOpt)
      │       │       ├─ contentOrientation = CopyTypeMapping.contentOrientation.convertWtoE(orientation)
      │       │       ├─ plexMode + bindingFormat = CopyTypeMapping.originalPlexMode.convertWtoE(scanDuplex)
      │       │       ├─ imagePreviewMode = CopyTypeMapping.imagePreviewMode.convertWtoE(copyPreview)
      │       │       ├─ scanCaptureMode = CopyTypeMapping.scanCaptureMode.convertWtoE(captureMode)
      │       │       └─ scanProgressMode = CopyTypeMapping.scanProgressMode.convertWtoE(progressDialogMode)
      │       ├─ ScanTicket.name = storeJobName (or timestamp default)
      │       ├─ ScanTicket.folderName = storeJobFolderName
      │       └─ scanJobService.createScanJob(packageName, scanJobCreate)
      │           → E2 POST /ext/scanJob/v1/scanJobAgents/{agentId}/scanJobs
      │           → return scanJobId
      │
      ├─ [4] scanJobId null check → On failure: ReportErrorState + PreferenceStorage.remove(rid)
      │
      ├─ [5] Replace PreferenceStorage key: RID → scanJobId
      │       → StoredCopyJobPreferenceStorage.replaceKey(context, rid, scanJobId)
      │
      ├─ [6] stateMachine.setJobId(scanJobId)
      │
      └─ [7] return MonitoringStoredCopyJobState(scanJobId)
              ↓
[MonitoringStoredCopyJobState] ★ NEW
  ├─ onEnter(): JobletService.startMonitoring + TL_EV_JOB_PROGRESS (inherited)
  ├─ onProcess(): registerNotificationCallback()
  │   └─ scanJobService.registerNotificationCallback(scanNotificationCallback)
  │       → Await E2 websocket ScanNotification
  │
  ├─ On ScanNotification received:
  │   ├─ Extract ScanJobStatus
  │   ├─ updateJobStatus(jobBundle, scanJobStatus)  ★ Scan→Copy state conversion
  │   │   ├─ totalImagesScanned → CopyJobData.setImagesScanned()
  │   │   ├─ scanningActivity → CopyJobState.setScanningState()
  │   │   └─ (no printingState — scan stage only)
  │   └─ processJobDoneStatus(stateMachine, jobDoneStatus) (reuse inherited method)
  │       ├─ JdsSucceeded → MSG_REPORT_COMPLETED
  │       └─ JdsFailed → MSG_REPORT_FAIL
  │
  └─ onExit(): scanJobService.unRegisterNotificationCallback()
        ↓
[CopyJobCompletedState] (existing)
  └─ processCompletedJob() → If mode==null, set STORE
        ↓
[EndState]
```

#### 3.1.2 Classification of Scan Options vs Print Options Within CopyAttributes

| CopyAttributes Field | Attribute | Create(ScanTicket) | Release(CopyOptions) |
|---------------------|------|-------|---------|
| `scanSize` (originalMediaSize) | Scan | ✅ | ❌ |
| `scanSource` (originalMediaSource) | Scan | ✅ | ❌ |
| `scanDuplex` (originalPlexMode) | Scan | ✅ | ❌ |
| `colorMode` | Both | ✅ | ✅ |
| `contentType` | Both | ✅ | ✅ |
| `contentOrientation` | Both | ✅ | ✅ |
| `copyPreview` (imagePreviewMode) | Scan | ✅ | ❌ |
| `captureMode` (scanCaptureMode) | Scan | ✅ | ❌ |
| `progressDialogMode` (scanProgressMode) | Scan | ✅ | ❌ |
| `copies` | Print | ❌ | ✅ |
| `printSize` (outputMediaSize) | Print | ❌ | ✅ |
| `paperSource` (outputMediaSource) | Print | ❌ | ✅ |
| `printDuplex` (outputDuplexBinding) | Print | ❌ | ✅ |
| `paperType` (outputMediaType) | Print | ❌ | ✅ |
| `outputBin` (outputMediaDestination) | Print | ❌ | ✅ |
| `scaleMode` (scaleSelection) | Print | ❌ | ✅ |
| `collateMode` (collationType) | Print | ❌ | ✅ |
| `numberUpMode` (pagesPerSheet) | Print | ❌ | ✅ |
| `storeJobName` | Metadata | ✅ (ticket name) | ❌ |
| `storeJobFolderName` | Metadata | ✅ (ticket folder) | ❌ |

#### 3.1.3 Type Mapping Strategy

When converting CopyAttributes → E2 ScanTicket, **CopyTypeMapping's E2 types use the same E2 enums as ScanTypeMapping**:
- `CopyTypeMapping.originalMediaSize` → E2 `MediaSizeId` (ScanTicket's `mediaSize` is also `MediaSizeId`)
- `CopyTypeMapping.colorMode` → E2 `ColorMode` (ScanTicket's `colorMode` is also `ColorMode`)

Therefore, after obtaining E2 types via `CopyTypeMapping.*.convertWtoE()`, they can be set directly on ScanTicket fields.
However, **since Java field names between ScanTicket and CopyJobTicket may differ**, the actual `com.hp.ext.service.scanJob.ScanOptions` class setters need to be verified.

### 3.2 Enumerate Stored Copy Jobs

```
[App] CopierService.enumerateStoredJob(Context, Result)
  ↓
[WorkpathLib] → ContentProvider.call(ENUMERATE_JOBS, arg, extras)
  ↓
[OXPCopyletContentProvider.call()]
  case ENUMERATE_JOBS:
    → enumerateStoredJobs(appPackageName) ★ NEW method
        ├─ copyDeviceService.enumerateStoredJobs(packageName) ★ NEW interface
        │   → E2 GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
        │   → return StoredJobs (E2 response type)
        │
        ├─ CopyJobAdapter.convertToStoredJobInfoList(storedJobs) ★ NEW
        │   └─ Map each member → StoredJobInfo:
        │       ├─ jobId → storedJobId
        │       ├─ jobName → storedJobName
        │       ├─ folderName → storedJobFolderName
        │       ├─ jobTimestamp → storeJobTimestamp
        │       ├─ jobUserName → storedJobUserName
        │       └─ totalPages → totalPages
        │
        └─ return JsonParser.toJson(storedJobInfoList)
```

### 3.3 Release Stored Copy Job

```
[App] CopierService.releaseStoredJob(Context, StoredJobAttributes{storedJobId, copies})
  ↓
[OXPCopyletReleaseBroadcastReceiver] → CopyJobIntentService.startRelease(bundle)
  ↓
[CreatingCopyJobState.processReleaseJobIntent()] ★ Re-implement existing skeleton
  ├─ ReleaseRequestIntent.getIntentParams(extraParams)
  ├─ Parse StoredJobAttributes (storedJobId, copies, credentials)
  │
  ├─ [1] PreferenceStorage.get(storedJobId) → Restore original CopyAttributes JSON
  │       → If null, return ReportErrorState
  │
  ├─ [2] Create Release JobBundle
  │       → createReleaseCopyJobBundle(clientVersion, storedJobAttributes, savedCopyAttrsJson) ★ NEW
  │       → JobType.COPY, mode=STORE, state=ACTIVE
  │
  ├─ [3] Construct CopyOptions (original attrs + release overrides)
  │       → CopyJobAdapter.buildCopyOptionsForRelease(savedCopyAttrsJson, storedJobAttributes) ★ NEW
  │       │   ├─ Deserialize savedCopyAttrsJson → CopyAttributesReader
  │       │   ├─ storedJobAttributes.getCopies() → Override copies
  │       │   ├─ Reuse existing getCopyJobTicket() logic to create E2 CopyOptions
  │       │   └─ return CopyOptions
  │
  ├─ [4] Call E2 Release API
  │       → copyJobService.releaseStoredJob(packageName, storedJobId, copyOptions) ★ NEW interface
  │       → E2 POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}/release
  │         {"copyOptions":{...}}
  │       → return CopyJob (includes copyJobId)
  │
  ├─ [5] stateMachine.setJobId(copyJobId)
  │
  └─ [6] return MonitoringCopyJobState(copyJobId) ← Reuse existing class
          (After Release, monitoring is done via E2 CopyNotification)
```

**Key Point:** After Release, the same notifications as a regular E2 Copy Job are received, so the existing `MonitoringCopyJobState` is reused as-is.

### 3.4 Delete Stored Copy Job

```
[App] CopierService.deleteStoredJob(Context, jobId, JobCredentialsAttributes, Result)
  ↓
[WorkpathLib] → ContentProvider.call(DELETE_JOB, extras{KEY_STORED_JOB_ID, KEY_DELETE_REQ})
  ↓
[OXPCopyletContentProvider.call()]
  case DELETE_JOB:
    → deleteJob(pi, clientVersion, jobId, intent.getExtras(), bundle)
        ├─ ResultReceiver created (linked with CountDownLatch)
        ├─ CopyJobIntentService.startDelete(context, reqBundle, resultReceiver)
        └─ countDownLatch.await(60, SECONDS)
  ↓
[CreatingCopyJobState.processDeleteJobIntent()] ★ Re-implement
  ├─ Extract EXTRA_RESULT_RECEIVER from Intent ★ Key change
  ├─ Extract storedJobId
  ├─ try:
  │   ├─ copyJobService.deleteStoredJob(packageName, storedJobId) ★ NEW interface
  │   │   → E2 DELETE /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}
  │   ├─ StoredCopyJobPreferenceStorage.remove(context, storedJobId)
  │   ├─ ResultReceiver.send(RESULT_OK, resultBundle) ★ Release CountDownLatch
  │   └─ return EndState()
  └─ catch:
      ├─ ResultReceiver.send(RESULT_FAIL, errorBundle)
      └─ return EndState()
```

**Key Change:** Delete does not require an asynchronous Job lifecycle (Monitoring). After sending the delete request to E2, the result is immediately sent via `ResultReceiver` and transitions to `EndState`. Using the existing `ReportErrorState` would not invoke `ResultReceiver`, so delete-specific result sending logic is required.

### 3.5 Profile & Default Options

**Phase 1 (Current Scope): Maintaining Existing API**
- `getCapabilities()` → Uses only E2 copy profile `"base"` profile (no change)
- `getDefaults()` → Uses E2 copy `defaultOptions` (no change)
- Add `builder.addJobExecutionMode(STORE)` in `CopyOptionProfileAdapter` to indicate STORE mode support

**Phase 2 (Future): New API**
- `getAllCapabilities()` → Combine `"base"` + (`"storedCopy"` ∪ scan `"jobStorage"`)
- `getAllDefaults()` → standard defaultOptions + (copy defaultOptions ∪ scan `"jobStorage"` defaultOptions)

---

## 4. Detailed File Changes

### 4.1 NEW Files

#### 4.1.1 `adapter/StoredCopyJobAdapter.java`

**Location:** `CopyLet/src/.../copylet/adapter/StoredCopyJobAdapter.java`

```java
package com.hp.jetadvantage.link.services.copylet.adapter;

import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;

public class StoredCopyJobAdapter {
    private static final String TAG = "Copylet/SCJAdap";
    
    /**
     * Extracts scan options from CopyAttributes and creates an E2 ScanJob(destination=jobStorage).
     * 
     * @return scanJobId (on success) or null
     */
    public static String createScanJobForStorage(
            IDeviceScanJobService scanJobService,
            String packageName,
            CopyAttributesReader copyAttributes) throws Exception {
        
        // 1. Query E2 scan defaultOptions
        //    → scanJobService.getDefaultOptions(packageName)
        //    → Use defaults corresponding to jobStorage destination
        
        // 2. Construct ScanOptions
        //    → Convert to E2 types via CopyTypeMapping and call ScanOptions setters
        //    ★ Note: ScanOptions setter names need verification
        //      - setMediaSize() or setInputMediaSize()
        //      - setMediaSource() or setInputSource()
        //      - setColorMode()
        //      - setContentType()
        //      - setContentOrientation()
        //      - setPlexMode() + setBindingFormat() or setDuplex()
        
        // 3. Construct DestinationOptions
        //    → DestinationOptions.setJobStorage(new JobStorageDestination())
        
        // 4. Construct ScanTicket
        //    → ScanTicket.setScanOptions(scanOptions)
        //    → ScanTicket.setDestinationOptions(destinationOptions)
        //    → Metadata: set name, folderName
        
        // 5. Create ScanJob_Create and call API
        //    → scanJobService.createScanJob(packageName, scanJobCreate)
        //    → return scanJobId
    }
}
```

**Items to Verify:**
- Exact setter names for scan options in `com.hp.ext.service.scanJob.ScanOptions` class
- How to set jobStorage in `com.hp.ext.service.scanJob.DestinationOptions`
- How to set name/folderName on `ScanTicket` (direct field or within ScanOptions)
- Reference: Verify `DestinationOptions` construction pattern in ScanLet's `ScanTicketAdapter.getScanTicket()`

#### 4.1.2 `service/MonitoringStoredCopyJobState.java`

**Location:** `CopyLet/src/.../copylet/service/MonitoringStoredCopyJobState.java`

```java
package com.hp.jetadvantage.link.services.copylet.service;

import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;

/**
 * Monitoring state used during Stored Copy Job creation.
 * Since an E2 ScanJob is executed internally, ScanNotification is received
 * and converted to CopyJobState/CopyJobData.
 * 
 * Reference Patterns:
 * - MonitoringScanJobState: ScanNotification → ScanJobState (ScanLet)
 * - MonitoringCopyJobState: CopyNotification → CopyJobState (CopyLet)
 * - This class: ScanNotification → CopyJobState (CopyLet — hybrid)
 */
public class MonitoringStoredCopyJobState extends MonitoringJobState {
    private IDeviceScanJobService scanJobService;
    private IE2PayloadCallback<ScanNotification> scanNotificationCallback;
    
    protected MonitoringStoredCopyJobState(String scanJobId) {
        super(scanJobId);
        TAG = TAG + "/SCopy";
        scanJobService = new StandardDeviceScanJobService();
    }
    
    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        scanNotificationCallback = (appPackageId, notification) -> {
            if (appPackageId == null || notification == null) return;
            
            ScanJobNotificationContent content = notification.getJobNotification();
            if (!notification.isJobNotification() || content == null) return;
            
            ScanJobStatus scanJobStatus = content.getScanJobStatus();
            if (scanJobStatus != null) {
                updateJobStatus(stateMachine.getJobBundle(), scanJobStatus);
                processJobDoneStatus(stateMachine, scanJobStatus.getJobDoneStatus());
            }
        };
        scanJobService.registerNotificationCallback(scanNotificationCallback);
    }
    
    @Override
    protected void unregisterNotificationCallback() {
        scanJobService.unRegisterNotificationCallback();
    }
    
    /**
     * Converts ScanJobStatus to CopyJobData and updates JobBundle.
     * Since Stored Copy Job includes only the scan stage:
     * - Only imagesScanned is updated (sheetsPrinted remains 0)
     * - Only scanningActivity is reflected in CopyJobState
     */
    private void updateJobStatus(Bundle jobBundle, ScanJobStatus scanJobStatus) {
        if (jobBundle == null || scanJobStatus == null) return;
        
        // Extract and set totalImagesScanned
        int totalImagesScanned = convertUnsigned32ToInt(scanJobStatus.getTotalImagesScanned());
        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, totalImagesScanned);
        
        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) return;
        
        CopyJobData copyJobData = jobInfo.getJobData();
        copyJobData.setImagesScanned(totalImagesScanned);
        
        // Convert ScanJobStatus → CopyJobState
        CopyJobState jobState = new CopyJobState(CopyJobState.State.ACTIVE);
        // Map scanning activity (refer to MonitoringScanJobState)
        // ★ ScanJobStatus activity field structure needs to be verified
        copyJobData.setJobState(jobState);
    }
}
```

**Differences from MonitoringCopyJobState:**
| Item | MonitoringCopyJobState | MonitoringStoredCopyJobState |
|------|----------------------|----------------------------|
| Device Service | IDeviceCopyJobService | IDeviceScanJobService |
| Notification | CopyNotification | ScanNotification |
| Status | CopyJobStatus | ScanJobStatus |
| Output JobType | COPY | COPY (same — backward compat) |
| Output JobData | CopyJobData | CopyJobData (same) |
| imagesScanned | ✅ | ✅ |
| sheetsPrinted | ✅ | ❌ (scan stage only) |
| scanningActivity | ✅ | ✅ |
| printingActivity | ✅ | ❌ |

#### 4.1.3 `service/StoredCopyJobPreferenceStorage.java`

**Location:** `CopyLet/src/.../copylet/service/StoredCopyJobPreferenceStorage.java`

```java
package com.hp.jetadvantage.link.services.copylet.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persists original CopyAttributes for Stored Copy Jobs.
 * Saved on Create, retrieved on Release, deleted on Delete.
 * 
 * Storage format: { scanJobId : copyAttributesJson }
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
    
    /**
     * On Create: initially saved with RID → after scanJobId obtained, key is replaced
     */
    public static void replaceKey(Context context, String oldKey, String newKey) {
        SharedPreferences prefs = getPrefs(context);
        String value = prefs.getString(oldKey, null);
        if (value != null) {
            prefs.edit()
                .remove(oldKey)
                .putString(newKey, value)
                .apply();
        }
    }
    
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}
```

**Thread Safety Considerations:**
- `SharedPreferences.apply()` reflects immediately in memory, asynchronous to disk — safe within the same process
- `CopyJobIntentService` processes sequentially on a single `HandlerThread`, so no concurrent access
- `replaceKey()` is not atomic, but since processing is single-threaded, this is not an issue

**Error Recovery Considerations:**
- If ScanJob creation fails, data saved with the RID key needs to be cleaned up
- After device reboot, jobs may exist in PreferenceStorage but not on E2 → needs synchronization with enumerate results (future improvement)

### 4.2 UPDATE Files

#### 4.2.1 `DeviceServices/Interfaces/.../IDeviceCopyJobService.java`

```java
// ★ 3 methods to add

/**
 * Query Stored Copy Job list.
 * E2: GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
 */
StoredJobs enumerateStoredJobs(String packageName) throws BoundDeviceException;

/**
 * Release (print) a Stored Copy Job.
 * E2: POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}/release
 * @param copyOptions copy options to apply on release
 * @return created CopyJob info (includes copyJobId)
 */
CopyJob releaseStoredJob(String packageName, String storedJobId, 
                         CopyOptions copyOptions) throws BoundDeviceException;

/**
 * Delete a Stored Copy Job.
 * E2: DELETE /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}
 */
void deleteStoredJob(String packageName, String storedJobId) throws BoundDeviceException;
```

#### 4.2.2 `DeviceServices/Standard/.../StandardDeviceCopyJobService.java`

Follow existing patterns (`createCopyJob`, etc.) for implementation:

```java
// Enumerate — use existing getAsync pattern
public StoredJobs enumerateStoredJobs(String packageName) throws BoundDeviceException {
    // CopyServiceClient client = getClient(packageName);
    // return client.copyAgents().getMember(getAgentId(packageName))
    //     .storedJobs().getAsync(?params?).getResult();
    // ★ Verification needed: Whether storedJobs() method chain exists
    // ★ Alternative: Implement direct HTTP GET call
}

// Release — use existing executeAsync pattern
public CopyJob releaseStoredJob(String packageName, String storedJobId, 
                                CopyOptions copyOptions) throws BoundDeviceException {
    // ★ Verify if ReleaseRequest type exists in E2 library
    // client.copyAgents().getMember(agentId)
    //     .storedJobs().getMember(storedJobId)
    //     .release().executeAsync(releaseRequest).getResult();
}

// Delete
public void deleteStoredJob(String packageName, String storedJobId) throws BoundDeviceException {
    // client.copyAgents().getMember(agentId)
    //     .storedJobs().getMember(storedJobId)
    //     .deleteAsync().getResult();
}
```

#### 4.2.3 `service/CreatingCopyJobState.java`

**Change 1: STORE branch within `processCopyJobIntent()`**

```java
// Existing:
private BaseJobIntentServiceState processCopyJobIntent(
        BaseJobIntentServiceStateMachine stateMachine, Bundle extraParams) throws Exception {
    CopyToRequestIntent.IntentParams reqParams = CopyToRequestIntent.getIntentParams(extraParams);
    // ... validation ...
    CopyAttributesReader copyAttributesReader = new CopyAttributesReader(reqParams.getCopyAttributes());
    int clientVersion = ...;

    // ★ Added: STORE mode branch
    if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
        return processStoredCopyJobIntent(stateMachine, reqParams, copyAttributesReader, clientVersion);
    }

    // Existing Standard Copy flow (no change)
    stateMachine.setJobBundle(createCopyJobBundle(clientVersion, copyAttributesReader));
    // ...
    return createCopyJob(stateMachine, reqParams.getPackageName());
}
```

**Change 2: `processStoredCopyJobIntent()` new method**
(Refer to the sequence in Section 3.1.1)

**Change 3: `processReleaseJobIntent()` re-implementation**
(Refer to the sequence in Section 3.3)
- Currently calls `createCopyJob(stateMachine, packageName)` — this creates a regular E2 copy job, which is completely incorrect behavior
- Replace with: restore original CopyAttributes from PreferenceStorage → call release API

**Change 4: `processDeleteJobIntent()` re-implementation**
(Refer to the sequence in Section 3.4)
- Extract `ResultReceiver` from Intent and send result
- Call E2 delete API and clean up PreferenceStorage

**Change 5: Context Access**
- `processStoredCopyJobIntent()` and `processDeleteJobIntent()` need `Context` (for PreferenceStorage, ScanJobService)
- Accessible via `stateMachine.getContext()` (`getContext()` exists in BaseJobIntentServiceStateMachine)

#### 4.2.4 `adapter/CopyJobAdapter.java`

**Methods to Add:**

```java
/**
 * Converts E2 StoredJobs response to Workpath StoredJobInfo list.
 */
public static List<StoredJobInfo> convertToStoredJobInfoList(StoredJobs storedJobs) {
    // ★ E2 StoredJobs members structure needs verification
    // Enumerate response example from Overview document:
    // {"members": [{"jobId":"", "jobName":"", "folderName":"", 
    //               "jobTimestamp":"", "jobUserName":"", "totalPages":1}]}
    
    // Convert each member → StoredJobInfo.Builder
    // ★ StoredJobInfo has no Builder — verify if direct constructor or Builder addition is needed
}

/**
 * Construct E2 CopyOptions for Release.
 * Extract print-related options from saved original CopyAttributes + override copies.
 */
public static CopyOptions buildCopyOptionsForRelease(
        String savedCopyAttributesJson, 
        StoredJobAttributes storedJobAttributes) throws Exception {
    
    // 1. JSON → CopyAttributesReader
    CopyAttributesReader reader = new CopyAttributesReader(savedCopyAttributesJson);
    
    // 2. Override copies (from StoredJobAttributes)
    // ★ CopyAttributesReader may be immutable → handle separately
    
    // 3. Reuse existing getCopyJobTicket() logic to create CopyOptions
    // ★ getCopyJobTicket() is private static → need to change access level or
    //    include DefaultOptions query within buildCopyOptionsForRelease()
    
    // 4. E2 release API expects {"copyOptions":{...}} format
    //    → Pass the CopyOptions itself, not CopyJobTicket
}
```

**Note:** `getCopyJobTicket()` is private static, so to reuse it, the access level needs to be changed or a common method extracted.

#### 4.2.5 `provider/OXPCopyletContentProvider.java`

```java
// Change: Implement ENUMERATE_JOBS case
case Copylet.Method.ENUMERATE_JOBS:
    resultStr = enumerateStoredJobs(appPackageName);
    break;

// Additional method:
private String enumerateStoredJobs(String packageName) throws SdkException {
    try {
        StoredJobs storedJobs = copyDeviceService.enumerateStoredJobs(packageName);
        List<StoredJobInfo> list = CopyJobAdapter.convertToStoredJobInfoList(storedJobs);
        return JsonParser.getInstance().toJson(list);
    } catch (BoundDeviceException e) {
        throw new SdkConnectionErrorException("Device is not connected");
    } catch (Exception e) {
        throw new SdkServiceErrorException("Failed to enumerate stored jobs: " + e.getMessage());
    }
}
```

#### 4.2.6 `adapter/CopyOptionProfileAdapter.java` (Optional)

**Minimal Change for Current Phase 1:**
```java
// In createCopyAttributeCapsBuilder():
// Existing:
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);

// Changed:
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.STORE); // ★ Add STORE support
```

Without this change, the App may encounter `CapabilitiesExceededException` when calling `StoreCopyBuilder.build(caps)`.

---

## 5. Overall File Change Summary

| # | File Path (relative) | Change | Phase | Description |
|---|-----------------|------|-------|------|
| 1 | `DeviceServices/Interfaces/.../IDeviceCopyJobService.java` | UPDATE | 1 | Add enumerate, release, delete methods |
| 2 | `DeviceServices/Standard/.../StandardDeviceCopyJobService.java` | UPDATE | 1 | E2 REST call implementation |
| 3 | `CopyLet/src/.../service/StoredCopyJobPreferenceStorage.java` | **NEW** | 2 | SharedPreferences CRUD |
| 4 | `CopyLet/src/.../adapter/StoredCopyJobAdapter.java` | **NEW** | 2 | CopyAttrs→ScanTicket(jobStorage) |
| 5 | `CopyLet/src/.../service/MonitoringStoredCopyJobState.java` | **NEW** | 2 | ScanNotification→CopyJobState |
| 6 | `CopyLet/src/.../service/CreatingCopyJobState.java` | UPDATE | 2,4,5 | STORE branch, release, delete implementation |
| 7 | `CopyLet/src/.../adapter/CopyJobAdapter.java` | UPDATE | 3,4 | convertToStoredJobInfoList, buildCopyOptionsForRelease |
| 8 | `CopyLet/src/.../provider/OXPCopyletContentProvider.java` | UPDATE | 3 | ENUMERATE_JOBS implementation |
| 9 | `CopyLet/src/.../adapter/CopyOptionProfileAdapter.java` | UPDATE | 2 | Add STORE JobExecutionMode |

---

## 6. Development Order (Phase-based)

### Phase 1: Device Service Layer
1. `IDeviceCopyJobService` — Add 3 methods
2. `StandardDeviceCopyJobService` — E2 REST implementation
3. **Prerequisite verification:** Whether storedJobs method chain exists in E2 CopyServiceClient

### Phase 2: Create Stored Copy Job
1. `StoredCopyJobPreferenceStorage` — CRUD implementation
2. `CopyOptionProfileAdapter` — Add STORE mode (App builder compatibility)
3. `StoredCopyJobAdapter` — CopyAttrs→ScanTicket conversion + ScanJob creation
4. `MonitoringStoredCopyJobState` — ScanNotification→CopyJobState
5. `CreatingCopyJobState` — STORE branch + `processStoredCopyJobIntent()`

### Phase 3: Enumerate Stored Copy Jobs
1. `CopyJobAdapter.convertToStoredJobInfoList()` — E2→Workpath conversion
2. `OXPCopyletContentProvider` — ENUMERATE_JOBS implementation

### Phase 4: Release Stored Copy Job
1. `CopyJobAdapter.buildCopyOptionsForRelease()` — Release CopyOptions construction
2. `CreatingCopyJobState.processReleaseJobIntent()` — Re-implementation

### Phase 5: Delete Stored Copy Job
1. `CreatingCopyJobState.processDeleteJobIntent()` — E2 delete + ResultReceiver + PreferenceStorage cleanup

### Phase 6: Testing
1. Unit tests (Adapter conversion, PreferenceStorage CRUD)
2. Integration tests (E2 integration)
3. Full scenario: Create → Enumerate → Release → Delete

---

## 7. E2 Library Pre-Verification Checklist

Items that must be verified before starting development:

| # | Item to Verify | How to Verify | Alternative |
|---|----------|----------|------|
| 1 | `CopyServiceClient` has `storedJobs()` method chain | Source/Javadoc of E2 client library | Implement direct REST calls |
| 2 | `StoredJobs` response type exists | Search `com.hp.ext.service.copy` package | Create POJO directly |
| 3 | `StoredJobs.members` field structure | E2 API docs or actual response | Verify with real device test |
| 4 | Release API request type (`ReleaseRequest`?) | E2 client library | Implement direct HTTP POST |
| 5 | `ScanOptions` class setter names | `com.hp.ext.service.scanJob.ScanOptions` source | - |
| 6 | `DestinationOptions` jobStorage setting method | `com.hp.ext.service.scanJob.DestinationOptions` | Refer to ScanLet code |
| 7 | `ScanTicket` name/folderName setting location | ScanTicket class structure | - |
| 8 | `ScanNotification` and `ScanJobStatus` structure | `com.hp.ext.service.scanJob` | Refer to MonitoringScanJobState |
| 9 | `StoredJobInfo` constructor/Builder pattern | JetAdvantageLinkApi source | - |

---

## 8. Risk Factors and Mitigation

| Risk | Impact | Mitigation |
|------|------|------|
| E2 storedJobs client not supported | Phase 1 delay | Implement direct REST calls (refer to DeviceServiceClientImpl pattern) |
| ScanOptions and CopyOptions field name differences | Phase 2 mapping errors | Detailed comparison of ScanTicketAdapter code and E2 ScanOptions Javadoc |
| PreferenceStorage data loss (app update/reboot) | Missing original options on Release | Add PreferenceStorage synchronization logic with enumerate results (after Phase 2) |
| CopyJobCompletedState STORE default setting | Possible malfunction on Create STORE completion | Always explicitly set mode=STORE during STORE create to prevent null |
| Delete's ResultReceiver not being called | ContentProvider 60-second timeout | Implement explicit ResultReceiver.send() in processDeleteJobIntent |

---

## 9. Key Design Decisions

| # | Decision | Rationale |
|---|------|------|
| 1 | Create ScanJob directly within CopyLet (no ScanLet delegation) | Option 2 component definition + ScanLet JobType.SCAN fixed issue |
| 2 | Separate MonitoringStoredCopyJobState class | Different receiving services for ScanNotification/CopyNotification |
| 3 | Persist CopyAttributes via SharedPreferences | JobLet pattern, single-thread safety |
| 4 | Reuse CopyTypeMapping E2 types | E2 imaging/media types are common across Copy/Scan |
| 5 | Delete directly responds via ResultReceiver without asynchronous lifecycle | Delete is a single API call, monitoring is unnecessary |
| 6 | Release reuses existing MonitoringCopyJobState | After Release, E2 CopyJob is created so CopyNotification is received |
| 7 | Add STORE JobExecutionMode to capabilities in Phase 1 | Required for App's StoreCopyBuilder.build(caps) validation to pass |
| 8 | Profile/Options maintains existing API (base only) | Per the Phase separation in the architecture document |
