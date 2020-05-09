package com.banyan.poc.moduleB.repository;

import com.banyan.poc.moduleB.model.MongoDBMessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mongoOperations executes MongoDB operations.
 *
 * MongoDB transactions are turned off by default and need to be explicitly enabled in JavaConfig.
 * Unless you specify a MongoTransactionManager within your application context, transaction support is DISABLED.
 *
 * NOTE: Some operations cannot be executed within a MongoDB transaction. Refer to the MongoDB documentation to learn more about Multi Document Transactions.
 * Spring data repositories are using MongoTemplate under the covers.
 *
 * The implementation of MessageDaoImpl can be added to the runtime proxy of MongoDBMessageRepository. See MongoDBMessageRepository for details.
 *
 * @Repository Tells Spring that SQLExceptions should be translated into DataAccessExceptions.
 */
@Repository
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String MESSAGES_COLLECTION = "messages";

    public List<MongoDBMessageModel> findAllFakeMessages(){
        return mongoTemplate.findAll(MongoDBMessageModel.class);
    }

    public long findFakeMessageCountByCharacter(String character){
        Query query = new Query();
        query.addCriteria(Criteria.where("character").is(character));
        return mongoTemplate.count(query, MESSAGES_COLLECTION);
    }

    public long findFakeMessageCountByQuote(String quote){
        BasicQuery basicQuery = new BasicQuery("{ quote: \""+quote+"\"}");
        return mongoTemplate.count(basicQuery, MESSAGES_COLLECTION);
    }
}
