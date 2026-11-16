#!/bin/sh

# Bail on error
set -ex

# Homebrew name for the tomcat we should use
TOMCAT_NAME='tomcat'

# Find tomcat
if ! brew list "${TOMCAT_NAME}" >/dev/null 2>&1; then
    echo "Error: homebrew package ${TOMCAT_NAME} not found" 1>&2
    exit 1
fi
TOMCAT_ROOT=`find -s $(brew --prefix)/opt -maxdepth 1 -name "${TOMCAT_NAME}" -print -quit`
if [ -z "${TOMCAT_ROOT}" ]; then
    echo "Error: can't find install directory for ${TOMCAT_NAME}" 1>&2
    exit 1
fi

# Clean up
rm -rf target

# Build stuff
mvn ${CLEAN} package

# Identify (most recently built) WAR file
WAR=`find target -maxdepth 1 -name 'vbug-*.war' | xargs /bin/ls -1t | head -n 1`

# Stick it there
ln -f "${WAR}" "${TOMCAT_ROOT}/libexec/webapps/vbug.war"

# Restart tomcat
brew services restart "${TOMCAT_NAME}"

# Show logs as we run
tail -F "${HOME}/vbug.log"
