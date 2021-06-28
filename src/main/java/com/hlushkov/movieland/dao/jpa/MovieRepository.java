package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
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
    public List<Movie> findAll(FindMoviesRequest findMoviesRequest) {
        String query = "SELECT m FROM Movie m";
        String generatedQuery = generateQuery(query, findMoviesRequest);
        log.debug("Find all movies request processing starting");
        return entityManager.createQuery(generatedQuery, Movie.class)
                .setHint("org.hibernate.cacheable", Boolean.TRUE)
                .getResultList();
    }

    @Override
    public List<Movie> findRandom() {
        String query = "SELECT m FROM Movie m ORDER BY RAND()";
        return entityManager.createQuery(query, Movie.class).setMaxResults(randomMovieCount).getResultList();
    }

    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        String query = "SELECT m FROM Genre g JOIN g.movies m WHERE g.id = :genreId";
        String generatedQuery = generateQuery(query, findMoviesRequest);
        TypedQuery<Movie> typedQuery = entityManager.createQuery(generatedQuery, Movie.class);
        typedQuery.setParameter("genreId", genreId);
        return typedQuery.getResultList();
    }

    String generateQuery(String query, FindMoviesRequest findMoviesRequest) {
        String orderBy = " ORDER BY ";
        if (findMoviesRequest.getRatingDirection().isPresent()) {
            if (findMoviesRequest.getRatingDirection().get() == SortDirection.DESC) {
                return query + orderBy + "rating " + SortDirection.DESC.getDirection();
            }
        } else if (findMoviesRequest.getPriceDirection().isPresent()) {
            if (findMoviesRequest.getPriceDirection().get() == SortDirection.DESC) {
                return query + orderBy + "price " + SortDirection.DESC.getDirection();
            } else {
                return query + orderBy + "price " + SortDirection.ASC.getDirection();
            }
        }

        return query;
    }
}
