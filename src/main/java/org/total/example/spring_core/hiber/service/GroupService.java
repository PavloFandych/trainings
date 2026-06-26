package org.total.example.spring_core.hiber.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.hiber.TransactionHelper;
import org.total.example.spring_core.hiber.entity.Group;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    // students collection NOT initialized — accessing it outside session causes LazyInitializationException
    public Group getById(Long id) {
        try (var session = sessionFactory.openSession()) {
            return session.find(Group.class, id);
        }
    }

    // students collection NOT initialized — accessing it outside session causes LazyInitializationException
    public List<Group> getAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("select g from Group g", Group.class).list();
        }
    }

    // JOIN FETCH loads students + profiles in one query
    public Group getByIdWithStudents(Long id) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select g from Group g left join fetch g.students s left join fetch s.profile where g.id = :id",
                            Group.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        }
    }

    // JOIN FETCH loads students + profiles in one query
    public List<Group> getAllWithStudents() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select g from Group g left join fetch g.students s left join fetch s.profile",
                            Group.class)
                    .list();
        }
    }

    public Group save(String number, Long gradYear) {
        return transactionHelper.executeInTransaction(session -> {
            Group group = new Group(number, gradYear);
            session.persist(group);
            return group;
        });
    }
}
