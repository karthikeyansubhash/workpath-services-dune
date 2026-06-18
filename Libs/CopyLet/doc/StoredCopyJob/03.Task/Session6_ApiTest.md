# StoredCopyJob API 테스트 개발 요청서 (Session 6)

## 개요
이 문서는 SDK 소비자(앱/연동사)가 사용하는 **Workpath API (`tests/api/apiTest`)** 레벨에서 StoredCopyJob 기능이 정상적으로 노출되고 동작하는지 검증하기 위한 테스트 코드 작성 지침입니다.

## 목표
`API-JetAdvantageLinkApi`에 새롭게 추가된 StoredCopy 기능(STORE 모드 복사, 저장된 작업 목록 조회, 릴리즈, 삭제)에 대한 `apiTest` 모듈 내 자동화 테스트를 구현합니다.

## 대상 API
- `CopierService.submit` (STORE 모드: `CopyAttributes.StoreCopyBuilder` 사용)
- `CopierService.enumerateStoredJob(Context, Result)`
- `CopierService.releaseStoredJob(Context, StoredJobAttributes)`
- `CopierService.deleteStoredJob(Context, String, JobCredentialsAttributes, Result)`

## 상세 구현 단계

### 1. `build.gradle` 의존성 추가
API 테스트에서 내부 구현체를 직접 참조해야 할 경우 `tests/api/apiTest/build.gradle`에 다음을 추가하세요.
```gradle
androidTestImplementation project(':API-JetAdvantageLinkApi')
```

### 2. JobObserver 확장 (필요 시)
기존 `com.hp.workpath.api.job.JobService.AbstractJobletObserver` 대신 `com.hp.jetadvantage.link.api.job.JobService.AbstractJobletObserver`를 상속받는 `LinkApiJobObserver`를 구현하여 비동기 작업(submit 등)의 진행 상태와 결과를 수신할 래퍼를 만듭니다.

### 3. API Test 클래스 작성
`tests/api/apiTest/src/androidTest/java/com/hp/workpath/apitest/copier/StoredCopyJobApiTest.java` 파일을 생성하고 다음 테스트 케이스를 포함합니다:
1. **enumerateStoredJob 테스트**: `isSupported` 체크 후 호출하여 `List<StoredJobInfo>`를 정상적으로 반환하는지 (또는 에러코드 없이 빈결과/NotSupported 리턴하는지) 확인.
2. **submitStoredCopy 테스트**: `getDefaults()`로 속성을 얻은 뒤 `StoreCopyBuilder`를 통해 Folder/Job Name을 셋팅하고 `submit`을 호출, 결과 RID가 유효한지 확인.
3. **releaseStoredJob 테스트**: 가상의 `StoredJobAttributes`를 생성하여 호출 시 UUID 형식의 요청 RID가 반환되는지 확인.
4. **deleteStoredJob 테스트**: 가상의 `jobId`를 삭제 요청하여 `Result` 객체에 Crash 없이 적절한 응답이 반환되는지 확인.

## 검증 방법
테스트 기기(E2 또는 Dune Emulator)를 PC에 ADB 연결한 후 아래 명령을 통해 전체 API 테스트가 통과하는지 확인합니다.
```bash
./gradlew :Test-WorkpathAPIs:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.hp.workpath.apitest.copier.StoredCopyJobApiTest"
```
이 테스트들은 SDK가 공개적으로 제공하는 기능의 계약(Contract)이 깨지지 않는지 보장하는 용도입니다.
