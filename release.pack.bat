@echo off
setlocal EnableExtensions

cd /d "%~dp0"
set "BASE_DIR=%~dp0"
set "TARGET_DIR=%BASE_DIR%target"
set "RELEASE_DIR=%BASE_DIR%release"
set "DEP_DIR=%TARGET_DIR%\dependency"
set "APP_JAR=%TARGET_DIR%\391.jar"
for /f %%I in ('powershell -NoProfile -Command "(Get-Date).ToString(\"yyyyMMdd-HHmmss\")"') do set "TS=%%I"
if not defined TS set "TS=%RANDOM%%RANDOM%"
set "ZIP_FILE=%BASE_DIR%kms391-server-release-%TS%.zip"

if not exist "%APP_JAR%" (
    echo [ERROR] Build output not found: %APP_JAR%
    echo [HINT] Compile first, for example: mvn clean package
    pause
    exit /b 1
)

echo [INFO] Packaging existing build artifacts only...
if exist "%RELEASE_DIR%" (
    echo [STEP] Cleaning existing release directory...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$p = '%RELEASE_DIR%';" ^
        "if (Test-Path -LiteralPath $p) {" ^
        "  attrib -r -s -h -l ""$p\*"" /s /d 2>$null;" ^
        "  Remove-Item -LiteralPath $p -Recurse -Force -ErrorAction Stop" ^
        "}" >nul 2>&1
    if exist "%RELEASE_DIR%" (
        rmdir /s /q "%RELEASE_DIR%" >nul 2>&1
    )
    if exist "%RELEASE_DIR%" (
        echo [WARN] Failed to clean old release directory: %RELEASE_DIR%
        echo [WARN] Will use a fallback staging directory for this run.
    )
)
if exist "%RELEASE_DIR%" (
    set "RELEASE_DIR=%BASE_DIR%release-%TS%"
)
if /i not "%RELEASE_DIR%"=="%BASE_DIR%release" (
    echo [WARN] Using fallback staging directory: %RELEASE_DIR%
)
mkdir "%RELEASE_DIR%"
mkdir "%RELEASE_DIR%\lib"

echo [STEP] Copying application jar...
copy /y "%APP_JAR%" "%RELEASE_DIR%\" >nul

if exist "%DEP_DIR%\*.jar" (
    echo [STEP] Copying runtime dependency jars...
    copy /y "%DEP_DIR%\*.jar" "%RELEASE_DIR%\lib\" >nul
) else (
    echo [ERROR] No runtime dependency jars found in: %DEP_DIR%
    echo [HINT] Build first so dependencies are copied to target\dependency:
    echo [HINT]   mvn clean package
    pause
    exit /b 1
)

if exist "%BASE_DIR%scripts" (
    echo [STEP] Copying scripts ...
    xcopy "%BASE_DIR%scripts" "%RELEASE_DIR%\scripts\" /e /i /y >nul
)
if exist "%BASE_DIR%wz" (
    echo [STEP] Copying wz ... ^(this can take a long time^)
    xcopy "%BASE_DIR%wz" "%RELEASE_DIR%\wz\" /e /i /y >nul
)
if exist "%BASE_DIR%sql" (
    echo [STEP] Copying sql ...
    xcopy "%BASE_DIR%sql" "%RELEASE_DIR%\sql\" /e /i /y >nul
)
set "DB_SQL=%BASE_DIR%sql\kms391.sql"
if exist "%DB_SQL%" (
    if not exist "%RELEASE_DIR%\sql" mkdir "%RELEASE_DIR%\sql"
    echo [STEP] Copying sql\kms391.sql ...
    copy /y "%DB_SQL%" "%RELEASE_DIR%\sql\" >nul
) else (
    echo [WARN] Missing expected database script: %DB_SQL%
)
if exist "%BASE_DIR%Properties" (
    echo [STEP] Copying Properties ...
    xcopy "%BASE_DIR%Properties" "%RELEASE_DIR%\Properties\" /e /i /y >nul
)

if exist "%BASE_DIR%recvops.properties" copy /y "%BASE_DIR%recvops.properties" "%RELEASE_DIR%\" >nul
if exist "%BASE_DIR%sendops.properties" copy /y "%BASE_DIR%sendops.properties" "%RELEASE_DIR%\" >nul

if exist "%BASE_DIR%start.bat" (
    echo [STEP] Copying start.bat ...
    copy /y "%BASE_DIR%start.bat" "%RELEASE_DIR%\" >nul
) else (
    echo [WARN] Missing start.bat in project root.
)

if exist "%BASE_DIR%stop.bat" (
    echo [STEP] Copying stop.bat ...
    copy /y "%BASE_DIR%stop.bat" "%RELEASE_DIR%\" >nul
) else (
    echo [WARN] Missing stop.bat in project root.
)

echo [STEP] Creating run.bat ...
(
echo @echo off
echo setlocal
echo call "%%~dp0start.bat"
) > "%RELEASE_DIR%\run.bat"

echo [STEP] Creating zip package...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ErrorActionPreference='Stop';" ^
    "$src = Get-ChildItem -LiteralPath '%RELEASE_DIR%' -Force;" ^
    "if ($null -eq $src -or $src.Count -eq 0) { throw 'Release staging directory is empty.' }" ^
    "Compress-Archive -Path $src.FullName -DestinationPath '%ZIP_FILE%' -Force"
if %ERRORLEVEL% NEQ 0 (
    echo [WARN] Compress-Archive failed, fallback to tar...
    where tar >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to create zip package. Neither Compress-Archive nor tar succeeded.
        pause
        exit /b 1
    )
    tar -a -c -f "%ZIP_FILE%" -C "%RELEASE_DIR%" .
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to create zip package.
        pause
        exit /b %ERRORLEVEL%
    )
)

echo [INFO] Done.
echo [INFO] Output zip: %ZIP_FILE%
pause
exit /b 0
