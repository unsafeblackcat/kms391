@echo off
setlocal EnableExtensions

cd /d "%~dp0"
set "BASE_DIR=%~dp0"
set "PID_FILE=%BASE_DIR%server.pid"
set "SERVER_PID="

if exist "%PID_FILE%" (
    set /p SERVER_PID=<"%PID_FILE%"
)

if not defined SERVER_PID (
    echo [WARN] server.pid not found, trying to detect process by command line...
    for /f %%P in ('powershell -NoProfile -ExecutionPolicy Bypass -Command "$p = Get-CimInstance Win32_Process | Where-Object { $_.Name -match ''java'' -and $_.CommandLine -like ''*server.Start*'' } | Select-Object -First 1 -ExpandProperty ProcessId; if($p){$p}"') do set "SERVER_PID=%%P"
)

if not defined SERVER_PID (
    echo [WARN] Server process not found.
    exit /b 0
)

echo(%SERVER_PID%| findstr /r "^[0-9][0-9]*$" >nul
if errorlevel 1 (
    echo [WARN] Invalid PID value detected: %SERVER_PID%
    if exist "%PID_FILE%" del /f /q "%PID_FILE%" >nul 2>&1
    exit /b 0
)

powershell -NoProfile -ExecutionPolicy Bypass -Command "if (Get-Process -Id %SERVER_PID% -ErrorAction SilentlyContinue) { exit 0 } else { exit 1 }"
if errorlevel 1 (
    echo [WARN] PID %SERVER_PID% is not running.
    if exist "%PID_FILE%" del /f /q "%PID_FILE%" >nul 2>&1
    exit /b 0
)

if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "JCMD_EXE="
if exist "%JAVA_HOME%\bin\jcmd.exe" (
    set "JCMD_EXE=%JAVA_HOME%\bin\jcmd.exe"
) else (
    where jcmd >nul 2>&1
    if %ERRORLEVEL% EQU 0 set "JCMD_EXE=jcmd"
)

if defined JCMD_EXE (
    echo [STEP] Requesting graceful JVM shutdown via jcmd...
    "%JCMD_EXE%" %SERVER_PID% VM.exit >nul 2>&1
) else (
    echo [WARN] jcmd not found, fallback to taskkill (non-force)...
    taskkill /PID %SERVER_PID% >nul 2>&1
)

timeout /t 8 /nobreak >nul
powershell -NoProfile -ExecutionPolicy Bypass -Command "if (Get-Process -Id %SERVER_PID% -ErrorAction SilentlyContinue) { exit 1 } else { exit 0 }"
if errorlevel 1 (
    echo [WARN] Server is still running. Please run this again or stop manually.
    exit /b 1
)

if exist "%PID_FILE%" del /f /q "%PID_FILE%" >nul 2>&1
echo [INFO] Server stopped safely.
exit /b 0
