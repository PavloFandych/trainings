package org.total.example.spring_core.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.total.example.spring_core.web.enums.ReservationStatus;
import org.total.example.spring_core.web.model.Reservation;
import org.total.example.spring_core.web.model.ReservationSearchCriteria;
import org.total.example.spring_core.web.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/search")
    public Page<Reservation> search(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "userIds", required = false) List<Long> userIds,
            @RequestParam(value = "roomIds", required = false) List<Long> roomIds,
            @RequestParam(value = "statuses", required = false) List<ReservationStatus> statuses,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable pageable) {
        ReservationSearchCriteria criteria = new ReservationSearchCriteria(ids, userIds, roomIds,
                statuses, startDate, endDate);
        log.info("searching reservations by {}, page {}", criteria, pageable);
        return reservationService.search(criteria, pageable);
    }

    @GetMapping("/active")
    public Page<Reservation> findActiveInPeriod(
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to,
            Pageable pageable) {
        log.info("searching active reservations from {} to {}, page {}", from, to, pageable);
        return reservationService.findActiveInPeriod(from, to, pageable);
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody @Valid Reservation reservation) {
        log.info("saving reservation {}", reservation);
        return reservationService.save(reservation);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Reservation update(@RequestParam("id") Long id, @RequestBody @Valid Reservation reservation) {
        log.info("updating reservation {}", id);
        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("id") Long id) {
        log.info("deleting reservation {}", id);
        reservationService.delete(id);
    }

    @PostMapping("/approve")
    public Reservation approve(@RequestParam("id") Long id) {
        log.info("approving reservation {}", id);
        return reservationService.approve(id);
    }

    @PostMapping("/cancel")
    public Reservation cancel(@RequestParam("id") Long id) {
        log.info("cancelling reservation {}", id);
        return reservationService.cancel(id);
    }
}
