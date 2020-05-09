package com.banyan.poc.moduleA.jms;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.service.MessageService;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import({QueueProducer.class, TopicProducer.class})
@ActiveProfiles("local")
public class JmsUnitTests {

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private TopicProducer topicProducer;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Value("${modulea.jms.queue.name}")
    private String queueName;

    @Value("${modulea.jms.topic.name}")
    private String topicName;

    @Test
    public void testJmsQueue(){
        MessageBean messageBean = getFakeMessage();
        queueProducer.produceMessage(messageBean);

        String jsonMessage = new Gson().toJson(messageBean);
        Mockito.verify(jmsTemplate, times(1)).convertAndSend(queueName,jsonMessage);
    }

    @Test
    public void testJmsTopic(){
        MessageBean messageBean = getFakeMessage();
        topicProducer.produceMessage(messageBean);

        String jsonMessage = new Gson().toJson(messageBean);
        Mockito.verify(jmsTemplate, times(1)).convertAndSend(topicName,jsonMessage);
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