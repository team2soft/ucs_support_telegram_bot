package com.ukrcarservice.UcsSupportBot.repository;

import com.ukrcarservice.UcsSupportBot.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
