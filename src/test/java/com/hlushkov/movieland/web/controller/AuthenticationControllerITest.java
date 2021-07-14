package com.hlushkov.movieland.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.dao.jpa.UserRepository;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.security.jwt.JwtConfig;
import com.hlushkov.movieland.security.jwt.JwtTokenVerifier;
import com.hlushkov.movieland.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.hlushkov.movieland.security.util.BlockedTokensStoringService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private LocalSessionFactoryBean localSessionFactoryBean;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private BlockedTokensStoringService blockedTokensStoringService;

    @BeforeEach
    void beforeEach() throws Exception {
        userRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession())
                .addFilters(new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationConfiguration.getAuthenticationManager(),
                        jwtConfig,
                        secretKey),
                        new JwtTokenVerifier(secretKey, jwtConfig, blockedTokensStoringService))
                .build();
    }

    @AfterAll
    void afterAll() {
        userRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @Test
    @DisplayName("Return status 200")
    void home() throws Exception {
        //when+then
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse();
    }

    @Test
    @DisplayName("Add user to db")
    void signUp() throws Exception {
        //prepare
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> userCredentials =
                Map.of("username", "test", "password", "test", "email", "test@gmail.com");
        String json = mapper.writeValueAsString(userCredentials);
        //when+then
        mockMvc.perform(post("/signUp")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResultForAuthFilter.class,
            cleanBefore = true, cleanAfter = true)
    @DisplayName("Add jwt access token to tokens block list")
    void logout() throws Exception {
        //prepare
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> userCredentials =
                Map.of("username", "test", "password", "test");
        String json = mapper.writeValueAsString(userCredentials);

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse();

        String jwtAccessToken = response.getHeader("Authorization");
        assertNotNull(jwtAccessToken);
        assertTrue(jwtAccessToken.contains("Bearer eyJhbGciOiJIUzUxMiJ9"));

        //when+then
        mockMvc.perform(get("/api/v1/logout")
                .header("Authorization", jwtAccessToken))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/movie")
                .header("Authorization", jwtAccessToken))
                .andDo(print())
                .andExpect(status().isForbidden()).andReturn();
    }
}