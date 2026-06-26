package org.total.example.spring_core.web.exceptions.responses;

import java.time.LocalDateTime;

public record ErrorResponse(String message, String path, LocalDateTime serverTime) {

    public static ErrorResponse of(String message, String path) {
        return new ErrorResponse(message, path, LocalDateTime.now());
    }
}
