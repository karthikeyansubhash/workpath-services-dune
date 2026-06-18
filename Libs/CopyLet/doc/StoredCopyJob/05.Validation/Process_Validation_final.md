# Stored Copy Job Process Final Analysis (Final — Cross-Validation Complete)

Analysis Date: 2026-04-01Cross-Validation Sources:

- `Process_Validation_1.md` — 1st analysis (Claude Sonnet 4)
- `Process_Validation_2.md` — 2nd analysis (separate session)
- `Process_Validation_3.md` — 3rd analysis (Claude Opus 4.6, for cross-validation)

---

## Cross-Validation Summary

### Points of Agreement Across 3 Analyses

- **Store/Enumerate/Release full code flow**: All 3 documents derived the same flow. Function call order, parameters, and branching logic all match.
- **Implementation assessment**: All 3 documents assessed as "**implemented correctly as designed**".
- **Issue #1 (Key match verification)**: All 3 documents identified the same issue — UUID match between scanJobId and storedJob.jobId.
- **Issue #4 (Prefs not cleaned on monitoring failure)**: All 3 documents identified the same issue.

### Differences Between Analyses and Resolution

| Item                       | process_summary.md (1st)                                       | 09.ProcessSummary.md (2nd)                   | process_summary_2.md (3rd)                      | Final Verdict                                            |
| -------------------------- | -------------------------------------------------------------- | -------------------------------------------- | ----------------------------------------------- | ---------------------------------------------------- |
| Issue #2 (duplex)          | Pointed out potential mismatch between `getPrintDuplex()` and `getScanDuplex()` | Not mentioned                                    | Same finding + additional E2 outputSides field name analysis     | **Retained** — detailed analysis below                     |
| Issue #3 (both null)       | Identified                                                         | Not mentioned (only DefaultOptions fallback described)       | Same finding + fix code provided                      | **Retained** — edge case but defensive code recommended     |
| Issue #5 (Post-Release cleanup) | Not mentioned                                                      | Pointed out PreferenceStorage not cleaned after Release     | Same finding + confirmed already handled in Delete         | **Low** — not critical since handled in Delete |
| createCopyJobBundle semantics | Not separately mentioned                                                 | Analyzed as Issue #3 → concluded **working correctly** | bundleMode analysis included → concluded **working correctly** | **Closed** — working correctly                               |
| Context passing               | Not separately mentioned                                                 | Verified as Issue #4 → concluded **correctly implemented**   | Null check inclusion confirmed                             | **Closed** — working correctly                               |

---

## 1. Overall Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│ App Layer                                                                   │
│   CopyAttributes(JobExecutionMode=STORE) → startCopy()                     │
│   StoredJobAttributes(storedJobId, copies) → startRelease()                │
│   ContentResolver.call(ENUMERATE_JOBS) → enumerateStoredJobs()             │
│   ContentResolver.call(DELETE_JOB)     → deleteStoredJob()                 │
└───┬────────────────────────┬────────────────────────┬──────────┬────────────┘
    │ STORE                  │ RELEASE                │ ENUMERATE│ DELETE
    ▼                        ▼                        │          │
┌─────────────────────── CopyJobIntentService ─────── │ ─────────│────────────┐
│  startCopy()              startRelease()             │   startDelete()       │
│  PARAMS_TYPE_COPY         PARAMS_TYPE_RELEASE        │   PARAMS_TYPE_DELETE  │
└───┬────────────────────────┬────────────────────────┘│──────────┬────────────┘
    │                        │                         │          │
    ▼                        ▼                         ▼          ▼
┌─────────────── CreatingCopyJobState ──────── OXPCopyletContentProvider ─────┐
│  processCopyJobIntent()   processReleaseJobIntent() call(ENUMERATE_JOBS)    │
│    └→ processStoredCopyJobIntent()                  call(DELETE_JOB)        │
│                           processDeleteJobIntent()   → deleteJob()          │
└───┬────────────────────────┬────────────────────────┬──────────┬────────────┘
    │                        │                        │          │
    ▼                        ▼                        ▼          ▼
┌── StoredCopyJobAdapter  CopyJobAdapter ─────── CopyJobAdapter  E2 Remove ──┐
│  createScanJobForStorage  getCopyJobTicketForRelease  convertTo...  delete  │
│  (E2 ScanJob POST)       (E2 Release POST)   (E2 GET storedJobs)  (E2 POST)│
└───┬────────────────────────┬────────────────────────┬──────────┬────────────┘
    │                        │                        │          │
    ▼                        ▼                        ▼          ▼
┌── StoredCopyJobPreferenceStorage ──────────────────────────────────────────┐
│  saveCopyAttributes(rid)   getCopyAttributes(storedJobId)  remove(jobId)   │
│  replaceKey(rid→scanJobId)                                                 │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Store Process (STORE Mode Copy → E2 ScanJob → PreferenceStorage Save)

### 2.1 Trigger

App sets `CopyAttributes.setJobExecutionMode(STORE)` then calls `CopyJobIntentService.startCopy(context, bundle)`.

### 2.2 Detailed Code Flow

