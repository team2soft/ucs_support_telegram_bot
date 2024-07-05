package com.ukrcarservice.UcsSupportBot.repository;

import com.ukrcarservice.UcsSupportBot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatIdOrderByMessageIdDesc(Long chatId);
    Message findFirstByChatIdOrderByMessageIdDesc(Long chatId);
}
