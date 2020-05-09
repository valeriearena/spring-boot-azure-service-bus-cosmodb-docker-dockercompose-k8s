package com.banyan.poc.moduleA.jms;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${modulea.jms.queue.name}")
    private String queueName;

    public void produceMessage(MessageBean messageBean){
        String jsonMessage = new Gson().toJson(messageBean);
        log.info("Module A putting JMS message on queue {}: {}", queueName, jsonMessage);
        jmsTemplate.convertAndSend(queueName, jsonMessage);
    }
}
