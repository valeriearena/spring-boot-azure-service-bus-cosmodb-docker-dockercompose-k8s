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
 * @RestController is a convenience annotation that combines @Controller and @ResponseBody.
 * @Controller tells Spring that DispatcherServlet should map incoming requests to /message should be mapped to ModuleBRestController.
 * @ResponseBody tells Spring that the return value of handler methods should be written to the body of response.
 *
 * 	The following maps an HTTP request to /message to the ModuleBRestController.
 * 	@RequestMapping(path = "message", produces = "application/json")
 *
 * 	This is a shortcut:
 * 	@RequestMapping("/message")
 */
@Slf4j
@RestController
@RequestMapping("/message")
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
