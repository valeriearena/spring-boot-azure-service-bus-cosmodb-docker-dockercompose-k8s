/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

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
 * @JmsListener is a method level annotation so we still need to use @Component
 */
@Slf4j
@Service
public class QueueConsumer {

    @JmsListener(destination = "${moduleb.jms.queue.name}", containerFactory = "${moduleb.jms.queue.connection.factory}")
    public void receiveMessage(TextMessage message){
        try {
            MessageBean messageBean = new Gson().fromJson(message.getText(), MessageBean.class);
            log.info("Module B consuming JMS message put on queue: {}", messageBean);
        }
        catch (JMSException e){
            log.error(e.getMessage(), e);
        }
    }
}
