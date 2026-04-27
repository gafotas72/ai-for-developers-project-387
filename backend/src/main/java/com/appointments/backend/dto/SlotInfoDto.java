package com.appointments.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Slot info DTO.
 */
public class SlotInfoDto {
    private Long id;
    @JsonProperty("event_title")
    private String eventTitle;
    @JsonProperty("guest_name")
    private String guestName;
    @JsonProperty("slot_title")
    private String slotTitle;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;

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
     * Gets event title.
     *
     * @return event title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Sets event title.
     *
     * @param eventTitle the event title
     */
    public void setEventTitle(final String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Gets guest name.
     *
     * @return guest name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Sets guest name.
     *
     * @param guestName the guest name
     */
    public void setGuestName(final String guestName) {
        this.guestName = guestName;
    }

    /**
     * Gets slot title.
     *
     * @return slot title
     */
    public String getSlotTitle() {
        return slotTitle;
    }

    /**
     * Sets slot title.
     *
     * @param slotTitle the slot title
     */
    public void setSlotTitle(final String slotTitle) {
        this.slotTitle = slotTitle;
    }

    /**
     * Gets start time.
     *
     * @return start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(final LocalDateTime endTime) {
        this.endTime = endTime;
    }
}