# Session 5: Android Instrumented Tests (androidTest)

> **StoredCopyJob Development — Session 5/5 (Final)**
> **Phase 6b (Android Instrumented Tests)**
> **Reference Document:** `StoredCopyJob_DevelopmentPlan_Final.md` Section 13.3
> **Prerequisites:** Sessions 1-3 (Production code) + Session 4 (Unit tests) completed

---

## 1. Session Goal

Write and run Android instrumented tests (androidTest). Verify actual Android environment behavior on an emulator or physical device.

**Completion Criteria:**
- 3 new androidTest files created ✅
- All instrumented tests pass ✅ (19 passed)
- **StoredCopyJob full implementation complete! 🎉**

---

## 2. Test Environment Information

**Test Location:** `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/copylet/`

**Frameworks & Dependencies:**
- AndroidJUnit4 (`@RunWith(AndroidJUnit4.class)`)
- AndroidX Test Core + Espresso
- `InstrumentationRegistry.getInstrumentation().getTargetContext()` — Real Android Context
- Mockito-android (Mockito for Android instrumented environment)
- `androidx.test:rules:1.6.1`

**Existing Test Patterns (must reference):**

1. **`CopyJobIntentServiceStateMachineInstrumentedTest.java`** — StateMachine instrumented test:
   - `extends BaseInstrumentedTest`
   - Uses `InstrumentationRegistry.getInstrumentation().getTargetContext()`
   - MockIntentService creation (getApplicationContext, getContentResolver overrides)
   - `CopyJobIntentServiceStateMachine` actual instance + actual Intent/Bundle

2. **`CopyDeviceAdapterInstrumentedTest.java`** — Device Adapter instrumented test:
   - E2 physical device API call tests
   - `@RunWith(AndroidJUnit4.class)`

**Test Structure Pattern:**
```java
@RunWith(AndroidJUnit4.class)
public class XxxInstrumentedTest extends BaseInstrumentedTest {
    Context context;

    @Before
    public void setUp() {
        super.SetUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testSomething() {
        // Tests based on real Android Context
    }
}
```

---

## 3. Work Order and Detailed Instructions

### Test File 1: StoredCopyJobPreferenceStorageInstrumentedTest.java (NEW)

**File:** `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/copylet/service/StoredCopyJobPreferenceStorageInstrumentedTest.java`

**Purpose:** Verify actual SharedPreferences CRUD (using Android Context from physical device/emulator)

> Unit tests used Mock SharedPreferences, but here we use **actual SharedPreferences**.

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `testSaveAndGet` | save(key, json) → get(key) == json verified |
| 2 | `testGetNonExistentKey_ReturnsNull` | Key not saved → get() == null |
| 3 | `testRemove` | save() → remove() → get() == null |
| 4 | `testReplaceKey` | save(oldKey) → replaceKey(oldKey, newKey) → get(newKey) == value, get(oldKey) == null |
| 5 | `testReplaceKey_OldKeyNotExist` | replaceKey(nonExistKey, newKey) → get(newKey) == null (no crash) |
| 6 | `testDataPersistence` | save() → get() again (same Context) — SharedPreferences data persistence |
| 7 | `testMultipleEntries` | Save multiple key/value pairs, then verify each is correctly retrieved |

**Pre/Post Test Cleanup:**
```java
@Before
public void setUp() {
    super.SetUp();
    context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    // Clear SharedPreferences before test
    context.getSharedPreferences("stored_copy_job_map", Context.MODE_PRIVATE)
            .edit().clear().apply();
}

@After
public void tearDown() {
    // Cleanup after test
    context.getSharedPreferences("stored_copy_job_map", Context.MODE_PRIVATE)
            .edit().clear().apply();
}
```

---

### Test File 2: CreatingCopyJobStateStoredInstrumentedTest.java (NEW)

**File:** `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobStateStoredInstrumentedTest.java`

**Purpose:** Verify StateMachine state transitions in actual Android environment

**Existing Reference:** `CopyJobIntentServiceStateMachineInstrumentedTest.java` — MockIntentService + actual Context + StateMachine pattern

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `testStoredCopyJobIntentCreation` | Create Intent with STORE mode CopyAttributes → verify PARAMS_TYPE == COPY + mode == STORE |
| 2 | `testReleaseIntentCreation` | ReleaseRequestIntent → verify PARAMS_TYPE == RELEASE + StoredJobAttributes included |
| 3 | `testDeleteIntentCreation` | Delete Intent → verify PARAMS_TYPE == DELETE + KEY_STORED_JOB_ID + ResultReceiver included |
| 4 | `testDeleteIntentResultReceiverOnIntent` | Verify ResultReceiver is stored at Intent level (not in extraParams) |
| 5 | `testStateMachineInitWithStoredCopyIntent` | Pass Stored Copy Intent to StateMachine → verify initializeJob() entry |
| 6 | `testCreateReleaseCopyJobBundle` | createReleaseCopyJobBundle() → verify JobInfo.JobType == COPY, CopyJobData.mode == STORE |

