package org.total.example.spring_core.web.service.impl;

import org.total.example.spring_core.web.model.Reservation;

import java.util.List;

record ReservationSearchPage(List<Reservation> content, long totalElements) {
}
