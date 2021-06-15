package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class MovieRepository implements MovieDao {
    @Value("${movie.random.count}")
    private int randomMovieCount;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findAll() {
        String query = "SELECT m FROM Movie m";
        return entityManager.createQuery(query, Movie.class).getResultList();
    }

    @Override
    public List<Movie> findRandom() {
        String query = "SELECT m FROM Movie m ORDER BY RAND()";
        return entityManager.createQuery(query, Movie.class).setMaxResults(randomMovieCount).getResultList();
    }

}
