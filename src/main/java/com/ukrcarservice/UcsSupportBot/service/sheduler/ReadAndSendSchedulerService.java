package com.ukrcarservice.UcsSupportBot.service.sheduler;

import com.ukrcarservice.UcsSupportBot.entity.User;
import com.ukrcarservice.UcsSupportBot.repository.MessageRepository;
import com.ukrcarservice.UcsSupportBot.repository.UserRepository;
import com.ukrcarservice.UcsSupportBot.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReadAndSendSchedulerService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TelegramBotService telegramBotService;

//    @Scheduled(cron = "${cron.read-and-send}")
    private void sendMessage() {
        log.info("Вычитываем сообщения");
        var messages = messageRepository.findAll();
        var users = userRepository.findAll();
        messages.forEach(message -> {
            for (User user : users) {
//                telegramBotService.prepareAndSendMessage(user.getChatId(), message.getMessage());
                telegramBotService.sendMessage(user.getChatId(), message.getMessage());
            }
        });
    }
}
