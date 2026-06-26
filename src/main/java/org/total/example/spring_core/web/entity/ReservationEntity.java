package org.total.example.spring_core.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.total.example.spring_core.web.enums.ReservationStatus;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "room_id", nullable = false)
    Long roomId;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    ReservationStatus status;

    public ReservationEntity() {
    }
}
