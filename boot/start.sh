#!/bin/bash
cd `dirname $0`
DEPLOY_DIR=`pwd`

nohup java -jar $DEPLOY_DIR/*.jar 2>&1 &
echo "ID GENERATOR STARTED!"
PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |grep -v grep | awk '{print $2}'`
echo "PID: $PIDS"
