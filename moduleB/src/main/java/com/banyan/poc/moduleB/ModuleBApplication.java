package com.banyan.poc.moduleB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringApplication starts the Spring Boot application. The spring container gets started once SpringApplication.run() method is called.
 * @SpringBootApplication is a combination of @Configuration + @ComponentScan + @EnableAutoConfiguration.
 *
 * When the main method runs following steps occur:
 * 1. Application Context is started.
 * 2. Using application context autodiscovery occurs: @ComponentScan
 * 3. All default configurations are set up based on dependencies, properties, and annotations.
 * 4. An embedded Tomcat servlet container is started if spring-web is listed as a dependency and the spring-web module JAR is present on the classpath.
 *
 * SpringApplication::run -> SpringApplication::refreshContext -> SpringApplication::refresh -> AbstractApplicationContext::refresh ->
 *      ServletWebServerApplicationContext::finishRefresh -> ServletWebServerApplicationContext::startWebServer -> TomcatWebServer::start
 */
@SpringBootApplication
public class ModuleBApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleBApplication.class, args);
    }
}
