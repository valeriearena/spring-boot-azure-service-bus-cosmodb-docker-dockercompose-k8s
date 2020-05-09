package com.banyan.poc.moduleA.service;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.jms.QueueProducer;
import com.banyan.poc.moduleA.jms.TopicProducer;
import com.banyan.poc.moduleA.restclient.ModuleARestClient;
import com.banyan.poc.moduleA.service.MessageService;
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

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import({MessageService.class})
@ActiveProfiles("local")
public class MessageServiceUnitTestsWithJMSAndRestClientMocks {

    @Autowired
    private MessageService messageService;

    @MockBean
    private QueueProducer queueProducer;

    @MockBean
    private TopicProducer topicProducer;

    @MockBean
    private ModuleARestClient moduleARestClient;

    @Test
    public void testJmsQueue(){
        MessageBean messageBean = getFakeMessage();
        Mockito.when(moduleARestClient.postModuleB()).thenReturn(messageBean);
        messageService.queueMessage();
        Mockito.verify(queueProducer, times(1)).produceMessage(messageBean);
    }

    @Test
    public void testJmsTopic(){
        MessageBean messageBean = getFakeMessage();
        Mockito.when(moduleARestClient.postModuleB()).thenReturn(messageBean);
        messageService.publishMessage();
        Mockito.verify(topicProducer, times(1)).produceMessage(messageBean);
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