```
[Step 1] CopyJobIntentService.startCopy(context, bundle)
  File: CopyJobIntentService.java
  Method: startCopy(Context, Bundle) — static
  Action: createCopyIntent(context, bundle, "paramsCopy", null) → start()

[Step 2] CreatingCopyJobState.initializeJob(intent, stateMachine)
  File: CreatingCopyJobState.java
  Action: intent.getStringExtra(PARAMS_TYPE) → "paramsCopy"
        → switch → processCopyJobIntent(stateMachine, extraParams)

[Step 3] processCopyJobIntent(stateMachine, extraParams)
  File: CreatingCopyJobState.java
  Action:
    CopyToRequestIntent.IntentParams reqParams = CopyToRequestIntent.getIntentParams(extraParams);
    CopyAttributesReader copyAttributesReader = new CopyAttributesReader(reqParams.getCopyAttributes());
    int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : copyAttributesReader.getVersion();
  
    // Key branching
    if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
        return processStoredCopyJobIntent(stateMachine, reqParams, clientVersion, copyAttributesReader);
    }
    // If not STORE, follows normal copy flow (createCopyJob)

[Step 4] processStoredCopyJobIntent(stateMachine, reqParams, clientVersion, copyAttributesReader)
  File: CreatingCopyJobState.java

  [4-a] Job Bundle setup (for app callback)
        stateMachine.setJobBundle(createCopyJobBundle(clientVersion, copyAttributesReader, NORMAL));
        → Internally, if reader.getJobExecutionMode() == STORE:
          mJobInfo.setJobName(storeJobName)
          mCopyJobData.setJobExecutionMode(STORE)
        → Correctly delivered to app as jobExecutionMode=STORE ✅

  [4-b] Save CopyAttributes to SharedPreferences
        StoredCopyJobPreferenceStorage.saveCopyAttributes(context, rid, copyAttributes)
        → Key: rid (request UUID issued by app)
        → Value: CopyAttributes → Parcel.writeToParcel() → parcel.marshall() → Base64.encodeToString()
        → Saved before ScanJob creation (pre-preservation) ✅

  [4-c] E2 ScanJob creation (JobStorage destination)
        IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        String scanJobId = StoredCopyJobAdapter.createScanJobForStorage(scanJobService, packageName, copyAttributesReader);
    
        Inside StoredCopyJobAdapter:
          buildScanTicket(copyAttributes):
            buildScanOptions(copyAttributes) — 9 scan-related attributes:
              ┌─────────────────────────────────┬──────────────────────────┬──────────────────────────┐
              │ Workpath Attribute               │ E2 ScanOptions Attribute  │ CopyTypeMapping Mapping    │
              ├─────────────────────────────────┼──────────────────────────┼──────────────────────────┤
              │ getColorMode()                  │ setColorMode()           │ colorMode                │
              │ getOrientation()                │ setContentOrientation()  │ contentOrientation       │
              │ getTextGraphicsOptimization()   │ setContentType()         │ contentType              │
              │ getCopyPreview()                │ setImagePreviewMode()    │ imagePreviewMode         │
              │ getScanSize()                   │ setMediaSize()           │ originalMediaSize        │
              │ getScanSource()                 │ setMediaSource()         │ originalMediaSource      │
              │ getScanDuplex()                 │ setPlexMode()+MediaBF()  │ originalPlexMode         │
              │ getCaptureMode()                │ setScanCaptureMode()     │ scanCaptureMode          │
              │ getProgressDialogMode()         │ setScanProgressMode()    │ scanProgressMode         │
              └─────────────────────────────────┴──────────────────────────┴──────────────────────────┘
            buildJobStorageDestination(copyAttributes) — storage settings:
              ┌─────────────────────────────────┬──────────────────────────┬──────────────────────────┐
              │ Item                              │ Source                    │ Condition                 │
              ├─────────────────────────────────┼──────────────────────────┼──────────────────────────┤
              │ jobName                         │ getStoreJobName()/timestamp│ Always                    │
              │ folderName                      │ getStoreJobFolderName()  │ Only when not empty        │
              │ jobPassword                     │ credentials.password     │ Only when not empty        │
              │ jobPasswordType                 │ credentials.passwordType │ Always (default SjptNone) │
              └─────────────────────────────────┴──────────────────────────┴──────────────────────────┘
      
          scanJobService.createScanJob(packageName, scanJobCreate)
          → return scanJob.getScanJobId().getValue().toString()  // UUID string

        ⚠️ Print-related attributes (copies, printSize, printDuplex, paperSource, etc.)
           are NOT included in the ScanJob. They are preserved only in PreferenceStorage
           and restored during Release.

  [4-d] Cleanup on ScanJob creation failure
        scanJobId == null → StoredCopyJobPreferenceStorage.remove(context, rid) → ReportErrorState ✅

  [4-e] Key replacement on success
        StoredCopyJobPreferenceStorage.replaceKey(context, rid, scanJobId)
        → prefs.edit().remove(rid).putString(scanJobId, value).apply()
        → Subsequent Enumerate/Release can look up using scanJobId key

[Step 5] return MonitoringStoredCopyJobState(scanJobId)
  File: MonitoringStoredCopyJobState.java
  Action:
    scanJobService.registerNotificationCallback() → receive ScanNotification
    ScanJobStatus → CopyJobState conversion:
      scanningActivity → scanningState
      processingActivity → processingState
      transmittingActivity → printingState (storage completion stage mapped to printing)
      cancelingActivity → cancelingState
    sheetsPrinted = 0 (scan only)
    processJobDoneStatus():
      JdsSucceeded → MSG_REPORT_COMPLETED
      JdsFailed → MSG_REPORT_FAIL
      JdsCanceled → MSG_REPORT_CANCELLED
```

### 2.3 Store Process Implementation Assessment (3 Analyses Consensus)

| Verification Item                    | Result  | 3 Analyses Consensus       |
| -------------------------------- | ------- | -------------------------- |
| STORE mode branching                 | ✅ Normal | All agree                  |
| CopyAttributes pre-save             | ✅ Normal | All agree                  |
| 9 scan attributes → E2 ScanOptions | ✅ Normal | All agree (same attribute list) |
| JobStorage destination setup         | ✅ Normal | All agree                  |
| RID→scanJobId key replacement       | ✅ Normal | All agree                  |
| RID cleanup on failure               | ✅ Normal | All agree                  |
| App callback (jobExecutionMode=STORE)| ✅ Normal | 2nd/3rd explicitly confirmed |
| ScanNotification monitoring          | ✅ Normal | All agree                  |

---

## 3. Enumerate Process (Stored Job List Query + savedAttributes Merge)

### 3.1 Trigger

App calls `ContentResolver.call(Copylet.CONTENT_OXP_URI, Method.ENUMERATE_JOBS, ...)`.

### 3.2 Detailed Code Flow

