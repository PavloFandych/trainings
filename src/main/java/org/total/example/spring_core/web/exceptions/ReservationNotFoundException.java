package org.total.example.spring_core.web.exceptions;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Reservation not found: " + id);
    }
}
