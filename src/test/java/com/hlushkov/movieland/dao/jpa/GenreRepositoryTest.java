package com.hlushkov.movieland.dao.jpa;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DataSet(provider = TestData.GenreProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all genres from DB")
    void findAll() {
        //when
        List<Genre> actualGenreList = genreRepository.findAll();
        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());
    }
}