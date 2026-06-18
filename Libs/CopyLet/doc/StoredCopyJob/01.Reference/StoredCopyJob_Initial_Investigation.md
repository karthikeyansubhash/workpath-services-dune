# Profile

## Workpath API : CopierService

- CopyAttributesCaps getCapabilities(Context, Result)

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

Standard copy:  "Copy Options Profile" has a "base" profile for standard copy intent

| GET /ext/copy/v1/profile{ "base":{}, "storedCopy":{} } |
| ------------------------------------------------------ |

2. Stored copy: profile is separated into one for creating a stored job and another for releasing it.

   a) for creating a stored job: "jobStorage" scan profile

| GET /ext/scan/v1/profile{ "base":{}, "http":{}, jobStorage:{}} |
| -------------------------------------------------------------- |

    b) for releasing a stored job: "storedCopy" copy profile

| GET /ext/copy/v1/profile{ "base":{}, "storedCopy":{}} |
| ----------------------------------------------------- |

### Considerations

1. Workpath API: The existing Workpath API provides only a single common copy profile for both standard copy and stored copy.

   1. Existing API: The existing API will provide only a standard copy profile for Dune.
      -> Need to investigate more the profile difference between standard copy and stored copy
   2. New  API: define a new additional API to provide both copy profiles
      **List<****CopyAttributesCaps** **> **CopierService.**getAll****Capabilities** (Context, Result) or
      **CopyAttributesCaps** CopierService. **getCapabilities** (Context, JobExecutionMode, Result)
2. Stored Copy Profile: The E2 separated scan(jobStorage) and copy(storedCopy) profiles need to be combined into a single Workpath stored copy profile.

# Default Options

## Workpath API : CopierService

- CopyAttributes getDefaults(Context, Result)

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

1. Standard copy : GET /ext/copy/v1/defaultOptions
2. Stored copy: defaultOptions is separated into one for creating a stored job and another for releasing it.
   1. for creating a stored job: scan defaultOptions for "jobStorage"
      GET /ext/scan/v1/defaultOptions
      {"http":{},"jobStorage":{}}
   2. for releasing a stored job: copy defaultOptions
      GET /ext/copy/v1/defaultOptions

### Considerations

1. Workpath API: The existing Workpath API provides only a single common defaultOptions for both standard copy and stored copy.
   1. Existing API: The existing API will provide only standard copy default options for Dune.
      - Need to investigate more the default option difference between the standard copy and the stored copy
   2. New  API: define a new additional API to provide copy default options for both standard copy and stored copy
      - List `<CopyAttributes>` CopierService.getAllDefaults(Context, Result) or
      - CopyAttributes CopierService.getDefaults(Context, JobExecutionMode, Result)
      - Stored Copy Default Options: The separated E2 scan(jobStorage) and copy default options need to be combined into a single Workpath stored copy default options.

# Create StoredJob

## Workpath API : CopierService

1. String submit(Context, CopyAttributes, CopyletAttributes)

- JobExecutionMode.NORMAL
- JobExecutionMode.STORE

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

1. Standard copy: create a copy job with copy options
    - POST /ext/copy/v1/copyAgents/{agentId}/copyJobs
    {"ticket":{" ":{...}}}
2. Stored copy: Create a scan job with scan options for the  "jobStorage" destination.
    - POST /ext/scanJob/v1/scanJobAgents/{agentId}/scanJobs
    {"scanTicket":{
     "destinationOptions":{"jobStorage":{}},
     "scanOptions":{..}}

### Considerations

1. Standard Copy: No change.
2. Stored Copy: Workpath app submits full copy options to create a stored copy job.
    1. Workpath copy service needs to extract scan options from the submitted copy options and create a scan job for the "jobStorage" destination.
    2. Workpath copy service needs to store the original copy options with the created stored job Id  to use when releasing the stored job.
    3. Workpath scan service needs to handle the scan job as a Workpath copy job (JobType.COPY, CopyJobState, CopyJobData) to abstract the internal process and provide backward compatibility to App.


# Enumerate StoredJobs

## Workpath API : CopierService

- List `<StoredJobInfo>` enumerateStoredJob(Context, Result)

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

- GET /ext/copy/v1/copyAgents/{agentId}/storedJobs?includeMembers

### Considerations

# Release StoredJob

## Workpath API : CopierService

- String releaseStoredJob(Context, StoredJobAttributes)

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

- "Release Stored Copy Job Request" modified to take a full "copyOptions" intent instead of simply the number of copies
- POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{jobId}/release
{"copyOptions":{...}}

### Considerations

# Delete StoredJob

## Workpath API : CopierService

- void deleteStoredJob(Context , jobId, JobCredentialsAttributes ,Result)

### E2 (re-design: Separate "scan" vs "copy" settings for stored copy jobs)

### Considerations
