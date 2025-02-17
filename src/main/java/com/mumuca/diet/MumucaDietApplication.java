package com.mumuca.diet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.mumuca.diet")
@EnableCaching
@EnableJpaAuditing
public class MumucaDietApplication {
	public static void main(String[] args) {
		SpringApplication.run(MumucaDietApplication.class, args);
	}
}