```
[Step 1] OXPCopyletContentProvider.call() → ENUMERATE_JOBS case
  File: OXPCopyletContentProvider.java
  Action:
    StoredJobs storedJobs = copyDeviceService.enumerateStoredJobs(appPackageName);
    List<StoredJobInfo> storedJobInfoList =
        CopyJobAdapter.convertToStoredJobInfoList(getContext(), storedJobs, copyDeviceService, appPackageName);
    bundle.putParcelableArrayList(Result.KEY_RESULT, new ArrayList<>(storedJobInfoList));

[Step 2] E2 StoredJobs collection query
  copyDeviceService.enumerateStoredJobs(appPackageName)
  → E2 GET /ext/copy/v1/copyAgents/{agentId}/storedJobs
  → StoredJobs response (summary only, detailed fields may be absent)

[Step 3] CopyJobAdapter.convertToStoredJobInfoList(context, storedJobs, copyJobService, packageName)
  File: CopyJobAdapter.java

  For each StoredJob member:

  [3-a] fetchStoredJobDetails(copyJobService, packageName, summaryJob)
        → E2 GET /storedJobs/{jobId} (individual detail query)
        → Detailed fields: colorMode, originalMediaSize, outputSides, copies, totalPages, etc.
        → On failure, uses summary as-is (fallback) ✅

  [3-b] Look up savedAttributes from PreferenceStorage
        if (context != null && detailedJob != null && detailedJob.getJobId() != null) {
            savedAttributes = StoredCopyJobPreferenceStorage.getCopyAttributes(
                context, detailedJob.getJobId().toString());
        }
        → Key: detailedJob.getJobId().toString()
        → CopyJobIdentifier.toString() → java.util.UUID.toString() (UUID string)

  [3-c] convertStoredJob(detailedJob, savedAttributes)
    
        E2 field extraction:
        ┌──────────────────────────┬────────────────────────────┬───────────────────────────────────────┬──────────┐
        │ E2 StoredJob Field        │ Extraction Method            │ Conversion Logic                        │ Default   │
        ├──────────────────────────┼────────────────────────────┼───────────────────────────────────────┼──────────┤
        │ getJobId()               │ .toString()                │ Direct                                  │ ""       │
        │ getFolderName()          │ nullToEmpty()              │ null → ""                               │ ""       │
        │ getJobName()             │ nullToEmpty()              │ null → ""                               │ ""       │
        │ getJobUserName()         │ nullToEmpty()              │ null → ""                               │ ""       │
        │ getJobTimestamp()        │ nullToEmpty()              │ null → ""                               │ ""       │
        │ getJobPasswordType()     │ convertPasswordType()      │ SjptNumericPIN→NUMERIC, etc.            │ NONE     │
        │ getTotalPages()          │ extractTotalPages()        │ .getValue().intValue()                  │ 0        │
        │ getCopies()              │ extractCopies()            │ .getValue().intValue()                  │ 1        │
        │ getColorMode()           │ extractColorMode()         │ CopyTypeMapping.colorMode.convertEtoW() │ DEFAULT  │
        │ getOriginalMediaSize()   │ extractScanSize()          │ CopyTypeMapping.originalMediaSize.cEW() │ DEFAULT  │
        │ getOutputSides()         │ extractDuplex()            │ CopyTypeMapping.originalPlexMode.cEW()  │ NONE     │
        └──────────────────────────┴────────────────────────────┴───────────────────────────────────────┴──────────┘

        savedAttributes override:
        ┌──────────────────────────┬─────────────────────────────┬─────────────────────────────────┐
        │ Field                     │ Override Method               │ Override Condition                 │
        ├──────────────────────────┼─────────────────────────────┼─────────────────────────────────┤
        │ colorMode                │ resolveSavedColorMode()     │ reader.getColorMode() != DEFAULT│
        │ scanSize                 │ resolveSavedScanSize()      │ reader.getScanSize() != DEFAULT │
        │ duplex                   │ resolveSavedDuplex()        │ reader.getPrintDuplex() != null │
        │ copies                   │ resolveSavedCopies()        │ reader.getCopies() > 0          │
        └──────────────────────────┴─────────────────────────────┴─────────────────────────────────┘
        ⚠️ duplex override uses getPrintDuplex() — see Issue #2

        → StoredJobInfo creation:
        new StoredJobInfo(jobId, folderName, jobName, userName, passwordType, timestamp,
                          copies, colorMode, scanSize, duplex, totalPages)

[Step 4] bundle.putParcelableArrayList(Result.KEY_RESULT, new ArrayList<>(storedJobInfoList))
  → Returns List<StoredJobInfo> to app
```

### 3.3 Enumerate Process Implementation Assessment (3 Analyses Consensus)

| Verification Item                      | Result        | 3 Analyses Consensus                        |
| ---------------------------------- | ------------- | ------------------------------------ |
| E2 summary + individual detail GET     | ✅ Normal     | All agree                            |
| PreferenceStorage lookup (null-safe)   | ✅ Normal     | 2nd/3rd confirmed                    |
| E2→Workpath field conversion          | ✅ Normal     | All agree (same mapping table)       |
| savedAttributes override              | ✅ Normal     | All agree (only non-DEFAULT values)  |
| duplex override source                | ⚠️ Review needed | 1st/3rd flagged, 2nd not mentioned → Issue #2 |

---

## 4. Release Process (Stored Job → Print as E2 CopyJob)

### 4.1 Trigger

App calls `CopyJobIntentService.startRelease(context, bundle)`.
`bundle` contains `StoredJobAttributes` (storedJobId, copies override, credentials included).

### 4.2 Detailed Code Flow

