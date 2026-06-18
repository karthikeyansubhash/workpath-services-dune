# Session 4: Unit Tests — Coverage 80% or Higher

> **StoredCopyJob Development — Session 4/5**
> **Phase 6a (Unit Tests)**
> **Reference Document:** `StoredCopyJob_DevelopmentPlan_Final.md` Section 13.2
> **Prerequisites:** All production code implementation completed in Sessions 1-3

---

## 1. Session Goal

Write unit tests for StoredCopyJob production code. **Code coverage of 80% or higher** is mandatory.

**Completion Criteria:**
- 5 new test files created + 1 existing test file updated
- All unit tests pass: `./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.*"`
- Line coverage of 80% or higher for newly added/modified code

---

## 2. Test Environment Information

**Test Location:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/`

**Frameworks & Dependencies:**
- JUnit 4 (`@RunWith(MockitoJUnitRunner.class)`)
- Mockito + mockito-inline (static method mocking support)
- `unitTests.returnDefaultValues = true` — Android framework methods return default values

**Existing Test Patterns (must reference):**
- `CreatingCopyJobStateTest.java` — `testMode=true` constructor, Mock Intent/Bundle/StateMachine/Service patterns
- `MonitoringCopyJobStateUnitTest.java` — Direct `copyJobService` field replacement, Notification callback test patterns
- `CopyOptionProfileAdapterUnitTest.java` — Caps/Options conversion test patterns

**Test Structure Pattern:**
```java
@RunWith(MockitoJUnitRunner.class)
public class XxxTest {
    @Mock private SomeDependency mockDependency;

    private TargetClass target;

    @Before
    public void setUp() {
        target = new TargetClass(/* testMode or mock injection */);
    }

    @Test
    public void GivenCondition_WhenAction_ThenExpectedResult() {
        // Arrange
        when(mockDependency.method()).thenReturn(value);
        // Act
        Result result = target.doSomething();
        // Assert
        assertTrue(result instanceof ExpectedType);
        verify(mockDependency).method();
    }
}
```

---

## 3. Work Order and Detailed Instructions

### Test File 1: StoredCopyJobPreferenceStorageTest.java (NEW)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/service/StoredCopyJobPreferenceStorageTest.java`

**Target:** `StoredCopyJobPreferenceStorage` — SharedPreferences CRUD

**Note:** With `unitTests.returnDefaultValues = true`, Android SharedPreferences returns default values (null, false, etc.). Actual SharedPreferences behavior is verified in androidTest, so here we use Context mock + SharedPreferences mock patterns.

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `GivenContext_WhenSave_ThenPutStringCalled` | Verify SharedPreferences.Editor.putString(key, value).apply() is called when save() is invoked |
| 2 | `GivenSavedData_WhenGet_ThenCorrectValueReturned` | Verify getString(key, null) return value when get() is invoked |
| 3 | `GivenSavedData_WhenRemove_ThenRemoveCalled` | Verify Editor.remove(key).apply() is called when remove() is invoked |
| 4 | `GivenSavedData_WhenReplaceKey_ThenOldRemovedAndNewSaved` | Verify remove(oldKey) + putString(newKey, value) when replaceKey() is invoked |
| 5 | `GivenNoData_WhenReplaceKey_ThenNothingHappens` | Verify no action when old key doesn't exist |
| 6 | `GivenNoData_WhenGet_ThenNullReturned` | Verify null return when querying a non-existent key |

**Mock Structure:**
```java
@Mock private Context mockContext;
@Mock private SharedPreferences mockSharedPreferences;
@Mock private SharedPreferences.Editor mockEditor;

@Before
public void setUp() {
    when(mockContext.getSharedPreferences("stored_copy_job_map", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences);
    when(mockSharedPreferences.edit()).thenReturn(mockEditor);
    when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
    when(mockEditor.remove(anyString())).thenReturn(mockEditor);
}
```

---

### Test File 2: StoredCopyJobAdapterTest.java (NEW)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/adapter/StoredCopyJobAdapterTest.java`

