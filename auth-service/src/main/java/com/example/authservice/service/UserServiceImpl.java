package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        existing.ifPresent(it->{
            throw new UserAlreadyExistsException("user already exists username : " + user.getUsername());
        });

        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        log.info("created user : "+user.getUsername());



    }
}
