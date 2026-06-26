package org.total.example.spring_core.web.exceptions;

import java.time.LocalDate;

public class ReservationDateConflictException extends RuntimeException {

    public ReservationDateConflictException(Long roomId, LocalDate start, LocalDate end) {
        super("Room " + roomId + " is already booked between " + start + " and " + end);
    }
}
