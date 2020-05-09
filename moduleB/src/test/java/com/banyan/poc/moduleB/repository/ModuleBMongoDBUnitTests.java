package com.banyan.poc.moduleB.repository;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.banyan.poc.moduleB.model.MongoDBMessageModel;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(MessageDaoImpl.class)
@ActiveProfiles("local")
public class ModuleBMongoDBUnitTests {

    @Autowired
    private MongoDBMessageRepository repository;

    @Autowired
    private MessageDaoImpl messageDaoImpl;

    @Test
    public void test() {

        MessageBean messageBean = getFakeMessage();
        MongoDBMessageModel model = new MongoDBMessageModel(messageBean.getBackToTheFutureCharacter(), messageBean.getBackToTheFutureQuote());

        MongoDBMessageModel persistedModel = repository.save(model);

        MongoDBMessageModel result = repository.findById(persistedModel.getId()).get();
        Assertions.assertNotNull(result.getId());

        long totalListFromRepository = repository.count();
        List<MongoDBMessageModel> totalListFromDao = messageDaoImpl.findAllFakeMessages();
        Assertions.assertEquals(totalListFromRepository, totalListFromDao.size());

        List<MongoDBMessageModel> modelCharacterList = repository.findByBackToTheFutureCharacter(model.getBackToTheFutureCharacter());
        Assertions.assertTrue(modelCharacterList.size() >= 1);

        long characterCount = messageDaoImpl.findFakeMessageCountByCharacter(model.getBackToTheFutureCharacter());
        Assertions.assertEquals(characterCount, modelCharacterList.size());

        List<MongoDBMessageModel> modelQuoteList = repository.findByQuote(model.getBackToTheFutureQuote());
        Assertions.assertTrue(modelQuoteList.size() >= 1);

        long quoteCount = messageDaoImpl.findFakeMessageCountByQuote(model.getBackToTheFutureQuote());
        Assertions.assertEquals(quoteCount, modelQuoteList.size());

    }

    private MessageBean getFakeMessage(){
        Faker faker = new Faker();
        BackToTheFuture backToTheFuture = faker.backToTheFuture();
        MessageBean messageBean = new MessageBean();
        messageBean.setBackToTheFutureCharacter(backToTheFuture.character());
        messageBean.setBackToTheFutureQuote(backToTheFuture.quote());
        return messageBean;
    }
}