```
[Step 1] CopyJobIntentService.startRelease(context, bundle)
  File: CopyJobIntentService.java
  Action: createCopyIntent(context, bundle, "paramsRelease", null) → start()

[Step 2] CreatingCopyJobState.initializeJob()
  mParamsType == "paramsRelease" → processReleaseJobIntent(stateMachine, extraParams)

[Step 3] processReleaseJobIntent(stateMachine, extraParams)
  File: CreatingCopyJobState.java

  [3-a] Parameter extraction
        ReleaseRequestIntent.IntentParams reqParams = ReleaseRequestIntent.getIntentParams(extraParams);
        StoredJobAttributes storedJobAttributes = reqParams.getStoredJobAttributes();
        String storedJobId = storedJobAttributes.getStoredJobId();
        int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : storedJobAttributes.getVersion();
        String packageName = reqParams.getPackageName();

  [3-b] Restore saved CopyAttributes
        CopyAttributes savedCopyAttributes = StoredCopyJobPreferenceStorage.getCopyAttributes(
            stateMachine.getContext(), storedJobId);
        → Key: storedJobId (StoredJobInfo.jobId received by app from Enumerate)
        → If null, uses DefaultOptions fallback

  [3-c] Job Bundle setup (for app callback)
        CopyAttributesReader releaseReader = savedCopyAttributes != null
            ? new CopyAttributesReader(savedCopyAttributes) : null;
        stateMachine.setJobBundle(createCopyJobBundle(clientVersion, releaseReader, STORE));
        → bundleMode=STORE → isStoredRelease=true
        → mCopyJobData.setJobExecutionMode(STORE) hard-coded
        → jobName: storeJobName or "[Stored Job]"

  [3-d] E2 DefaultOptions query
        DefaultOptions defaultOptions = copyJobService.getDefaultOptions(packageName);

  [3-e] CopyOptions build
        buildReleaseCopyOptions(releaseRequest, savedCopyAttributes, storedJobAttributes, defaultOptions, storedJobId)
    
        Branching:
        ┌─────────────────────────────────────────┬────────────────────────────────────────────────────┐
        │ Condition                                 │ Action                                              │
        ├─────────────────────────────────────────┼────────────────────────────────────────────────────┤
        │ savedAttrs != null && defaultOpts != null│ getCopyJobTicketForRelease() → 18 attribute conversion │
        │ savedAttrs == null && defaultOpts != null│ convertDefaultOptionsToCopyOptions() → JSON fallback   │
        │ Both null                                 │ setCopyOptions() not called ⚠️ (Issue #3)            │
        └─────────────────────────────────────────┴────────────────────────────────────────────────────┘

        Normal path: getCopyJobTicketForRelease(savedCopyAttributes, releaseCopies, defaultOptions)
          → getCopyJobTicket(reader, defaultOptions) — 18 attribute conversion:
          ┌────┬───────────────────────────────┬─────────────────────────────┬────────────────────────┐
          │ #  │ Workpath (reader)              │ E2 CopyOptions               │ CopyTypeMapping         │
          ├────┼───────────────────────────────┼─────────────────────────────┼────────────────────────┤
          │ 1  │ getCopies()                   │ setCopies()                 │ copies                 │
          │ 2  │ getColorMode()                │ setColorMode()              │ colorMode              │
          │ 3  │ getOrientation()              │ setContentOrientation()     │ contentOrientation     │
          │ 4  │ getPrintSize()                │ setOutputMediaSize()        │ outputMediaSize        │
          │ 5  │ getPaperSource()              │ setOutputMediaSource()      │ outputMediaSource      │
          │ 6  │ getScaleMode()                │ setScaleSelection()         │ scaleSelection         │
          │ 7  │ getScanDuplex()               │ setOriginalPlexMode()+BF() │ originalPlexMode       │
          │ 8  │ getScanSize()                 │ setOriginalMediaSize()      │ originalMediaSize      │
          │ 9  │ getScanSource()               │ setOriginalMediaSource()    │ originalMediaSource    │
          │ 10 │ getCopyPreview()              │ setImagePreviewMode()       │ imagePreviewMode       │
          │ 11 │ getPrintDuplex()              │ setOutputDuplexBinding()    │ outputDuplexBinding    │
          │ 12 │ getCollateMode()              │ setCollationType()          │ collationType          │
          │ 13 │ getPaperType()                │ setOutputMediaType()        │ outputMediaType        │
          │ 14 │ getTextGraphicsOptimization() │ setContentType()            │ contentType            │
          │ 15 │ getNumberUpMode()             │ setPagesPerSheet()          │ pagesPerSheet          │
          │ 16 │ getOutputBin()                │ setOutputMediaDestination() │ outputMediaDestination │
          │ 17 │ getProgressDialogMode()       │ setScanProgressMode()       │ scanProgressMode       │
          │ 18 │ getCaptureMode()              │ setScanCaptureMode()        │ scanCaptureMode        │
          └────┴───────────────────────────────┴─────────────────────────────┴────────────────────────┘
      
          → copies override: if releaseCopies > 0, overwrite ticket.getCopyOptions().setCopies()

        Fallback path: convertDefaultOptionsToCopyOptions(defaultOptions)
          → ObjectMapper.convertValue(defaultOptions, ObjectNode) → remove("$opMeta","links")
          → ObjectMapper.readValue(json, CopyOptions.class)
          → JSON-based conversion (device defaults)

  [3-f] Password setting
        applyReleaseJobPassword(releaseRequest, storedJobAttributes)
        → credentials.passwordType != NONE && != null → releaseRequest.setJobPassword(password)
        → If NONE, password not set (prevents E2 rejection) ✅

  [3-g] E2 Release API call
        StoredJob_Release releaseResult = copyJobService.releaseStoredJob(packageName, storedJobId, releaseRequest);
        → E2 POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}/release
        → releaseResult.getCopyJob() → obtain copyJobId

[Step 4] return MonitoringCopyJobState(copyJobId)
  File: MonitoringCopyJobState.java
  Action: CopyNotification received → CopyJobStatus → scan+print progress reporting
```

### 4.3 Release Process Implementation Assessment (3 Analyses Consensus)

| Verification Item                       | Result      | 3 Analyses Consensus              |
| ----------------------------------- | ----------- | -------------------------- |
| savedCopyAttributes restoration        | ✅ Normal   | All agree                  |
| 18-attribute CopyOptions conversion    | ✅ Normal   | All agree (same attribute list) |
| copies override (releaseCopies > 0)    | ✅ Normal   | All agree                  |
| DefaultOptions fallback (JSON conversion) | ✅ Normal | All agree                  |
| Password handling (not set if NONE)    | ✅ Normal   | All agree                  |
| Both null case                          | ⚠️ Not handled | 1st/3rd flagged → Issue #3   |
| Print monitoring                        | ✅ Normal   | All agree                  |

---

## 5. Delete Process (Delete Stored Job)

### 5.1 Two Paths Exist for Delete

| Path | Trigger | Entry Point | Processing Location |
|------|--------|--------|-----------|
| **Explicit Delete** | App calls `ContentProvider.call(DELETE_JOB)` | `OXPCopyletContentProvider` → `CopyJobIntentService.startDelete()` | `CreatingCopyJobState.processDeleteJobIntent()` |
| **Auto Delete (DeleteOnRelease)** | `RetentionModeOnRelease == DELETE` during Release | `processReleaseJobIntent()` → info stored in extraJobBundle | `CopyJobCompletedState.deleteStoredJobOnRelease()` |

---

### 5.2 Explicit Delete Process

#### 5.2.1 Trigger

App calls `ContentResolver.call(Copylet.CONTENT_OXP_URI, Copylet.Method.DELETE_JOB, ...)`.
`bundle` contains `KEY_STORED_JOB_ID` (ID of target to delete) and `KEY_DELETE_REQ` (Intent, containing `DeleteRequestIntent.IntentParams` inside).

#### 5.2.2 Detailed Code Flow

