# Session 2: Enumerate + Release Stored Copy Job

> **StoredCopyJob Development — Session 2/5**
> **Phase 3 (Enumerate) + Phase 4 (Release)**
> **Reference Document:** `StoredCopyJob_DevelopmentPlan_Final.md` Section 5.2, 5.3, 7.2, 8 Phase 3-4
> **Prerequisites:** Session 1 completed (DeviceService interface + Create Stored Copy Job)

---

## 1. Session Goal

In this session, we implement Enumerate (query list of stored Copy Jobs) and Release (execute printing of a stored Copy Job).

**Completion Criteria:**
- `OXPCopyletContentProvider.ENUMERATE_JOBS` implementation complete (App → ContentProvider → E2 → StoredJobInfo list returned)
- `CreatingCopyJobState.processReleaseJobIntent()` reimplementation complete (PreferenceStorage → CopyOptions → E2 Release → MonitoringCopyJobState)
- `./gradlew :Let-CopyLet:assembleDebug` build succeeds

---

## 2. Work Order and Detailed Instructions

### Step 1: CopyJobAdapter.java — Add convertToStoredJobInfoList()

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyJobAdapter.java`
**Type:** UPDATE

**Purpose:** Convert E2 `StoredJobs` response to SDK `StoredJobInfo` list

```java
/**
 * Converts E2 StoredJobs response to a list of SDK StoredJobInfo.
 * E2 member fields → StoredJobInfo fields:
 *   jobId → storedJobId
 *   jobName → jobName
 *   folderName → folderName
 *   jobTimestamp → timestamp
 *   jobUserName → userName
 *   totalPages → totalPages
 *
 * @param storedJobs E2 StoredJobs response (from enumerateStoredJobs)
 * @return List of StoredJobInfo for SDK consumption
 */
public static List<StoredJobInfo> convertToStoredJobInfoList(StoredJobs storedJobs) {
    List<StoredJobInfo> result = new ArrayList<>();
    if (storedJobs == null || storedJobs.getMembers() == null) {
        return result;
    }
    for (/* StoredJobMember member : storedJobs.getMembers() */) {
        // ★ Verify actual type/field names of E2 StoredJobs.members
        // Verify StoredJobInfo.Builder or constructor pattern (JetAdvantageLinkApi)
        StoredJobInfo info = new StoredJobInfo(/* ... */);
        // info.setStoredJobId(member.getJobId().getValue().toString());
        // info.setJobName(member.getJobName());
        // info.setFolderName(member.getFolderName());
        // info.setTimestamp(member.getJobTimestamp());
        // info.setUserName(member.getJobUserName());
        // info.setTotalPages(member.getTotalPages());
        result.add(info);
    }
    return result;
}
```

**Required imports to add:**
```java
import com.hp.jetadvantage.link.api.copier.StoredJobInfo;
import com.hp.ext.service.copy.StoredJobs;
import java.util.ArrayList;
import java.util.List;
```

> ⚠️ **E2 Type Verification:** Verify the return type of `StoredJobs.getMembers()` and each member's getter names in the actual E2 library.

---

### Step 2: OXPCopyletContentProvider.java — Implement ENUMERATE_JOBS

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/provider/OXPCopyletContentProvider.java`
**Type:** UPDATE

**Current Status:** `ENUMERATE_JOBS` case throws `SdkNotSupportedException` [DUNE-169955]

**Implementation:**

```java
case Copylet.Method.ENUMERATE_JOBS:
    return enumerateStoredJobs(appPackageName);
```

**New method to add:**

```java
private Bundle enumerateStoredJobs(String appPackageName) {
    Bundle bundle = new Bundle();
    try {
        // 1. Query stored jobs list from E2
        IDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
        StoredJobs storedJobs = copyJobService.enumerateStoredJobs(appPackageName);

        // 2. Convert E2 response → StoredJobInfo list
        List<StoredJobInfo> storedJobInfoList = CopyJobAdapter.convertToStoredJobInfoList(storedJobs);

        // 3. JSON serialize and return in Bundle
        String json = JsonParser.toJson(storedJobInfoList);
        Result.pack(bundle, Result.RESULT_OK);
        bundle.putString(Copylet.Keys.KEY_STORED_JOBS_JSON, json);
    } catch (Exception e) {
        SLog.e(TAG, "enumerateStoredJobs failed: " + e.getMessage(), e);
        Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
    }
    return bundle;
}
```

