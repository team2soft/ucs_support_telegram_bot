package com.ukrcarservice.UcsSupportBot.repository;

import com.ukrcarservice.UcsSupportBot.entity.FeedbackTheme;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackThemeRepository extends JpaRepository<FeedbackTheme, Integer> {
    @Cacheable(cacheNames="FeedbackThemesByLocale", key = "#locale")
    @Query(value = " select ft.theme_id as theme_id,                      " +
                   "   case when :locale = 'ru' then ft.feedback_theme_ru " +
                   "        when :locale = 'en' then ft.feedback_theme_en " +
                   "        else  ft.feedback_theme_ua                    " + // добавьте аналогичные кейсы для других языков
                   "    end as feedback_theme,                            " +
                   "   case when :locale = 'ru' then ft.placeholder_ru    " +
                   "        when :locale = 'en' then ft.placeholder_en    " +
                   "        else ft.placeholder_ua                        " + // добавьте аналогичные кейсы для других языков
                   "   end as placeholder                                 " +
                   "  from ukrcarservice.feedback_theme_s1 ft             " +
                   "  order by ft.theme_id desc                            ", nativeQuery = true)
    List<FeedbackTheme> findFeedbackThemesByLocale(@Param("locale") String locale);

    @Cacheable(cacheNames="FeedbackThemeByThemeIdAndLocale", key = "#themeId + '_' + #locale")
    @Query(value =  " select ft.theme_id as theme_id,                      " +
                    "   case when :locale = 'ru' then ft.feedback_theme_ru " +
                    "        when :locale = 'en' then ft.feedback_theme_en " +
                    "        else  ft.feedback_theme_ua                    " + // добавьте аналогичные кейсы для других языков
                    "    end as feedback_theme,                            " +
                    "   case when :locale = 'ru' then ft.placeholder_ru    " +
                    "        when :locale = 'en' then ft.placeholder_en    " +
                    "        else ft.placeholder_ua                        " + // добавьте аналогичные кейсы для других языков
                    "   end as placeholder                                 " +
                    "  from ukrcarservice.feedback_theme_s1 ft             " +
                    " where ft.theme_id = :themeId                         ", nativeQuery = true)
    FeedbackTheme findFeedbackThemeByThemeIdAndLocale(@Param("themeId") Integer themeId, @Param("locale") String locale);
}
