package com.banyan.poc.moduleA.controller;

import com.banyan.poc.moduleA.bean.MessageBean;
import com.banyan.poc.moduleA.service.MessageService;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * @RestController is a convenience annotation that combines @Controller and @ResponseBody.
 * @Controller tells Spring that DispatcherServlet should map incoming requests to /message should be mapped to ModuleARestController.
 * @ResponseBody tells Spring that the return value of handler methods should be written to the body of response.
 *
 * 	The following maps an HTTP request to /name to the ModuleARestController.
 * 	@RequestMapping(path = "name", produces = "application/json")
 *
 * 	This is a shortcut:
 * 	@RequestMapping("/name")
 */
@Slf4j
@RestController
@RequestMapping("/name")
public class ModuleARestController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/helloworld")
	public MessageBean helloworld() {
		return getFakeMessage();
	}

	@GetMapping()
	public MessageBean getName() {
		return messageService.getMessage();
	}

	@PostMapping("/store")
	public MessageBean postName() {
		log.info("Module A received a request.");
		MessageBean messageBean = messageService.postMessage();
		log.info("Module A returning the response: {}", messageBean);
		return messageBean;
	}

	@PostMapping("/queue")
	public MessageBean queue() {
		log.info("Module A received a request.");
		MessageBean messageBean = messageService.queueMessage();
		log.info("Module A returning the response: {}", messageBean);
		return messageBean;
	}

	@PostMapping("/publish")
	public MessageBean topic() {
		log.info("Module A received a request.");
		MessageBean messageBean = messageService.publishMessage();
		log.info("Module A returning the response: {}", messageBean);
		return messageBean;
	}

	@ExceptionHandler(WebClientResponseException.class)
	public String WebClientResponseException(WebClientResponseException e) {
		String errorMessage = String.format("WebClient Error: Status Code = %d, Error Message = %s", e.getStatusCode().value(), e.getResponseBodyAsString());
		log.error("Error from WebClient: {}", errorMessage);
		return errorMessage;
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
