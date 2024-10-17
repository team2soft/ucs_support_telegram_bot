package com.ukrcarservice.ucs_support_telegram_bot.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendMessageRequest {
    @NotNull(message = "chatId cannot be null")
    private Long chatId;
    @NotBlank(message = "text cannot be empty")
    private String text;

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "chatId=" + chatId +
                ", text='" + text + '\'' +
                '}';
    }
}
