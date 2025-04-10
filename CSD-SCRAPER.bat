@echo off
cd /d C:\CSD-SCRAPER
chcp 65001 > nul

echo Running CSD-SCRAPER...
java -Dfile.encoding=UTF-8 -cp "CSD-SCRAPER.jar;jar_lib/*" csd_newscorner2_auto.main

pause
