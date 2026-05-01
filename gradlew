#!/usr/bin/env sh

APP_HOME=$(cd "${0%/*}" 2>/dev/null && pwd -P)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_MAIN="org.gradle.wrapper.GradleWrapperMain"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "ERROR: Could not find Gradle wrapper JAR: $WRAPPER_JAR" >&2
  exit 1
fi

if [ -n "$JAVA_HOME" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
else
  JAVA_EXE="java"
fi

if ! command -v "$JAVA_EXE" >/dev/null 2>&1; then
  echo "ERROR: Java is not available. Check JAVA_HOME or PATH." >&2
  exit 1
fi

exec "$JAVA_EXE" -classpath "$WRAPPER_JAR" "$WRAPPER_MAIN" "$@"
