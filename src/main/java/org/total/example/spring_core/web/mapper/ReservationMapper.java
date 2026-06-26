package org.total.example.spring_core.web.mapper;

import org.mapstruct.Mapper;
import org.total.example.spring_core.web.entity.ReservationEntity;
import org.total.example.spring_core.web.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation toModel(ReservationEntity entity);

    ReservationEntity toEntity(Reservation reservation);
}
