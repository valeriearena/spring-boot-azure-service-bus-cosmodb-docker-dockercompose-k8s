package com.banyan.poc.moduleA.service;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.jms.QueueProducer;
import com.banyan.poc.moduleA.jms.TopicProducer;
import com.banyan.poc.moduleA.restclient.ModuleARestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

    @Autowired
    private ModuleARestClient restClient;

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private TopicProducer topicProducer;

    public MessageBean getMessage(){
        return restClient.getModuleB();
    }

    public MessageBean postMessage(){
        return restClient.postModuleB();
    }

    public MessageBean queueMessage(){
        MessageBean messageBean = restClient.postModuleB();
        queueProducer.produceMessage(messageBean);
        return messageBean;
    }

    public MessageBean publishMessage(){
        MessageBean messageBean = restClient.postModuleB();
        topicProducer.produceMessage(messageBean);
        return messageBean;
    }

}
