package org.total.example.spring_core.hiber;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TransactionHelper {

    private final SessionFactory sessionFactory;

    public void runInTransaction(Consumer<Session> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                action.accept(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback(); // rollback runs BEFORE session.close()
                throw e;
            }
        }
    }

    public <T> T executeInTransaction(Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                T result = action.apply(session);
                transaction.commit();
                return result;
            } catch (Exception e) {
                transaction.rollback(); // rollback runs BEFORE session.close()
                throw e;
            }
        }
    }
}
