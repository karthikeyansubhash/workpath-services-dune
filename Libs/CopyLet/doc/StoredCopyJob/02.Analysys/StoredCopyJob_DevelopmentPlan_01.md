# StoredCopyJob Development Plan

> **DUNE-169955: Workpath Stored Copy Job**
> This document is a development execution plan based on the architecture documents (StoredCopyJob_Overview.md, StoredCopyJob_Initial_Investigation.md, PlantUML diagrams) and existing CopyLet code analysis.

---

## 1. Current State Analysis

### 1.1 Existing Standard Copy Job Flow Summary

The Standard Copy Job operates with the following flow:

```
[App] → submit(CopyAttributes)
  → [OXPCopyletBroadcastReceiver] → CopyJobIntentService.startCopy()
    → [CopyJobIntentServiceStateMachine]
      → [CreatingCopyJobState] → processCopyJobIntent()
        → CopyJobAdapter.createCopyJob() → IDeviceCopyJobService.createCopyJob()
          → E2 POST /ext/copy/v1/copyAgents/{agentId}/copyJobs
      → [MonitoringCopyJobState] → registerNotificationCallback()
        → E2 CopyNotification → CopyJobStatus → CopyJobState/CopyJobData
      → [CopyJobCompletedState] → processCompletedJob()
      → [EndState]
```

**Key Component Roles:**

| Component | Role |
|----------|------|
| `OXPCopyletBroadcastReceiver` | Receives Copy submit Intent from the App, calls `CopyJobIntentService.startCopy()` |
| `OXPCopyletReleaseBroadcastReceiver` | Receives Release Intent from the App, calls `CopyJobIntentService.startRelease()` |
| `OXPCopyletContentProvider` | Handles synchronous queries (getCaps, getDefaults, isSupported, enumerate, delete) |
| `CopyJobIntentService` | IntentService-based — creates HandlerThread + StateMachine for asynchronous operations |
| `CopyJobIntentServiceStateMachine` | State machine management (Creating → Monitoring → Completed → End) |
| `CreatingCopyJobState` | Branches to COPY/RELEASE/DELETE based on `PARAMS_TYPE`, creates JobBundle, creates E2 job |
| `CopyJobAdapter` | Converts Workpath CopyAttributes → E2 CopyJobTicket and calls E2 API |
| `CopyDeviceAdapter` | Reverse converts E2 DefaultOptions/Profile → Workpath CopyAttributes |
| `CopyOptionProfileAdapter` | Converts E2 Profile → Workpath CopyAttributesCaps |
| `MonitoringCopyJobState` | Receives E2 CopyNotification → updates CopyJobState/CopyJobData |
| `CopyJobCompletedState` | Post-processing of completed Jobs |

### 1.2 Already Existing StoredCopyJob Infrastructure

Code analysis reveals that significant infrastructure for StoredCopyJob is already in place:

**✅ Already Implemented:**
- `CopyJobIntentService` - `PARAMS_TYPE_RELEASE`, `PARAMS_TYPE_DELETE` constants and `startRelease()`, `startDelete()` methods
- `CreatingCopyJobState` - `processReleaseJobIntent()`, `processDeleteJobIntent()` methods (skeleton)
- `OXPCopyletReleaseBroadcastReceiver` - Release Intent reception and routing
- `OXPCopyletContentProvider` - `DELETE_JOB`, `ENUMERATE_JOBS` method cases (skeleton)
- `CopyAttributes.JobExecutionMode` - `NORMAL`, `STORE` enumerations
- `CopyAttributes.StoreCopyBuilder` - `setStoreJobName()`, `setStoreJobFolderName()`, etc.
- `StoredJobAttributes` - `StoredJobBuilder(storedJobId)`, `setCopies()`, `setJobCredentials()`
- `StoredJobInfo` - Stored job list information class
- `ReleaseRequestIntent` / `DeleteRequestIntent` - Intent parameter classes
- `Copylet.Method.ENUMERATE_JOBS`, `Copylet.Method.DELETE_JOB` - API method constants
- `Copylet.Keys.KEY_STORED_JOB_ID`, `KEY_DELETE_REQ` - Key constants
- ScanLet's `IDeviceScanJobService` - jobStorage destination support

**❌ Not Implemented (remains as TODO):**
- `StoredCopyJobAdapter` (New) - Copy → Scan attribute conversion adapter
- `IDeviceCopyJobService` - No stored job enumerate, release, delete methods
- `StandardDeviceCopyJobService` - Implementation of the above methods
- `CreatingCopyJobState.processCopyJobIntent()` - `JobExecutionMode.STORE` branch handling
- `processReleaseJobIntent()` - Actual release implementation (currently calls regular `createCopyJob`)
- `processDeleteJobIntent()` - Actual delete implementation (currently returns NOT_SUPPORTED error)
- `deleteStoredCopyJob()` - Not implemented
- Preference Storage - StoredCopyJobMap `<JobId, CopyJobIntent>` persistence
- `OXPCopyletContentProvider.ENUMERATE_JOBS` - Not implemented (NOT_SUPPORTED)
- Scan → Copy Job state conversion (converting Scan notifications to Copy Job)
- Stored copy distinction in Profile/DefaultOptions

