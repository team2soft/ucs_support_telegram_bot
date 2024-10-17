package com.ukrcarservice.ucs_support_telegram_bot.controller;


import com.ukrcarservice.ucs_support_telegram_bot.model.SendMessageRequest;
import com.ukrcarservice.ucs_support_telegram_bot.model.SendMessageResponse;
import com.ukrcarservice.ucs_support_telegram_bot.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class MainController {

    private final TelegramBotService telegramBotService;

    @PostMapping(path = "/send-message")
    public SendMessageResponse sendMessage(@Valid @RequestBody SendMessageRequest sendMessageRequest){
        SendMessageResponse sendMessageResponse = new SendMessageResponse();
        try {
            telegramBotService.sendMessage(sendMessageRequest.getChatId(), sendMessageRequest.getText());
            sendMessageResponse.setCode(1);
            sendMessageResponse.setMessage("Message sent");
        }catch (Exception e){
            sendMessageResponse.setCode(0);
            sendMessageResponse.setMessage("Something went wrong. Message was not sent. Please try again later.");
        }

        return sendMessageResponse;
    }
}
