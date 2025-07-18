#!/usr/bin/env bash

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
MANIFEST_FILE="manifest.mf"

# Compile the Java file
echo "Compiling $JAVA_FILE with -source $JAVA_VERSION -target $JAVA_VERSION..."
javac -source "$JAVA_VERSION" -target "$JAVA_VERSION" "$JAVA_FILE"
STATUS=$?

if [ $STATUS -eq 0 ]; then
  echo "Compilation successful."

  # Collect class files
  classfiles=( "$BASENAME.class" "$BASENAME"\$*.class )
  tojar=()
  for f in "${classfiles[@]}"; do
    for match in $f; do
      [ -e "$match" ] && tojar+=("$match")
    done
  done

  # Collect additional resources
  resources=()
  for ext in mid png wav mp3; do
    if [ -f "$BASENAME.$ext" ]; then
      resources+=("$BASENAME.$ext")
    fi
  done

  if [ "$BASENAME" = "TetrisApplet" ]; then
    for f in *.wav; do
      [ -e "$f" ] && resources+=("$f")
    done
  fi

  # Create a manifest with Main-Class set
  echo "Main-Class: $BASENAME" > "$MANIFEST_FILE"
  echo "" >> "$MANIFEST_FILE"  # mandatory blank line

  echo "Creating $JARFILE with main class '$BASENAME'..."
  jar cfm "$JARFILE" "$MANIFEST_FILE" "${tojar[@]}" "${resources[@]}"

  JAR_STATUS=$?
  if [ $JAR_STATUS -eq 0 ]; then
    echo "JAR created: $JARFILE"
    rm -f "${tojar[@]}" "$MANIFEST_FILE"
    echo "Cleaned up .class files and manifest."
  else
    echo "Failed to create JAR archive."
    exit 4
  fi
else
  echo "Compilation failed."
  exit 3
fi