---

## 2. StoredCopyJob Feature Development Items

### 2.1 Create Stored Copy Job

**Design Principle (Option 2: Centralized Logic):**
When the App submits with `CopyAttributes(JobExecutionMode.STORE)`, the Copy Service internally creates an E2 ScanJob (destination: jobStorage). The original CopyAttributes are saved to PreferenceStorage for use during Release.

**Sequence:**
```
[App] submit(CopyAttributes{mode=STORE})
  → [OXPCopyletBroadcastReceiver]
    → CopyJobIntentService.startCopy(bundle)
      → [CreatingCopyJobState.processCopyJobIntent()]
        → JobExecutionMode == STORE detected
        → createCopyJobBundle(clientVersion, copyAttributesReader) // JobType.COPY, mode=STORE
        → createStoredCopyJob(stateMachine, packageName) // ★ New flow
          → StoredCopyJobAdapter.extractScanAttributes(CopyAttributesReader) // ★ New: Copy→Scan option extraction
          → StoredCopyJobAdapter.createScanJobForJobStorage(scanJobService, packageName, scanAttributes) // ★ New: E2 ScanJob creation
            → IDeviceScanJobService.createScanJob(packageName, ScanJob_Create{destination=jobStorage})
              → E2 POST /ext/scanJob/v1/scanJobAgents/{agentId}/scanJobs
          → scanJobId returned
          → PreferenceStorage.save(scanJobId, CopyAttributes) // ★ New: Save original copy options
          → return MonitoringStoredCopyJobState(scanJobId) // ★ New: Convert Scan notifications to Copy state
```

**Files to Modify/Create:**

| File | Change Type | Content |
|------|-----------|------|
| `adapter/StoredCopyJobAdapter.java` | **NEW** | CopyAttributes → E2 ScanTicket conversion, jobStorage destination configuration |
| `service/CreatingCopyJobState.java` | UPDATE | Add STORE mode branch in `processCopyJobIntent()`, add `createStoredCopyJob()` method |
| `service/MonitoringStoredCopyJobState.java` | **NEW** | Monitoring that converts E2 ScanNotification → CopyJobState/CopyJobData |
| `service/StoredCopyJobPreferenceStorage.java` | **NEW** | `<JobId, CopyAttributes>` mapping SharedPreferences persistence |

### 2.2 Enumerate Stored Copy Jobs

**Sequence:**
```
[App] enumerateStoredJob(Context, Result)
  → [WorkpathLib] ContentProvider.call(ENUMERATE_JOBS)
    → [OXPCopyletContentProvider.call()]
      → case ENUMERATE_JOBS:
        → IDeviceCopyJobService.enumerateStoredJobs(packageName) // ★ New interface method
          → E2 GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
        → CopyJobAdapter.convertToStoredJobInfoList(e2Response) // ★ New: E2→Workpath conversion
      → return List<StoredJobInfo> as JSON
```

**Files to Modify/Create:**

| File | Change Type | Content |
|------|-----------|------|
| `IDeviceCopyJobService.java` | UPDATE | Add `enumerateStoredJobs()` method |
| `StandardDeviceCopyJobService.java` | UPDATE | E2 REST call implementation (GET .../storedJobs?includeMembers) |
| `provider/OXPCopyletContentProvider.java` | UPDATE | Implement `ENUMERATE_JOBS` case |
| `adapter/CopyJobAdapter.java` | UPDATE | E2 StoredJob response → `StoredJobInfo` list conversion |

### 2.3 Release Stored Copy Job

**Sequence:**
```
[App] releaseStoredJob(Context, StoredJobAttributes{jobId, copies})
  → [OXPCopyletReleaseBroadcastReceiver]
    → CopyJobIntentService.startRelease(bundle)
      → [CreatingCopyJobState.processReleaseJobIntent()]
        → StoredJobAttributes parsing
        → PreferenceStorage.get(storedJobId) → Restore saved CopyAttributes // ★ New
        → CopyJobAdapter.releaseCopyJob(copyJobService, storedJobId, copyAttributes) // ★ New
          → IDeviceCopyJobService.releaseStoredJob(packageName, jobId, copyOptions) // ★ New interface
            → E2 POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}/release
              {"copyOptions":{...}}
        → return MonitoringCopyJobState(copyJobId) // Reuse existing Copy notification monitoring
```

