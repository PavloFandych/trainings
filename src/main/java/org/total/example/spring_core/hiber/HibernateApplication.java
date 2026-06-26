package org.total.example.spring_core.hiber;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.total.example.spring_core.hiber.entity.Course;
import org.total.example.spring_core.hiber.entity.Group;
import org.total.example.spring_core.hiber.entity.Profile;
import org.total.example.spring_core.hiber.entity.Student;
import org.total.example.spring_core.hiber.service.CourseService;
import org.total.example.spring_core.hiber.service.GroupService;
import org.total.example.spring_core.hiber.service.ProfileService;
import org.total.example.spring_core.hiber.service.StudentService;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "org.total.example.spring_core.hiber"
})
public class HibernateApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HibernateApplication.class, args);

        CourseService courseService = context.getBean(CourseService.class);
        GroupService groupService = context.getBean(GroupService.class);
        StudentService studentService = context.getBean(StudentService.class);
        ProfileService profileService = context.getBean(ProfileService.class);

        // ── SETUP ──────────────────────────────────────────────────────────────
        Course math = courseService.save(new Course("math-1", "math"));
        Course physics = courseService.save(new Course("physics-1", "physics"));

        Group group1 = groupService.save("A-101", 2025L);
        Group group2 = groupService.save("B-202", 2026L);

        Student john = studentService.save(new Student("John Doe", 20, group1));
        Student jane = studentService.save(new Student("Jane Doe", 22, group1));
        Student alice = studentService.save(new Student("Alice Smith", 25, group2));

        Profile _ = profileService.save(new Profile("John's bio", LocalDateTime.now(), john));

        courseService.enrollStudent(math.getId(), john.getId());
        courseService.enrollStudent(math.getId(), jane.getId());
        courseService.enrollStudent(physics.getId(), alice.getId());

        // ── @OneToOne inverse — always EAGER ───────────────────────────────────
        // FK lives in profiles.student_id, so Hibernate always fires a SELECT to check existence
        log.info("=== @OneToOne inverse (always EAGER) ===");
        Student johnLoaded = studentService.getById(john.getId());
        Student janeLoaded = studentService.getById(jane.getId());
        log.info("John profile: {}", johnLoaded.getProfile()); // Profile(...)
        log.info("Jane profile: {}", janeLoaded.getProfile()); // null — no profile saved

        // ── @ManyToOne LAZY — loaded via JOIN FETCH in getById ─────────────────
        log.info("=== @ManyToOne LAZY (loaded via JOIN FETCH) ===");
        log.info("John's group: {}", johnLoaded.getGroup().getNumber()); // A-101

        // ── @OneToMany LAZY ────────────────────────────────────────────────────
        log.info("=== @OneToMany LAZY ===");

        // getById: students collection NOT initialized — accessing it throws LazyInitializationException
        Group groupLazy = groupService.getById(group1.getId());
        try {
            int _ = groupLazy.getStudents().size();
        } catch (LazyInitializationException e) {
            log.warn("getById: students not initialized → LazyInitializationException (expected)");
        }

        // getByIdWithStudents: JOIN FETCH initializes students + profiles in one query
        Group groupWithStudents = groupService.getByIdWithStudents(group1.getId());
        log.info("getByIdWithStudents → students in group {}: {}",
                groupWithStudents.getNumber(),
                groupWithStudents.getStudents().stream().map(Student::getName).toList());

        // ── @ManyToMany — native INSERT, 1 query ──────────────────────────────
        log.info("=== @ManyToMany enrollment ===");
        log.info("Enrolled {} in '{}'", john.getName(), math.getName());
        log.info("Enrolled {} in '{}'", jane.getName(), math.getName());
        log.info("Enrolled {} in '{}'", alice.getName(), physics.getName());

        // ── CRUD ──────────────────────────────────────────────────────────────
        log.info("=== CRUD ===");

        // update via merge (SELECT + UPDATE, dirty checking)
        john.setAge(21);
        Student _ = studentService.update(john);
        log.info("Updated John age via merge → {}", studentService.getById(john.getId()).getAge());

        // update via HQL bulk-update (1 UPDATE, no dirty checking)
        alice.setAge(26);
        studentService.updateDirect(alice);
        log.info("Updated Alice age via HQL  → {}", studentService.getById(alice.getId()).getAge());

        log.info("All students: {}", studentService.getAll().stream().map(Student::getName).toList());

        studentService.deleteById(jane.getId());
        log.info("After delete Jane: {}", studentService.getAll().stream().map(Student::getName).toList());

        context.close();
    }
}