**Target:** `StoredCopyJobAdapter.createScanJobForStorage()`

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `GivenValidCopyAttributes_WhenCreateScanJob_ThenScanJobCreated` | Normal flow: Verify scanJobService.createScanJob() is called + scanJobId returned |
| 2 | `GivenCopyAttributes_WhenCreateScanJob_ThenScanOptionsCorrectlyMapped` | Verify CopyAttributes → ScanOptions conversion (each field) |
| 3 | `GivenCopyAttributes_WhenCreateScanJob_ThenJobStorageDestinationSet` | Verify DestinationOptions.jobStorage is set |
| 4 | `GivenStoreJobName_WhenCreateScanJob_ThenTicketNameSet` | Verify ScanTicket name/folderName are set |
| 5 | `GivenNullScanJobService_WhenCreateScanJob_ThenExceptionThrown` | null service → Exception |
| 6 | `GivenServiceError_WhenCreateScanJob_ThenExceptionPropagated` | E2 error → Exception propagated |
| 7 | `GivenCredentials_WhenCreateScanJob_ThenPasswordFieldsSet` | Verify storeJobPasswordType, storeJobPassword are set |
| 8 | `GivenRetentionMode_WhenCreateScanJob_ThenRetentionFieldsSet` | Verify retentionModeOnPowerCycle, retentionModeOnRelease are set |

**Mock Structure:**
```java
@Mock private IDeviceScanJobService mockScanJobService;
@Mock private CopyAttributesReader mockCopyAttributes;
@Mock private DefaultOptions mockDefaultOptions;

@Before
public void setUp() {
    when(mockScanJobService.getDefaultOptions(anyString())).thenReturn(mockDefaultOptions);
}
```

---

### Test File 3: MonitoringStoredCopyJobStateTest.java (NEW)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/service/MonitoringStoredCopyJobStateTest.java`

**Target:** `MonitoringStoredCopyJobState` — ScanNotification monitoring

**Existing Reference:** `MonitoringCopyJobStateUnitTest.java` (same pattern, CopyNotification → ScanNotification)

**Required Test Cases:**

| # | Test Method | Verification |
|---|------------|----------|
| 1 | `GivenState_WhenRegisterCallback_ThenScanNotificationCallbackCreated` | Verify scanJobService.registerNotificationCallback() is called when registerNotificationCallback() is invoked |
| 2 | `GivenState_WhenUnregisterCallback_ThenCallbackUnregistered` | Verify scanJobService.unRegisterNotificationCallback() is called when unregisterNotificationCallback() is invoked |
| 3 | `GivenScanNotification_WhenJobStatusReceived_ThenCopyJobStateUpdated` | Verify ScanJobStatus → CopyJobState conversion + JobBundle update |
| 4 | `GivenScanNotification_WhenJobDone_ThenProcessJobDoneStatusCalled` | Verify ScanJobStatus.jobDoneStatus → processJobDoneStatus() transition |
| 5 | `GivenNullNotification_WhenCallbackInvoked_ThenNoAction` | null notification → ignored |
| 6 | `GivenState_WhenCreated_ThenStateNameIsMonitoringJobState` | Verify STATE_NAME inheritance ("MonitoringJobState") |
| 7 | `GivenScanNotification_WhenImagesScanned_ThenCountUpdated` | Verify imagesScanned counter update |

**Mock Structure:**
```java
@Mock private IDeviceScanJobService mockScanJobService;
@Mock private CopyJobIntentServiceStateMachine mockStateMachine;
@Mock private Bundle mockJobBundle;

private MonitoringStoredCopyJobState state;

@Before
public void setUp() {
    state = new MonitoringStoredCopyJobState("testScanJobId");
    state.scanJobService = mockScanJobService;  // Direct field replacement (MonitoringCopyJobState pattern)
}
```

---

### Test File 4: CreatingCopyJobStateStoredTest.java (NEW)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/service/CreatingCopyJobStateStoredTest.java`

**Target:** Stored Copy related branches in `CreatingCopyJobState` (processStoredCopyJobIntent, processReleaseJobIntent, processDeleteJobIntent)

