package com.appointments.backend.repository;

import com.appointments.backend.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

/**
 * Slot repository.
 */
public interface SlotRepository extends JpaRepository<Slot, Long> {

    /**
     * Find by event type id and date.
     *
     * @param eventTypeId event type id
     * @param date date
     * @return list of slots
     */
    @Query(value = "SELECT * FROM slots WHERE event_type_id = :eventTypeId AND CAST(start_time AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    List<Slot> findByEventTypeIdAndDate(@Param("eventTypeId") Long eventTypeId, @Param("date") LocalDate date);

    /**
     * Find by owner id and date.
     *
     * @param ownerId owner id
     * @param startDate start date
     * @param pageable pageable
     * @return page of slots
     */
    @Query(value = "SELECT s.* FROM slots s INNER JOIN event_types e ON s.event_type_id = e.id WHERE e.user_id = :ownerId AND s.start_time >= NOW()", nativeQuery = true)
    Page<Slot> findByOwnerIdAndDate(@Param("ownerId") Long ownerId, @Param("startDate") LocalDate startDate, Pageable pageable);

    /**
     * Find by owner id and date.
     *
     * @param ownerId owner id
     * @param startDate start date
     * @return list of slots
     */
    @Query(value = "SELECT s.* FROM slots s INNER JOIN event_types e ON s.event_type_id = e.id WHERE e.user_id = :ownerId AND s.start_time >= NOW()", nativeQuery = true)
    List<Slot> findByOwnerIdAndDate(@Param("ownerId") Long ownerId, @Param("startDate") LocalDate startDate);

    /**
     * Find by owner id and exact date.
     *
     * @param ownerId owner id
     * @param date date
     * @return list of slots
     */
    @Query(value = "SELECT s.* FROM slots s INNER JOIN event_types e ON s.event_type_id = e.id WHERE e.user_id = :ownerId AND CAST(s.start_time AS DATE) = CAST(:date AS DATE)", nativeQuery = true)
    List<Slot> findByOwnerIdAndExactDate(@Param("ownerId") Long ownerId, @Param("date") LocalDate date);
}