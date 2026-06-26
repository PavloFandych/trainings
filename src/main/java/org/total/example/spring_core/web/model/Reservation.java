package org.total.example.spring_core.web.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.total.example.spring_core.web.enums.ReservationStatus;

import java.time.LocalDate;

public record Reservation(
        @Null Long id, @NotNull @Min(0) Long userId,
        @NotNull @Min(0) Long roomId, @NotNull @FutureOrPresent LocalDate startDate,
        @NotNull @FutureOrPresent LocalDate endDate, @Null ReservationStatus status
) {
}