**Required imports to add:**
```java
import com.hp.ext.service.copy.StoredJobs;
import com.hp.jetadvantage.link.api.copier.StoredJobInfo;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyJobAdapter;
import java.util.List;
```

> ⚠️ **KEY_STORED_JOBS_JSON Verification:** Check if a key for stored jobs JSON is already defined in `Copylet.Keys`.
> If not, use an appropriate key name or follow the existing Result pack pattern.

---

### Step 3: CopyJobAdapter.java — Add buildCopyOptionsForRelease()

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyJobAdapter.java`
**Type:** UPDATE

**Purpose:** Combine the saved original CopyAttributes + StoredJobAttributes at Release time to create E2 CopyOptions

**Reference:**
- Final Document Section 5.1.2 — Fields marked ✅ for Release (Print-only + common)
- Existing `getCopyJobTicket()` — CopyOptions setter pattern (access level may need to change from private static, or copy logic)
- Existing `getCopyJobTicket()` takes a `DefaultOptions` parameter

**CopyAttributes Fields Included in Release (Section 5.1.2):**
- **Print-only:** copies, printSize, paperSource, printDuplex, paperType, outputBin, scaleMode, collateMode, numberUpMode
- **Common:** colorMode, contentType, contentOrientation

```java
/**
 * Builds CopyOptions for Release operation.
 * Combines the original CopyAttributes (saved at Create time) with Release overrides (StoredJobAttributes).
 *
 * @param savedCopyAttributesJson Original CopyAttributes JSON from PreferenceStorage
 * @param storedJobAttributes Release request parameters (may override copies, etc.)
 * @param defaultOptions E2 DefaultOptions for fallback values
 * @return CopyOptions for the E2 release API call
 */
public static CopyOptions buildCopyOptionsForRelease(
        String savedCopyAttributesJson,
        StoredJobAttributes storedJobAttributes,
        DefaultOptions defaultOptions) {

    // 1. Restore original CopyAttributes from JSON
    CopyAttributesReader originalAttrs = /* JSON deserialization */;

    // 2. Build CopyOptions — refer to getCopyJobTicket() pattern
    //    ★ Since getCopyJobTicket() is private static:
    //      Option A: Change access level to package-private/protected for reuse
    //      Option B: Copy only the necessary logic inside this method
    CopyOptions copyOptions = new CopyOptions();

    // 3. Set Print options (Release-only)
    //    copies: storedJobAttributes.getCopies() takes priority, fallback to originalAttrs
    //    printSize, paperSource, printDuplex, paperType, outputBin, scaleMode, collateMode, numberUpMode:
    //    Get from originalAttrs

    // 4. Common options (colorMode, contentType, contentOrientation)
    //    Get from originalAttrs

    return copyOptions;
}
```

> ⚠️ **getCopyJobTicket() Access Level:**
> Currently `private static`. To reuse CopyTypeMapping conversion logic in `buildCopyOptionsForRelease()`,
> change the access level to `static` (package-private) or extract the core conversion logic into a separate helper.

---

### Step 4: CreatingCopyJobState.java — Reimplement processReleaseJobIntent()

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobState.java`
**Type:** UPDATE

**Current Status:** `setJobBundle` is commented out → NPE occurs (Final Document Section 12.2)

**Reimplementation — Full replacement following Final Document Section 5.3 pseudocode:**

