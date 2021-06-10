package com.hlushkov.movieland.dao.impl;


import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.util.EntityManagerProvider;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
//@DBRider
//@RunWith(JUnit4.class)
//@TestExecutionListeners
@ExtendWith(DBUnitExtension.class)
@RunWith(JUnitPlatform.class)
//@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateMovieDaoTest {
    @Autowired
    private HibernateMovieDao hibernateMovieDao;

    @Rule
    public EntityManagerProvider emProvider = EntityManagerProvider.instance("riderDB");

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection());

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void findAll() {
        //when
        List<Movie> actualMovies = hibernateMovieDao.findAll();
        //then
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
    }
}