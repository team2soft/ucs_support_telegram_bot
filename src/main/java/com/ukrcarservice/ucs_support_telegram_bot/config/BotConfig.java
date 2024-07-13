package com.ukrcarservice.ucs_support_telegram_bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
//@PropertySource("application.properties")
@Data
public class BotConfig {
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    @Value("${bot.id}")
    Long botId;

//    @Value("${bot.owner}")
//    Long ownerId;
}
