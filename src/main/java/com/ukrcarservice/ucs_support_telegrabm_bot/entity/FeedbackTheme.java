package com.ukrcarservice.ucs_support_telegrabm_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedbackTheme {
    @Id
    @NotNull
    @Column(name = "theme_id")
    private Integer themeId;

    @Column(name = "feedback_theme")
    private String feedbackTheme;

    @Column(name = "placeholder")
    private String placeholder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeedbackTheme)) return false;

        FeedbackTheme that = (FeedbackTheme) o;

        return themeId.equals(that.themeId);
    }

    @Override
    public int hashCode() {
        return themeId.hashCode();
    }

    @Override
    public String toString() {
        return "FeedbackTheme{" +
                "themeId=" + themeId +
                ", feedbackTheme='" + feedbackTheme + '\'' +
                ", placeholder='" + placeholder + '\'' +
                '}';
    }
}
