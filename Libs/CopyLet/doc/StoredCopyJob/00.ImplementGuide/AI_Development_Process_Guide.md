# AI-Based Feature Development Process Guide

> This document is an **AI collaborative development process template** based on the development experience of the StoredCopyJob epic (DUNE-169955).
> When developing new features, following the steps in this document in order enables systematic development.

---

## Overall Process Overview

```
Phase 1: Reference
  ↓
Phase 2: Analysis
  ↓
Phase 3: Task
  ↓
Phase 4: Implementation
  ↓
Phase 5: Testing
  ↓
Phase 6: Validation
  ↓
Phase 7: Report
```

---

## Phase 1: Reference

### Purpose

Research requirements and related technologies, and organize reference documents.

### Deliverables

- Stored in the `01.Reference/` folder
- Requirements overview document (md)
- Initial investigation document (md) — API specifications, existing code analysis, design option comparison
- Architecture diagrams (puml) — sequence, component

### Actual Example (StoredCopyJob)

| File                                            | Description                                         |
| ----------------------------------------------- | --------------------------------------------------- |
| `StoredCopyJob_Overview.md`                   | Overall scope, design option comparison, detailed component definitions |
| `StoredCopyJob_Initial_Investigation.md`      | E2 API specification research, Workpath API mapping, Considerations |
| `StoredCopyJob_Create.puml`                   | Create sequence diagram                             |
| `StoredCopyJob_EnumerateAndRelease.puml`      | Enumerate/Release sequence diagram                  |
| `StoredCopyJob_ProfileAndDefaultOptions.puml` | Profile/DefaultOptions sequence diagram             |

### Example AI Request

```
Read the following requirements document and create an initial investigation document.
- [Requirements document path or content]

Include:
1. Analysis of existing related code (how it currently works)
2. APIs and data flows to be added/modified
3. If there are multiple design options, organize them in a comparison table
4. PlantUML sequence diagrams

Save documents in the {feature_name}/01.Reference/ folder.
```

---

## Phase 2: Analysis and Development Plan

### Purpose

Perform in-depth analysis of requirements and code to create a development plan.

### Core Principle: Analysis-Derive-Review Loop

1. **Analysis:** Read and understand requirements documents, architecture documents, and related existing code
2. **Derive:** Identify files, methods, data flows, and risk factors to be modified or added
3. **Review:** Cross-check derived results against requirements and existing code for omissions, errors, or inconsistencies
4. **Repeat:** Repeat steps 1–3 until there is nothing more to add

### Core Principle: Cross-Validation

- Write the development plan **independently at least twice** and compare results
- Each analysis is performed independently without referencing previous results
- Analyze differences to produce the final integrated document

### Deliverables

- Stored in the `02.Analysys/` folder
- 1st development plan (md)
- 2nd development plan — for cross-validation (md)
- Final integrated development plan (md) — difference comparison table + consolidated enhancements

### Actual Example (StoredCopyJob)

| File                                       | Description                                           |
| ------------------------------------------ | ----------------------------------------------------- |
| `StoredCopyJob_DevelopmentPlan_01.md`    | 1st analysis: existing code state, items to implement, flow diagrams |
| `StoredCopyJob_DevelopmentPlan_02.md`    | 2nd analysis: independent cross-validation, architecture inconsistencies found |
| `StoredCopyJob_DevelopmentPlan_Final.md` | Integration: Plan1 vs Plan2 comparison of 11 items, final decisions |

### Items to Include in the Development Plan

1. **Current State Analysis** — Existing code behavior flow, already implemented infrastructure
2. **TODO/Skeleton Analysis** — List of unimplemented items (location, current state, required work)
3. **NEW File List** — Files to be newly created and their roles
4. **UPDATE File List** — Existing files to be modified and change descriptions
5. **Data Type Mapping** — External API ↔ Internal API type conversion table
6. **Development Phase Definition** — Scope of work per phase
7. **Risk Factors** — Identified risks and mitigation strategies
8. **Library Pre-check Checklist** — External dependency verification items

### Example AI Request

```
Analyze the documents in 01.Reference/ and related source code, and create a development plan.

Rules:
1. Analyze requirements and related code, repeating the analysis/derive/review loop until there is nothing more to add
2. Perform independent cross-validation analysis without referencing previously written plans
3. Precisely analyze TODO/skeleton items in existing code to identify all unimplemented items without omission

Save documents in the {feature_name}/02.Analysys/ folder.
```

