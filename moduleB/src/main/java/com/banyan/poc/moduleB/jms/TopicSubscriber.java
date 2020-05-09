package com.banyan.poc.moduleB.jms;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @JmsListener Tells Spring to listen for JMS messages on the specified destination (push vs pull model of JMSTemplate::receive, which blocks.)
 */
@Slf4j
@Service
public class TopicSubscriber {

    @JmsListener(destination = "${moduleb.jms.topic.name}", containerFactory = "${moduleb.jms.topic.connection.factory}", subscription = "${moduleb.jms.topic.subscription}")
    public void receiveMessage(TextMessage message)throws JMSException {
        try {
            MessageBean messageBean = new Gson().fromJson(message.getText(), MessageBean.class);
            log.info("Module B consuming JMS message published to topic: {}", messageBean);
        }
        catch (JMSException e){
            log.error(e.getMessage(), e);
        }
    }

}
