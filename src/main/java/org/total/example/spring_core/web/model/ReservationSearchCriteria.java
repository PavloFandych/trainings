package org.total.example.spring_core.web.model;

import org.total.example.spring_core.web.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchCriteria(
        List<Long> ids,
        List<Long> userIds,
        List<Long> roomIds,
        List<ReservationStatus> statuses,
        LocalDate startDate,
        LocalDate endDate
) {
}