**Files to Modify/Create:**

| File | Change Type | Content |
|------|-----------|------|
| `IDeviceCopyJobService.java` | UPDATE | Add `releaseStoredJob()` method |
| `StandardDeviceCopyJobService.java` | UPDATE | E2 REST call implementation (POST .../storedJobs/{id}/release) |
| `service/CreatingCopyJobState.java` | UPDATE | Actual implementation of `processReleaseJobIntent()` |
| `adapter/CopyJobAdapter.java` | UPDATE | Add `releaseCopyJob()` method — create copy ticket and call E2 |
| `service/StoredCopyJobPreferenceStorage.java` | UPDATE | Retrieve and delete saved CopyAttributes |

### 2.4 Delete Stored Copy Job

**Sequence:**
```
[App] deleteStoredJob(Context, jobId, JobCredentialsAttributes, Result)
  → [WorkpathLib] ContentProvider.call(DELETE_JOB, extras{jobId, credentials})
    → [OXPCopyletContentProvider.call()]
      → case DELETE_JOB:
        → CopyJobIntentService.startDelete(context, reqBundle, resultReceiver)
          → [CreatingCopyJobState.processDeleteJobIntent()]
            → IDeviceCopyJobService.deleteStoredJob(packageName, jobId) // ★ New interface
              → E2 DELETE /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}
            → PreferenceStorage.remove(jobId) // ★ New: Delete saved options too
            → Return result via ResultReceiver
```

**Files to Modify/Create:**

| File | Change Type | Content |
|------|-----------|------|
| `IDeviceCopyJobService.java` | UPDATE | Add `deleteStoredJob()` method |
| `StandardDeviceCopyJobService.java` | UPDATE | E2 REST call implementation (DELETE .../storedJobs/{id}) |
| `service/CreatingCopyJobState.java` | UPDATE | Actual implementation of `deleteStoredCopyJob()` |
| `service/StoredCopyJobPreferenceStorage.java` | UPDATE | Delete saved CopyAttributes |

### 2.5 Profile & Default Options (Existing API — Phase 1)

**Current Scope (Maintaining Existing API):**
The existing `getCapabilities()` and `getDefaults()` provide Standard Copy Profile only. In the existing API, only the `"base"` profile of the E2 copy profile is used.

```
[App] getCapabilities()
  → [OXPCopyletContentProvider] → GET_CAPS
    → CopyOptionProfileAdapter.getCaps() → E2 GET /ext/copy/v1/profile
    → profile.getBase() used (no change)
    → return CopyAttributesCaps
```

> **Note:** Future New API (getAllCapabilities, getAllDefaults) will be handled in a separate Phase. At that time, the E2 scan profile's `"jobStorage"` + E2 copy profile's `"storedCopy"` will be combined to construct a Stored Copy-specific profile.

---

## 3. Detailed File Changes

### 3.1 NEW Files

#### 3.1.1 `adapter/StoredCopyJobAdapter.java` (New)

**Purpose:** When creating a Stored Copy Job, convert Workpath CopyAttributes to an E2 ScanJob request

**Class Design:**
```java
package com.hp.jetadvantage.link.services.copylet.adapter;

public class StoredCopyJobAdapter {
    
    /**
     * Extracts scan-related options from CopyAttributes, creates an E2 ScanTicket,
     * and creates an E2 ScanJob with the destination set to jobStorage.
     *
     * @param scanJobService  scan device service
     * @param packageName     app package name
     * @param copyAttributes  original Copy attributes received from the app
     * @return created scanJobId
     */
    public static String createScanJobForStorage(
            IDeviceScanJobService scanJobService,
            String packageName,
            CopyAttributesReader copyAttributes) throws Exception {
        // 1. Query E2 scan defaultOptions (jobStorage profile)
        // 2. Extract scan-related options from CopyAttributes:
        //    - originalMediaSize → scanOptions.inputMediaSize
        //    - originalMediaSource → scanOptions.inputSource
        //    - originalPlexMode → scanOptions.plexMode
        //    - colorMode → scanOptions.colorMode  
        //    - contentType → scanOptions.contentType
        //    - contentOrientation → scanOptions.orientation
        //    - storeJobName → scanTicket.name
        //    - storeJobFolderName → scanTicket.folderName
        // 3. Create ScanJob_Create (destination: jobStorage)
        // 4. scanJobService.createScanJob(packageName, scanJobCreate)
        // 5. return scanJobId
    }
}
```

**Reference Pattern:** Refer to the structure of `ScanTicketAdapter.createScanJob()`, but set the destination to jobStorage and use scan-related type mapping instead of CopyTypeMapping.

**Notes:**
- CopyAttributes contains both scan options and print options
- Only scan-related options should be extracted and set in the ScanTicket (Print options are saved in PreferenceStorage)
- E2 ScanJob types use the `com.hp.ext.service.scanJob.*` package

