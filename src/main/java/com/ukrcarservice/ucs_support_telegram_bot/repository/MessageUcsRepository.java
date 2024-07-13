package com.ukrcarservice.ucs_support_telegram_bot.repository;

import com.ukrcarservice.ucs_support_telegram_bot.entity.MessageUcs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageUcsRepository extends JpaRepository<MessageUcs, Long> {

    List<MessageUcs> findAllByChatIdOrderByMessageIdDesc(Long chatId);
    MessageUcs findFirstByChatIdOrderByMessageIdDesc(Long chatId);
}
