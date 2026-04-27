package com.appointments.backend.mapper;

import com.appointments.backend.dto.UserDto;
import com.appointments.backend.entity.User;
import org.mapstruct.Mapper;

/**
 * User mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts entity to DTO.
     *
     * @param user the user
     * @return DTO
     */
    UserDto toDto(User user);

    /**
     * Converts DTO to entity.
     *
     * @param dto the DTO
     * @return entity
     */
    //TODO Check if we really need this
    User toEntity(UserDto dto);
}
