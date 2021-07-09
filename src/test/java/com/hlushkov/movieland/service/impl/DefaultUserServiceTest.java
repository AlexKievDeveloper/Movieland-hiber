package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.request.SignUpRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultUserServiceTest {
    @Autowired
    private DefaultUserService userService;

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResult.class, cleanBefore = true, cleanAfter = true)
    @DisplayName("Returns user from DB by username")
    void loadUserByUsername() {
        //when
        UserDetails userDetails = userService.loadUserByUsername("test");
        //then
        assertEquals("test", userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProviderResult.class, cleanBefore = true, cleanAfter = true)
    @DisplayName("Throws user not found exception user from DB by username")
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        //when + then
        NoResultException actualNoResultException =
                assertThrows(NoResultException.class, () -> userService.loadUserByUsername("test2"));
        assertEquals("No entity found for query", actualNoResultException.getMessage());
    }

    @Test
    @DataSet(provider = TestData.SingUpUserProvider.class, executeStatementsBefore = "SELECT setval('users_id_seq', 1)",
            cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SingUpUserProviderResultForService.class)
    @DisplayName("Add user to db")
    void signUp() {
        //prepare
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("test");
        signUpRequest.setEmail("test@gmail.com");
        //when
        userService.signUp(signUpRequest);
    }
}