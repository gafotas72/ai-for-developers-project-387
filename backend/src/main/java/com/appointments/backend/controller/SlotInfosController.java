package com.appointments.backend.controller;

import com.appointments.backend.dto.SlotInfoDto;
import com.appointments.backend.service.SlotInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for slot info.
 */
@RestController
public class SlotInfosController {

    private final SlotInfoService slotInfoService;

    /**
     * Constructor.
     *
     * @param slotInfoService slot info service
     */
    public SlotInfosController(final SlotInfoService slotInfoService) {
        this.slotInfoService = slotInfoService;
    }

    /**
     * List slot infos by owner.
     *
     * @param ownerId owner id
     * @param startTime start time filter
     * @param limit max results
     * @param offset page offset
     * @return list of slot infos
     */
    @GetMapping("/slot-infos")
    public ResponseEntity<List<SlotInfoDto>> listByOwner(
            @RequestParam(name = "owner_id") final Long ownerId,
            @RequestParam(name = "start_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    final LocalDate startTime,
            @RequestParam(required = false) final Integer limit,
            @RequestParam(required = false) final Integer offset) {
        int page = (offset != null) ? offset : 0;
        int size = (limit != null) ? limit : 20;
        var result = slotInfoService.listByOwner(ownerId, startTime, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }
}