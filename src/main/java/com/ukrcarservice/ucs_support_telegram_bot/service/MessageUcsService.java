package com.ukrcarservice.ucs_support_telegram_bot.service;

import com.ukrcarservice.ucs_support_telegram_bot.entity.MessageUcs;
import com.ukrcarservice.ucs_support_telegram_bot.repository.MessageUcsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class MessageUcsService {

    private final MessageUcsRepository messageUcsRepository;

    public List<MessageUcs> findAllByChatIdOrderByMessageIdDesc(Long chatId){
        return messageUcsRepository.findAllByChatIdOrderByMessageIdDesc(chatId);
    }

    public MessageUcs findFirstByChatIdOrderByMessageIdDesc(Long chatId){
        return messageUcsRepository.findFirstByChatIdOrderByMessageIdDesc(chatId);
    }

    public MessageUcs save(MessageUcs messageUcs){
        if(messageUcs.getMessageId() == null && messageUcs.getCreateDttm() == null){
            messageUcs.setCreateDttm(new Date());
        }
        return messageUcsRepository.save(messageUcs);
    }


}
