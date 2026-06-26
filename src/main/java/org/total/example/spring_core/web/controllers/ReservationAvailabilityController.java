package org.total.example.spring_core.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.total.example.spring_core.web.model.CheckAvailabilityRequest;
import org.total.example.spring_core.web.model.CheckAvailabilityResponse;
import org.total.example.spring_core.web.service.ReservationAvailabilityService;

@Slf4j
@RestController
@RequestMapping("/reservation-availability")
@RequiredArgsConstructor
public class ReservationAvailabilityController {

    private final ReservationAvailabilityService reservationAvailabilityService;

    @PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CheckAvailabilityResponse checkAvailability(
            @RequestBody @Valid CheckAvailabilityRequest request) {
        log.info("checking availability for {}", request);
        return reservationAvailabilityService.checkAvailability(request);
    }
}
