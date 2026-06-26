package org.total.example.spring_core.web.model;

import org.total.example.spring_core.web.enums.AvailabilityStatus;

public record CheckAvailabilityResponse(
        String message, AvailabilityStatus status) {
}
