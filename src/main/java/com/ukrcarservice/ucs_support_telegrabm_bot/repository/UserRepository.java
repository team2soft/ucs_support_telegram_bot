package com.ukrcarservice.ucs_support_telegrabm_bot.repository;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
