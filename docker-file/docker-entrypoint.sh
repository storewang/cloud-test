#!/bin/sh
set -e
ARG_NUM=$#
JAVA_OPS="-Djava.security.egd=file:/dev/urandom"

if [ -z $profiles ]; then
   echo "set profiles"
   profiles="dev"
fi

if [ -z $serverPort ]; then
   echo "set server port"
   serverPort="8080"
fi

JAVA_OPS="${JAVA_OPS} -Dspring.profiles.active=${profiles} -Dserver.port=${serverPort}"

if [ $configServer ]; then
    echo "set configServer"
    JAVA_OPS="${JAVA_OPS} -Dspring.cloud.nacos.config.server-addr=${configServer}"
fi

if [ -z $appname ]; then
   echo "set appname"
   appname="app.jar"
fi

JAVA_OPS="${JAVA_OPS} -jar ${appname}"

echo "${JAVA_OPS}"
echo "will run as java ${JAVA_OPS}"

if [ $ARG_NUM -gt 0 ]; then
   echo "args grate then 0"
   exec "$@"
fi

if [ $ARG_NUM -eq 0 ]; then
   echo "args.length eq 0"
   java ${JAVA_OPS}
fi