**Existing Reference:** `CreatingCopyJobStateTest.java` — `testMode=true` constructor + Mock patterns

**Required Test Cases:**

| # | Test Method | Target | Verification |
|---|------------|------|----------|
| 1 | `GivenStoreMode_WhenProcessCopyJob_ThenStoredCopyJobIntentProcessed` | processStoredCopyJobIntent | STORE branch entered + MonitoringStoredCopyJobState returned |
| 2 | `GivenStoreMode_WhenScanJobFails_ThenReportErrorReturned` | processStoredCopyJobIntent | scanJobId null → ReportErrorState + PreferenceStorage cleanup |
| 3 | `GivenStoreMode_WhenScanJobCreated_ThenPreferenceStorageSaved` | processStoredCopyJobIntent | Verify PreferenceStorage.save() + replaceKey() calls |
| 4 | `GivenValidRelease_WhenProcessRelease_ThenMonitoringCopyStateReturned` | processReleaseJobIntent | Release success → MonitoringCopyJobState returned |
| 5 | `GivenNoStoredAttrs_WhenProcessRelease_ThenReportErrorReturned` | processReleaseJobIntent | Not in PreferenceStorage → ReportErrorState |
| 6 | `GivenNullStoredJobAttributes_WhenProcessRelease_ThenErrorReturned` | processReleaseJobIntent | reqParams missing → ReportErrorState |
| 7 | `GivenValidDelete_WhenProcessDelete_ThenResultReceiverSent` | processDeleteJobIntent | Success → resultReceiver.send(RESULT_OK) + EndState |
| 8 | `GivenDeleteFailed_WhenProcessDelete_ThenErrorResultSent` | processDeleteJobIntent | Exception → resultReceiver.send(RESULT_CANCELED) |
| 9 | `GivenNoStoredJobId_WhenProcessDelete_ThenErrorResultSent` | processDeleteJobIntent | storedJobId null → resultReceiver.send(RESULT_CANCELED) |
| 10 | `GivenNullResultReceiver_WhenProcessDelete_ThenNoException` | processDeleteJobIntent | ResultReceiver null → EndState without exception |

**Mock Structure:**
```java
@Mock private Intent mockIntent;
@Mock private BaseJobIntentServiceStateMachine mockStateMachine;
@Mock private Bundle mockBundle;
@Mock private Bundle mockExtraParams;
@Mock private IDeviceCopyJobService mockCopyJobService;
@Mock private ResultReceiver mockResultReceiver;
@Mock private Context mockContext;

private CreatingCopyJobState state;

@Before
public void setUp() {
    when(mockStateMachine.getContext()).thenReturn(mockContext);
    state = new CreatingCopyJobState(true, mockIntent, mockCopyJobService);
}
```

> ⚠️ **processDeleteJobIntent's Intent parameter:**
> Mock setup needed for `intent.getParcelableExtra(EXTRA_RESULT_RECEIVER)`:
> ```java
> when(mockIntent.getParcelableExtra(CopyJobIntentService.EXTRA_RESULT_RECEIVER))
>         .thenReturn(mockResultReceiver);
> ```

---

### Test File 5: CopyJobAdapterStoredTest.java (NEW)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyJobAdapterStoredTest.java`

**Target:** Stored Copy related new methods in `CopyJobAdapter`

**Required Test Cases:**

| # | Test Method | Target | Verification |
|---|------------|------|----------|
| 1 | `GivenStoredJobs_WhenConvert_ThenStoredJobInfoListReturned` | convertToStoredJobInfoList | Normal conversion — verify jobId, jobName, folderName, etc. mapping |
| 2 | `GivenNullStoredJobs_WhenConvert_ThenEmptyListReturned` | convertToStoredJobInfoList | null input → empty list |
| 3 | `GivenEmptyMembers_WhenConvert_ThenEmptyListReturned` | convertToStoredJobInfoList | Empty members list → empty list |
| 4 | `GivenMultipleMembers_WhenConvert_ThenAllMembersFetched` | convertToStoredJobInfoList | Multiple members → verify individual GET call count |
| 5 | `GivenDetailedJob_WhenFetched_ThenFieldsMapped` | fetchStoredJobDetails | Individual GET success → verify detailed field conversion |
| 6 | `GivenFetchFails_WhenConvert_ThenSummaryUsed` | fetchStoredJobDetails | Individual GET failure → summary fallback |
| 7 | `GivenSavedAttrsAndStoredAttrs_WhenBuildRelease_ThenCopyOptionsReturned` | getCopyJobTicketForRelease | Normal flow — CopyJobTicket returned |
| 8 | `GivenStoredAttrsWithCopiesOverride_WhenBuildRelease_ThenCopiesOverridden` | getCopyJobTicketForRelease | storedJobAttributes.getCopies() takes priority |

