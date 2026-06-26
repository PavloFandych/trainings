package org.total.example.spring_core.web.exceptions;

public class ReservationCreationException extends RuntimeException {

    public ReservationCreationException(String message) {
        super(message);
    }
}
