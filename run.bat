@echo off
setlocal
echo ============================================================
echo Starting Online Examination System in Terminal...
echo ============================================================

set MVN_CMD="C:\Program Files\NetBeans-25\netbeans\java\maven\bin\mvn.cmd"

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set MVN_CMD=mvn
)

call %MVN_CMD% compile exec:java
pause
