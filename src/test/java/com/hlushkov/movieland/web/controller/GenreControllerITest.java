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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreControllerITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestData.GenreProvider.class, cleanAfter = true, transactional = true)
    @DisplayName("Returns all genres")
    void findAll() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/genre"))
                .andDo(print())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").isNotEmpty())
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").isNotEmpty())
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].name").isNotEmpty())
                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(jsonPath("$[4].name").isNotEmpty())
                .andExpect(jsonPath("$[5].id").value(6))
                .andExpect(jsonPath("$[5].name").isNotEmpty())
                .andExpect(jsonPath("$[6].id").value(7))
                .andExpect(jsonPath("$[6].name").isNotEmpty())
                .andExpect(jsonPath("$[7].id").value(8))
                .andExpect(jsonPath("$[7].name").isNotEmpty())
                .andExpect(jsonPath("$[8].id").value(9))
                .andExpect(jsonPath("$[8].name").isNotEmpty())
                .andExpect(jsonPath("$[9].id").value(10))
                .andExpect(jsonPath("$[9].name").isNotEmpty())
                .andExpect(jsonPath("$[10].id").value(11))
                .andExpect(jsonPath("$[10].name").isNotEmpty())
                .andExpect(jsonPath("$[11].id").value(12))
                .andExpect(jsonPath("$[11].name").isNotEmpty())
                .andExpect(jsonPath("$[12].id").value(13))
                .andExpect(jsonPath("$[12].name").isNotEmpty())
                .andExpect(jsonPath("$[13].id").value(14))
                .andExpect(jsonPath("$[13].name").isNotEmpty())
                .andExpect(jsonPath("$[14].id").value(15))
                .andExpect(jsonPath("$[14].name").isNotEmpty())
                .andExpect(status().isOk()).andReturn().getResponse();
        //then
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getHeader("Content-Type"));
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertNotNull(response.getContentAsString());
    }
}
