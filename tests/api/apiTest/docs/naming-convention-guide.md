# API Test Method Naming Convention Guide

## Quick Start

For most test cases, use this format:
```
[ClassName]_[method1]_[method2]_..._$Given[X]_When[Y]_Then[Z]
```
- **ClassName**: Target API class name being tested
- **method1, method2, ...**: Target API method names being tested
- **Given[X]_When[Y]_Then[Z]**: Test scenario (BDD style)
**Example:**
```java
@Test
public void DeviceService_connect_$GivenNetworkAvailable_WhenConnect_ThenSuccess() { }
```

**For simple cases:**
```java
@Test
public void AccessoryService_isReady_$ReturnsTrue() { }
```

---

## Recommended Naming Convention

### Format
```
[ClassName]_[Method1]_[Method2]_..._$[Scenario]
```

### Structure Description
- **ClassName**: Target API class name being tested
- **Methods**: One or more target API method names being tested (separated by `_`)
- **Scenario**: Everything after `$` (test scenario or expected result)
  - **Recommended**: `Given_When_Then` format (BDD style)

### Parsing Rules
1. **With `$`**: Everything after `$` is the Scenario (`_$` recommended, `$` alone also acceptable)
2. **Without `$`**: Last `_` separated part is the Scenario

---

## Examples

### Basic Form (Single Method)
```java
@Test
public void AccessoryService_isReady_$ReturnsTrue() { }

@Test  
public void DeviceService_getDeviceInfo_$ReturnsDeviceInfo() { }

// Without $ is also possible (for simple cases)
public void AccessoryService_isSupported_ReturnsTrue() { }
```

### Multiple Methods (Sequence)
```java
@Test
public void AccessoryService_open_getinfo_close_$ReturnsResultOK() { }

@Test
public void ScannerService_startScan_getScanStatus_cancelScan_$WorksCorrectly() { }
```

### Detailed Scenario Expression (Given_When_Then Recommended)
```java
@Test
public void AccessoryService_open_getinfo_close_$GivenDeviceReady_WhenOpen_ThenReturnsSuccess() { }

@Test
public void DeviceService_connect_$GivenNetworkAvailable_WhenConnect_ThenSuccess() { }

@Test
public void ScannerService_startScan_$GivenDeviceBusy_WhenStartScan_ThenThrowsException() { }

// Simple result expression is also acceptable
@Test
public void AccessoryService_isReady_$ReturnsTrue() { }
```

## Scenario Naming Patterns

### Recommended: Given_When_Then Pattern (BDD Style)
```java
Given[Precondition]_When[Action]_Then[Expected]

// Examples:
GivenDeviceReady_WhenOpen_ThenSuccess
GivenNetworkAvailable_WhenConnect_ThenReturnsConnection
GivenDeviceBusy_WhenStart_ThenThrowsException

// Each block (Given/When/Then) is written in CamelCase
// Blocks are separated by underscore (_)
```

### Alternative Patterns (For Simple Cases)

| Pattern | Purpose | Examples |
|---------|---------|----------|
| `ReturnsXxx` | Return value validation | `ReturnsTrue`, `ReturnsDeviceInfo` |
| `ThrowsXxx` | Exception verification | `ThrowsNullPointerException` |
| `UpdatesXxx` | State change verification | `UpdatesConfiguration` |
| `CallsXxx` | Callback invocation verification | `CallsOnReady`, `CallsObserver` |
| `ValidatesXxx` | Input validation | `ValidatesInput` |
| `HandlesXxx` | Exception handling verification | `HandlesTimeout` |

---

## Key Summary

1. **Format**: `[ClassName]_[Method1]_[Method2]_..._$[Scenario]`
2. **$ Separator**: Clarifies Scenario (`_$` recommended)
3. **Parsing**: If `$` exists, everything after is Scenario; otherwise, last part
4. **Scenario Naming**: `Given[X]_When[Y]_Then[Z]` pattern recommended (for simple cases, `ReturnsXxx`, `ThrowsXxx`, etc. are acceptable)
