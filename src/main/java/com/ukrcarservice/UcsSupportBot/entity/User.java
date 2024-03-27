package com.ukrcarservice.UcsSupportBot.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
}