#### 3.1.2 `service/MonitoringStoredCopyJobState.java` (New)

**Purpose:** When creating a Stored Copy Job, receive E2 ScanNotification and convert it to Workpath CopyJobState/CopyJobData

**Class Design:**
```java
package com.hp.jetadvantage.link.services.copylet.service;

/**
 * Monitoring state that converts ScanNotification to CopyJobState.
 * The existing MonitoringCopyJobState handles CopyNotification,
 * but during Stored Copy Job creation, a ScanJob is executed internally,
 * so ScanNotification must be received and converted to CopyJobState.
 */
public class MonitoringStoredCopyJobState extends MonitoringJobState {
    private IDeviceScanJobService scanJobService;
    private IE2PayloadCallback<ScanNotification> scanNotificationCallback;

    protected MonitoringStoredCopyJobState(String scanJobId) {
        super(scanJobId);
    }

    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        // Register ScanNotification callback
        // Convert ScanJobStatus → CopyJobState
        // - scanningActivity → CopyJobState.scanningState
        // - jobDoneStatus handling (reuse existing processJobDoneStatus)
        // Update CopyJobData:
        //   - totalImagesScanned → imagesScanned
    }

    @Override
    protected void unregisterNotificationCallback() {
        // Unregister ScanNotification callback
    }
}
```

**Reference Pattern:** Combination of `MonitoringScanJobState`'s ScanNotification processing logic + `MonitoringCopyJobState`'s CopyJobState update logic

**Notes:**
- `MonitoringCopyJobState` receives CopyNotification from `IDeviceCopyJobService`
- `MonitoringStoredCopyJobState` receives ScanNotification from `IDeviceScanJobService`
- However, the JobBundle's JobType is `COPY` and JobData is `CopyJobData` (for App compatibility)

#### 3.1.3 `service/StoredCopyJobPreferenceStorage.java` (New)

**Purpose:** Persist StoredCopyJobMap `<JobId, CopyAttributes>`

**Class Design:**
```java
package com.hp.jetadvantage.link.services.copylet.service;

/**
 * Saves/retrieves/deletes original CopyAttributes for Stored Copy Jobs
 * using SharedPreferences.
 * - Key: scanJobId (or storedJobId)
 * - Value: CopyAttributes JSON string
 * 
 * On Create: save(RID → CopyAttributes), then replace key after scanJobId is obtained
 * On Release: get(storedJobId) → Restore CopyAttributes
 * On Delete: remove(storedJobId)
 */
public class StoredCopyJobPreferenceStorage {
    private static final String PREF_FILE_NAME = "stored_copy_job_prefs";

    public static void save(Context context, String jobId, String copyAttributesJson) { }
    public static String get(Context context, String jobId) { }
    public static void remove(Context context, String jobId) { }
    public static void replaceKey(Context context, String oldKey, String newKey) { }
}
```

**Reference Pattern:** SharedPreferences usage pattern from `JobDataContentProvider` (`mJobDataSP`)

**Key Management During Create Flow:**
1. App submit → RID created → `save(RID, copyAttributesJson)`
2. E2 ScanJob created → scanJobId obtained  
3. `replaceKey(RID, scanJobId)` — Replace RID with actual scanJobId
4. On Release, retrieve by storedJobId (= scanJobId)

### 3.2 UPDATE Files

#### 3.2.1 `DeviceServices/Interfaces/.../IDeviceCopyJobService.java`

**Methods to Add:**
```java
// Stored Job Enumerate
StoredJobs enumerateStoredJobs(String packageName) throws BoundDeviceException;

// Stored Job Release  
CopyJob releaseStoredJob(String packageName, String storedJobId, CopyOptions copyOptions) throws BoundDeviceException;

// Stored Job Delete
void deleteStoredJob(String packageName, String storedJobId) throws BoundDeviceException;
```

> **Note:** The `StoredJobs` response type may already be defined in the E2 copy client library. If not, it needs to be created.

#### 3.2.2 `DeviceServices/Standard/.../StandardDeviceCopyJobService.java`

**Implementation to Add (E2 REST Endpoints):**
```java
// Enumerate: GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers
public StoredJobs enumerateStoredJobs(String packageName) {
    // client.copyAgents().getMember(agentId).storedJobs().getAsync(...)
}

// Release: POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}/release
public CopyJob releaseStoredJob(String packageName, String storedJobId, CopyOptions copyOptions) {
    // client.copyAgents().getMember(agentId).storedJobs().getMember(jobId).release().executeAsync(...)
}

// Delete: DELETE /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}
public void deleteStoredJob(String packageName, String storedJobId) {
    // client.copyAgents().getMember(agentId).storedJobs().getMember(jobId).deleteAsync(...)
}
```

