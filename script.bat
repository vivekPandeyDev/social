@echo off
setlocal


:: Start Keycloak in a new Command Prompt window
start cmd /c "cd /d C:\keycloak-24.0.5 && .\bin\kc.bat start-dev --http-port=9090 --debug 5005"

:: Wait for 10 seconds
timeout /t 10 /nobreak


:: Start Zipkin in a new Command Prompt window
start cmd /k "cd /d C: && java -jar zipkin-server-3.4.0-exec.jar"

:: Wait for 10 seconds
timeout /t 5 /nobreak

:: Start Zookeeper in a new Command Prompt window
start cmd /c "cd /d C:\kafka && .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties"

:: Wait for 10 seconds
timeout /t 10 /nobreak

:: Start Kafka in a new Command Prompt window
start cmd /c "cd /d C:\kafka && .\bin\windows\kafka-server-start.bat .\config\server.properties"




endlocal