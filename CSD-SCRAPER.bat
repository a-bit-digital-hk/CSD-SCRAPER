@echo off
cd /d C:\CSD-SCRAPER
chcp 65001 > nul

if not exist log mkdir log

for /f "tokens=2 delims==." %%I in ('wmic OS Get localdatetime /value') do set datetime=%%I
set datetime=%datetime:~0,4%-%datetime:~4,2%-%datetime:~6,2%_%datetime:~8,2%-%datetime:~10,2%-%datetime:~12,2%

set logfile=log\csd_auto_%datetime%.log

echo Running CSD-SCRAPER .......... >> %logfile% 2>&1
echo %cd%\%~nx0 >> %logfile% 2>&1
"C:\Program Files\Java\jdk-13.0.2\bin\java.exe" -Dfile.encoding=UTF-8 -jar target\CSD-SCRAPER.jar >> %logfile% 2>&1

echo Script execution completed. Logs saved to %logfile%
