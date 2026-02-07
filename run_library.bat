@echo off
REM ===============================
REM Run Library Management System
REM ===============================

REM Set project root folder
SET PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

REM ===============================
REM 1. Start Backend Server
REM ===============================
echo Starting backend server (port 8080)...
start "Backend Server" cmd /k "mvn clean compile exec:java"

REM ===============================
REM 2. Wait a few seconds to let backend start
REM ===============================
timeout /t 5 /nobreak >nul

REM ===============================
REM 3. Open Frontend in Default Browser
REM ===============================
echo Opening frontend in default browser...
start "" "%PROJECT_DIR%\ui\html\index.html"

REM ===============================
REM Done
REM ===============================
echo.
echo ✅ Backend started
echo ✅ Frontend opened in browser
pause