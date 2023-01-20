package com.example.myreform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing // Time 관련 엔티티 부분 사용을 위해서

@SpringBootApplication
@EnableJpaAuditing
public class MyreformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyreformApplication.class, args);
	}

}
