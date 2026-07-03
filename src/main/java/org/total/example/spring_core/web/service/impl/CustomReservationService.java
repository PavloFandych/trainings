package org.total.example.spring_core.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.total.example.spring_core.web.entity.ReservationEntity;
import org.total.example.spring_core.web.enums.ReservationStatus;
import org.total.example.spring_core.web.exceptions.ReservationCreationException;
import org.total.example.spring_core.web.exceptions.ReservationDateConflictException;
import org.total.example.spring_core.web.exceptions.ReservationNotFoundException;
import org.total.example.spring_core.web.exceptions.ReservationUpdateException;
import org.total.example.spring_core.web.mapper.ReservationMapper;
import org.total.example.spring_core.web.model.Reservation;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;
import org.total.example.spring_core.web.repository.ManualReservationRepository;
import org.total.example.spring_core.web.service.ReservationService;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomReservationService implements ReservationService {

    private final ManualReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationSearchCache reservationSearchCache;

    @Override
    public Page<Reservation> search(ReservationSearchCriteria criteria, Pageable pageable) {
        ReservationSearchPage cached = reservationSearchCache.search(criteria, pageable);
        return new PageImpl<>(cached.content(), pageable, cached.totalElements());
    }

    @Override
    public Page<Reservation> findActiveInPeriod(LocalDate from, LocalDate to, Pageable pageable) {
        if (from.isAfter(to)) {
            throw new ReservationCreationException("Invalid period: 'from' must not be after 'to'");
        }
        return reservationRepository.findActiveInPeriod(from, to, pageable)
                .map(reservationMapper::toModel);
    }

    @Override
    @CacheEvict(cacheNames = ReservationSearchCache.CACHE_NAME, allEntries = true)
    public Reservation save(Reservation reservation) {
        if (hasInvalidDateOrder(reservation)) {
            throw new ReservationCreationException("Invalid reservation dates");
        }
        findDateOverlap(
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                null)
                .ifPresent(conflict -> {
                    throw new ReservationDateConflictException(reservation.roomId(),
                            conflict.startDate(), conflict.endDate());
                });
        ReservationEntity toSave = reservationMapper.toEntity(reservation);
        toSave.setStatus(ReservationStatus.PENDING);

        return reservationMapper.toModel(reservationRepository.save(toSave));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = ReservationSearchCache.CACHE_NAME, allEntries = true)
    public Reservation update(Long id, Reservation reservation) {
        ReservationEntity existing = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (existing.getStatus() != ReservationStatus.PENDING) {
            throw new ReservationUpdateException(id, existing.getStatus());
        }
        if (hasInvalidDateOrder(reservation)) {
            throw new ReservationCreationException("Invalid reservation dates");
        }
        findDateOverlap(reservation.roomId(), reservation.startDate(), reservation.endDate(), id)
                .ifPresent(conflict -> {
                    throw new ReservationDateConflictException(reservation.roomId(), conflict.startDate(), conflict.endDate());
                });
        existing.setUserId(reservation.userId());
        existing.setRoomId(reservation.roomId());
        existing.setStartDate(reservation.startDate());
        existing.setEndDate(reservation.endDate());
        existing.setStatus(ReservationStatus.PENDING);

        // existing stays managed within @Transactional, so Hibernate flushes
        // the changes via dirty checking on commit — an explicit save() is redundant here
        return reservationMapper.toModel(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = ReservationSearchCache.CACHE_NAME, allEntries = true)
    public Reservation approve(Long id) {
        ReservationEntity existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (existing.getStatus() != ReservationStatus.PENDING) {
            throw new ReservationUpdateException(id, existing.getStatus());
        }
        existing.setStatus(ReservationStatus.APPROVED);

        // existing stays managed within @Transactional, so Hibernate flushes
        // the changes via dirty checking on commit — an explicit save() is redundant here
        return reservationMapper.toModel(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = ReservationSearchCache.CACHE_NAME, allEntries = true)
    public Reservation cancel(Long id) {
        ReservationEntity existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (existing.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationUpdateException(id, existing.getStatus());
        }
        existing.setStatus(ReservationStatus.CANCELLED);

        // existing stays managed within @Transactional, so Hibernate flushes
        // the changes via dirty checking on commit — an explicit save() is redundant here
        return reservationMapper.toModel(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = ReservationSearchCache.CACHE_NAME, allEntries = true)
    public void delete(Long id) {
        if (reservationRepository.deleteByIdAndGetCount(id) == 0) {
            throw new ReservationNotFoundException(id);
        }
    }

    private boolean hasInvalidDateOrder(Reservation reservation) {
        return reservation.startDate().isAfter(reservation.endDate());
    }

    private Optional<Reservation> findDateOverlap(
            Long roomId, LocalDate start, LocalDate end, Long excludeId) {
        return reservationRepository
                .findOverlapping(roomId, start, end, excludeId, PageRequest.of(0, 1))
                .stream()
                .map(reservationMapper::toModel)
                .findFirst();
    }
}
