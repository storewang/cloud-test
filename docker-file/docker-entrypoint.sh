#!/bin/sh
set -e
ARG_NUM=$#
JAVA_OPS="-Djava.security.egd=file:/dev/urandom"

if [ -z $jvmSize ]; then
   echo "set jvm_sizes"
   jvmSize="-server -Xms512m -Xmx512m -XX:NewSize=128m -XX:MetaspaceSize=200m -XX:MaxMetaspaceSize=200m -XX:MaxDirectMemorySize=200m"
fi

if [ -z $printGc ]; then
   echo "set print_pc_opt"
   printGc="-Xloggc:/logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi

JAVA_OPS="${JAVA_OPS} ${jvmSize} ${printGc}"

if [ $dumpError ]; then
   echo "set dump_error"
   JAVA_OPS="${JAVA_OPS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs/dump.log"
fi

if [ $8099 ]; then
   echo "set jvm_debug_port"
   JAVA_OPS="${JAVA_OPS} -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=${jvmDebugPort}"
fi

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

if [ $discoveryServer ]; then
    echo "set discoveryServer"
    JAVA_OPS="${JAVA_OPS} -Dspring.cloud.nacos.discovery.server-addr=${discoveryServer}"
fi

if [ $optExt ]; then
    echo "set opt_ext"
    JAVA_OPS="${JAVA_OPS} ${optExt}"
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
