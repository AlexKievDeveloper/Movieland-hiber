package com.hlushkov.movieland.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtUsernameAndPasswordAuthenticationFilterITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @BeforeEach
    void beforeEach() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession())
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationConfiguration.getAuthenticationManager(),
                        jwtConfig,
                        secretKey))
                .build();
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResultForAuthFilter.class,
            cleanBefore = true, cleanAfter = true)
    @DisplayName("Authenticate user by username and password.Return status 200 and JWT authorization token")
    void login() throws Exception {
        //prepare
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> userCredentials =
                Map.of("username", "test", "password", "test");
        String json = mapper.writeValueAsString(userCredentials);
        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse();

        assertNotNull(response.getHeader("Authorization"));
        assertTrue(response.getHeader("Authorization").contains("Bearer eyJhbGciOiJIUzUxMiJ9"));
    }

}