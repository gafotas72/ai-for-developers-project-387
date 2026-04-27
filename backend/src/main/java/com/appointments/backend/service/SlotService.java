package com.appointments.backend.service;

import com.appointments.backend.dto.SlotDto;
import com.appointments.backend.entity.EventType;
import com.appointments.backend.entity.Slot;
import com.appointments.backend.entity.SlotState;
import com.appointments.backend.mapper.SlotMapper;
import com.appointments.backend.repository.EventTypeRepository;
import com.appointments.backend.repository.SlotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Slot service.
 */
@Service
@Transactional
public class SlotService {

    private final SlotRepository slotRepository;
    private final EventTypeRepository eventTypeRepository;
    private final SlotMapper slotMapper;

    /**
     * Constructor.
     *
     * @param slotRepository slot repository
     * @param eventTypeRepository event type repository
     * @param slotMapper slot mapper
     */
    public SlotService(
            final SlotRepository slotRepository,
            final EventTypeRepository eventTypeRepository,
            final SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.slotMapper = slotMapper;
    }

    /**
     * Update slot.
     *
     * @param id slot id
     * @param dto slot data
     * @return updated slot
     */
    public SlotDto update(final Long id, final SlotDto dto) {
        if (id == 0L && dto.getState() == SlotState.Filled) {
            Slot entity = slotMapper.toEntity(dto);
            entity.setId(null);
            Slot saved = slotRepository.save(entity);
            return slotMapper.toDto(saved);
        }

        if (id != 0 && dto.getState() == SlotState.Free) {
            slotRepository.deleteById(id);
            SlotDto result = slotMapper.toDto(dto);
            result.setId(0L);
            return result;
        }

        return slotRepository.findById(id)
                .map(entity -> {
                    entity.setEventTypeId(dto.getEventTypeId());
                    entity.setGuestId(dto.getGuestId());
                    entity.setTitle(dto.getTitle());
                    entity.setStartTime(dto.getStartTime());
                    entity.setEndTime(dto.getEndTime());
                    entity.setState(dto.getState());
                    Slot saved = slotRepository.save(entity);
                    return slotMapper.toDto(saved);
                })
                .orElse(null);
    }

    /**
     * List slots by owner.
     *
     * @param ownerId owner id
     * @param startTime start time filter
     * @param pageable pageable
     * @return list of slot DTOs
     */
    public List<SlotDto> listByOwner(
            final Long ownerId,
            final LocalDate startTime,
            final Pageable pageable) {
        Page<Slot> slots = slotRepository.findByOwnerIdAndDate(ownerId, startTime, pageable);
        return slots.map(slotMapper::toDto).toList();
    }

    /**
     * List slots by event type.
     *
     * @param eventTypeId event type id
     * @param date date filter
     * @return list of slot DTOs
     */
    public List<SlotDto> listByEventType(final Long eventTypeId, final LocalDate date) {
        EventType eventType = eventTypeRepository.findById(eventTypeId).orElse(null);
        if (eventType == null) {
            return List.of();
        }

        Long ownerId = eventType.getUserId();
        List<Slot> existingSlots = slotRepository.findByOwnerIdAndExactDate(ownerId, date);

        List<SlotDto> result = new ArrayList<>();
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
        int duration = eventType.getDuration();

        LocalDateTime slotStart = dayStart;
        while (slotStart.plusMinutes(duration).isBefore(dayEnd) || slotStart.plusMinutes(duration).isEqual(dayEnd)) {
            LocalDateTime slotEnd = slotStart.plusMinutes(duration);

            SlotDto dto = new SlotDto();
            dto.setId(0L);
            dto.setEventTypeId(eventTypeId);
            dto.setGuestId(null);
            dto.setStartTime(slotStart);
            dto.setEndTime(slotEnd);
            dto.setState(SlotState.Free);

            for (Slot existing : existingSlots) {
                if (intervalsOverlap(slotStart, slotEnd, existing.getStartTime(), existing.getEndTime())) {
                    dto.setState(SlotState.Filled);
                    dto.setGuestId(existing.getGuestId());

                    if (existing.getEventTypeId() != null
                            && existing.getEventTypeId().equals(eventTypeId)
                            && existing.getStartTime().equals(slotStart)
                            && existing.getEndTime().equals(slotEnd)) {
                        dto.setId(existing.getId());
                        dto.setTitle(existing.getTitle());
                    }
                    break;
                }
            }

            result.add(dto);
            slotStart = slotEnd;
        }

        return result;
    }

    /**
     * Check if two time intervals overlap.
     *
     * @param start1 first interval start
     * @param end1 first interval end
     * @param start2 second interval start
     * @param end2 second interval end
     * @return true if intervals overlap
     */
    private boolean intervalsOverlap(
            final LocalDateTime start1,
            final LocalDateTime end1,
            final LocalDateTime start2,
            final LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}