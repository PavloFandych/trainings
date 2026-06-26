package org.total.example.spring_core.web.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.total.example.spring_core.web.entity.ReservationEntity;

import java.time.LocalDate;
import java.util.List;

public interface ManualReservationRepository
        extends JpaRepository<ReservationEntity, Long>, QueryDSLReservationRepository {

    @Modifying
    @Query("delete from ReservationEntity r where r.id = :id")
    int deleteByIdAndGetCount(@Param("id") Long id);

    @Query("""
            select r from ReservationEntity r
            where r.roomId = :roomId
              and r.status <> org.total.example.spring_core.web.enums.ReservationStatus.CANCELLED
              and (:excludeId is null or r.id <> :excludeId)
              and r.startDate <= :endDate
              and r.endDate >= :startDate
            """)
    List<ReservationEntity> findOverlapping(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeId") Long excludeId,
            Pageable pageable);
}
