package com.banyan.poc.moduleB.service;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.banyan.poc.moduleB.model.MongoDBMessageModel;
import com.banyan.poc.moduleB.repository.MessageDao;
import com.banyan.poc.moduleB.repository.MongoDBMessageRepository;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import({MessageService.class})
@ActiveProfiles("local")
public class MessageServiceUnitTestsWithMongoDBMocks {

    @Autowired
    private MessageService messageService;

    @MockBean
    private MongoDBMessageRepository repository;

    @MockBean
    private MessageDao dao;

    @Test
    public void testSave(){

        MessageBean messageBean = getFakeMessage();
        MongoDBMessageModel model = new MongoDBMessageModel(messageBean.getBackToTheFutureCharacter(), messageBean.getBackToTheFutureQuote());
        MongoDBMessageModel savedModel = new MongoDBMessageModel(messageBean.getBackToTheFutureCharacter(), messageBean.getBackToTheFutureQuote());
        savedModel.setId("1");

        Optional<MongoDBMessageModel> option = Optional.of(savedModel);

        List<MongoDBMessageModel> messageList = new ArrayList<>();

        Mockito.when(repository.save(model)).thenReturn(savedModel);
        Mockito.when((repository.findById(savedModel.getId()))).thenReturn(option);
        Mockito.when(repository.findByBackToTheFutureCharacter(savedModel.getBackToTheFutureCharacter())).thenReturn(Mockito.anyList());
        Mockito.when(repository.findByQuote(savedModel.getBackToTheFutureQuote())).thenReturn(messageList);

        Mockito.when(dao.findAllFakeMessages()).thenReturn(Mockito.anyList());
        Mockito.when(dao.findFakeMessageCountByCharacter(savedModel.getBackToTheFutureCharacter())).thenReturn(Mockito.anyLong());
        Mockito.when(dao.findFakeMessageCountByQuote(savedModel.getBackToTheFutureQuote())).thenReturn(Mockito.anyLong());

        messageService.save(messageBean);

        Mockito.verify(repository, times(1)).save(model);
        Mockito.verify(repository, times(1)).findById(savedModel.getId());
        Mockito.verify(repository, times(1)).findByBackToTheFutureCharacter(savedModel.getBackToTheFutureCharacter());
        Mockito.verify(repository, times(1)).findByQuote(savedModel.getBackToTheFutureQuote());

        Mockito.verify(dao, times(1)).findAllFakeMessages();
        Mockito.verify(dao, times(1)).findFakeMessageCountByCharacter(savedModel.getBackToTheFutureCharacter());
        Mockito.verify(dao, times(1)).findFakeMessageCountByQuote(savedModel.getBackToTheFutureQuote());

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
