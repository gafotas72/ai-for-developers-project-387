package com.appointments.backend.repository;

import com.appointments.backend.entity.User;
import com.appointments.backend.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find by role.
     *
     * @param role user role
     * @return optional user
     */
    Optional<User> findByRole(UserRole role);
}