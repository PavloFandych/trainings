package org.total.example.spring_core.hiber.service;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.total.example.spring_core.hiber.TransactionHelper;
import org.total.example.spring_core.hiber.entity.Profile;

@Service
@AllArgsConstructor
public class ProfileService {

    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    // JOIN FETCH loads student in one query so the detached entity is fully usable
    public Profile findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select p from Profile p left join fetch p.student where p.id = :id",
                            Profile.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        }
    }

    public Profile save(Profile profile) {
        return transactionHelper.executeInTransaction(session -> {
            session.persist(profile);
            // IDENTITY strategy sends INSERT immediately to get the DB-generated id
            return profile;
        });
    }
}
