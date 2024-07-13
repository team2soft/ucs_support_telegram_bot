package com.ukrcarservice.ucs_support_telegram_bot.repository;

import com.ukrcarservice.ucs_support_telegram_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
