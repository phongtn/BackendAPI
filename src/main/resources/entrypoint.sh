#!/usr/bin/env bash
export IP_ADDRESS=`ifconfig eth0| sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p'`
export HOSTNAME=`hostname`

echo "===== Detecting IP & Hostname ====="
echo "IP Address: $IP_ADDRESS"
echo "Hostname: $HOSTNAME"

echo "===== Starting Service ====="
java -jar -Xmx512m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=${APPLICATION_PROFILE:-default} /app/service.jar
