package com.example.demo.configuration;

import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
//                roles.add()
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("Admin.123"))
//                        .roles(roles)
                        .build();
                userRepository.save(user);

            }
        };

    }
}