---

## Phase 3: Task

### Purpose

Divide work into units that AI can execute, and create task request documents.

### Core Principle: Session Splitting

- **1 session = 1–10 file modifications + build verification** level of granularity
- Each session request must be written so it can be **independently understood and executed**
- Prerequisites, reference documents, completion criteria, and build verification commands must be specified

### Deliverables

- Stored in the `03.Task/` folder
- Per-session task request documents (md) — Session1, Session2, ...

### Actual Example (StoredCopyJob) — Split into 7 sessions

| Session   | File                                 | Scope                                   | Files Modified      |
| --------- | ------------------------------------ | --------------------------------------- | ------------------- |
| Session 1 | `Session1_DeviceService_Create.md` | Device Service interface + Create implementation | 5                   |
| Session 2 | `Session2_Enumerate_Release.md`    | Enumerate + Release implementation      | 3                   |
| Session 3 | `Session3_Delete.md`               | Delete implementation + full integration build | 2                   |
| Session 4 | `Session4_UnitTests.md`            | Unit tests (coverage 80%+)              | 5 new + 1 modified  |
| Session 5 | `Session5_AndroidTest.md`          | Android instrumented tests              | 3 new               |
| Session 6 | `Session6_ApiTest.md`              | API level tests                         | 1 new               |
| Session 7 | `Session7_FixSonarQubeFindings.md` | SonarQube static analysis issue fixes   | 4 modified          |

### Task Request Document Structure (Template)

```markdown
# Session N: {Task Title}

> **{Epic Name} Development — Session N/M**
> **Phase:** {Applicable Phase}
> **Reference Documents:** `{Final Development Plan}.md` Section X, Y
> **Prerequisites:** Sessions 1 through (N-1) completed

---

## 1. Session Goal
{Summary of what will be implemented in this session}

**Completion Criteria:**
- {Specific completion condition 1}
- {Specific completion condition 2}
- `./gradlew :{module}:assembleDebug` build succeeds

---

## 2. Work Order and Detailed Instructions

### Step 1: {Filename} — {Change Description}
**File:** `{file path}`
**Type:** NEW / UPDATE

{Specific code change instructions — method signatures, logic flow, existing patterns to reference}

> ⚠️ **Note:** {Items requiring verification}

### Step 2: ...

---

## 3. Build Verification
{List of build commands}
```

### Example AI Request

```
Create AI task request documents split by session based on the final development plan.

Rules:
1. Split into manageable work units per session (1–3 file modifications + build verification)
2. Write each request so AI can independently understand and execute it
3. Specify prerequisites, reference documents, completion criteria, and build commands
4. Place production code first, test code later

Save documents in the {feature_name}/03.Task/ folder.
```

---

## Phase 4: Implementation

### Purpose

Implement code according to the task request documents and verify builds.

### Core Principle: Build-Fix Loop

1. Complete code modifications
2. Execute APK build
3. Check for build errors → fix → rebuild
4. **Repeat until build succeeds**
5. Report successful build result to the user

### Build Variant Handling

- When changing interfaces, **modify all implementations together** (Standard, Sim, Clients)
- Verify builds for `release`, `debugForSim`, and `releaseForSim` in addition to `debug`

### Example AI Request

```
Read the task request document 03.Task/Session1_xxx.md and implement the code.

Rules:
1. Implement in the order of Steps in the request document
2. After code modifications, always verify the build (if errors occur, fix and rebuild repeatedly)
3. Report the final successful build result
4. Document the work in an md file (path: {feature_name}/04.Result/)
```

---

## Phase 5: Testing

### Purpose

Write tests for production code. Target 80% or higher code line coverage.

### Test Types

| Type         | Framework         | Location                               | Run Command                                                    |
| ------------ | ----------------- | -------------------------------------- | -------------------------------------------------------------- |
| Unit Test    | JUnit 4 + Mockito | `tests/services/src/test/`           | `./gradlew :Test-WorkpathServices:testDebugUnitTest`         |
| Android Test | AndroidJUnit4     | `tests/services/src/androidTest/`    | `./gradlew :Test-WorkpathServices:connectedDebugAndroidTest` |
| API Test     | AndroidJUnit4     | `tests/api/apiTest/src/androidTest/` | `./gradlew :Test-WorkpathAPIs:connectedDebugAndroidTest`     |

