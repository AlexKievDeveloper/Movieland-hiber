package com.hlushkov.movieland.dao.impl;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
public class HibernateMovieDao implements MovieDao {
    private final SessionFactory sessionFactory;

    @Override
    public List<Movie> finaAll() {

        try (Session session = sessionFactory.openSession()) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = cb.createQuery(Movie.class);
        Root<Movie> rootEntry = criteriaQuery.from(Movie.class);
        CriteriaQuery<Movie> all = criteriaQuery.select(rootEntry);

        TypedQuery<Movie> allQuery = session.createQuery(all);
        return allQuery.getResultList();
        }
        //return List.of();
    }
}


/*        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Movie> movies = session.get(Movie.class);
            session.getTransaction().commit();
            return movies;
        }
        return List.of();*/