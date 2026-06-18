# Sonar Qube Issue List

## CopyJobAdapter.java

1. Line88: A "NullPointerException" could be thrown; "detailedJob" is nullable here.
   - **Fixed**: `detailedJob != null` 가드 조건 추가 (`convertToStoredJobInfoList` 루프 내)
2. Line105: Refactor this method to reduce its Cognitive Complexity from 16 to the 15 allowed.
   - **Fixed**: `fetchStoredJobDetails`의 로그 문자열을 `buildStoredJobLogString(StoredJob)` 헬퍼 메서드로 추출하여 복잡도 감소
3. Line120: Define a constant instead of duplicating this literal ", copies=" 3 times.
   - **Fixed**: `private static final String LOG_COPIES = ", copies=";` 상수 정의 및 3곳 모두 적용
4. Line121: Define a constant instead of duplicating this literal ", totalPages=" 3 times.
   - **Fixed**: `private static final String LOG_TOTAL_PAGES = ", totalPages=";` 상수 정의 및 3곳 모두 적용
5. Line132: Refactor this method to reduce its Cognitive Complexity from 37 to the 15 allowed.
   - **Fixed**: `convertStoredJob` 메서드에서 다음 헬퍼 메서드들을 추출하여 복잡도 대폭 감소:
     - `buildStoredJobLogString()` — 로그 문자열 생성 (ternary 제거)
     - `nullToEmpty()` — null→"" 변환
     - `extractTotalPages()`, `extractCopies()`, `extractColorMode()`, `extractScanSize()`, `extractDuplex()` — E2 필드 추출
     - `resolveSavedColorMode()`, `resolveSavedScanSize()`, `resolveSavedDuplex()`, `resolveSavedCopies()` — savedAttributes 오버라이드
6. Line269: Replace generic exceptions with specific library exceptions or a custom exception.
   - **Fixed**: `getCopyJobTicketForRelease`, `convertDefaultOptionsToCopyOptions`, `getCopyJobTicket` 세 메서드의 `throws Exception` → `throws IOException` 변경 (ObjectMapper 사용에 의한 실제 예외 타입)
7. Line73: The return type of this method should be an interface such as "List" rather than the implementation "ArrayList".
   - **Fixed**: `convertToStoredJobInfoList` 반환 타입을 `ArrayList<StoredJobInfo>` → `List<StoredJobInfo>`로 변경
   - **Related fix**: 반환 타입 변경에 따른 호출부 수정 (빌드 에러 해결)
     - `CopyJobAdapterStoredTest.java`: 21개 `ArrayList<StoredJobInfo> result` → `List<StoredJobInfo> result` 변경, `import java.util.List` 추가
     - `OXPCopyletContentProvider.java`: 변수 타입 `ArrayList<StoredJobInfo>` → `List<StoredJobInfo>` 변경, `putParcelableArrayList()` 호출 시 `new ArrayList<>(storedJobInfoList)` 래핑, `import java.util.List` 추가
   - **Build result**: `:Let-CopyLet:assembleDebug` BUILD SUCCESSFUL, `:Test-WorkpathServices:testDebugUnitTest` (CopyJobAdapterStoredTest) BUILD SUCCESSFUL

## StoredCopyJobAdapter.java

1. Line60: Replace generic exceptions with specific library exceptions or a custom exception.
   - **Fixed**: `createScanJobForStorage`에서 `throws Exception` 제거 (메서드 내 checked exception 발생 없음 — 서비스 인터페이스가 checked exception 미선언, ObjectMapper 사용은 내부 try-catch 처리)

## CreatingCopyJobState.java

2. Line284: Refactor this method to reduce its Cognitive Complexity from 16 to the 15 allowed.
   - **Fixed**: `processDeleteJobIntent`의 비밀번호 설정 로직을 `applyRemoveJobPassword()` 헬퍼 메서드로 추출하여 복잡도 감소
3. Line364: Refactor this method to reduce its Cognitive Complexity from 23 to the 15 allowed.
   - **Fixed**: `processReleaseJobIntent`의 비밀번호 설정 로직을 `applyReleaseJobPassword()`, CopyOptions 빌드 로직을 `buildReleaseCopyOptions()` 헬퍼 메서드로 추출하여 복잡도 감소
4. Line286: Replace generic exceptions with specific library exceptions or a custom exception.
   - **Fixed**: `processDeleteJobIntent`에서 `throws Exception` 제거 (내부 try-catch로 모든 예외 처리, checked exception 전파 없음)
5. Line286: Remove the declaration of thrown exception 'java.lang.Exception', as it cannot be thrown from method's body.
   - **Fixed**: 항목 3과 동일 — `throws Exception` 선언 제거

## MonitoringStoredCopyJobState.java

1. Line38: Remove the "scanNotificationCallback" field and declare it as a local variable in the relevant methods.
   - **Fixed**: `scanNotificationCallback` 필드를 제거하고 `registerNotificationCallback()` 메서드 내 지역 변수로 선언
2. Line55: Make this anonymous inner class a lambda.
   - **Fixed**: `IE2PayloadCallback<ScanNotification>` 익명 내부 클래스를 람다 `(appPackageId, notification) -> { ... }`로 변환
   - **Build result**: `:Let-CopyLet:assembleDebug` BUILD SUCCESSFUL, 4개 variant 전체 BUILD SUCCESSFUL

## StandardDeviceCopyJobService.java

1. Line134: Define a constant instead of duplicating this literal ", storedJobId=" 3 times.
   - **Fixed**: `private static final String LOG_STORED_JOB_ID = ", storedJobId=";` 상수 정의 및 `getStoredJob`, `releaseStoredJob`, `deleteStoredJob` 3곳 적용
   - **Build result**: `:DeviceServices-Standard:assembleDebug` BUILD SUCCESSFUL
