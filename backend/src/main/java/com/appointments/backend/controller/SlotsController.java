package com.appointments.backend.controller;

import com.appointments.backend.dto.SlotDto;
import com.appointments.backend.service.SlotService;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for slots.
 */
@RestController
public class SlotsController {

    private final SlotService slotService;

    /**
     * Constructor.
     *
     * @param slotService slot service
     */
    public SlotsController(final SlotService slotService) {
        this.slotService = slotService;
    }

    /**
     * Update slot.
     *
     * @param id slot id
     * @param dto slot data
     * @return updated slot
     */
    @PutMapping("/slots/{id}")
    public ResponseEntity<SlotDto> update(
            @PathVariable final Long id,
            @RequestBody final SlotDto dto) {
        SlotDto updated = slotService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * List slots by owner.
     *
     * @param ownerId owner id
     * @param startTime start time filter
     * @param limit max results
     * @param offset page offset
     * @return list of slots
     */
    @GetMapping("/slots/by-owner")
    public ResponseEntity<List<SlotDto>> listByOwner(
            @RequestParam(name = "owner_id") final Long ownerId,
            @RequestParam(name = "start_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    final LocalDate startTime,
            @RequestParam(required = false) final Integer limit,
            @RequestParam(required = false) final Integer offset) {
        int page = (offset != null) ? offset : 0;
        int size = (limit != null) ? limit : 20;
        List<SlotDto> result = slotService.listByOwner(ownerId, startTime, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    /**
     * List slots by event type.
     *
     * @param eventTypeId event type id
     * @param date date filter
     * @return list of slots
     */
    @GetMapping("/slots/by-event-type")
    public ResponseEntity<List<SlotDto>> listByEventType(
            @RequestParam(name = "event_type_id") final Long eventTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
        var result = slotService.listByEventType(eventTypeId, date);
        return ResponseEntity.ok(result);
    }
}