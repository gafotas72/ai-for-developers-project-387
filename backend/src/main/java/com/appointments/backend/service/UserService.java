package com.appointments.backend.service;

import com.appointments.backend.dto.UserDto;
import com.appointments.backend.entity.UserRole;
import com.appointments.backend.mapper.UserMapper;
import com.appointments.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Constructor.
     *
     * @param userRepository user repository
     * @param userMapper user mapper
     */
    public UserService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Get owner user.
     *
     * @return owner user DTO
     */
    public UserDto getOwner() {
        return userRepository.findByRole(UserRole.Owner)
                .map(userMapper::toDto)
                .orElse(null);
    }

    /**
     * Get guest user.
     *
     * @return guest user DTO
     */
    public UserDto getGuest() {
        return userRepository.findByRole(UserRole.Guest)
                .map(userMapper::toDto)
                .orElse(null);
    }
}