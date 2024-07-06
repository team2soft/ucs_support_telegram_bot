package com.ukrcarservice.ucs_support_telegrabm_bot.service;

import com.ukrcarservice.ucs_support_telegrabm_bot.config.BotConfig;
import com.ukrcarservice.ucs_support_telegrabm_bot.entity.FeedbackTheme;
import com.ukrcarservice.ucs_support_telegrabm_bot.entity.MessageUcs;
import com.ukrcarservice.ucs_support_telegrabm_bot.entity.User;
import com.ukrcarservice.ucs_support_telegrabm_bot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;

@Log4j2
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${employees.support}")
    List<Integer> employeesSupport;
    @Value("${employees.call-center}")
    List<Integer> employeesCallCenter;
    @Value("${ucs.support.team.telegram.chat.id}")
    String ucsSupportTeamTelegramChatId;
//    public static final String YES_BUTTON = "YES_BUTTON";
//    public static final String NO_BUTTON = "NO_BUTTON";
//    public static final String YES = "Yes";
//    public static final String NO = "No";
//    public static final String COMMAND_REGISTER = "/register";
//    public static final String COMMAND_HELP = "/help";
//    public static final String BTN_TEXT_HELP = "Інформація про цього бота";
//    public static final String BTN_TEXT_SETTINGS = "Налаштування користувача та бота";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong: ";
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_SEND = "/send";
    public static final String COMMAND_FEEDBACK = "/feedback";
    public static final String COMMAND_OPEN_CHAT = "/chat";
    public static final String BTN_TEXT_START = "Підключитися, перезавантажити бота";
    public static final String BTN_TEXT_FEEDBACK = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB Надіслати фідбек, задати питання, повідомити про проблему";
    public static final String BTN_TEXT_CLOSE_CHAT= "❌ Закрити чат";
    public static final String BTN_TEXT_OPEN_CHAT= "Почати чат за техпідтримкою";
    public static final String TEXT_CLOSE_CHAT= "Чат закритий";
    public static final String TEXT_OPEN_CHAT= "Будь ласка, залиште своє повідомлення.";
    public static final String TEXT_WELCOME_TO_SUPPORT_CHAT= "Ласкаво просимо до чату техпідтримки.";

    private final BotConfig config;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedbackThemeService feedbackThemeService;
    @Autowired
    private MessageUcsService messageService;
    @Autowired
    private UserService userService;

    public TelegramBotService(BotConfig config) {
        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand(COMMAND_START, BTN_TEXT_START));
//        commands.add(new BotCommand("/help", BTN_TEXT_HELP));
//        commands.add(new BotCommand("/settings", BTN_TEXT_SETTINGS));
        commands.add(new BotCommand(COMMAND_FEEDBACK, BTN_TEXT_FEEDBACK));
