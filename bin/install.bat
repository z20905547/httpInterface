@echo off

cd %~dp0
cd ..

set base_path=%cd%

echo [INFO] install project.
call mvn clean source:jar install -Dmaven.test.skip=true

echo [INFO] reset database data
call mvn antrun:run

echo [INFO] create project archetype.
call mvn archetype:create-from-project

echo [INFO] install archetype to local m2 repository
cd %base_path%\target\generated-sources\archetype
call mvn clean install -Dmaven.test.skip=true

echo [INFO] delete project generated-sources target
cd %base_path%\
rd /S /Q target

echo [INFO] start base framework app
start "base framework" mvn clean jetty:run
echo [INFO] Please wait a moment. When you see "[INFO] Started Jetty Server", you can visit: http://localhost:8080/base-curd/ to view the demo

cd bin

pause