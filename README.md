# spring-boot-azure-service-bus-cosmodb-docker-dockercompose-k8s
Spring Boot app that implements Spring REST controllers and WebClient and uses JMS and Spring Data.

When deploying locally, the Spring Boot app integrates with ActiveMQ and MongoDB. The repo comes with a script to build docker images for each module in the repo. It also comes with scripts and docker-compose yaml files to deploy all application modules, as well as ActiveMQ and MongoDB containers.

When deploying to Azure, the Spring Boot app integrates with Azure Service Bus and Cosmos DB with MongoDB API. The repo comes with a script to build docker images for each module in the repo and deploy to an Azure Kubernetes Service cluster using Dev Spaces.

# Usage
* When the following curl is executed: 
1. Module A will call Module B. 
2. Module B will generate a message and save it in Azure MongoDB (CosmoDB with MongoDB API).
3. Module B will return the message to Module A. <br/>
```curl -X POST http://localhost:8080/name/store```

* When the following curl is executed: 
1. Module A will call Module B. 
2. Module B will generate a message and save it in MongoDB.
3. Module B will return the message to Module A.
4. Module A will put the message on the Service Bus JMS queue. <br/>
```curl -X POST http://localhost:8080/name/queue```

* When the following curl is executed:  
1. Module A will call Module B. 
2. Module B will generate a message and save it in MongoDB.
3. Module B will return the message to Module A.
4. Module A will publis the message to the Service Bus JMS topic. <br/>
```curl -X POST http://localhost:8080/name/publish```

