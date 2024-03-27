package com.ukrcarservice.UcsSupportBot.repository;

import com.ukrcarservice.UcsSupportBot.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}