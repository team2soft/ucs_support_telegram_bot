package com.ukrcarservice.UcsSupportBot.service;

import com.ukrcarservice.UcsSupportBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "welcome message"));
        commands.add(new BotCommand("/help", "how to use this bot"));
        commands.add(new BotCommand("/settings", "set your preferences"));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, "This is UCS support telegram bot.");
                    break;
                default:
                    sendMessage(chatId, "Command is not supported.");
            }
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = String.format("Hello, %s!", firstName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Something went wrong: " + e.getMessage());
        }
    }
}
