# Workpath API Test

Workpath Services API integration test project.

## Overview

This project tests various Workpath Services APIs from the perspective of a Test App. The Test App calls APIs using the Workpath SDK Library for different scenarios. The test framework uses Android Instrumentation Tests.

## Quick Start

### Prerequisites
- WorkpathAPI Test App (Test-WorkpathAPIs-debug.apk) must be installed on the target device
- ADB connection to the target device must be available

### Build

```bash
# Windows (PowerShell)
.\gradlew :tests:api:apiTest:assembleDebug
.\gradlew :tests:api:apiTest:assembleDebugAndroidTest
```

### Running Tests

```bash
# Run all tests
.\gradlew :tests:api:apiTest:connectedDebugAndroidTest

# Run specific test class
.\gradlew :tests:api:apiTest:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hp.workpath.apitest.accessory.hid.AccessoryServiceTest

# Run specific test method
.\gradlew :tests:api:apiTest:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hp.workpath.apitest.accessory.hid.AccessoryServiceTest#AccessoryService_isSupported_\$ReturnsTrue
```

### Test Results

After running tests, results can be found at:
- HTML Report: `build/reports/androidTests/connected/debug/index.html`
- XML Results: `build/outputs/androidTest-results/connected/debug`

## API Test Method Naming Convention

All test methods follow a standardized naming convention to improve readability and enable automated documentation generation.

**Format**: `[ClassName]_[Method1]_[Method2]_..._$[Scenario]`

**Quick Examples**:
```java
// Simple test
public void AccessoryService_isReady_$ReturnsTrue() { }

// BDD-style test
public void ScannerService_submit_$GivenHttpDestination_WhenSubmit_ThenReturnsRidAndJobCompletes() { }
```

📖 **For complete naming guidelines and examples, see**: [`docs/naming-convention-guide.md`](docs/naming-convention-guide.md)

## Target APIs

This project tests the following Workpath SDK APIs:
- Accessory Service (HID)
- Configuration Service
- Scanner Service
- Mass Storage Service
- And more...

📋 **For the complete list of tested APIs, see**: [`docs/workpath-api-list.md`](docs/workpath-api-list.md)


