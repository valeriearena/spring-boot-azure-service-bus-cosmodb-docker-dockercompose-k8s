package com.banyan.poc.moduleA.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MessageBean implements Serializable {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String backToTheFutureCharacter;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String backToTheFutureQuote;

}
