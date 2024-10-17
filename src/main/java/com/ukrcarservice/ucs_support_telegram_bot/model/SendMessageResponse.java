package com.ukrcarservice.ucs_support_telegram_bot.model;

import lombok.Data;

@Data
public class SendMessageResponse {
    private Integer code;
    private String message;

    @Override
    public String toString() {
        return "SendMessageResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
