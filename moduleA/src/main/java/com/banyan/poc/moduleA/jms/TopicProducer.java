package com.banyan.poc.moduleA.jms;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${modulea.jms.topic.name}")
    private String topicName;

    public void produceMessage(Object messageBean){
        String jsonMessage = new Gson().toJson(messageBean);
        log.info("Module A publishing JMS message to topic {} for Module B: {}", topicName, jsonMessage);
        jmsTemplate.convertAndSend(topicName, jsonMessage);
    }
}
