#!/bin/sh

# If JAVA_HOME is not set, uncomment and use below
# JAVA_HOME=/opt/sun-jdk-1.5.0

# DO NOT EDIT BELOW THIS LINE
MAXMEM=512m

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

PRG="$0"

# need this for relative symlinks
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
done

saveddir=`pwd`

APP_HOME=`dirname "$PRG"`/..

# make it fully qualified
APP_HOME=`cd "$APP_HOME" && pwd`

cd "$saveddir"

exec ${JAVA_HOME}/bin/java -Xmx$MAXMEM -classpath "$APP_HOME"/lib/classworlds-1.1.jar -Dclassworlds.conf=classworlds.conf "-Dapp.home=$APP_HOME" org.codehaus.classworlds.Launcher $*
