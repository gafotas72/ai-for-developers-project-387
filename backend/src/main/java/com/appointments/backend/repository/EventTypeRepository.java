package com.appointments.backend.repository;

import com.appointments.backend.entity.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Event type repository.
 */
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    /**
     * Find by user id.
     *
     * @param userId user id
     * @param pageable pageable
     * @return page of event types
     */
    Page<EventType> findByUserId(Long userId, Pageable pageable);
}