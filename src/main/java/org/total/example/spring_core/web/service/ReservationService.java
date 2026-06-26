package org.total.example.spring_core.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.total.example.spring_core.web.model.Reservation;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;

import java.time.LocalDate;

public interface ReservationService {

    Page<Reservation> search(ReservationSearchCriteria criteria, Pageable pageable);

    Page<Reservation> findActiveInPeriod(LocalDate from, LocalDate to, Pageable pageable);

    Reservation save(Reservation reservation);

    Reservation update(Long id, Reservation reservation);

    void delete(Long id);

    Reservation approve(Long id);

    Reservation cancel(Long id);
}