> **Verification Needed:** Check whether the E2 Copy Service Client library (`com.hp.ext.clients.device.CopyServiceClient`) already supports `storedJobs()` related method chains. If not, an E2 library update may be required.

#### 3.2.3 `service/CreatingCopyJobState.java`

**Change 1: `processCopyJobIntent()` — Add STORE mode branch**
```java
private BaseJobIntentServiceState processCopyJobIntent(...) throws Exception {
    // ... existing code ...
    CopyAttributesReader copyAttributesReader = new CopyAttributesReader(reqParams.getCopyAttributes());
    
    // ★ Added: Branch based on JobExecutionMode
    if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
        return processStoredCopyJobIntent(stateMachine, reqParams, copyAttributesReader, clientVersion);
    }
    
    // Existing Standard Copy flow
    stateMachine.setJobBundle(createCopyJobBundle(clientVersion, copyAttributesReader));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(), reqParams.getPackageName()));
    stateMachine.setJobAttributesReader(copyAttributesReader);
    return createCopyJob(stateMachine, reqParams.getPackageName());
}
```

**Change 2: `processStoredCopyJobIntent()` new method**
```java
private BaseJobIntentServiceState processStoredCopyJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        CopyToRequestIntent.IntentParams reqParams,
        CopyAttributesReader copyAttributesReader,
        int clientVersion) throws Exception {
    
    // 1. Create JobBundle (JobType.COPY, mode=STORE)
    stateMachine.setJobBundle(createCopyJobBundle(clientVersion, copyAttributesReader));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(), reqParams.getPackageName()));
    stateMachine.setJobAttributesReader(copyAttributesReader);
    
    // 2. Save original CopyAttributes to PreferenceStorage (key: RID)
    String rid = reqParams.getReqId();
    StoredCopyJobPreferenceStorage.save(context, rid, reqParams.getCopyAttributes());
    
    // 3. Create E2 ScanJob via StoredCopyJobAdapter
    IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
    String scanJobId = StoredCopyJobAdapter.createScanJobForStorage(
            scanJobService, reqParams.getPackageName(), copyAttributesReader);
    
    if (scanJobId == null || scanJobId.isEmpty()) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                "Failed to create stored copy job");
    }
    
    // 4. Replace PreferenceStorage key (RID → scanJobId)
    StoredCopyJobPreferenceStorage.replaceKey(context, rid, scanJobId);
    
    // 5. Set jobId in stateMachine
    stateMachine.setJobId(scanJobId);
    
    // 6. Transition to state that monitors ScanNotification
    return new MonitoringStoredCopyJobState(scanJobId);
}
```

**Change 3: `processReleaseJobIntent()` actual implementation**
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
    int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : storedJobAttributes.getVersion();
    
    // 1. Restore saved original CopyAttributes from PreferenceStorage
    String savedCopyAttributesJson = StoredCopyJobPreferenceStorage.get(context, storedJobId);
    if (savedCopyAttributesJson == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "Stored copy attributes not found for jobId: " + storedJobId);
    }
    
    // 2. Create Release JobBundle
    //    — Apply copies from StoredJobAttributes to update original CopyAttributes
    stateMachine.setJobBundle(createReleaseCopyJobBundle(clientVersion, storedJobAttributes, savedCopyAttributesJson));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(), reqParams.getPackageName()));
    
    // 3. Call E2 release via CopyJobAdapter
    CopyOptions copyOptions = CopyJobAdapter.buildCopyOptionsForRelease(savedCopyAttributesJson, storedJobAttributes);
    CopyJob copyJob = copyJobService.releaseStoredJob(reqParams.getPackageName(), storedJobId, copyOptions);
    
    if (copyJob == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                "Failed to release stored copy job");
    }
    
    String copyJobId = copyJob.getCopyJobId().getValue().toString();
    stateMachine.setJobId(copyJobId);
    
    // 4. Transition to existing MonitoringCopyJobState (E2 Copy notification monitoring)
    return new MonitoringCopyJobState(copyJobId);
}
```

**Change 4: `processDeleteJobIntent()` / `deleteStoredCopyJob()` actual implementation**
```java
private BaseJobIntentServiceState processDeleteJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams) throws Exception {
    
    DeleteRequestIntent.IntentParams reqParams = DeleteRequestIntent.getIntentParams(extraParams);
    if (reqParams == null || !extraParams.containsKey(Copylet.Keys.KEY_STORED_JOB_ID)) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "Stored JobID not found");
    }
    
    String storedJobId = extraParams.getString(Copylet.Keys.KEY_STORED_JOB_ID);
    String packageName = reqParams.getPackageName();
    
    // 1. Delete stored job via E2
    copyJobService.deleteStoredJob(packageName, storedJobId);
    
    // 2. Also delete from PreferenceStorage
    StoredCopyJobPreferenceStorage.remove(context, storedJobId);
    
    // 3. Return result (via ResultReceiver)
    // ResultReceiver is set in OXPCopyletContentProvider.deleteJob()
    return new EndState(); // Or appropriate success state
}
```

#### 3.2.4 `provider/OXPCopyletContentProvider.java`

**Change: `ENUMERATE_JOBS` case implementation**
```java
case Copylet.Method.ENUMERATE_JOBS:
    resultStr = enumerateStoredJobs(appPackageName);
    break;
