package com.appointments.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Slot entity.
 */
@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type_id")
    private Long eventTypeId;

    @Column(name = "guest_id")
    private Long guestId;

    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotState state;

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
     * Gets event type id.
     *
     * @return event type id
     */
    public Long getEventTypeId() {
        return eventTypeId;
    }

    /**
     * Sets event type id.
     *
     * @param eventTypeId the event type id
     */
    public void setEventTypeId(final Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    /**
     * Gets guest id.
     *
     * @return guest id
     */
    public Long getGuestId() {
        return guestId;
    }

    /**
     * Sets guest id.
     *
     * @param guestId the guest id
     */
    public void setGuestId(final Long guestId) {
        this.guestId = guestId;
    }

    /**
     * Gets title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.title = title;
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

    /**
     * Gets state.
     *
     * @return state
     */
    public SlotState getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(final SlotState state) {
        this.state = state;
    }
}