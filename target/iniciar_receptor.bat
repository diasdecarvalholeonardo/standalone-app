@echo off
cd /d C:\SAAS\standalone-app\target
start cmd /k "java -cp classes com.standalone.screensender.receiver.ServidorReceptor"