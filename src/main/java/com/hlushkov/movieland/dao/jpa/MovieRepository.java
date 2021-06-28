package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
@Resource
public class MovieRepository implements MovieDao {
    @Value("${movie.random.count}")
    private int randomMovieCount;
    @Getter(value = AccessLevel.PACKAGE)
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findAll() {
        String query = "SELECT m FROM Movie m";
        log.debug("Find all movies request processing starting");
        return entityManager.createQuery(query, Movie.class)
                .setHint("org.hibernate.cacheable", Boolean.TRUE)
                .getResultList();
    }

    @Override
    public List<Movie> findRandom() {
        String query = "SELECT m FROM Movie m ORDER BY RAND()";
        return entityManager.createQuery(query, Movie.class).setMaxResults(randomMovieCount).getResultList();
    }

    @Override
    public List<Movie> findByGenre(int genreId) {
        String query = "SELECT m FROM Genre g JOIN g.movies m WHERE g.id = :genreId";
        TypedQuery<Movie> typedQuery = entityManager.createQuery(query, Movie.class);
        typedQuery.setParameter("genreId", genreId);
        return typedQuery.getResultList();
    }
}