```
[Step 1] OXPCopyletContentProvider.call() → DELETE_JOB case
  File: OXPCopyletContentProvider.java
  Action:
    Extract KEY_STORED_JOB_ID, KEY_DELETE_REQ(Intent) from extras
    → deleteJob(pi, clientVersion, jobId, intent.getExtras(), bundle)

[Step 2] deleteJob() — ResultReceiver + CountDownLatch pattern
  File: OXPCopyletContentProvider.java
  Action:
    Create CountDownLatch(1)
    Create ResultReceiver → on onReceiveResult, Result.parse() → Result.pack(bundle) → countDown()
    Add KEY_STORED_JOB_ID to reqBundle
    CopyJobIntentService.startDelete(context, reqBundle, resultReceiver)
    countDownLatch.await(60, TimeUnit.SECONDS)  ← 60-second timeout
    On timeout → Result.pack(RESULT_FAIL, SYSTEM_ERROR, "Request timed out")

[Step 3] CopyJobIntentService.startDelete(context, bundle, resultReceiver)
  File: CopyJobIntentService.java
  Action:
    createCopyIntent(context, bundle, "paramsDelete", resultReceiver)
    → Intent.putExtra(PARAMS_TYPE, "paramsDelete")
    → Intent.putExtra(EXTRA_PARAMS, bundle)        ← Nested Bundle (reqParams)
    → Intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver)  ← ★ Stored directly at Intent level
    start(context, intent, CopyJobIntentService.class)

[Step 4] CreatingCopyJobState.initializeJob(intent, stateMachine)
  File: CreatingCopyJobState.java
  Action: mParamsType == "paramsDelete"
        → switch → processDeleteJobIntent(stateMachine, extraParams, intent)
        ★ Key: intent passed along (because ResultReceiver is at Intent level)

[Step 5] processDeleteJobIntent(stateMachine, extraParams, intent)
  File: CreatingCopyJobState.java

  [5-a] ResultReceiver extraction
        ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        ★ Extracted directly from intent, not extraParams — Intent structure:
          Intent
           ├─ PARAMS_TYPE (String) = "paramsDelete"
           ├─ EXTRA_PARAMS (Bundle): Nested Bundle — DeleteRequestIntent.IntentParams
           └─ EXTRA_RESULT_RECEIVER (ResultReceiver): ← Intent level ★

  [5-b] Parameter extraction and validation
        DeleteRequestIntent.IntentParams reqParams = DeleteRequestIntent.getIntentParams(extraParams)
        String storedJobId = extraParams.getString(Copylet.Keys.KEY_STORED_JOB_ID)
        String packageName = reqParams.getPackageName()
        → If storedJobId is null/empty → sendResultToReceiver(CANCELED) → EndState

  [5-c] Password handling
        RemoveStoredJobRequest removeRequest = new RemoveStoredJobRequest();
        applyRemoveJobPassword(removeRequest, reqParams);
        
        Inside applyRemoveJobPassword:
        ┌─────────────────────────────┬──────────────────────────────────────┐
        │ Condition                     │ Action                                │
        ├─────────────────────────────┼──────────────────────────────────────┤
        │ credentials == null          │ Password not set (log: No password)   │
        │ passwordType == null/NONE    │ Password not set (log: No password)   │
        │ passwordType != NONE          │ removeRequest.setJobPassword(pwd)    │
        └─────────────────────────────┴──────────────────────────────────────┘
        → Uses reqParams.getJobCredentialsAttributes() (password provided by app)

  [5-d] E2 Delete API call
        StoredJob_Remove removeResult = copyJobService.deleteStoredJob(packageName, storedJobId, removeRequest);
        → E2 POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}/remove
        → Request Body: {"jobPassword":"..."} (if password exists) or {} (if none)

  [5-e] Success handling
        removeResult != null:
          StoredCopyJobPreferenceStorage.remove(context, storedJobId)  ← SharedPreferences cleanup
          Result.pack(resultBundle, RESULT_OK)
          sendResultToReceiver(resultReceiver, Activity.RESULT_OK, resultBundle)
        
        removeResult == null (E2 response parsing failure, etc.):
          Result.pack(errorBundle, RESULT_FAIL, SERVICE_ERROR, ...)
          sendResultToReceiver(resultReceiver, Activity.RESULT_CANCELED, errorBundle)

  [5-f] Exception handling
        catch (Exception e):
          Result.pack(errorBundle, RESULT_FAIL, SERVICE_ERROR, e.getMessage())
          sendResultToReceiver(resultReceiver, Activity.RESULT_CANCELED, errorBundle)

[Step 6] return EndState()
  → Delete does not require async Monitoring → immediate transition to EndState
  → ResultReceiver.onReceiveResult() → releases CopyletContentProvider's CountDownLatch
  → Result returned to app
```

#### 5.2.3 Intent Structure and ResultReceiver Location

```
Intent (CopyJobIntentService.startDelete)
 ├─ PARAMS_TYPE (String) = "paramsDelete"
 ├─ EXTRA_PARAMS (Bundle):
 │   ├─ KEY_STORED_JOB_ID (String): storedJobId of delete target
 │   └─ DeleteRequestIntent.IntentParams:
 │       ├─ packageName (String)
 │       └─ JobCredentialsAttributes: passwordType + password
 └─ EXTRA_RESULT_RECEIVER (ResultReceiver) ← Connected to ContentProvider's CountDownLatch
```

⚠️ **Key Design**: `ResultReceiver` is stored directly at the **Intent level**, not in the `EXTRA_PARAMS` Bundle.
`extraParams.getParcelable(EXTRA_RESULT_RECEIVER)` **returns null**, so it must be extracted using `intent.getParcelableExtra()`.

---

### 5.3 Auto Delete (DeleteOnRelease) Process

#### 5.3.1 Trigger

Automatic deletion when releasing a Stored Job configured with `CopyAttributes.Builder.setRetentionModeOnRelease(RetentionMode.DELETE)`.

#### 5.3.2 Detailed Code Flow

```
[Step 1] processReleaseJobIntent() — Store deletion info
  File: CreatingCopyJobState.java
  Action: After successful Release E2 API call, before transitioning to MonitoringCopyJobState
  
    storeDeleteOnReleaseInfo(stateMachine, savedCopyAttributes, storedJobAttributes, packageName, storedJobId)
    
    Internal:
      savedCopyAttributes == null → return (deletion not needed)
      CopyAttributesReader.getStoredJobRetentionModeOnRelease() != DELETE → return
      
      If DELETE, store 4 keys in extraJobBundle:
      ┌───────────────────────────────────┬──────────┬────────────────────────────────────┐
      │ Key                                 │ Type      │ Value                               │
      ├───────────────────────────────────┼──────────┼────────────────────────────────────┤
      │ KEY_DELETE_ON_RELEASE             │ boolean  │ true                               │
      │ KEY_DELETE_STORED_JOB_ID          │ String   │ storedJobId                        │
      │ KEY_DELETE_PACKAGE_NAME           │ String   │ packageName                        │
      │ KEY_DELETE_PASSWORD               │ String   │ credentials.getStoreJobPassword()  │
      └───────────────────────────────────┴──────────┴────────────────────────────────────┘
      
      Password storage condition:
        credentials != null
        && passwordType != null
        && passwordType != NONE
        → If password is null, stores "" (empty string)

[Step 2] MonitoringCopyJobState(copyJobId) — Copy progress monitoring
  File: MonitoringCopyJobState.java
  Action: CopyNotification-based scan+print progress reporting
        JdsSucceeded/JdsPartiallySucceeded → MSG_REPORT_COMPLETED
        → Transition to CopyJobCompletedState

[Step 3] CopyJobCompletedState.processCompletedJob() — Perform actual deletion
  File: CopyJobCompletedState.java
  Action:
    1. Set JobExecutionMode (existing logic)
    2. deleteStoredJobOnRelease(stateMachine)

    Inside deleteStoredJobOnRelease:
      Check KEY_DELETE_ON_RELEASE in extraJobBundle
      → false/not present → return (normal Release, deletion not needed)

      Extract storedJobId, packageName, password

      IDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
      RemoveStoredJobRequest removeRequest = new RemoveStoredJobRequest();
      password != null && !isEmpty → removeRequest.setJobPassword(password)
      → E2 POST /storedJobs/{storedJobId}/remove (with password)
      → On success: StoredCopyJobPreferenceStorage.remove(context, storedJobId)
      → On failure: SLog.e() (exception caught, does not affect Copy result)

[Step 4] → EndState
```

