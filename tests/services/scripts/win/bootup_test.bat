@echo off
rem Bootup test for workpath system/service apk (v0.1)

rem Check if device argument is provided
if "%1"=="" (
    echo Usage: %0 device_name #
    exit /b 1
)

if "%2"=="" (
    set restartCnt=1
) else (
    set restartCnt=%2
)
echo restartCnt=%restartCnt%
rem Set the path to adb command
set _ADB_PATH=adb.exe

rem Connect to the Android device
%_ADB_PATH% devices

rem Launch a specific application
echo [Setup][switch to root]
%_ADB_PATH% root
echo [Setup][uninstall com.hp.jetadvantage.link.system]
%_ADB_PATH% uninstall com.hp.jetadvantage.link.system

echo [Setup][uninstall com.hp.jetadvantage.link.services]
%_ADB_PATH% uninstall com.hp.jetadvantage.link.services

echo [Setup] clear log
%_ADB_PATH% logcat -c
echo [Setup][install SystemAppDebug.apk]
%_ADB_PATH% install -r -t SystemAppDebug.apk

echo [Setup][install WorkpathServices-Dune-debug.apk]
%_ADB_PATH% install -r -t WorkpathServices-Dune-debug.apk

echo ------------------- [TEST][TC1][START][start link.services] -------------------
echo [Test][Start SystemService]
%_ADB_PATH% shell am startservice -n com.hp.jetadvantage.link.system/.services.SystemService

echo [Wait] 10 seconds to wait init..
timeout /t 10 >nul
echo ------------------- [TEST][TC1][END] -------------------

echo ------------------- [TEST][TC2][START][restart link.services] -------------------
for /l %%i in (1,1,%restartCnt%) do (
    echo [Repeat] %%i 
	
	echo [Test][TC2][Stop link.services]
	%_ADB_PATH% shell am force-stop  com.hp.jetadvantage.link.services
	timeout /t 1 >nul
	
	echo [Test][TC2][restart link.services]
	%_ADB_PATH% shell am startservice -n com.hp.jetadvantage.link.services/com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService
	
	echo [Wait] 10 seconds to wait init..
	timeout /t 10 >nul

)
echo ------------------- [TEST][TC2][END] -------------------

echo ------------------- [TEST][TC3][START][Restarting link.system] -------------------
for /l %%i in (1,1,%restartCnt%) do (
    echo [Repeat] %%i 

	
	echo [Test][TC3][Stop link.services]
	%_ADB_PATH% shell am force-stop  com.hp.jetadvantage.link.system
	timeout /t 1 >nul
	
	echo [Test][TC3][restart SystemService]
	%_ADB_PATH% shell am startservice -n com.hp.jetadvantage.link.system/.services.SystemService

	echo [Wait] 10 seconds to wait init..
	timeout /t 10 >nul

)
echo ------------------- [TEST][TC3][END] -------------------


rem timeout /t 10
echo capture log
%_ADB_PATH% logcat -s [SDK][BOOT]DSS -d > bootlog.txt


