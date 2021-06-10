package com.hlushkov.movieland.dao.impl;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
//@Repository
public class HibernateMovieDao implements MovieDao {
    //private final SessionFactory sessionFactory;
    @PersistenceContext
    private EntityManager entityManager;

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public List<Movie> findAll() {
/*        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationEntry> cq = cb.createQuery(ConfigurationEntry.class);
        Root<ConfigurationEntry> rootEntry = cq.from(ConfigurationEntry.class);
        CriteriaQuery<ConfigurationEntry> all = cq.select(rootEntry);
        TypedQuery<ConfigurationEntry> allQuery = em.createQuery(all);
        return allQuery.getResultList();*/


        CriteriaBuilder criteriaBuilder = emf.createEntityManager().getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        CriteriaQuery<Movie> all = criteriaQuery.select(root);
        TypedQuery<Movie> allQuery = entityManager.createQuery(all);
        List<Movie> movies = allQuery.getResultList();
        for (Movie movie : movies) {
            log.info("Movie: {}", movie);
        }
        return movies;
    }

}



/*        try (Session session = sessionFactory.openSession()) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> rootEntry = criteriaQuery.from(Movie.class);
        CriteriaQuery all = criteriaQuery.select(rootEntry);

        TypedQuery<Movie> allQuery = session.createQuery(all);
        return allQuery.getResultList();
        }*/

/*        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Movie> movies = session.get(Movie.class);
            session.getTransaction().commit();
            return movies;
        }
        return List.of();*/