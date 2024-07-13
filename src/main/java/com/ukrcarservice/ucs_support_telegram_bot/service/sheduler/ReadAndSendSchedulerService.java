package com.ukrcarservice.ucs_support_telegram_bot.service.sheduler;

import com.ukrcarservice.ucs_support_telegram_bot.entity.User;
import com.ukrcarservice.ucs_support_telegram_bot.repository.MessageUcsRepository;
import com.ukrcarservice.ucs_support_telegram_bot.repository.UserRepository;
import com.ukrcarservice.ucs_support_telegram_bot.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReadAndSendSchedulerService {

    private final UserRepository userRepository;
    private final MessageUcsRepository messageUcsRepository;
    private final TelegramBotService telegramBotService;

//    @Scheduled(cron = "${cron.read-and-send}")
    private void sendMessage() {
        log.info("Вычитываем сообщения");
        var messages = messageUcsRepository.findAll();
        var users = userRepository.findAll();
//        messages.forEach(message -> {
//            for (User user : users) {
////                telegramBotService.prepareAndSendMessage(user.getChatId(), message.getMessage());
//                telegramBotService.sendMessage(user.getChatId(), message.getText());
//            }
//        });
    }
}
