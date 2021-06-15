package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        CriteriaQuery<Movie> all = criteriaQuery.select(root);
        TypedQuery<Movie> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    @Transactional
    public List<Movie> findRandom() {
        String query = "SELECT m FROM Movie m ORDER BY RAND()";
        return entityManager.createQuery(query, Movie.class).setMaxResults(randomMovieCount).getResultList();
    }

}
