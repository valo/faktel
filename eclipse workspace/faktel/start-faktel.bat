@echo off
set LIB_DIR=lib
set CLASSPATH=%LIB_DIR%/poi-3.1-FINAL.jar;%LIB_DIR%/opencsv-1.8.jar;%LIB_DIR%/faktel.jar;

start "FakTel Demo" "C:\Program Files\Java\jre6\bin\javaw" com.faktel.gui.FakEngineGUI config/settings.xml
REM "C:\Program Files\Java\jre6\bin\java" com.faktel.FakEngine config/settings.xml