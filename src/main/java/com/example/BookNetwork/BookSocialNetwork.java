package com.example.BookNetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
//for auditing listener to work

@EnableAsync
@SpringBootApplication
public class BookSocialNetwork {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialNetwork.class, args);
	}

}
