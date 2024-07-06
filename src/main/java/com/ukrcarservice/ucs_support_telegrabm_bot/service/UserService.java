package com.ukrcarservice.ucs_support_telegrabm_bot.service;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.User;
import com.ukrcarservice.ucs_support_telegrabm_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userid){
        Optional<User> user = userRepository.findById(userid);
        return user.isPresent() ? user.get() : null;
    }
}
