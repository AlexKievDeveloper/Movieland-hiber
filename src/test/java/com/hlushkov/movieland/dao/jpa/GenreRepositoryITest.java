package com.hlushkov.movieland.dao.jpa;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Genre;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.*;
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
class GenreRepositoryITest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private LocalSessionFactoryBean localSessionFactoryBean;

    @BeforeEach
    void beforeEach() {
        genreRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @AfterAll
    void afterAll() {
        genreRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @Test
    @DataSet(provider = TestData.GenreProvider.class, cleanAfter = true, transactional = true)
    @DisplayName("Returns list with all genres and cache it")
    void findAll() {
        //prepare
        SQLStatementCountValidator.reset();
        //when
        List<Genre> actualGenreList = genreRepository.findAll();
        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());
        assertSelectCount(1);
    }

    @Test
    @DataSet(provider = TestData.GenreProvider.class, cleanAfter = true, transactional = true)
    @DisplayName("Returns list with all genres and cache it")
    void findAllTestForCache() {
        //prepare
        SQLStatementCountValidator.reset();
        //when
        genreRepository.findAll();
        genreRepository.findAll();
        List<Genre> actualGenreList = genreRepository.findAll();
        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());
        assertSelectCount(1);
    }
}
