package org.total.example.spring_core.web.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.util.CollectionUtils;
import org.total.example.spring_core.web.entity.QReservationEntity;
import org.total.example.spring_core.web.entity.ReservationEntity;
import org.total.example.spring_core.web.enums.ReservationStatus;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;
import org.total.example.spring_core.web.repository.QueryDSLReservationRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class QueryDSLReservationRepositoryImpl implements QueryDSLReservationRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<ReservationEntity> search(ReservationSearchCriteria criteria, Pageable pageable) {
        QReservationEntity reservation = QReservationEntity.reservationEntity;

        BooleanBuilder predicate = new BooleanBuilder();
        if (!CollectionUtils.isEmpty(criteria.ids())) {
            predicate.and(reservation.id.in(criteria.ids()));
        }
        if (!CollectionUtils.isEmpty(criteria.userIds())) {
            predicate.and(reservation.userId.in(criteria.userIds()));
        }
        if (!CollectionUtils.isEmpty(criteria.roomIds())) {
            predicate.and(reservation.roomId.in(criteria.roomIds()));
        }
        if (!CollectionUtils.isEmpty(criteria.statuses())) {
            predicate.and(reservation.status.in(criteria.statuses()));
        }
        if (criteria.startDate() != null) {
            predicate.and(reservation.startDate.eq(criteria.startDate()));
        }
        if (criteria.endDate() != null) {
            predicate.and(reservation.endDate.eq(criteria.endDate()));
        }

        return fetchPage(reservation, predicate, pageable);
    }

    @Override
    public Page<ReservationEntity> findActiveInPeriod(LocalDate from, LocalDate to, Pageable pageable) {
        QReservationEntity reservation = QReservationEntity.reservationEntity;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(reservation.status.ne(ReservationStatus.CANCELLED))
                .and(reservation.startDate.loe(to))
                .and(reservation.endDate.goe(from));

        return fetchPage(reservation, predicate, pageable);
    }

    private Page<ReservationEntity> fetchPage(QReservationEntity reservation,
                                              BooleanBuilder predicate, Pageable pageable) {
        Querydsl querydsl = new Querydsl(entityManager, new PathBuilder<>(ReservationEntity.class,
                "reservationEntity"));

        JPQLQuery<ReservationEntity> query = queryFactory.selectFrom(reservation).where(predicate);
        List<ReservationEntity> content = querydsl.applyPagination(pageable, query).fetch();

        Long total = queryFactory.select(reservation.count())
                .from(reservation).where(predicate).fetchOne();
        long totalCount = total != null ? total : 0L;

        return new PageImpl<>(content, pageable, totalCount);
    }
}
