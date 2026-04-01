@echo off
setlocal EnableExtensions

cd /d "%~dp0"
set "BASE_DIR=%~dp0"
set "PID_FILE=%BASE_DIR%server.pid"
set "LOG_DIR=%BASE_DIR%logs"
set "OUT_LOG=%LOG_DIR%\server.out.log"
set "ERR_LOG=%LOG_DIR%\server.err.log"
set "LAUNCH_ERR_LOG=%LOG_DIR%\start-launch.err.log"
set "PID_TMP=%TEMP%\kms391_start_pid_%RANDOM%.tmp"
set "EXIST_PID="
set "SERVER_PID="

if exist "%PID_FILE%" (
    set /p "EXIST_PID="<"%PID_FILE%"
    echo(%EXIST_PID%| findstr /r "^[0-9][0-9]*$" >nul
    if not errorlevel 1 (
        powershell -NoProfile -ExecutionPolicy Bypass -Command "if (Get-Process -Id %EXIST_PID% -ErrorAction SilentlyContinue) { exit 0 } else { exit 1 }"
        if not errorlevel 1 (
            echo [WARN] Server already running. PID=%EXIST_PID%
            echo [HINT] Use stop.bat first if you want to restart.
            pause
            exit /b 0
        )
    ) else (
        echo [WARN] Invalid PID content in server.pid, ignoring stale file.
    )
    del /f /q "%PID_FILE%" >nul 2>&1
)

set "JAVA_EXE="
if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Java\jdk-21"
if exist "%JAVA_HOME%\bin\java.exe" (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)
if not defined JAVA_EXE (
    if exist "C:\Program Files\Java\latest\bin\java.exe" (
        set "JAVA_EXE=C:\Program Files\Java\latest\bin\java.exe"
    )
)
if not defined JAVA_EXE (
    for /f "delims=" %%J in ('where java.exe 2^>nul') do (
        if not defined JAVA_EXE set "JAVA_EXE=%%J"
    )
)
if not defined JAVA_EXE (
    echo [ERROR] java.exe not found. Please install JDK/JRE or set JAVA_HOME.
    pause
    exit /b 1
)

if not defined KMS_XMS set "KMS_XMS=512M"
if not defined KMS_XMX set "KMS_XMX=4096M"

if not exist "%BASE_DIR%391.jar" (
    echo [ERROR] Missing 391.jar in current directory.
    echo [HINT] Please make sure the release package is complete.
    pause
    exit /b 1
)

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

if not exist "%BASE_DIR%lib" (
    echo [ERROR] Missing lib directory.
    echo [HINT] Runtime dependencies are required ^(HikariCP, Netty, MariaDB driver, etc.^).
    echo [HINT] Rebuild package with: mvn clean package dependency:copy-dependencies -DincludeScope=runtime
    pause
    exit /b 1
)

if not exist "%BASE_DIR%lib\HikariCP*.jar" (
    echo [ERROR] Missing HikariCP runtime jar under lib.
    echo [HINT] Current error usually is: NoClassDefFoundError: com/zaxxer/hikari/HikariConfig
    echo [HINT] Rebuild package with: mvn clean package dependency:copy-dependencies -DincludeScope=runtime
    pause
    exit /b 1
)

if exist "%LAUNCH_ERR_LOG%" del /f /q "%LAUNCH_ERR_LOG%" >nul 2>&1
if exist "%PID_TMP%" del /f /q "%PID_TMP%" >nul 2>&1

powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; $p = Start-Process -FilePath $env:JAVA_EXE -ArgumentList @('-Xms' + $env:KMS_XMS,'-Xmx' + $env:KMS_XMX,'-Dfile.encoding=UTF-8','-Dnet.sf.odinms.wzpath=wz','-cp','391.jar;lib/*','server.Start') -WorkingDirectory $env:BASE_DIR -RedirectStandardOutput $env:OUT_LOG -RedirectStandardError $env:ERR_LOG -PassThru; [Console]::Out.WriteLine($p.Id)" > "%PID_TMP%" 2> "%LAUNCH_ERR_LOG%"

if exist "%PID_TMP%" (
    set /p "SERVER_PID="<"%PID_TMP%"
    del /f /q "%PID_TMP%" >nul 2>&1
)

if not defined SERVER_PID (
    echo [ERROR] Failed to start server process.
    if exist "%LAUNCH_ERR_LOG%" (
        echo [HINT] Launcher error: %LAUNCH_ERR_LOG%
    )
    echo [HINT] Check logs:
    echo [HINT]   %OUT_LOG%
    echo [HINT]   %ERR_LOG%
    pause
    exit /b 1
)

echo(%SERVER_PID%| findstr /r "^[0-9][0-9]*$" >nul
if errorlevel 1 (
    echo [ERROR] Failed to capture valid PID. Got: %SERVER_PID%
    if exist "%LAUNCH_ERR_LOG%" (
        echo [HINT] Launcher error: %LAUNCH_ERR_LOG%
    )
    echo [HINT] Check logs:
    echo [HINT]   %OUT_LOG%
    echo [HINT]   %ERR_LOG%
    pause
    exit /b 1
)

> "%PID_FILE%" echo %SERVER_PID%

echo [INFO] Server started.
echo [INFO] PID: %SERVER_PID%
echo [INFO] JVM Heap: -Xms%KMS_XMS% -Xmx%KMS_XMX%
echo [INFO] STDOUT: %OUT_LOG%
echo [INFO] STDERR: %ERR_LOG%
exit /b 0
