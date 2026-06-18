# Workpath API Test List

## Overview
This document contains the complete list of Workpath API methods that need to be tested.

## API Methods

| API Package Name | API Class Name | API Method Name |
|-----------------|----------------|-----------------|
| com.hp.workpath.api | Workpath | getInstance |
| com.hp.workpath.api | Workpath | getVersionCode |
| com.hp.workpath.api | Workpath | getVersionName |
| com.hp.workpath.api | Workpath | initialize |
| com.hp.workpath.api.access | AccessService | getCurrentPrincipal |
| com.hp.workpath.api.access | AccessService | getDeviceAccessToken |
| com.hp.workpath.api.access | AccessService | initiateSignIn |
| com.hp.workpath.api.access | AccessService | isSupported |
| com.hp.workpath.api.access | AccessService | signIn |
| com.hp.workpath.api.access | AccessService | signOut |
| com.hp.workpath.api.access | AbstractAuthenticationService | onSignIn |
| com.hp.workpath.api.access | AbstractAuthenticationService | onSignOut |
| com.hp.workpath.api.access | AbstractAuthenticationService | onPrePrompt |
| com.hp.workpath.api.accessory.hid | AccessoryService | close |
| com.hp.workpath.api.accessory.hid | AccessoryService | enumerateAccessories |
| com.hp.workpath.api.accessory.hid | AccessoryService | getInfo |
| com.hp.workpath.api.accessory.hid | AccessoryService | getOwnedAccessories |
| com.hp.workpath.api.accessory.hid | AccessoryService | getSharedAccessories |
| com.hp.workpath.api.accessory.hid | AccessoryService | isReady |
| com.hp.workpath.api.accessory.hid | AccessoryService | isSupported |
| com.hp.workpath.api.accessory.hid | AccessoryService | open |
| com.hp.workpath.api.accessory.hid | AccessoryService | readReport |
| com.hp.workpath.api.accessory.hid | AccessoryService | releaseSharedAccessory |
| com.hp.workpath.api.accessory.hid | AccessoryService | resendOwnedAccessoryContext |
| com.hp.workpath.api.accessory.hid | AccessoryService | reserveSharedAccessory |
| com.hp.workpath.api.accessory.hid | AccessoryService | startReading |
| com.hp.workpath.api.accessory.hid | AccessoryService | stopReading |
| com.hp.workpath.api.accessory.hid | AccessoryService | writeReport |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryObserver | onContextChange |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryObserver | onReceive |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryObserver | register |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryObserver | unregister |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryStartObserver | onReady |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryStartObserver | register |
| com.hp.workpath.api.accessory.hid | AbstractAccessoryStartObserver | unregister |
| com.hp.workpath.api.attestation | AttestationService | getAppToken |
| com.hp.workpath.api.attestation | AttestationService | isSupported |
| com.hp.workpath.api.config | ConfigService | getDefaultConfig |
| com.hp.workpath.api.config | ConfigService | isSupported |
| com.hp.workpath.api.config | ConfigService | setDefaultConfig |
| com.hp.workpath.api.config | AbstractConfigChangeObserver | onChange |
| com.hp.workpath.api.config | AbstractConfigChangeObserver | register |
| com.hp.workpath.api.config | AbstractConfigChangeObserver | unregister |
| com.hp.workpath.api.copier | CopierService | deleteStoredJob |
| com.hp.workpath.api.copier | CopierService | enumerateStoredJob |
| com.hp.workpath.api.copier | CopierService | getCapabilities |
| com.hp.workpath.api.copier | CopierService | getDefaults |
| com.hp.workpath.api.copier | CopierService | isSupported |
| com.hp.workpath.api.copier | CopierService | releaseStoredJob |
| com.hp.workpath.api.copier | CopierService | submit |
| com.hp.workpath.api.device | DeviceService | getString |
| com.hp.workpath.api.device | DeviceService | getStringArray |
| com.hp.workpath.api.device | DeviceService | isSupported |
| com.hp.workpath.api.device.events | DeviceEventsService | getDeviceEvents |
| com.hp.workpath.api.device.events | DeviceEventsService | isSupported |
| com.hp.workpath.api.device.events | AbstractDeviceEventsChangeObserver | onChange |
| com.hp.workpath.api.device.events | AbstractDeviceEventsChangeObserver | register |
| com.hp.workpath.api.device.events | AbstractDeviceEventsChangeObserver | unregister |
| com.hp.workpath.api.device.settings | DeviceSettingsService | disableExternalPrinting |
| com.hp.workpath.api.device.settings | DeviceSettingsService | enableExternalPrinting |
| com.hp.workpath.api.device.settings | DeviceSettingsService | isSupported |
| com.hp.workpath.api.deviceusage | DeviceUsageService | getDeviceUsageInfo |
| com.hp.workpath.api.deviceusage | DeviceUsageService | isSupported |
| com.hp.workpath.api.helper.email | Email | getDefaults |
| com.hp.workpath.api.helper.email | Email | isSupported |
| com.hp.workpath.api.helper.email | Email | send |
| com.hp.workpath.api.job | JobService | cancelJob |
| com.hp.workpath.api.job | JobService | getJobInfo |
| com.hp.workpath.api.job | JobService | isSupported |
| com.hp.workpath.api.job | JobService | monitorJobInBackground |
| com.hp.workpath.api.job | JobService | monitorJobInForeground |
| com.hp.workpath.api.job | AbstractJobletObserver | onCancel |
| com.hp.workpath.api.job | AbstractJobletObserver | onComplete |
| com.hp.workpath.api.job | AbstractJobletObserver | onFail |
| com.hp.workpath.api.job | AbstractJobletObserver | onProgress |
| com.hp.workpath.api.job | AbstractJobletObserver | register |
| com.hp.workpath.api.job | AbstractJobletObserver | unregister |
| com.hp.workpath.api.launcher | LauncherService | isSupported |
| com.hp.workpath.api.launcher | LauncherService | launch |
| com.hp.workpath.api.massstorage | MassStorageService | getStorageList |
| com.hp.workpath.api.massstorage | MassStorageService | isSupported |
| com.hp.workpath.api.massstorage | AbstractMassStorageChangeObserver | onChange |
| com.hp.workpath.api.massstorage | AbstractMassStorageChangeObserver | register |
| com.hp.workpath.api.massstorage | AbstractMassStorageChangeObserver | unregister |
| com.hp.workpath.api.printer | PrinterService | getCapabilities |
| com.hp.workpath.api.printer | PrinterService | getDefaults |
| com.hp.workpath.api.printer | PrinterService | isSupported |
| com.hp.workpath.api.printer | PrinterService | submit |
| com.hp.workpath.api.printer | PrinterStatus | getStatus |
| com.hp.workpath.api.printer | PrinterStatus | getTrays |
| com.hp.workpath.api.printer | PrinterStatus | isSupported |
| com.hp.workpath.api.scanner | ScannerService | getCapabilities |
| com.hp.workpath.api.scanner | ScannerService | getDefaults |
| com.hp.workpath.api.scanner | ScannerService | getFileOptionsCapabilities |
| com.hp.workpath.api.scanner | ScannerService | isSupported |
| com.hp.workpath.api.scanner | ScannerService | submit |
| com.hp.workpath.api.scanner | ScannerStatus | getStatus |
| com.hp.workpath.api.scanner | ScannerStatus | isSupported |
| com.hp.workpath.api.statistics | StatisticsService | commit |
| com.hp.workpath.api.statistics | StatisticsService | getAllJobsList |
| com.hp.workpath.api.statistics | StatisticsService | getJobInfo |
| com.hp.workpath.api.statistics | StatisticsService | getJobInfoByJobSequence |
| com.hp.workpath.api.statistics | StatisticsService | getLastCommittedJobSequence |
| com.hp.workpath.api.statistics | StatisticsService | getLastCompletedJobInfo |
| com.hp.workpath.api.statistics | StatisticsService | getLastCompletedJobSequence |
| com.hp.workpath.api.statistics | StatisticsService | getLastJobInfo |
| com.hp.workpath.api.statistics | StatisticsService | getLastJobSequence |
| com.hp.workpath.api.statistics | StatisticsService | getTotalCount |
| com.hp.workpath.api.statistics | StatisticsService | isSupported |
| com.hp.workpath.api.statistics | AbstractStatisticsNotificationObserver | onChange |
| com.hp.workpath.api.statistics | AbstractStatisticsNotificationObserver | onComplete |
| com.hp.workpath.api.statistics | AbstractStatisticsNotificationObserver | register |
| com.hp.workpath.api.statistics | AbstractStatisticsNotificationObserver | unregister |
| com.hp.workpath.api.supplies | SuppliesService | getSuppliesInfo |
| com.hp.workpath.api.supplies | SuppliesService | isSupported |
| com.hp.workpath.api.webservices | WebServices | isSupported |
| com.hp.workpath.api.webservices | WebServices | register |
| com.hp.workpath.api.webservices | AbstractWebServices.Callback | authenticated |
| com.hp.workpath.api.webservices | AbstractWebServices.Callback | delete |
| com.hp.workpath.api.webservices | AbstractWebServices.Callback | get |
| com.hp.workpath.api.webservices | AbstractWebServices.Callback | post |
| com.hp.workpath.api.webservices | AbstractWebServices.Callback | put |

## Summary

- **Total APIs**: 124 methods
- **Packages**: 15 unique packages
- **Classes**: 24 unique classes
