@ECHO OFF
@REM Minimal Gradle Wrapper script for Windows
SETLOCAL

SET DIRNAME=%~dp0
IF "%DIRNAME%"=="" SET DIRNAME=.
SET APP_HOME=%DIRNAME%

SET WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
SET WRAPPER_MAIN=org.gradle.wrapper.GradleWrapperMain

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO ERROR: Could not find Gradle wrapper JAR: "%WRAPPER_JAR%"
  EXIT /B 1
)

IF DEFINED JAVA_HOME (
  SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
) ELSE (
  SET JAVA_EXE=java.exe
)

"%JAVA_EXE%" -version >NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
  ECHO ERROR: Java is not available. Check JAVA_HOME or PATH.
  EXIT /B 1
)

"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" %WRAPPER_MAIN% %*
SET EXIT_CODE=%ERRORLEVEL%

ENDLOCAL & EXIT /B %EXIT_CODE%
