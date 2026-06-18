# Workpath Platform Services Repository for the Dune Platform

## Description
This repository contains the source code for providing Workpath services. It has two main interfaces:

1. **Front-End: Serving Third-Party Applications**  
   Workpath services offer third-party Android applications a variety of MFP features. This enables these applications to request Workpath services using the Workpath API calls to create jobs on devices or obtain necessary device information.

2. **Back-End: Connecting with the Dune Platform**  
   Workpath services connect with the Dune platform by utilizing various APIs, including the E2 Public API, E2 Private Interop API, Public CDM API, and E2 Websocket for callback notifications. This integration provides the necessary device's functionalities to the applications, ensuring that Workpath services are robust, scalable, and seamlessly integrated with the Dune platform's infrastructure.

## Installation

1. **For Production Release**
   - Build the project on the Jenkins server.
   - Deploy the built artifact (`target/WorkpathServices-dune.apk`) to the [workpath_packages_smartux repository](https://github-partner.azc.ext.hp.com/jedi/workpath_packages_smartux/tree/25d/green/apks_dune) to build the RoidInstaller package.

2. **For Simulator Release**
   - Build the project on the Jenkins server.
   - Deploy the built artifact (`simulator/WorkpathServices-dune.apk`) to the [AVD Docker repository](https://github.azc.ext.hp.com/workpathframework/server_script/tree/master/avd_on_docker/apks) to build the AVD Docker image.

3. **For Local Development and Testing**
   1. **Build Locally with a Build Variant:**
      - `debug`: Enable debug logs and disable ProGuard obfuscation for a target hardware device.
      - `release`: Disable debug logs and enable ProGuard obfuscation for a target hardware device.
      - `debugForSim`: Enable debug logs and disable ProGuard obfuscation for an AVD simulator.
      - `releaseForSim`: Disable debug logs and enable ProGuard obfuscation for an AVD simulator.

   2. **Install Locally Built APK to a Target Hardware Device or AVD Simulator:**
      - `adb install -r WorkpathServices-dune.apk`
      - `adb shell am broadcast -a com.hp.workpath.intent.action.CALL_DEVICE_READY -n com.hp.jetadvantage.link.system/.receivers.CDMCallReceiver`

## PI Testing
When you create a pull request, a Pre-Integration (PI) test for the PR will be triggered automatically by the GitHub Action. The test will run on a dedicated emulator device, and the test result will be displayed on the PR page.
The PI test will check the following:
- Building the PR branch with all of the build variants
- Unit testing
- Android instrumented testing
- API testing

## Merging Changes from the PR
Once the PR is approved and PI testing has passed, the PR can be merged into the `master` branch by applying the https://github.azc.ext.hp.com/workpath-dune/workpath-services-dune/labels/ready%20to%20merge label. 
The GitHub action for the label will update the version name and merge the PR.

## APK Version
1. Version file : https://github.azc.ext.hp.com/workpath-dune/workpath-services-dune/blob/master/version.properties
2. Versioning Rule
   - version = [Workpath API Version]-[s/r].[#]+[D/J][YYYYMMDD]
      - [Workpath API Version] : Workpath API Version, Currently(2024.12.12) it's 1.6.2.
      - [s/r].[#] : [s] – s branch (master), [r]- r branch, [#]- counter increasing by +1 with each PR
      - [D/J] : D for Dune
      - [YYYYMMDD] : KST Time
   - ex) `1.6.2-s.14+D20241212`
3. Github Action for automatic version update when merging PR : https://github.azc.ext.hp.com/workpath-dune/workpath-services-dune/actions/workflows/version.yml

## License
Information about the project's license.
