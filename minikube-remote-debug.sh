#!/bin/bash
# Deployment Usage: ./minikube-remote-debug.sh 'modulename:localport'
# Example deploy and remote debug both moduleA and moduleB: ./minikube-remote-debug.sh 'moduleA:8080 moduleB:8090'
# Example deploy moduleA and moduleB and remote debug moduleA only: ./minikube-remote-debug.sh 'moduleA:8080 moduleB'
# Cleanup Usage: ./minikube-remote-debug.sh 'modulename:cleanup'
# Example moduleA and moduleB cleanup: ./minikube-remote-debug.sh 'moduleA:cleanup moduleB:cleanup'
# -------------------------------------------------
path="/Users/mayuresh.bhojane/Bitbucket/banyan-spring-boot-cicd-poc"

module_list=$1
module_array=($(echo $module_list | tr " " "\n"))
echo "SCRIPT ARGUMENTS: $module_list"
echo "_________________________________________________________________"
echo "INTELLIJ WORKSPACE: $path"
echo "_________________________________________________________________"
echo "SETTING MINIKUBE ENVIRONMENT"
eval $(minikube docker-env)
if [[ $? -ne 0 ]]; then
    echo "MAKE SURE MINIKUBE IS RUNNING. USE 'minikube start --driver=virtualbox' COMMAND TO START MINIKUBE."
    exit 1
fi
echo "_________________________________________________________________"
echo "DELETING TELEPRESENCE DEPLOYMENT (if it exists)"
kubectl delete deployments -l telepresence
echo "_________________________________________________________________"
for i in "${module_array[@]}"
do
  module_port_array=($(echo $i | tr ":" "\n"))
  pod=`echo ${module_port_array[0]} | tr [:upper:] [:lower:]`

  cd "$path/${module_port_array[0]}"
  echo "PWD: $PWD"
  echo "_________________________________________________________________"

  echo "STARTING CLEANUP FOR ${module_port_array[0]} ..."
  echo "DELETING MODULE DEPLOYMENT (if it exists)"
  kubectl delete deployments springbootcidcpockubernetes-${pod}
  kubectl delete svc springbootcidcpockubernetes-${pod}
  echo "CLEANUP FOR ${module_port_array[0]} IS DONE."
  echo "_________________________________________________________________"

  if [ "${module_port_array[1]}" == "cleanup" ]
  then
    	continue
  fi

  echo "CREATING DOCKER IMAGE AND DEPLOYING MODULE ${module_port_array[0]} TO MINIKUBE"
  echo "CREATING IMAGE ..."
  ./gradlew jibDockerBuild --image=springbootcidcpockubernetes-${pod}
  echo "_________________________________________________________________"
  echo "DEPLOYING ..."
  kubectl create -f ${pod}.yml

 	if [ ! -z "${module_port_array[1]}" ]
	then
    echo "_________________________________________________________________"
 		echo "SWAPPING REMOTE ${pod} WITH LOCAL ${pod} ON PORT ${module_port_array[1]}"
 		osascript -e 'tell app "Terminal" to activate'
    osascript -e 'tell app "System Events" to tell process "Terminal" to keystroke "t" using command down'

    echo "telepresence --swap-deployment springbootcidcpockubernetes-'$pod' --expose '${module_port_array[1]}':'${module_port_array[1]}' --method inject-tcp"
    osascript -e 'tell app "Terminal" to do script "telepresence --swap-deployment springbootcidcpockubernetes-'$pod' --expose '${module_port_array[1]}':8080 --method inject-tcp" in front window'

		echo "${pod} IS DEPLOYED! START DEBUGGING!"
    echo "_________________________________________________________________"
	else
	  	echo "${pod} IS DEPLOYED!"
	fi

done



