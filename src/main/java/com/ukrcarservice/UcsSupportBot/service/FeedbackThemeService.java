package com.ukrcarservice.UcsSupportBot.service;

import com.ukrcarservice.UcsSupportBot.entity.FeedbackTheme;
import com.ukrcarservice.UcsSupportBot.repository.FeedbackThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class FeedbackThemeService {

    private final FeedbackThemeRepository feedbackThemeRepository;

    public List<FeedbackTheme> getFeedbackThemesByLocale(String locale){
        return feedbackThemeRepository.findFeedbackThemesByLocale(locale);
    }

    public FeedbackTheme getFeedbackThemeByThemeIdAndLocale(Integer themeId, String locale){
        return feedbackThemeRepository.findFeedbackThemeByThemeIdAndLocale(themeId, locale);
    }

}
