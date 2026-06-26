package org.total.example.spring_core.hiber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "age")
    private Integer age;

    // inverse side — FK lives in profiles.student_id; LAZY is ignored here, always EAGER
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    // owner side — FK "group_id" lives here; LAZY works
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*group_id is in students*/name = "group_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_students_groups"))
    private Group group;

    // owner side — join table "students_courses" managed here; LAZY by default for @ManyToMany
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(
                    /*student_id is in students_courses*/name = "student_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    /*course_id is in students_courses*/name = "course_id",
                    referencedColumnName = "id"
            ),
            foreignKey = @ForeignKey(name = "fk_students_courses_student_id"),
            inverseForeignKey = @ForeignKey(name = "fk_students_courses_course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String name, Integer age, Group group) {
        this.name = name;
        this.age = age;
        this.group = group;
    }
}
