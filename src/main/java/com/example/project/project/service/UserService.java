package com.example.project.project.service;

import com.example.project.project.exception.ExistingUsernameException;
import com.example.project.project.model.User;
import com.example.project.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public int sum(final int val1, final int val2){
        log.info("sum {} and {} ", val1, val2);
        return val1 + val2;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("looking up user: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public User createNewAccount(final User user) throws ExistingUsernameException {

        if(userRepository.existsByUsername(user.getUsername())) {
            throw new ExistingUsernameException("Existing username");
        }

        log.info("Saving new Account: username: {}, password: {}, Email: {} ", user.getUsername(), user.getPassword(), user.getEmail());
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved = userRepository.save(user);
        userSaved.setPassword(password);

        return userSaved;
    }
}