```

**Additional Method:**
```java
private String enumerateStoredJobs(String packageName) throws SdkException {
    try {
        StoredJobs storedJobs = copyDeviceService.enumerateStoredJobs(packageName);
        List<StoredJobInfo> storedJobInfoList = CopyJobAdapter.convertToStoredJobInfoList(storedJobs);
        return JsonParser.getInstance().toJson(storedJobInfoList);
    } catch (BoundDeviceException e) {
        throw new SdkConnectionErrorException("Device is not connected");
    } catch (Exception e) {
        throw new SdkServiceErrorException("Failed to enumerate stored jobs: " + e.getMessage());
    }
}
```

#### 3.2.5 `adapter/CopyJobAdapter.java`

**Additional Methods:**
```java
/**
 * Converts E2 StoredJobs response to Workpath StoredJobInfo list
 */
public static List<StoredJobInfo> convertToStoredJobInfoList(StoredJobs storedJobs) {
    // E2 StoredJobs.members → List<StoredJobInfo> conversion
    // Map from each member: jobId, jobName, folderName, jobTimestamp, jobUserName, totalPages
}

/**
 * Creates CopyOptions for Release
 * Combines saved original CopyAttributes with StoredJobAttributes (copies, etc.)
 */
public static CopyOptions buildCopyOptionsForRelease(
        String savedCopyAttributesJson,
        StoredJobAttributes storedJobAttributes) {
    // 1. Deserialize savedCopyAttributesJson → CopyAttributes
    // 2. Apply override attributes from storedJobAttributes such as copies
    // 3. Convert CopyAttributes → E2 CopyOptions (reuse existing getCopyJobTicket logic)
}
```

#### 3.2.6 `service/CopyJobIntentServiceStateMachine.java`

**Change: Verify `createCreatingJobState()`**
The current implementation returns `new CreatingCopyJobState(intent)`, so no existing code changes are needed. Changes will be handled within `CreatingCopyJobState` internal modifications.

**Possible Change: Update possibleNextStates**
Since `MonitoringStoredCopyJobState` is added as a new state, it may need to be allowed in the state transition validity check.

---

## 4. Development Order (Work Process)

### Phase 1: Device Service Layer (Foundation Work)
> Add stored job methods to the IDeviceCopyJobService interface and implement E2 REST calls in StandardDeviceCopyJobService.

**Step 1.1** Update `IDeviceCopyJobService` interface
- Add `enumerateStoredJobs()`, `releaseStoredJob()`, `deleteStoredJob()`

**Step 1.2** Implement `StandardDeviceCopyJobService`
- Implement E2 REST endpoint calls (enumerate, release, delete)
- Need to verify storedJobs-related API chain in E2 client library

### Phase 2: Create Stored Copy Job
> Core feature — When App submits in STORE mode, internally creates an E2 ScanJob.

**Step 2.1** Implement `StoredCopyJobPreferenceStorage`
- SharedPreferences-based CRUD (save, get, remove, replaceKey)

**Step 2.2** Implement `StoredCopyJobAdapter`
- CopyAttributes → E2 ScanTicket(destination=jobStorage) conversion
- IDeviceScanJobService call

**Step 2.3** Implement `MonitoringStoredCopyJobState`
- E2 ScanNotification reception → CopyJobState/CopyJobData conversion
- Combination of `MonitoringScanJobState` + `MonitoringCopyJobState` patterns

**Step 2.4** Update `CreatingCopyJobState`
- Add STORE mode branch in `processCopyJobIntent()`
- Implement `processStoredCopyJobIntent()`

### Phase 3: Enumerate Stored Copy Jobs  
> Synchronous query functionality via ContentProvider

**Step 3.1** Implement `CopyJobAdapter` — `convertToStoredJobInfoList()`

**Step 3.2** Implement `OXPCopyletContentProvider` — `ENUMERATE_JOBS` case

### Phase 4: Release Stored Copy Job
> Core feature to select and print a stored Job

**Step 4.1** Implement `CopyJobAdapter` — `buildCopyOptionsForRelease()`

**Step 4.2** Actual implementation of `CreatingCopyJobState` — `processReleaseJobIntent()`
- Restore original CopyAttributes from PreferenceStorage
- Call E2 release API
- Transition to existing MonitoringCopyJobState

### Phase 5: Delete Stored Copy Job

**Step 5.1** Actual implementation of `CreatingCopyJobState` — `processDeleteJobIntent()` / `deleteStoredCopyJob()`
- Call E2 delete API
- Clean up PreferenceStorage

### Phase 6: Testing & Integration

**Step 6.1** Write unit tests
- `StoredCopyJobAdapter` conversion logic tests
- `StoredCopyJobPreferenceStorage` CRUD tests
- `CopyJobAdapter.convertToStoredJobInfoList()` conversion tests
- `CopyJobAdapter.buildCopyOptionsForRelease()` conversion tests

**Step 6.2** Integration tests
- E2 real device/simulator integration tests
- Full Create → Enumerate → Release → Delete scenario tests

---

## 5. Overall File Change Summary

| # | File Path | Change Type | Phase |
|---|----------|-----------|-------|
| 1 | `DeviceServices/Interfaces/.../IDeviceCopyJobService.java` | UPDATE | 1 |
| 2 | `DeviceServices/Standard/.../StandardDeviceCopyJobService.java` | UPDATE | 1 |
| 3 | `CopyLet/src/.../service/StoredCopyJobPreferenceStorage.java` | **NEW** | 2 |
| 4 | `CopyLet/src/.../adapter/StoredCopyJobAdapter.java` | **NEW** | 2 |
| 5 | `CopyLet/src/.../service/MonitoringStoredCopyJobState.java` | **NEW** | 2 |
| 6 | `CopyLet/src/.../service/CreatingCopyJobState.java` | UPDATE | 2, 4, 5 |
| 7 | `CopyLet/src/.../adapter/CopyJobAdapter.java` | UPDATE | 3, 4 |
| 8 | `CopyLet/src/.../provider/OXPCopyletContentProvider.java` | UPDATE | 3 |

---

## 6. Dependencies and Items Requiring Verification

### 6.1 E2 Client Library Verification Needed

Verify client library support for the following E2 REST endpoints:

| Endpoint | Client Method Chain | Verification Needed |
|-----------|-------------------|-----------|
| `GET /ext/copy/v1/copyAgents/{id}/storedJobs?includeMembers` | `copyAgents().getMember(id).storedJobs().getAsync(...)` | ⚠️ |
| `POST /ext/copy/v1/copyAgents/{id}/storedJobs/{jid}/release` | `...storedJobs().getMember(jid).release().executeAsync(...)` | ⚠️ |
| `DELETE /ext/copy/v1/copyAgents/{id}/storedJobs/{jid}` | `...storedJobs().getMember(jid).deleteAsync(...)` | ⚠️ |
| `POST /ext/scanJob/v1/scanJobAgents/{id}/scanJobs` (jobStorage dest) | Already supported ✅ | - |

If `storedJobs()` related method chains are missing from the E2 Copy Service Client:
- Direct REST call implementation may be needed in `com.hp.ext.clients.device.CopyServiceClient` (or similar class)
- Or verify an E2 library version update

### 6.2 E2 Response Data Type Verification

| Type | Package | Verification Needed |
|------|--------|-----------|
| `StoredJobs` (enumerate response) | `com.hp.ext.service.copy.*` | ⚠️ |
| `StoredJobMember` (individual stored job info) | `com.hp.ext.service.copy.*` | ⚠️ |
| How to set jobStorage destination in ScanJob_Create | `com.hp.ext.service.scanJob.*` | ⚠️ |

### 6.3 Scan Type Mapping

When creating a Stored Copy Job, scan-related options from CopyAttributes need to be converted to E2 ScanTicket options. Required mappings:

| CopyAttributes (Workpath) | ScanTicket (E2) | Existing Mapping? |
|---------------------------|-----------------|----------------|
| `originalMediaSize` (ScanSize) | `inputMediaSize` (MediaSizeId) | ✅ CopyTypeMapping |
| `originalMediaSource` (ScanSource) | `inputSource` (MediaInputId) | ✅ CopyTypeMapping |
| `originalPlexMode` (Duplex) | `plexMode` (PlexMode) | ✅ CopyTypeMapping |
| `colorMode` (ColorMode) | `colorMode` (ColorMode) | ✅ CopyTypeMapping |
| `contentType` (TextGraphicsOptimization) | `contentType` (DocumentContentType) | ✅ CopyTypeMapping |
| `contentOrientation` (Orientation) | `contentOrientation` (ContentOrientation) | ✅ CopyTypeMapping |
| `storeJobName` | `scanTicket.name` | - (set directly) |
| `storeJobFolderName` | `scanTicket.folderName` | - (set directly) |

> Since `convertWtoE()` defined in `CopyTypeMapping` produces E2 types, `StoredCopyJobAdapter` can reuse CopyTypeMapping. However, since ScanTicket field names may differ from CopyJobTicket, verification is needed.

---

## 7. Architecture Diagram Supplement

### 7.1 Create Stored Copy Job — Detailed Component Flow

```
App
 │ submit(CopyAttributes{mode=STORE})
 ▼
