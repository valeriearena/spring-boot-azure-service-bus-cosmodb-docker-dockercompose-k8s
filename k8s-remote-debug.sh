#!/bin/bash
# Deployment Usage: ./k8s-remote-debug.sh 'modulename:localport'
# Example deploy moduleA and moduleB: ./k8s-remote-debug.sh 'moduleA:8080 moduleB:8090'
# Example deploy moduleA and moduleB and remote debug moduleA and moduleB: ./k8s-remote-debug.sh 'moduleA:8080 moduleB:8090'
# Example deploy moduleA and moduleB and remote debug moduleA only: ./k8s-remote-debug.sh 'moduleA:8080 moduleB:8090'
# Cleanup Usage: ./k8s-remote-debug.sh 'modulename:cleanup'
# Example moduleA and moduleB cleanup: ./k8s-remote-debug.sh 'moduleA:cleanup moduleB:cleanup'
# Example deploy moduleA and cleanup moduleB: ./k8s-remote-debug.sh 'moduleA:cleanup moduleB:cleanup'
# -------------------------------------------------
module_list=$1
module_array=($(echo $module_list | tr " " "\n"))
echo "SCRIPT ARGUMENTS: $module_list"
echo "_________________________________________________________________"
echo "PATHS AND ENVIRONMENT VARIABLES ..."
echo "INTELLIJ WORKSPACE: $INTELLIJ_PROJECT_PATH"
echo "SPRING_ACTIVE_PROFILE: $SPRING_ACTIVE_PROFILE"
echo "MESSAGING_CONNECTION: $MESSAGING_CONNECTION"
echo "MONGO_DB_CONNECTION: $MONGO_DB_CONNECTION"
echo "MODULEB_MESSAGE_ENDPOINT: $MODULEB_MESSAGE_ENDPOINT"
echo "LOG_DIR: $LOG_DIR"
echo "_________________________________________________________________"

echo "DELETING TELEPRESENCE DEPLOYMENT (if it exists)"
kubectl delete deployments -l telepresence

for i in "${module_array[@]}"
do
	module_port_array=($(echo $i | tr ":" "\n"))
	pod=`echo ${module_port_array[0]} | tr [:upper:] [:lower:]`

  cd "$INTELLIJ_PROJECT_PATH/${module_port_array[0]}"

  echo "_________________________________________________________________"
  echo "PWD: $PWD"
  echo "_________________________________________________________________"


  if [ "${module_port_array[1]}" == "cleanup" ]
  then
  	echo "_________________________________________________________________"
  	echo "STARTING CLEANUP FOR ${module_port_array[0]} ..."
  	echo "CLEANING UP: DELETING MODULE DEPLOYMENT (if it exists)"
  	azds down -y
  	echo "CLEANING UP: DELETING DEV SPACES DOCKERFILE AND HELM CHARTS"
  	rm azds.yaml
  	rm Dockerfile
  	rm Dockerfile.develop*
  	rm .dockerignore
  	rm -R charts/
  	echo "CLEANING UP: DELETING TEMP FILES"
  	rm src/main/resources/application.properties-e
  	echo "CLEANUP FOR ${module_port_array[0]} IS DONE."
  	echo "_________________________________________________________________"
    continue
  fi


  echo "_________________________________________________________________"
  is_dir_prepared=$(azds prep --enable-ingress)
  if [[ "$is_dir_prepared" != *"Current working directory is already prepared."* ]]
  then
    echo "${is_dir_prepared}"
  	echo "GENERATING DEV SPACES DOCKERFILE AND HELM CHARTS ..."
  	sed -i -e 's/3.5-jdk-8-slim/3.6.3-jdk-11/g' Dockerfile.develop
  	sed -i -e 's/ENTRYPOINT \["java","-jar","target\/'"${pod}"'-0.1.0.jar"\]//g' Dockerfile.develop

  	echo "ENV SPRING_ACTIVE_PROFILE=\"$SPRING_ACTIVE_PROFILE\"" >> Dockerfile.develop
  	echo "ENV MODULEB_MESSAGE_ENDPOINT=\"$MODULEB_MESSAGE_ENDPOINT\"" >> Dockerfile.develop
  	echo "ENV MONGO_DB_CONNECTION=\"$MONGO_DB_CONNECTION\"" >> Dockerfile.develop
  	echo "ENV MESSAGING_CONNECTION=\"$MESSAGING_CONNECTION\"" >> Dockerfile.develop
    echo "ENV LOG_DIR=\"$LOG_DIR\"" >> Dockerfile.develop

  	echo "ENTRYPOINT [\"java\",\"-jar\",\"target/"$pod"-0.1.0.jar\"]" >> Dockerfile.develop

  else
  	echo "${is_dir_prepared}"
  fi
  echo "_________________________________________________________________"


  echo "_________________________________________________________________"
  echo "DEPLOYING MODULE ${module_port_array[0]} ..."
  server_port=$(grep "server.port" src/main/resources/application.properties)
  sed -i -e 's/'"${server_port}"'/server.port=8080/g' src/main/resources/application.properties
  azds up -d
  sed -i -e 's/server.port=8080/'"${server_port}"'/g' src/main/resources/application.properties
  echo "_________________________________________________________________"


 	if [ ! -z "${module_port_array[1]}" ]
	then
    echo "_________________________________________________________________"
 		echo "SWAPPING REMOTE ${pod} WITH LOCAL ${pod} ON PORT ${module_port_array[1]}"
 		osascript -e 'tell app "Terminal" to activate'
    osascript -e 'tell app "System Events" to tell process "Terminal" to keystroke "t" using command down'

    echo "telepresence --swap-deployment '$pod' --expose '${module_port_array[1]}':'${module_port_array[1]}' --method inject-tcp"
    osascript -e 'tell app "Terminal" to do script "telepresence --swap-deployment '$pod' --expose '${module_port_array[1]}':8080 --method inject-tcp" in front window'

		echo "${pod} IS DEPLOYED! START DEBUGGING!"
    echo "_________________________________________________________________"
	else
	  	echo "${pod} IS DEPLOYED!"
	fi

done

#  rm src/main/resources/.!*application.properties
#  sed -i -e 's/EXPOSE 8080/EXPOSE '"${module_port_array[1]}"'/g' Dockerfile.develop
#  sed -i -e 's/  port\: 80/  port\: '"${module_port_array[1]}"'/g' charts/$pod/values.yaml
#  sed -i -e 's/  containerPort\: 8080/  containerPort\: '"${module_port_array[1]}"'/g' charts/$pod/values.yaml





