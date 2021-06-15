package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list of all movies in json format")
    void findAllMovies() throws Exception {
        //when

        MockHttpServletResponse response = mockMvc.perform(get("/movie"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse();
        //then
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertNotNull(response.getContentAsString());
        assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
        assertTrue(response.getContentAsString().contains("The Green Mile"));
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of three random movies in json format")
    void findThreeRandomMovies() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/random"))
                .andDo(print())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].nameRussian").isNotEmpty())
                .andExpect(jsonPath("$[0].nameNative").isNotEmpty())
                .andExpect(jsonPath("$[0].yearOfRelease").isNotEmpty())
                .andExpect(jsonPath("$[0].description").isNotEmpty())
                .andExpect(jsonPath("$[0].rating").isNotEmpty())
                .andExpect(jsonPath("$[0].price").isNotEmpty())
                .andExpect(jsonPath("$[0].picturePath").isNotEmpty())

                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].nameRussian").isNotEmpty())
                .andExpect(jsonPath("$[1].nameNative").isNotEmpty())
                .andExpect(jsonPath("$[1].yearOfRelease").isNotEmpty())
                .andExpect(jsonPath("$[1].description").isNotEmpty())
                .andExpect(jsonPath("$[1].rating").isNotEmpty())
                .andExpect(jsonPath("$[1].price").isNotEmpty())
                .andExpect(jsonPath("$[1].picturePath").isNotEmpty())

                .andExpect(jsonPath("$[2].id").isNotEmpty())
                .andExpect(jsonPath("$[2].nameRussian").isNotEmpty())
                .andExpect(jsonPath("$[2].nameNative").isNotEmpty())
                .andExpect(jsonPath("$[2].yearOfRelease").isNotEmpty())
                .andExpect(jsonPath("$[2].description").isNotEmpty())
                .andExpect(jsonPath("$[2].rating").isNotEmpty())
                .andExpect(jsonPath("$[2].price").isNotEmpty())
                .andExpect(jsonPath("$[2].picturePath").isNotEmpty())

                .andExpect(jsonPath("$[3].id").doesNotExist())
                .andExpect(jsonPath("$[3].nameRussian").doesNotExist())
                .andExpect(jsonPath("$[3].nameNative").doesNotExist())
                .andExpect(jsonPath("$[3].yearOfRelease").doesNotExist())
                .andExpect(jsonPath("$[3].description").doesNotExist())
                .andExpect(jsonPath("$[3].rating").doesNotExist())
                .andExpect(jsonPath("$[3].price").doesNotExist())
                .andExpect(jsonPath("$[3].picturePath").doesNotExist())

                .andExpect(status().isOk()).andReturn().getResponse();
        //then
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertNotNull(response.getContentAsString());
    }
}
