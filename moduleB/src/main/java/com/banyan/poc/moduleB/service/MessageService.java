package com.banyan.poc.moduleB.service;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.banyan.poc.moduleB.model.MongoDBMessageModel;
import com.banyan.poc.moduleB.repository.MessageDao;
import com.banyan.poc.moduleB.repository.MongoDBMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MongoDBMessageRepository repository;

    @Autowired
    private MessageDao dao;

    public void save(MessageBean messageBean) {

        MongoDBMessageModel model = new MongoDBMessageModel(messageBean.getBackToTheFutureCharacter(), messageBean.getBackToTheFutureQuote());

        MongoDBMessageModel persistedModel = repository.save(model);

        MongoDBMessageModel result = repository.findById(persistedModel.getId()).get();

        long totalCount = repository.count();

        List<MongoDBMessageModel> modelCharacterList = repository.findByBackToTheFutureCharacter(model.getBackToTheFutureCharacter());

        List<MongoDBMessageModel> modelQuoteList = repository.findByQuote(model.getBackToTheFutureQuote());

        List<MongoDBMessageModel> totalList = dao.findAllFakeMessages();

        long characterCount = dao.findFakeMessageCountByCharacter(model.getBackToTheFutureCharacter());

        long quoteCount = dao.findFakeMessageCountByQuote(model.getBackToTheFutureQuote());

    }

}