**MockIntentService Pattern (from existing reference):**
```java
IntentService mockIntentService = new IntentService("MockIntentService") {
    @Override
    protected void onHandleIntent(Intent intent) {}

    @Override
    public Context getApplicationContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Override
    public ContentResolver getContentResolver() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
    }
};
```

**Intent Construction Test:**
```java
// Verify STORE Intent construction
Bundle extraParams = new Bundle();
extraParams.putString(EXTRA_REQ_ID, testRid);
extraParams.putString(EXTRA_APP_PACKAGE_NAME, testPackageName);
// CopyAttributes with mode=STORE
// ...

Intent intent = new Intent(context, CopyJobIntentService.class);
intent.putExtra(CopyJobIntentService.PARAMS_TYPE, CopyJobIntentService.PARAMS_TYPE_COPY);
intent.putExtra(CopyJobIntentService.EXTRA_PARAMS, extraParams);

// DELETE Intent + ResultReceiver
ResultReceiver resultReceiver = new ResultReceiver(null);
intent.putExtra(CopyJobIntentService.EXTRA_RESULT_RECEIVER, resultReceiver);
```

---

### Test File 3: StoredCopyJobAdapterInstrumentedTest.java (NEW)

**File:** `tests/services/src/androidTest/java/com/hp/jetadvantage/link/services/copylet/adapter/StoredCopyJobAdapterInstrumentedTest.java`

**Purpose:** Verify actual type validation of CopyAttrs → ScanTicket conversion + E2 integration test (when physical device is connected)

**Existing Reference:** `CopyDeviceAdapterInstrumentedTest.java` — Device adapter + actual API call patterns

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `testCopyAttributesToScanOptionsMapping` | Actual CopyAttributesReader → ScanOptions conversion — verify each field type/value |
| 2 | `testJobStorageDestinationCreation` | DestinationOptions.jobStorage object creation + field verification |
| 3 | `testScanTicketNameAndFolderSet` | Verify ScanTicket name, folderName settings |
| 4 | `testCopyTypeMappingConsistency` | Verify E2 type conversion via CopyTypeMapping is compatible with ScanOptions |
| 5 | `testE2ScanJobCreation` | (Physical device only) Actual IDeviceScanJobService.createScanJob() call + response verification |

