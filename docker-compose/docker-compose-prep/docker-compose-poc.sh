#!/bin/bash
echo "MONGO_DB_CONNECTION: $MONGO_DB_CONNECTION"
echo "MESSAGING_CONNECTION: $MESSAGING_CONNECTION"
echo "MODULEB_MESSAGE_ENDPOINT: $MODULEB_MESSAGE_ENDPOINT"
echo "LOG_DIR: $LOG_DIR"

sed -i -e "s|\[MONGO_DB_CONNECTION\]|$MONGO_DB_CONNECTION|g" $HOME/docker-compose.env
sed -i -e "s|\[MESSAGING_CONNECTION\]|$MESSAGING_CONNECTION|g" $HOME/docker-compose.env
sed -i -e "s|\[MODULEB_MESSAGE_ENDPOINT\]|$MODULEB_MESSAGE_ENDPOINT|g" $HOME/docker-compose.env
sed -i -e "s|\[LOG_DIR\]|$LOG_DIR|g" $HOME/docker-compose.env

read -p "ENTER MODULES: "  modules

module_array=($(echo $modules | tr " " "\n"))
echo "_________________________________________________________________"
echo "SCRIPT ARGUMENTS: $modules"
echo "INTELLIJ WORKSPACE: $INTELLIJ_PROJECT_PATH"
echo "_________________________________________________________________"

for i in "${module_array[@]}"
do
	module_image_name=`echo ${i} | tr [:upper:] [:lower:]`
  cd "$INTELLIJ_PROJECT_PATH/${module_image_name}"

  echo "_________________________________________________________________"
  echo "PWD: $PWD"
  echo "BUILDING DOCKER IMAGE: $module_image_name"
  ./gradlew clean build -x test jibDockerBuild --image=$module_image_name
  echo "DONE BUILDING DOCKER IMAGE: $module_image_name"
  echo "_________________________________________________________________"
done
