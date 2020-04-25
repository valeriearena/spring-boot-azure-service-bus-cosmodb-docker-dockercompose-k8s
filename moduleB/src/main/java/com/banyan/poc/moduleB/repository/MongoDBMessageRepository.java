package com.banyan.poc.moduleB.repository;

import com.banyan.poc.moduleB.model.MongoDBMessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repositories are all interfaces. MongoDBMessageRepository extends from existing Spring Data repositories,
 * which declare CRUD operations and queries (MongoRepository extends PagingAndSortingRepository extends CrudRepository).
 * Spring creates a runtime proxy that can be injected into MessageService.
 *
 * There are two types of custom queries: queries derived from method names and queries defined in the @Query annotation.
 * The queries declared in method names follow specific naming conventions. Spring Boot fails to start if there are any errors. Spring validates the method names at startup.
 *
 * MongoDB transactions are turned off by default and need to be explicitly enabled in JavaConfig.
 * Unless you specify a MongoTransactionManager within your application context, transaction support is DISABLED.
 *
 * NOTE: Some operations cannot be executed within a MongoDB transaction. Refer to the MongoDB documentation to learn more about Multi Document Transactions.
 * Spring data repositories are using MongoTemplate under the covers.
 *
 * MessageDaoImpl is the DAO implementation using MongoTemplate. The DAO implementation can be added to the proxy implementation by adding MessageDao to extends:
 *
 *      public interface MongoDBMessageRepository extends MongoRepository<MongoDBMessageModel, String>, MessageDao
 *
 * @Repository Tells Spring that SQLExceptions should be translated into DataAccessExceptions.
 */
@Repository
public interface MongoDBMessageRepository extends MongoRepository<MongoDBMessageModel, String>{

    List<MongoDBMessageModel> findByBackToTheFutureCharacter(String character);

    @Query("{ quote : ?0 }")
    List<MongoDBMessageModel> findByQuote(String quote);

}