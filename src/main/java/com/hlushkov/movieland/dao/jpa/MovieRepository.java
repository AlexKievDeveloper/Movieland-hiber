package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.entity.Movie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
@Resource
public class MovieRepository implements MovieDao {
    @Value("${movie.random.count}")
    private int randomMovieCount;
    @Getter
    @PersistenceContext
    private EntityManager entityManager;
    private final LocalSessionFactoryBean localSessionFactoryBean;

    @Transactional
    @Override
    public List<Movie> findRandom() {
        String query = "SELECT m FROM Movie m ORDER BY RAND()";
        return entityManager.createQuery(query, Movie.class).setMaxResults(randomMovieCount).getResultList();
    }

    @Transactional
    @Override
    public List<Movie> findAll(FindMoviesRequest findMoviesRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movieRoot = criteriaQuery.from(Movie.class);
        CriteriaQuery<Movie> generatedCriteriaQuery = generateQuery(movieRoot, criteriaQuery, findMoviesRequest);
        return entityManager.createQuery(generatedCriteriaQuery)
                .setHint("org.hibernate.cacheable", Boolean.TRUE)
                .getResultList();
    }

    @Transactional
    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movieRoot = criteriaQuery.from(Movie.class);
        Join<Movie, Genre> movieGenreSetJoin = movieRoot.join("genres");
        ParameterExpression<Integer> integerParameterExpression = criteriaBuilder.parameter(Integer.class);
        criteriaQuery.where(criteriaBuilder.equal(movieGenreSetJoin.get("id"), integerParameterExpression));

        CriteriaQuery<Movie> generatedCriteriaQuery = generateQuery(movieRoot, criteriaQuery, findMoviesRequest);

        TypedQuery<Movie> movieTypedQuery = entityManager.createQuery(generatedCriteriaQuery);
        movieTypedQuery.setParameter(integerParameterExpression, genreId);
        return movieTypedQuery.getResultList();
    }

    private CriteriaQuery<Movie> generateQuery(Root<Movie> root, CriteriaQuery<Movie> criteriaQuery, FindMoviesRequest findMoviesRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery.select(root);

        if (findMoviesRequest.getRatingDirection().isPresent()) {
            if (findMoviesRequest.getRatingDirection().get() == SortDirection.DESC) {
                return criteriaQuery.orderBy(criteriaBuilder.desc(root.get("rating")));
            }
        } else if (findMoviesRequest.getPriceDirection().isPresent()) {
            if (findMoviesRequest.getPriceDirection().get() == SortDirection.DESC) {
                return criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")));
            } else {
                return criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")));
            }
        }

        return criteriaQuery;
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${movie.cache.update.time.interval}", fixedRateString = "${movie.cache.update.time.interval}")
    void updateCacheValues() {
        entityManager.clear();
        SessionFactory sessionFactory = localSessionFactoryBean.getObject();
        if (sessionFactory != null) {
            sessionFactory.getCache().evictAllRegions();
            FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
            findMoviesRequest.setPriceDirection(Optional.empty());
            findMoviesRequest.setRatingDirection(Optional.empty());
            findAll(findMoviesRequest);
            log.info("Refreshed movies cache");
        } else {
            log.info("Session factory is null");
        }
    }
}
