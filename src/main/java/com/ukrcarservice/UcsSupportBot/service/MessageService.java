package com.ukrcarservice.UcsSupportBot.service;

import com.ukrcarservice.UcsSupportBot.entity.Message;
import com.ukrcarservice.UcsSupportBot.repository.FeedbackThemeRepository;
import com.ukrcarservice.UcsSupportBot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> findAllByChatIdOrderByMessageIdDesc(Long chatId){
        return messageRepository.findAllByChatIdOrderByMessageIdDesc(chatId);
    }

    public Message findFirstByChatIdOrderByMessageIdDesc(Long chatId){
        return messageRepository.findFirstByChatIdOrderByMessageIdDesc(chatId);
    }

}
