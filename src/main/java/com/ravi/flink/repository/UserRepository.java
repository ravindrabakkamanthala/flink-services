package com.ravi.flink.repository;

import com.ravi.flink.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserRepository {

    private final EntityManagerFactory entityManagerFactory;

    public UserRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager createEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

    public void save(User user) {
        EntityManager entityManager = createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.merge(user);
            entityTransaction.commit();
            entityManager.close();
        } catch (Exception e) {
            log.error("Exception Occurred while persisiting User data:: %s", e.getMessage());
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
