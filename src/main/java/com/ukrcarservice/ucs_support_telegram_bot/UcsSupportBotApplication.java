package com.ukrcarservice.ucs_support_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UcsSupportBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(UcsSupportBotApplication.class, args);
	}

}
