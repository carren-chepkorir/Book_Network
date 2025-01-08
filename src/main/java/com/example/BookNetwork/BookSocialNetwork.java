package com.example.BookNetwork;

import com.example.BookNetwork.role.RoleRepository;
import com.example.BookNetwork.role.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//for auditing listener to work

@EnableAsync
@SpringBootApplication
public class BookSocialNetwork {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialNetwork.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository){
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(
						Roles.builder()
								.name("USER").build()
				);
			}
		};
	}

}
