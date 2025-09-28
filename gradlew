#!/bin/sh

set -e

DIR="$(cd "$(dirname "$0")" && pwd)"
GRADLE_WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
  echo "Gradle wrapper JAR is missing. Please run 'gradle wrapper' locally to regenerate." >&2
  exit 1
fi

if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_EXEC="$JAVA_HOME/bin/java"
else
  JAVA_EXEC=$(command -v java || true)
fi

if [ -z "$JAVA_EXEC" ]; then
  echo "Java executable not found. Please install Java 17 or set JAVA_HOME." >&2
  exit 1
fi

exec "$JAVA_EXEC" -jar "$GRADLE_WRAPPER_JAR" "$@"
