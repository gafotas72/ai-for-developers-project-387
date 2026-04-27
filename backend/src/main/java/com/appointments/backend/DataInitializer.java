package com.appointments.backend;

import com.appointments.backend.entity.User;
import com.appointments.backend.entity.UserRole;
import com.appointments.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Data initializer for default users.
 */
@Configuration
public class DataInitializer {

    /**
     * Initializes default data.
     *
     * @param userRepository user repository
     * @return command line runner
     */
    @Bean
    public CommandLineRunner initData(final UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User owner = new User();
                owner.setName("Owner");
                owner.setRole(UserRole.Owner);
                userRepository.save(owner);

                User guest = new User();
                guest.setName("Guest");
                guest.setRole(UserRole.Guest);
                userRepository.save(guest);
            }
        };
    }
}