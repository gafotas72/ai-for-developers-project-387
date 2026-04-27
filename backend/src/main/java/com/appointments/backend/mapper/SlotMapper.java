package com.appointments.backend.mapper;

import com.appointments.backend.dto.SlotDto;
import com.appointments.backend.entity.Slot;
import org.mapstruct.Mapper;

/**
 * Slot mapper.
 */
@Mapper(componentModel = "spring")
public interface SlotMapper {
    /**
     * Converts entity to DTO.
     *
     * @param entity the entity
     * @return DTO
     */
    SlotDto toDto(Slot entity);

    /**
     * Converts DTO to entity.
     *
     * @param dto the DTO
     * @return entity
     */
    Slot toEntity(SlotDto dto);

    /**
     * Copies DTO.
     *
     * @param dto the DTO
     * @return DTO
     */
    SlotDto toDto(SlotDto dto);
}