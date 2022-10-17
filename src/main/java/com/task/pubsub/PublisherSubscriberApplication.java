package com.task.pubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Publisher - Subscriber application.
 */
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class PublisherSubscriberApplication {
	public static void main(String[] args) {
		SpringApplication.run(PublisherSubscriberApplication.class, args);
	}
}
