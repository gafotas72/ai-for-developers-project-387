package com.appointments.backend.service;

import com.appointments.backend.dto.EventTypeDto;
import com.appointments.backend.entity.EventType;
import com.appointments.backend.mapper.EventTypeMapper;
import com.appointments.backend.repository.EventTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Event type service.
 */
@Service
@Transactional
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventTypeMapper eventTypeMapper;

    /**
     * Constructor.
     *
     * @param eventTypeRepository event type repository
     * @param eventTypeMapper event type mapper
     */
    public EventTypeService(
            final EventTypeRepository eventTypeRepository,
            final EventTypeMapper eventTypeMapper) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeMapper = eventTypeMapper;
    }

    /**
     * List event types.
     *
     * @param userId user id filter
     * @param pageable pageable
     * @return list of event type DTOs
     */
    public List<EventTypeDto> list(final Long userId, final Pageable pageable) {
        Page<EventType> eventTypes = (userId != null)
                ? eventTypeRepository.findByUserId(userId, pageable)
                : eventTypeRepository.findAll(pageable);
        return eventTypes.map(eventTypeMapper::toDto).toList();
    }

    /**
     * Get event type by id.
     *
     * @param id event type id
     * @return event type DTO or null
     */
    public EventTypeDto getById(final Long id) {
        return eventTypeRepository.findById(id)
                .map(eventTypeMapper::toDto)
                .orElse(null);
    }

    /**
     * Create event type.
     *
     * @param dto event type data
     * @return created event type
     */
    public EventTypeDto create(final EventTypeDto dto) {
        EventType entity = eventTypeMapper.toEntity(dto);
        entity.setId(null);
        EventType saved = eventTypeRepository.save(entity);
        return eventTypeMapper.toDto(saved);
    }

    /**
     * Update event type.
     *
     * @param id event type id
     * @param dto event type data
     * @return updated event type or null
     */
    public EventTypeDto update(final Long id, final EventTypeDto dto) {
        return eventTypeRepository.findById(id)
                .map(entity -> {
                    entity.setUserId(dto.getUserId());
                    entity.setTitle(dto.getTitle());
                    entity.setDescription(dto.getDescription());
                    entity.setDuration(dto.getDuration());
                    EventType saved = eventTypeRepository.save(entity);
                    return eventTypeMapper.toDto(saved);
                })
                .orElse(null);
    }

    /**
     * Delete event type.
     *
     * @param id event type id
     * @return true if deleted
     */
    public boolean delete(final Long id) {
        if (eventTypeRepository.existsById(id)) {
            eventTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}