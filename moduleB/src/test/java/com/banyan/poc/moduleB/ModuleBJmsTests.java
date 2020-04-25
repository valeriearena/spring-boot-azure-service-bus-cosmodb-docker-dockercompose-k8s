package com.banyan.poc.moduleB;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles("local")
public class ModuleBJmsTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${moduleb.jms.queue.name}")
    private String queueName;

    @Value("${moduleb.jms.topic.name}")
    private String topicName;

    @Test
    public void testJmsQueueConsumer()throws Exception{
        MessageBean messageBean = getFakeMessage();
        String jsonMessage = new Gson().toJson(messageBean);
        jmsTemplate.convertAndSend(queueName, jsonMessage);
        TimeUnit.SECONDS.sleep(1);
        int count = countPendingMessages(queueName);
        Assertions.assertEquals(0, count);
    }

    @Test
    public void testJmsTopicSubscriber()throws Exception{
        MessageBean messageBean = getFakeMessage();
        String jsonMessage = new Gson().toJson(messageBean);
        jmsTemplate.convertAndSend(topicName, jsonMessage);
        TimeUnit.SECONDS.sleep(1);
        int count = countPendingMessages(queueName);
        Assertions.assertEquals(0, count);
    }

    public int countPendingMessages(String destination) {
        Integer totalPendingMessages = this.jmsTemplate.browse(destination, (session, browser) -> Collections.list(browser.getEnumeration()).size());
        return totalPendingMessages == null ? 0 : totalPendingMessages;
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
