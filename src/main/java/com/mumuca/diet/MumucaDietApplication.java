package com.mumuca.diet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.mumuca.diet")
@EnableCaching
public class MumucaDietApplication {
	public static void main(String[] args) {
		SpringApplication.run(MumucaDietApplication.class, args);
	}
}
