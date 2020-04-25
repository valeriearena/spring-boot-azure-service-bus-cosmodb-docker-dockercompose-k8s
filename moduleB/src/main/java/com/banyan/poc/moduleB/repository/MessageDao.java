package com.banyan.poc.moduleB.repository;

import com.banyan.poc.moduleB.model.MongoDBMessageModel;

import java.util.List;

public interface MessageDao {

    List<MongoDBMessageModel> findAllFakeMessages();

    long findFakeMessageCountByCharacter(String character);

    long findFakeMessageCountByQuote(String quote);
}
