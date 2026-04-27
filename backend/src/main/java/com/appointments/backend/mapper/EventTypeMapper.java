package com.appointments.backend.mapper;

import com.appointments.backend.dto.EventTypeDto;
import com.appointments.backend.entity.EventType;
import org.mapstruct.Mapper;

/**
 * Event type mapper.
 */
@Mapper(componentModel = "spring")
public interface EventTypeMapper {
    /**
     * Converts entity to DTO.
     *
     * @param entity the entity
     * @return DTO
     */
    EventTypeDto toDto(EventType entity);

    /**
     * Converts DTO to entity.
     *
     * @param dto the DTO
     * @return entity
     */
    EventType toEntity(EventTypeDto dto);
}