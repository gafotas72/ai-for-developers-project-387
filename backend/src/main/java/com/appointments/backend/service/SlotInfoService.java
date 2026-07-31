package com.appointments.backend.service;

import com.appointments.backend.dto.SlotInfoDto;
import com.appointments.backend.entity.User;
import com.appointments.backend.entity.Slot;
import com.appointments.backend.entity.EventType;
import com.appointments.backend.repository.EventTypeRepository;
import com.appointments.backend.repository.SlotRepository;
import com.appointments.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Slot info service.
 */
@Service
@Transactional(readOnly = true)
public class SlotInfoService {

    private final SlotRepository slotRepository;
    private final EventTypeRepository eventTypeRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param slotRepository slot repository
     * @param eventTypeRepository event type repository
     * @param userRepository user repository
     */
    public SlotInfoService(
            final SlotRepository slotRepository,
            final EventTypeRepository eventTypeRepository,
            final UserRepository userRepository) {
        this.slotRepository = slotRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.userRepository = userRepository;
    }

    /**
     * List slot infos by owner.
     *
     * @param ownerId owner id
     * @param startTime start time filter
     * @param pageable pageable
     * @return list of slot info DTOs
     */
    public List<SlotInfoDto> listByOwner(
            final Long ownerId,
            final LocalDateTime startTime,
            final Pageable pageable) {
        Page<Slot> slots = slotRepository.findByOwnerIdAndDate(ownerId, startTime, pageable);
        return slots.stream().map(slot -> {
            SlotInfoDto dto = new SlotInfoDto();
            dto.setId(slot.getId());
            dto.setSlotTitle(slot.getTitle());
            dto.setStartTime(slot.getStartTime());
            dto.setEndTime(slot.getEndTime());

            EventType eventType = eventTypeRepository.findById(slot.getEventTypeId()).orElse(null);
            if (eventType != null) {
                dto.setEventTitle(eventType.getTitle());
            }

            if (slot.getGuestId() != null) {
                User guest = userRepository.findById(slot.getGuestId()).orElse(null);
                if (guest != null) {
                    dto.setGuestName(guest.getName());
                }
            }

            return dto;
        }).toList();
    }
}