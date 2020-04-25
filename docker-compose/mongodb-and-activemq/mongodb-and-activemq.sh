#!/bin/bash
read -p "[start] [stop]: "  command

cd $INTELLIJ_PROJECT_PATH/docker-compose/mongodb-and-activemq/

if [ "${command}" == "start" ]
then
  docker-compose up -d
  exit
fi

if [ "${command}" == "stop" ]
then
  docker-compose down
  exit
fi