#!/bin/bash

# Bail on error
set -e

# Contants
MAVEN_XMLNS='http://maven.apache.org/POM/4.0.0'

# Get project name version
NAME=`xml sel -N m="${MAVEN_XMLNS}" -T -t -m /m:project/m:artifactId -v . pom.xml`
VERSION=`xml sel -N m="${MAVEN_XMLNS}" -T -t -m /m:project/m:version -v . pom.xml`

# Get target JAR file
JAR_FILE="target/${NAME}-${VERSION}.jar"

# Do we need to (re)build the project?
REBUILD='true'
if [ -f "${JAR_FILE}" -a -z "`find pom.xml frontend src -type f -newer \"${JAR_FILE}\" -print 2>/dev/null`" ]; then
    REBUILD='false'
fi

if [ "${REBUILD}" = 'true' ]; then
    mvn -Dmaven.javadoc.skip=true clean package
fi

# Run application
java -jar target/"${NAME}"-"${VERSION}".jar ${1+"$@"}
