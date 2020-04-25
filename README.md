# banyan-springboot
Repository for Spring Boot POCs.

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

