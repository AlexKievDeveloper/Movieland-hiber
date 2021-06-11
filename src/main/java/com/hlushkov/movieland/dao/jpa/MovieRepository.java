package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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

}
