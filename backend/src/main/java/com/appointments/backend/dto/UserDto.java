package com.appointments.backend.dto;

import com.appointments.backend.entity.UserRole;

/**
 * User DTO.
 */
public class UserDto {
    private Long id;
    private String name;
    private UserRole role;

    /**
     * Gets id.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets role.
     *
     * @return role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(final UserRole role) {
        this.role = role;
    }
}