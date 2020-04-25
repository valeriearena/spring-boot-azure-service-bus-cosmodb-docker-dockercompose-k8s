package com.banyan.poc.moduleA;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.jms.QueueProducer;
import com.banyan.poc.moduleA.jms.TopicProducer;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("local")
public class ModuleAJmsTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private TopicProducer topicProducer;

    @Value("${modulea.jms.queue.name}")
    private String queueName;

    @Value("${modulea.jms.topic.name}")
    private String topicName;

    @Test
    public void testJmsQueue() throws Exception {
        MessageBean messageToSend = getFakeMessage();
        queueProducer.produceMessage(messageToSend);
        String message = (String)jmsTemplate.receiveAndConvert(queueName);
        MessageBean messageReceived = new Gson().fromJson(message, MessageBean.class);
        Assertions.assertTrue(messageReceived.equals(messageToSend));
    }

    @Test
    public void testJmsTopic() throws Exception {
        MessageBean messageToSend = getFakeMessage();
        topicProducer.produceMessage(messageToSend);
        String message = (String)jmsTemplate.receiveAndConvert(topicName);
        MessageBean messageReceived = new Gson().fromJson(message, MessageBean.class);
        Assertions.assertTrue(messageReceived.equals(messageToSend));
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