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
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("application/json", response.getContentType());
        assertNotNull(response.getContentAsString());
        assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
        assertTrue(response.getContentAsString().contains("The Green Mile"));
    }
}
