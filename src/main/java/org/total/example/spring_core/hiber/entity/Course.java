package org.total.example.spring_core.hiber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    // inverse side — join table managed by Student.courses; mappedBy refers to Student.courses (Java field)
    // @ToString.Exclude: prevents toString() from triggering LAZY initialization outside a session
    @ToString.Exclude
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;

    public Course() {
    }

    public Course(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