#### 5.3.3 Timing Design Rationale

```
CreatingCopyJobState (Release)
  ├── Release API call success → CopyJob created
  ├── storeDeleteOnReleaseInfo() → Store deletion info in extraJobBundle
  │                                (Deletion NOT executed here ★)
  └── → MonitoringCopyJobState (Copy progress monitoring)
           │   Scanning → Printing in progress...
           └── MSG_REPORT_COMPLETED (Copy complete)
                └── → CopyJobCompletedState
                       ├── processCompletedJob() : Set JobExecutionMode.STORE
                       └── deleteStoredJobOnRelease() : E2 deletion + PreferenceStorage cleanup
                            └── → EndState
```

⚠️ **Why deletion must NOT happen immediately after Release (before Copy completion)**:
- If deleted while the E2 device is referencing the StoredJob for Copy, a **Critical Error** occurs
- Deletion must happen only after the Copy completion notification (`MSG_REPORT_COMPLETED`)

#### 5.3.4 Explicit Delete vs Auto Delete Comparison

| Item | Explicit Delete | Auto Delete (DeleteOnRelease) |
|------|---------------|-------------------------------|
| Trigger | App's explicit `DELETE_JOB` call | `RetentionModeOnRelease == DELETE` during Release |
| Password source | `DeleteRequestIntent.IntentParams.getJobCredentialsAttributes()` (provided by app) | `StoredJobAttributes.getJobCredentialsAttributes()` (Release parameters) |
| Result delivery | `ResultReceiver` → `CountDownLatch` → ContentProvider return | None (side effect after Copy completion, only logs on failure) |
| Execution timing | Immediate (EndState) | After Copy completion (CopyJobCompletedState) |
| E2 API | Same: `POST /storedJobs/{id}/remove` | Same: `POST /storedJobs/{id}/remove` |
| PreferenceStorage cleanup | Same: `StoredCopyJobPreferenceStorage.remove()` | Same: `StoredCopyJobPreferenceStorage.remove()` |
| Monitoring | Not needed | Included in Release's MonitoringCopyJobState |

---

### 5.4 Delete Process Implementation Assessment

| Verification Item | Result | Notes |
|-----------|------|------|
| Explicit Delete: E2 Remove API call | ✅ Normal | `deleteStoredJob(packageName, storedJobId, removeRequest)` |
| Explicit Delete: Password delivery | ✅ Normal | `applyRemoveJobPassword()` — not set if NONE |
| Explicit Delete: PreferenceStorage cleanup | ✅ Normal | `remove(context, storedJobId)` after E2 deletion success |
| Explicit Delete: ResultReceiver result return | ✅ Normal | `Result.pack()` format, extracted from Intent level |
| Explicit Delete: Error handling | ✅ Normal | `RESULT_FAIL` + `SERVICE_ERROR` on E2 failure/exception |
| Explicit Delete: 60-second timeout | ✅ Normal | `CountDownLatch.await(60, SECONDS)` |
| Auto Delete: RetentionMode branching | ✅ Normal | `CopyAttributesReader.getStoredJobRetentionModeOnRelease()` |
| Auto Delete: Password delivery | ✅ Normal | `StoredJobAttributes.getJobCredentialsAttributes()` → extraJobBundle |
| Auto Delete: Executed after Copy completion | ✅ Normal | `CopyJobCompletedState.deleteStoredJobOnRelease()` |
| Auto Delete: Copy result unaffected on deletion failure | ✅ Normal | Exception caught + log only |
| Auto Delete: PreferenceStorage cleanup | ✅ Normal | `remove()` on E2 deletion success, retained on failure |

---

## 6. Discovered Issues (Cross-Validation Consensus)

---

### Issue #1: SharedPreferences Key Match — scanJobId vs storedJob.jobId [✅ Closed — UUID Identity Confirmed]

**3 Analyses Consensus Status**: All identified the same issue → **Verified via actual device logs (2026-03-31)**

**Location**:

- Save: `CreatingCopyJobState.processStoredCopyJobIntent()` → `replaceKey(rid, scanJobId)`
- Lookup (Enumerate): `CopyJobAdapter.convertToStoredJobInfoList()` → `getCopyAttributes(detailedJob.getJobId().toString())`
- Lookup (Release): `CreatingCopyJobState.processReleaseJobIntent()` → `getCopyAttributes(storedJobId)`

**Verification Result (actual device log — 2026-03-31 17:49)**:

✅ **Confirmed that ScanJobId and StoredJob.jobId UUID values are identical. Issue closed.**

Log evidence:

```
[Store phase]
1. E2 ScanJob response:
   "scanJobId":"db940936-818c-4f5e-9730-b8d045bc81ec"

2. PreferenceStorage key replacement:
   replaceKey : oldKey=f52b3973-0562-48dc-a809-a4b21c1938cf → newKey=db940936-818c-4f5e-9730-b8d045bc81ec
   → PreferenceStorage key = db940936-818c-4f5e-9730-b8d045bc81ec

3. Monitoring complete:
   Job[db940936-818c-4f5e-9730-b8d045bc81ec] JobDoneStatus: jdsSucceeded

[Enumerate phase]
4. E2 StoredJobs collection response:
   "jobId":"db940936-818c-4f5e-9730-b8d045bc81ec"  ← Same UUID ✅

5. E2 individual StoredJob detail response:
   "jobId":"db940936-818c-4f5e-9730-b8d045bc81ec"  ← Same UUID ✅

6. PreferenceStorage lookup:
   getCopyAttributes : key=db940936-818c-4f5e-9730-b8d045bc81ec  ← Lookup with same key ✅

7. savedAttributes applied successfully:
   convertStoredJob : Applying saved attributes from PreferenceStorage  ← Lookup success ✅
   convertStoredJob WP : jobId=db940936-818c-4f5e-9730-b8d045bc81ec,
     [colorMode=AUTO, scanSize=AUTO, duplex=NONE, copies=1, totalPages=1]
```

**Conclusion**:

- After ScanJob completion, the E2 server assigns the **same UUID** as `StoredJob.jobId` when creating the StoredJob
- Therefore, after `replaceKey(rid, scanJobId)`, calling `getCopyAttributes(storedJob.jobId)` during Enumerate/Release results in **exact key match**, successfully retrieving savedAttributes
- **No code changes needed**
- Verification log: `05.Validation/Issue_1_validation_log.txt`

---

### Issue #2: resolveSavedDuplex() Uses getPrintDuplex() [Minor — Verification Needed]

**3 Analyses Consensus Status**: 1st/3rd flagged, 2nd not mentioned

