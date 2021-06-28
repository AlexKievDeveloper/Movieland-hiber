package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class GenreRepository implements GenreDao {
    @Getter(value = AccessLevel.PACKAGE)
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        String query = "SELECT g FROM Genre g";
        log.debug("Find all genres request processing starting");
        return entityManager.createQuery(query, Genre.class)
                .setHint(QueryHints.HINT_CACHEABLE, true)
                .getResultList();
    }
}
