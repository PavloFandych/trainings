package org.total.example.spring_core.hiber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "student_groups")
@Getter
@Setter
@ToString
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "graduation_year")
    private Long graduationYear;

    // inverse side — FK "group_id" lives in students table; mappedBy refers to Student.group (Java field)
    // no cascade: delete a group only after reassigning its students, otherwise FK constraint violation
    // @ToString.Exclude: prevents toString() from triggering LAZY initialization outside a session
    @ToString.Exclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Student> students;

    public Group() {
    }

    public Group(String number, Long graduationYear) {
        this.number = number;
        this.graduationYear = graduationYear;
    }
}
