package org.total.example.spring_core.web.exceptions;

import org.total.example.spring_core.web.enums.ReservationStatus;

public class ReservationUpdateException extends RuntimeException {

    public ReservationUpdateException(Long id, ReservationStatus status) {
        super("Reservation " + id + " cannot be updated because it is " + status);
    }
}
