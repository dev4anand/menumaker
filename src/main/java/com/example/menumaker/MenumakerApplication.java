package com.example.menumaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.menumaker.model")
@EnableJpaRepositories("com.example.menumaker.repository")
public class MenumakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenumakerApplication.class, args);
	}

}
