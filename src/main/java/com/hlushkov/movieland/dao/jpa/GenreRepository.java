package com.hlushkov.movieland.dao.jpa;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Resource
public class GenreRepository implements GenreDao {
    @Getter(value = AccessLevel.PACKAGE)
    @PersistenceContext
    private EntityManager entityManager;
    private final LocalSessionFactoryBean localSessionFactoryBean;

    @Transactional
    @Override
    public List<Genre> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> criteriaQuery = criteriaBuilder.createQuery(Genre.class);
        Root<Genre> genreRoot = criteriaQuery.from(Genre.class);
        criteriaQuery.select(genreRoot);
        return entityManager.createQuery(criteriaQuery)
                .setHint("org.hibernate.cacheable", Boolean.TRUE)
                .getResultList();
    }

    @Transactional
    @PostConstruct
    @Scheduled(initialDelayString = "${genre.cache.update.time.interval}", fixedRateString = "${genre.cache.update.time.interval}")
    public void updateCacheValues() {
        entityManager.clear();
        SessionFactory sessionFactory = localSessionFactoryBean.getObject();
        if (sessionFactory != null) {
            sessionFactory.getCache().evictAllRegions();
            findAll();
            log.info("Refreshed genres cache");
        } else {
            log.info("Session factory is null");
        }
    }
}
