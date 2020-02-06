cd docflow
call mvn clean install -DskipTests
echo on
cd ..\webapp
call mvn clean install -DskipTests
pause