OXPCopyletBroadcastReceiver
 │ CopyJobIntentService.startCopy(bundle)
 ▼
CopyJobIntentService (HandlerThread)
 │ createStateMachine() → CopyJobIntentServiceStateMachine
 ▼
CopyJobIntentServiceStateMachine
 │ MSG_START → CreatingCopyJobState
 ▼
CreatingCopyJobState
 │ initializeJob()
 │ ├─ setTargetPackage()
 │ ├─ isSupported()
 │ ├─ PARAMS_TYPE == COPY
 │ └─ processCopyJobIntent()
 │     ├─ JobExecutionMode == STORE → processStoredCopyJobIntent()
 │     │   ├─ createCopyJobBundle(STORE)
 │     │   ├─ StoredCopyJobPreferenceStorage.save(RID, attrs)
 │     │   ├─ StoredCopyJobAdapter.createScanJobForStorage()
 │     │   │   ├─ extractScanAttributes(CopyAttributes)
 │     │   │   ├─ buildScanTicket(destination=jobStorage)
 │     │   │   └─ IDeviceScanJobService.createScanJob()
 │     │   │       └─ E2: POST /ext/scanJob/v1/.../scanJobs
 │     │   ├─ PreferenceStorage.replaceKey(RID → scanJobId)
 │     │   └─ return MonitoringStoredCopyJobState(scanJobId)
 │     │
 │     └─ JobExecutionMode == NORMAL → (existing Standard Copy flow)
 ▼
