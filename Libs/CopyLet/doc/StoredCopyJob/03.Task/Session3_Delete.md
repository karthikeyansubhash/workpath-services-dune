# Session 3: Delete Stored Copy Job + Full Integration Verification

> **StoredCopyJob Development — Session 3/5**
> **Phase 5 (Delete)**
> **Reference Document:** `StoredCopyJob_DevelopmentPlan_Final.md` Section 2.2, 5.4, 7.3, 8 Phase 5, 12.1, 12.3
> **Prerequisites:** Session 1 (Create) + Session 2 (Enumerate + Release) completed

---

## 1. Session Goal

In this session, we implement Delete Stored Copy Job and verify the integrated build of all production code.

**Completion Criteria:**
- `processDeleteJobIntent()` signature change (Intent parameter added)
- `initializeJob()` switch statement passes Intent in DELETE case
- ResultReceiver extracted directly from Intent and sent in `Result.pack()` format via send()
- E2 deleteStoredJob + PreferenceStorage.remove() calls
- `./gradlew :Let-CopyLet:assembleDebug` build succeeds
- Full module build: `./gradlew :Test-WorkpathServices:assembleDebug` succeeds

---

## 2. Work Order and Detailed Instructions

### Step 1: CreatingCopyJobState.java — Change DELETE Case in initializeJob()

**File:** `Libs/CopyLet/src/main/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobState.java`
**Type:** UPDATE

**Current Status:** Inside `initializeJob()` switch statement:
```java
case CopyJobIntentService.PARAMS_TYPE_DELETE:
    return processDeleteJobIntent(stateMachine, extraParams);
```

**After Change:** Pass Intent additionally:
```java
case CopyJobIntentService.PARAMS_TYPE_DELETE:
    return processDeleteJobIntent(stateMachine, extraParams, intent);
```

> ⚠️ **Key Reason:** `ResultReceiver` is stored **directly on the Intent**, not in the `extraParams` Bundle.
> Intent Structure (Section 12.3):
> ```
> Intent
>  ├─ PARAMS_TYPE (String)
>  ├─ EXTRA_PARAMS (Bundle): Nested Bundle — CopyAttributes/StoredJobAttributes
>  └─ EXTRA_RESULT_RECEIVER (ResultReceiver): Intent level! ← This one
> ```
> `extraParams.getParcelable(EXTRA_RESULT_RECEIVER)` → **Returns null!**

---

### Step 2: CreatingCopyJobState.java — Full Replacement of processDeleteJobIntent()

**Current Status:**
```java
private BaseJobIntentServiceState processDeleteJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams) throws Exception {
    // ...
    return deleteStoredCopyJob(stateMachine, extraParams);
    // → ReportErrorState(NOT_SUPPORTED) + ResultReceiver not called → 60-second timeout
}
```

**Replacement Implementation — Full Final Document Section 5.4:**

```java
/**
 * ★ Signature change: Intent parameter added (because ResultReceiver is stored directly on the Intent)
 */
private BaseJobIntentServiceState processDeleteJobIntent(
        BaseJobIntentServiceStateMachine stateMachine,
        Bundle extraParams, Intent intent) throws Exception {

    // ★ Key: Extract ResultReceiver directly from Intent (not from extraParams!)
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
        // 1. E2 delete
        copyJobService.deleteStoredJob(packageName, storedJobId);

        // 2. PreferenceStorage cleanup
        StoredCopyJobPreferenceStorage.remove(stateMachine.getContext(), storedJobId);

        // 3. ★ Send success result to ResultReceiver → releases ContentProvider CountDownLatch
        //    ★ Must use Result.pack() format! (Section 12.1)
        Bundle resultBundle = new Bundle();
        Result.pack(resultBundle, Result.RESULT_OK);
        sendResultToReceiver(resultReceiver, Activity.RESULT_OK, resultBundle);
    } catch (Exception e) {
        SLog.e(TAG, "deleteStoredCopyJob failed: " + e.getMessage(), e);
        Bundle errorBundle = new Bundle();
        Result.pack(errorBundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        sendResultToReceiver(resultReceiver, Activity.RESULT_CANCELED, errorBundle);
    }

    // ★ Delete does not need async lifecycle (Monitoring) → go directly to EndState
    return new EndState();
}
```

---

### Step 3: CreatingCopyJobState.java — Add sendResultToReceiver() Helper

**Add to the same file:**

```java
/**
 * Helper: Send error result to ResultReceiver (String message version).
 * Null-safe — if receiver is null (shouldn't happen in normal flow), silently no-op.
 */
private void sendResultToReceiver(ResultReceiver receiver, int resultCode, String errorMsg) {
    if (receiver != null) {
        Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, errorMsg);
        receiver.send(resultCode, bundle);
    }
}

/**
 * Helper: Send result Bundle to ResultReceiver.
 * The bundle MUST be packed with Result.pack() format — see Section 12.1.
 */
private void sendResultToReceiver(ResultReceiver receiver, int resultCode, Bundle bundle) {
    if (receiver != null) {
        receiver.send(resultCode, bundle);
    }
}
```

