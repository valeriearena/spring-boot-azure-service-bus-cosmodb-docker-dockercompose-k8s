package com.banyan.poc.moduleB.controller;

import com.banyan.poc.moduleB.bean.MessageBean;
import com.banyan.poc.moduleB.service.MessageService;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController Tells Spring that all handler methods should have return value written to the body of response.
 */
@Slf4j
@RestController
@RequestMapping(path = "message", produces = "application/json")
public class ModuleBRestController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/helloworld")
	public MessageBean helloworld() {
		return getFakeMessage();
	}

	@GetMapping()
	public MessageBean getMessage() {
		log.info("Module B received a request from Module A.");
		MessageBean messageBean = getFakeMessage();
		messageService.save(messageBean);
		return messageBean;
	}

	@PostMapping()
	public MessageBean postMessage() {
		log.info("Module B received a request from Module A.");
		MessageBean messageBean = getFakeMessage();
		messageService.save(messageBean);
		return messageBean;
	}

	@ExceptionHandler({DataAccessException.class})
	public String handleException(DataAccessException e) {
		String errorMessage = String.format("Database Error: Error Message = %s", e.getMessage());
		log.error(e.getMessage(), e);
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