### Example AI Request

```
Read the task request document 03.Task/Session4_UnitTests.md and write unit tests.

Rules:
1. Reference existing test patterns to maintain consistency
2. Target 80% or higher code line coverage
3. Use Given-When-Then naming pattern
4. Report results after confirming all tests pass
```

---

## Phase 6: Validation

### Purpose

Analyze the process of implemented code, perform actual device testing, and resolve discovered issues.

### 6.1 Process Validation (Cross-Validation)

- **Independently analyze the complete process of implemented code 2–3 times**
- Compare each analysis result to identify issues
- Integrate into the final validation document

### 6.2 Device Log-Based Validation

- For issues that cannot be confirmed through code analysis alone, **collect actual device logs** for verification
- Reflect validation results in the document (✅ Closed / ❌ Fix Required)

### 6.3 Issue Response

- **Error log** attachment (.txt)
- **Root cause** analysis (code flow tracing)
- **Fix details** documented (before/after)
- **Verification results** (build success, tests passed, device log confirmation)

### Deliverables

- `05.Validation/` — Process analysis documents, device logs
- `06.Issues/` — Per-issue analysis documents + error logs

### Actual Example (StoredCopyJob)

**Validation Documents:**

| File                            | Description                                 |
| ------------------------------- | ------------------------------------------- |
| `Process_Validation_1.md`     | 1st process analysis                        |
| `Process_Validation_2.md`     | 2nd process analysis (cross-validation)     |
| `Process_Validation_3.md`     | 3rd process analysis (different AI model)   |
| `Process_Validation_final.md` | Final integration (comparison table + 5 issues identified) |
| `Issue_1_validation_log.txt`  | Issue #1 verified via device log → UUID match confirmed |

**Issue Documents:** 10 issues total × (analysis.md + log.txt)

### Example AI Request

```
Analyze the Store/Enumerate/Release process of the implemented code.

Rules:
1. Trace the code flow in detail from start to finish
2. Perform independently from previous analyses (cross-validation)
3. Classify discovered issues by severity
4. Save results in {feature_name}/05.Validation/

---

This log file is a device log for verifying Issue #{N}.
Please verify {description of verification target}.
Update the results in Process_Validation_final.md.
```

---

## Phase 7: Report

### Purpose

Summarize overall development progress and results.

### Deliverables

- Stored in the `04.Result/` folder
- Development report (md) — Progress table, modified file list, build/test results

### Contents

1. **Overall Progress** — By work story basis, by development phase basis
2. **Modified File List** — NEW/UPDATE classification, change description per file
3. **Test Results** — Number of unit tests passed, number of Android tests passed
4. **Build Results** — Final build success confirmation
5. **Outstanding Issues** — Summary of remaining issues

---

## Folder Structure Template

When developing a new feature, manage documents with the following structure:

```
Libs/{module}/doc/{feature_name}/
├── 01.Reference/          # Requirements, architecture, sequence diagrams
│   ├── {feature_name}_Overview.md
│   ├── {feature_name}_Investigation.md
│   └── *.puml
├── 02.Analysys/           # Development plans (cross-validated)
│   ├── {feature_name}_DevelopmentPlan_01.md
│   ├── {feature_name}_DevelopmentPlan_02.md
│   └── {feature_name}_DevelopmentPlan_Final.md
├── 03.Task/               # AI task request documents (per session)
│   ├── Session1_xxx.md
│   ├── Session2_xxx.md
│   └── ...
├── 04.Result/             # Development reports, process summaries
│   └── {feature_name}_DevelopmentReport.md
├── 05.Validation/         # Process validation (cross-validated)
│   ├── Process_Validation_1.md
│   ├── Process_Validation_final.md
│   └── *.txt (device logs)
└── 06.Issues/             # Issue response
    ├── 01.{issue_name}.md
    ├── 01-1.{log}.txt
    └── ...
```

---

## Key Summary: What to Provide to AI When Starting New Feature Development

1. **This document** (AI_Development_Process_Guide.md) — Process understanding
2. **Requirements document** — What needs to be built
3. **.github/copilot-instructions.md** — Project base rules (auto-applied)
4. **Related existing code paths** — Starting point for AI analysis

Then AI can proceed systematically from Phase 1 in order to carry out development.
