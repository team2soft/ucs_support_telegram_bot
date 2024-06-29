package com.ukrcarservice.UcsSupportBot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "messages", schema = "telegram")
@JsonIgnoreProperties
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id", updatable = false, nullable = false)
    private Long chatId;
    @Column(name = "message")
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return chatId.equals(message.chatId);
    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }


}
