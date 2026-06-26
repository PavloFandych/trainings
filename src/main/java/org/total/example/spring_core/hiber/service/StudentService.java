package org.total.example.spring_core.hiber.service;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.hiber.TransactionHelper;
import org.total.example.spring_core.hiber.entity.Student;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    // JOIN FETCH loads profile and group in one query so the detached entity is fully usable
    public Student getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select s from Student s left join fetch s.profile left join fetch s.group where s.id = :id",
                            Student.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        }
    }

    public Student getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select s from Student s left join fetch s.profile left join fetch s.group where s.name = :name",
                            Student.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }
    }

    public List<Student> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select s from Student s left join fetch s.profile left join fetch s.group",
                            Student.class)
                    .list();
        }
    }

    public Student save(Student student) {
        return transactionHelper.executeInTransaction(session -> {
            session.persist(student);
            // IDENTITY strategy sends INSERT immediately to get the DB-generated id
            return student;
        });
    }

    // SELECT + UPDATE; Hibernate skips UPDATE if nothing changed (dirty checking)
    public Student update(Student student) {
        return transactionHelper.executeInTransaction(session -> session.merge(student));
    }

    // 1 UPDATE regardless of whether data changed; no entity loading
    public void updateDirect(Student student) {
        transactionHelper.runInTransaction(session ->
                session.createMutationQuery(
                                "update Student s set s.name = :name, s.age = :age where s.id = :id")
                        .setParameter("name", student.getName())
                        .setParameter("age", student.getAge())
                        .setParameter("id", student.getId())
                        .executeUpdate()
        );
    }

    // SELECT + DELETE; throws if not found
    public void deleteById(Long id) {
        transactionHelper.runInTransaction(session -> {
            Student student = session.find(Student.class, id);
            if (student == null) throw new IllegalArgumentException("Student not found: " + id);
            session.remove(student);
        });
    }

    // JPQL delete — older API; Hibernate 6 prefers createMutationQuery() for DML
    public void deleteByIdJpql(Long id) {
        transactionHelper.runInTransaction(session ->
                session.createQuery("delete from Student s where s.id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }

    // native SQL — use actual table/column names, bypasses HQL parser
    public void deleteByIdNativeSql(Long id) {
        transactionHelper.runInTransaction(session ->
                session.createNativeQuery("DELETE FROM students WHERE id = :id", Student.class)
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }

    // HQL bulk-delete: 1 DELETE, no entity loading
    public void deleteByIdDirect(Long id) {
        transactionHelper.runInTransaction(session ->
                session.createMutationQuery("delete from Student where id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }
}
