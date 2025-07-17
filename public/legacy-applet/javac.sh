#!/usr/bin/env bash

# Compiles the specified Java file, or all .java files in the current directory if none is specified.
# You can override the Java version by setting JAVA_VERSION, e.g.:
#   RUN COMMAND:
#   JAVA_VERSION=8 bash javac.sh MyApplet.java

JAVA_VERSION="${JAVA_VERSION:-8}"

if [ $# -eq 0 ]; then
  echo "Error: Please specify a Java file to compile."
  exit 1
fi

JAVA_FILE="$1"
if [ ! -f "$JAVA_FILE" ]; then
  echo "Error: File '$JAVA_FILE' not found!"
  exit 2
fi

BASENAME="${JAVA_FILE%.java}"
CLASSFILE="$BASENAME.class"
JARFILE="$BASENAME.jar"

# Compile the Java file
echo "Compiling $JAVA_FILE with -source $JAVA_VERSION -target $JAVA_VERSION..."
javac -source "$JAVA_VERSION" -target "$JAVA_VERSION" "$JAVA_FILE"
STATUS=$?

if [ $STATUS -eq 0 ]; then
  echo "Compilation successful."
  # Find resources with the same base name (e.g., .mid, .png, .wav, .mp3)
  resources=()
  for ext in mid png wav mp3; do
    if [ -f "$BASENAME.$ext" ]; then
      resources+=("$BASENAME.$ext")
    fi
  done
  echo "Creating $JARFILE with $CLASSFILE${resources:+ and ${resources[*]}}..."
  jar cf "$JARFILE" "$CLASSFILE" "${resources[@]}"
  JAR_STATUS=$?
  if [ $JAR_STATUS -eq 0 ]; then
    echo "JAR created: $JARFILE"
    # Remove the .class file after creating the jar
    rm -f "$CLASSFILE"
    echo "Removed $CLASSFILE, only $JARFILE remains."
  else
    echo "Failed to create JAR archive."
    exit 4
  fi
else
  echo "Compilation failed."
  exit 3
fi
