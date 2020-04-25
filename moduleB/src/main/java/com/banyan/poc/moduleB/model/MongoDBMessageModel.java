package com.banyan.poc.moduleB.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "messages")
public class MongoDBMessageModel {

  @Id
  private String id;

  @Field("character")
  private String backToTheFutureCharacter;

  @Field("quote")
  private String backToTheFutureQuote;

  // Use constructor to help prevent an object from being able to be set up in an invalid state.
  public MongoDBMessageModel(String character, String quote){
    this.backToTheFutureCharacter = character;
    this.backToTheFutureQuote = quote;
  }

}