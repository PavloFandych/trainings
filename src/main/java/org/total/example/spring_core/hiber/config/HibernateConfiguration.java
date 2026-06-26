package org.total.example.spring_core.hiber.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.total.example.spring_core.hiber.entity.Course;
import org.total.example.spring_core.hiber.entity.Group;
import org.total.example.spring_core.hiber.entity.Profile;
import org.total.example.spring_core.hiber.entity.Student;

@Configuration
public class HibernateConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Profile.class)
                .addAnnotatedClass(Group.class)
                .addAnnotatedClass(Course.class)
                .addPackage("org.total.example.spring_core.controllers")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "root")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop"); // to run the app without PSQLException

        return configuration.buildSessionFactory();
    }
}
