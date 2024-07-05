package com.ukrcarservice.ucs_support_telegrabm_bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "messages", schema = "telegram")
@JsonIgnoreProperties
public class MessageUcs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", updatable = false, nullable = false)
    private Long messageId;
    @Column(name = "create_dttm")
    private Date createDttm;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "user_id_from")
    private Long userIdFrom;
    @Column(name = "user_id_to")
    private Long userIdTo;
    @Column(name = "feedback_theme_id")
    private Integer feedbackThemeId;
    @Column(name = "lang")
    private String lang;
    @Column(name = "text")
    private String text;

    // ссылка на объект Employee
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private User chat;
    @ManyToOne
    @JoinColumn(name = "user_id_from", referencedColumnName = "chat_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private User userFrom;
    @ManyToOne
    @JoinColumn(name = "user_id_to", referencedColumnName = "chat_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private User userTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageUcs)) return false;

        MessageUcs that = (MessageUcs) o;

        return messageId.equals(that.messageId);
    }

    @Override
    public int hashCode() {
        return messageId.hashCode();
    }
}
