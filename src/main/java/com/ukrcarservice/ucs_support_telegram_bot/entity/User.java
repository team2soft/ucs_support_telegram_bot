package com.ukrcarservice.ucs_support_telegram_bot.entity;

import lombok.*;

import javax.persistence.*;
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
    @Column(name = "email_usc")
    private String emailUsc;
    @Column(name = "phone_usc")
    private String phoneUsc;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Column(name = "registered_at")
    private Timestamp registeredAt;

    // ссылка на объект Employee
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private Employee employee;


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
