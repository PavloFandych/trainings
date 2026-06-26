package org.total.example.spring_core.hiber.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.hiber.TransactionHelper;
import org.total.example.spring_core.hiber.entity.Course;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final TransactionHelper transactionHelper;

    public Course save(Course course) {
        return transactionHelper.executeInTransaction(session -> {
            session.persist(course);
            return course;
        });
    }

    public void enrollStudent(Long courseId, Long studentId) {
        transactionHelper.runInTransaction(session ->
                session.createNativeQuery(
                                "INSERT INTO students_courses (student_id, course_id) VALUES (:studentId, :courseId)")
                        .setParameter("studentId", studentId)
                        .setParameter("courseId", courseId)
                        .executeUpdate()
        );
    }
}
