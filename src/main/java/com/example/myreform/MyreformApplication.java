package com.example.myreform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyreformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyreformApplication.class, args);
	}

}