//        commands.add(new BotCommand(COMMAND_OPEN_CHAT, BTN_TEXT_OPEN_CHAT));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("onUpdateReceived(update)  : update={}", update);
        String language = "ua";
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if(update.getMessage().getFrom() != null && update.getMessage().getFrom().getLanguageCode() != null ){
                language = update.getMessage().getFrom().getLanguageCode();
            }
            if (update.getMessage().getText().equals(BTN_TEXT_FEEDBACK)) {
                prepareAndSendMessageFeedback(chatId, "Виберіть тему звернення", language.toLowerCase());
            } else if (update.getMessage().getText().equals(BTN_TEXT_CLOSE_CHAT)) {
                Long userId = update.getMessage().getChat().getId();
                closeChatWithUser(String.valueOf(userId));
            } else if (message.contains(COMMAND_SEND) /*&& config.getOwnerId() == chatId*/) {
                int index = message.indexOf(" ");
                String textToSend = message.substring(index < 0 ? 0 : index);
                Iterable<User> users = userRepository.findAll();
                users.forEach(u -> {
                    if(u.getChatId().longValue() == chatId){
//                        prepareAndSendMessage(chatId, textToSend);
                        sendMessage(chatId, textToSend);
                    }
                });
            } else {
                switch (message) {
                    case COMMAND_START:
                    case "/start@UkrCarServiceSupportBot":
                    case "/start@TestUkrCarServiceSupportBot":
                        registerUser(update.getMessage());
                        helloNewUser(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case COMMAND_OPEN_CHAT:
                        Long userId = update.getMessage().getChat().getId();
                        startNewChatWithUser(String.valueOf(userId), TEXT_OPEN_CHAT);
                        break;
//                    case COMMAND_HELP:
// //                        prepareAndSendMessage(chatId, "This is UCS support telegram bot.");
//                        sendMessage(chatId, "This is UCS support telegram bot.");
//                        break;
                    case COMMAND_FEEDBACK:
                        prepareAndSendMessageFeedback(chatId, "Виберіть тему звернення", language.toLowerCase());
                        break;
                    default:
                        MessageUcs messageUcs = messageService.findFirstByChatIdOrderByMessageIdDesc(chatId);
                        if (messageUcs != null && messageUcs.getFeedbackThemeId() != null && messageUcs.getText() == null) {
                            // есть подготовленное сообщение на тему фидбека без текста пользователя
                            // значит мы получили вопрос на эту тему
                            // - обновить сообщение
                            // - отправить email стандартный по фидбеку
                            // - ответить, что мы приняли вопрос и специалисты уже над ним работают
                            //   если в вопросе подразумевается ответ, то мы ответим максимально быстро
                            messageUcs.setText(message);
                            messageService.save(messageUcs);

                            this.sendMessageToUcsSupportTeam(language, message, chatId, messageUcs);

                        } else {
                            // нет подготовленных сообщение на тему фидбека без текста пользователя
                            // значит от пользователя пришел простой вопрос
                            // - отправить всем менеджерам, кто отвечает за телеграм (support - QA)
                            //   о том, что есть вопрос от какого-то юзера и надо тветить
                            // - ?? шаблонно ответить -
                            // - ?? если нерабочее время, то ответить
                            //   ?? Дякуємо за звернення, передаю інформацію нашому експерту, він зв'яжеться з Вами у робочий час нашого центру обслуговування клієнтів: 09:00 – 18:00 :)
                            MessageUcs messageUcsNew = new MessageUcs();
                            messageUcsNew.setChatId(chatId);
                            messageUcsNew.setText(message);
                            messageUcsNew.setUserIdFrom(chatId);
                            messageUcsNew.setUserIdTo(config.getBotId());
                            messageService.save(messageUcsNew);

                        }
//                        prepareAndSendMessage(chatId, "Command is not supported.");
//                        sendMessage(chatId, "Command is not supported.");
//                        if(openedChats.contains(chatId)){
//                            if(true) {
//                                break;
//                            }
//                            SendMessage message1 = new SendMessage();
//                            message1.setChatId(String.valueOf(chatId));
//                            message1.setText("Дякуємо за звернення. Підключаємо оператора.");
//
//                            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//                            // делает кнопку/рядок узким в одну строку
//                            keyboardMarkup.setResizeKeyboard(true);
//
//                            keyboardMarkup.setIsPersistent(true);
//                            // прячет кнопку/рядок после его нажатия
//                            // иначе прячется только вручную
//                            // true - отображается всегда
//                            keyboardMarkup.setOneTimeKeyboard(true);
//                            //        keyboardMarkup.setSelective(false);
//
//                            List<KeyboardRow> keyboardRows = new ArrayList<>();
//                            KeyboardRow row1 = new KeyboardRow();
//                            KeyboardButton inlineKeyboardButton = new KeyboardButton();
//                            inlineKeyboardButton.setText(BTN_TEXT_CLOSE_CHAT);
//                            row1.add(inlineKeyboardButton);
//                            keyboardRows.add(row1);
//
//                            keyboardMarkup.setKeyboard(keyboardRows);
//
//                            message1.setReplyMarkup(keyboardMarkup);
//
//                            try {
//                                execute(message1);
//                            } catch (TelegramApiException e) {
//                                log.error(SOMETHING_WENT_WRONG + e.getMessage());
//                            }
//                        }
                }
            }
        } else if (update.hasCallbackQuery()) {
            log.info("onUpdateReceived(Update update) : else if (update.hasCallbackQuery()) : {}", update.hasCallbackQuery());
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

//            Long senderId = update.getCallbackQuery().getMessage().getFrom().getId();
//            if (closedChats.contains(senderId)) {
//                return; // Игнорируем сообщения от пользователя
//            }
            if (callbackData.startsWith("feedbackThemeId")) {
                String[] callbackDataArr = callbackData.split("=");
                Integer feedbackThemeId = Integer.valueOf(callbackDataArr[1]);
                String lang = callbackDataArr[2];
                FeedbackTheme feedbackTheme = feedbackThemeService.getFeedbackThemeByThemeIdAndLocale(feedbackThemeId, lang);
                sendMessage(chatId,  EmojiParser.parseToUnicode(String.format("%s\n%s", feedbackTheme.getFeedbackTheme(), " :arrow_down:")));
                MessageUcs messageUcs = new MessageUcs();
                messageUcs.setChatId(chatId);
                messageUcs.setFeedbackThemeId(feedbackThemeId);
                messageUcs.setLang(lang);
                messageUcs.setUserIdFrom(chatId);
                messageUcs.setUserIdTo(update.getCallbackQuery().getMessage().getFrom().getId());
                messageService.save(messageUcs);
            }
        }
    }

    private void sendMessageToUcsSupportTeam(String language, String message, long chatId, MessageUcs messageUcs) {
        StringBuilder text2 = new StringBuilder();
        text2.append("Пользователь <b>https://t.me/");
        text2.append(userService.findById(chatId).getUserName());
        text2.append("</b> написал фидбек \n<i>Тема:</i> \n");
        text2.append("<b><i>");
        text2.append(replaceMarkdownText(feedbackThemeService.getFeedbackThemeByThemeIdAndLocale(messageUcs.getFeedbackThemeId(), language).getFeedbackTheme(), "HTML"));
        text2.append("</i></b> \n<i>Текст:</i> \n");
        text2.append("<b><i>");
        text2.append(replaceMarkdownText(message, "HTML"));
        text2.append("</i></b>");
        sendMarkdownMessageToUser(ucsSupportTeamTelegramChatId, text2.toString(), "HTML");
    }

    private String replaceMarkdownText(String income, String markdownType){
        switch(markdownType.toUpperCase()) {
            case "HTML":
//            case "MARKDOWN":
            default:
            return income.replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#039;")
                    ;
        }
    }

    // Метод для отправки сообщения пользователю
    public void sendMessageToUser(String userId, String messageText) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            message.setText(messageText);

            execute(message); // Отправка сообщения пользователю
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMarkdownMessageToUser(String userId, String messageText, String markdownType) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(userId);
            message.setText(messageText);
            message.setParseMode(markdownType);

            execute(message); // Отправка сообщения пользователю
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    // Добавьте метод для начала отдельного чата с пользователем
    public void startNewChatWithUser(String userId, String initialMessage) {
        // Начните чат, отправив пользователю первое сообщение
//        sendMessageToUser(userId, initialMessage);

        openedChats.add(Long.parseLong(userId));
        closedChats.remove(Long.parseLong(userId));

        SendMessage message = new SendMessage();
        message.setChatId(userId);

        message.setText(TEXT_WELCOME_TO_SUPPORT_CHAT);

        try {
            execute(message); // Отправка сообщения пользователю
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        message.setText(initialMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // делает кнопку/рядок узким в одну строку
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setIsPersistent(true);
        // прячет кнопку/рядок после его нажатия
        // иначе прячется только вручную
        // true - отображается всегда
        keyboardMarkup.setOneTimeKeyboard(true);
//        keyboardMarkup.setSelective(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton inlineKeyboardButton = new KeyboardButton();
        inlineKeyboardButton.setText(BTN_TEXT_CLOSE_CHAT);
        row1.add(inlineKeyboardButton);
        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message); // Отправка сообщения пользователю
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private Set<Long> closedChats = new HashSet<>();
    private Set<Long> openedChats = new HashSet<>();

    // Метод для игнорирования сообщений от пользователя
    public void closeChatWithUser(String userId) {
        long userIdLong = Long.parseLong(userId);
//        sendMessageToUser(userId, "Чат закрыт");

        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(TEXT_CLOSE_CHAT);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // делает кнопку/рядок узким в одну строку
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setIsPersistent(false);
        // прячет кнопку/рядок после его нажатия
        // иначе прячется только вручную
        // true - отображается всегда
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton inlineKeyboardButton = new KeyboardButton();
        inlineKeyboardButton.setText(BTN_TEXT_FEEDBACK);
        row1.add(inlineKeyboardButton);
        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message); // Отправка сообщения пользователю
            closedChats.add(userIdLong);
            openedChats.remove(userIdLong);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }



    /**
     * Выведения предложения выбрать тему обращения (фидбека)
     * и вывод списка возможных вариантов
     * @param chatId
     * @param textToSend
     */
    public void prepareAndSendMessageFeedback(long chatId, String textToSend, String language) {
        log.info("prepareAndSendMessageFeedback(long chatId, String textToSend) : chatId={} : textToSend={} : language={}", chatId, textToSend, language);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        // Создаем Inline кнопки для списка элементов
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<FeedbackTheme> feedbackThemes = feedbackThemeService.getFeedbackThemesByLocale(language);

        for(FeedbackTheme feedbackTheme : feedbackThemes) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            // Добавляем элементы списка
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(feedbackTheme.getFeedbackTheme());
            btn.setCallbackData("feedbackThemeId=" + feedbackTheme.getThemeId() + "=" + language.trim());
            rowInline.add(btn);
            rowsInline.add(0, rowInline);
        }
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        // Отправляем сообщение с Inline кнопками
        message.setReplyMarkup(inlineKeyboardMarkup);

        MessageUcs messageUcs = new MessageUcs();
        messageUcs.setChatId(chatId);
        messageUcs.setUserIdFrom(config.getBotId());
        messageUcs.setUserIdTo(chatId);
        messageUcs.setLang(language);
        messageUcs.setText(message.getText());
//        messageUcs.setCreateDttm(new Date());
        messageService.save(messageUcs);

        executeMessage(message);
    }

    /**
     * ?? Работа в редактируемым сообщением ??
     * @param text
     * @param chatId
     * @param messageId
     */
    private void executeEditMessageText(String text, Long chatId, Integer messageId) {
        log.info("executeEditMessageText(String text, Long chatId, Integer messageId) : chatId={} : messageId={} : text={}", chatId, messageId, text);
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

    /**
     * Отправка сообщения в чат по его ID
     * - вставляется меню кнопок
     * @param chatId
     * @param textToSend
     */
    public void sendMessage(long chatId, String textToSend) {
        log.info("sendMessage(long chatId, String textToSend) : chatId={} : textToSend={}", chatId, textToSend);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
//        message.setParseMode(ParseMode.MARKDOWN);
        executeMessage(message);
    }

    /**
     * Непосредственная отправка сообщения
     * @param message
     */
    private void executeMessage(SendMessage message) {
        log.info("executeMessage(SendMessage message) : message={}", message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(SOMETHING_WENT_WRONG + e.getMessage());
        }
    }

    /**
     * Метод, который делает приветствие новому пользователю
     * -- прим.: юзается EmojiParser.parseToUnicode(....)
     * @param chatId
     * @param firstName
     */
    private void helloNewUser(long chatId, String firstName) {
        log.info("helloNewUser(long chatId, String firstName) : chatId={} : firstName={}", chatId, firstName);
        String answer = "";
        if(firstName == null) {
            answer = EmojiParser.parseToUnicode(String.format("Hello! %s", " :relaxed:"));
        } else {
            answer = EmojiParser.parseToUnicode(String.format("Hello, %s! %s", firstName, " :relaxed:"));
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // делает кнопку/рядок узким в одну строку
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setIsPersistent(false);
        // прячет кнопку/рядок после его нажатия
        // иначе прячется только вручную
        // true - отображается всегда
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton inlineKeyboardButton = new KeyboardButton();
        inlineKeyboardButton.setText(BTN_TEXT_FEEDBACK);
        row1.add(inlineKeyboardButton);
        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        MessageUcs messageUcs = new MessageUcs();
        messageUcs.setChatId(chatId);
        messageUcs.setText(message.getText());
        messageUcs.setUserIdFrom(config.getBotId());
        messageUcs.setUserIdTo(chatId);
        messageService.save(messageUcs);

        executeMessage(message);
    }

    /**
     * Регистрация нового пользователя в нашей базе пользователей бота
     * - проверяет наличие, если НЕТ, то добавляет в базу
     * @param message
     */
    private void registerUser(Message message) {
        log.info("registerUser(MessageUcs message) : message={}", message);
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
            log.info("registerUser(MessageUcs message) : User saved: {}", saved);
        }
    }

//    /**
//     * НЕ ЮЗАЕМ (пока)
//     * подтверждение, хотят ли зарегаться
//     * @param chatId
//     */
//    private void register(long chatId) {
//        log.info("register(long chatId) : chatId={}", chatId);
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Do you really want to register?");
//
//        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//        List<InlineKeyboardButton> row = new ArrayList<>();
//        InlineKeyboardButton yesButton = new InlineKeyboardButton();
//        yesButton.setText(YES);
//        yesButton.setCallbackData(YES_BUTTON);
//
//        InlineKeyboardButton noButton = new InlineKeyboardButton();
//        noButton.setText(NO);
//        noButton.setCallbackData(NO_BUTTON);
//
//        row.add(yesButton);
//        row.add(noButton);
//
//        rows.add(row);
//
//        inlineMarkup.setKeyboard(rows);
//        message.setReplyMarkup(inlineMarkup);
//
//        executeMessage(message);
//    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

//    /**
//     * USE sendMessage(long chatId, String textToSend) as equal
//     * Подготовка и отправка сообщения
//     * @param chatId
//     * @param textToSend
//     */
//    public void prepareAndSendMessage(long chatId, String textToSend) {
//        log.info("prepareAndSendMessage(long chatId, String textToSend) : chatId={} : textToSend={}", chatId, textToSend);
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(textToSend);
//
//        executeMessage(message);
//    }
//    private SendMessage createMessageWithBtn(long chatId){
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//
////        // https://core.telegram.org/constructor/replyKeyboardMarkup
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//
//        // плейсхолдер - для одного запроса можем подставлять вариант начала текста
////        keyboardMarkup.setInputFieldPlaceholder("InputFieldPlaceholder = Вася");
//
//        // делает кнопку/рядок узким в одну строку
//        keyboardMarkup.setResizeKeyboard(true);
//
//        keyboardMarkup.setIsPersistent(false);
//        // прячет кнопку/рядок после его нажатия
//        // иначе прячется только вручную
//        // true - отображается всегда
//        keyboardMarkup.setOneTimeKeyboard(false);
//        keyboardMarkup.setSelective(true);
//
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow row1 = new KeyboardRow();
//        KeyboardButton inlineKeyboardButton = new KeyboardButton();
//        inlineKeyboardButton.setText(BTN_TEXT_FEEDBACK);
//        row1.add(inlineKeyboardButton);
//        keyboardRows.add(row1);
//
//        keyboardMarkup.setKeyboard(keyboardRows);
//
//        message.setReplyMarkup(keyboardMarkup);
//
//        return message;
//    }

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//        log.info("onUpdatesReceived(List<Update> updates)  : updates={}", updates);
//        for(Update update : updates) {
//            if (update.hasMessage() && update.getMessage().hasText()) {
//                String message = update.getMessage().getText();
//                long chatId = update.getMessage().getChatId();
//                if (update.getMessage().getText().equals(BTN_TEXT_FEEDBACK)) {
//                    prepareAndSendMessageFeedback(chatId, "Виберіть тему звернення");
//                } else if (message.contains(COMMAND_SEND) /*&& config.getOwnerId() == chatId*/) {
//                    int index = message.indexOf(" ");
//                    String textToSend = message.substring(index < 0 ? 0 : index);
//                    Iterable<User> users = userRepository.findAll();
//                    users.forEach(u -> {
//                        if (u.getChatId().longValue() == chatId) {
////                        prepareAndSendMessage(chatId, textToSend);
//                            sendMessage(chatId, textToSend);
//                        }
//                    });
//                } else {
//                    switch (message) {
//                        case COMMAND_START:
//                            registerUser(update.getMessage());
//                            helloNewUser(chatId, update.getMessage().getChat().getFirstName());
//                            break;
////                    case COMMAND_REGISTER:
////                        register(chatId);
////                        break;
////                    case COMMAND_HELP:
//// //                        prepareAndSendMessage(chatId, "This is UCS support telegram bot.");
////                        sendMessage(chatId, "This is UCS support telegram bot.");
////                        break;
//                        case COMMAND_FEEDBACK:
//                            prepareAndSendMessageFeedback(chatId, "Виберіть тему звернення");
//                            break;
//                        default:
////                        prepareAndSendMessage(chatId, "Command is not supported.");
//                            sendMessage(chatId, "Command is not supported.");
//                    }
//                }
//            } else if (update.hasCallbackQuery()) {
//                log.info("onUpdateReceived(Update update) : else if (update.hasCallbackQuery()) : {}", update.hasCallbackQuery());
//                String callbackData = update.getCallbackQuery().getData();
//                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
//                Long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//                if (callbackData.equals(YES_BUTTON)) {
//                    String text = "Pressed YES";
//                    executeEditMessageText(text, chatId, messageId);
//                } else if (callbackData.equals(NO_BUTTON)) {
//                    String text = "Pressed NO";
//                    executeEditMessageText(text, chatId, messageId);
//                }
//            }
//        }
//    }
}