**Required imports to add:**
```java
import android.app.Activity;
import android.os.ResultReceiver;
```

---

### Step 4: Remove or Replace Existing deleteStoredCopyJob() Method

The current `deleteStoredCopyJob()` returns `ReportErrorState(NOT_SUPPORTED)`.
This method is no longer called, so:
- **Option A (Recommended):** Delete the method
- **Option B:** Replace the method body with new logic (however, since the signature differs, deletion is cleaner)

---

### Step 5: Build Verification

```bash
# CopyLet build
./gradlew :Let-CopyLet:assembleDebug

# Full test module build (all Phase integration verification)
./gradlew :Test-WorkpathServices:assembleDebug
```

Fix build errors immediately and rebuild.

---

## 3. Key Considerations

### ResultReceiver Result Format (★ Most Important)

`OXPCopyletContentProvider.deleteJob()`'s ResultReceiver parses results as follows (Section 12.1):
```java
protected void onReceiveResult(final int resultCode, final Bundle resultData) {
    Result result = Result.parse(resultData, new Result());
    Result.pack(bundle, result);
    countDownLatch.countDown();
}
```

Therefore, the Bundle **must be created in `Result.pack()` format**:
```java
// ✅ Correct format:
Bundle resultBundle = new Bundle();
Result.pack(resultBundle, Result.RESULT_OK);
resultReceiver.send(Activity.RESULT_OK, resultBundle);

// ❌ Incorrect format: Bundle.EMPTY → Result.parse() fails
resultReceiver.send(Activity.RESULT_OK, Bundle.EMPTY);
```

### Intent Structure (Section 12.3)

```
Intent (CopyJobIntentService.class)
 ├─ PARAMS_TYPE (String): "paramsDelete"
 ├─ EXTRA_PARAMS (Bundle): Nested Bundle
 │   ├─ KEY_STORED_JOB_ID
 │   └─ KEY_DELETE_REQ (JobCredentialsAttributes)
 └─ EXTRA_RESULT_RECEIVER (ResultReceiver)  ★ Intent level!
```

`intent.getBundleExtra(EXTRA_PARAMS)` → `extraParams` (nested Bundle, no ResultReceiver)
`intent.getParcelableExtra(EXTRA_RESULT_RECEIVER)` → ResultReceiver ✅

### Delete Does Not Need Monitoring
Delete is a single E2 API call → return result → EndState.
Neither MonitoringCopyJobState nor MonitoringStoredCopyJobState is used.

---

## 4. Created/Modified Files Checklist

| # | File | Type | Step |
|---|------|------|------|
| 1 | `CreatingCopyJobState.java` | UPDATE (initializeJob DELETE case) | Step 1 |
| 2 | `CreatingCopyJobState.java` | UPDATE (processDeleteJobIntent replacement) | Step 2 |
| 3 | `CreatingCopyJobState.java` | UPDATE (+sendResultToReceiver helper) | Step 3 |
| 4 | `CreatingCopyJobState.java` | UPDATE (deleteStoredCopyJob removal) | Step 4 |

> This session modifies only the single file `CreatingCopyJobState.java`.

---

## 5. Full File Status Check at Session 3 Completion

After Session 3 completion, all production code should be in the following state:

| File | Status |
|------|------|
| IDeviceCopyJobService.java | ✅ Completed in Session 1 |
| StandardDeviceCopyJobService.java | ✅ Completed in Session 1 |
| StoredCopyJobPreferenceStorage.java | ✅ Completed in Session 1 (NEW) |
| CopyOptionProfileAdapter.java | ✅ Completed in Session 1 |
| StoredCopyJobAdapter.java | ✅ Completed in Session 1 (NEW) |
| MonitoringStoredCopyJobState.java | ✅ Completed in Session 1 (NEW) |
| CreatingCopyJobState.java | ✅ Completed in Session 1+2+3 (STORE branch + Release + Delete) |
| CopyJobAdapter.java | ✅ Completed in Session 2 |
| OXPCopyletContentProvider.java | ✅ Completed in Session 2 |

**All production code implementation complete!** Test code writing begins in the next session.

## 6. Prompt to Request from User After Session Completion

```
Referring to StoredCopyJob_DevelopmentPlan_Final.md, please write
unit tests according to Session4_UnitTests.md.
All production code implementation is complete from Sessions 1-3.
Test code coverage must be at least 80%.
Please run the tests and fix any failing tests.
```
