package org.total.example.spring_core.web.service;

import org.total.example.spring_core.web.model.CheckAvailabilityRequest;
import org.total.example.spring_core.web.model.CheckAvailabilityResponse;

public interface ReservationAvailabilityService {

    CheckAvailabilityResponse checkAvailability(CheckAvailabilityRequest request);
}
