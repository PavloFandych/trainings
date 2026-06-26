package org.total.example.spring_core.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.total.example.spring_core.web.entity.ReservationEntity;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;

import java.time.LocalDate;

public interface QueryDSLReservationRepository {

    Page<ReservationEntity> search(ReservationSearchCriteria criteria, Pageable pageable);

    Page<ReservationEntity> findActiveInPeriod(LocalDate from, LocalDate to, Pageable pageable);
}
