@echo off
setlocal EnableExtensions
@title Dump Quests

cd /d "%~dp0"

if not exist "target\classes\tools\wztosql\DumpQuests.class" (
    echo [ERROR] Missing compiled class: target\classes\tools\wztosql\DumpQuests.class
    echo [HINT] Build project first in IDEA: Build -^> Build Project, or use Maven.
    pause
    exit /b 1
)

dir /b "target\dependency\HikariCP*.jar" >nul 2>&1
if errorlevel 1 (
    dir /b "lib\HikariCP*.jar" >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Missing runtime dependency HikariCP.
        echo [HINT] Provide runtime jars under target\dependency ^| lib.
        echo [HINT] If you use Maven: mvn clean package dependency:copy-dependencies -DincludeScope=runtime
        pause
        exit /b 1
    )
)

set "CP=target\classes;target\dependency\*;lib\*;391.jar"
java -server -cp "%CP%" -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpQuests
pause
