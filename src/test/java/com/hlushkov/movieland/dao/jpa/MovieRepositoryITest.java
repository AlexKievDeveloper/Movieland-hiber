package com.hlushkov.movieland.dao.jpa;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.List;
import java.util.Optional;

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

    @AfterAll
    void afterAll() {
        movieRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void findAll() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovies = movieRepository.findAll(findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from cache")
    void findAllTestForCache() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        movieRepository.findAll(findMoviesRequest);
        movieRepository.findAll(findMoviesRequest);
        List<Movie> actualMovies = movieRepository.findAll(findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorting by rating DESC")
    void findMoviesWithRatingDirectionTest() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovies = movieRepository.findAll(findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertEquals(8.9, actualMovies.get(0).getRating());
        assertEquals(8.8, actualMovies.get(1).getRating());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorted by price DESC")
    void findMoviesWithPriceDESCDirectionTest() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovies = movieRepository.findAll(findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertEquals(134.67, actualMovies.get(0).getPrice());
        assertEquals(123.45, actualMovies.get(1).getPrice());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorted by price ASC")
    void findMoviesWithPriceASCDirectionTest() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovies = movieRepository.findAll(findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertEquals(123.45, actualMovies.get(0).getPrice());
        assertEquals(134.67, actualMovies.get(1).getPrice());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list with 3 random movies from DB")
    void findRandom() {
        //prepare
        SQLStatementCountValidator.reset();
        //when
        List<Movie> actualMovies = movieRepository.findRandom();
        //then
        assertNotNull(actualMovies);
        assertEquals(3, actualMovies.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre")
    void findByGenre() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovies = movieRepository.findByGenre(1, findMoviesRequest);
        //then
        assertNotNull(actualMovies);
        assertEquals(4, actualMovies.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void findMoviesByGenreSortedByRating() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = movieRepository.findByGenre(2, findMoviesRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void findMoviesByGenreSortedByPriceDesc() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = movieRepository.findByGenre(2, findMoviesRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price ASC from DB")
    void findMoviesByGenreSortedByPriceAsc() {
        //prepare
        SQLStatementCountValidator.reset();
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        findMoviesRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = movieRepository.findByGenre(2, findMoviesRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
        assertSelectCount(1);
    }
}