**Location**: `CopyJobAdapter.java` → `resolveSavedDuplex()` method

```java
private static CopyAttributes.Duplex resolveSavedDuplex(CopyAttributes.Duplex e2Value, CopyAttributesReader reader) {
    if (reader.getPrintDuplex() != null) {   // ← Uses printDuplex
        return reader.getPrintDuplex();
    }
    return e2Value;
}
```

**Analysis**:

- `extractDuplex()` uses E2 `StoredJob.getOutputSides()` (PlexMode) → `CopyTypeMapping.originalPlexMode.convertEtoW()`
- The name `CopyTypeMapping.originalPlexMode` implies "original" = **scan source** duplex
- However, `resolveSavedDuplex()` overrides with `getPrintDuplex()` (print duplex)

**From E2 Specification Perspective**:

- The E2 `StoredJob.outputSides` field name itself implies "output" = output duplex
- But in CopyTypeMapping, it's mapped as `originalPlexMode` → treated as scan duplex

**Conclusion**:

- If E2 `StoredJob.outputSides` represents **scan source duplex**: `getScanDuplex()` is correct
- If E2 `StoredJob.outputSides` represents **output duplex**: `getPrintDuplex()` is correct (current code)
- For Enumerate display purposes, since the goal is to show "what duplex setting was used for storage," **scan duplex** is likely more appropriate

**Fix** (if changing to scan duplex):

```java
// File: CopyJobAdapter.java
// Method: resolveSavedDuplex()

// Before:
private static CopyAttributes.Duplex resolveSavedDuplex(CopyAttributes.Duplex e2Value, CopyAttributesReader reader) {
    if (reader.getPrintDuplex() != null) {
        return reader.getPrintDuplex();
    }
    return e2Value;
}

// After:
private static CopyAttributes.Duplex resolveSavedDuplex(CopyAttributes.Duplex e2Value, CopyAttributesReader reader) {
    if (reader.getScanDuplex() != null) {
        return reader.getScanDuplex();
    }
    return e2Value;
}
```

**Note**: This change only affects **Enumerate display values**. During Release, `getCopyJobTicket()` correctly converts `getScanDuplex()` → `originalPlexMode` and `getPrintDuplex()` → `outputDuplexBinding` **separately**, so Release is not affected.

---

### Issue #3: Both savedCopyAttributes and defaultOptions Null During Release [Low — Edge Case]

**3 Analyses Consensus Status**: 1st/3rd flagged, 2nd not mentioned

**Location**: `CreatingCopyJobState.buildReleaseCopyOptions()`

```java
} else {
    SLog.e(TAG, "processReleaseJobIntent : Both savedCopyAttributes and defaultOptions are null!");
    // ← releaseRequest.setCopyOptions() not called
}
```

**Analysis**:

- `defaultOptions` is null → `copyJobService.getDefaultOptions()` failed (network error, etc.)
- `savedCopyAttributes` is null → PreferenceStorage key mismatch or data loss
- If both are null, Release proceeds without CopyOptions → E2 behavior uncertain

**Probability of occurrence**: Very low (getDefaultOptions failure + PreferenceStorage data absence occurring simultaneously)

**Fix**:

```java
// File: CreatingCopyJobState.java
// Method: buildReleaseCopyOptions()

// Before:
} else {
    SLog.e(TAG, "processReleaseJobIntent : Both savedCopyAttributes and defaultOptions are null!");
}

// After:
} else {
    SLog.e(TAG, "processReleaseJobIntent : Both savedCopyAttributes and defaultOptions are null!");
    throw new IOException("Cannot build CopyOptions: both savedCopyAttributes and defaultOptions are null");
}
```

- `buildReleaseCopyOptions()` has `throws IOException` declared
- The calling `initializeJob()`'s `catch(Exception e)` block handles it by returning `ReportErrorState`
- App receives `RESULT_FAIL` + `SERVICE_ERROR`

---

### Issue #4: SharedPreferences Not Cleaned on ScanJob Failure/Cancellation During Monitoring [Minor]

**3 Analyses Consensus Status**: All identified the same issue

**Location**: `MonitoringStoredCopyJobState.java`

**Analysis**:

- After successful `processStoredCopyJobIntent()`, enters `MonitoringStoredCopyJobState`
- If ScanJob is `JdsFailed` or `JdsCanceled` at E2:
  - `processJobDoneStatus()` → `requestToTransitState()` → `JobFailedState`/`JobCanceledState` → `EndState`
  - `StoredCopyJobPreferenceStorage.remove(context, scanJobId)` is **not called** in this path
- No StoredJob is created at E2, so it won't be returned in Enumerate — harmless to app
- However, stale data accumulates in SharedPreferences

**Impact**: Gradual increase in SharedPreferences file size (not critical)

**Fix**:

```java
// File: MonitoringStoredCopyJobState.java
// Method 1: Override processJobDoneStatus

@Override
protected void processJobDoneStatus(BaseJobIntentServiceStateMachine stateMachine, JobDoneStatus jobDoneStatus) {
    // Clean up PreferenceStorage on failure/cancellation
    if (jobDoneStatus != null && stateMachine != null) {
        String statusValue = jobDoneStatus.getValue();
        if (JobDoneStatus.JdsFailed.getValue().equals(statusValue) 
                || JobDoneStatus.JdsCanceled.getValue().equals(statusValue)) {
            StoredCopyJobPreferenceStorage.remove(stateMachine.getContext(), jobId);
            SLog.d(TAG, "processJobDoneStatus : Cleaned up PreferenceStorage for jobId=" + jobId 
                    + " (status=" + statusValue + ")");
        }
    }
    super.processJobDoneStatus(stateMachine, jobDoneStatus);
}
```

```java
// Method 2 (alternative): Determine failure in onExit
// However, distinguishing failure/success at onExit time may be difficult, so Method 1 is recommended
```

---

### Issue #5: PreferenceStorage Not Cleaned After Release [Low — May Be Intentional]

**3 Analyses Consensus Status**: 2nd/3rd flagged, 1st not mentioned

**Location**: `CreatingCopyJobState.processReleaseJobIntent()`

**Analysis**:

- `StoredCopyJobPreferenceStorage.remove()` not called after successful Release
- E2 `deleteOnRelease=true` (default) → StoredJob auto-deleted → not shown in next Enumerate
- E2 `deleteOnRelease=false` → Re-release possible → PreferenceStorage retention needed
- During Delete, `processDeleteJobIntent()` calls `remove(context, storedJobId)` **already implemented ✅**

**Conclusion**: Current behavior is **likely intentional**. Since cleanup happens during Delete, no urgent fix needed.

**Optional Improvement**:

```
At app startup or during Enumerate call:
  1. Collect jobId list from E2 enumerateStoredJobs() results
  2. Iterate all keys in PreferenceStorage
  3. Delete keys not present in E2 list (garbage collection)

Location: End of OXPCopyletContentProvider.call(ENUMERATE_JOBS) case
Or as a separate utility method
```

