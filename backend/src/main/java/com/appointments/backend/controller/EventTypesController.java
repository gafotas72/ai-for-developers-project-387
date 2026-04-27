package com.appointments.backend.controller;

import com.appointments.backend.dto.EventTypeDto;
import com.appointments.backend.service.EventTypeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for event types.
 */
@RestController
public class EventTypesController {

    private final EventTypeService eventTypeService;

    /**
     * Constructor.
     *
     * @param eventTypeService event type service
     */
    public EventTypesController(final EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    /**
     * List event types.
     *
     * @param userId user id filter
     * @param limit max results
     * @param offset page offset
     * @return list of event types
     */
    @GetMapping("/event-types")
    public ResponseEntity<List<EventTypeDto>> list(
            @RequestParam(name = "user_id", required = false) final Long userId,
            @RequestParam(required = false) final Integer limit,
            @RequestParam(required = false) final Integer offset) {
        int page = (offset != null) ? offset : 0;
        int size = (limit != null) ? limit : 20;
        List<EventTypeDto> result = eventTypeService.list(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    /**
     * Get event type by id.
     *
     * @param id event type id
     * @return event type
     */
    @GetMapping("/event-types/{id}")
    public ResponseEntity<EventTypeDto> read(@PathVariable final Long id) {
        EventTypeDto eventType = eventTypeService.getById(id);
        if (eventType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventType);
    }

    /**
     * Create event type.
     *
     * @param dto event type data
     * @return created event type
     */
    @PostMapping("/event-types")
    public ResponseEntity<EventTypeDto> create(@RequestBody final EventTypeDto dto) {
        EventTypeDto created = eventTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update event type.
     *
     * @param id event type id
     * @param dto event type data
     * @return updated event type
     */
    @PutMapping("/event-types/{id}")
    public ResponseEntity<EventTypeDto> update(
            @PathVariable final Long id,
            @RequestBody final EventTypeDto dto) {
        EventTypeDto updated = eventTypeService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete event type.
     *
     * @param id event type id
     * @return no content
     */
    @DeleteMapping("/event-types/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        boolean deleted = eventTypeService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}