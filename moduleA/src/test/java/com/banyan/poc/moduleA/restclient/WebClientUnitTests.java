package com.banyan.poc.moduleA.restclient;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.restclient.ModuleARestClient;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@Import({ModuleARestClient.class})
public class WebClientUnitTests {

    @Autowired
    private ModuleARestClient moduleARestClient;

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @BeforeEach
    void initialize() {
        String uri = String.format("http://localhost:%s", mockBackEnd.getPort());
        moduleARestClient.setUri(uri);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testPost(){
        MessageBean mockMessageBean = getFakeMessage();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(new Gson().toJson(mockMessageBean))
                .addHeader("Content-Type", "application/json"));

        MessageBean messageBean = moduleARestClient.postModuleB();

        Assertions.assertEquals(mockMessageBean.getBackToTheFutureCharacter(), messageBean.getBackToTheFutureCharacter());
        Assertions.assertEquals(mockMessageBean.getBackToTheFutureQuote(), messageBean.getBackToTheFutureQuote());

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
