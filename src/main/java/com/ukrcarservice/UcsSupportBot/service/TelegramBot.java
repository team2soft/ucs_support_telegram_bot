package com.ukrcarservice.UcsSupportBot.service;

import com.ukrcarservice.UcsSupportBot.config.BotConfig;
import com.ukrcarservice.UcsSupportBot.entity.User;
import com.ukrcarservice.UcsSupportBot.repository.MessageRepository;
import com.ukrcarservice.UcsSupportBot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    public static final String YES_BUTTON = "YES_BUTTON";
    public static final String NO_BUTTON = "NO_BUTTON";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong: ";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_REGISTER = "/register";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_SEND = "/send";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
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

            if (message.contains(COMMAND_SEND) && config.getOwnerId() == chatId) {
                String textToSend = message.substring(message.indexOf(" "));
                Iterable<User> users = userRepository.findAll();
                users.forEach(u -> prepareAndSendMessage(chatId, textToSend));
            } else {
                switch (message) {
                    case COMMAND_START:
                        registerUser(update.getMessage());
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case COMMAND_REGISTER:
                        register(chatId);
                        break;
                    case COMMAND_HELP:
                        prepareAndSendMessage(chatId, "This is UCS support telegram bot.");
                        break;
                    default:
                        prepareAndSendMessage(chatId, "Command is not supported.");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "Pressed YES";
                executeEditMessageText(text, chatId, messageId);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Pressed NO";
                executeEditMessageText(text, chatId, messageId);
            }
        }
    }

    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you really want to register?");

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText(YES);
        yesButton.setCallbackData(YES_BUTTON);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText(NO);
        noButton.setCallbackData(NO_BUTTON);

        row.add(yesButton);
        row.add(noButton);

        rows.add(row);

        inlineMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineMarkup);

        executeMessage(message);
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();

            User saved = userRepository.save(
                    User.builder()
                            .chatId(chatId)
                            .userName(chat.getUserName())
                            .firstName(chat.getFirstName())
                            .lastName(chat.getLastName())
                            .registeredAt(new Timestamp(System.currentTimeMillis()))
                            .build()
            );
            log.info("User saved: {}", saved);
        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = EmojiParser.parseToUnicode(String.format("Hello, %s! %s", firstName, ":relaxed:"));
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Register");
        row1.add("Show my data");
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add("One");
        row2.add("Two");
        row2.add("Three");
        keyboardRows.add(row2);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    private void executeEditMessageText(String text, Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId(messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(SOMETHING_WENT_WRONG + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(SOMETHING_WENT_WRONG + e.getMessage());
        }
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void sendMessage() {
        var messages = messageRepository.findAll();
        var users = userRepository.findAll();
        messages.forEach(message -> {
            for (User user : users) {
                prepareAndSendMessage(user.getChatId(), message.getMessage());
            }
        });
    }
}
