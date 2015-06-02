@echo off
call gradlew clean
call gradlew aR
xcopy /Y lib\build\outputs\aar\*.aar release\*.*