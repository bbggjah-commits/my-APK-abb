#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# For Cygwin or MSYS, use an adapted path.
case "`uname`" in
  CYGWIN*|MSYS*)
    CLASSPATH=`cygpath -wp "$CLASSPATH"`
    ;;
esac

# OS specific support (must be put in a separate file to be sourced)
case "`uname`" in
  Darwin)
    OS_SPECIFIC_CODE=true
    ;;
  *)
    OS_SPECIFIC_CODE=false
    ;;
esac

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Determine the script directory.
SCRIPT_DIR=$(dirname "$0")

# Determine the full path to the script.
SCRIPT_PATH=$(cd "$SCRIPT_DIR" && pwd -P)

# Determine the full path to the wrapper jar.
WRAPPER_JAR="$SCRIPT_PATH/gradle/wrapper/gradle-wrapper.jar"

# Check if the wrapper jar exists.
if [ ! -f "$WRAPPER_JAR" ] ; then
    die "ERROR: The specified Gradle wrapper jar '$WRAPPER_JAR' does not exist.

Please ensure that the file is present and readable."
fi

# Determine the full path to the wrapper properties file.
WRAPPER_PROPERTIES="$SCRIPT_PATH/gradle/wrapper/gradle-wrapper.properties"

# Check if the wrapper properties file exists.
if [ ! -f "$WRAPPER_PROPERTIES" ] ; then
    die "ERROR: The specified Gradle wrapper properties file '$WRAPPER_PROPERTIES' does not exist.

Please ensure that the file is present and readable."
fi

# Execute the wrapper.
exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS -classpath "$WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
