package org.total.example.spring_core.hiber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@ToString
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "last_seen_time")
    private LocalDateTime lastSeenTime;

    // owner side — FK "student_id" lives here; LAZY works
    // @ToString.Exclude: prevents circular Profile → Student → Profile → ...
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*student_id is in profiles*/name = "student_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_profiles_students"))
    private Student student;

    public Profile() {
    }

    public Profile(String bio, LocalDateTime lastSeenTime, Student student) {
        this.bio = bio;
        this.lastSeenTime = lastSeenTime;
        this.student = student;
    }
}
