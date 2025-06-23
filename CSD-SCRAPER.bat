

@echo off
cd /d C:\CSD-SCRAPER
chcp 65001 > nul

:: Create log folder if it doesn't exist
if not exist log mkdir log

:: Get current date-time in format YYYY-MM-DD_HH-MM-SS
for /f "tokens=2 delims==." %%I in ('wmic OS Get localdatetime /value') do set datetime=%%I
set datetime=%datetime:~0,4%-%datetime:~4,2%-%datetime:~6,2%_%datetime:~8,2%-%datetime:~10,2%-%datetime:~12,2%

:: Define log file name
set logfile=log\csd_auto_%datetime%.log

echo Running CSD-SCRAPER .......... >> %logfile% 2>&1
echo %cd%\%~nx0 >> %logfile% 2>&1
"C:\Program Files\Java\jdk-13.0.2\bin\java.exe" -Dfile.encoding=UTF-8 -cp "CSD-SCRAPER.jar;jar_lib/*" csd_newscorner2_auto.main >> %logfile% 2>&1

echo Script execution completed. Logs saved to %logfile%
::exit
::pause
