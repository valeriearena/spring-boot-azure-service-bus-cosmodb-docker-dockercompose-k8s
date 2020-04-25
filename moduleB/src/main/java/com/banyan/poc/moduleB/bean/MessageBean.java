package com.banyan.poc.moduleB.bean;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MessageBean implements Serializable {

	private String backToTheFutureCharacter;
	private String backToTheFutureQuote;

}
