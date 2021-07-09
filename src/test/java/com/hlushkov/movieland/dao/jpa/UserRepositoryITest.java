package com.hlushkov.movieland.dao.jpa;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.MovielandUser;
import com.hlushkov.movieland.entity.MovielandUserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaSystemException;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryITest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocalSessionFactoryBean localSessionFactoryBean;

    @BeforeEach
    void beforeEach() {
        userRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @AfterAll
    void afterAll() {
        userRepository.getEntityManager().clear();
        localSessionFactoryBean.getObject().getCache().evictAllRegions();
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResult.class, cleanBefore = true, cleanAfter = true)
    @DisplayName("Returns user by id")
    void findByName() {
        //when
        Optional<MovielandUser> actualOptionalMovielandUser = userRepository.findByUsername("test");

        if (actualOptionalMovielandUser.isPresent()) {
            MovielandUser movielandUser = actualOptionalMovielandUser.get();
            assertEquals("test", movielandUser.getUsername());
            assertEquals("test", movielandUser.getPassword());
            assertEquals("test@gmail.com", movielandUser.getEmail());
            assertEquals(MovielandUserRole.USER, movielandUser.getMovielandUserRole());
            assertTrue(movielandUser.isEnabled());
        }
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProvider.class, executeStatementsBefore = "SELECT setval('users_id_seq', 1)",
            cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SingUpUserProviderResult.class)
    @DisplayName("Add user to db")
    void signUp() {
        //prepare
        MovielandUser movielandUser = MovielandUser.builder()
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .movielandUserRole(MovielandUserRole.USER)
                .isEnabled(true)
                .build();
        //when
        userRepository.save(movielandUser);
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResult.class, cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SingUpUserProviderResult.class)
    @DisplayName("Add user to db")
    void signUpIfUserWithTheSameNameAlreadyExist() {
        //prepare
        MovielandUser movielandUser = MovielandUser.builder()
                .username("test")
                .password("test")
                .email("test@gmail.com")
                .movielandUserRole(MovielandUserRole.USER)
                .isEnabled(true)
                .build();
        //when
        PersistenceException actualException = assertThrows(PersistenceException.class, () -> {
            userRepository.save(movielandUser);
        });
        assertEquals("org.hibernate.exception.ConstraintViolationException: could not execute statement",
                actualException.getMessage());
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResult.class, cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SingUpUserProviderResult.class)
    @DisplayName("Add user to db")
    void signUpIfUserWithTheSameEmailAlreadyExist() {
        //prepare
        MovielandUser movielandUser = MovielandUser.builder()
                .username("test2")
                .password("test")
                .email("test@gmail.com")
                .movielandUserRole(MovielandUserRole.USER)
                .isEnabled(true)
                .build();
        //when
        PersistenceException actualException = assertThrows(PersistenceException.class, () -> {
            userRepository.save(movielandUser);
        });
        assertEquals("org.hibernate.exception.ConstraintViolationException: could not execute statement",
                actualException.getMessage());
    }

}