```java
private BaseJobIntentServiceState processReleaseJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams) throws Exception {

    // 1. Extract Request params
    ReleaseRequestIntent.IntentParams reqParams = ReleaseRequestIntent.getIntentParams(extraParams);
    if (reqParams == null || reqParams.getStoredJobAttributes() == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "StoredJobAttributes not found");
    }

    StoredJobAttributes storedJobAttributes = reqParams.getStoredJobAttributes();
    String storedJobId = storedJobAttributes.getStoredJobId();
    int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel()
                                                        : storedJobAttributes.getVersion();

    // 2. Restore original CopyAttributes from PreferenceStorage
    String savedCopyAttributesJson = StoredCopyJobPreferenceStorage.get(
            stateMachine.getContext(), storedJobId);
    if (savedCopyAttributesJson == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                "Stored copy attributes not found for jobId: " + storedJobId);
    }

    // 3. Create JobBundle + ExtraJobBundle for Release
    //    ★ createReleaseCopyJobBundle() is a NEW method (refer to existing createCopyJobBundle())
    stateMachine.setJobBundle(createReleaseCopyJobBundle(clientVersion, storedJobAttributes,
                                                         savedCopyAttributesJson));
    stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(),
                                                        reqParams.getPackageName()));

    // 4. Build CopyOptions (original attrs + release overrides)
    DefaultOptions defaultOptions = copyJobService.getDefaultOptions(reqParams.getPackageName());
    CopyOptions copyOptions = CopyJobAdapter.buildCopyOptionsForRelease(
            savedCopyAttributesJson, storedJobAttributes, defaultOptions);

    // 5. E2 Release API call
    CopyJob copyJob = copyJobService.releaseStoredJob(
            reqParams.getPackageName(), storedJobId, copyOptions);
    if (copyJob == null) {
        return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                "Failed to release stored copy job");
    }

    String copyJobId = copyJob.getCopyJobId().getValue().toString();
    stateMachine.setJobId(copyJobId);

    // 6. Reuse existing MonitoringCopyJobState (E2 CopyNotification monitoring)
    return new MonitoringCopyJobState(copyJobId);
}
```

**Additional Required Method: createReleaseCopyJobBundle() (NEW)**

```java
/**
 * Creates a job bundle for a Release operation.
 * Similar to createCopyJobBundle() but uses stored attributes instead of live CopyAttributesReader.
 * ★ Refer to existing createCopyJobBundle(), but set mode to original STORE, state to ACTIVE
 */
private Bundle createReleaseCopyJobBundle(int clientVersion, StoredJobAttributes storedJobAttributes,
                                           String savedCopyAttributesJson) {
    Bundle jobBundle = new Bundle();
    
    // Build JobInfo
    JobInfo jobInfo = new JobInfo(JobInfo.JobType.COPY, clientVersion);
    
    // Build CopyJobData — since this is Release, mode remains STORE
    CopyJobData copyJobData = new CopyJobData();
    copyJobData.setJobState(new CopyJobState(CopyJobState.State.ACTIVE));
    copyJobData.setJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);
    // Set storedJobId
    // Override copies (storedJobAttributes.getCopies())
    
    jobInfo.packToBundle(jobBundle);
    copyJobData.packToBundle(jobBundle);
    return jobBundle;
}
```

---

### Step 5: Build Verification

```bash
./gradlew :Let-CopyLet:assembleDebug
```

Fix build errors immediately and rebuild.

---

## 3. Key Reference Points

### Rationale for Reusing MonitoringCopyJobState After Release
After Release, E2 creates a regular CopyJob, which triggers **CopyNotification**.
Therefore, the existing `MonitoringCopyJobState` is used as-is, and `MonitoringStoredCopyJobState` (ScanNotification) is not used.

### PreferenceStorage Cleanup Policy After Release
- The current implementation **maintains PreferenceStorage** and only cleans up on DELETE
- If the E2 server automatically deletes the storedJob, synchronization cleanup can occur during the next enumerate
- Additional cleanup logic can be added in the future based on `retentionModeOnRelease` settings

### getCopyJobTicket() Access Level Issue
- Currently `private static` → CopyTypeMapping conversion logic cannot be reused in `buildCopyOptionsForRelease()`
- **Solution:** Change access level to package-private (`static`) or extract the conversion logic into a separate method

---

## 4. Created/Modified Files Checklist

| # | File | Type | Step |
|---|------|------|------|
| 1 | `CopyJobAdapter.java` | UPDATE (+convertToStoredJobInfoList) | Step 1 |
| 2 | `OXPCopyletContentProvider.java` | UPDATE (ENUMERATE_JOBS implementation) | Step 2 |
| 3 | `CopyJobAdapter.java` | UPDATE (+buildCopyOptionsForRelease) | Step 3 |
| 4 | `CreatingCopyJobState.java` | UPDATE (processReleaseJobIntent replacement, createReleaseCopyJobBundle addition) | Step 4 |

## 5. Prompt to Request from User After Session Completion

```
Referring to StoredCopyJob_DevelopmentPlan_Final.md, please implement
Phase 5 (Delete) according to Session3_Delete.md.
Session 1 (Create) + Session 2 (Enumerate + Release) are already complete.
Please build and fix any build errors.
```
