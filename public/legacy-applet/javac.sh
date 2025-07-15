#!/usr/bin/env bash

# Usage: ./compilejava.sh [JavaFile.java]
# Compiles the specified Java file, or all .java files in the current directory if none is specified.
# You can override the Java version by setting JAVA_VERSION, e.g.:
#   RUN COMMAND:
#   JAVA_VERSION=11 bash javac.sh MyApplet.java

JAVA_VERSION="${JAVA_VERSION:-8}"

if [ $# -eq 0 ]; then
  # No file specified, compile all .java files in the directory
  JAVA_FILES=(*.java)
  if [ "${JAVA_FILES[0]}" = "*.java" ]; then
    echo "No Java files found in the current directory."
    exit 1
  fi
  echo "Compiling all Java files with -source $JAVA_VERSION -target $JAVA_VERSION..."
  javac -source "$JAVA_VERSION" -target "$JAVA_VERSION" "${JAVA_FILES[@]}"
  STATUS=$?
else
  JAVA_FILE="$1"
  if [ ! -f "$JAVA_FILE" ]; then
    echo "Error: File '$JAVA_FILE' not found!"
    exit 2
  fi
  echo "Compiling $JAVA_FILE with -source $JAVA_VERSION -target $JAVA_VERSION..."
  javac -source "$JAVA_VERSION" -target "$JAVA_VERSION" "$JAVA_FILE"
  STATUS=$?
fi

if [ $STATUS -eq 0 ]; then
  echo "Compilation successful."
else
  echo "Compilation failed."
  exit 3
fi
