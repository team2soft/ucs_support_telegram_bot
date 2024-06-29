package com.ukrcarservice.UcsSupportBot.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users", schema = "telegram")
public class User {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String userName;
    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return chatId.equals(user.chatId);
    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
