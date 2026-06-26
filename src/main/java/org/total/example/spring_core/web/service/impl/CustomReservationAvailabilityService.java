package org.total.example.spring_core.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.web.enums.AvailabilityStatus;
import org.total.example.spring_core.web.exceptions.ReservationCreationException;
import org.total.example.spring_core.web.model.CheckAvailabilityRequest;
import org.total.example.spring_core.web.model.CheckAvailabilityResponse;
import org.total.example.spring_core.web.repository.ManualReservationRepository;
import org.total.example.spring_core.web.service.ReservationAvailabilityService;

@Service
@RequiredArgsConstructor
public class CustomReservationAvailabilityService implements ReservationAvailabilityService {

    private final ManualReservationRepository reservationRepository;

    @Override
    public CheckAvailabilityResponse checkAvailability(CheckAvailabilityRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new ReservationCreationException(
                    "Invalid period: 'startDate' must not be after 'endDate'");
        }

        boolean hasOverlap = !reservationRepository
                .findOverlapping(request.roomId(), request.startDate(), request.endDate(), null, PageRequest.of(0, 1))
                .isEmpty();

        return hasOverlap
                ? new CheckAvailabilityResponse(
                "Room is already booked for the given period", AvailabilityStatus.RESERVED)
                : new CheckAvailabilityResponse(
                "Room is available", AvailabilityStatus.AVAILABLE);
    }
}