**Note:** `convertToStoredJobInfoList(StoredJobs, IDeviceCopyJobService, String)` — 3-parameter signature. Converts after individual GET calls.

---

### Test File 6: CopyOptionProfileAdapterUnitTest.java (UPDATE)

**File:** `tests/services/src/test/java/com/hp/jetadvantage/link/services/copylet/adapter/CopyOptionProfileAdapterUnitTest.java`

**1 test case to add:**

```java
@Test
public void GivenCopyAttributeCaps_WhenBuilt_ThenStoreJobExecutionModeIncluded() {
    // Arrange + Act: Obtain Caps via getCaps() or createCopyAttributeCapsBuilder()
    // Assert: Verify that caps contains JobExecutionMode.STORE
    CopyAttributesCaps caps = /* ... */;
    assertTrue(caps.getJobExecutionModes().contains(CopyAttributes.JobExecutionMode.STORE));
}
```

---

## 4. Test Execution Commands

```bash
# Run all CopyLet unit tests
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.*"

# Run individual test files (for debugging)
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.service.StoredCopyJobPreferenceStorageTest"
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.adapter.StoredCopyJobAdapterTest"
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.service.MonitoringStoredCopyJobStateTest"
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.service.CreatingCopyJobStateStoredTest"
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.adapter.CopyJobAdapterStoredTest"
```

Fix test failures by modifying production code or test code and re-running.

---

## 5. Coverage Verification

```bash
# Generate JaCoCo coverage report (if configured)
./gradlew :Test-WorkpathServices:jacocoTestReport
```

**Files Subject to 80% Coverage Criteria:**
- `StoredCopyJobAdapter.java`
- `MonitoringStoredCopyJobState.java`
- `StoredCopyJobPreferenceStorage.java`
- `CreatingCopyJobState.java` (Stored branches: processStoredCopyJobIntent, processReleaseJobIntent, processDeleteJobIntent)
- `CopyJobAdapter.java` (Stored methods: convertToStoredJobInfoList, buildCopyOptionsForRelease)
- `CopyOptionProfileAdapter.java` (addJobExecutionMode STORE addition)

---

## 6. Created/Modified Files Checklist

| # | File | Type | Location |
|---|------|------|------|
| 1 | `StoredCopyJobPreferenceStorageTest.java` | NEW | `tests/services/src/test/.../copylet/service/` |
| 2 | `StoredCopyJobAdapterTest.java` | NEW | `tests/services/src/test/.../copylet/adapter/` |
| 3 | `MonitoringStoredCopyJobStateTest.java` | NEW | `tests/services/src/test/.../copylet/service/` |
| 4 | `CreatingCopyJobStateStoredTest.java` | NEW | `tests/services/src/test/.../copylet/service/` |
| 5 | `CopyJobAdapterStoredTest.java` | NEW | `tests/services/src/test/.../copylet/adapter/` |
| 6 | `CopyOptionProfileAdapterUnitTest.java` | UPDATE (+1 test) | `tests/services/src/test/.../copylet/adapter/` |

## 7. Prompt to Request from User After Session Completion

```
Referring to StoredCopyJob_DevelopmentPlan_Final.md, please write
Android instrumented tests (androidTest) according to Session5_AndroidTest.md.
Production code from Sessions 1-3 and unit tests from Session 4 are complete.
Please run the tests and fix any failing tests.
```
