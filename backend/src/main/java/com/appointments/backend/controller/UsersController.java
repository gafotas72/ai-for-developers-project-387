package com.appointments.backend.controller;

import com.appointments.backend.dto.UserDto;
import com.appointments.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for users.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    /**
     * Constructor.
     *
     * @param userService user service
     */
    public UsersController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Get owner user.
     *
     * @return owner user
     */
    @GetMapping("/owner")
    public ResponseEntity<UserDto> getOwner() {
        UserDto owner = userService.getOwner();
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(owner);
    }

    /**
     * Get guest user.
     *
     * @return guest user
     */
    @GetMapping("/guest")
    public ResponseEntity<UserDto> getGuest() {
        UserDto guest = userService.getGuest();
        if (guest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(guest);
    }
}