MonitoringStoredCopyJobState (NEW)
 │ registerNotificationCallback()
 │ ├─ IDeviceScanJobService.registerNotificationCallback()
 │ ├─ ScanNotification received
 │ ├─ ScanJobStatus → CopyJobState conversion
 │ └─ CopyJobData update (imagesScanned)
 │ processJobDoneStatus()
 │ ├─ Succeeded → MSG_REPORT_COMPLETED
 │ └─ Failed → MSG_REPORT_FAIL
 ▼
CopyJobCompletedState
 │ processCompletedJob()
 ▼
EndState
```

### 7.2 Release Stored Copy Job — Detailed Component Flow

```
App
 │ releaseStoredJob(StoredJobAttributes{jobId, copies})
 ▼
OXPCopyletReleaseBroadcastReceiver
 │ CopyJobIntentService.startRelease(bundle)
 ▼
CopyJobIntentServiceStateMachine
 │ MSG_START → CreatingCopyJobState
 ▼
CreatingCopyJobState
 │ initializeJob()
 │ ├─ PARAMS_TYPE == RELEASE
 │ └─ processReleaseJobIntent()
 │     ├─ StoredJobAttributes parsing
 │     ├─ PreferenceStorage.get(storedJobId) → original CopyAttributes
 │     ├─ CopyJobAdapter.buildCopyOptionsForRelease(attrs, storedAttrs)
 │     ├─ IDeviceCopyJobService.releaseStoredJob(pkg, jobId, copyOptions)
 │     │   └─ E2: POST /ext/copy/v1/.../storedJobs/{id}/release
 │     └─ return MonitoringCopyJobState(copyJobId) ← existing class reused
 ▼
MonitoringCopyJobState (existing)
 │ CopyNotification received → CopyJobState update
 ▼
CopyJobCompletedState
 ▼
EndState
```

---

## 8. Key Design Decision Summary

| # | Decision | Reason |
|---|------|------|
| 1 | Option 2: Centralized Logic (handled within Copy Service) | Architecture document decision. Concentrate Copy-related logic in Copy Service |
| 2 | ScanNotification for Create, CopyNotification for Release | Create internally uses ScanJob so ScanNotification is needed. Release uses CopyJob so existing CopyNotification is reused |
| 3 | Persist CopyAttributes via SharedPreferences | Follows existing JobLet's JobDataContentProvider pattern. Simple and sufficient performance |
| 4 | Separate MonitoringStoredCopyJobState into its own class | Since existing MonitoringCopyJobState handles CopyNotification, a separate class is needed for ScanNotification → CopyJobState conversion |
| 5 | Reuse existing CopyTypeMapping | E2 types are the same during Copy→Scan attribute conversion, so existing mappings can be leveraged |
| 6 | Profile/DefaultOptions maintains existing API (Phase 1) | Future getAllCapabilities/getAllDefaults will be implemented in a separate Phase |