---

## 7. Overall Data Flow Summary (Cross-Validation Confirmed)

```
[STORE]
  App CopyAttributes(STORE) ── CopyJobIntentService.startCopy()
       │
       ├── [4-b] PreferenceStorage.save(rid, CopyAttributes)     ← Preserve full CopyAttributes via Parcel
       │
       ├── [4-c] StoredCopyJobAdapter.createScanJobForStorage()   ← Only 9 scan attributes to E2 ScanJob
       │         → buildScanOptions(9) + buildJobStorageDestination(name/folder/pwd)
       │         → E2 ScanJob POST → scanJobId
       │
       ├── [4-d] On failure → remove(rid) → ReportErrorState
       │
       ├── [4-e] On success → replaceKey(rid, scanJobId)            ← Key replacement: rid → scanJobId
       │
       └── [Step5] MonitoringStoredCopyJobState(scanJobId)
                   → ScanNotification-based monitoring
                   → transmittingActivity → printingState
                   → JdsSucceeded → JobCompletedState
                   → App onComplete()

[ENUMERATE]
  App → ContentProvider.call(ENUMERATE_JOBS)
       │
       ├── E2 GET /storedJobs → StoredJobs(collection, summary only)
       │
       ├── forEach member:
       │   ├── E2 GET /storedJobs/{jobId} → Detailed fields (colorMode, copies, totalPages, etc.)
       │   ├── PreferenceStorage.get(jobId) → savedAttributes (key = scanJobId from replaceKey above)
       │   └── convertStoredJob(e2Detail, savedAttrs) → E2 defaults + saved override
       │
       └── App ← List<StoredJobInfo> (jobId, name, folder, copies, colorMode, scanSize, duplex, ...)

[RELEASE]
  App → CopyJobIntentService.startRelease(storedJobId, copies, pwd)
       │
       ├── PreferenceStorage.get(storedJobId) → savedCopyAttributes
       │   (storedJobId = StoredJobInfo.jobId received from Enumerate)
       │
       ├── copyJobService.getDefaultOptions() → DefaultOptions
       │
       ├── buildReleaseCopyOptions():
       │   ├── Normal: getCopyJobTicketForRelease(savedAttrs, copies, defaultOpts)
       │   │         → getCopyJobTicket() 18 attribute conversion
       │   │         → copies override (releaseCopies > 0)
       │   ├── Fallback: convertDefaultOptionsToCopyOptions(defaultOpts) → JSON conversion
       │   └── Both null: log only ⚠️
       │
       ├── applyReleaseJobPassword() → only when not NONE
       │
       ├── E2 POST /storedJobs/{id}/release (CopyOptions + password)
       │   → StoredJob_Release → CopyJob
       │
       └── MonitoringCopyJobState(copyJobId)
           → CopyNotification-based scan+print monitoring
           → App onComplete()

[DELETE — Explicit]
  App → ContentResolver.call(DELETE_JOB, storedJobId, DeleteRequestIntent)
       │
       ├── OXPCopyletContentProvider.deleteJob()
       │   Create CountDownLatch(1) + ResultReceiver
       │   CopyJobIntentService.startDelete(bundle, resultReceiver)
       │
       ├── CreatingCopyJobState.processDeleteJobIntent(stateMachine, extraParams, intent)
       │   ├── ResultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER) ★Intent level
       │   ├── DeleteRequestIntent.IntentParams → storedJobId, packageName, credentials
       │   ├── RemoveStoredJobRequest + applyRemoveJobPassword(credentials)
       │   ├── E2 POST /storedJobs/{id}/remove (with password)
       │   ├── Success: PreferenceStorage.remove(storedJobId)
       │   │        Result.pack(RESULT_OK) → ResultReceiver.send()
       │   └── Failure: Result.pack(RESULT_FAIL, SERVICE_ERROR) → ResultReceiver.send()
       │
       ├── → EndState (Monitoring not needed)
       └── CountDownLatch released → Result returned to app

[DELETE — Auto (RetentionModeOnRelease=DELETE)]
  After successful Release:
       │
       ├── CreatingCopyJobState.storeDeleteOnReleaseInfo()
       │   └── Store in extraJobBundle: {deleteOnRelease, storedJobId, packageName, password}
       │
       ├── → MonitoringCopyJobState(copyJobId) — Copy progress monitoring
       │
       ├── MSG_REPORT_COMPLETED (Copy complete)
       │   → CopyJobCompletedState.processCompletedJob()
       │       └── deleteStoredJobOnRelease()
       │           ├── Extract deletion info from extraJobBundle
       │           ├── RemoveStoredJobRequest + setJobPassword(password)
       │           ├── E2 POST /storedJobs/{id}/remove
       │           ├── Success: PreferenceStorage.remove(storedJobId)
       │           └── Failure: SLog.e() (does not affect Copy result)
       │
       └── → EndState
```

---

## 8. Final Implementation Assessment

### Overall Conclusion: **Implemented Correctly as Designed** (3 Analyses Consensus + Delete Verification Added)

The entire Store→Enumerate→Release→Delete pipeline faithfully implements the following design:

1. **Store**: Preserves CopyAttributes locally via Parcel+Base64 while sending only scan-related attributes to E2 ScanJob(JobStorage)
2. **Enumerate**: After querying E2 StoredJobs, supplements E2 null fields with locally stored original CopyAttributes for app display
3. **Release**: Restores full copy options (18 attributes) from locally stored original CopyAttributes and passes them to E2 CopyJob for printing
4. **Delete (Explicit)**: E2 Remove API + password + PreferenceStorage cleanup, synchronous result return via ResultReceiver→CountDownLatch pattern
5. **Delete (Auto)**: If RetentionModeOnRelease=DELETE during Release, auto-deletes after Copy completion, state passed via extraJobBundle

### Issue Priority

| Priority | Issue                                    | Severity  | Action                                                                       |
| -------- | --------------------------------------- | ------- | -------------------------------------------------------------------------- |
| ~~1~~   | ~~Issue #1: UUID key match verification~~        | ✅ Closed | **UUID identity confirmed via actual device logs (2026-03-31)** — No code changes needed |
| 2        | Issue #2: duplex source (print vs scan)   | Minor   | **Decide after verifying E2 StoredJob.outputSides specification**                       |
| 3        | Issue #3: Both null not handled              | Low     | Add IOException throw (defensive code)                                         |
| 4        | Issue #4: Prefs not cleaned on monitoring failure | Minor   | Cleanup via processJobDoneStatus override                                  |
| 5        | Issue #5: Prefs not cleaned after Release       | Low     | May be intentional, optional garbage collection                                     |

---
