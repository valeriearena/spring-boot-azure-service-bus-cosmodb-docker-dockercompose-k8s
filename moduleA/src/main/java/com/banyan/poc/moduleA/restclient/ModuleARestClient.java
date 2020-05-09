package com.banyan.poc.moduleA.restclient;

import com.banyan.poc.moduleA.bean.MessageBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * WebClient uses the Reactive Streams API. WebClient has replaced RestTemplate, which is deprecated as of Spring 4.3.
 *
 * Although WebClient supports asynchronous REST calls, the WebClient in ModuleARestClient
 * is used in a synchronous manner and blocks until it receives a response.
 */
@Slf4j
@Data
@Component
public class ModuleARestClient {

    @Value("${modulea.uri}")
    private String uri;

    private WebClient webClient;

//    @PostConstruct
//    public void init(){
//        webClient = WebClient
//                .builder()
//                .exchangeStrategies( // Explicitly enable header logging. By default, headers are masked.
//                        ExchangeStrategies.builder().codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true)).build())
//                .baseUrl(uri)
//                .build();
//    }

    public MessageBean postModuleB() {
        log.info("Module A sending a request to Module B. uri={}", uri);
        webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true).wiretap(true)))
                .exchangeStrategies( // Explicitly enable header logging. By default, headers are masked.
                        ExchangeStrategies.builder().codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true)).build())
                .baseUrl(uri)
                .build();

        MessageBean messageBean = webClient.post().retrieve().bodyToMono(MessageBean.class).block();
        log.info("Module A received response from Module B: {}", messageBean);
        return messageBean;
    }

    public MessageBean getModuleB() {
        log.info("Module A sending a request to Module B. uri={}", uri);
        webClient = WebClient
                .builder()
                .exchangeStrategies( // Explicitly enable header logging. By default, headers are masked.
                        ExchangeStrategies.builder().codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true)).build())
                .baseUrl(uri)
                .build();
        MessageBean messageBean = webClient.get().retrieve().bodyToMono(MessageBean.class).block();
        log.info("Module A received response from Module B: {}", messageBean);
        return messageBean;
    }

//    public MessageBean getModuleB() {
//        log.info("Module A sending a request to Module B. uri={}", uri);
//        webClient.post()
//                .exchange()
//                .flatMap( clientResponse -> {
//                    //Error handling
//                    if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
//                        return clientResponse.createException().flatMap( Mono::error );
//                    }
//                    return clientResponse.bodyToMono(MessageBean.class);
//                });
//
//        MessageBean messageBean = webClient.get().retrieve().bodyToMono(MessageBean.class).block();
//        log.info("Module A received response from Module B: {}", messageBean);
//        return messageBean;
//    }
}
