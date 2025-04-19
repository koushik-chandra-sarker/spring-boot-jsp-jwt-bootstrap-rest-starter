//package com.example.demo;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Component
//public class DataInitializer {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostConstruct
//    public void init() {
//        createUserIfNotExists("admin", "admin@example.com", "admin", "ROLE_ADMIN");
//        createUserIfNotExists("user", "user@example.com", "12345", "ROLE_USER");
//    }
//
//    private void createUserIfNotExists(String username, String email, String rawPassword, String role) {
//        if (userRepository.findByUsername(username).isEmpty()) {
//            User user = new User();
//            user.setUsername(username);
//            user.setEmail(email);
//            user.setPassword(passwordEncoder.encode(rawPassword));
//            user.setRoles(Collections.singletonList(role));
//            userRepository.save(user);
//            System.out.println("Created user: " + username + " with role: " + role);
//        }
//    }
//}
