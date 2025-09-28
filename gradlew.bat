@ECHO OFF
SETLOCAL

SET DIR=%~dp0
SET WRAPPER_JAR=%DIR%\gradle\wrapper\gradle-wrapper.jar

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Gradle wrapper JAR is missing. Please run "gradle wrapper" to regenerate.
  EXIT /B 1
)

IF NOT DEFINED JAVA_HOME (
  SET JAVA_EXE=java.exe
) ELSE (
  SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
)

"%JAVA_EXE%" -jar "%WRAPPER_JAR%" %*
