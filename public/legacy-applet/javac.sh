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
  # Find all .class files for this base (main and inner classes)
  classfiles=( "$BASENAME.class" "$BASENAME"\$*.class )
  # Only include files that exist
  tojar=()
  for f in "${classfiles[@]}"; do
    for match in $f; do
      [ -e "$match" ] && tojar+=("$match")
    done
  done

  # Add resources as before
  resources=()
  for ext in mid png wav mp3; do
    if [ -f "$BASENAME.$ext" ]; then
      resources+=("$BASENAME.$ext")
    fi
  done

  # For TetrisApplet, include all .mid files in the directory
  if [ "$BASENAME" = "TetrisApplet" ]; then
    for f in *.wav; do
      [ -e "$f" ] && resources+=("$f")
    done
  fi

  echo "Creating $JARFILE with ${tojar[*]}${resources:+ and ${resources[*]}}..."
  jar cf "$JARFILE" "${tojar[@]}" "${resources[@]}"
  rm -f "${tojar[@]}"
  JAR_STATUS=$?
  if [ $JAR_STATUS -eq 0 ]; then
    echo "JAR created: $JARFILE"
    # Remove the .class file after creating the jar
    # The .class files are now removed by the new logic
    echo "Removed all .class files, only $JARFILE remains."
  else
    echo "Failed to create JAR archive."
    exit 4
  fi
else
  echo "Compilation failed."
  exit 3
fi
