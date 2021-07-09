package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.entity.MovielandUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository implements UserDao {
    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Optional<MovielandUser> findByUsername(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MovielandUser> criteriaQuery = criteriaBuilder.createQuery(MovielandUser.class);
        Root<MovielandUser> userRoot = criteriaQuery.from(MovielandUser.class);
        criteriaQuery.select(userRoot).where(userRoot.get("username").in(username));

        MovielandUser movielandUser = entityManager.createQuery(criteriaQuery)
                .setHint("org.hibernate.cacheable", Boolean.TRUE)
                .getSingleResult();

        return Optional.ofNullable(movielandUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void save(MovielandUser movielandUser) {
        log.info("Sign up user method invoked: {}", movielandUser);
        entityManager.persist(movielandUser);
    }
}

