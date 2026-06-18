# Copilot Development Guidelines (Workpath-services Project)

## Project Overview
- **Platform:** Android (Java, Gradle)
- **Structure:** Multi-module Gradle project (Libs/, Apps/, tests/, API-Tests/)
- **Build System:** Gradle wrapper (`./gradlew`)
- **Testing:** JUnit 4 + Mockito (Unit), AndroidJUnit4 (Instrumented)

---

## 1. Development Process Principles

### 1.1 Analysis-Derive-Review Loop

When writing a development plan, repeat the following cycle **until there is nothing more to add**:

1. **Analysis:** Read and understand requirements documents, architecture documents (md, puml), and related existing code
2. **Derive:** Identify files, methods, data flows, and risk factors that need to be modified or added
3. **Review:** Cross-check derived results against requirements and existing code to identify omissions, errors, or inconsistencies
4. **Repeat:** If new findings emerge during the review, restart from step 1

### 1.2 Cross-Validation

Important analyses (development plans, process analyses, etc.) must be performed **independently at least twice**, and results compared to produce the final document. Each analysis is conducted independently without referencing previous analysis results.

### 1.3 Session Splitting Principle

Task requests submitted to AI follow these principles:
- Split into **manageable work units** per session (1 session ≈ 1–3 file modifications + build verification)
- Each session request must be written so that **AI can independently understand and execute it on its own**
- Specify prerequisites, reference documents, completion criteria, and build verification commands

### 1.4 Documentation Principle

**During all development work, document the work done in md files at the designated path.**
- Record analysis results, design decision rationale, code changes, and issue resolution processes
- Organize document paths by feature folder and phase

### 1.5 Build-Fix Loop

After code modifications are complete, repeat the following loop **until the build succeeds**:
1. Execute APK build (assembleDebug for the relevant module or full build)
2. Check and fix build errors
3. Rebuild → repeat until no errors remain
4. Report the final successful build result to the user

### 1.6 Test Coverage

When writing test code, target **80% or higher code line coverage**.

---

## 2. Build Commands

```bash
# Individual module build
./gradlew :Let-CopyLet:assembleDebug
./gradlew :DeviceServices-Interfaces:assembleDebug
./gradlew :DeviceServices-Standard:assembleDebug

# Full Services build (production code integration check)
./gradlew :Test-WorkpathServices:assembleDebug

# Sim build (for simulator)
./gradlew :Test-WorkpathServices:assembleDebugForSim

# Release build
./gradlew :Test-WorkpathServices:assembleRelease
./gradlew :Test-WorkpathServices:assembleReleaseForSim

# Unit tests
./gradlew :Test-WorkpathServices:testDebugUnitTest

# Run specific test class only
./gradlew :Test-WorkpathServices:testDebugUnitTest --tests "com.hp.jetadvantage.link.services.copylet.*"

# Android instrumented tests
./gradlew :Test-WorkpathServices:connectedDebugAndroidTest
```

---

## 3. Code Writing Rules

### 3.1 Java Code Style
- Follow SonarQube rules: Cognitive Complexity ≤ 15, no literal duplication, use specific exception types
- Use interfaces (List) instead of implementation classes (ArrayList) for return types
- NullPointerException prevention: perform null checks before using nullable objects

### 3.2 Test Code Patterns
- **Unit Test:** `@RunWith(MockitoJUnitRunner.class)`, Given-When-Then naming
- **Android Test:** `@RunWith(AndroidJUnit4.class)`, `extends BaseInstrumentedTest`
- Always reference existing test patterns to maintain consistency

### 3.3 Build Variant Handling
- Four build variants exist: `debug`, `release`, `debugForSim`, `releaseForSim`
- When adding methods to an interface, **modify all implementations** (Standard, Sim, etc.) together
- After writing new code, verify builds for variants other than `debug` as well
