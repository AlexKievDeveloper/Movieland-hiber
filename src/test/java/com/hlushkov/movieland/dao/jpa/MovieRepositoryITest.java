package com.hlushkov.movieland.dao.jpa;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieRepositoryITest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private LocalSessionFactoryBean localSessionFactoryBean;

    @BeforeEach
    void beforeEach() {
        movieRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void findAll() {
        //when
        List<Movie> actualMovies = movieRepository.findAll();
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from cache")
    void findAllTestForCache() {
        //prepare
        SQLStatementCountValidator.reset();
        //when
        movieRepository.findAll();
        movieRepository.findAll();
        List<Movie> actualMovies = movieRepository.findAll();
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list with 3 random movies from DB")
    void findRandom() {
        //when
        List<Movie> actualMovies = movieRepository.findRandom();

        //then
        assertNotNull(actualMovies);
        assertEquals(3, actualMovies.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list with 3 random movies from DB")
    void findRandomTestForCache() {
        //when
        List<Movie> actualMovies = movieRepository.findRandom();

        //then
        assertNotNull(actualMovies);
        assertEquals(3, actualMovies.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre")
    void findByGenre() {
        //when
        movieRepository.findByGenre(1);
        List<Movie> actualMovies = movieRepository.findByGenre(1);

        //then
        assertNotNull(actualMovies);
        assertEquals(4, actualMovies.size());
    }
}
