package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GenreRepository implements GenreDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        String query = "SELECT g FROM Genre g";
        return entityManager.createQuery(query, Genre.class).getResultList();
    }
}
