package org.total.example.spring_core.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.total.example.spring_core.web.entity.ReservationEntity;
import org.total.example.spring_core.web.mapper.ReservationMapper;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;
import org.total.example.spring_core.web.repository.ManualReservationRepository;

@Component
@RequiredArgsConstructor
class ReservationSearchCache {

    public static final String CACHE_NAME = "reservationsSearch";

    private final ManualReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Cacheable(cacheNames = CACHE_NAME, key = "#criteria.toString() + '::' + #pageable.toString()")
    public ReservationSearchPage search(ReservationSearchCriteria criteria, Pageable pageable) {
        Page<ReservationEntity> page = reservationRepository.search(criteria, pageable);
        return new ReservationSearchPage(
                page.map(reservationMapper::toModel).getContent(),
                page.getTotalElements());
    }
}