> ⚠️ **Physical Device Test (#5):** In environments without a connected physical device, use `@Ignore` or `Assume.assumeTrue()` pattern to skip.

```java
@Test
public void testE2ScanJobCreation() {
    // Check physical device connection — skip if not connected
    IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
    Assume.assumeTrue("Device not connected, skipping E2 test", scanJobService.isSupported());

    // Actual E2 call test
    // ...
}
```

---

## 4. Test Execution Commands

> ⚠️ **Prerequisite:** ADB-connected device or emulator required
> **Note:** `connectedDebugAndroidTest` does not support the `--tests` flag. Use `-Pandroid.testInstrumentationRunnerArguments.class` instead.

```bash
# Run all 3 StoredCopy instrumented tests
./gradlew :Test-WorkpathServices:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.hp.jetadvantage.link.services.copylet.service.StoredCopyJobPreferenceStorageInstrumentedTest,com.hp.jetadvantage.link.services.copylet.service.CreatingCopyJobStateStoredInstrumentedTest,com.hp.jetadvantage.link.services.copylet.adapter.StoredCopyJobAdapterInstrumentedTest"

# Run individual test files (for debugging)
./gradlew :Test-WorkpathServices:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.hp.jetadvantage.link.services.copylet.service.StoredCopyJobPreferenceStorageInstrumentedTest"
```

### ✅ Execution Results (2026-03-27)

| Test Class | Result | Details |
|-------------|------|------|
| StoredCopyJobPreferenceStorageInstrumentedTest | **7/7 PASS** | Actual SharedPreferences CRUD verified |
| CreatingCopyJobStateStoredInstrumentedTest | **6/6 PASS** | Intent creation + StateMachine verified |
| StoredCopyJobAdapterInstrumentedTest | **6/6 PASS** | CopyTypeMapping verified |
| **Total** | **19 PASS, 1 SKIP** | HP-Printer - 12 device |

> **Technical Note:** `CopyAttributes` test instances are created directly via `Parcel` serialization without `CopyAttributesCaps` validation.
> Uses the `CopyAttributes.CREATOR.createFromParcel(parcel)` pattern.

---

## 5. Created/Modified Files Checklist

| # | File | Type | Location |
|---|------|------|------|
| 1 | `StoredCopyJobPreferenceStorageInstrumentedTest.java` | NEW | `tests/services/src/androidTest/.../copylet/service/` |
| 2 | `CreatingCopyJobStateStoredInstrumentedTest.java` | NEW | `tests/services/src/androidTest/.../copylet/service/` |
| 3 | `StoredCopyJobAdapterInstrumentedTest.java` | NEW | `tests/services/src/androidTest/.../copylet/adapter/` |

---

## 6. Full Development Completion Checklist

Full StoredCopyJob implementation status at Session 5 completion:

### Production Code (17 files)
| # | File | Type | Session | ✅ |
|---|------|------|------|---|
| 1 | IDeviceCopyJobService.java | UPDATE | Session 1 | □ |
| 2 | StandardDeviceCopyJobService.java | UPDATE | Session 1 | □ |
| 3 | StoredCopyJobPreferenceStorage.java | NEW | Session 1 | □ |
| 4 | CopyOptionProfileAdapter.java | UPDATE | Session 1 | □ |
| 5 | StoredCopyJobAdapter.java | NEW | Session 1 | □ |
| 6 | MonitoringStoredCopyJobState.java | NEW | Session 1 | □ |
| 7 | CreatingCopyJobState.java | UPDATE | Session 1+2+3 | □ |
| 8 | CopyJobAdapter.java | UPDATE | Session 2 | □ |
| 9 | OXPCopyletContentProvider.java | UPDATE | Session 2 | □ |
| 10 | CopyDeviceAdapter.java | UPDATE | Session 4 | □ |
| 11 | MonitoringCopyJobState.java | UPDATE | Session 4 | □ |
| 12 | CopyJobIntentService.java | UPDATE | Session 4 | □ |
| 13 | ReleaseStoredJobRequest.java | UPDATE | Session 4 | □ |
| 14 | StoredJob.java | UPDATE | Session 4 | □ |
| 15 | ResourceFacadeHelper.java | UPDATE | Session 4 | □ |
| 16 | ReleaseStoredJobOperationResourceFacade.java | UPDATE | Session 4 | □ |
| 17 | RemoveStoredJobOperationResourceFacade.java | UPDATE | Session 4 | □ |

### Unit Tests (6 files)
| # | File | Type | Session | ✅ |
|---|------|------|------|---|
| 1 | StoredCopyJobPreferenceStorageTest.java | NEW | Session 4 | □ |
| 2 | StoredCopyJobAdapterTest.java | NEW | Session 4 | □ |
| 3 | MonitoringStoredCopyJobStateTest.java | NEW | Session 4 | □ |
| 4 | CreatingCopyJobStateStoredTest.java | NEW | Session 4 | □ |
| 5 | CopyJobAdapterStoredTest.java | NEW | Session 4 | □ |
| 6 | CopyOptionProfileAdapterUnitTest.java | UPDATE | Session 4 | □ |

### Android Instrumented Tests (3 files)
| # | File | Type | Session | ✅ |
|---|------|------|------|---|
| 1 | StoredCopyJobPreferenceStorageInstrumentedTest.java | NEW | Session 5 | ✅ |
| 2 | CreatingCopyJobStateStoredInstrumentedTest.java | NEW | Session 5 | ✅ |
| 3 | StoredCopyJobAdapterInstrumentedTest.java | NEW | Session 5 | ✅ |

### Build & Tests
| Item | Command | ✅ |
|------|--------|---|
| CopyLet build | `./gradlew :Let-CopyLet:assembleDebug` | □ |
| Test module build | `./gradlew :Test-WorkpathServices:assembleDebug` | □ |
| All unit tests pass | `./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "...copylet.*"` | □ |
| Code coverage 80%+ | `./gradlew :Test-WorkpathServices:jacocoTestReport` | □ |
| All androidTests pass | (19 passed — 2026-03-27) | ✅ |

> **🎉 When all items are ✅, DUNE-169955 StoredCopyJob implementation is complete!**
